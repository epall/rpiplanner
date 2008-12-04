require 'rubygems'
require 'hpricot'
require 'net/http'
require 'uri'
require 'builder'

CATALOG_NUMBER = /([A-Z]{4}) (\d*)/
HYPHENATED_CATALOG_NUMBER = /([A-Z0-9-]{9})/

# TODO:

def pull_class(coid)
  res = Net::HTTP.get(URI.parse("http://catalog.rpi.edu/preview_course.php?catoid=5&coid=#{coid}&print"))
  doc = Hpricot(res)
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

def pull_dept(department)
  req = Net::HTTP::Post.new('http://catalog.rpi.edu/content.php?catoid=5&navoid=111')
  req.set_form_data({'filter[27]' => department,
        'filter[29]' => '', 'cpage' => 1, 'cur_cat_oid' => 5, 'filter[32]' => 1,
        'search_database' => 'Filter', 'filter[keyword]' => ''})

  doc = Hpricot(Net::HTTP.new('catalog.rpi.edu').start {|http| http.request(req) }.body)

  courses = []

  doc.search("a:not(.footer) [@target=\"_blank\"]").each do |c|
    onclick = c.get_attribute('onclick')
    coid = onclick.match('showCourse\(\'5\', \'(.*)\',this.*')[1]
    name = c.inner_html
    courses << coid
  end
  
  if doc.search("a[@href=\"javascript:course_search(2);\"]")
    req = Net::HTTP::Post.new('http://catalog.rpi.edu/content.php?catoid=5&navoid=111')
    req.set_form_data({'filter[27]' => department,
          'filter[29]' => '', 'cpage' => 2, 'cur_cat_oid' => 5, 'filter[32]' => 1,
          'search_database' => 'Filter', 'filter[keyword]' => ''})

    doc = Hpricot(Net::HTTP.new('catalog.rpi.edu').start {|http| http.request(req) }.body)

    doc.search("a:not(.footer) [@target=\"_blank\"]").each do |c|
      onclick = c.get_attribute('onclick')
      coid = onclick.match('showCourse\(\'5\', \'(.*)\',this.*')[1]
      name = c.inner_html
      courses << coid
    end
  end

  return courses
end

def parse_year_parts(description, b)
  retval = nil

  b.availableTerms {
    case description
    when /^(Offered )?(each term|Fall and spring).$/i
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Fall, spring,? (and )?summer (terms|session 2) annually.$/
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Fall and spring terms?( annually)?.$/i
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Spring( term| semester)?( annually)?.?( .)?$/i
      b.tag!('year-part', 'SPRING')
    when /^(Offered )?Fall( term)?( annually)?.?$/i
      b.tag!('year-part', 'FALL')
    when /^Offered on (sufficient )?demand.$/
      retval = "Offered on sufficient demand."
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^(Offered )?spring term on sufficient demand.$/i
      retval = "Offered on sufficient demand."
      b.tag!('year-part', 'SPRING')
    when "Fall term on sufficient demand."
      retval = "Offered on sufficient demand."
      b.tag!('year-part', 'FALL')
    when /^(Offered on sufficient demand. )?(Offered biannually|Alternate years).$/
      retval = "Offered on sufficient demand." if $1
      retval = "Offered biannually"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^(Offered on|On) (the )?availability (of )?(the )?(instructor|faculty).?$/
      retval = "Offered on availability of #{$5}."
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^(Spring|Fall)(,| term) odd-numbered years.$/i
      retval = "odd-numbered years ONLY"
      b.tag!('year-part', $1.upcase)
    when /^(Spring|Fall) term even.?numbered years.$/i
      retval = "even-numbered years ONLY"
      b.tag!('year-part', $1.upcase)
    when "Fall term alternate years."
      retval = "alternate years ONLY"
      b.tag!('year-part', 'FALL')
    when /^(Graduate course; spring semester, alternate years|Spring term alternate years.)$/
      retval = "alternate years ONLY"
      b.tag!('year-part', 'FALL')
    when /^(Offered )?Annually.?$/
      retval = "Offered anually. Unclear which term"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Offered in conjunction with senior courses.$/
      retval = "Offered in conjunction with senior courses."
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^A fall-spring sequence annually.$/
      retval = "A fall-spring sequence annually"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Spring-fall sequence annually.$/
      retval = "A spring-fall sequence annually"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Consult department( about when offered)?.$/
      retval = "Consult department for availability"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Offered by individual arrangement.$/
      retval = "Offered by individual arrangement."
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /^Summer term annually.$/
      retval = "Only offered during the summer"
    when /^4 credit hours/  
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when ""
      retval = "Unclear when course is offered"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    else
      raise "No match found for year part "+description
    end
  }

  if retval
    return "\n\nNOTE: "+retval
  else
    return ''
  end
