package com.jt.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Service(timeout = 3_000)
public class DubboUserServiceImpl implements DubboUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;

    @Override
    public void saveUser(User user) {
        //1.密码加密
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setEmail(user.getPhone()).setPassword(md5Pass).setCreated(new Date()).setUpdated(user.getCreated());
        System.out.println("malegebi");
        userMapper.insert(user);
    }

    @Override
    public String doLogin(User user) {
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //将对象中不为null的属性当做where条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        User userDb = userMapper.selectOne(queryWrapper);
        String token = null;
        if (userDb!=null){
            //生成redis的key
            String tokenTemp = "JT_TOKEN_"+Instant.now().toEpochMilli()+user.getUsername();
            tokenTemp = DigestUtils.md5DigestAsHex(tokenTemp.getBytes());
            userDb.setPassword("真实的我已经隐藏起来了");
            String json = ObjectMapperUtil.toJSON(userDb);
            jedisCluster.setex(tokenTemp,7*24*60*60,json);
            token = tokenTemp;
        }
        return token;
    }

}
