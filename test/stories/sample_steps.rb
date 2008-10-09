# import java.lang.String

steps_for (:sample) do
  Given 'a string "$1"' do |value|
    @s = java.lang.String.new(value)
  end
  
  When 'the string is reversed' do
    rs = @s.toString
    @s = java.lang.String.new(rs.reverse)
  end
  
  Then 'the string should equal "$result"' do |result|
    @s.toString.should == result
  end
end

with_steps_for(:sample) do
  run 'test/stories/sample.story'
end