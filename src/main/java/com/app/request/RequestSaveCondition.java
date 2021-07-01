package com.app.request;

import java.io.Serializable;

public class RequestSaveCondition implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2233496139024266774L;
	
	private Integer form_id;
	private String rule_name;
	private String condition_name;
	private String condition_expression;
	private String condition_description;
	private Integer user_id;
	
	public Integer getForm_id() {
		return form_id;
	}
	public void setForm_id(Integer form_id) {
		this.form_id = form_id;
	}
	public String getRule_name() {
		return rule_name;
	}
	public void setRule_name(String rule_name) {
		this.rule_name = rule_name;
	}
	public String getCondition_name() {
		return condition_name;
	}
	public void setCondition_name(String condition_name) {
		this.condition_name = condition_name;
	}
	public String getCondition_expression() {
		return condition_expression;
	}
	public void setCondition_expression(String condition_expression) {
		this.condition_expression = condition_expression;
	}
	public String getCondition_description() {
		return condition_description;
	}
	public void setCondition_description(String condition_description) {
		this.condition_description = condition_description;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

}
