package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.callback;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月28日
 *
 * @author zhanghongkun
 */
@FunctionalInterface
public interface DomainSaveMappingCallBack {

    /**
     * 回调同步用户接口
     * 
     * @throws BusinessException 业务异常
     */
    void callBackSyncUser() throws BusinessException;
}
