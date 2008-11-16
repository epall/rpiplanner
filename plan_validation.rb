def course_present(ctlgNumber)
  found = false
  $plan.terms.each do |term|
    term.courses.each do |course|
      found |= (course.catalog_number == ctlgNumber)
    end
  end
  return found
end

def require_course(ctlgNumber)
  $errors << "Requred course not present: #{ctlgNumber}" if !course_present(ctlgNumber)
end

def require_courses(courses)
  courses.each {|c| require_course(c)}
end

def require_one_of(courses)
  found = false
  courses.each {|c| found |= course_present(c)}
  $errors << "One of #{courses.join(',')} not present" if !found
end