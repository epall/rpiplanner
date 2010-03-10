degree "B.S. Bioinformatics & Molecular Biology 2012", 6 do |d|
  d.school "School of Science"

  d.section "Communication Requirement" do |s|
    s.valid_courses do |course|
      course.description =~ /(communication|writing)[- ]intensive/i
    end
    s.exclusive = false
    s.count = 2
  end

  d.section "Mathematics Requirement" do |s|
    s.valid_courses {|course| course.catalogNumber =~ /MAT[HP]-[12346].../}
    s.count = 3
  end
  
  d.section "Chemistry Requirement" do |s|
    s.courses 'CHEM-1100','CHEM-1200','CHEM-2250','CHEM-2260','CHEM-4330','CHEM-4340','CHEM-2230','CHEM-2240'
  end
  
  d.section "Computer Science Requirement" do |s|
    s.courses 'CSCI-1100','CSCI-1200'
    s.count = 2
  end
  
  d.section "Physics Requirement" do |s|
	s.courses 'PHYS-1100','PHYS-1200'
	s.count = 2
  end
	
  d.section "Required Courses" do |s|
    s.courses 'BIOL-1010','BIOL-2120','BIOL-4620','BIOL-4760','BIOL-4770','BIOL-4540','BIOL-4720','BIOL-4550','BIOL-4630'
	s.one_of 'BIOL-4630','BIOL-4640','CHEM-4300'
  end
  
  d.section "Culminating Experience" do |s|
    s.courses "BIOL-4990"
  end

  d.section "H&SS Core" do |s|
    s.credits = 24
    s.description = <<-EOF
    Select a minimum of 2 4-credit courses in Humanities 
    Select a minimum of 2 4-credit courses in the Social Sciences 
    No more than 3 1000-level H&SS courses may be applied to the H&SS Core 
    No more than 6 credits may be taken P/NC 
    At least ONE 4 credit course must be at the 4000 level 
    No more than 2 courses may transfer towards the 
    H&SS Core (including Adv Placement), excludes Transfer Students. 
    Depth Requirement: 2 4-credit courses in same H or SS 
    subject area with at least 1 above the .1000 level and none on Pass/No Credit 
    Engineering majors (except ROTC cadets) choose a 2 credit 
    course to meet their Profess Development II requirement. 
    (PSYC 4170 or STSS 4840 Professional Development). 
    The TOTAL H&SS Core Requirement is 22 credits.
    EOF
    s.valid_courses_special do |courses|
      numonethousand = 0
      courses.select do |course|
        if ['IHSS','ARTS','LANG','LITR','COMM','WRIT','STSH','PHIL',
          'COGS','ECON','IHSS','PSYC','STSS'].include?(course.catalogNumber[0..3])
          if course.level == '1000'
            numonethousand += 1
            numonethousand <= 3
          else
            true
          end
        else
          false
        end
      end
    end
    
    s.valid_courses do |course|
        ['IHSS','ARTS','LANG','LITR','COMM','WRIT','STSH','PHIL',
          'COGS','ECON','IHSS','PSYC','STSS'].include?(course.catalogNumber[0..3])
    end
    
    s.must_have "minimum of 2 4-credit courses in Humanities" do |courses|
      courses = courses.find_all do |course| 
        course.credits == 4 && ['IHSS','ARTS','LANG','LITR','COMM','WRIT','STSH','PHIL'].include?(course.catalogNumber[0..3])
      end
      courses.size >= 2
    end

    s.must_have "minimum of 2 4-credit courses in the Social Sciences" do |courses|
      courses = courses.find_all do |course| 
        course.credits == 4 && ['COGS','ECON','IHSS','PSYC','STSS'].include?(course.catalogNumber[0..3])
      end
      courses.size >= 2
    end

    s.must_have "one 4 credit course at the 4000 level" do |courses|
      courses.find {|c| c.level == '4000' && c.credits == 4}
    end
    
    s.must_have "Depth Requirement" do |courses|
      departments = {}
      courses.each do |course|
        dept = course.catalogNumber[0..3]
        if course.credits == 4
          departments[dept] ||= []
          departments[dept] << course
        end
      end
      
      success = false
      departments.each do |dept, courses|
        above1k = false
        courses.each do |course|
          above1k ||= course.level != '1000'
        end
        if courses.size >= 2 && above1k
          success = true
        end
      end
      success
    end
  end

  d.section "Free Electives" do |s|
    s.valid_courses {|c| true}
    s.credits = 28
  end

  d.total_credits = 128
end