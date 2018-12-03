package com.genie.flow.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.junit.Test;

import java.io.*;
import java.net.URL;

public class BpmnImage {

    @Test
    public void buildPng() throws IOException {
        ProcessDiagramGenerator generator = new DefaultProcessDiagramGenerator();

        String srcPath = "/test_data/bpmn.json";
        URL url = BpmnConverterTest.class.getResource(srcPath);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(url);
        BpmnModel model = converter.convertToBpmnModel(jsonNode);

        InputStream is = generator.generateJpgDiagram(model);

        String path = BpmnImage.class.getResource("/").getPath();
        File file = new File(path + "data.jpg");
        OutputStream outputStream = new FileOutputStream(file);


        byte[] buff = new byte[1024];
        int size = 0;
        while(size >= 0){
            outputStream.write(buff, 0, size);
             size = is.read(buff, 0, 1024);
        }

        outputStream.close();
        is.close();
    }
}
