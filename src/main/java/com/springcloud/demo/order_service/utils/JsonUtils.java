package com.springcloud.demo.order_service.utils;

/**
 * @Author: Elvis
 * @Description:
 * @Date: 2020/2/14 9:56
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * json工具类
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * json字符串转JsonNode对象的方法
     * @param str
     * @return
     */
    public static JsonNode str2JsonNode(String str){
        try {
            return objectMapper.readTree(str);
        } catch (IOException e) {
           return null;
        }
    }
}
