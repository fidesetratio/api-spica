package com.app.model;

import java.io.Serializable;

public class HighRiskCountriesPp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8823029437197805219L;
	
	private String kewarganegaraan_pp;
	private String negara_tpt_tinggal_pp;
	
	public String getKewarganegaraan_pp() {
		return kewarganegaraan_pp;
	}
	public void setKewarganegaraan_pp(String kewarganegaraan_pp) {
		this.kewarganegaraan_pp = kewarganegaraan_pp;
	}
	public String getNegara_tpt_tinggal_pp() {
		return negara_tpt_tinggal_pp;
	}
	public void setNegara_tpt_tinggal_pp(String negara_tpt_tinggal_pp) {
		this.negara_tpt_tinggal_pp = negara_tpt_tinggal_pp;
	}

}
