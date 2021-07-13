package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PremiumValidationProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4108405800468572658L;
	
	private Integer mste_age;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String lku_id;
	private BigDecimal mspr_tsi;
	private BigDecimal mspr_premium;
	private Integer lscb_kali;
	private BigDecimal mu_jlh_premi;
	private BigDecimal lscb_kali_old;
	private BigDecimal lscb_kali_factor_tiga;
	private BigDecimal lscb_kali_factor_empat;
	
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}
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
	public BigDecimal getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(BigDecimal mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
	public BigDecimal getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(BigDecimal mspr_premium) {
		this.mspr_premium = mspr_premium;
	}
	public Integer getLscb_kali() {
		return lscb_kali;
	}
	public void setLscb_kali(Integer lscb_kali) {
		this.lscb_kali = lscb_kali;
	}
	public BigDecimal getMu_jlh_premi() {
		return mu_jlh_premi;
	}
	public void setMu_jlh_premi(BigDecimal mu_jlh_premi) {
		this.mu_jlh_premi = mu_jlh_premi;
	}
	public BigDecimal getLscb_kali_old() {
		return lscb_kali_old;
	}
	public void setLscb_kali_old(BigDecimal lscb_kali_old) {
		this.lscb_kali_old = lscb_kali_old;
	}
	public BigDecimal getLscb_kali_factor_tiga() {
		return lscb_kali_factor_tiga;
	}
	public void setLscb_kali_factor_tiga(BigDecimal lscb_kali_factor_tiga) {
		this.lscb_kali_factor_tiga = lscb_kali_factor_tiga;
	}
	public BigDecimal getLscb_kali_factor_empat() {
		return lscb_kali_factor_empat;
	}
	public void setLscb_kali_factor_empat(BigDecimal lscb_kali_factor_empat) {
		this.lscb_kali_factor_empat = lscb_kali_factor_empat;
	}
}
