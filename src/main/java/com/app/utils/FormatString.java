package com.app.utils;

import java.io.Serializable;

public class FormatString implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4918160342439645826L;
	
	public static String rpad(String karakter, String kata, int panjang) {
		if(kata==null) return null;
		StringBuffer result = new StringBuffer();
		if (kata.length() < panjang) {
			for (int i = 0; i < panjang - kata.length(); i++) {
				result.append(karakter);
			}
			result.append(kata);
			return result.toString();
		} else {
			return kata;
		}
	}

}
