package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.UUID;

import javax.persistence.*;

/**
 * <br>
 * Description:客户信息 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/7 <br>
 *
 * @author xwx
 */
@Entity
@Table(name = "t_rcc_customer_info")
public class CustomerInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String customerName;

    private String customerType;

    private String province;

    private String city;

    private String area;

    private String edb;

    private String channelCompany;

    private String channelContactDetails;

    private String maintenancePersonnel;

    private String contactDetails;

    @Version
    private Integer version;

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public String getEdb() {
        return edb;
    }

    public void setEdb(String edb) {
        this.edb = edb;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getChannelCompany() {
        return channelCompany;
    }

    public void setChannelCompany(String channelCompany) {
        this.channelCompany = channelCompany;
    }

    public String getChannelContactDetails() {
        return channelContactDetails;
    }

    public void setChannelContactDetails(String channelContactDetails) {
        this.channelContactDetails = channelContactDetails;
    }

    public String getMaintenancePersonnel() {
        return maintenancePersonnel;
    }

    public void setMaintenancePersonnel(String maintenancePersonnel) {
        this.maintenancePersonnel = maintenancePersonnel;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }
}
