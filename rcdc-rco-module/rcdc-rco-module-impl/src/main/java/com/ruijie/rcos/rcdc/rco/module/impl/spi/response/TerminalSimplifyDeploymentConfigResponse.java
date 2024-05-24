package com.ruijie.rcos.rcdc.rco.module.impl.spi.response;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/28
 *
 * @author Jarman
 */
public class TerminalSimplifyDeploymentConfigResponse {


    /**
     * 终端极简部署配置开关
     */
    private Boolean enableTerminalSimplifyDeployment;


    /**
     * 用户名称
     */
    private String userName;


    /**
     * IDV 镜像ID
     */
    private UUID idvImageTemplateId;


    /**
     * TCI 镜像ID
     */
    private UUID voiImageTemplateId;


    public Boolean getEnableTerminalSimplifyDeployment() {
        return enableTerminalSimplifyDeployment;
    }

    public void setEnableTerminalSimplifyDeployment(Boolean enableTerminalSimplifyDeployment) {
        this.enableTerminalSimplifyDeployment = enableTerminalSimplifyDeployment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getIdvImageTemplateId() {
        return idvImageTemplateId;
    }

    public void setIdvImageTemplateId(UUID idvImageTemplateId) {
        this.idvImageTemplateId = idvImageTemplateId;
    }

    public UUID getVoiImageTemplateId() {
        return voiImageTemplateId;
    }

    public void setVoiImageTemplateId(UUID voiImageTemplateId) {
        this.voiImageTemplateId = voiImageTemplateId;
    }


}
