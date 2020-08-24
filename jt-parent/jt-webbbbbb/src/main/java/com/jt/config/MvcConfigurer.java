package com.jt.config;

import com.jt.intercepter.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfigurer implements WebMvcConfigurer{
	@Autowired
	private UserInterceptor userInterceptor;
	
	//开启匹配后缀型配置
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		
		configurer.setUseSuffixPatternMatch(true);
	}

	/**
	 * /**表示拦截多级请求路径
	 * @param registry
	 */
	//定义拦截器 添加需要匹配的路径a
	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(userInterceptor)
				.addPathPatterns("/cart/**","/order/**");
		//如果有多个拦截器,可以addInterceptor多次
	}

}
