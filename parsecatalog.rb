require 'rubygems'
require 'hpricot'
require 'net/http'
require 'uri'
require 'builder'

CATALOG_NUMBER = /([A-Z]{4}) (\d*)/
HYPHENATED_CATALOG_NUMBER = /([A-Z0-9-]{9})/

require 'ripcatalog' unless File.exist?('catalog')

# load patches in preparation
file = File.new( "patches.xml" )
doc = Hpricot(file.read)

@database_patches = {}
(doc/"course").each do |course|
  @database_patches[course.at("catalognumber").inner_text] = course
end

def patch_course(course)
  patch = @database_patches[course[:catalogNumber]]
  return if patch.nil?
  
  if patch/"countsas"
    course[:countsAs] = (patch % "countsas").inner_text
  end
  
  @database_patches.delete(course[:catalogNumber])
end

def finish_patches(builder)
  @database_patches.each do |catalogNumber, patch|
    builder.course {
      ['title', 'description', 'department', 'catalogNumber', 
        'credits', 'isOfficial', 'doubleCount', 'countsAs'].each do |field|
        builder.tag!(field, (patch % field.downcase).inner_text) if patch % field.downcase
      end
      
      builder.availableTerms {
        builder.tag!('year-part', 'FALL')
        builder.tag!('year-part', 'SPRING')
      }
    }
  end
end

# catalog parsing stuff

def parse_course(rawdoc)
  doc = Hpricot(rawdoc)
  course = {}
  fulltitle = doc.search("td h1").inner_text
  titleparts = fulltitle.match('(.*) - (.*)')
  course[:title] = titleparts[2]
  course[:catalogNumber] = titleparts[1].sub(' ', '-')

  textnodes = doc.at('td.block_content_popup').children.select{|e| e.text?}
  course[:requisites] = textnodes.find{|n| n.inner_text =~ /(Co|Pre)requisite.*/}.inner_text.strip rescue ''

  course[:description] = textnodes[2].inner_text.strip

  doc.search('td.block_content_popup em').each do |emEl|
    case emEl.inner_text
    when "Credit Hours:"
      course[:credits] = emEl.next_node.next_node.inner_text.to_i
    end
  end

  doc.search('td.block_content_popup strong').each do |strongEl|
    case strongEl.inner_text
    when "When Offered:"
      course[:offered] = strongEl.next_node.inner_text.strip
    when "Cross Listed:"
      course[:description] += "\n\n"+strongEl.next_node.inner_text.strip
    end
  end

  course[:offered] ||= ''

  course[:department] = doc.search('td span.n1_header')[0].inner_text

  return course
end

