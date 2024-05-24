package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28 16:40
 *
 * @author coderLee23
 */
public class EditPushInstallPackageRequest implements WebRequest {

    /**
     * 推送安装包id
     */
    @ApiModelProperty(value = "推送安装包id", required = true)
    @NotNull
    private UUID id;

    /**
     * 软件安装包 id
     */
    @ApiModelProperty(value = "文件id", required = true)
    @NotNull
    private UUID softAppId;

    /**
     * 应用名称
     */
    @ApiModelProperty(value = "名应用名称", required = true)
    @NotBlank
    @TextMedium
    @TextName
    private String appName;

    /**
     * 静默安装参数
     */
    @ApiModelProperty(value = "静默安装参数")
    @Nullable
    private String quietInstallParam;

    /**
     * 静默安装参数【人工】
     */
    @ApiModelProperty(value = "静默安装参数【人工】")
    @Size(max = 200)
    @Nullable
    private String manualQuietInstallParam;

    /**
     * 执行压缩包中的文件
     */
    @ApiModelProperty(value = "执行压缩包中的文件")
    @Size(max = 200)
    @Nullable
    private String executeFilePath;

    /**
     * 应用描述
     */
    @ApiModelProperty(value = "应用描述")
    @Nullable
    private String appDesc;

    @ApiModelProperty(value = "操作系统类型【Windows,Linux】")
    @NotNull
    private OsPlatform osPlatform;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSoftAppId() {
        return softAppId;
    }

    public void setSoftAppId(UUID softAppId) {
        this.softAppId = softAppId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getQuietInstallParam() {
        return quietInstallParam;
    }

    public void setQuietInstallParam(String quietInstallParam) {
        this.quietInstallParam = quietInstallParam;
    }

    public String getManualQuietInstallParam() {
        return manualQuietInstallParam;
    }

    public void setManualQuietInstallParam(String manualQuietInstallParam) {
        this.manualQuietInstallParam = manualQuietInstallParam;
    }

    public String getExecuteFilePath() {
        return executeFilePath;
    }

    public void setExecuteFilePath(String executeFilePath) {
        this.executeFilePath = executeFilePath;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public OsPlatform getOsPlatform() {
        return osPlatform;
    }

    public void setOsPlatform(OsPlatform osPlatform) {
        this.osPlatform = osPlatform;
    }
}
