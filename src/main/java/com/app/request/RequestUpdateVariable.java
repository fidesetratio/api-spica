package com.app.request;

import java.io.Serializable;

public class RequestUpdateVariable implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1364242449864847109L;
	
	private Integer form_id;
	private String variable_name;
	private String variable_type;
	private String variable_expression;
	private String variable_description;
	private Integer user_id;
	
	public Integer getForm_id() {
		return form_id;
	}
	public void setForm_id(Integer form_id) {
		this.form_id = form_id;
	}
	public String getVariable_name() {
		return variable_name;
	}
	public void setVariable_name(String variable_name) {
		this.variable_name = variable_name;
	}
	public String getVariable_type() {
		return variable_type;
	}
	public void setVariable_type(String variable_type) {
		this.variable_type = variable_type;
	}
	public String getVariable_expression() {
		return variable_expression;
	}
	public void setVariable_expression(String variable_expression) {
		this.variable_expression = variable_expression;
	}
	public String getVariable_description() {
		return variable_description;
	}
	public void setVariable_description(String variable_description) {
		this.variable_description = variable_description;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

}
