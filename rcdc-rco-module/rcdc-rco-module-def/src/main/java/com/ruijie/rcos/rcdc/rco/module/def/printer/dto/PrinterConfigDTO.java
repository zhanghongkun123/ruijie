package com.ruijie.rcos.rcdc.rco.module.def.printer.dto;


import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/17
 *
 * @author chenjiehui
 */
public class PrinterConfigDTO {

    /**
     * 配置 id
     */
    private UUID id;

    /**
     * 配置名称
     */
    private String configName;

    /**
     * 打印机连接模式
     */
    private String printerConnectType;

    /**
     * 打印机型号
     */
    private String printerModel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * ip
     */
    private String printerIp;

    /**
     * 配置描述
     */
    private String configDescription;

    /**
     * 支持的系统
     */
    private String configSupportOs;


    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }


    public String getPrinterModel() {
        return printerModel;
    }

    public void setPrinterModel(String printerModel) {
        this.printerModel = printerModel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getConfigDescription() {
        return configDescription;
    }

    public void setConfigDescription(String configDescription) {
        this.configDescription = configDescription;
    }

    public String getPrinterIp() {
        return printerIp;
    }

    public void setPrinterIp(String printerIp) {
        this.printerIp = printerIp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrinterConnectType() {
        return printerConnectType;
    }

    public void setPrinterConnectType(String printerConnectType) {
        this.printerConnectType = printerConnectType;
    }

    public String getConfigSupportOs() {
        return configSupportOs;
    }

    public void setConfigSupportOs(String configSupportOs) {
        this.configSupportOs = configSupportOs;
    }
}
