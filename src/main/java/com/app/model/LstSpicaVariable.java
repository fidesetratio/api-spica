package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstSpicaVariable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1353428964476597423L;
	
	private Integer lspc_form_id;
	private String lspc_variable_name;
	private String lspc_variable_type;
	private String lspc_variable_expression;
	private String lspc_variable_description;
	private Date lspc_variable_created_date;
	private Integer lspc_variable_created_by;
	private Date lspc_variable_modified_date;
	private Integer lspc_variable_modified_by;
	private Integer lspc_variable_flag_active;
	
	public Integer getLspc_form_id() {
		return lspc_form_id;
	}
	public void setLspc_form_id(Integer lspc_form_id) {
		this.lspc_form_id = lspc_form_id;
	}
	public String getLspc_variable_name() {
		return lspc_variable_name;
	}
	public void setLspc_variable_name(String lspc_variable_name) {
		this.lspc_variable_name = lspc_variable_name;
	}
	public String getLspc_variable_type() {
		return lspc_variable_type;
	}
	public void setLspc_variable_type(String lspc_variable_type) {
		this.lspc_variable_type = lspc_variable_type;
	}
	public String getLspc_variable_expression() {
		return lspc_variable_expression;
	}
	public void setLspc_variable_expression(String lspc_variable_expression) {
		this.lspc_variable_expression = lspc_variable_expression;
	}
	public String getLspc_variable_description() {
		return lspc_variable_description;
	}
	public void setLspc_variable_description(String lspc_variable_description) {
		this.lspc_variable_description = lspc_variable_description;
	}
	public Date getLspc_variable_created_date() {
		return lspc_variable_created_date;
	}
	public void setLspc_variable_created_date(Date lspc_variable_created_date) {
		this.lspc_variable_created_date = lspc_variable_created_date;
	}
	public Integer getLspc_variable_created_by() {
		return lspc_variable_created_by;
	}
	public void setLspc_variable_created_by(Integer lspc_variable_created_by) {
		this.lspc_variable_created_by = lspc_variable_created_by;
	}
	public Date getLspc_variable_modified_date() {
		return lspc_variable_modified_date;
	}
	public void setLspc_variable_modified_date(Date lspc_variable_modified_date) {
		this.lspc_variable_modified_date = lspc_variable_modified_date;
	}
	public Integer getLspc_variable_modified_by() {
		return lspc_variable_modified_by;
	}
	public void setLspc_variable_modified_by(Integer lspc_variable_modified_by) {
		this.lspc_variable_modified_by = lspc_variable_modified_by;
	}
	public Integer getLspc_variable_flag_active() {
		return lspc_variable_flag_active;
	}
	public void setLspc_variable_flag_active(Integer lspc_variable_flag_active) {
		this.lspc_variable_flag_active = lspc_variable_flag_active;
	}

}
