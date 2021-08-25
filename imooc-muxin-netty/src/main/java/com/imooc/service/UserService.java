package com.imooc.service;

import java.util.List;

import com.imooc.pojo.Users;
import com.imooc.pojo.vo.FriendRequestVO;

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
	
	// 根据用户名查询用户对象
	public Users queryUserInfoByUsername(String username);

	// 添加好友请求记录,保存到数据库
	public void sendFriendRequest(String myUserId, String friendUsername);
	
	// 查询好友的添加请求
	public List<FriendRequestVO> queryFriendRequestList(String acceptUserId);
}
