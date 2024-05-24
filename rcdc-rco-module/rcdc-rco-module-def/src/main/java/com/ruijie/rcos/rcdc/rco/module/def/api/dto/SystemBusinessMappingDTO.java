package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import org.springframework.lang.Nullable;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.06
 *
 * @author linhj
 */
public class SystemBusinessMappingDTO {
    /**
     * 逻辑唯一标识，无业务含义
     */
    @Nullable
    private UUID id;

    /**
     * 系统类型
     */
    private String systemType;

    /**
     * 二级业务类型
     */
    private String businessType;

    /**
     * 源业务标识
     */
    private String srcId;

    /**
     * 目标业务标识
     */
    private String destId;

    /**
     * 自定义内容
     */
    private String context;

    private Integer version;

    private Date createDate = new Date();

    private Date updateDate = new Date();

    public SystemBusinessMappingDTO() {

    }

    public SystemBusinessMappingDTO(String systemType, String businessType, String srcId, String destId, String context) {
        this.systemType = systemType;
        this.businessType = businessType;
        this.srcId = srcId;
        this.destId = destId;
        this.context = context;
    }

    public SystemBusinessMappingDTO(String businessType, String srcId, String destId, String context) {
        this.systemType = SyncUpgradeConsts.SYSTEM_TYPE_MTOOL;
        this.businessType = businessType;
        this.srcId = srcId;
        this.destId = destId;
        this.context = context;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
