package rpiplanner;

import java.util.ArrayList;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;
import rpiplanner.model.ValidationError;

public class PlanValidator {
	private static PlanValidator instance;
	private BSFManager rubyEnvironment;
	
	private PlanValidator(){
		BSFManager.registerScriptingEngine("ruby", 
                "org.jruby.javasupport.bsf.JRubyEngine", 
                new String[] { "rb" });

		rubyEnvironment = new BSFManager();
		try { // cause Ruby to load
			rubyEnvironment.eval("ruby", "(java)", 0, 0, "true");
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static PlanValidator getInstance(){
		if(instance == null)
			instance = new PlanValidator();
		return instance;
	}
	
	public boolean isValid(PlanOfStudy plan){
		for(Degree degree : plan.getDegrees()){
			if(!satisfiesDegree(plan, degree))
				return false;
		}
		return true;
	}
	
	public boolean satisfiesDegree(PlanOfStudy plan, Degree degree){
		return validate(plan, degree).length == 0;
	}

	public ValidationError[] validate(PlanOfStudy plan, Degree degree){
		try {
			ArrayList<String> errors = new ArrayList<String>();
			ArrayList<String> warnings = new ArrayList<String>();
			rubyEnvironment.declareBean("plan", plan, PlanOfStudy.class);
			rubyEnvironment.declareBean("errors", errors, ArrayList.class);
			rubyEnvironment.declareBean("warnings", warnings, ArrayList.class);
			
			rubyEnvironment.eval("ruby", "(java)", 1, 1, degree.getValidationCode());

			rubyEnvironment.undeclareBean("plan");
			rubyEnvironment.undeclareBean("errors");
			rubyEnvironment.undeclareBean("warnings");
			
			if(errors.isEmpty() && warnings.isEmpty())
				return new ValidationError[0];
			
			ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>(errors.size() + warnings.size());
			for(String err : errors)
				validationErrors.add(new ValidationError(ValidationError.Type.ERROR, err));
			for(String warn : warnings)
				validationErrors.add(new ValidationError(ValidationError.Type.WARNING, warn));
			
			return validationErrors.toArray(new ValidationError[0]);

		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ValidationError[0];
		}
	}
}
