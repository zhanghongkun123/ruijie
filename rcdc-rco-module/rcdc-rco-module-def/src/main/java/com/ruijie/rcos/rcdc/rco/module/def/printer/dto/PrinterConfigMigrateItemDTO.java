package com.ruijie.rcos.rcdc.rco.module.def.printer.dto;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/13
 *
 * @author zhangsiming
 */
public class PrinterConfigMigrateItemDTO extends PrinterConfigDTO {

    private Integer configSerial;

    private String configDetail;

    private String configMd5;

    private Boolean configEnableCovered;

    private String printerName;

    private String printerPort;

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

    public Boolean getConfigEnableCovered() {
        return configEnableCovered;
    }

    public void setConfigEnableCovered(Boolean configEnableCovered) {
        this.configEnableCovered = configEnableCovered;
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

    public String getConfigMd5() {
        return configMd5;
    }

    public void setConfigMd5(String configMd5) {
        this.configMd5 = configMd5;
    }
}
