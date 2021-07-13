package com.app.controller;

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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.app.feignclient.ServiceWorkflowEngine;
import com.app.model.DocumentStatus;
import com.app.model.LstSpicaCondition;
import com.app.model.LstSpicaField;
import com.app.model.LstSpicaForm;
import com.app.model.LstSpicaRule;
import com.app.model.LstSpicaVariable;
import com.app.model.MstWfDocument;
import com.app.request.RequestEvaluateForm;
import com.app.response.ResponseData;
import com.app.services.AutodebetServices;
import com.app.services.SpicaServices;
import com.app.utils.CommonUtils;
import com.app.utils.Evaluator;
import com.app.utils.EvaluatorRule;

@Component
public class SpicaKafkaController {
	
	@Autowired
	private SpicaServices services;
	
	@Autowired
	private CommonUtils utils;
	
	@Autowired
	private ServiceWorkflowEngine workflowEngineServices;
	
	@Autowired
	private AutodebetServices autodebetServices;
	
	private static Logger logger = Logger.getLogger(SpicaKafkaController.class);
	
	@KafkaListener(topics = "SPICAUAT", groupId = "uatspica2")
	public void EvaluateForm(String message) {
		String field_name = null;
		String select_command = null;
		Boolean clean = true;
		String spica_result = null;
		String error_message = null;
		Integer process_number = null;
		Integer no_temp = null;
		
		HashMap<String, Object> fieldAndVariable = new HashMap<String, Object>();
		HashMap<String, Object> field = new HashMap<String, Object>();
		HashMap<String, Object> conditionResultMap = new HashMap<String, Object>();
		HashMap<String, Object> ruleResultMap = new HashMap<String, Object>();
		
		//Parse param from kafka
		JSONObject spicaKafka = new JSONObject(message);
		
		Integer form_id = spicaKafka.getInt("form_id");
		if(spicaKafka.get("no_temp") != null || spicaKafka.get("no_temp") != "") {
			no_temp = spicaKafka.getInt("no_temp");
		}
		String app_name = spicaKafka.getString("app_name");
		String process_name = "SPICA";
		Object primary_attribute = spicaKafka.get("reg_spaj");
		Integer user_id = 0;
		Integer evaluate_type = 2;
		
		try {
			
			logger.info("SPICA started (" + primary_attribute +")");
			
			//set time started
			Date time_started = services.selectSysdate();
			
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
									} catch (Exception e) {

										error_message = e.getMessage();
										logger.error("ERROR: " + error_message);
											
									}
								}
							} catch (Exception e) {
								
								error_message = e.getMessage();
								logger.error("ERROR: "+ error_message);
								
								throw new Exception (error_message);
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
												    }	
											    } else {
											    	error_message = "Form rule condition not found";
													logger.error(error_message);
																
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
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
											    	ruleResult.put("status", "Not Passed");
											    			 
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
													    }	
												    } else {
												    	error_message = "Form rule condition not found";
														logger.error(error_message);
																	
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
														ruleResult.put("status", "Passed");
												    			 
														ruleStatus.add(ruleResult);	
													} else {
												    	ruleResult.put("status", "Not Passed");
												    			 
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
														ruleResult.put("status", "Passed");
												    			 
														ruleStatus.add(ruleResult);	
													} else {
												    	ruleResult.put("status", "Not Passed");
												    			 
												    	ruleStatus.add(ruleResult);
												    	
												    	//SET FLAG CLEAN TO FALSE
												    	clean = false;
												    }
												}
											}
										} catch (Exception e) {
											error_message = e.getMessage();
											logger.error("ERROR: " + error_message);
										}
									}
									
									logger.info("SPICA processed successfully (" + primary_attribute +")");
									
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
									
									//insert into spica process history
									services.insertSpicaProcessHistory(no_temp, primary_attribute.toString(), app_name, process_name, "SPICA processed successfully", null, process_number);
									
									//call workflow engine
									//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
								
								} catch(Exception e) {
									error_message = e.getMessage();
									logger.error("ERROR: "+ error_message);
									
									throw new Exception (error_message);
								}
							} else {
								error_message = "Form rule not found";
								logger.error(error_message);
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
												    }	
											    } else {
											    	error_message = "Form rule condition not found";
													logger.error(error_message);
																
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
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
											    	ruleResult.put("status", "Not Passed");
											    			 
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
													    }	
												    } else {
												    	error_message = "Form rule condition not found";
														logger.error(error_message);
																	
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
														ruleResult.put("status", "Passed");
												    			 
														ruleStatus.add(ruleResult);	
													} else {
												    	ruleResult.put("status", "Not Passed");
												    			 
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
														ruleResult.put("status", "Passed");
												    			 
														ruleStatus.add(ruleResult);	
													} else {
												    	ruleResult.put("status", "Not Passed");
												    			 
												    	ruleStatus.add(ruleResult);
												    	
												    	//SET FLAG CLEAN TO FALSE
												    	clean = false;
												    }
												}
											}
										} catch (Exception e) {
											error_message = e.getMessage();
											logger.error("ERROR: " + error_message);
										}
									}
									
									logger.info("SPICA processed successfully (" + primary_attribute +")");
									
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
									
									//insert into spica process history
									services.insertSpicaProcessHistory(no_temp, primary_attribute.toString(), app_name, process_name, "SPICA processed successfully", null, process_number);
									
									//call workflow engine
									//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
							    
								} catch(Exception e) {
									error_message = e.getMessage();
									logger.error("ERROR: "+ error_message);
									
									throw new Exception (error_message);
								}
							} else {
							    error_message = "Form rule not found";
							    logger.error(error_message);
							}
						}
					}
					else {
						error_message = "Form Field not found";
						logger.error(error_message);
					}
				} else {
					error_message = "Form not found";
					logger.error(error_message);
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
								} catch (Exception e) {
									error_message = e.getMessage();
									logger.error("ERROR: " + error_message);	
								}	
							}
						} catch (Exception e) {
							error_message = e.getMessage();
							logger.error("ERROR: "+ error_message);
							
							throw new Exception (error_message);
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
											    }	
										    } else {
										    	error_message = "Form rule condition not found";
												logger.error(error_message);
															
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
												ruleResult.put("status", "Passed");
										    			 
												ruleStatus.add(ruleResult);	
											} else {
										    	ruleResult.put("status", "Not Passed");
										    			 
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
												    }	
											    } else {
											    	error_message = "Form rule condition not found";
													logger.error(error_message);
																
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
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
											    	ruleResult.put("status", "Not Passed");
											    			 
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
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
											    	ruleResult.put("status", "Not Passed");
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    }
											}
										}
									} catch (Exception e) {
										error_message = e.getMessage();
										logger.error("ERROR: " + error_message);
									}
								}
								
								logger.info("SPICA processed successfully (" + primary_attribute +")");
								
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
								
								//insert into spica process history
								services.insertSpicaProcessHistory(no_temp, primary_attribute.toString(), app_name, process_name, "SPICA processed successfully", null, process_number);
								
								//call workflow engine
								//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
							
							} catch(Exception e) {
								error_message = e.getMessage();
								logger.error("ERROR: "+ error_message);
								
								throw new Exception (error_message);
							}
						} else {
							error_message = "Form rule not found";
							logger.error(error_message);
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
											    }	
										    } else {
										    	error_message = "Form rule condition not found";
												logger.error(error_message);
															
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
												ruleResult.put("status", "Passed");
										    			 
												ruleStatus.add(ruleResult);	
											} else {
										    	ruleResult.put("status", "Not Passed");
										    			 
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
												    }	
											    } else {
											    	error_message = "Form rule condition not found";
													logger.error(error_message);
																
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
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
											    	ruleResult.put("status", "Not Passed");
											    			 
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
													ruleResult.put("status", "Passed");
											    			 
													ruleStatus.add(ruleResult);	
												} else {
											    	ruleResult.put("status", "Not Passed");
											    			 
											    	ruleStatus.add(ruleResult);
											    	
											    	//SET FLAG CLEAN TO FALSE
											    	clean = false;
											    }
											}
										}
									} catch (Exception e) {
										error_message = e.getMessage();
										logger.error("ERROR: " + error_message);
									}
								}
								
								logger.info("SPICA processed successfully (" + primary_attribute +")");
								
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
								
								//insert into spica process history
								services.insertSpicaProcessHistory(no_temp, primary_attribute.toString(), app_name, process_name, "SPICA processed successfully", null, process_number);
								
								//call workflow engine
								//callSpicaWorfklowEngine(primary_attribute, clean, user_id);
							
							} catch(Exception e) {
								error_message = e.getMessage();
								logger.error("ERROR: "+ error_message);
								
								throw new Exception (error_message);
							}  
						} else {
						    error_message = "Form rule not found";
						    logger.error(error_message);
						}
					}
				} else {
					error_message = "Form not found";
					logger.error(error_message);
				}
			}
		} catch (Exception e) {;
			error_message = "Evaluate form error: " + e;
			logger.error(error_message);
			
			//insert into spica process history
			services.insertSpicaProcessHistory(no_temp, primary_attribute.toString(), app_name, process_name, "SPICA process error", error_message, null);
		}
	
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
//					logger.info("SPAJ TRANSFERRED TO FINANCE FOR AUTODEBET PROCESS ("+ reg_spaj +")");
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

