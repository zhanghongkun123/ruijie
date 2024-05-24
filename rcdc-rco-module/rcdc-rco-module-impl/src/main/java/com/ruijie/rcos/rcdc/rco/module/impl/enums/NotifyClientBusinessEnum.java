package com.ruijie.rcos.rcdc.rco.module.impl.enums;

/**
 * Description: 通知客户端的操作业务类型
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/28 17:28
 *
 * @author ketb
 */
public enum NotifyClientBusinessEnum {

    REMOTE_ASSIST(107, "remote_assist"),

    CREATE_VNC(111, "create_vnc"),

    CANCEL_REMOTE_ASSIST(110, "cancel_remote_assist");

    private int businessCode;

    private String businessName;

    NotifyClientBusinessEnum(int businessCode, String businessName) {
        this.businessCode = businessCode;
        this.businessName = businessName;
    }

    public int getBusinessCode() {
        return businessCode;
    }


    public String getBusinessName() {
        return businessName;
    }

}
