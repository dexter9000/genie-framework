package com.genie.flow.bpmn.parser.handler;

import org.flowable.bpmn.model.BaseElement;

public abstract class AbstractParseHandler<T extends BaseElement> implements BpmnParseHandler {

    public void parse(BaseElement element) {
        T baseElement = (T) element;
        executeParse(baseElement);
    }

    protected abstract void executeParse(T element);
}
