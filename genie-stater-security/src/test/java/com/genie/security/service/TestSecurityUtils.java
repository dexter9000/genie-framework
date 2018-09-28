package com.genie.security.service;

import com.alibaba.fastjson.JSONObject;
import com.genie.security.TestUtil;
import com.genie.security.config.SecurityProperties;
import com.genie.security.dto.UserDTO;
import com.genie.security.util.HttpClient;
import com.genie.security.util.SecurityUtils;
import com.genie.security.util.SignUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.genie.security.util.SecurityUtils.HTTP_CONTENT_KEY;
import static com.genie.security.util.SecurityUtils.HTTP_STATUS_KEY;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClient.class})
@PowerMockIgnore( {"javax.management.*", "javax.net.ssl.*"})
public class TestSecurityUtils {

    String api_key = "cm-b8i90cd";
    String secret = "428dbf82c7b24923a8b0c94a4addcc40";

    String appId = "c2c75c2b-ca70-4302-a3fe-421f91f849fc";
    String subscriptionKey = "d99fbb3e10244254ba8315b5d78483ec";
    String baseUrl = "https://transitsharedqaapim0.azure-api.cn/dmp-bdhrbac/api/privileges/";
    String token = "Bearer eyJhbGciOiJSUzUxMiIsImtpZCI6IjEifQ.eyJzY29wZSI6WyJvcGVuaWQiLCJwcm9maWxlIl0sImNsaWVudF9pZCI6IkNoaW5hIFBhYVMgUGxhdGZvcm0iLCJVaWQiOiJETDY0MDkiLCJFbWFpbCI6Imxlby5jaGVuQG5lcXVhbC5jb20iLCJVc2VybmFtZSI6ImNoZW4uY2wuMiIsIkZpcnN0TmFtZSI6IkxlbyIsIlNob3J0TmFtZSI6ImNoZW4uY2wuMiIsIkxhc3ROYW1lIjoiQ2hlbiIsImV4cCI6MTUzMTM5OTY2NX0.mpGokz7_MXrXX6Sb5iYLYEFNZVsUavQijGJOlFoy-YqUEDdFi1R1180VCiNjeQE6YTzY2xi58XCprUsrzTId-d7AwLnvyxpOoEp6Qd20xgEetAW9FxJpEiNOQt7RTuAjjMjp6HWblPwj-sdvFfUHd624pLZd9eQtq16UhbVFwWJZqaipnUbyep7v_fggHkKUj2WFwIudrJaWfrh-pAP-Y4bge-32MfSVR8eSZm8Ig6A4-8k_zyOLMw7J_5mPSoEOl11NpJyq_VUQDXXOlWVXKjxjWemvY_D82dIZM2Rjn1eWD6zj-Z8qCjLRfslAIekaAzgX2CHiwmE13ySl5ZXC5Q";
    private SecurityUtils securityUtils;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        securityUtils = new SecurityUtils();
        SecurityProperties securityProperties = new SecurityProperties();
        securityProperties.setAppid(appId);
        securityProperties.setBaseurl(baseUrl);
        securityProperties.setSubscriptionKey(subscriptionKey);
        securityProperties.setApiKey(api_key);
        securityProperties.setSecret(secret);
        securityUtils.setSecurityProperties(securityProperties);

