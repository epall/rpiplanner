include_class "rpiplanner.model.Degree"
include_class "rpiplanner.model.PlanOfStudy"
include_class "rpiplanner.model.Course"
include_class "rpiplanner.PlanValidator"

describe PlanValidator do
  before :each do
    @validator = PlanValidator.instance
  end
  
  it "should eval the code contained in the degree" do
    degree = Degree.new
    plan = PlanOfStudy.new
    
    degree.validationCode = "$errors << 'fail'"
    @validator.satisfiesDegree(plan, degree).should == false
    
    degree.validationCode = "true"
    @validator.satisfiesDegree(plan, degree).should == true
  end
  
  it "should be possible to iterate each course" do
    degree = Degree.new
    plan = PlanOfStudy.new
    course1 = Course.new
    course1.catalogNumber = "XXXX-0000"
    
    plan.terms[0].courses << course1
    
    degree.validationCode = <<-EOF
    each_course do |course|
      $errors << "foo"
    end
    EOF
    
    @validator.validate(plan, degree)[0].toString.should == "foo"
  end
end