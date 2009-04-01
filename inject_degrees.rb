require "rexml/document"

file = File.new( "course_database.xml" )
doc = REXML::Document.new file
# doc = REXML::Document.new
# doc.elements.delete_all '//course'
doc.elements.delete_all '//degree'

Dir.glob("degrees/*.rb").each do |file|
  degree = REXML::Element.new "degree"
  contents = File.new(file, 'r')
  validationCode = contents.readline.strip
  validationCode =~ /degree "(.*)", ([0123456789]*) do |d|/
  validationCode << contents.read(nil)
  contents.close
  
  id = REXML::Element.new("id")
  id.text = $2.to_s
  degree.add id
  
  name = REXML::Element.new("name")
  name.text = $1
  degree.add name
  
  code = REXML::Element.new("validationCode")
  code.text = validationCode
  degree.add code
  
  doc.root.add degree
end

# formatter = REXML::Formatters::Pretty.new
# formatter.write_document(doc, $stdout)
out = File.open("temp.xml", "w")
doc.write(out,2)
out.close

File.rename("course_database.xml", "course_database.xml.orig")
File.rename("temp.xml", "course_database.xml")
