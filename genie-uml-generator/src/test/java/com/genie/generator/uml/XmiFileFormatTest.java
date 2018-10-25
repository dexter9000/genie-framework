package com.genie.generator.uml;

import org.junit.Test;

public class XmiFileFormatTest {

    @Test
    public void generate() {
        String path = Generator.class.getResource("/model").getPath();
        XmiFileFormat.generate(path + "/source.xmi",
            path + "/target.xmi",null);

    }
}
