package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.annotation.MaintainFilterAction;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.RcoLoginAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.RcdcIacAdminLoginFailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AdminLoginRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AdminLoginResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.AdminLoginExceptionEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: 管理员在终端登录请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/19 10:26
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.START_ADMIN_LOGIN)
@MaintainFilterAction
public class AdminLoginHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLoginHandlerSPIImpl.class);

    private static final Integer FAILURE = -1;

    private static final Integer IN_MAINTENANCE_CODE = -2;


    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;


    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;


    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");
        if (isInMaintenanceMode()) {
            CbbResponseShineMessage message = ShineMessageUtil.buildErrorResponseMessage(request, IN_MAINTENANCE_CODE);
            messageHandlerAPI.response(message);
            return;
        }
        CbbResponseShineMessage cbbResponseShineMessage;
        AdminLoginRequestDTO adminLoginRequestDTO = null;
        String macAddr;
        try {
            TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
            macAddr = Optional.ofNullable(terminalDTO.getMacAddr()).orElse(terminalDTO.getWirelessMacAddr());
            macAddr = StringUtils.isEmpty(macAddr) ? macAddr : macAddr.toUpperCase();
        } catch (BusinessException e) {
            macAddr = request.getTerminalId();
            LOGGER.error(String.format("管理员在终端[%s]登录获取MAC地址失败，设置为SPI接口terminalId的值", macAddr), e);
        }

        try {
            adminLoginRequestDTO = getAdminLoginRequest(request);
            cbbResponseShineMessage = adminLogin(request, adminLoginRequestDTO);
            LOGGER.info("管理员[{}]在终端[{}]登录", adminLoginRequestDTO.getAdminName(), macAddr);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_LOGIN_ON_TERMINAL_LOG_KEY, adminLoginRequestDTO.getAdminName(), macAddr);
        } catch (BusinessException e) {
            LOGGER.error("管理员{}在终端{}登录异常", adminLoginRequestDTO.getAdminName(), request.getTerminalId(), e);
            Integer code = AdminLoginExceptionEnum.getCorrespondingCode(e.getKey());
            String errorMsg = e.getAttachment(UserTipContainer.Constants.USER_TIP, e.getI18nMessage());
            // IAC业务异常key为纯数字，IAC业务异常返回国际化后的提示语给shine
            if (StringUtils.isNumeric(e.getKey())) {
                RcdcIacAdminLoginFailDTO rcdcIacAdminLoginFailDTO = new RcdcIacAdminLoginFailDTO();
                rcdcIacAdminLoginFailDTO.setIacBusinessMsg(errorMsg);
                cbbResponseShineMessage = ShineMessageUtil.buildResponseMessageWithContent(request, code, rcdcIacAdminLoginFailDTO);
            } else {
                cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, code);
            }
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_LOGIN_FAIL_IN_TERMINAL, e, adminLoginRequestDTO.getAdminName(), macAddr,
                    errorMsg);

        } catch (Exception e) {
            LOGGER.error("未知异常", e);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, FAILURE);
        }

        LOGGER.info("管理员在终端登录响应的响应信息为：[{}]", JSON.toJSONString(cbbResponseShineMessage));
        messageHandlerAPI.response(cbbResponseShineMessage);
    }


    private boolean isInMaintenanceMode() {
        SystemMaintenanceState systemMaintenanceState = maintenanceModeMgmtAPI.getMaintenanceMode();
        if (systemMaintenanceState != SystemMaintenanceState.NORMAL) {
            LOGGER.info("当前系统维护模式状态[{}], 终端管理员登陆业务暂停！", systemMaintenanceState);
            return true;
        }
        return false;
    }

    private CbbResponseShineMessage adminLogin(CbbDispatcherRequest request, AdminLoginRequestDTO adminLoginRequestDTO) throws BusinessException {
        RcoLoginAdminRequest rcoLoginAdminRequest = new RcoLoginAdminRequest().setUserName(adminLoginRequestDTO.getAdminName())
                .setRawPassword(adminLoginRequestDTO.getPassword()).setOnlyValidAdminPwd(Boolean.TRUE).setSubSystem(SubSystem.CDC);
        IacAdminDTO baseAdminDTO = adminMgmtAPI.loginAdmin(rcoLoginAdminRequest);
        Assert.notNull(baseAdminDTO, "baseAdminDTO cannot be null!");

        // 除了内置管理员admin、sysadmin无须检查权限问题 不进入分支处理
        // 5.4分级分权 除角色审计管理员与安全管理员外都可以登录 有关联超级管理员的 不需要走这个分支
        // 是否角色名称是超级管理员
        boolean isRoleAdmin = adminPermissionAPI.roleIsAdminOrAdminNameIsSysadmin(baseAdminDTO.getId());
        if (!DefaultAdmin.SYSADMIN.getName().equals(adminLoginRequestDTO.getAdminName())
                && !DefaultAdmin.ADMIN.getName().equals(adminLoginRequestDTO.getAdminName()) && !isRoleAdmin) {
            UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();
            String adminName = adminLoginRequestDTO.getAdminName();
            // 检查管理员是否是管理员
            checkAllowAdmin(roleIdArr, adminName);
            // 检查管理员是否有终端所在终端组权限
            checkHasTerminalGroupPermission(baseAdminDTO, request.getTerminalId());
        }

        // 添加会话记录，返回会话ID
        UUID sessionId = cacheManager.add(baseAdminDTO, request.getTerminalId());

        AdminLoginResultDTO adminLoginResultDTO = new AdminLoginResultDTO(sessionId);

        return ShineMessageUtil.buildResponseMessage(request, adminLoginResultDTO);
    }

    /**
     * 检查是否是系统管理员 (除审计 安全管理员外的)
     *
     * @param roleIdArr roleIdArr
     * @param adminName 管理员名称
     * @throws BusinessException ex
     */
    private void checkAllowAdmin(UUID[] roleIdArr, String adminName) throws BusinessException {

        List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);

        LOGGER.info("baseRoleDTOList: {}", JSON.toJSONString(baseRoleDTOList));
        // 角色 是内置审计 安全管理员 不允许接入
        boolean enableAllowAdmin = baseRoleDTOList.stream()
                .anyMatch(dto -> RoleType.AUDADMIN.getName().equals(dto.getRoleName()) || RoleType.SECADMIN.getName().equals(dto.getRoleName()));
        if (enableAllowAdmin) {
            LOGGER.error("终端登录的管理员{}不是系统管理员", adminName);
            throw new BusinessException(BusinessKey.RCDC_RCO_NOT_SYS_ADMIN, adminName);
        }
    }


    private void checkHasTerminalGroupPermission(IacAdminDTO baseAdminDTO, String termianlId) throws BusinessException {
        UUID adminId = baseAdminDTO.getId();
        String adminName = baseAdminDTO.getUserName();

        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(termianlId);
        UUID terminalGroupId = terminalDTO.getTerminalGroupId();

        ListTerminalGroupIdRequest listTerminalGroupRequest = new ListTerminalGroupIdRequest();
        listTerminalGroupRequest.setAdminId(adminId);
        ListTerminalGroupIdResponse listTerminalGroupIdResponse = adminDataPermissionAPI.listTerminalGroupIdByAdminId(listTerminalGroupRequest);
        List<String> hasPermissionUUIDList = listTerminalGroupIdResponse.getTerminalGroupIdList();
        LOGGER.info("当前终端所在终端组id：{}，当前管理员有权限的终端组id列表：{}", terminalGroupId, JSON.toJSONString(hasPermissionUUIDList));

        boolean hasTerminalPermission =
                hasPermissionUUIDList.stream().anyMatch(hasPermissionUUID -> terminalGroupId.toString().equals(hasPermissionUUID));

        if (!hasTerminalPermission) {
            LOGGER.info("管理员[{}]没有当前终端所在终端组权限", adminName);
            throw new BusinessException(BusinessKey.RCDC_RCO_ADMIN_NOT_HAS_TERMINAL_GROUP_PERMISSION, adminName,
                    terminalDTO.getUpperMacAddrOrTerminalId());
        }
        LOGGER.info("管理员[{}]具有当前终端所在终端组权限", adminName);
    }

    private AdminLoginRequestDTO getAdminLoginRequest(CbbDispatcherRequest request) {
        AdminLoginRequestDTO adminLoginRequestDTO = JSONObject.parseObject(request.getData(), AdminLoginRequestDTO.class);
        Assert.notNull(adminLoginRequestDTO, "cannot get admin info from request");
        Assert.hasText(adminLoginRequestDTO.getAdminName(), "adminName cannot be blank!");
        Assert.hasText(adminLoginRequestDTO.getPassword(), "password cannot be blank!");
        Assert.hasText(adminLoginRequestDTO.getAction(), "action cannot be blank!");
        return adminLoginRequestDTO;
    }
}
