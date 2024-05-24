package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.maintenance.module.def.api.BusinessMaintenanceAPI;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbBusinessMaintenanceDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbBusinessMaintenanceImageProgressRequestDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbTaskDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.enums.CbbImageListType;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.GlobalStrategyBussinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ListImageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.BaseMaintenanceStateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.DeskProgressInfo;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.ImageProgressInfo;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.UpgradeImageProgressWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.enums.ImageListType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.maintenance.EnterBusinessMaintenanceWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.maintenance.StopTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response.BusinessMaintenanceDetailWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response.BusinessMaintenanceProgressResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.util.LocalImageTemplatePageRequestCovertUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.webmvc.api.annotation.NoMaintenanceUrl;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年04月08日
 *
 * @author nt
 */
@Controller
@RequestMapping("rco/maintenance")
public class MaintenanceModeCtrl {

    private static final String IMAGE_ROLE_TYPE = "imageRoleType";

    private static final String PLATFORM_ID = "platformId";

    private static final String CBB_IMAGE_TYPE = "cbbImageType";

    private static final String IMAGE_TEMPLATE_STATE = "imageTemplateState";

    private static final String DESK_TYPE = "deskType";

    private static final String IS_DELETE = "isDelete";

    private static final String DESK_STATE = "deskState";

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private BusinessMaintenanceAPI businessMaintenanceAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    /**
     * 获取维护模式信息
     *
     * @param webRequest webRequest
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "info")
    @NoMaintenanceUrl
    @NoAuthUrl
    public DefaultWebResponse getInfo(DefaultWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");

        BaseMaintenanceStateDTO dto = new BaseMaintenanceStateDTO(maintenanceModeMgmtAPI.getMaintenanceMode());

        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * 获取维护模式信息 -- for test
     *
     * @param webRequest webRequest
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "start")
    @NoMaintenanceUrl
    public DefaultWebResponse start(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");

        BaseUpgradeDTO upgradeDto = new BaseUpgradeDTO();
        upgradeDto.setId(UUID.randomUUID());
        upgradeDto.setType(BaseUpgradeDTO.UpgradeType.ONLINE);
        maintenanceModeMgmtAPI.startMaintenanceMode(upgradeDto);

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 获取维护模式信息 -- for test
     *
     * @param webRequest webRequest
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "stop")
    @NoMaintenanceUrl
    public DefaultWebResponse stop(DefaultWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");

        BaseUpgradeDTO upgradeDto = new BaseUpgradeDTO();
        upgradeDto.setId(UUID.randomUUID());
        upgradeDto.setType(BaseUpgradeDTO.UpgradeType.ONLINE);
        maintenanceModeMgmtAPI.quitMaintenanceMode(upgradeDto);

        return DefaultWebResponse.Builder.success();
    }


    /**
     * 进入业务维护模式
     * 
     * @param request request
     * @param sessionContext sessionContext
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "enterBusinessMode")
    @NoBusinessMaintenanceUrl
    public DefaultWebResponse enterBusinessMode(EnterBusinessMaintenanceWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        // admin 账号才有权限操作
        if (!permissionHelper.isAdminName(sessionContext)) {
            throw new BusinessException(GlobalStrategyBussinessKey.RCDC_RCO_NOT_DEFAULT_ADMIN_NOT_ALLOW);
        }
        List<UUID> imageList = ArrayUtils.isEmpty(request.getRecoveryImageArr()) ? new ArrayList<>()
                : Stream.of(request.getRecoveryImageArr()).collect(Collectors.toList());
        CbbBusinessMaintenanceDTO cbbBusinessMaintenanceDTO = new CbbBusinessMaintenanceDTO();
        BeanUtils.copyProperties(request, cbbBusinessMaintenanceDTO);
        cbbBusinessMaintenanceDTO.setRecoveryImageList(imageList);
        cbbBusinessMaintenanceDTO.setCbbImageListType(
                Objects.nonNull(request.getImageListType()) ? CbbImageListType.valueOf(request.getImageListType().toString()) : null);


        businessMaintenanceAPI.enterBusinessMaintenance(cbbBusinessMaintenanceDTO);

        auditLogAPI.recordLog(BusinessKey.RCDC_ENTER_BUSINESS_MAINTENANCE, sessionContext.getUserName());

        return DefaultWebResponse.Builder.success();
    }



    /**
     * 获取表单信息
     * 
     * @param request request
     * @return BusinessMaintenanceDetailWebResponse
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "detail")
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<BusinessMaintenanceDetailWebResponse> getBusinessMaintenanceDetail(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbBusinessMaintenanceDTO businessMaintenanceInfo = businessMaintenanceAPI.getBusinessMaintenanceInfo();
        BusinessMaintenanceDetailWebResponse response = new BusinessMaintenanceDetailWebResponse();
        response.setAfterTipShutdownTime(businessMaintenanceInfo.getAfterTipShutdownTime());
        response.setEnablePublishRecoveryImage(businessMaintenanceInfo.getEnablePublishRecoveryImage());
        response.setRetrySoftCloseCount(businessMaintenanceInfo.getRetrySoftCloseCount());
        response.setTipContent(businessMaintenanceInfo.getTipContent());
        response.setEnableForceShutdown(businessMaintenanceInfo.getEnableForceShutdown());

        if (Objects.nonNull(businessMaintenanceInfo.getCbbImageListType())) {
            response.setImageListType(ImageListType.valueOf(businessMaintenanceInfo.getCbbImageListType().toString()));
        }

        if (!CollectionUtils.isEmpty(businessMaintenanceInfo.getRecoveryImageList())) {
            response.setRecoveryImageArr(formatImageArr(businessMaintenanceInfo.getRecoveryImageList()));
        }
        return CommonWebResponse.success(response);
    }

    private GroupIdLabelEntry[] formatImageArr(List<UUID> imageList) {

        List<CbbImageTemplateDetailDTO> imageTemplateDetailDTOList = imageTemplateMgmtAPI.listAllImageTemplate();
        return imageTemplateDetailDTOList.stream().filter(i -> imageList.contains(i.getId())).map(i -> {
            GroupIdLabelEntry imageLabel = new GroupIdLabelEntry();
            imageLabel.setId(i.getId().toString());
            imageLabel.setLabel(i.getImageName());
            return imageLabel;
        }).collect(Collectors.toList()).toArray(new GroupIdLabelEntry[] {});
    }

    /**
     * 退出业务维护模式
     * 
     * @param request request
     * @param sessionContext sessionContext
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "exitBusinessMaintenance")
    @NoBusinessMaintenanceUrl
    public DefaultWebResponse exitBusinessMaintenance(DefaultWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        // admin 账号才有权限操作
        if (!permissionHelper.isAdminName(sessionContext)) {
            throw new BusinessException(GlobalStrategyBussinessKey.RCDC_RCO_NOT_DEFAULT_ADMIN_NOT_ALLOW);
        }

        businessMaintenanceAPI.quitBusinessMaintenance();

        auditLogAPI.recordLog(BusinessKey.RCDC_EXIT_BUSINESS_MAINTENANCE, sessionContext.getUserName());

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 获取维护模式进度详情
     * 
     * @param request request
     * @return 进度实体
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "getBusinessMaintenanceInfo")
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<BusinessMaintenanceProgressResponse> getBusinessInfo(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        ImageProgressInfo imageProgressInfo = new ImageProgressInfo();

        CloudPlatformDTO defaultCloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
        UUID defaultPlatformId = defaultCloudPlatform.getId();

        // 构建查询全部镜像查询条件
        ConditionQueryRequest conditionQueryRequest = buildAllImageTemplateQueryRequest();
        long defaultPlatformImageTemplateCount = imageTemplateMgmtAPI.countByConditions(conditionQueryRequest);

        imageProgressInfo.setTotal(defaultPlatformImageTemplateCount);

        // 构建属于稳定状态的镜像模板查询条件
        conditionQueryRequest = buildStateImageTemplateQueryRequest();

        long steadyImageNum = imageTemplateMgmtAPI.countByConditions(conditionQueryRequest);
        imageProgressInfo.setSteadyStateImage(steadyImageNum);

        // 构建默认云平台未删除条件查询条件
        conditionQueryRequest = buildNotDeleteVdiPlatformDeskQueryRequest(defaultPlatformId);
        DeskProgressInfo deskProgressInfo = new DeskProgressInfo();
        long defaultPlatformNotDeleteVdiDeskCount = cbbDeskMgmtAPI.countByConditions(conditionQueryRequest);
        deskProgressInfo.setTotal(defaultPlatformNotDeleteVdiDeskCount);

        // 构建默认云平台未删除且属于关机状态条件查询条件
        conditionQueryRequest = buildNotDeleteVdiPlatformClosePlatformDeskQueryRequest(defaultPlatformId);
        long defaultPlatformNotDeleteCloseVdiDeskCount = cbbDeskMgmtAPI.countByConditions(conditionQueryRequest);
        deskProgressInfo.setCloseDeskCount(defaultPlatformNotDeleteCloseVdiDeskCount);

        BusinessMaintenanceProgressResponse response = new BusinessMaintenanceProgressResponse();
        response.setDesk(deskProgressInfo);
        response.setImage(imageProgressInfo);

        CbbTaskDTO[] taskArr = businessMaintenanceAPI.queryTaskList().toArray(new CbbTaskDTO[0]);
        response.setTaskArr(taskArr);
        response.setStatus(businessMaintenanceAPI.getBusinessStatus());

        return CommonWebResponse.success(response);
    }

    /**
     * @param webRequest web请求
     * @return 返回业务升级模式后置处理镜像gt升级进度
     */
    @RequestMapping(value = "getUpgradeImageInfo")
    public DefaultWebResponse getUpgradeImageInfo(UpgradeImageProgressWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");
        CbbBusinessMaintenanceImageProgressRequestDTO requestDTO = new CbbBusinessMaintenanceImageProgressRequestDTO();
        requestDTO.setFrom(webRequest.getFrom());
        return DefaultWebResponse.Builder.success(businessMaintenanceAPI.getImageProgress(requestDTO));
    }


