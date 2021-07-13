package com.app.request;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class RequestUpdateFormField implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5384585679944314637L;
	
	private Integer form_id;
	private String form_source;
	private Integer user_id;
	
	public Integer getForm_id() {
		return form_id;
	}
	public void setForm_id(Integer form_id) {
		this.form_id = form_id;
	}
	public String getForm_source() {
		return form_source;
	}
	public void setForm_source(String form_source) {
		this.form_source = form_source;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
}
