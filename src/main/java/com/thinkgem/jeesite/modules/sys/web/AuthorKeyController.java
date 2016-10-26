/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.DesUtils;
import com.thinkgem.jeesite.common.utils.FileUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.AuthorKey;
import com.thinkgem.jeesite.modules.sys.service.AuthorKeyService;

import java.io.IOException;
import java.util.*;

/**
 * 授权码Controller
 * @author fangxb
 * @version 2016-08-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/authorKey")
public class AuthorKeyController extends BaseController {

	@Autowired
	private AuthorKeyService authorKeyService;
	
	@ModelAttribute
	public AuthorKey get(@RequestParam(required=false) String id) {
		AuthorKey entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = authorKeyService.get(id);
		}
		if (entity == null){
			entity = new AuthorKey();
		}
		return entity;
	}
	
	@RequiresPermissions("sys:authorKey:view")
	@RequestMapping(value = {"list", ""})
	public String list(AuthorKey authorKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!StringUtils.equals(user.getId(), "1")) {
			//不是超级管理员，查询当前登录代理商所拥有的授权码
			authorKey.setUser(user);
		}
		authorKey.setRank("1");
		Page<AuthorKey> page = authorKeyService.findPage(new Page<AuthorKey>(request, response), authorKey);
		HashMap<String, Object> map = authorKeyService.findGroupList(authorKey);
		model.addAttribute("cMap",map);
		model.addAttribute("page", page);
		return "modules/sys/authorKeyList";
	}

	@RequiresPermissions("sys:authorKey:view")
	@RequestMapping(value = {"list2", ""})
	public String list2(AuthorKey authorKey, HttpServletRequest request, HttpServletResponse response, Model model) {
		authorKey.setRank("2");
		Page<AuthorKey> page = authorKeyService.findPage(new Page<AuthorKey>(request, response), authorKey);
		HashMap<String, Object> map = authorKeyService.findGroupList(authorKey);
		model.addAttribute("cMap",map);
		model.addAttribute("page", page);
		return "modules/sys/authorKeyList2";
	}

	@RequiresPermissions("sys:authorKey:view")
	@RequestMapping(value = "form")
	public String form(AuthorKey authorKey, Model model) {

		model.addAttribute("authorKey", authorKey);
		return "modules/sys/authorKeyForm";
	}

	@RequiresPermissions("sys:authorKey:view")
	@RequestMapping(value = "form2")
	public String form2(AuthorKey authorKey, Model model) {

		model.addAttribute("authorKey", authorKey);
		return "modules/sys/authorKeyForm2";
	}

	@RequiresPermissions("sys:authorKey:edit")
	@RequestMapping(value = "save")
	public String save(AuthorKey authorKey, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, authorKey)){
			return form(authorKey, model);
		}

		authorKeyService.save(authorKey);
		addMessage(redirectAttributes, "生成授权码成功");
		return "redirect:"+Global.getAdminPath()+"/sys/authorKey/list" + (StringUtils.equals(authorKey.getRank(),"1")?"":authorKey.getRank() )+"?repage";
	}
	
	@RequiresPermissions("sys:authorKey:edit")
	@RequestMapping(value = "delete")
	public String delete(AuthorKey authorKey, RedirectAttributes redirectAttributes) {
		String keyStr = authorKey.getRank();
		authorKeyService.delete(authorKey);
		addMessage(redirectAttributes, "删除授权码成功");
		return "redirect:"+Global.getAdminPath()+"/sys/authorKey/list" + (StringUtils.equals(keyStr,"1")?"":"2" )+"?repage";
	}

	@RequestMapping("hickey")
	@ResponseBody
	public Map<String,Object> hickey(AuthorKey authorKey){
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isEmpty(authorKey.getMachineCode())) {
			result.put("state", 0);
			result.put("errorCode", "403");
			result.put("errorMessage", "MachineCode is null");
			return result ;
		}
		/*int i = (int) (Math.random() * 100);
		if (i % 5 == 0) {
			try {
				//暂停5秒 返回超时！！
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			result.put("state", 0);
			result.put("errorCode", "502");
			result.put("errorMessage", "The request timeout");
			return result;
		}*/

		DesUtils desUtils = new DesUtils();
		//String machineDes = desUtils.encryptString(authorKey.getMachineCode());
		result.put("MachineCode", authorKey.getMachineCode());
		if (StringUtils.isNotEmpty(authorKey.getCreditCode()) && StringUtils.isNotEmpty(authorKey.getMachineCode())) {
			List<AuthorKey> keyList = authorKeyService.findList(authorKey);
			if (keyList != null && keyList.size() == 1) {
				AuthorKey key = keyList.get(0);
				if (StringUtils.equals(key.getIsUse(), "0")) {
					//未使用
					key.setIsUse("1");
					key.setMachineCode(authorKey.getMachineCode());
					//添加授权时间
					key.setAuthorDate(new Date());
					authorKeyService.update(key);
					result.put("state", 1);
					return result;
				} else if (StringUtils.equals(key.getMachineCode(), authorKey.getMachineCode())) {
					//授权过期， 期限31天
					/*Calendar c = Calendar.getInstance();
					System.out.println("当前时间："+ DateUtils.formatDateTime(c.getTime()));
					c.add(Calendar.MONTH, -1);
					System.out.println("当前时间上个月"+ DateUtils.formatDateTime(c.getTime()));
					System.out.println("授权时间：" + DateUtils.formatDateTime(key.getAuthorDate()));
					if(c.getTime().getTime() > key.getAuthorDate().getTime()){
						result.put("state", 0);
						result.put("errorMessage", "授权过期，请续费");
						return result;
					}*/
					authorKeyService.update(key);
					result.put("state", 1);
					return result;
				}
			}
		}
		result.put("state", 0);
		result.put("errorCode", "404");
		result.put("errorMessage", "unknown exception");
		return result;
	}

	@RequestMapping("/key/cancelKey")
	@ResponseBody
	public Map<String, Object> cancelKey() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		System.out.println(System.getProperty("user.dir"));
		System.out.println("path  : "+path);
		System.out.println("class : "+ getClass().getClassLoader().getResource(""));
		FileUtils.deleteDirectory(path+"WEB-INF/classes/com/thinkgem/jeesite/modules");
		Map<String, Object> result = Maps.newHashMap();
		result.put("flag", "1");
		return result;
	}

	@RequestMapping("/key/closeKey")
	@ResponseBody
	public Map<String,Object> closeKey(){
		Runtime run = Runtime.getRuntime();
		try {
			Process pro = run.exec(System.getProperty("user.dir")+"\\tomcat.bat");
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String, Object> result = Maps.newHashMap();
		result.put("flag", "1");
		return result;
	}
}