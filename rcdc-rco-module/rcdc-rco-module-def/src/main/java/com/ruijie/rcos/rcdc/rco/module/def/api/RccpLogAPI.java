package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.rccplog.dto.RccpLogCollectStateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/14 16:55
 *
 * @author ketb
 */
public interface RccpLogAPI {

    /**
     * 收集rccp日志
     * @throws BusinessException 业务异常
     */
    void collectRccpLog() throws BusinessException;

    /**
     * 查询事件状态
     * @return 返回状态结果
     * @throws BusinessException 业务异常
     */
    RccpLogCollectStateDTO getCollectRccpLogState() throws BusinessException;

}
