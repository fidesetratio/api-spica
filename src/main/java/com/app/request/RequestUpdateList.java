package com.app.request;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class RequestUpdateList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5396925393575311569L;
	
	private String list_name;
	private String list_description;
	private String list_source;
	private String list_field_name;
	private String list_field_type;
	private Integer user_id;
	
	public String getList_name() {
		return list_name;
	}
	public void setList_name(String list_name) {
		this.list_name = list_name;
	}
	public String getList_description() {
		return list_description;
	}
	public void setList_description(String list_description) {
		this.list_description = list_description;
	}
	public String getList_source() {
		return list_source;
	}
	public void setList_source(String list_source) {
		this.list_source = list_source;
	}
	public String getList_field_name() {
		return list_field_name;
	}
	public void setList_field_name(String list_field_name) {
		this.list_field_name = list_field_name;
	}
	public String getList_field_type() {
		return list_field_type;
	}
	public void setList_field_type(String list_field_type) {
		this.list_field_type = list_field_type;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	
}
