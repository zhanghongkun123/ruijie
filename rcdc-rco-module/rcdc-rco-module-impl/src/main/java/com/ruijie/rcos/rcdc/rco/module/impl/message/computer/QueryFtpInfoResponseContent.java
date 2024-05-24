package com.ruijie.rcos.rcdc.rco.module.impl.message.computer;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/9 14:33
 *
 * @author ketb
 */
public class QueryFtpInfoResponseContent extends BaseResponseContent {

    private String ftpUser;

    private String ftpPasswd;

    public QueryFtpInfoResponseContent(String business) {
        super(business);
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPasswd() {
        return ftpPasswd;
    }

    public void setFtpPasswd(String ftpPasswd) {
        this.ftpPasswd = ftpPasswd;
    }
}
