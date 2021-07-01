package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class MedicalTableProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7597873748484259276L;

	private String reg_spaj;
	private Integer lstb_id;
	private Integer flag_vip;
	private Integer mspo_age;
	private Date mste_beg_date;
	private Integer mste_age;
	private String lku_id;
	private Integer lsre_id;
	private Long sar;
	private Long sar_medis;
	
	public Integer getLstb_id() {
		return lstb_id;
	}
	public void setLstb_id(Integer lstb_id) {
		this.lstb_id = lstb_id;
	}
	public Integer getFlag_vip() {
		return flag_vip;
	}
	public void setFlag_vip(Integer flag_vip) {
		this.flag_vip = flag_vip;
	}
	public Integer getMspo_age() {
		return mspo_age;
	}
	public void setMspo_age(Integer mspo_age) {
		this.mspo_age = mspo_age;
	}
	public Date getMste_beg_date() {
		return mste_beg_date;
	}
	public void setMste_beg_date(Date mste_beg_date) {
		this.mste_beg_date = mste_beg_date;
	}
	public Integer getMste_age() {
		return mste_age;
	}
	public void setMste_age(Integer mste_age) {
		this.mste_age = mste_age;
	}
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Long getSar() {
		return sar;
	}
	public void setSar(Long sar) {
		this.sar = sar;
	}
	public Long getSar_medis() {
		return sar_medis;
	}
	public void setSar_medis(Long sar_medis) {
		this.sar_medis = sar_medis;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public String getReg_spaj() {
		return reg_spaj;
	}
	public void setReg_spaj(String reg_spaj) {
		this.reg_spaj = reg_spaj;
	}
}
