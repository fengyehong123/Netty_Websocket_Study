package com.imooc.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UsersMapper userMapper;
	
	// 注入ID自动生成对象
	@Autowired
	private Sid sid;

	// 查询当前用户名是否存在
	// 添加事务
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public boolean queryUsernameIsExist(String username) {
		
		Users users = new Users();
		users.setUsername(username);
		Users result = userMapper.selectOne(users);
		
		return result != null ? true : false;
	}
	
	// 添加事务
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Users queryUserForLogin(String username, String pwd) {
		
		// 构造查询条件对象
		Example userExample = new Example(Users.class);
		Criteria criteria = userExample.createCriteria();
		// 放入要查询的条件
		criteria.andEqualTo("username", username);
		criteria.andEqualTo("password", pwd);
		
		Users result = userMapper.selectOneByExample(userExample);
		return result;
	}

	// 用户注册
	@Override
	public Users saveUser(Users user) {
		
		// TODO 为每一个用户生成一个唯一的二维码
		user.setQrcode("");
		
		// 为注册的用户生成一个ID
		String id = sid.nextShort();
		user.setId(id);
		
		// 将用户信息插入到数据库
		userMapper.insert(user);
		
		// 将user信息返回给前端
		return user;
	}

}
