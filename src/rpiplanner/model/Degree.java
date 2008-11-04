package rpiplanner.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("degree")
public class Degree {
	private String name;
	private String note;
	private String validationCode;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getValidationCode(){
		return validationCode;
	}
	public void setValidationCode(String validationCode){
		this.validationCode = validationCode;
	}
	public String toString(){
		return getName();
	}
}
