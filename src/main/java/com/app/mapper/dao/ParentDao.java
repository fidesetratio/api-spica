package com.app.mapper.dao;

import java.text.DateFormat;

public abstract class ParentDao {
	
	protected DateFormat defaultDateFormat;

	public DateFormat getDefaultDateFormat() {
		return defaultDateFormat;
	}

	public void setDefaultDateFormat(DateFormat defaultDateFormat) {
		this.defaultDateFormat = defaultDateFormat;
	}

}
