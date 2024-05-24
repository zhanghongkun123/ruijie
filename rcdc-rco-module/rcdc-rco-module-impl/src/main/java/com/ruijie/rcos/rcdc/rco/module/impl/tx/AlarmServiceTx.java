package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbAlarmDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/22
 *
 * @author XiaoJiaXin
 */
public interface AlarmServiceTx {

    /**
     * 保存记录告警
     * @param alarmDTO DTO
     * @throws BusinessException 业务异常
     */
    void saveAlarmAndRecord(CbbAlarmDTO alarmDTO) throws BusinessException;
}
