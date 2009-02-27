require 'java'

class apcredit

  include Java::Rpiplanner::Apcredit
 -
def getcourse(tests, scores)

apcourses = []
  if test == "Computer Science AB"  && score == 5
  	apcourses << str_to_course("CSCI-1100")
  	apcourses << str_to_course("CHEM-1200")
  elsif test == "Computer Science AB" && score == 4
  	apcourses << str_to_course("CSCI-1100")
  elsif test == "Computer Science A" && score == 5
  	apcourses << str_to_course("CSCI-1100")
  elsif test == "Government & Politics" && [4,5].include?(score)
  	apcourses << str_to_course("STSS-1000")
  elsif test == "Biology" && score == 5
  	apcourses << str_to_course("BIOL-1010")
  elsif test == "Biology" && score == 4
  	apcourses << str_to_course("BIOL-1000")
  elsif test == "Chemistry" && score == 4
  	apcourses << str_to_course("CHEM-1100")
  	apcourses << str_to_course("CHEM-1200")
  elsif ((["English Language","English Literature"].include?(test)) && [4,5].include?(score)
  	apcourses << str_to_course("WRIT-1000")
  elsif (["American History","European History","World History"].include(test)) && [4,5].include?(score)
  	apcourses << str_to_course("STSH-1000")
  elsif test == "Statistics" && [4,5].include?(score)
  	apcourses << str_to_course("MGMT-2100")
  elsif test == "Psychology" && [4,5].include?(score)
  	apcourses << str_to_course("PSYC-1200")

  apcourses.to_java(Java::RpiplannerModel::Course)
  end

  def getscore(course)

  end

end

