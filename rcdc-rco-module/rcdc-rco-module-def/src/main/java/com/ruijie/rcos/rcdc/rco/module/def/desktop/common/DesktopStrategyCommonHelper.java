package com.ruijie.rcos.rcdc.rco.module.def.desktop.common;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern.*;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.*;

/**
 * Description: 云桌面策略协助类。用于openAPI中，根据桌面校验云桌面策略是否可用（与前端的可用结果一致）
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-07-21
 *
 * @author linke
 */
@Service
public class DesktopStrategyCommonHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopStrategyCommonHelper.class);

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    /**
     * 校验云桌面策略是否符合桌面
     * @param deskId 云桌面id
     * @param deskStrategyId 云桌面策略id
     * @throws BusinessException 业务异常
     */
    public void checkStrategy(UUID deskId, UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        CloudDesktopDetailDTO deskInfo = getDeskInfo(deskId);
        DeskStrategyDTO deskStrategyDTO = getDeskStrategyInfo(deskInfo, deskStrategyId);
        UUID imageId = deskInfo.getDesktopImageId();
        if (imageId != null) {
            getPageDeskStrategyByImageId(deskStrategyDTO, imageId);
        }
        getPageDeskStrategyByDeskId(deskStrategyDTO, deskId);
        if (deskInfo.getUserProfileStrategyId() != null && deskInfo.getSessionType() != CbbDesktopSessionType.MULTIPLE) {
            getPageDeskStrategyByUserProfileStrategyId(deskStrategyDTO, deskInfo.getUserProfileStrategyId());
        }
        UUID[] desktopPoolIdArr = null;
        String desktopPoolModel = null;
        if (Objects.nonNull(deskInfo.getDesktopPoolId())) {
            desktopPoolIdArr = new UUID[]{deskInfo.getDesktopPoolId()};
            CbbDesktopPoolDTO cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(deskInfo.getDesktopPoolId());
            desktopPoolModel = cbbDesktopPoolDTO.getPoolModel().name();
        }
        checkDeskStrategyCanUserByDesktopPool(deskStrategyDTO, desktopPoolIdArr, desktopPoolModel);

        if (!deskStrategyDTO.getCanUsed()) {
            throw new BusinessException(RCO_DESK_COMMON_STRATEGY_NOT_MATCH, deskStrategyId.toString(), deskStrategyDTO.getCanUsedMessage());
        }
    }

    private DeskStrategyDTO getDeskStrategyInfo(CloudDesktopDetailDTO dto, UUID deskStrategyId) throws BusinessException {
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder().setPageLimit(0, 5);
        requestBuilder.in("id",  new UUID[]{deskStrategyId});
        String deskType = dto.getDeskType();
        requestBuilder.in("strategyType",  new CbbStrategyType[]{CbbStrategyType.valueOf(deskType)});
        StringBuilder sb = new StringBuilder();
        switch (deskType) {
            case "IDV":
                requestBuilder.in("desktopType", new CbbCloudDeskPattern[]{PERSONAL, RECOVERABLE, APP_LAYER});
                sb.append("个性、还原、应用分发");
                break;
            case "VOI":
                requestBuilder.in("desktopType", new CbbCloudDeskPattern[]{PERSONAL, RECOVERABLE});
                sb.append("个性、还原");
                break;
            default:
                if (StringUtils.hasText(dto.getDesktopType())) {
                    requestBuilder.in("desktopType", new CbbCloudDeskPattern[]{CbbCloudDeskPattern.valueOf(dto.getDesktopType())});
                    HashMap<CbbCloudDeskPattern, String> patternMap = new HashMap<>();
                    patternMap.put(PERSONAL, "个性");
                    patternMap.put(RECOVERABLE, "还原");
                    patternMap.put(APP_LAYER, "应用分层");
                    sb.append(patternMap.get(CbbCloudDeskPattern.valueOf(dto.getDesktopType())));
                } else {
                    requestBuilder.isNull("desktopType");
                }
        }

        PageQueryResponse<DeskStrategyDTO> pageQueryResponse = deskStrategyAPI.pageDeskStrategyQuery(requestBuilder.build());
        if (pageQueryResponse == null || pageQueryResponse.getItemArr().length == 0) {

            throw new BusinessException(RCO_DESK_COMMON_STRATEGY_AND_DESK_NOT_MATCH, deskStrategyId.toString(),
                    dto.getId().toString(), deskType, sb.toString());
        }
        return pageQueryResponse.getItemArr()[0];
    }

    private CloudDesktopDetailDTO getDeskInfo(UUID deskId) throws BusinessException {
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
        if (DesktopType.APP_LAYER.toString().equals(cloudDesktopDetailDTO.getDesktopType())
                && Objects.nonNull(cloudDesktopDetailDTO.getSystemDisk())) {
            cloudDesktopDetailDTO.setSystemDisk(cloudDesktopDetailDTO.getSystemDisk() + Constants.SYSTEM_DISK_CAPACITY_INCREASE_SIZE);
        }
        if (CbbCloudDeskType.IDV.name().equals(cloudDesktopDetailDTO.getDesktopCategory())) {
            cloudDesktopDetailDTO.setDesktopCategory(cloudDesktopDetailDTO.getCbbImageType());
            cloudDesktopDetailDTO.setDeskType(cloudDesktopDetailDTO.getCbbImageType());
        }
        return cloudDesktopDetailDTO;
    }

    private void getPageDeskStrategyByImageId(DeskStrategyDTO dto, UUID imageId) {

        String canUsedMessage;
        try {
            if (Objects.equals(CbbStrategyType.VDI.name(), dto.getStrategyType())) {
                canUsedMessage = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(imageId, dto.getId());
            } else if (Objects.equals(CbbStrategyType.IDV.name(), dto.getStrategyType())) {
                canUsedMessage = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(imageId, dto.getId());
            } else {
                canUsedMessage = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByImageId(imageId, dto.getId());
            }
        } catch (BusinessException e) {
            LOGGER.error("根据云桌面Id限制查询云桌面策略列表异常", e);
            canUsedMessage = LocaleI18nResolver.resolve(RCO_DESK_COMMON_STRATEGY_QUERY_EXCEPTIONS);
        }
        if (!canUsedMessage.isEmpty()) {
            dto.setCanUsed(false);
        }
        dto.setCanUsedMessage(canUsedMessage);
    }

    private void getPageDeskStrategyByDeskId(DeskStrategyDTO dto, UUID deskId) {

        if (!dto.getCanUsed()) {
            return;
        }
        String canUsedMessage;
        try {
            if (Objects.equals(CbbStrategyType.VDI.name(), dto.getStrategyType())) {
                canUsedMessage = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByDeskId(deskId, dto.getId());
            } else if (Objects.equals(CbbStrategyType.IDV.name(), dto.getStrategyType())) {
                canUsedMessage = getIDVCanUseMessage(deskId, dto);
            } else if (Objects.equals(CbbStrategyType.VOI.name(), dto.getStrategyType())) {
                canUsedMessage = getVOICanUseMessage(deskId, dto);
            } else {
                throw new BusinessException(RCDC_RCO_COMMON_STRATEGY_TYPE_NOT_MATCH, dto.getStrategyType().toString());
            }
        } catch (BusinessException e) {
            LOGGER.error("根据云桌面Id限制查询云桌面策略列表异常", e);
            canUsedMessage = LocaleI18nResolver.resolve(RCO_DESK_COMMON_STRATEGY_QUERY_EXCEPTIONS);
        }
        if (!canUsedMessage.isEmpty()) {
            dto.setCanUsed(false);
        }
        dto.setCanUsedMessage(canUsedMessage);
    }

    private String getIDVCanUseMessage(UUID deskId, DeskStrategyDTO dto) throws BusinessException {
        Boolean isDeskEnableFullSystemDisk = userDesktopMgmtAPI.getDeskEnableFullSystemDiskByDeskId(deskId);
        // 开启系统盘自动扩容的桌面，不允许使用未开启系统盘自动扩容的策略
        if (Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && !Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(RCDC_RCO_COMMON_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_NOT_FULL_STRATEGY_IDV);
        }
        // 没有开启系统盘自动扩容的桌面，不允许使用开启系统盘自动扩容的策略
        if (!Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(RCDC_RCO_COMMON_NOT_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_FULL_STRATEGY_IDV);
        }

        return cbbIDVDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByDeskId(deskId, dto.getId());
    }

    private void getPageDeskStrategyByUserProfileStrategyId(DeskStrategyDTO dto, UUID userProfileStrategyId) {

        String canUsedMessage;
        try {
            // 不需要区分桌面类型
            canUsedMessage = deskStrategyAPI.getDeskStrategyUsedMessageByUserProfileStrategyId(dto.getId());
        } catch (BusinessException e) {
            LOGGER.error("根据用户配置策略Id:[{}]限制查询云桌面策略列表异常", userProfileStrategyId, e);
            canUsedMessage = LocaleI18nResolver.resolve(RCO_DESK_COMMON_STRATEGY_QUERY_EXCEPTIONS);
        }
        if (!canUsedMessage.isEmpty()) {
            dto.setCanUsed(false);
            dto.setCanUsedMessage(canUsedMessage);
        }

    }

    private String getVOICanUseMessage(UUID deskId, DeskStrategyDTO dto) throws BusinessException {
        Boolean isDeskEnableFullSystemDisk = userDesktopMgmtAPI.getDeskEnableFullSystemDiskByDeskId(deskId);
        // 开启系统盘自动扩容的桌面，不允许使用未开启系统盘自动扩容的策略
        if (Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && !Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(RCDC_RCO_COMMON_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_NOT_FULL_STRATEGY_VOI);
        }
        // 没有开启系统盘自动扩容的桌面，不允许使用开启系统盘自动扩容的策略
        if (!Boolean.TRUE.equals(isDeskEnableFullSystemDisk)
                && Boolean.TRUE.equals(dto.getEnableFullSystemDisk())) {
            return LocaleI18nResolver.resolve(RCDC_RCO_COMMON_NOT_FULL_SYSTEM_DISK_DESK_CAN_NOT_USE_FULL_STRATEGY_VOI);
        }

        return cbbVOIDeskStrategyMgmtAPI.getDeskStrategyUsedMessageByDeskId(deskId, dto.getId());
    }

    /**
     * 根据桌面池ID判断策略是否可用，并添加提示语句
     *
     * @param dto dto
     * @param desktopPoolIdArr  desktopPoolIdArr
     * @param desktopPoolModel desktopPoolModel
     */
    public void checkDeskStrategyCanUserByDesktopPool(DeskStrategyDTO dto, @Nullable UUID[] desktopPoolIdArr,
                                                      @Nullable String desktopPoolModel) {
        Assert.notNull(dto, "dto must not be null");

        if (ArrayUtils.isEmpty(desktopPoolIdArr) && StringUtils.isEmpty(desktopPoolModel)) {
            return;
        }
        if (ArrayUtils.isNotEmpty(desktopPoolIdArr)) {
            for (UUID desktopPoolId : desktopPoolIdArr) {
                checkStrategyCanUseByDesktopPoolId(desktopPoolId, dto);
            }
        }

        if (StringUtils.hasText(desktopPoolModel)) {
            checkStrategyCanUseByDesktopPoolModel(desktopPoolModel, dto);
        }
    }

    private void checkStrategyCanUseByDesktopPoolId(UUID desktopPoolId, DeskStrategyDTO dto) {

        if (Boolean.FALSE.equals(dto.getCanUsed())) {
            return;
        }
        String canUsedMessage;
        try {
            canUsedMessage = cbbDesktopPoolMgmtAPI.getStrategyUsedMessageByDesktopPoolId(desktopPoolId, dto.getId());
        } catch (BusinessException e) {
            LOGGER.error("根据云桌面Id限制查询云桌面策略列表异常", e);
            canUsedMessage = LocaleI18nResolver.resolve(RCO_DESK_COMMON_STRATEGY_QUERY_EXCEPTIONS);
        }
        if (!canUsedMessage.isEmpty()) {
            dto.setCanUsed(false);
        }
        dto.setCanUsedMessage(canUsedMessage);

    }

    private void checkStrategyCanUseByDesktopPoolModel(String desktopPoolModel, DeskStrategyDTO dto) {

        if (Boolean.FALSE.equals(dto.getCanUsed())) {
            return;
        }
        String canUsedMessage;
        try {
            CbbDesktopPoolModel model = CbbDesktopPoolModel.valueOf(desktopPoolModel);
            canUsedMessage = cbbDesktopPoolMgmtAPI.getStrategyUsedMessageByDesktopPoolModel(model, dto.getId());
        } catch (BusinessException e) {
            LOGGER.error(String.format("桌面池模式[%s]格式错误", desktopPoolModel), e);
            return;
        }
        if (!canUsedMessage.isEmpty()) {
            dto.setCanUsed(false);
        }
        dto.setCanUsedMessage(canUsedMessage);

    }

}
