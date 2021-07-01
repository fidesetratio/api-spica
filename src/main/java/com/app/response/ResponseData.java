package com.app.response;

import java.io.Serializable;
import java.util.HashMap;

public class ResponseData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8817496587771869531L;
	private Boolean error;
	private String message;
	private HashMap<String, Object> data;

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
}

