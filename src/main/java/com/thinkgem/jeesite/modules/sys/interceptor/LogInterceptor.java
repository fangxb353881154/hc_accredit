/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.interceptor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.AppDesUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.modules.sys.utils.LogUtils;

/**
 * 日志拦截器
 * @author ThinkGem
 * @version 2014-8-19
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor {

	private static final ThreadLocal<Long> startTimeThreadLocal =
			new NamedThreadLocal<Long>("ThreadLocal StartTime");
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,  Object handler) throws Exception {
		if (logger.isDebugEnabled()){
			long beginTime = System.currentTimeMillis();//1、开始时间  
	        startTimeThreadLocal.set(beginTime);		//线程绑定变量（该数据只有当前请求的线程可见）  
	        logger.debug("开始计时: {}  URI: {}", new SimpleDateFormat("hh:mm:ss.SSS").format(beginTime), request.getRequestURI());
		}
		AppDesUtils app = new AppDesUtils();
		String date = app.decryptString(Global.getConfig("trial.date"));
		String startUp = Global.getConfig("tomcat.startup");
		if (StringUtils.isEmpty(startUp) || StringUtils.isEmpty(date)) {
			logger.debug("系统异常，Tomcat启动时间：{}   启动时限：{}", startUp, date);
			throw  new RuntimeException("系统异常");
		}
		try {
			long su = Long.valueOf(startUp);
			int d = Integer.valueOf(date);
			long newDate = new Date().getTime();
			if (newDate - su > (1000 * 60 * 60 * 24 * 7)) {
				logger.debug("授权超时，关闭项目...");
				Runtime run = Runtime.getRuntime();
				try {
					String path = System.getProperty("user.dir") + "\\tomcat.bat";
					logger.debug(path);
					Process pro = run.exec(path);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		} catch (Exception e) {
			logger.debug("系统异常，配置错误！Tomcat启动时间：{}   启动时限：{}", startUp, date);
			return false;
		}


		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		if (modelAndView != null){
			logger.info("ViewName: " + modelAndView.getViewName());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {

		// 保存日志
		LogUtils.saveLog(request, handler, ex, null);
		
		// 打印JVM信息。
		if (logger.isDebugEnabled()){
			long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）  
			long endTime = System.currentTimeMillis(); 	//2、结束时间  
	        logger.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
	        		new SimpleDateFormat("hh:mm:ss.SSS").format(endTime), DateUtils.formatDateTime(endTime - beginTime),
					request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024, Runtime.getRuntime().totalMemory()/1024/1024, Runtime.getRuntime().freeMemory()/1024/1024, 
					(Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024); 
		}
		
	}

}
