package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstSpicaRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5408526909067939389L;
	
	private Integer lspc_form_id;
	private String lspc_rule_name;
	private String lspc_rule_description;
	private String lspc_rule_expression;
	private String lspc_rule_error_msg;
	private Date lspc_rule_created_date;
	private Integer lspc_rule_created_by;
	private Date lspc_rule_modified_date;
	private Integer lspc_rule_modified_by;
	private Integer lspc_rule_flag_active;
	private String lspc_rule_prerequisites;
	private Integer lspc_rule_category_id;
	
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
	public String getLspc_rule_description() {
		return lspc_rule_description;
	}
	public void setLspc_rule_description(String lspc_rule_description) {
		this.lspc_rule_description = lspc_rule_description;
	}
	public String getLspc_rule_expression() {
		return lspc_rule_expression;
	}
	public void setLspc_rule_expression(String lspc_rule_expression) {
		this.lspc_rule_expression = lspc_rule_expression;
	}
	public String getLspc_rule_error_msg() {
		return lspc_rule_error_msg;
	}
	public void setLspc_rule_error_msg(String lspc_rule_error_msg) {
		this.lspc_rule_error_msg = lspc_rule_error_msg;
	}
	public Date getLspc_rule_created_date() {
		return lspc_rule_created_date;
	}
	public void setLspc_rule_created_date(Date lspc_rule_created_date) {
		this.lspc_rule_created_date = lspc_rule_created_date;
	}
	public Integer getLspc_rule_created_by() {
		return lspc_rule_created_by;
	}
	public void setLspc_rule_created_by(Integer lspc_rule_created_by) {
		this.lspc_rule_created_by = lspc_rule_created_by;
	}
	public Date getLspc_rule_modified_date() {
		return lspc_rule_modified_date;
	}
	public void setLspc_rule_modified_date(Date lspc_rule_modified_date) {
		this.lspc_rule_modified_date = lspc_rule_modified_date;
	}
	public Integer getLspc_rule_modified_by() {
		return lspc_rule_modified_by;
	}
	public void setLspc_rule_modified_by(Integer lspc_rule_modified_by) {
		this.lspc_rule_modified_by = lspc_rule_modified_by;
	}
	public Integer getLspc_rule_flag_active() {
		return lspc_rule_flag_active;
	}
	public void setLspc_rule_flag_active(Integer lspc_rule_flag_active) {
		this.lspc_rule_flag_active = lspc_rule_flag_active;
	}
	public String getLspc_rule_prerequisites() {
		return lspc_rule_prerequisites;
	}
	public void setLspc_rule_prerequisites(String lspc_rule_prerequisites) {
		this.lspc_rule_prerequisites = lspc_rule_prerequisites;
	}
	public Integer getLspc_rule_category_id() {
		return lspc_rule_category_id;
	}
	public void setLspc_rule_category_id(Integer lspc_rule_category_id) {
		this.lspc_rule_category_id = lspc_rule_category_id;
	}
	
}
