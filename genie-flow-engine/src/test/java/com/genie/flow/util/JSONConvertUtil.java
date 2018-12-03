package com.genie.flow.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * json字符串和对象转换工具
 */
public class JSONConvertUtil {

    public JSONConvertUtil() {
    }

    /**
     * df
     *
     * @param jsonStr json字符串
     * @param clz     json对应的类型
     * @return 类型对应的实体类
     * @throws ClassNotFoundException
     */
    public static <T> T toJavaObject(String jsonStr, Class<T> clz, String clzKey) throws ClassNotFoundException {
        JSONObject jSONObject = JSONObject.parseObject(jsonStr);
        jsonObjAsEntity(jSONObject, clzKey);
        return JSONObject.parseObject(jSONObject.toJSONString(), clz);
    }

    /**
     * 字符转换成json列表
     * @param jsonStr
     * @param clzKey
     * @return
     * @throws ClassNotFoundException
     */
    public static List toJavaArray(String jsonStr, String clzKey) throws ClassNotFoundException {
        JSONArray jSONArray = JSONObject.parseArray(jsonStr);
        List list = new ArrayList();
        jsonArrAsEntity(jSONArray, list, clzKey);

        return list;
    }

    private static void jsonArrAsEntity(JSONArray jSONArray, List list, String clzKey) throws ClassNotFoundException {
        for (Object obj : jSONArray) {
            if (obj instanceof JSONObject) {
                JSONObject jsonObj = (JSONObject) obj;
                jsonObjAsEntity(jsonObj, clzKey);
                list.add(JSONObject.parseObject(JSONObject.toJSONString(jsonObj), Class.forName(jsonObj.getString(clzKey))));
            }
            if (obj instanceof JSONArray) {
                jsonArrAsEntity((JSONArray) obj, list, clzKey);
            }
        }
    }


    private static void jsonObjAsEntity(JSONObject json, String clzKey) throws ClassNotFoundException {
        List<String> keys = json.entrySet()
                .stream()
                .map(e -> e.getKey())
                .collect(Collectors.toList());

        for (String key : keys) {
            Object value = json.get(key);
            if (value instanceof JSONObject) {
                JSONObject jsonObj = (JSONObject) value;
                jsonObjAsEntity(jsonObj, clzKey);
                json.put(key, JSONObject.parseObject(JSONObject.toJSONString(jsonObj), Class.forName(jsonObj.getString(clzKey))));
            }

            if (value instanceof JSONArray) {
                List list = new ArrayList();
                for (Object obj : (JSONArray) value) {
                    if (obj instanceof String) {
                        list.add(obj);
                    } else if (obj instanceof JSONObject) {
                        JSONObject jsonObj = (JSONObject) obj;
                        jsonObjAsEntity(jsonObj, clzKey);
                        list.add(JSONObject.parseObject(JSONObject.toJSONString(jsonObj), Class.forName(jsonObj.getString(clzKey))));
                    } else {
                        jsonArrAsEntity((JSONArray) obj, list, clzKey);
                    }
                }
                json.put(key, list);
            }
        }
    }

}
