# B.S. Computer Science 2011
$taken_courses = []

# Communication Requirement
commclasses = 0
each_course do |course|
  commclasses += 1 if course.description =~ /(communication|writing)-intensive/i
end
$errors << "Communication requirement: #{commclasses} of 2" if commclasses < 2

# Math & Science Courses
require_courses(['CSCI-1100', 'CSCI-1200','CSCI-2300','CSCI-2400','CSCI-2500','CSCI-4430','CSCI-4210','CSCI-4440'])
require_courses(['MATH-1010','MATH-1020','MATH-2800','PHYS-1100','BIOL-1010','CHEM-1100'])

# Mathematics Option
mathematics = false
each_course do |course|
  if course.catalogNumber =~ /MAT[HP]-[2346].../
    $taken_courses << course
    mathematics = true
  end
end
$errors << "Mathematics option not satsifed" unless mathematics

# Computer Science Option (3 classes)
compsci_opt = 0
each_course do |course|
  if course.catalogNumber =~ /CSCI-[46].../
    $taken_courses << course
    compsci_opt += 1
  elsif course.catalogNumber =~ /ECSE-4[67]../ && !['4630','4640','4720'].include?(course.catalogNumber[5..8])
    $taken_courses << course
    compsci_opt += 1
  elsif course.catalogNumber == 'ECSE-4490'
    $taken_courses << course
    compsci_opt += 1
  end
end
$errors << "Only #{compsci_opt} out of 3 Computer Science Option courses" if compsci_opt < 3

# Science Option
science_opt = false
each_course do |course|
  if ['ASTR','BIOL','CHEM','ERTH','PHYS'].include?(course.catalogNumber[0..3]) && 
    course.credits == 4 &&
    course.catalogNumber != 'ERTH-1030'
    $taken_courses << course
  end
end
$errors << "Science option not satisfied" unless science_opt

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
$errors << "Only #{free_credits} out of 28 free elective credits" if free_credits < 28

# Total credit hours required
total_credits = 0
each_course do |course|
  total_credits += course.credits
end
$errors << "Only #{total_credits} out of 128 total credits" if total_credits < 128
