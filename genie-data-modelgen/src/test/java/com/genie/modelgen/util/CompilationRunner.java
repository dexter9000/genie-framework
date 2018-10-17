package com.genie.modelgen.util;


import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.*;


public class CompilationRunner extends BlockJUnit4ClassRunner {
    private final List<Class<?>> testEntities;
    private final List<Class<?>> preCompileEntities;
    private final List<String> mappingFiles;
    private final Map<String, String> processorOptions;
    private final String packageName;
    private boolean ignoreCompilationErrors;


    public CompilationRunner(Class<?> clazz) throws InitializationError {
        super( clazz );
        this.testEntities = new ArrayList<Class<?>>();
        this.preCompileEntities = new ArrayList<Class<?>>();
        this.mappingFiles = new ArrayList<String>();
        this.processorOptions = new HashMap<String, String>();
        Package pkg = clazz.getPackage();
        this.packageName = pkg != null ? pkg.getName() : null;

        processWithClasses( clazz.getAnnotation( WithClasses.class ) );
        processWithMappingFiles( clazz.getAnnotation( WithMappingFiles.class ) );
        processOptions(
            clazz.getAnnotation( WithProcessorOption.class ),
            clazz.getAnnotation( WithProcessorOption.List.class )
        );

        ignoreCompilationErrors = clazz.getAnnotation( IgnoreCompilationErrors.class ) != null;
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Statement statement = super.methodBlock( method );
        processAnnotations( method );
        if ( !annotationProcessorNeedsToRun() ) {
            return statement;
        }

        return new CompilationStatement(
            statement,
            testEntities,
            preCompileEntities,
            mappingFiles,
            processorOptions,
            ignoreCompilationErrors
        );
    }

    private void processWithClasses(WithClasses withClasses) {
        if ( withClasses != null ) {
            Collections.addAll( testEntities, withClasses.value() );
            Collections.addAll( preCompileEntities, withClasses.preCompile() );
        }
    }

    private void processWithMappingFiles(WithMappingFiles withMappingFiles) {
        if ( withMappingFiles != null ) {
            String packageNameAsPath = TestUtil.fcnToPath( packageName );
            for ( String mappingFile : withMappingFiles.value() ) {
                mappingFiles.add( packageNameAsPath + TestUtil.RESOURCE_SEPARATOR + mappingFile );
            }
        }
    }

    private void processOptions(WithProcessorOption withProcessorOption,
                                WithProcessorOption.List withProcessorOptionsListAnnotation) {
        addOptions( withProcessorOption );
        if ( withProcessorOptionsListAnnotation != null ) {
            for ( WithProcessorOption option : withProcessorOptionsListAnnotation.value() ) {
                addOptions( option );
            }
        }
    }

    private void processAnnotations(FrameworkMethod method) {
        // configuration will be added to potential class level configuration
        processWithClasses( method.getAnnotation( WithClasses.class ) );
        processWithMappingFiles( method.getAnnotation( WithMappingFiles.class ) );
        processOptions(
            method.getAnnotation( WithProcessorOption.class ),
            method.getAnnotation( WithProcessorOption.List.class )
        );

        // overrides potential class level configuration
        ignoreCompilationErrors = method.getAnnotation( IgnoreCompilationErrors.class ) != null;
    }

    private void addOptions(WithProcessorOption withProcessorOptionsAnnotation) {
        if ( withProcessorOptionsAnnotation != null ) {
            processorOptions.put( withProcessorOptionsAnnotation.key(), withProcessorOptionsAnnotation.value() );
        }
    }

    private boolean annotationProcessorNeedsToRun() {
        return !testEntities.isEmpty() || !mappingFiles.isEmpty();
    }
}
