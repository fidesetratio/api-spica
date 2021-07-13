package com.app.model;

import java.io.Serializable;

public class RekeningBeneficiary implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3463928577467292971L;
	
	private String mrc_nama;
	private String mrc_no_ac;
	private String kode_bank;
	
	public String getMrc_nama() {
		return mrc_nama;
	}
	public void setMrc_nama(String mrc_nama) {
		this.mrc_nama = mrc_nama;
	}
	public String getMrc_no_ac() {
		return mrc_no_ac;
	}
	public void setMrc_no_ac(String mrc_no_ac) {
		this.mrc_no_ac = mrc_no_ac;
	}
	public String getKode_bank() {
		return kode_bank;
	}
	public void setKode_bank(String kode_bank) {
		this.kode_bank = kode_bank;
	}
}
