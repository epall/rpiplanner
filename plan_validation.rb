def course_present(ctlgNumber)
  found = nil
  $plan.terms.each do |term|
    term.courses.each do |course|
      found ||= course if course.catalog_number == ctlgNumber
    end
  end
  return found
end

def each_course
  $plan.terms.each do |term|
    term.courses.each do |course|
      yield course
    end
  end
end

def require_course(ctlgNumber)
  if (course = course_present(ctlgNumber))
    $taken_courses << course
  else
    $errors << "Requred course not present: #{ctlgNumber}"
  end
end

def require_courses(courses)
  courses.each {|c| require_course(c)}
end

def require_one_of(courses)
  found = nil
  courses.each do |c|
    if !$taken_courses.find{|course| course.catalog_number == c}
      if (course = course_present(c))
        $taken_courses << course if !found
        found ||= course
      end
    end
  end
  $errors << "One of #{courses.join(',')} not present" if found.nil?
end