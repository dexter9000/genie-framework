package com.genie.security.client.feign;

import com.genie.security.util.SecurityUtils;
import com.genie.security.util.SignUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class ApimFeignRequestInterceptor implements RequestInterceptor {

    public static final String BEARER = "Bearer";
    public static final String AUTHORIZATION = "Authorization";

    private final String apiKey;
    private final String secret;
    private final String subscriptionKey;

    public ApimFeignRequestInterceptor(String apiKey, String secret, String subscriptionKey) {
        this.apiKey = apiKey;
        this.secret = secret;
        this.subscriptionKey = subscriptionKey;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // TODO
        String authorization = requestTemplate.headers().get("Authorization").stream().findFirst().get();
        if(StringUtils.isNotBlank(authorization)){
            requestTemplate.header("Authorization", SecurityUtils.getCurrentUserTokenLogin());
        }
        requestTemplate.header("Ocp-Apim-Subscription-Key", subscriptionKey);
        requestTemplate.header("Auth-Type", "ssofed");

        Map<String, Collection<String>> queries = requestTemplate.queries();
        queries.put("api_key", Collections.singleton(apiKey));
        queries.put("nonce_str", Collections.singleton(UUID.randomUUID().toString()));
        String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd hh:mm:ss");
        queries.put("timestamp", Collections.singleton(timestamp));

        Map<String, String> uriVariables = new HashMap<>();
        queries.entrySet().forEach(entry -> {
            uriVariables.put(entry.getKey(), entry.getValue().stream().findFirst().get());
        });
        try {
            String sign = SignUtils.createSign(uriVariables, secret, new String(requestTemplate.body()));
            queries.put("sign", Collections.singleton(sign));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
}
