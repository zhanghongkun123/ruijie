package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting;

import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月17日
 * 
 * @author zhuangchenwu
 */
public class CreateSpecialDeviceConfigWebRequest implements WebRequest {

    @NotBlank
    @Size(max = 4)
    private String firmFlag;
    
    @NotBlank
    @Size(max = 4)
    private String productFlag;
    
    @Nullable
    @Size(max = 126)
    private String specialDeviceFlag;
    
    @Nullable
    @Size(max = 2)
    private String requestType;
    
    @Nullable
    @Size(max = 2)
    private String request;
    
    @Nullable
    @Size(max = 4)
    private String value;
    
    @Nullable
    @Size(max = 4)
    private String index;
    
    @Nullable
    @Size(max = 2)
    private String status;
    
    @Nullable
    @Size(max = 767)
    private String confStr;
    
    @Nullable
    private String custom;
    
    @Nullable
    private Boolean enableRestore;
    
    @Nullable
    private Boolean enableReuse;
    
    @Nullable
    private Boolean enablePcRedirect;

    public String getFirmFlag() {
        return firmFlag;
    }

    public void setFirmFlag(String firmFlag) {
        this.firmFlag = firmFlag;
    }

    public String getProductFlag() {
        return productFlag;
    }

    public void setProductFlag(String productFlag) {
        this.productFlag = productFlag;
    }

    public String getSpecialDeviceFlag() {
        return specialDeviceFlag;
    }

    public void setSpecialDeviceFlag(String specialDeviceFlag) {
        this.specialDeviceFlag = specialDeviceFlag;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConfStr() {
        return confStr;
    }

    public void setConfStr(String confStr) {
        this.confStr = confStr;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public Boolean getEnableRestore() {
        return enableRestore;
    }

    public void setEnableRestore(Boolean enableRestore) {
        this.enableRestore = enableRestore;
    }

    public Boolean getEnableReuse() {
        return enableReuse;
    }

    public void setEnableReuse(Boolean enableReuse) {
        this.enableReuse = enableReuse;
    }

    public Boolean getEnablePcRedirect() {
        return enablePcRedirect;
    }

    public void setEnablePcRedirect(Boolean enablePcRedirect) {
        this.enablePcRedirect = enablePcRedirect;
    }
    
}
