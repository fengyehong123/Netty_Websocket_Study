 window.app = {
 	
 	/**
 	 * 后端Netty服务器的IP地址
 	 * 注意:使用本机电脑当做服务器,然后用手机APP访问的时候,由于是内网,要保证手机和电脑同时连接WIFI
 	 * 然后使用WIFI网卡的IP地址进行连接
 	 */
 	serverUrl: 'http://192.168.31.100:8080',
 	
 	/**
 	 * 图片服务器FastDFS的地址(虚拟机CentOS6)
 	 */
 	imageServerUrl: 'http://192.168.31.5/group1/',
 	
 	// 判断字符串是否为空
 	isNotNull: function(str) {
 		if(str != null && str != undefined && str != "") {
 			return true;
 		}
 		
 		return false;
 	}, 
 	
 	/**
 	 * 展示弹出框,用来代替原生的alert
 	 * 默认mui的不支持居中和自定义icon,我们使用h5+来代替
 	 */
 	showToast: function(msg, type) {
 		// 指定弹出消息,弹出框的图标和弹出框的位置
 		plus.nativeUI.toast(msg, {icon: "image/" + type + ".png", verticalAlign: "center"})
 	},
 	/**
 	 * 保存用户的全局对象
 	 */
 	setUserGlobalInfo: function(user){
 		let userInfoStr = JSON.stringify(user);
 		// 将登录成功的用户信息保存到手机APP的缓存中(相当于浏览器的缓存)
 		plus.storage.setItem("userInfo", userInfoStr);
 	},
 	/**
 	 * 获取用户的全局对象
 	 */
 	getUserGlobalInfo: function() {
 		// 将app缓存中的用户信息字符串反序列化为一个对象
 		return JSON.parse(plus.storage.getItem("userInfo"));
 	},
 	/**
 	 * 清空登录的用户信息缓存
 	 */
 	userLogout: function() {
 		plus.storage.removeItem("userInfo");
 	},
 	
 	/**
	 * 保存用户的联系人列表
	 */
	setContactList: function(contactList) {
		var contactListStr = JSON.stringify(contactList);
		plus.storage.setItem("contactList", contactListStr);
	},
	
	/**
	 * 获取本地缓存中的联系人列表
	 */
	getContactList: function() {
		var contactListStr = plus.storage.getItem("contactList");
		
		if (!this.isNotNull(contactListStr)) {
			return [];
		}
		
		return JSON.parse(contactListStr);
	},
 }
