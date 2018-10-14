package com.genie.es.entity;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class MappingPropertyTest {

    @Test
    public void test(){
        Map<String, MappingProperty> properties = new HashMap<>();
        MappingProperty property = new MappingProperty("integer");
        properties.put("amount", property);

        JSONObject jsonProperties = new JSONObject();
        jsonProperties.put("properties", properties);

        JSONObject json = new JSONObject();
        json.put("product", jsonProperties);

        System.out.println(json.toJSONString());
    }
}
