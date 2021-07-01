package com.app.model;

import org.springframework.stereotype.Component;

@Component
public class InterBankInquiry {

	private String responseCode;
	private String responseMessage;
	private String responseTimestamp;
	private String destinationAccountNum;
	private String destinationAccountName;
	private String destinationBankName;
	private String retrievalReffNum;
	
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
	public String getDestinationAccountNum() {
		return destinationAccountNum;
	}
	public void setDestinationAccountNum(String destinationAccountNum) {
		this.destinationAccountNum = destinationAccountNum;
	}
	public String getDestinationAccountName() {
		return destinationAccountName;
	}
	public void setDestinationAccountName(String destinationAccountName) {
		this.destinationAccountName = destinationAccountName;
	}
	public String getDestinationBankName() {
		return destinationBankName;
	}
	public void setDestinationBankName(String destinationBankName) {
		this.destinationBankName = destinationBankName;
	}
	public String getRetrievalReffNum() {
		return retrievalReffNum;
	}
	public void setRetrievalReffNum(String retrievalReffNum) {
		this.retrievalReffNum = retrievalReffNum;
	}
	
}
