package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUamSearchDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CheckDuplicationResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskSoftType;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionFileManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaSupportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskGetGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskSearchGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.AppTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionExecType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionLocationType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.CompressPackageConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.TerminalGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.TerminalGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UserGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UserGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.distribution.dto.DistributeTaskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.distribution.request.SearchDistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.TaskGroupDesktopPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.TaskSelectGroupDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.util.SortUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeSubTaskCancelTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeSubTaskRetryTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeTaskCancelTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeTaskCreateAndRunTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeTaskDeleteTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.DistributeTaskRunTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.batchtask.item.DistributeSubTaskOperateBatchItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.resquest.DistributeTaskCreateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo.AppClientSubTaskVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo.DesktopSubTaskVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo.DistributeParameterDataVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo.DistributeParameterVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo.DistributeTaskBasicInfoVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.LinuxPathUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.WindowsPathUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.NameWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CUSTOM_LOCATION_NULL;

;

/**
 * Description: 文件分发任务Controller
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 14:17
 *
 * @author zhangyichi
 */
@Controller
@RequestMapping("/rco/fileDistribute/task")
public class DistributeTaskController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeTaskController.class);

    /**
     * 默认任务有效时间（一周）
     */
    private static final long DEFAULT_TASK_INVALID_INTERVAL = TimeUnit.DAYS.toMillis(7);

    /**
     * 任务名称长度限制
     */
    private static final int TASK_NAME_MAX_LENGTH = 64;

    @Autowired
    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private RcaSupportAPI rcaSupportAPI;

    @Autowired
    private CbbDeskSoftMgmtAPI cbbDeskSoftMgmtAPI;

    @Autowired
    private FileDistributionFileManageAPI fileDistributionFileManageAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建文件分发时选择软件列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建文件分发时选择软件列表")
    @RequestMapping(value = "/deskSoft/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<CbbDeskSoftDTO>> pageDeskSoft(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        // 如果排序规则为空，则默认采用创建时间倒序
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        CbbUamSearchDeskSoftDTO cbbUamSearchDeskSoftDTO = new CbbUamSearchDeskSoftDTO();
        cbbUamSearchDeskSoftDTO.setFileName(request.getSearchKeyword());
        cbbUamSearchDeskSoftDTO.setIncludeDeskSoftTypeList(Collections.singletonList(DeskSoftType.FILE));

        DefaultPageResponse<CbbDeskSoftDTO> cbbDeskSoftPageResponse = cbbDeskSoftMgmtAPI.pageDeskSoft(cbbUamSearchDeskSoftDTO, pageable);
        return CommonWebResponse.success(cbbDeskSoftPageResponse);
    }

    /**
     * 创建文件分发任务并执行
     *
     * @param request 创建任务请求
     * @param builder 批任务
     * @param sessionContext 上下文
     * @return 批任务信息
     * @throws BusinessException 业务异常
     */
    @RequestMapping("createAndRun")
    public DefaultWebResponse createTaskAndRun(DistributeTaskCreateWebRequest request,
            BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        CreateDistributeTaskRequest apiRequest = buildCreateDistributeTaskRequest(request);
        validLocation(apiRequest);
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(), apiRequest.getTaskName());

        // 文件分发不支持多会话桌面池桌面
        List<CloudDesktopDTO> desktopDTOList = userDesktopMgmtAPI.listDesktopByDesktopIds(request.getDesktopIdList());
        long count = desktopDTOList.stream().filter(dto -> CbbDesktopSessionType.MULTIPLE == dto.getSessionType()).count();
        if (count > 0) {
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_AND_RUN_TASK_FAIL_MULTI_SESSIONTYPE,
                    request.getTaskName());
        }

        DistributeTaskCreateAndRunTaskHandler batchTaskHandler = new DistributeTaskCreateAndRunTaskHandler(batchTaskItem);
        batchTaskHandler.setAuditLogAPI(auditLogAPI);
        batchTaskHandler.setFileDistributionTaskManageAPI(fileDistributionTaskManageAPI);
        batchTaskHandler.setApiRequest(apiRequest);
        batchTaskHandler.setGeneralPermissionHelper(generalPermissionHelper);
        batchTaskHandler.setSessionContext(sessionContext);

        BatchTaskSubmitResult result = builder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_AND_RUN_TASK_NAME)
                .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CREATE_AND_RUN_TASK_DESC).registerHandler(batchTaskHandler)
                .start();

        return DefaultWebResponse.Builder.success(result);
    }

    private CreateDistributeTaskRequest buildCreateDistributeTaskRequest(DistributeTaskCreateWebRequest request) {
        CreateDistributeTaskRequest apiRequest = new CreateDistributeTaskRequest();
        // 复制其他字段
        BeanCopyUtil.copy(request, apiRequest);

        // 重组任务参数字段
        DistributeParameterVO parameterVO = request.getParameter();
        DistributeParameterDTO parameterDTO = new DistributeParameterDTO();
        BeanCopyUtil.copy(parameterVO, parameterDTO);

        parameterDTO.setExecType(FileDistributionExecType.valueOf(parameterVO.getExecType()));
        parameterDTO.setLocationType(FileDistributionLocationType.valueOf(parameterVO.getLocationType()));
        parameterDTO.setCompressPackageConfig(buildCompressPackageConfig(parameterVO.getCompressPackageConfig()));

        List<DistributeParameterDataVO> dataList = parameterVO.getDataList();
        if (!CollectionUtils.isEmpty(dataList)) {
            List<DistributeParameterDataDTO> parameterDataDTOList = dataList.stream().map(parameterDataVO -> {
                DistributeParameterDataDTO dataDTO = new DistributeParameterDataDTO();
                BeanCopyUtil.copy(parameterDataVO, dataDTO);
                return dataDTO;
            }).collect(Collectors.toList());
            parameterDTO.setDataList(parameterDataDTOList);
        }

        if (StringUtils.isNotBlank(parameterVO.getOsLike())) {
            parameterDTO.setOsLike(FileDistributionOsType.valueOf(parameterVO.getOsLike()));
        }

        apiRequest.setParameter(parameterDTO);

        return apiRequest;
    }

    private CompressPackageConfigDTO buildCompressPackageConfig(CompressPackageConfigDTO compressPackageConfig) {
        if (compressPackageConfig == null) {
            // 空的情况下直接返回
            return null;
        }
        CompressPackageConfigDTO result = new CompressPackageConfigDTO();
        if (BooleanUtils.isTrue(compressPackageConfig.getEnableAutoDecompress())) {
            BeanUtils.copyProperties(compressPackageConfig, result);
        } else {
            // 没有开启自动解压的情况下，其他配置无需入库
            result.setEnableAutoDecompress(compressPackageConfig.getEnableAutoDecompress());
        }
        return result;
    }


    /**
     * 获取文件分发列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取文件分发列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<DistributeTaskDetailDTO>> pageDistributeTask(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        // 如果排序规则为空，则默认采用创建时间倒序
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchDistributeTaskDTO searchDistributeTaskDTO = new SearchDistributeTaskDTO();
        searchDistributeTaskDTO.setTaskName(request.getSearchKeyword());

        generalPermissionHelper.setPermissionParam(sessionContext, searchDistributeTaskDTO);

        DefaultPageResponse<DistributeTaskDetailDTO> distributeTaskPageResponse =
                fileDistributionTaskManageAPI.pageDistributeTask(searchDistributeTaskDTO, pageable);

        return CommonWebResponse.success(distributeTaskPageResponse);
    }



    /**
     * 校验文件分发任务名称
     *
     * @param request 任务名称
     * @return 校验结果
     */
    @RequestMapping("checkDuplication")
    public DefaultWebResponse checkDuplication(NameWebRequest request) {
        Assert.notNull(request, "request cannot be null!");

        List<DistributeTaskDTO> taskDTOList = fileDistributionTaskManageAPI.findByTaskName(request.getName());
        CheckDuplicationResultDTO dto = checkTaskName(request.getName(), taskDTOList);
        return DefaultWebResponse.Builder.success(dto);
    }

    private CheckDuplicationResultDTO checkTaskName(String deskSoftName, List<DistributeTaskDTO> taskDTOList) {
        Assert.hasText(deskSoftName, "deskSoftName can not be blank");
        CheckDuplicationResultDTO dto = new CheckDuplicationResultDTO();

        // 检查文件名长度
        if (deskSoftName.length() > TASK_NAME_MAX_LENGTH) {
            dto.setHasDuplication(Boolean.TRUE);
            dto.setErrorMsg(LocaleI18nResolver.resolve(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_NAME_LENGTH_ILLEGAL));
            return dto;
        }

        // 检查文件名是否重复
        if (!CollectionUtils.isEmpty(taskDTOList)) {
            dto.setHasDuplication(Boolean.TRUE);
            dto.setErrorMsg(LocaleI18nResolver.resolve(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_NAME_REPEATED));
            return dto;
        }

        dto.setHasDuplication(Boolean.FALSE);
        return dto;
    }

    /**
     * 获取文件分发任务（父任务）基本信息
     *
     * @param request 父任务ID
     * @param sessionContext 上下文
     * @return 任务基本信息
     * @throws BusinessException 业务异常
     */
    @RequestMapping("basicInfo")
    public DefaultWebResponse getBasicInfo(IdWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        // 校验是否有该记录权限
        UUID taskId = request.getId();

        generalPermissionHelper.checkPermission(sessionContext, taskId, AdminDataPermissionType.FILE_DISTRIBUTION);

        DistributeTaskParameterDTO basicInfoDTO = fileDistributionTaskManageAPI.getBasicInfo(new IdRequest(taskId));
        DistributeTaskBasicInfoVO basicInfoVO = buildDistributeTaskBasicInfoVO(basicInfoDTO);
        return DefaultWebResponse.Builder.success(basicInfoVO);
    }

    private DistributeTaskBasicInfoVO buildDistributeTaskBasicInfoVO(DistributeTaskParameterDTO basicInfoDTO) {
        DistributeTaskBasicInfoVO basicInfoVO = new DistributeTaskBasicInfoVO();
        // 复制其他字段
        BeanCopyUtil.copy(basicInfoDTO, basicInfoVO);

        // 重组任务参数
        DistributeParameterDTO parameterDTO = basicInfoDTO.getParameter();
        DistributeParameterVO parameterVO = new DistributeParameterVO();

        BeanCopyUtil.copy(parameterDTO, parameterVO);

        parameterVO.setExecType(parameterDTO.getExecType().name());
        parameterVO.setLocationType(parameterDTO.getLocationType().name());
        if (Objects.nonNull(parameterDTO.getOsLike())) {
            parameterVO.setOsLike(parameterDTO.getOsLike().name());
        }
        List<DistributeParameterDataVO> parameterDataVOList = parameterDTO.getDataList().stream().map(dataDTO -> {
            DistributeParameterDataVO dataVO = new DistributeParameterDataVO();
            BeanCopyUtil.copy(dataDTO, dataVO);
            return dataVO;
        }).collect(Collectors.toList());
        parameterVO.setDataList(parameterDataVOList);

        basicInfoVO.setParameter(parameterVO);
        return basicInfoVO;
    }

    /**
     * 开始执行文件分发任务（父任务操作执行）
     *
     * @param request 父任务ID
     * @param builder 批任务
     * @param sessionContext 上下文
     * @return 批任务信息
     * @throws BusinessException 业务异常
     */
    @Deprecated
    @RequestMapping("run")
    @EnableAuthority
    public DefaultWebResponse runTask(IdWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID taskId = request.getId();
        generalPermissionHelper.checkPermission(sessionContext, taskId, AdminDataPermissionType.FILE_DISTRIBUTION);

        DistributeTaskParameterDTO taskInfo = fileDistributionTaskManageAPI.getBasicInfo(new IdRequest(taskId));
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(taskId, taskInfo.getTaskName());

        DistributeTaskRunTaskHandler batchTaskHandler = new DistributeTaskRunTaskHandler(batchTaskItem);
        batchTaskHandler.setAuditLogAPI(auditLogAPI);
        batchTaskHandler.setFileDistributionTaskManageAPI(fileDistributionTaskManageAPI);
        BatchTaskSubmitResult result = builder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RUN_TASK_NAME)
                .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RUN_TASK_DESC).registerHandler(batchTaskHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 取消文件分发任务（父任务）
     *
     * @param request 父任务ID
     * @param builder 批处理
     * @param sessionContext 上下文
     * @return 批处理任务
     * @throws BusinessException 业务异常
     */
    @RequestMapping("cancel")
    @EnableAuthority
    public DefaultWebResponse cancelTask(IdWebRequest request, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        UUID taskId = request.getId();
        generalPermissionHelper.checkPermission(sessionContext, taskId, AdminDataPermissionType.FILE_DISTRIBUTION);

        DistributeTaskParameterDTO taskInfo = fileDistributionTaskManageAPI.getBasicInfo(new IdRequest(taskId));
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(taskId, taskInfo.getTaskName());

        DistributeTaskCancelTaskHandler batchTaskHandler = new DistributeTaskCancelTaskHandler(batchTaskItem);
        batchTaskHandler.setAuditLogAPI(auditLogAPI);
        batchTaskHandler.setFileDistributionTaskManageAPI(fileDistributionTaskManageAPI);
        BatchTaskSubmitResult result = builder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_NAME)
                .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_TASK_DESC).registerHandler(batchTaskHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 删除文件分发任务（父任务）
     *
     * @param request 父任务ID数组
     * @param builder 批处理
     * @param sessionContext SessionContext
     * @return 批处理任务
     * @throws BusinessException 业务异常
     */
    @RequestMapping("delete")
    @EnableAuthority
    public DefaultWebResponse deleteTask(IdArrWebRequest request, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        final UUID[] idArr = request.getIdArr();
        if (idArr.length == 1) {
            UUID taskId = idArr[0];
            generalPermissionHelper.checkPermission(sessionContext, taskId, AdminDataPermissionType.FILE_DISTRIBUTION);
            return singleDeleteProcess(taskId);
        } else {
            return batchDeleteProcess(idArr, builder, sessionContext);
        }
    }

    private DefaultWebResponse batchDeleteProcess(UUID[] idArr, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_TASK_ITEM_NAME)).build())
                .iterator();
        DistributeTaskDeleteTaskHandler handler = new DistributeTaskDeleteTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setGeneralPermissionHelper(generalPermissionHelper);
        handler.setSessionContext(sessionContext);
        handler.setFileDistributionTaskManageAPI(fileDistributionTaskManageAPI);
        BatchTaskSubmitResult result = builder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_BATCH_DELETE_TASK_TASK_NAME)
                .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_BATCH_DELETE_TASK_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse singleDeleteProcess(UUID taskId) throws BusinessException {

        DistributeTaskParameterDTO taskInfo = fileDistributionTaskManageAPI.getBasicInfo(new IdRequest(taskId));
        if (taskInfo != null) {
            fileDistributionTaskManageAPI.deleteTask(new IdRequest(taskId));
            generalPermissionHelper.deletePermission(taskId, AdminDataPermissionType.FILE_DISTRIBUTION);
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_DELETE_TASK_ITEM_SUCCESS_DESC, taskInfo.getTaskName());
        }
        return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    /**
     * 分发任务（父任务）复制前检查
     *
     * @param request 父任务ID
     * @return 默认响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping("copyCheck")
    public DefaultWebResponse copyCheck(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        UUID taskId = request.getId();
        DistributeTaskParameterDTO taskInfo = fileDistributionTaskManageAPI.getBasicInfo(new IdRequest(taskId));

        // 检查分发参数
        DistributeParameterDTO parameter = taskInfo.getParameter();
        if (parameter == null || CollectionUtils.isEmpty(parameter.getDataList())) {
            LOGGER.error("父任务[{}]分发参数[{}]不合法，复制失败", taskId, JSONObject.toJSONString(parameter));
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_COPY_CHECK_PARA_ILLEGAL);
        }

        // 检查关联文件
        boolean hasDeletedFile = parameter.getDataList().stream().anyMatch(this::checkFileDeletedById);
        if (hasDeletedFile) {
            LOGGER.error("参数配置[{}]中有不存在的分发文件，复制失败", parameter.getId());
            throw new BusinessException(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_COPY_CHECK_FILE_DELETED);
        }

        return DefaultWebResponse.Builder.success();
    }

    private boolean checkFileDeletedById(DistributeParameterDataDTO data) {
        try {
            cbbDeskSoftMgmtAPI.getDeskSoft(data.getId());
            return false;
        } catch (BusinessException e) {
            LOGGER.error("文件[{}]不存在", data.getId());
            return true;
        }

    }


    /**
     * 分页查询向云桌面的子任务
     *
     * @param request 分页请求
     * @return 子任务列表（云桌面）
     * @throws BusinessException 业务异常
     */
    @RequestMapping("subtask/desktopList")
    public DefaultWebResponse listSubTaskForDesktop(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        PageQueryResponse<DistributeSubTaskDTO> queryResponse =
                fileDistributionTaskManageAPI.pageQuerySubTask(request, FileDistributionTargetType.DESKTOP);
        DistributeSubTaskDTO[] subTaskDTOArr = queryResponse.getItemArr();
        if (subTaskDTOArr == null || ArrayUtils.isEmpty(subTaskDTOArr)) {
            return DefaultWebResponse.Builder.success(queryResponse);
        }

        DesktopSubTaskVO[] subTaskVOArr = new DesktopSubTaskVO[subTaskDTOArr.length];
        for (int i = 0; i < subTaskDTOArr.length; i++) {
            DistributeSubTaskDTO taskDTO = subTaskDTOArr[i];
            CloudDesktopDetailDTO desktopDetail = getCloudDesktopDetailDTO(taskDTO.getTargetId());

            DesktopSubTaskVO subTaskVO = new DesktopSubTaskVO();
            BeanCopyUtil.copy(desktopDetail, subTaskVO);
            subTaskVO.setDesktopCategory(desktopDetail.getDeskType());
            subTaskVO.setSubTaskId(taskDTO.getId());
            subTaskVO.setStatus(taskDTO.getStatus());
            subTaskVO.setMessage(taskDTO.getMessage());

            if (Objects.nonNull(desktopDetail.getDesktopImageId())) {
                CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(desktopDetail.getDesktopImageId());

                subTaskVO.setImageUsage(imageTemplateDetail.getImageUsage());
                subTaskVO.setDesktopCategory(imageTemplateDetail.getCbbImageType().name());
            }

            subTaskVOArr[i] = subTaskVO;
        }
        PageQueryResponse<DesktopSubTaskVO> response = new PageQueryResponse<>(subTaskVOArr, queryResponse.getTotal());
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 分页查询向应用客户端的子任务
     *
     * @param request 分页请求
     * @return 子任务列表（应用客户端）
     * @throws BusinessException 业务异常
     */
    @RequestMapping("subtask/appClientList")
    public DefaultWebResponse listSubTaskForAppClient(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        PageQueryResponse<DistributeSubTaskDTO> queryResponse =
                fileDistributionTaskManageAPI.pageQuerySubTask(request, FileDistributionTargetType.APP_CLIENT);
        DistributeSubTaskDTO[] subTaskDTOArr = queryResponse.getItemArr();
        if (subTaskDTOArr == null || ArrayUtils.isEmpty(subTaskDTOArr)) {
            return DefaultWebResponse.Builder.success(queryResponse);
        }

        AppClientSubTaskVO[] subTaskVOArr = new AppClientSubTaskVO[subTaskDTOArr.length];
        for (int i = 0; i < subTaskDTOArr.length; i++) {
            DistributeSubTaskDTO taskDTO = subTaskDTOArr[i];
            AppTerminalDTO appClientDetail = getAppTerminalDTO(taskDTO.getTargetId());
            AppClientSubTaskVO subTaskVO = new AppClientSubTaskVO();
            BeanCopyUtil.copy(appClientDetail, subTaskVO);
            subTaskVO.setSubTaskId(taskDTO.getId());
            subTaskVO.setStatus(taskDTO.getStatus());
            subTaskVO.setMessage(taskDTO.getMessage());
            subTaskVOArr[i] = subTaskVO;
        }
        PageQueryResponse<AppClientSubTaskVO> response = new PageQueryResponse<>(subTaskVOArr, queryResponse.getTotal());
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 取消文件分发任务（子任务）
     *
     * @param request 子任务ID数组
     * @param builder 批处理任务
     * @return 默认响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping("subtask/cancel")
    public DefaultWebResponse cancelSubTask(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");

        UUID[] subTaskIdArr = request.getIdArr();
        if (subTaskIdArr.length == 1) {
            // 单独操作
            IdRequest subTaskIdRequest = new IdRequest(subTaskIdArr[0]);
            fileDistributionTaskManageAPI.cancelSubTask(subTaskIdRequest);

            DistributeSubTaskOperateBatchItem operateBatchItem =
                    getSubTaskOperateBatchItem(subTaskIdRequest, FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_SUB_TASK_ITEM_NAME);
            String parentTaskName = operateBatchItem.getParentTask().getTaskName();
            String targetName = operateBatchItem.getTargetName();
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_SUB_TASK_SUCCESS_LOG, parentTaskName, targetName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[] {});
        }
        Iterator<DistributeSubTaskOperateBatchItem> iterator = Stream.of(subTaskIdArr) //
                .distinct() //
                .map(id -> getSubTaskOperateBatchItem(new IdRequest(id),
                        FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_SUB_TASK_ITEM_NAME)) //
                .iterator();

        DistributeSubTaskCancelTaskHandler handler = new DistributeSubTaskCancelTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setFileDistributionTaskManageAPI(fileDistributionTaskManageAPI);
        BatchTaskSubmitResult result = builder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_SUB_TASK_NAME)
                .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_CANCEL_SUB_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 重试文件分发任务（子任务）
     *
     * @param request 子任务ID数组
     * @param builder 批处理任务
     * @return 默认响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping("subtask/retry")
    public DefaultWebResponse retrySubTask(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");

        UUID[] subTaskIdArr = request.getIdArr();
        if (subTaskIdArr.length == 1) {
            // 单独操作
            IdRequest subTaskIdRequest = new IdRequest(subTaskIdArr[0]);
            fileDistributionTaskManageAPI.retrySubTask(subTaskIdRequest);

            DistributeSubTaskOperateBatchItem operateBatchItem =
                    getSubTaskOperateBatchItem(subTaskIdRequest, FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_ITEM_NAME);
            String parentTaskName = operateBatchItem.getParentTask().getTaskName();
            String targetName = operateBatchItem.getTargetName();
            auditLogAPI.recordLog(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_SUCCESS_LOG, parentTaskName, targetName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[] {});
        }

        // 批量操作
        Iterator<DistributeSubTaskOperateBatchItem> iterator = Stream.of(subTaskIdArr) //
                .distinct()//
                .map(id -> getSubTaskOperateBatchItem(new IdRequest(id),
                        FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_ITEM_NAME)) //
                .iterator();

        DistributeSubTaskRetryTaskHandler handler = new DistributeSubTaskRetryTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setFileDistributionTaskManageAPI(fileDistributionTaskManageAPI);
        BatchTaskSubmitResult result = builder.setTaskName(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_NAME)
                .setTaskDesc(FileDistributionBusinessKey.RCDC_RCO_FILE_DISTRIBUTION_RETRY_SUB_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    private DistributeSubTaskOperateBatchItem getSubTaskOperateBatchItem(IdRequest subTaskIdRequest, String itemNameI18NKey) {
        // 创建批处理对象
        DistributeSubTaskOperateBatchItem item =
                new DistributeSubTaskOperateBatchItem(subTaskIdRequest.getId(), LocaleI18nResolver.resolve(itemNameI18NKey));
        try {
            // 查询子任务信息
            DistributeSubTaskDTO subTaskInfo = fileDistributionTaskManageAPI.getSubTaskInfo(subTaskIdRequest);
            item.setSubTask(subTaskInfo);
            // 查询父任务信息
            DistributeTaskParameterDTO parentTaskParameterInfo =
                    fileDistributionTaskManageAPI.getBasicInfo(new IdRequest(subTaskInfo.getParentTaskId()));
            item.setParentTask(parentTaskParameterInfo);
            // 查询分发对象信息
            switch (subTaskInfo.getTargetType()) {
                case DESKTOP:
                    CloudDesktopDetailDTO desktopDetailDTO = getCloudDesktopDetailDTO(subTaskInfo.getTargetId());
                    item.setTargetName(desktopDetailDTO.getDesktopName());
                    break;
                case APP_CLIENT:
                    AppTerminalDTO appTerminalDTO = getAppTerminalDTO(subTaskInfo.getTargetId());
                    item.setTargetName(appTerminalDTO.getHostName());
                    break;
                default:
                    LOGGER.info("分发子任务[{}]不支持的文件分发对象类型[{}]", subTaskInfo.getId(), subTaskInfo.getTargetType().name());
            }
        } catch (Exception e) {
            LOGGER.error("获取文件分发子任务[{}]批处理对象失败", subTaskIdRequest.getId(), e);
        }
        return item;
    }

    private CloudDesktopDetailDTO getCloudDesktopDetailDTO(String subTaskTargetId) {
        UUID desktopId = UUID.fromString(subTaskTargetId);
        CloudDesktopDetailDTO desktopDetail = new CloudDesktopDetailDTO();
        try {
            desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
        } catch (Exception e) {
            LOGGER.error("查询桌面[{}]信息失败", desktopId, e);
        }
        return desktopDetail;
    }

    private AppTerminalDTO getAppTerminalDTO(String subTaskTargetId) {
        int appClientId = Integer.parseInt(subTaskTargetId);
        AppTerminalDTO appClientDetail = new AppTerminalDTO();
        try {
            appClientDetail = rcaSupportAPI.getAppTerminalDetail(appClientId);
        } catch (Exception e) {
            LOGGER.error("获取云应用客户端[{}]信息失败", appClientId, e);
        }
        return appClientDetail;
    }

    private void validLocation(CreateDistributeTaskRequest apiRequest) throws BusinessException {
        DistributeParameterDTO distributeParameterDTO = apiRequest.getParameter();

        // 检查路径是否合法
        if (distributeParameterDTO.getLocationType() == FileDistributionLocationType.CUSTOM) {
            String location = Optional.ofNullable(distributeParameterDTO.getLocation())
                    .orElseThrow(() -> new BusinessException(RCDC_RCO_FILE_DISTRIBUTION_CUSTOM_LOCATION_NULL, apiRequest.getTaskName()));
            checkPath(distributeParameterDTO, location);
        }


        // 检查解压路径是否合法，开启自动解压的时候才需要校验路径
        if (apiRequest.getParameter().getCompressPackageConfig() != null
                && StringUtils.isNotBlank(apiRequest.getParameter().getCompressPackageConfig().getDecompressPath())
                && BooleanUtils.isTrue(apiRequest.getParameter().getCompressPackageConfig().getEnableAutoDecompress())) {
            String decompressPath = apiRequest.getParameter().getCompressPackageConfig().getDecompressPath();
            checkPath(distributeParameterDTO, decompressPath);
        }

    }

    private void checkPath(DistributeParameterDTO distributeParameterDTO, String location) throws BusinessException {
        if (distributeParameterDTO.getOsLike() == FileDistributionOsType.WINDOWS) {
            WindowsPathUtils.checkWindowsPath(location);
        } else if (distributeParameterDTO.getOsLike() == FileDistributionOsType.LINUX) {
            LinuxPathUtils.checkLinuxPath(location);
        }
    }


    /**
     * 文件分发 - 批量添加云桌面--终端组--根据用户点击，获取该组的云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件分发 - 批量添加云桌面--终端组--根据用户点击，获取该组的云桌面数据")
    @RequestMapping(value = "/terminalGroup/desktop/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<TerminalGroupDesktopRelatedDTO>> pageTerminalGroupDesktop(TaskGroupDesktopPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID terminalGroupId = request.getGroupId();
        if (Objects.nonNull(terminalGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, terminalGroupId, AdminDataPermissionType.TERMINAL_GROUP);
        }

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO = new TaskSearchGroupDesktopRelatedDTO();
        taskSearchGroupDesktopRelatedDTO.setSearchName(request.getSearchKeyword());
        taskSearchGroupDesktopRelatedDTO.setGroupId(terminalGroupId);
        taskSearchGroupDesktopRelatedDTO.setDeskStateList(request.getDeskStateList());
        taskSearchGroupDesktopRelatedDTO.setPlatformStatusList(request.getPlatformStatusList());

        generalPermissionHelper.setPermissionParam(sessionContext, taskSearchGroupDesktopRelatedDTO);

        DefaultPageResponse<TerminalGroupDesktopRelatedDTO> terminalGroupDesktopRelatedPageResponse =
                fileDistributionTaskManageAPI.pageTerminalGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO, pageable);

        return CommonWebResponse.success(terminalGroupDesktopRelatedPageResponse);
    }



    /**
     * 文件分发 - 批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量
     *
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件分发 - 批量添加云桌面--终端组--获所有终端组的云桌面数量")
    @RequestMapping(value = "/terminalGroup/desktopCount/list", method = RequestMethod.POST)
    public CommonWebResponse<List<TerminalGroupDesktopCountDTO>> listTerminalGroupDesktopCount(SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext must not be null");

        TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO = new TaskGetGroupDesktopCountDTO();
        generalPermissionHelper.setPermissionParam(sessionContext, taskGetGroupDesktopCountDTO);

        List<TerminalGroupDesktopCountDTO> userGroupDesktopCountList =
                fileDistributionTaskManageAPI.listTerminalGroupDesktopCount(taskGetGroupDesktopCountDTO);

        return CommonWebResponse.success(userGroupDesktopCountList);
    }


    /**
     * 文件分发 - 批量添加云桌面--终端组--根据用户点击，获取该组的云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件分发 - 批量添加云桌面--终端组--根据用户选中，获取该组下所有云桌面数据")
    @RequestMapping(value = "/terminalGroup/selectDesktop/list", method = RequestMethod.POST)
    public CommonWebResponse<List<TerminalGroupDesktopRelatedDTO>> listTerminalGroupDesktop(TaskSelectGroupDesktopRequest request,
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

        TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO = new TaskSearchGroupDesktopRelatedDTO();
        taskSearchGroupDesktopRelatedDTO.setGroupIdList(terminalGroupIdList);

        List<TerminalGroupDesktopRelatedDTO> terminalGroupDesktopRelatedList =
                fileDistributionTaskManageAPI.listTerminalGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO);

        return CommonWebResponse.success(terminalGroupDesktopRelatedList);
    }


    /**
     * 文件分发 - 批量添加云桌面--用户组--根据用户点击，获取该组的云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件分发 - 批量添加云桌面--用户组--根据用户点击，获取该组的云桌面数据")
    @RequestMapping(value = "/userGroup/desktop/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserGroupDesktopRelatedDTO>> pageUserGroupDesktop(TaskGroupDesktopPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID userGroupId = request.getGroupId();
        if (Objects.nonNull(userGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, userGroupId, AdminDataPermissionType.USER_GROUP);
        }

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO = new TaskSearchGroupDesktopRelatedDTO();
        taskSearchGroupDesktopRelatedDTO.setSearchName(request.getSearchKeyword());
        taskSearchGroupDesktopRelatedDTO.setGroupId(userGroupId);
        taskSearchGroupDesktopRelatedDTO.setDeskStateList(request.getDeskStateList());
        taskSearchGroupDesktopRelatedDTO.setPlatformStatusList(request.getPlatformStatusList());
        taskSearchGroupDesktopRelatedDTO.setPlatformTypeList(request.getPlatformTypeList());

        generalPermissionHelper.setPermissionParam(sessionContext, taskSearchGroupDesktopRelatedDTO);

        DefaultPageResponse<UserGroupDesktopRelatedDTO> userGroupDesktopRelatedPageResponse =
                fileDistributionTaskManageAPI.pageUserGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO, pageable);

        return CommonWebResponse.success(userGroupDesktopRelatedPageResponse);
    }

    /**
     * 文件分发 - 批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量
     *
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件分发 - 批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量")
    @RequestMapping(value = "/userGroup/desktopCount/list", method = RequestMethod.POST)
    public CommonWebResponse<List<UserGroupDesktopCountDTO>> listUserGroupDesktopCount(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext must not be null");

        TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO = new TaskGetGroupDesktopCountDTO();
        generalPermissionHelper.setPermissionParam(sessionContext, taskGetGroupDesktopCountDTO);

        List<UserGroupDesktopCountDTO> userGroupDesktopCountList =
                fileDistributionTaskManageAPI.listUserGroupDesktopCount(taskGetGroupDesktopCountDTO);

        return CommonWebResponse.success(userGroupDesktopCountList);
    }


    /**
     * 文件分发任务：批量添加云桌面--用户组--根据用户选中，获取该组下所有云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("文件分发任务：批量添加云桌面[用户组下获取云桌面列表]")
    @RequestMapping(value = "/userGroup/selectDesktop/list", method = RequestMethod.POST)
    public CommonWebResponse<List<UserGroupDesktopRelatedDTO>> listUserGroupDesktop(TaskSelectGroupDesktopRequest request,
            SessionContext sessionContext) throws BusinessException {
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

        TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO = new TaskSearchGroupDesktopRelatedDTO();
        taskSearchGroupDesktopRelatedDTO.setGroupIdList(userGroupIdList);

        List<UserGroupDesktopRelatedDTO> userGroupDesktopRelatedList =
                fileDistributionTaskManageAPI.listUserGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO);

        return CommonWebResponse.success(userGroupDesktopRelatedList);
    }
}
