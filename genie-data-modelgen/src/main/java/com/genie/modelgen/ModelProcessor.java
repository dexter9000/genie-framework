package com.genie.modelgen;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.genie.data.annotation.CriteriaEntity")
public class ModelProcessor extends AbstractProcessor {

    /**
     * Whether this processor claims all processed annotations exclusively or not.
     */
    private static final boolean ANNOTATIONS_CLAIMED_EXCLUSIVELY = false;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        // nothing to do in the last round
        if (!roundEnvironment.processingOver()) {
            // process any mappers left over from previous rounds
            Set<? extends Element> elements = roundEnvironment.getRootElements();

            for (Element element : elements) {
                handleRootElementAnnotationMirrors(element);
            }
        }

        return ANNOTATIONS_CLAIMED_EXCLUSIVELY;
    }

    private void handleRootElementAnnotationMirrors(final Element element) {

    }
}
