package com.app.request;

public class InterBankInquiryRequest {

	private String destinationBankCode;
	private String destinationAccountNum;
	
	public String getDestinationBankCode() {
		return destinationBankCode;
	}
	public void setDestinationBankCode(String destinationBankCode) {
		this.destinationBankCode = destinationBankCode;
	}
	public String getDestinationAccountNum() {
		return destinationAccountNum;
	}
	public void setDestinationAccountNum(String destinationAccountNum) {
		this.destinationAccountNum = destinationAccountNum;
	}

	
}
