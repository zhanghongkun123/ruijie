package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 13:55
 *
 * @author ketb
 */
public class SoftwareStrategyRelatedSoftwareIdArrWebRequest implements WebRequest {

    /**
     * 这个id真实内容 策略Id+软件ID
     * {"idArr":["af8ee0c4-7331-4e25-ad85-4820d51dbcb0|5dec11ef-d959-4904-ae65-a28df44490ef"]}
     */
    @NotNull
    private String[] idArr;

    public SoftwareStrategyRelatedSoftwareIdArrWebRequest() {
    }

    public SoftwareStrategyRelatedSoftwareIdArrWebRequest(String[] idArr) {
        this.idArr = idArr;
    }

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }
}
