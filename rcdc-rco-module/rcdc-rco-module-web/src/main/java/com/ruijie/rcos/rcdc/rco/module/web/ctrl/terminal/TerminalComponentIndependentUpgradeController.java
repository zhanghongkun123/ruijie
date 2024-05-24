package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.TerminalConstants.DEFAULT_TERMINAL_GROUP_UUID;
import static com.ruijie.rcos.rcdc.rco.module.web.Constants.*;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.RcoBusinessKey.RCDC_RCO_PARAM_EXCEED_LIMITS;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_HAS_EMPTY_VALUE;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_IS_INVALID;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONException;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalComponentIndependentUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.TerminalIdMappingUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.AddComponentIndependentUpgradeTerminalBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.CancelComponentIndependentUpgradeTerminalBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DeleteComponentIndependentUpgradePackageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.DeleteSingleComponentIndependentUpgradePackageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.RetryComponentIndependentUpgradeTerminalBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.TerminalUpgradeBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.UploadIndependentUpgradePackageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.CheckAllowUploadUpgradePackageWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.PackComponentsWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.TerminalUpgradeItemArrayWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.UpgradeComponentsWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.CheckAllowUploadContentVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.ComponentIndependentUpgradePackageTaskDetailVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.ComponentIndependentUpgradeTaskDetailContentVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.CreateSystemUpgradeTaskContentVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.PackageComponentVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.UpgradeComponentVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.UpgradeTerminalDetailVO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradeAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeTaskStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.BetweenTimeRangeMatch;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.MatchEqual;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiOperation;

/**
 * Description: 终端组件独立升级
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/14
 *
 * @author lyb
 */
