package rpiplanner.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("degree")
public class Degree {
	private String name;
	private String note;
	private String validationCode;
	
	@XStreamOmitField
	private ValidationError[] errors;
	
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
	public ValidationError[] getErrors() {
		return errors;
	}
	public void setErrors(ValidationError[] errors) {
		this.errors = errors;
	}
	public ValidationError.Type validationStatus(){
		ValidationError.Type err = null;
		if(errors == null)
			return err;
		for(ValidationError e : errors){
			if(e.getType() == ValidationError.Type.ERROR){
				err = ValidationError.Type.ERROR;
			}
			else if(e.getType() == ValidationError.Type.WARNING && err == null){
				err = ValidationError.Type.WARNING;
			}
		}
		return err;
	}
}
