package com.qu.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * String工具类
 *
 */
public class StringUtil {


    private StringUtil() {
        throw new Error("StringUtil error");
    }

    /**
     * 判断是否包含某个字段
     *
     * @param o
     * @param keyword
     * @return
     */
    public static boolean isContains(Object o, String keyword) {
        if (null == o) {
            return false;
        }

        String str = JSONObject.toJSONString(o);

        int index = str.indexOf(keyword);

        return index >= 0;
    }

    public static String transObjToStr(Object o) {
        if (null == o) {
            return null;
        }
        return o.toString();
    }

    public static void main(String[] args) {
        String str = "assistDep: \"\"\n" +
                "id: \"\"\n" +
                "loginName: \"test10\"\n" +
                "mainDep: \"1a7d6e1d58114a2fabc492ed6ab9c73b\"\n" +
                "mainPos: \"10013\"\n" +
                "phone: \"18223921188\"\n" +
                "roleId: \"4f671b87b4e74d0da9f58a43d945eecc\"\n" +
                "status: \"open\"\n" +
                "system: \"\"\n" +
                "userCode: \"002\"\n" +
                "userId: \"\"\n" +
                "userName: \"\"\n" +
                " ";
        Map<String, String> map = new HashMap<>();
        String[] arr = str.split("\n");
        for (int e = 0, len = arr.length; e < len; e++) {
            String str2 = arr[e];
            String[] arr2 = str2.split(":");
            String key = arr2[0];
            String val = arr2[1].replaceAll("\\\"", "");
            if ("\"null\"".equals(val)) {
                val = null;
            }
            map.put(key.trim(), val.trim());
        }
        System.out.println(JSONObject.toJSONString(map));
    }

    public static String objToStr(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }
}
