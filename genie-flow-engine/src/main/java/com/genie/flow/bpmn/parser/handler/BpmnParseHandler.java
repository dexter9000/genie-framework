package com.genie.flow.bpmn.parser.handler;

import org.flowable.bpmn.model.BaseElement;

public interface BpmnParseHandler {

    void parse(BaseElement element);
}
