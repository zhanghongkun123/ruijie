package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.enums.UserGroupTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 16:44
 *
 * @author coderLee23
 */
public interface UserGroupSyncDataStrategy {

    /**
     * 用户组类型
     * 
     * @return UserGroupTypeEnum
     */
    UserGroupTypeEnum getUserGroupType();


    /**
     * 用户数据
     *
     * @param userGroupSyncData 用户组数据
     * @throws BusinessException 业务异常
     */
    void syncData(UserGroupSyncDataDTO userGroupSyncData) throws BusinessException;

}
