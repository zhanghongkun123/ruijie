package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsAdminTokenVerifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BaseAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.uws.AdminTokenRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RandomTokenResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: UWS 拓展API
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-19 13:52:00
 *
 * @author zjy
 */
public interface UwsDockingAPI {

    /**
     * 发送桌面创建通知
     *
     * @param desktopId 桌面id
     * @Date 2021/11/23 11:08
     * @Author zjy
     **/
    void notifyDesktopAdd(UUID desktopId);

    /**
     * 发送桌面更新通知
     *
     * @param desktopId      桌面id
     * @param cloudDeskState 桌面状态
     * @Date 2021/11/23 18:20
     * @Author zjy
     **/
    void notifyDesktopStateUpdate(UUID desktopId, CbbCloudDeskState cloudDeskState);

    /**
     * 发送桌面删除通知
     *
     * @param desktopId 桌面id
     * @Date 2021/11/24 10:53
     * @Author zjy
     **/
    void notifyDesktopDelete(UUID desktopId);

    /**
     * 通知桌面从回收站恢复
     *
     * @param desktopId 桌面id
     * @Date 2021/12/6 10:30
     * @Author zjy
     **/
    void notifyDesktopRecover(UUID desktopId);

    /**
     * 用户删除通知
     *
     * @param userIds 用户id列表
     * @Date 2021/11/22 9:47
     * @Author zjy
     **/
    void notifyUserDeleted(List<UUID> userIds);

    /**
     * 用户密码更新通知
     *
     * @param userIds 用户id列表
     * @Date 2021/11/22 9:47
     * @Author zjy
     **/
    void notifyUserUpdatePwd(List<UUID> userIds);

    /**
     * 用户禁用通知
     *
     * @param userIds 用户id列表
     * @Date 2021/11/22 9:47
     * @Author zjy
     **/
    void notifyUserDisabled(List<UUID> userIds);

    /**
     * 获取UWS Admin 临时token
     *
     * @param baseAdminRequest admin信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2021/11/29 10:26
     * @Author zjy
     **/
    RandomTokenResponse getRandomToken(BaseAdminRequest baseAdminRequest) throws BusinessException;

    /**
     * 验证临时token是否有效
     *
     * @param adminTokenRequest adminToken
     * @return 返回值
     * @Date 2021/11/29 10:37
     * @Author zjy
     **/
    UwsAdminTokenVerifyDTO verifyAdminToken(AdminTokenRequest adminTokenRequest);

    /**
     * 修改密码开关更改通知
     *
     * @param allowChangePwd 是否允许修改
     * @Date 2021/12/3 8:21
     * @Author zjy
     **/
    void notifyModifyPwdConfigChanged(Boolean allowChangePwd);

    /**
     * 初始化 uws cm app
     *
     * @Date 2021/12/27 16:29
     * @Author zjy
     **/
    void initCmApp();

    /**
     * 获取UWS组件启用情况标识
     *
     * @return 启用情况
     */
    String getUwsComponentFlag();

    /**
     * 终端状态更新通知
     *
     * @param desktopId 绑定桌面id
     * @param terminalId 终端id
     * @param terminalState 终端状态
     * @Date 2022/4/6 15:23
     * @Author zhengjingyong
     **/
    void notifyTerminalStateUpdate(UUID desktopId, String terminalId, CbbTerminalStateEnums terminalState);

    /**
     * 初始化 uws cm ISO
     *
     * @Date 2024/1/5 16:29
     * @Author lifeng
     **/
    void initCmISO();
}

