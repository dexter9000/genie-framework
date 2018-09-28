package com.genie.security.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 签名工具类
 */
public class SignUtils {

    private SignUtils() {
    }

    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    public static String createSign(Map<String, String> uriVariables, String secret, Object body) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return createSign(uriVariables, secret, JSONObject.toJSONString(body));
    }

    /**
     * 创建签名
     *
     * @param uriVariables url参数
     * @param secret       秘钥
     * @param body         消息体，如果是对象需要转为JSON
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String createSign(Map<String, String> uriVariables, String secret, String body) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        List<String> paramList = new ArrayList<>();
        if (null != uriVariables && uriVariables.size() > 0) {
            uriVariables.forEach((key, value) -> {
                Collections.addAll(paramList, key + "=" + value);
            });
        }
        if (StringUtils.isNotBlank(body)) {
            Collections.addAll(paramList, "body=" + body + "");
        }

        Collections.sort(paramList);

        String paramStr = String.format("%s%s%s", secret, StringUtils.join(paramList, "&"), secret);
        log.debug("Origion text : " + paramStr);
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        byte[] bytes = instance.digest(paramStr.getBytes("UTF-8"));
        String result1 = Hex.encodeHexString(bytes).toUpperCase();
        log.debug(result1);
        String result2 = DigestUtils.sha256Hex(paramStr).toUpperCase();
        log.debug(result2);
        return result2;
    }

    /**
     * json对象转换为字符串
     *
     * @param jsonObject json对象
     * @return 字符串
     */
    public static String jsonObjectToString(JSONObject jsonObject) {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = jsonObject.keySet();
        List<String> keys = new ArrayList<String>(keySet);
        Collections.sort(keys);
        for (String key : keys) {
            Object ob = jsonObject.get(key);
            if (ob instanceof JSONObject) {
                JSONObject b = jsonObject.getJSONObject(key);
                sb.append("&").append(key).append("={").append(jsonObjectToString(b)).append("}");
            } else if (ob instanceof JSONArray) {
                JSONArray c = jsonObject.getJSONArray(key);
                sb.append("&").append(key).append("=[").append(jsonArrayToString(c)).append("]");
            } else {
                sb.append("&").append(key).append("=").append(jsonObject.getString(key));
            }

        }
        if (sb.length() > 0) {
            return sb.toString().substring(1);
        }
        return "";
    }

    /**
     * json数组转换为字符串
     *
     * @param jsonArray json数组
     * @return 字符串
     */
    public static String jsonArrayToString(JSONArray jsonArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            Object element = jsonArray.get(i);
            String str;
            if (element instanceof JSONObject) {
                str = jsonObjectToString((JSONObject) element);
            } else {
                str = element.toString();
            }
            list.add(str);
        }

        if (!list.isEmpty()) {
            Collections.sort(list);
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s).append(",");
            }
            String ss = sb.toString();
            return ss.substring(0, ss.length() - 1);
        }
        return "";
    }
}
