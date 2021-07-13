package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstSpicaForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1632613070054805703L;
	
	private Integer lspc_form_id;
	private String lspc_form_type;
	private String lspc_form_description;
	private String lspc_form_source;
	private String lspc_form_primary_attribute;
	private Date lspc_form_created_date;
	private Integer lspc_form_created_by;
	private Date lspc_form_modified_date;
	private Integer lspc_form_modified_by;
	private Integer lspc_form_flag_active;
	
	public Integer getLspc_form_id() {
		return lspc_form_id;
	}
	public void setLspc_form_id(Integer lspc_form_id) {
		this.lspc_form_id = lspc_form_id;
	}
	public String getLspc_form_type() {
		return lspc_form_type;
	}
	public void setLspc_form_type(String lspc_form_type) {
		this.lspc_form_type = lspc_form_type;
	}
	public String getLspc_form_description() {
		return lspc_form_description;
	}
	public void setLspc_form_description(String lspc_form_description) {
		this.lspc_form_description = lspc_form_description;
	}
	public String getLspc_form_source() {
		return lspc_form_source;
	}
	public void setLspc_form_source(String lspc_form_source) {
		this.lspc_form_source = lspc_form_source;
	}
	public String getLspc_form_primary_attribute() {
		return lspc_form_primary_attribute;
	}
	public void setLspc_form_primary_attribute(String lspc_form_primary_attribute) {
		this.lspc_form_primary_attribute = lspc_form_primary_attribute;
	}
	public Date getLspc_form_created_date() {
		return lspc_form_created_date;
	}
	public void setLspc_form_created_date(Date lspc_form_created_date) {
		this.lspc_form_created_date = lspc_form_created_date;
	}
	public Integer getLspc_form_created_by() {
		return lspc_form_created_by;
	}
	public void setLspc_form_created_by(Integer lspc_form_created_by) {
		this.lspc_form_created_by = lspc_form_created_by;
	}
	public Date getLspc_form_modified_date() {
		return lspc_form_modified_date;
	}
	public void setLspc_form_modified_date(Date lspc_form_modified_date) {
		this.lspc_form_modified_date = lspc_form_modified_date;
	}
	public Integer getLspc_form_modified_by() {
		return lspc_form_modified_by;
	}
	public void setLspc_form_modified_by(Integer lspc_form_modified_by) {
		this.lspc_form_modified_by = lspc_form_modified_by;
	}
	public Integer getLspc_form_flag_active() {
		return lspc_form_flag_active;
	}
	public void setLspc_form_flag_active(Integer lspc_form_flag_active) {
		this.lspc_form_flag_active = lspc_form_flag_active;
	}

}
