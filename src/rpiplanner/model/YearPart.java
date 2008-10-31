/**
 * 
 */
package rpiplanner.model;

public enum YearPart {
	FALL,
	SPRING;
	
	public String toString(){
		String s = super.toString();
		s = s.substring(0, 1) + s.substring(1).toLowerCase();
		return s;
	}
}