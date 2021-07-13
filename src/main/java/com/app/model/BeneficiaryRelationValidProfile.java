package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class BeneficiaryRelationValidProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1761562402688157126L;
	
	private Date msaw_birth;
	private String lsre_relation;
	private Date mspe_date_birth;
	private Integer mspe_sts_mrt;
	
	public Date getMsaw_birth() {
		return msaw_birth;
	}
	public void setMsaw_birth(Date msaw_birth) {
		this.msaw_birth = msaw_birth;
	}
	public String getLsre_relation() {
		return lsre_relation;
	}
	public void setLsre_relation(String lsre_relation) {
		this.lsre_relation = lsre_relation;
	}
	public Date getMspe_date_birth() {
		return mspe_date_birth;
	}
	public void setMspe_date_birth(Date mspe_date_birth) {
		this.mspe_date_birth = mspe_date_birth;
	}
	public Integer getMspe_sts_mrt() {
		return mspe_sts_mrt;
	}
	public void setMspe_sts_mrt(Integer mspe_sts_mrt) {
		this.mspe_sts_mrt = mspe_sts_mrt;
	}
}
