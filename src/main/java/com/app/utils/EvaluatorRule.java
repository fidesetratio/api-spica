package com.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

public class EvaluatorRule {
	
	private String rule;
	private String prerequisites;
	private String expression;
	private String error_message;
	private String description;
	private Integer rule_category_id;
	private List<String> dependencyVariables;
	
	public EvaluatorRule(String rule,String prerequisites, String expression, String error_message, String description, Integer rule_category_id) {
		this.prerequisites = prerequisites;
		this.rule = rule;
		this.expression = expression;
		this.error_message = error_message;
		this.description = description;
		this.rule_category_id = rule_category_id;
		this.dependencyVariables = this.extractRulePrerequisites(prerequisites);
		
	}

	private List<String> extractRulePrerequisites(String prerequisites){
		if(prerequisites == null) {
			prerequisites = "";
		}
		
		//String pattern = "\\s*[|&]+\\s*" 
		String pattern = "[^|&]+";		
		
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(prerequisites);
		List<String> result = new ArrayList<String>();
		while (m.find()) {
			result.add(m.group(0).trim());
		}
		
		return result;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getPrerequisites() {
		return prerequisites;
	}

	public void setPrerequisites(String prerequisites) {
		this.prerequisites = prerequisites;
	}

	public List<String> getDependencyVariables() {
		return dependencyVariables;
	}

	public void setDependencyVariables(List<String> dependencyVariables) {
		this.dependencyVariables = dependencyVariables;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRule_category_id() {
		return rule_category_id;
	}

	public void setRule_category_id(Integer rule_category_id) {
		this.rule_category_id = rule_category_id;
	}
}
