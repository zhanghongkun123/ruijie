package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/15
 *
 * @author nt
 */
public interface AppTerminalAPI {

    /**
     * 获取windows软终端全量包下载路径
     * @param terminalType 操作系统类型枚举
     * @return 下载路径
     * @throws BusinessException 业务异常
     */
    String getAppDownloadUrl(CbbTerminalTypeEnums terminalType) throws BusinessException;
}
