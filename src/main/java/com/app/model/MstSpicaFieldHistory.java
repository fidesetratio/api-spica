package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstSpicaFieldHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1659328714898830440L;
	
	private Integer lspc_form_id;
	private String mspc_form_hist_primary_att;
	private String lspc_field_name;
	private String mspc_field_hist_value;
	private Date mspc_field_hist_created_date;
	private Integer mspc_field_hist_created_by;
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
	public String getLspc_field_name() {
		return lspc_field_name;
	}
	public void setLspc_field_name(String lspc_field_name) {
		this.lspc_field_name = lspc_field_name;
	}
	public String getMspc_field_hist_value() {
		return mspc_field_hist_value;
	}
	public void setMspc_field_hist_value(String mspc_field_hist_value) {
		this.mspc_field_hist_value = mspc_field_hist_value;
	}
	public Date getMspc_field_hist_created_date() {
		return mspc_field_hist_created_date;
	}
	public void setMspc_field_hist_created_date(Date mspc_field_hist_created_date) {
		this.mspc_field_hist_created_date = mspc_field_hist_created_date;
	}
	public Integer getMspc_field_hist_created_by() {
		return mspc_field_hist_created_by;
	}
	public void setMspc_field_hist_created_by(Integer mspc_field_hist_created_by) {
		this.mspc_field_hist_created_by = mspc_field_hist_created_by;
	}
	public Integer getMspc_form_hist_process_number() {
		return mspc_form_hist_process_number;
	}
	public void setMspc_form_hist_process_number(Integer mspc_form_hist_process_number) {
		this.mspc_form_hist_process_number = mspc_form_hist_process_number;
	}

}
