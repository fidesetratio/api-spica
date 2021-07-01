package com.app.response;

import org.springframework.stereotype.Component;

@Component
public class ApiResult {
	
	private String responseCode;
	private String responseMessage;
	private String responseTimestamp;
	private Object result;
	
	public ApiResult() {
	}
	
	public ApiResult(String responseCode, String responseMessage, String responseTimestamp) {
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.responseTimestamp = responseTimestamp;
	}

	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getResponseTimestamp() {
		return responseTimestamp;
	}
	public void setResponseTimestamp(String responseTimestamp) {
		this.responseTimestamp = responseTimestamp;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	
}
