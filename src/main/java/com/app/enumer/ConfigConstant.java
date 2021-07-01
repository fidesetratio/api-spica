package com.app.enumer;

public enum ConfigConstant {
	
	accountNumber_Interbank("0316031099"),
	secretKey("ab80ff61-bb0e-4f36-8cb1-e5d80796ace9"),
	xApiKey("94eb95e2-f38b-4fe2-bbc5-6cad256fccc3"),
	clientId("SANDBOX"),
	usernamePassword("186703ca-141a-496b-b43c-396465f99324:ed14bf2e-e6ba-4ffb-89f6-6124392b8045");
	
	private String value;

	ConfigConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
