package com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: ftp配置信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/7 10:21 下午
 *
 * @author zhouhuan
 */
public class FtpConfigDTO {
    @Nullable
    private String ftpServerIP;

    @NotNull
    private Integer ftpPort;

    @NotNull
    private String ftpUserName;

    @NotNull
    private String ftpUserPassword;

    @NotNull
    private String ftpPath;

    @NotNull
    private String fileDir;

    /**
     * 企金版本1.1版本后，服务器FTP支持SSL加密传输
     */
    @Nullable
    private Boolean enableSSL = Boolean.TRUE;

    @Nullable
    public String getFtpServerIP() {
        return this.ftpServerIP;
    }

    public void setFtpServerIP(@Nullable String ftpServerIP) {
        this.ftpServerIP = ftpServerIP;
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
