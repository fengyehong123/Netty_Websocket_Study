 window.app = {
 	
 	// 后端服务器的IP地址
 	serverUrl: 'http://192.168.31.100:8080',
 	
 	// 判断字符串是否为空
 	isNotNull: function(str) {
 		if(str != null && str != undefined && str != "") {
 			return true;
 		}
 		
 		return false;
 	}, 
 	
 	// 展示弹出框,用来代替原生的alert
 	showToast: function(msg, type) {
 		// 指定弹出消息,弹出框的图标和弹出框的位置
 		plus.nativeUI.toast(msg, {icon: "image/" + type + ".png", verticalAlign: "center"})
 	}
 }
