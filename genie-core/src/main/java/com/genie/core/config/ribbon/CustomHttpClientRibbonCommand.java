package com.genie.core.config.ribbon;

import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest;
import org.springframework.cloud.netflix.ribbon.apache.RibbonLoadBalancingHttpClient;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.apache.HttpClientRibbonCommand;

public class CustomHttpClientRibbonCommand extends HttpClientRibbonCommand {

    public CustomHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context,
                                         ZuulProperties zuulProperties) {
        super(commandKey, client, context, zuulProperties);
    }

    public CustomHttpClientRibbonCommand(String commandKey, RibbonLoadBalancingHttpClient client, RibbonCommandContext context,
                                         ZuulProperties zuulProperties,
                                         FallbackProvider zuulFallbackProvider) {
        super(commandKey, client, context, zuulProperties, zuulFallbackProvider);
    }

    @Override
    protected RibbonApacheHttpRequest createRequest() throws Exception {
        RibbonApacheHttpRequest ribbonApacheHttpRequest = new CustomRibbonApacheHttpRequest(this.context);
        return ribbonApacheHttpRequest;
    }
}
