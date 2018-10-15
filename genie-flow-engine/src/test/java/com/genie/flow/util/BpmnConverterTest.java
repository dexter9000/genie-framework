package com.genie.flow.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public class BpmnConverterTest {

    @Test
    public void testJsonConverter() throws IOException {

        String srcPath = "/test_data/bpmn.json";
        URL url = BpmnConverterTest.class.getResource(srcPath);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(url);
        BpmnModel model = converter.convertToBpmnModel(jsonNode);


        Process process = model.getMainProcess();
        System.out.println(process.getId());

        FlowElement flowElement = process.findFlowElementsOfType(StartEvent.class).get(0);
        System.out.println(flowElement.getId());

        List<SequenceFlow> sequenceFlows = ((FlowNode) flowElement).getOutgoingFlows();

        for (SequenceFlow flow : sequenceFlows) {
            System.out.println(flow.getTargetRef());
        }
    }

    @Test
    public void testXmlConverter() throws XMLStreamException, IOException {
        String srcPath = "/test_data/UC504.bpmn20.xml";
        InputStream input = BpmnConverterTest.class.getResource(srcPath).openStream();

        Reader in = new InputStreamReader(input);
        BpmnXMLConverter converter = new BpmnXMLConverter();
        XMLInputFactory xif = XMLInputFactory.newInstance();
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel model = converter.convertToBpmnModel(xtr);

        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        ObjectNode node = jsonConverter.convertToJson(model);


        System.out.println(node.toString());
    }
}
