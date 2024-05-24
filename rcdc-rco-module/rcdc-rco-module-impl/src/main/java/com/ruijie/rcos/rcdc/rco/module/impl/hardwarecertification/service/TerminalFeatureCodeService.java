package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service;

import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 *
 * Description: 终端特征码关联表service服务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public interface TerminalFeatureCodeService {

    /**
     * 根据terminalId获取终端特征码。若还没生成硬件特征码，则会生成硬件特征码并且入库，然后返回
     *
     * @param terminalId terminalId
     * @return 终端特征码
     * @throws BusinessException 业务异常
     */
    String saveAndGetFeatureCode(String terminalId) throws BusinessException;

    /**
     *  根据terminalId删除记录
     *
     *  @param terminalId 终端ID
     */
    void deleteByTerminalId(String terminalId);
}
