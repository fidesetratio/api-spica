package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class ProductAgeValidationProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1865927481093172147L;
	
	private Date mspe_date_birth;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Date mspr_beg_date;
	
	public Date getMspe_date_birth() {
		return mspe_date_birth;
	}
	public void setMspe_date_birth(Date mspe_date_birth) {
		this.mspe_date_birth = mspe_date_birth;
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
	public Date getMspr_beg_date() {
		return mspr_beg_date;
	}
	public void setMspr_beg_date(Date mspr_beg_date) {
		this.mspr_beg_date = mspr_beg_date;
	}

}
