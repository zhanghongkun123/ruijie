package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.IdvTerminalGroupDetailResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/26 11:41
 *
 * @author conghaifeng
 */
public interface UserTerminalGroupMgmtAPI {

    /**
     * @description 创建idv|voi终端组
     * @param request 创建idv|voi终端组请求
     * @return UUID 唯一标识
     * @throws BusinessException 业务异常
     */
    UUID createIdvTerminalGroup(IdvCreateTerminalGroupRequest request) throws BusinessException;

    /**
     * @description 编辑idv|voi终端组
     * @param request 编辑idv|voi终端组请求
     * @throws BusinessException 业务异常
     */
    void editIdvTerminalGroup(IdvEditTerminalGroupRequest request) throws BusinessException;

    /**
     * @description 删除idv|voi终端组
     * @param id 删除idv|voi终端组请求
     * @throws BusinessException 业务异常
     */
    void deleteTerminalGroupDesktopConfig(UUID id) throws BusinessException;

    /**
     * @description idv|voi终端组详细信息
     * @param groupId 获取idv|voi终端组详细信息请求
     * @return IdvTerminalGroupDetailResponse idv终端组详细信息
     * @throws BusinessException 业务异常
     */
    IdvTerminalGroupDetailResponse idvTerminalGroupDetail(UUID groupId) throws BusinessException;

    /**
     * 判断是否有镜像绑定终端组
     * @param imageId 镜像ID
     * @return true  绑定
     */
    Boolean hasImageBindTerminalGroup(UUID imageId);
}
