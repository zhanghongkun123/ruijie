package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUpdateBatchUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQueryUserListPageDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserIdentityConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.UserIdentityConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.UserOrGroupBatchStateCache;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;

/**
 * Description: 批量应用用户或用户组认证策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/22
 *
 * @author TD
 */
public class UserCertificationBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserCertificationBatchTaskHandler.class);

    private static final ThreadExecutor CUSTOM_THREAD_EXECUTOR =
            ThreadExecutors.newBuilder("UserCertificationBatchTaskHandler").maxThreadNum(5).queueSize(50000).build();

    private BaseAuditLogAPI auditLogAPI;

    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    private UserIdentityConfigDTO userIdentityConfigDTO;

    private IacUserGroupMgmtAPI userGroupAPI;

    private IacUserMgmtAPI userAPI;

    private UserInfoAPI userInfoAPI;

    private DataSyncAPI dataSyncAPI;

    public UserCertificationBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
                                             IacUserGroupMgmtAPI userGroupAPI, IacUserMgmtAPI userAPI, UserInfoAPI userInfoAPI) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.userGroupAPI = userGroupAPI;
        this.userAPI = userAPI;
        this.userInfoAPI = userInfoAPI;
    }

    /**
     * 设置认证策略操作API
     * 
     * @param userIdentityConfigAPI 认证策略操作API
     * @return UserCertificationBatchTaskHandler
     */
    public UserCertificationBatchTaskHandler setUserIdentityConfigAPI(IacUserIdentityConfigMgmtAPI userIdentityConfigAPI) {
        this.userIdentityConfigAPI = userIdentityConfigAPI;
        return this;
    }

    /**
     * 设置认证策略参数
     * 
     * @param userIdentityConfigDTO 认证策略参数
     * @return UserCertificationBatchTaskHandler
     */
    public UserCertificationBatchTaskHandler setUserIdentityConfigDTO(UserIdentityConfigDTO userIdentityConfigDTO) {
        this.userIdentityConfigDTO = userIdentityConfigDTO;
        return this;
    }

    /**
     * 同步集群
     *
     * @param dataSyncAPI 同步集群
     * @return UserCertificationBatchTaskHandler
     */
    public UserCertificationBatchTaskHandler setDataSyncAPI(DataSyncAPI dataSyncAPI) {
        this.dataSyncAPI = dataSyncAPI;
        return this;
    }



    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 标记批量任务完成
        UserOrGroupBatchStateCache.STATE.removeSyncTask(UserOrGroupBatchStateCache.USER_OR_GROUP_KEY);
        return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_EDIT_USER_OR_GROUP_IDENTITY_CERTIFICATION_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        UpdateUserCertificationBatchTaskItem certificationBatchTaskItem = (UpdateUserCertificationBatchTaskItem) batchTaskItem;
        UUID itemId = certificationBatchTaskItem.getItemID();
        // 用户级别
        if (certificationBatchTaskItem.getRelatedType() == IacConfigRelatedType.USER) {
            return doExecute(certificationBatchTaskItem);
        }
        // 用户组级别
        UUID[] filterUserIdArr = certificationBatchTaskItem.getFilterUserIdArr();
        if (filterUserIdArr == null) {
            return doExecute(certificationBatchTaskItem);
        }
        String objectName = null;
        try {
            // 用户组下用户级别
            objectName = userGroupAPI.getUserGroupDetail(itemId).getName();
            List<UUID> userIdList = queryUserListByGroupId(itemId, filterUserIdArr);
            List<UUID> normalUserIdList = filterUserListByType(itemId, Lists.newArrayList(IacUserTypeEnum.NORMAL));
            List<UUID> thirdPartyUserIdList = filterUserListByType(itemId, Lists.newArrayList(IacUserTypeEnum.THIRD_PARTY));
            List<UUID> adUserList = filterUserListByType(itemId, Lists.newArrayList(IacUserTypeEnum.AD, IacUserTypeEnum.LDAP));
            if (!CollectionUtils.isEmpty(normalUserIdList)) {
                userInfoAPI.updateBatchUserInvalidTime(true, normalUserIdList, userIdentityConfigDTO.getInvalidTime(),
                        userIdentityConfigDTO.getAccountExpireDate());
            }
            if (!CollectionUtils.isEmpty(adUserList)) {
                userInfoAPI.updateBatchUserInvalidTime(false, adUserList, userIdentityConfigDTO.getInvalidTime(), null);
            }
            if (!CollectionUtils.isEmpty(thirdPartyUserIdList)) {
                userInfoAPI.updateBatchUserInvalidTime(false, thirdPartyUserIdList, userIdentityConfigDTO.getInvalidTime(), null);
            }

            // 第三方用户和其他用户需要分开处理
            AtomicInteger successNum = new AtomicInteger();
            List<UUID> withOutThirdPartyUserIdList = new ArrayList<>(userIdList);
            withOutThirdPartyUserIdList.removeAll(thirdPartyUserIdList);
            int pageSize = 900;
            int exceed = withOutThirdPartyUserIdList.size() % pageSize > 0 ? 1 : 0;
            long totalPages = withOutThirdPartyUserIdList.size() / pageSize + exceed;
            int thirdPartyExceed = thirdPartyUserIdList.size() % pageSize > 0 ? 1 : 0;
            long thirdPartyTotalPages = thirdPartyUserIdList.size() / pageSize + thirdPartyExceed;
            // 分批推送，计算出总推送次数
            CountDownLatch countDownLatch = new CountDownLatch((int) totalPages + (int)thirdPartyTotalPages);
            // 更新非第三方用户
            batchUpdateIdentityConfig(objectName, withOutThirdPartyUserIdList, successNum,
                    countDownLatch, userIdentityConfigDTO);

            // 更新第三方用户，不可修改主要认证类型
            final UserIdentityConfigDTO thirdPartyUserIdConfig = new UserIdentityConfigDTO();
            BeanUtils.copyProperties(userIdentityConfigDTO, thirdPartyUserIdConfig);
            thirdPartyUserIdConfig.setOpenAccountPasswordCertification(false);
            thirdPartyUserIdConfig.setOpenThirdPartyCertification(true);
            batchUpdateIdentityConfig(objectName, thirdPartyUserIdList, successNum,
                    countDownLatch, thirdPartyUserIdConfig);

            this.await(countDownLatch, objectName);
            auditLogAPI.recordLog(certificationBatchTaskItem.getSuccessKey(), objectName, String.valueOf(successNum.get()),
                    String.valueOf(userIdList.size() - successNum.get()));
            return DefaultBatchTaskItemResult.builder()
                    .batchTaskItemStatus(successNum.get() == userIdList.size() ? BatchTaskItemStatus.SUCCESS : BatchTaskItemStatus.FAILURE)
                    .msgKey(certificationBatchTaskItem.getSuccessKey())
                    .msgArgs(objectName, String.valueOf(successNum.get()), String.valueOf(userIdList.size() - successNum.get())).build();
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            LOGGER.error("分页更新用户组：" + itemId + "下用户，失败认证策略", e);
            String errorName = StringUtils.isEmpty(objectName) ? String.valueOf(itemId) : objectName;
            auditLogAPI.recordLog(certificationBatchTaskItem.getFailKey(), errorName, exceptionMsg);
            throw new BusinessException(certificationBatchTaskItem.getFailKey(), e, errorName, exceptionMsg);
        }
    }

    private void batchUpdateIdentityConfig(String objectName, List<UUID> userIdList,
                                           AtomicInteger successNum, CountDownLatch countDownLatch,
                                           UserIdentityConfigDTO userIdentityConfigDTO) {
        final int finalPageSize = 900;
        int exceed = userIdList.size() % finalPageSize > 0 ? 1 : 0;
        final long totalPages = userIdList.size() / finalPageSize + exceed;
        final IacUserIdentityConfigDTO finalUserIdentityConfigDTO = new IacUserIdentityConfigDTO();
        BeanUtils.copyProperties(userIdentityConfigDTO, finalUserIdentityConfigDTO);
        String finalObjectName = objectName;
        for (int currentPage = 1; currentPage <= totalPages; currentPage++) {
            int finalCurrentPage = currentPage;
            CUSTOM_THREAD_EXECUTOR.execute(() -> {
                // 分页用户列表 一次1000条
                List<UUID> usernameList = userIdList.stream().skip((finalCurrentPage - 1) * finalPageSize).
                        limit(finalPageSize).collect(Collectors.toList());
                // 推送用户数据
                try {
                    IacUpdateBatchUserIdentityConfigRequest iacUpdateBatchUserIdentityConfigRequest = new IacUpdateBatchUserIdentityConfigRequest();
                    iacUpdateBatchUserIdentityConfigRequest.setDto(finalUserIdentityConfigDTO);
                    iacUpdateBatchUserIdentityConfigRequest.setRelatedList(usernameList);
                    successNum.addAndGet(userIdentityConfigAPI.updateBatchUserIdentityConfig(iacUpdateBatchUserIdentityConfigRequest));

                    for (UUID userId : usernameList) {
                        // 批量同步用户
                        dataSyncAPI.activeSyncUserData(userId);
                    }
                } catch (Exception e) {
                    LOGGER.error("更新用户组：" + finalObjectName + "下用户认证策略，失败：" + usernameList.size(), e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
    }

    private BatchTaskItemResult doExecute(UpdateUserCertificationBatchTaskItem certificationBatchTaskItem) throws BusinessException {
        UUID itemID = certificationBatchTaskItem.getItemID();
        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest();
        BeanUtils.copyProperties(userIdentityConfigDTO, userIdentityConfigRequest);
        userIdentityConfigRequest.setRelatedId(itemID);
        userIdentityConfigRequest.setRelatedType(certificationBatchTaskItem.getRelatedType());
        String objectName = null;
        try {
            if (certificationBatchTaskItem.getRelatedType() == IacConfigRelatedType.USER) {
                objectName = batchUpdateUser(itemID, userAPI);
                IacUserDetailDTO userDetailDTO = userAPI.getUserDetail(itemID);
                if (userDetailDTO.getUserType() == IacUserTypeEnum.THIRD_PARTY) {
                    userIdentityConfigRequest.setOpenAccountPasswordCertification(false);
                    userIdentityConfigRequest.setOpenThirdPartyCertification(true);
                }
            } else {
                IacUserGroupDetailDTO userGroupDetail = userGroupAPI.getUserGroupDetail(itemID);
                if (ObjectUtils.isNotEmpty(userGroupDetail)) {
                    objectName = userGroupDetail.getName();
                    if (userGroupDetail.isThirdPartyGroup()) {
                        userIdentityConfigRequest.setOpenAccountPasswordCertification(false);
                        userIdentityConfigRequest.setOpenThirdPartyCertification(true);
                    }
                }
                //更新用户组信息
                batchUpdateUserGroup(itemID, userGroupAPI);
            }
            LOGGER.info("编辑用户或用户组:[{}]身份认证配置：{}", objectName, itemID, JSON.toJSONString(userIdentityConfigRequest));
            userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);

            if (certificationBatchTaskItem.getRelatedType() == IacConfigRelatedType.USER) {
                dataSyncAPI.activeSyncUserData(itemID);
            } else {
                dataSyncAPI.activeSyncUserGroupData(itemID);
            }

            auditLogAPI.recordLog(certificationBatchTaskItem.getSuccessKey(), objectName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(certificationBatchTaskItem.getSuccessKey()).msgArgs(objectName).build();
        } catch (BusinessException e) {
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            LOGGER.error("编辑用户或用户组认证策略失败, ID：" + itemID, e);
            String errorName = StringUtils.isEmpty(objectName) ? String.valueOf(itemID) : objectName;
            auditLogAPI.recordLog(certificationBatchTaskItem.getFailKey(), errorName, exceptionMsg);
            throw new BusinessException(certificationBatchTaskItem.getFailKey(), e, errorName, exceptionMsg);
        }
    }

    private List<UUID> queryUserListByGroupId(UUID groupId, UUID[] filterUserIdArr) throws BusinessException {
        List<UUID> filterUserList = Arrays.asList(filterUserIdArr);
        List<UUID> userIdList = new ArrayList<>();
        // 组下用户可能会超过1000,接口限制一次最多返回1000条数据
        IacPageResponse<IacUserDetailDTO> pageResult = pageQueryByGroupId(groupId, 0);
        // 总页数
        int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
        for (int page = 0; page < totalPage; page++) {
            // 前面已查过，不重复查
            if (page == 0) {
                userIdList.addAll(filterUserIdList(pageResult.getItemArr(), filterUserList));
                continue;
            }
            pageResult = pageQueryByGroupId(groupId, page);
            if (pageResult.getTotal() == 0) {
                break;
            }
            userIdList.addAll(filterUserIdList(pageResult.getItemArr(), filterUserList));
        }
        return userIdList;
    }
    
    private List<UUID> filterUserIdList(IacUserDetailDTO[] userDetailDTOArr, List<UUID> filterUserList) {
        return Arrays.stream(userDetailDTOArr).filter(cbbUserDetailDTO -> cbbUserDetailDTO.getUserType() != IacUserTypeEnum.VISITOR)
                .filter(cbbUserDetailDTO -> !filterUserList.contains(cbbUserDetailDTO.getId())).map(IacUserDetailDTO::getId)
                .collect(Collectors.toList());
    }

    private void await(CountDownLatch countDownLatch, String objectName) {
        try {
            LOGGER.info("等待更新用户组：" + objectName + "下用户认证策略，子线程执行结束");
            countDownLatch.await(30, TimeUnit.MINUTES);
            LOGGER.info("结束用户组：" + objectName + "下用户认证策略，子线程执行结束，退出await");
        } catch (InterruptedException e) {
            LOGGER.error("锁定用户组：" + objectName + "下用户，线程等待异常，尝试结束当前线程");
            Thread.currentThread().interrupt();
        }
    }

    private String batchUpdateUser(UUID itemId, IacUserMgmtAPI userAPI) throws BusinessException {
        IacUserDetailDTO userDetailDTO = userAPI.getUserDetail(itemId);
        String objectName = userDetailDTO.getUserName();
        IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
        BeanUtils.copyProperties(userDetailDTO, cbbUpdateUserDTO);
        cbbUpdateUserDTO.setId(userDetailDTO.getId());
        cbbUpdateUserDTO.setGroupId(userDetailDTO.getGroupId());
        // 不在此处触发同步
        cbbUpdateUserDTO.setEnableSyncData(false);
        
        IacUserTypeEnum userType = cbbUpdateUserDTO.getUserType();
        if (IacUserTypeEnum.AD == userType || IacUserTypeEnum.LDAP == userType ||
                IacUserTypeEnum.THIRD_PARTY == userType) {
            cbbUpdateUserDTO.setInvalidTime(userIdentityConfigDTO.getInvalidTime());
            LOGGER.info("用户配置：AD/LDAP域/第三方用户，更新用户[{}]失效时间", objectName);
            userAPI.updateUser(cbbUpdateUserDTO);
        } else {
            cbbUpdateUserDTO.setInvalidTime(userIdentityConfigDTO.getInvalidTime());
            cbbUpdateUserDTO.setAccountExpires(userIdentityConfigDTO.getAccountExpireDate());
            userAPI.updateUser(cbbUpdateUserDTO);
            LOGGER.info("用户配置：更新用户[{}]过期时间和失效天数", objectName);
        }
        return objectName;
    }

    private void batchUpdateUserGroup(UUID itemId, IacUserGroupMgmtAPI userGroupAPI) throws BusinessException {
        IacUserGroupDetailDTO userGroupDetailDTO = userGroupAPI.getUserGroupDetail(itemId);
        String objectName = userGroupDetailDTO.getName();
        IacUpdateUserGroupDTO cbbUpdateUserGroupDTO = new IacUpdateUserGroupDTO();
        BeanUtils.copyProperties(userGroupDetailDTO, cbbUpdateUserGroupDTO);

        cbbUpdateUserGroupDTO.setInvalidTime(userIdentityConfigDTO.getInvalidTime());
        cbbUpdateUserGroupDTO.setAccountExpires(userIdentityConfigDTO.getAccountExpireDate());

        // 不在此处触发同步
        cbbUpdateUserGroupDTO.setEnableSyncData(false);

        userGroupAPI.updateUserGroup(cbbUpdateUserGroupDTO);
        LOGGER.info("用户组配置：更新用户组[{}]过期时间和失效天数", objectName);
    }

    private List<UUID> filterUserListByType(UUID userGroupId, List<IacUserTypeEnum> userTypeList) throws BusinessException {
        List<UUID> userIdList = new ArrayList<>();
        // 组下用户可能会超过1000,接口限制一次最多返回1000条数据
        IacPageResponse<IacUserDetailDTO> pageResult = pageQueryByGroupId(userGroupId, 0);
        // 总页数
        int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
        for (int page = 0; page < totalPage; page++) {
            // 前面已查过，不重复查
            if (page == 0) {
                userIdList.addAll(filterUserListByType(pageResult.getItemArr(), userTypeList));
                continue;
            }
            pageResult = pageQueryByGroupId(userGroupId, page);
            if (pageResult.getTotal() == 0) {
                break;
            }
            userIdList.addAll(filterUserListByType(pageResult.getItemArr(), userTypeList));
        }
        return userIdList;
    }

    private List<UUID> filterUserListByType(IacUserDetailDTO[] userDetailDTOArr, List<IacUserTypeEnum> userTypeList) {
        return Arrays.stream(userDetailDTOArr).filter(cbbUserDetailDTO -> userTypeList.contains(cbbUserDetailDTO.getUserType()))
                .map(IacUserDetailDTO::getId)
                .collect(Collectors.toList());
    } 

    private IacPageResponse<IacUserDetailDTO> pageQueryByGroupId(UUID groupId, Integer page) throws BusinessException {
        IacQueryUserListPageDTO pageDTO = new IacQueryUserListPageDTO();
        pageDTO.setGroupId(groupId);
        pageDTO.setPage(page);
        pageDTO.setLimit(Constants.MAX_QUERY_LIST_SIZE);
        return userAPI.pageQueryUserListByGroupId(pageDTO);
    }
}
