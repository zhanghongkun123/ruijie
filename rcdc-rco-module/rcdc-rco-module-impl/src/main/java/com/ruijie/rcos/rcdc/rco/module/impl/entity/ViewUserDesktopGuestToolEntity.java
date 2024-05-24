package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskext.CbbGtModuleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopGuestToolDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * 云桌面GT查询对象
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年8月30日
 * 
 * @author linrenjian
 */
@Entity
@Table(name = "v_cbb_user_desktop_guesttool")
public class ViewUserDesktopGuestToolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String desktopName;

    private UUID cbbDesktopId;

    private String pattern;// CbbC

    /**
     * 最后启动时间
     */
    private Date latestRunningTime;

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
    @Enumerated(EnumType.STRING)
    private CbbOsType osType;

    /**
     * GT最新心跳时间
     **/
    private Date gtHeartBeatTime;

    /**
     * 模块信息
     */
    private String moduleInfo;

    @Version
    private Integer version;

    /**
     * 数据库实体 转DTO
     * 
     * @return UserDesktopGuestToolDTO
     */

    public UserDesktopGuestToolDTO convertToDTO() {
        UserDesktopGuestToolDTO userDesktopGuestToolDTO = new UserDesktopGuestToolDTO();
        BeanUtils.copyProperties(this, userDesktopGuestToolDTO);
        if (StringUtils.isNotBlank(this.moduleInfo)) {
            userDesktopGuestToolDTO.setModuleList(JSONObject.parseArray(this.moduleInfo, CbbGtModuleDTO.class));
        }
        return userDesktopGuestToolDTO;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public void setDeskType(String desktopType) {
        this.deskType = desktopType;
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

    public String getModuleInfo() {
        return moduleInfo;
    }

    public void setModuleInfo(String moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public String getGuestToolVersion() {
        return guestToolVersion;
    }

    public void setGuestToolVersion(String guestToolVersion) {
        this.guestToolVersion = guestToolVersion;
    }
}
