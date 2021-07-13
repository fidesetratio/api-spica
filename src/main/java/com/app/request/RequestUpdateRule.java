package com.app.request;

import java.io.Serializable;

public class RequestUpdateRule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4288982992488838832L;
	
	private Integer form_id;
	private String rule_name;
	private String rule_description;
	private String rule_expression;
	private String rule_error_msg;
	private Integer user_id;
	private String rule_prerequisites;
	private Integer rule_category_id;
	
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
	public String getRule_description() {
		return rule_description;
	}
	public void setRule_description(String rule_description) {
		this.rule_description = rule_description;
	}
	public String getRule_expression() {
		return rule_expression;
	}
	public void setRule_expression(String rule_expression) {
		this.rule_expression = rule_expression;
	}
	public String getRule_error_msg() {
		return rule_error_msg;
	}
	public void setRule_error_msg(String rule_error_msg) {
		this.rule_error_msg = rule_error_msg;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getRule_prerequisites() {
		return rule_prerequisites;
	}
	public void setRule_prerequisites(String rule_prerequisites) {
		this.rule_prerequisites = rule_prerequisites;
	}
	public Integer getRule_category_id() {
		return rule_category_id;
	}
	public void setRule_category_id(Integer rule_category_id) {
		this.rule_category_id = rule_category_id;
	}
}
