package com.genie.modelgen;

import com.genie.data.annotation.CriteriaEntity;
import com.genie.data.annotation.CriteriaField;
import com.genie.data.filter.*;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.genie.data.annotation.CriteriaEntity")
public class ModelProcessor extends AbstractProcessor {

    /**
     * Whether this processor claims all processed annotations exclusively or not.
     */
    private static final boolean ANNOTATIONS_CLAIMED_EXCLUSIVELY = false;

    String outPath = ModelProcessor.class.getResource("/").getPath() + "../generated-sources/annotations";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {

        if (!roundEnvironment.processingOver()) {
            printMessage("Generate Class...");
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(CriteriaEntity.class);

            for (Element element : elements) {
                handleRootElementAnnotationMirrors(element);
            }
        }
        return ANNOTATIONS_CLAIMED_EXCLUSIVELY;
    }

    public String getPackageName(Element type) {
        return type.toString().substring(0, type.toString().lastIndexOf('.'));
    }

    private String getClassName(Element element) {
        return element.getSimpleName().toString() + '_';
    }

    private void handleRootElementAnnotationMirrors(final Element element) {

        if (ElementKind.CLASS == element.getKind()) {

            printMessage(element.toString());

            String packageName = getPackageName(element);
            TypeSpec.Builder typeBuild = TypeSpec.classBuilder(getClassName(element))
                .addModifiers(Modifier.PUBLIC);

            element.getEnclosedElements().stream()
                .filter(e -> ElementKind.FIELD == e.getKind())
                .forEach(field -> {
                    CriteriaField a = field.getAnnotation(CriteriaField.class);
                    if (a != null) {
                        if (a.enabled()) {

                        } else {
                            // 不作为查询字段
                        }
                    } else {
                        // 默认条件
                        createField(typeBuild, field.getSimpleName().toString(), field.asType().toString());
                    }
                });
            JavaFile javaFile = JavaFile.builder(packageName, typeBuild.build()).build();

            try {


                javaFile.writeTo(new File(outPath).toPath());
            } catch (IOException e) {
                printMessage("Error: " + e.getMessage());
            }
        }
    }

    private void createField(TypeSpec.Builder builder, String fieldName, String fieldType) {
        Type filterType = null;
        if ("String".equals(fieldType)) filterType = StringFilter.class;
        if ("Integer".equals(fieldType)) filterType = IntegerFilter.class;
        if ("Long".equals(fieldType)) filterType = LongFilter.class;
        if ("Double".equals(fieldType)) filterType = DoubleFilter.class;
        if ("Float".equals(fieldType)) filterType = FloatFilter.class;
        if ("Short".equals(fieldType)) filterType = ShortFilter.class;
        if ("BigDecimal".equals(fieldType)) filterType = BigDecimalFilter.class;
        if ("Boolean".equals(fieldType)) filterType = BooleanFilter.class;

        if (filterType == null) {
            filterType = StringFilter.class;
        }
        builder.addField(FieldSpec.builder(filterType, fieldName, Modifier.PRIVATE).build());

        createGetMethodSpec(builder,fieldName, filterType);
    }

    private void createGetMethodSpec(TypeSpec.Builder builder, String fieldName, Type filterType){
        String code = "return this.$N;\n";
        String getMethodName = (new StringBuilder()).append("get")
            .append(Character.toUpperCase(fieldName.charAt(0)))
            .append(fieldName.substring(1)).toString();
        MethodSpec getMethod = MethodSpec.methodBuilder(getMethodName)
            .addModifiers(Modifier.PUBLIC)
            .returns(filterType)
            .addCode(code, fieldName)
            .build();

        builder.addMethod(getMethod);
    }

    private void printMessage(String message) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
    }
}
