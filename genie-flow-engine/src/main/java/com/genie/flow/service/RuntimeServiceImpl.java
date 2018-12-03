package com.genie.flow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genie.flow.model.ProcessInstance;
import com.genie.flow.bpmn.parser.handler.BpmnParseHandler;
import com.genie.flow.bpmn.parser.handler.StartEventParseHandler;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuntimeServiceImpl implements RuntimeService {

    /**
     * 启动流程
     * @param processDefinitionKey
     * @param variables
     * @return
     */
    @Override
    public ProcessInstance startProcessInstance(String processDefinitionKey, Map<String, Object> variables) {
        BpmnModel model = new BpmnModel();

        Process process = model.getMainProcess();

        ProcessInstance processInstance = new ProcessInstance(process);
        return processInstance;
    }

    public ProcessInstance startProcessInstanceByJson(String json, Map<String, Object> variables) throws IOException {
        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);
        BpmnModel bpmnModel = converter.convertToBpmnModel(jsonNode);
        Process process = bpmnModel.getMainProcess();

        ProcessInstance processInstance = new ProcessInstance(process);
        return processInstance;
    }


    @Override
    public ProcessInstance startProcessInstanceByMessage(String messageName) {
        return null;
    }

    @Override
    public void messageEventReceived(String messageName, String executionId) {

    }


    public List<BpmnParseHandler> getDefaultBpmnParseHandlers(){
        List<BpmnParseHandler> bpmnParserHandlers = new ArrayList<>();

        bpmnParserHandlers.add(new StartEventParseHandler());


        return bpmnParserHandlers;
    }
}
