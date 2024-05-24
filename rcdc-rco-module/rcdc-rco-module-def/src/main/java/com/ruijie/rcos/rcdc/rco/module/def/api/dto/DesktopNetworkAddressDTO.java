package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 远程协助web控制器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月27日
 *
 * @author nt
 */
public class DesktopNetworkAddressDTO extends IdLabelEntry {

    private String ip;

    private Object row;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Object getRow() {
        return row;
    }

    public void setRow(Object row) {
        this.row = row;
    }

}
