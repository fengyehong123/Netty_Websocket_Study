package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
// 扫描mybatis mapper包所在的路径
@MapperScan(basePackages = "com.imooc.mapper")
// 扫描所有需要的包,包含一些自用的工具类包所在的路径
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
public class Application { 
	
	// 将工具类放入SpringBoot容器中
	@Bean
	public SpringUtil getSpingUtil() {
		return new SpringUtil();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
