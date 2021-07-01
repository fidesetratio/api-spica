package com.app.utils;

import java.io.Serializable;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserConfiguration;
import org.mvel2.ParserContext;

public class MvelUtility {

	
	public static Object eval(String str, Map<String, Object> vars) {
		  ParserConfiguration pconf = new ParserConfiguration();
		 // pconf.addPackageImport("org.kie.internal.task.api.model");
		 //  pconf.addPackageImport("org.jbpm.services.task");
		 //  pconf.addPackageImport("org.jbpm.services.task.impl.model");
		 // pconf.addPackageImport("org.jbpm.services.task.query");
		 // pconf.addPackageImport("org.jbpm.services.task.internals.lifecycle");
		 // pconf.addImport(Status.class);
		 // pconf.addImport(Allowed.class);
		  pconf.addPackageImport("java.util");
		  pconf.addPackageImport("com.app.utility");
		  
		  ParserContext context = new ParserContext(pconf);
		  Serializable s = MVEL.compileExpression(str.trim(), context);
		  if (vars != null) {
		    return  MVEL.executeExpression(s, vars);
		  } else {
	
			  return  MVEL.executeExpression(s);
		  }
		}	
	
}
