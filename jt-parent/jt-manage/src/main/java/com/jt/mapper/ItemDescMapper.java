package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.ItemDesc;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ItemDescMapper extends BaseMapper<ItemDesc> {
    @Select("select * from tb_item_desc where item_id=#{itemId}")
    ItemDesc selectByItemId(@Param("itemId") Long itemId);
}
