package com.app.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.app.model.Predefined;
import com.app.model.Result;
import com.app.utils.MvelUtility;

@Component
public class CommonUtils {
	
	public Object execute(String expression,HashMap<String,Object> vars) {
		String var = expression.substring(0,expression.indexOf("("));
		//String var2 = expression.substring(expression.indexOf("("));
		//System.out.println(var);
		HashMap<String,Predefined> ruleSyntax = new HashMap<String, Predefined>();

		ruleSyntax.put(var, new Predefined(var, "PF."+expression));
		
		Predefined executor = ruleSyntax.get(var);
		executor.setParameters(vars);
		return executor.eval();
	}
	
	public String extractPredefinedFunction(String expression) {
		String result = null;
		
		Pattern regex = Pattern.compile("^[^\\[{(,]*");
		Matcher regexMatcher = regex.matcher(expression);
		if (regexMatcher.find()) {
			result = regexMatcher.group();
		    //System.out.println(regexMatcher.group());
		}
		
		return result;
	}
	
	public String nomorPolis(String kata) {
//		Regex reg9 = new Regex("\\d{9,9}");
//		Regex reg11 = new Regex("\\d{11,11}");
//		Regex reg14 = new Regex("\\d{14,14}");
		
		if(kata==null){
			return kata;
		}else if(kata.length()==9){
			return kata.substring(0,2)+"."+kata.substring(2);
		}else if(kata.length()==11){
			return kata.substring(0,2)+"."+kata.substring(2,6)+"."+kata.substring(6);
		}else if(kata.length()==14){
			return kata.substring(0,2)+"."+kata.substring(2,5)+"."+kata.substring(5,9)+"."+kata.substring(9);
		}else return kata;

	}
}
