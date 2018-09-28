package com.genie.es.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.genie.es.annotation.ESIndex;
import com.genie.es.annotation.Field;

import java.time.ZonedDateTime;
import java.util.Date;

@ESIndex("member_profile")
public class Profile {

    private String superid;
    @Field("member_id")
    private String memberId;
    @JSONField(name = "marketing_program")
    private String marketingProgram;
    private String reg_brand;
    private String reg_channel;
    private String reg_source;
    private String gender;
    private String birthday;
    private String province;
    private String city;
    private String main_counter;
    private String baby_birthday;
    private String tier;
    private String tier_join_date;
    private String tier_expiry_date;
    private String points_loyalty;
    private String points_lifetime;
    private String points_balance_base;
    private String points_tobe_expired;
    private String wechat_openid;
    private String wechat_unionid;
    private String email_md5;
    private String mobile_md5;
    private String opt_mobile_call;
    private String opt_sms;
    private String opt_email;
    private String opt_wechat;
    private String bind_date_time;
    private String terms_version;
    @JSONField(name = "created_time")
    private Date createdTime;
    @JSONField(name = "modified_time", format="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Z", pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private ZonedDateTime modifiedTime;
    private String record_status;
    private String data_retention;
    private String task_id;
    private String customized_tag;

    public String getSuperid() {
        return superid;
    }

    public void setSuperid(String superid) {
        this.superid = superid;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMarketingProgram() {
        return marketingProgram;
    }

    public void setMarketingProgram(String marketingProgram) {
        this.marketingProgram = marketingProgram;
    }

    public String getReg_brand() {
        return reg_brand;
    }

    public void setReg_brand(String reg_brand) {
        this.reg_brand = reg_brand;
    }

    public String getReg_channel() {
        return reg_channel;
    }

    public void setReg_channel(String reg_channel) {
        this.reg_channel = reg_channel;
    }

    public String getReg_source() {
        return reg_source;
    }

    public void setReg_source(String reg_source) {
        this.reg_source = reg_source;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMain_counter() {
        return main_counter;
    }

    public void setMain_counter(String main_counter) {
        this.main_counter = main_counter;
    }

    public String getBaby_birthday() {
        return baby_birthday;
    }

    public void setBaby_birthday(String baby_birthday) {
        this.baby_birthday = baby_birthday;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getTier_join_date() {
        return tier_join_date;
    }

    public void setTier_join_date(String tier_join_date) {
        this.tier_join_date = tier_join_date;
    }

    public String getTier_expiry_date() {
        return tier_expiry_date;
    }

    public void setTier_expiry_date(String tier_expiry_date) {
        this.tier_expiry_date = tier_expiry_date;
    }

    public String getPoints_loyalty() {
        return points_loyalty;
    }

    public void setPoints_loyalty(String points_loyalty) {
        this.points_loyalty = points_loyalty;
    }

    public String getPoints_lifetime() {
        return points_lifetime;
    }

    public void setPoints_lifetime(String points_lifetime) {
        this.points_lifetime = points_lifetime;
    }

    public String getPoints_balance_base() {
        return points_balance_base;
    }

    public void setPoints_balance_base(String points_balance_base) {
        this.points_balance_base = points_balance_base;
    }

    public String getPoints_tobe_expired() {
        return points_tobe_expired;
    }

    public void setPoints_tobe_expired(String points_tobe_expired) {
        this.points_tobe_expired = points_tobe_expired;
    }

    public String getWechat_openid() {
        return wechat_openid;
    }

    public void setWechat_openid(String wechat_openid) {
        this.wechat_openid = wechat_openid;
    }

    public String getWechat_unionid() {
        return wechat_unionid;
    }

    public void setWechat_unionid(String wechat_unionid) {
        this.wechat_unionid = wechat_unionid;
    }

    public String getEmail_md5() {
        return email_md5;
    }

    public void setEmail_md5(String email_md5) {
        this.email_md5 = email_md5;
    }

    public String getMobile_md5() {
        return mobile_md5;
    }

    public void setMobile_md5(String mobile_md5) {
        this.mobile_md5 = mobile_md5;
    }

    public String getOpt_mobile_call() {
        return opt_mobile_call;
    }

    public void setOpt_mobile_call(String opt_mobile_call) {
        this.opt_mobile_call = opt_mobile_call;
    }

    public String getOpt_sms() {
        return opt_sms;
    }

    public void setOpt_sms(String opt_sms) {
        this.opt_sms = opt_sms;
    }

    public String getOpt_email() {
        return opt_email;
    }

    public void setOpt_email(String opt_email) {
        this.opt_email = opt_email;
    }

    public String getOpt_wechat() {
        return opt_wechat;
    }

    public void setOpt_wechat(String opt_wechat) {
        this.opt_wechat = opt_wechat;
    }

    public String getBind_date_time() {
        return bind_date_time;
    }

    public void setBind_date_time(String bind_date_time) {
        this.bind_date_time = bind_date_time;
    }

    public String getTerms_version() {
        return terms_version;
    }

    public void setTerms_version(String terms_version) {
        this.terms_version = terms_version;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public ZonedDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(ZonedDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getRecord_status() {
        return record_status;
    }

    public void setRecord_status(String record_status) {
        this.record_status = record_status;
    }

    public String getData_retention() {
        return data_retention;
    }

    public void setData_retention(String data_retention) {
        this.data_retention = data_retention;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCustomized_tag() {
        return customized_tag;
    }

    public void setCustomized_tag(String customized_tag) {
        this.customized_tag = customized_tag;
    }
}
