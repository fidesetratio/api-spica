package com.app.feignclient;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.app.response.ResponseData;

@Component
public class ServiceWorkflowEngineCallback implements ServiceWorkflowEngine {

	@Override
	public ResponseData processDocument(@RequestHeader ("role_id") Integer roleId,
			@RequestHeader ("action") String action, 
			@RequestHeader ("lus_id") Integer lusId, 
			@RequestHeader ("state") Integer state, 
			@RequestHeader ("type_id") Integer type_id, 
			@RequestHeader ("doc_id") Integer docId, 
			@RequestHeader ("parent_doc_id") Integer parentDocId,
			@RequestHeader ("priority") Integer priority,
			@RequestHeader ("remarks") String remarks,
			@RequestBody String json) {
		ResponseData responseData = new ResponseData();
		responseData.setError(true);
		responseData.setData(null);
		responseData.setMessage("Hit API Workflow Engine error");
		
		return responseData;
	}

}