    /**
     * 获取维护模式镜像列表
     *
     * @param webRequest web请求
     * @return 返回镜像列表
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "getImageList")
    public DefaultWebResponse getImageList(ListImageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");

        LocalImageTemplatePageRequest request = LocalImageTemplatePageRequestCovertUtils.covert(webRequest);
        DefaultPageResponse<CbbImageTemplateDetailDTO> cbbImageTemplateDetailDTODefaultPageResponse = businessMaintenanceAPI.getImageList(request);

        return DefaultWebResponse.Builder.success(cbbImageTemplateDetailDTODefaultPageResponse);
    }

    /**
     * 停止任务
     *
     * @param webRequest 停止任务请求
     * @return 停止任务
     */
    @RequestMapping(value = "stopTask")
    @NoBusinessMaintenanceUrl
    public DefaultWebResponse stopTask(StopTaskRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");

        CbbTaskDTO cbbTaskDTO = new CbbTaskDTO();
        cbbTaskDTO.setTaskName(webRequest.getTaskName());
        cbbTaskDTO.setTaskType(webRequest.getTaskType());
        cbbTaskDTO.setTaskId(webRequest.getTaskId());
        cbbTaskDTO.setStopTaskName(webRequest.getStopTaskName());
        cbbTaskDTO.setStopTaskParams(webRequest.getStopTaskParams());
        cbbTaskDTO.setCanStop(webRequest.getCanStop());

        businessMaintenanceAPI.stop(cbbTaskDTO);

        return DefaultWebResponse.Builder.success();
    }


