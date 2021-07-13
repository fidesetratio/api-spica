package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstSpicaConditionHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -691931983945890058L;
	
	private Integer lspc_form_id;
	private String mspc_form_hist_primary_att;
	private String lspc_rule_name;
	private String lspc_condition_name;
	private String lspc_condition_expression;
	private String mspc_condition_hist_result;
	private Date mspc_condition_hist_created;
	private Integer mspc_condition_hist_created_by;
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
	public String getMspc_condition_hist_result() {
		return mspc_condition_hist_result;
	}
	public void setMspc_condition_hist_result(String mspc_condition_hist_result) {
		this.mspc_condition_hist_result = mspc_condition_hist_result;
	}
	public Date getMspc_condition_hist_created() {
		return mspc_condition_hist_created;
	}
	public void setMspc_condition_hist_created(Date mspc_condition_hist_created) {
		this.mspc_condition_hist_created = mspc_condition_hist_created;
	}
	public Integer getMspc_condition_hist_created_by() {
		return mspc_condition_hist_created_by;
	}
	public void setMspc_condition_hist_created_by(Integer mspc_condition_hist_created_by) {
		this.mspc_condition_hist_created_by = mspc_condition_hist_created_by;
	}
	public Integer getMspc_form_hist_process_number() {
		return mspc_form_hist_process_number;
	}
	public void setMspc_form_hist_process_number(Integer mspc_form_hist_process_number) {
		this.mspc_form_hist_process_number = mspc_form_hist_process_number;
	}
	
}
