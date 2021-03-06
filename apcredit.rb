require 'java'

class Apcredit
  include Java::Rpiplanner::Apcredit

  def getcourse(tests, scores)
    apcourses = []
    i = 0

    tests.each do
      test = tests[i]
      score = scores[i]
      i+=1

      if test == "Computer Science AB"  && score == 5
        apcourses << str_to_course("CSCI-1100")
        apcourses << str_to_course("CSCI-1200")
      elsif test == "Computer Science AB" && score == 4
        apcourses << str_to_course("CSCI-1100")
      elsif test == "Computer Science A" && score == 5
        apcourses << str_to_course("CSCI-1100")
      elsif test == "Government & Politics" && (score == 4)
        apcourses << str_to_course("STSS-1000")
      elsif test == "Government & Politics" && (score == 5)
        apcourses << str_to_course("STSS-1000")
      elsif test == "Biology" && score == 5
        apcourses << str_to_course("BIOL-1010")
      elsif test == "Biology" && score == 4
        apcourses << str_to_course("BIOL-1000")
      elsif test == "Chemistry" && score == 4
        apcourses << str_to_course("CHEM-1100")
        apcourses << str_to_course("CHEM-1200")
      elsif test == "Chemistry" && score == 5
        apcourses << str_to_course("CHEM-1100")
        apcourses << str_to_course("CHEM-1200")
      elsif test == "English Language" && score == 4
        apcourses << str_to_course("WRIT-1000")
      elsif test == "English Language" && score == 5
        apcourses << str_to_course("WRIT-1000")
      elsif test == "English Literature" && score == 4
        apcourses << str_to_course("WRIT-1000")
      elsif test == "English Literature" && score == 5
        apcourses << str_to_course("WRIT-1000")
      elsif test == "Environmental Science" && score >= 4
        apcourses << str_to_course("IENV-1000")
      elsif test == "American History" && score == 4
        apcourses << str_to_course("STSH-1000")
      elsif test == "European History" && score == 4
        apcourses << str_to_course("STSH-1000")
      elsif test == "World History" && score == 4
        apcourses << str_to_course("STSH-1000")
      elsif test == "American History" && score == 5
        apcourses << str_to_course("STSH-1000")
      elsif test == "European History" && score == 5
        apcourses << str_to_course("STSH-1000")
      elsif test == "World History" && score == 5
        apcourses << str_to_course("STSH-1000")
      elsif test == "Statistics" && score == 4
        apcourses << str_to_course("MGMT-2100")
      elsif test == "Statistics" && score == 5
        apcourses << str_to_course("MGMT-2100")
      elsif test == "Psychology" && score == 5
        apcourses << str_to_course("PSYC-1200")
      elsif test == "Psychology" && score == 4
        apcourses << str_to_course("PSYC-1200")
      elsif test == "Microeconomics & Macroeconomics" && score == 3
        apcourses << str_to_course("ECON-1201")
      elsif test == "Microeconomics & Macroeconomics" && score == 4
        apcourses << str_to_course("ECON-1200")
      elsif test == "Microeconomics & Macroeconomics" && score == 5
        apcourses << str_to_course("ECON-1200")
      elsif test == "French" && score == 4
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1111")
      elsif test == "French" && score == 5
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1111")
        apcourses << str_to_course("LANG-1121")
      elsif test == "Spanish" && score == 4
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1511")
      elsif test == "Spanish" && score == 5
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1511")
        apcourses << str_to_course("LANG-1521")
      elsif test == "Japanese" && score == 4
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1211")
      elsif test == "Japanese" && score == 5
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1211")
        apcourses << str_to_course("LANG-1221")
      elsif test == "Chinese" && score == 4
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1411")
      elsif test == "Chinese" && score == 5
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1411")
        apcourses << str_to_course("LANG-1421")
      elsif test == "German" && score == 4
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1311")
      elsif test == "German" && score == 5
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1311")
        apcourses << str_to_course("LANG-1321")
      elsif test == "Italian" && score == 4
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1611")
      elsif test == "Italian" && score == 5
        apcourses << str_to_course("LANG-1000")
        apcourses << str_to_course("LANG-1611")
        apcourses << str_to_course("LANG-1621")
      elsif test == "Physics C: Mechanics" && score == 4
        if tests.include?("Physics C: Electricity/Magnetism")
          pos = tests.index("Physics C: Electricity/Magnetism")
          if scores[pos] == 4 || scores[pos] == 5
            apcourses << str_to_course("PHYS-1200")
          end
          apcourses << str_to_course("PHYS-1100")
        end
      elsif test == "Physics C: Mechanics" && score == 5
        if tests.include?("Physics C: Electricity/Magnetism")
          pos = tests.index("Physics C: Electricity/Magnetism")
          if scores[pos] == 4 || scores[pos] == 5
            apcourses << str_to_course("PHYS-1200")
          end
          apcourses << str_to_course("PHYS-1100")
        end
      end
    end

    return apcourses.to_java(Java::RpiplannerModel::Course)
  end
end
