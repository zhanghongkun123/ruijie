package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.common;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DeskStrategyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 云桌面策略协助类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-07-21
 *
 * @author linke
 */
@Service
public class DesktopStrategyHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStrategyHelper.class);

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    /**
     * 根据桌面池ID判断策略是否可用，并添加提示语句
     *
     * @param pageQueryResponse pageQueryResponse
     * @param desktopPoolIdArr  desktopPoolIdArr
     * @param desktopPoolModel  desktopPoolModel
     * @param sessionType 会话类型
     */
    public void checkDeskStrategyCanUserByDesktopPool(PageQueryResponse<DeskStrategyDTO> pageQueryResponse, @Nullable UUID[] desktopPoolIdArr,
                                                      @Nullable String desktopPoolModel, String sessionType) {
        Assert.notNull(pageQueryResponse, "pageQueryResponse must not be null");
        Assert.notNull(sessionType, "sessionType must not be null");

        if (ArrayUtils.isEmpty(desktopPoolIdArr) && StringUtils.isEmpty(desktopPoolModel)) {
            return;
        }
        if (ArrayUtils.isNotEmpty(desktopPoolIdArr)) {
            for (UUID desktopPoolId : desktopPoolIdArr) {
                checkStrategyCanUseByDesktopPoolId(desktopPoolId, pageQueryResponse.getItemArr());
            }
        }

        if (StringUtils.hasText(desktopPoolModel)) {
            checkStrategyCanUseByDesktopPoolModel(desktopPoolModel, pageQueryResponse.getItemArr());
        }

        if (StringUtils.hasText(sessionType)) {
            checkStrategyCanUseBySessionType(sessionType, pageQueryResponse.getItemArr());
        }
    }


    private void checkStrategyCanUseBySessionType(String sessionType, DeskStrategyDTO[] itemArr) {
        for (DeskStrategyDTO deskStrategyDTO : itemArr) {
            if (Boolean.FALSE.equals(deskStrategyDTO.getCanUsed())) {
                continue;
            }
            CbbDesktopSessionType type = CbbDesktopSessionType.valueOf(sessionType);
            if (deskStrategyDTO.getSessionType() != type) {
                deskStrategyDTO.setCanUsed(false);
                String canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_SESSION_TYPE_NON_CONFORMANCE);
                deskStrategyDTO.setCanUsedMessage(canUsedMessage);
            }
        }
    }

    /**
     * 根据本地盘大小判断策略是否可用，并添加提示语句
     *
     * @param pageQueryResponse pageQueryResponse
     * @param personalDisk      personalDisk
     */
    public void checkDeskStrategyCanUseByPersonalDisk(PageQueryResponse<DeskStrategyDTO> pageQueryResponse, @Nullable Integer personalDisk) {
        Assert.notNull(pageQueryResponse, "pageQueryResponse must not be null");
        if (Objects.isNull(personalDisk) || ArrayUtils.isEmpty(pageQueryResponse.getItemArr())) {
            return;
        }
        for (DeskStrategyDTO deskStrategyDTO : pageQueryResponse.getItemArr()) {
            if (Boolean.FALSE.equals(deskStrategyDTO.getCanUsed())) {
                return;
            }
            if (personalDisk <= 0 && BooleanUtils.isTrue(deskStrategyDTO.getEnableOpenDesktopRedirect())) {
                deskStrategyDTO.setCanUsed(Boolean.FALSE);
                deskStrategyDTO.setCanUsedMessage(
                        LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK));
            }
        }
    }

    private void checkStrategyCanUseByDesktopPoolId(UUID desktopPoolId, DeskStrategyDTO[] strategyDTOArr) {
        for (DeskStrategyDTO deskStrategyDTO : strategyDTOArr) {
            if (Boolean.FALSE.equals(deskStrategyDTO.getCanUsed())) {
                return;
            }
            String canUsedMessage;
            try {
                canUsedMessage = cbbDesktopPoolMgmtAPI.getStrategyUsedMessageByDesktopPoolId(desktopPoolId, deskStrategyDTO.getId());
            } catch (BusinessException e) {
                LOGGER.error("根据云桌面Id限制查询云桌面策略列表异常", e);
                canUsedMessage = LocaleI18nResolver.resolve(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_QUERY_EXCEPTIONS);
            }
            if (!canUsedMessage.isEmpty()) {
                deskStrategyDTO.setCanUsed(false);
            }
            deskStrategyDTO.setCanUsedMessage(canUsedMessage);
        }
    }

    private void checkStrategyCanUseByDesktopPoolModel(String desktopPoolModel, DeskStrategyDTO[] strategyDTOArr) {
        for (DeskStrategyDTO deskStrategyDTO : strategyDTOArr) {
            if (Boolean.FALSE.equals(deskStrategyDTO.getCanUsed())) {
                continue;
            }
            String canUsedMessage;
            try {
                CbbDesktopPoolModel model = CbbDesktopPoolModel.valueOf(desktopPoolModel);
                canUsedMessage = cbbDesktopPoolMgmtAPI.getStrategyUsedMessageByDesktopPoolModel(model, deskStrategyDTO.getId());
            } catch (BusinessException e) {
                LOGGER.error(String.format("桌面池模式[%s]格式错误", desktopPoolModel), e);
                continue;
            }
            if (!canUsedMessage.isEmpty()) {
                deskStrategyDTO.setCanUsed(false);
            }
            deskStrategyDTO.setCanUsedMessage(canUsedMessage);
        }
    }

    /**
     * 构建排序条件
     *
     * @param sortArr        排序条件
     * @param requestBuilder 参数构造器
     */
    public void buildPageQueryRequestSort(Sort[] sortArr, PageQueryBuilderFactory.RequestBuilder requestBuilder) {
        Assert.notNull(sortArr, "sortArr must not be null");
        Assert.notNull(requestBuilder, "requestBuilder must not be null");
        if (ArrayUtils.isEmpty(sortArr)) {
            return;
        }
        String[] ascSortArr = Arrays.stream(sortArr).filter(sort -> sort.getDirection() == Sort.Direction.ASC)
                .map(Sort::getFieldName).toArray(String[]::new);
        String[] descSortArr = Arrays.stream(sortArr).filter(sort -> sort.getDirection() == Sort.Direction.DESC)
                .map(Sort::getFieldName).toArray(String[]::new);
        if (ArrayUtils.isNotEmpty(ascSortArr)) {
            requestBuilder.asc(ascSortArr);
        }
        if (ArrayUtils.isNotEmpty(descSortArr)) {
            requestBuilder.desc(descSortArr);
        }
    }
}
