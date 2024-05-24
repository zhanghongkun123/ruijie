package com.ruijie.rcos.rcdc.rco.module.impl.computer.service;

import com.ruijie.rcos.rcdc.rco.module.impl.computer.dto.NotifyComputerDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: pc终端操作通知
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/17
 *
 * @author zqj
 */
public interface NotifyComputerChangeService {


    /**
     * 增量同步用户数据
     * @param notifyComputerDTO 实体
     * @throws BusinessException 业务异常
     */
    void notifyComputerChange(NotifyComputerDTO notifyComputerDTO) throws BusinessException;



}
