package com.ruijie.rcos.rcdc.rco.module.impl.printer.dto;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: 打印机配置基本信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/21
 *
 * @author chenjiehui
 */
public class PrinterConfigBaseInfoDTO {

    /**
     * 配置名称
     */
    @JSONField(name = "name")
    private String configName;

    /**
     * ip 地址
     */
    @JSONField(name = "ip")
    private String printerIp;

    /**
     * 打印机连接模式
     */
    @JSONField(name = "type")
    private String printerConnectType;

    /**
     * 打印机名称
     */
    @JSONField(name = "printer_name")
    private String printerName;

    /**
     * 打印机端口
     */
    @JSONField(name = "port")
    private String printerPort;

    /**
     * 打印机端口
     */
    @JSONField(name = "model")
    private String printerModel;

    /**
     * 配置支持的操作系统
     */
    @JSONField(name = "oses")
    private String configSupportOs;


    /**
     * 配置描述
     */
    @JSONField(name = "comment")
    private String configDescription;

    @JSONField(name = "update_time")
    private Date updateTime;


    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPrinterConnectType() {
        return printerConnectType;
    }

    public void setPrinterConnectType(String printerConnectType) {
        this.printerConnectType = printerConnectType;
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

    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String printerModel) {
        this.printerModel = printerModel;
    }

    public String getConfigSupportOs() {
        return configSupportOs;
    }

    public void setConfigSupportOs(String configSupportOs) {
        this.configSupportOs = configSupportOs;
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
    }
}