        mockHttpClient();
    }

    private void mockHttpClient() throws Exception {
        UserDTO userDTO = TestUtil.parseFile("/test_data/privileges.json", UserDTO.class);

        PowerMockito.mockStatic(HttpClient.class);
        Map<String, String> result = new HashMap<>();
        result.put(HTTP_STATUS_KEY, "200");
        result.put(HTTP_CONTENT_KEY, JSONObject.toJSONString(userDTO));
        PowerMockito.when(HttpClient.sendGet(anyString(), any(Map.class)))
                .thenReturn(result);

        securityUtils = PowerMockito.spy(securityUtils);
        PowerMockito.when(securityUtils, "sendRequest", anyString(), anyString(), anyString(), any(Map.class), any(HttpHeaders.class)).thenReturn(userDTO);
    }

    @Test
    public void getCurrentUserBrands() {
        Set<String> set = SecurityUtils.getCurrentUserBrands(null);
        System.out.println(JSONObject.toJSONString(set));
    }

    @Test
    public void getBrandTokenNull() {
        Map<String, Set<String>> setTokenNull = SecurityUtils.getSecurityByUserId(null, null);
        System.out.println(JSONObject.toJSONString(setTokenNull));
    }

    @Test
    public void getBrandNotBrank() {
        Map<String, Set<String>> setbrank = SecurityUtils.getSecurityByUserId(null, "123");
        System.out.println(JSONObject.toJSONString(setbrank));
    }

    @Test
    public void getBrandNotUserId() {
        Map<String, Set<String>> setUser = SecurityUtils.getSecurityByUserId("123", "123");
        System.out.println(JSONObject.toJSONString(setUser));
    }

    @Test
    public void getSecurityByUserId() {
        Map<String, Set<String>> userDTO = SecurityUtils.getSecurityByUserId("123", null);
        System.out.println(JSONObject.toJSONString(userDTO));
    }

    @Test
    public void getCurrentUserLogin() {
        String result = SecurityUtils.getCurrentUserLogin();
        System.out.println(result);
    }

    @Test
    public void getCurrentUserTokenLogin() {
        String result = SecurityUtils.getCurrentUserTokenLogin();
        System.out.println(result);
    }

    @Test
    public void getMehtod() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("nonce_str", "abc");
        map.put("timestamp", "2017-07-05 12:33:40");

        map.put("id", "5b42e596d4043f0007e4f4a9");

        String sign = SignUtils.createSign(map, secret, null);

        System.out.println("?api_key=" + api_key + "&nonce_str=abc&timestamp=2017-07-05 12:33:40&sign=" + sign);
    }

    @Test
    public void postMehtod() throws NoSuchAlgorithmException, UnsupportedEncodingException {

        Map<String, String> map = new HashMap<>();
        map.put("api_key", api_key);
        map.put("nonce_str", "abc");
        map.put("timestamp", "2017-07-05 12:33:40");

        JSONObject obj = new JSONObject();
        obj.put("action", "create");
        obj.put("campaignId", "001");
        obj.put("cron", "");
        obj.put("startTime", "2018-07-08");
        obj.put("endTime", "2018-07-15");
        obj.put("idType", "PHONENUMBER");
        obj.put("marketingProgram", 67);
        obj.put("segmentId", "5b435256f17fcc0007e8a25b");
        obj.put("taskId", "001");


        String sign = SignUtils.createSign(map, secret, obj.toJSONString());

        System.out.println("?api_key=" + api_key + "&nonce_str=abc&timestamp=2017-07-05 12:33:40&sign=" + sign);

        System.out.println("==============");
        String jsonStr = "{\"action\":\"create\",\"taskId\":\"2018071101\",\"campId\":\"5b20c0032314d61c6c6b849c\",\"taskType\":\"single\",\"marketingProgram\":67,\"rule\":{\"name\":\"skii\",\"type\":null,\"condition\":{\"profile\":{\"logicType\":\"AND\",\"filters\":[{\"field\":\"marketing_program\",\"op\":\"eq\",\"value\":[\"67\"]},{\"field\":\"reg_brand\",\"op\":\"eq\",\"value\":[\"pampers\"]}]},\"behaviors\":{\"logicType\":\"AND\",\"event\":[{\"actionType\":\"AND\",\"timerange\":\"180601‐180731\",\"filters\":[{\"field\":\"Average_reg_brand\",\"op\":\"lt\",\"value\":[\"ariel\"]}]}]}},\"exclude\":{\"profile\":{\"logicType\":\"AND\",\"filters\":[{\"field\":\"marketing_program\",\"op\":\"eq\",\"value\":[\"67\"]},{\"field\":\"reg_date\",\"op\":\"eq\",\"value\":[\"20170601\"]}]},\"behaviors\":{\"logicType\":\"AND\",\"event\":[{\"actionType\":\"AND\",\"timerange\":\"180601‐180731\",\"filters\":[{\"field\":\"Maximum_reg_brand\",\"op\":\"eq\",\"value\":[\"pampers\"]},{\"field\":\"Average_channel\",\"op\":\"neq\",\"value\":[\"QQ\"]}]}]}}},\"timerange\":\"170822‐170932\",\"triggerCron\":\"\",\"outputField\":[\"superid\",\"member_id\",\"marketing_program\",\"reg_brand\",\"reg_channel\",\"fullName\",\"reg_date\",\"gender\",\"birthday\",\"province\",\"city\",\"main_counter\",\"baby_birthday\",\"tier\",\"tier_join_date\",\"tier_expiry_date\",\"loyalty_point\",\"lifetimepoint\",\"pointsbalancebase\",\"tobeexpiredpoints\",\"wechat_openid\",\"wechat_unionid\",\"email_md5\",\"mobile_md5\",\"mobile_call_opt\",\"sms_opt\",\"wechat_opt\",\"bind_date_time\",\"terms_version\"]}";
        sign = SignUtils.createSign(map, secret, jsonStr);
        System.out.println("?api_key=" + api_key + "&nonce_str=abc&timestamp=2017-07-05 12:33:40&sign=" + sign);

    }

    @Test
    public void postAndGetMehtod() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        JSONObject obj = new JSONObject();
        obj.put("a", "a");
        obj.put("b", "b");
        obj.put("c", "c");
        JSONObject dObj = new JSONObject();
        obj.put("d", dObj);


        Map<String, String> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        map.put("c", "c");
        System.out.println(SignUtils.createSign(map, "secret", obj.toJSONString()));
    }

    @Test
    @Ignore
    public void sendMessage() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String apiKey = api_key;
        String subscriptionKey = "2eb43f1210754d7aa33cb293a43f3f0e";
        String message = "{\"action\":\"create\",\"campaignId\":\"001\",\"cron\":\"\",\"endTime\":\"2018-07-15\",\"idType\":\"PHONENUMBER\",\"marketingProgram\":67,\"segmentId\":\"5b435256f17fcc0007e8a25b\",\"startTime\":\"2018-07-08\",\"taskId\":\"001\"}";
        String url = "https://transitsharedqaapim0.azure-api.cn/cm-audience/api/audience/task/batch?api_key={api_key}&nonce_str={nonce_str}&timestamp={timestamp}&sign={sign}";
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpHeaders.add("Ocp-Apim-Subscription-Key", subscriptionKey);
        httpHeaders.add("Auth-Type", "ssofed");
        httpHeaders.add("Authorization", "Bearer test");
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<String> httpEntity = new HttpEntity<>(message, httpHeaders);

        Map<String, String> uriVariables = new HashMap<>();

        String nonceStr = "abc";
        String timestamp = "2018-07-11 11:50:25";

        uriVariables.put("api_key", apiKey);
        uriVariables.put("nonce_str", nonceStr);
        uriVariables.put("timestamp", timestamp);

        String sign = SignUtils.createSign(uriVariables, secret, message);

        System.out.println(sign);
//        uriVariables.put("eventTypeId", eventTypeId);
        uriVariables.put("sign", sign);

        System.out.println(url);
        System.out.println(uriVariables);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, String>>() {
        }, uriVariables);

        Map<String, String> responseEntityBody = responseEntity.getBody();
        System.out.println("responseEntity status:" + responseEntity.getStatusCode() + ",body:" + responseEntityBody);
    }


}
