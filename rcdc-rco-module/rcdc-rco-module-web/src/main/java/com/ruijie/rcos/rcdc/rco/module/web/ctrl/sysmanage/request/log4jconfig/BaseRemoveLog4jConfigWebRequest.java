package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.log4jconfig;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年02月19日
 *
 * @author GuoZhouYue
 */
public class BaseRemoveLog4jConfigWebRequest implements WebRequest {

    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
