package com.genie.flow.service;

import com.genie.flow.model.ProcessInstance;

import java.util.Map;

public interface RuntimeService {

    ProcessInstance startProcessInstance(String processDefinitionKey, Map<String, Object> variables);

    ProcessInstance startProcessInstanceByMessage(String messageName);

    void messageEventReceived(String messageName, String executionId);

}
