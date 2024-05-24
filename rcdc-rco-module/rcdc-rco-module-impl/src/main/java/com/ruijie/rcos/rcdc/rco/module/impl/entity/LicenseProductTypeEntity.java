package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

/**
 * Description: license产品型号信息表
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月12日
 *
 * @author zouqi
 */
@Entity
@Table(name = "t_rco_license_product_type")
public class LicenseProductTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**License产品编号  */
    private String featureId;
    
    /**License控制项编码  */
    private String featureCode;

    /**可授权数量  */
    private Integer licenseNum;

    /**创建时间  */
    private Date createTime;

    @Version
    private Integer version;
    
    
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public Integer getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(Integer licenseNum) {
        this.licenseNum = licenseNum;
    }

}
