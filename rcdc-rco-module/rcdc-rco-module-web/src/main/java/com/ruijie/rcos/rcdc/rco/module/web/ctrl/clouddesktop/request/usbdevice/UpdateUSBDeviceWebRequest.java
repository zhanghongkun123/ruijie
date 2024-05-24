package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
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
public class UpdateUSBDeviceWebRequest implements WebRequest {

    @NotNull
    private UUID id;

    @NotNull
    private UUID usbTypeId;

    @Nullable
    private String note;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUsbTypeId() {
        return usbTypeId;
    }

    public void setUsbTypeId(UUID usbTypeId) {
        this.usbTypeId = usbTypeId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
