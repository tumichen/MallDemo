package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Controller
@RequestMapping("/user")
public class UserController {
    @Reference(timeout = 3_000,check = false)
    private DubboUserService userService;
    @Autowired
    private JedisCluster jedisCluster;

    @RequestMapping("/{moduleName}")
    public String moduleName(@PathVariable String moduleName){
        return moduleName;
    }

    @RequestMapping("/doRegister")
    @ResponseBody
    public SysResult saveUser(User user){
        userService.saveUser(user);
        return SysResult.success();
    }

    /**
     * cookie的介绍:
     *
     * @param user
     * @param response
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response){
        String token = userService.doLogin(user);
        if (StringUtils.isEmpty(token)){
            return SysResult.fail();
        }
        Cookie cookie = new Cookie("JT_TICKET", token);
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/");
        cookie.setDomain("jt.com");
        response.addCookie(cookie);
        return SysResult.success();
    }

    @RequestMapping("/logout")
    public String doLogout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies.length>0){
            for (Cookie cookie : cookies) {
                if("JT_TICKET".equals(cookie.getName())) {
                    //获取指定数据的值
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(token)){
            jedisCluster.del(token);
            Cookie cookie = new Cookie("JT_TICKET","");
            cookie.setMaxAge(0);//立即删除
            cookie.setPath("/");
            cookie.setDomain("jt.com");
            response.addCookie(cookie);
        }
        return "redirect:/";
    }
}
