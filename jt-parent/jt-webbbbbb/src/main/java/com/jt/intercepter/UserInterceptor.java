package com.jt.intercepter;

import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private JedisCluster jedisCluster;
    /**
     * true:表示放行
     * false:表示拦截,然后重定向
     * 业务需求:
     * 依据,token和redis里是否有数据
     * 如果上述数据没问题
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //1.获取cookie数据
        Cookie[] cookies = request.getCookies();
        String token = null;
        if(cookies.length>0) {
            for (Cookie cookie : cookies) {
                if("JT_TICKET".equals(cookie.getName())) {
                    //获取指定数据的值
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (!StringUtils.isEmpty(token)){
            String userJson = jedisCluster.get(token);
            if (!StringUtils.isEmpty(userJson)){
                User user = ObjectMapperUtil.toObject(userJson, User.class);
                //利用域传参
                //request.setAttribute("JT_USER",user);
                UserThreadLocal.set(user);
                return true;
            }
        }
        response.sendRedirect("/user/login.html");
        return false;
    }
}
