package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.impl;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserChildOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserOperNotifyEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacSyncAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 15:07
 *
 * @author coderLee23
 */
@Service
public class UpdateUserOperSyncNotifyStrategy extends AbstractOperSyncNotifyStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUserOperSyncNotifyStrategy.class);


    private static final String UWS_NOTIFY_USER_UPDATE_PWD = "uws_notify_user_update_pwd";

    @Autowired
    private IacAdminMgmtAPI iacAdminMgmtAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Override
    public String getOper() {
        return UserOperNotifyEnum.UPDATE_USER.getOper();
    }

    @Override
    public void syncNotifyUserChange(UserOperSyncNotifyDTO cbbUserOperSyncNotifyRequest) throws BusinessException {
        Assert.notNull(cbbUserOperSyncNotifyRequest, "cbbUserOperSyncNotifyRequest must not be null");

        LOGGER.info("处理修改用户[{}]信息事件", cbbUserOperSyncNotifyRequest.getRelatedId());
        if (cbbUserOperSyncNotifyRequest.getRelatedId() != null
                && UserChildOperNotifyEnum.UPDATE_USER_PASSWORD.getOper().equals(cbbUserOperSyncNotifyRequest.getChildOper())) {
            // 统一通知用户密码修改，事务中不允许发送mq
            IacUserDetailDTO userDetail = userAPI.getUserDetail(cbbUserOperSyncNotifyRequest.getRelatedId());
            asyncNotifyUserUpdatePwd(userDetail);
        } else {
            // 处理用户与池的绑定关系
            updateUserRelatedPool(cbbUserOperSyncNotifyRequest.getRelatedId());
        }

        IacUserDetailDTO userDetailDTO = userAPI.getUserDetail(cbbUserOperSyncNotifyRequest.getRelatedId());
        try {
            updateThirdPartyUserIdentityConfig(userDetailDTO);
        } catch (Exception ex) {
            LOGGER.error("更新第三方用户身份信息发生异常，ex: ", ex);
        }

        try {
            syncPwdToAdmin(userDetailDTO, cbbUserOperSyncNotifyRequest.getChildOper());
        } catch (Exception e) {
            LOGGER.error("同步用户[{}]密码给管理员失败", userDetailDTO.getUserName(), e);
        }

        if (!UserChildOperNotifyEnum.UPDATE_USER_PASSWORD.getOper().equals(cbbUserOperSyncNotifyRequest.getChildOper())
                && Boolean.TRUE.equals(cbbUserOperSyncNotifyRequest.getEnableSyncData())) {
            dataSyncService.activeSyncUserData(cbbUserOperSyncNotifyRequest.getRelatedId());
        }
    }

    private void syncPwdToAdmin(IacUserDetailDTO userDetail, String childOperate) throws BusinessException {
        // 如果是普通用户修改密码，rco接口调用修改用户密码处已经有同步密码给管理员管理员密码了，这里不再需要处理。
        // 只有是外部用户，我们无法查询到密码，所以才通过用户表更新密码的事件来同步
        if (userDetail.getUserType() == IacUserTypeEnum.NORMAL || userDetail.getUserType() == IacUserTypeEnum.VISITOR) {
            LOGGER.info("用户[{}]类型为[{}]，无需同步密码给管理员", userDetail.getUserName(), userDetail.getUserType());
            return;
        }

        if (!UserChildOperNotifyEnum.UPDATE_USER_PASSWORD.getOper().equals(childOperate)) {
            LOGGER.info("用户[{}]操作不是修改密码，无需同步密码给管理员", userDetail.getUserName());
            return;
        }
        if (!UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole())) {
            LOGGER.info("用户[{}]不是管理员，无需同步密码给管理员", userDetail.getUserName());
            return;
        }
        LOGGER.info("同步用户[{}]密码给管理员", userDetail.getUserName());
        IacAdminDTO adminInfo = adminMgmtAPI.getAdminByUserName(userDetail.getUserName());
        IacSyncAdminPwdRequest syncRequest = new IacSyncAdminPwdRequest();
        syncRequest.setId(adminInfo.getId());
        syncRequest.setNewPwd(RedLineUtil.convertUserKey2AdminKey(userDetail.getPassword()));
        iacAdminMgmtAPI.syncAdminPwd(syncRequest);
    }


    private void asyncNotifyUserUpdatePwd(IacUserDetailDTO detail) {
        ThreadExecutors.execute(UWS_NOTIFY_USER_UPDATE_PWD, () -> {
            try {
                if (IacUserTypeEnum.AD != detail.getUserType() && IacUserTypeEnum.LDAP != detail.getUserType()) {
                    LOGGER.info("通知修改用户密码成功，用户id：{}", detail.getId());
                    uwsDockingAPI.notifyUserUpdatePwd(Collections.singletonList(detail.getId()));
                    rccmManageService.notifyUserUpdatePwd(detail);
                } else {
                    LOGGER.info("用户类型为AD/LDAP，不发送密码变更通知，用户id：{}", detail.getId());
                }
            } catch (Exception ex) {
                LOGGER.error("查询用户信息发生异常，用户id [{}]", detail.getId().toString(), ex);
                return;
            }
            userNotifyAPI.notifyUserPwdToGt(detail.getId());
        });
    }

    private void updateUserRelatedPool(UUID userId) {
        // 用户绑定着个人盘
        userDiskMgmtAPI.diskDetailByUserId(userId).ifPresent(userDiskDetailDTO -> {
            UUID diskPoolId = userDiskDetailDTO.getDiskPoolId();
            UUID groupId = userDiskDetailDTO.getGroupId();
            // 用户组跟该磁盘池绑定关系存在
            if (Objects.nonNull(diskPoolUserDAO.findByDiskPoolIdAndRelatedId(diskPoolId, groupId))) {
                diskPoolUserDAO.deleteByDiskPoolIdAndRelatedTypeAndRelatedIdIn(diskPoolId, IacConfigRelatedType.USER,
                        Collections.singletonList(userId));
                return;
            }
            if (Objects.isNull(diskPoolUserDAO.findByDiskPoolIdAndRelatedId(diskPoolId, userId))) {
                // 用户/用户组跟该磁盘池绑定关系已删除，将用户与池绑定关系
                diskPoolUserAPI.saveDiskPoolRelatedUser(diskPoolId, userId);
            }
        });

        desktopPoolUserMgmtAPI.dealRelationAfterUpdateUser(userId);
    }

    private void updateThirdPartyUserIdentityConfig(IacUserDetailDTO userDetailDTO) throws BusinessException {
        if (userDetailDTO.getUserType() != IacUserTypeEnum.THIRD_PARTY) {
            return;
        }

        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(
                IacConfigRelatedType.USER, userDetailDTO.getId());
        // 仅处理普通用户转为第三方用户时，主要认证信息未同步变更的问题，其他属性保持不变
        userIdentityConfigRequest.setOpenThirdPartyCertification(Boolean.TRUE);
        userIdentityConfigRequest.setOpenAccountPasswordCertification(Boolean.FALSE);
        userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);
    }
}
