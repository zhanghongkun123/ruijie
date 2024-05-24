package com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * <br>
 * Description: 客户信息 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
public class CustomerInfoDTO {

    /**
     * 客户名
     */
    @NotNull
    private String customerName;

    /**
     * 客户类型
     */
    @NotNull
    private String customerType;

    /**
     * 省份
     */
    @NotNull
    private String province;

    /**
     * 城市
     */
    @NotNull
    private String city;

    /**
     * 区
     */
    @NotNull
    private String area;

    /**
     * 所属单位
     */
    @NotNull
    private String edb;

    /**
     * 渠道公司
     */
    @Nullable
    private String channelCompany;

    /**
     * 渠道联系方式
     */
    @Nullable
    private String channelContactDetails;

    /**
     * 维护人员
     */
    @Nullable
    private String maintenancePersonnel;

    /**
     * 联系方式
     */
    @Nullable
    private String contactDetails;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Nullable
    public String getChannelCompany() {
        return channelCompany;
    }

    public void setChannelCompany(@Nullable String channelCompany) {
        this.channelCompany = channelCompany;
    }

    @Nullable
    public String getChannelContactDetails() {
        return channelContactDetails;
    }

    public void setChannelContactDetails(@Nullable String channelContactDetails) {
        this.channelContactDetails = channelContactDetails;
    }

    @Nullable
    public String getMaintenancePersonnel() {
        return maintenancePersonnel;
    }

    public void setMaintenancePersonnel(@Nullable String maintenancePersonnel) {
        this.maintenancePersonnel = maintenancePersonnel;
    }

    @Nullable
    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(@Nullable String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEdb() {
        return edb;
    }

    public void setEdb(String edb) {
        this.edb = edb;
    }
}
