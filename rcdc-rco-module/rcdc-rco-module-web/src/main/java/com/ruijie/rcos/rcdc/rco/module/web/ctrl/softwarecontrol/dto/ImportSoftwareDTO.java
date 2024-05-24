package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.dto;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.constants.SoftwareControlConstants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.SoftwareValidateRules;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/14
 *
 * @author lihengjing
 */
public class ImportSoftwareDTO {

    private Integer rowNum;

    private String softwareGroupName;

    private String softwareGroupType;

    private String softwareGroupDesc;

    private String softwareName;

    private String softwareDesc;

    private String digitalSign;

    private String digitalSignFlag;

    private String productName;

    private String productNameFlag;

    private String processName;

    private String processNameFlag;

    private String originalFileName;

    private String originalFileNameFlag;

    private String fileCustomMd5;

    private String fileCustomMd5Flag;

    private String directoryFlag;

    private String id;

    private String parentId;

    private List<ImportSoftwareDTO> childrenList;

    /**
     * 厂商数字签名(黑名单) 
     */
    private String digitalSignBlackFlag;

    /**
     * 产品名称(黑名单) 
     */
    private String productNameBlackFlag;

    /**
     * 进程名(黑名单) 
     */
    private String processNameBlackFlag;

    /**
     * 原始文件名(黑名单) 
     */
    private String originalFileNameBlackFlag;

    /**
     * 自定义md5值(黑名单) 
     */
    private String fileCustomMd5BlackFlag;

    /**
     * @return 返回装换 software DTO
     */
    public SoftwareDTO convertToSoftwareDTO() {
        SoftwareDTO request = new SoftwareDTO();
        BeanUtils.copyProperties(this, request);
        String softwareName = this.getSoftwareName();
        if (softwareName.length() > SoftwareValidateRules.SOFTWARE_NAME_SIZE) {
            softwareName = softwareName.substring(0, SoftwareValidateRules.SOFTWARE_NAME_SIZE);
        }
        request.setName(softwareName);
        request.setDescription(this.getSoftwareDesc());
        request.setDigitalSignFlag(formatFlag(this.getDigitalSignFlag()));
        request.setProductNameFlag(formatFlag(this.getProductNameFlag()));
        request.setProcessNameFlag(formatFlag(this.getProcessNameFlag()));
        request.setOriginalFileNameFlag(formatFlag(this.getOriginalFileNameFlag()));
        request.setFileCustomMd5Flag(formatFlag(this.getFileCustomMd5Flag()));
        request.setDirectoryFlag(formatFlag(this.getDirectoryFlag()));

        //黑名单字段
        request.setDigitalSignBlackFlag(formatFlag(this.getDigitalSignBlackFlag()));
        request.setProductNameBlackFlag(formatFlag(this.getProductNameBlackFlag()));
        request.setProcessNameBlackFlag(formatFlag(this.getProcessNameBlackFlag()));
        request.setOriginalFileNameBlackFlag(formatFlag(this.getOriginalFileNameBlackFlag()));
        request.setFileCustomMd5BlackFlag(formatFlag(this.getFileCustomMd5BlackFlag()));

        request.setId(UUID.randomUUID());
        request.setTopLevelFile(true);
        return request;
    }

    private Boolean formatFlag(String flag) {
        return SoftwareControlConstants.SOFTWARE_IMPORT_FLAG_TRUE.equals(flag.trim());
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getSoftwareGroupName() {
        return softwareGroupName;
    }

    public void setSoftwareGroupName(String softwareGroupName) {
        this.softwareGroupName = softwareGroupName;
    }

    public String getSoftwareGroupType() {
        return softwareGroupType;
    }

    public void setSoftwareGroupType(String softwareGroupType) {
        this.softwareGroupType = softwareGroupType;
    }

    public String getSoftwareGroupDesc() {
        return softwareGroupDesc;
    }

    public void setSoftwareGroupDesc(String softwareGroupDesc) {
        this.softwareGroupDesc = softwareGroupDesc;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getSoftwareDesc() {
        return softwareDesc;
    }

    public void setSoftwareDesc(String softwareDesc) {
        this.softwareDesc = softwareDesc;
    }

    public String getDigitalSign() {
        return digitalSign;
    }

    public void setDigitalSign(String digitalSign) {
        this.digitalSign = digitalSign;
    }

    public String getDigitalSignFlag() {
        return digitalSignFlag;
    }

    public void setDigitalSignFlag(String digitalSignFlag) {
        this.digitalSignFlag = digitalSignFlag;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameFlag() {
        return productNameFlag;
    }

    public void setProductNameFlag(String productNameFlag) {
        this.productNameFlag = productNameFlag;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getProcessNameFlag() {
        return processNameFlag;
    }

    public void setProcessNameFlag(String processNameFlag) {
        this.processNameFlag = processNameFlag;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getOriginalFileNameFlag() {
        return originalFileNameFlag;
    }

    public void setOriginalFileNameFlag(String originalFileNameFlag) {
        this.originalFileNameFlag = originalFileNameFlag;
    }

    public String getFileCustomMd5() {
        return fileCustomMd5;
    }

    public void setFileCustomMd5(String fileCustomMd5) {
        this.fileCustomMd5 = fileCustomMd5;
    }

    public String getFileCustomMd5Flag() {
        return fileCustomMd5Flag;
    }

    public void setFileCustomMd5Flag(String fileCustomMd5Flag) {
        this.fileCustomMd5Flag = fileCustomMd5Flag;
    }

    public String getDirectoryFlag() {
        return directoryFlag;
    }

    public void setDirectoryFlag(String directoryFlag) {
        this.directoryFlag = directoryFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<ImportSoftwareDTO> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<ImportSoftwareDTO> childrenList) {
        this.childrenList = childrenList;
    }

    public String getDigitalSignBlackFlag() {
        return digitalSignBlackFlag;
    }

    public void setDigitalSignBlackFlag(String digitalSignBlackFlag) {
        this.digitalSignBlackFlag = digitalSignBlackFlag;
    }

    public String getProductNameBlackFlag() {
        return productNameBlackFlag;
    }

    public void setProductNameBlackFlag(String productNameBlackFlag) {
        this.productNameBlackFlag = productNameBlackFlag;
    }

    public String getProcessNameBlackFlag() {
        return processNameBlackFlag;
    }

    public void setProcessNameBlackFlag(String processNameBlackFlag) {
        this.processNameBlackFlag = processNameBlackFlag;
    }

    public String getOriginalFileNameBlackFlag() {
        return originalFileNameBlackFlag;
    }

    public void setOriginalFileNameBlackFlag(String originalFileNameBlackFlag) {
        this.originalFileNameBlackFlag = originalFileNameBlackFlag;
    }

    public String getFileCustomMd5BlackFlag() {
        return fileCustomMd5BlackFlag;
    }

    public void setFileCustomMd5BlackFlag(String fileCustomMd5BlackFlag) {
        this.fileCustomMd5BlackFlag = fileCustomMd5BlackFlag;
    }
}
