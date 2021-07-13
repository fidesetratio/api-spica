package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstSpicaCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5225433856650376804L;
	
	private Integer lspc_form_id;
	private String lspc_rule_name;
	private String lspc_condition_name;
	private String lspc_condition_expression;
	private String lspc_condition_description;
	private Date lspc_condition_created_date;
	private Integer lspc_condition_created_by;
	private Date lspc_condition_modified_date;
	private Integer lspc_condition_modified_by;
	private Integer lspc_condition_flag_active;
	
	public Integer getLspc_form_id() {
		return lspc_form_id;
	}
	public void setLspc_form_id(Integer lspc_form_id) {
		this.lspc_form_id = lspc_form_id;
	}
	public String getLspc_rule_name() {
		return lspc_rule_name;
	}
	public void setLspc_rule_name(String lspc_rule_name) {
		this.lspc_rule_name = lspc_rule_name;
	}
	public String getLspc_condition_name() {
		return lspc_condition_name;
	}
	public void setLspc_condition_name(String lspc_condition_name) {
		this.lspc_condition_name = lspc_condition_name;
	}
	public String getLspc_condition_expression() {
		return lspc_condition_expression;
	}
	public void setLspc_condition_expression(String lspc_condition_expression) {
		this.lspc_condition_expression = lspc_condition_expression;
	}
	public String getLspc_condition_description() {
		return lspc_condition_description;
	}
	public void setLspc_condition_description(String lspc_condition_description) {
		this.lspc_condition_description = lspc_condition_description;
	}
	public Date getLspc_condition_created_date() {
		return lspc_condition_created_date;
	}
	public void setLspc_condition_created_date(Date lspc_condition_created_date) {
		this.lspc_condition_created_date = lspc_condition_created_date;
	}
	public Integer getLspc_condition_created_by() {
		return lspc_condition_created_by;
	}
	public void setLspc_condition_created_by(Integer lspc_condition_created_by) {
		this.lspc_condition_created_by = lspc_condition_created_by;
	}
	public Date getLspc_condition_modified_date() {
		return lspc_condition_modified_date;
	}
	public void setLspc_condition_modified_date(Date lspc_condition_modified_date) {
		this.lspc_condition_modified_date = lspc_condition_modified_date;
	}
	public Integer getLspc_condition_modified_by() {
		return lspc_condition_modified_by;
	}
	public void setLspc_condition_modified_by(Integer lspc_condition_modified_by) {
		this.lspc_condition_modified_by = lspc_condition_modified_by;
	}
	public Integer getLspc_condition_flag_active() {
		return lspc_condition_flag_active;
	}
	public void setLspc_condition_flag_active(Integer lspc_condition_flag_active) {
		this.lspc_condition_flag_active = lspc_condition_flag_active;
	}

}
