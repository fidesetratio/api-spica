package com.app.utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.app.model.HouseInquiry;
import com.app.model.InterBankInquiry;

@Component
public class ObjectConverter {

	public static HouseInquiry houseInquiryConverter(JSONObject jsonObj) throws JSONException {
		HouseInquiry houseInquiry = new HouseInquiry();
		houseInquiry.setResponseCode(jsonObj.isNull("responseCode") ? null : jsonObj.get("responseCode").toString());
		houseInquiry.setResponseMessage(jsonObj.isNull("responseMessage") ? null : jsonObj.get("responseMessage").toString());
		houseInquiry.setResponseTimestamp(jsonObj.isNull("responseTimestamp") ? null : jsonObj.get("responseTimestamp").toString());
		houseInquiry.setAccountCurrency(jsonObj.isNull("accountCurrency") ? null : jsonObj.get("accountCurrency").toString());
		houseInquiry.setAccountNumber(jsonObj.isNull("accountNumber") ? null : jsonObj.get("accountNumber").toString());
		houseInquiry.setAccountStatus(jsonObj.isNull("accountStatus") ? null : jsonObj.get("accountStatus").toString());
		houseInquiry.setAccountType(jsonObj.isNull("accountType") ? null : jsonObj.get("accountType").toString());
		houseInquiry.setCustomerName(jsonObj.isNull("customerName") ? null : jsonObj.get("customerName").toString());
		return houseInquiry;
	}

	public static InterBankInquiry interBankInquiryConverter(JSONObject jsonObj) throws JSONException {
		InterBankInquiry interBankInquiry = new InterBankInquiry();
		interBankInquiry.setResponseCode(jsonObj.isNull("responseCode") ? null : jsonObj.get("responseCode").toString());
		interBankInquiry.setResponseMessage(jsonObj.isNull("responseMessage") ? null : jsonObj.get("responseMessage").toString());
		interBankInquiry.setResponseTimestamp(jsonObj.isNull("responseTimestamp") ? null : jsonObj.get("responseTimestamp").toString());
		interBankInquiry.setDestinationAccountNum(jsonObj.isNull("destinationAccountNum") ? null : jsonObj.get("destinationAccountNum").toString());
		interBankInquiry.setDestinationAccountName(jsonObj.isNull("destinationAccountName") ? null : jsonObj.get("destinationAccountName").toString());
		interBankInquiry.setDestinationBankName(jsonObj.isNull("destinationBankName") ? null : jsonObj.get("destinationBankName").toString());
		interBankInquiry.setRetrievalReffNum(jsonObj.isNull("retrievalReffNum") ? null : jsonObj.get("retrievalReffNum").toString());
		return interBankInquiry;
	}

}
