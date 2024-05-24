package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.osfile;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年3月28日 <br>
 * 
 * @author Lbd
 */
public class DeleteOsFileWebRequest implements WebRequest {

    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
