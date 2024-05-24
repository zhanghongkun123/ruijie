package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.rco.module.web.service.SoftwareValidateRules;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.SoftwareControlBusinessKey.*;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public class CreateSoftwareRequest implements WebRequest {

    /**
     * 软件ID
     */
    @ApiModelProperty(value = "软件ID")
    @Nullable
    private UUID id;

    /**
     * 软件名称
     */
    @ApiModelProperty(value = "软件名称，通用名称格式", required = true)
    @NotBlank
    @Size(min = 1, max = 64)
    @TextName
    private String name;

    /**
     * 软件分组ID
     */
    @ApiModelProperty(value = "软件分组ID", required = true)
    @NotNull
    private UUID groupId;

    /**
     * 软件分组描述
     */
    @ApiModelProperty(value = "软件分组描述")
    @Nullable
    private String description;

    /**
     * 厂商数字签名
     */
    @ApiModelProperty(value = "厂商数字签名")
    @Size(message = RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_DIGITAL_SIGN_TOO_LENGTH_MESSAGE, max = SoftwareValidateRules.PRODUCT_NAME_SIZE)
    @Nullable
    private String digitalSign;

    /**
     * 厂商数字签名 GT传送标志
     */
    @ApiModelProperty(value = "厂商数字签名 GT传送标志")
    @Nullable
    private Boolean digitalSignFlag;

    /**
     * 安装路径
     */
    @ApiModelProperty(value = "安装路径")
    @Size(message = RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_INSTALL_PATH_TOO_LENGTH_MESSAGE, max = SoftwareValidateRules.INSTALL_PATH_SIZE)
    @Nullable
    private String installPath;

    /**
     * 安装路径 GT传送标志
     */
    @ApiModelProperty(value = "安装路径 GT传送标志")
    @Nullable
    private Boolean installPathFlag;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    @Size(message = RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PRODUCT_NAME_TOO_LENGTH_MESSAGE, max = SoftwareValidateRules.PRODUCT_NAME_SIZE)
    @Nullable
    private String productName;

    /**
     * 产品名称 GT传送标志
     */
    @ApiModelProperty(value = "产品名称 GT传送标志")
    @Nullable
    private Boolean productNameFlag;

    /**
     * 进程名
     */
    @ApiModelProperty(value = "进程名")
    @Size(message = RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_PROCESS_NAME_TOO_LENGTH_MESSAGE, max = SoftwareValidateRules.PROCESS_NAME_SIZE)
    @Nullable
    private String processName;

    /**
     * 进程名 GT传送标志
     */
    @ApiModelProperty(value = "进程名 GT传送标志")
    @Nullable
    private Boolean processNameFlag;

    /**
     * 原始文件名
     */
    @ApiModelProperty(value = "原始文件名")
    @Size(message = RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_ORIGINAL_FILE_NAME_TOO_LENGTH_MESSAGE, max = SoftwareValidateRules.ORIGINAL_FILE_NAME_SIZE)
    @Nullable
    private String originalFileName;

    /**
     * 原始文件名 GT传送标志
     */
    @ApiModelProperty(value = "原始文件名 GT传送标志")
    @Nullable
    private Boolean originalFileNameFlag;

    /**
     * 自定义md5值
     */
    @ApiModelProperty(value = "文件特征码")
    @Size(message = RCDC_RCO_SOFTWARE_IMPORT_SOFTWARE_FILE_CUSTOM_MD5_TOO_LENGTH_MESSAGE, max = SoftwareValidateRules.FILE_CUSTOM_MD5_SIZE)
    @Nullable
    private String fileCustomMd5;

    /**
     * 自定义md5值 GT传送标志
     */
    @ApiModelProperty(value = "文件特征码 GT传送标志")
    @Nullable
    private Boolean fileCustomMd5Flag;

    /**
     * 厂商数字签名(黑名单) 
     */
    @Nullable
    private Boolean digitalSignBlackFlag;

    /**
     * 产品名称(黑名单) 
     */
    @Nullable
    private Boolean productNameBlackFlag;

    /**
     * 进程名(黑名单) 
     */
    @Nullable
    private Boolean processNameBlackFlag;

    /**
     * 原始文件名(黑名单) 
     */
    @Nullable
    private Boolean originalFileNameBlackFlag;

    /**
     * 自定义md5值(黑名单) 
     */
    @Nullable
    private Boolean fileCustomMd5BlackFlag;


    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getDigitalSign() {
        return digitalSign;
    }

    public void setDigitalSign(@Nullable String digitalSign) {
        this.digitalSign = digitalSign;
    }

    @Nullable
    public Boolean getDigitalSignFlag() {
        return digitalSignFlag;
    }

    public void setDigitalSignFlag(@Nullable Boolean digitalSignFlag) {
        this.digitalSignFlag = digitalSignFlag;
    }

    @Nullable
    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(@Nullable String installPath) {
        this.installPath = installPath;
    }

    @Nullable
    public Boolean getInstallPathFlag() {
        return installPathFlag;
    }

    public void setInstallPathFlag(@Nullable Boolean installPathFlag) {
        this.installPathFlag = installPathFlag;
    }

    @Nullable
    public String getProductName() {
        return productName;
    }

    public void setProductName(@Nullable String productName) {
        this.productName = productName;
    }

    @Nullable
    public Boolean getProductNameFlag() {
        return productNameFlag;
    }

    public void setProductNameFlag(@Nullable Boolean productNameFlag) {
        this.productNameFlag = productNameFlag;
    }

    @Nullable
    public String getProcessName() {
        return processName;
    }

    public void setProcessName(@Nullable String processName) {
        this.processName = processName;
    }

    @Nullable
    public Boolean getProcessNameFlag() {
        return processNameFlag;
    }

    public void setProcessNameFlag(@Nullable Boolean processNameFlag) {
        this.processNameFlag = processNameFlag;
    }

    @Nullable
    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(@Nullable String originalFileName) {
        this.originalFileName = originalFileName;
    }

    @Nullable
    public Boolean getOriginalFileNameFlag() {
        return originalFileNameFlag;
    }

    public void setOriginalFileNameFlag(@Nullable Boolean originalFileNameFlag) {
        this.originalFileNameFlag = originalFileNameFlag;
    }

    @Nullable
    public String getFileCustomMd5() {
        return fileCustomMd5;
    }

    public void setFileCustomMd5(@Nullable String fileCustomMd5) {
        this.fileCustomMd5 = fileCustomMd5;
    }

    @Nullable
    public Boolean getFileCustomMd5Flag() {
        return fileCustomMd5Flag;
    }

    public void setFileCustomMd5Flag(@Nullable Boolean fileCustomMd5Flag) {
        this.fileCustomMd5Flag = fileCustomMd5Flag;
    }

    @Nullable
    public Boolean getDigitalSignBlackFlag() {
        return digitalSignBlackFlag;
    }

    public void setDigitalSignBlackFlag(@Nullable Boolean digitalSignBlackFlag) {
        this.digitalSignBlackFlag = digitalSignBlackFlag;
    }

    @Nullable
    public Boolean getProductNameBlackFlag() {
        return productNameBlackFlag;
    }

    public void setProductNameBlackFlag(@Nullable Boolean productNameBlackFlag) {
        this.productNameBlackFlag = productNameBlackFlag;
    }

    @Nullable
    public Boolean getProcessNameBlackFlag() {
        return processNameBlackFlag;
    }

    public void setProcessNameBlackFlag(@Nullable Boolean processNameBlackFlag) {
        this.processNameBlackFlag = processNameBlackFlag;
    }

    @Nullable
    public Boolean getOriginalFileNameBlackFlag() {
        return originalFileNameBlackFlag;
    }

    public void setOriginalFileNameBlackFlag(@Nullable Boolean originalFileNameBlackFlag) {
        this.originalFileNameBlackFlag = originalFileNameBlackFlag;
    }

    @Nullable
    public Boolean getFileCustomMd5BlackFlag() {
        return fileCustomMd5BlackFlag;
    }

    public void setFileCustomMd5BlackFlag(@Nullable Boolean fileCustomMd5BlackFlag) {
        this.fileCustomMd5BlackFlag = fileCustomMd5BlackFlag;
    }
}
