include_class "rpiplanner.model.PlanOfStudy"

describe PlanOfStudy do
  before(:each) do
    @plan = PlanOfStudy.new
  end
  it "should have a list of terms" do
    @plan.should respond_to(:terms)
  end
end