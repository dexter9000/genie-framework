package com.genie.flow.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.genie.flow.bpmn.parser.handler.StartEventParseHandler;
import com.genie.flow.util.BpmnConverterTest;
import com.genie.flow.util.TestUtil;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class RuntimeServiceImplTest {

    RuntimeService service = new RuntimeServiceImpl();

    @Test
    public void test() throws IOException {
//        service.startProcessInstance();
        String srcPath = "/test_data/bpmn.json";
        String json = TestUtil.readFile(srcPath);
        BpmnJsonConverter converter = new BpmnJsonConverter();
        URL url = BpmnConverterTest.class.getResource(srcPath);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(json);

        BpmnModel bpmnModel = converter.convertToBpmnModel(jsonNode);

        FlowElement flowElement = bpmnModel.getMainProcess().findFlowElementsOfType(StartEvent.class).get(0);

        StartEventParseHandler handler = new StartEventParseHandler();
        handler.parse(flowElement);

    }
}
