package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/23
 *
 * @author linrenjian
 */
public class ImportVDIDeskDTO {

    private Integer rowNum;

    private String userName;

    private String vdiImageTemplateName;

    private String vdiStrategyName;

    private String vdiNetworkName;

    private String vdiStoragePoolName;

    private String vdiClusterName;
    
    private String cloudPlatformName;

    private String vdiCpu;

    private String vdiMemory;

    private String vdiSystemSize;

    private String vdiPersonSize;

    private String vdiPersonDiskStoragePoolName;

    private String vdiVgpuModel;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVdiImageTemplateName() {
        return vdiImageTemplateName;
    }

    public void setVdiImageTemplateName(String vdiImageTemplateName) {
        this.vdiImageTemplateName = vdiImageTemplateName;
    }

    public String getVdiStrategyName() {
        return vdiStrategyName;
    }

    public void setVdiStrategyName(String vdiStrategyName) {
        this.vdiStrategyName = vdiStrategyName;
    }

    public String getVdiNetworkName() {
        return vdiNetworkName;
    }

    public void setVdiNetworkName(String vdiNetworkName) {
        this.vdiNetworkName = vdiNetworkName;
    }

    public String getVdiStoragePoolName() {
        return vdiStoragePoolName;
    }

    public void setVdiStoragePoolName(String vdiStoragePoolName) {
        this.vdiStoragePoolName = vdiStoragePoolName;
    }

    public String getVdiClusterName() {
        return vdiClusterName;
    }

    public void setVdiClusterName(String vdiClusterName) {
        this.vdiClusterName = vdiClusterName;
    }

    public String getCloudPlatformName() {
        return cloudPlatformName;
    }

    public void setCloudPlatformName(String cloudPlatformName) {
        this.cloudPlatformName = cloudPlatformName;
    }

    public String getVdiCpu() {
        return vdiCpu;
    }

    public void setVdiCpu(String vdiCpu) {
        this.vdiCpu = vdiCpu;
    }

    public String getVdiMemory() {
        return vdiMemory;
    }

    public void setVdiMemory(String vdiMemory) {
        this.vdiMemory = vdiMemory;
    }

    public String getVdiSystemSize() {
        return vdiSystemSize;
    }

    public void setVdiSystemSize(String vdiSystemSize) {
        this.vdiSystemSize = vdiSystemSize;
    }

    public String getVdiPersonSize() {
        return vdiPersonSize;
    }

    public void setVdiPersonSize(String vdiPersonSize) {
        this.vdiPersonSize = vdiPersonSize;
    }

    public String getVdiPersonDiskStoragePoolName() {
        return vdiPersonDiskStoragePoolName;
    }

    public void setVdiPersonDiskStoragePoolName(String vdiPersonDiskStoragePoolName) {
        this.vdiPersonDiskStoragePoolName = vdiPersonDiskStoragePoolName;
    }

    public String getVdiVgpuModel() {
        return vdiVgpuModel;
    }

    public void setVdiVgpuModel(String vdiVgpuModel) {
        this.vdiVgpuModel = vdiVgpuModel;
    }
}
