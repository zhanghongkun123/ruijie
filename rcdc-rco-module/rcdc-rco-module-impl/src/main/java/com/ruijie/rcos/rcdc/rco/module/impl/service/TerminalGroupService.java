package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author chenzj
 */
public interface TerminalGroupService {
    
    /**
     * 获取终端分组层级名称数组
     * @param groupId 分组id
     * @return 分组层级名称数组
     * @throws BusinessException 业务异常
     */
    String[] getTerminalGroupNameArr(UUID groupId) throws BusinessException;

}
