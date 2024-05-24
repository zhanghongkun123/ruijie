package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportAuditApplyExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ViewAuditApplyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.response.GetExportExcelResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile.batchtask.DiscardAuditFileApplyBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile.batchtask.UpdateAuditFileApplyBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.common.request.HandleApplyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
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
@Api(tags = "文件导出审批申请管理")
@Controller
@RequestMapping("/rco/security/auditFileApply")
public class AuditFileApplyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditFileApplyController.class);

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
    private PermissionHelper permissionHelper;

    /**
     * 获取文件导出审批申请单详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取文件导出审批申请单详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-导出文件审计，获取文件导出审计申请单详情"})})
    @EnableAuthority
    public CommonWebResponse<AuditApplyDetailDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        AuditApplyDetailDTO auditApplyDetailDTO = auditApplyMgmtAPI.findAuditApplyDetailById(idWebRequest.getId());
        return CommonWebResponse.success(auditApplyDetailDTO);
    }

    /**
     * 审批文件导出审批申请单
     *
     * @param handleApplyRequest 入参
     * @param builder 批处理构造器
     * @param sessionContext session信息
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("审批文件导出审批申请单")
    @RequestMapping(value = {"approve", "reject"}, method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-导出文件审计，审批文件导出审计申请单"})})
    @EnableAuthority
    public CommonWebResponse handleApply(HandleApplyRequest handleApplyRequest, BatchTaskBuilder builder,
                                         SessionContext sessionContext) throws BusinessException {
        Assert.notNull(handleApplyRequest, "handleApplyRequest must not be null");
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
            auditLogAPI.recordLog(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_UPDATE_OPERATE_SUCCESS_LOG, applySerialNumber,
                    auditApplyAuditLogDTO.getAuditorState().getMessage());
            return CommonWebResponse.success(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_UPDATE_OPERATE_SUCCESS_LOG,
                    new String[] {applySerialNumber, auditApplyAuditLogDTO.getAuditorState().getMessage()});
        } else {
            // 批量审批任务
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                    .itemName(AuditFileBusinessKey.RCDC_AUDIT_FILE_BATCH_UPDATE_ITEM_NAME, new String[] {}).build()).iterator();
            UpdateAuditFileApplyBatchTaskHandler handler = new UpdateAuditFileApplyBatchTaskHandler(this.auditApplyMgmtAPI, iterator, auditLogAPI);
            // 获取当前登录用户ID
            handler.setAuditApplyAuditLogDTO(auditApplyAuditLogDTO);
            BatchTaskSubmitResult result = builder.setTaskName(AuditFileBusinessKey.RCDC_AUDIT_FILE_BATCH_UPDATE_TASK_NAME)
                    .setTaskDesc(AuditFileBusinessKey.RCDC_AUDIT_FILE_BATCH_UPDATE_DESC).registerHandler(handler).start();
            return CommonWebResponse.success(result);
        }
    }

    /**
     * 管理员废弃申请单
     * @param handleApplyRequest 入参
     * @param builder 批处理构造器
     * @param sessionContext session信息
     * @return 相应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("废弃申请单")
    @RequestMapping(value = {"discard"}, method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-废弃申请单"})})
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> discardApply(IdArrWebRequest handleApplyRequest, BatchTaskBuilder builder,
                                          SessionContext sessionContext) throws BusinessException {
        Assert.notNull(handleApplyRequest, "handleApplyRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID[] idArr = handleApplyRequest.getIdArr();
        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
        String failReason = LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_MANAGE_HAND_DISCARD_APPLY,
                baseAdminDTO.getUserName());
        // 批量废弃任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_TASK_NAME, new String[] {}).build()).iterator();
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            String applySerialNumber = auditApplyMgmtAPI.findAuditApplyDetailById(idArr[0]).getApplySerialNumber();
            DiscardAuditFileApplyBatchTaskHandler handler = new DiscardAuditFileApplyBatchTaskHandler(iterator, failReason, applySerialNumber);
            result = builder.setTaskName(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_TASK_NAME)
                    .setTaskDesc(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_TASK_DESC).registerHandler(handler).start();
        } else {
            DiscardAuditFileApplyBatchTaskHandler handler = new DiscardAuditFileApplyBatchTaskHandler(iterator, failReason, null);
            result = builder.setTaskName(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_BATH_TASK_NAME)
                    .setTaskDesc(AuditFileBusinessKey.RCDC_AUDIT_FILE_AUDITOR_DISCARD_BATH_TASK_DESC).registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 获取文件导出审批申请列表
     *
     * @param request 分页复杂查询
     * @param sessionContext session信息
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取文件导出审批申请列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse list(PageQueryRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(request);
        generateUserQueryRequest(builder, sessionContext);
        PageQueryResponse<ViewAuditApplyDTO> response = auditApplyMgmtAPI.auditApplyPageQuery(builder.build());
        return DefaultWebResponse.Builder.success(response);
    }

    private void generateUserQueryRequest(PageQueryBuilderFactory.RequestBuilder  pageReqBuilder,
                                          SessionContext sessionContext) throws BusinessException {
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] groupIdArr = permissionHelper.getUserGroupIdArr(sessionContext.getUserId());
            pageReqBuilder.in("groupId", groupIdArr);
        }
    }

    /**
     * 异步导出文件导出审批申请单数据到excel
     *
     * @param request 复杂查询
     * @param sessionContext Session上下文
     * @return result
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/export", method = RequestMethod.POST)
    @ApiOperation("导出文件导出审批申请单")
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
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
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
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    @EnableAuthority
    public DownloadWebResponse downloadExportFile(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportExcelResponse exportFile = exportAuditApplyExcelAPI.getExportFile(userId);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        return builder.setFile(exportFile.getExportFile(), false).setName("exportAuditFileApply", "xlsx").build();
    }

}

