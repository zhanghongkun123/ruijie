package com.ruijie.rcos.rcdc.rco.module.impl.enums;


/**
 *
 * Description: 网络桥接模式
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public enum NetworkModelEnums {
    BRIDGE("bridge"),
    NAT("nat");

    private String name;

    NetworkModelEnums(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
