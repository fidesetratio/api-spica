package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstSpicaFormHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5750846530496569301L;
	
	private Integer lspc_form_id;
	private String mspc_form_hist_primary_att;
	private Date mspc_form_hist_time_started;
	private Date mspc_form_hist_time_finished;
	private Date mspc_form_hist_created_date;
	private Integer mspc_form_hist_created_by;
	private String mspc_form_hist_result;
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
	public Date getMspc_form_hist_time_started() {
		return mspc_form_hist_time_started;
	}
	public void setMspc_form_hist_time_started(Date mspc_form_hist_time_started) {
		this.mspc_form_hist_time_started = mspc_form_hist_time_started;
	}
	public Date getMspc_form_hist_time_finished() {
		return mspc_form_hist_time_finished;
	}
	public void setMspc_form_hist_time_finished(Date mspc_form_hist_time_finished) {
		this.mspc_form_hist_time_finished = mspc_form_hist_time_finished;
	}
	public Date getMspc_form_hist_created_date() {
		return mspc_form_hist_created_date;
	}
	public void setMspc_form_hist_created_date(Date mspc_form_hist_created_date) {
		this.mspc_form_hist_created_date = mspc_form_hist_created_date;
	}
	public Integer getMspc_form_hist_created_by() {
		return mspc_form_hist_created_by;
	}
	public void setMspc_form_hist_created_by(Integer mspc_form_hist_created_by) {
		this.mspc_form_hist_created_by = mspc_form_hist_created_by;
	}
	public String getMspc_form_hist_result() {
		return mspc_form_hist_result;
	}
	public void setMspc_form_hist_result(String mspc_form_hist_result) {
		this.mspc_form_hist_result = mspc_form_hist_result;
	}
	public Integer getMspc_form_hist_process_number() {
		return mspc_form_hist_process_number;
	}
	public void setMspc_form_hist_process_number(Integer mspc_form_hist_process_number) {
		this.mspc_form_hist_process_number = mspc_form_hist_process_number;
	}
}
