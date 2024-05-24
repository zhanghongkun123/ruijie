package com.ruijie.rcos.rcdc.rco.module.impl.message.computer;

/**
 * Description: 询问pc是否故障的请求回复
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/31 17:15
 *
 * @author ketb
 */
public class CheckFaultResponseContent extends BaseResponseContent {

    private String mac;

    private String faultDescription;

    public CheckFaultResponseContent(String business) {
        super(business);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }
}
