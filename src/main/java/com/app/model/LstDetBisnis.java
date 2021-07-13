package com.app.model;

import java.io.Serializable;

public class LstDetBisnis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4756844697052654714L;
	
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private String lsdbs_name;
	private Integer flag_clean;
	
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
	public String getLsdbs_name() {
		return lsdbs_name;
	}
	public void setLsdbs_name(String lsdbs_name) {
		this.lsdbs_name = lsdbs_name;
	}
	public Integer getFlag_clean() {
		return flag_clean;
	}
	public void setFlag_clean(Integer flag_clean) {
		this.flag_clean = flag_clean;
	}	
}
