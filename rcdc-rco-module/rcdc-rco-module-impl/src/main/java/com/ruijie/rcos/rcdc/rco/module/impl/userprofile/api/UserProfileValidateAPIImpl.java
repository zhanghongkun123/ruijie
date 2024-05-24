package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileValidateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 路径的判断处理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/11
 *
 * @author zwf
 */
public class UserProfileValidateAPIImpl implements UserProfileValidateAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileValidateAPIImpl.class);

    @Autowired
    private UserProfilePathDAO userProfilePathDAO;

    @Autowired
    private UserProfileStrategyDAO userProfileStrategyDAO;

    @Autowired
    private UserProfilePathGroupDAO userProfilePathGroupDAO;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private UserProfileMainPathDAO userProfileMainPathDAO;

    @Autowired
    private UserProfileStrategyRelatedDAO userProfileStrategyRelatedDAO;

    private static final int MAX_PATH_NUM = 200;

    @Override
    public void validatePathNum(UUID[] idArr) throws BusinessException {
        Assert.notNull(idArr, "idArr must not be null");
        int count = userProfilePathDAO.countByUserProfilePathIdArr(idArr);
        if (count > MAX_PATH_NUM) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_PATH_NUM_OVER, String.valueOf(count),
                    String.valueOf(MAX_PATH_NUM));
        }
    }

    @Override
    public void validateGroupIdExist(UUID groupId) throws BusinessException {
        Assert.notNull(groupId, "groupId must not be null");

        UserProfilePathGroupEntity userProfilePathGroup = userProfilePathGroupDAO.getOne(groupId);
        if (userProfilePathGroup == null) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_GROUP_NOT_EXIST);
        }
    }

    @Override
    public void validateUserProfileChildPath(UserProfileChildPathDTO[] userProfileChildPathArr,
                                             @Nullable UUID userProfilePathId) throws BusinessException {
        Assert.notNull(userProfileChildPathArr, "userProfileChildPathArr must not be null");
        int total = 0;
        for (UserProfileChildPathDTO childPathDTO : userProfileChildPathArr) {
            total += childPathDTO.getPathArr().length;
        }

        if (total > MAX_PATH_NUM) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_NUM_OVER,
                    String.valueOf(total), String.valueOf(MAX_PATH_NUM));
        }

        if (userProfilePathId != null) {
            validateRelatedStrategy(userProfilePathId, total);
        }
    }

    private void validateRelatedStrategy(UUID userProfilePathId, int total) throws BusinessException {
        List<UUID> strategyIdList = userProfileStrategyRelatedDAO.findStrategyIdByRelatedId(userProfilePathId);

        for (UUID strategyId : strategyIdList) {
            List<UUID> userProfilePathIdList = userProfileStrategyRelatedDAO.findRelatedIdByStrategyIdExceptId(strategyId, userProfilePathId);
            if (!userProfilePathIdList.isEmpty()) {
                UUID[] idArr = userProfilePathIdList.toArray(new UUID[userProfilePathIdList.size()]);
                int count = userProfilePathDAO.countByUserProfilePathIdArr(idArr) + total;
                if (count > MAX_PATH_NUM) {
                    String userProfilePathName = userProfileMainPathDAO.findNameById(userProfilePathId);
                    String userProfileStrategyName = userProfileStrategyDAO.findNameById(strategyId);
                    LOGGER.error("编辑个性化配置[{}]导致策略[{}]关联路径为[{}],超出[{}]条，编辑失败", userProfilePathId, strategyId,
                            count, MAX_PATH_NUM);
                    throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_PATH_STRATEGY_NUM_OVER,
                            userProfilePathName, userProfileStrategyName, String.valueOf(count), String.valueOf(MAX_PATH_NUM));
                }
            }
        }
    }

    @Override
    public void validateUserProfileStrategyMustStoragePersonal(UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId must not be null");
        UserProfileStrategyEntity userProfileStrategy = getUserProfileStrategy(userProfileStrategyId);
        if (userProfileStrategy.getStorageType() != UserProfileStrategyStorageTypeEnum.PERSONAL) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_STRATEGY_STORAGE_MUST_PERSONAL,
                    userProfileStrategy.getName());
        }
    }

    @Override
    public void validateUserProfileStrategyMustStorageLocal(UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId must not be null");
        UserProfileStrategyEntity userProfileStrategy = getUserProfileStrategy(userProfileStrategyId);
        if (userProfileStrategy.getStorageType() != UserProfileStrategyStorageTypeEnum.LOCAL) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_STRATEGY_STORAGE_MUST_LOCAL,
                    userProfileStrategy.getName());
        }
    }

    @Override
    public void validateStorageMustFileServer(UUID userProfileStrategyId) throws BusinessException {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId must not be null");
        UserProfileStrategyEntity userProfileStrategy = getUserProfileStrategy(userProfileStrategyId);
        if (userProfileStrategy.getStorageType() != UserProfileStrategyStorageTypeEnum.FILE_SERVER) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_STRATEGY_STORAGE_MUST_FILE_SERVER,
                    userProfileStrategy.getName());
        }
    }

    @Override
    public void validateUserProfileStrategyImageRefuse(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId must be not null");

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        CbbOsType osType = imageTemplateDetail.getOsType();
        if (!CbbOsType.isWin7UpOS(osType) && !CbbOsType.isWinServerOs(osType)) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_IMAGE_CONFLICT);
        }
    }

    private UserProfileStrategyEntity getUserProfileStrategy(UUID userProfileStrategyId) throws BusinessException {
        UserProfileStrategyEntity userProfileStrategy = userProfileStrategyDAO.getOne(userProfileStrategyId);
        if (userProfileStrategy == null) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDA_NO_STRATEGY);
        }

        return userProfileStrategy;
    }

    @Override
    public boolean isPoolDesktopWithoutUser(UUID desktopId, DesktopPoolType desktopPoolType) {
        Assert.notNull(desktopId, "desktopId must not be null");
        Assert.notNull(desktopPoolType, "desktopPoolType must not be null");

        if (!DesktopPoolType.isPoolDesktop(desktopPoolType)) {
            return false;
        }

        UUID userId = userDesktopDAO.findUserIdByCbbDesktopId(desktopId);
        if (userId != null) {
            return false;
        }

        return true;
    }

    @Override
    public List<UUID> validateUserProfilePathExist(UUID[] userProfilePathIdArr) {
        Assert.notNull(userProfilePathIdArr, "userProfilePathIdArr must not be null");

        return userProfileMainPathDAO.getUserProfilePathIdByIdIn(userProfilePathIdArr);
    }
}