end

def parse_requisites(requisites, b)
  retval = nil
  prerequisites = []
  corequisites = []
  requiredP = true
  pickOneP = false
  requiredC = true
  pickOneC = false
  
  case requisites
  when /^Prerequisites: EEVP Professional Master’s students or permission of instructor.$/
    retval = "Must be EEVP Professional Master’s students or have permission of instructor"
  
  # primary catchall
  when /^Prerequisites?:.{1,3}([A-Z]{4}|Chem) (\d*)( or equivalent)?\
( or (permission|consent) of instructor)?( and programming experience)?\
( and knowledge of PASCAL, C, or LISP)?\
( or thorough knowledge of a scientific computer language, preferably C)?\
(, linear systems theory and transform theory)?(;.*)?\
(. Consult department about when offered)?\
( and some knowledge of matrices)?\
( and familiarity with elementary ordinary and partial differential equations)?.?$/
    prerequisites << $1.upcase+'-'+$2
    messages = []
    requiredP =  false if $3 || $4
    messages << "prerequisite can be replaced with an equivalent" if $3
    messages << "prerequisite can be skipped on permission of instructor" if $4
    messages << "programming experience recommended" if $6
    messages << "knowledge of PASCAL, C, or LISP required" if $7
    messages << "prerequisite can be skipped with thorough knowledge of a scientific computer language, preferably C" if $8
    messages << "linear systems theory and transform theory required" if $9
    messages << $10 if $10
    messages << "some knowledge of matrices required" if $12
    messages << "familiarity with elementary ordinary and partial differential equations required"
    retval = messages.join("\n")
  # sometimes they use dashes
  when /^Prerequisite: #{HYPHENATED_CATALOG_NUMBER}( or permission of instructor).?$/
    prerequisites << $1
    retval = "prerequisite can be skipped on permission of instructor" if $2
  when /^Prerequisite\/Corequisite: #{CATALOG_NUMBER}/
    corequisites << $1+'-'+$2
  when /^Prerequisite:.{1,3}#{CATALOG_NUMBER} recommended.$/
    requiredP = false
    retval = $1
  when /^Prerequisites?: (probability theory and )?#{CATALOG_NUMBER}.$/
    prerequisites << $2+'-'+$3
    retval = "probability theory required" if $1
  #two prerequisites
  when /^Prerequisites?:.{1,3}#{CATALOG_NUMBER}( and|,) #{CATALOG_NUMBER}\
