package com.ruijie.rcos.rcdc.rco.module.impl.printer.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 打印机配置实体类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/15
 *
 * @author chenjiehui
 */
@Entity
@Table(name = "t_rco_printer_manage")
public class PrinterConfigEntity {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 流水号
     */
    private Integer configSerial;

    /**
     * 配置详情
     */
    private String configDetail;

    /**
     * 配置文件md5
     */
    private String configMd5;

    /**
     * 配置文件是否已被覆盖
     */
    private Boolean configEnableCovered;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 配置支持的操作系统
     */
    private String configSupportOs;

    /**
     * 配置描述
     */
    private String configDescription;

    /**
     * 打印机连接模式
     */
    private String printerConnectType;

    /**
     * 打印机型号
     */
    private String printerModel;

    /**
     * 打印机名称
     */
    private String printerName;

    /**
     * 打印机端口
     */
    private String printerPort;

    /**
     * ip
     */
    private String printerIp;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 版本号
     */
    @Version
    private Integer version;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getConfigSerial() {
        return configSerial;
    }

    public void setConfigSerial(Integer configSerial) {
        this.configSerial = configSerial;
    }

    public String getConfigDetail() {
        return configDetail;
    }

    public void setConfigDetail(String configDetail) {
        this.configDetail = configDetail;
    }

    public String getConfigMd5() {
        return configMd5;
    }

    public void setConfigMd5(String configMd5) {
        this.configMd5 = configMd5;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigSupportOs() {
        return configSupportOs;
    }

    public void setConfigSupportOs(String configSupportOs) {
        this.configSupportOs = configSupportOs;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configComment) {
        this.configDescription = configComment;
    }

    public String getPrinterConnectType() {
        return printerConnectType;
    }

    public void setPrinterConnectType(String printerConnectType) {
        this.printerConnectType = printerConnectType;
    }

    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String printerModel) {
        this.printerModel = printerModel;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getPrinterPort() {
        return printerPort;
    }

    public void setPrinterPort(String printerPort) {
        this.printerPort = printerPort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
    }

    public Boolean getConfigEnableCovered() {
        return configEnableCovered;
    }

    public void setConfigEnableCovered(Boolean configEnableCovered) {
        this.configEnableCovered = configEnableCovered;
    }
}
