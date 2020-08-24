package com.jt.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import com.jt.service.DubboCartService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service(timeout = 3_000)
public class DubboCartServiceImpl implements DubboCartService {
    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<Cart> findCartListByUserId(Long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return cartMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
        cartMapper.delete(queryWrapper);
    }

    /**
     * 思路:
     *    通过user_id,item_id能查到,,则应该增加数量
     *    null表示第一次购买,
     *    !null表示已经购买过;
     * @param cart
     */
    @Override
    public void saveCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("item_id",cart.getItemId());
        queryWrapper.eq("user_id",cart.getUserId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if (cartDB==null){
            cart.setCreated(new Date()).setUpdated(cart.getCreated());
            cartMapper.insert(cart);
        }else {
            int num = cartDB.getNum()+cart.getNum();
            Cart cartTemp = new Cart();
            cartTemp.setId(cartDB.getId()).setNum(num).setUpdated(new Date());
            cartMapper.updateById(cartTemp);
        }
    }

    @Override
    public void updateCartNum(Cart cart) {
        Cart cartTemp = new Cart();
        cartTemp.setNum(cart.getNum())
                .setUpdated(new Date());
        QueryWrapper<Cart> updateWrapper = new QueryWrapper<Cart>();
        updateWrapper.eq("user_id", cart.getUserId())
                .eq("item_id", cart.getItemId());
        cartMapper.update(cartTemp, updateWrapper);

    }
}
