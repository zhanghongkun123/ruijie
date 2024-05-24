package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageTemplateRestorePointState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.10.13
 *
 * @author liangyifeng
 */
public class SnapshotInfoDTO extends PlatformBaseInfoDTO {
    private UUID id;

    private Date createTime;

    private UUID imageTemplateId;

    private String name;

    private boolean enableLocked;

    private String remark;

    private String imageName;

    private Boolean hasInUse;

    private CbbImageTemplateRestorePointState state;

    /**
     * 绑定链接克隆个性云桌面数量
     */
    private Integer clouldDeskopNumOfPersonal = 0;

    /**
     * 绑定链接克隆还原的云桌面数量
     */
    private Integer clouldDeskopNumOfRecoverable = 0;


    /***
     * 绑定链接克隆应用分层的云桌面数量
     */
    private Integer clouldDeskopNumOfAppLayer = 0;

    /**
     * 镜像类型
     */
    private CbbImageType cbbImageType;

    public Integer getClouldDeskopNumOfPersonal() {
        return clouldDeskopNumOfPersonal;
    }

    public void setClouldDeskopNumOfPersonal(Integer clouldDeskopNumOfPersonal) {
        this.clouldDeskopNumOfPersonal = clouldDeskopNumOfPersonal;
    }

    public Integer getClouldDeskopNumOfRecoverable() {
        return clouldDeskopNumOfRecoverable;
    }

    public void setClouldDeskopNumOfRecoverable(Integer clouldDeskopNumOfRecoverable) {
        this.clouldDeskopNumOfRecoverable = clouldDeskopNumOfRecoverable;
    }

    public Integer getClouldDeskopNumOfAppLayer() {
        return clouldDeskopNumOfAppLayer;
    }

    public void setClouldDeskopNumOfAppLayer(Integer clouldDeskopNumOfAppLayer) {
        this.clouldDeskopNumOfAppLayer = clouldDeskopNumOfAppLayer;
    }

    public CbbImageTemplateRestorePointState getState() {
        return state;
    }

    public void setState(CbbImageTemplateRestorePointState state) {
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnableLocked() {
        return enableLocked;
    }

    public void setEnableLocked(boolean enableLocked) {
        this.enableLocked = enableLocked;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Boolean getHasInUse() {
        return hasInUse;
    }

    public void setHasInUse(Boolean hasInUse) {
        this.hasInUse = hasInUse;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }
}
