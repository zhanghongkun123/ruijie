package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 创建USB类型
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月20日
 * 
 * @author Ghang
 */
public class CreateUSBTypeWebRequest implements WebRequest {

    @NotBlank
    @TextShort
    @TextName
    private String usbTypeName;

    @TextMedium
    private String note;

    public String getUsbTypeName() {
        return usbTypeName;
    }

    public void setUsbTypeName(String usbTypeName) {
        this.usbTypeName = usbTypeName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
