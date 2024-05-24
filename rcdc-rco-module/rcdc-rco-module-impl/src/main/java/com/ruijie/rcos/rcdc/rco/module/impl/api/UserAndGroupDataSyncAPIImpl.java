package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacLdapMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyAuthPlatformConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.util.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserAndGroupDataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DataSyncResult;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.DataSyncBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.constant.DataSyncConstant;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.enums.UserGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.init.UserGroupSyncDataStrategyFactory;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.init.UserSyncDataStrategyFactory;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.group.UserGroupSyncDataStrategy;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.strategy.user.UserSyncDataStrategy;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserSyncDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserGroupSyncDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserSyncDataService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/20 11:13
 *
 * @author coderLee23
 */
public class UserAndGroupDataSyncAPIImpl implements UserAndGroupDataSyncAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAndGroupDataSyncAPIImpl.class);

    @Autowired
    private ViewUserGroupSyncDataService viewUserGroupSyncDataService;

    @Autowired
    private ViewUserSyncDataService viewUserSyncDataService;

    @Autowired
    private UserGroupSyncDataStrategyFactory userGroupSyncDataStrategyFactory;

    @Autowired
    private UserSyncDataStrategyFactory userSyncDataStrategyFactory;

    @Autowired
    private DataSyncLogService dataSyncLogService;

    @Autowired
    private IacThirdPartyUserAPI cbbThirdPartyUserAPI;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private IacLdapMgmtAPI cbbLdapMgmtAPI;



    private static final ThreadExecutor USER_THREAD_EXECUTOR = ThreadExecutors.newBuilder(DataSyncConstant.BATCH_SYNC_USER_DATA_THREAD)
            .maxThreadNum(DataSyncConstant.MAX_THREAD_NUM).queueSize(DataSyncConstant.QUEUE_SIZE).build();

    private static final ThreadExecutor USER_GROUP_THREAD_EXECUTOR = ThreadExecutors.newBuilder(DataSyncConstant.BATCH_SYNC_USER_GROUP_DATA_THREAD)
            .maxThreadNum(DataSyncConstant.MAX_THREAD_NUM).queueSize(DataSyncConstant.QUEUE_SIZE).build();



    @Override
    public List<UserGroupSyncDataDTO> listUserGroupSyncData() {
        List<ViewUserGroupSyncDataEntity> viewUserGroupSyncDataList = viewUserGroupSyncDataService.findAll();

        return viewUserGroupSyncDataList.stream().map(entity -> {
            UserGroupSyncDataDTO userGroupSyncDataDTO = new UserGroupSyncDataDTO();
            BeanUtils.copyProperties(entity, userGroupSyncDataDTO);
            return userGroupSyncDataDTO;
        }).collect(Collectors.toList());

    }

    @Override
    public Page<UserSyncDataDTO> pageUserSyncData(List<IacUserTypeEnum> userTypeList, Pageable pageable) {
        Assert.notEmpty(userTypeList, "userTypeList must not be null or empty");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUserSyncDataEntity> viewUserSyncDataPage = viewUserSyncDataService.pageUserSyncData(userTypeList, pageable);

        return viewUserSyncDataPage.map(entity -> {
            UserSyncDataDTO userSyncDataDTO = new UserSyncDataDTO();
            BeanUtils.copyProperties(entity, userSyncDataDTO);
            // 当前版本加密KEY变化，从数据查询出来数据需要做加密KEY值变化处理
            String decryptPassword = AesUtil.descrypt(entity.getPassword(), RedLineUtil.getRealInnerUserRedLine());
            userSyncDataDTO.setPassword(AesUtil.encrypt(decryptPassword, RedLineUtil.getRealUserRedLine()));
            return userSyncDataDTO;
        });
    }

    @Override
    public DataSyncResult syncUserGroupData(List<JSONObject> userGroupSyncDataList) {
        Assert.notEmpty(userGroupSyncDataList, "userGroupSyncDataList must not be null");
        DataSyncResult dataSyncResult = new DataSyncResult();
        AtomicInteger successNum = new AtomicInteger(0);
        AtomicInteger failureNum = new AtomicInteger(0);
        // 深度分组
        Map<Integer, List<UserGroupSyncDataDTO>> userGroupDepthMap =
                userGroupSyncDataList.stream().map(object -> object.toJavaObject(UserGroupSyncDataDTO.class))
                        .collect(Collectors.groupingBy(UserGroupSyncDataDTO::getDepth, LinkedHashMap::new, Collectors.toList()));
        // 深度排序
        List<Map.Entry<Integer, List<UserGroupSyncDataDTO>>> sortedKeyList  = userGroupDepthMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("用户组排序后的数据：{}",JSON.toJSONString(sortedKeyList));
        }
        for (Map.Entry<Integer, List<UserGroupSyncDataDTO>> userGroupListEntry : sortedKeyList) {
            // 根据深度顺序创建用户组
            List<UserGroupSyncDataDTO> userGroupList = userGroupListEntry.getValue();
            CountDownLatch countDownLatch = new CountDownLatch(userGroupList.size());
            for (UserGroupSyncDataDTO userGroupSyncDataDTO : userGroupList) {
                USER_GROUP_THREAD_EXECUTOR.submit(() -> {
                    UserGroupTypeEnum userGroupType = getUserGroupType(userGroupSyncDataDTO);
                    try {
                        // 检测是否允许同步
                        checkUserGroupSyncDataIsAllow(userGroupType);

                        UserGroupSyncDataStrategy userGroupSyncDataStrategy =
                                userGroupSyncDataStrategyFactory.getUserGroupSyncDataStrategy(userGroupType);
                        userGroupSyncDataStrategy.syncData(userGroupSyncDataDTO);
                        successNum.incrementAndGet();
                    } catch (Exception e) {
                        LOGGER.error("更新数据" + JSON.toJSONString(userGroupSyncDataDTO) + "异常！", e);
                        String message = e.getMessage();
                        if (e instanceof BusinessException) {
                            BusinessException ex = (BusinessException) e;
                            message = ex.getI18nMessage();
                        }

                        String context = LocaleI18nResolver.resolve(DataSyncBusinessKey.RCDC_RCO_SYNC_USER_GROUP_DATA_FAIL,
                                userGroupSyncDataDTO.getName(), message);
                        dataSyncLogService.saveDataSyncLog(context);
                        failureNum.incrementAndGet();
                    } finally {
                        countDownLatch.countDown();
                    }
                });
            }

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                LOGGER.error("等待处理异常", e);
                Thread.currentThread().interrupt();
            }
        }

        dataSyncResult.setSuccessNum(successNum.get());
        dataSyncResult.setFailureNum(failureNum.get());

        return dataSyncResult;
    }

    @Override
    public DataSyncResult syncUserData(List<JSONObject> userSyncDataJsonList) {
        Assert.notNull(userSyncDataJsonList, "userSyncDataJsonList must not be null");
        DataSyncResult dataSyncResult = new DataSyncResult();
        AtomicInteger successNum = new AtomicInteger(0);
        AtomicInteger failureNum = new AtomicInteger(0);

        List<UserSyncDataDTO> userSyncDataList =
                userSyncDataJsonList.stream().map(data -> data.toJavaObject(UserSyncDataDTO.class)).collect(Collectors.toList());

        CountDownLatch countDownLatch = new CountDownLatch(userSyncDataList.size());
        for (UserSyncDataDTO userSyncDataDTO : userSyncDataList) {
            IacUserTypeEnum userType = userSyncDataDTO.getUserType();
            USER_THREAD_EXECUTOR.submit(() -> {
                try {
                    // 检测用户是否允许同步
                    checkUserSyncDataIsAllow(userType);

                    UserSyncDataStrategy userSyncDataStrategy = userSyncDataStrategyFactory.getUserSyncDataStrategy(userType);
                    userSyncDataStrategy.syncData(userSyncDataDTO);
                    successNum.incrementAndGet();
                } catch (Exception e) {
                    LOGGER.error("更新数据" + JSON.toJSONString(userSyncDataDTO) + "异常！", e);
                    String message = e.getMessage();
                    if (e instanceof BusinessException) {
                        BusinessException ex = (BusinessException) e;
                        message = ex.getI18nMessage();
                    }

                    String context = LocaleI18nResolver.resolve(DataSyncBusinessKey.RCDC_RCO_SYNC_USER_DATA_FAIL, userSyncDataDTO.getName(), message);
                    dataSyncLogService.saveDataSyncLog(context);
                    failureNum.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error("等待处理异常", e);
            Thread.currentThread().interrupt();
        }

        dataSyncResult.setSuccessNum(successNum.get());
        dataSyncResult.setFailureNum(failureNum.get());

        return dataSyncResult;
    }

    @Override
    public DataSyncResult activeSyncUserData(JSONObject userData) {
        Assert.notNull(userData, "userData must not be null");
        UserSyncDataDTO userSyncData = userData.toJavaObject(UserSyncDataDTO.class);

        DataSyncResult dataSyncResult = new DataSyncResult();
        IacUserTypeEnum userType = userSyncData.getUserType();
        try {
            checkUserSyncDataIsAllow(userType);

            UserSyncDataStrategy userSyncDataStrategy = userSyncDataStrategyFactory.getUserSyncDataStrategy(userType);
            userSyncDataStrategy.syncData(userSyncData);
            dataSyncResult.setSuccessNum(1);
        } catch (Exception e) {
            LOGGER.error("更新数据" + JSON.toJSONString(userSyncData) + "异常！", e);
            String message = e.getMessage();
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                message = ex.getI18nMessage();
            }
            String context = LocaleI18nResolver.resolve(DataSyncBusinessKey.RCDC_RCO_SYNC_USER_DATA_FAIL, userSyncData.getName(), message);
            dataSyncLogService.saveDataSyncLog(context);
            dataSyncResult.setFailureNum(1);
        }

        return dataSyncResult;
    }

    @Override
    public DataSyncResult activeSyncUserGroupData(JSONObject userGroupData) {
        Assert.notNull(userGroupData, "userGroupData must not be null");
        DataSyncResult dataSyncResult = new DataSyncResult();

        UserGroupSyncDataDTO userGroupSyncData = userGroupData.toJavaObject(UserGroupSyncDataDTO.class);

        UserGroupTypeEnum userGroupType = getUserGroupType(userGroupSyncData);
        try {
            // 检测是否允许同步
            checkUserGroupSyncDataIsAllow(userGroupType);

            UserGroupSyncDataStrategy userGroupSyncDataStrategy = userGroupSyncDataStrategyFactory.getUserGroupSyncDataStrategy(userGroupType);
            userGroupSyncDataStrategy.syncData(userGroupSyncData);
            dataSyncResult.setSuccessNum(1);
        } catch (Exception e) {
            LOGGER.error("更新数据" + JSON.toJSONString(userGroupSyncData) + "异常！", e);
            String message = e.getMessage();
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                message = ex.getI18nMessage();
            }

            String context = LocaleI18nResolver.resolve(DataSyncBusinessKey.RCDC_RCO_SYNC_USER_GROUP_DATA_FAIL, userGroupSyncData.getName(), message);
            dataSyncLogService.saveDataSyncLog(context);
            dataSyncResult.setFailureNum(1);
        }

        return dataSyncResult;
    }

    private UserGroupTypeEnum getUserGroupType(UserGroupSyncDataDTO userGroupSyncDataDTO) {
        if (Boolean.TRUE.equals(userGroupSyncDataDTO.getAdGroup())) {
            return UserGroupTypeEnum.AD;
        }

        if (Boolean.TRUE.equals(userGroupSyncDataDTO.getLdapGroup())) {
            return UserGroupTypeEnum.LDAP;
        }

        if (Boolean.TRUE.equals(userGroupSyncDataDTO.getThirdPartyGroup())) {
            return UserGroupTypeEnum.THIRD_PARTY;
        }

        return UserGroupTypeEnum.NORMAL;
    }

    private void checkUserGroupSyncDataIsAllow(UserGroupTypeEnum userGroupType) throws BusinessException {
        switch (userGroupType) {
            case AD:
                IacDomainConfigDetailDTO adConfigDetailDTO = cbbAdMgmtAPI.getAdConfig();
                if (Boolean.FALSE.equals(adConfigDetailDTO.getEnable())) {
                    // 未开启AD域
                    throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_SYNC_DATA_AD_NOT_ENABLED);
                }
                break;
            case LDAP:
                IacDomainConfigDetailDTO ldapConfigDetailDTO = cbbLdapMgmtAPI.getLdapConfig();
                if (Boolean.FALSE.equals(ldapConfigDetailDTO.getEnable())) {
                    // 未开启LDAP域
                    throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_SYNC_DATA_LDAP_NOT_ENABLED);
                }
                break;
            case THIRD_PARTY:
                BaseThirdPartyAuthPlatformConfigDTO thirdPartyConfig = cbbThirdPartyUserAPI.getThirdPartyConfig();
                if (Objects.isNull(thirdPartyConfig) || Boolean.FALSE.equals(thirdPartyConfig.getThirdPartyEnable())) {
                    // 未开启第三方认证
                    throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_SYNC_DATA_THIRD_PARTY_NOT_ENABLED);
                }
                break;
            case NORMAL:
            default:
                break;
        }
    }

    private void checkUserSyncDataIsAllow(IacUserTypeEnum cbbUserType) throws BusinessException {
        switch (cbbUserType) {
            case AD:
                IacDomainConfigDetailDTO adConfigDetailDTO = cbbAdMgmtAPI.getAdConfig();
                if (Boolean.FALSE.equals(adConfigDetailDTO.getEnable())) {
                    // 未开启AD域
                    throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_SYNC_DATA_AD_NOT_ENABLED);
                }
                break;
            case LDAP:
                IacDomainConfigDetailDTO ldapConfigDetailDTO = cbbLdapMgmtAPI.getLdapConfig();
                if (Boolean.FALSE.equals(ldapConfigDetailDTO.getEnable())) {
                    // 未开启LDAP域
                    throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_SYNC_DATA_LDAP_NOT_ENABLED);
                }
                break;
            case THIRD_PARTY:
                BaseThirdPartyAuthPlatformConfigDTO thirdPartyConfig = cbbThirdPartyUserAPI.getThirdPartyConfig();
                if (Objects.isNull(thirdPartyConfig) || Boolean.FALSE.equals(thirdPartyConfig.getThirdPartyEnable())) {
                    // 未开启第三方认证
                    throw new BusinessException(DataSyncBusinessKey.RCDC_RCO_SYNC_DATA_THIRD_PARTY_NOT_ENABLED);
                }
                break;
            case NORMAL:
            default:
                break;
        }
    }

}
