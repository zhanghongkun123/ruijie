package com.ruijie.rcos.rcdc.rco.module.web.ctrl.computername.response;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/10
 *
 * @author wjp
 */
public class GetComputerNameWebResponse {

    private String computerName;

    public GetComputerNameWebResponse(String computerName) {
        this.setComputerName(computerName);
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }
}
