package com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.request;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDriverInstallMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageEditType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 远程编辑镜像请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年03月24日
 *
 * @author xgx
 */
public class RemoteTerminalEditImageTemplateRequest implements WebRequest, Serializable {
    @NotNull
    private UUID imageTemplateId;

    @Nullable
    private String terminalId;

    @Nullable
    private ImageEditType imageEditType ;

    @Nullable
    private String cpuType;

    @Nullable
    private CbbDriverInstallMode mode = CbbDriverInstallMode.NO_INSTALL;

    @Nullable
    private CbbImageType cbbImageType;

    @Nullable
    private String imageTemplateName;

    @Nullable
    private String terminalMac;

    @Nullable
    private String note;

    @Nullable
    private CbbOsType imageSystemType;

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

    @Nullable
    public ImageEditType getImageEditType() {
        return imageEditType;
    }

    public void setImageEditType(@Nullable ImageEditType imageEditType) {
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

    @Nullable
    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(@Nullable String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    @Nullable
    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(@Nullable String terminalMac) {
        this.terminalMac = terminalMac;
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

}
