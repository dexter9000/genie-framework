package com.genie.mongodb.entity;

import com.genie.data.annotation.ShardingId;
import com.genie.data.annotation.ShardingTable;

@ShardingTable("member")
public class Member {

    private String id;
    @ShardingId
    private String city;
    private String area;
    private String name;

    private long age;

    public Member(String id, String area) {
        this.id = id;
        this.area = area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
