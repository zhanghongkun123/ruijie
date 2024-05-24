package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskext.CbbDetailDeskGtInfo;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskext.CbbGtModuleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 14:44
 *
 * @author linrenjian
 */
public class UserDesktopGuestToolDTO {


    private UUID id;

    private String desktopName;

    private UUID cbbDesktopId;

    private String pattern;// CbbC

    /**
     * 最后启动时间
     */
    private Date latestRunningTime;

    /**
     * GT最新心跳时间
     **/
    private Date gtHeartBeatTime;

    private String deskState;// CbbCloudDeskState

    private String deskType;

    /*
     * GT 版本 ，可能为空 不一定准确
     */
    private String guestToolVersion;

    /**
     * 保存编辑前的gt版本号
     */
    private String beforeEditGuestToolVersion;

    private String cbbImageType;

    private String deskCreateMode;


    /**
     * 镜像操作系统类型
     */
    private CbbOsType osType;

    /**
     * 模块信息
     */
    private List<CbbGtModuleDTO> moduleList;


    /**
     * 转桌面信息
     *
     * @return CbbDetailDeskExtInfo
     */

    public CbbDetailDeskGtInfo convertDetailDeskExtInfo() {
        CbbDetailDeskGtInfo cbbDetailDeskExtInfo = new CbbDetailDeskGtInfo();
        cbbDetailDeskExtInfo.setId(cbbDesktopId);
        cbbDetailDeskExtInfo.setGuestToolVersion(guestToolVersion);
        cbbDetailDeskExtInfo.setGtHeartBeatTime(gtHeartBeatTime);
        if (StringUtils.hasText(pattern)) {
            cbbDetailDeskExtInfo.setPattern(CbbCloudDeskPattern.valueOf(pattern));
        }
        cbbDetailDeskExtInfo.setLatestRunningTime(latestRunningTime);
        cbbDetailDeskExtInfo.setBeforeEditGuestToolVersion(beforeEditGuestToolVersion);
        if (StringUtils.hasText(cbbImageType)) {
            cbbDetailDeskExtInfo.setCbbImageType(CbbImageType.valueOf(cbbImageType));
        }
        cbbDetailDeskExtInfo.setDesktopName(desktopName);
        cbbDetailDeskExtInfo.setOsType(osType);
        return cbbDetailDeskExtInfo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public UUID getCbbDesktopId() {
        return cbbDesktopId;
    }

    public void setCbbDesktopId(UUID cbbDesktopId) {
        this.cbbDesktopId = cbbDesktopId;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Date getLatestRunningTime() {
        return latestRunningTime;
    }

    public void setLatestRunningTime(Date latestRunningTime) {
        this.latestRunningTime = latestRunningTime;
    }

    public String getDeskState() {
        return deskState;
    }

    public void setDeskState(String deskState) {
        this.deskState = deskState;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public String getBeforeEditGuestToolVersion() {
        return beforeEditGuestToolVersion;
    }

    public void setBeforeEditGuestToolVersion(String beforeEditGuestToolVersion) {
        this.beforeEditGuestToolVersion = beforeEditGuestToolVersion;
    }

    public String getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(String cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public String getDeskCreateMode() {
        return deskCreateMode;
    }

    public void setDeskCreateMode(String deskCreateMode) {
        this.deskCreateMode = deskCreateMode;
    }

    public Date getGtHeartBeatTime() {
        return gtHeartBeatTime;
    }

    public void setGtHeartBeatTime(Date gtHeartBeatTime) {
        this.gtHeartBeatTime = gtHeartBeatTime;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }


    public String getGuestToolVersion() {
        return guestToolVersion;
    }

    public void setGuestToolVersion(String guestToolVersion) {
        this.guestToolVersion = guestToolVersion;
    }

    public List<CbbGtModuleDTO> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<CbbGtModuleDTO> moduleList) {
        this.moduleList = moduleList;
    }
}
