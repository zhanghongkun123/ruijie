package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUsbStorageDeviceMappingMode;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/4/11 <br>
 *
 * @author yyz
 */
public class DeskStrategyRecommendDetailResponse extends DefaultResponse {

    private UUID id;

    private String strategyName;

    private String desktopType;

    private Integer systemDisk;

    private boolean enableInternet;

    private boolean enableUsbReadOnly;

    private boolean isShow;

    private UUID[] usbTypeIdArr;

    private CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode;

    /**
     * 连接协议
     */
    private CbbEstProtocolType estProtocolType;

    /**
     * 协议配置
     */
    private AgreementDTO agreementInfo;

    /**
     * 透明加解密总开关
     */
    private Boolean enableTransparentEncrypt;

    public UUID[] getUsbTypeIdArr() {
        return usbTypeIdArr;
    }

    public void setUsbTypeIdArr(UUID[] usbTypeIdArr) {
        this.usbTypeIdArr = usbTypeIdArr;
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

    public String getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public boolean isEnableInternet() {
        return enableInternet;
    }

    public void setEnableInternet(boolean enableInternet) {
        this.enableInternet = enableInternet;
    }

    public boolean isEnableUsbReadOnly() {
        return enableUsbReadOnly;
    }

    public void setEnableUsbReadOnly(boolean enableUsbReadOnly) {
        this.enableUsbReadOnly = enableUsbReadOnly;
    }
    
    public boolean getIsShow() {
        return isShow;
    }

    public void setIsShow(boolean show) {
        isShow = show;
    }

    public CbbUsbStorageDeviceMappingMode getUsbStorageDeviceMappingMode() {
        return usbStorageDeviceMappingMode;
    }

    public void setUsbStorageDeviceMappingMode(CbbUsbStorageDeviceMappingMode usbStorageDeviceMappingMode) {
        this.usbStorageDeviceMappingMode = usbStorageDeviceMappingMode;
    }

    public CbbEstProtocolType getEstProtocolType() {
        return estProtocolType;
    }

    public void setEstProtocolType(CbbEstProtocolType estProtocolType) {
        this.estProtocolType = estProtocolType;
    }

    public AgreementDTO getAgreementInfo() {
        return agreementInfo;
    }

    public void setAgreementInfo(AgreementDTO agreementInfo) {
        this.agreementInfo = agreementInfo;
    }

    public Boolean getEnableTransparentEncrypt() {
        return enableTransparentEncrypt;
    }

    public void setEnableTransparentEncrypt(Boolean enableTransparentEncrypt) {
        this.enableTransparentEncrypt = enableTransparentEncrypt;
    }
}
