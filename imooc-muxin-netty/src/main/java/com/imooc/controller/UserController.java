package com.imooc.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UsersBO;
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
	
	@PostMapping("/uploadFaceBase64")
	public IMoocJSONResult uploadFaceBase64(@RequestBody UsersBO userBo) throws Exception {
		
		// 获取前端传输的base64字符串,然后转换为文件对象之后再上传
		String base64Data = userBo.getFaceData();
		String userFacePath = "D:\\" + userBo.getUserId() + "userface64.png";
		
		// 将前端图片的base64字符串转换为图片,保存在本地
		FileUtils.base64ToFile(userFacePath, base64Data);
		
		// 将前端传入的图片传入到FastDFS服务器
		MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
		// 获取上传成功之后的url(大图)
		String url = fastDFSClient.uploadFace(faceFile);
		
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
		Users updateUserInfo = userservice.updateUserInfo(user);
		
		return IMoocJSONResult.ok(updateUserInfo);
	}
}
