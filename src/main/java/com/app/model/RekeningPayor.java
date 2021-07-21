package com.app.model;

import java.io.Serializable;

public class RekeningPayor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3463928577467292971L;
	
	private String mar_holder;
	private String mar_acc_no;
	private String lsbp_id;
	private String kode_bank;
	private Integer mste_flag_cc;
	
	public String getMar_holder() {
		return mar_holder;
	}
	public void setMar_holder(String mar_holder) {
		this.mar_holder = mar_holder;
	}
	public String getMar_acc_no() {
		return mar_acc_no;
	}
	public void setMar_acc_no(String mar_acc_no) {
		this.mar_acc_no = mar_acc_no;
	}
	public String getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(String lsbp_id) {
		this.lsbp_id = lsbp_id;
	}
	public String getKode_bank() {
		return kode_bank;
	}
	public void setKode_bank(String kode_bank) {
		this.kode_bank = kode_bank;
	}
	public Integer getMste_flag_cc() {
		return mste_flag_cc;
	}
	public void setMste_flag_cc(Integer mste_flag_cc) {
		this.mste_flag_cc = mste_flag_cc;
	}
}
