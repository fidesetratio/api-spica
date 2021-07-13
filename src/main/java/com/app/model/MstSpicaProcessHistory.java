package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MstSpicaProcessHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2313590621248481712L;
	
	private String no_temp;
	private String reg_spaj;
	private String app_name;
	private String process_name;
	private String description;
	private String error_message;
	private Integer process_number;
	private Date created_date;
	
	public String getNo_temp() {
		return no_temp;
	}
	public void setNo_temp(String no_temp) {
		this.no_temp = no_temp;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getProcess_name() {
		return process_name;
	}
	public void setProcess_name(String process_name) {
		this.process_name = process_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	public Integer getProcess_number() {
		return process_number;
	}
	public void setProcess_number(Integer process_number) {
		this.process_number = process_number;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

}
