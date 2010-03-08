require 'rubygems'
require 'typhoeus'
require 'hpricot'
require 'net/http'
require 'uri'
require 'fileutils'

$hydra = Typhoeus::Hydra.new(:max_concurrency => 5)

def pull_dept(department)
  $stderr.puts "Pulling #{department}"
  req = Typhoeus::Request.new('http://catalog.rpi.edu/content.php?catoid=5&navoid=111',
                              :params => {'filter[27]' => department,
                                'filter[29]' => '', 'cpage' => 1, 'cur_cat_oid' => 5, 'filter[32]' => 1,
                                'search_database' => 'Filter', 'filter[keyword]' => ''})

  req.on_complete do |response|
    FileUtils.mkdir('catalog/'+department) rescue nil
    $stderr.puts "Parsing #{department}"
    doc = Hpricot(response.body)

    doc.search("a:not(.footer) [@target=\"_blank\"]").each do |c|
      onclick = c.get_attribute('onclick')
      coid = onclick.match('showCourse\(\'5\', \'(.*)\',this.*')[1]
      name = c.inner_html
      yield coid
    end

    if doc.search("a[@href=\"javascript:course_search(2);\"]")
      page2 = Typhoeus::Request.new('http://catalog.rpi.edu/content.php?catoid=5&navoid=111',
                                    :method => :post, :params => {'filter[27]' => department,
                                    'filter[29]' => '', 'cpage' => 2, 'cur_cat_oid' => 5, 'filter[32]' => 1,
                                    'search_database' => 'Filter', 'filter[keyword]' => ''})

      page2.on_complete do |response|
        $stderr.puts "Parsing 2nd page of #{department}"
        doc = Hpricot(response.body)

        doc.search("a:not(.footer) [@target=\"_blank\"]").each do |c|
          onclick = c.get_attribute('onclick')
          coid = onclick.match('showCourse\(\'5\', \'(.*)\',this.*')[1]
          name = c.inner_html
          yield coid
        end
      end
      $hydra.queue page2
    end
  end

  $hydra.queue req
end

all_departments = ["ARCH", "ARTS", "ASTR", "BCBP", "BIOL", "BMED", "CHEM", "CHME", "CISH", 
  "CIVL", "COGS", "COMM", "CSCI", "DSES", "ECON", "ECSE", "ENGR", "ENVE", 
  "EPOW", "ERTH", "ESCI", "IENV", "IHSS", "IHSS", "ISCI", "ITEC", "LANG", 
  "LGHT", "LITR", "MANE", "MATH", "MATP", "MGMT", "MTLE", "NSST", "PHIL", 
  "PHYS", "PSYC", "STSH", "STSS", "USAF", "USAR", "USNA", "WRIT"]

FileUtils.mkdir('catalog') rescue nil

all_departments.each_slice(3) do |depts|
  depts.each do |dept|
    unless File.exist?('catalog/'+dept)
      pull_dept(dept) do |coid|
        #$stderr.puts "fetching #{coid}"
        req = Typhoeus::Request.new("http://catalog.rpi.edu/preview_course.php?catoid=5&coid=#{coid}&print")
        req.on_complete do |response|
          doc = Hpricot(response.body)
          fulltitle = doc.search("td h1").inner_text
          catalogNumber = fulltitle.match('(.*) - (.*)')[1].sub(' ', '-')
          #$stderr.puts "Dumping #{catalogNumber}"
          out = File.open("catalog/#{dept}/#{catalogNumber}.html", 'w')
          out.write(response.body)
          out.close
        end
        $hydra.queue req
      end
    end
  end
  $hydra.run
end
