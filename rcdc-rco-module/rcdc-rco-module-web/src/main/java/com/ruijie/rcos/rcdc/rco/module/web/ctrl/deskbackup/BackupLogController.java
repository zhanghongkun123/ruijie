package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.dto.BackupProgressDTO;
import com.ruijie.rcos.rcdc.backup.module.def.dto.CbbBackupLogDTO;
import com.ruijie.rcos.rcdc.backup.module.def.enums.BackupStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.StopServerBackupSingleTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo.BackupLogVO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.annotation.NoMaintenanceUrl;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Description: 备份日志管理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月30日
 *
 * @author qiuzy
 */
@Api(tags = "备份日志管理")
@Controller
@RequestMapping("/rco/serverbackup/log")
public class BackupLogController {

    @Autowired
    private CbbBackupLogAPI cbbBackupLogAPI;

    @Autowired
    private CbbBackupAPI cbbBackupAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    /**
     * 服务器备份日志列表
     *
     * @param request 页面/页数
     * @return 响应
     * @throws BusinessException BusinessException
     */
    @ApiOperation(value = "服务器备份日志列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"服务器备份日志列表"})})
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DefaultWebResponse listServerBackupLog(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        final PageQueryResponse<CbbBackupLogDTO> response = cbbBackupLogAPI.pageQuery(request);
        return DefaultWebResponse.Builder.success(parseBackupLogResponse(response));
    }

    private PageQueryResponse<BackupLogVO> parseBackupLogResponse(PageQueryResponse<CbbBackupLogDTO> cbbResponse) {
        CbbBackupLogDTO[] itemArr = cbbResponse.getItemArr();
        BackupLogVO[] voArr = Arrays.stream(itemArr).map(cbbBackupLogDTO -> {
            BackupLogVO backupLogVO = new BackupLogVO();
            BeanUtils.copyProperties(cbbBackupLogDTO, backupLogVO);
            if (cbbBackupLogDTO.getBackupState() == BackupStateEnum.DOING) {
                BackupProgressDTO backupProgressDTO = cbbBackupLogAPI.getProgressByLogId(cbbBackupLogDTO.getId());
                backupLogVO.setPercentage(backupProgressDTO.getPercentage() == null ? BigDecimal.ZERO.toString() : backupProgressDTO.getPercentage());
            }
            return backupLogVO;
        }).toArray(BackupLogVO[]::new);
        PageQueryResponse<BackupLogVO> response = new PageQueryResponse<>(voArr, cbbResponse.getTotal());
        return response;
    }

    /**
     * 查询备份恢复任务进度
     *
     * @param idWebRequest 任务ID
     * @return 响应
     * @throws BusinessException BusinessException
     */
    @ApiOperation(value = "查询备份恢复任务进度")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"查询备份恢复任务进度"})})
    @RequestMapping(value = "progress/get", method = RequestMethod.POST)
    @NoMaintenanceUrl
    public DefaultWebResponse getTaskProgress(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest can not be null");

        return DefaultWebResponse.Builder.success(cbbBackupLogAPI.getProgressByLogId(idWebRequest.getId()));
    }

    /**
     * 停止备份任务
     *
     * @param idWebRequest 任务id
     * @param builder 批量任务创建对象
     * @return 结果集
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "停止备份任务")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"停止备份任务"})})
    @RequestMapping(value = "stop", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse stop(IdWebRequest idWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idWebRequest, "id不能为空");
        Assert.notNull(builder, "BatchTaskBuilder不能为空");

        final BatchTaskItem batchTaskItem = DefaultBatchTaskItem.builder().itemId(idWebRequest.getId())
                .itemName(LocaleI18nResolver.resolve(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_ITEM_NAME)).build();
        StopServerBackupSingleTaskHandler handler =
                new StopServerBackupSingleTaskHandler(cbbBackupAPI, batchTaskItem, auditLogAPI, cbbBackupLogAPI, pageQueryBuilderFactory);
        BatchTaskSubmitResult result = builder.setTaskName(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_SINGLE_TASK_NAME)
                .setTaskDesc(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_STOP_RUNNING_SINGLE_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }
}
