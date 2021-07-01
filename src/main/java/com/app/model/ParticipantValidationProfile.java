package com.app.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ParticipantValidationProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7095102404557280561L;
	
	private Date mspo_beg_date;
	private Integer lsre_id;
	private Date tgl_lahir;
	private Integer kelamin;
	private BigDecimal premi;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	
	public Date getMspo_beg_date() {
		return mspo_beg_date;
	}
	public void setMspo_beg_date(Date mspo_beg_date) {
		this.mspo_beg_date = mspo_beg_date;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public Date getTgl_lahir() {
		return tgl_lahir;
	}
	public void setTgl_lahir(Date tgl_lahir) {
		this.tgl_lahir = tgl_lahir;
	}
	public Integer getKelamin() {
		return kelamin;
	}
	public void setKelamin(Integer kelamin) {
		this.kelamin = kelamin;
	}
	public BigDecimal getPremi() {
		return premi;
	}
	public void setPremi(BigDecimal premi) {
		this.premi = premi;
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

}
