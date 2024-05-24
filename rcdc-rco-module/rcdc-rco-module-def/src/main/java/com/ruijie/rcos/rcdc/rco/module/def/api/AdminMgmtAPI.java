package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.RcoLoginAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpgradeAdminRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * <p>Title: AdminMgmtAPI</p>
 * <p>Description: Function Description</p>
 * <p>Copyright: Ruijie Co., Ltd. (c) 2020</p>
 * <p>@Author: xiejian</p>
 * <p>@Date: 2020/1/7 14:51</p>
 */
public interface AdminMgmtAPI {

    /**
     * 升级为管理员
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void upgradeAdmin(UpgradeAdminRequest request) throws BusinessException;

    /**
     * 更新管理员数据权限
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    void updateAdminDataPermission(UpdateAdminDataPermissionRequest request) throws BusinessException;

    /**
     * 管理员登录操作
     *
     * @param rcoLoginRequest 认证请求
     * @return 管理员DTO类
     * @throws BusinessException 业务异常
     */
    IacAdminDTO loginAdmin(RcoLoginAdminRequest rcoLoginRequest) throws BusinessException;

    /**
     * 管理员免认证登录
     *
     * @param rcoLoginRequest 认证请求
     * @return 管理员DTO类
     * @throws BusinessException 业务异常
     */
    IacAdminDTO loginAdminNoAuth(RcoLoginAdminRequest rcoLoginRequest) throws BusinessException;

    /**
     * 修改为指定类型管理员的禁用状态
     *
     * @param userType 用户类型
     * @throws BusinessException 业务异常
     */
    void modifyAdminStatus(IacUserTypeEnum userType) throws BusinessException;

    /**
     * 同步修改管理员禁用状态
     *
     * @param userDetail 用户信息
     * @throws BusinessException 业务异常
     */
    void syncUserStatusToAdmin(IacUserDetailDTO userDetail) throws BusinessException;


    /**
     * 管理员预登录终端并获取会话
     *
     * @param adminId 管理员id
     * @param terminalId 终端id
     * @return UUID
     * @throws BusinessException 业务异常
     */
    UUID preLoginTerminalAndGetSessionId(UUID adminId, String terminalId) throws BusinessException;

    /**
     * 根据名称查管理员信息
     * 
     * @param userName 用户名
     * @return IacAdminDTO
     * @throws BusinessException 业务异常
     */
    IacAdminDTO getAdminByUserName(String userName) throws BusinessException;
}
