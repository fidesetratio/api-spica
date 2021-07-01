package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class TopUpSingleValidationProfile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6589202837020530329L;
	
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String lku_id;
	private BigDecimal mu_jlh_premi;
	
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
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public BigDecimal getMu_jlh_premi() {
		return mu_jlh_premi;
	}
	public void setMu_jlh_premi(BigDecimal mu_jlh_premi) {
		this.mu_jlh_premi = mu_jlh_premi;
	}
}
