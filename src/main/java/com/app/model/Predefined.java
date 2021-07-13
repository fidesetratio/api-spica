package com.app.model;

import java.util.HashMap;

import com.app.utils.MvelUtility;

public class Predefined {
	
	private String expression;
	private String functionName;
	private HashMap<String,Object> parameters;
	private String error;
	
	
	
	public Predefined(String functionName, String expression) {
		this.functionName = functionName;
		this.expression = expression;
		this.parameters = new HashMap<String, Object>();
		
		
	}
	
	public void addParameter(String var, Object value) {
		this.parameters.put(var, value);
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public HashMap<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(HashMap<String, Object> parameters) {
		this.parameters = parameters;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	
	public Object eval() throws IllegalArgumentException {
		return	MvelUtility.eval(expression,parameters);
		
	}
	public Result eval(boolean ret) {
		if(ret) {
			Result result = new Result();
			result.setErrorMessage("ok");
			try {
				Object object = this.eval();
				result.setValue(object);
			}catch(Exception e) {
				result.setErrorMessage("error:"+e.getMessage());
			}
		
		return result;
		}
		
		return null;
		
	}

}
