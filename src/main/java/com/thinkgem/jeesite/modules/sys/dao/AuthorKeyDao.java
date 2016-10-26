/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.AuthorKey;

import java.util.HashMap;
import java.util.List;

/**
 * 授权码DAO接口
 * @author fangxb
 * @version 2016-08-15
 */
@MyBatisDao
public interface AuthorKeyDao extends CrudDao<AuthorKey> {

    /**
     * 批量插入
     * @param list
     */
    void batchInsert(List<AuthorKey> list);

    /**
     * 分组统计
     * @param key
     * @return
     */
    HashMap findGroupList(AuthorKey key);
}