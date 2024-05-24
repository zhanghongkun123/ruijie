package com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDriverInstallMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageEditType;

/**
 * Description: 终端上编辑镜像DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月29日
 *
 * @author ypp
 */
public class RemoteTerminalEditImageTemplateDTO {

    private UUID imageTemplateId;

    private String terminalId;

    private ImageEditType imageEditType;

    private String cpuType;

    private CbbDriverInstallMode mode = CbbDriverInstallMode.NO_INSTALL;

    private CbbImageType cbbImageType;

    private String imageTemplateName;

    private String terminalMac;

    private Boolean enableNested;

    private String adminName;

    private UUID adminSessionId;

    @Nullable
    private String note;

    @Nullable
    private CbbOsType imageSystemType;

    @Nullable
    private Integer systemDisk;

    @Nullable
    private String computerName;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    public ImageEditType getImageEditType() {
        return imageEditType;
    }

    public void setImageEditType(ImageEditType imageEditType) {
        this.imageEditType = imageEditType;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public CbbDriverInstallMode getMode() {
        return mode;
    }

    public void setMode(CbbDriverInstallMode mode) {
        this.mode = mode;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }


    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(Boolean enableNested) {
        this.enableNested = enableNested;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public UUID getAdminSessionId() {
        return adminSessionId;
    }

    public void setAdminSessionId(UUID adminSessionId) {
        this.adminSessionId = adminSessionId;
    }

    @Nullable
    public String getNote() {
        return note;
    }

    public void setNote(@Nullable String note) {
        this.note = note;
    }

    @Nullable
    public CbbOsType getImageSystemType() {
        return imageSystemType;
    }

    public void setImageSystemType(@Nullable CbbOsType imageSystemType) {
        this.imageSystemType = imageSystemType;
    }

    @Nullable
    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(@Nullable Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    @Nullable
    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(@Nullable String computerName) {
        this.computerName = computerName;
    }
}
