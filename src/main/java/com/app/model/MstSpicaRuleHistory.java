package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstSpicaRuleHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4684191532160841197L;
	
	private Integer lspc_form_id;
	private String mspc_form_hist_primary_att;
	private String lspc_rule_name;
	private String lspc_rule_expression;
	private String mspc_rule_hist_result;
	private Date mspc_rule_hist_created_date;
	private Integer mspc_rule_hist_created_by;
	private Integer lspc_rule_category_id;
	private Integer mspc_form_hist_process_number;
	
	public Integer getLspc_form_id() {
		return lspc_form_id;
	}
	public void setLspc_form_id(Integer lspc_form_id) {
		this.lspc_form_id = lspc_form_id;
	}
	public String getMspc_form_hist_primary_att() {
		return mspc_form_hist_primary_att;
	}
	public void setMspc_form_hist_primary_att(String mspc_form_hist_primary_att) {
		this.mspc_form_hist_primary_att = mspc_form_hist_primary_att;
	}
	public String getLspc_rule_name() {
		return lspc_rule_name;
	}
	public void setLspc_rule_name(String lspc_rule_name) {
		this.lspc_rule_name = lspc_rule_name;
	}
	public String getLspc_rule_expression() {
		return lspc_rule_expression;
	}
	public void setLspc_rule_expression(String lspc_rule_expression) {
		this.lspc_rule_expression = lspc_rule_expression;
	}
	public String getMspc_rule_hist_result() {
		return mspc_rule_hist_result;
	}
	public void setMspc_rule_hist_result(String mspc_rule_hist_result) {
		this.mspc_rule_hist_result = mspc_rule_hist_result;
	}
	public Date getMspc_rule_hist_created_date() {
		return mspc_rule_hist_created_date;
	}
	public void setMspc_rule_hist_created_date(Date mspc_rule_hist_created_date) {
		this.mspc_rule_hist_created_date = mspc_rule_hist_created_date;
	}
	public Integer getMspc_rule_hist_created_by() {
		return mspc_rule_hist_created_by;
	}
	public void setMspc_rule_hist_created_by(Integer mspc_rule_hist_created_by) {
		this.mspc_rule_hist_created_by = mspc_rule_hist_created_by;
	}
	public Integer getLspc_rule_category_id() {
		return lspc_rule_category_id;
	}
	public void setLspc_rule_category_id(Integer lspc_rule_category_id) {
		this.lspc_rule_category_id = lspc_rule_category_id;
	}
	public Integer getMspc_form_hist_process_number() {
		return mspc_form_hist_process_number;
	}
	public void setMspc_form_hist_process_number(Integer mspc_form_hist_process_number) {
		this.mspc_form_hist_process_number = mspc_form_hist_process_number;
	}
	
}
