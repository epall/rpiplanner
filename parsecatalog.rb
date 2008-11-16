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

  return courses
end

def parse_year_parts(description, b)
  retval = nil

  b.availableTerms {
    case description
    when "Spring term even-numbered years."
      retval = "even-numbered years ONLY"
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when "Fall, spring, and summer terms annually."
      b.tag!('rpiplanner.model.YearPart', 'FALL')
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when /Fall and spring terms( annually)?./
      b.tag!('rpiplanner.model.YearPart', 'FALL')
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when /Spring( term)?( [aA]nnually)?.?/
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when /Fall( term)? annually.?/
      b.tag!('rpiplanner.model.YearPart', 'FALL')
    when "Offered on sufficient demand."
      retval = "Offered on sufficient demand."
      b.tag!('rpiplanner.model.YearPart', 'FALL')
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when "Offered spring term on sufficient demand."
      retval = "Offered on sufficient demand."
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when "Fall term on sufficient demand."
      retval = "Offered on sufficient demand."
      b.tag!('rpiplanner.model.YearPart', 'FALL')
    when "Offered on availability of instructor."
      retval = "Offered on availability of instructor."
      b.tag!('rpiplanner.model.YearPart', 'FALL')
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when "Fall term odd-numbered years."
      retval = "odd-numbered years ONLY"
      b.tag!('rpiplanner.model.YearPart', 'FALL')
    when "Fall term even-numbered years."
      retval = "even-numbered years ONLY"
      b.tag!('rpiplanner.model.YearPart', 'FALL')
    when "Fall term alternate years."
      retval = "alternate years ONLY"
      b.tag!('rpiplanner.model.YearPart', 'FALL')
    when "Spring term alternate years."
      retval = "alternate years ONLY"
      b.tag!('rpiplanner.model.YearPart', 'FALL')
    when "Offered biannually."
      retval = "Offered biannually"
      b.tag!('rpiplanner.model.YearPart', 'FALL')
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
    when ""
      retval = "Unclear when course is offered"
      b.tag!('rpiplanner.model.YearPart', 'FALL')
      b.tag!('rpiplanner.model.YearPart', 'SPRING')
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
  when /Prerequisite: ([A-Z]*) (\d*) or permission of instructor./
    prerequisites << $1+'-'+$2
    retval = "prerequisite can be skipped on permission of instructor"
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

builder = Builder::XmlMarkup.new(:indent => 2)
xml = builder.courses do |b|
  ['ECSE', 'CSCI'].each do |dept|
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
        b.credits(course[:credits])
      end
    end
  end
end

puts xml