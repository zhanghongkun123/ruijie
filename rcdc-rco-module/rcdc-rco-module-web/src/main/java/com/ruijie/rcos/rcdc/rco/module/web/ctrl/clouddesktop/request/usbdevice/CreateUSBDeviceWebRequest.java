package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月26日
 * 
 * @author Ghang
 */
public class CreateUSBDeviceWebRequest implements WebRequest {

    @NotNull
    private UUID usbTypeId;
    
    @NotNull
    @Size( max = 4)
    private String firmFlag;
    
    @NotNull
    @Size( max = 4)
    private String productFlag;
    
    @Nullable
    private String firm;
    
    @Nullable
    private String product;

    @Nullable
    private String note;

    public UUID getUsbTypeId() {
        return usbTypeId;
    }

    public void setUsbTypeId(UUID usbTypeId) {
        this.usbTypeId = usbTypeId;
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

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
