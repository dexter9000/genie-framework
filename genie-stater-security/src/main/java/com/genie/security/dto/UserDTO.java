package com.genie.security.dto;

import com.alibaba.fastjson.JSONObject;


public class UserDTO {

    private String userId;

    private String username;

    private String appId;

    private JSONObject Roles;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public JSONObject getRoles() {
        return Roles;
    }

    public void setRoles(JSONObject roles) {
        Roles = roles;
    }
}
