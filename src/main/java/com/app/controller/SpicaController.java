package com.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.el.ELProcessor;

import org.jboss.logging.Logger;
import org.jgrapht.Graph;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.feignclient.ServiceWorkflowEngine;
import com.app.model.DocumentStatus;
import com.app.model.LstSpicaCondition;
import com.app.model.LstSpicaField;
import com.app.model.LstSpicaForm;
import com.app.model.LstSpicaList;
import com.app.model.LstSpicaPf;
import com.app.model.LstSpicaRule;
import com.app.model.LstSpicaVariable;
import com.app.model.LstVariable;
import com.app.model.MstWfDocument;
import com.app.request.RequestVariableEvaluator;
import com.app.request.RequestEvaluateForm;
import com.app.request.RequestSaveCondition;
import com.app.request.RequestSaveForm;
import com.app.request.RequestSaveList;
import com.app.request.RequestSaveRule;
import com.app.request.RequestSaveVariable;
import com.app.request.RequestTest;
import com.app.request.RequestUpdateCondition;
import com.app.request.RequestUpdateForm;
import com.app.request.RequestUpdateFormField;
import com.app.request.RequestUpdateList;
import com.app.request.RequestUpdateRule;
import com.app.request.RequestUpdateVariable;
import com.app.response.ResponseData;
import com.app.services.AutodebetServices;
import com.app.services.SpicaServices;
import com.app.utils.CommonUtils;
import com.app.utils.Evaluator;
import com.app.utils.EvaluatorRule;

@RestController
public class SpicaController {
	
	@Autowired
	private SpicaServices services;
	
	@Autowired
	private CommonUtils utils;
	
	@Autowired
	private ServiceWorkflowEngine workflowEngineServices;
	
	@Autowired
	private AutodebetServices autodebetServices;
	
	private static Logger logger = Logger.getLogger(SpicaController.class);
	
	@PostMapping("/test")
	public ResponseData Test(@RequestBody RequestTest requestTest) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		try {
			
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			String pattern = "yyyy-MM-dd";
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    	Date newDate = simpleDateFormat.parse("1987-07-06");
			
			map.put("nik", "3173044607870002");
			map.put("regSpaj", "37202147414");
			map.put("date", newDate);
			map.put("booleanResult", true);
			map.put("mcl_id", "002100014972");
			map.put("listName", "List High Risk Countries");
			map.put("profile_target", "pemegang");
			map.put("screening_type", "DTTOT");
			map.put("account_name", "JONOMADE MADEMADEMADEMADE IMAMADE");
			map.put("account_number", "00800000");
			map.put("bank_code", "008");
			map.put("threshold_fuzzy", 90);
			map.put("nama_tertanggung", "AGUNG RESERA PUTRA");
			map.put("gender", "Perempuan");
			map.put("no_hp", "081270441742");
			map.put("no_account", "0053061206");
			map.put("email", "agoenk_reser@yahoo.com");
			
			Object value = utils.execute("BankInquiry(regSpaj,threshold_fuzzy)", map);
			
			//spajDocumentStatusProcess("09210547075", 0);
			
			error = false;
			message = "Success";
			data.put("Value", value);
			//data.put("hasil", hasilConc);
			
//			HashMap<String, Object> mapWf = new HashMap<String, Object>();
//			mapWf.put("source_orion", "1");
			
//			RequestWfEngineHeaders headers = new RequestWfEngineHeaders();
//			headers.setRole_id(2);
//			headers.setAction("ETL");
//			headers.setLus_id(0);
//			headers.setState(1);
//			headers.setType_id(1);
//			headers.setDoc_id(164);
//			headers.setParent_doc_id(164);
//			headers.setPriority(30);
//			headers.setRemarks("TESTING SPICA");
			
//			Map<String, String> headers = new HashMap<>();
//			headers.put("role_id", "2");
//			headers.put("action", "ETL");
//			headers.put("lus_id", "0");
//			headers.put("state", "1");
//			headers.put("type_id", "1");
//			headers.put("doc_id", "164");
//			headers.put("parent_doc_id", "164");
//			headers.put("priority", "30");
//			headers.put("remarks", "TESTING SPICA");
			
			//ResponseData responseWF = workflowEngineServices.processDocument(2, "ETL", 0, 1, 1, 164, 164, 30, "TESTING SPICA", "TESTING SPICA");
			
			//callSpicaWorfklowEngine("37202147095", true, 0);
			
			//AutodebetServices autodebetServices = new AutodebetServices();
			//autodebetServices.prosesAutoDebetNB("37202147095", 0, 218, 10, 17);
			
		} catch (Exception e) {
			error = true;
			message = "Error:" + e;
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	
	}
	
	@PostMapping("/variableevaluator")
	public ResponseData VariableEvaluator(@RequestBody RequestVariableEvaluator requestVariable) {
		
		Boolean error = true;
		String message = null;
		String resultErr = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		try {
			
			ArrayList<LstVariable> listVariable = requestVariable.getVariableList();
			
			if(!listVariable.isEmpty()) {
				Graph<EvaluatorRule, DefaultEdge> directedGraph = new DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>(DefaultEdge.class);
				List<EvaluatorRule> engine = new ArrayList<EvaluatorRule>();
				ArrayList<Object> variableSorted = new ArrayList<>();
				
				//get all list variable
				for(LstVariable req : listVariable) {
					//engine.add(new EvaluatorRule(req.getVariable(), req.getExpression()));
				}
				
				Map<String, EvaluatorRule> taskNameToTaskMap = engine.stream()
		                .collect(Collectors.toMap(evaluatorrule -> evaluatorrule.getRule(), evaluatorrule -> evaluatorrule));
				
				//evaluate every variable
				 for (EvaluatorRule e : engine) {
			            directedGraph.addVertex(e);
			            for (String dependency : e.getDependencyVariables()) {
			            	EvaluatorRule dependencyEval = taskNameToTaskMap.get(dependency);
			            	if(dependencyEval == null) {
			            			continue;
			            	}
			                directedGraph.addVertex(dependencyEval);
			                directedGraph.addEdge(dependencyEval, e);
			            }
				 }
				 
				 TopologicalOrderIterator<EvaluatorRule, DefaultEdge> sortitbaseddependency = 
			        		new TopologicalOrderIterator<EvaluatorRule, DefaultEdge>((DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>) directedGraph);
			    
			     //put result to hashmap
			     while (sortitbaseddependency.hasNext()) {
					try {
						
						EvaluatorRule eval = sortitbaseddependency.next();
						HashMap<String, Object> mapList = new HashMap<>();
						mapList.put("variable", eval.getRule());
						mapList.put("expression", eval.getPrerequisites());

						variableSorted.add(mapList);
						
					} catch (Exception e) {
						
						resultErr = e.getMessage();
						logger.error("ERROR: " + resultErr);
						
					}
			     }
		
			     error = false;
			     message = "Success";
			     data.put("sorted_variable", variableSorted);
			     
			}
		} catch (Exception e) {
			error = true;
			resultErr = e.toString();
			message = "Error:" + resultErr;
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	
	}
	
	@PostMapping("/saveform")
	public ResponseData SaveForm(@RequestBody RequestSaveForm requestSaveForm) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		String form_type = requestSaveForm.getForm_type();
		String form_description = requestSaveForm.getForm_description();
		String form_source = requestSaveForm.getForm_source();
		String form_primary_attribute = requestSaveForm.getForm_primary_attribute();
		Integer user_id = requestSaveForm.getUser_id();
		
		try {
			
			Integer form_id = services.insertLstSpicaForm(form_type, form_description, form_source, form_primary_attribute, user_id);
			
			if(form_id != null) {			
				//Insert form field
				services.insertFormField(form_id, form_source, user_id);
				
				error = false;
				message = "Form has been created successfully";
				data.put("form_id", form_id);
				data.put("form_type", form_type);
				data.put("form_description", form_description);
				data.put("form_source", form_source);
				data.put("form_primary_attribute", form_primary_attribute);		
			} else {
				error = true;
				message = "Form insert error";
				logger.error(message);
			}
		} catch (Exception e) {
			error = true;
			message = "Form insert error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/savevariable")
	public ResponseData SaveVariable(@RequestBody RequestSaveVariable requestSaveVariable) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		String predefinedFunctionName = null;
		
		Integer form_id = requestSaveVariable.getForm_id();
		String variable_name = requestSaveVariable.getVariable_name();
		String variable_type = requestSaveVariable.getVariable_type();
		String variable_description = requestSaveVariable.getVariable_description();
		String variable_expression = requestSaveVariable.getVariable_expression();
		Integer user_id = requestSaveVariable.getUser_id();
		
		try {
			
			//Check if variable name already existed
			LstSpicaVariable variableExist = services.checkFormVariableExist(form_id, variable_name);
			
			if(variableExist != null) {
				error = true;
				message = "Variable name already exist, please choose another name";
				logger.error(message);
			} else {
				//get variable from form id to check first save or not
				ArrayList<LstSpicaVariable> lstSpicaVariable = services.selectFormVariable(form_id);
				
				//if first save then save without checking dependency
				if(lstSpicaVariable.isEmpty()) {
					
					//extract predefined function name from variable expression
					predefinedFunctionName = utils.extractPredefinedFunction(variable_expression);
					
					//get predefined function detail
					LstSpicaPf lstSpicaPf = services.selectPredefinedFunction(predefinedFunctionName);
					
					//check if predefined function in expression is already defined
					if(lstSpicaPf != null) {
						
						//check if predefined function type equals variable type, if equals then save
						if(lstSpicaPf.getLspc_pf_type().equalsIgnoreCase(variable_type)) {
					
							services.insertLstSpicaVariable(form_id, variable_name, variable_type, variable_description, variable_expression, user_id);
							
							error = false;
							message = "Variable has been saved successfully";
							data.put("form_id", form_id);
							data.put("variable_name", variable_name);
							data.put("variable_type", variable_type);
							data.put("variable_description", variable_description);
							data.put("variable_expression", variable_expression);
						} else {
							error = true;
							message = "Variable type did not match with predefined function return type";
							logger.error(message);
						}
					} else {
						error = true;
						message = "Predefined function in variable expression not found, please check again";
						logger.error(message);
					}
				} 
				//if not first save then save with checking variable dependency
				else {
					//extract predefined function name from variable expression
					predefinedFunctionName = utils.extractPredefinedFunction(variable_expression);
					
					//get predefined function detail
					LstSpicaPf lstSpicaPf = services.selectPredefinedFunction(predefinedFunctionName);
					
					//check if predefined function in expression is already defined
					if(lstSpicaPf != null) {
						
						//check if predefined function type equals variable type, if equals then save
						if(lstSpicaPf.getLspc_pf_type().equalsIgnoreCase(variable_type)) {
							
							Graph<Evaluator, DefaultEdge> directedGraph = new DirectedAcyclicGraph<Evaluator, DefaultEdge>(DefaultEdge.class);
							List<Evaluator> engine = new ArrayList<Evaluator>();
							
							//add new variable to list for dependency checking
							engine.add(new Evaluator("V"+variable_name, variable_expression));
							
							//add all list variable to evaluator
							for(LstSpicaVariable variable : lstSpicaVariable) {
								engine.add(new Evaluator("V"+variable.getLspc_variable_name(), variable.getLspc_variable_expression()));
							}
							
							Map<String, Evaluator> taskNameToTaskMap = engine.stream()
					                .collect(Collectors.toMap(evaluator -> evaluator.getVariable(), evaluator -> evaluator));
							
							//evaluate every variable before save
							try {
								 for (Evaluator e : engine) {
							            directedGraph.addVertex(e);
							            for (String dependency : e.getDependencyVariables()) {
							            	Evaluator dependencyEval = taskNameToTaskMap.get(dependency);
							            	if(dependencyEval == null) {
							            		if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
							            			continue;
							            		};
							            	}
							                directedGraph.addVertex(dependencyEval);
							                directedGraph.addEdge(dependencyEval, e);
							            }
								 }
								 
								 	services.insertLstSpicaVariable(form_id, variable_name, variable_type, variable_description, variable_expression, user_id);
									
									error = false;
									message = "Variable has been saved successfully";
									data.put("form_id", form_id);
									data.put("variable_name", variable_name);
									data.put("variable_type", variable_type);
									data.put("variable_description", variable_description);
									data.put("variable_expression", variable_expression);
								 
							} catch (Exception e) {
								error = true;
								message = "Cyclic redundancy check error, please check & evaluate your variables";
								logger.error(message);
							}
						} else {
							error = true;
							message = "Variable type did not match with predefined function return type";
							logger.error(message);
						}
					} else {
						error = true;
						message = "Predefined function in variable expression not found, please check your variable expression";
						logger.error(message);
					}
				}
			}
		} catch (Exception e) {
			error = true;
			message = "Save variable error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/saverule")
	public ResponseData SaveRule(@RequestBody RequestSaveRule requestSaveRule) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Integer form_id = requestSaveRule.getForm_id();
		String rule_name = requestSaveRule.getRule_name();
		String rule_description = requestSaveRule.getRule_description();
		String rule_expression = requestSaveRule.getRule_expression();
		String rule_error_msg = requestSaveRule.getRule_error_msg();
		Integer user_id = requestSaveRule.getUser_id();
		String rule_prerequisites = requestSaveRule.getRule_prerequisites();
		Integer rule_category_id = requestSaveRule.getRule_category_id();
		
