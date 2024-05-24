package com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.util.RedLineUtil;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.RccmUnifiedManageRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RccmClusterUnifiedManageStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.DataSyncBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserGroupSyncDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserSyncDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/12 20:03
 *
 * @author coderLee23
 */
@Service
public class DataSyncServiceImpl implements DataSyncService {


    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncServiceImpl.class);

    @Autowired
    protected DataSyncLogService dataSyncLogService;

    @Autowired
    protected ViewUserGroupSyncDataService viewUserGroupSyncDataService;

    @Autowired
    protected ViewUserSyncDataService viewUserSyncDataService;

    @Autowired
    protected RccmManageService rccmManageService;

    @Override
    public void activeSyncUserData(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        List<RccmClusterUnifiedManageStrategyDTO> clusterUnifiedManageStrategyList = rccmManageService.getClusterUnifiedManageStrategy();
        if (CollectionUtils.isEmpty(clusterUnifiedManageStrategyList)) {
            LOGGER.warn("集群同步策略不存在，无需同步");
            return;
        }

        RccmClusterUnifiedManageStrategyDTO cluster = clusterUnifiedManageStrategyList.get(0);
        if (cluster.getRole() != RccmUnifiedManageRoleEnum.MASTER || Boolean.FALSE.equals(cluster.getEnableSyncUser())) {
            LOGGER.warn("非主集群或未开启用户/用户组同步，无需同步");
            return;
        }

        Optional<ViewUserSyncDataEntity> userSyncDataEntityOptional = viewUserSyncDataService.findById(userId);
        if (!userSyncDataEntityOptional.isPresent()) {
            LOGGER.warn("用户数据已不存在，无需同步");
            return;
        }
        // 转发到从集群保存或更新用户组数据
        ViewUserSyncDataEntity viewUserSyncDataEntity = userSyncDataEntityOptional.get();
        if (viewUserSyncDataEntity.getUserType() == IacUserTypeEnum.VISITOR) {
            LOGGER.warn("访客用户 {} 无需同步，无需同步", viewUserSyncDataEntity.getName());
            return;
        }

        try {
            UserSyncDataDTO userSyncDataDTO = new UserSyncDataDTO();
            BeanUtils.copyProperties(viewUserSyncDataEntity, userSyncDataDTO);
            // 当前版本加密KEY变化，从数据查询出来数据需要做加密KEY值变化处理
            String decryptPassword = AesUtil.descrypt(userSyncDataDTO.getPassword(), RedLineUtil.getRealInnerUserRedLine());
            userSyncDataDTO.setPassword(AesUtil.encrypt(decryptPassword, RedLineUtil.getRealUserRedLine()));

            rccmManageService.syncUser(userSyncDataDTO);
        } catch (Exception e) {
            LOGGER.error("同步数据失败！", e);
            String message = e.getMessage();
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                message = ex.getI18nMessage();
            }

            String userContent =
                    LocaleI18nResolver.resolve(DataSyncBusinessKey.RCDC_RCO_SYNC_USER_DATA_FAIL, viewUserSyncDataEntity.getName(), message);
            dataSyncLogService.saveDataSyncLog(userContent);
        }
    }



    @Override
    public void activeSyncUserGroupData(UUID userGroupId) {
        Assert.notNull(userGroupId, "userGroupId must not be null");
        List<RccmClusterUnifiedManageStrategyDTO> clusterUnifiedManageStrategyList = rccmManageService.getClusterUnifiedManageStrategy();
        if (CollectionUtils.isEmpty(clusterUnifiedManageStrategyList)) {
            LOGGER.warn("集群同步策略不存在，无需同步");
            return;
        }

        RccmClusterUnifiedManageStrategyDTO cluster = clusterUnifiedManageStrategyList.get(0);
        if (cluster.getRole() != RccmUnifiedManageRoleEnum.MASTER || Boolean.FALSE.equals(cluster.getEnableSyncUser())) {
            LOGGER.warn("非主集群或未开启用户/用户组同步，无需同步");
            return;
        }

        Optional<ViewUserGroupSyncDataEntity> userGroupSyncDataEntityOptional = viewUserGroupSyncDataService.findById(userGroupId);
        if (!userGroupSyncDataEntityOptional.isPresent()) {
            LOGGER.warn("用户组数据已不存在，无需同步");
            return;
        }

        // 转发到从集群保存或更新用户组数据
        ViewUserGroupSyncDataEntity viewUserGroupSyncDataEntity = userGroupSyncDataEntityOptional.get();
        try {
            UserGroupSyncDataDTO userGroupSyncDataDTO = new UserGroupSyncDataDTO();
            BeanUtils.copyProperties(viewUserGroupSyncDataEntity, userGroupSyncDataDTO);

            rccmManageService.syncUserGroup(userGroupSyncDataDTO);
        } catch (Exception e) {
            LOGGER.error("同步数据失败！", e);
            String message = e.getMessage();
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                message = ex.getI18nMessage();
            }

            String userGroupContent = LocaleI18nResolver.resolve(DataSyncBusinessKey.RCDC_RCO_SYNC_USER_GROUP_DATA_FAIL,
                    viewUserGroupSyncDataEntity.getName(), message);
            dataSyncLogService.saveDataSyncLog(userGroupContent);
        }
    }
}
