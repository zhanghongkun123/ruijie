package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbtype;

import java.util.UUID;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: USB类型命名唯一性校验
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月12日
 * 
 * @author Ghang
 */
public class USBTypeCheckDuplicationWebRequest implements WebRequest {

    @Nullable
    private UUID id;
    
    @TextName
    @TextShort
    @NotBlank
    private String usbTypeName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsbTypeName() {
        return usbTypeName;
    }

    public void setUsbTypeName(String usbTypeName) {
        this.usbTypeName = usbTypeName;
    }
    
}
