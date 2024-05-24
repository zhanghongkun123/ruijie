package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileValidateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileCleanGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileCleanTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Description: 清理用户配置下的路径数据
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/27
 *
 * @author zwf
 */
public class CleanUserProfilePathInfoBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CleanUserProfilePathInfoBatchTaskHandler.class);

    private static final int SINGLE = 1;

    private UserProfileMgmtAPI userProfileMgmtAPI;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private UUID userProfileTargetId;

    private UserProfileCleanTypeEnum cleanType;

    private String deskName = StringUtils.EMPTY;

    private UserProfileValidateAPI userProfileValidateAPI;

    public void setUserProfileTargetId(UUID userProfileTargetId) {
        this.userProfileTargetId = userProfileTargetId;
    }

    public void setCleanType(UserProfileCleanTypeEnum cleanType) {
        this.cleanType = cleanType;
    }

    public void setUserProfileValidateAPI(UserProfileValidateAPI userProfileValidateAPI) {
        this.userProfileValidateAPI = userProfileValidateAPI;
    }

    public CleanUserProfilePathInfoBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI,
                                                    UserProfileMgmtAPI userProfileMgmtAPI, CbbDeskMgmtAPI cbbDeskMgmtAPI) {
        super(iterator);
        Assert.notNull(userProfileMgmtAPI, "the userProfileMgmtAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        Assert.notNull(cbbDeskMgmtAPI, "cbbDeskMgmtAPI must not be null");
        this.auditLogAPI = auditLogAPI;
        this.userProfileMgmtAPI = userProfileMgmtAPI;
        this.cbbDeskMgmtAPI = cbbDeskMgmtAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "the batchTaskItem is null.");
        UUID deskId = batchTaskItem.getItemID();
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        deskName = deskDTO.getName();
        switch (cleanType) {
            case PATH:
                return cleanForPath(deskDTO);
            case CONFIGURATION:
                return cleanForConfiguration(deskDTO);
            case STRATEGY:
                return cleanForStrategy(deskDTO);
            default:
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_NOT_FOUND_TYPE, deskName);
        }
    }

    private BatchTaskItemResult cleanForPath(CbbDeskDTO deskDTO) throws BusinessException {
        List<UserProfilePathDetailDTO> userProfilePathDTOList = userProfileMgmtAPI.getEffectiveUserProfilePathByChildPathId(userProfileTargetId);
        UserProfileCleanGuestToolMsgDTO guestToolMsgDTO = new UserProfileCleanGuestToolMsgDTO();
        UUID deskId = deskDTO.getDeskId();
        boolean hasCleanPath = !CollectionUitls.isEmpty(userProfilePathDTOList);
        String msg = getMessage(userProfilePathDTOList);
        if (hasCleanPath) {
            dealBeforeSendMessage(userProfilePathDTOList, guestToolMsgDTO, deskDTO);
        }

        try {
            validateDeskTopRunningState(deskDTO.getDeskState());
            userProfileMgmtAPI.sendCleanUserProfilePathMessage(guestToolMsgDTO, deskId);

            saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_CHILD_PATH_CLEAN_SUCCESS, deskName, msg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_CHILD_PATH_CLEAN_SUCCESS).msgArgs(deskName, msg).build();
        } catch (BusinessException e) {
            LOGGER.error("通知云桌面[{}]进行路径[{}]清理失败，失败原因：", deskId, msg, e);

            //非运行池桌面，审计日志特殊处理
            if (userProfileValidateAPI.isPoolDesktopWithoutUser(deskId, deskDTO.getDesktopPoolType())) {
                saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_CHILD_PATH_CLEAN_FAIL_IGNORE, deskName, msg);
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_CHILD_PATH_CLEAN_FAIL_IGNORE, e, deskName, msg);
            }
            // 一个路径
            saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_CHILD_PATH_CLEAN_FAIL, deskName, msg);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_CHILD_PATH_CLEAN_FAIL, e, deskName, msg);
        }
    }

    private BatchTaskItemResult cleanForConfiguration(CbbDeskDTO deskDTO) throws BusinessException {
        List<UserProfilePathDetailDTO> userProfilePathDTOList = userProfileMgmtAPI.getEffectiveUserProfilePathByPathId(userProfileTargetId);
        UserProfileCleanGuestToolMsgDTO guestToolMsgDTO = new UserProfileCleanGuestToolMsgDTO();
        UUID deskId = deskDTO.getDeskId();
        boolean hasCleanPath = !CollectionUitls.isEmpty(userProfilePathDTOList);
        String msg = getMessage(userProfilePathDTOList);
        if (hasCleanPath) {
            dealBeforeSendMessage(userProfilePathDTOList, guestToolMsgDTO, deskDTO);
        }

        try {
            validateDeskTopRunningState(deskDTO.getDeskState());
            userProfileMgmtAPI.sendCleanUserProfilePathMessage(guestToolMsgDTO, deskId);

            // 一个配置,存在多条路径
            saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_SUCCESS, deskName, msg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_SUCCESS).msgArgs(deskName, msg).build();

        } catch (BusinessException e) {
            LOGGER.error("通知云桌面[{}]进行路径[{}]清理失败，失败原因：", deskId, msg, e);

            //非运行池桌面，审计日志特殊处理
            if (userProfileValidateAPI.isPoolDesktopWithoutUser(deskId, deskDTO.getDesktopPoolType())) {
                saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_FAIL_IGNORE, deskName, msg);
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_FAIL_IGNORE, e, deskName, msg);
            }
            // 一个配置,存在多条路径
            saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_FAIL, deskName, msg);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_FAIL, e, deskName, msg);
        }
    }

    private BatchTaskItemResult cleanForStrategy(CbbDeskDTO deskDTO) throws BusinessException {
        List<UserProfilePathDetailDTO> userProfilePathDTOList = userProfileMgmtAPI.getEffectiveUserProfilePathByStrategyId(userProfileTargetId);
        UserProfileCleanGuestToolMsgDTO guestToolMsgDTO = new UserProfileCleanGuestToolMsgDTO();
        UUID deskId = deskDTO.getDeskId();
        boolean hasCleanPath = !CollectionUitls.isEmpty(userProfilePathDTOList);
        String msg = getMessage(userProfilePathDTOList);
        if (hasCleanPath) {
            dealBeforeSendMessage(userProfilePathDTOList, guestToolMsgDTO, deskDTO);
        }

        try {
            validateDeskTopRunningState(deskDTO.getDeskState());
            userProfileMgmtAPI.sendCleanUserProfilePathMessage(guestToolMsgDTO, deskId);

            // 一个策略,存在多个配置
            saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_CLEAN_SUCCESS, deskName, msg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_CLEAN_SUCCESS).msgArgs(deskName, msg).build();

        } catch (BusinessException e) {
            LOGGER.error("通知云桌面[{}]进行路径[{}]清理失败，失败原因：", deskId, msg, e);

            //非运行池桌面，审计日志特殊处理
            if (userProfileValidateAPI.isPoolDesktopWithoutUser(deskId, deskDTO.getDesktopPoolType())) {
                saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_CLEAN_FAIL_IGNORE, deskName, msg);
                throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_CLEAN_FAIL_IGNORE, e, deskName, msg);
            }
            // 一个策略,存在多个配置
            saveDeskCleanLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_CLEAN_FAIL, deskName, msg);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_CLEAN_FAIL, e, deskName, msg);
        }
    }

    private void dealBeforeSendMessage(List<UserProfilePathDetailDTO> userProfilePathDTOList,
                                       UserProfileCleanGuestToolMsgDTO guestToolMsgDTO, CbbDeskDTO deskDTO) {
        LOGGER.debug("通知云桌面,开始对待清理路径列表进行分组，deskId:{} ", deskDTO.getDeskId());
        userProfileMgmtAPI.getGuestToolCleanPath(guestToolMsgDTO, userProfilePathDTOList);

        //预先缓存清理配置的命令
        if (isNeedToSaveFailRequest(deskDTO.getDeskId(), deskDTO.getDesktopPoolType(), true)) {
            userProfileMgmtAPI.saveFailCleanRequest(guestToolMsgDTO, deskDTO.getDeskId());
        }
    }

    private String getMessage(List<UserProfilePathDetailDTO> userProfilePathDTOList) {
        if (!userProfilePathDTOList.isEmpty()) {
            switch (cleanType) {
                case PATH:
                    return userProfilePathDTOList.get(0).getPath();
                case CONFIGURATION:
                    return userProfilePathDTOList.get(0).getName();
                case STRATEGY:
                    return userProfileMgmtAPI.findStrategyNameByStrategyId(userProfileTargetId);
                default:
                    return StringUtils.EMPTY;
            }
        }

        return StringUtils.EMPTY;
    }

    private boolean isNeedToSaveFailRequest(UUID deskId, DesktopPoolType desktopPoolType, boolean hasCleanPath) {
        //非绑定用户的池桌面不保存相关信息
        return (!userProfileValidateAPI.isPoolDesktopWithoutUser(deskId, desktopPoolType)) && hasCleanPath;
    }

    private void validateDeskTopRunningState(CbbCloudDeskState deskState) throws BusinessException {
        if (CbbCloudDeskState.RUNNING != deskState) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_VALIDATE_DESKTOP_CLOSE, deskName);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount + successCount > SINGLE) {
            return buildDefaultFinishResult(successCount, failCount, UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_BATCH_SUCCESS);
        }

        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_BATCH_SINGLE_SUCCESS)
                    .msgArgs(new String[] {deskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_CLEAN_BATCH_SINGLE_FAIL)
                    .msgArgs(new String[] {deskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    private void saveDeskCleanLog(String key, String deskName, String msg) {
        auditLogAPI.recordLog(key, deskName, msg);
    }
}
