package com.jt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
import org.junit.Test;

import java.io.IOException;

public class ObjectToJson {
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 将对象转换成json
     */
    @Test
    public void toJson(){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(100l).setItemDesc("测试数据");
        try {
            String json = mapper.writeValueAsString(itemDesc);
            System.out.println(json);
            ItemDesc itemDesc1 = mapper.readValue(json,ItemDesc.class);
            System.out.println(itemDesc1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
