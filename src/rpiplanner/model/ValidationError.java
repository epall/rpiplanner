package rpiplanner.model;

public class ValidationError {
	public enum Type {WARNING, ERROR};
	
	private Type type;
	private String message;
	
	public ValidationError(Type type, String message) {
		this.type = type;
		this.message = message;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
