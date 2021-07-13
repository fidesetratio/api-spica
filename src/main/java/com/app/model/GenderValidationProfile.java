package com.app.model;

import java.io.Serializable;

public class GenderValidationProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1930979093452872829L;
	
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Integer mspe_sex;
	
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
	public Integer getMspe_sex() {
		return mspe_sex;
	}
	public void setMspe_sex(Integer mspe_sex) {
		this.mspe_sex = mspe_sex;
	}

}
