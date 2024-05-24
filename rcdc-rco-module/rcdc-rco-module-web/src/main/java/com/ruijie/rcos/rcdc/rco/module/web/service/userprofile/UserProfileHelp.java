package com.ruijie.rcos.rcdc.rco.module.web.service.userprofile;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileValidateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask.PushUserProfilePathUpdateBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CleanUserProfilePathInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CreateUserProfileStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.UserProfileStrategyListRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: UPM辅助service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/11
 *
 * @author zwf
 */
@Service
public class UserProfileHelp {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileHelp.class);

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserProfileValidateAPI userProfileValidateAPI;

    @Autowired
    private UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    private static final int ZERO_SIZE = 0;

    /**
     * 选定全部云桌面清理时，设置云桌面ID列表
     *
     * @param webRequest     请求
     * @param sessionContext session信息
     * @throws BusinessException 异常处理
     */
    public void setCloudDesktopIdArrWhenAllClean(CleanUserProfilePathInfoRequest webRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        if (webRequest.getIsAllClean().booleanValue()) {
            LOGGER.info("用户[{}]开启清理全部云桌面功能", sessionContext.getUserId());

            UUID[] idArr;
            if (permissionHelper.isAllGroupPermission(sessionContext)) {
                List<UUID> desktopIdList = userDesktopMgmtAPI.findAllDesktopId();
                idArr = desktopIdList.toArray(new UUID[desktopIdList.size()]);
            } else {
                idArr = permissionHelper.getDesktopIdArr(sessionContext);
            }

            LOGGER.info("需要清理的云桌面对象[{}]", Arrays.toString(idArr));

            webRequest.setCloudDesktopIdArr(idArr);
        }
    }


    /**
     * 处理选定路径被删除的情形
     *
     * @param request 请求
     * @throws BusinessException 异常处理
     */
    public void validateUserProfilePathExist(CreateUserProfileStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UserProfileStrategyRelatedDTO[] pathArr = request.getPathArr();
        Set<UUID> userProfilePathIdList = new HashSet<>();
        for (UserProfileStrategyRelatedDTO relatedDTO : pathArr) {
            userProfilePathIdList.add(relatedDTO.getId());
        }

        // 检查重复的ID
        if (userProfilePathIdList.size() != pathArr.length) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_WITHOUT_PATH, request.getName());
        }

        UUID[] userProfilePathIdArr = userProfilePathIdList.toArray(new UUID[userProfilePathIdList.size()]);
        List<UUID> userProfilePathIdExistList = userProfileValidateAPI.validateUserProfilePathExist(userProfilePathIdArr);

        int actualNum = userProfilePathIdExistList.size();
        int selectNum = userProfilePathIdList.size();
        if (actualNum == ZERO_SIZE) {
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_WITHOUT_PATH, request.getName());
        }

        if (selectNum > actualNum) {
            LOGGER.warn("选定的配置数目[{}]大于实际存在的配置数目[{}]，说明此时存在选定配置已被删除，执行过滤操作", userProfilePathIdList.size(),
                    userProfilePathIdExistList.size());
            request.setPathArr(getActualPathArr(pathArr, userProfilePathIdExistList));
        }
    }

    private UserProfileStrategyRelatedDTO[] getActualPathArr(UserProfileStrategyRelatedDTO[] pathArr, List<UUID> userProfilePathIdExistList) {
        List<UserProfileStrategyRelatedDTO> actualPathList = new ArrayList<>();
        for (UserProfileStrategyRelatedDTO path : pathArr) {
            if (userProfilePathIdExistList.contains(path.getId())) {
                actualPathList.add(path);
            }
        }

        return actualPathList.toArray(new UserProfileStrategyRelatedDTO[actualPathList.size()]);
    }

    /**
     * 构造向云桌面同步个性化配置变更的批处理
     *
     * @param builder      批处理框架
     * @param desktopIdArr 云桌面ID列表
     * @return 结果
     * @throws BusinessException 异常处理
     */
    public DefaultWebResponse buildPushUserProfilePathUpdate(BatchTaskBuilder builder, UUID[] desktopIdArr) throws BusinessException {
        Assert.notNull(builder, "builder is not null");
        Assert.notNull(desktopIdArr, "desktopIdArr must not be null");

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(desktopIdArr).map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_BATCH_NOTIFY_UPDATE_TASK_NAME, new String[]{}).build()).iterator();
        PushUserProfilePathUpdateBatchTaskHandler handler = new PushUserProfilePathUpdateBatchTaskHandler(iterator, userProfileStrategyNotifyAPI);
        BatchTaskSubmitResult result =
                builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_BATCH_NOTIFY_UPDATE_TASK_NAME, new String[]{})
                        .setUniqueId(UUID.randomUUID())
                        .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_BATCH_NOTIFY_UPDATE_TASK_DESC, new String[]{}).enableParallel()
                        .registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 根据条件查看策略是否可以使用
     *
     * @param webRequest        UserProfileStrategyListRequest
     * @param pageQueryResponse PageQueryResponse
     */
    public void filterCanUseUserProfileStrategy(UserProfileStrategyListRequest webRequest, PageQueryResponse<UserProfileStrategyDTO> pageQueryResponse) {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(pageQueryResponse, "pageQueryResponse must not be null");
        String sessionType = Optional.ofNullable(webRequest.getSessionType()).orElse(CbbDesktopSessionType.SINGLE.name());
        if (Objects.equals(sessionType, CbbDesktopSessionType.SINGLE.name())) {
            // 单会话的过滤条件不变
            if (Objects.nonNull(webRequest.getImageId())) {
                getPageUserProfileStrategyByImageId(pageQueryResponse, webRequest.getImageId());
            }
            if (Objects.nonNull(webRequest.getStrategyId())) {
                getPageUserProfileStrategyByStrategyId(pageQueryResponse, webRequest.getStrategyId());
            }
            dealCanUseBySessionType(pageQueryResponse, sessionType);
        }

        if (Objects.equals(sessionType, CbbDesktopSessionType.MULTIPLE.name())) {
            // 多会话过滤只能使用文件服务器存储类型
            dealCanUseBySessionType(pageQueryResponse, sessionType);
        }
    }

    private void getPageUserProfileStrategyByStrategyId(PageQueryResponse<UserProfileStrategyDTO> pageQueryResponse, UUID strategyId) {
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(dto -> {
            String canUsedMessage;
            try {
                canUsedMessage = userProfileMgmtAPI.getStrategyUsedMessageByStrategyId(strategyId);
            } catch (BusinessException e) {
                LOGGER.error("根据云桌面策略Id:[{}]限制查询用户配置策略列表异常", strategyId, e);
                canUsedMessage = LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_QUERY_EXCEPTIONS);
            }
            if (!canUsedMessage.isEmpty()) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }

    private void getPageUserProfileStrategyByImageId(PageQueryResponse<UserProfileStrategyDTO> pageQueryResponse, UUID imageId) {
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(dto -> {
            String canUsedMessage;
            try {
                canUsedMessage = userProfileMgmtAPI.getStrategyUsedMessageByImageId(imageId);
            } catch (BusinessException e) {
                LOGGER.error("根据镜像Id:[{}]限制查询用户配置策略列表异常", imageId, e);
                canUsedMessage = LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_QUERY_EXCEPTIONS);
            }
            if (!canUsedMessage.isEmpty()) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }

    private void dealCanUseBySessionType(PageQueryResponse<UserProfileStrategyDTO> pageQueryResponse, String sessionType) {
        if (ArrayUtils.isEmpty(pageQueryResponse.getItemArr())) {
            return;
        }
        for (UserProfileStrategyDTO userProfileStrategyDTO : pageQueryResponse.getItemArr()) {
            if (BooleanUtils.isFalse(userProfileStrategyDTO.getCanUsed())) {
                continue;
            }
            if (Objects.equals(sessionType, CbbDesktopSessionType.MULTIPLE.name())
                    && userProfileStrategyDTO.getStorageType() != UserProfileStrategyStorageTypeEnum.FILE_SERVER) {
                userProfileStrategyDTO.setCanUsed(false);
                userProfileStrategyDTO.setCanUsedMessage(
                        LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_MULTI_SESSION_MUST_FILE_SERVER));
            }
            if (Objects.equals(sessionType, CbbDesktopSessionType.SINGLE.name())
                    && userProfileStrategyDTO.getStorageType() == UserProfileStrategyStorageTypeEnum.FILE_SERVER) {
                userProfileStrategyDTO.setCanUsed(false);
                userProfileStrategyDTO.setCanUsedMessage(
                        LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_SINGLE_SESSION_NOT_FILE_SERVER));
            }
        }
    }
}
