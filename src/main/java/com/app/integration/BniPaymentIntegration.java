package com.app.integration;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.app.enumer.ConfigConstant;
import com.app.enumer.Environment;
import com.app.model.HouseInquiry;
import com.app.model.InterBankInquiry;
import com.app.request.HouseInquiryRequest;
import com.app.request.InterBankInquiryRequest;
import com.app.utils.ObjectConverter;
import com.app.utils.Util;

@Component
public class BniPaymentIntegration {

	/** GET INHOUSE INQUIRY **/
	public static Map<String, Object> getInHouseInquiryMap(String access_token, HouseInquiryRequest houseInquiryRequest) {

		JSONObject request = new JSONObject();
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		HouseInquiry objHouseInquiry = new HouseInquiry();
		Map<String, Object> map = new HashMap<String, Object>();

		final String url = Environment.GET_INHOUSE_INQUIRY.getUrl() + "?access_token="+ access_token;
		try {

			HttpHeaders headers = new HttpHeaders();
//			headers.add("x-api-key", JwtConstant.API_KEY.getValue());
			headers.add("x-api-key", ConfigConstant.xApiKey.getValue());
			headers.add("Content-Type", "application/json");

			request.put("clientId", Util.clientIdEncode());
			request.put("accountNo", houseInquiryRequest.getAccountNo());
			request.put("signature", Util.generateJWTToken(request.toString()));

			HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
			map.put("request_url", request.toString());
			map.put("url", url);

			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				try {
					JSONObject jsonSrc = new JSONObject(response.getBody().toString());
					JSONObject jsonObj = jsonSrc.getJSONObject("getInHouseInquiryResponse");
					JSONObject param = jsonObj.getJSONObject("parameters");

					objHouseInquiry = ObjectConverter.houseInquiryConverter(param);

					map.put("response_url", response.getBody().toString());
					map.put("inHouseInquiry", objHouseInquiry);
					map.put("httpStatus", HttpStatus.OK);

				} catch (JSONException e) {

					map.put("response_url", response.getBody().toString());
					map.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);
					throw new RuntimeException("JSONException occurred");
				}
			}

		} catch (HttpClientErrorException httpClientErrorException) {
			JSONObject jsonSrc = new JSONObject(httpClientErrorException.getResponseBodyAsString());
			JSONObject jsonObj = jsonSrc.getJSONObject("getInHouseInquiryResponse");
			JSONObject param = jsonObj.getJSONObject("parameters");

			objHouseInquiry = ObjectConverter.houseInquiryConverter(param);

			map.put("response_url", httpClientErrorException.getResponseBodyAsString());
			map.put("inHouseInquiry", objHouseInquiry);
			map.put("httpStatus", HttpStatus.BAD_REQUEST);

		} catch (HttpServerErrorException httpServerErrorException) {
			map.put("response_url", httpServerErrorException.getResponseBodyAsString());
			map.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			map.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return map;
	}

	/** GET INTERBANK INQUIRY **/
	public static Map<String, Object> getInterBankInquiryMap(String access_token, InterBankInquiryRequest interBankReq) {

		JSONObject request = new JSONObject();
		ResponseEntity<String> response = null;
		RestTemplate restTemplate = new RestTemplate();
		InterBankInquiry objInterBank = new InterBankInquiry();
		Map<String, Object> map = new HashMap<String, Object>();

//		final String url = Environment.DEV.getUrl() + Environment.GET_INTERBANK_INQUIRY.getUrl() + "?access_token="+ access_token;
		
		final String url = Environment.GET_INTERBANK_INQUIRY.getUrl() + "?access_token="+ access_token;

		try {
			HttpHeaders headers = new HttpHeaders();
//			headers.add("x-api-key", JwtConstant.API_KEY.getValue());
			headers.add("x-api-key", ConfigConstant.xApiKey.getValue());
			headers.add("Content-Type", "application/json");

			request.put("clientId", Util.clientIdEncode());
			request.put("accountNum", ConfigConstant.accountNumber_Interbank.getValue());
			request.put("destinationBankCode", interBankReq.getDestinationBankCode());
			request.put("destinationAccountNum", interBankReq.getDestinationAccountNum());
			request.put("customerReferenceNumber", Util.customerReferenceGenerator());
			request.put("signature", Util.generateJWTToken(request.toString()));

			HttpEntity<String> entity = new HttpEntity<>(request.toString(), headers);
			map.put("request_url", request.toString());
			map.put("url", url);

			response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				try {
					JSONObject jsonSrc = new JSONObject(response.getBody().toString());
					JSONObject jsonObj = jsonSrc.getJSONObject("getInterbankInquiryResponse");
					JSONObject param = jsonObj.getJSONObject("parameters");
					objInterBank = ObjectConverter.interBankInquiryConverter(param);

					map.put("response_url", response.getBody().toString());
					map.put("interBankInquiry", objInterBank);
					map.put("httpStatus", HttpStatus.OK);

				} catch (JSONException e) {
					map.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);
					throw new RuntimeException("JSONException occurred");
				}
			}

		} catch (HttpClientErrorException httpClientErrorException) {
			JSONObject jsonSrc = new JSONObject(httpClientErrorException.getResponseBodyAsString());
			JSONObject jsonObj = jsonSrc.getJSONObject("getInterbankInquiryResponse");
			JSONObject param = jsonObj.getJSONObject("parameters");
			objInterBank = ObjectConverter.interBankInquiryConverter(param);

			map.put("response_url", httpClientErrorException.getResponseBodyAsString());
			map.put("interBankInquiry", objInterBank);
			map.put("httpStatus", HttpStatus.BAD_REQUEST);

		} catch (HttpServerErrorException httpServerErrorException) {
			map.put("response_url", httpServerErrorException.getResponseBodyAsString());
			map.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (Exception e) {
			map.put("httpStatus", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return map;
	}


}
