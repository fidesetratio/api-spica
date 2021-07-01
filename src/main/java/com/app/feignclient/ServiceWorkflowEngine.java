package com.app.feignclient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.response.ResponseData;

@Component
@FeignClient(name = "api-workflow-engine", fallback = ServiceWorkflowEngineCallback.class)
public interface ServiceWorkflowEngine {

	@RequestMapping("/process")
	public ResponseData processDocument(
			@RequestHeader ("role_id") Integer roleId,
			@RequestHeader ("action") String action, 
			@RequestHeader ("lus_id") Integer lusId, 
			@RequestHeader ("state") Integer state, 
			@RequestHeader ("type_id") Integer type_id, 
			@RequestHeader ("doc_id") Integer docId, 
			@RequestHeader ("parent_doc_id") Integer parentDocId,
			@RequestHeader ("priority") Integer priority,
			@RequestHeader ("remarks") String remarks,
			@RequestBody String json);

}
