require 'java'

def str_to_course(str)
  if str.class == String
    attempt = $courseDatabase.getCourse(str)
    if attempt.nil?
      attempt = Java::RpiplannerModel::EditableCourse.new
      attempt.catalogNumber = str
    end
    return attempt
  end
  
  #otherwise it's an array
  return str.map {|catalogNumber| str_to_course(catalogNumber)}
end

class SectionDescriptor
  attr_writer :exclusive, :count, :credits, :description
  attr_reader :exclusive
  
  def initialize
    @must_haves = {}
  end
  
  def valid_courses(&func)
    @course_filter = func
  end
  
  def valid_courses_special(&func)
    @course_filter_special = func
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
  
  def exclusive?
    @exclusive.nil? ? true : @exclusive
  end
  
  def specific?
    !!(@courses || @one_of)
  end
  
  def potential_courses(available_courses)
    available_courses.select {|course| @course_filter.call(course)}
  end
  
  def validate(available_courses)
    missing_courses = []
    applied_courses = []
    potential_courses = []
    messages = []
    @course_filter ||= Proc.new { true }
    if @course_filter_special
      potential_courses = @course_filter_special.call(available_courses)
    else
      potential_courses = available_courses.select {|course| @course_filter.call(course)}
    end

    if @courses
      applied_courses += potential_courses.select{ |course| @courses.include? course.catalogNumber}
      missing_courses += str_to_course(@courses - applied_courses.map(&:catalogNumber))
    elsif @credits
      running = 0
      potential_courses.each do |course|
        if running < @credits
          applied_courses << course
          running += course.credits
        end
      end
      messages << "Minimum of #{@credits} credits" if running < @credits
    elsif @count
      running = 0
      potential_courses.each do |course|
        if running < @count
          applied_courses << course
          running += 1
        end
      end
      messages << "Minimum of #{@count} courses" if running < @count
    end
    if @one_of
      candidates = potential_courses.select { |course| @one_of.include? course.catalogNumber}
      if candidates.size == 0
        missing_courses += str_to_course(@one_of)
      else
        applied_courses << candidates[0]
      end
    end
    if @must_haves
      @must_haves.each do |message, validation|
        messages << message unless validation.call(potential_courses)
      end
    end
    
    return {'missing' => missing_courses,
      'applied' => applied_courses,
      'messages' => messages}
  end
end

class DegreeDescriptor
  include Java::RpiplannerValidation::DegreeValidator
  attr_writer :total_credits
  
  class Results
    include Java::RpiplannerValidation::ValidationResult
    
    def initialize
      @sections = {}
    end
    
    def add(name, result)
      @sections[name] = result
    end
    
    def percentComplete
      successful = @sections.inject(0) {|sum, section| section[1].isSuccess ? sum + 1 : sum }
      return 100 * successful.to_f/@sections.size
    end
    
    def getSectionResults(name)
      @sections[name]
    end
  end
  
  class SectionResult
    include Java::RpiplannerValidation::ValidationResult::Section
    def initialize(params)
      @data = params
    end
    
    def missingCourses
      @data['missing'].to_java(Java::RpiplannerModel::Course)
    end
    
    def appliedCourses
      @data['applied'].to_java(Java::RpiplannerModel::Course)
    end
    
    def messages
      @data['messages'].to_java(:string)
    end
    
    def isSuccess
      @data['missing'].empty? && @data['messages'].empty?
    end
    
    def potentialCourses
      return nil if @potentialCourses.nil?
      return @potentialCourses.call.to_java(Java::RpiplannerModel::Course)
    end
    
    def preparePotentialCourses(&block)
      @potentialCourses = block
    end
  end

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
  
  def validate(plan)
    result = Results.new
    exclusivecourses = plan.terms.inject([]) { |sum,term| sum + term.courses.to_a }
    courses = plan.terms.inject([]) { |sum,term| sum + term.courses.to_a }
    
    @sections.each do |section_name, descriptor|
      sectionResult = nil
      
      if descriptor.exclusive?
        rawdata = descriptor.validate(exclusivecourses)
        exclusivecourses.reject! {|course| rawdata['applied'].include? course}
        section = SectionResult.new(rawdata)
        section.preparePotentialCourses do
          descriptor.potential_courses($courseDatabase.listCourses)
        end unless descriptor.specific?
        
        result.add(section_name, section)
      else
        section = SectionResult.new(descriptor.validate(courses))
        section.preparePotentialCourses do
          descriptor.potential_courses($courseDatabase.listCourses)
        end unless descriptor.specific?

        result.add(section_name, section)
      end
    end
    return result
  end
end

def degree(name, id)
  descriptor = DegreeDescriptor.new
  yield(descriptor)
  $degrees[id] = descriptor
end
