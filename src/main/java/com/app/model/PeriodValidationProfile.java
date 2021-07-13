package com.app.model;

import java.io.Serializable;
import java.util.Date;

public class PeriodValidationProfile implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6441636668549506296L;
	private Integer mste_age;
	private Integer lsbs_id;
	private Integer lsdbs_number;
	private Date mspr_beg_date;
	private Date mspr_end_date;
	private Integer mspr_ins_period;
	private Integer mspo_pay_period;
	private Integer mspo_installment;
	
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
	public Date getMspr_beg_date() {
		return mspr_beg_date;
	}
	public void setMspr_beg_date(Date mspr_beg_date) {
		this.mspr_beg_date = mspr_beg_date;
	}
	public Date getMspr_end_date() {
		return mspr_end_date;
	}
	public void setMspr_end_date(Date mspr_end_date) {
		this.mspr_end_date = mspr_end_date;
	}
	public Integer getMspr_ins_period() {
		return mspr_ins_period;
	}
	public void setMspr_ins_period(Integer mspr_ins_period) {
		this.mspr_ins_period = mspr_ins_period;
	}
	public Integer getMspo_pay_period() {
		return mspo_pay_period;
	}
	public void setMspo_pay_period(Integer mspo_pay_period) {
		this.mspo_pay_period = mspo_pay_period;
	}
	public Integer getMspo_installment() {
		return mspo_installment;
	}
	public void setMspo_installment(Integer mspo_installment) {
		this.mspo_installment = mspo_installment;
	}
}
