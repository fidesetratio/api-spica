package com.app.model;

import java.io.Serializable;

public class LstVariable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6384366970987732183L;
	
	private String variable;
	private String expression;
	
	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}

}
