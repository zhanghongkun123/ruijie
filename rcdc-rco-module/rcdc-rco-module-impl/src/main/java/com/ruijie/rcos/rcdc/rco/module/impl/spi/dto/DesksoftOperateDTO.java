package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.alibaba.fastjson.annotation.JSONField;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/16
 *
 * @author jarman
 */
public class DesksoftOperateDTO {

    @JSONField(name = "productVersion")
    private String softVersion;

    private String companyName;

    /**
     * 产品名称
     */
    private String productName;

    @JSONField(name = "filePathName")
    private String filePath;

    @JSONField(name = "fileDescription")
    private String fileDescription;

    @JSONField(name = "oriFileName")
    private String oriFileName;

    /**
     * 记录数
     */
    @JSONField(name = "operateTimes")
    private Integer operateTimes;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getOperateTimes() {
        return operateTimes;
    }

    public void setOperateTimes(Integer operateTimes) {
        this.operateTimes = operateTimes;
    }

    public String getSoftVersion() {
        return softVersion;
    }

    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDescription() {
        return fileDescription;
    }

    public void setFileDescription(String fileDescription) {
        this.fileDescription = fileDescription;
    }

    public String getOriFileName() {
        return oriFileName;
    }

    public void setOriFileName(String oriFileName) {
        this.oriFileName = oriFileName;
    }


    @Override
    public boolean equals(Object o) {
        Assert.notNull(o, "object cannot be null");
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DesksoftOperateDTO that = (DesksoftOperateDTO) o;
        return Objects.equals(softVersion, that.softVersion) &&
                Objects.equals(companyName, that.companyName) &&
                Objects.equals(filePath, that.filePath) &&
                Objects.equals(fileDescription, that.fileDescription) &&
                Objects.equals(oriFileName, that.oriFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(softVersion, companyName, filePath, fileDescription, oriFileName);
    }
}

