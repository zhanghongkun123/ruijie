package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;

import java.util.Date;
import java.util.UUID;

/**
 * Description: CheckUploadRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21
 *
 * @author chenl
 */
public class PacketInfoResponse extends DefaultWebRequest {


    private UUID id;

    private String name;

    private String productType;

    private CbbOsType osType;

    private CbbCpuArchType archType;

    private String version;

    private String status;

    private Long size;

    private Date updateTime;

    private Date createTime;

    private String sourceType;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public CbbCpuArchType getArchType() {
        return archType;
    }

    public void setArchType(CbbCpuArchType archType) {
        this.archType = archType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
