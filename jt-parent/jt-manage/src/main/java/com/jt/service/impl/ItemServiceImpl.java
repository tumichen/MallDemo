package com.jt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.VO.EasyUI_Table;
import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.mapper.ItemDescMapper;
import com.jt.pojo.BasePojo;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.ItemMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;


	@Override
	public EasyUI_Table findItemPage(Integer pageCurrent, Integer countPerPage) {
		Integer totleRows = itemMapper.selectCount(null);
		//执行分页查询
		int startIndex = (pageCurrent-1)*countPerPage;
		List<Item> list = itemMapper.findItemPage(startIndex, countPerPage);
		return new EasyUI_Table(totleRows,list);
	}

	@Transactional
	@Override
	public void saveItem(Item item, ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		itemMapper.insert(item);
		//主键自增,该怎么获取
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
	}

	/**
	 * roolbackfor 指定异常回滚类型
	 *
	 * @param item
	 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		itemDesc.setItemId(item.getId());
		itemDesc.setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}

	@Override
	@Transactional
	public void deleteItems(Long[] ids) {
		//mybatis写法
		List<Long> list = Arrays.asList(ids);
		itemMapper.deleteBatchIds(list);
		itemDescMapper.deleteBatchIds(list);
		//mybatis写法
		//itemMapper.deleteItems(ids);
	}

	@Override
	public void updateStatus(Long[] ids, int status) {
		Item item = new Item();
		item.setStatus(status).setUpdated(new Date());
		List<Long> list = Arrays.asList(ids);
		QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("id",list);
		itemMapper.update(item,queryWrapper);
	}

	@Override
	@Cache_Find(keyType = KEY_ENUM.AUTO)
	public ItemDesc findItemDescById(Long itemId) {
		ItemDesc itemDesc = itemDescMapper.selectById(itemId);
		return itemDesc;
	}

	@Override
	@Cache_Find(keyType = KEY_ENUM.AUTO)
	public Item findItemById(Long itemId) {
		Item item = itemMapper.selectById(itemId);
		return item;
	}

}
