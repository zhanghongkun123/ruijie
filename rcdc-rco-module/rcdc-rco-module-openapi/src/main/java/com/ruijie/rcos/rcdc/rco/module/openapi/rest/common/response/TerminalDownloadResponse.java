package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response;

/**
 * Description: 终端App下载信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-27
 *
 * @author td
 */
public class TerminalDownloadResponse {

    /**
     * 是否就绪：默认
     */
    private Boolean enableDownload;

    /**
     * 包名
     */
    private String completePackageName;


    private String completePackageUrl;

    public String getCompletePackageUrl() {
        return completePackageUrl;
    }

    public void setCompletePackageUrl(String completePackageUrl) {
        this.completePackageUrl = completePackageUrl;
    }

    public Boolean getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(Boolean enableDownload) {
        this.enableDownload = enableDownload;
    }

    public String getCompletePackageName() {
        return completePackageName;
    }

    public void setCompletePackageName(String completePackageName) {
        this.completePackageName = completePackageName;
    }
}
