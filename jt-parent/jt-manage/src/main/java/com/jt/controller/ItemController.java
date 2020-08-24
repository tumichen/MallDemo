package com.jt.controller;

import com.jt.VO.EasyUI_Table;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jt.service.ItemService;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/item/")
public class ItemController {
	
	@Autowired
	private ItemService itemService;

    @RequestMapping("/query")
	public EasyUI_Table findItemPage(Integer page,Integer rows){
        return itemService.findItemPage(page,rows);
    }
    @RequestMapping("/query/item/desc/{itemId}")
    public SysResult findItemDescById(@PathVariable Long itemId){
        ItemDesc itemDesc = itemService.findItemDescById(itemId);
        return SysResult.success(itemDesc);
    }

    @RequestMapping("/save")
    public SysResult saveItem(Item item, ItemDesc itemDesc){
        try {
            itemService.saveItem(item,itemDesc);
            return SysResult.success();
        }catch (Exception e){
            e.printStackTrace();
            return SysResult.fail();
        }
    }
    @RequestMapping("/update")
    public SysResult updateItem(Item item,ItemDesc itemDesc){
        itemService.update(item,itemDesc);
        return SysResult.success();
    }
    @RequestMapping("/delete")
    public SysResult deleteItem(Long[] ids){
        itemService.deleteItems(ids);
        return SysResult.success();
    }
    @RequestMapping("/instock")
    public SysResult itemInstock(Long[] ids){
        int status = 2;//下架
        itemService.updateStatus(ids,status);
        return SysResult.success();
    }
    @RequestMapping("/reshelf")
    public SysResult itemReshelf(Long[] ids){
        int status =1;
        itemService.updateStatus(ids,status);
        return SysResult.success();
    }

}
