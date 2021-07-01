package com.app.utils;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.app.enumer.ConfigConstant;
import com.app.enumer.Environment;
import com.app.enumer.JwtConstant;

@Component
public class Util {
	
	public static String generateJWTToken(String payload) throws RuntimeException {

		String header = JwtConstant.HEADER.getValue();
		String base64UrlHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(header.getBytes());
		String base64UrlPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes());

		try {
//			String base64UrlSignature = hmacEncode(base64UrlHeader + "." + base64UrlPayload, JwtConstant.SECRET.getValue());
			String base64UrlSignature = hmacEncode(base64UrlHeader + "." + base64UrlPayload, 
										ConfigConstant.secretKey.getValue());
			
			return base64UrlHeader + "." + base64UrlPayload + "." + base64UrlSignature;

		} catch (Exception e) {
			throw new RuntimeException("Unable to generate a JWT token.");
		}
	}

	/**
	 * Helper method that encodes data using HmacSHA256 and key.
	 * 
	 * @param data data to encode
	 * @param key  Secret key used during encoding.
	 * @return Base64UrlEncoded string without padding
	 */
	private static String hmacEncode(String data, String key) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secret_key);

		return Base64.getUrlEncoder().withoutPadding().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
	}

	/** GET TOKEN **/
	public static String getToken() {

		String result = null;
		ResponseEntity<String> response = null;
		RestTemplate rest = new RestTemplate();
		
		String url = Environment.GET_TOKEN.getUrl();

		String auth = encodeBase64String(ConfigConstant.usernamePassword.getValue());
		
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("content-type", "application/x-www-form-urlencoded");
			headers.add("Authorization", "Basic " + auth); 						// username:password base 64
//			headers.add("Authorization", "Basic " + Environment.AUTH.getUrl()); // username:password base 64

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type", "client_credentials");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
			response = rest.exchange(url, HttpMethod.POST, entity, String.class);
//			response = rest.exchange(Environment.BNI.getUrl(), HttpMethod.POST, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK) {
				try {
					JSONObject resultToken = new JSONObject(response.getBody().toString());
					result = (String) resultToken.get("access_token");

				} catch (JSONException e) {
					throw new RuntimeException("JSONException occurred");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/** FUNGSI GENERATOR ENCODE BASE64 **/
	public static String encodeBase64String(String encodeStr) {
		return Base64.getEncoder().encodeToString(encodeStr.getBytes());
	}
	
	public static String clientIdEncode() {
		String idClient = ConfigConstant.clientId.getValue();
		return JwtConstant.ID_CLIENT.getValue() + encodeBase64String(idClient);
	}
	
	/** FUNGSI GENERATOR CUSTOMER REFERENCE NUMBER **/
	public static String customerReferenceGenerator() {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Random random = new Random();
		int randomNum = random.nextInt(900) + 100;
		String customerRefence = f.format(new Date()) + String.valueOf(randomNum);
		return customerRefence;
	}
	
}
