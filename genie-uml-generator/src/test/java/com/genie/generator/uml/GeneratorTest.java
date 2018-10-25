package com.genie.generator.uml;

import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneratorTest {

    @Test
    public void test() throws IOException {

        String path = Generator.class.getResource("/").getPath();
        System.out.println(path + "generate");

        XmiFileFormat.generate(path + "/model/source.xmi",
            path + "/model/domain.xmi",null);

        AcceleoStandaloneCompiler compiler = new AcceleoStandaloneCompiler();
        compiler.execute(path + "/mtl", path + "/mtl");

        URI modelURI = URI.createFileURI(path + "/model/domain.xmi");

        File folder = new File(path + "generate");

        List<String> arguments = new ArrayList<>();

        Generator generator = new Generator(modelURI, folder, arguments, "/mtl/generateClass.mtl");

        generator.doGenerate(new BasicMonitor());
    }


}
