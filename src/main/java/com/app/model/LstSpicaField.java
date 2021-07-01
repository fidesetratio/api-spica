package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstSpicaField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8785640479743295443L;
	
	private Integer lspc_form_id;
	private String lspc_field_name;
	private String lspc_field_type;
	private String lspc_field_nullable;
	private Date lspc_field_created_date;
	private Integer lspc_field_created_by;
	private Date lspc_field_modified_date;
	private Integer lspc_field_modified_by;
	private Integer lspc_field_flag_active;
	
	public Integer getLspc_form_id() {
		return lspc_form_id;
	}
	public void setLspc_form_id(Integer lspc_form_id) {
		this.lspc_form_id = lspc_form_id;
	}
	public String getLspc_field_name() {
		return lspc_field_name;
	}
	public void setLspc_field_name(String lspc_field_name) {
		this.lspc_field_name = lspc_field_name;
	}
	public String getLspc_field_type() {
		return lspc_field_type;
	}
	public void setLspc_field_type(String lspc_field_type) {
		this.lspc_field_type = lspc_field_type;
	}
	public String getLspc_field_nullable() {
		return lspc_field_nullable;
	}
	public void setLspc_field_nullable(String lspc_field_nullable) {
		this.lspc_field_nullable = lspc_field_nullable;
	}
	public Date getLspc_field_created_date() {
		return lspc_field_created_date;
	}
	public void setLspc_field_created_date(Date lspc_field_created_date) {
		this.lspc_field_created_date = lspc_field_created_date;
	}
	public Integer getLspc_field_created_by() {
		return lspc_field_created_by;
	}
	public void setLspc_field_created_by(Integer lspc_field_created_by) {
		this.lspc_field_created_by = lspc_field_created_by;
	}
	public Date getLspc_field_modified_date() {
		return lspc_field_modified_date;
	}
	public void setLspc_field_modified_date(Date lspc_field_modified_date) {
		this.lspc_field_modified_date = lspc_field_modified_date;
	}
	public Integer getLspc_field_modified_by() {
		return lspc_field_modified_by;
	}
	public void setLspc_field_modified_by(Integer lspc_field_modified_by) {
		this.lspc_field_modified_by = lspc_field_modified_by;
	}
	public Integer getLspc_field_flag_active() {
		return lspc_field_flag_active;
	}
	public void setLspc_field_flag_active(Integer lspc_field_flag_active) {
		this.lspc_field_flag_active = lspc_field_flag_active;
	}

}
