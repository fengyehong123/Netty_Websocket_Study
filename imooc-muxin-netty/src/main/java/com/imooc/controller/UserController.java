package com.imooc.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imooc.pojo.Users;
import com.imooc.service.UserService;
import com.imooc.utils.IMoocJSONResult;
import com.imooc.utils.MD5Utils;

@RestController
@RequestMapping("/u")
public class UserController {
	
	@Autowired
	private UserService userservice;
	
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
		}
		
		
		return IMoocJSONResult.ok();
	}
}