def parse_year_parts(description)
  parts = []
  messages = []

  case description
  when /^(Offered )?(each term|Fall and spring( term)?)( annually)?.$/i
    parts << 'FALL'
    parts << 'SPRING'
  when /^Fall, spring,? (and )?summer (terms|session 2) annually.$/
    parts << 'FALL'
    parts << 'SPRING'
  when /^Fall and spring terms?( annually)?.$/i
    parts << 'FALL'
    parts << 'SPRING'
  when /^Spring( term| semester)?( annually)?( only)?.?( .)?$/i
    parts << 'SPRING'
  when /^(Offered )?Fall( term| semester)?( annually| yearly)?.?( Includes laboratory experience.)?$/i
    parts << 'FALL'
  when /^Offered on (sufficient )?demand.$/
    messages << "Offered on sufficient demand."
    parts << 'FALL'
    parts << 'SPRING'
  when /^(Offered )?spring term on sufficient demand.$/i
    messages << "Offered on sufficient demand."
    parts << 'SPRING'
  when "Fall term on sufficient demand."
    messages << "Offered on sufficient demand."
    parts << 'FALL'
  when /^(Offered on sufficient demand. )?(Offered biannually|Alternate years).$/
    messages << "Offered on sufficient demand." if $1
    messages << "Offered biannually"
    parts << 'FALL'
    parts << 'SPRING'
  when /^(Offered (on|upon)) (the )?availability (of )?(the )?(instructor|faculty).?$/i
    messages << "Offered on availability of #{$5}."
    parts << 'FALL'
    parts << 'SPRING'
  when /^(Spring|Fall)(,| term) odd(-| )?numbered years.$/i
    messages << "odd-numbered years ONLY"
    parts << $1.upcase
  when /^(Spring|Fall)(,| term),? even(-| )number(ed|s) years.$/i
    messages << "even-numbered years ONLY"
    parts << $1.upcase
  when /^(Spring|Fall)(,| term)? (\(of )?even[.-]?numbered years(\))?.$/i
    messages << "even-numbered years ONLY"
    parts << $1.upcase
  when /^(Spring|Fall) term,? alternat(e|ive) years./
    messages << "alternate years ONLY"
    parts << $1.upcase
  when /^Fall, every other year.$/i
    messages << "alternate years ONLY"
    parts << 'FALL'
  when /^(Graduate course; spring semester, alternate years|Spring term alternate years.)$/
    messages << "alternate years ONLY"
    parts << 'SPRING'
  when /^Offered alternate years.$/
    messages << "alternate years ONLY"
    parts << 'FALL'
    parts << 'SPRING'
  when /^(Offered )? ?(Fall and spring)? ?Annually.?$/i
    messages << "Offered anually. Unclear which term"
    parts << 'FALL'
    parts << 'SPRING'
  when /^Offered in conjunction with senior courses.$/
    messages << "Offered in conjunction with senior courses."
    parts << 'FALL'
    parts << 'SPRING'
  when /^A fall-spring sequence annually.$/
    messages << "A fall-spring sequence annually"
    parts << 'FALL'
    parts << 'SPRING'
  when /^Spring-fall sequence annually.$/
    messages << "A spring-fall sequence annually"
    parts << 'FALL'
    parts << 'SPRING'
  when /^Consult department( about when offered)?.$/
    messages << "Consult department for availability"
    parts << 'FALL'
    parts << 'SPRING'
  when /^Offered by individual arrangement.$/
    messages << "Offered by individual arrangement."
    parts << 'FALL'
    parts << 'SPRING'
  when /^Summer term annually.$/
    messages << "Only offered during the summer"
  when /^Spring or summer term( annually)?.$/i
    parts << 'SPRING'
  when /^4 credit hours/  
    parts << 'FALL'
    parts << 'SPRING'
  when ""
    messages << "Unclear when course is offered"
    parts << 'FALL'
    parts << 'SPRING'
  else
    raise "No match found for year part: #{description}"
  end
  
  return {:parts => parts, :messages => messages}
end

def parse_requisites(requisites)
  prerequisites = []
  corequisites = []
  requiredP = true
  pickOneP = false
  requiredC = true
  pickOneC = false
  messages = []
  
  case requisites
  when /^Prerequisites: EEVP Professional Master’s students or permission of instructor.$/
    messages << "Must be EEVP Professional Master’s students or have permission of instructor"
  
  # primary catchall
  when /^Prerequisites?:.{1,3}([A-Z]{4}|Chem) (\d*)( or equivalent)?\
( (and|or) (permission|consent) of instructor)?( and programming experience)?\
( and knowledge of PASCAL, C, or LISP)?\
( or thorough knowledge of a scientific computer language, preferably C)?\
(, linear systems theory and transform theory)?(;.*)?\
(. Consult department about when offered)?\
( and some knowledge of matrices)?\
( and familiarity with elementary ordinary and partial differential equations)?\
(. Restricted to junior and senior engineering majors only)?.?$/
    prerequisites << $1.upcase+'-'+$2
    requiredP =  false if $3 || $4 || $8
    messages << "prerequisite can be replaced with an equivalent" if $3
    messages << "prerequisite can be skipped on permission of instructor" if $4
    messages << "programming experience recommended" if $6
    messages << "knowledge of PASCAL, C, or LISP required" if $7
    messages << "prerequisite can be skipped with thorough knowledge of a scientific computer language, preferably C" if $8
    messages << "linear systems theory and transform theory required" if $9
    messages << $10 if $10
    messages << "some knowledge of matrices required" if $12
    messages << "familiarity with elementary ordinary and partial differential equations required" if $13
    messages << "restricted to junior and senior engineering majors only" if $14
  # sometimes they use dashes
  when /^Prerequisite: #{HYPHENATED_CATALOG_NUMBER}( or permission of instructor).?$/
    prerequisites << $1
    messages << "prerequisite can be skipped on permission of instructor" if $2
  when /^Prerequisite\/Corequisite: #{CATALOG_NUMBER}/
    corequisites << $1+'-'+$2
  when /^Prerequisite:.{1,3}#{CATALOG_NUMBER} recommended.$/
    requiredP = false
    messages << $1
  when /^Prerequisites?: (probability theory and )?#{CATALOG_NUMBER}.$/
    prerequisites << $2+'-'+$3
    messages << "probability theory required" if $1
  when /^Prerequisites?:.{1,2}satisfactory completion of #{CATALOG_NUMBER}.$/
    prerequisites << $1+'-'+$2
  #two prerequisites
  when /^Prerequisites?:.{1,3}#{CATALOG_NUMBER}( and|,) #{CATALOG_NUMBER}\
