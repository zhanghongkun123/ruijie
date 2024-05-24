package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/19
 *
 * @author zqj
 */
public class ImportComputerDTO {

    private Integer rowNum;

    private String name;

    private String groupNames;

    private String userName;

    private String ip;

    private String remark;

    private String deskStrategy;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeskStrategy() {
        return deskStrategy;
    }

    public void setDeskStrategy(String deskStrategy) {
        this.deskStrategy = deskStrategy;
    }
}
