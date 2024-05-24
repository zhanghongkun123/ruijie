package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTaskAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbAppTestNameDuplicationDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbAppTestTargetDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbTestLogFileInfoDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbDownloadTestLogRequest;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.test.complete.CbbUamCompleteTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DataSourceTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskGtMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppTestAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RequestSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.CompleteDesktopTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.CompleteDesktopTestBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.DeleteAppTestBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.DeleteAppTestDesktopBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.DeleteAppTestDesktopTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.EnterDesktopTestBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.EnterTestDesktopTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.TestTaskRedistributeBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.log.DownloadLogTestWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.test.complete.CompleteDesktopTestWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.test.complete.CompleteTestWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.test.enter.EnterDesktopTestWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response.TestInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.util.SortUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.CheckDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey.RCDC_RCO_APPCENTER_TEST_APP_DISK_NAME_NOT_EXITS;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_ITEM_NAME;

;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月28日
 *
 * @author zhk
 */
@Api(tags = "应用测试")
@Controller
@RequestMapping("/rco/appCenter/appTest")
public class AppTestController {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AppTestController.class);

    @Autowired
    private UamAppTestAPI uamAppTestAPI;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private AppCenterHelper appCenterHelper;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private CbbUamAppTestTargetAPI testTargetAPI;

    @Autowired
    private CbbDeskMgmtAPI deskMgmtAPI;


    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    private static final String SYMBOL_SPOT = "/";

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    public static final String ID = "id";

    @Autowired
    CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    @Autowired
    CbbUamAppTestTaskAPI cbbUamAppTestTaskAPI;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskGtMgmtAPI cbbVDIDeskGtMgmtAPI;

    /**
     * 创建测试任务
     *
     * @param webRequest 请求参数
     * @param sessionContext 请求参数
     * @return web response
     * @throws BusinessException ex
     */
    @ApiOperation("创建测试任务")
    @RequestMapping(value = "/create")
    @EnableAuthority
    public DefaultWebResponse create(CreateAppTestWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        CbbAppTestNameDuplicationDTO cbbAppTestNameDuplicationDTO = new CbbAppTestNameDuplicationDTO(webRequest.getName(), null);
        CheckDuplicationResponse duplicationResponse =
                new CheckDuplicationResponse(cbbUamAppTestAPI.checkAppTestNameDuplication(cbbAppTestNameDuplicationDTO));
        if (duplicationResponse.isHasDuplication()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_APP_TEST_NAME_CHECK_DUPLICATION_FAIL);
        }

        // 当前之前一个应用磁盘
        UUID appDiskId = webRequest.getAppIdArr()[0];
        final AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(appDiskId);
        UUID imageTemplateId = appSoftwarePackageDTO.getImageTemplateId();
        String osVersion = appSoftwarePackageDTO.getOsVersion();
        CbbOsType osType = appSoftwarePackageDTO.getOsType();
        CbbImageType appSoftwarePackageType = appSoftwarePackageDTO.getAppSoftwarePackageType();

        appCenterHelper.checkSoftwarePackage(webRequest.getDeskIdArr(), imageTemplateId, osVersion, osType, appSoftwarePackageType);

        try {
            appCenterHelper.checkTestingDesk(webRequest.getDeskIdArr());
            UamAppTestDTO upmAppTestDTO = new UamAppTestDTO();
            BeanUtils.copyProperties(webRequest, upmAppTestDTO);
            upmAppTestDTO.setImageTemplateId(imageTemplateId);
            upmAppTestDTO.setOsVersion(osVersion);
            upmAppTestDTO.setOsType(osType);
            upmAppTestDTO.setAppSoftwarePackageType(appSoftwarePackageType);
            upmAppTestDTO.setAppSoftwarePackageId(webRequest.getAppIdArr()[0]);

            final UUID testId = uamAppTestAPI.createAppTest(upmAppTestDTO);
            // 保存权限数据
            generalPermissionHelper.savePermission(sessionContext, testId, AdminDataPermissionType.DELIVERY_TEST);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_CREATE_SUCCESS_LOG, upmAppTestDTO.getName());
            return DefaultWebResponse.Builder.success(testId);
        } catch (BusinessException e) {
            LOGGER.error("创建应用测试失败", e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_CREATE_FAIL_LOG, webRequest.getName(), e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 校验用户名是否有重名
     *
     * @param webRequest 页面请求参数
     * @return 返回结果
     */
    @ApiOperation(value = "校验测试名是否有重名")
    @RequestMapping(value = "/checkAppTestNameDuplication")
    public DefaultWebResponse checkAppTestNameDuplication(AppTestNameDuplicationWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest不能为null");

        CbbAppTestNameDuplicationDTO cbbAppTestNameDuplicationDTO = new CbbAppTestNameDuplicationDTO(webRequest.getName(), webRequest.getId());
        CheckDuplicationResponse duplicationResponse =
                new CheckDuplicationResponse(cbbUamAppTestAPI.checkAppTestNameDuplication(cbbAppTestNameDuplicationDTO));
        return DefaultWebResponse.Builder.success(duplicationResponse);
    }


    /**
     * 编辑测试任务
     *
     * @param webRequest 请求参数
     * @param sessionContext 上下文
     * @return web response
     * @throws BusinessException ex
     */
    @ApiOperation("编辑测试任务")
    @RequestMapping(value = "/edit")
    @EnableAuthority
    public DefaultWebResponse edit(EditAppTestWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        generalPermissionHelper.checkPermission(sessionContext, webRequest.getId(), AdminDataPermissionType.DELIVERY_TEST);
        CbbAppTestNameDuplicationDTO cbbAppTestNameDuplicationDTO = new CbbAppTestNameDuplicationDTO(webRequest.getName(), webRequest.getId());
        CheckDuplicationResponse duplicationResponse =
                new CheckDuplicationResponse(cbbUamAppTestAPI.checkAppTestNameDuplication(cbbAppTestNameDuplicationDTO));
        if (duplicationResponse.isHasDuplication()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_APP_TEST_NAME_CHECK_DUPLICATION_FAIL);
        }
        appCenterHelper.validateTestTaskFinished(webRequest.getId());
        CbbUamAppTestDTO upmAppTestDTO = new CbbUamAppTestDTO();
        BeanUtils.copyProperties(webRequest, upmAppTestDTO);
        try {
            cbbUamAppTestAPI.editUamAppTest(upmAppTestDTO);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_EDIT_SUCCESS_LOG, webRequest.getName());
            return DefaultWebResponse.Builder.success(BusinessKey.RCDC_RCO_APPCENTER_TEST_EDIT_SUCCESS_LOG, new String[] {webRequest.getName()});
        } catch (BusinessException e) {
            LOGGER.error("编辑应用测试失败", e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_EDIT_FAIL_LOG, webRequest.getName(), e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 查询测试列表
     *
     * @param request 分页条件
     * @param sessionContext 上下文
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "测试列表")
    @RequestMapping(value = "/list")
    public DefaultWebResponse getAppTestPage(GetDeliveryTestPageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "GetDeliveryTestPageWebRequest不能为空");
        Assert.notNull(sessionContext, "sessionContext不能为空");

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort;
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        } else {
            List<Sort.Order> orderList = new ArrayList<>();
            orderList.add(new Sort.Order(Sort.Direction.DESC, DBConstants.HAS_APP_UPDATE));
            orderList.add(new Sort.Order(Sort.Direction.DESC, DBConstants.CREATE_TIME));
            sort = Sort.by(orderList);
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchDeliveryTestDTO searchDeliveryTestDTO = new SearchDeliveryTestDTO();
        BeanUtils.copyProperties(request, searchDeliveryTestDTO);
        searchDeliveryTestDTO.setName(request.getSearchKeyword());

        generalPermissionHelper.setPermissionParam(sessionContext, searchDeliveryTestDTO);

        DefaultPageResponse<RcoUamAppTestDTO> cbbUamAppTestPageResponse = uamAppTestAPI.pageUamDeliveryTest(searchDeliveryTestDTO, pageable);

        return DefaultWebResponse.Builder.success(cbbUamAppTestPageResponse);
    }


    /**
     * 查询测试信息
     *
     * @param request 查询id
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "查询测试信息")
    @RequestMapping(value = "/getInfo")
    public DefaultWebResponse getAppTestInfo(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request不能为空");
        final CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(request.getId());
        TestInfoResponse response = new TestInfoResponse();
        BeanUtils.copyProperties(uamAppTestInfo, response);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 查询测试桌面列表信息
     *
     * @param pageQueryRequest 分页条件
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "测试桌面列表信息")
    @RequestMapping(value = "/desktop/list")
    public DefaultWebResponse getDesktopPage(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "PageWebRequest不能为空");

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        final PageQueryResponse<AppTestDesktopInfoDTO> queryResponse = uamAppTestAPI.pageQueryAppTestDesktop(builder.build());
        return DefaultWebResponse.Builder.success(queryResponse);
    }

    /**
     * 查询测试应用列表信息
     *
     * @param pageQueryRequest 分页条件
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "测试应用列表详情信息")
    @RequestMapping(value = "/app/list")
    public DefaultWebResponse getAppPage(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "PageWebRequest不能为空");

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        final PageQueryResponse<AppTestApplicationInfoDTO> queryResponse = uamAppTestAPI.pageQueryAppTestApp(builder.build());
        return DefaultWebResponse.Builder.success(queryResponse);
    }

    /**
     * 查询桌面应用列表详情
     *
     * @param pageQueryRequest 分页条件
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "测试桌面应用列表详情信息")
    @RequestMapping(value = "/deskApp/list")
    public DefaultWebResponse deskAppPage(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "PageWebRequest不能为空");

        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        final PageQueryResponse<AppTestDeskAppInfoDTO> queryResponse = uamAppTestAPI.pageQueryAppTestDeskApp(builder.build());
        return DefaultWebResponse.Builder.success(queryResponse);
    }

    /**
     * 删除测试任务
     *
     * @param request 页面请求参数
     * @param builder 批量任务
     * @param sessionContext 请求参数
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "删除测试任务")
    @RequestMapping(value = "/delete")
    @EnableAuthority
    public DefaultWebResponse deleteAppTest(IdArrWebRequest request, SessionContext sessionContext, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "UserIdWebRequest不能为null");
        Assert.notEmpty(request.getIdArr(), "id不能为空");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        Assert.notNull(builder, "BatchTaskBuilder不能为null");

        final List<DefaultBatchTaskItem> taskItemList = Stream.of(request.getIdArr()).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_ITEM_NAME)).build())
                .collect(Collectors.toList());
        DeleteAppTestBatchTaskHandler handler = new DeleteAppTestBatchTaskHandler(taskItemList, sessionContext);
        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_ITEM_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_APPCENTER_TEST_DELETE_TASK_DESC).setUniqueId(request.getIdArr()[0]).enableParallel()
                .registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 新增测试桌面
     *
     * @param webRequest 页面请求参数
     * @param sessionContext 上下文
     * @return 结果集
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "新增测试桌面")
    @RequestMapping(value = "/addDesktop")
    @EnableAuthority
    public DefaultWebResponse addAppTestDesktop(AppTestDesktopWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest不能为null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        appCenterHelper.validateTestTaskPausingAndFinished(webRequest.getTestId());
        // 校验权限
        generalPermissionHelper.checkPermission(sessionContext, webRequest.getTestId(), AdminDataPermissionType.DELIVERY_TEST);
        // 校验云桌面是否满足添加条件
        List<UUID> deskIdList = new ArrayList<>();
        for (UUID deskId : webRequest.getDeskIdArr()) {
            appCenterHelper.testDeskCheck(webRequest.getTestId(), deskId);
            deskIdList.add(deskId);
        }
        uamAppTestAPI.addAppTestDesk(webRequest.getTestId(), deskIdList);
        return DefaultWebResponse.Builder.success(BusinessKey.RCDC_RCO_APPCENTER_TEST_ADD_DESKTOP_SUCCESS, new String[] {});
    }

    /**
     * 删除测试桌面
     *
     * @param webRequest 页面请求参数
     * @param sessionContext 上下文
     * @param builder 批任务
     * @return 结果集
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "删除测试桌面")
    @RequestMapping(value = "/desktop/delete")
    @EnableAuthority
    public DefaultWebResponse deleteAppTestDesktop(AppTestDesktopWebRequest webRequest, SessionContext sessionContext, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest不能为null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        Assert.notNull(builder, "builder must not be null");

        // 校验权限
        generalPermissionHelper.checkPermission(sessionContext, webRequest.getTestId(), AdminDataPermissionType.DELIVERY_TEST);
        // 批量删除测试桌面
        final List<DeleteAppTestDesktopTaskItem> appTestDesktopTaskItemList = Arrays.stream(webRequest.getDeskIdArr())
                .map(id -> DeleteAppTestDesktopTaskItem.builder().itemId(id).testId(webRequest.getTestId())
                        .itemName(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_ITEM_NAME, new String[] {}).build())
                .collect(Collectors.toList());
        DeleteAppTestDesktopBatchTaskHandler handler = new DeleteAppTestDesktopBatchTaskHandler(appTestDesktopTaskItemList);
        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_ITEM_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_DELETE_TASK_DESC).setUniqueId(webRequest.getTestId()).enableParallel()
                .registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * 完成测试任务
     *
     * @param webRequest 页面请求参数
     * @param sessionContext 上下文
     * @param builder 批任务
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "完成测试任务")
    @RequestMapping(value = "/complete")
    @EnableAuthority
    public DefaultWebResponse completeTest(CompleteTestWebRequest webRequest, SessionContext sessionContext, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest不能为null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID testId = webRequest.getTestId();
        CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(testId);
        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, testId, AdminDataPermissionType.DELIVERY_TEST);
        appCenterHelper.validateTestTaskFinished(webRequest.getTestId());
        List<CbbAppTestTargetDTO> testTargetList = testTargetAPI.findByTestIdAndStateIn(testId, DesktopTestStateEnum.getProcessingStateList());
        // 空的则表明没有测试中的云桌面，
        if (CollectionUtils.isEmpty(testTargetList)) {
            completeTest(webRequest);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_APPCENTER_TEST_TASK_SUCCESS, uamAppTestInfo.getName());
            return DefaultWebResponse.Builder.success(BusinessKey.RCDC_RCO_APPCENTER_TEST_TASK_SUCCESS, new String[] {uamAppTestInfo.getName()});
        }

        List<CompleteDesktopTaskItem> completeItemList = new ArrayList<>();
        for (CbbAppTestTargetDTO testTargetDTO : testTargetList) {
            final CompleteDesktopTaskItem completeItem = CompleteDesktopTaskItem.builder().itemId(UUID.randomUUID())
                    .itemName(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_TEST_ITEM_NAME)).state(webRequest.getState())
                    .desktopId(testTargetDTO.getResourceId()).testId(testId).build();
            completeItemList.add(completeItem);
        }
        CompleteTestTaskDTO completeTestTaskDTO = new CompleteTestTaskDTO();
        BeanUtils.copyProperties(webRequest, completeTestTaskDTO);
        completeTestTaskDTO.setBatchDesktop(true);
        CompleteDesktopTestBatchTaskHandler handler = new CompleteDesktopTestBatchTaskHandler(completeItemList, completeTestTaskDTO);
        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_TEST_ITEM_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_TEST_TASK_DESC).setUniqueId(testId).enableParallel()
                .registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    private void completeTest(CompleteTestWebRequest webRequest) throws BusinessException {
        CbbUamCompleteTestDTO request = new CbbUamCompleteTestDTO();
        request.setState(webRequest.getState());
        request.setTestId(webRequest.getTestId());
        request.setReason(webRequest.getReason());
        cbbUamAppTestAPI.completeTest(request);
    }


    /**
     * 进入测试任务
     *
     * @param webRequest 页面请求参数
     * @param sessionContext 上下文
     * @param builder 批任务
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "开始测试任务")
    @RequestMapping(value = "/enter")
    @EnableAuthority
    public DefaultWebResponse enterTest(EnterDesktopTestWebRequest webRequest, SessionContext sessionContext, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest不能为null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID testId = webRequest.getTestId();
        appCenterHelper.validateTestTaskPausingAndFinished(webRequest.getTestId());
        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, testId, AdminDataPermissionType.DELIVERY_TEST);
        List<EnterTestDesktopTaskItem> taskItemList = new ArrayList<>();
        for (UUID desktopId : webRequest.getDeskIdArr()) {
            final RcoUserDesktopDTO rcoUserDesktopDTO = userDesktopMgmtAPI.findByDesktopId(desktopId);
            final EnterTestDesktopTaskItem testDesktopTaskItem =
                    EnterTestDesktopTaskItem.builder().adminId(sessionContext.getUserId()).itemId(UUID.randomUUID()).testId(testId)
                            .desktopId(desktopId).itemName(LocaleI18nResolver
                                    .resolve(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_ITEM_NAME, rcoUserDesktopDTO.getDesktopName()))
                            .build();
            taskItemList.add(testDesktopTaskItem);
        }
        EnterDesktopTestBatchTaskHandler handler = new EnterDesktopTestBatchTaskHandler(taskItemList);
        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_TEST_DESKTOP_BATCH_ITEM_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_APPCENTER_TEST_ENTER_TEST_DESKTOP_TASK_DESC).enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }


    /**
     * 完成单桌面 测试任务
     *
     * @param webRequest 页面请求参数
     * @param sessionContext 上下文
     * @param builder 批任务
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "完成单桌面测试任务")
    @RequestMapping(value = "/desktop/complete")
    @EnableAuthority
    public DefaultWebResponse completeDesktopTest(CompleteDesktopTestWebRequest webRequest, SessionContext sessionContext, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest不能为null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID testId = webRequest.getTestId();
        cbbUamAppTestAPI.getUamAppTestInfo(testId);
        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, testId, AdminDataPermissionType.DELIVERY_TEST);

        List<CompleteDesktopTaskItem> taskItemList = new ArrayList<>();
        for (UUID desktopId : webRequest.getDeskIdArr()) {
            final RcoUserDesktopDTO rcoUserDesktopDTO = userDesktopMgmtAPI.findByDesktopId(desktopId);
            final CompleteDesktopTaskItem desktopTaskItem = CompleteDesktopTaskItem
                    .builder().itemId(UUID.randomUUID()).itemName(LocaleI18nResolver
                            .resolve(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_DESKTOP_ITEM_NAME, rcoUserDesktopDTO.getDesktopName()))
                    .state(webRequest.getState()).desktopId(desktopId).testId(testId).build();
            taskItemList.add(desktopTaskItem);
        }
        CompleteTestTaskDTO completeTestTaskDTO = new CompleteTestTaskDTO();
        completeTestTaskDTO.setBatchDesktop(false);
        completeTestTaskDTO.setReason(webRequest.getReason());
        CompleteDesktopTestBatchTaskHandler handler = new CompleteDesktopTestBatchTaskHandler(taskItemList, completeTestTaskDTO);
        BatchTaskSubmitResult result = builder.setTaskName(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_TEST_DESKTOP_BATCH_ITEM_NAME)
                .setTaskDesc(BusinessKey.RCDC_RCO_APPCENTER_TEST_COMPLETE_DESKTOP_TASK_DESC).setUniqueId(webRequest.getDeskIdArr()[0])
                .enableParallel().registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 下载失败日志
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     * @throws IOException io异常
     */
    @RequestMapping(value = "/downloadLog", method = RequestMethod.GET)
    @EnableAuthority
    public DownloadWebResponse downloadLog(DownloadLogTestWebRequest request) throws BusinessException, IOException {
        Assert.notNull(request, "request不能为null");

        CbbDownloadTestLogRequest downloadTestLogRequest = new CbbDownloadTestLogRequest();
        downloadTestLogRequest.setDesktopId(request.getDesktopId());
        downloadTestLogRequest.setTestId(request.getTestId());
        CbbTestLogFileInfoDTO response = testTargetAPI.queryFailLog(downloadTestLogRequest);

        String filePath = response.getLogFilePath();
        final File file = new File(filePath);
        if (StringUtils.isEmpty(filePath) || !file.exists()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_LOG_NOT_EXIST);
        }
        final String fileNameWithoutPrefix = filePath.substring(filePath.lastIndexOf(SYMBOL_SPOT) + 1);
        String logNameWithoutSuffix = fileNameWithoutPrefix.substring(0, fileNameWithoutPrefix.lastIndexOf('.'));
        String suffix = filePath.substring(filePath.lastIndexOf('.') + 1);
        return new DownloadWebResponse.Builder().setContentType("application/octet-stream").setName(logNameWithoutSuffix, suffix).setFile(file)
                .build();
    }

    /**
     * 收集日志
     *
     * @param request 请求参数
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("请求收集GT日志")
    @RequestMapping(value = "/collect/log")
    @EnableAuthority
    public DefaultWebResponse collectLog(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        cbbUamAppTestTaskAPI.collectAppTestLog(request.getId());
        return DefaultWebResponse.Builder.success();
    }

    /**
     * 用户组桌面（个性）
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("用户组桌面（个性）")
    @RequestMapping(value = "/userGroup/desktop/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserGroupDesktopRelatedDTO>> pageUserGroupGroupDesktop(GroupDesktopPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID userGroupId = request.getGroupId();
        if (Objects.nonNull(userGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, userGroupId, AdminDataPermissionType.USER_GROUP);
        }
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }
        SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO = new SearchGroupDesktopRelatedDTO();
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);
        searchGroupDesktopRelatedDTO.setSearchName(request.getSearchKeyword());
        searchGroupDesktopRelatedDTO.setGroupId(userGroupId);
        searchGroupDesktopRelatedDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_TEST);
        searchGroupDesktopRelatedDTO.setDeskStateList(request.getDeskStateList());
        searchGroupDesktopRelatedDTO.setPlatformStatusList(request.getPlatformStatusList());

        UUID currentTaskId = request.getCurrentTaskId();
        if (Objects.nonNull(currentTaskId)) {
            CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(currentTaskId);
            searchGroupDesktopRelatedDTO.setOsType(uamAppTestInfo.getOsType());
            searchGroupDesktopRelatedDTO.setOsVersion(uamAppTestInfo.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(uamAppTestInfo.getAppSoftwarePackageType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(uamAppTestInfo.getImageTemplateId());
            searchGroupDesktopRelatedDTO.setFilterGroupId(currentTaskId);
        } else {
            searchGroupDesktopRelatedDTO.setOsType(request.getOsType());
            searchGroupDesktopRelatedDTO.setCbbImageType(request.getCbbImageType());
            searchGroupDesktopRelatedDTO.setOsVersion(request.getOsVersion());
            searchGroupDesktopRelatedDTO.setImageTemplateId(request.getImageTemplateId());
        }

        // 测试任务只有 应用磁盘
        searchGroupDesktopRelatedDTO.setAppDeliveryType(request.getAppDeliveryType());

        generalPermissionHelper.setPermissionParam(sessionContext, searchGroupDesktopRelatedDTO);

        DefaultPageResponse<UserGroupDesktopRelatedDTO> userGroupDesktopRelatedPageResponse =
                appDeliveryMgmtAPI.pageUserGroupDesktopRelated(searchGroupDesktopRelatedDTO, pageable);
        return CommonWebResponse.success(userGroupDesktopRelatedPageResponse);
    }


    /**
     * 应用测试-批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用测试-批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量")
    @RequestMapping(value = "/userGroup/desktopCount/list", method = RequestMethod.POST)
    public CommonWebResponse<List<UserGroupDesktopCountDTO>> listUserGroupDesktopCount(GetGroupDesktopCountRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        GetGroupDesktopCountDTO getGroupDesktopCountDTO = new GetGroupDesktopCountDTO();
        getGroupDesktopCountDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_TEST);

        UUID currentTaskId = request.getCurrentTaskId();
        if (Objects.nonNull(currentTaskId)) {
            CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(currentTaskId);
            getGroupDesktopCountDTO.setOsType(uamAppTestInfo.getOsType());
            getGroupDesktopCountDTO.setOsVersion(uamAppTestInfo.getOsVersion());
            getGroupDesktopCountDTO.setCbbImageType(uamAppTestInfo.getAppSoftwarePackageType());
            getGroupDesktopCountDTO.setImageTemplateId(uamAppTestInfo.getImageTemplateId());
            getGroupDesktopCountDTO.setFilterGroupId(currentTaskId);
        } else {
            getGroupDesktopCountDTO.setOsType(request.getOsType());
            getGroupDesktopCountDTO.setOsVersion(request.getOsVersion());
            getGroupDesktopCountDTO.setCbbImageType(request.getCbbImageType());
            getGroupDesktopCountDTO.setImageTemplateId(request.getImageTemplateId());
        }

        getGroupDesktopCountDTO.setAppDeliveryType(request.getAppDeliveryType());

        generalPermissionHelper.setPermissionParam(sessionContext, getGroupDesktopCountDTO);

        List<UserGroupDesktopCountDTO> userGroupDesktopCountList = appDeliveryMgmtAPI.listUserGroupDesktopCount(getGroupDesktopCountDTO);

        return CommonWebResponse.success(userGroupDesktopCountList);
    }


    /**
     * 用户组所有符合可选的桌面列表（个性）
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("用户组所有符合可选的桌面列表（个性）")
    @RequestMapping(value = "/userGroup/selectDesktop/list", method = RequestMethod.POST)
    public CommonWebResponse<List<UserGroupDesktopRelatedDTO>> listUserGroupDesktop(SelectGroupDesktopRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID userGroupId = request.getGroupId();
        // 用户组id为空，则认为是选择了根【总览】
        List<UUID> userGroupIdList = new ArrayList<>();
        if (Objects.nonNull(userGroupId)) {
            // loadById 用于判定id是否存在
            IacUserGroupDetailDTO userGroupDetail = cbbUserGroupAPI.getUserGroupDetail(userGroupId);
            // 递归获取该节点含所有子节点id
            userGroupIdList = cbbUserGroupAPI.listByGroupIdRecursive(userGroupDetail.getId());
        }

        // 如果非超管，则需要跟该用户数据权限进行过滤
        boolean isAllPermission = generalPermissionHelper.isAllPermission(sessionContext);
        if (!isAllPermission) {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            List<UUID> uuidList = generalPermissionHelper.listByPermissionType(baseAdminDTO.getId(), AdminDataPermissionType.USER_GROUP);
            // 需要数据权限
            if (CollectionUtils.isEmpty(uuidList)) {
                // 该用户没有获取该数据权限，直接返回空集合
                LOGGER.warn("该用户[{}]没有任何用户组数据权限！", baseAdminDTO.getUserName());
                return CommonWebResponse.success();
            }

            // 选择组id，获取交集
            if (Objects.nonNull(userGroupId)) {
                userGroupIdList.retainAll(uuidList);
            } else {
                // 选择了根【总览】，则获取该用户的用户组即为他所有的数据权限
                userGroupIdList.addAll(uuidList);
            }
        }


        SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO = new SearchGroupDesktopRelatedDTO();

        UUID currentTaskId = request.getCurrentTaskId();
        if (Objects.nonNull(currentTaskId)) {
            CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(currentTaskId);
            searchGroupDesktopRelatedDTO.setOsType(uamAppTestInfo.getOsType());
            searchGroupDesktopRelatedDTO.setOsVersion(uamAppTestInfo.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(uamAppTestInfo.getAppSoftwarePackageType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(uamAppTestInfo.getImageTemplateId());
            searchGroupDesktopRelatedDTO.setFilterGroupId(currentTaskId);
        } else {
            searchGroupDesktopRelatedDTO.setOsType(request.getOsType());
            searchGroupDesktopRelatedDTO.setOsVersion(request.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(request.getCbbImageType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(request.getImageTemplateId());
        }

        searchGroupDesktopRelatedDTO.setGroupIdList(userGroupIdList);
        searchGroupDesktopRelatedDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_TEST);
        searchGroupDesktopRelatedDTO.setAppDeliveryType(request.getAppDeliveryType());

        List<UserGroupDesktopRelatedDTO> userGroupDesktopRelatedList = appDeliveryMgmtAPI.listUserGroupDesktopRelated(searchGroupDesktopRelatedDTO);

        return CommonWebResponse.success(userGroupDesktopRelatedList);
    }

    /**
     * 终端组桌面（个性）
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("终端组桌面（个性）")
    @RequestMapping(value = "/terminalGroup/desktop/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<TerminalGroupDesktopRelatedDTO>> pageTerminalGroupDesktop(GroupDesktopPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID terminalGroupId = request.getGroupId();
        if (Objects.nonNull(terminalGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, terminalGroupId, AdminDataPermissionType.TERMINAL_GROUP);
        }
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }
        SearchGroupDesktopRelatedDTO searchRequest = new SearchGroupDesktopRelatedDTO();
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);
        searchRequest.setSearchName(request.getSearchKeyword());
        searchRequest.setCbbImageType(request.getCbbImageType());
        searchRequest.setOsType(request.getOsType());
        searchRequest.setDataSourceType(DataSourceTypeEnum.DELIVERY_TEST);
        searchRequest.setDeskStateList(request.getDeskStateList());
        searchRequest.setGroupId(terminalGroupId);

        UUID currentTaskId = request.getCurrentTaskId();
        if (Objects.nonNull(currentTaskId)) {
            CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(currentTaskId);
            searchRequest.setOsType(uamAppTestInfo.getOsType());
            searchRequest.setOsVersion(uamAppTestInfo.getOsVersion());
            searchRequest.setCbbImageType(uamAppTestInfo.getAppSoftwarePackageType());
            searchRequest.setImageTemplateId(uamAppTestInfo.getImageTemplateId());
            searchRequest.setFilterGroupId(currentTaskId);
        } else {
            searchRequest.setOsType(request.getOsType());
            searchRequest.setOsVersion(request.getOsVersion());
            searchRequest.setCbbImageType(request.getCbbImageType());
            searchRequest.setImageTemplateId(request.getImageTemplateId());
        }

        searchRequest.setAppDeliveryType(request.getAppDeliveryType());
        searchRequest.setPlatformStatusList(request.getPlatformStatusList());

        generalPermissionHelper.setPermissionParam(sessionContext, searchRequest);

        DefaultPageResponse<TerminalGroupDesktopRelatedDTO> terminalGroupDesktopRelatedPageResponse =
                appDeliveryMgmtAPI.pageTerminalGroupDesktopRelated(searchRequest, pageable);

        return CommonWebResponse.success(terminalGroupDesktopRelatedPageResponse);
    }


    /**
     * 应用测试-批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用测试-批量添加云桌面--终端组--获所有终端组的云桌面数量")
    @RequestMapping(value = "/terminalGroup/desktopCount/list", method = RequestMethod.POST)
    public CommonWebResponse<List<TerminalGroupDesktopCountDTO>> listTerminalGroupDesktopCount(GetGroupDesktopCountRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        GetGroupDesktopCountDTO getGroupDesktopCountDTO = new GetGroupDesktopCountDTO();
        getGroupDesktopCountDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_TEST);

        UUID deliveryGroupId = request.getCurrentTaskId();
        if (Objects.nonNull(deliveryGroupId)) {
            CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(deliveryGroupId);
            getGroupDesktopCountDTO.setOsType(uamAppTestInfo.getOsType());
            getGroupDesktopCountDTO.setOsVersion(uamAppTestInfo.getOsVersion());
            getGroupDesktopCountDTO.setCbbImageType(uamAppTestInfo.getAppSoftwarePackageType());
            getGroupDesktopCountDTO.setImageTemplateId(uamAppTestInfo.getImageTemplateId());
            getGroupDesktopCountDTO.setFilterGroupId(deliveryGroupId);
        } else {
            CbbOsType osType = request.getOsType();
            CbbImageType cbbImageType = request.getCbbImageType();
            getGroupDesktopCountDTO.setOsType(osType);
            getGroupDesktopCountDTO.setOsVersion(request.getOsVersion());
            getGroupDesktopCountDTO.setCbbImageType(cbbImageType);
            getGroupDesktopCountDTO.setImageTemplateId(request.getImageTemplateId());
        }

        getGroupDesktopCountDTO.setAppDeliveryType(request.getAppDeliveryType());

        generalPermissionHelper.setPermissionParam(sessionContext, getGroupDesktopCountDTO);
        List<TerminalGroupDesktopCountDTO> userGroupDesktopCountList = appDeliveryMgmtAPI.listTerminalGroupDesktopCount(getGroupDesktopCountDTO);

        return CommonWebResponse.success(userGroupDesktopCountList);
    }


    /**
     * 终端组桌面（个性）
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("选择终端组获取所有可选桌面（个性）")
    @RequestMapping(value = "/terminalGroup/selectDesktop/list", method = RequestMethod.POST)
    public CommonWebResponse<List<TerminalGroupDesktopRelatedDTO>> listTerminalGroupDesktop(SelectGroupDesktopRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID terminalGroupId = request.getGroupId();
        // 终端组id为空，则认为是选择了根【总览】
        List<UUID> terminalGroupIdList = new ArrayList<>();
        if (Objects.nonNull(terminalGroupId)) {
            // loadById 用于判定id是否存在
            CbbTerminalGroupDetailDTO cbbTerminalGroupDetailDTO = cbbTerminalGroupMgmtAPI.loadById(terminalGroupId);
            // 递归获取该节点含所有子节点id
            terminalGroupIdList = cbbTerminalGroupMgmtAPI.listByGroupIdRecursive(cbbTerminalGroupDetailDTO.getId());
        }

        // 如果非超管，则需要跟该用户数据权限进行过滤
        boolean isAllPermission = generalPermissionHelper.isAllPermission(sessionContext);
        if (!isAllPermission) {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            List<UUID> uuidList = generalPermissionHelper.listByPermissionType(baseAdminDTO.getId(), AdminDataPermissionType.TERMINAL_GROUP);
            // 需要数据权限
            if (CollectionUtils.isEmpty(uuidList)) {
                // 该用户没有获取该数据权限，直接返回空集合
                LOGGER.warn("该用户[{}]没有任何终端组数据权限！", baseAdminDTO.getUserName());
                return CommonWebResponse.success();
            }

            // 选择组id，获取交集
            if (Objects.nonNull(terminalGroupId)) {
                terminalGroupIdList.retainAll(uuidList);
            } else {
                // 选择了根【总览】，则获取该用户的终端组即为他所有的数据权限
                terminalGroupIdList.addAll(uuidList);
            }
        }


        SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO = new SearchGroupDesktopRelatedDTO();

        UUID currentTaskId = request.getCurrentTaskId();
        if (Objects.nonNull(currentTaskId)) {
            CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(currentTaskId);
            searchGroupDesktopRelatedDTO.setOsType(uamAppTestInfo.getOsType());
            searchGroupDesktopRelatedDTO.setOsVersion(uamAppTestInfo.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(uamAppTestInfo.getAppSoftwarePackageType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(uamAppTestInfo.getImageTemplateId());
            searchGroupDesktopRelatedDTO.setFilterGroupId(currentTaskId);
        } else {
            searchGroupDesktopRelatedDTO.setOsType(request.getOsType());
            searchGroupDesktopRelatedDTO.setOsVersion(request.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(request.getCbbImageType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(request.getImageTemplateId());
        }

        searchGroupDesktopRelatedDTO.setGroupIdList(terminalGroupIdList);
        searchGroupDesktopRelatedDTO.setFilterGroupId(request.getCurrentTaskId());
        searchGroupDesktopRelatedDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_TEST);
        searchGroupDesktopRelatedDTO.setAppDeliveryType(request.getAppDeliveryType());

        List<TerminalGroupDesktopRelatedDTO> terminalGroupDesktopRelatedList =
                appDeliveryMgmtAPI.listTerminalGroupDesktopRelated(searchGroupDesktopRelatedDTO);

        return CommonWebResponse.success(terminalGroupDesktopRelatedList);
    }

    /**
     * 添加交付应用-点击弹出交付应用列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("添加测试应用-点击弹出应用磁盘列表")
    @RequestMapping(value = "/testApp/selectAppDisk/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamAppDiskDTO>> pageAppDisk(GetTestAppDiskPageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有编辑该记录权限
        UUID testId = request.getTestId();
        if (Objects.nonNull(testId)) {
            generalPermissionHelper.checkPermission(sessionContext, testId, AdminDataPermissionType.DELIVERY_TEST);
        }

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.UPDATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);
        SearchAppDiskDTO searchAppDiskDTO = new SearchAppDiskDTO();
        BeanUtils.copyProperties(request, searchAppDiskDTO);
        searchAppDiskDTO.setAppName(request.getSearchKeyword());
        searchAppDiskDTO.setFilterGroupId(request.getTestId());
        searchAppDiskDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_TEST);
        searchAppDiskDTO.setRequestSource(RequestSourceEnum.DELIVERY_TEST);

        generalPermissionHelper.setPermissionParam(sessionContext, searchAppDiskDTO);

        DefaultPageResponse<UamAppDiskDTO> uamAppPageResponse = appDeliveryMgmtAPI.pageAppDisk(searchAppDiskDTO, pageable);
        return CommonWebResponse.success(uamAppPageResponse);
    }

    /**
     * 应用测试重新交付
     *
     * @param request request
     * @param builder builder
     * @param sessionContext sessionContext
     * @return CommonWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用测试-重新交付")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0)})
    @RequestMapping(value = "/redistribute/appDisk", method = RequestMethod.POST)
    public CommonWebResponse<BatchTaskSubmitResult> redistribute(IdWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有编辑该记录权限
        UUID testId = request.getId();
        generalPermissionHelper.checkPermission(sessionContext, testId, AdminDataPermissionType.DELIVERY_TEST);

        List<CbbAppTestTargetDTO> cbbAppTestTargetDTOList =
                Optional.ofNullable(cbbUamAppTestTargetAPI.findByTestId(request.getId())).orElse(Collections.emptyList());
        cbbAppTestTargetDTOList = cbbAppTestTargetDTOList.stream()
                .filter(cbbAppTestTargetDTO -> cbbAppTestTargetDTO.getState() == DesktopTestStateEnum.TESTING).collect(Collectors.toList());
        CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(testId);
        if (uamAppTestInfo.getAppDisk() == null) {
            throw new BusinessException(RCDC_RCO_APPCENTER_TEST_APP_DISK_NAME_NOT_EXITS, uamAppTestInfo.getName());
        }
        if (ObjectUtils.isEmpty(cbbAppTestTargetDTOList)) {
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_TASK_REDISTRIBUTE_SUCCESS_LOG, uamAppTestInfo.getAppDisk());
            return CommonWebResponse.success(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_TASK_REDISTRIBUTE_SUCCESS_LOG,
                    new String[] {uamAppTestInfo.getAppDisk()});
        }


        List<EnterTestDesktopTaskItem> enterTestDesktopTaskItemList = getEnterTestDesktopTaskItemList(sessionContext, cbbAppTestTargetDTOList);

        TestTaskRedistributeBatchTaskHandler testTaskRedistributeBatchTaskHandler =
                applicationContext.getBean(TestTaskRedistributeBatchTaskHandler.class, enterTestDesktopTaskItemList, uamAppTestInfo);


        BatchTaskSubmitResult result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_TASK_NAME) //
                .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_UAM_APP_TEST_REDISTRIBUTE_TASK_DESC) //
                .enableParallel().registerHandler(testTaskRedistributeBatchTaskHandler) //
                .start();

        return CommonWebResponse.success(result);
    }

    private List<EnterTestDesktopTaskItem> getEnterTestDesktopTaskItemList(SessionContext sessionContext,
            List<CbbAppTestTargetDTO> cbbAppTestTargetDTOList) {
        List<EnterTestDesktopTaskItem> enterTestDesktopTaskItemList = new ArrayList<>();
        for (CbbAppTestTargetDTO cbbAppTestTargetDTO : cbbAppTestTargetDTOList) {
            final EnterTestDesktopTaskItem testDesktopTaskItem = EnterTestDesktopTaskItem.builder().adminId(sessionContext.getUserId()) //
                    .itemId(cbbAppTestTargetDTO.getResourceId()) //
                    .testId(cbbAppTestTargetDTO.getTestId()) //
                    .desktopId(cbbAppTestTargetDTO.getResourceId()) //
                    .itemName(LocaleI18nResolver.resolve(RCDC_UAM_APP_TEST_REDISTRIBUTE_ITEM_NAME)) //
                    .build();
            enterTestDesktopTaskItemList.add(testDesktopTaskItem);
        }
        return enterTestDesktopTaskItemList;
    }

}