@Controller
@RequestMapping("/rco/terminal/component/independent/upgrade")
@EnableCustomValidate(enable = false)
public class TerminalComponentIndependentUpgradeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalComponentIndependentUpgradeController.class);

    @Autowired
    private CbbTerminalComponentIndependentUpgradeAPI componentIndependentUpgradeAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private TerminalComponentIndependentUpgradeAPI terminalUpgradeAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    private static final String[] PARAM_PRODUCT_TYPE_31XX = {"RG-CT3120", "RG-CT3100-G2", "RG-CT3100C-G2", "RG-CT3100L-G2"};

    /**
     * 上传系统升级文件
     *
     * @param file 上传文件
     * @param taskBuilder 异步任务构造对象
     * @return 上传响应返回
     * @throws BusinessException 业务异常
     */
    @ApiOperation("上传升级包")
    @RequestMapping(value = "/package/create")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse uploadPackage(ChunkUploadFile file, BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(file, "file can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");
        Assert.notNull(file.getCustomData(), "file.getCustomData() can not be null");
        CbbTerminalTypeEnums terminalType;
        try {
            terminalType = file.getCustomData().getObject(TERMINAL_TYPE, CbbTerminalTypeEnums.class);
        } catch (JSONException e) {
            throw new BusinessException(RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_IS_INVALID, e, "terminalType");
        }

        if (Objects.isNull(terminalType)) {
            throw new BusinessException(RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_IS_INVALID, "terminalType");
        }
        String osType = terminalType.getOsType();
        String osPlatform = getSpecialOsPlatform(terminalType);
        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem
                .builder().itemId(UUID.randomUUID()).itemName(LocaleI18nResolver
                        .resolve(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_ITEM_NAME, osType, osPlatform))
                .build();
        UploadIndependentUpgradePackageBatchTaskHandler upgradePackageHandler =
                new UploadIndependentUpgradePackageBatchTaskHandler(file, taskItem, auditLogAPI, componentIndependentUpgradePackageAPI);
        upgradePackageHandler.setTerminalType(terminalType);

        BatchTaskSubmitResult result =
                taskBuilder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_NAME, osType, osPlatform)
                        .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_DESC, osType, osPlatform,
                                file.getFileName())
                        .registerHandler(upgradePackageHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 校验升级包是否允许上传
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("校验升级包是否允许上传")
    @RequestMapping(value = "/package/checkAllowUpload")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public CheckAllowUploadUpgradePackageWebResponse checkAllowUploadPackage(CheckUpgradePackageAllowUploadWebRequest request)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbCheckAllowUploadPackageDTO checkRequest = new CbbCheckAllowUploadPackageDTO(request.getFileSize());
        checkRequest.setTerminalType(request.getTerminalType());
        checkRequest.setFileName(request.getFileName());
        CbbCheckAllowUploadPackageResultDTO response = componentIndependentUpgradePackageAPI.checkAllowUploadPackage(checkRequest);

        CheckAllowUploadUpgradePackageWebResponse webResponse = new CheckAllowUploadUpgradePackageWebResponse();
        webResponse.setStatus(Status.SUCCESS);
        if (response.getAllowUpload()) {
            return buildSuccessResponse(webResponse);
        }

        return buildErrorResponse(response, webResponse);
    }

    private CheckAllowUploadUpgradePackageWebResponse buildSuccessResponse(CheckAllowUploadUpgradePackageWebResponse webResponse) {
        webResponse.setContent(new CheckAllowUploadContentVO(false, null));
        return webResponse;
    }

    private CheckAllowUploadUpgradePackageWebResponse buildErrorResponse(CbbCheckAllowUploadPackageResultDTO response,
            CheckAllowUploadUpgradePackageWebResponse webResponse) {
        final List<String> errorList = response.getErrorList();
        String errorMsg = "";
        if (!CollectionUtils.isEmpty(errorList)) {
            errorMsg = LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_NOT_ALLOWED)
                    + StringUtils.join(errorList, ERROR_MSG_SPERATOR);
        }
        webResponse.setContent(new CheckAllowUploadContentVO(true, errorMsg));
        return webResponse;
    }

    /**
     * 删除升级包
     *
     * @param request 请求参数
     * @param builder 批任务对象
     * @return 上传响应返回
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除升级包")
    @RequestMapping(value = "/package/delete")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse deletePackage(DeleteTerminalUpgradePackageWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        final UUID[] packageIdArr = request.getIdArr();

        if (packageIdArr.length == 1) {
            return deleteSingleUpgradePackage(packageIdArr[0], builder);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator =
                    Stream.of(packageIdArr)
                            .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                    .itemName(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_ITEM_NAME).build())
                            .iterator();
            DeleteComponentIndependentUpgradePackageBatchTaskHandler handler =
                    new DeleteComponentIndependentUpgradePackageBatchTaskHandler(this.componentIndependentUpgradePackageAPI, iterator, auditLogAPI);

            BatchTaskSubmitResult result =
                    builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_TASK_NAME)
                            .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_PACKAGE_TASK_DESC) //
                            .registerHandler(handler).start();

            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleUpgradePackage(UUID packageId, BatchTaskBuilder builder) throws BusinessException {

        DefaultBatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(packageId)
                .itemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_ITEM_NAME))
                .build();
        DeleteSingleComponentIndependentUpgradePackageBatchTaskHandler handler =
                new DeleteSingleComponentIndependentUpgradePackageBatchTaskHandler(componentIndependentUpgradePackageAPI, batchTaskItem, auditLogAPI);
        BatchTaskSubmitResult result =
                builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_NAME)
                        .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_DELETE_SINGLE_PACKAGE_TASK_DESC)
                        .registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 升级包信息列表
     *
     * @param request 分页请求信息
     * @return 分页列表信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取组件独立升级包列表")
    @RequestMapping(value = "/package/list")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse listPackage(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbTerminalComponentIndependentUpgradePackageInfoDTO[] dtoArr =
                componentIndependentUpgradePackageAPI.listComponentIndependentUpgradePackage();
        List<ComponentIndependentUpgradePackageTaskDetailVO> detailContentVOList = Lists.newArrayList();

        for (CbbTerminalComponentIndependentUpgradePackageInfoDTO packageInfoDTO : dtoArr) {
            // 查询升级包对应的升级任务
            ComponentIndependentUpgradePackageTaskDetailVO detailContentVO = new ComponentIndependentUpgradePackageTaskDetailVO();
            if (packageInfoDTO.getUpgradeTaskId() != null) {
                detailContentVO = getUpgradePackageTaskDetailVO(packageInfoDTO.getUpgradeTaskId());
            }
            detailContentVO.updateFields(packageInfoDTO);
            detailContentVOList.add(detailContentVO);
        }

        ComponentIndependentUpgradePackageTaskDetailVO[] itemArr = detailContentVOList.toArray(new ComponentIndependentUpgradePackageTaskDetailVO[0]);
        return DefaultWebResponse.Builder.success(itemArr);
    }

    /**
     * 添加终端系统升级任务
     *
     * @param request 添加升级请求
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建组件独立升级任务")
    @RequestMapping(value = "/create")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse create(CreateTerminalSystemUpgradeWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        // 创建升级任务无需校验

        return createUpgradeTask(request);
    }

    /**
     * 创建组件独立升级任务
     *
     * @param request 创建升级任务请求
     * @return 组件独立升级任务id
     */
    private DefaultWebResponse createUpgradeTask(CreateTerminalSystemUpgradeWebRequest request) throws BusinessException {
        UUID packageId = request.getPackageId();
        String[] terminalIdArr = request.getTerminalIdArr();
        validateTerminalIdArr(terminalIdArr);
        CbbAddComponentIndependentUpgradeTaskDTO addTaskRequest = new CbbAddComponentIndependentUpgradeTaskDTO();
        addTaskRequest.setPackageId(packageId);
        addTaskRequest.setTerminalIdArr(terminalIdArr);
        addTaskRequest.setTerminalGroupIdArr(request.getTerminalGroupIdArr());
        CbbAddComponentIndependentUpgradeTaskResultDTO response;
        String packageName = packageId.toString();
        try {
            packageName = getPackageName(packageId);
            checkCreateParam(request);
            response = componentIndependentUpgradeAPI.addComponentIndependentUpgradeTask(addTaskRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CREATE_TASK_SUCCESS_LOG, packageName);
            CreateSystemUpgradeTaskContentVO contentVO = new CreateSystemUpgradeTaskContentVO();
            contentVO.setUpgradeTaskId(response.getUpgradeTaskId());
            return DefaultWebResponse.Builder.success(contentVO);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CREATE_TASK_FAIL_LOG, packageName,
                    e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    private void checkCreateParam(CreateTerminalSystemUpgradeWebRequest request) throws BusinessException {
        if (ArrayUtils.isEmpty(request.getTerminalIdArr()) && ArrayUtils.isEmpty(request.getTerminalGroupIdArr())) {
            // 创建终端升级任务时，任务中的终端和终端分组不可都未空
            throw new BusinessException(
                    TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CREATE_TASK_TERMINAL_AND_GROUP_BOTH_EMPTY_ERROR);
        }

        if (!ArrayUtils.isEmpty(request.getTerminalIdArr())) {
            if (Arrays.stream(request.getTerminalIdArr()).distinct().count() < request.getTerminalIdArr().length) {
                throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_HAS_DUPLICATE_VALUE);
            }

            List<CbbTerminalBasicInfoDTO> basicInfoDTOList = cbbTerminalOperatorAPI.queryByIdList(Arrays.asList(request.getTerminalIdArr()));

            if (basicInfoDTOList.stream().anyMatch(item -> item.getTerminalPlatform() == CbbTerminalPlatformEnums.VDI
                    && !Arrays.asList(PARAM_PRODUCT_TYPE_31XX).contains(item.getProductType()))) {
                throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_HAS_VDI_TERMINAL);
            }
        }
    }

    /**
     * 获取组件独立升级任务列表
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询历史记录")
    @RequestMapping(value = "/list")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse list(TerminalSystemUpgradeTaskListWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        validateLimit(request.getLimit());
        PageSearchRequest apiRequest = new PageSearchRequest(request);
        BetweenTimeRangeMatch betweenTimeRangeMatch = new BetweenTimeRangeMatch(request.getStartTime(), request.getEndTime(), "createTime");
        apiRequest.setBetweenTimeRangeMatch(betweenTimeRangeMatch);

        convertListTaskMatchEqual(apiRequest);
        final DefaultPageResponse<CbbComponentIndependentUpgradeTaskDTO> resp =
                componentIndependentUpgradeAPI.pageQueryComponentIndependentUpgradeTask(apiRequest);
        List<CbbComponentIndependentUpgradeTaskDTO> taskDTOList = Arrays.stream(resp.getItemArr()).collect(Collectors.toList());

        List<ComponentIndependentUpgradePackageTaskDetailVO> detailContentVOList = Lists.newArrayList();
        for (CbbComponentIndependentUpgradeTaskDTO upgradeTaskDTO : taskDTOList) {
            // 查询升级任务补充信息
            ComponentIndependentUpgradePackageTaskDetailVO detailContentVO = getUpgradePackageTaskDetailVO(upgradeTaskDTO.getId());
            detailContentVO.updateFields(upgradeTaskDTO);
            detailContentVOList.add(detailContentVO);
        }

        ComponentIndependentUpgradePackageTaskDetailVO[] itemArr =
                detailContentVOList.toArray(new ComponentIndependentUpgradePackageTaskDetailVO[] {});
        TerminalUpgradeItemArrayWebResponse<ComponentIndependentUpgradePackageTaskDetailVO> webResponse = new TerminalUpgradeItemArrayWebResponse<>();
        webResponse.setItemArr(itemArr);
        webResponse.setTotal(resp.getTotal());
        return DefaultWebResponse.Builder.success(webResponse);
    }

    private void convertListTaskMatchEqual(PageSearchRequest apiRequest) throws BusinessException {
        apiRequest.coverMatchEqualForUUID(COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_ID_FIELD_NAME);
        final MatchEqual[] matchEqualArr = apiRequest.getMatchEqualArr();
        if (ArrayUtils.isEmpty(matchEqualArr)) {
            return;
        }
        setPackageTypeMatchEqual(apiRequest);
        for (MatchEqual me : matchEqualArr) {
            if (COMPONENT_INDEPENDENT_UPGRADE_TASK_STATE_FIELD_NAME.equals(me.getName())) {
                Object[] valueArr = me.getValueArr();
                CbbSystemUpgradeTaskStateEnums[] stateArr = new CbbSystemUpgradeTaskStateEnums[valueArr.length];
                for (int i = 0; i < valueArr.length; i++) {
                    stateArr[i] = CbbSystemUpgradeTaskStateEnums.valueOf(String.valueOf(valueArr[i]));
                }
                me.setValueArr(stateArr);
            }
        }
    }

    private void setPackageTypeMatchEqual(PageSearchRequest apiRequest) {
        MatchEqual[] matchEqualArr = apiRequest.getMatchEqualArr();
        String[] terminalOsTypeArr = new String[0];
        String[] terminalTypeArr = new String[0];
        for (int i = 0; i < matchEqualArr.length; i++) {
            if (TERMINAL_OS_TYPE.equals(matchEqualArr[i].getName())) {
                terminalOsTypeArr = (String[]) matchEqualArr[i].getValueArr();
                matchEqualArr = ArrayUtils.remove(matchEqualArr, i);
                break;
            }
        }
        for (int i = 0; i < matchEqualArr.length; i++) {
            if (TERMINAL_TYPE.equals(matchEqualArr[i].getName())) {
                terminalTypeArr = (String[]) matchEqualArr[i].getValueArr();
                matchEqualArr = ArrayUtils.remove(matchEqualArr, i);
                break;
            }
        }
        if (ArrayUtils.isEmpty(terminalOsTypeArr) && ArrayUtils.isEmpty(terminalTypeArr)) {
            return;
        }

        MatchEqual packageTypeMatchEqual = getPackageTypeMatchEqual(terminalOsTypeArr, terminalTypeArr);
        matchEqualArr = ArrayUtils.add(matchEqualArr, packageTypeMatchEqual);
        apiRequest.setMatchEqualArr(matchEqualArr);
    }

    private MatchEqual getPackageTypeMatchEqual(String[] terminalOsTypeArr, String[] terminalTypeArr) {
        CbbTerminalTypeEnums[] allTerminalTypeEnumArr = CbbTerminalTypeEnums.values();
        Object[] selectedTerminalTypeArr = Arrays.stream(allTerminalTypeEnumArr).filter(type -> {
            if (ArrayUtils.isEmpty(terminalOsTypeArr)) {
                return true;
            } else {
                return ArrayUtils.contains(terminalOsTypeArr, type.getOsType());
            }
        }).filter(type -> {
            if (ArrayUtils.isEmpty(terminalTypeArr)) {
                return true;
            } else {
                return ArrayUtils.contains(terminalTypeArr, type.getPlatform());
            }
        }).toArray();
        return new MatchEqual(COMPONENT_INDEPENDENT_UPGRADE_TASK_PACKAGE_TYPE_FIELD_NAME, selectedTerminalTypeArr);
    }

    private ComponentIndependentUpgradePackageTaskDetailVO getUpgradePackageTaskDetailVO(UUID upgradeTaskId) throws BusinessException {
        PageSearchRequest apiRequest = new PageSearchRequest();
        apiRequest.setPage(0);
        apiRequest.setLimit(MAX_TERMINAL_SUPPORT);
        MatchEqual matchEqual = new MatchEqual(COMPONENT_INDEPENDENT_UPGRADE_TASK_ID_FIELD_NAME, new String[] {upgradeTaskId.toString()});
        apiRequest.setMatchEqualArr(new MatchEqual[] {matchEqual});
        convertListTerminalMatchEqual(apiRequest);
        DefaultPageResponse<CbbSystemUpgradeTaskTerminalDTO> resp =
                componentIndependentUpgradeAPI.pageQueryComponentIndependentUpgradeTaskTerminal(apiRequest);

        ComponentIndependentUpgradePackageTaskDetailVO detailContentVO = new ComponentIndependentUpgradePackageTaskDetailVO();
        if (!ArrayUtils.isEmpty(resp.getItemArr())) {
            // 计算各状态下的终端数量
            ComponentIndependentUpgradeTaskDetailContentVO contentVO = new ComponentIndependentUpgradeTaskDetailContentVO();
            contentVO.setTotal(resp.getTotal());
            countTerminalNumByState(contentVO, resp.getItemArr());
            BeanUtils.copyProperties(contentVO, detailContentVO);
        }
        return detailContentVO;
    }

    /**
     * 获取组件独立升级任务终端列表
     *
     * @param request 请求参数
     * @param sessionContext sessionContext
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取组件独立升级任务下包含的终端列表")
    @RequestMapping(value = "/terminal/list")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse listTerminal(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        validateLimit(request.getLimit());
        final PageSearchRequest apiRequest = new PageSearchRequest(request);
        UUID upgradeTaskId = convertListTerminalMatchEqual(apiRequest);
        List<UpgradeTerminalDetailVO> detailVOList = Lists.newArrayList();
        long total = 0L;

        // taskID存在则查询相关信息
        if (upgradeTaskId != null) {
            final DefaultPageResponse<CbbSystemUpgradeTaskTerminalDTO> resp =
                    componentIndependentUpgradeAPI.pageQueryComponentIndependentUpgradeTaskTerminal(apiRequest);
            for (CbbSystemUpgradeTaskTerminalDTO terminalDTO : resp.getItemArr()) {
                CbbTerminalBasicInfoDTO terminalInfoResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalDTO.getTerminalId());
                UpgradeTerminalDetailVO detailVO = new UpgradeTerminalDetailVO();
                BeanUtils.copyProperties(terminalDTO, detailVO);
                detailVO.setProductType(terminalInfoResponse.getProductType());
                detailVO.setRainOsVersion(terminalInfoResponse.getRainOsVersion());
                detailVO.setTerminalOsType(terminalInfoResponse.getTerminalOsType());
                detailVOList.add(detailVO);
            }
            total = resp.getTotal();
        }

        UpgradeTerminalDetailVO[] itemArr = detailVOList.toArray(new UpgradeTerminalDetailVO[] {});
        TerminalUpgradeItemArrayWebResponse<UpgradeTerminalDetailVO> webResponse = new TerminalUpgradeItemArrayWebResponse<>();
        webResponse.setItemArr(itemArr);
        webResponse.setTotal(total);
        return DefaultWebResponse.Builder.success(webResponse);
    }

    /**
     * 获取组件独立升级任务终端列表
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取组件独立升级任务详情")
    @RequestMapping(value = "/detail")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse detail(GetIndependentUpgradeTaskDetailWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        final PageSearchRequest apiRequest = buildSearchRequest(request.getUpgradeTaskId());
        CbbGetTaskUpgradeTerminalDTO cbbGetTaskUpgradeTerminalDTO = new CbbGetTaskUpgradeTerminalDTO();
        cbbGetTaskUpgradeTerminalDTO.setTaskId(request.getUpgradeTaskId());
        final List<CbbSystemUpgradeTaskTerminalDTO> terminalDTOList =
                componentIndependentUpgradeAPI.listIndependentUpgradeTerminalByTaskId(cbbGetTaskUpgradeTerminalDTO);

        final CbbComponentIndependentUpgradeTaskDTO dto = componentIndependentUpgradeAPI.findTerminalUpgradeTaskById(request.getUpgradeTaskId());

        final DefaultPageResponse<CbbTerminalGroupDetailDTO> groupResp =
                componentIndependentUpgradeAPI.pageQueryComponentIndependentUpgradeTaskTerminalGroup(apiRequest);

        return DefaultWebResponse.Builder.success(buildUpgradeTerminalListVO(terminalDTOList.toArray(new CbbSystemUpgradeTaskTerminalDTO[0]),
                terminalDTOList.size(), dto, groupResp.getItemArr()));
    }

    private ComponentIndependentUpgradeTaskDetailContentVO buildUpgradeTerminalListVO(CbbSystemUpgradeTaskTerminalDTO[] terminalArr, long total,
            CbbComponentIndependentUpgradeTaskDTO upgradeTask, CbbTerminalGroupDetailDTO[] terminalGroupArr) {
        ComponentIndependentUpgradeTaskDetailContentVO contentVO = new ComponentIndependentUpgradeTaskDetailContentVO();
        contentVO.setTerminalArr(terminalArr);
        contentVO.setTotal(total);
        contentVO.setUpgradeTask(upgradeTask);
        countTerminalNumByState(contentVO, terminalArr);

        List<CbbTerminalComponentIndependentUpgradePackageComponentDTO> componentDTOList =
                componentIndependentUpgradePackageAPI.listPackageComponentsByPackageId(upgradeTask.getUpgradePackageId());
        contentVO.setPackageComponentArr(convertToPackageComponentVO(componentDTOList));

        if (ArrayUtils.isEmpty(terminalGroupArr)) {
            contentVO.setTerminalGroupArr(new IdLabelEntry[0]);
        } else {
            IdLabelEntry[] idLabelEntryArr = Arrays.stream(terminalGroupArr).map(group -> {
                IdLabelEntry idLabelEntry = new IdLabelEntry();
                idLabelEntry.setId(group.getId());
                idLabelEntry.setLabel(group.getGroupName());
                return idLabelEntry;
            }).toArray(IdLabelEntry[]::new);
            contentVO.setTerminalGroupArr(idLabelEntryArr);
        }

        return contentVO;
    }

    private void countTerminalNumByState(ComponentIndependentUpgradeTaskDetailContentVO contentVO, CbbSystemUpgradeTaskTerminalDTO[] itemArr) {
        if (itemArr == null || itemArr.length == 0) {
            return;
        }
        for (CbbSystemUpgradeTaskTerminalDTO upgradeTerminal : itemArr) {
            switch (upgradeTerminal.getTerminalUpgradeState()) {
                case WAIT:
                    contentVO.setWaitNum(contentVO.getWaitNum() + 1);
                    break;
                case UPGRADING:
                    contentVO.setUpgradingNum(contentVO.getUpgradingNum() + 1);
                    break;
                case SUCCESS:
                    contentVO.setSuccessNum(contentVO.getSuccessNum() + 1);
                    break;
                case FAIL:
                    contentVO.setFailNum(contentVO.getFailNum() + 1);
                    break;
                case UNSUPPORTED:
                    contentVO.setUnsupportNum(contentVO.getUnsupportNum() + 1);
                    break;
                case UNDO:
                    contentVO.setUndoNum(contentVO.getUndoNum() + 1);
                    break;
                default:
                    break;
            }
        }

    }

    private UUID convertListTerminalMatchEqual(PageSearchRequest apiRequest) throws BusinessException {
        apiRequest.coverMatchEqualForUUID(COMPONENT_INDEPENDENT_UPGRADE_TASK_ID_FIELD_NAME);
        final MatchEqual[] matchEqualArr = apiRequest.getMatchEqualArr();
        if (ArrayUtils.isEmpty(matchEqualArr)) {
            // 没有匹配项则返回空
            return null;
        }
        UUID upgradeTaskId = null;
        for (MatchEqual me : matchEqualArr) {
            final String name = me.getName();
            final Object[] valueArr = me.getValueArr();
            if (COMPONENT_INDEPENDENT_UPGRADE_TASK_ID_FIELD_NAME.equals(name) && valueArr.length > 0) {
                upgradeTaskId = UUID.fromString(String.valueOf(valueArr[0]));
            }
            if (COMPONENT_INDEPENDENT_UPGRADE_TERMINAL_UPGRADE_STATE_FIELD_NAME.equals(name)) {
                CbbSystemUpgradeStateEnums[] stateArr = new CbbSystemUpgradeStateEnums[valueArr.length];
                for (int i = 0; i < valueArr.length; i++) {
                    stateArr[i] = CbbSystemUpgradeStateEnums.valueOf(String.valueOf(valueArr[i]));
                }
                me.setValueArr(stateArr);
            }
        }
        return upgradeTaskId;
    }

    private PageSearchRequest buildSearchRequest(UUID upgradeTaskId) {
        MatchEqual[] matchEqualArr = new MatchEqual[1];
        MatchEqual matchEqual = new MatchEqual();
        matchEqual.setName(COMPONENT_INDEPENDENT_UPGRADE_TASK_ID_FIELD_NAME);
        matchEqual.setValueArr(new Object[] {upgradeTaskId});
        matchEqualArr[0] = matchEqual;
        PageSearchRequest request = new PageSearchRequest();
        request.setMatchEqualArr(matchEqualArr);
        return request;
    }

    /**
     * 关闭组件独立升级任务
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("关闭组件独立升级任务")
    @RequestMapping(value = "/close")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse close(CloseSystemUpgradeTaskWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String packageName = upgradeTaskId.toString();
        try {
            final CbbComponentIndependentUpgradeTaskDTO upgradeTaskDTO = componentIndependentUpgradeAPI.findTerminalUpgradeTaskById(upgradeTaskId);
            packageName = upgradeTaskDTO.getPackageName();
            componentIndependentUpgradeAPI.closeComponentIndependentUpgradeTask(upgradeTaskId);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_TASK_CLOSE_SUCCESS_LOG, packageName);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_TASK_CLOSE_FAIL_LOG, packageName,
                    e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 取消组件独立升级任务（等待中的升级终端）
     *
     * @param request 请求参数
     * @param builder 批任务对象
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("等待中的终端取消组件独立升级")
    @RequestMapping(value = "/cancel")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse cancel(CancelTerminalSystemUpgradeWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String[] terminalIdArr = request.getTerminalIdArr();
        validateTerminalIdArr(terminalIdArr);
        if (terminalIdArr.length == 1) {
            return cancelSingleUpgradeTerminal(terminalIdArr[0], upgradeTaskId);
        }

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<TerminalUpgradeBatchTaskItem> iterator = Stream.of(idArr) //
                .map(id -> buildTerminalUpgradeItem(upgradeTaskId, //
                        TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_ITEM_NAME, id))
                .iterator();
        CancelComponentIndependentUpgradeTerminalBatchTaskHandler handler = new CancelComponentIndependentUpgradeTerminalBatchTaskHandler(
                this.componentIndependentUpgradeAPI, this.cbbTerminalOperatorAPI, idMap, iterator, auditLogAPI);

        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_TASK_DESC) //
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse cancelSingleUpgradeTerminal(String terminalId, UUID upgradeTaskId) throws BusinessException {
        CbbUpgradeTerminalDTO cancelRequest = new CbbUpgradeTerminalDTO();
        cancelRequest.setTerminalId(terminalId);
        cancelRequest.setUpgradeTaskId(upgradeTaskId);
        CbbTerminalBasicInfoDTO basicInfoDTO;
        try {
            basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_FAIL_LOG, terminalId.toUpperCase(),
                    e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_FAIL, e, e.getI18nMessage());
        }
        try {
            componentIndependentUpgradeAPI.cancelIndependentUpgradeTerminal(cancelRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_SUCCESS_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase());
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_SUCCESS,
                    new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_FAIL_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase(), e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_CANCEL_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 重试（失败的升级终端）
     *
     * @param request 请求参数
     * @param builder 批任务对象
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("失败的终端升级重试接口")
    @RequestMapping(value = "/retry")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse retry(RetryTerminalSystemUpgradeWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        Assert.notNull(request.getUpgradeTaskId(), "upgradeTaskId can not be null");
        Assert.notEmpty(request.getTerminalIdArr(), "terminalIdArr can not be empty");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String[] terminalIdArr = request.getTerminalIdArr();
        validateTerminalIdArr(terminalIdArr);
        if (terminalIdArr.length == 1) {
            return retrySingleUpgradeTerminal(terminalIdArr[0], upgradeTaskId);
        }

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<TerminalUpgradeBatchTaskItem> iterator = Stream.of(idArr)//
                .map(id -> buildTerminalUpgradeItem(upgradeTaskId, //
                        TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_ITEM_NAME, id))
                .iterator();
        RetryComponentIndependentUpgradeTerminalBatchTaskHandler handler = new RetryComponentIndependentUpgradeTerminalBatchTaskHandler(
                this.componentIndependentUpgradeAPI, this.cbbTerminalOperatorAPI, idMap, iterator, auditLogAPI);

        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_TASK_DESC) //
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse retrySingleUpgradeTerminal(String terminalId, UUID upgradeTaskId) throws BusinessException {
        CbbUpgradeTerminalDTO retryRequest = new CbbUpgradeTerminalDTO();
        retryRequest.setTerminalId(terminalId);
        retryRequest.setUpgradeTaskId(upgradeTaskId);
        CbbTerminalBasicInfoDTO basicInfoDTO;
        try {
            basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_FAIL_LOG, terminalId.toUpperCase(),
                    e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_FAIL, e, e.getI18nMessage());
        }
        try {
            componentIndependentUpgradeAPI.retryIndependentUpgradeTerminal(retryRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_SUCCESS_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase());
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_FAIL_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase(), e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_RETRY_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 追加组件独立升级终端及分组
     *
     * @param request 请求参数
     * @param builder 批任务对象
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("组件独立升级任务追加终端及分组")
    @RequestMapping(value = "/append")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse append(AppendTerminalSystemUpgradeWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String[] terminalIdArr = request.getTerminalIdArr();
        validateTerminalIdArr(terminalIdArr);
        if (!ArrayUtils.isEmpty(request.getTerminalGroupIdArr())) {
            editComponentIndependentUpgradeGroup(upgradeTaskId, request.getTerminalGroupIdArr());
        }

        if (terminalIdArr.length == 1) {
            return appendSingleTerminal(upgradeTaskId, terminalIdArr[0]);
        }

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<TerminalUpgradeBatchTaskItem> iterator = Stream.of(idArr)
                .map(id -> buildTerminalUpgradeItem(upgradeTaskId, TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_ITEM_NAME, id))
                .iterator();
        AddComponentIndependentUpgradeTerminalBatchTaskHandler handler = new AddComponentIndependentUpgradeTerminalBatchTaskHandler(
                this.componentIndependentUpgradeAPI, this.cbbTerminalOperatorAPI, idMap, iterator, auditLogAPI);

        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_TASK_DESC).registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private void editComponentIndependentUpgradeGroup(UUID upgradeTaskId, UUID[] terminalGroupIdArr) {
        CbbUpgradeTerminalGroupDTO cbbRequest = new CbbUpgradeTerminalGroupDTO();
        cbbRequest.setTerminalGroupIdArr(terminalGroupIdArr);
        cbbRequest.setUpgradeTaskId(upgradeTaskId);
        String upgradePackageName = upgradeTaskId.toString();
        try {
            CbbComponentIndependentUpgradeTaskDTO upgradeTaskDTO = componentIndependentUpgradeAPI.findTerminalUpgradeTaskById(upgradeTaskId);
            Assert.notNull(upgradeTaskDTO, "upgrade task can not be null");
            upgradePackageName = upgradeTaskDTO.getPackageName();
            componentIndependentUpgradeAPI.editComponentIndependentUpgradeTerminalGroup(cbbRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_EDIT_TERMINAL_GROUP_SUCCESS_LOG,
                    upgradePackageName);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_EDIT_TERMINAL_GROUP_FAIL_LOG, upgradePackageName,
                    e.getI18nMessage());
        }
    }

    private DefaultWebResponse appendSingleTerminal(UUID upgradeTaskId, String terminalId) throws BusinessException {
        CbbUpgradeTerminalDTO addRequest = new CbbUpgradeTerminalDTO();
        addRequest.setTerminalId(terminalId);
        addRequest.setUpgradeTaskId(upgradeTaskId);
        CbbTerminalBasicInfoDTO basicInfoDTO;
        try {
            basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL_LOG, terminalId.toUpperCase(),
                    e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL, e, e.getI18nMessage());
        }
        try {
            componentIndependentUpgradeAPI.addComponentIndependentUpgradeTerminal(addRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_SUCCESS_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase());
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL_LOG,
                    basicInfoDTO.getMacAddr().toUpperCase(), e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_ADD_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 构建组件独立升级任务终端任务项
     *
     * @param upgradeTaskId 组件独立升级任务id
     * @param id 终端id映射uuid
     * @param businessKey 任务项key
     * @return 任务项对象
     */
    private TerminalUpgradeBatchTaskItem buildTerminalUpgradeItem(final UUID upgradeTaskId, String businessKey, UUID id) {
        return new TerminalUpgradeBatchTaskItem(id, LocaleI18nResolver.resolve(businessKey), upgradeTaskId);
    }

    /**
     * 获取可组件独立升级的终端列表
     *
     * @param request 请求参数
     * @param sessionContext sessionContext
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取组件独立升级任务可升级终端列表")
    @RequestMapping(value = "/basicInfo/terminal/list")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse listTerminalBasicInfo(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        validateLimit(request.getLimit());

        PageSearchRequest pageSearchRequest = new PageSearchRequest(request);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> terminalGroupIdStrList = getPermissionTerminalGroupIdList(sessionContext.getUserId());
            String terminalGroupId = getTerminalGroupId(request);
            // 如果传来的终端组是空的 处理拥有的终端组集合授权
            if (StringUtils.isEmpty(terminalGroupId)) {
                appendTerminalGroupIdMatchEqual(pageSearchRequest, terminalGroupIdStrList);
            } else {
                // 如果所拥有的终端组权限 不包含查询的终端组ID 则直接返回空
                if (!terminalGroupIdStrList.contains(terminalGroupId)) {
                    DefaultPageResponse response = new DefaultPageResponse();
                    response.setItemArr(Collections.emptyList().toArray());
                    return DefaultWebResponse.Builder.success(response);
                }
            }
        }
        PageQueryResponse<CbbIndependentUpgradeableTerminalListDTO> pageResp = terminalUpgradeAPI.findUpgradeableTerminals(pageSearchRequest);
        return DefaultWebResponse.Builder.success(pageResp);
    }

    /**
     * 获取组件独立升级包所含组件列表
     *
     * @param request 请求
     * @return 响应
     */
    @ApiOperation("组件独立升级包所含组件列表")
    @RequestMapping(value = "/package/component/list")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse listPackageComponents(GetPackageComponentsWebRequest request) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getPackageId(), "request.getPackageId() can not be null");
        List<CbbTerminalComponentIndependentUpgradePackageComponentDTO> dtoList =
                componentIndependentUpgradePackageAPI.listPackageComponentsByPackageId(request.getPackageId());
        PackComponentsWebResponse response = new PackComponentsWebResponse();
        response.setItemArr(convertToPackageComponentVO(dtoList));
        return DefaultWebResponse.Builder.success(response);
    }

    private PackageComponentVO[] convertToPackageComponentVO(List<CbbTerminalComponentIndependentUpgradePackageComponentDTO> dtoList) {
        PackageComponentVO[] componentVOArr = null;
        if (!CollectionUtils.isEmpty(dtoList)) {
            componentVOArr = dtoList.stream().map(PackageComponentVO::new).toArray(PackageComponentVO[]::new);
        }
        return componentVOArr;
    }

    /**
     * 获取组件独立升级任务所含组件列表
     *
     * @param request 请求
     * @return 响应
     */
    @ApiOperation("组件独立升级任务所含组件列表")
    @RequestMapping(value = "/component/list")
    @ApiVersions(@ApiVersion(value = Version.V3_2_101))
    @EnableAuthority
    public DefaultWebResponse listUpgradeComponents(GetUpgradeComponentsWebRequest request) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getUpgradeId(), "request.getUpgradeId() can not be null");
        List<CbbTerminalComponentIndependentUpgradeComponentDTO> dtoList =
                componentIndependentUpgradeAPI.listUpgradeComponentsByUpgradeId(request.getUpgradeId());
        UpgradeComponentsWebResponse response = new UpgradeComponentsWebResponse();
        response.setItemArr(convertToUpgradeComponentVO(dtoList));
        return DefaultWebResponse.Builder.success(response);
    }


    private UpgradeComponentVO[] convertToUpgradeComponentVO(List<CbbTerminalComponentIndependentUpgradeComponentDTO> dtoList) {
        UpgradeComponentVO[] componentVOArr = null;
        if (!CollectionUtils.isEmpty(dtoList)) {
            componentVOArr = dtoList.stream().map(UpgradeComponentVO::new).toArray(UpgradeComponentVO[]::new);
        }
        return componentVOArr;
    }

    /**
     * 当前前端只会上传一个groupId进行查询
     */
    private String getTerminalGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals("groupId") && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return StringUtils.EMPTY;
    }

    private List<String> getPermissionTerminalGroupIdList(UUID userId) throws BusinessException {
        ListTerminalGroupIdRequest listTerminalGroupIdRequest = new ListTerminalGroupIdRequest();
        listTerminalGroupIdRequest.setAdminId(userId);
        ListTerminalGroupIdResponse listTerminalGroupIdResponse = adminDataPermissionAPI.listTerminalGroupIdByAdminId(listTerminalGroupIdRequest);
        return listTerminalGroupIdResponse.getTerminalGroupIdList();
    }

    private void appendTerminalGroupIdMatchEqual(PageSearchRequest request, List<String> terminalGroupIdList) throws BusinessException {
        List<UUID> uuidList = terminalGroupIdList.stream().map(UUID::fromString).collect(Collectors.toList());
        uuidList.add(DEFAULT_TERMINAL_GROUP_UUID);
        UUID[] uuidArr = uuidList.toArray(new UUID[0]);
        request.appendCustomMatchEqual(new MatchEqual("terminalGroupIdArr", uuidArr));
    }

    private String getPackageName(UUID packageId) {
        try {
            CbbTerminalComponentIndependentUpgradePackageInfoDTO upgradePackageInfoDTO =
                    componentIndependentUpgradePackageAPI.findPackageInfoByPackageId(packageId);
            return upgradePackageInfoDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error("获取升级包[{}]名称异常", packageId, e);
            return packageId.toString();
        }
    }

    private String getSpecialOsPlatform(CbbTerminalTypeEnums terminalTypeEnum) {
        String osPlatform = terminalTypeEnum.getPlatform();
        if (terminalTypeEnum == CbbTerminalTypeEnums.IDV_LINUX) {
            osPlatform = "TCI/IDV";
        }
        return osPlatform;
    }

    private void validateLimit(Integer limit) throws BusinessException {
        if (limit < MIN_LIMIT || limit > MAX_LIMIT) {
            throw new BusinessException(RCDC_RCO_PARAM_EXCEED_LIMITS, LIMIT, MIN_LIMIT.toString(), MAX_LIMIT.toString());
        }
    }

    private void validateTerminalIdArr(String[] terminalIdArr) throws BusinessException {
        if (ArrayUtils.isNotEmpty(terminalIdArr)) {
            for (String terminalId : terminalIdArr) {
                if (StringUtils.isBlank(terminalId)) {
                    throw new BusinessException(RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PARAM_HAS_EMPTY_VALUE, TERMINAL_ID_ARR);
                }
            }
        }
    }
}
