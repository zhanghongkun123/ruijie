package com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.user;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 16:44
 *
 * @author coderLee23
 */
public interface UserSyncDataStrategy {

    /**
     * 是否是ad域或ldap
     * 
     * @return true or false
     */
    IacUserTypeEnum getCbbUserType();


    /**
     * 用户数据
     * 
     * @param userSyncData 用户数据
     * @throws BusinessException 业务异常
     */
    void syncData(UserSyncDataDTO userSyncData) throws BusinessException;

}
