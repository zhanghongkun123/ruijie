package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbdevice;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月28日
 *
 * @author Ghang
 */
public class DeleteUSBDeviceBatchWebRequest implements WebRequest {

    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
    
}
