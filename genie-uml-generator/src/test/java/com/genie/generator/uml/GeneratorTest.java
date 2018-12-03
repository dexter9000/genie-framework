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

        String targetXmi = "/model/domain.xmi";
        String path = Generator.class.getResource("/").getPath();
        System.out.println(path + "generate");

        System.out.println("generate xmi...");
        XmiFileFormat.generate(path + "/model/Domain1.xmi",
            path + targetXmi,null);

        System.out.println("compiler mtl to emtl...");
        AcceleoStandaloneCompiler compiler = new AcceleoStandaloneCompiler();
        compiler.execute(path + "/mtl", path + "/mtl");

        URI modelURI = URI.createFileURI(path + targetXmi);

        File folder = new File(path + "generate");

        List<String> arguments = new ArrayList<>();

        System.out.println("generate code...");
        Generator generator = new Generator(modelURI, folder, arguments, "/mtl/generateClass.mtl");

        generator.doGenerate(new BasicMonitor());
    }


}
