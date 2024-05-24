package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.io.Serializable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月11日
 *
 * @author zhk
 */
public class CheckTerminalWhiteDTO extends EqualsHashcodeSupport implements Serializable {
    /**
     * 终端id
     */

    private String terminalId;

    /**
     * 产品型号
     */
    private String productType;

    /**
     * 终端mac
     */
    private String terminalMac;

    /**
     * SN码
     */
    private String serialNumber;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
