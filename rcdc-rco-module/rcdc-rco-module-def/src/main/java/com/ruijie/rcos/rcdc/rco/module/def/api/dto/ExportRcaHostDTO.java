package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Description: 导出实体类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/17
 *
 * @author zhiweiHong
 */
public class ExportRcaHostDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportRcaHostDTO.class);

    /**
     * 下载状态国际化的前缀，使用这个前缀加上下载状态匹配对应国际化状态
     */
    private static final String HOST_SESSION_COUNT_SPLIT = "/";

    /**
     * 纳管应用主机名称
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_NAME)
    private String name;

    /**
     * 纳管应用主机IP
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_IP)
    private String ip;

    /**
     * 纳管应用主机描述
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_DESC)
    private String description;

    /**
     * 纳管应用主机状态：collecting(收集中)、online(在线)、offline(离线)
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_STATUS)
    private String status;

    /**
     * 所属应用池类型
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_POOL_TYPE)
    private String poolType;

    /**
     * 绑定的应用池名称
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_POOL_NAME)
    private String poolName;

    /**
     * 会话类型
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_SESSION_TYPE)
    private String hostSessionType;

    /**
     * 会话数
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_SESSION_COUNT)
    private String sessionCount;

    /**
     * 纳管应用主机的应用数量
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_APP_COUNT)
    private String appCount;

    /**
     * 纳管应用主机CPU
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_CPU_CORES)
    private String cpu;

    /**
     * 纳管应用主机内存，单位GB
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_MEMORY)
    private String memoryGb;

    /**
     * 纳管应用主机磁盘大小，单位GB
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_SYSTEM_DISK_SIZE)
    private String systemDiskSize;

    /**
     * 应用主机代理版本号
     */
    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_ONE_AGENT_VERSION)
    private String oneAgentVersion;

    /**
     * 应用主机标签
     */
    @ExcelHead(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXPORT_HOST_COLUMNS_REMARK)
    private String remark;


    public ExportRcaHostDTO() {

    }

    // 从RcaHostDTO中获得数据生成实体
    public ExportRcaHostDTO(RcaHostDTO rcaHostDTO) {
        Assert.notNull(rcaHostDTO, "rcaHostDTO is not null");

        BeanUtils.copyProperties(rcaHostDTO, this);
        this.status = formatHostStatus(rcaHostDTO.getStatus());
        this.poolType = formatPoolType(rcaHostDTO.getPoolType());
        this.hostSessionType = formatHostSessionType(rcaHostDTO.getHostSessionType());
        this.sessionCount = formatSessionCount(rcaHostDTO.getSessionUsage(), rcaHostDTO.getMaxSessionCount());
        this.appCount = formatAppCount(rcaHostDTO.getAppCount());
        this.cpu = formatCpu(rcaHostDTO.getCpu());
        this.memoryGb = formatMemoryGb(rcaHostDTO.getMemoryGb());
        this.systemDiskSize = formatSystemDiskSize(rcaHostDTO.getSystemDiskSize());

        try {
            format();
        } catch (IllegalAccessException e) {
            LOGGER.info("格式化ExportCloudDesktopDTO类失败，失败原因是{}", e);
        }
    }

    private String formatHostStatus(RcaEnum.HostStatus status) {
        if (status == null) {
            return "";
        }
        String businessKey = null;
        switch (status) {
            case COLLECTING_LOG:
                businessKey = BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_STATUS_COLLECTING_LOG;
                break;
            case ONLINE:
                businessKey = BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_STATUS_ONLINE;
                break;
            case OFFLINE:
                businessKey = BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_STATUS_OFFLINE;
                break;
            default:
                LOGGER.error("export rcaHost can not find status in HostStatus, now is {}", status);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String formatPoolType(RcaEnum.PoolType poolType) {
        if (poolType == null) {
            return "";
        }
        String businessKey = null;
        switch (poolType) {
            case STATIC:
                businessKey = BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_POOL_TYPE_STATIC;
                break;
            case DYNAMIC:
                businessKey = BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_POOL_TYPE_DYNAMIC;
                break;
            default:
                LOGGER.error("export rcaHost can not find poolType in RcaEnum.PoolType, now is {}", poolType);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String formatHostSessionType(RcaEnum.HostSessionType hostSessionType) {
        if (hostSessionType == null) {
            return "";
        }
        String businessKey = null;
        switch (hostSessionType) {
            case SINGLE:
                businessKey = BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_SESSION_TYPE_SINGLE;
                break;
            case MULTIPLE:
                businessKey = BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_SESSION_TYPE_MULTIPLE;
                break;
            default:
                LOGGER.error("export rcaHost can not find hostSessionType in RcaEnum.HostSessionType, now is {}", hostSessionType);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String formatSessionCount(Integer sessionUsage, Integer maxSessionCount) {
        String sessionUsageStr = null;
        String maxSessionCountStr = null;
        if (Objects.isNull(sessionUsage)) {
            sessionUsageStr = "";
        } else {
            sessionUsageStr = sessionUsage.toString();
        }
        if (Objects.isNull(maxSessionCount)) {
            maxSessionCountStr = "";
        } else if (maxSessionCount == 0) {
            maxSessionCountStr = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_SESSION_COUNT_MAX_NO_LIMIT);
        } else {
            maxSessionCountStr = maxSessionCount.toString();
        }
        return sessionUsageStr + HOST_SESSION_COUNT_SPLIT + maxSessionCountStr;
    }

    private String formatAppCount(Integer appCount) {
        String appCountStr = "";
        if (Objects.nonNull(appCount)) {
            appCountStr = appCount.toString();
        }
        return appCountStr;
    }

    private String formatCpu(Integer cpu) {
        String cpuStr = "";
        if (Objects.nonNull(cpu)) {
            cpuStr = cpu.toString();
        }
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_CPU_CORES_UNIT, cpuStr);
    }

    private String formatMemoryGb(Double memoryGb) {
        String memoryGbStr = "";
        if (Objects.nonNull(memoryGb)) {
            memoryGbStr = memoryGb.toString();
        }
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_MEMORY_UNIT, memoryGbStr);
    }

    private String formatSystemDiskSize(Integer systemDiskSize) {
        String systemDiskSizeStr = "";
        if (Objects.nonNull(systemDiskSize)) {
            systemDiskSizeStr = systemDiskSize.toString();
        }
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_RCA_THIRD_PARTY_HOST_EXCEL_HEAD_HOST_SYSTEM_DISK_SIZE_UNIT, systemDiskSizeStr);
    }

    private void format() throws IllegalAccessException {
        Class<? extends ExportRcaHostDTO> clzz = this.getClass();
        Field[] fieldArr = clzz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, "");
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public String getHostSessionType() {
        return hostSessionType;
    }

    public void setHostSessionType(String hostSessionType) {
        this.hostSessionType = hostSessionType;
    }

    public String getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(String sessionCount) {
        this.sessionCount = sessionCount;
    }

    public String getAppCount() {
        return appCount;
    }

    public void setAppCount(String appCount) {
        this.appCount = appCount;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemoryGb() {
        return memoryGb;
    }

    public void setMemoryGb(String memoryGb) {
        this.memoryGb = memoryGb;
    }

    public String getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(String systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getOneAgentVersion() {
        return oneAgentVersion;
    }

    public void setOneAgentVersion(String oneAgentVersion) {
        this.oneAgentVersion = oneAgentVersion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