( or equivalent)?( or graduate standing)?( or permission of (the )?instructor)?\
(. (ECSE 2800 or BMED 2800 or permission of instructor also required))?\
( or basic knowledge \(at the graduate level\) of semiconductor devices or permission of the instructor)?\
(\..{3}Spring term even-numbered years)?.{0,3}$/
    prerequisites << $1+'-'+$2
    prerequisites << $4+'-'+$5
    messages << "#{$4+'-'+$5} can be replaced with equivalent" if $6
    messages << "#{$4+'-'+$5} can be skipped if in graduate standing" if $8
    messages << "#{$4+'-'+$5} can be skipped on permission of instructor" if $8
    messages << $11 if $10
    messages << "basic knowledge (at the graduate level) of semiconductor devices or permission of the instructor required" if $12
  when /Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER} (or concurrent).$/
    prerequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
  # two either-or prerequisites
  when /^Prerequisite:.{1,3}#{CATALOG_NUMBER} or #{CATALOG_NUMBER}( or equivalent)?( (and|or) permission of instructor)?.?$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    requiredP = false
    pickOneP = true
    requiredP = true if $7 && $7 == 'and'
    messages << "prerequisite can be replaced with equivalent" if $5
    messages << "prerequisite can be skipped on permission of instructor" if $6 && $7 == 'or'
    messages << "must get permission of instructor to take coures" if $7 == 'and'
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, or permission of instructor; (.*).$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    requiredP = false
    pickOneP = true
    messages << $5
  # three prerequisites
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER}(; #{CATALOG_NUMBER} recommended)?.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $6+'-'+$7
    messages << "#{$6+'-'+$7} only recommended"
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, and #{CATALOG_NUMBER} or permission of instructor.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $5+'-'+$6
    messages << "#{$5+'-'+$6} can be skipped on permission of instructor" if $7
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, #{CATALOG_NUMBER} or equivalent.$/
    #FIXME not quite right, but the best we can do with our current format
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $5+'-'+$6
  # three pick-one prerequisites
  # A or B or C
  when /^Prerequisite: #{CATALOG_NUMBER} or #{CATALOG_NUMBER} or #{CATALOG_NUMBER}(, or permission of instructor)?.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $5+'-'+$6
    pickOneP = true
    requiredP = false if $7
  when /^Prerequisites:.{1,2}CHEM 1100 and ENGR 1600 or ENGR 2010.$/
    prerequisites << 'CHEM-1100'
    prerequisites << 'ENGR-1600'
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER} \(or equivalent\); #{CATALOG_NUMBER} desirable.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    messages << "#{$3+'-'+$4} can be replaced with equivalent. #{$5+'-'+$6} desirable"
  when /^Prerequisite: #{CATALOG_NUMBER} or #{CATALOG_NUMBER} \(#{CATALOG_NUMBER}\) or equivalent.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $5+'-'+$6
    pickOneP = true
    requiredP = false
  when /^Prerequisites: ([A-Z]{4}) (\d{4})\/(\d{4}) and ([A-Z]{4}) (\d{4})\/(\d{4}).$/
    prerequisites << $1+'-'+$2
    prerequisites << $1+'-'+$3
    prerequisites << $4+'-'+$5
    prerequisites << $4+'-'+$6
  when /^Prerequisites: #{CATALOG_NUMBER} or #{CATALOG_NUMBER}, and #{CATALOG_NUMBER} or equivalent.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    pickOneP = true
    requiredC = false
    messages << "#{$5+'-'+$6} can be replaced with equivalent"
  when /^Prerequisites: #{CATALOG_NUMBER} \(or #{CATALOG_NUMBER}\) and #{CATALOG_NUMBER}.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    pickOneP = true
  # A or B, C, D
  # This isn't possible to do correctly with current scheme
  when /^Prerequisites: #{CATALOG_NUMBER} or #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, #{CATALOG_NUMBER}. ?(Not recommended for Freshmen and Sophomores.)?$/
    prerequisites << $1+'-'+$2
    prerequisites << $5+'-'+$6
    prerequisites << $7+'-'+$8
    messages << $9
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER} or equivalent, some familiarity with Java\/C\+\+.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    messages << "#{$3+'-'+$4} can be replaced with equivalent\nsome familiarity with Java required"
  # two prerequisites, one corequisite
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER}. Corequisites?: #{CATALOG_NUMBER}( and senior standing)?.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    messages << 'senior standing required' if $7
  # one prerequisite, one corequisite
  when /^Prerequisites?: (#{CATALOG_NUMBER}|C programming skills)( or equivalent,? or permission of instructor)?.{2,4}Corequisite: #{CATALOG_NUMBER}( and senior standing)?.$/
    if $1 == 'C programming skills'
      messages << 'C programming skills required'
    else
      prerequisites << $2+'-'+$3
    end
    if $4
      messages << "#{$2+'-'+$3} can be replaced with equivalent or skipped on permission of instructor"
      requiredP = false
    end
    corequisites << $5+'-'+$6
    messages << 'senior standing required' if $7
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER} or #{CATALOG_NUMBER}. Corequisite: #{CATALOG_NUMBER}./
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    corequisites << $7+'-'+$8
    messages << "ALternative to #{$3+'-'+$4} prerequisite: #{$5+'-'+$6}"
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, (and )?#{CATALOG_NUMBER}.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $6+'-'+$7
  when /^Prerequisites?: #{HYPHENATED_CATALOG_NUMBER} or #{HYPHENATED_CATALOG_NUMBER}.$/
    prerequisites << $1 << $2
    pickOneP = true
  when /^Prerequisites?: #{CATALOG_NUMBER} or #{CATALOG_NUMBER}( or permission of instructor)?(. Students who have passed CSCI 1200 cannot register for this course)?.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    pickOneP = true
    requiredP = false
    messages << "prerequisite can be skipped on permission of instructor" if $5
    messages << "Students who have passed CSCI 1200 cannot register for this course" if $6
  when /^Prerequisite: #{CATALOG_NUMBER}. Corequisite: #{CATALOG_NUMBER} or #{CATALOG_NUMBER}./
    prerequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    pickOneC = true
  when /^Prerequisites: #{HYPHENATED_CATALOG_NUMBER},  #{HYPHENATED_CATALOG_NUMBER}, and  #{HYPHENATED_CATALOG_NUMBER}.$/
    prerequisites << $1
    prerequisites << $2
    prerequisites << $3
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, and senior standing.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    messages << "senior standing required"
  when /^Prerequisites: #{HYPHENATED_CATALOG_NUMBER} and either #{HYPHENATED_CATALOG_NUMBER} or #{HYPHENATED_CATALOG_NUMBER}.$/
    prerequisites << $1
    messages << "requires EITHER #{$2} or #{$3} as a prerequisite"
  when /^Prerequisite:.{1,3}#{HYPHENATED_CATALOG_NUMBER}( or|,) #{HYPHENATED_CATALOG_NUMBER} or permission of instructor.?$/i
    prerequisites << $1 << $3
    pickOneP = true
    requiredP = false
    messages << "prerequisite can be skipped on permission of instructor"
  when /^Prerequisites: #{CATALOG_NUMBER} and basic probability such as in MATH 2800, ENGR-2600 or ECSE 4500.$/
    prerequisites << $1+'-'+$2
    messages << "requires basic probability such as in MATH 2800, ENGR-2600 or ECSE 4500"
  when /^Prerequisites: #{CATALOG_NUMBER}. Co-requisite: #{CATALOG_NUMBER} or #{CATALOG_NUMBER}( recommended)?.$/
    prerequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    pickOneC = true
    requiredC = !$7
  when /^Prerequisites: #{CATALOG_NUMBER}. Corequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER} and senior standing.$/
    prerequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    messages << "senior standing required"
  when /^Corequisites?: #{CATALOG_NUMBER}( and senior standing)?./  
    corequisites << $1+'-'+$2
    messages << "senior standing required" if $3
  when /^Prerequisites?: ([a-z].*)$/
    messages << $1
  when /^CHEM 2210 or a similar course in organic chemistry is a co- or prerequisite.$/
    prerequisites << "CHEM-2210"
    messages << "a similar course to CHEM-2210 in organic chemistry can be substituted for the CHEM-2210 prerequisite"
  when /^A continuation of #{CATALOG_NUMBER}, which is a prerequisite.$/
    messages << "Continuation of #{$1}-#{$2}"
    prerequisites << $1+'-'+$2
  when /^Prerequisite\/Corequisite: Completion of Advanced Laboraty Requirement for Biology.$/
    messages << "Prerequisite/Corequisite: Completion of Advanced Laboratory Requirement for Biology"
  when /^There are no formal prerequisites.$/
    messages << requisites
  when /^Prerequisite: This is a continuation of the fall course #{CATALOG_NUMBER}$/
    prerequisites << $1+'-'+$2
  when /^Experiments depend on the theoretical material in #{CATALOG_NUMBER} and #{CATALOG_NUMBER}, which are corequisites.$/
    corequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    messages << requisites
  when /^Prerequisite:.{1,3}Mathematics major, Corequisite:.{1,3}#{HYPHENATED_CATALOG_NUMBER} or permission of instructor.$/
    corequisites << $1
    requiredC = false
    messages << "Must be mathematics major. Corequisite can be skipped on permission of instructor."
  when /^Prerequisite:.{1,3}#{CATALOG_NUMBER} or graduate standing or permission of the instructor.( MATH 4100 is desirable but not required.)?$/
    prerequisites << $1+'-'+$2
    requiredP = false
    messages << "Prerequisite can be skipped with graduate standing or permission of the instructor."
    messages << "MATH 4100 is desirable but not required" if $3
  when /^Prerequisites or corequisites: #{CATALOG_NUMBER} or #{CATALOG_NUMBER} or equivalent.$/
    corequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    pickOneC = true
  when /^Prerequisite:.{1,3}PHYS 2510 or permission of instructor. Consult department about when offered.$/
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER} or equivalent.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    messages << "#{$3}-#{$4} can be replaced with equivalent"
  when "Prerequisite: ARCH 2200, Design Studio, or permission of the instructor."
    prerequisites << 'ARCH-2200'
    messages << "Prerequisite can be replaced with Design Studio or ignored with permission of instructor"
  when /^Prerequisite: Jr\/Sr. Standing$/
    messages << "Junior or senior standing required"
  when /^Prerequisite: A knowledge of organic chemistry is required.$/
    messages << requisites
  when /^Prerequisite:.{1,3}(permission of instructor|[Ff]irst-year math majors( only)?|one graduate course in mechanics of solids|a working knowledge of fluid mechanics and heat transfer).$/
    messages << "Prerequisite: "+$1
  when /^Prerequisites?:.{1,3}(major in Management, Architecture or H&SS|first-year math TA|graduate student in mathematics|Open to mathematics seniors only|Mathematical Methods in Physics or permission of the instructor).$/
    messages << "Prerequisite: "+$1
  when /^Prerequisites?:.{1,3}(PDI I or PDI II|any 2000-level STS course|junior or senior status|graduate status|graduate standing in STS|junior or senior standing|one philosophy or STS course)( or permission of instructor)?.$/
    messages << requisites
  when /^Prerequisites: #{CATALOG_NUMBER} or #{CATALOG_NUMBER} and major in Management or Economics, or permission of instructor.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    pickOneP = true
    requiredP = false
    messages << "Must be majoring in Management or Economics\nprerequisite can be skipped on permission of instructor"
  when /^Prerequisite:.{1,3}(1000-level course \(or higher\) in STS|a 1000-level social science course|permission of a supervising faculty member).$/
    messages << requisites
  when /^Prerequisites: BIOL 1010, college-level math, or permission of the instructor./
    prerequisites << 'BIOL-1010'
    messages << "college-level math, or permission of the instructor required"
  when /^Prerequisite:.{1,3}ENGR 1010. The course is limited to junior and senior engineering majors. A similar course is offered in Cognitive Science, and students cannot take both courses for credit.$/
    prerequisites << 'ENGR-1010'
    messages << "limited to junior and senior engineering majors. A similar course is offered in Cognitive Science, and students cannot take both courses for credit."
  when /^Prerequisite:.{1,3}STSH 1110\/STSS 1110 or permission of instructor.$/
    prerequisites << 'STSH-1110'
    prerequisites << 'STSS-1110'
    pickOneP = true
    requiredP = false
    messages << "Prerequisite can be skipped on permission of instructor"
  when /^Prerequisites:.{1,3}Vary with topic.$/
    nil
  when ''
    nil
  else
    raise "No match found for #{requisites}"
  end

  return {:requiredP => requiredP, :pickOneP => pickOneP,
    :requiredC => requiredC, :pickOneC => pickOneC, :messages => messages,
    :prerequisites => prerequisites, :corequisites => corequisites}
