require 'rubygems'
require 'hpricot'
require 'net/http'
require 'uri'
require 'builder'

# TODO:
# * Pull multiple pages
# * Clean up parse_year_parts

def pull_class(coid)
  res = Net::HTTP.get(URI.parse("http://catalog.rpi.edu/preview_course.php?catoid=5&coid=#{coid}&print"))
  doc = Hpricot(res)
  course = {}
  fulltitle = doc.search("td h1").inner_text
  titleparts = fulltitle.match('(.*) - (.*)')
  course[:title] = titleparts[2]
  course[:catalogNumber] = titleparts[1].sub(' ', '-')

  textnodes = doc.at('td.block_content_popup').children.select{|e| e.text?}
  course[:requisites] = textnodes.find{|n| n.inner_text =~ /.*requisite.*/}.inner_text.strip rescue ''
  # puts "requisites for #{course[:catalogNumber]}: #{course[:requisites]}"
  # puts  textnodes.find{|n| n.inner_text =~ /.*requisite.*/}

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
    when "Offered each term."  
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when "Spring term even-numbered years."
      retval = "even-numbered years ONLY"
      b.tag!('year-part', 'SPRING')
    when /Fall, spring,? (and )?summer (terms|session 2) annually./
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /Fall and spring terms?( annually)?./
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /Spring( term)?( [aA]nnually)?.?/
      b.tag!('year-part', 'SPRING')
    when /Fall( term)?( annually)?.?/
      b.tag!('year-part', 'FALL')
    when "Offered on sufficient demand."
      retval = "Offered on sufficient demand."
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when "Offered spring term on sufficient demand."
      retval = "Offered on sufficient demand."
      b.tag!('year-part', 'SPRING')
    when "Fall term on sufficient demand."
      retval = "Offered on sufficient demand."
      b.tag!('year-part', 'FALL')
    when "Offered on sufficient demand. Offered biannually."
      retval = "Offered on sufficient demand."
      retval = "Offered biannually"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /Offered on availability of (instructor|faculty).?/
      retval = "Offered on availability of #{$1}."
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when "Fall term odd-numbered years."
      retval = "odd-numbered years ONLY"
      b.tag!('year-part', 'FALL')
    when "Fall term even-numbered years."
      retval = "even-numbered years ONLY"
      b.tag!('year-part', 'FALL')
    when "Fall term alternate years."
      retval = "alternate years ONLY"
      b.tag!('year-part', 'FALL')
    when "Spring term alternate years."
      retval = "alternate years ONLY"
      b.tag!('year-part', 'FALL')
    when "Offered biannually."
      retval = "Offered biannually"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /(Offered )?Annually.?/
      retval = "Offered anually. Unclear which term"
      b.tag!('year-part', 'FALL')
      b.tag!('year-part', 'SPRING')
    when /Offered in conjunction with senior courses./
      retval = "Offered in conjunction with senior courses."
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
  
  case requisites
  when /Prerequisites?: *([A-Za-z]*) (\d*)( or equivalent)?( or permission of instructor)?.?/
    prerequisites << $1.upcase+'-'+$2
    retval = "prerequisite can be replaced with an equivalent" if $3
    retval ||= ""
    retval += "\nprerequisite can be skipped on permission of instructor" if $4
  when /Prerequisites: ([A-Z]*) (\d*) and ([A-Z]*) (\d*)./
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
  when /Prerequisites?: (probability theory and )?([A-Z]*) (\d*)./
    prerequisites << $2+'-'+$3
    retval = "probability theory required" if $1
  when /Prerequisites: ([A-Z]*) (\d*), ([A-Z]*) (\d*), (and )?([A-Z]*) (\d*)./
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    prerequisites << $6+'-'+$7
  when /Prerequisites: ([A-Z]*) (\d*), ([A-Z]*) (\d*), and senior standing./
    prerequisites << $1+'-'+$2
    prerequisites << $3+'-'+$4
    retval = "senior standing required"
  when /Prerequisites: ([A-Z0-9-]*) and either ([A-Z0-9-]*) or ([A-Z0-9-]*)./
    prerequisites << $1
    retval = "requires EITHER #{$2} or #{$3} as a prerequisite"
  when /Prerequisites: ([A-Z]*) (\d*) and basic probability such as in MATH 2800, ENGR-2600 or ECSE 4500./
    prerequisites << $1+'-'+$2
    retval  ="requires basic probability such as in MATH 2800, ENGR-2600 or ECSE 4500"
  when /Prerequisites: ([A-Z]*) (\d*). Co-requisite: ([A-Z]*) (\d*) or ([A-Z]*) (\d*)( recommended)?./
    prerequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    retval = "corequisite only recommended" if $7
  when /Prerequisites: ([A-Z]*) (\d*). Corequisites: ([A-Z]*) (\d*), ([A-Z]*) (\d*) and senior standing./
    prerequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    corequisites << $5+'-'+$6
    retval = "senior standing required"
  when /Corequisites?: ([A-Z]*) (\d*)( and senior standing)?./  
    corequisites << $1+'-'+$2
    retval = "senior standing required" if $3
  when /Prerequisites?: ([a-z].*)./
    retval = $1
  when /Prerequisite: ([A-Z0-9-]*) or ([A-Z0-9-]*)./
    prerequisites << $1
    retval = "Alternative prerequisite: $2"
  when /CHEM 2210 or a similar course in organic chemistry is a co- or prerequisite./
    prerequisites << "CHEM-2210"
    retval = "a similar course to CHEM-2210 in organic chemistry can be substituted for the CHEM-2210 prerequisite"
  when /A continuation of ([A-Z]*) (\d*), which is a prerequisite./
    retval = "Continuation of #{$1}-#{$2}"
    prerequisites << $1+'-'+$2
  when /Prerequisite\/Corequisite: Completion of Advanced Laboraty Requirement for Biology./
    retval = "Prerequisite/Corequisite: Completion of Advanced Laboratory Requirement for Biology"
  when /^There are no formal prerequisites.*/
    retval = requisites
  when /Prerequisite: This is a continuation of the fall course ([A-Z]*) (\d*)/
    prerequisites << $1+'-'+$2
  when /Experiments depend on the theoretical material in ([A-Z]*) (\d*) and ([A-Z]*) (\d*), which are corequisites./
    corequisites << $1+'-'+$2
    corequisites << $3+'-'+$4
    retval = requisites
  when ''
    nil
  else
    raise "No match found for '#{requisites}'"
  end

  b.prerequisites{
    prerequisites.each do |p|
      b.course{
        b.catalogNumber(p)
      }
    end
  }
  b.corequisites{
    corequisites.each do |p|
      b.course{
        b.catalogNumber(p)
      }
    end
  }
  
  if retval
    return "\n\nNOTE: "+retval
  else
    return ''
  end
end
# 
# puts pull_class(7985).inspect
# exit

#Successfully parsed departments: ECSE, CSCI, ENGR, BIOL, MANE
#in progress: CHEM, ECON
# TODO: IHSS, PHYS, STSS

builder = Builder::XmlMarkup.new(:indent => 2)
xml = builder.courses do |b|
  ['ECON'].each do |dept|
    pull_dept(dept).each do |coid|
      b.course do |b|
        course = pull_class(coid)
        b.title(course[:title])
        description = course[:description]

        begin
          description += parse_year_parts(course[:offered], b)
          description += parse_requisites(course[:requisites], b)
        rescue
          puts $!
          puts course[:catalogNumber]
          exit
        end

        b.description(description)
        b.department(course[:department])
        b.catalogNumber(course[:catalogNumber])
        b.credits(course[:credits] || 0)
      end
    end
  end
  
  Dir.glob("degrees/*.rb").each do |file|
    degree = File.new(file, 'r')
    builder.degree {
      builder.name(degree.readline.strip[2..1000])
      builder.validationCode(degree.read(nil))
    }
    degree.close
  end
end

puts xml
