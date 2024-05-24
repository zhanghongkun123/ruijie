package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.util.Assert;

/**
 * Description: 水印显示的自定义字段映射的值对象,根据与前端定义的自定义字段，需要从后端查询到对应的值
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */
public class WatermarkFieldMappingValueDTO {

    private String userName;

    private String deskName;

    private String deskIp;

    private String deskMac;

    private String customContent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public String getDeskIp() {
        return deskIp;
    }

    public void setDeskIp(String deskIp) {
        this.deskIp = deskIp;
    }

    public String getDeskMac() {
        return deskMac;
    }

    public void setDeskMac(String deskMac) {
        this.deskMac = deskMac;
    }

    public String getCustomContent() {
        return customContent;
    }

    public void setCustomContent(String customContent) {
        this.customContent = customContent;
    }

    @Override
    public boolean equals(Object obj) {
        Assert.notNull(obj, "obj is not null");
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
