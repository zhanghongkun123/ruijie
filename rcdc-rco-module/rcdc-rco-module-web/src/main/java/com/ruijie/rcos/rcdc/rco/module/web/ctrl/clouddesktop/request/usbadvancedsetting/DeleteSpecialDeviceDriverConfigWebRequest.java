package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月17日
 * 
 * @author zhuangchenwu
 */
public class DeleteSpecialDeviceDriverConfigWebRequest implements WebRequest {

    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
