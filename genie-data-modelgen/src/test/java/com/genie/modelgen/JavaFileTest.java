package com.genie.modelgen;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.Test;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

public class JavaFileTest {

    @Test
    public void test() throws IOException {

        FieldSpec fieldSpec = FieldSpec.builder(String.class, "name", Modifier.PRIVATE).build();

        MethodSpec main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(String.class)
            .build();

        TypeSpec spec = TypeSpec.classBuilder("HelloWorld")
            .addMethod(main)
            .addField(fieldSpec)
            .build();

        JavaFile javaFile = JavaFile.builder("com", spec).build();
        File file = new File("target/generated-sources/annotations");
        javaFile.writeTo(file.toPath());
    }
}
