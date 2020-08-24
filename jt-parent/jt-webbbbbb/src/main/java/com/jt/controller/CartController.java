package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 3_000,check = false)
    private DubboCartService dubboCartService;
    //要将购物车表中数据拿出:根据userId
    @RequestMapping("/show")
    public String showCart(Model model, HttpServletRequest request){
        //User user = (User)request.getAttribute("JT_USER");
        Long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = dubboCartService.findCartListByUserId(userId);
        model.addAttribute("cartList",cartList);
        return "cart";
    }

    /**
     * 如果restful风格的,参数与对象的属性相同,则可以用对象接收
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCart(Cart cart){
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.deleteCart(cart);
        return "redirect:/cart/show.html";
    }

    /**
     * 利用页面的表单提交获取参数之后跳转购物车展现页面
     * @param cart
     * @return
     */
    @RequestMapping("/add/{itemId}")
    public String saveCart(Cart cart){
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.saveCart(cart);
        return "redirect:/cart/show.html";
    }

    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody
    public SysResult updateCartNum(Cart cart) {
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        dubboCartService.updateCartNum(cart);
        return SysResult.success();
    }

}
