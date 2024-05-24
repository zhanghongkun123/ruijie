package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DeskCreateMode;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.DateUtils;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Description: 导出应用桌面实体类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月11日
 *
 * @author liuwc
 */
public class ExportRcaHostDesktopDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportRcaHostDesktopDTO.class);

    /**
     * 下载状态国际化的前缀，使用这个前缀加上下载状态匹配对应国际化状态
     */
    private static final String DOWNLOAD_STATE_PREFIX = "rcdc_rco_download_state_";

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_DESKTOP_NAME)
    private String desktopName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_NAME)
    private String userName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_REAL_NAME)
    private String realName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_USER_TYPE)
    private String userType;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_DESKTOP_POOL_TYPE)
    private String rcaPoolType;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_DESKTOP_POOL_NAME)
    private String rcaPoolName;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_DESKTOP_IP)
    private String desktopIp;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_DESKTOP_STATE)
    private String desktopState;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_DESKTOP_CATEGORY)
    private String desktopCategory;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_DESKTOP_CREATE_MODE)
    private String desktopCreateMode;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CPU)
    private String cpu;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_MEMORY)
    private String memory;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_SYSTEM_DISK)
    private String systemDisk;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_PERSONAL_DISK)
    private String persionalDisk;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_VGPU_SIZE)
    private String vgpuModel;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_SESSION_TYPE)
    private String rcaHostSessionType;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_COLUMNS_HOST_MAX_SESSION)
    private String rcaMaxSessionCount;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_HOST_COLUMNS_DESKTOP_TYPE)
    private String desktopType;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_ROOT_IMAGE_NAME)
    private String rootImageName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_IMAGE_NAME)
    private String imageName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_PHYSICAL_SERVERIP)
    private String physicalServerIp;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_COMPUTER_NAME)
    private String computerName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CLUSTER_NAME)
    private String clusterName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_ONE_AGENT_VERSION)
    private String oneAgentVersion;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CREATE_TIME)
    private String createTime;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CLOUD_PLATFORM_NAME)
    private String platformName;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CLOUD_PLATFORM_STATUS)
    private String platformStatus;

    @ExcelHead(BusinessKey.RCDC_RCO_EXPORT_COLUMNS_CLOUD_PLATFORM_TYPE)
    private String platformType;

    @ExcelHead(RcaBusinessKey.RCDC_RCO_EXPORT_HOST_COLUMNS_REMARK)
    private String remark;


    public ExportRcaHostDesktopDTO() {

    }

    // 从CloudDeskTopDTO中获得数据生成实体
    public ExportRcaHostDesktopDTO(RcaHostDesktopDTO rcaHostCloudDesktopDTO) {
        Assert.notNull(rcaHostCloudDesktopDTO, "rcaHostCloudDesktopDTO is not null");

        BeanUtils.copyProperties(rcaHostCloudDesktopDTO, this);
        this.createTime = DateUtils.format(rcaHostCloudDesktopDTO.getCreateTime(), DateUtils.NORMAL_DATE_FORMAT);
        this.cpu = formatObject(rcaHostCloudDesktopDTO.getCpu(), BusinessKey.RCDC_RCO_FORMAT_CPU);
        this.memory = formatObject(rcaHostCloudDesktopDTO.getMemory(), BusinessKey.RCDC_RCO_FORMAT_MEMORY);
        this.systemDisk = formatObject(rcaHostCloudDesktopDTO.getSystemDisk(), BusinessKey.RCDC_RCO_FORMAT_SYSTEMDISK);
        this.persionalDisk = formatObject(rcaHostCloudDesktopDTO.getPersonDisk(), BusinessKey.RCDC_RCO_FORMAT_PERSIONDISK);
        this.vgpuModel = rcaHostCloudDesktopDTO.getVgpuModel();
        this.desktopState = parseDesktopState(rcaHostCloudDesktopDTO.getDesktopState());
        this.desktopType = parseDesktopType();
        this.desktopCreateMode = parseDesktopCreateMode(rcaHostCloudDesktopDTO.getDeskCreateMode());
        this.userType = parseUserType();
        // 导出专用 原来 镜像（版本）名称->镜像名称/版本名称, 源镜像名称->镜像名称
        // 相应的rootImageName = rootImageName!=null ? rootImageName : imageName
        // 相应的imageName = rootImageName!=null ? imageName : null
        this.rootImageName = StringUtils.isEmpty(rcaHostCloudDesktopDTO.getRootImageName()) ? rcaHostCloudDesktopDTO.getImageName()
                : rcaHostCloudDesktopDTO.getRootImageName();
        this.imageName = StringUtils.isEmpty(rcaHostCloudDesktopDTO.getRootImageName()) ? null : rcaHostCloudDesktopDTO.getImageName();
        this.platformName = rcaHostCloudDesktopDTO.getPlatformName();
        this.platformStatus = rcaHostCloudDesktopDTO.getPlatformStatus().getDesc();
        this.platformType = parsePlatformType(rcaHostCloudDesktopDTO.getPlatformType());
        this.rcaHostSessionType = parseRcaHostSessionType(rcaHostCloudDesktopDTO.getRcaHostSessionType());
        this.rcaMaxSessionCount =
                parseRcaSessionUsageCount(rcaHostCloudDesktopDTO.getRcaSessionUsage(), rcaHostCloudDesktopDTO.getRcaMaxSessionCount());
        this.rcaPoolType = parseRcaPoolType(rcaHostCloudDesktopDTO.getRcaPoolType());
        try {
            format();
        } catch (IllegalAccessException e) {
            LOGGER.info("格式化ExportCloudDesktopDTO类失败，失败原因是{}", e);
        }
    }

    private String parseUserType() {
        if (StringUtils.isEmpty(this.userType)) {
            return "";
        }
        IacUserTypeEnum userTypeEnum = IacUserTypeEnum.valueOf(this.userType);
        String businessKey = null;
        switch (userTypeEnum) {
            case NORMAL:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_NORMAL;
                break;
            case VISITOR:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_VISITOR;
                break;
            case AD:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_AD;
                break;
            case LDAP:
                businessKey = BusinessKey.RCDC_RCO_USER_TYPE_LDAP;
                break;
            default:
                LOGGER.error("can not find userTypeEnum in BaseUserTypeEnum, now is {}", userTypeEnum);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseDownloadFinishTime(Date downloadFinishTime) {
        return Optional.ofNullable(downloadFinishTime).map(time -> DateUtils.format(downloadFinishTime, DateUtils.NORMAL_DATE_FORMAT))
                .orElse(StringUtils.EMPTY);
    }

    private String parseDownloadState(DownloadStateEnum downloadState) {
        // 使用字符串拼接的方式，在I18N文件中找到对应的状态
        return Optional.ofNullable(downloadState).map(state -> LocaleI18nResolver.resolve(DOWNLOAD_STATE_PREFIX + downloadState.name().toLowerCase()))
                .orElse(StringUtils.EMPTY);
    }

    private String formatLatestLoginTime(CloudDesktopDTO cloudDesktopDTO) {
        if (Objects.isNull(cloudDesktopDTO.getLatestLoginTime())) {
            // 若最近登录时间为空（新创建的未登录过的云桌面），则为空，不设置默认值
            return null;
        }
        return DateUtils.format(cloudDesktopDTO.getLatestLoginTime(), DateUtils.NORMAL_DATE_FORMAT);
    }

    private String formatObject(Object obj, String key) {
        if (Objects.isNull(obj) || (obj instanceof String && StringUtils.isEmpty((String) obj))) {
            return "--";
        }

        return LocaleI18nResolver.resolve(key, String.valueOf(obj));
    }

    private void format() throws IllegalAccessException {
        Class<? extends ExportRcaHostDesktopDTO> clzz = this.getClass();
        Field[] fieldArr = clzz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, "");
            }
        }
    }

    public String getDesktopName() {
        return desktopName;
    }

    private String parseDesktopState(String desktopState) {
        if (StringUtils.isEmpty(desktopState)) {
            return "";
        }
        CbbCloudDeskState cbbCloudDeskState = CbbCloudDeskState.valueOf(desktopState);
        String businessKey = null;
        switch (cbbCloudDeskState) {
            case SLEEP:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_SLEEP;
                break;
            case RUNNING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_RUNNING;
                break;
            case CLOSE:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_CLOSE;
                break;
            case SHUTTING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_SHUTTING;
                break;
            case DELETING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_DELETING;
                break;
            case RESTORING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_RESTORING;
                break;
            case UPDATING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_UPDATING;
                break;
            case START_UP:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_START_UP;
                break;
            case REBOOTING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_REBOOTING;
                break;
            case WAKING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_WAKING;
                break;
            case CREATING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_CREATING;
                break;
            case ERROR:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_ERROR;
                break;
            case FAILBACKING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_FAILBACKING;
                break;
            case OFF_LINE:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_OFF_LINE;
                break;
            case DOWNLOADING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_DOWNLOADING;
                break;
            case SNAPSHOT_CREATING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_SNAPSHOT_CREATING;
                break;
            case SNAPSHOT_RESTORING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_SNAPSHOT_RESTORING;
                break;
            case MOVING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_MOVING;
                break;
            case COMPLETE_DELETING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_COMPLETE_DELETING;
                break;
            case SLEEPING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_SLEEPING;
                break;
            case RECYCLE_BIN:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_RECYCLE_BIN;
                break;
            case RECOVING:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_STATE_RECOVING;
                break;
            default:
                LOGGER.error("can not find state in cbbCloudDeskState, now is {}", cbbCloudDeskState);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseDesktopType() {
        if (StringUtils.isEmpty(this.desktopType)) {
            return "";
        }

        CbbCloudDeskPattern cbbCloudDeskPattern = CbbCloudDeskPattern.valueOf(this.desktopType);

        String businessKey = null;
        switch (cbbCloudDeskPattern) {
            case PERSONAL:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_TYPE_PERSONAL;
                break;
            case APP_LAYER:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_TYPE_APP_LAYER;
                break;
            case RECOVERABLE:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_TYPE_RECOVERABLE;
                break;
            default:
                LOGGER.error("can not find desktop in cbbCloudDeskPattern, now is {}", cbbCloudDeskPattern);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseDesktopCreateMode(String desktopCreateMode) {
        DeskCreateMode deskCreateMode = DeskCreateMode.valueOf(desktopCreateMode);
        String businessKey = null;
        switch (deskCreateMode) {
            case LINK_CLONE:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_CREATE_MODE_LINK_CLONE;
                break;
            case FULL_CLONE:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_CREATE_MODE_FULL_CLONE;
                break;
            case OTHER:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_CREATE_MODE_OTHER;
                break;
            default:
                LOGGER.error("can not find deskCreateMode in DeskCreateMode, now is {}", deskCreateMode);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parsePlatformType(CloudPlatformType cloudPlatformType) {
        if (Objects.isNull(cloudPlatformType)) {
            return "";
        }
        String businessKey = null;
        switch (cloudPlatformType) {
            case RCCP:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_PLATFORM_TYPE_RCCP;
                break;
            case ZETA:
                businessKey = BusinessKey.RCDC_RCO_DESKTOP_PLATFORM_TYPE_ZETA;
                break;
            default:
                LOGGER.error("can not find cloudPlatformType, now is {}", cloudPlatformType);
                return cloudPlatformType.name();
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseRcaHostSessionType(RcaEnum.HostSessionType hostSessionType) {
        String businessKey = null;
        switch (hostSessionType) {
            case SINGLE:
                businessKey = RcaBusinessKey.RCDC_RCO_DESKTOP_SESSION_TYPE_SINGLE;
                break;
            case MULTIPLE:
                businessKey = RcaBusinessKey.RCDC_RCO_DESKTOP_SESSION_TYPE_MULTIPLE;
                break;
            default:
                LOGGER.error("can not find deskCreateMode in DeskCreateMode, now is {}", hostSessionType);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseRcaPoolType(RcaEnum.PoolType hostPoolType) {
        String businessKey = null;
        switch (hostPoolType) {
            case STATIC:
                businessKey = RcaBusinessKey.RCDC_RCO_DESKTOP_RCA_POOL_TYPE_STATIC;
                break;
            case DYNAMIC:
                businessKey = RcaBusinessKey.RCDC_RCO_DESKTOP_RCA_POOL_TYPE_DYNAMIC;
                break;
            default:
                LOGGER.error("can not find deskCreateMode in DeskCreateMode, now is {}", hostPoolType);
                return "";
        }
        return LocaleI18nResolver.resolve(businessKey);
    }

    private String parseRcaSessionUsageCount(int usageCount, int maxSessionCount) {
        String usage = String.valueOf(usageCount);
        String maxSession = String.valueOf(maxSessionCount);

        if (String.valueOf(NumberUtils.INTEGER_ZERO).equals(maxSession)) {
            maxSession = LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_RCO_DESKTOP_RCA_POOL_SESSION_WITHOUT_LIMIT);
        }
        return LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_RCO_DESKTOP_RCA_POOL_USAGE_MAX_SESSION, usage, maxSession);
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopCategory() {
        return desktopCategory;
    }

    public void setDesktopCategory(String desktopCategory) {
        this.desktopCategory = desktopCategory;
    }

    public String getDesktopCreateMode() {
        return desktopCreateMode;
    }

    public void setDesktopCreateMode(String desktopCreateMode) {
        this.desktopCreateMode = desktopCreateMode;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getRootImageName() {
        return rootImageName;
    }

    public void setRootImageName(String rootImageName) {
        this.rootImageName = rootImageName;
    }

    public String getDesktopIp() {
        return desktopIp;
    }

    public void setDesktopIp(String desktopIp) {
        this.desktopIp = desktopIp;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public String getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(String desktopState) {
        this.desktopState = desktopState;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(String systemDisk) {
        this.systemDisk = systemDisk;
    }

    public String getPersionalDisk() {
        return persionalDisk;
    }

    public void setPersionalDisk(String persionalDisk) {
        this.persionalDisk = persionalDisk;
    }

    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(String vgpuModel) {
        this.vgpuModel = vgpuModel;
    }

    public String getPhysicalServerIp() {
        return physicalServerIp;
    }

    public void setPhysicalServerIp(String physicalServerIp) {
        this.physicalServerIp = physicalServerIp;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getRcaPoolType() {
        return rcaPoolType;
    }

    public void setRcaPoolType(String rcaPoolType) {
        this.rcaPoolType = rcaPoolType;
    }

    public String getRcaPoolName() {
        return rcaPoolName;
    }

    public void setRcaPoolName(String rcaPoolName) {
        this.rcaPoolName = rcaPoolName;
    }

    public String getRcaHostSessionType() {
        return rcaHostSessionType;
    }

    public void setRcaHostSessionType(String rcaHostSessionType) {
        this.rcaHostSessionType = rcaHostSessionType;
    }

    public String getRcaMaxSessionCount() {
        return rcaMaxSessionCount;
    }

    public void setRcaMaxSessionCount(String rcaMaxSessionCount) {
        this.rcaMaxSessionCount = rcaMaxSessionCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getOneAgentVersion() {
        return oneAgentVersion;
    }

    public void setOneAgentVersion(String oneAgentVersion) {
        this.oneAgentVersion = oneAgentVersion;
    }

    public String getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(String platformStatus) {
        this.platformStatus = platformStatus;
    }
}
