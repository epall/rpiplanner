require 'java'

class apcredit

  include Java::Rpiplanner::Apcredit
 -
def getcourse(test, score)

apcourses = []
  if test == "Computer Science AB"  && [5].include?(score)
  	apcourses << str_to_course("CSCI-1100")
  	apcourses << str_to_course("CHEM-1200")
  elsif test == "Computer Science AB" && [4].include?(score)
  	apcourses << str_to_course("CSCI-1100")
  elsif test == "Computer Science A" && [5].include?(score)
  	apcourses << str_to_course("CSCI-1100")
  when test == "Government & Politics" && [4,5].include?(score)
  	apcourses << str_to_course("STSS-1000")
  when test == "Biology" && [5].include?(score)
  	apcourses << str_to_course("BIOL-1010")
  when test == "Biology" && [4].include?(score)
  	apcourses << str_to_course("BIOL-1000")
  when test == "Chemistry" && [5].include?(score)
  	apcourses << str_to_course("CHEM-1100")
  	apcourses << str_to_course("CHEM-1200")
  when ((["English Language","English Literature"].include?(test)) && [4,5].include?(score)
  	apcourses << str_to_course("WRIT-1000")
  when (["American History","European History","World History"].include(test)) && [4,5].include?(score)
  	apcourses << str_to_course("STSH-1000")
  when test == "Statistics" && [4,5].include?(score)
  	apcourses << str_to_course("MGMT-2100")
  when test == "Psychology" && [4,5].include?(score)
  	apcourses << str_to_course("PSYC-1200")

  apcourses.to_java(Java::RpiplannerModel::Course)
  end

  def getscore(course)

  end

end

