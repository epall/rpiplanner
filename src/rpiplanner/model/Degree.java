/* RPI Planner - Customized plans of study for RPI students.
 *
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

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
