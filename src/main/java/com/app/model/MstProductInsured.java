package com.app.model;

import java.io.Serializable;

public class MstProductInsured implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2960912526551189610L;
	private String lku_id;
	private Long mspr_premium;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Integer lscb_id;
	private Integer flag_gio;
	private Long mspr_tsi;
	
	public String getLku_id() {
		return lku_id;
	}
	public void setLku_id(String lku_id) {
		this.lku_id = lku_id;
	}
	public Long getMspr_premium() {
		return mspr_premium;
	}
	public void setMspr_premium(Long mspr_premium) {
		this.mspr_premium = mspr_premium;
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
	public Integer getLscb_id() {
		return lscb_id;
	}
	public void setLscb_id(Integer lscb_id) {
		this.lscb_id = lscb_id;
	}
	public Integer getFlag_gio() {
		return flag_gio;
	}
	public void setFlag_gio(Integer flag_gio) {
		this.flag_gio = flag_gio;
	}
	public Long getMspr_tsi() {
		return mspr_tsi;
	}
	public void setMspr_tsi(Long mspr_tsi) {
		this.mspr_tsi = mspr_tsi;
	}
}
