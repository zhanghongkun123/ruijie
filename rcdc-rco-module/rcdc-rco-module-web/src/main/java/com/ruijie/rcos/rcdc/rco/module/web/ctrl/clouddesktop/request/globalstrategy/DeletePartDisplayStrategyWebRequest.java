package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月1日
 * 
 * @author Ghang
 */
public class DeletePartDisplayStrategyWebRequest implements WebRequest {

    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
