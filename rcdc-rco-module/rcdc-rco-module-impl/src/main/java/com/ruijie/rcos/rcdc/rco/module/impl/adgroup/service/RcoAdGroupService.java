package com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service;


import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: Ad域组相关操作
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author zqj
 */
public interface RcoAdGroupService {


    /**
     * 检查是否分配AD域安全组
     * @param userName 用户名
     * @return 是否已分配
     */
    boolean checkUserAdGroupResult(String userName);

    /**
     * 获取用户所属的安全组集合
     * @param userId 用户组ID
     * @return 安全组ID集合
     * @throws BusinessException 业务异常
     */
    List<UUID> getUserRelatedAdGroupList(UUID userId) throws BusinessException;
}