end

builder = Builder::XmlMarkup.new(:indent => 2)
xml = builder.courses do |b|
  ['ARTS','ASTR','BIOL','BMED','CSCI','CHEM','CHME','CIVL','COMM','ECON','ECSE',
    'ENGR','EPOW','IHSS','LITR','MANE','MATH','MTLE','PHIL','PHYS','PSYC','STSH',
    'STSS'].each do |dept|
    files = Dir["catalog/#{dept}/*.html"]
    files.each do |filename|
      file = File.open(filename, 'r')
      course = parse_course(file.read(nil))
      file.close
      description = course[:description]
      raise "Invalid catalog number: #{course[:catalogNumber]}" unless course[:catalogNumber] =~ /[A-Z]{4}-[\d]{4}$/
      begin
        year_parts = parse_year_parts(course[:offered])
        requisites = parse_requisites(course[:requisites])
        patch_course(course)
        
        b.course do |b|
          description += "\n\n" unless year_parts[:messages].empty? && requisites[:messages].empty?
          year_parts[:messages].each do |msg|
            description += "NOTE: #{msg}\n"
          end
          requisites[:messages].each do |msg|
            description += "NOTE: #{msg}\n"
          end
          b.title(course[:title])
          b.description(description)
          b.department(course[:department])
          b.catalogNumber(course[:catalogNumber])
          b.countsAs(course[:countsAs]) if course[:countsAs]
          b.credits(course[:credits] || 0)
          
          b.prerequisites('required' => requisites[:requiredP], 'pickOne' => requisites[:pickOneP]){
            requisites[:prerequisites].each { |p|
              b.course{
                b.catalogNumber(p)
              }
            }
          } unless requisites[:prerequisites].empty?

          b.corequisites('required' => requisites[:requiredC], 'pickOne' => requisites[:pickOneC]){
            requisites[:corequisites].each {|p|
              b.course{
                b.catalogNumber(p)
              }
            }
          } unless requisites[:corequisites].empty?
          
          unless year_parts[:parts].empty?
            b.availableTerms {
              year_parts[:parts].each {|part| b.tag!('year-part', part)}
            }
          end
          
          b.isOfficial('true')
          
          # 4900 courses are seminars, independent study, etc and should be counted
          # each time they are taken
          b.doubleCount(!!(course[:catalogNumber] =~ /....-[46]9[46]0/))
        end
      rescue => err
        $stderr.puts "#{course[:catalogNumber]} - #{course[:title]}"
        $stderr.puts err
      end
    end
  end
  
  finish_patches(b)
  
  Dir.glob("degrees/*.rb").each do |file|
    contents = File.new(file, 'r')
    validationCode = contents.readline.strip
    validationCode =~ /degree "(.*)", ([0123456789]*) do |d|/
    id = $2.to_s
    name = $1.to_s
    school = contents.readline.strip
    school = school.match(/d.school "(.*)"/)[1]
    validationCode << contents.read(nil)
    contents.close
    builder.degree {
      builder.id(id)
      builder.name(name)
      builder.school(school)
      builder.validationCode(validationCode)
    }
  end
end

puts xml