    private static ConditionQueryRequest buildNotDeleteVdiPlatformClosePlatformDeskQueryRequest(UUID defaultPlatformId) {
        ConditionQueryRequest conditionQueryRequest;
        conditionQueryRequest = new DefaultConditionQueryRequestBuilder().eq(DESK_TYPE, CbbCloudDeskType.VDI)
                .eq(IS_DELETE, false)
                .eq(PLATFORM_ID, defaultPlatformId)
                .eq(DESK_STATE, CbbCloudDeskState.CLOSE)
                .build();
        return conditionQueryRequest;
    }

    private static ConditionQueryRequest buildNotDeleteVdiPlatformDeskQueryRequest(UUID defaultPlatformId) {
        ConditionQueryRequest conditionQueryRequest;
        conditionQueryRequest = new DefaultConditionQueryRequestBuilder().eq(DESK_TYPE, CbbCloudDeskType.VDI)
                .eq(IS_DELETE, false)
                .eq(PLATFORM_ID, defaultPlatformId)
                .build();
        return conditionQueryRequest;
    }

    private ConditionQueryRequest buildStateImageTemplateQueryRequest() {
        return new DefaultConditionQueryRequestBuilder().composite(criteriaBuilder -> {

            Match steadyImageTemplateMatch = criteriaBuilder.in(IMAGE_TEMPLATE_STATE, ImageTemplateState.getSteadyStateArr().toArray());
            Match templateTypeMatch = criteriaBuilder.eq(IMAGE_ROLE_TYPE, ImageRoleType.TEMPLATE);

            // VDI类型、指定云平台、处于稳定状态、角色类型是TEMPLATE的镜像模板

            return criteriaBuilder.and(templateTypeMatch, steadyImageTemplateMatch);
        }).build();
    }

    private ConditionQueryRequest buildAllImageTemplateQueryRequest() {
        return new DefaultConditionQueryRequestBuilder().composite(criteriaBuilder -> criteriaBuilder.eq(IMAGE_ROLE_TYPE, ImageRoleType.TEMPLATE)).build();
    }

}
