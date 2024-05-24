package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto;

/**
 * Description: ftp配置信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/7 10:21 下午
 *
 * @author zhouhuan
 */
public class FtpConfigDTO {


    private String ftpServerIp;

    private Integer ftpPort;

    private String ftpUserName;

    private String ftpUserPassword;

    private String ftpPath;

    private String fileDir;

    /**
     * 企金版本1.1版本后，服务器FTP支持SSL加密传输
     */
    private Boolean enableSSL = Boolean.TRUE;

    public String getFtpServerIp() {
        return ftpServerIp;
    }

    public void setFtpServerIp(String ftpServerIp) {
        this.ftpServerIp = ftpServerIp;
    }

    public Integer getFtpPort() {
        return ftpPort;
    }

    public void setFtpPort(Integer ftpPort) {
        this.ftpPort = ftpPort;
    }

    public String getFtpUserName() {
        return ftpUserName;
    }

    public void setFtpUserName(String ftpUserName) {
        this.ftpUserName = ftpUserName;
    }

    public String getFtpUserPassword() {
        return ftpUserPassword;
    }

    public void setFtpUserPassword(String ftpUserPassword) {
        this.ftpUserPassword = ftpUserPassword;
    }

    public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }

    public String getFileDir() {
        return fileDir;
    }

    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }

    public Boolean getEnableSSL() {
        return enableSSL;
    }

    public void setEnableSSL(Boolean enableSSL) {
        this.enableSSL = enableSSL;
    }
}