		try {
			
			LstSpicaRule ruleExist = services.checkFormRuleExist(form_id, rule_name);
			
			if(ruleExist != null) {
				error = true;
				message = "Rule name already exist, please choose another name";
				logger.error(message);
			} else {
				
				//get rule to check first save or not
				ArrayList<LstSpicaRule> lstSpicaRule = services.selectFormRule(form_id);
				
				//if first save then save without checking dependency
				if(lstSpicaRule.isEmpty()) {
				
					services.insertLstSpicaRule(form_id, rule_name, rule_description, rule_expression, rule_error_msg, user_id, rule_prerequisites, rule_category_id);
					
					error = false;
					message = "Rule has been saved successfully";
					data.put("form_id", form_id);
					data.put("rule_name", rule_name);
					data.put("rule_description", rule_description);
					data.put("rule_expression", rule_expression);
					data.put("rule_error_msg", rule_error_msg);
					data.put("rule_prerequisites", rule_prerequisites);
					data.put("rule_category_id", rule_category_id);
				}
				//if rule prerequisites == null, then save without checking dependency
				else if(rule_prerequisites == null){
					services.insertLstSpicaRule(form_id, rule_name, rule_description, rule_expression, rule_error_msg, user_id, rule_prerequisites, rule_category_id);
					
					error = false;
					message = "Rule has been saved successfully";
					data.put("form_id", form_id);
					data.put("rule_name", rule_name);
					data.put("rule_description", rule_description);
					data.put("rule_expression", rule_expression);
					data.put("rule_error_msg", rule_error_msg);
					data.put("rule_prerequisites", rule_prerequisites);
					data.put("rule_category_id", rule_category_id);
				}
				//check dependency first before saving
				else {
					Graph<EvaluatorRule, DefaultEdge> directedGraphRule = new DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>(DefaultEdge.class);
					List<EvaluatorRule> engineRule = new ArrayList<EvaluatorRule>();
					
					//add new rule to list for dependency checking
					engineRule.add(new EvaluatorRule(rule_name, rule_prerequisites, rule_expression, rule_error_msg, rule_description, rule_category_id));
						
					//get all list rule
					for(LstSpicaRule rule : lstSpicaRule) {
						engineRule.add(new EvaluatorRule(rule.getLspc_rule_name(), rule.getLspc_rule_prerequisites(), rule.getLspc_rule_expression(), rule.getLspc_rule_error_msg(), rule.getLspc_rule_description(), rule.getLspc_rule_category_id()));
					}
						
					Map<String, EvaluatorRule> taskNameToTaskMapRule = engineRule.stream()
							.collect(Collectors.toMap(evaluatorrule -> evaluatorrule.getRule(), evaluatorrule -> evaluatorrule));
						
					//evaluate every rule prerequisites
					try {
						for (EvaluatorRule e : engineRule) {
							directedGraphRule.addVertex(e);
						    for (String dependency : e.getDependencyVariables()) {
						    	EvaluatorRule dependencyEval = taskNameToTaskMapRule.get(dependency);
						    	if(dependencyEval == null) {
						    		continue;
						    	}
						        directedGraphRule.addVertex(dependencyEval);
						        directedGraphRule.addEdge(dependencyEval, e);
						    }
						}
						
						services.insertLstSpicaRule(form_id, rule_name, rule_description, rule_expression, rule_error_msg, user_id, rule_prerequisites, rule_category_id);
						
						error = false;
						message = "Rule has been saved successfully";
						data.put("form_id", form_id);
						data.put("rule_name", rule_name);
						data.put("rule_description", rule_description);
						data.put("rule_expression", rule_expression);
						data.put("rule_error_msg", rule_error_msg);
						data.put("rule_prerequisites", rule_prerequisites);
						data.put("rule_category_id", rule_category_id);
						
					} catch(Exception e) {
						error = true;
						message = "Cyclic redundancy check error, please check & evaluate your rule prerequisites";
						logger.error(message);
					}	
				}
			}
		} catch (Exception e) {
			error = true;
			message = "Save rule error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/savecondition")
	public ResponseData SaveCondition(@RequestBody RequestSaveCondition requestSaveCondition) {
		
		Boolean error = true;
		String message = null;
		String predefinedFunctionName = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Integer form_id = requestSaveCondition.getForm_id();
		String rule_name = requestSaveCondition.getRule_name();
		String condition_name = requestSaveCondition.getCondition_name();
		String condition_description = requestSaveCondition.getCondition_description();
		String condition_expression = requestSaveCondition.getCondition_expression();
		Integer user_id = requestSaveCondition.getUser_id();
		
		try {
			
			LstSpicaCondition lstSpicaCondition = services.checkFormRuleConditionExist(form_id, rule_name, condition_name);
			
			if(lstSpicaCondition != null) {
				error = true;
				message = "Condition name already exist, please choose another name";
				logger.error(message);
			} else {
				//extract predefined function name from variable expression
				predefinedFunctionName = utils.extractPredefinedFunction(condition_expression);
				
				//get predefined function detail
				LstSpicaPf lstSpicaPf = services.selectPredefinedFunction(predefinedFunctionName);
				
				//check if predefined function in expression is already defined
				if(lstSpicaPf != null) {
					services.insertLstSpicaCondition(form_id, rule_name, condition_name, condition_expression, condition_description, user_id);
					
					error = false;
					message = "Condition has been saved successfully";
					data.put("form_id", form_id);
					data.put("rule_name", rule_name);
					data.put("condition_description", condition_description);
					data.put("condition_expression", condition_expression);
				} else {
					error = true;
					message = "Predefined function in condition expression not found, please check your condition expression";
					logger.error(message);
				}
			}
		} catch (Exception e) {
			error = true;
			message = "Save condition error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/evaluateform")
	public ResponseData EvaluateForm(@RequestBody RequestEvaluateForm requestEvaluateForm) {
		//set time started
		Date time_started = new Date();
		
		Boolean error = true;
		String message = null;
		String resultErr = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		String field_name = null;
		String select_command = null;
		Boolean clean = true;
		String spica_result = null;
		Integer process_number = null;
		
		HashMap<String, Object> fieldAndVariable = new HashMap<String, Object>();
		HashMap<String, Object> field = new HashMap<String, Object>();
		HashMap<String, Object> conditionResultMap = new HashMap<String, Object>();
		HashMap<String, Object> ruleResultMap = new HashMap<String, Object>();
		
		Integer form_id = requestEvaluateForm.getForm_id();
		Object primary_attribute = requestEvaluateForm.getPrimary_attribute();
		Integer user_id = requestEvaluateForm.getUser_id();
		Integer evaluate_type = requestEvaluateForm.getEvaluate_type();
		
		try {
			
			Integer mspc_form_hist_process_number = services.selectMaxFormHistProcessNumber(primary_attribute.toString());
			
			if(mspc_form_hist_process_number == null) {
				process_number = 1;
			} else {
				process_number = mspc_form_hist_process_number + 1;
			}
			
			//evaluate_type = 1 --> get all field value, else get field value when used by expression only
			if(evaluate_type == 1) {
				//Get Form
				LstSpicaForm lstSpicaForm = services.selectForm(form_id);
				
				//check if form is found
				if(lstSpicaForm != null) {
					
					ArrayList<LstSpicaField> lstSpicaField = services.selectFormField(form_id);
					
					if(!lstSpicaField.isEmpty()) {
						
						for(LstSpicaField form_field : lstSpicaField) {
							if(select_command == null) {
				            	select_command = form_field.getLspc_field_name() + ",";
				            } else {
				            	select_command = select_command + form_field.getLspc_field_name() + ",";
				            } 
						}
						
						if(select_command != null) {
							//remove comma in the end of select command
							select_command = select_command.substring(0, select_command.length() - 1);
							
							//select field value and put into hashmap
							field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
							
							//put field value into field and variable hashmap to process
							for (Map.Entry<String, Object> entry : field.entrySet()) {
								
								if(fieldAndVariable.containsKey("F"+entry.getKey())) {
									continue;
								} else {
									fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
									
									//Insert into field history
					    			if(entry.getValue() != null) {
					    				services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id, process_number);
					    			} else {
					    				services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id, process_number);
					    			}
								}
						    }
						}
						
						//get form variable
						ArrayList<LstSpicaVariable> lstSpicaVariable = services.selectFormVariable(form_id);
							
						//check if form variable is found
						if(!lstSpicaVariable.isEmpty()) {
							Graph<Evaluator, DefaultEdge> directedGraph = new DirectedAcyclicGraph<Evaluator, DefaultEdge>(DefaultEdge.class);
							List<Evaluator> engine = new ArrayList<Evaluator>();
							//ArrayList<Object> variableSorted = new ArrayList<>();
							ArrayList<Object> variableValue = new ArrayList<>();
								
							//add all list variable to evaluator
							for(LstSpicaVariable variable : lstSpicaVariable) {
								engine.add(new Evaluator("V"+variable.getLspc_variable_name(), variable.getLspc_variable_expression()));
							}
								
							Map<String, Evaluator> taskNameToTaskMap = engine.stream()
						                .collect(Collectors.toMap(evaluator -> evaluator.getVariable(), evaluator -> evaluator));
								
							//evaluate every variable
							try {
								for (Evaluator e : engine) {
									directedGraph.addVertex(e);
								    for (String dependency : e.getDependencyVariables()) {
								    	Evaluator dependencyEval = taskNameToTaskMap.get(dependency);
								    	if(dependencyEval == null) {
								    		if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
									    		continue;
									    	};
								    	}
								    	directedGraph.addVertex(dependencyEval);
								        directedGraph.addEdge(dependencyEval, e);
								    }
								}
								
								TopologicalOrderIterator<Evaluator, DefaultEdge> sortitbaseddependency = 
										new TopologicalOrderIterator<Evaluator, DefaultEdge>((DirectedAcyclicGraph<Evaluator, DefaultEdge>) directedGraph);
								    
								//put variable & variable value to hashmap
								while (sortitbaseddependency.hasNext()) {
									try {
											
										Evaluator eval = sortitbaseddependency.next();
											
										//evaluate variable expression and put to fieldAndVariable hashmap
										fieldAndVariable.put(eval.getVariable(), utils.execute(eval.getExpression(), fieldAndVariable));
											
										//insert into variable history
										services.insertMstSpicaVariableHistory(form_id, primary_attribute.toString(), eval.getVariable(), eval.getExpression(), fieldAndVariable.get(eval.getVariable()).toString(), user_id, process_number);
											
										//untuk balikan sorted variable
		//								HashMap<String, Object> mapList = new HashMap<>();
		//								mapList.put("variable", eval.getVariable());
		//								mapList.put("expression", eval.getExpression());
		//								variableSorted.add(mapList);
											
										//untuk balikan value dari setiap variable
										HashMap<String, Object> variableResult = new HashMap<>();
										variableResult.put("variable", eval.getVariable());
										variableResult.put("value", fieldAndVariable.get(eval.getVariable()));
										variableValue.add(variableResult);								
									} catch (Exception e) {
											
										error = true;
										resultErr = e.getMessage();
										message = resultErr;
										logger.error("ERROR: " + resultErr);
											
									}
								}
							} catch (Exception e) {
								error = true;
								resultErr = "Variable: "+ e.getMessage();
								message = resultErr;
								logger.error("ERROR: "+ resultErr);
								
								throw new Exception (message);
							}
								     
							//get form rule list
							ArrayList<LstSpicaRule> lstSpicaRule = services.selectFormRule(form_id);
								     
							//check if form rule is found
							if(!lstSpicaRule.isEmpty()) {
								    	 
//								//ArrayList<Object> conditionResultList = new ArrayList<>();
//								    	 
//								ArrayList<Object> ruleStatus = new ArrayList<>();
//									
//								//import el processor engine to evaluate rule
//								ELProcessor elProcessor = new ELProcessor();
//									
//								//evaluate rule condition
//								for(LstSpicaRule rule : lstSpicaRule) {
//								    		 
//									//get all rule condition
//									ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, rule.getLspc_rule_name());
//								    		 
//									if(!lstSpicaCondition.isEmpty()) {
//											
//										for(LstSpicaCondition condition : lstSpicaCondition) {
//		
//											Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
//									    			 
//											//put condition result to hashmap for evaluate rule
//											conditionResultMap.put(condition.getLspc_condition_name(), result);
//									    			 
//											//insert into condition history
//											services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id);
//									    			 
//											//untuk balikan result dari setiap condition
//		//							    	HashMap<String, Object> conditionResult = new HashMap<>();
//		//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
//		//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
//		//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
//		//							    	conditionResult.put("result", result);
//		//							    	conditionResultList.add(conditionResult);
//									    }	
//								    } else {
//								    	error = true;
//								    	message = "Form rule condition not found";
//										logger.error(message);
//													
//										throw new Exception("Form rule condition not found");
//								    }
//								    		 
//									//evaluate rule expression 
//									for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
//										String key = entrySet.getKey();
//										Object value = entrySet.getValue();
//								    	         
//										elProcessor.setValue(key, value);
//									}
//		
//									Boolean result = (Boolean) elProcessor.eval(rule.getLspc_rule_expression());
//								    		 
//									HashMap<String, Object> ruleResult = new HashMap<>();
//								    		 
//									if(result) {
//										ruleResult.put("rule_name", rule.getLspc_rule_name());
//										ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//										ruleResult.put("rule_description", rule.getLspc_rule_description());
//										ruleResult.put("status", "Passed");
//								    			 
//										ruleStatus.add(ruleResult);	
//									} else {
//										ruleResult.put("rule_name", rule.getLspc_rule_name());
//								    	ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//								    	ruleResult.put("status", "Not Passed");
//								    	ruleResult.put("error_message", rule.getLspc_rule_error_msg());
//								    	ruleResult.put("rule_description", rule.getLspc_rule_description());
//								    			 
//								    	ruleStatus.add(ruleResult);
//								    } 
//								    		 
//									//insert into rule history
//								    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), rule.getLspc_rule_expression(), ruleResult.get("status").toString(), user_id);
//								}
//								    	 
//								error = false;
//								message = "Form has been evaluated successfully";
//								//data.put("sorted_variable", variableSorted);
//								data.put("variable_value", variableValue);
//								//data.put("condition_result", conditionResultList);
//								data.put("list_rule_status", ruleStatus);
								    
								//RULE PREREQUISITES
									
								Graph<EvaluatorRule, DefaultEdge> directedGraphRule = new DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>(DefaultEdge.class);
								List<EvaluatorRule> engineRule = new ArrayList<EvaluatorRule>();
									
								//get all list rule
								for(LstSpicaRule rule : lstSpicaRule) {
									engineRule.add(new EvaluatorRule(rule.getLspc_rule_name(), rule.getLspc_rule_prerequisites(), rule.getLspc_rule_expression(), rule.getLspc_rule_error_msg(), rule.getLspc_rule_description(), rule.getLspc_rule_category_id()));
								}
									
								Map<String, EvaluatorRule> taskNameToTaskMapRule = engineRule.stream()
										.collect(Collectors.toMap(evaluatorrule -> evaluatorrule.getRule(), evaluatorrule -> evaluatorrule));
									
								//evaluate every rule prerequisites
								try {
									for (EvaluatorRule e : engineRule) {
										directedGraphRule.addVertex(e);
									    for (String dependency : e.getDependencyVariables()) {
									    	EvaluatorRule dependencyEval = taskNameToTaskMapRule.get(dependency);
									    	if(dependencyEval == null) {
									    		continue;
									    	}
									        directedGraphRule.addVertex(dependencyEval);
									        directedGraphRule.addEdge(dependencyEval, e);
									    }
									}
										 
									TopologicalOrderIterator<EvaluatorRule, DefaultEdge> sortitbaseddependencyrule = 
									new TopologicalOrderIterator<EvaluatorRule, DefaultEdge>((DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>) directedGraphRule);
									    
									//import el processor engine to evaluate rule
									ELProcessor elProcessor = new ELProcessor();
										
									ArrayList<Object> ruleStatus = new ArrayList<>();
										 
									//evalute & put rule result to hashmap
									while (sortitbaseddependencyrule.hasNext()) {
										try {
											EvaluatorRule eval = sortitbaseddependencyrule.next();
												
											if(eval.getPrerequisites() == null)
											{
												//get all rule condition
												ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
											    		 
												if(!lstSpicaCondition.isEmpty()) {
														
													for(LstSpicaCondition condition : lstSpicaCondition) {
					
														Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
												    			 
														//put condition result to hashmap for evaluate rule
														conditionResultMap.put(condition.getLspc_condition_name(), result);
												    			 
														//insert into condition history
														services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
												    			 
														//untuk balikan result dari setiap condition
					//							    	HashMap<String, Object> conditionResult = new HashMap<>();
					//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
					//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
					//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
					//							    	conditionResult.put("result", result);
					//							    	conditionResultList.add(conditionResult);
												    }	
											    } else {
											    	error = true;
											    	message = "Form rule condition not found";
													logger.error(message);
																
													throw new Exception("Form rule condition not found");
											    }
												
												//evaluate rule expression 
												for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
													String key = entrySet.getKey();
													Object value = entrySet.getValue();
											    	         
													elProcessor.setValue(key, value);
												}
					
												Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
												
												//put rule result to map, for evalute prerequisites
												ruleResultMap.put(eval.getRule(), result);
		 
												HashMap<String, Object> ruleResult = new HashMap<>();
											    		 
												if(result) {
													ruleResult.put("rule_name", eval.getRule());
													ruleResult.put("rule_expression", eval.getExpression());
													ruleResult.put("rule_description", eval.getDescription());
													ruleResult.put("rule_category_id", eval.getRule_category_id());
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
													ruleResult.put("rule_name", eval.getRule());
											    	ruleResult.put("rule_expression", eval.getExpression());
											    	ruleResult.put("rule_category_id", eval.getRule_category_id());
											    	ruleResult.put("status", "Not Passed");
											    	ruleResult.put("error_message", eval.getError_message());
											    	ruleResult.put("rule_description", eval.getDescription());
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    } 
											    		 
												//insert into rule history
											    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
											} else {
												//evaluate rule prerequisites
												for (Map.Entry<String, Object> entrySet : ruleResultMap.entrySet()) {
													String key = entrySet.getKey();
													Object value = entrySet.getValue();
											    	         
													elProcessor.setValue(key, value);
												}
												
												Boolean resultPrerequisites = (Boolean) elProcessor.eval(eval.getPrerequisites());
												
												//if rule prerequisites == true then proceed condition, else set rule to false
												if(resultPrerequisites == true) {
													//get all rule condition
													ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
												    		 
													if(!lstSpicaCondition.isEmpty()) {
															
														for(LstSpicaCondition condition : lstSpicaCondition) {
						
															Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
													    			 
															//put condition result to hashmap for evaluate rule
															conditionResultMap.put(condition.getLspc_condition_name(), result);
													    			 
															//insert into condition history
															services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
													    			 
															//untuk balikan result dari setiap condition
						//							    	HashMap<String, Object> conditionResult = new HashMap<>();
						//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
						//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
						//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
						//							    	conditionResult.put("result", result);
						//							    	conditionResultList.add(conditionResult);
													    }	
												    } else {
												    	error = true;
												    	message = "Form rule condition not found";
														logger.error(message);
																	
														throw new Exception("Form rule condition not found");
												    }
													
													//evaluate rule expression 
													for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
														String key = entrySet.getKey();
														Object value = entrySet.getValue();
												    	         
														elProcessor.setValue(key, value);
													}
						
													Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
													
													//put rule result to map, for evalute prerequisites
													ruleResultMap.put(eval.getRule(), result);
			 
													HashMap<String, Object> ruleResult = new HashMap<>();
												    		 
													if(result) {
														ruleResult.put("rule_name", eval.getRule());
														ruleResult.put("rule_expression", eval.getExpression());
														ruleResult.put("rule_description", eval.getDescription());
														ruleResult.put("rule_category_id", eval.getRule_category_id());
														ruleResult.put("status", "Passed");
														ruleResult.put("rule_prerequisites", eval.getPrerequisites());
														ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
														ruleStatus.add(ruleResult);	
													} else {
														ruleResult.put("rule_name", eval.getRule());
												    	ruleResult.put("rule_expression", eval.getExpression());
												    	ruleResult.put("rule_category_id", eval.getRule_category_id());
												    	ruleResult.put("status", "Not Passed");
												    	ruleResult.put("error_message", eval.getError_message());
												    	ruleResult.put("rule_description", eval.getDescription());
												    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
												    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
												    	ruleStatus.add(ruleResult);
												    	
												    	//SET FLAG CLEAN TO FALSE
												    	clean = false;
												    } 
												    		 
													//insert into rule history
												    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
												} else {
													Boolean result = false;
													
													//put rule result to map, for evalute prerequisites
													ruleResultMap.put(eval.getRule(), result);
			 
													HashMap<String, Object> ruleResult = new HashMap<>();
												    		 
													if(result) {
														ruleResult.put("rule_name", eval.getRule());
														ruleResult.put("rule_expression", eval.getExpression());
														ruleResult.put("rule_description", eval.getDescription());
														ruleResult.put("rule_category_id", eval.getRule_category_id());
														ruleResult.put("status", "Passed");
														ruleResult.put("rule_prerequisites", eval.getPrerequisites());
														ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
														ruleStatus.add(ruleResult);	
													} else {
														ruleResult.put("rule_name", eval.getRule());
												    	ruleResult.put("rule_expression", eval.getExpression());
												    	ruleResult.put("rule_category_id", eval.getRule_category_id());
												    	ruleResult.put("status", "Not Passed");
												    	ruleResult.put("error_message", eval.getError_message());
												    	ruleResult.put("rule_description", eval.getDescription());
												    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
												    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
												    	ruleStatus.add(ruleResult);
												    	
												    	//SET FLAG CLEAN TO FALSE
												    	clean = false;
												    }
												}
											}
										} catch (Exception e) {
											resultErr = e.getMessage();
											logger.error("ERROR: " + resultErr);
										}
									}
									
									error = false;
									message = "Form has been evaluated successfully";
									//data.put("sorted_variable", variableSorted);
									data.put("variable_value", variableValue);
									//data.put("condition_result", conditionResultList);
									data.put("list_rule_status", ruleStatus);
									
									if(clean == true) {
										spica_result = "Clean";
										
										//update document for e-lions integration
										spajDocumentStatusProcess(primary_attribute.toString(), 1);
									} else {
										spica_result = "Unclean";
										
										//update document for e-lions integration
										spajDocumentStatusProcess(primary_attribute.toString(), 0);
									}
									
									//insert into form history
									services.insertMstSpicaFormHistory(form_id, primary_attribute.toString(), time_started, new Date(), user_id, spica_result, process_number);
									
									//call workflow engine
									//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
								
								} catch(Exception e) {
									error = true;
									resultErr = "Rule: "+ e.getMessage();
									message = resultErr;
									logger.error("ERROR: "+ resultErr);
									
									throw new Exception (message);
								}
							} else {
								error = true;
								message = "Form rule not found";
								logger.error(message);
							} 
						}
						//if form has no variables
						else {
							//get form rule list
							ArrayList<LstSpicaRule> lstSpicaRule = services.selectFormRule(form_id);
							     
							//check if form rule is found
							if(!lstSpicaRule.isEmpty()) {
							    	 
//								//ArrayList<Object> conditionResultList = new ArrayList<>();
//							    	 
//								ArrayList<Object> ruleStatus = new ArrayList<>();
//								
//								//import el processor engine to evaluate rule
//								ELProcessor elProcessor = new ELProcessor();
//							    	 
//								//evaluate rule condition
//								for(LstSpicaRule rule : lstSpicaRule) {
//							    		 
//									//get all rule condition
//									ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, rule.getLspc_rule_name());
//							    		 
//									if(!lstSpicaCondition.isEmpty()) {
//										
//										for(LstSpicaCondition condition : lstSpicaCondition) {
//		
//											Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
//								    			 
//											//put condition result to hashmap for evaluate rule
//											conditionResultMap.put(condition.getLspc_condition_name(), result);
//								    			 
//											//insert into condition history
//											services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id);
//								    			 
//											//untuk balikan result dari setiap condition
//		//						    		HashMap<String, Object> conditionResult = new HashMap<>();
//		//						    		conditionResult.put("condition_name", condition.getLspc_condition_name());
//		//						    		conditionResult.put("condition_expression", condition.getLspc_condition_expression());
//		//						    		conditionResult.put("rule_parent", condition.getLspc_rule_name());
//		//						    		conditionResult.put("result", result);
//		//						    		conditionResultList.add(conditionResult);
//								    	}
//							    	} else {
//							    		error = true;
//							    		message = "Form rule condition not found";
//										logger.error(message);
//												
//										throw new Exception("Form rule condition not found");
//							    	}
//							    		 
//									//evaluate rule expression 
//							    	for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
//							    		String key = entrySet.getKey();
//							    		Object value = entrySet.getValue();
//							    	         
//							    		elProcessor.setValue(key, value);
//							    	}
//		
//							    	Boolean result = (Boolean) elProcessor.eval(rule.getLspc_rule_expression());
//							    		 
//							    	HashMap<String, Object> ruleResult = new HashMap<>();
//							    		 
//							    	if(result) {
//							    		ruleResult.put("rule_name", rule.getLspc_rule_name());
//							    		ruleResult.put("status", "Passed");
//							    		ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//										ruleResult.put("rule_description", rule.getLspc_rule_description());
//							    			 
//							    		ruleStatus.add(ruleResult);	
//							    	} else {
//							    		ruleResult.put("rule_name", rule.getLspc_rule_name());
//							    		ruleResult.put("status", "Not Passed");
//							    		ruleResult.put("error_message", rule.getLspc_rule_error_msg());
//							    		ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//										ruleResult.put("rule_description", rule.getLspc_rule_description());
//							    			 
//							    		ruleStatus.add(ruleResult);
//							    	}
//							    	
//							    	//insert into rule history
//							    	services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), rule.getLspc_rule_expression(), ruleResult.get("status").toString(), user_id);
//							    }
//							    	 
//							    error = false;
//							    message = "Form has been evaluated successfully";
//							    //data.put("condition_result", conditionResultList);
//							    data.put("list_rule_status", ruleStatus);
							    
							    //RULE PREREQUISITES
								
								Graph<EvaluatorRule, DefaultEdge> directedGraphRule = new DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>(DefaultEdge.class);
								List<EvaluatorRule> engineRule = new ArrayList<EvaluatorRule>();
									
								//get all list rule
								for(LstSpicaRule rule : lstSpicaRule) {
									engineRule.add(new EvaluatorRule(rule.getLspc_rule_name(), rule.getLspc_rule_prerequisites(), rule.getLspc_rule_expression(), rule.getLspc_rule_error_msg(), rule.getLspc_rule_description(), rule.getLspc_rule_category_id()));
								}
									
								Map<String, EvaluatorRule> taskNameToTaskMapRule = engineRule.stream()
										.collect(Collectors.toMap(evaluatorrule -> evaluatorrule.getRule(), evaluatorrule -> evaluatorrule));
									
								//evaluate every rule prerequisites
								try {
									for (EvaluatorRule e : engineRule) {
										directedGraphRule.addVertex(e);
									    for (String dependency : e.getDependencyVariables()) {
									    	EvaluatorRule dependencyEval = taskNameToTaskMapRule.get(dependency);
									    	if(dependencyEval == null) {
									    		continue;
									    	}
									        directedGraphRule.addVertex(dependencyEval);
									        directedGraphRule.addEdge(dependencyEval, e);
									    }
									}
										 
									TopologicalOrderIterator<EvaluatorRule, DefaultEdge> sortitbaseddependencyrule = 
									new TopologicalOrderIterator<EvaluatorRule, DefaultEdge>((DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>) directedGraphRule);
									    
									//import el processor engine to evaluate rule
									ELProcessor elProcessor = new ELProcessor();
										
									ArrayList<Object> ruleStatus = new ArrayList<>();
										 
									//evalute & put rule result to hashmap
									while (sortitbaseddependencyrule.hasNext()) {
										try {
											EvaluatorRule eval = sortitbaseddependencyrule.next();
												
											if(eval.getPrerequisites() == null)
											{
												//get all rule condition
												ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
											    		 
												if(!lstSpicaCondition.isEmpty()) {
														
													for(LstSpicaCondition condition : lstSpicaCondition) {
					
														Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
												    			 
														//put condition result to hashmap for evaluate rule
														conditionResultMap.put(condition.getLspc_condition_name(), result);
												    			 
														//insert into condition history
														services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
												    			 
														//untuk balikan result dari setiap condition
					//							    	HashMap<String, Object> conditionResult = new HashMap<>();
					//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
					//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
					//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
					//							    	conditionResult.put("result", result);
					//							    	conditionResultList.add(conditionResult);
												    }	
											    } else {
											    	error = true;
											    	message = "Form rule condition not found";
													logger.error(message);
																
													throw new Exception("Form rule condition not found");
											    }
												
												//evaluate rule expression 
												for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
													String key = entrySet.getKey();
													Object value = entrySet.getValue();
											    	         
													elProcessor.setValue(key, value);
												}
					
												Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
												
												//put rule result to map, for evalute prerequisites
												ruleResultMap.put(eval.getRule(), result);
		 
												HashMap<String, Object> ruleResult = new HashMap<>();
											    		 
												if(result) {
													ruleResult.put("rule_name", eval.getRule());
													ruleResult.put("rule_expression", eval.getExpression());
													ruleResult.put("rule_description", eval.getDescription());
													ruleResult.put("rule_category_id", eval.getRule_category_id());
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
													ruleResult.put("rule_name", eval.getRule());
											    	ruleResult.put("rule_expression", eval.getExpression());
											    	ruleResult.put("rule_category_id", eval.getRule_category_id());
											    	ruleResult.put("status", "Not Passed");
											    	ruleResult.put("error_message", eval.getError_message());
											    	ruleResult.put("rule_description", eval.getDescription());
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    } 
											    		 
												//insert into rule history
											    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
											} else {
												//evaluate rule prerequisites
												for (Map.Entry<String, Object> entrySet : ruleResultMap.entrySet()) {
													String key = entrySet.getKey();
													Object value = entrySet.getValue();
											    	         
													elProcessor.setValue(key, value);
												}
												
												Boolean resultPrerequisites = (Boolean) elProcessor.eval(eval.getPrerequisites());
												
												//if rule prerequisites == true then proceed condition, else set rule to false
												if(resultPrerequisites == true) {
													//get all rule condition
													ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
												    		 
													if(!lstSpicaCondition.isEmpty()) {
															
														for(LstSpicaCondition condition : lstSpicaCondition) {
						
															Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
													    			 
															//put condition result to hashmap for evaluate rule
															conditionResultMap.put(condition.getLspc_condition_name(), result);
													    			 
															//insert into condition history
															services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
													    			 
															//untuk balikan result dari setiap condition
						//							    	HashMap<String, Object> conditionResult = new HashMap<>();
						//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
						//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
						//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
						//							    	conditionResult.put("result", result);
						//							    	conditionResultList.add(conditionResult);
													    }	
												    } else {
												    	error = true;
												    	message = "Form rule condition not found";
														logger.error(message);
																	
														throw new Exception("Form rule condition not found");
												    }
													
													//evaluate rule expression 
													for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
														String key = entrySet.getKey();
														Object value = entrySet.getValue();
												    	         
														elProcessor.setValue(key, value);
													}
						
													Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
													
													//put rule result to map, for evalute prerequisites
													ruleResultMap.put(eval.getRule(), result);
			 
													HashMap<String, Object> ruleResult = new HashMap<>();
												    		 
													if(result) {
														ruleResult.put("rule_name", eval.getRule());
														ruleResult.put("rule_expression", eval.getExpression());
														ruleResult.put("rule_description", eval.getDescription());
														ruleResult.put("rule_category_id", eval.getRule_category_id());
														ruleResult.put("status", "Passed");
														ruleResult.put("rule_prerequisites", eval.getPrerequisites());
														ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
														ruleStatus.add(ruleResult);	
													} else {
														ruleResult.put("rule_name", eval.getRule());
												    	ruleResult.put("rule_expression", eval.getExpression());
												    	ruleResult.put("rule_category_id", eval.getRule_category_id());
												    	ruleResult.put("status", "Not Passed");
												    	ruleResult.put("error_message", eval.getError_message());
												    	ruleResult.put("rule_description", eval.getDescription());
												    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
												    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
												    	ruleStatus.add(ruleResult);
												    	
												    	//SET FLAG CLEAN TO FALSE
												    	clean = false;
												    } 
												    		 
													//insert into rule history
												    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
												} else {
													Boolean result = false;
													
													//put rule result to map, for evalute prerequisites
													ruleResultMap.put(eval.getRule(), result);
			 
													HashMap<String, Object> ruleResult = new HashMap<>();
												    		 
													if(result) {
														ruleResult.put("rule_name", eval.getRule());
														ruleResult.put("rule_expression", eval.getExpression());
														ruleResult.put("rule_description", eval.getDescription());
														ruleResult.put("rule_category_id", eval.getRule_category_id());
														ruleResult.put("status", "Passed");
														ruleResult.put("rule_prerequisites", eval.getPrerequisites());
														ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
														ruleStatus.add(ruleResult);	
													} else {
														ruleResult.put("rule_name", eval.getRule());
												    	ruleResult.put("rule_expression", eval.getExpression());
												    	ruleResult.put("rule_category_id", eval.getRule_category_id());
												    	ruleResult.put("status", "Not Passed");
												    	ruleResult.put("error_message", eval.getError_message());
												    	ruleResult.put("rule_description", eval.getDescription());
												    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
												    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
												    			 
												    	ruleStatus.add(ruleResult);
												    	
												    	//SET FLAG CLEAN TO FALSE
												    	clean = false;
												    }
												}
											}
										} catch (Exception e) {
											resultErr = e.getMessage();
											logger.error("ERROR: " + resultErr);
										}
									}
									
									error = false;
									message = "Form has been evaluated successfully";
									//data.put("condition_result", conditionResultList);
									data.put("list_rule_status", ruleStatus);
									
									if(clean == true) {
										spica_result = "Clean";
										
										//update document for e-lions integration
										spajDocumentStatusProcess(primary_attribute.toString(), 1);
									} else {
										spica_result = "Unclean";
										
										//update document for e-lions integration
										spajDocumentStatusProcess(primary_attribute.toString(), 0);
									}
									
									//insert into form history
									services.insertMstSpicaFormHistory(form_id, primary_attribute.toString(), time_started, new Date(), user_id, spica_result, process_number);
									
									//call workflow engine
									//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
							    
								} catch(Exception e) {
									error = true;
									resultErr = "Rule: "+ e.getMessage();
									message = resultErr;
									logger.error("ERROR: "+ resultErr);
									
									throw new Exception (message);
								}
							} else {
								error = true;
							    message = "Form rule not found";
							    logger.error(message);
							}
						}
					}
					else {
						error = true;
						message = "Form Field not found";
						logger.error(message);
					}
				} else {
					error = true;
					message = "Form not found";
					logger.error(message);
				}
			} else if(evaluate_type == 2) {
				//Get Form
				LstSpicaForm lstSpicaForm = services.selectForm(form_id);
				
				//check if form is found
				if(lstSpicaForm != null) {
						
					//get form variable
					ArrayList<LstSpicaVariable> lstSpicaVariable = services.selectFormVariable(form_id);
						
					//check if form variable is found
					if(!lstSpicaVariable.isEmpty()) {
						Graph<Evaluator, DefaultEdge> directedGraph = new DirectedAcyclicGraph<Evaluator, DefaultEdge>(DefaultEdge.class);
						List<Evaluator> engine = new ArrayList<Evaluator>();
						//ArrayList<Object> variableSorted = new ArrayList<>();
						ArrayList<Object> variableValue = new ArrayList<>();
							
						//add all list variable to evaluator
						for(LstSpicaVariable variable : lstSpicaVariable) {
							engine.add(new Evaluator("V"+variable.getLspc_variable_name(), variable.getLspc_variable_expression()));
						}
							
						Map<String, Evaluator> taskNameToTaskMap = engine.stream()
					                .collect(Collectors.toMap(evaluator -> evaluator.getVariable(), evaluator -> evaluator));
							
						//evaluate every variable
						try {
							for (Evaluator e : engine) {
								directedGraph.addVertex(e);
							    for (String dependency : e.getDependencyVariables()) {
							    	Evaluator dependencyEval = taskNameToTaskMap.get(dependency);
							    	if(dependencyEval == null) {
							    		if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
								    		if(dependency.startsWith("F")) {
								    			if(fieldAndVariable.containsKey(dependency)){
								    				continue;
								    			} else {
									            	//get field name from expression
									            	field_name = dependency.substring(1);
									            	
									            	//add field name for query select
										            if(select_command == null) {
										            	select_command = field_name + ",";
										            } else {
										            	select_command = select_command + field_name + ",";
										            }
									    						
									            	continue;
								    			}
								            } else {
								            	continue;
								            }
								    	};
							    	}
							    	directedGraph.addVertex(dependencyEval);
							        directedGraph.addEdge(dependencyEval, e);
							    }
							}
							
							if(select_command != null) {
								//remove comma in the end of select command
								select_command = select_command.substring(0, select_command.length() - 1);
								
								//select field value and put into hashmap
								field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
								
								//put field value into field and variable hashmap to process
								for (Map.Entry<String, Object> entry : field.entrySet()) {
									
									if(fieldAndVariable.containsKey("F"+entry.getKey())) {
										continue;
									} else {
										fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
										
										//Insert into field history
						    			if(entry.getValue() != null) {
						    				services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id, process_number);
						    			} else {
						    				services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id, process_number);
						    			}
									}
							    }
							}
							
							TopologicalOrderIterator<Evaluator, DefaultEdge> sortitbaseddependency = 
									new TopologicalOrderIterator<Evaluator, DefaultEdge>((DirectedAcyclicGraph<Evaluator, DefaultEdge>) directedGraph);
							    
							//put variable & variable value to hashmap
							while (sortitbaseddependency.hasNext()) {
								try {
										
									Evaluator eval = sortitbaseddependency.next();
										
									//evaluate variable expression and put to fieldAndVariable hashmap
									fieldAndVariable.put(eval.getVariable(), utils.execute(eval.getExpression(), fieldAndVariable));
										
									//insert into variable history
									services.insertMstSpicaVariableHistory(form_id, primary_attribute.toString(), eval.getVariable(), eval.getExpression(), fieldAndVariable.get(eval.getVariable()).toString(), user_id, process_number);
										
									//untuk balikan sorted variable
	//								HashMap<String, Object> mapList = new HashMap<>();
	//								mapList.put("variable", eval.getVariable());
	//								mapList.put("expression", eval.getExpression());
	//								variableSorted.add(mapList);
										
									//untuk balikan value dari setiap variable
									HashMap<String, Object> variableResult = new HashMap<>();
									variableResult.put("variable", eval.getVariable());
									variableResult.put("value", fieldAndVariable.get(eval.getVariable()));
									variableValue.add(variableResult);								
								} catch (Exception e) {
									error = true;
									resultErr = e.getMessage();
									message = resultErr;
									logger.error("ERROR: " + resultErr);	
								}	
							}
						} catch (Exception e) {
							error = true;
							resultErr = "Variable: "+ e.getMessage();
							message = resultErr;
							logger.error("ERROR: "+ resultErr);
							
							throw new Exception (message);
						}
							     
						//get form rule list
						ArrayList<LstSpicaRule> lstSpicaRule = services.selectFormRule(form_id);
							     
						//check if form rule is found
						if(!lstSpicaRule.isEmpty()) {
							    	 
//							//ArrayList<Object> conditionResultList = new ArrayList<>();
//							    	 
//							ArrayList<Object> ruleStatus = new ArrayList<>();
//								
//							//import el processor engine to evaluate rule
//							ELProcessor elProcessor = new ELProcessor();
//							    	 
//							//evaluate rule condition
//							for(LstSpicaRule rule : lstSpicaRule) {
//							    		 
//								//get all rule condition
//								ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, rule.getLspc_rule_name());
//							    		 
//								if(!lstSpicaCondition.isEmpty()) {
//										
//									//kosongkan hashmap field & query select command
//									field = null;
//									select_command = null;
//										
//									for(LstSpicaCondition condition : lstSpicaCondition) {
//											
//										Evaluator evaluator = new Evaluator(condition.getLspc_condition_name(), condition.getLspc_condition_expression());
//	
//										for (String dependency : evaluator.getDependencyVariables()) {
//											if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
//												if(dependency.startsWith("F")) {
//													if(fieldAndVariable.containsKey(dependency)){
//														continue;
//											    	}else {			
//											    		field_name = dependency.substring(1);
//												           
//											    		//add field name for query select
//												        if(select_command == null) {
//												        	select_command = field_name + ",";
//												        } else {
//												        	select_command = select_command + field_name + ",";
//												        }
//												    						
//												        continue;
//											    	}
//												} else {
//													continue;
//												}
//											};
//										}
//								    }
//										
//									if(select_command != null) {
//										//remove comma in the end of select command
//										select_command = select_command.substring(0, select_command.length() - 1);
//											
//										//select field value and put into hashmap
//										field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
//											
//										//put field value into field and variable hashmap to process
//										for (Map.Entry<String, Object> entry : field.entrySet()) {
//												
//											if(fieldAndVariable.containsKey("F"+entry.getKey())) {
//												continue;
//											} else {
//												fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
//													
//												//Insert into field history
//									    		if(entry.getValue() != null) {
//									    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id);
//									    		} else {
//									    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id);
//									    		}
//											}
//										}
//									}
//										
//									for(LstSpicaCondition condition : lstSpicaCondition) {
//	
//										Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
//								    			 
//										//put condition result to hashmap for evaluate rule
//										conditionResultMap.put(condition.getLspc_condition_name(), result);
//								    			 
//										//insert into condition history
//										services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id);
//								    			 
//										//untuk balikan result dari setiap condition
//	//							    	HashMap<String, Object> conditionResult = new HashMap<>();
//	//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
//	//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
//	//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
//	//							    	conditionResult.put("result", result);
//	//							    	conditionResultList.add(conditionResult);
//								    }	
//							    } else {
//							    	error = true;
//							    	message = "Form rule condition not found";
//									logger.error(message);
//												
//									throw new Exception("Form rule condition not found");
//							    }
//							    		 
//								//evaluate rule expression	 
//								for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
//									String key = entrySet.getKey();
//									Object value = entrySet.getValue();
//							    	         
//									elProcessor.setValue(key, value);
//								}
//	
//								Boolean result = (Boolean) elProcessor.eval(rule.getLspc_rule_expression());
//							    		 
//								HashMap<String, Object> ruleResult = new HashMap<>();
//							    		 
//								if(result) {
//									ruleResult.put("rule_name", rule.getLspc_rule_name());
//									ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//									ruleResult.put("rule_description", rule.getLspc_rule_description());
//									ruleResult.put("status", "Passed");
//							    			 
//									ruleStatus.add(ruleResult);	
//								} else {
//									ruleResult.put("rule_name", rule.getLspc_rule_name());
//							    	ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//							    	ruleResult.put("status", "Not Passed");
//							    	ruleResult.put("error_message", rule.getLspc_rule_error_msg());
//							    	ruleResult.put("rule_description", rule.getLspc_rule_description());
//							    			 
//							    	ruleStatus.add(ruleResult);
//							    } 
//							    		 
//								//insert into rule history
//							    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), rule.getLspc_rule_expression(), ruleResult.get("status").toString(), user_id);
//							}
//							    	 
//							error = false;
//							message = "Form has been evaluated successfully";
//							//data.put("sorted_variable", variableSorted);
//							data.put("variable_value", variableValue);
//							//data.put("condition_result", conditionResultList);
//							data.put("list_rule_status", ruleStatus);
							
							//RULE PREREQUISITES
							
							Graph<EvaluatorRule, DefaultEdge> directedGraphRule = new DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>(DefaultEdge.class);
							List<EvaluatorRule> engineRule = new ArrayList<EvaluatorRule>();
								
							//get all list rule
							for(LstSpicaRule rule : lstSpicaRule) {
								engineRule.add(new EvaluatorRule(rule.getLspc_rule_name(), rule.getLspc_rule_prerequisites(), rule.getLspc_rule_expression(), rule.getLspc_rule_error_msg(), rule.getLspc_rule_description(), rule.getLspc_rule_category_id()));
							}
								
							Map<String, EvaluatorRule> taskNameToTaskMapRule = engineRule.stream()
									.collect(Collectors.toMap(evaluatorrule -> evaluatorrule.getRule(), evaluatorrule -> evaluatorrule));
								
							//evaluate every rule prerequisites
							try {
								for (EvaluatorRule e : engineRule) {
									directedGraphRule.addVertex(e);
								    for (String dependency : e.getDependencyVariables()) {
								    	EvaluatorRule dependencyEval = taskNameToTaskMapRule.get(dependency);
								    	if(dependencyEval == null) {
								    		continue;
								    	}
								        directedGraphRule.addVertex(dependencyEval);
								        directedGraphRule.addEdge(dependencyEval, e);
								    }
								}
									 
								TopologicalOrderIterator<EvaluatorRule, DefaultEdge> sortitbaseddependencyrule = 
								new TopologicalOrderIterator<EvaluatorRule, DefaultEdge>((DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>) directedGraphRule);
								    
								//import el processor engine to evaluate rule
								ELProcessor elProcessor = new ELProcessor();
									
								ArrayList<Object> ruleStatus = new ArrayList<>();
									 
								//evalute & put rule result to hashmap
								while (sortitbaseddependencyrule.hasNext()) {
									try {
										EvaluatorRule eval = sortitbaseddependencyrule.next();
											
										if(eval.getPrerequisites() == null)
										{
											//get all rule condition
											ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
										    		 
											if(!lstSpicaCondition.isEmpty()) {
													
												//kosongkan hashmap field & query select command
												field = null;
												select_command = null;
													
												for(LstSpicaCondition condition : lstSpicaCondition) {
														
													Evaluator evaluator = new Evaluator(condition.getLspc_condition_name(), condition.getLspc_condition_expression());
				
													for (String dependency : evaluator.getDependencyVariables()) {
														if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
															if(dependency.startsWith("F")) {
																if(fieldAndVariable.containsKey(dependency)){
																	continue;
														    	}else {			
														    		field_name = dependency.substring(1);
															           
														    		//add field name for query select
															        if(select_command == null) {
															        	select_command = field_name + ",";
															        } else {
															        	select_command = select_command + field_name + ",";
															        }
															    						
															        continue;
														    	}
															} else {
																continue;
															}
														};
													}
											    }
													
												if(select_command != null) {
													//remove comma in the end of select command
													select_command = select_command.substring(0, select_command.length() - 1);
													
													//select field value and put into hashmap
													field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
													
													//put field value into field and variable hashmap to process
													for (Map.Entry<String, Object> entry : field.entrySet()) {
														
														if(fieldAndVariable.containsKey("F"+entry.getKey())) {
															continue;
														} else {
															fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
																
															//Insert into field history
												    		if(entry.getValue() != null) {
												    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id, process_number);
												    		} else {
												    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id, process_number);
												    		}
														}
													}
												}
													
												for(LstSpicaCondition condition : lstSpicaCondition) {
				
													Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
											    			 
													//put condition result to hashmap for evaluate rule
													conditionResultMap.put(condition.getLspc_condition_name(), result);
											    			 
													//insert into condition history
													services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
											    			 
													//untuk balikan result dari setiap condition
				//							    	HashMap<String, Object> conditionResult = new HashMap<>();
				//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
				//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
				//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
				//							    	conditionResult.put("result", result);
				//							    	conditionResultList.add(conditionResult);
											    }	
										    } else {
										    	error = true;
										    	message = "Form rule condition not found";
												logger.error(message);
															
												throw new Exception("Form rule condition not found");
										    }
											
											//evaluate rule expression 
											for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
												String key = entrySet.getKey();
												Object value = entrySet.getValue();
										    	         
												elProcessor.setValue(key, value);
											}
				
											Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
											
											//put rule result to map, for evalute prerequisites
											ruleResultMap.put(eval.getRule(), result);
	 
											HashMap<String, Object> ruleResult = new HashMap<>();
										    		 
											if(result) {
												ruleResult.put("rule_name", eval.getRule());
												ruleResult.put("rule_expression", eval.getExpression());
												ruleResult.put("rule_description", eval.getDescription());
												ruleResult.put("rule_category_id", eval.getRule_category_id());
												ruleResult.put("status", "Passed");
										    			 
												ruleStatus.add(ruleResult);	
											} else {
												ruleResult.put("rule_name", eval.getRule());
										    	ruleResult.put("rule_expression", eval.getExpression());
										    	ruleResult.put("rule_category_id", eval.getRule_category_id());
										    	ruleResult.put("status", "Not Passed");
										    	ruleResult.put("error_message", eval.getError_message());
										    	ruleResult.put("rule_description", eval.getDescription());
										    			 
										    	ruleStatus.add(ruleResult);
										    	
										    	//SET FLAG CLEAN TO FALSE
										    	clean = false;
										    } 
										    		 
											//insert into rule history
										    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
										} else {
											//evaluate rule prerequisites
											for (Map.Entry<String, Object> entrySet : ruleResultMap.entrySet()) {
												String key = entrySet.getKey();
												Object value = entrySet.getValue();
										    	         
												elProcessor.setValue(key, value);
											}
											
											Boolean resultPrerequisites = (Boolean) elProcessor.eval(eval.getPrerequisites());
											
											//if rule prerequisites == true then proceed condition, else set rule to false
											if(resultPrerequisites == true) {
												//get all rule condition
												ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
											    		 
												if(!lstSpicaCondition.isEmpty()) {
														
													//kosongkan hashmap field & query select command
													field = null;
													select_command = null;
														
													for(LstSpicaCondition condition : lstSpicaCondition) {
															
														Evaluator evaluator = new Evaluator(condition.getLspc_condition_name(), condition.getLspc_condition_expression());
					
														for (String dependency : evaluator.getDependencyVariables()) {
															if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
																if(dependency.startsWith("F")) {
																	if(fieldAndVariable.containsKey(dependency)){
																		continue;
															    	}else {			
															    		field_name = dependency.substring(1);
																           
															    		//add field name for query select
																        if(select_command == null) {
																        	select_command = field_name + ",";
																        } else {
																        	select_command = select_command + field_name + ",";
																        }
																    						
																        continue;
															    	}
																} else {
																	continue;
																}
															};
														}
												    }
														
													if(select_command != null) {
														//remove comma in the end of select command
														select_command = select_command.substring(0, select_command.length() - 1);
														
														//select field value and put into hashmap
														field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
															
														//put field value into field and variable hashmap to process
														for (Map.Entry<String, Object> entry : field.entrySet()) {
																
															if(fieldAndVariable.containsKey("F"+entry.getKey())) {
																continue;
															} else {
																fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
																	
																//Insert into field history
													    		if(entry.getValue() != null) {
													    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id, process_number);
													    		} else {
													    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id, process_number);
													    		}
															}
														}
													}
														
													for(LstSpicaCondition condition : lstSpicaCondition) {
					
														Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
												    			 
														//put condition result to hashmap for evaluate rule
														conditionResultMap.put(condition.getLspc_condition_name(), result);
												    			 
														//insert into condition history
														services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
												    			 
														//untuk balikan result dari setiap condition
					//							    	HashMap<String, Object> conditionResult = new HashMap<>();
					//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
					//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
					//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
					//							    	conditionResult.put("result", result);
					//							    	conditionResultList.add(conditionResult);
												    }	
											    } else {
											    	error = true;
											    	message = "Form rule condition not found";
													logger.error(message);
																
													throw new Exception("Form rule condition not found");
											    }
												
												//evaluate rule expression 
												for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
													String key = entrySet.getKey();
													Object value = entrySet.getValue();
											    	         
													elProcessor.setValue(key, value);
												}
					
												Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
												
												//put rule result to map, for evalute prerequisites
												ruleResultMap.put(eval.getRule(), result);
		 
												HashMap<String, Object> ruleResult = new HashMap<>();
											    		 
												if(result) {
													ruleResult.put("rule_name", eval.getRule());
													ruleResult.put("rule_expression", eval.getExpression());
													ruleResult.put("rule_description", eval.getDescription());
													ruleResult.put("rule_category_id", eval.getRule_category_id());
													ruleResult.put("status", "Passed");
													ruleResult.put("rule_prerequisites", eval.getPrerequisites());
													ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
													ruleStatus.add(ruleResult);	
												} else {
													ruleResult.put("rule_name", eval.getRule());
											    	ruleResult.put("rule_expression", eval.getExpression());
											    	ruleResult.put("rule_category_id", eval.getRule_category_id());
											    	ruleResult.put("status", "Not Passed");
											    	ruleResult.put("error_message", eval.getError_message());
											    	ruleResult.put("rule_description", eval.getDescription());
											    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
											    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    } 
											    		 
												//insert into rule history
											    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
											} else {
												Boolean result = false;
												
												//put rule result to map, for evalute prerequisites
												ruleResultMap.put(eval.getRule(), result);
		 
												HashMap<String, Object> ruleResult = new HashMap<>();
											    		 
												if(result) {
													ruleResult.put("rule_name", eval.getRule());
													ruleResult.put("rule_expression", eval.getExpression());
													ruleResult.put("rule_description", eval.getDescription());
													ruleResult.put("rule_category_id", eval.getRule_category_id());
													ruleResult.put("status", "Passed");
													ruleResult.put("rule_prerequisites", eval.getPrerequisites());
													ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
													ruleStatus.add(ruleResult);	
												} else {
													ruleResult.put("rule_name", eval.getRule());
											    	ruleResult.put("rule_expression", eval.getExpression());
											    	ruleResult.put("rule_category_id", eval.getRule_category_id());
											    	ruleResult.put("status", "Not Passed");
											    	ruleResult.put("error_message", eval.getError_message());
											    	ruleResult.put("rule_description", eval.getDescription());
											    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
											    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    }
											}
										}
									} catch (Exception e) {
										resultErr = e.getMessage();
										logger.error("ERROR: " + resultErr);
									}
								}
								
								error = false;
								message = "Form has been evaluated successfully";
								//data.put("sorted_variable", variableSorted);
								data.put("variable_value", variableValue);
								//data.put("condition_result", conditionResultList);
								data.put("list_rule_status", ruleStatus);
								
								if(clean == true) {
									spica_result = "Clean";
									
									//update document for e-lions integration
									spajDocumentStatusProcess(primary_attribute.toString(), 1);
								} else {
									spica_result = "Unclean";
									
									//update document for e-lions integration
									spajDocumentStatusProcess(primary_attribute.toString(), 0);
								}
								
								//insert into form history
								services.insertMstSpicaFormHistory(form_id, primary_attribute.toString(), time_started, new Date(), user_id, spica_result, process_number);
								
								//call workflow engine
								//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
							
							} catch(Exception e) {
								error = true;
								resultErr = "Rule: "+ e.getMessage();
								message = resultErr;
								logger.error("ERROR: "+ resultErr);
								
								throw new Exception (message);
							}
						} else {
							error = true;
							message = "Form rule not found";
							logger.error(message);
						} 
					}
					//if form has no variables
					else {
						//get form rule list
						ArrayList<LstSpicaRule> lstSpicaRule = services.selectFormRule(form_id);
						     
						//check if form rule is found
						if(!lstSpicaRule.isEmpty()) {
						    	 
//							//ArrayList<Object> conditionResultList = new ArrayList<>();
//						    	 
//							ArrayList<Object> ruleStatus = new ArrayList<>();
//							
//							//import el processor engine to evaluate rule
//							ELProcessor elProcessor = new ELProcessor();
//						    	 
//							//evaluate rule condition
//							for(LstSpicaRule rule : lstSpicaRule) {
//						    		 
//								//get all rule condition
//								ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, rule.getLspc_rule_name());
//						    		 
//								if(!lstSpicaCondition.isEmpty()) {
//									//kosongkan hashmap field & query select command
//									field = null;
//									select_command = null;
//									
//									for(LstSpicaCondition condition : lstSpicaCondition) {
//										
//										Evaluator evaluator = new Evaluator(condition.getLspc_condition_name(), condition.getLspc_condition_expression());
//	
//										for (String dependency : evaluator.getDependencyVariables()) {
//											if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
//												if(dependency.startsWith("F")) {
//													if(fieldAndVariable.containsKey(dependency)){
//														continue;
//										    		}else {			
//											            field_name = dependency.substring(1);
//											           
//											            //add field name for query select
//											            if(select_command == null) {
//											            	select_command = field_name + ",";
//											            } else {
//											            	select_command = select_command + field_name + ",";
//											            }
//											    						
//											            continue;
//										    		}
//												} else {
//													continue;
//												}
//											};
//										}
//							    	}
//									
//									if(select_command != null) {
//										//remove comma in the end of select command
//										select_command = select_command.substring(0, select_command.length() - 1);
//										
//										//select field value and put into hashmap
//										field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
//										
//										//put field value into field and variable hashmap to process
//										for (Map.Entry<String, Object> entry : field.entrySet()) {
//											
//											if(fieldAndVariable.containsKey("F"+entry.getKey())) {
//												continue;
//											} else {
//												fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
//												
//												//Insert into field history
//								    			if(entry.getValue() != null) {
//								    				services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id);
//								    			} else {
//								    				services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id);
//								    			}
//											}
//									    }
//									}
//									
//									for(LstSpicaCondition condition : lstSpicaCondition) {
//	
//										Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
//							    			 
//										//put condition result to hashmap for evaluate rule
//										conditionResultMap.put(condition.getLspc_condition_name(), result);
//							    			 
//										//insert into condition history
//										services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id);
//							    			 
//										//untuk balikan result dari setiap condition
//	//						    		HashMap<String, Object> conditionResult = new HashMap<>();
//	//						    		conditionResult.put("condition_name", condition.getLspc_condition_name());
//	//						    		conditionResult.put("condition_expression", condition.getLspc_condition_expression());
//	//						    		conditionResult.put("rule_parent", condition.getLspc_rule_name());
//	//						    		conditionResult.put("result", result);
//	//						    		conditionResultList.add(conditionResult);
//							    	}
//						    	} else {
//						    		error = true;
//						    		message = "Form rule condition not found";
//									logger.error(message);
//											
//									throw new Exception("Form rule condition not found");
//						    	}
//						    		 
//								//evaluate rule expression 
//						    	for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
//						    		String key = entrySet.getKey();
//						    		Object value = entrySet.getValue();
//						    	         
//						    		elProcessor.setValue(key, value);
//						    	}
//	
//						    	Boolean result = (Boolean) elProcessor.eval(rule.getLspc_rule_expression());
//						    		 
//						    	HashMap<String, Object> ruleResult = new HashMap<>();
//						    		 
//						    	if(result) {
//						    		ruleResult.put("rule_name", rule.getLspc_rule_name());
//						    		ruleResult.put("status", "Passed");
//						    		ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//									ruleResult.put("rule_description", rule.getLspc_rule_description());
//						    			 
//						    		ruleStatus.add(ruleResult);	
//						    	} else {
//						    		ruleResult.put("rule_name", rule.getLspc_rule_name());
//						    		ruleResult.put("status", "Not Passed");
//						    		ruleResult.put("error_message", rule.getLspc_rule_error_msg());
//						    		ruleResult.put("rule_expression", rule.getLspc_rule_expression());
//									ruleResult.put("rule_description", rule.getLspc_rule_description());
//						    			 
//						    		ruleStatus.add(ruleResult);
//						    	}
//						    	
//						    	//insert into rule history
//						    	services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), rule.getLspc_rule_name(), rule.getLspc_rule_expression(), ruleResult.get("status").toString(), user_id);
//						    }
//						    	 
//						    error = false;
//						    message = "Form has been evaluated successfully";
//						    //data.put("condition_result", conditionResultList);
//						    data.put("list_rule_status", ruleStatus);
						    
						    //RULE PREREQUISITES
						    
							Graph<EvaluatorRule, DefaultEdge> directedGraphRule = new DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>(DefaultEdge.class);
							List<EvaluatorRule> engineRule = new ArrayList<EvaluatorRule>();
								
							//get all list rule
							for(LstSpicaRule rule : lstSpicaRule) {
								engineRule.add(new EvaluatorRule(rule.getLspc_rule_name(), rule.getLspc_rule_prerequisites(), rule.getLspc_rule_expression(), rule.getLspc_rule_error_msg(), rule.getLspc_rule_description(), rule.getLspc_rule_category_id()));
							}
								
							Map<String, EvaluatorRule> taskNameToTaskMapRule = engineRule.stream()
									.collect(Collectors.toMap(evaluatorrule -> evaluatorrule.getRule(), evaluatorrule -> evaluatorrule));
								
							//evaluate every rule prerequisites
							try {
								for (EvaluatorRule e : engineRule) {
									directedGraphRule.addVertex(e);
								    for (String dependency : e.getDependencyVariables()) {
								    	EvaluatorRule dependencyEval = taskNameToTaskMapRule.get(dependency);
								    	if(dependencyEval == null) {
								    		continue;
								    	}
								        directedGraphRule.addVertex(dependencyEval);
								        directedGraphRule.addEdge(dependencyEval, e);
								    }
								}
									 
								TopologicalOrderIterator<EvaluatorRule, DefaultEdge> sortitbaseddependencyrule = 
								new TopologicalOrderIterator<EvaluatorRule, DefaultEdge>((DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>) directedGraphRule);
								    
								//import el processor engine to evaluate rule
								ELProcessor elProcessor = new ELProcessor();
									
								ArrayList<Object> ruleStatus = new ArrayList<>();
									 
								//evalute & put rule result to hashmap
								while (sortitbaseddependencyrule.hasNext()) {
									try {
										EvaluatorRule eval = sortitbaseddependencyrule.next();
											
										if(eval.getPrerequisites() == null)
										{
											//get all rule condition
											ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
										    		 
											if(!lstSpicaCondition.isEmpty()) {
													
												//kosongkan hashmap field & query select command
												field = null;
												select_command = null;
													
												for(LstSpicaCondition condition : lstSpicaCondition) {
														
													Evaluator evaluator = new Evaluator(condition.getLspc_condition_name(), condition.getLspc_condition_expression());
				
													for (String dependency : evaluator.getDependencyVariables()) {
														if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
															if(dependency.startsWith("F")) {
																if(fieldAndVariable.containsKey(dependency)){
																	continue;
														    	}else {			
														    		field_name = dependency.substring(1);
															           
														    		//add field name for query select
															        if(select_command == null) {
															        	select_command = field_name + ",";
															        } else {
															        	select_command = select_command + field_name + ",";
															        }
															    						
															        continue;
														    	}
															} else {
																continue;
															}
														};
													}
											    }
													
												if(select_command != null) {
													//remove comma in the end of select command
													select_command = select_command.substring(0, select_command.length() - 1);
														
													//select field value and put into hashmap
													field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
														
													//put field value into field and variable hashmap to process
													for (Map.Entry<String, Object> entry : field.entrySet()) {
															
														if(fieldAndVariable.containsKey("F"+entry.getKey())) {
															continue;
														} else {
															fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
																
															//Insert into field history
												    		if(entry.getValue() != null) {
												    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id, process_number);
												    		} else {
												    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id, process_number);
												    		}
														}
													}
												}
													
												for(LstSpicaCondition condition : lstSpicaCondition) {
				
													Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
											    			 
													//put condition result to hashmap for evaluate rule
													conditionResultMap.put(condition.getLspc_condition_name(), result);
											    			 
													//insert into condition history
													services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
											    			 
													//untuk balikan result dari setiap condition
				//							    	HashMap<String, Object> conditionResult = new HashMap<>();
				//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
				//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
				//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
				//							    	conditionResult.put("result", result);
				//							    	conditionResultList.add(conditionResult);
											    }	
										    } else {
										    	error = true;
										    	message = "Form rule condition not found";
												logger.error(message);
															
												throw new Exception("Form rule condition not found");
										    }
											
											//evaluate rule expression 
											for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
												String key = entrySet.getKey();
												Object value = entrySet.getValue();
										    	         
												elProcessor.setValue(key, value);
											}
				
											Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
											
											//put rule result to map, for evalute prerequisites
											ruleResultMap.put(eval.getRule(), result);
	 
											HashMap<String, Object> ruleResult = new HashMap<>();
										    		 
											if(result) {
												ruleResult.put("rule_name", eval.getRule());
												ruleResult.put("rule_expression", eval.getExpression());
												ruleResult.put("rule_description", eval.getDescription());
												ruleResult.put("rule_category_id", eval.getRule_category_id());
												ruleResult.put("status", "Passed");
										    			 
												ruleStatus.add(ruleResult);	
											} else {
												ruleResult.put("rule_name", eval.getRule());
										    	ruleResult.put("rule_expression", eval.getExpression());
										    	ruleResult.put("rule_category_id", eval.getRule_category_id());
										    	ruleResult.put("status", "Not Passed");
										    	ruleResult.put("error_message", eval.getError_message());
										    	ruleResult.put("rule_description", eval.getDescription());
										    			 
										    	ruleStatus.add(ruleResult);
										    	
										    	//SET FLAG CLEAN TO FALSE
										    	clean = false;
										    } 
										    		 
											//insert into rule history
										    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
										} else {
											//evaluate rule prerequisites
											for (Map.Entry<String, Object> entrySet : ruleResultMap.entrySet()) {
												String key = entrySet.getKey();
												Object value = entrySet.getValue();
										    	         
												elProcessor.setValue(key, value);
											}
											
											Boolean resultPrerequisites = (Boolean) elProcessor.eval(eval.getPrerequisites());
											
											//if rule prerequisites == true then proceed condition, else set rule to false
											if(resultPrerequisites == true) {
												//get all rule condition
												ArrayList<LstSpicaCondition> lstSpicaCondition = services.selectRuleCondition(form_id, eval.getRule());
											    		 
												if(!lstSpicaCondition.isEmpty()) {
														
													//kosongkan hashmap field & query select command
													field = null;
													select_command = null;
														
													for(LstSpicaCondition condition : lstSpicaCondition) {
															
														Evaluator evaluator = new Evaluator(condition.getLspc_condition_name(), condition.getLspc_condition_expression());
					
														for (String dependency : evaluator.getDependencyVariables()) {
															if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
																if(dependency.startsWith("F")) {
																	if(fieldAndVariable.containsKey(dependency)){
																		continue;
															    	}else {			
															    		field_name = dependency.substring(1);
																           
															    		//add field name for query select
																        if(select_command == null) {
																        	select_command = field_name + ",";
																        } else {
																        	select_command = select_command + field_name + ",";
																        }
																    						
																        continue;
															    	}
																} else {
																	continue;
																}
															};
														}
												    }
														
													if(select_command != null) {
														//remove comma in the end of select command
														select_command = select_command.substring(0, select_command.length() - 1);
															
														//select field value and put into hashmap
														field = services.getValueField(select_command, lstSpicaForm.getLspc_form_source(), lstSpicaForm.getLspc_form_primary_attribute(), primary_attribute);
															
														//put field value into field and variable hashmap to process
														for (Map.Entry<String, Object> entry : field.entrySet()) {
																
															if(fieldAndVariable.containsKey("F"+entry.getKey())) {
																continue;
															} else {
																fieldAndVariable.put("F"+ entry.getKey(), entry.getValue());
																	
																//Insert into field history
													    		if(entry.getValue() != null) {
													    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), entry.getValue().toString(), user_id, process_number);
													    		} else {
													    			services.insertMstSpicaFieldHistory(form_id, primary_attribute.toString(), entry.getKey(), null, user_id, process_number);
													    		}
															}
														}
													}
														
													for(LstSpicaCondition condition : lstSpicaCondition) {
					
														Boolean result = (Boolean) utils.execute(condition.getLspc_condition_expression(), fieldAndVariable);
												    			 
														//put condition result to hashmap for evaluate rule
														conditionResultMap.put(condition.getLspc_condition_name(), result);
												    			 
														//insert into condition history
														services.insertMstSpicaConditionHistory(form_id, primary_attribute.toString(), eval.getRule(), condition.getLspc_condition_name(), condition.getLspc_condition_expression(), result.toString(), user_id, process_number);
												    			 
														//untuk balikan result dari setiap condition
					//							    	HashMap<String, Object> conditionResult = new HashMap<>();
					//							    	conditionResult.put("condition_name", condition.getLspc_condition_name());
					//							    	conditionResult.put("condition_expression", condition.getLspc_condition_expression());
					//							    	conditionResult.put("rule_parent", condition.getLspc_rule_name());
					//							    	conditionResult.put("result", result);
					//							    	conditionResultList.add(conditionResult);
												    }	
											    } else {
											    	error = true;
											    	message = "Form rule condition not found";
													logger.error(message);
																
													throw new Exception("Form rule condition not found");
											    }
												
												//evaluate rule expression 
												for (Map.Entry<String, Object> entrySet : conditionResultMap.entrySet()) {
													String key = entrySet.getKey();
													Object value = entrySet.getValue();
											    	         
													elProcessor.setValue(key, value);
												}
					
												Boolean result = (Boolean) elProcessor.eval(eval.getExpression());
												
												//put rule result to map, for evalute prerequisites
												ruleResultMap.put(eval.getRule(), result);
		 
												HashMap<String, Object> ruleResult = new HashMap<>();
											    		 
												if(result) {
													ruleResult.put("rule_name", eval.getRule());
													ruleResult.put("rule_expression", eval.getExpression());
													ruleResult.put("rule_description", eval.getDescription());
													ruleResult.put("rule_category_id", eval.getRule_category_id());
													ruleResult.put("status", "Passed");
													ruleResult.put("rule_prerequisites", eval.getPrerequisites());
													ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
													ruleStatus.add(ruleResult);	
												} else {
													ruleResult.put("rule_name", eval.getRule());
											    	ruleResult.put("rule_expression", eval.getExpression());
											    	ruleResult.put("rule_category_id", eval.getRule_category_id());
											    	ruleResult.put("status", "Not Passed");
											    	ruleResult.put("error_message", eval.getError_message());
											    	ruleResult.put("rule_description", eval.getDescription());
											    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
											    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    } 
											    		 
												//insert into rule history
											    services.insertMstSpicaRuleHistory(form_id, primary_attribute.toString(), eval.getRule(), eval.getExpression(), ruleResult.get("status").toString(), user_id, eval.getRule_category_id(), process_number);
											} else {
												Boolean result = false;
												
												//put rule result to map, for evalute prerequisites
												ruleResultMap.put(eval.getRule(), result);
		 
												HashMap<String, Object> ruleResult = new HashMap<>();
											    		 
												if(result) {
													ruleResult.put("rule_name", eval.getRule());
													ruleResult.put("rule_expression", eval.getExpression());
													ruleResult.put("rule_description", eval.getDescription());
													ruleResult.put("rule_category_id", eval.getRule_category_id());
													ruleResult.put("status", "Passed");
													ruleResult.put("rule_prerequisites", eval.getPrerequisites());
													ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
													ruleStatus.add(ruleResult);	
												} else {
													ruleResult.put("rule_name", eval.getRule());
											    	ruleResult.put("rule_expression", eval.getExpression());
											    	ruleResult.put("rule_category_id", eval.getRule_category_id());
											    	ruleResult.put("status", "Not Passed");
											    	ruleResult.put("error_message", eval.getError_message());
											    	ruleResult.put("rule_description", eval.getDescription());
											    	ruleResult.put("rule_prerequisites", eval.getPrerequisites());
											    	ruleResult.put("rule_prerequisites_result", resultPrerequisites);
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    }
											}
										}
									} catch (Exception e) {
										resultErr = e.getMessage();
										logger.error("ERROR: " + resultErr);
									}
								}
								
								error = false;
								message = "Form has been evaluated successfully";
								//data.put("condition_result", conditionResultList);
								data.put("list_rule_status", ruleStatus);
								
								if(clean == true) {
									spica_result = "Clean";
									
									//update document for e-lions integration
									spajDocumentStatusProcess(primary_attribute.toString(), 1);
								} else {
									spica_result = "Unclean";
									
									//update document for e-lions integration
									spajDocumentStatusProcess(primary_attribute.toString(), 0);
								}
								
								//insert into form history
								services.insertMstSpicaFormHistory(form_id, primary_attribute.toString(), time_started, new Date(), user_id, spica_result, process_number);
								
								//call workflow engine
								//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
							
							} catch(Exception e) {
								error = true;
								resultErr = "Rule: "+ e.getMessage();
								message = resultErr;
								logger.error("ERROR: "+ resultErr);
								
								throw new Exception (message);
							}  
						} else {
							error = true;
						    message = "Form rule not found";
						    logger.error(message);
						}
					}
				} else {
					error = true;
					message = "Form not found";
					logger.error(message);
				}
			}
		} catch (Exception e) {
			error = true;
			message = "Evaluate form error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);
		
		return response;
	
	}

	@PostMapping("/updateform")
	public ResponseData UpdateForm(@RequestBody RequestUpdateForm requestUpdateForm) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Integer form_id = requestUpdateForm.getForm_id();
		String form_type = requestUpdateForm.getForm_type();
		String form_description = requestUpdateForm.getForm_description();
		String form_source = requestUpdateForm.getForm_source();
		String form_primary_attribute = requestUpdateForm.getForm_primary_attribute();
		Integer user_id = requestUpdateForm.getUser_id();
		
		try {
			
			//Get form
			LstSpicaForm lstSpicaForm = services.selectForm(form_id);
			
			//check if form found
			if(lstSpicaForm != null) {
				
				//check if form source changed, if not then update form
				if(lstSpicaForm.getLspc_form_source().equalsIgnoreCase(form_source)) {
					//update form
					services.updateForm(form_id, form_type, form_description, form_source, form_primary_attribute, user_id);
					
					error = false;
					message = "Form has been updated successfully";
					data.put("form_id", form_id);
					data.put("form_type", form_type);
					data.put("form_description", form_description);
					data.put("form_source", form_source);
					data.put("form_primary_attribute", form_primary_attribute);
				}
				//if form source changed then delete previous form field then insert form field from new form source then update form  
				else {
					//delete previous form field
					services.deleteFormField(form_id);
					
					//Insert form field from new form source
					services.insertFormField(form_id, form_source, user_id);
						
					//update form
					services.updateForm(form_id, form_type, form_description, form_source, form_primary_attribute, user_id);
						
					error = false;
					message = "Form has been updated successfully";
					data.put("form_id", form_id);
					data.put("form_type", form_type);
					data.put("form_description", form_description);
					data.put("form_source", form_source);
					data.put("form_primary_attribute", form_primary_attribute);
				}
				
			} else {
				error = true;
				message = "Form not found";
				logger.error(message);
			}
			
		} catch (Exception e) {
			error = true;
			message = "Form update error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/updateformfield")
	public ResponseData UpdateFormField(@RequestBody RequestUpdateFormField requestUpdateFormField) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Integer form_id = requestUpdateFormField.getForm_id();
		String form_source = requestUpdateFormField.getForm_source();
		Integer user_id = requestUpdateFormField.getUser_id();
		
		try {
			
			//Get form
			LstSpicaForm lstSpicaForm = services.selectForm(form_id);
			
			//check if form found
			if(lstSpicaForm != null) {
				//Insert new form field, if there is new column in form source
				services.insertFormField(form_id, form_source, user_id);
				
				//Update form field flag active to not active, if there is column deleted in form source
				services.updateFormField(form_id, form_source, user_id);
						
				error = false;
				message = "Form Field has been updated successfully";
			} else {
				error = true;
				message = "Form not found";
				logger.error(message);
			}
		} catch (Exception e) {
			error = true;
			message = "Form Field update error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/updatevariable")
	public ResponseData UpdateVariable(@RequestBody RequestUpdateVariable requestUpdateVariable) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		String predefinedFunctionName = null;
		
		Integer form_id = requestUpdateVariable.getForm_id();
		String variable_name = requestUpdateVariable.getVariable_name();
		String variable_type = requestUpdateVariable.getVariable_type();
		String variable_description = requestUpdateVariable.getVariable_description();
		String variable_expression = requestUpdateVariable.getVariable_expression();
		Integer user_id = requestUpdateVariable.getUser_id();
		
		try {
			//extract predefined function name from variable expression
			predefinedFunctionName = utils.extractPredefinedFunction(variable_expression);
					
			//get predefined function detail
			LstSpicaPf lstSpicaPf = services.selectPredefinedFunction(predefinedFunctionName);
					
			//check if predefined function in expression is already defined
			if(lstSpicaPf != null) {	
				//check if predefined function type equals variable type, if equals then check dependency
				if(lstSpicaPf.getLspc_pf_type().equalsIgnoreCase(variable_type)) {
					//get all form variable to be evaluated
					ArrayList<LstSpicaVariable> lstSpicaVariable = services.selectFormVariable(form_id);
					
					Graph<Evaluator, DefaultEdge> directedGraph = new DirectedAcyclicGraph<Evaluator, DefaultEdge>(DefaultEdge.class);
					List<Evaluator> engine = new ArrayList<Evaluator>();
					
					//add submitted variable to be edit to list for dependency checking
					engine.add(new Evaluator("V"+variable_name, variable_expression));

					//add all list variable to evaluator
					for(LstSpicaVariable variable : lstSpicaVariable) {
						//except submitted variable to be edit
						if(!variable.getLspc_variable_name().equalsIgnoreCase(variable_name)) {
							engine.add(new Evaluator("V"+variable.getLspc_variable_name(), variable.getLspc_variable_expression()));
						}
					}
					
					Map<String, Evaluator> taskNameToTaskMap = engine.stream()
			                .collect(Collectors.toMap(evaluator -> evaluator.getVariable(), evaluator -> evaluator));
					
					//evaluate every variable before update
					try {
						for (Evaluator e : engine) {
							directedGraph.addVertex(e);
							for (String dependency : e.getDependencyVariables()) {
								Evaluator dependencyEval = taskNameToTaskMap.get(dependency);
					            if(dependencyEval == null) {
					            	if(dependency.startsWith("F") || dependency.startsWith("'") || dependency.matches("^[0-9]*$")) {
					            		continue;
					            	};
					            }
					            directedGraph.addVertex(dependencyEval);
					            directedGraph.addEdge(dependencyEval, e);
							}
						 }
						 
						 //update form variable
						 services.updateLstSpicaVariable(form_id, variable_name, variable_type, variable_description, variable_expression, user_id);
						 
						 //services.insertLstSpicaVariable(form_id, variable_name, variable_type, variable_description, variable_expression, user_id);
							
						 error = false;
						 message = "Variable has been updated successfully";
						 data.put("form_id", form_id);
						 data.put("variable_name", variable_name);
						 data.put("variable_type", variable_type);
						 data.put("variable_description", variable_description);
						 data.put("variable_expression", variable_expression);
					} catch (Exception e) {
						error = true;
						message = "Cyclic redundancy check error, please check & evaluate your variables";
						logger.error(message);
					}
				} else {
					error = true;
					message = "Variable type did not match with predefined function return type";
					logger.error(message);
				}
			} else {
				error = true;
				message = "Predefined function in variable expression not found, please check again";
				logger.error(message);
			}
		} catch (Exception e) {
			error = true;
			message = "Update variable error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/updaterule")
	public ResponseData UpdateRule(@RequestBody RequestUpdateRule requestUpdateRule) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Integer form_id = requestUpdateRule.getForm_id();
		String rule_name = requestUpdateRule.getRule_name();
		String rule_description = requestUpdateRule.getRule_description();
		String rule_expression = requestUpdateRule.getRule_expression();
		String rule_error_msg = requestUpdateRule.getRule_error_msg();
		Integer user_id = requestUpdateRule.getUser_id();
		String rule_prerequisites = requestUpdateRule.getRule_prerequisites();
		Integer rule_category_id = requestUpdateRule.getRule_category_id();
		
		try {
			if(rule_prerequisites == null || rule_prerequisites == "") {
				services.updateLstSpicaRule(form_id, rule_name, rule_description, rule_expression, rule_error_msg, user_id, rule_prerequisites, rule_category_id);
					
				error = false;
				message = "Rule has been updated successfully";
				data.put("form_id", form_id);
				data.put("rule_name", rule_name);
				data.put("rule_description", rule_description);
				data.put("rule_expression", rule_expression);
				data.put("rule_error_msg", rule_error_msg);
				data.put("rule_prerequisites", rule_prerequisites);
			} else {
				ArrayList<LstSpicaRule> lstSpicaRule = services.selectFormRule(form_id);
				
				Graph<EvaluatorRule, DefaultEdge> directedGraphRule = new DirectedAcyclicGraph<EvaluatorRule, DefaultEdge>(DefaultEdge.class);
				List<EvaluatorRule> engineRule = new ArrayList<EvaluatorRule>();
				
				//add updated rule to list for dependency checking
				engineRule.add(new EvaluatorRule(rule_name, rule_prerequisites, rule_expression, rule_error_msg, rule_description, rule_category_id));
					
				//get all list rule
				for(LstSpicaRule rule : lstSpicaRule) {
					//add every form rule to check dependency, except the one to be edited
					if(!rule.getLspc_rule_name().equalsIgnoreCase(rule_name)) {
					engineRule.add(new EvaluatorRule(rule.getLspc_rule_name(), rule.getLspc_rule_prerequisites(), rule.getLspc_rule_expression(), rule.getLspc_rule_error_msg(), rule.getLspc_rule_description(), rule.getLspc_rule_category_id()));
					}
				}
					
				Map<String, EvaluatorRule> taskNameToTaskMapRule = engineRule.stream()
						.collect(Collectors.toMap(evaluatorrule -> evaluatorrule.getRule(), evaluatorrule -> evaluatorrule));
					
				//evaluate every rule prerequisites
				try {
					for (EvaluatorRule e : engineRule) {
						directedGraphRule.addVertex(e);
					    for (String dependency : e.getDependencyVariables()) {
					    	EvaluatorRule dependencyEval = taskNameToTaskMapRule.get(dependency);
					    	if(dependencyEval == null) {
					    		continue;
					    	}
					        directedGraphRule.addVertex(dependencyEval);
					        directedGraphRule.addEdge(dependencyEval, e);
					    }
					}
					
					services.updateLstSpicaRule(form_id, rule_name, rule_description, rule_expression, rule_error_msg, user_id, rule_prerequisites, rule_category_id);
					
					error = false;
					message = "Rule has been updated successfully";
					data.put("form_id", form_id);
					data.put("rule_name", rule_name);
					data.put("rule_description", rule_description);
					data.put("rule_expression", rule_expression);
					data.put("rule_error_msg", rule_error_msg);
					data.put("rule_prerequisites", rule_prerequisites);
					data.put("rule_category_id", rule_category_id);
					
				} catch(Exception e) {
					error = true;
					message = "Cyclic redundancy check error, please check & evaluate your rule prerequisites";
					logger.error(message);
				}
			}
		} catch (Exception e) {
			error = true;
			message = "Update rule error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/updatecondition")
	public ResponseData UpdateCondition(@RequestBody RequestUpdateCondition requestUpdateCondition) {
		
		Boolean error = true;
		String message = null;
		String predefinedFunctionName = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		Integer form_id = requestUpdateCondition.getForm_id();
		String rule_name = requestUpdateCondition.getRule_name();
		String condition_name = requestUpdateCondition.getCondition_name();
		String condition_description = requestUpdateCondition.getCondition_description();
		String condition_expression = requestUpdateCondition.getCondition_expression();
		Integer user_id = requestUpdateCondition.getUser_id();
		
		try {
			//extract predefined function name from variable expression
			predefinedFunctionName = utils.extractPredefinedFunction(condition_expression);
				
			//get predefined function detail
			LstSpicaPf lstSpicaPf = services.selectPredefinedFunction(predefinedFunctionName);
				
			//check if predefined function in expression is already defined
			if(lstSpicaPf != null) {
				
				services.updateLstSpicaCondition(form_id, rule_name, condition_name, condition_expression, condition_description, user_id);
					
				error = false;
				message = "Condition has been updated successfully";
				data.put("form_id", form_id);
				data.put("rule_name", rule_name);
				data.put("condition_description", condition_description);
				data.put("condition_expression", condition_expression);
			} else {
				error = true;
				message = "Predefined function in condition expression not found, please check your condition expression";
				logger.error(message);
			}
		} catch (Exception e) {
			error = true;
			message = "Update condition error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@PostMapping("/savelist")
	public ResponseData SaveList(@RequestBody RequestSaveList requestSaveList) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		String list_name = requestSaveList.getList_name();
		String list_description = requestSaveList.getList_description();
		String list_source = requestSaveList.getList_source();
		String list_field_name = requestSaveList.getList_field_name();
		String list_field_type = requestSaveList.getList_field_type();
		Integer user_id = requestSaveList.getUser_id();
		
		try {
			
			LstSpicaList lstSpicaList = services.selectList(list_name);
			
			if(lstSpicaList != null) {
				error = true;
				message = "List name already exist, please choose another name";
			} else {
				
				services.insertLstSpicaList(list_name, list_description, list_source, list_field_name, list_field_type, user_id);
				
				error = false;
				message = "List has been created successfully";
				data.put("list_name", list_name);
				data.put("list_description", list_description);
				data.put("list_source", list_source);
				data.put("list_field_name", list_field_name);
				data.put("list_field_type", list_field_type);	
			}
		} catch (Exception e) {
			error = true;
			message = "Save list error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	}
	
	@PostMapping("/updatelist")
	public ResponseData UpdateList(@RequestBody RequestUpdateList requestUpdateList) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		String list_name = requestUpdateList.getList_name();
		String list_description = requestUpdateList.getList_description();
		String list_source = requestUpdateList.getList_source();
		String list_field_name = requestUpdateList.getList_field_name();
		String list_field_type = requestUpdateList.getList_field_type();
		Integer user_id = requestUpdateList.getUser_id();
		
		try {
			
			//Get list
			LstSpicaList lstSpicaList = services.selectList(list_name);
			
			//check if list found
			if(lstSpicaList != null) {
				
				services.updateLstSpicaList(list_name, list_description, list_source, list_field_name, list_field_type, user_id);
				
				error = false;
				message = "List has been updated successfully";
				data.put("list_name", list_name);
				data.put("list_description", list_description);
				data.put("list_source", list_source);
				data.put("list_field_name", list_field_name);
				data.put("list_field_type", list_field_type);
				
			} else {
				error = true;
				message = "List not found";
				logger.error(message);
			}
			
		} catch (Exception e) {
			error = true;
			message = "List update error: " + e;
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	
	}
	
	@GetMapping("/spicahistory")
	public ResponseData ViewSpicaHistory(@RequestParam String reg_spaj) {
		
		Boolean error = true;
		String message = null;
		HashMap<String, Object> data = new HashMap<String, Object>();
		
		try {
			if(reg_spaj == null) {
				error = true;
				message = "Reg SPAJ must be filled";
				data = null;
				
				logger.error("Reg SPAJ must be filled");
			} else {
				ArrayList<HashMap<String, Object>> spica_result = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> aml_result = new ArrayList<HashMap<String, Object>>();
				ArrayList<HashMap<String, Object>> ofac_result = new ArrayList<HashMap<String, Object>>();
				
				ArrayList<HashMap<String, Object>> spica = services.getSpicaHistory(reg_spaj);
				ArrayList<HashMap<String, Object>> aml = services.getAMLResult(reg_spaj);
				ArrayList<HashMap<String, Object>> ofac = services.getOFACResult(reg_spaj);
				
				for(HashMap<String, Object> spicaResult : spica) {
					HashMap<String, Object> spicaTemp = new HashMap<String, Object>();
					
					spicaTemp.put("REG_SPAJ", spicaResult.get("REG_SPAJ"));
					spicaTemp.put("RULE_NAME", spicaResult.get("RULE_NAME"));
					spicaTemp.put("RULE_DESCRIPTION", spicaResult.get("DESCRIPTION"));
					spicaTemp.put("RESULT", spicaResult.get("RESULT"));
					spicaTemp.put("CREATED_DATE", spicaResult.get("CREATED_DATE"));
					
					spica_result.add(spicaTemp);
				}
				
				for(HashMap<String, Object> ofacResult : ofac) {
					HashMap<String, Object> ofacTemp = new HashMap<String, Object>();
					
					ofacTemp.put("REG_SPAJ", ofacResult.get("REG_SPAJ"));
					ofacTemp.put("PROFILE", ofacResult.get("PROFILE"));
					ofacTemp.put("NAME", ofacResult.get("NAME"));
					ofacTemp.put("COUNTRY", ofacResult.get("COUNTRY"));
					ofacTemp.put("STATUS_MESSAGE", ofacResult.get("STATUS_MESSAGE"));
					
					ofac_result.add(ofacTemp);
				}
				
				for(HashMap<String, Object> amlResult : aml) {
					HashMap<String, Object> amlTemp = new HashMap<String, Object>();
					
					amlTemp.put("REG_SPAJ", amlResult.get("REG_SPAJ"));
					amlTemp.put("PROFILE", amlResult.get("PROFILE"));
					amlTemp.put("SCREENING_NAME", amlResult.get("SCREENING_NAME"));
					amlTemp.put("SCREENING_DESCRIPTION", amlResult.get("SCREENING_DESCRIPTION"));
					amlTemp.put("RESULT", amlResult.get("RESULT"));
					amlTemp.put("CREATED_DATE", amlResult.get("CREATED_DATE"));
					
					aml_result.add(amlTemp);
				}
				
				error = false;
				message = "Successfully get SPICA history";
				data.put("OFAC_RESULT_LIST", ofac_result);
				data.put("AML_RESULT_LIST", aml_result);
				data.put("SPICA_RESULT_LIST", spica_result);
			}	
		} catch (Exception e) {
			error = true;
			message = "Error:" + e.getMessage();
			
			logger.error(message);
		}
		
		ResponseData response = new ResponseData();
		response.setError(error);
		response.setMessage(message);
		response.setData(data);

		
		return response;
	}
	
	private void callSpicaWorfklowEngine(Object reg_spaj, Boolean clean, Integer user_id) {
		try {
			Integer role_id;
			String action;
			Integer lus_id;
			Integer state;
			Integer type_id;
			Integer parent_doc_id;
			Integer doc_id;
			Integer priority;
			String remarks;
			String request;
			
			if(clean == true) {
				//CALL SPICA CLEAN WORKFLOW ENGINE
				MstWfDocument mstWfDocument = services.selectDocByDocNumber(reg_spaj.toString());
				
				if(mstWfDocument != null) {
					type_id = 1;
					state = 2;
					action = "Clean";
					role_id = 3;
					parent_doc_id = mstWfDocument.getMswf_parent_doc_id();
					doc_id = mstWfDocument.getMswf_doc_id();
					lus_id = user_id; 
					priority = 30;
					remarks = "Process SPAJ SPICA Clean";
					request = "{\"source_orion\":\"1\"}";
					
					ResponseData responseWF = workflowEngineServices.processDocument(role_id, action, lus_id, state, type_id, doc_id, parent_doc_id, priority, remarks, request);
					
					if(responseWF.getError() == true) {
						logger.error("Hit Workflow Engine Services Error");
					} else {
						logger.info("Document number: " + reg_spaj.toString() +" processed successfully");
					}
				} else {
					logger.error("Document number: " + reg_spaj.toString() +" not found in MST_WF_DOCUMENT");
				}
			} else {
				//CALL SPICA UNCLEAN WORKFLOW ENGINE
				MstWfDocument mstWfDocument = services.selectDocByDocNumber(reg_spaj.toString());
				
				if(mstWfDocument != null) {
					type_id = 1;
					state = 2;
					action = "Unclean";
					role_id = 3;
					parent_doc_id = mstWfDocument.getMswf_parent_doc_id();
					doc_id = mstWfDocument.getMswf_doc_id();
					lus_id = user_id; 
					priority = 30;
					remarks = "Process SPAJ SPICA Unclean";
					request = "{\"source_orion\":\"1\"}";
					
					ResponseData responseWF = workflowEngineServices.processDocument(role_id, action, lus_id, state, type_id, doc_id, parent_doc_id, priority, remarks, request);
					
					if(responseWF.getError() == true) {
						logger.error("Hit Workflow Engine Services Error");
					} else {
						logger.info("Document number: " + reg_spaj.toString() +" processed successfully");
					}
				} else {
					logger.error("Document number: " + reg_spaj.toString() +" not found in MST_WF_DOCUMENT");
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	private void spajDocumentStatusProcess(String reg_spaj, Integer clean) {
		Integer lspd_id = null;
		Integer lssp_id = null;
		Integer lssa_id = null;
		Integer flag_speedy = null;
		Integer flag_qa = null;
		String description = null;
		
		try {
			//SELECT STATUS DOCUMENT
			DocumentStatus document_status = services.selectDocumentStatusStatic(reg_spaj);
			
			//PROSES CLEAN & UNCLEAN KE 1 LSPD_ID
			//IF SPICA CLEAN & TIDAK AUTODEBET
			if(clean == 1) {
				lspd_id = 218;
				lssp_id = document_status.getLssp_id();
				lssa_id = 17;
				flag_speedy = 1;
				flag_qa = 0;
				description = "TRANSFER KE QA (SPICA CLEAN)";
				
				//UPDATE LSPD ID IN MST_POLICY
				services.updateMstPolicyLspdId(reg_spaj, lspd_id);
				
				//UPDATE LSPD ID, LSSA ID, FLAG SPEEDY & FLAG QA IN MST_INSURED
				services.updateMstInsured(reg_spaj, lspd_id, lssa_id, flag_speedy);
				
				//INSERT INTO MST POSITION SPAJ
				services.insertMstPositionSpaj(reg_spaj, lspd_id, lssp_id,lssa_id, 0, description);
				
				
				//CEK VALIDASI AUTODEBET PREMI PERTAMA, JIKA IYA MAKA TRANSFER RECUR
				String reg_spaj_not_valid = services.selectValidasiAutodebetPremiPertama(reg_spaj);
				
				if(reg_spaj_not_valid == null) {
					autodebetServices.prosesAutoDebetNB(reg_spaj, 0, lspd_id, lssp_id, lssa_id);
					logger.info("SPAJ TRANSFERRED TO FINANCE FOR AUTODEBET PROCESS ("+ reg_spaj +")");
				}
				logger.info("SPAJ CLEAN ("+ reg_spaj +")");
			}
			//IF SPICA UNCLEAN
			else if(clean == 0) {
				lspd_id = 218;
				lssp_id = document_status.getLssp_id();
				lssa_id = 17;
				flag_speedy = 0;
				flag_qa = 0;
				description = "TRANSFER KE QA (SPICA UNCLEAN)";
				
				//UPDATE LSPD ID IN MST_POLICY
				services.updateMstPolicyLspdId(reg_spaj, lspd_id);
				
				//UPDATE LSPD ID, LSSA ID, FLAG SPEEDY & FLAG QA IN MST_INSURED
				services.updateMstInsured(reg_spaj, lspd_id, lssa_id, flag_speedy);
				
				//INSERT INTO MST POSITION SPAJ
				services.insertMstPositionSpaj(reg_spaj, lspd_id, lssp_id, lssa_id, 0, description);
				
				logger.info("SPAJ UNCLEAN ("+ reg_spaj +")");
	
			}
			
//			//IF SPICA CLEAN & TIDAK AUTODEBET
//			if(clean == 1) {
//				lspd_id = 218;
//				lssp_id = document_status.getLssp_id();
//				lssa_id = 17;
//				flag_speedy = 1;
//				flag_qa = 0;
//				description = "SPICA CLEAN";
//				
//				//UPDATE LSPD ID IN MST_POLICY
//				services.updateMstPolicyLspdId(reg_spaj, lspd_id);
//				
//				//UPDATE LSPD ID, LSSA ID, FLAG SPEEDY & FLAG QA IN MST_INSURED
//				services.updateMstInsured(reg_spaj, lspd_id, lssa_id, flag_speedy, flag_qa);
//				
//				//INSERT INTO MST POSITION SPAJ
//				services.insertMstPositionSpaj(reg_spaj, lspd_id, lssp_id,lssa_id, 0, description);
//				
//				
//				//PROSES TRANSFER RECURRING AUTODEBET PREMI PERTAMA
//				String reg_spaj_not_valid = services.selectValidasiAutodebetPremiPertama(reg_spaj);
//				
//				if(reg_spaj_not_valid == null) {
//					autodebetServices.prosesAutoDebetNB(reg_spaj, 0, lspd_id, lssp_id, lssa_id);
//					logger.info("SPAJ TRANSFERRED AUTODEBET PROCESSED TO FINANCE ("+ reg_spaj +")");
//				}
//				logger.info("SPAJ CLEAN ("+ reg_spaj +")");
//			}
//			//IF SPICA UNCLEAN
//			else if(clean == 0) {
//				lspd_id = 2;
//				lssp_id = document_status.getLssp_id();
//				lssa_id = 17;
//				flag_speedy = 0;
//				flag_qa = 0;
//				description = "TRANSFER KE UNDERWRITING (SPICA UNCLEAN)";
//				
//				//UPDATE LSPD ID IN MST_POLICY
//				services.updateMstPolicyLspdId(reg_spaj, lspd_id);
//				
//				//UPDATE LSPD ID, LSSA ID, FLAG SPEEDY & FLAG QA IN MST_INSURED
//				services.updateMstInsured(reg_spaj, lspd_id, lssa_id, flag_speedy, flag_qa);
//				
//				//INSERT INTO MST POSITION SPAJ
//				services.insertMstPositionSpaj(reg_spaj, lspd_id, lssp_id, lssa_id, 0, description);
//				
//				logger.info("SPAJ UNCLEAN ("+ reg_spaj +")");
//	
//			}
		} catch (Exception e) {
			logger.info(e);
		}
	}
	
	
}

