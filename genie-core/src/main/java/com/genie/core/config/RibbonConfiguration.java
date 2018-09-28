package com.genie.core.config;

import com.genie.core.config.ribbon.CustomHttpClientRibbonCommandFactory;
import com.genie.core.properties.RibbonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.RibbonCommandFactory;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Set;

public class RibbonConfiguration {

    @Autowired(required = false)
    private Set<FallbackProvider> zuulFallbackProviders = Collections.emptySet();

    @Autowired
    private RibbonProperties ribbonProperties;

    @Bean
    @ConditionalOnMissingBean
    public RibbonCommandFactory<?> ribbonCommandFactory(
        SpringClientFactory clientFactory, ZuulProperties zuulProperties) {
        return new CustomHttpClientRibbonCommandFactory(clientFactory, zuulProperties, zuulFallbackProviders);
    }
}
