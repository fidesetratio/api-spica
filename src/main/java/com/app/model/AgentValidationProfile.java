package com.app.model;

import java.io.Serializable;

public class AgentValidationProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5811541518989080029L;
	
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Integer id_jal;
	private String msag_id;
	private String lrb_id;
	private String reff_id;
	private String mspo_ao;
	
	public Integer getLsbs_id() {
		return lsbs_id;
	}
	public void setLsbs_id(Integer lsbs_id) {
		this.lsbs_id = lsbs_id;
	}
	public Integer getLsdbs_number() {
		return lsdbs_number;
	}
	public void setLsdbs_number(Integer lsdbs_number) {
		this.lsdbs_number = lsdbs_number;
	}
	public Integer getId_jal() {
		return id_jal;
	}
	public void setId_jal(Integer id_jal) {
		this.id_jal = id_jal;
	}
	public String getMsag_id() {
		return msag_id;
	}
	public void setMsag_id(String msag_id) {
		this.msag_id = msag_id;
	}
	public String getLrb_id() {
		return lrb_id;
	}
	public void setLrb_id(String lrb_id) {
		this.lrb_id = lrb_id;
	}
	public String getReff_id() {
		return reff_id;
	}
	public void setReff_id(String reff_id) {
		this.reff_id = reff_id;
	}
	public String getMspo_ao() {
		return mspo_ao;
	}
	public void setMspo_ao(String mspo_ao) {
		this.mspo_ao = mspo_ao;
	}
}
