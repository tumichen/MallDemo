package com.jt.service.impl;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.util.HttpClientService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {
    private static final String URL_ITEM_ITEMID = "http://manage.jt.com/web/item/findItemById/";
    private static final String URL_ITEMDESC_ITEMID = "http://manage.jt.com/web/item/findItemDescById/";
    @Autowired
    private HttpClientService httpClient;
    @Override
    public Item findItemById(Long itemId) {
        String url = URL_ITEM_ITEMID+itemId;
        String itemJson = httpClient.doGet(url);
        return ObjectMapperUtil.toObject(itemJson,Item.class);
    }

    @Override
    public ItemDesc findItemDescById(Long itemId) {
        String url = URL_ITEMDESC_ITEMID+itemId;
        String itemDescJson = httpClient.doGet(url);
        ItemDesc itemDesc = ObjectMapperUtil.toObject(itemDescJson, ItemDesc.class);
        return itemDesc;
    }
}
