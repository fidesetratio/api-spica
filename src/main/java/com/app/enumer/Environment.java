package com.app.enumer;

public enum Environment {
	
//	GET_TOKEN("https://apidev.bni.co.id:8067/api/oauth/token"),
//	GET_INHOUSE_INQUIRY("https://apidev.bni.co.id:8067/H2H/v2/getinhouseinquiry"),
//	GET_INTERBANK_INQUIRY("https://apidev.bni.co.id:8067/H2H/v2/getinterbankinquiry");
	
	//PRODUCTION
	GET_TOKEN("https://api.bni.co.id:443/api/oauth/token"),
	GET_INHOUSE_INQUIRY("https://api.bni.co.id:443/H2H/v2/getinhouseinquiry"),
	GET_INTERBANK_INQUIRY("https://api.bni.co.id:443/H2H/v2/getinterbankinquiry");
	
	private String url;

	Environment(String envUrl) {
		this.url = envUrl;
	}

	public String getUrl() {
		return url;
	}
	
}
