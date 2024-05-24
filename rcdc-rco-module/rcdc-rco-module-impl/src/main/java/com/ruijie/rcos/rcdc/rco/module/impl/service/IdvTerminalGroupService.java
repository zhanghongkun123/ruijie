package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/12 19:26
 *
 * @author conghaifeng
 */
public interface IdvTerminalGroupService {

    /**
     * 创建idv终端组
     * @param request 创建idv终端组请求
     * @return UUID 唯一标识
     * @throws BusinessException 业务异常
     */
    UUID saveIdvTerminalGroup(IdvCreateTerminalGroupRequest request) throws BusinessException;

    /**
     * 编辑idv终端组
     * @param request 编辑idv终端组请求
     * @throws BusinessException 业务异常
     */
    void editIdvTerminalGroup(IdvEditTerminalGroupRequest request) throws BusinessException;

    /**
     * 删除终端组桌面配置
     * @param id 请求参数
     * @throws BusinessException 业务异常
     */
    void deleteTerminalGroupDesktopConfig(UUID id) throws BusinessException;
}
