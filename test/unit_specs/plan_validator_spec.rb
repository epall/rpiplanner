include_class "rpiplanner.model.Degree"
include_class "rpiplanner.model.PlanOfStudy"
include_class "rpiplanner.PlanValidator"

describe PlanValidator do
  before :each do
    @validator = PlanValidator.instance
  end
  
  it "should eval the code contained in the degree" do
    degree = Degree.new
    plan = PlanOfStudy.new
    
    degree.validationCode = "false"
    @validator.satisfiesDegree(plan, degree).should == false
    
    degree.validationCode = "true"
    @validator.satisfiesDegree(plan, degree).should == true
  end
end