package com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;

/**
 * Description: 镜像文件
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @author chenjuan
 */
public class ImageFileDTO {

    private String baseFileName;

    private String baseFilePath;

    private String baseFileMd5;

    private Long baseFileSize = 0L;

    private BtInfoDTO baseBtInfo;

    private String diffFilePath;

    private String diffFileMd5;

    private String diffFileName;

    private Long diffFileSize = 0L;

    private BtInfoDTO diffBtInfo;

    /**
     * 磁盘类型 数据盘:DATA, 系统盘：SYSTEM
     */
    private CbbImageDiskType diskType;

    /**
     * 磁盘空间
     */
    private Integer diskSize;

    /**
     * 盘符
     */
    private String diskSymbol;

    public String getBaseFileName() {
        return baseFileName;
    }

    public void setBaseFileName(String baseFileName) {
        this.baseFileName = baseFileName;
    }

    public String getBaseFilePath() {
        return baseFilePath;
    }

    public void setBaseFilePath(String baseFilePath) {
        this.baseFilePath = baseFilePath;
    }

    public String getBaseFileMd5() {
        return baseFileMd5;
    }

    public void setBaseFileMd5(String baseFileMd5) {
        this.baseFileMd5 = baseFileMd5;
    }

    public BtInfoDTO getBaseBtInfo() {
        return baseBtInfo;
    }

    public void setBaseBtInfo(BtInfoDTO baseBtInfo) {
        this.baseBtInfo = baseBtInfo;
    }

    public String getDiffFilePath() {
        return diffFilePath;
    }

    public void setDiffFilePath(String diffFilePath) {
        this.diffFilePath = diffFilePath;
    }

    public String getDiffFileMd5() {
        return diffFileMd5;
    }

    public void setDiffFileMd5(String diffFileMd5) {
        this.diffFileMd5 = diffFileMd5;
    }

    public String getDiffFileName() {
        return diffFileName;
    }

    public void setDiffFileName(String diffFileName) {
        this.diffFileName = diffFileName;
    }

    public BtInfoDTO getDiffBtInfo() {
        return diffBtInfo;
    }

    public void setDiffBtInfo(BtInfoDTO diffBtInfo) {
        this.diffBtInfo = diffBtInfo;
    }

    public Long getBaseFileSize() {
        return baseFileSize;
    }

    public void setBaseFileSize(Long baseFileSize) {
        this.baseFileSize = baseFileSize;
    }

    public Long getDiffFileSize() {
        return diffFileSize;
    }

    public void setDiffFileSize(Long diffFileSize) {
        this.diffFileSize = diffFileSize;
    }

    public CbbImageDiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(CbbImageDiskType diskType) {
        this.diskType = diskType;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public String getDiskSymbol() {
        return diskSymbol;
    }

    public void setDiskSymbol(String diskSymbol) {
        this.diskSymbol = diskSymbol;
    }
}
