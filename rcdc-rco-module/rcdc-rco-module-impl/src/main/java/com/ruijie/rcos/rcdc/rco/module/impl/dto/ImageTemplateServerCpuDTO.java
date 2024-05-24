package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateCpuInstallState;

import java.util.Date;
import java.util.UUID;

/**
 * Description: cpu实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/19
 *
 * @author zhiweiHong
 */
public class ImageTemplateServerCpuDTO {

    private UUID id;

    private UUID imageTemplateId;

    private Integer version;

    private String serverCpuType;

    private ImageTemplateCpuInstallState state;

    private Date createTime;

    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getServerCpuType() {
        return serverCpuType;
    }

    public void setServerCpuType(String serverCpuType) {
        this.serverCpuType = serverCpuType;
    }

    public ImageTemplateCpuInstallState getState() {
        return state;
    }

    public void setState(ImageTemplateCpuInstallState state) {
        this.state = state;
    }


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;

    }

}
