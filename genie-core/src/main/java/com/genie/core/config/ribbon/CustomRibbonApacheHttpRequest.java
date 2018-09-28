package com.genie.core.config.ribbon;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.springframework.cloud.netflix.ribbon.apache.RibbonApacheHttpRequest;
import org.springframework.cloud.netflix.ribbon.support.RibbonCommandContext;

import java.util.List;

import static org.springframework.cloud.netflix.ribbon.support.RibbonRequestCustomizer.Runner.customize;

public class CustomRibbonApacheHttpRequest extends RibbonApacheHttpRequest {

    public CustomRibbonApacheHttpRequest(RibbonCommandContext context) {
        super(context);
    }

    @Override
    public HttpUriRequest toRequest(RequestConfig requestConfig) {
        final RequestBuilder builder = RequestBuilder.create(this.context.getMethod());
        builder.setUri(this.uri);
        for (final String name : this.context.getHeaders().keySet()) {
            final List<String> values = this.context.getHeaders().get(name);
            for (final String value : values) {
                builder.addHeader(name, value);
            }
        }

        for (final String name : this.context.getParams().keySet()) {
            final List<String> values = this.context.getParams().get(name);
            for (final String value : values) {
                builder.addParameter(name, value);
            }
        }

        if (this.context.getRequestEntity() != null) {
            final BasicHttpEntity entity;
            entity = new BasicHttpEntity();
            entity.setContent(this.context.getRequestEntity());
            // if the entity contentLength isn't set, transfer-encoding will be set
            // to chunked in org.apache.http.protocol.RequestContent. See gh-1042
            if (this.context.getContentLength() != null) {
                entity.setContentLength(this.context.getContentLength());
            } else if ("GET".equals(this.context.getMethod())) {
                entity.setContentLength(0);
            }
            builder.setEntity(entity);
        }

        customize(this.context.getRequestCustomizers(), builder);

        //todo 这里处理个性的timeout信息
        if(uri.getPath().equals("/review/timeout")){
            RequestConfig.Builder configBuilder = RequestConfig.copy(builder.getConfig());
            configBuilder.setConnectionRequestTimeout(30*1000);
            configBuilder.setConnectTimeout(30*1000);
            configBuilder.setSocketTimeout(30*1000);
            builder.setConfig(configBuilder.build());
        }else{
            builder.setConfig(requestConfig);
        }

        return builder.build();
    }
}
