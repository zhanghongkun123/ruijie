package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * 云桌面GT管理API接口
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author linrenjian
 */
public interface DesktopGuestToolMgmtAPI {


    /**
     * 定时解除告警新增告警
     * @throws BusinessException 异常
     */
    void releaseAlarmAndCreateAlarm() throws BusinessException;

    /**
     *解除告警
     * @param deskId 桌面ID
     */
    void releaseGuestToolAlarm(UUID deskId);
}
