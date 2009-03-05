degree "B.S. Bioinformatics and Molecular Biology 2012", 2 do |d|
  d.section "Communication Requirement" do |s|
    s.valid_courses do |course|
      course.description =~ /(communication|writing)-intensive/i
    end
    s.exclusive = false
    s.count = 2
  end
  
