package com.app.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.integration.BniPaymentIntegration;
import com.app.request.HouseInquiryRequest;
import com.app.request.InterBankInquiryRequest;

@Service
public class BniPaymentService {

	/**
	 * GET INHOUSE INQUIRY
	 **/
	public static Map<String, Object> getInHouseInquiryMap(String token, HouseInquiryRequest houseInqReq) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map = BniPaymentIntegration.getInHouseInquiryMap(token, houseInqReq);
		
		return map;
	}

	/**
	 * GET INTERBANK INQUIRY
	 **/
	public static Map<String, Object> getInterBankInquiry(String token, InterBankInquiryRequest interBankReq) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map = BniPaymentIntegration.getInterBankInquiryMap(token, interBankReq);
		return map;
	}

}
