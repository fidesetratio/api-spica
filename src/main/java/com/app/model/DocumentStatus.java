package com.app.model;

import java.io.Serializable;

public class DocumentStatus implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1924042133482497410L;
	
	private Integer lspd_id;
	private Integer lssp_id;
	private Integer lssa_id;
	
	public Integer getLspd_id() {
		return lspd_id;
	}
	public void setLspd_id(Integer lspd_id) {
		this.lspd_id = lspd_id;
	}
	public Integer getLssp_id() {
		return lssp_id;
	}
	public void setLssp_id(Integer lssp_id) {
		this.lssp_id = lssp_id;
	}
	public Integer getLssa_id() {
		return lssa_id;
	}
	public void setLssa_id(Integer lssa_id) {
		this.lssa_id = lssa_id;
	}

}
