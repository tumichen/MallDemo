package com.jt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ObjectMapperUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    /**
     * 对象转换json
     */
    public static String toJSON(Object target){
        String result = null;
        try {
            result =  mapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }

    /**
     * json转换为对象
     */
    public static <T> T toObject(String json,Class<T> targetClass){
        T object = null;
        try {
            object = mapper.readValue(json, targetClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return object;
    }

}
