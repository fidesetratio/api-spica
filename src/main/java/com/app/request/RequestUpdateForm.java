package com.app.request;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class RequestUpdateForm implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3659006430835177171L;
	
	private Integer form_id;
	private String form_type;
	private String form_description;
	private String form_source;
	private String form_primary_attribute;
	private Integer user_id;
	
	public Integer getForm_id() {
		return form_id;
	}
	public void setForm_id(Integer form_id) {
		this.form_id = form_id;
	}
	public String getForm_type() {
		return form_type;
	}
	public void setForm_type(String form_type) {
		this.form_type = form_type;
	}
	public String getForm_description() {
		return form_description;
	}
	public void setForm_description(String form_description) {
		this.form_description = form_description;
	}
	public String getForm_source() {
		return form_source;
	}
	public void setForm_source(String form_source) {
		this.form_source = form_source;
	}
	public String getForm_primary_attribute() {
		return form_primary_attribute;
	}
	public void setForm_primary_attribute(String form_primary_attribute) {
		this.form_primary_attribute = form_primary_attribute;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
}
