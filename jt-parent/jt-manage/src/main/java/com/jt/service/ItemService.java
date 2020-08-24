package com.jt.service;

import com.jt.VO.EasyUI_Table;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

public interface ItemService {
	EasyUI_Table findItemPage(Integer pageCurrent, Integer countPerPage);
	void saveItem(Item item, ItemDesc itemDesc);
	void update(Item item,ItemDesc itemDesc);
	void deleteItems(Long[] ids);

	void updateStatus(Long[] ids, int status);

	ItemDesc findItemDescById(Long itemId);

    Item findItemById(Long itemId);
}
