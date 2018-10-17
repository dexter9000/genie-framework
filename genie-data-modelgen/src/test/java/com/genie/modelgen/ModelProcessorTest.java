package com.genie.modelgen;

import com.genie.modelgen.util.CompilationRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.genie.modelgen.util.TestUtil.*;
import static org.junit.Assert.assertTrue;

@RunWith(CompilationRunner.class)
public class ModelProcessorTest {

    @Test
    public void testGeneratedAnnotationGenerated() {
        assertMetamodelClassGeneratedFor( TestEntity.class );

        // need to check the source because @Generated is not a runtime annotation
        String metaModelSource = getMetaModelSourceAsString( TestEntity.class );

        dumpMetaModelSourceFor( TestEntity.class );
        String generatedString = "@Generated(value = \"org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor\", date = \"";

        assertTrue( "@Generated should also contain the date parameter.", metaModelSource.contains( generatedString ) );
    }
}
