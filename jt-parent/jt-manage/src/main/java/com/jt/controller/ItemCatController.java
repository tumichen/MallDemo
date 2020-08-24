package com.jt.controller;

import com.jt.VO.EasyUI_Tree;
import com.jt.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    @RequestMapping("queryItemName")
    public String findItemCatNameById(Long itemCatId) {
        return itemCatService.findItemCatNameById(itemCatId);
    }

    /**
     * 实现树形结构查询
     *
     * @param parentId
     * @return EasyUI_tree
     */
    @RequestMapping("/list")
    public List<EasyUI_Tree> findItemCatByParentId(@RequestParam(name = "id", defaultValue = "0") Long parentId) {
        //return itemCatService.findItemCatByCache(parentId);
        //应用aop的缓存
        return itemCatService.findItemCatByParentId(parentId);
    }

}
