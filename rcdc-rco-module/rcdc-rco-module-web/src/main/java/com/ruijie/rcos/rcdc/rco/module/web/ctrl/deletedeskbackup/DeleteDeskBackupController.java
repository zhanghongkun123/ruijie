package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup;

import java.util.*;
import java.util.stream.Stream;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDetailDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.request.BackupIdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.DeskBackupMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.DeskBackupDiskDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.DeskBackupDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.batchtask.DeskBackupDiskBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.batchtask.DeskBackupDiskBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.dto.DeskBackupRestoreDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.dto.DeskDeleteDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.dto.DeskDeleteDiskListDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.request.DeskBackupDiskRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.request.DeskBackupRestoreDiskRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
@Api(tags = "恢复已删除云桌面的磁盘")
@Controller
@RequestMapping("/rco/clouddesktop/backup_del_desk")
public class DeleteDeskBackupController {

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private DeskBackupMgmtAPI deskBackupMgmtAPI;

    @Autowired
    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 备份列表
     *
     * @param webRequest 请求参数
     * @return 返回信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "备份列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"备份列表"})})
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DefaultWebResponse list(PageQueryRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder(webRequest);

        requestBuilder.eq("deskId", new UUID(0, 0));
        return DefaultWebResponse.Builder.success(cbbVDIDeskBackupAPI.pageQuery(requestBuilder.build()));
    }

    /**
     * 磁盘备份列表
     *
     * @param request 请求参数
     * @return 返回信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "磁盘备份列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"磁盘备份列表"})})
    @RequestMapping(value = "diskList", method = RequestMethod.POST)
    public DefaultWebResponse getDiskList(DeskBackupDiskRequest request)
            throws BusinessException {
        Assert.notNull(request, "request must not null");
        CbbDeskBackupDetailDTO backupDTO = cbbVDIDeskBackupAPI.findDeskBackupInfoById(request.getId());
        BackupIdRequest backupIdRequest = new BackupIdRequest();
        backupIdRequest.setBackupId(request.getId());
        backupIdRequest.setPlatformId(backupDTO.getPlatformId());
        DeskBackupDiskDTO deskBackupDisk = deskBackupMgmtAPI.getDeskBackupDiskInfo(backupIdRequest);
        DeskDeleteDiskListDTO deskDeleteDiskList = new DeskDeleteDiskListDTO();
        List<DeskDeleteDiskDTO> deleteDiskDTOList = new ArrayList<>();
        for (DeskBackupDiskDetailDTO dto : deskBackupDisk.getDiskBackupList()) {
            DeskDeleteDiskDTO deskDeleteDiskDTO = new DeskDeleteDiskDTO();
            deskDeleteDiskDTO.setId(dto.getDiskBackupId());
            deskDeleteDiskDTO.setDiskId(dto.getDiskId());
            deskDeleteDiskDTO.setDiskUse(dto.getDiskUse());
            deskDeleteDiskDTO.setCapacity(dto.getCapacity());
            deskDeleteDiskDTO.setState(deskBackupDisk.getState());
            deskDeleteDiskDTO.setVmBackupId(deskBackupDisk.getVmBackupId());
            deskDeleteDiskDTO.setExternalStorageId(deskBackupDisk.getExternalStorageId());
            deskDeleteDiskDTO.setVmId(deskBackupDisk.getVmId());
            deskDeleteDiskDTO.setName(deskBackupDisk.getName());
            deskDeleteDiskDTO.setSnapshotId(dto.getSnapshotId());
            deleteDiskDTOList.add(deskDeleteDiskDTO);
        }
        DeskDeleteDiskDTO[] deskDeleteDiskArr = deleteDiskDTOList.toArray(new DeskDeleteDiskDTO[0]);
        deskDeleteDiskList.setItemArr(deskDeleteDiskArr);
        deskDeleteDiskList.setTotal(deleteDiskDTOList.size());
        return DefaultWebResponse.Builder.success(deskDeleteDiskList);
    }

    /**
     * 恢复磁盘
     *
     * @param request        请求参数
     * @param builder        builder
     * @return 返回信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "恢复磁盘")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"恢复磁盘"})})
    @RequestMapping(value = "/vm_restore_disk", method = RequestMethod.POST)
    public CommonWebResponse<BatchTaskSubmitResult> restoreBackupDisk(DeskBackupRestoreDiskRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(request.getVmId());
        if (cbbDeskDTO == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, request.getVmId().toString());
        }
        if (cbbDeskDTO.getDeskState() != CbbCloudDeskState.CLOSE) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, "请选择关机状态的云桌面，再进行恢复");
        }
        CbbDeskBackupDetailDTO backupDTO = cbbVDIDeskBackupAPI.findDeskBackupInfoById(request.getBackupId());
        // 判断桌面和备份是否在同一云平台下
        if (!Objects.equals(cbbDeskDTO.getPlatformId(), backupDTO.getPlatformId())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_BACKUP_RESTORE_CLOUD_PLATFORM_ERROR, backupDTO.getName(), cbbDeskDTO.getName());
        }
        DeskBackupRestoreDiskDTO[] diskBackupArr = request.getDiskBackupArr();
        final Iterator<DeskBackupDiskBatchTaskItem> iterator = Stream.of(diskBackupArr).map(item -> {
            DeskBackupDiskBatchTaskItem batchTaskItem = new DeskBackupDiskBatchTaskItem();
            batchTaskItem.setItemId(item.getDiskBackupId());
            batchTaskItem.setDiskBackupId(item.getDiskBackupId());
            batchTaskItem.setBackupId(request.getBackupId());
            batchTaskItem.setDiskUse(item.getDiskUse());
            batchTaskItem.setCapacity(item.getCapacity());
            batchTaskItem.setDeskId(request.getVmId());
            batchTaskItem.setDeskName(cbbDeskDTO.getName());
            batchTaskItem.setItemName(LocaleI18nResolver.resolve(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_DISK_RESTORE_ITEM_NAME));
            batchTaskItem.setSnapshotId(item.getSnapshotId());
            return batchTaskItem;
        }).iterator();

        DeskBackupDiskBatchTaskHandler handler = new DeskBackupDiskBatchTaskHandler(iterator, auditLogAPI);
        handler.setDeskId(request.getVmId());
        BatchTaskSubmitResult result;
        int diskNum = diskBackupArr.length;
        if (diskNum == 1) {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_DISK_SINGLE_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_DISK_RESTORE_DESC, backupDTO.getName(), String.valueOf(diskNum),
                            cbbDeskDTO.getName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_DISK_BATCH_TASK_NAME)
                    .setTaskDesc(UserBusinessKey.RCDC_RCO_DESKTOP_DELETE_DISK_RESTORE_DESC, backupDTO.getName(), String.valueOf(diskNum),
                            cbbDeskDTO.getName())
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }
}
