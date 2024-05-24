package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DeleteIDVDesktopOperateLogTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 云桌面审计日志API
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/2
 *
 * @author XiaoJiaXin
 */
public interface DesktopOpLogMgmtAPI {

    /**
     * 构造request
     *
     * @param deskId  云桌面ID
     * @param operateLogTypeEnums 操作入口类型
     * @return DesktopOpLogRequest
     * @throws BusinessException 业务异常
     */
    DesktopOpLogDTO buildDeleteDesktopOpLogRequest(UUID deskId, DeleteIDVDesktopOperateLogTypeEnums operateLogTypeEnums) throws BusinessException;

    /**
     * 保存云桌面审计日志
     *
     * @param desktopOpLogRequest 审计日志request
     */
    void saveOperateLog(DesktopOpLogDTO desktopOpLogRequest);
}