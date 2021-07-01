package com.app.model;

import java.io.Serializable;

public class MstWfDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2145470418612432035L;
	
	private Integer mswf_parent_doc_id;
	private Integer mswf_doc_id;
	
	public Integer getMswf_parent_doc_id() {
		return mswf_parent_doc_id;
	}
	public void setMswf_parent_doc_id(Integer mswf_parent_doc_id) {
		this.mswf_parent_doc_id = mswf_parent_doc_id;
	}
	public Integer getMswf_doc_id() {
		return mswf_doc_id;
	}
	public void setMswf_doc_id(Integer mswf_doc_id) {
		this.mswf_doc_id = mswf_doc_id;
	}
	
}
