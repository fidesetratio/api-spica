package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstSpicaVariableHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3005439027761180597L;
	
	private Integer lspc_form_id;
	private String mspc_form_hist_primary_att;
	private String lspc_variable_name;
	private String lspc_variable_expression;
	private String mspc_variable_hist_value;
	private Date mspc_variable_hist_created;
	private Integer mspc_variable_hist_created_by;
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
	public String getLspc_variable_name() {
		return lspc_variable_name;
	}
	public void setLspc_variable_name(String lspc_variable_name) {
		this.lspc_variable_name = lspc_variable_name;
	}
	public String getLspc_variable_expression() {
		return lspc_variable_expression;
	}
	public void setLspc_variable_expression(String lspc_variable_expression) {
		this.lspc_variable_expression = lspc_variable_expression;
	}
	public String getMspc_variable_hist_value() {
		return mspc_variable_hist_value;
	}
	public void setMspc_variable_hist_value(String mspc_variable_hist_value) {
		this.mspc_variable_hist_value = mspc_variable_hist_value;
	}
	public Date getMspc_variable_hist_created() {
		return mspc_variable_hist_created;
	}
	public void setMspc_variable_hist_created(Date mspc_variable_hist_created) {
		this.mspc_variable_hist_created = mspc_variable_hist_created;
	}
	public Integer getMspc_variable_hist_created_by() {
		return mspc_variable_hist_created_by;
	}
	public void setMspc_variable_hist_created_by(Integer mspc_variable_hist_created_by) {
		this.mspc_variable_hist_created_by = mspc_variable_hist_created_by;
	}
	public Integer getMspc_form_hist_process_number() {
		return mspc_form_hist_process_number;
	}
	public void setMspc_form_hist_process_number(Integer mspc_form_hist_process_number) {
		this.mspc_form_hist_process_number = mspc_form_hist_process_number;
	}

}
