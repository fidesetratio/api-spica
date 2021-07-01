package com.app.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2545923989475728158L;
	
	public static ArrayList serializableList(List dataList){
		if(dataList!=null){
			return new ArrayList(dataList);
		}else{
			return null;
		}
	}
	
	public static HashMap serializableMap(Map dataMap){
		if(dataMap!=null){
			return new HashMap(dataMap);
		}else{
			return null;
		}
	}
	
	public static boolean isEmpty(Object cek) {
		if(cek==null) return true;
		else	if(cek instanceof String) {
			String tmp = (String) cek;
				if(tmp.trim().equals("")) return true;
				else return false;
		}else if(cek instanceof List) {
			List tmp = (List) cek;
			return tmp.isEmpty();
		}else if(cek instanceof Map){
			return ((Map) cek).isEmpty();
		}else if(cek instanceof Integer || cek instanceof Long|| cek instanceof Double|| cek instanceof Float|| cek instanceof BigDecimal || cek instanceof Date){
			return false;
		}
		return true;
	}

}
