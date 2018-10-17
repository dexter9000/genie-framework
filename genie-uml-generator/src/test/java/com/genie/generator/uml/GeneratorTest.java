package com.genie.generator.uml;

import org.eclipse.acceleo.common.utils.ModelUtils;
import org.eclipse.acceleo.engine.generation.strategy.PreviewStrategy;
import org.eclipse.acceleo.engine.service.AcceleoService;
import org.eclipse.acceleo.model.mtl.Module;
import org.eclipse.acceleo.model.mtl.resource.AcceleoResourceSetImpl;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeneratorTest {

    @Test
    public void test() throws IOException {

        String path = Generator.class.getResource("/").getPath();
        URI modelURI = URI.createFileURI(path + "model/model.xmi");
        File folder = new File(path + "generate");

        List<String> arguments = new ArrayList<String>();

        Generator generator = new Generator(modelURI, folder, arguments,"/mtl/readme.emtl");

//        for (int i = 2; i < args.length; i++) {
//            generator.addPropertiesFile(args[i]);
//        }
        generator.doGenerate(new BasicMonitor());

    }

    @Test
    public void test1() throws IOException {


        String path = "/data/xmi/standAloneGenerate.emtl";

        ResourceSet resourceSet = new AcceleoResourceSetImpl();
        URI moduleURI = URI.createFileURI(URI.decode(path));
        EObject eObject = ModelUtils.load(moduleURI, resourceSet);
        Module module = (Module)eObject;

        path = "/data/model.ecore";
        URI modelURI = URI.createFileURI(URI.decode(path));
        EObject myModel = ModelUtils.load(modelURI, resourceSet);

        AcceleoService service = new AcceleoService(new PreviewStrategy());
        Map<String, String> doGenerate = service.doGenerate(module, "generateElement", myModel, null, null);
    }
}
