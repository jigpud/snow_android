package com.jigpud.snow.util.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jigpud
 */
public class JsonUtilTest {
    @Test
    public void testObject() {
        String json = "{\"name\": \"jigpud\"}";
        User user = JsonUtil.fromJson(json, User.class);
        System.out.println("My name is " + user.getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testList() {
        List<String> list = new ArrayList<>();
        list.add("jigpud");
        String json = JsonUtil.toJson(list);
        System.out.println(json);
        for (String str : (List<String>) JsonUtil.fromJson(json, List.class)) {
            System.out.println(str);
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class User {
        private String name;
    }
}
