package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RoleType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.CreateAdminWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.FirstEnableDefaultAdminWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.LoginAdminWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.ModifyAdminPwdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.ModifyOtherAdminPwdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.UpdateAdminWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.UpgradeAdminWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月12日
 * 
 * @author zhuangchenwu
 */
@Service
public class AdminCustomValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminCustomValidation.class);

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    /**
     * 管理员登录请求参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void loginAdminValidate(LoginAdminWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

    }

    /**
     * 创建管理员请求参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createAdminValidate(CreateAdminWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        roleTypeValidate(request.getRoleId(), request.getUserGroupArr(), request.getTerminalGroupArr());
    }


    /**
     * 升级管理员请求参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void upgradeAdminValidate(UpgradeAdminWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        roleTypeValidate(request.getRoleId(), request.getUserGroupArr(), request.getTerminalGroupArr());
    }

    /**
     * 编辑管理员请求参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void updateAdminValidate(UpdateAdminWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        roleTypeValidate(request.getRoleId(), request.getUserGroupArr(), request.getTerminalGroupArr());
    }

    /**
     * 修改管理员密码请求参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void modifyAdminPwdValidate(ModifyAdminPwdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");


    }

    /**
     * 修改管理员密码请求参数校验
     * 
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void modifyOtherAdminPwdValidate(ModifyOtherAdminPwdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

    }

    /**
     * 第一次启用内置管理员
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void firstEnableDefaultAdminPwdValidate(FirstEnableDefaultAdminWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

    }

    private void roleTypeValidate(UUID roleId, String[] userGroupIdArr, String[] terminalGroupIdArr) throws BusinessException {

        IacRoleDTO role = baseRoleMgmtAPI.getRole(roleId);
        if (role == null) {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_ROLE_ERROR);
        }

        // 如果是内置的审计管理员 安全管理员 系统管理员 不需要选择用户组 终端组校验
        if (!RoleType.AUDADMIN.getName().equals(role.getRoleName()) && !RoleType.SECADMIN.getName().equals(role.getRoleName())
                && !RoleType.ADMIN.getName().equals(role.getRoleName())) {
            // 校验用户组
            validSysadminUserGroupId(userGroupIdArr);
            // 校验终端组
            validSysadminTerminalGroupId(terminalGroupIdArr);
        }


    }

    private String getRoleName(UUID roleId) throws BusinessException {
        try {
            // roleID为非法的角色ID
            IacRoleDTO baseRoleDTO = baseRoleMgmtAPI.getRole(roleId);
            return baseRoleDTO.getRoleName();
        } catch (BusinessException e) {
            LOGGER.error("valid role info error!", e);
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_ROLE_ERROR, e);
        }
    }

    private void validSysadminUserGroupId(String[] userGroupIdArr) throws BusinessException {
        if (userGroupIdArr != null && userGroupIdArr.length > 0) {
            IacUserGroupDetailDTO[] userGroupDTOArr = userGroupAPI.getAllUserGroup();
            boolean isIncludeDefaultGroup = false;
            for (String userGroupId : userGroupIdArr) {
                if (userGroupId.equals(UserGroupHelper.USER_GROUP_ROOT_ID)) {
                    continue;
                }
                if (userGroupId.equals(IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID.toString())) {
                    isIncludeDefaultGroup = true;
                }
                boolean isExist = false;
                for (IacUserGroupDetailDTO dto : userGroupDTOArr) {
                    if (userGroupId.equals(dto.getId().toString())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    LOGGER.error("illegal userGroupId [{}]", userGroupId);
                    throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_USER_GROUP_ID_ERROR);
                }
            }
            if (!isIncludeDefaultGroup) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_DEFAULT_USER_GROUP_ID_ERROR);
            }
        } else {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_DEFAULT_USER_GROUP_ID_ERROR);
        }
    }

    private void validSysadminTerminalGroupId(String[] terminalGroupIdArr) throws BusinessException {
        if (terminalGroupIdArr != null && terminalGroupIdArr.length > 0) {
            List<CbbTerminalGroupDetailDTO> terminalGroupDTOList = terminalGroupMgmtAPI.listTerminalGroup();
            boolean isIncludeDefaultGroup = false;
            for (String terminalGroupId : terminalGroupIdArr) {
                if (terminalGroupId.equals(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID)) {
                    continue;
                }
                if (terminalGroupId.equals(CbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID.toString())) {
                    isIncludeDefaultGroup = true;
                }
                boolean isExist = false;
                for (CbbTerminalGroupDetailDTO dto : terminalGroupDTOList) {
                    if (terminalGroupId.equals(dto.getId().toString())) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    LOGGER.error("illegal userGroupId [{}]", terminalGroupId);
                    throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_TERMINAL_GROUP_ID_ERROR);
                }
            }
            if (!isIncludeDefaultGroup) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_DEFAULT_TERMINAL_GROUP_ID_ERROR);
            }
        } else {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_CREATE_ADMIN_VALID_DEFAULT_TERMINAL_GROUP_ID_ERROR);
        }
    }


}
