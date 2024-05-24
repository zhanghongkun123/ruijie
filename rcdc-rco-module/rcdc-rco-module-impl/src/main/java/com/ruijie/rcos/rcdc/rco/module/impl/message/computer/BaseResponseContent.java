package com.ruijie.rcos.rcdc.rco.module.impl.message.computer;

/**
 * Description: 消息回复基本信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/31 17:08
 *
 * @author ketb
 */
public class BaseResponseContent {

    private String business;

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public BaseResponseContent(String business) {
        this.business = business;
    }
}
