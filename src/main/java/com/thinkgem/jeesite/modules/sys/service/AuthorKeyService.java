/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.IdGen;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.sys.entity.AuthorKey;
import com.thinkgem.jeesite.modules.sys.dao.AuthorKeyDao;

/**
 * 授权码Service
 * @author fangxb
 * @version 2016-08-15
 */
@Service
@Transactional(readOnly = true)
public class AuthorKeyService extends CrudService<AuthorKeyDao, AuthorKey> {

	@Autowired
	private AuthorKeyDao authorKeyDao;

	public AuthorKey get(String id) {
		return super.get(id);
	}
	
	public List<AuthorKey> findList(AuthorKey authorKey) {
		return super.findList(authorKey);
	}
	
	public Page<AuthorKey> findPage(Page<AuthorKey> page, AuthorKey authorKey) {
		return super.findPage(page, authorKey);
	}

	@Transactional(readOnly = false)
	public void update(AuthorKey key) {
		key.setLaseDate(new Date());
		authorKeyDao.update(key);
	}

	@Transactional(readOnly = false)
	public void save(AuthorKey authorKey) {
		//super.save(authorKey);
		String creditCode = authorKey.getCreditCode();
		int num = Integer.valueOf(creditCode);
		List<AuthorKey> list = Lists.newArrayList();
		Date newDate = new Date();
		for(int i= 0; i< num; i++ ) {
			String keyCode = Global.getConfig("author.key.prefix") + IdGen.randomBase62(5) + IdGen.randomBase62(25);

			AuthorKey key = new AuthorKey(keyCode);
			key.setUser(new User(authorKey.getUser().getId()));
			key.setCreateDate(newDate);
			key.setRank(authorKey.getRank());
			list.add(key);
		}
		authorKeyDao.batchInsert(list);
	}
	
	@Transactional(readOnly = false)
	public void delete(AuthorKey authorKey) {
		super.delete(authorKey);
	}


	public HashMap findGroupList(AuthorKey key) {
		HashMap map = new HashMap();
		key.setPage(null);
		//未使用
		key.setIsUse("0");
		List<AuthorKey> list = authorKeyDao.findAllList(key);
		map.put("isNot", list == null ? 0 : list.size());
		//已使用
		key.setIsUse("1");
		list = authorKeyDao.findAllList(key);
		map.put("isHas", list == null ? 0 : list.size());
		return map;
	}
}