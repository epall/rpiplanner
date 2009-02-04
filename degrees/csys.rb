degree "B.S. Computer & Systems Engineering 2011", 2 do |d|
  d.section "Communication Requirement" do |s|
    s.valid_courses do |course|
      course.description =~ /(communication|writing)-intensive/i
    end
    s.exclusive = false
    s.count = 2
  end

  d.section "Math & Science" do |s|
    s.courses 'CSCI-1100','CSCI-1200','CSCI-2300','MATH-1010','MATH-1020','MATH-2400','MATH-2800','PHYS-1100','PHYS-1200','CHEM-1100'
  end

  d.section "Core Engineering" do |s|
    s.courses 'ENGR-1100','ENGR-2050','ENGR-2350','ENGR-1200','ENGR-4010'
    s.one_of 'ENGR-1300','ENGR-1310'
  end

  d.section "Required Courses" do |s|
    s.courses 'ECSE-2010','ECSE-2610','ECSE-2660','ECSE-2410','ECSE-4500'
  end

  d.section "Multidisciplinary Elective" do |s|
    s.one_of 'ENGR-1600','ENGR-2090','ENGR-2250','ENGR-2530'
  end

  d.section "Software Engineering Elective" do |s|
    s.one_of 'ECSE-4690','ECSE-4750','CSCI-4380','CSCI-4440','CSCI-4600'
  end

  d.section "Design Elective" do |s|
    s.one_of 'ECSE-4780','ECSE-4900','ECSE-4980','MANE-4220','EPOW-4850'
  end

  d.section "Restricted Electives" do |s|
    s.credits = 9
    s.valid_courses do |course|
      course.catalogNumber =~ /(ECSE|CSCI)/
    end
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
    numonethousand = 0
    s.valid_courses do |course|
      if ['IHSS','ARTS','LANG','LITR','COMM','WRIT','STSH','PHIL',
        'COGS','ECON','IHSS','PSYC','STSS'].include?(course.catalogNumber[0..3])
        if course.level = '1000'
          numonethousand += 1
          false if numonethousand > 3
        else
          true
        end
      end
    end

    s.must_have "minimum of 2 4-credit courses in Humanities" do |courses|
      courses = courses.find_all do |course| 
        course.credits == 4 && ['IHSS','ARTS','LANG','LITR','COMM','WRIT','STSH','PHIL'].include?(course.department)
      end
      courses.count >= 2
    end

    s.must_have "minimum of 2 4-credit courses in the Social Sciences" do |courses|
      courses = courses.find_all do |course| 
        course.credits == 4 && ['COGS','ECON','IHSS','PSYC','STSS'].include?(course.department)
      end
      courses.count >= 2
    end

    s.must_have "one 4 credit course at the 4000 level" do |courses|
      courses.find {|c| c.level == '4000'}
    end

    s.must_have "Professional Development II" do |courses|
      courses.find {|c| c.catalogNumber == 'PSYC-4170' || c.catalogNumber == 'STSS-4840'}
    end
  end

  d.section "Free Electives" do |s|
    s.valid_courses {|c| true}
    s.credits = 12
  end

  d.total_credits = 128
end