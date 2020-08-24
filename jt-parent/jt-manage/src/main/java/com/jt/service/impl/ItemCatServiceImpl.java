package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.VO.EasyUI_Tree;
import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.service.ItemCatService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private ItemCatMapper itemCatMapper;
//    @Autowired
//    private Jedis jedis;
    @Autowired
    private JedisCluster jedis;

    @Override
    public String findItemCatNameById(Long itemCatId) {
        ItemCat itemCat = itemCatMapper.selectById(itemCatId);
        return itemCat.getName();
    }

    @Override
    @Cache_Find(keyType = KEY_ENUM.AUTO)
    public List<EasyUI_Tree> findItemCatByParentId(Long parentId) {
        ArrayList<EasyUI_Tree> treeList = new ArrayList<>();
        List<ItemCat> itemCatList = findItemCatList(parentId);
        itemCatList.stream().forEach((itemCat) -> {
            long id = itemCat.getId();
            String text = itemCat.getName();
            String state = itemCat.getIsParent() ? "closed" : "open";
            EasyUI_Tree tree = new EasyUI_Tree(id, text, state);
            treeList.add(tree);
        });
        return treeList;
    }

    /**
     * 查询缓存
     *
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUI_Tree> findItemCatByCache(Long parentId) {
        List<EasyUI_Tree> treeList = new ArrayList<>();
        String key = "ITEM_CAT_" + parentId;
        String result = jedis.get(key);
        if (StringUtils.isEmpty(result)) {
            System.out.println("我走数据库了");
            treeList = findItemCatByParentId(parentId);
            String json = ObjectMapperUtil.toJSON(treeList);
            jedis.set(key, json);
        } else {
            System.out.println("我走缓存了");
            treeList = ObjectMapperUtil.toObject(result, treeList.getClass());
        }
        return treeList;
    }

    public List<ItemCat> findItemCatList(Long parentId) {
        QueryWrapper<ItemCat> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", parentId);
        return itemCatMapper.selectList(wrapper);
    }
}
