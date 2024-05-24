package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.SecurityConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditFileApplyAlarmRuleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbBaseAlarmRuleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbWeeklyWorkTimeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbWorkTimeCycleTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.util.SecurityUtils;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyAuditLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyAuditLogStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.constants.AuditFileConstants;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums.AuditFileStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.constants.AuditPrinterConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditPrintApplyAlarmRuleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.dao.AuditApplyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.entity.AuditApplyEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.tx.AuditApplyServiceTx;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.dao.AuditFileDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.entity.AuditFileEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao.AuditFilePrintInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity.AuditFilePrintInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.SecurityBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.io.IoUtil;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.DateUtils;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.HOME_EXTERNAL_STORAGE_PATH;

/**
 * Description: 文件导出审批管理实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
public class AuditFileServiceImpl implements AuditFileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditFileServiceImpl.class);

    /** 2M 缓冲区 */
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    /**
     * 更新状态重试最大次数
     */
    private static final int RETRY_MAX_COUNT = 3;

    /**
     * 删除指定申请单下文件服务器的文件线程名称
     */
    private static final String DELETE_APPLY_FILE_THREAD_NAME = "deleteAuditApplyExternalStorageFile";

    @Autowired
    private AuditApplyDAO auditApplyDAO;

    @Autowired
    private AuditFileDAO auditFileDAO;

    @Autowired
    private AuditFilePrintInfoDAO auditFilePrintInfoDAO;

    @Autowired
    private AuditApplyServiceTx auditApplyServiceTx;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private AuditPrinterService auditPrinterService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;
    
    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;
    
    @Override
    public void checkAuditFileApplyAlarm(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException {
        Assert.notNull(auditApplyDetailDTO, "auditFileApplyDetailDTO not be null");
        List<AuditFileDTO> auditFileList = auditApplyDetailDTO.getAuditFileList();
        Assert.notNull(auditFileList, "auditFileList cannot be null");
        List<AuditApplyAuditLogDTO> auditorList = auditApplyDetailDTO.getAuditorList();
        Assert.notNull(auditorList, "auditorList cannot be null");

        // 是否打印申请单
        boolean isPrint = AuditApplyTypeEnum.PRINT == auditApplyDetailDTO.getApplyType();

        // 本次申请文件总大小
        long currentTotalFileSize = 0L;
        // 本次申请文件数量
        int currentTotalFileCount = 0;
        // 本次申请文件打印页数
        int currentTotalFilePage = 0;

        // 当天申请文件数量
        int currentDayAlreadyApplyFileCount = 0;
        // 当天申请文件总大小
        long currentDayAlreadyApplyFileSize = 0L;
        // 当天申请文件总页数
        int currentDayAlreadyPrintFilePage = 0;

        // 本小时申请文件数量
        int currentHourAlreadyApplyFileCount = 0;
        // 本小时打印页数
        int currentHourAlreadyPrintFilePage = 0;

        // 单文件大小超过限制
        boolean isSingleFileSizeLimit = false;

        AuditFileStrategyDTO auditFileGlobalStrategyDTO = this.obtainAuditFileStrategy(auditApplyDetailDTO.getDesktopId());
        AuditPrinterStrategyDTO auditPrinterGlobalStrategyDTO = auditPrinterService.obtainAuditPrinterStrategy(auditApplyDetailDTO.getDesktopId());

        // 1 遍历所有文件信息计算文件大小 统计各类信息
        for (AuditFileDTO auditFileDTO : auditFileList) {
            // 最好计算一下文件md5 防止客户端欺诈
            currentTotalFileCount++;
            currentTotalFileSize += auditFileDTO.getFileSize() == null ? 0L : auditFileDTO.getFileSize();
            currentTotalFilePage += auditFileDTO.getFilePage() == null ? 0 : auditFileDTO.getFilePage();
            auditFileDTO.setId(UUID.randomUUID());
            auditFileDTO.setApplyId(auditApplyDetailDTO.getId());
            auditFileDTO.setFileState(AuditFileStateEnum.UPLOADING);
            auditFileDTO.setCreateTime(new Date());
            auditFileDTO.setUpdateTime(new Date());
            auditFileDTO.setFileServerTempPath(null);
            auditFileDTO.setFileServerStoragePath(null);
            if (StringUtils.isBlank(auditFileDTO.getFileSuffix())) {
                String[] fileNameArr = auditFileDTO.getFileName().split("\\.");
                if (fileNameArr.length > 1) {
                    auditFileDTO.setFileSuffix(fileNameArr[fileNameArr.length - 1]);
                }
            }
            isSingleFileSizeLimit = auditFileDTO.getFileSize() > auditFileGlobalStrategyDTO.getSingleFileSizeLimit();
        }

        // 1.1 查询当天本云桌面所有通过、在途的导出申请单
        List<AuditApplyStateEnum> auditFileApplyStateList = Lists.newArrayList();
        auditFileApplyStateList.add(AuditApplyStateEnum.CANCELED);
        auditFileApplyStateList.add(AuditApplyStateEnum.REJECTED);
        auditFileApplyStateList.add(AuditApplyStateEnum.FAIL);
        AuditApplyStateEnum[] auditFileApplyStateArr = new AuditApplyStateEnum[auditFileApplyStateList.size()];
        List<AuditApplyEntity> desktopApplyList = auditApplyDAO.findByApplyTypeAndDesktopIdAndStateNotInAndCreateTimeGreaterThanAndCreateTimeLessThan(
                auditApplyDetailDTO.getApplyType(), auditApplyDetailDTO.getDesktopId(), auditFileApplyStateList.toArray(auditFileApplyStateArr),
                DateUtil.getDayStartTime(new Date()), DateUtil.getDayEndTime(new Date()));
        if (!CollectionUtils.isEmpty(desktopApplyList)) {
            // 1.2 查询当天本云桌面所有通过、在途的导出文件
            List<UUID> applyIdList = desktopApplyList.stream().map(AuditApplyEntity::getId).collect(Collectors.toList());
            UUID[] applyIdArr = new UUID[applyIdList.size()];
            List<AuditFileStateEnum> auditFileStateList = Lists.newArrayList();
            auditFileStateList.add(AuditFileStateEnum.NOT_NEED);
            auditFileStateList.add(AuditFileStateEnum.REJECTED);
            auditFileStateList.add(AuditFileStateEnum.FAIL);
            AuditFileStateEnum[] auditFileStateArr = new AuditFileStateEnum[auditFileStateList.size()];
            List<AuditFileEntity> auditFileEntityList =
                    auditFileDAO.findByApplyIdInAndFileStateNotIn(applyIdList.toArray(applyIdArr), auditFileStateList.toArray(auditFileStateArr));


            // 1.3 计算已申请通过、在途的导出文件数量、大小
            if (!CollectionUtils.isEmpty(auditFileEntityList)) {
                // 本小时起止时间
                Date hourStart = DateUtil.getHourStart(new Date());
                Date hourEnd = DateUtil.getHourEnd(new Date());

                for (AuditFileEntity auditFileEntity : auditFileEntityList) {
                    // 获取打印成功、在途记录
                    AuditFilePrintInfoEntity auditFilePrintInfoEntity = null;
                    if (isPrint) {
                        Optional<AuditFilePrintInfoEntity> printInfoEntityOptional = auditFilePrintInfoDAO.findByFileId(auditFileEntity.getId());
                        if (printInfoEntityOptional.isPresent()) {
                            auditFilePrintInfoEntity = printInfoEntityOptional.get();
                            if (auditFilePrintInfoEntity.getPrintState() == PrintStateEnum.FAIL) {
                                auditFilePrintInfoEntity = null;
                            }
                        }
                    }
                    // 当天申请文件数量
                    currentDayAlreadyApplyFileCount++;
                    // 当天申请文件大小
                    currentDayAlreadyApplyFileSize += auditFileEntity.getFileSize() == null ? 0L : auditFileEntity.getFileSize();
                    if (auditFilePrintInfoEntity != null) {
                        currentDayAlreadyPrintFilePage +=
                                auditFilePrintInfoEntity.getPrintPageCount() == null ? 0 : auditFilePrintInfoEntity.getPrintPageCount();
                    }
                    // 本小时数量
                    if (SecurityUtils.isEffectiveDate(auditFileEntity.getCreateTime(), hourStart, hourEnd)) {
                        // 本小时申请文件数量
                        currentHourAlreadyApplyFileCount++;
                        if (auditFilePrintInfoEntity != null) {
                            // 本小时打印页数（待打印、打印成功）
                            currentHourAlreadyPrintFilePage +=
                                    auditFilePrintInfoEntity.getPrintPageCount() == null ? 0 : auditFilePrintInfoEntity.getPrintPageCount();
                        }
                    }
                }
            }

        }

        // 1.4 文件数量、大小谎报漏报时更新数据
        if (!Objects.equals(auditApplyDetailDTO.getTotalFileSize(), currentTotalFileSize)
                || !Objects.equals(auditApplyDetailDTO.getTotalFileCount(), currentTotalFileCount)
                || !Objects.equals(auditApplyDetailDTO.getTotalFilePage(), currentTotalFilePage)) {
            auditApplyDetailDTO.setTotalFileSize(currentTotalFileSize);
            auditApplyDetailDTO.setTotalFileCount(currentTotalFileCount);
            auditApplyDetailDTO.setTotalFilePage(currentTotalFilePage);
        }

        // 2.检查是否生成（可疑行为）告警
        Boolean enableAlarm = isPrint ? auditPrinterGlobalStrategyDTO.getEnableAlarm() : auditFileGlobalStrategyDTO.getEnableAlarm();
        CbbBaseAlarmRuleDTO alarmRule = isPrint ? auditPrinterGlobalStrategyDTO.getAlarmRule() : auditFileGlobalStrategyDTO.getAlarmRule();
        if (enableAlarm && alarmRule != null) {
            // 2.1 （打印文件审计-可疑行为）检查是否在工作时间
            // 2.1 （导出文件审计-可疑行为）检查是否在工作时间
            if (CbbWorkTimeCycleTypeEnum.WEEKLY == alarmRule.getWorkTimeCycleType() && alarmRule.getWeeklyWorkTimeList() != null) {
                boolean isInWorkTime = false;
                int weekIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                for (CbbWeeklyWorkTimeDTO weeklyWorkTimeDTO : alarmRule.getWeeklyWorkTimeList()) {
                    if (weeklyWorkTimeDTO.getDayOfWeek() != null && weekIndex == weeklyWorkTimeDTO.getDayOfWeek()) {
                        // 判断在对应时间内
                        try {
                            SimpleDateFormat workTimeFormat = new SimpleDateFormat(SecurityConstants.SECURITY_WORK_TIME_FORMAT);
                            Date startTime = workTimeFormat.parse(weeklyWorkTimeDTO.getStartTime());
                            Date endTime = workTimeFormat.parse(weeklyWorkTimeDTO.getEndTime());
                            Date currentTime = workTimeFormat.parse(DateUtils.format(new Date(), SecurityConstants.SECURITY_WORK_TIME_FORMAT));
                            isInWorkTime = SecurityUtils.isEffectiveDate(currentTime, startTime, endTime);
                        } catch (ParseException e) {
                            // 工作时间配置有误
                            throw new BusinessException(SecurityBusinessKey.RCDC_RCO_SECURITY_GLOBAL_CONFIG_WORK_TIME_ERROR, e);
                        }
                    }
                }
                if (!isInWorkTime) {
                    // 产生可疑行为告警
                    String alarmContent = LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_NOT_IN_WORK_TIME,
                            DateUtils.format(new Date(), DateUtils.NORMAL_DATE_FORMAT));
                    saveAlarm(false, auditApplyDetailDTO, alarmContent);
                }
            }
            if (isPrint) {
                // 2.2 （打印文件审计-可疑行为）检查单小时申请文件页数
                CbbAuditPrintApplyAlarmRuleDTO auditPrintApplyAlarmRuleDTO = (CbbAuditPrintApplyAlarmRuleDTO) alarmRule;
                Integer oneHourPrintPageNumLimit = auditPrintApplyAlarmRuleDTO.getOneHourPrintPageNumLimit();
                if (oneHourPrintPageNumLimit != null && oneHourPrintPageNumLimit > 0
                        && currentHourAlreadyPrintFilePage + currentTotalFilePage > oneHourPrintPageNumLimit) {
                    // 产生可疑行为告警
                    String alarmContent = LocaleI18nResolver.resolve(
                            AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_GLOBAL_CONFIG_ALARM_RULE_ONE_HOUR_PRINT_PAGE_NUM_LIMIT,
                            String.valueOf(oneHourPrintPageNumLimit),
                            String.valueOf(currentHourAlreadyPrintFilePage + currentTotalFilePage));
                    saveAlarm(false, auditApplyDetailDTO, alarmContent);
                }
            } else {
                // 2.2 （导出文件审计-可疑行为）检查单小时申请文件数量
                CbbAuditFileApplyAlarmRuleDTO auditFileApplyAlarmRuleDTO = (CbbAuditFileApplyAlarmRuleDTO) alarmRule;
                Integer oneHourFileNumLimit = auditFileApplyAlarmRuleDTO.getOneHourFileNumLimit();
                if (oneHourFileNumLimit != null && oneHourFileNumLimit > 0
                        && currentHourAlreadyApplyFileCount + currentTotalFileCount > oneHourFileNumLimit) {
                    // 产生可疑行为告警
                    String alarmContent = LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ONE_HOUR_FILE_NUM_LIMIT,
                            String.valueOf(oneHourFileNumLimit),
                            String.valueOf(currentHourAlreadyApplyFileCount + currentTotalFileCount));
                    saveAlarm(false, auditApplyDetailDTO, alarmContent);
                }
            }
        }

        // 3.检查是否生成（限制行为）告警
        if (isPrint) {
            // 3.1 （打印文件审计-限制行为）检查当天文件总页数
            Integer oneDayPrintPageNumLimit = auditPrinterGlobalStrategyDTO.getOneDayPrintPageNumLimit();
            if (oneDayPrintPageNumLimit != null && oneDayPrintPageNumLimit > 0 
                    && currentDayAlreadyPrintFilePage + currentTotalFilePage > oneDayPrintPageNumLimit) {
                // 产生告警 关闭申请单
                this.alarmAndCloseApply(auditApplyDetailDTO, AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_APPLY_ONE_DAY_PRINT_PAGE_NUM_LIMIT,
                        String.valueOf(oneDayPrintPageNumLimit), String.valueOf(currentDayAlreadyPrintFilePage),
                        String.valueOf(currentTotalFilePage));
            }
        } else {
            // 3.1 （导出文件审计-限制行为）检查单文件大小
            if (isSingleFileSizeLimit) {
                // 产生限制行为告警 关闭申请单
                alarmAndCloseApply(auditApplyDetailDTO, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_SINGLE_FILE_SIZE_LIMIT,
                        CapacityUnitUtils.dynamicChange(new BigDecimal(auditFileGlobalStrategyDTO.getSingleFileSizeLimit())));
            }

            // 3.2 （导出文件审计-限制行为）检查单个申请单文件个数
            Integer singleApplyFileNumLimit = auditFileGlobalStrategyDTO.getSingleApplyFileNumLimit();
            if (singleApplyFileNumLimit != null && singleApplyFileNumLimit > 0 && currentTotalFileCount > singleApplyFileNumLimit) {
                // 产生限制行为告警 关闭申请单
                alarmAndCloseApply(auditApplyDetailDTO, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_SINGLE_APPLY_FILE_NUM_LIMIT,
                        String.valueOf(singleApplyFileNumLimit));
            }

            // 3.3 （导出文件审计-限制行为）检查当天文件总数量
            Integer oneDayFileNumLimit = auditFileGlobalStrategyDTO.getOneDayFileNumLimit();
            if (oneDayFileNumLimit != null && oneDayFileNumLimit > 0 
                    && currentDayAlreadyApplyFileCount + currentTotalFileCount > oneDayFileNumLimit) {
                // 产生限制行为告警 关闭申请单
                alarmAndCloseApply(auditApplyDetailDTO, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ONE_DAY_FILE_NUM_LIMIT,
                        String.valueOf(oneDayFileNumLimit), String.valueOf(currentDayAlreadyApplyFileCount));
            }
            // 3.4 （导出文件审计-限制行为）检查当天文件总大小
            Long oneDayFileTotalSizeLimit = auditFileGlobalStrategyDTO.getOneDayFileTotalSizeLimit();
            if (oneDayFileTotalSizeLimit != null && oneDayFileTotalSizeLimit > 0
                    && currentDayAlreadyApplyFileSize + currentTotalFileSize > oneDayFileTotalSizeLimit) {
                // 产生限制行为告警 关闭申请单
                alarmAndCloseApply(auditApplyDetailDTO, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ONE_DAY_FILE_SIZE_LIMIT,
                        CapacityUnitUtils.dynamicChange(new BigDecimal(oneDayFileTotalSizeLimit)), 
                        CapacityUnitUtils.dynamicChange(new BigDecimal(currentDayAlreadyApplyFileSize)));
            }
        }
        
        // 4.安全打印和文件流转需要进行内容审计，空间大小校验
        boolean enableAuditFileContent = BooleanUtils.isTrue(
                isPrint ? auditPrinterGlobalStrategyDTO.getEnableAuditPrintContent() : auditFileGlobalStrategyDTO.getEnableAuditFileContent());
        if (enableAuditFileContent) {
            try {
                // 校验ftp可用空间大小 < 待上传的文件总大小
                long usableSpace = new File(Constants.AUDIT_FILE_FTP_TEMP_PATH).getUsableSpace();
                if (usableSpace < auditApplyDetailDTO.getTotalFileSize()) {
                    LOGGER.error("文件导出审批FTP临时空间不足，可用空间大小：{}，" +
                            "待上传的文件总大小：{}", usableSpace, auditApplyDetailDTO.getTotalFileSize());
                    throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_FTP_DIR_NO_SPACE);
                }
                checkExternalStorageSpace(auditApplyDetailDTO.getTotalFileSize());
            } catch (BusinessException e) {
                // 记录失败原因
                auditApplyDetailDTO.setFailReason(e.getI18nMessage());
                // 产生限制行为告警 关闭申请单
                alarmAndCloseApply(auditApplyDetailDTO, e.getKey(), e.getArgArr());
            }
        }
    }


    @Override
    public void alarmAndCloseApply(AuditApplyDetailDTO auditApplyDetailDTO, String alarmTypeKey, @Nullable String... args) throws BusinessException {
        Assert.notNull(auditApplyDetailDTO, "auditFileApplyDetail must not be null");
        Assert.hasText(alarmTypeKey, "alarmTypeKey is not empty");
        Assert.notNull(args, "args is not null");
        // 产生告警
        String alarmContent = LocaleI18nResolver.resolve(alarmTypeKey, args);

        saveAlarm(true, auditApplyDetailDTO, alarmContent);

        this.fillAuditFileApplyDetailDTOAllState(auditApplyDetailDTO);
        // 保存申请单
        auditApplyServiceTx.createAuditApply(auditApplyDetailDTO);

        throw new BusinessException(alarmTypeKey, args);
    }

    @Override
    public void saveAlarm(boolean isFatalAlarm, AuditApplyDetailDTO auditApplyDetailDTO, String alarmContent) {
        Assert.notNull(auditApplyDetailDTO, "auditFileApplyDetail must not be null");
        Assert.hasText(alarmContent, "alarmContent is not empty");
        String content;
        boolean isExport = AuditApplyTypeEnum.EXPORT == auditApplyDetailDTO.getApplyType();
        if (isExport) {
            content = LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ALARM_CONTENT,
                    isFatalAlarm ? LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ALARM_LEVEL_ERROR)
                            : LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ALARM_LEVEL_WARN),
                    auditApplyDetailDTO.getApplySerialNumber(), alarmContent);
        } else {
            content = LocaleI18nResolver.resolve(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_APPLY_ALARM_CONTENT,
                    isFatalAlarm ? LocaleI18nResolver.resolve(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINTER_APPLY_ALARM_LEVEL_ERROR)
                            : LocaleI18nResolver.resolve(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINTER_APPLY_ALARM_LEVEL_WARN),
                    auditApplyDetailDTO.getApplySerialNumber(), alarmContent);
        }

        SaveAlarmRequest request = new SaveAlarmRequest();
        request.setAlarmNameByI18nKey(
                isExport ? AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_ALARM_NAME : AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_ALARM_NAME);
        request.setAlarmType(isExport ? AuditFileConstants.AUDIT_FILE_ALARM_TYPE : AuditPrinterConstants.AUDIT_PRINT_ALARM_TYPE);
        request.setAlarmCode(buildAlarmCode(request.getAlarmType(), isFatalAlarm, auditApplyDetailDTO.getId(), content));
        request.setAlarmContent(content);
        request.setAlarmLevel(AlarmLevel.WARN);
        Date alarmTime = new Date();
        request.setAlarmTime(alarmTime);
        request.setEnableSendMail(true);
        request.setBusinessId(String.valueOf(auditApplyDetailDTO.getId()));
        baseAlarmAPI.saveAlarm(request);

        // 查询对应告警ID
        Sort sort = new Sort();
        sort.setSortField("firstAlarmTime");
        sort.setDirection(Sort.Direction.DESC);

        ListAlarmRequest alarmRequest = new ListAlarmRequest();
        alarmRequest.setBusinessIdArr(new String[] {String.valueOf(auditApplyDetailDTO.getId())});
        alarmRequest.setEnableQueryHistory(false);
        alarmRequest.setSort(sort);

        DefaultPageResponse<AlarmDTO> response = baseAlarmAPI.listAlarmList(alarmRequest);
        if (response.getItemArr() != null && response.getItemArr().length > 0) {
            auditApplyDetailDTO.setAlarmIds(StringUtils.join(Arrays.stream(response.getItemArr())
                            .map(AlarmDTO::getId).collect(Collectors.toSet()), AuditFileConstants.APPLY_SEPARATOR));
        }

        // 产生告警更新申请单
        if (isFatalAlarm) {
            auditApplyDetailDTO.setState(AuditApplyStateEnum.REJECTED);
        }

    }

    private String buildAlarmCode(String type, boolean isFatalAlarm, UUID id, String content) {
        // 告警返回信息中没有明确方式区分alarmCode,这里基于资源类型，id和告警内容的hashCode做alarmCode
        return type + "-" + (isFatalAlarm ? "warn" : "error") + "-" + id + "-" + content.hashCode();
    }

    @Override
    public void checkEnableAuditFile(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId not be null");
        AuditFileStrategyDTO auditFileGlobalStrategyDTO = obtainAuditFileStrategy(deskId);
        if (!BooleanUtils.isTrue(auditFileGlobalStrategyDTO.getEnableAuditFile())) {
            // 未开启文件导出审批功能
            LOGGER.warn("桌面[{}]文件导出审批功能【未开启】", deskId);
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_DISABLE);
        }
    }


    @Override
    public AuditFileStrategyDTO obtainAuditFileStrategy(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId not be null");
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskDTO.getStrategyId());
        AuditFileStrategyDTO auditFileStrategyDTO = new AuditFileStrategyDTO();
        if (BooleanUtils.isTrue(deskStrategy.getEnableAuditFile()) && Objects.nonNull(deskStrategy.getAuditFileInfo())) {
            BeanUtils.copyProperties(deskStrategy.getAuditFileInfo(), auditFileStrategyDTO);
        }
        auditFileStrategyDTO.setEnableAuditFile(deskStrategy.getEnableAuditFile());
        return auditFileStrategyDTO;
    }

    @Override
    public void saveFileStorageSpaceNotEnoughAlarm(String applySerialNumber) {
        Assert.notNull(applySerialNumber, "applySerialNumber not be null");
        SaveAlarmRequest request = new SaveAlarmRequest();
        request.setAlarmNameByI18nKey(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_ALARM_NAME);
        request.setAlarmType(AuditFileConstants.AUDIT_FILE_ALARM_TYPE);
        request.setAlarmCode(AuditFileConstants.AUDIT_FILE_ALARM_TYPE + "-" + applySerialNumber);
        request.setAlarmContent(LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_ALARM_INSUFFICIENT_SPACE, applySerialNumber));
        request.setAlarmLevel(AlarmLevel.WARN);
        request.setAlarmTime(new Date());
        request.setEnableSendMail(true);
        baseAlarmAPI.saveAlarm(request);
    }

    @Override
    public void editAuditFileGlobalStrategy(AuditFileGlobalConfigDTO auditFileGlobalConfigDTO) {
        Assert.notNull(auditFileGlobalConfigDTO, "auditFileGlobalStrategy not null");
        AuditFileGlobalConfigDTO oldConfig = obtainAuditFileGlobalConfig();

        if (!JSON.toJSONString(oldConfig).equals(JSON.toJSONString(auditFileGlobalConfigDTO))) {
            // 发生变更 更新配置
            globalParameterService.updateParameter(AuditFileConstants.AUDIT_FILE_GLOBAL_STRATEGY_KEY, JSON.toJSONString(auditFileGlobalConfigDTO));
        }
    }


    @Override
    public void fillAuditFileApplyDetailDTOAllState(AuditApplyDetailDTO auditApplyDetailDTO) throws BusinessException {
        Assert.notNull(auditApplyDetailDTO, "auditFileApplyDetail must not be null");

        boolean isPrint = AuditApplyTypeEnum.PRINT == auditApplyDetailDTO.getApplyType();

        AuditFileStrategyDTO auditFileGlobalStrategyDTO = obtainAuditFileStrategy(auditApplyDetailDTO.getDesktopId());
        AuditPrinterStrategyDTO auditPrinterGlobalStrategyDTO = auditPrinterService.obtainAuditPrinterStrategy(auditApplyDetailDTO.getDesktopId());

        boolean isCloseApply = AuditApplyStateEnum.enableApplyClose(auditApplyDetailDTO.getState());

        boolean enableAuditFileContent = BooleanUtils.isTrue(
                isPrint ? auditPrinterGlobalStrategyDTO.getEnableAuditPrintContent() : auditFileGlobalStrategyDTO.getEnableAuditFileContent());
        boolean enableAutoApprove = BooleanUtils
                .isTrue(isPrint ? auditPrinterGlobalStrategyDTO.getEnableAutoApprovePrintApply() : auditFileGlobalStrategyDTO.getEnableAutoApprove());

        // 1.保存申请单
        // 申请单非关闭申请类型 才进行审批状态赋值
        if (!isCloseApply) {
            if (!enableAuditFileContent) {
                // 无需上传文件时 判断是否自动同意
                if (enableAutoApprove) {
                    auditApplyDetailDTO.setState(AuditApplyStateEnum.APPROVED);
                } else {
                    auditApplyDetailDTO.setState(AuditApplyStateEnum.PENDING_APPROVAL);
                }
            } else {
                // 需要上传文件
                auditApplyDetailDTO.setState(AuditApplyStateEnum.UPLOADING);
            }
        }

        // 2.保存文件列表
        for (AuditFileDTO auditFileDTO : auditApplyDetailDTO.getAuditFileList()) {
            if (!enableAuditFileContent || isCloseApply) {
                auditFileDTO.setFileState(AuditFileStateEnum.NOT_NEED);
            } else {
                // 存在文件后缀则进行拼接
                String fileSuffix = auditFileDTO.getFileSuffix();
                fileSuffix = StringUtils.hasText(fileSuffix) ? "." + fileSuffix : StringUtils.EMPTY;
                auditFileDTO.setFileServerTempPath(File.separator + auditFileDTO.getId() + File.separator + auditFileDTO.getId() + fileSuffix);
            }
        }

        // 3.保存审批流程
        for (AuditApplyAuditLogDTO auditApplyAuditLogDTO : auditApplyDetailDTO.getAuditorList()) {
            if (AuditApplyStateEnum.APPROVED == auditApplyDetailDTO.getState()) {
                auditApplyAuditLogDTO.setAuditorState(AuditApplyAuditLogStateEnum.APPROVED);
                auditApplyAuditLogDTO.setAuditorOpinion(LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_AUTO_APPROVED));
            } else if (AuditApplyStateEnum.REJECTED == auditApplyDetailDTO.getState()) {
                auditApplyAuditLogDTO.setAuditorState(AuditApplyAuditLogStateEnum.REJECTED);
                auditApplyAuditLogDTO.setAuditorOpinion(LocaleI18nResolver.resolve(
                        StringUtils.isEmpty(auditApplyDetailDTO.getFailReason()) ? 
                                AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_EXCEED_LIMIT_AUTO_REJECT :
                                AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_SERVER_ERROR_AUTO_REJECT));
            } else {
                auditApplyAuditLogDTO.setAuditorState(AuditApplyAuditLogStateEnum.PENDING_APPROVAL);
            }
        }
    }

    @Override
    public AuditFileDTO findAuditFileById(UUID fileId) throws BusinessException {
        Assert.notNull(fileId, "fileId must not be null");

        AuditFileEntity auditFileEntity = auditFileDAO.findById(fileId)
                .orElseThrow(() -> new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_NOT_EXIST, String.valueOf(fileId)));
        AuditFileDTO auditFileDTO = new AuditFileDTO();
        BeanUtils.copyProperties(auditFileEntity, auditFileDTO);
        return auditFileDTO;
    }

    @Override
    public AuditFileGlobalConfigDTO obtainAuditFileGlobalConfig() {
        String auditFileGlobalStrategy = globalParameterService.findParameter(AuditFileConstants.AUDIT_FILE_GLOBAL_STRATEGY_KEY);
        return JSON.parseObject(auditFileGlobalStrategy, AuditFileGlobalConfigDTO.class);
    }

    @Override
    public CbbLocalExternalStorageDTO checkAuditFileGlobalExtStorageState() throws BusinessException {
        // 校验外置存储
        AuditFileGlobalConfigDTO fileGlobalConfigDTO = obtainAuditFileGlobalConfig();
        UUID extStorageId = fileGlobalConfigDTO.getExternalStorageId();
        // 未开启外置存储
        if (BooleanUtils.isFalse(fileGlobalConfigDTO.getEnableExtStorage()) || Objects.isNull(extStorageId)) {
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_GLOBAL_NOT_OPEN_EXTERNAL_STORAGE);
        }
        // 外置存储状态不可用
        CbbLocalExternalStorageDTO extStorageDetail = cbbExternalStorageMgmtAPI.getExternalStorageDetail(extStorageId);
        if (extStorageDetail.getExtStorageState() != ExternalStorageHealthStateEnum.HEALTHY &&
                extStorageDetail.getExtStorageState() != ExternalStorageHealthStateEnum.WARNING) {
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_STATE_NOT_AVAILABLE, extStorageDetail.getName());
        }
        return extStorageDetail;
    }

    @Override
    public void handleUploadedByFileId(UUID fileId, Date applyCreateTime) throws BusinessException, InterruptedException {
        Assert.notNull(fileId, "fileId must be not null");
        Assert.notNull(applyCreateTime, "applyCreateTime must be not null");
        AuditFileEntity auditFileEntity = auditFileDAO.findById(fileId)
                .orElseThrow(() -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_EXPORT_APPLY_FILE_NOT_EXIST, String.valueOf(fileId)));
        UUID applyId = auditFileEntity.getApplyId();
        Path tempPath = Paths.get(Constants.AUDIT_FILE_FTP_TEMP_PATH, auditFileEntity.getFileServerTempPath());
        
        // 1.校验申请单和文件单是否处于待文件上传状态，不是则把客户端上传的文件删除
        AuditApplyEntity auditFileApply = auditApplyDAO.findById(applyId).orElseThrow(
            () -> new BusinessException(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST, String.valueOf(applyId)));
        if (AuditFileStateEnum.UPLOADING != auditFileEntity.getFileState() || AuditApplyStateEnum.UPLOADING != auditFileApply.getState()) {
            try {
                deleteTempFile(tempPath, auditFileEntity.getId());
            } catch (IOException e) {
                LOGGER.error("文件流转审计申请单[{}],删除临时文件[{}]异常", applyId, auditFileEntity.getId(), e);
            }
            return;
        }
        
        // 2.计算剩余空间,告警并退出循环
        File tempFile = tempPath.toFile();
        if (!tempFile.exists()) {
            // 是否需要重试处理
            this.needRetryHandler(fileId, true, LocaleI18nResolver.resolve(
                    AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_TEMP_PATH_NOT_FIND, String.valueOf(fileId), tempFile.getPath()));
            LOGGER.warn("申请单[{}]临时文件[{}]不存在,路径[{}]", applyId, auditFileEntity.getId(), tempPath);
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_TEMP_PATH_NOT_FIND, String.valueOf(fileId),
                    tempFile.getPath());
        }
        
        // 3.更新文件状态进行文件完整性校验
        if (updateAuditFileState(applyId, auditFileEntity.getId(), AuditFileStateEnum.COMPUTING, null) == 0) {
            LOGGER.info("申请单[{}]下的文件单[{}]更新为：COMPUTING状态失败，直接结束流程", applyId, fileId);
            return;
        }
        // 临时文件大小必须和数据库保存的大小一致、md5因为客户端和CDC对个别文件算出来不一致，不进行比对
        if (!Objects.equals(auditFileEntity.getFileSize(), tempFile.length())) {
            LOGGER.warn("申请单[{}]临时文件[{}]校验大小不一致，客户端上传文件大小为：{}，计算出的文件大小：{}，删除临时文件[{}]",
                    applyId, auditFileEntity.getId(), auditFileEntity.getFileSize(), tempFile.length(), tempPath);
            try {
                deleteTempFile(tempPath, auditFileEntity.getId());
            } catch (IOException e) {
                LOGGER.error("撤回文件流转审计申请单[{}],删除临时文件[{}]异常", applyId, auditFileEntity.getId(), e);
            }
            this.needRetryHandler(fileId, true,
                    LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_MD5_NOT_MATCH));
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_MD5_NOT_MATCH);
        }
        // 4.复制文件到存储目录,再次校验MD5
        String createTime = DateUtils.format(applyCreateTime, DateUtils.DEFAULT_DATE_FORMAT);
        String fileSuffix = auditFileEntity.getFileSuffix();
        fileSuffix = StringUtils.isEmpty(fileSuffix) ? "" : "." + fileSuffix;
        int retryCount = 0;
        while (retryCount < AuditFileConstants.MAX_RETRY_COUNT) {
            // 校验文件服务器是否可用
            UUID extStorageId = validateExternalStorage(auditFileEntity, tempPath);
            try {
                // 校验线程是否终止
                validateFileUploadedThread();
                String fileServerStoragePrefix = String.format(HOME_EXTERNAL_STORAGE_PATH, extStorageId);
                Path storagePath = Paths.get(fileServerStoragePrefix, Constants.AUDIT_FILE_STORAGE_PATH, createTime,
                        applyId + "_" + auditFileEntity.getId().toString() + fileSuffix);
                File storageFile = storagePath.toFile();
                String fileServerStoragePath = storageFile.getPath();
                // 数据库保存相对路径：audit_file/xxx/xxx
                String filePath = fileServerStoragePath.substring(fileServerStoragePath.indexOf(Constants.AUDIT_FILE_STORAGE_PATH));
                // 把路径更新到数据库，防止出现异常中断场景，后续空间无法释放
                updateAuditFileInfo(auditFileEntity.getId(), filePath);
                LOGGER.debug("申请单[{}]临时文件[{}]开始进行copy", applyId, fileId);
                copy(tempFile, storageFile);
                LOGGER.debug("申请单[{}]临时文件[{}]完成copy", applyId, fileId);
                // 如果文件复制完成，发现申请单为FAIL，CANCELED 或 文件单为FAIL，则删除文件结束流程
                if (hasTerminateFlow(applyId, fileId)) {
                    Files.deleteIfExists(storagePath);
                    deleteTempFile(tempPath, auditFileEntity.getId());
                    LOGGER.warn("申请单[{}]或文件[{}]状态为FAIL，直接删除文件[{}]", applyId, fileId, storagePath);
                    return;
                }
                // 文件大小必须一致、md5因为客户端和CDC对个别文件算出来不一致，不进行比对
                if (Objects.equals(auditFileEntity.getFileSize(), storageFile.length())) {
                    deleteTempFile(tempPath, auditFileEntity.getId());
                    updateAuditFileState(applyId, auditFileEntity.getId(), AuditFileStateEnum.UPLOADED, null);
                    LOGGER.info("申请单[{}]复制文件[{}]到文件服务器[{}]成功，删除临时文件成功", applyId,
                            auditFileEntity.getId(), fileServerStoragePath);
                    return;
                }
                LOGGER.warn("申请单[{}]复制文件[{}]校验文件大小不一致，[{}]", applyId, auditFileEntity.getId(), storagePath);
                Files.deleteIfExists(storagePath);
                throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_ERROR);
            } catch (InterruptedException e) {
                LOGGER.error("申请单[{}]复制文件[{}]过程中被取消", applyId, auditFileEntity.getId());
                throw e;
            } catch (Exception ex) {
                LOGGER.error("申请单[{}]复制文件[{}]过程中发生错误", applyId, auditFileEntity.getId(), ex);
                if (retryCount == AuditFileConstants.MAX_RETRY_COUNT - 1) {
                    // 记录错误信息，更新申请单状态
                    this.needRetryHandler(fileId, false,
                            LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_ERROR));
                    throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_ERROR, ex);
                }
            }
            retryCount++;
        }
    }

    @Override
    public void initAuditFileState() {
        List<AuditFileEntity> fileEntityList = auditFileDAO.findByFileState(AuditFileStateEnum.COMPUTING);
        if (CollectionUtils.isEmpty(fileEntityList)) {
            return;
        }
        fileEntityList.forEach(auditFileEntity -> auditFileEntity.setFileState(AuditFileStateEnum.UPLOADING));
        auditFileDAO.saveAll(fileEntityList);
    }

    @Override
    public void deleteAuditApplyFiles(UUID applyId, LockableExecutor.LockableRunnable updateApplyRunnable) throws BusinessException {
        Assert.notNull(applyId, "applyId must be not null");
        Assert.notNull(updateApplyRunnable, "updateApplyRunnable must be not null");
        List<AuditFileEntity> auditFileList = auditFileDAO.findByApplyId(applyId);
        if (CollectionUtils.isEmpty(auditFileList)) {
            // 执行更新逻辑
            updateApplyRunnable.run();
            return;
        }
        // 删除FTP临时空间上的文件
        auditFileList.parallelStream().forEach(auditFileEntity -> {
            try {
                String fileServerTempPath = auditFileEntity.getFileServerTempPath();
                if (StringUtils.hasText(fileServerTempPath)) {
                    deleteTempFile(Paths.get(Constants.AUDIT_FILE_FTP_TEMP_PATH, fileServerTempPath), auditFileEntity.getId());
                }
            } catch (Exception e) {
                LOGGER.error("文件流转审计申请单[{}]，删除临时文件[{}]异常", applyId, auditFileEntity.getId(), e);
            }
        });
        // 执行更新逻辑
        updateApplyRunnable.run();
        // 异步删除文件服务器上指定申请单下的文件：防止出现文件服务器异常卡主10s的情况
        ThreadExecutors.execute(DELETE_APPLY_FILE_THREAD_NAME, () -> deleteAuditApplyExternalStorageFile(applyId, auditFileList));
    }
    
    private void deleteAuditApplyExternalStorageFile(UUID applyId, List<AuditFileEntity> auditFileList) {
        // 检查文件服务器
        UUID extStorageId;
        try {
            extStorageId = this.checkAuditFileGlobalExtStorageState().getId();
        } catch (Exception e) {
            LOGGER.error("文件流转审计申请单[{}]，检查文件服务器出现异常，直接结束删除流程", applyId, e);
            return;
        }
        // 删除文件服务器上指定申请单下的文件
        auditFileList.parallelStream().forEach(auditFileEntity -> {
            try {
                if (StringUtils.hasText(auditFileEntity.getFileServerStoragePath()) && Objects.nonNull(extStorageId)) {
                    String fileServerStoragePrefix = String.format(HOME_EXTERNAL_STORAGE_PATH, extStorageId);
                    Files.deleteIfExists(Paths.get(fileServerStoragePrefix, auditFileEntity.getFileServerStoragePath()));
                }
            } catch (Exception e) {
                LOGGER.error("文件流转审计申请单[{}]，删除文件服务器上的文件[{}]异常", applyId, auditFileEntity.getId(), e);
            }
        });
    }
    
    private UUID validateExternalStorage(AuditFileEntity auditFileEntity, Path tempPath) throws BusinessException {
        // 外置存储校验
        try {
            return this.checkExternalStorageSpace(auditFileEntity.getFileSize());
        } catch (BusinessException e) {
            LOGGER.error("文件[{}]上传-校验导出审批持久化存储空间出错:", auditFileEntity.getId(), e);
            // 删除临时文件
            try {
                deleteTempFile(tempPath, auditFileEntity.getId());
            } catch (IOException ex) {
                LOGGER.error("删除ftp[{}]临时文件失败：", tempPath, e);
            }
            // 外置存储校验出错，直接置为失败
            this.needRetryHandler(auditFileEntity.getId(), false, e.getI18nMessage());
            throw e;
        }
    }

    private UUID checkExternalStorageSpace(Long totalFileSize) throws BusinessException {
        CbbLocalExternalStorageDTO externalStorageDTO = checkAuditFileGlobalExtStorageState();
        // 文件服务器空间不足
        if (externalStorageDTO.getUsableCapacity() < totalFileSize) {
            throw new BusinessException(AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_SPACE_NOT_ENOUGH, externalStorageDTO.getName());
        }
        return externalStorageDTO.getId();
    }

    private void needRetryHandler(UUID fileId, Boolean canRetry, String errMsg) throws BusinessException {
        Assert.notNull(fileId, "fileId must be not null");
        Assert.notNull(canRetry, "canRetry must be not null");
        Assert.hasText(errMsg, "errMsg must be not null");
        AuditFileEntity auditFileEntity = auditFileDAO.getOne(fileId);
        Integer retryCount = Optional.ofNullable(auditFileEntity.getRetryCount()).orElse(0);
        // 达到最大重试次数/不需要重试直接失败
        if (retryCount >= AuditFileConstants.MAX_RETRY_COUNT || BooleanUtils.isFalse(canRetry)) {
            // 删除申请单全部临时文件，更新文件和申请单状态
            UUID applyId = auditFileEntity.getApplyId();
            try {
                this.deleteAuditApplyFiles(applyId, () -> auditApplyServiceTx.discardAuditApply(applyId, errMsg));
            } catch (BusinessException e) {
                LOGGER.error("申请单[{}]文件[{}]上传文件更新失败，直接终止", applyId, fileId);
            }
            return;
        }
        // 未达到最大重试次数，则累记+1，状态更新为文件待上传
        updateAuditFileState(auditFileEntity.getApplyId(), fileId, AuditFileStateEnum.UPLOADING, ++retryCount);
    }

    private int updateAuditFileState(UUID applyId, UUID fileId, AuditFileStateEnum fileState, Integer retryCount) {
        AtomicInteger result = new AtomicInteger();
        int tryNum = 0;
        while (tryNum <= RETRY_MAX_COUNT) {
            try {
                LockableExecutor.executeWithTryLock(AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK + applyId, () -> {
                    AuditFileEntity auditFileEntity = auditFileDAO.getOne(fileId);
                    if (AuditFileStateEnum.enableAuditFileFinish(auditFileEntity.getFileState())) {
                        return;
                    }
                    auditFileEntity.setFileState(fileState);
                    auditFileEntity.setUpdateTime(new Date());
                    if (Objects.nonNull(retryCount)) {
                        auditFileEntity.setRetryCount(retryCount);
                    }
                    auditFileDAO.save(auditFileEntity);
                    result.getAndIncrement();
                }, AuditFileConstants.AUDIT_FILE_APPLY_STATE_LOCK_TIMEOUT);
                break;
            } catch (BusinessException e) {
                LOGGER.error("更新申请单[{}]文件单[{}]发生错误，当前已重试{}次", applyId, fileId, tryNum, e);
                // 更新错误处理
                updateAuditFileExceptionHandler(applyId, fileId, tryNum);
                tryNum++;
            }
        }
        return result.get();
    }

    private void updateAuditFileExceptionHandler(UUID applyId, UUID fileId, int tryNum) {
        // 达到最大执行次数，抛出异常
        if (tryNum == RETRY_MAX_COUNT) {
            LOGGER.warn("更新申请单[{}]文件单[{}]发生错误，已重试{}次，结束更新文件单状态请求", applyId, fileId, tryNum);
            AuditFileEntity auditFileEntity = auditFileDAO.getOne(fileId);
            // 防止因锁异常导致文件单状态一直卡在校验中的问题
            if (auditFileEntity.getFileState() == AuditFileStateEnum.COMPUTING) {
                auditFileEntity.setFileState(AuditFileStateEnum.UPLOADING);
                auditFileDAO.save(auditFileEntity);
            }
        }
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e1) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void updateAuditFileInfo(UUID id, String path) {
        AuditFileEntity auditFileEntity = auditFileDAO.getOne(id);
        auditFileEntity.setFileServerStoragePath(path);
        auditFileEntity.setUpdateTime(new Date());
        auditFileDAO.save(auditFileEntity);
    }


    private void deleteTempFile(Path tempPath, UUID fileId) throws IOException {
        // 删除临时文件
        Files.deleteIfExists(tempPath);
        // 删除临时文件夹
        Files.deleteIfExists(Paths.get(Constants.AUDIT_FILE_FTP_TEMP_PATH, fileId.toString()));
    }

    private void copy(File sourceFile, File targetFile) throws IOException, InterruptedException {
        IoUtil.createFile(targetFile);
        try (InputStream input = new FileInputStream(sourceFile);
             OutputStream output = new FileOutputStream(targetFile)) {
            final byte[] bufArr = new byte[BUFFER_SIZE];
            while (true) {
                final int len = input.read(bufArr, 0, bufArr.length);
                if (len < 0) {
                    break;
                }
                // 校验线程是否终止
                validateFileUploadedThread();

                output.write(bufArr, 0, len);
            }
            output.flush();
        }
    }
    
    private boolean hasTerminateFlow(UUID applyId, UUID fileId) {
        AuditApplyStateEnum applyState = auditApplyDAO.findById(applyId).map(AuditApplyEntity::getState).orElse(AuditApplyStateEnum.FAIL);
        AuditFileStateEnum fileState = auditFileDAO.findById(fileId).map(AuditFileEntity::getFileState).orElse(AuditFileStateEnum.FAIL);
        return applyState == AuditApplyStateEnum.FAIL || applyState == AuditApplyStateEnum.CANCELED || 
                fileState == AuditFileStateEnum.FAIL || fileState == AuditFileStateEnum.REJECTED || 
                fileState == AuditFileStateEnum.APPROVED;
    }
    
    private void validateFileUploadedThread() throws InterruptedException {
        // 判断线程是否中断，终端则抛出异常，结束当前copy操作
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Uploaded File thread cancelled");
        }
    }
}
