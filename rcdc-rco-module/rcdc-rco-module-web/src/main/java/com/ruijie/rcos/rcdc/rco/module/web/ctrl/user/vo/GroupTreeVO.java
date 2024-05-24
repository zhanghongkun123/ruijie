package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 分组树形结构
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/17
 *
 * @author Jarman
 */
public class GroupTreeVO extends IdLabelEntry {

    @SuppressWarnings("PMD.ArrayOrListPropertyNamingRule")
	private String[] childrenArr;

    public String[] getChildren() {
        return childrenArr;
    }

    public void setChildren(String[] children) {
        this.childrenArr = children;
    }
}
