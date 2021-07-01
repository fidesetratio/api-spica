package com.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

public class Evaluator {
	
	private String variable;
	private String expression;
	private List<String> dependencyVariables;
	
	public Evaluator(String variable,String command) {
		this.expression = command;
		this.variable = variable;
		this.dependencyVariables = this.extractVariable(command);
		
	}

	private List<String> extractVariable(String command){
		//pattern variable evaluator test logic
		//String pattern = "[A-Za-z0-9]+";
		
		//pattern variable evaluator SPICA
		String pattern = "([^(,)]+)(?!.*\\()";		
		
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(command);
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group(0).trim());
			//System.out.println(m.group(0).trim());
		}
		return result;
	}
	
	
	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public List<String> getDependencyVariables() {
		return dependencyVariables;
	}

	public void setDependencyVariables(List<String> dependencyVariables) {
		this.dependencyVariables = dependencyVariables;
	}

}
