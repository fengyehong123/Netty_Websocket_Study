package com.imooc.service.impl;

import java.io.IOException;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.enums.SearchFriendsStatusEnum;
import com.imooc.mapper.MyFriendsMapper;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.MyFriends;
import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.FastDFSClient;
import com.imooc.utils.FileUtils;
import com.imooc.utils.QRCodeUtils;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UsersMapper userMapper;
	
	@Autowired
	private MyFriendsMapper myFriendsMapper;
	
	// 注入二维码生成的工具类
	@Autowired
	private QRCodeUtils qrCodeUtils;
	
	// 注入FastDFS客户端
	@Autowired
	private FastDFSClient fastDFSClient;
	
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
		
		// 为注册的用户生成一个ID
		String id = sid.nextShort();
		user.setId(id);
		
		// 为每一个用户生成一个唯一的二维码
		String qrCodePath = "D://user" + id + "qrcode.png";
		// 生成一个二维码;参数1:二维码生成的路径 参数2:放入二维码中的信息 
		qrCodeUtils.createQRCode(qrCodePath, "muxin_qrcode:" + user.getUsername());
		// 读取保存到本地的二维码文件,将文件转换为MultipartFile对象
		MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
		String qrCodeUrl = "";
		try {
			qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 将生成的二维码地址信息保存到数据库中
		user.setQrcode(qrCodeUrl);
		
		// 将用户信息插入到数据库
		userMapper.insert(user);
		
		// 将user信息返回给前端
		return user;
	}
	
	// 更新用户的信息
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Users updateUserInfo(Users user) {
		
		userMapper.updateByPrimaryKeySelective(user);
		return queryUserById(user.getId());
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	private Users queryUserById(String userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
		
		// 查询朋友用户是否存在
		Users user = queryUserInfoByUsername(friendUsername);
		
		// 搜索的用户如果不存在,返回[无此用户]
		if (user == null) {
			return SearchFriendsStatusEnum.USER_NOT_EXIST.status;
		}
		
		// 搜索的账号是自己,返回[不能添加自己]
		if (user.getId().equals(myUserId)) {
			return SearchFriendsStatusEnum.NOT_YOURSELF.status;
		}
		
		// 搜索的朋友已经是自己的好友,返回[该用户已经是你的好友]
		Example mfe = new Example(MyFriends.class);
		Criteria mf = mfe.createCriteria();
		mf.andEqualTo("myUserId", myUserId);
		mf.andEqualTo("myFriendUserId", user.getId());
		
		MyFriends myFriendsRel = myFriendsMapper.selectOneByExample(mfe);
		if (myFriendsRel != null) {
			return SearchFriendsStatusEnum.ALREADY_FRIENDS.status;
		}
		
		// 返回成功,可以进行好友添加
		return SearchFriendsStatusEnum.SUCCESS.status;
	}
	
	public Users queryUserInfoByUsername(String username) {
		
		Example userExample = new Example(Users.class);
		Criteria createCriteria = userExample.createCriteria();
		createCriteria.andEqualTo("username", username);
		
		return userMapper.selectOneByExample(userExample);
	}
}
