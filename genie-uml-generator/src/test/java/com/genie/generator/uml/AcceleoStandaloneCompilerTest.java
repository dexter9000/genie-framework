package com.genie.generator.uml;

import org.junit.Test;

public class AcceleoStandaloneCompilerTest {

    @Test
    public void test(){
        String path = Generator.class.getResource("/").getPath();

        AcceleoStandaloneCompiler compiler = new AcceleoStandaloneCompiler();
        compiler.execute(path, path);
    }

}
