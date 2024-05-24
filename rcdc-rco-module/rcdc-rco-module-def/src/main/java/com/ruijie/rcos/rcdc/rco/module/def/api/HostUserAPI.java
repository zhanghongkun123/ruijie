package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.HostUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.HostUserGroupBindDeskNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/22
 *
 * @author zqj
 */
public interface HostUserAPI {


    /**
     * 获取桌面关联用户分页
     *
     * @param pageRequest pageRequest
     * @return list
     */
    DefaultPageResponse<UserListDTO> deskBindUserPage(PageSearchRequest pageRequest);

    /**
     * 删除关系
     *
     * @param id id
     * @throws BusinessException 业务异常
     */
    void removeById(UUID id) throws BusinessException;

    /**
     * 获取主机用户关系
     *
     * @param id id
     * @return HostUserDTO
     */
    HostUserDTO findById(UUID id);

    /**
     * 桌面解除用户关联
     *
     * @param deskId deskId
     * @param userId userId
     * @throws BusinessException BusinessException
     */
    void unBindUser(UUID deskId, UUID userId) throws BusinessException;

    /**
     * 获取关联关系列表
     *
     * @param deskId deskId
     * @return 列表
     */
    List<HostUserDTO> findByDeskId(UUID deskId);

    /**
     * 清理终端信息
     *
     * @param terminalId terminalId
     */
    void clearTerminalIdByUserAndTerminalId(String terminalId);

    /**
     * 根据桌面池ID获取用户组绑定的桌面数量
     *
     * @param desktopPoolId 桌面池ID
     * @return List<HostUserGroupBindDeskNumDTO>
     */
    List<HostUserGroupBindDeskNumDTO> countGroupDeskNumByDesktopPoolId(UUID desktopPoolId);
}
