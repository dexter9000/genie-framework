package com.genie.core.config.apidoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Register Springfox plugins.
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnBean(Docket.class)
@AutoConfigureAfter(SwaggerAutoConfiguration.class)
public class SwaggerPluginsAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SwaggerPluginsAutoConfiguration.class);

//    @Configuration
//    @ConditionalOnClass(Pageable.class)
//    public static class SpringPagePluginConfiguration {
//
//        @Bean
//        @ConditionalOnMissingBean
//        public PageableParameterBuilderPlugin pageableParameterBuilderPlugin(TypeNameExtractor typeNameExtractor,
//                                                                             TypeResolver typeResolver) {
//            log.info("Create PageableParameterBuilderPlugin");
//            return new PageableParameterBuilderPlugin(typeNameExtractor, typeResolver);
//        }
//    }
}
