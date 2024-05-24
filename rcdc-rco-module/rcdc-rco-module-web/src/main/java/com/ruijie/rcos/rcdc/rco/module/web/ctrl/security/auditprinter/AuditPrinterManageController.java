package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditprinter;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditPrinterMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportAuditApplyExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportPrintLogExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ViewAuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintLogStaticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.ViewAuditPrintLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditprinter.batchtask.UpdateAuditPrinterApplyBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.common.request.HandleApplyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
@Api(tags = "安全打印审计申请管理")
@Controller
@RequestMapping("/rco/security/auditPrinter")
public class AuditPrinterManageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditPrinterManageController.class);

    @Autowired
    private AuditPrinterMgmtAPI auditPrinterMgmtAPI;

    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private ExportAuditApplyExcelAPI exportAuditApplyExcelAPI;

    @Autowired
    private ExportPrintLogExcelAPI exportPrintLogExcelAPI;

    @Autowired
    private PermissionHelper permissionHelper;
    
    /**
     * 获取打印记录日志列表
     *
     * @param request 分页复杂查询
     * @param sessionContext session信息
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取打印记录日志列表")
    @RequestMapping(value = "/printLog/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，获取打印记录日志列表"})})
    @EnableAuthority
    public DefaultWebResponse list(PageQueryRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        generateUserGroupQueryRequest(builder, sessionContext);
        PageQueryResponse<ViewAuditPrintLogDTO> response = auditPrinterMgmtAPI.auditPrintLogPageQuery(builder.build());
        return DefaultWebResponse.Builder.success(response);
    }

    private void generateUserGroupQueryRequest(PageQueryBuilderFactory.RequestBuilder  pageReqBuilder,
                                               SessionContext sessionContext) throws BusinessException {
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] groupIdArr = permissionHelper.getUserGroupIdArr(sessionContext.getUserId());
            pageReqBuilder.in("groupId", groupIdArr);
        }
    }
    
    /**
     * 获取打印记录日志统计报表
     *
     * @param request 分页复杂查询
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取打印记录日志统计报表")
    @RequestMapping(value = "/printLogStatic/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，获取打印记录日志统计报表"})})
    public DefaultWebResponse printLogStaticList(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        PageQueryResponse<AuditPrintLogStaticDTO> response = auditPrinterMgmtAPI.auditPrintLogStaticPageQuery(builder.build());
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 获取安全打印审计申请列表
     *
     * @param request 分页复杂查询
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取安全打印审计申请列表")
    @RequestMapping(value = "/apply/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，获取安全打印审计申请列表"})})
    @EnableAuthority
    public DefaultWebResponse applyList(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        PageQueryResponse<ViewAuditApplyDTO> response = auditApplyMgmtAPI.auditApplyPageQuery(builder.build());
        return DefaultWebResponse.Builder.success(response);
    }


    /**
     * 获取安全打印审计申请单详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取安全打印审计申请单详情")
    @RequestMapping(value = {"/apply/detail", "/apply/getInfo"}, method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，获取安全打印审计申请单详情"})})
    @EnableAuthority
    public CommonWebResponse<AuditPrintApplyDetailDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        AuditPrintApplyDetailDTO auditPrintApplyDetailDTO = auditPrinterMgmtAPI.findAuditPrintApplyDetailById(idWebRequest.getId());
        return CommonWebResponse.success(auditPrintApplyDetailDTO);
    }

    /**
     * 审批安全打印审计申请单
     *
     * @param handleApplyRequest 入参
     * @param builder 批处理构造器
     * @param sessionContext session信息
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("审批安全打印审计申请单")
    @RequestMapping(value = {"/apply/approve", "/apply/reject"}, method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，审批安全打印审计申请单"})})
    @EnableAuthority
    public CommonWebResponse handleApply(HandleApplyRequest handleApplyRequest, BatchTaskBuilder builder,
                                         SessionContext sessionContext) throws BusinessException {
        Assert.notNull(handleApplyRequest, "updateAuditPrinterApplyRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID[] idArr = handleApplyRequest.getIdArr();

        AuditApplyAuditLogDTO auditApplyAuditLogDTO = new AuditApplyAuditLogDTO();
        auditApplyAuditLogDTO.setAuditorId(sessionContext.getUserId());
        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
        auditApplyAuditLogDTO.setAuditorName(baseAdminDTO.getUserName());
        auditApplyAuditLogDTO.setAuditorOpinion(handleApplyRequest.getAuditorOpinion());
        auditApplyAuditLogDTO.setAuditorState(handleApplyRequest.getAuditorState());

        if (idArr.length == 1) {
            auditApplyAuditLogDTO.setApplyId(idArr[0]);
            String applySerialNumber = auditApplyMgmtAPI.handleAuditApplyByAuditor(auditApplyAuditLogDTO);
            auditLogAPI.recordLog(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_AUDITOR_UPDATE_OPERATE_SUCCESS_LOG, applySerialNumber,
                    auditApplyAuditLogDTO.getAuditorState().getMessage());
            return CommonWebResponse.success(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_AUDITOR_UPDATE_OPERATE_SUCCESS_LOG,
                    new String[] {applySerialNumber, auditApplyAuditLogDTO.getAuditorState().getMessage()});
        } else {
            // 批量审批任务
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                    .itemName(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_BATCH_UPDATE_ITEM_NAME, new String[] {}).build()).iterator();
            UpdateAuditPrinterApplyBatchTaskHandler handler =
                    new UpdateAuditPrinterApplyBatchTaskHandler(this.auditApplyMgmtAPI, iterator, auditLogAPI);
            // 获取当前登录用户ID
            handler.setAuditApplyAuditLogDTO(auditApplyAuditLogDTO);
            BatchTaskSubmitResult result = builder.setTaskName(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_BATCH_UPDATE_TASK_NAME)
                    .setTaskDesc(AuditPrinterBusinessKey.RCDC_AUDIT_PRINTER_BATCH_UPDATE_DESC).registerHandler(handler).start();
            return CommonWebResponse.success(result);
        }
    }

    /**
     * 异步导出安全打印审计申请单数据到excel
     *
     * @param request 复杂查询
     * @param sessionContext Session上下文
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/apply/export", method = RequestMethod.POST)
    @ApiOperation("导出安全打印审计申请单")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，导出安全打印审计申请单"})})
    @EnableAuthority
    public CommonWebResponse export(PageQueryRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        exportAuditApplyExcelAPI.exportDataAsync(builder, userId);
        return CommonWebResponse.success();
    }

    /**
     * 获取导出报表结果
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出报表结果")
    @RequestMapping(value = "/apply/getExportResult", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，获取导出报表结果"})})
    @EnableAuthority
    public CommonWebResponse<ExportExcelCacheDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        ExportExcelCacheDTO exportExcelCacheDTO = exportAuditApplyExcelAPI.getExportDataCache(userId);
        return CommonWebResponse.success(exportExcelCacheDTO);
    }

    /**
     * 下载报表excel
     *
     * @param sessionContext SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下载报表excel")
    @RequestMapping(value = "/apply/downloadExportData", method = RequestMethod.GET)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，下载报表excel"})})
    @EnableAuthority
    public DownloadWebResponse downloadExportFile(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportExcelResponse exportFile = exportAuditApplyExcelAPI.getExportFile(userId);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(exportFile.getExportFile(), false).setName("exportAuditPrinterApply", "xlsx").build();
    }

    /**
     * 异步导出安全打印审计打印日志数据到excel
     *
     * @param request 复杂查询
     * @param sessionContext Session上下文
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/printLog/export", method = RequestMethod.POST)
    @ApiOperation("导出安全打印审计打印日志")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，导出安全打印审计打印日志"})})
    @EnableAuthority
    public CommonWebResponse printLogExport(PageQueryRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        exportPrintLogExcelAPI.exportDataAsync(builder, userId);
        return CommonWebResponse.success();
    }

    /**
     * 获取导出报表结果
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出报表结果")
    @RequestMapping(value = "/printLog/getExportResult", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，获取导出报表结果"})})
    @EnableAuthority
    public CommonWebResponse<ExportExcelCacheDTO> getPrintLogExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        ExportExcelCacheDTO exportExcelCacheDTO = exportPrintLogExcelAPI.getExportDataCache(userId);
        return CommonWebResponse.success(exportExcelCacheDTO);
    }

    /**
     * 下载报表excel
     *
     * @param sessionContext SessionContext
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下载报表excel")
    @RequestMapping(value = "/printLog/downloadExportData", method = RequestMethod.GET)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-安全打印审计，下载报表excel"})})
    @EnableAuthority
    public DownloadWebResponse downloadPrintLogExportFile(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportExcelResponse exportFile = exportPrintLogExcelAPI.getExportFile(userId);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(exportFile.getExportFile(), false).setName("exportAuditPrintLog", "xlsx").build();
    }

}

