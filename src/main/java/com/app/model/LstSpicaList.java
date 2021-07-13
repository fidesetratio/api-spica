package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class LstSpicaList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5132692287769085429L;
	
	private String lspc_list_name;
	private String lspc_list_description;
	private String lspc_list_source;
	private String lspc_list_field_name;
	private String lspc_list_field_type;
	private Date lspc_list_created_date;
	private Integer lspc_list_created_by;
	private Date lspc_list_modified_date;
	private Integer lspc_list_modified_by;
	private Integer lspc_list_flag_active;
	
	public String getLspc_list_name() {
		return lspc_list_name;
	}
	public void setLspc_list_name(String lspc_list_name) {
		this.lspc_list_name = lspc_list_name;
	}
	public String getLspc_list_description() {
		return lspc_list_description;
	}
	public void setLspc_list_description(String lspc_list_description) {
		this.lspc_list_description = lspc_list_description;
	}
	public String getLspc_list_source() {
		return lspc_list_source;
	}
	public void setLspc_list_source(String lspc_list_source) {
		this.lspc_list_source = lspc_list_source;
	}
	public String getLspc_list_field_name() {
		return lspc_list_field_name;
	}
	public void setLspc_list_field_name(String lspc_list_field_name) {
		this.lspc_list_field_name = lspc_list_field_name;
	}
	public String getLspc_list_field_type() {
		return lspc_list_field_type;
	}
	public void setLspc_list_field_type(String lspc_list_field_type) {
		this.lspc_list_field_type = lspc_list_field_type;
	}
	public Date getLspc_list_created_date() {
		return lspc_list_created_date;
	}
	public void setLspc_list_created_date(Date lspc_list_created_date) {
		this.lspc_list_created_date = lspc_list_created_date;
	}
	public Integer getLspc_list_created_by() {
		return lspc_list_created_by;
	}
	public void setLspc_list_created_by(Integer lspc_list_created_by) {
		this.lspc_list_created_by = lspc_list_created_by;
	}
	public Date getLspc_list_modified_date() {
		return lspc_list_modified_date;
	}
	public void setLspc_list_modified_date(Date lspc_list_modified_date) {
		this.lspc_list_modified_date = lspc_list_modified_date;
	}
	public Integer getLspc_list_modified_by() {
		return lspc_list_modified_by;
	}
	public void setLspc_list_modified_by(Integer lspc_list_modified_by) {
		this.lspc_list_modified_by = lspc_list_modified_by;
	}
	public Integer getLspc_list_flag_active() {
		return lspc_list_flag_active;
	}
	public void setLspc_list_flag_active(Integer lspc_list_flag_active) {
		this.lspc_list_flag_active = lspc_list_flag_active;
	}

}
