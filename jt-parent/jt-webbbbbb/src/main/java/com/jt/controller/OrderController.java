package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference(timeout = 3_000)
    private DubboCartService cartService;
    @Reference(timeout = 3_000)
    private DubboOrderService orderService;

    @RequestMapping("/create")
    public String create(Model model) {
        //获取用户的购物车信息
        Long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("carts", cartList);
        return "order-cart";
    }

    @RequestMapping("/submit")
    @ResponseBody
    public SysResult saveOrder(Order order){
        order.setUserId(UserThreadLocal.get().getId());
        String orderID = orderService.saveOrder(order);
        if (StringUtils.isEmpty(orderID)){
            return SysResult.fail();
        }
        return SysResult.success(orderID);
    }

    @RequestMapping("/success")
    public String findOrderById(String id,Model model){
        Order order = orderService.findById(id);
        model.addAttribute("order",order);
        return "success";
    }




}
