package com.app.model;

import java.io.Serializable;

public class LstValidationAgeParticipant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2361510713122266054L;
	
	private Integer rider_id;
	private Integer lsre_id;
	private Integer plan;
	private Integer insured_age_from_flag;
	private Integer insured_age_from;
	private Integer insured_age_to;
	
	public Integer getRider_id() {
		return rider_id;
	}
	public void setRider_id(Integer rider_id) {
		this.rider_id = rider_id;
	}
	public Integer getLsre_id() {
		return lsre_id;
	}
	public void setLsre_id(Integer lsre_id) {
		this.lsre_id = lsre_id;
	}
	public Integer getPlan() {
		return plan;
	}
	public void setPlan(Integer plan) {
		this.plan = plan;
	}
	public Integer getInsured_age_from_flag() {
		return insured_age_from_flag;
	}
	public void setInsured_age_from_flag(Integer insured_age_from_flag) {
		this.insured_age_from_flag = insured_age_from_flag;
	}
	public Integer getInsured_age_from() {
		return insured_age_from;
	}
	public void setInsured_age_from(Integer insured_age_from) {
		this.insured_age_from = insured_age_from;
	}
	public Integer getInsured_age_to() {
		return insured_age_to;
	}
	public void setInsured_age_to(Integer insured_age_to) {
		this.insured_age_to = insured_age_to;
	}

}
