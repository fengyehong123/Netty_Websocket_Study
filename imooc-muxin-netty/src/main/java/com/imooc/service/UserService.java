package com.imooc.service;

import com.imooc.pojo.Users;

public interface UserService {

	// 判断用户名是否存在
	public boolean queryUsernameIsExist(String username);
	
	// 用户登录注册
	public Users queryUserForLogin(String username, String pwd);
	
	// 用户注册
	public Users saveUser(Users user);
	
	// 修改用户的信息
	public Users updateUserInfo(Users user);
	
	// 搜索朋友的前置条件
	public Integer preconditionSearchFriends(String myUserId, String friendUsername);
}