( or equivalent)?( or graduate standing)?( or permission of (the )?instructor)?\
(. (ECSE 2800 or BMED 2800 or permission of instructor also required))?\
( or basic knowledge \(at the graduate level\) of semiconductor devices or permission of the instructor)?\
(\..{3}Spring term even-numbered years)?.{0,3}$/
    prerequisites << $1+'-'+$2
    prerequisites << $4+'-'+$5
    messages = []
    messages << "#{$4+'-'+$5} can be replaced with equivalent" if $6
    messages << "#{$4+'-'+$5} can be skipped if in graduate standing" if $8
    messages << "#{$4+'-'+$5} can be skipped on permission of instructor" if $8
    messages << $11 if $10
    messages << "basic knowledge (at the graduate level) of semiconductor devices or permission of the instructor required" if $12
    retval = messages.join("\n")
  # two either-or prerequisites
  when /^Prerequisite:.{1,3}#{CATALOG_NUMBER} or #{CATALOG_NUMBER}( or equivalent)?( or permission of instructor)?.?$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    requiredP = false
    pickOneP = true
    messages = []
    messages << "prerequisite can be replaced with equivalent" if $5
    messages << "prerequisite can be skipped on permission of instructor" if $6
    retval = messages.join("\n")
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, or permission of instructor; (.*).$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    requiredP = false
    pickOneP = true
    retval = $5
  # three prerequisites
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER}(; #{CATALOG_NUMBER} recommended)?.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $6+'-'+$7
    retval = "#{$6+'-'+$7} only recommended"
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER}, and #{CATALOG_NUMBER} or permission of instructor.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $5+'-'+$6
    messages << "#{$5+'-'+$6} can be skipped on permission of instructor" if $7
  # three pick-one prerequisites
  when /^Prerequisite: #{CATALOG_NUMBER} or #{CATALOG_NUMBER} or #{CATALOG_NUMBER}.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $5+'-'+$6
    pickOneP = true
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER} \(or equivalent\); #{CATALOG_NUMBER} desirable.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    retval = "#{$3+'-'+$4} can be replaced with equivalent. #{$5+'-'+$6} desirable"
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
    retval = "#{$5+'-'+$6} can be replaced with equivalent"
  when /^Prerequisites: #{CATALOG_NUMBER} \(or #{CATALOG_NUMBER}\) and #{CATALOG_NUMBER}.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    pickOneP = true
  when /^Prerequisites: #{CATALOG_NUMBER}, #{CATALOG_NUMBER} or equivalent, some familiarity with Java\/C\+\+.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    retval = "#{$3+'-'+$4} can be replaced with equivalent\nsome familiarity with Java required"
  # two prerequisites, one corequisite
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER}. Corequisites?: #{CATALOG_NUMBER}( and senior standing)?.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    retval = 'senior standing required' if $7
  # one prerequisite, one corequisite
  when /^Prerequisites?: (#{CATALOG_NUMBER}|C programming skills)( or equivalent,? or permission of instructor)?.{2,4}Corequisite: #{CATALOG_NUMBER}( and senior standing)?.$/
    messages = []
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
    retval = messages.join("\n")
  when /^Prerequisites: #{CATALOG_NUMBER} and #{CATALOG_NUMBER} or #{CATALOG_NUMBER}. Corequisite: #{CATALOG_NUMBER}./
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    corequisites << $7+'-'+$8
    retval = "ALternative to #{$3+'-'+$4} prerequisite: #{$5+'-'+$6}"
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
    retval = "prerequisite can be skipped on permission of instructor" if $5
    retval = "Students who have passed CSCI 1200 cannot register for this course" if $6
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
    retval = "senior standing required"
  when /^Prerequisites: #{HYPHENATED_CATALOG_NUMBER} and either #{HYPHENATED_CATALOG_NUMBER} or #{HYPHENATED_CATALOG_NUMBER}.$/
    prerequisites << $1
    retval = "requires EITHER #{$2} or #{$3} as a prerequisite"
  when /^Prerequisite:.{1,3}#{HYPHENATED_CATALOG_NUMBER}( or|,) #{HYPHENATED_CATALOG_NUMBER} or permission of instructor.?$/i
    prerequisites << $1 << $3
    pickOneP = true
    requiredP = false
    retval = "prerequisite can be skipped on permission of instructor"
  when /^Prerequisites: #{CATALOG_NUMBER} and basic probability such as in MATH 2800, ENGR-2600 or ECSE 4500.$/
    prerequisites << $1+'-'+$2
    retval  ="requires basic probability such as in MATH 2800, ENGR-2600 or ECSE 4500"
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
    retval = "senior standing required"
  when /^Corequisites?: #{CATALOG_NUMBER}( and senior standing)?./  
    corequisites << $1+'-'+$2
    retval = "senior standing required" if $3
  when /^Prerequisites?: ([a-z].*)$/
    retval = $1
  when /^CHEM 2210 or a similar course in organic chemistry is a co- or prerequisite.$/
    prerequisites << "CHEM-2210"
    retval = "a similar course to CHEM-2210 in organic chemistry can be substituted for the CHEM-2210 prerequisite"
  when /^A continuation of #{CATALOG_NUMBER}, which is a prerequisite.$/
    retval = "Continuation of #{$1}-#{$2}"
    prerequisites << $1+'-'+$2
  when /^Prerequisite\/Corequisite: Completion of Advanced Laboraty Requirement for Biology.$/
    retval = "Prerequisite/Corequisite: Completion of Advanced Laboratory Requirement for Biology"
  when /^There are no formal prerequisites.$/
    retval = requisites
  when /^Prerequisite: This is a continuation of the fall course #{CATALOG_NUMBER}$/
    prerequisites << $1+'-'+$2
  when /^Experiments depend on the theoretical material in #{CATALOG_NUMBER} and #{CATALOG_NUMBER}, which are corequisites.$/
    corequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    retval = requisites
  when /^Prerequisite:.{1,3}Mathematics major, Corequisite:.{1,3}#{HYPHENATED_CATALOG_NUMBER} or permission of instructor.$/
    corequisites << $1
    requiredC = false
    retval = "Must be mathematics major. Corequisite can be skipped on permission of instructor."
  when /^Prerequisite:.{1,3}#{CATALOG_NUMBER} or graduate standing or permission of the instructor.( MATH 4100 is desirable but not required.)?$/
    messages = []
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
    retval = "#{$3}-#{$4} can be replaced with equivalent"
  when "Prerequisite: ARCH 2200, Design Studio, or permission of the instructor."
    prerequisites << 'ARCH-2200'
    retval = "Prerequisite can be replaced with Design Studio or ignored with permission of instructor"
  when /^Prerequisite: Jr\/Sr. Standing$/
    retval = "Junior or senior standing required"
  when /^Prerequisite: A knowledge of organic chemistry is required.$/
    retval = requisites
  when /^Prerequisite:.{1,3}(permission of instructor|[Ff]irst-year math majors( only)?|one graduate course in mechanics of solids|a working knowledge of fluid mechanics and heat transfer).$/
    retval = "Prerequisite: "+$1
  when /^Prerequisites?:.{1,3}(major in Management, Architecture or H&SS|first-year math TA|graduate student in mathematics|Open to mathematics seniors only|Mathematical Methods in Physics or permission of the instructor).$/
    retval = "Prerequisite: "+$1
  when /^Prerequisites?:.{1,3}(PDI I or PDI II|any 2000-level STS course|junior or senior status|graduate status|graduate standing in STS|junior or senior standing|one philosophy or STS course)( or permission of instructor)?.$/
    retval = requisites
  when /^Prerequisites: #{CATALOG_NUMBER} or #{CATALOG_NUMBER} and major in Management or Economics, or permission of instructor.$/
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    pickOneP = true
    requiredP = false
    retval = "Must be majoring in Management or Economics\nprerequisite can be skipped on permission of instructor"
  when /^Prerequisite:.{1,3}(1000-level course \(or higher\) in STS|a 1000-level social science course|permission of a supervising faculty member).$/
    retval = requisites
  when /^Prerequisites:.{1,3}Vary with topic.$/
    nil
  when ''
    nil
  else
    raise "No match found for /^#{requisites}$/"
  end

  b.prerequisites('required' => requiredP, 'pickOne' => pickOneP){
    prerequisites.each do |p|
      b.course{
        b.catalogNumber(p)
      }
    end
  } unless prerequisites.empty?
  
  b.corequisites('required' => requiredC, 'pickOne' => pickOneC){
    corequisites.each do |p|
      b.course{
        b.catalogNumber(p)
      }
    end
  } unless corequisites.empty?
  
  if retval && retval != ''
    return "\n\nNOTE: "+retval
  else
    return ''
  end
end

# puts pull_class(8103).inspect
# exit

# builderX = Builder::XmlMarkup.new(:indent => 2)
# xmlX = builderX.foo{
#   parse_requisites('Prerequisites: ENGR 2530 and CIVL 2630 or equivalent.', builderX)
# }
# puts xmlX
# exit

#actually successful: 'CSCI', 'ECSE', 'ENGR', 'PHYS', 'MATH', 'IHSS'
#in progress: 'STSS', 'STSH'

successfully_parsed_departments = ['CSCI', 'ECSE', 'ENGR', 'BIOL', 'MANE',
  'MATH', 'CHEM', 'ECON','PHYS', 'IHSS', 'STSS', 'STSH', 'PSYC','BMED',
  'EPOW']

builder = Builder::XmlMarkup.new(:indent => 2)
xml = builder.courses do |b|
  # successfully_parsed_departments.each do |dept|
  ['CSCI', 'ECSE', 'ENGR', 'PHYS', 'MATH', 'IHSS'].each do |dept|
    pull_dept(dept).each do |coid|
      course = pull_class(coid)
      description = course[:description]
      raise "Invalid catalog number: #{course[:catalogNumber]}" unless course[:catalogNumber] =~ /[A-Z]{4}-[\d]{4}$/

      begin
        b.course do |b|
          description += parse_year_parts(course[:offered], b)
          description += parse_requisites(course[:requisites], b)
          b.title(course[:title])
          b.description(description)
          b.department(course[:department])
          b.catalogNumber(course[:catalogNumber])
          b.credits(course[:credits] || 0)
        end
      rescue => err
        $stderr.puts err
        # $stderr.print err.backtrace.join("\n")
        $stderr.puts course[:catalogNumber]
      end
    end
  end
  
  Dir.glob("degrees/*.rb").each do |file|
    degree = File.new(file, 'r')
    header = degree.readline.strip
    builder.degree {
      builder.id(header[1..4].to_i)
      builder.name(header[6..1000])
      builder.validationCode(degree.read(nil))
    }
    degree.close
  end
end

puts xml
