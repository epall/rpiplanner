require 'rubygems'
require 'hpricot'
require 'net/http'
require 'uri'
require 'fileutils'

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

all_departments = ["ARCH", "ARTS", "ASTR", "BCBP", "BIOL", "BMED", "CHEM", "CHME", "CISH", 
  "CIVL", "COGS", "COMM", "CSCI", "DSES", "ECON", "ECSE", "ENGR", "ENVE", 
  "EPOW", "ERTH", "ESCI", "IENV", "IHSS", "IHSS", "ISCI", "ITEC", "LANG", 
  "LGHT", "LITR", "MANE", "MATH", "MATP", "MGMT", "MTLE", "NSST", "PHIL", 
  "PHYS", "PSYC", "STSH", "STSS", "USAF", "USAR", "USNA", "WRIT"]

FileUtils.mkdir('catalog') rescue nil

all_departments.each do |dept|
  unless File.exist?('catalog/'+dept)
    pull_dept(dept).each do |coid|
      FileUtils.mkdir('catalog/'+dept) rescue nil
      contents = Net::HTTP.get(URI.parse("http://catalog.rpi.edu/preview_course.php?catoid=5&coid=#{coid}&print"))
      doc = Hpricot(contents)
      fulltitle = doc.search("td h1").inner_text
      catalogNumber = fulltitle.match('(.*) - (.*)')[1].sub(' ', '-')
      out = File.open("catalog/#{dept}/#{catalogNumber}.html", 'w')
      out.write(contents)
      out.close
      puts "Downloading #{catalogNumber}"
    end
  end
end
