package com.genie.security.util;

import com.alibaba.fastjson.JSONObject;
import com.genie.security.config.SecurityProperties;
import com.genie.security.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@Component
public class SecurityUtils {

    private static final Logger log = LoggerFactory.getLogger(SecurityUtils.class);

    private static ThreadLocal<String> tokenLocal = new ThreadLocal<>();
    private static Map<String, UserDTO> users = new HashMap<>();

    // HTTP请求时状态KEY
    public static final String HTTP_STATUS_KEY = "statusCode";
    public static final String HTTP_CONTENT_KEY = "content";
    public static final String AUTH_KEY = "privileges";
    public static final String MARKETING_PROGRAM = "marketing_program";

    private static SecurityProperties properties;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public void setSecurityProperties(SecurityProperties properties) {
        SecurityUtils.properties = properties;
    }

    /**
     * 通过当前用户token和userId查询用户信息
     * @param token 当前用户token，用于访问鉴权接口
     * @param userId 要查询的用户id
     * @return
     */
    public static UserDTO getUser(String token, String userId) {
        if(StringUtils.isBlank(token)){
            log.error("Token is blank!", token);
        }

        String url = properties.getBaseurl();
        if(StringUtils.isNotBlank(userId)){
            url = url.concat("/").concat(properties.getAppid());
        }
        url = url.concat("?api_key={api_key}&nonce_str={nonce_str}&timestamp={timestamp}&sign={sign}");
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("api_key", properties.getApiKey());
        uriVariables.put("nonce_str", UUID.randomUUID().toString());
        String timestamp = dateFormat.format(new Date());
        uriVariables.put("timestamp", timestamp);
        //签名
        try {
            String sign = SignUtils.createSign(uriVariables, properties.getSecret(), null);
            uriVariables.put("sign", sign);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error("createSign error!", e);
            return null;
        }
        //设置请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        httpHeaders.add("Auth-Type", "ssofed");
        httpHeaders.add("Authorization", token);
        httpHeaders.add("Content-Type", "application/json");
        //发送请求
        return sendRequest(token, userId, url, uriVariables, httpHeaders);
    }

    private static UserDTO sendRequest(String token,
                                       String userId,
                                       String url,
                                       Map<String, String> uriVariables,
                                       HttpHeaders httpHeaders) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserDTO> responseEntity = restTemplate.exchange(url,
            HttpMethod.GET,
            httpEntity,
            new ParameterizedTypeReference<UserDTO>() {},
            uriVariables);

        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return responseEntity.getBody();
        }else{
            log.error("Get User error by token [{}] and userId [{}]!", token, userId);
            return null;
        }
    }

    /**
     * 获取当前用户所有的品牌
     *
     * @param brand brandId
     */
    public static Set<String> getCurrentUserBrands(String brand) {
        Set<String> set = new HashSet<>();
        String token = tokenLocal.get();
        UserDTO userDTO = getUser(token, null);
        Map<String, Set<String>> map = getSecurityMap(userDTO, brand);
        for (String key : map.keySet()) {
            set.add(key);
        }
        return set;
    }

    /**
     * 根据用户ID获取权限
     *
     * @param userId userId
     * @param brand  brandId
     * @return
     */
    public static Map<String, Set<String>> getSecurityByUserId(String userId, String brand) {
        if (StringUtils.isBlank(userId)) {
            log.error("userId is null!");
            return new HashMap<>();
        }
        String token = tokenLocal.get();
        UserDTO userDTO = getUser(token, userId);
        if (null == userDTO) {
            log.error("Get user [{}] is null", userId);
            return new HashMap<>();
        }
        log.debug("userDTO:{}", userDTO);


        return getSecurityMap(userDTO, brand);
    }

    /**
     * 根据用户数据查询指定品牌的权限
     *
     * @param userDTO 用户数据
     * @param brand   品牌，如果品牌为空，则返回所有品牌的权限，否则返回指定品牌的权限
     * @return 权限集合，键是品牌id，值是权限集合， 当用户数据为空则返回空集合
     */
    private static Map<String, Set<String>> getSecurityMap(UserDTO userDTO, String brand) {
        if (userDTO == null) {
            log.error("UserDTO is null");
            return new HashMap<>();
        }
        JSONObject obj = userDTO.getRoles();
        Map<String, Set<String>> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : obj.entrySet()) {
            JSONObject roleJSONObject = JSONObject.parseObject(entry.getValue().toString());
            String privileges = roleJSONObject.get(AUTH_KEY).toString();
            JSONObject privilegesJSONObject = JSONObject.parseObject(privileges);
            Set<String> set = new HashSet<>();
            List<String> brandList = new ArrayList<>();
            for (Map.Entry<String, Object> priviEntry : privilegesJSONObject.entrySet()) {
                if (priviEntry.getValue() instanceof Boolean) {
                    set.add(priviEntry.getKey());
                } else if (priviEntry.getValue() instanceof JSONObject) {
                    JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(priviEntry.getValue()));
                    String brandStr = jsonObject.getString(MARKETING_PROGRAM);
                    if (StringUtils.isNotBlank(brandStr)) {
                        String[] brandArray = brandStr.split(",");
                        for (String temp : brandArray) {
                            brandList.add(temp.substring(temp.lastIndexOf(":") + 1));
                        }
                    }
                }
            }
            for (String brandKey : brandList) {
                if (map.containsKey(brandKey)) {
                    map.get(brandKey).addAll(set);
                } else {
                    map.put(brandKey, set);
                }
            }
        }

        // 解析返回的数据
        if (StringUtils.isBlank(brand)) {
            return map;
        } else {
            Map<String, Set<String>> brandMap = new HashMap<>();
            brandMap.put(brand, map.get(brand));
            return brandMap;
        }
    }

    public static void setCurrentUserLogin(String token) {
        tokenLocal.set(token);
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static String getCurrentUserLogin() {
        String token = tokenLocal.get();
        UserDTO user = users.get(token);
        if (user == null) {
            user = getUser(token, null);
            if (null == user) {
                log.error("get current user [{}] is null", token);
                return "";
            }
            users.put(token, user);
        }
        return user.getUserId();
    }


    /**
     * 获取当前用户token
     *
     * @return 当前用户token, 如果访问用户没有登录，则返回空字符串
     */
    public static String getCurrentUserTokenLogin() {
        return (tokenLocal.get() != null) ? tokenLocal.get() : "";
    }
}
