package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;


	@Override
	public Boolean findUserCheck(String param, Integer type) {
		String column = (type==1)?"username":((type==2)?"phone":"email");
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column,param);
		Integer count = userMapper.selectCount(queryWrapper);
		return count==0?false:true;
	}
}
