include_class "rpiplanner.model.YearPart"

describe YearPart do
  it "should give a reasonable toString" do
    spring = YearPart::SPRING
    spring.toString().should == "Spring"
  end
end