package com.app.model;

import java.io.Serializable;

public class MstRekeningCustomer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -728104018007364262L;
	
	private Integer lsbp_id;
	private String mrc_no_acc;
	private String mrc_atas_nama;
	
	public Integer getLsbp_id() {
		return lsbp_id;
	}
	public void setLsbp_id(Integer lsbp_id) {
		this.lsbp_id = lsbp_id;
	}
	public String getMrc_no_acc() {
		return mrc_no_acc;
	}
	public void setMrc_no_acc(String mrc_no_acc) {
		this.mrc_no_acc = mrc_no_acc;
	}
	public String getMrc_atas_nama() {
		return mrc_atas_nama;
	}
	public void setMrc_atas_nama(String mrc_atas_nama) {
		this.mrc_atas_nama = mrc_atas_nama;
	}

}
