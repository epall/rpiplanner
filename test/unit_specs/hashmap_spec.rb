import java.util.HashMap

describe "An empty", HashMap do
  before :each do
    @hash_map = HashMap.new
  end

  it "should be able to add an entry to it" do
    @hash_map.put "foo", "bar"
    @hash_map.get("foo").should == "bar"
  end

  it "should not be empty after an entry has been added to it" do
    @hash_map.put "foo", "bar"
    @hash_map.should_not be_empty
  end

  it "should be empty" do
    @hash_map.should be_empty
    @hash_map.size.should == 0
  end

  it "should return a keyset iterator that throws an exception on next" do
    proc do
      @hash_map.key_set.iterator.next
    end.should raise_error(java.util.NoSuchElementException)
  end
end