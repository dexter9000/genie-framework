package com.genie.core.utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JSONConvertUtilTest {

    @Test
    public void toJavaObject() throws ClassNotFoundException {
        String jsonStr = "{id:'1',name:'test'}";
        String clzKey = "TestPerson";
        TestPerson testPerson = JSONConvertUtil.toJavaObject(jsonStr, TestPerson.class, clzKey);
        assertEquals("1",testPerson.getId());
    }

//    @Test
//    public void toJavaArray() throws ClassNotFoundException {
//        String jsonStr = "[{id:'1',name:'test'}]";
//        String clzKey = TestPerson.class.getName();
//        List list = JSONConvertUtil.toJavaArray(jsonStr, clzKey);
//        TestPerson testPerson = (TestPerson) list.get(0);
//        assertEquals("1",testPerson.getId());
//    }

    @Test
    public void toJavaArray() throws ClassNotFoundException {
        String jsonStrArray = "[{id:'1',name:'test',TestPerson:'"+TestPerson.class.getName()+"'},{id:'2',name:'test',TestPerson:'"+TestPerson.class.getName()+"'}]";
        String clzKey = TestPerson.class.getSimpleName();
        List list = JSONConvertUtil.toJavaArray(jsonStrArray, clzKey);
    }

}

class TestPerson {
    public String id;

    public String name;

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

    @Override
    public String toString() {
        return super.toString();
    }
}
//class TestClass {
//    public Integer id;
//
//    public TestPerson person;
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public TestPerson getPerson() {
//        return person;
//    }
//
//    public void setPerson(TestPerson person) {
//        this.person = person;
//    }
//
//    @Override
//    public String toString() {
//        return super.toString();
//    }
//}
