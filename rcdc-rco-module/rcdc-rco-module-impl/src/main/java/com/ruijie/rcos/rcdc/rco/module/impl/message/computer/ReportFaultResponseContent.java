package com.ruijie.rcos.rcdc.rco.module.impl.message.computer;

import java.util.Date;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/2 10:44
 *
 * @author ketb
 */
public class ReportFaultResponseContent extends BaseResponseContent {

    private String mac;

    private int result;

    private String faultDescription;

    private Date reportTime;

    public ReportFaultResponseContent(String business) {
        super(business);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }
}
