package com.app.request;

import org.springframework.stereotype.Component;

@Component
public class HouseInquiryRequest {

	private String accountNo;

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
}
