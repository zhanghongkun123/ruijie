package com.ruijie.rcos.rcdc.rco.module.impl.est;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31
 *
 * @author lihengjing
 */
public interface EstCommonActionNotifyService {

    /**
     * EstCommonActionService.responseToEst RCDC跟EST透传通知接口
     *
     * @param terminalId 终端id
     * @param subAction 子动作
     * @param result JSON内容
     * @throws BusinessException 异常
     */
    void responseToEst(String terminalId, String subAction, String result) throws BusinessException;

}
