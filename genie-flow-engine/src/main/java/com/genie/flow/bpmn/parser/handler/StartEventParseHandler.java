package com.genie.flow.bpmn.parser.handler;

import org.flowable.bpmn.model.EventDefinition;
import org.flowable.bpmn.model.StartEvent;

public class StartEventParseHandler extends AbstractParseHandler<StartEvent> {

    @Override
    protected void executeParse(StartEvent element) {
        EventDefinition eventDefinition = element.getEventDefinitions().get(0);
    }
}
