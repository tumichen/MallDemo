package com.jt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ItemMapper extends BaseMapper<Item> {
    @Select("SELECT * FROM tb_item ORDER BY updated DESC LIMIT #{startIndex},#{countPerPage}")
    List<Item> findItemPage(@Param("startIndex") Integer startIndex,
                            @Param("countPerPage") Integer countPerPage);

    /**
     * mybatis的传参问题:
     * mybatis默认不允许多值传参
     * 需要用户将多值转化为单值
     * 使用集合封装
     * @param ids
     * @return
     */
    int deleteItems(@Param("ids") Long[] ids);
}
