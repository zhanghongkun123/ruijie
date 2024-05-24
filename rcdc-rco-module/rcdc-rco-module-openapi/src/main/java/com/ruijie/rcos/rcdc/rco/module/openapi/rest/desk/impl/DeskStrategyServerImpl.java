package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUSBTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskStrategyState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.OpenApiBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.DeskStrategyServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestDeskStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.help.DeskStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.CreateDeskStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.UpdateDeskStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread.AsyncUpdateDesktopStrategyThread;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.COMMA_SEPARATION_CHARACTER;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class DeskStrategyServerImpl implements DeskStrategyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyServerImpl.class);

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private DeskStrategyHelper deskStrategyHelper;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private OpenApiTaskInfoAPI openApiTaskInfoAPI;

    @Autowired
    private CbbDeskStrategyCommonAPI cbbDeskStrategyCommonAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Override
    public PageQueryResponse pageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("openApi desk_strategy pageQuery request:" + JSONObject.toJSONString(pageQueryRequest));
        }

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        PageQueryResponse<DeskStrategyDTO> pageQueryResponse = deskStrategyAPI.pageDeskStrategyQuery(builder.build());
        return convert(pageQueryResponse);
    }

    @Override
    public void createDeskStrategy(CreateDeskStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "createDeskStrategy request must not be null");

        // 参数校验
        DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
        BeanUtils.copyProperties(request, strategyCheckDTO);
        deskStrategyAPI.createDeskStrategyValidate(strategyCheckDTO);

        // 将USB外设名称转成ID
        UUID[] usbTypeIdArr = convertToUSBTypeId(request.getUsbTypeNameList());
        request.setUsbTypeIdArr(usbTypeIdArr);
        Object deskStrategyRequest = deskStrategyHelper.buildCbbCreateDeskStrategyRequest(request);
        LOGGER.info("deskStrategyRequest参数为：{}", JSON.toJSONString(deskStrategyRequest));
        UUID deskStrategyId = null;
        CbbStrategyType strategyType = request.getStrategyType();
        try {
            switch (strategyType) {
                case VDI:
                    deskStrategyId = getVDIDeskStrategyId((CbbCreateDeskStrategyVDIDTO) deskStrategyRequest);
                    break;
                case IDV:
                    CbbDeskStrategyIDVDTO deskStrategyIDVDTO = (CbbDeskStrategyIDVDTO) deskStrategyRequest;
                    deskStrategyId = cbbIDVDeskStrategyMgmtAPI.createDeskStrategyIDV(deskStrategyIDVDTO);
                    break;
                case VOI:
                    CbbDeskStrategyVOIDTO deskStrategyVOIDTO = (CbbDeskStrategyVOIDTO) deskStrategyRequest;
                    deskStrategyId = cbbVOIDeskStrategyMgmtAPI.createDeskStrategyVOI(deskStrategyVOIDTO);
                    break;
                case THIRD:
                    CbbDeskStrategyThirdPartyDTO deskStrategyThirdPartyDTO = (CbbDeskStrategyThirdPartyDTO) deskStrategyRequest;
                    LOGGER.info("创建第三方策略参数deskStrategyThirdPartyDTO为：{}", JSON.toJSONString(deskStrategyThirdPartyDTO));
                    deskStrategyId = cbbThirdPartyDeskStrategyMgmtAPI.createDeskStrategyThirdParty(deskStrategyThirdPartyDTO);
                    break;
                default:
                    throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_STRATEGY_TYPE_NOT_EXIST, strategyType.name());
            }
            if (strategyType != CbbStrategyType.THIRD) {
                cloudDeskComputerNameConfigAPI.createCloudDeskComputerNameConfig(request.getComputerName(), deskStrategyId);
            }
        } catch (BusinessException e) {
            LOGGER.error("createDeskStrategy error:", e);
            auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_CREATE_FAIL_LOG, e, request.getStrategyName(), e.getI18nMessage());
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_STRATEGY_CREATE_ERROR, e, e.getI18nMessage());
        }
        // 策略创建通知
        rccmManageAPI.createNotify(deskStrategyId);
        // 保存管理集群信息
        UUID finalDeskStrategyId = deskStrategyId;
        Optional.ofNullable(request.getUnifiedManageDataId()).ifPresent(unifiedManageDataId -> {
            UnifiedManageDataRequest unifiedManageDataRequest =
                    new UnifiedManageDataRequest(finalDeskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY, unifiedManageDataId);
            rccmManageAPI.saveUnifiedManage(unifiedManageDataRequest);
        });
        auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_CREATE_SUCCESS_LOG, request.getStrategyName());
    }

    private UUID getVDIDeskStrategyId(CbbCreateDeskStrategyVDIDTO deskStrategyRequest) throws BusinessException {
        return cbbVDIDeskStrategyMgmtAPI.createDeskStrategyVDI(deskStrategyRequest);
    }

    @Override
    public AsyncTaskResponse updateDeskStrategy(UpdateDeskStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "updateDeskStrategy request must not be null");
        // 参数校验
        try {
            DeskStrategyCheckDTO strategyCheckDTO = new DeskStrategyCheckDTO();
            BeanUtils.copyProperties(request, strategyCheckDTO);
            deskStrategyAPI.updateDeskStrategyValidate(strategyCheckDTO);

            // 将USB外设名称转成ID
            UUID[] usbTypeIdArr = convertToUSBTypeId(request.getUsbTypeNameList());
            request.setUsbTypeIdArr(usbTypeIdArr);

        } catch (BusinessException e) {
            LOGGER.error("updateDeskStrategy error:", e);
            auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e, request.getStrategyName(), e.getI18nMessage());
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_STRATEGY_UPDATE_ERROR, e, e.getI18nMessage());
        }
        AsyncUpdateDesktopStrategyThread updateDesktopStrategyThread = new AsyncUpdateDesktopStrategyThread(request.getId(),
                AsyncTaskEnum.MODIFY_DESK_STRATEGY, openApiTaskInfoAPI, request);
        updateDesktopStrategyThread.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI).setCbbVDIDeskStrategyMgmtAPI(cbbVDIDeskStrategyMgmtAPI)
                .setCloudDeskComputerNameConfigAPI(cloudDeskComputerNameConfigAPI).setDeskStrategyHelper(deskStrategyHelper)
                .setUserDesktopMgmtAPI(userDesktopMgmtAPI)
                .setCbbIDVDeskStrategyMgmtAPI(cbbIDVDeskStrategyMgmtAPI).setCbbVOIDeskStrategyMgmtAPI(cbbVOIDeskStrategyMgmtAPI)
                .setUserProfileMgmtAPI(userProfileMgmtAPI).setDeskStrategyTciNotifyAPI(deskStrategyTciNotifyAPI).setRccmManageAPI(rccmManageAPI)
                .setCbbThirdPartyDeskStrategyMgmtAPI(cbbThirdPartyDeskStrategyMgmtAPI)
                .setDeskStrategyAPI(deskStrategyAPI).setAuditLogAPI(auditLogAPI).setOpenApiTaskInfoAPI(openApiTaskInfoAPI);
        ThreadExecutors.execute(Constant.MODIFY_DESK_STRATEGY_THREAD, updateDesktopStrategyThread);
        return new AsyncTaskResponse(updateDesktopStrategyThread.getCustomTaskId());
    }

    @Override
    public void deleteDeskStrategy(UUID deskStrategyId, @Nullable UnifiedManageDataRequest manageDataRequest) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        // 如果是主从场景，云桌面策略ID可能出现不一致用UnifiedManageDataId获取云桌面策略ID
        if (Objects.nonNull(manageDataRequest)) {
            UnifiedManageDataDTO unifiedManageDataDTO = rccmManageAPI.findByUnifiedManageDataId(manageDataRequest.getUnifiedManageDataId());
            if (Objects.nonNull(unifiedManageDataDTO)) {
                deskStrategyId = unifiedManageDataDTO.getRelatedId();
                // 删除时需要用到当前集群数据中的RelatedId
                manageDataRequest.setRelatedId(unifiedManageDataDTO.getRelatedId());
            }
        }
        CbbDeskStrategyDTO deskStrategy = null;
        try {
            deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId);
            // 判断是否系统内置策略，不允许删除
            if (Constants.THIRD_PARTY_STRATEGY_ID.equals(deskStrategy.getId().toString())) {
                throw new BusinessException(OpenApiBusinessKey.RCO_DESK_STRATEGY_THIRD_PARTY_BUILT_IN, deskStrategy.getName());
            }
            deskStrategyAPI.checkDeskStrategyCanChange(deskStrategyId,
                    Objects.isNull(manageDataRequest) ? null : manageDataRequest.getUnifiedManageDataId());
            deskStrategyAPI.deleteDeskStrategy(deskStrategyId);
            auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_DELETE_SUCCESS_LOG, deskStrategy.getName());
        } catch (BusinessException e) {
            LOGGER.error("deleteDeskStrategy error:", e);
            cbbDeskStrategyCommonAPI.updateDeskStrategyState(deskStrategyId, CbbDeskStrategyState.AVAILABLE);
            // 如果是同步策略，删除失败则把策略变成本地策略
            if (Objects.nonNull(manageDataRequest)) {
                rccmManageAPI.deleteUnifiedManage(manageDataRequest);
            }
            String name = Objects.nonNull(deskStrategy) ? deskStrategy.getName() : deskStrategyId.toString();
            auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_DELETE_FAIL_LOG, e, name, e.getI18nMessage());
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_STRATEGY_DELETE_ERROR, e, e.getI18nMessage());
        }

    }

    @Override
    public void syncDeskStrategy(UpdateDeskStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "syncDeskStrategy request must not be null");
        LOGGER.info("UpdateDeskStrategyRequest参数为：{}", JSON.toJSONString(request));
        UnifiedManageDataDTO unifiedManageDataDTO = rccmManageAPI.findByUnifiedManageDataId(request.getUnifiedManageDataId());
        if (Objects.isNull(unifiedManageDataDTO)) {
            CreateDeskStrategyRequest createDeskStrategyRequest = new CreateDeskStrategyRequest();
            BeanUtils.copyProperties(request, createDeskStrategyRequest);
            this.createDeskStrategy(createDeskStrategyRequest);
        } else {
            request.setId(unifiedManageDataDTO.getRelatedId());
            this.updateDeskStrategy(request);
        }
    }

    private PageQueryResponse<RestDeskStrategyDTO> convert(PageQueryResponse<DeskStrategyDTO> pageQueryResponse) {
        List<RestDeskStrategyDTO> dtoList = new ArrayList<>();
        PageQueryResponse<RestDeskStrategyDTO> result = new PageQueryResponse<>();
        if (pageQueryResponse == null || pageQueryResponse.getItemArr() == null) {
            result.setTotal(0);
            result.setItemArr(dtoList.toArray(new RestDeskStrategyDTO[0]));
            return result;
        }
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(deskStrategyDTO -> {
            RestDeskStrategyDTO dto = new RestDeskStrategyDTO();
            BeanUtils.copyProperties(deskStrategyDTO, dto);
            dtoList.add(dto);
        });

        result.setTotal(pageQueryResponse.getTotal());
        result.setItemArr(dtoList.toArray(new RestDeskStrategyDTO[dtoList.size()]));
        return result;
    }

    private UUID[] convertToUSBTypeId(List<String> nameList) throws BusinessException {
        Map<String, UUID> typeMap = cbbUSBTypeMgmtAPI.findByNameIn(nameList).stream().collect(Collectors.toMap(CbbUSBTypeDTO::getUsbTypeName,
                CbbUSBTypeDTO::getId, (key1, key2) -> key2));

        // 找出不存在的外设名称
        List<String> absentNameList = new ArrayList<>();
        for (String name : nameList) {
            if (typeMap.containsKey(name)) {
                continue;
            }
            absentNameList.add(name);
        }

        if (CollectionUtils.isNotEmpty(absentNameList)) {
            throw new BusinessException(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_USB_TYPE_NOT_EXIST,
                    StringUtils.join(absentNameList, COMMA_SEPARATION_CHARACTER));
        }

        return typeMap.values().toArray(new UUID[0]);
    }
}
