package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

/**
 * Description: 终端管理密码扩API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/28
 *
 * @author yxq
 */
public interface TerminalManagePwdAPI {
    /**
     * 根据终端ID删除
     * @param terminalId 用户id
     */
    void deleteAuthenticationByTerminalId(UUID terminalId);
}
