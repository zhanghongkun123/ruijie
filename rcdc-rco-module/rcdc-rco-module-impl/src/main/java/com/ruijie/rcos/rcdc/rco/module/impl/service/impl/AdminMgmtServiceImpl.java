package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;


import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.CreateAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpgradeAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminMgmtService;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.create.CreateAdminContextKey;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.create.CreateAdminHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.AdminMgmtServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;


/**
 * Description: 已作废 查看最新实现 AdminMgmtServiceTxImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
@Deprecated
@Service
public class AdminMgmtServiceImpl implements AdminMgmtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminMgmtServiceImpl.class);

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private IacRoleMgmtAPI iacRoleMgmtAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private PermissionMgmtAPI permissionMgmtAPI;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private IacPermissionMgmtAPI iacPermissionMgmtAPI;

    @Autowired
    private AdminMgmtServiceTx adminMgmtServiceTx;

    @Override
    public void createAdmin(CreateAdminRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        stateMachineFactory.newBuilder(CreateAdminHandler.class)
                .initArg(CreateAdminContextKey.CREATE_ADMIN_REQUEST, request)
                .lockResources(request.getUserName())
                .start()
                .waitForAllProcessFinish();
    }

    @Override
    public void upgradeAdmin(UpgradeAdminRequest upgradeAdminRequest) throws BusinessException {
        Assert.notNull(upgradeAdminRequest, "upgradeAdminRequest is not null");

        // 5.4分级分权 关联管理员和用户，并且更新CBB组件维护的用户表的user_role角色维度为管理
        IacUserDetailDTO userDetail = userAPI.getUserDetail(upgradeAdminRequest.getUserId());
        // 升级管理员需要新增映射表的数据，并且创建随机符合AAA组件管理员密码的写入映射表中，
        CreateAdminRequest createAdminRequest = new CreateAdminRequest();
        // 拷贝 UpgradeAdminAdminRequest 到 createAdminRequest
        BeanUtils.copyProperties(upgradeAdminRequest, createAdminRequest);
        LOGGER.info("createAdminRequest:{}", JSON.toJSONString(createAdminRequest));
        // 如果用户非普通用户，首次不需要修改密码，其它都需要修改
        if (userDetail != null && (IacUserTypeEnum.AD == userDetail.getUserType() ||
                IacUserTypeEnum.LDAP == userDetail.getUserType() || IacUserTypeEnum.THIRD_PARTY == userDetail.getUserType())) {
            LOGGER.info("普通用户类型是AD或者LDAP域或第三方用户，不需要首次修改密码，管理员信息为:{}", JSON.toJSONString(upgradeAdminRequest));
            createAdminRequest.setHasFirstTimeLoggedIn(Boolean.FALSE);
            createAdminRequest.setUserType(userDetail.getUserType().toString());
        }
        // 创建管理员
        createAdmin(createAdminRequest);

        IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
        // 拷贝 userDetail 到 cbbUpdateUserDTO 后续如果出现update detail 不一致 需要后续添加代码兼容
        BeanUtils.copyProperties(userDetail, cbbUpdateUserDTO);
        // 设置管理员标识
        cbbUpdateUserDTO.setUserRole(UserRoleEnum.ADMIN.name());
        // 更新角色 由于TX事务原因 这个只能写在最后
        userAPI.updateUser(cbbUpdateUserDTO);


    }

    @Override
    public void updateAdminDataPermission(UpdateAdminDataPermissionRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        UUID adminId = request.getId();
        LockableExecutor.executeWithTryLock("update_admin_permission_prefix_" + adminId.toString(), () -> {
            adminMgmtServiceTx.updateAdminDataPermission(request);
        }, 5);
    }
}
