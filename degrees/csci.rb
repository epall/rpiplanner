degree "B.S. Computer Science 2011", 2 do |d|
  d.section "Communication Requirement" do |s|
    s.valid_courses do |course|
      course.description =~ /(communication|writing)[- ]intensive/i
    end
    s.exclusive = false
    s.count = 2
  end

  d.section "Math & Science" do |s|
    s.courses 'CSCI-1100', 'CSCI-1200','CSCI-2300','CSCI-2400','CSCI-2500','CSCI-4430','CSCI-4210','CSCI-4440','MATH-1010','MATH-1020','MATH-2800','PHYS-1100','BIOL-1010'
  end

  d.section "Mathematics Option" do |s|
    s.valid_courses {|course| course.catalogNumber =~ /MAT[HP]-[2346].../}
    s.count = 1
  end
  
  d.section "Computer Science Option" do |s|
    s.valid_courses do |course|
      course.catalogNumber =~ /CSCI-[46].../ ||
        (course.catalogNumber =~ /ECSE-4[67]../ && !['4630','4640','4720'].include?(course.catalogNumber[5..8])) ||
        course.catalogNumber == 'ECSE-4490'
    end
    s.count = 3
  end
  
  d.section "Science Option" do |s|
    s.valid_courses do |course|
      ['ASTR','BIOL','CHEM','ERTH','PHYS'].include?(course.catalogNumber[0..3]) && 
        course.credits == 4 &&
        course.catalogNumber != 'ERTH-1030'
    end
    s.count = 1
  end

  d.section "H&SS Core" do |s|
    s.credits = 22
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

    s.must_have "Professional Development II" do |courses|
      courses.find {|c| c.catalogNumber == 'PSYC-4170' || c.catalogNumber == 'STSS-4840'}
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