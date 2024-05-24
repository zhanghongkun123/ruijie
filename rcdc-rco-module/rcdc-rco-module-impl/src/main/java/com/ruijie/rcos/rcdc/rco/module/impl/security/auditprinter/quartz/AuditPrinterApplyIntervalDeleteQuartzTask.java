package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.quartz;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.service.AuditApplyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.tx.AuditPrinterServiceTx;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import com.ruijie.rcos.sk.base.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.HOME_EXTERNAL_STORAGE_PATH;

/**
 * Description: 定时任务根据全局策略中配置的清理周期，对周期前的数据进行删除处理。（定时任务每天2点触发）
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/8
 *
 * @author lihengjing
 */
@Service
@Quartz(scheduleTypeCode = "audit_printer_apply_interval_delete",
        scheduleName = AuditPrinterBusinessKey.RCDC_RCO_QUARTZ_AUDIT_PRINTER_APPLY_INTERVAL_DELETE, cron = "0 0 2 * * ?")
public class AuditPrinterApplyIntervalDeleteQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditPrinterApplyIntervalDeleteQuartzTask.class);

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private AuditApplyService auditApplyService;

    @Autowired
    private AuditFileService auditFileService;

    @Autowired
    private AuditPrinterServiceTx auditPrinterServiceTx;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        String taskName = LocaleI18nResolver.resolve(AuditPrinterBusinessKey.RCDC_RCO_QUARTZ_AUDIT_PRINTER_APPLY_INTERVAL_DELETE);

        SystemMaintenanceState maintenanceMode = maintenanceModeMgmtAPI.getMaintenanceMode();
        if (maintenanceMode != SystemMaintenanceState.NORMAL) {
            LOGGER.info("[{}]：任务不执行,当前处于维护模式:{}", taskName, maintenanceMode);
            return;
        }

        LOGGER.info("[{}]：任务执行开始", taskName);
        AuditFileGlobalConfigDTO auditFileGlobalConfigDTO = auditFileService.obtainAuditFileGlobalConfig();

        // 清理时间 = 当前时间 - 记录周期
        Integer interval = auditFileGlobalConfigDTO.getInterval();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -interval);
        // 查询更新时间小于清理时间的申请单
        List<AuditApplyDetailDTO> applyList = auditApplyService.findAuditApplyDetailListByUpdateTimeLessThan(cal.getTime());
        if (CollectionUitls.isEmpty(applyList)) {
            LOGGER.info("[{}]：任务执行结束，无历史数据需要清理", taskName);
            return;
        }

        // 删除文件后,再删除申请单数据库记录
        for (AuditApplyDetailDTO applyDetail : applyList) {
            if (AuditApplyTypeEnum.PRINT == applyDetail.getApplyType()) {
                try {
                    List<AuditFileDTO> fileList = applyDetail.getAuditFileList();
                    if (!CollectionUitls.isEmpty(fileList)) {
                        String createTime = DateUtils.format(applyDetail.getCreateTime(), DateUtils.DEFAULT_DATE_FORMAT);
                        for (AuditFileDTO auditFile : fileList) {
                            // 删除临时文件
                            if (StringUtils.hasText(auditFile.getFileServerTempPath())) {
                                Files.deleteIfExists(Paths.get(Constants.AUDIT_FILE_FTP_TEMP_PATH, auditFile.getFileServerTempPath()));
                            }
                            // 删除临时文件夹
                            Files.deleteIfExists(Paths.get(Constants.AUDIT_FILE_FTP_TEMP_PATH, auditFile.getId().toString()));
                            
                            // 删除持久文件
                            UUID externalStorageId = auditFileGlobalConfigDTO.getExternalStorageId();
                            if (Objects.nonNull(externalStorageId)) {
                                String fileServerStoragePrefix = String.format(HOME_EXTERNAL_STORAGE_PATH, externalStorageId);
                                if (StringUtils.hasText(auditFile.getFileServerStoragePath())) {
                                    Files.deleteIfExists(Paths.get(fileServerStoragePrefix, auditFile.getFileServerStoragePath()));
                                } else {
                                    String fileSuffix = auditFile.getFileSuffix();
                                    fileSuffix = StringUtils.isEmpty(fileSuffix) ? "" : "." + fileSuffix;
                                    Files.deleteIfExists(Paths.get(fileServerStoragePrefix, Constants.AUDIT_FILE_STORAGE_PATH, 
                                            createTime, auditFile.getApplyId().toString() + "_" + auditFile.getId().toString() + fileSuffix));
                                }
                                // 删除持久文件夹
                                deleteStorageDirectory(Paths.get(fileServerStoragePrefix, Constants.AUDIT_FILE_STORAGE_PATH, createTime));
                            }
                        }
                    }
                    auditPrinterServiceTx.deleteAuditPrintApply(applyDetail.getId());
                    LOGGER.info("定时任务删除申请单记录完成,申请单详情:{}", JSON.toJSONString(applyDetail));
                } catch (Exception e) {
                    LOGGER.error("定时任务删除申请单[{}]记录出现异常:", applyDetail.getId(), e);
                }
            }
        }

        LOGGER.info("[{}]：任务执行正常结束，共处理[{}]条记录", taskName, applyList.size());
    }

    /**
     * 判断日期文件夹内的文件为空,则删除文件夹
     *
     * @param path 日期文件夹
     * @throws IOException IO异常
     */
    private void deleteStorageDirectory(Path path) throws IOException {
        File file = path.toFile();
        if (!file.exists()) {
            return;
        }
        String[] fileArr = file.list();
        if (fileArr != null && fileArr.length == 0) {
            Files.deleteIfExists(path);
        }
    }
}
