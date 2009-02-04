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

import rpiplanner.RubyEnvironment;
import rpiplanner.validation.DegreeValidator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("degree")
public class Degree {
	private String name;
	private String note;
	private String validationCode;
	private long id = 0;
	
	@XStreamOmitField
	private DegreeValidator descriptor;
	
	/**
	 * Constructor for testing.
	 * @param name A name for the degree.
	 * @param validationCode Ruby code to execute for validation.
	 */
	public Degree(String name, String validationCode){
		this.name = name;
		this.validationCode = validationCode;
	}

	@XStreamOmitField
	private ValidationError[] errors;
	
	public String getName() {
		return name;
	}
	public String getNote() {
		return note;
	}
	public String getValidationCode(){
		return validationCode;
	}
	public long getID(){
		return id;
	}
	public String toString(){
		return getName();
	}
	public ValidationError[] getErrors() {
		if(errors == null)
			return new ValidationError[0];
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
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Degree){
			if(((Degree)obj).id == id)
				return true;
		}
		return false;
	}
	
	public String[] getSectionNames(){
		initDescriptor();
		return descriptor.getSectionNames();
	}
	
	private void initDescriptor(){
		if(descriptor == null)
			descriptor = RubyEnvironment.getInstance().getDegreeDescriptor(this);
	}
	public DegreeValidator getValidator() {
		initDescriptor();
		return descriptor;
	}
}
