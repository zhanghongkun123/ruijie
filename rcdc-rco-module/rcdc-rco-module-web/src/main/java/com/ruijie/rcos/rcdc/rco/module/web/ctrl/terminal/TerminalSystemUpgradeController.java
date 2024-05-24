package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.TerminalIdMappingUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.CheckAllowUploadUpgradePackageWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response.TerminalUpgradeItemArrayWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.*;
import com.ruijie.rcos.rcdc.rco.module.web.service.TerminalGroupHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalSystemUpgradeAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalSystemUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbSystemUpgradeTaskStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.BetweenTimeRangeMatch;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.MatchEqual;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.PageSearchRequest;
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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 终端系统升级
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月19日
 *
 * @author nt
 */
@Controller
@RequestMapping("/rco/terminal/system/upgrade")
@EnableCustomValidate(enable = false)
public class TerminalSystemUpgradeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalSystemUpgradeController.class);

    private static final String SYSTEM_UPGRADE_PACKAGE_ID_FIELD_NAME = "packageId";

    private static final String SYSTEM_UPGRADE_UPGRADE_TASK_STATE_FIELD_NAME = "upgradeTaskState";

    private static final String SYSTEM_UPGRADE_UPGRADE_TASK_ID_FIELD_NAME = "upgradeTaskId";

    private static final String SYSTEM_UPGRADE_UPGRADE_TASK_PACKAGE_TYPE_FIELD_NAME = "packageType";

    private static final String SYSTEM_UPGRADE_TERMINAL_UPGRADE_STATE_FIELD_NAME = "terminalUpgradeState";

    private static final String ERROR_MSG_SPERATOR = "，";

    private static final String TERMINAL_TYPE = "terminalType";

    private static final String TERMINAL_OS_TYPE = "terminalOsType";

    private static final Integer MAX_TERMINAL_SUPPORT = 10000;

    @Autowired
    private CbbTerminalSystemUpgradeAPI cbbTerminalUpgradeAPI;

    @Autowired
    private CbbTerminalSystemUpgradePackageAPI cbbTerminalUpgradePackageAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private TerminalUpgradeAPI terminalUpgradeAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    /**
     * 上传系统升级文件
     *
     * @param file 上传文件
     * @param taskBuilder 异步任务构造对象
     * @return 上传响应返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/package/createOTA")
    public DefaultWebResponse createOTA(ChunkUploadFile file, BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(file, "file can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_ITEM_NAME, //
                        file.getFileName()))
                .build();
        UploadUpgradePackageBatchTaskHandler upgradePackageHandler =
                new UploadUpgradePackageBatchTaskHandler(file, taskItem, auditLogAPI, cbbTerminalUpgradePackageAPI);

        BatchTaskSubmitResult result = taskBuilder.setTaskName(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_NAME, file.getFileName())
                .setTaskDesc(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_DESC, file.getFileName()) //
                .registerHandler(upgradePackageHandler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 上传系统升级文件
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/package/checkAllowUpload")
    public CheckAllowUploadUpgradePackageWebResponse checkAllowUploadPackage(CheckPackageAllowUploadWebRequest request) throws BusinessException {

        Assert.notNull(request, "request can not be null");

        CbbCheckAllowUploadPackageDTO checkRequest = new CbbCheckAllowUploadPackageDTO(request.getFileName(), request.getFileSize());
        checkRequest.setTerminalType(request.getTerminalType());
        CbbCheckAllowUploadPackageResultDTO response = cbbTerminalUpgradePackageAPI.checkAllowUploadPackage(checkRequest);

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
            errorMsg = LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_PACKAGE_UPLOAD_NOT_ALLOWED, new String[] {})
                    + StringUtils.join(errorList, ERROR_MSG_SPERATOR);
        }
        webResponse.setContent(new CheckAllowUploadContentVO(true, errorMsg));
        return webResponse;
    }

    /**
     * 上传系统升级文件
     *
     * @param request 请求参数
     * @param builder 批任务对象
     * @return 上传响应返回
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/package/delete")
    public DefaultWebResponse deletePackage(DeleteTerminalUpgradePackageWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        final UUID[] packageIdArr = request.getIdArr();

        if (packageIdArr.length == 1) {
            return deleteSingleUpgradePackage(packageIdArr[0], builder);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(packageIdArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                    .itemName(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_ITEM_NAME).build()).iterator();
            DeleteUpgradePackageBatchTaskHandler handler =
                    new DeleteUpgradePackageBatchTaskHandler(this.cbbTerminalUpgradePackageAPI, iterator, auditLogAPI);

            BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_TASK_NAME, new String[] {})
                    .setTaskDesc(TerminalBusinessKey.RCDC_DELETE_TERMINAL_UPGRADE_PACKAGE_TASK_DESC, new String[] {}) //
                    .registerHandler(handler).start();

            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleUpgradePackage(UUID packageId, BatchTaskBuilder builder) throws BusinessException {

        DefaultBatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(packageId)
                .itemName(LocaleI18nResolver.resolve(TerminalBusinessKey.RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_ITEM_NAME)).build();
        DeleteSingleUpgradePackageBatchTaskHandler handler =
                new DeleteSingleUpgradePackageBatchTaskHandler(cbbTerminalUpgradePackageAPI, batchTaskItem, auditLogAPI);
        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_NAME)
                .setTaskDesc(TerminalBusinessKey.RCDC_DELETE_SINGLE_TERMINAL_UPGRADE_PACKAGE_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 系统终端升级包信息列表
     *
     * @param request 分页请求信息
     * @return 分页列表信息
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/package/list")
    public DefaultWebResponse listPackage(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbTerminalSystemUpgradePackageInfoDTO[] dtoArr = cbbTerminalUpgradePackageAPI.listSystemUpgradePackage();
        List<UpgradePackageTaskDetailVO> detailContentVOList = Lists.newArrayList();

        for (CbbTerminalSystemUpgradePackageInfoDTO packageInfoDTO : dtoArr) {
            // 查询升级包对应的升级任务
            UpgradePackageTaskDetailVO detailContentVO = new UpgradePackageTaskDetailVO();
            if (packageInfoDTO.getUpgradeTaskId() != null) {
                detailContentVO = getUpgradePackageTaskDetailVO(packageInfoDTO.getUpgradeTaskId());
            }
            detailContentVO.updateFields(packageInfoDTO);
            detailContentVOList.add(detailContentVO);
        }

        UpgradePackageTaskDetailVO[] itemArr = detailContentVOList.toArray(new UpgradePackageTaskDetailVO[] {});
        TerminalUpgradeItemArrayWebResponse<UpgradePackageTaskDetailVO> webResponse = new TerminalUpgradeItemArrayWebResponse<>();
        webResponse.setItemArr(itemArr);
        return DefaultWebResponse.Builder.success(webResponse);
    }

    /**
     * 添加终端系统升级任务
     *
     * @param request 添加升级请求
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "create")
    public DefaultWebResponse create(CreateTerminalSystemUpgradeWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return createUpgradeTask(request);
    }

    /**
     * 创建刷机任务
     *
     * @param request 创建升级任务请求
     * @return 刷机任务id
     * @throws BusinessException 业务异常
     */
    private DefaultWebResponse createUpgradeTask(CreateTerminalSystemUpgradeWebRequest request) throws BusinessException {
        UUID packageId = request.getPackageId();
        String[] terminalIdArr = request.getTerminalIdArr();
        CbbAddSystemUpgradeTaskDTO addTaskRequest = new CbbAddSystemUpgradeTaskDTO();
        addTaskRequest.setPackageId(packageId);
        addTaskRequest.setTerminalIdArr(terminalIdArr);
        CbbAddSystemUpgradeTaskResultDTO response;
        String packageName = packageId.toString();
        try {
            packageName = getPackageName(packageId);
            checkCreateParam(request);
            response = cbbTerminalUpgradeAPI.addSystemUpgradeTask(addTaskRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_CREATE_UPGRADE_TERMINAL_TASK_SUCCESS_LOG, packageName);
            CreateSystemUpgradeTaskContentVO contentVO = new CreateSystemUpgradeTaskContentVO();
            contentVO.setUpgradeTaskId(response.getUpgradeTaskId());
            return DefaultWebResponse.Builder.success(contentVO);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_CREATE_UPGRADE_TERMINAL_TASK_FAIL_LOG, packageName, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    private void checkCreateParam(CreateTerminalSystemUpgradeWebRequest request) throws BusinessException {
        if (ArrayUtils.isEmpty(request.getTerminalIdArr()) && ArrayUtils.isEmpty(request.getTerminalGroupIdArr())) {
            // 创建终端升级任务时，任务中的终端和终端分组不可都未空
            throw new BusinessException(TerminalBusinessKey.RCDC_CREATE_UPGRADE_TERMINAL_TASK_TERMINAL_AND_GROUP_BOTH_EMPTY_ERROR);
        }
    }

    /**
     * 获取刷机任务列表
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse list(TerminalSystemUpgradeTaskListWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        PageSearchRequest apiRequest = new PageSearchRequest(request);
        BetweenTimeRangeMatch betweenTimeRangeMatch = new BetweenTimeRangeMatch(request.getStartTime(), request.getEndTime(), "createTime");
        apiRequest.setBetweenTimeRangeMatch(betweenTimeRangeMatch);

        convertListTaskMatchEqual(apiRequest);
        final DefaultPageResponse<CbbSystemUpgradeTaskDTO> resp = cbbTerminalUpgradeAPI.pageQuerySystemUpgradeTask(apiRequest);
        List<CbbSystemUpgradeTaskDTO> taskDTOList = Arrays.stream(resp.getItemArr()).collect(Collectors.toList());

        List<UpgradePackageTaskDetailVO> detailContentVOList = Lists.newArrayList();
        for (CbbSystemUpgradeTaskDTO upgradeTaskDTO : taskDTOList) {
            // 查询升级任务补充信息
            UpgradePackageTaskDetailVO detailContentVO = getUpgradePackageTaskDetailVO(upgradeTaskDTO.getId());
            detailContentVO.updateFields(upgradeTaskDTO);
            detailContentVOList.add(detailContentVO);
        }

        UpgradePackageTaskDetailVO[] itemArr = detailContentVOList.toArray(new UpgradePackageTaskDetailVO[] {});
        TerminalUpgradeItemArrayWebResponse<UpgradePackageTaskDetailVO> webResponse = new TerminalUpgradeItemArrayWebResponse<>();
        webResponse.setItemArr(itemArr);
        webResponse.setTotal(resp.getTotal());
        return DefaultWebResponse.Builder.success(webResponse);
    }

    private void convertListTaskMatchEqual(PageSearchRequest apiRequest) throws BusinessException {
        apiRequest.coverMatchEqualForUUID(SYSTEM_UPGRADE_PACKAGE_ID_FIELD_NAME);
        final MatchEqual[] matchEqualArr = apiRequest.getMatchEqualArr();
        if (ArrayUtils.isEmpty(matchEqualArr)) {
            return;
        }
        setPackageTypeMatchEqual(apiRequest);
        for (MatchEqual me : matchEqualArr) {
            if (SYSTEM_UPGRADE_UPGRADE_TASK_STATE_FIELD_NAME.equals(me.getName())) {
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
        return new MatchEqual(SYSTEM_UPGRADE_UPGRADE_TASK_PACKAGE_TYPE_FIELD_NAME, selectedTerminalTypeArr);
    }

    private UpgradePackageTaskDetailVO getUpgradePackageTaskDetailVO(UUID upgradeTaskId) throws BusinessException {
        PageSearchRequest apiRequest = new PageSearchRequest();
        apiRequest.setPage(0);
        apiRequest.setLimit(MAX_TERMINAL_SUPPORT);
        MatchEqual matchEqual = new MatchEqual(SYSTEM_UPGRADE_UPGRADE_TASK_ID_FIELD_NAME, new String[] {upgradeTaskId.toString()});
        apiRequest.setMatchEqualArr(new MatchEqual[] {matchEqual});
        convertListTerminalMatchEqual(apiRequest);
        DefaultPageResponse<CbbSystemUpgradeTaskTerminalDTO> resp = cbbTerminalUpgradeAPI.pageQuerySystemUpgradeTaskTerminal(apiRequest);

        UpgradePackageTaskDetailVO detailContentVO = new UpgradePackageTaskDetailVO();
        if (!ArrayUtils.isEmpty(resp.getItemArr())) {
            // 计算各状态下的终端数量
            UpgradeTaskDetailContentVO contentVO = new UpgradeTaskDetailContentVO();
            contentVO.setTotal(resp.getTotal());
            countTerminalNumByState(contentVO, resp.getItemArr());
            BeanUtils.copyProperties(contentVO, detailContentVO);
        }
        return detailContentVO;
    }

    /**
     * 获取刷机任务终端列表
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/terminal/list")
    public DefaultWebResponse listTerminal(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        final PageSearchRequest apiRequest = new PageSearchRequest(request);
        UUID upgradeTaskId = convertListTerminalMatchEqual(apiRequest);
        List<UpgradeTerminalDetailVO> detailVOList = Lists.newArrayList();
        long total = 0L;

        // taskID存在则查询相关信息
        if (upgradeTaskId != null) {
            final DefaultPageResponse<CbbSystemUpgradeTaskTerminalDTO> resp = cbbTerminalUpgradeAPI.pageQuerySystemUpgradeTaskTerminal(apiRequest);
            for (CbbSystemUpgradeTaskTerminalDTO terminalDTO : resp.getItemArr()) {
                CbbTerminalBasicInfoDTO terminalInfoResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalDTO.getTerminalId());
                UpgradeTerminalDetailVO detailVO = new UpgradeTerminalDetailVO();
                BeanUtils.copyProperties(terminalDTO, detailVO);
                detailVO.setProductType(terminalInfoResponse.getProductType());
                detailVO.setRainOsVersion(terminalInfoResponse.getRainOsVersion());
                detailVO.setTerminalOsType(terminalInfoResponse.getTerminalOsType());
                detailVO.parseNetworkInfoArr(terminalInfoResponse.getNetworkInfoArr());
                // 终端工作模式添加 返回VOI类型 针对windwos +VOI 可以点击添加升级使用
                detailVO.setTerminalPlatform(terminalInfoResponse.getTerminalPlatform());
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
     * 获取刷机任务终端列表
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse detail(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        final PageSearchRequest apiRequest = new PageSearchRequest(request);
        UUID upgradeTaskId = convertListTerminalMatchEqual(apiRequest);
        if (upgradeTaskId == null) {
            throw new BusinessException(TerminalBusinessKey.RCDC_COMMON_REQUEST_PARAM_ERROR);
        }
        final DefaultPageResponse<CbbSystemUpgradeTaskTerminalDTO> resp = cbbTerminalUpgradeAPI.pageQuerySystemUpgradeTaskTerminal(apiRequest);

        final CbbSystemUpgradeTaskDTO dto = cbbTerminalUpgradeAPI.findTerminalUpgradeTaskById(upgradeTaskId);

        final DefaultPageResponse<CbbTerminalGroupDetailDTO> groupResp = cbbTerminalUpgradeAPI.pageQuerySystemUpgradeTaskTerminalGroup(apiRequest);

        return DefaultWebResponse.Builder.success(buildUpgradeTerminalListVO(resp.getItemArr(), resp.getTotal(), dto, groupResp.getItemArr()));
    }

    private UpgradeTaskDetailContentVO buildUpgradeTerminalListVO(CbbSystemUpgradeTaskTerminalDTO[] terminalArr, long total,
            CbbSystemUpgradeTaskDTO upgradeTask, CbbTerminalGroupDetailDTO[] terminalGroupArr) {
        UpgradeTaskDetailContentVO contentVO = new UpgradeTaskDetailContentVO();
        contentVO.setTerminalArr(terminalArr);
        contentVO.setTotal(total);
        contentVO.setUpgradeTask(upgradeTask);
        countTerminalNumByState(contentVO, terminalArr);

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

    private void countTerminalNumByState(UpgradeTaskDetailContentVO contentVO, CbbSystemUpgradeTaskTerminalDTO[] itemArr) {
        if (itemArr == null || itemArr.length == 0) {
            return;
        }
        for (CbbSystemUpgradeTaskTerminalDTO upgradeTerminal : itemArr) {
            upgradeTerminal.getTerminalUpgradeState();
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
        apiRequest.coverMatchEqualForUUID(SYSTEM_UPGRADE_UPGRADE_TASK_ID_FIELD_NAME);
        final MatchEqual[] matchEqualArr = apiRequest.getMatchEqualArr();
        if (ArrayUtils.isEmpty(matchEqualArr)) {
            // 没有匹配项则返回空
            return null;
        }
        UUID upgradeTaskId = null;
        for (MatchEqual me : matchEqualArr) {
            final String name = me.getName();
            final Object[] valueArr = me.getValueArr();
            if (SYSTEM_UPGRADE_UPGRADE_TASK_ID_FIELD_NAME.equals(name) && valueArr.length > 0) {
                upgradeTaskId = UUID.fromString(String.valueOf(valueArr[0]));
            }
            if (SYSTEM_UPGRADE_TERMINAL_UPGRADE_STATE_FIELD_NAME.equals(name)) {
                CbbSystemUpgradeStateEnums[] stateArr = new CbbSystemUpgradeStateEnums[valueArr.length];
                for (int i = 0; i < valueArr.length; i++) {
                    stateArr[i] = CbbSystemUpgradeStateEnums.valueOf(String.valueOf(valueArr[i]));
                }
                me.setValueArr(stateArr);
            }
        }
        return upgradeTaskId;
    }

    /**
     * 关闭刷机任务
     *
     * @param request 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "close")
    public DefaultWebResponse close(CloseSystemUpgradeTaskWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String packageName = upgradeTaskId.toString();
        try {
            final CbbSystemUpgradeTaskDTO upgradeTaskDTO = cbbTerminalUpgradeAPI.findTerminalUpgradeTaskById(upgradeTaskId);
            packageName = upgradeTaskDTO.getPackageName();
            cbbTerminalUpgradeAPI.closeSystemUpgradeTask(upgradeTaskId);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_UPGRADE_TERMINAL_TASK_CLOSE_SUCCESS_LOG, packageName);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_UPGRADE_TERMINAL_TASK_CLOSE_FAIL_LOG, packageName, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 取消刷机任务（等待中的升级终端）
     *
     * @param request 请求参数
     * @param builder 批任务对象
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/cancel")
    public DefaultWebResponse cancel(CancelTerminalSystemUpgradeWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String[] terminalIdArr = request.getTerminalIdArr();

        if (terminalIdArr.length == 1) {
            return cancelSingleUpgradeTerminal(terminalIdArr[0], upgradeTaskId);
        }

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<TerminalUpgradeBatchTaskItem> iterator = Stream.of(idArr)
                .map(id -> buildTerminalUpgradeItem(upgradeTaskId, TerminalBusinessKey.RCDC_CANCEL_UPGRADE_TERMINAL_ITEM_NAME, id)).iterator();
        CancelUpgradeTerminalBatchTaskHandler handler =
                new CancelUpgradeTerminalBatchTaskHandler(this.cbbTerminalUpgradeAPI, idMap, iterator, auditLogAPI, cbbTerminalOperatorAPI);

        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_CANCEL_UPGRADE_TERMINAL_TASK_NAME, new String[] {}) //
                .setTaskDesc(TerminalBusinessKey.RCDC_CANCEL_UPGRADE_TERMINAL_TASK_DESC, new String[] {}) //
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse cancelSingleUpgradeTerminal(String terminalId, UUID upgradeTaskId) throws BusinessException {
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        CbbUpgradeTerminalDTO cancelRequest = new CbbUpgradeTerminalDTO();
        cancelRequest.setTerminalId(terminalId);
        cancelRequest.setUpgradeTaskId(upgradeTaskId);
        try {
            cbbTerminalUpgradeAPI.cancelUpgradeTerminal(cancelRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_CANCEL_UPGRADE_TERMINAL_SUCCESS_LOG, terminalAddr);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_CANCEL_UPGRADE_TERMINAL_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_CANCEL_UPGRADE_TERMINAL_FAIL_LOG, terminalAddr, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_CANCEL_UPGRADE_TERMINAL_FAIL, e, e.getI18nMessage());
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
    @RequestMapping(value = "/retry")
    public DefaultWebResponse retry(RetryTerminalSystemUpgradeWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String[] terminalIdArr = request.getTerminalIdArr();

        if (terminalIdArr.length == 1) {
            return retrySingleUpgradeTerminal(terminalIdArr[0], upgradeTaskId);
        }

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<TerminalUpgradeBatchTaskItem> iterator = Stream.of(idArr)
                .map(id -> buildTerminalUpgradeItem(upgradeTaskId, TerminalBusinessKey.RCDC_RETRY_UPGRADE_TERMINAL_ITEM_NAME, id)).iterator();
        RetryUpgradeTerminalBatchTaskHandler handler =
                new RetryUpgradeTerminalBatchTaskHandler(this.cbbTerminalUpgradeAPI, idMap, iterator, auditLogAPI, cbbTerminalOperatorAPI);

        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_RETRY_UPGRADE_TERMINAL_TASK_NAME, new String[] {})
                .setTaskDesc(TerminalBusinessKey.RCDC_RETRY_UPGRADE_TERMINAL_TASK_DESC, new String[] {}) //
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private DefaultWebResponse retrySingleUpgradeTerminal(String terminalId, UUID upgradeTaskId) throws BusinessException {
        CbbUpgradeTerminalDTO retryRequest = new CbbUpgradeTerminalDTO();
        retryRequest.setTerminalId(terminalId);
        retryRequest.setUpgradeTaskId(upgradeTaskId);
        try {
            cbbTerminalUpgradeAPI.retryUpgradeTerminal(retryRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_RETRY_UPGRADE_TERMINAL_SUCCESS_LOG, terminalId.toUpperCase());
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_RETRY_UPGRADE_TERMINAL_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_RETRY_UPGRADE_TERMINAL_FAIL_LOG, terminalId.toUpperCase(), e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_RETRY_UPGRADE_TERMINAL_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 追加刷机终端及分组
     *
     * @param request 请求参数
     * @param builder 批任务对象
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "append")
    public DefaultWebResponse append(AppendTerminalSystemUpgradeWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        final UUID upgradeTaskId = request.getUpgradeTaskId();
        String[] terminalIdArr = request.getTerminalIdArr();

        if (!ArrayUtils.isEmpty(request.getTerminalGroupIdArr())) {
            editSystemUpgradeGroup(upgradeTaskId, request.getTerminalGroupIdArr());
        }

        if (ArrayUtils.isEmpty(terminalIdArr)) {
            return DefaultWebResponse.Builder.success();
        }

        if (terminalIdArr.length == 1) {
            return appendSingleTerminal(upgradeTaskId, terminalIdArr[0]);
        }

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<TerminalUpgradeBatchTaskItem> iterator = Stream.of(idArr)
                .map(id -> buildTerminalUpgradeItem(upgradeTaskId, TerminalBusinessKey.RCDC_ADD_UPGRADE_TERMINAL_ITEM_NAME, id)).iterator();
        AddUpgradeTerminalBatchTaskHandler handler =
                new AddUpgradeTerminalBatchTaskHandler(this.cbbTerminalUpgradeAPI, idMap, iterator, auditLogAPI, cbbTerminalOperatorAPI);

        BatchTaskSubmitResult result = builder.setTaskName(TerminalBusinessKey.RCDC_ADD_UPGRADE_TERMINAL_TASK_NAME, new String[] {})
                .setTaskDesc(TerminalBusinessKey.RCDC_ADD_UPGRADE_TERMINAL_TASK_DESC, new String[] {}) //
                .registerHandler(handler).start();

        return DefaultWebResponse.Builder.success(result);
    }

    private void editSystemUpgradeGroup(UUID upgradeTaskId, UUID[] terminalGroupIdArr) {
        CbbUpgradeTerminalGroupDTO cbbRequest = new CbbUpgradeTerminalGroupDTO();
        cbbRequest.setTerminalGroupIdArr(terminalGroupIdArr);
        cbbRequest.setUpgradeTaskId(upgradeTaskId);
        String upgradePackageName = upgradeTaskId.toString();
        try {
            CbbSystemUpgradeTaskDTO upgradeTaskDTO = cbbTerminalUpgradeAPI.findTerminalUpgradeTaskById(upgradeTaskId);
            Assert.notNull(upgradeTaskDTO, "upgrade task can not be null");
            upgradePackageName = upgradeTaskDTO.getPackageName();
            cbbTerminalUpgradeAPI.editSystemUpgradeTerminalGroup(cbbRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_EDIT_UPGRADE_TERMINAL_GROUP_SUCCESS_LOG, upgradePackageName);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_EDIT_UPGRADE_TERMINAL_GROUP_FAIL_LOG, upgradePackageName, e.getI18nMessage());
        }
    }

    private DefaultWebResponse appendSingleTerminal(UUID upgradeTaskId, String terminalId) throws BusinessException {
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        CbbUpgradeTerminalDTO addRequest = new CbbUpgradeTerminalDTO();
        addRequest.setTerminalId(terminalId);
        addRequest.setUpgradeTaskId(upgradeTaskId);
        try {
            cbbTerminalUpgradeAPI.addSystemUpgradeTerminal(addRequest);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_ADD_UPGRADE_TERMINAL_SUCCESS_LOG, terminalAddr);
            return DefaultWebResponse.Builder.success(TerminalBusinessKey.RCDC_ADD_UPGRADE_TERMINAL_SUCCESS, new String[] {});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_ADD_UPGRADE_TERMINAL_FAIL_LOG, terminalAddr, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_ADD_UPGRADE_TERMINAL_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 构建刷机任务终端任务项
     *
     * @param upgradeTaskId 刷机任务id
     * @param id 终端id映射uuid
     * @param businessKey 任务项key
     * @return 任务项对象
     */
    private TerminalUpgradeBatchTaskItem buildTerminalUpgradeItem(final UUID upgradeTaskId, String businessKey, UUID id) {
        return new TerminalUpgradeBatchTaskItem(id, LocaleI18nResolver.resolve(businessKey), upgradeTaskId);
    }

    /**
     * 获取可刷机的终端列表
     *
     * @param request 请求参数
     * @param sessionContext 会话上下文
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/basicInfo/terminal/list")
    public DefaultWebResponse listTerminalBasicInfo(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        PageSearchRequest pageSearchRequest = new PageSearchRequest(request);
        // 查询是否有全部的权限 有则不进行限制条件
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
        PageQueryResponse<CbbUpgradeableTerminalListDTO> pageResp = terminalUpgradeAPI.findUpgradeableTerminals(pageSearchRequest);
        return DefaultWebResponse.Builder.success(pageResp);
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
        return "";
    }

    /**
     * 获取终端组权限集合
     *
     * @param adminId
     * @return
     * @throws BusinessException
     */
    private List<String> getPermissionTerminalGroupIdList(UUID adminId) throws BusinessException {
        ListTerminalGroupIdRequest listTerminalGroupIdRequest = new ListTerminalGroupIdRequest();
        listTerminalGroupIdRequest.setAdminId(adminId);
        ListTerminalGroupIdResponse listTerminalGroupIdResponse = adminDataPermissionAPI.listTerminalGroupIdByAdminId(listTerminalGroupIdRequest);
        return listTerminalGroupIdResponse.getTerminalGroupIdList();
    }

    /**
     * 添加所拥有的终端组集合
     *
     * @param request
     * @param userGroupIdStrList
     * @throws BusinessException
     */
    private void appendTerminalGroupIdMatchEqual(PageSearchRequest request, List<String> userGroupIdStrList) throws BusinessException {
        List<UUID> uuidList = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(TerminalGroupHelper.TERMINAL_GROUP_ROOT_ID))
                .map(UUID::fromString).collect(Collectors.toList());
        UUID[] uuidArr = uuidList.toArray(new UUID[uuidList.size()]);
        request.appendCustomMatchEqual(new MatchEqual("terminalGroupIdArr", uuidArr));
    }

    private String getPackageName(UUID packageId) {
        try {

            CbbTerminalSystemUpgradePackageInfoDTO upgradePackageInfoDTO = cbbTerminalUpgradePackageAPI.findById(packageId);
            return upgradePackageInfoDTO.getName();
        } catch (BusinessException e) {
            LOGGER.info("获取升级包名称异常", e);
            return packageId.toString();
        }
    }
}
