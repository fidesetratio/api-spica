package com.app.services;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.utils.Util;

@Service
public class TokenService {

	@Autowired
	private Util util;

	/**
	 * request token dari server BNI
	 **/
	public String getToken() {
		return util.getToken();
	}

}
