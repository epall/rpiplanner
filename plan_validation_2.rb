require 'java'

class SectionDescriptor
  attr_writer :exclusive, :count, :credits, :description
  
  def initialize
    @must_haves = {}
  end
  
  def valid_courses(&func)
    @course_filter = func
  end
  
  def courses(*courses)
    @courses = courses
  end
  
  def one_of(*courses)
    @one_of = courses
  end
  
  def must_have(description, &func)
    @must_haves[description] = func
  end
  
  def validate(plan_of_study)
    missing_courses = []
    applied_courses = []
    messages = []
    @course_filter ||= Proc.new { true }
    potential_courses = filter(plan_of_study)
    if @courses
      applied_courses += potential_courses.find_all{ |course| @courses.include? course.catalogNumber}.map(&:catalogNumber)
      missing_courses += @courses - applied_courses
    end
    if @one_of
      candidates = potential_courses.find_all{ |course| @one_of.include? course.catalogNumber}.map(&:catalogNumber)
      if candidates.count == 0
        missing_courses += @one_of
      else
        applied_courses << candidates[0]
      end
    end
    if @must_have
      @must_haves.each do |message, validation|
        messages << message unless validation.call(potential_courses)
      end
    end
    return {'missing' => missing_courses,
      'applied' => applied_courses,
      'messages' => messages}
  end
  
  private
  def filter(plan)
    courses = []
    plan.terms.each do |term|
      term.courses.each do |course|
        courses << course if @course_filter.call(course)
      end
    end
    return courses
  end
end

class DegreeDescriptor
  include Java::RpiplannerValidation::DegreeValidator
  attr_writer :total_credits

  def initialize
    @sections = {}
  end
  
  def section(name)
    descriptor = SectionDescriptor.new
    yield(descriptor)
    @sections[name] = descriptor
  end
  
  def getSectionNames
    @sections.keys.to_java(:string)
  end
  
  def validate(section_name, plan)
    @sections[section_name].validate(plan)
  end
end

def degree(name, id)
  descriptor = DegreeDescriptor.new
  yield(descriptor)
  $degrees[id] = descriptor
end
