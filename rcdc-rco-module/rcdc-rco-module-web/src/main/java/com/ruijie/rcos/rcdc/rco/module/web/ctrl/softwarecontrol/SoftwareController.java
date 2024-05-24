package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol;

import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportSoftwareAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftwarePageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.batchtask.CreateSoftwareBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.batchtask.ImportSoftwareBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.dto.ImportSoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.response.CheckNameDuplicationForSoftwareControlResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.ImportUserWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportSoftwareHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@Api(tags = "软件管控软件管理")
@Controller
@RequestMapping("/rco/software")
public class SoftwareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareController.class);

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private ExportSoftwareAPI exportSoftwareAPI;

    @Autowired
    private ImportSoftwareHandler importSoftwareHandler;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 小工具下载路径
     */
    private static final String SOFTWARE_CONTROL_TOOL_PATH = "template/";

    /**
     * 小工具文件名称
     */
    private static final String SOFTWARE_CONTROL_TOOL_FILE_NAME = "RG-GetSoftwareInfo";

    private static final String SYMBOL_SPOT = ".";

    /**
     * 小工具文件类型
     */
    private static final String SOFTWARE_CONTROL_TOOL_FILE_TYPE = "zip";

    /**
     * 导出软件信息文件名
     */
    private static final String EXPORT_SOFTWARE_FILENAME = "exportSoftware";

    /**
     * *查询
     *
     * @param request        页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("软件列表")
    public CommonWebResponse<DefaultPageResponse<SoftwareDTO>> list(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");
        SoftwarePageSearchRequest softwarePageSearchRequest = new SoftwarePageSearchRequest(request);
        softwarePageSearchRequest.setSortArr(request.getSort() != null ? Arrays.asList(request.getSort()).toArray(new Sort[0]) : null);
        String keyword = softwarePageSearchRequest.getSearchKeyword();
        if (StringUtils.hasText(keyword)) {
            softwarePageSearchRequest.setSearchKeyword(keyword.toLowerCase());
        }
        return CommonWebResponse.success(softwareControlMgmtAPI.softwarePageQuery(softwarePageSearchRequest));
    }

    /**
     * 移动软件
     *
     * @param moveSoftwareRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("移动软件")
    @RequestMapping(value = "move", method = RequestMethod.POST)
    public CommonWebResponse move(MoveSoftwareRequest moveSoftwareRequest) throws BusinessException {
        Assert.notNull(moveSoftwareRequest, "moveSoftwareRequest must not be null");
        softwareControlMgmtAPI.isImportingSoftware();

        //判断源组是否存在
        UUID sourceGroupId = moveSoftwareRequest.getSourceGroupId();
        SoftwareGroupDTO softwareGroupDTO = softwareControlMgmtAPI.findSoftwareGroupById(sourceGroupId);
        //判断目标组是否存在
        UUID targetGroupId = moveSoftwareRequest.getTargetGroupId();
        SoftwareGroupDTO targetGroupIdGroupDTO = softwareControlMgmtAPI.findSoftwareGroupById(targetGroupId);

        UUID[] idArr = moveSoftwareRequest.getIdArr();
        List<List<UUID>> idArrList = ListRequestHelper.subArray(idArr, Constants.OPERATE_MAX_SIZE);
        for (List<UUID> subIdList : idArrList) {
            softwareControlMgmtAPI.moveSoftware(subIdList, sourceGroupId, targetGroupId);
            List<SoftwareDTO> softwareDTOList = softwareControlMgmtAPI.findSoftwareByIdIn(subIdList);
            softwareDTOList.stream().forEach((softwareDTO) -> {
                //记录审计日志
                auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_GROUP_MOVE_SOFTWARE,
                        new String[]{softwareDTO.getName(), softwareGroupDTO.getName(), targetGroupIdGroupDTO.getName()});
            });
        }
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 编辑软件
     *
     * @param createSoftwareRequest 入参
     * @param builder               批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑软件")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public CommonWebResponse edit(CreateSoftwareRequest createSoftwareRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createSoftwareRequest, "createSoftwareRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        SoftwareDTO softwareExistDTO = softwareControlMgmtAPI.findSoftwareById(createSoftwareRequest.getId());
        if (!validateWhiteSoftwareFieldMoreThanOne(createSoftwareRequest, softwareExistDTO.getDirectoryFlag())) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_FIELD_ALL_NULL, new String[]{});
        }
        if (!validateBlackSoftwareFieldMoreThanOne(createSoftwareRequest, softwareExistDTO.getDirectoryFlag())) {
            throw new BusinessException(SoftwareControlBusinessKey.RCDC_RCO_BLACK_SOFTWARE_FIELD_ALL_NULL, new String[]{});
        }
        SoftwareDTO softwareDTO = new SoftwareDTO();
        BeanUtils.copyProperties(createSoftwareRequest, softwareDTO);
        softwareControlMgmtAPI.editSoftware(softwareDTO);

        //记录审计日志
        auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_EDIT,
                new String[]{createSoftwareRequest.getName()});
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }


    /**
     * 删除软件
     *
     * @param idArrWebRequest 入参
     * @param builder         批量任务创建对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除软件")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be null");
        // 批量删除任务
        for (UUID uuid : idArr) {
            SoftwareDTO dto = softwareControlMgmtAPI.findSoftwareById(uuid);
            softwareControlMgmtAPI.deleteSoftware(uuid);
            //记录审计日志
            auditLogAPI.recordLog(SoftwareControlBusinessKey.RCDC_RCO_SOFTWARE_DELETE,
                    new String[]{dto.getName()});
        }
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 获取软件详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取软件详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse<SoftwareDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        SoftwareDTO softwareGroupDTO = softwareControlMgmtAPI.findSoftwareById(idWebRequest.getId());
        return CommonWebResponse.success(softwareGroupDTO);
    }


    /**
     * 检查软件名称是否重复
     *
     * @param checkNameDuplicationRequest 入参
     * @return 响应
     */
    @ApiOperation("检查软件名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationForSoftwareControlResponse> checkNameDuplication(
            CheckSoftwareNameDuplicationRequest checkNameDuplicationRequest) {
        Assert.notNull(checkNameDuplicationRequest, "checkNameDuplicationRequest must not be null");
        CheckNameDuplicationForSoftwareControlResponse checkNameDuplicationResponse = new CheckNameDuplicationForSoftwareControlResponse(false);
        String name = checkNameDuplicationRequest.getName();
        UUID id = checkNameDuplicationRequest.getId();
        Boolean hasDuplication = softwareControlMgmtAPI.checkSoftwareNameDuplication(id, name);
        checkNameDuplicationResponse.setHasDuplication(hasDuplication);
        return CommonWebResponse.success(checkNameDuplicationResponse);
    }

    /**
     * 小工具下载
     *
     * @return 返回结果
     * @throws IOException 异常
     */
    @ApiOperation("小工具下载")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    @NoAuthUrl
    public DownloadWebResponse downloadTool() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(SOFTWARE_CONTROL_TOOL_PATH + SOFTWARE_CONTROL_TOOL_FILE_NAME + SYMBOL_SPOT + SOFTWARE_CONTROL_TOOL_FILE_TYPE);
        byte[] templateByteArr = IOUtils.toByteArray(inputStream);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setInputStream(new ByteArrayInputStream(templateByteArr), templateByteArr.length)
                .setName(SOFTWARE_CONTROL_TOOL_FILE_NAME, SOFTWARE_CONTROL_TOOL_FILE_TYPE).build();
    }

    /**
     * 检查白名单软件信息项至少为一 且勾选
     *
     * @param request 入参
     * @return true 校验通过 false 校验不通过
     */
    private boolean validateWhiteSoftwareFieldMoreThanOne(CreateSoftwareRequest request, Boolean directoryFlag) {
        // mvp版本不做软件白名单运行查杀，所以不收集安装路径信息
        return (directoryFlag
                || StringUtils.isNotBlank(request.getDigitalSign()) && Boolean.TRUE.equals(request.getDigitalSignFlag()))
                || (StringUtils.isNotBlank(request.getProductName()) && Boolean.TRUE.equals(request.getProductNameFlag()))
                || (StringUtils.isNotBlank(request.getProcessName()) && Boolean.TRUE.equals(request.getProcessNameFlag()))
                || (StringUtils.isNotBlank(request.getOriginalFileName()) && Boolean.TRUE.equals(request.getOriginalFileNameFlag()))
                || (StringUtils.isNotBlank(request.getFileCustomMd5()) && Boolean.TRUE.equals(request.getFileCustomMd5Flag()));
    }

    /**
     * 检查黑名单软件信息项至少为一 且勾选
     *
     * @param request 入参
     * @return true 校验通过 false 校验不通过
     */
    private boolean validateBlackSoftwareFieldMoreThanOne(CreateSoftwareRequest request, Boolean directoryFlag) {
        // mvp版本不做软件白名单运行查杀，所以不收集安装路径信息
        return (directoryFlag
                || StringUtils.isNotBlank(request.getDigitalSign()) && Boolean.TRUE.equals(request.getDigitalSignBlackFlag()))
                || (StringUtils.isNotBlank(request.getProductName()) && Boolean.TRUE.equals(request.getProductNameBlackFlag()))
                || (StringUtils.isNotBlank(request.getProcessName()) && Boolean.TRUE.equals(request.getProcessNameBlackFlag()))
                || (StringUtils.isNotBlank(request.getOriginalFileName()) && Boolean.TRUE.equals(request.getOriginalFileNameBlackFlag()))
                || (StringUtils.isNotBlank(request.getFileCustomMd5()) && Boolean.TRUE.equals(request.getFileCustomMd5BlackFlag()));
    }



    /**
     * 异步导出软件白名单数据到excel
     *
     * @param sessionContext           Session上下文
     * @param exportSoftwareWebRequest  请求参数
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出软件白名单")
    public CommonWebResponse export(ExportSoftwareWebRequest exportSoftwareWebRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(exportSoftwareWebRequest, "exportSoftwareWebRequest is null");
        Assert.notNull(sessionContext, "sessionContext is null");

        // 增加在某个软件组导入的场景
        Assert.notNull(sessionContext, "sessionContext is null");
        Sort[] sortArr = generateSortArr(null);
        String userId = sessionContext.getUserId().toString();
        ExportSoftwareRequest request = new ExportSoftwareRequest(userId, sortArr, exportSoftwareWebRequest.getGroupId());
        exportSoftwareAPI.exportDataAsync(request);
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }


    /**
     * 获取软件数据导出任务情况
     *
     * @param exportSoftwareWebRequest 请求参数
     * @param sessionContext           sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportSoftwareCacheDTO> getExportResult(ExportSoftwareWebRequest exportSoftwareWebRequest,
                                                                     SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        Assert.notNull(exportSoftwareWebRequest, "exportSoftwareWebRequest is null");

        String userId = sessionContext.getUserId().toString();
        ExportSoftwareRequest request = new ExportSoftwareRequest(userId, exportSoftwareWebRequest.getGroupId());
        GetExportSoftwareCacheResponse response = exportSoftwareAPI.getExportDataCache(request);
        return CommonWebResponse.success(response.getSoftwareCacheDTO());
    }


    /**
     * 下载软件数据excel
     *
     * @param emptyDownloadWebRequest 页面请求参数
     * @param sessionContext          SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(EmptyDownloadWebRequest emptyDownloadWebRequest
            , SessionContext sessionContext) throws BusinessException {
        Assert.notNull(emptyDownloadWebRequest, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        ExportSoftwareRequest request = new ExportSoftwareRequest(userId, emptyDownloadWebRequest.getGroupId());
        GetExportSoftwareFileResponse exportFile = exportSoftwareAPI.getExportFile(request);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(exportFile.getExportFile(), false).setName(EXPORT_SOFTWARE_FILENAME, "xlsx").build();
    }


    private Sort[] generateSortArr(Sort[] sortArr) {
        if (sortArr == null) {
            Sort sortFirst = new Sort();
            sortFirst.setSortField("groupId");
            sortFirst.setDirection(Sort.Direction.DESC);

            return new Sort[]{sortFirst};
        }
        List<Sort> sortList = new ArrayList<>();
        for (Sort sort : sortArr) {
            sortList.add(sort);
        }
        return sortList.toArray(new Sort[sortList.size()]);
    }


    /**
     * 导入软件数据
     *
     * @param file    导入的文件对象
     * @param builder 批人数处理
     * @return 返回WebResponse 对象
     * @throws IOException       IO异常
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导入软件数据")
    @RequestMapping(value = "importSoftware", method = RequestMethod.POST)
    public ImportUserWebResponse importSoftware(ChunkUploadFile file, BatchTaskBuilder builder) throws BusinessException, IOException {
        Assert.notNull(file, "file is not null");
        Assert.notNull(builder, "builder is not null");
        // 增加在某个软件组导入的场景
        UUID targetGroupId = null;
        if (file.getCustomData() != null && file.getCustomData().getString("targetGroupId") != null) {
            targetGroupId = UUID.fromString(file.getCustomData().getString("targetGroupId"));
        }
        List<ImportSoftwareDTO> importSoftwareList = importSoftwareHandler.getImportDataList(file);
        importSoftwareHandler.validate(importSoftwareList);
        try {
            // 开始导入软件，如果已存在导入任务不允许导入
            softwareControlMgmtAPI.isImportingSoftware();
            softwareControlMgmtAPI.startAddSoftwareData();
            //重新构造导入数据
            importSoftwareList = reStructureSoftwareList(importSoftwareList);

            // 校验通过，开始执行批量任务
            ImportSoftwareDTO[] softwareArr = importSoftwareList.toArray(new ImportSoftwareDTO[importSoftwareList.size()]);
            final Iterator<CreateSoftwareBatchTaskItem> iterator = Stream.of(softwareArr)
                    .map(software -> CreateSoftwareBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                            .itemName(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_ITEM_NAME, new String[]{}).itemSoftware(software).build())
                    .iterator();
            ImportSoftwareBatchTaskHandlerRequest request = new ImportSoftwareBatchTaskHandlerRequest();
            request.setBatchTaskItemIterator(iterator);
            request.setAuditLogAPI(auditLogAPI);
            request.setSoftwareControlMgmtAPI(softwareControlMgmtAPI);
            request.setTargetGroupId(targetGroupId);
            ImportSoftwareBatchTaskHandler handler = new ImportSoftwareBatchTaskHandler(request);
            BatchTaskSubmitResult batchTaskSubmitResult = builder.setTaskName(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_TASK_NAME)
                    .setTaskDesc(SoftwareControlBusinessKey.RCDC_RCO_IMPORT_SOFTWARE_TASK_DESC).registerHandler(handler).start();
            ImportUserWebResponse result = new ImportUserWebResponse();
            result.setStatus(WebResponse.Status.SUCCESS);
            result.setContent(batchTaskSubmitResult);
            return result;
        } catch (BusinessException e) {
            softwareControlMgmtAPI.finishAddSoftwareData();
            throw e;
        }
    }

    /**
     * 重新设置导入清单里的 绿色软件的上下级映射关系
     *
     * @param importSoftwareList
     */
    private List<ImportSoftwareDTO> reStructureSoftwareList(List<ImportSoftwareDTO> importSoftwareList) {
        if (!importSoftwareList.isEmpty()) {

            List<ImportSoftwareDTO> newImportSoftwareDTOList = new ArrayList<>(importSoftwareList.size());
            Map<String, List<ImportSoftwareDTO>> greenSoftwareChildrenMap = new HashMap<>(importSoftwareList.size());
            importSoftwareList.forEach(dto -> {
                //过滤所有绿色软件目录下的软件列表，并归类
                if (!SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(dto.getDirectoryFlag()) && dto.getParentId() != null) {
                    List<ImportSoftwareDTO> childSoftwareList = greenSoftwareChildrenMap.get(dto.getParentId());
                    if (childSoftwareList == null) {
                        childSoftwareList = new ArrayList<>();
                    }
                    childSoftwareList.add(dto);
                    greenSoftwareChildrenMap.put(dto.getParentId(), childSoftwareList);
                }
            });

            //重新构造数据，绿色软件算一个软件
            importSoftwareList.forEach(dto -> {
                if (dto.getParentId() == null) {
                    //确认为绿色软件
                    if (SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(dto.getDirectoryFlag())) {
                        dto.setChildrenList(greenSoftwareChildrenMap.get(dto.getId()));
                    }
                    newImportSoftwareDTOList.add(dto);
                }
            });
            return newImportSoftwareDTOList;
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}

