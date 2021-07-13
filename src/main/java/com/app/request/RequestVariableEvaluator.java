package com.app.request;

import java.io.Serializable;
import java.util.ArrayList;

import com.app.model.LstVariable;

public class RequestVariableEvaluator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4503494345792859488L;
	
	private ArrayList<LstVariable> variableList;

	public ArrayList<LstVariable> getVariableList() {
		return variableList;
	}

	public void setVariableList(ArrayList<LstVariable> variableList) {
		this.variableList = variableList;
	}

}
