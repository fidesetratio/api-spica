package com.app.model;

import java.io.Serializable;

public class LstSpicaPfParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1977287712044519322L;
	
	private String lspc_pf_name;
	private Integer lspc_pf_parameter_id;
	private String lspc_pf_parameter_type;
	private String lspc_pf_parameter_description;
	
	public String getLspc_pf_name() {
		return lspc_pf_name;
	}
	public void setLspc_pf_name(String lspc_pf_name) {
		this.lspc_pf_name = lspc_pf_name;
	}
	public Integer getLspc_pf_parameter_id() {
		return lspc_pf_parameter_id;
	}
	public void setLspc_pf_parameter_id(Integer lspc_pf_parameter_id) {
		this.lspc_pf_parameter_id = lspc_pf_parameter_id;
	}
	public String getLspc_pf_parameter_type() {
		return lspc_pf_parameter_type;
	}
	public void setLspc_pf_parameter_type(String lspc_pf_parameter_type) {
		this.lspc_pf_parameter_type = lspc_pf_parameter_type;
	}
	public String getLspc_pf_parameter_description() {
		return lspc_pf_parameter_description;
	}
	public void setLspc_pf_parameter_description(String lspc_pf_parameter_description) {
		this.lspc_pf_parameter_description = lspc_pf_parameter_description;
	}

}
