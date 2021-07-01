package com.app.model;

public class Result {

		private String errorMessage;
		private Object value;
		public String getErrorMessage() {
			return errorMessage;
		}
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}

	  public String toString() {
		  StringBuilder builder = new StringBuilder();
		  builder.append("[errorMessage:"+errorMessage);
		  builder.append(" ");
		  builder.append("value = "+value+"]");
		  return builder.toString();
		  
	  }
}
