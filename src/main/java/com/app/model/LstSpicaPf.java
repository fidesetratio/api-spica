package com.app.model;

import java.io.Serializable;

public class LstSpicaPf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6258768085661791655L;
	
	private String lspc_pf_name;
	private String lspc_pf_type;
	private Integer lspc_pf_flag_active;
	
	public String getLspc_pf_name() {
		return lspc_pf_name;
	}
	public void setLspc_pf_name(String lspc_pf_name) {
		this.lspc_pf_name = lspc_pf_name;
	}
	public String getLspc_pf_type() {
		return lspc_pf_type;
	}
	public void setLspc_pf_type(String lspc_pf_type) {
		this.lspc_pf_type = lspc_pf_type;
	}
	public Integer getLspc_pf_flag_active() {
		return lspc_pf_flag_active;
	}
	public void setLspc_pf_flag_active(Integer lspc_pf_flag_active) {
		this.lspc_pf_flag_active = lspc_pf_flag_active;
	}

}
