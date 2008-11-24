# B.S. Computer & Systems Engineering 2011
$taken_courses = []

# Communication Requirement
commclasses = 0
each_course do |course|
  commclasses = commclasses.succ if course.description =~ /(communication|writing)-intensive/i
end
$errors << "Communication requirement: #{comclasses} of 2" if commclasses < 2

# Math & Science Courses
require_courses(['CSCI-1200','CSCI-2300','MATH-1010','MATH-1020','MATH-2400','MATH-2800','PHYS-1100','PHYS-1200','CHEM-1100','CSCI-1100'])

# Core Engineering Courses
require_courses(['ENGR-1100','ENGR-2050','ENGR-2350','ENGR-1200','ENGR-1300','ENGR-4010'])

# Required Courses
require_courses(['ECSE-2010','ECSE-2610','ECSE-2660','ECSE-2410','ECSE-4500'])

# Multidisciplinary Elective
require_one_of(['ENGR-1600','ENGR-2090','ENGR-2250','ENGR-2530'])

#Software Engineering Elective
require_one_of(['ECSE-4690','ECSE-4750','CSCI-4380','CSCI-4440','CSCI-4600'])

# Design Elective
require_one_of(['ECSE-4780','ECSE-4900','ECSE-4980','MANE-4220','EPOW-4850'])

# Restricted Electives
# INCOMPLETE
restricted_credits = 0
each_course do |course|
  if course.catalogNumber =~ /(ECSE|CSCI)/ && !$taken_courses.include?(course)
    restricted_credits += course.credits
    $taken_courses << course
  end
end
$errors << "Only #{restricted_credits} out of 9 restricted elective credits" if restricted_credits < 9

# H&SS Core
humanities_credits = 0
socialscience_credits = 0
thousandlevel = 0
humanities_courses = []
socialscience_courses = []
fourcredits_hum = 0
fourcredits_soc = 0
fourthousand = false
each_course do |course|
  if ['IHSS','ARTS','LANG','LITR','COMM','WRIT','STSH','PHIL'].include?(course.catalogNumber[0..3])
    fourcredits_hum += 1 if course.credits == 4
    fourthousand |= (course.catalogNumber[5..5] == '4' && course.credits == 4)
    if course.catalogNumber[5..5] == '1' # 1000-level course
      if thousandlevel < 3
        humanities_credits += course.credits
        humanities_courses << course
        $taken_courses << course
      end
      thousandlevel+=1
    else
      humanities_credits += course.credits
      humanities_courses << course
      $taken_courses << course
    end
  elsif ['COGS','ECON','IHSS','PSYC','STSS'].include?(course.catalogNumber[0..3])
    fourcredits_soc += 1 if course.credits == 4
    fourthousand |= (course.catalogNumber[5..5] == '4' && course.credits == 4)
    if course.catalogNumber[5..5] == '1' # 1000-level course
      if thousandlevel < 3
        socialscience_credits += course.credits
        socialscience_courses << course
        $taken_courses << course
      end
      thousandlevel+=1
    else
      socialscience_credits += course.credits
      socialscience_courses << course
      $taken_courses << course
    end
  end
end
$errors << "Only #{humanities_credits+socialscience_credits} out of 22 H&SS credits" if (humanities_credits+socialscience_credits) < 22
$errors << "Minimum of 2 4-credit courses in Humanities" if fourcredits_hum < 2
$errors << "Minimum of 2 4-credit courses in Social Sciences" if fourcredits_soc < 2
$errors << "At least ONE 4 credit course must be at the 4000 level" if !fourthousand

#depth requirement
has_depth = false
departments = {}
(humanities_courses + socialscience_courses).each do |course|
  if departments[course.catalogNumber[0..3]].nil?
    departments[course.catalogNumber[0..3]] = [course]
  else
    departments[course.catalogNumber[0..3]] << course
  end
end

departments.each do |dept|
  if dept.size > 2 &&
    dept[0].credits >= 4 && dept[1].credits >= 4 &&
    (dept.find{|course| course.catalogNumber =~ /^.....[^1]/})
      has_depth = true
  end
end

$errors << "H&SS Depth Requirement not satisfied" if !has_depth

# Free Electives
free_credits = 0
each_course do |course|
  if !$taken_courses.include?(course)
    free_credits += course.credits
    $taken_courses << course
  end
end
$errors << "Only #{free_credits} out of 12 free elective credits" if free_credits < 12

# Total credit hours required
total_credits = 0
each_course do |course|
  total_credits += course.credits
end
$errors << "Only #{total_credits} out of 128 total credits" if total_credits < 128
