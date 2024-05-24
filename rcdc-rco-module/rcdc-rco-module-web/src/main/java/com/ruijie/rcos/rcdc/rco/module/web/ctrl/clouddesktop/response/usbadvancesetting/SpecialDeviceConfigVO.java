package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月23日
 * 
 * @author zhuangchenwu
 */
public class SpecialDeviceConfigVO {

    private UUID id;
    
    private String firmFlag;
    
    private String productFlag;

    private String specialDeviceFlag;
    
    private String requestType;
    
    private String request;
    
    private String value;
    
    private String index;
    
    private String status;
    
    private String confStr;
    
    private String custom;
    
    private Boolean enableRestore;
    
    private Boolean enableReuse;
    
    private Boolean enablePcRedirect;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
