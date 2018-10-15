package com.genie.flow.service;

import com.genie.flow.ProcessInstance;
import com.genie.flow.bpmn.parser.handler.BpmnParseHandler;
import com.genie.flow.bpmn.parser.handler.StartEventParseHandler;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuntimeServiceImpl implements RuntimeService {

    @Override
    public ProcessInstance startProcessInstance(String processDefinitionKey, Map<String, Object> variables) {
        BpmnModel model = new BpmnModel();

        FlowElement flowElement = model.getMainProcess().findFlowElementsOfType(StartEvent.class).get(0);

        StartEventParseHandler handler = new StartEventParseHandler();
        handler.parse(flowElement);

        return null;
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
