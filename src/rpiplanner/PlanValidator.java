package rpiplanner;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import rpiplanner.model.Degree;
import rpiplanner.model.PlanOfStudy;

public class PlanValidator {
	private static PlanValidator instance;
	private BSFManager rubyEnvironment;
	
	private PlanValidator(){
		BSFManager.registerScriptingEngine("ruby", 
                "org.jruby.javasupport.bsf.JRubyEngine", 
                new String[] { "rb" });

		rubyEnvironment = new BSFManager();
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
		try {
			rubyEnvironment.declareBean("plan", plan, PlanOfStudy.class);
			boolean valid = (Boolean)rubyEnvironment.eval("ruby", "(java)", 1, 1, degree.getValidationCode());
			rubyEnvironment.undeclareBean("plan");
			return valid;
		} catch (BSFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
