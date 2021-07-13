package com.app.request;

import java.io.Serializable;

public class RequestEvaluateForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6697223838041810909L;
	
	//evaluate_type = 1 --> get all field value
	//evaluate_type = 2 --> get field value when used by variable & condition expression only
	
	private Integer form_id;
	private Object primary_attribute;
	private Integer user_id;
	private Integer evaluate_type;
	
	public Integer getForm_id() {
		return form_id;
	}
	public void setForm_id(Integer form_id) {
		this.form_id = form_id;
	}
	public Object getPrimary_attribute() {
		return primary_attribute;
	}
	public void setPrimary_attribute(Object primary_attribute) {
		this.primary_attribute = primary_attribute;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public Integer getEvaluate_type() {
		return evaluate_type;
	}
	public void setEvaluate_type(Integer evaluate_type) {
		this.evaluate_type = evaluate_type;
	}
}
