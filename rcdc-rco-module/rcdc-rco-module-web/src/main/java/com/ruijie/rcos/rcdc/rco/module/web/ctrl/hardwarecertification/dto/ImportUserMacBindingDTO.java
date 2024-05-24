package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.dto;

/**
 * Description: 导入用户-mac绑定关系dto
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/23 19:02
 *
 * @author yxq
 */
public class ImportUserMacBindingDTO {

    private String userName;

    private String terminalMac;

    private Integer rowNum;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }
}
