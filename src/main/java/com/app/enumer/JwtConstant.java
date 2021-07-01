package com.app.enumer;

public enum JwtConstant {
	
	HEADER("{\"typ\":\"JWT\",\"alg\":\"HS256\"}"),
	ID_CLIENT("IDBNI");
//	SECRET("ab80ff61-bb0e-4f36-8cb1-e5d80796ace9"),
//	API_KEY("94eb95e2-f38b-4fe2-bbc5-6cad256fccc3"),
//	CLIENT_ID("IDBNIU0FOREJPWA==");
	
	private String value;

	JwtConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
