package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbSyncLoginAccountPermissionEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy.EditWatermarkWebRequest;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.22
 *
 * @author linhj
 */
public class UpdateVOIDeskStrategyWebRequest extends AbstractDeskStrategyWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面策略ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "云桌面策略名称", required = true)
    @NotBlank
    @Size(max = 64)
    private String strategyName;

    @ApiModelProperty(value = "计算机名前缀", required = false)
    @NotBlank
    private String computerName;

    /**
     * VOI 只允许个性 还原 没有应用分发 后续版本如果有支持 可扩展
     */
    @ApiModelProperty(value = "云桌面类型", required = true, allowableValues = "PERSONAL, RECOVERABLE")
    @NotNull
    private CbbCloudDeskPattern desktopType;

    @ApiModelProperty(value = "启用本地盘", required = true)
    @NotNull
    private Boolean enableAllowLocalDisk;

    @ApiModelProperty(value = "启用云桌面重定向", required = true)
    @NotNull
    private Boolean enableOpenDesktopRedirect;

    @ApiModelProperty(value = "外设策略", required = true)
    @NotNull
    private UUID[] usbTypeIdArr;

    @ApiModelProperty(value = "AD域加入OU路径")
    @Nullable
    private String adOu;

    /**
     * 水印配置内容
     */
    @ApiModelProperty("水印配置内容")
    @Nullable
    private EditWatermarkWebRequest watermarkInfo;

    /**
     * 桌面登录账号同步
     */
    @ApiModelProperty("桌面登录账号同步")
    @Nullable
    private Boolean desktopSyncLoginAccount;

    /**
     * 桌面登录密码同步
     */
    @ApiModelProperty("桌面登录密码同步")
    @Nullable
    private Boolean desktopSyncLoginPassword;

    /**
     * 桌面登录账号权限
     */
    @ApiModelProperty("桌面登录账号权限")
    @Nullable
    private CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission;

    public String getAdOu() {
        return adOu;
    }

    public void setAdOu(String adOu) {
        this.adOu = adOu;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public Boolean getEnableAllowLocalDisk() {
        return enableAllowLocalDisk;
    }

    public void setEnableAllowLocalDisk(Boolean enableAllowLocalDisk) {
        this.enableAllowLocalDisk = enableAllowLocalDisk;
    }

    public Boolean getEnableOpenDesktopRedirect() {
        return enableOpenDesktopRedirect;
    }

    public void setEnableOpenDesktopRedirect(Boolean enableOpenDesktopRedirect) {
        this.enableOpenDesktopRedirect = enableOpenDesktopRedirect;
    }

    public UUID[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    @Nullable
    public EditWatermarkWebRequest getWatermarkInfo() {
        return watermarkInfo;
    }

    public void setWatermarkInfo(@Nullable EditWatermarkWebRequest watermarkInfo) {
        this.watermarkInfo = watermarkInfo;
    }

    @Nullable
    public Boolean getDesktopSyncLoginAccount() {
        return desktopSyncLoginAccount;
    }

    public void setDesktopSyncLoginAccount(@Nullable Boolean desktopSyncLoginAccount) {
        this.desktopSyncLoginAccount = desktopSyncLoginAccount;
    }

    @Nullable
    public Boolean getDesktopSyncLoginPassword() {
        return desktopSyncLoginPassword;
    }

    public void setDesktopSyncLoginPassword(@Nullable Boolean desktopSyncLoginPassword) {
        this.desktopSyncLoginPassword = desktopSyncLoginPassword;
    }

    @Nullable
    public CbbSyncLoginAccountPermissionEnums getDesktopSyncLoginAccountPermission() {
        return desktopSyncLoginAccountPermission;
    }

    public void setDesktopSyncLoginAccountPermission(@Nullable CbbSyncLoginAccountPermissionEnums desktopSyncLoginAccountPermission) {
        this.desktopSyncLoginAccountPermission = desktopSyncLoginAccountPermission;
    }
}
