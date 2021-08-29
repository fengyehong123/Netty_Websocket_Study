package com.imooc.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.enums.OperatorFriendRequestTypeEnum;
import com.imooc.enums.SearchFriendsStatusEnum;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UsersBO;
import com.imooc.pojo.vo.MyFriendsVO;
import com.imooc.pojo.vo.UsersVO;
import com.imooc.service.UserService;
import com.imooc.utils.FastDFSClient;
import com.imooc.utils.FileUtils;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;

@RestController
@RequestMapping("/u")
public class UserController {
	
	@Autowired
	private UserService userservice;
	
	// 注入fastDFS客户端
	@Autowired
	private FastDFSClient fastDFSClient;
	
	/**
	 * 用户登录或者注册接口
	 * @throws Exception 
	 */
	@PostMapping("/registOrLogin")
	public IMoocJSONResult registOrLogin(@RequestBody Users user) throws Exception {
		
		// 判断用户名和密码不能为空
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())) {
			return IMoocJSONResult.errorMap("用户名或者密码不能为空...");
		}
		
		// 判断用户名是否存在,用户名存在就登录,不存在就注册
		boolean usernameIsExist = userservice.queryUsernameIsExist(user.getUsername());
		
		Users userResult = null;
		if (usernameIsExist) {
			// 登录
			userResult = userservice.queryUserForLogin(user.getUsername(), 
					MD5Utils.getMD5Str(user.getPassword()));
			if (userResult == null) {
				return IMoocJSONResult.errorMsg("用户名或密码不正确...");
			}
		} else {
			// 注册
			user.setNickname(user.getUsername());
			// 设置头像为空
			user.setFaceImage("");
			user.setFaceImageBig("");
			// 密码经过MD5加密
			user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
			userResult = userservice.saveUser(user);
		}
		
		/**
		 * userResult对象里面有很多前端用不到的信息,例如密码等字段
		 * 我们重新定义一个 UsersVO类(不包含密码等字段),将userResult中的信息拷贝到UsersVO类中
		 */
		UsersVO usersVO = new UsersVO();
		BeanUtils.copyProperties(userResult, usersVO);
		
		// 将登录注册成功的用户信息返回给前端
		return IMoocJSONResult.ok(usersVO);
	}
	
	/**
	 * 上传用户头像到图片服务器
	 * @param userBo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/uploadFaceBase64")
	public IMoocJSONResult uploadFaceBase64(@RequestBody UsersBO userBo) throws Exception {
		
		// 获取前端传输的base64字符串,然后转换为文件对象之后再上传
		String base64Data = userBo.getFaceData();
		String userFacePath = "D:\\" + userBo.getUserId() + "userface64.png";
		
		// 将前端图片的base64字符串转换为图片,保存在本地
		FileUtils.base64ToFile(userFacePath, base64Data);
		
		// 将前端传入的图片传入到FastDFS服务器
		MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
		String url = "";
		try {
			// 获取上传成功之后的url(大图)
			url = fastDFSClient.uploadFace(faceFile);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		// 获取图片的url
		System.out.println(url);
		
		// 获取缩略图的url
		String thump = "_80x80.";
		String arr[] = url.split("\\.");
		String thumpImgUrl = arr[0] + thump + arr[1];
		
		// 更新用户的头像
		Users user = new Users();
		user.setId(userBo.getUserId());
		user.setFaceImage(thumpImgUrl);
		user.setFaceImageBig(url);
		Users result = userservice.updateUserInfo(user);
		
		return IMoocJSONResult.ok(result);
	}
	
	/**
	 * 修改用户昵称
	 * @param userBo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/setNickname")
	public IMoocJSONResult setNickname(@RequestBody UsersBO userBo) throws Exception {
		
		// 更新用户的昵称
		Users user = new Users();
		user.setId(userBo.getUserId());
		user.setNickname(userBo.getNickname());
		Users result = userservice.updateUserInfo(user);
		
		return IMoocJSONResult.ok(result);
	}
	
	/**
	 * 搜索好友的接口,根据账号做匹配查询而不是模糊查询
	 * @param userBo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/search")
	public IMoocJSONResult searchUser(String myUserId, String friendUsername) throws Exception {
		
		// 判断myUserId和friendUsername不能为空
		if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)) {
			return IMoocJSONResult.errorMsg("");
		}
		
		/**
		 * 前置条件
		 * 1.搜索的用户如果不存在,返回[无此用户]
		 * 2.搜索的账号是自己,返回[不能添加自己]
		 * 3.搜索的朋友已经是自己的好友,返回[该用户已经是你的好友]
		 */
		Integer status = userservice.preconditionSearchFriends(myUserId, friendUsername);
		if (status == SearchFriendsStatusEnum.SUCCESS.status) {
			Users users = userservice.queryUserInfoByUsername(friendUsername);
			
			UsersVO usersVO = new UsersVO();
			BeanUtils.copyProperties(users, usersVO);
			
			return IMoocJSONResult.ok(usersVO);
		} else {
			String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
			return IMoocJSONResult.errorMsg(errorMsg);
		}	
	}
	
	/**
	 * 发送添加好友的请求
	 * @param userBo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/addFriendRequest")
	public IMoocJSONResult addFriendRequest(String myUserId, String friendUsername) throws Exception {
		
		// 判断myUserId和friendUsername不能为空
		if (StringUtils.isBlank(myUserId) || StringUtils.isBlank(friendUsername)) {
			return IMoocJSONResult.errorMsg("");
		}
		
		/**
		 * 前置条件
		 * 1.搜索的用户如果不存在,返回[无此用户]
		 * 2.搜索的账号是自己,返回[不能添加自己]
		 * 3.搜索的朋友已经是自己的好友,返回[该用户已经是你的好友]
		 */
		Integer status = userservice.preconditionSearchFriends(myUserId, friendUsername);
		if (status == SearchFriendsStatusEnum.SUCCESS.status) {
			userservice.sendFriendRequest(myUserId, friendUsername);
		} else {
			String errorMsg = SearchFriendsStatusEnum.getMsgByKey(status);
			return IMoocJSONResult.errorMsg(errorMsg);
		}
		
		return IMoocJSONResult.ok();
	}
	
	/**
	 * 发送添加好友的请求
	 * @param userId
	 * @return
	 */
	@PostMapping("/queryFriendRequests")
	public IMoocJSONResult queryFriendRequests(String userId) {
		
		// 0. 判断不能为空
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("");
		}
		
		// 1. 查询用户接受到的朋友申请
		return IMoocJSONResult.ok(userservice.queryFriendRequestList(userId));
	}
	
	/**
	 * 接受方 通过或者忽略朋友请求
	 * @param acceptUserId
	 * @param sendUserId
	 * @param operType
	 * @return
	 */
	@PostMapping("/operFriendRequest")
	public IMoocJSONResult operFriendRequest(String acceptUserId, String sendUserId, Integer operType) {
		
		// 0. acceptUserId sendUserId operType 判断不能为空
		if (StringUtils.isBlank(acceptUserId) 
				|| StringUtils.isBlank(sendUserId) 
				|| operType == null) {
			return IMoocJSONResult.errorMsg("");
		}
		
		// 1. 如果operType 没有对应的枚举值，则直接抛出空错误信息
		if (StringUtils.isBlank(OperatorFriendRequestTypeEnum.getMsgByType(operType))) {
			return IMoocJSONResult.errorMsg("");
		}
		
		if (operType == OperatorFriendRequestTypeEnum.IGNORE.type) {
			// 2. 判断如果忽略好友请求，则直接删除好友请求的数据库表记录
			userservice.deleteFriendRequest(sendUserId, acceptUserId);
		} else if (operType == OperatorFriendRequestTypeEnum.PASS.type) {
			// 3. 判断如果是通过好友请求，则互相增加好友记录到数据库对应的表
			//	   然后删除好友请求的数据库表记录
			userservice.passFriendRequest(sendUserId, acceptUserId);
		}
		
		// 4. 数据库查询好友列表
		List<MyFriendsVO> myFirends = userservice.queryMyFriends(acceptUserId);
		
		return IMoocJSONResult.ok(myFirends);
	}
	
	@PostMapping("/myFriends")
	public IMoocJSONResult myFriends(String userId) {
		// 0. userId 判断不能为空
		if (StringUtils.isBlank(userId)) {
			return IMoocJSONResult.errorMsg("");
		}
		
		// 1. 数据库查询好友列表
		List<MyFriendsVO> myFirends = userservice.queryMyFriends(userId);
		
		return IMoocJSONResult.ok(myFirends);
	}
}
