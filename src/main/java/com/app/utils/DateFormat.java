package com.app.utils;

import java.util.Date;

public abstract class DateFormat extends Format {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2135799181705200258L;
	
	public abstract StringBuffer format(Date date, StringBuffer toAppendTo,
            FieldPosition fieldPosition);
	
	public final String format(Date date)
    {
        return format(date, new StringBuffer(),
		      DontCareFieldPosition.INSTANCE).toString();
    }

}
