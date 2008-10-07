include_class "rpiplanner.Course"

describe Course do
  before :each do
    @course = Course.new
  end
  
  it "Should have a title" do
    @course.should respond_to :title
    @course.should respond_to :title=
    @course.title = "foo"
    @course.title.should == "foo"
  end
end