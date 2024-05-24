package com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbComputerClusterCpuArchEnums;
import com.ruijie.rcos.rcdc.rco.module.common.enums.CpuArchType;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: ServerModelWebResponse
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/4 15:46
 *
 * @author wjp
 */
public class ServerModelWebResponse {

    @ApiModelProperty(value = "服务器模式， init：未初始化， vdi：VDI部署模式， rcm：IDV部署模式， mini：MINI部署模式")
    private String serverModel;

    @ApiModelProperty(value = "CMS组件启动情况， init：未初始化， disabled：未启用， enabled：已启用")
    private String cmsComponent;

    @ApiModelProperty(value = "UWS组件启动情况， INIT_STATE：未初始化， DISABLED_STATE：未启用， ENABLED_STATE：已启用")
    private String uwsComponent;

    @ApiModelProperty(value = "虚拟应用启动情况")
    private Boolean enableVirtualApplication;


    @ApiModelProperty(value = "CPU 类型X86_64 ARM MIX_GROUP")
    private CbbComputerClusterCpuArchEnums serverClustercpuArch;


    public CbbComputerClusterCpuArchEnums getServerClustercpuArch() {
        return serverClustercpuArch;
    }

    public void setServerClustercpuArch(CbbComputerClusterCpuArchEnums serverClustercpuArch) {
        this.serverClustercpuArch = serverClustercpuArch;
    }

    public String getServerModel() {
        return serverModel;
    }

    public void setServerModel(String serverModel) {
        this.serverModel = serverModel;
    }

    public String getCmsComponent() {
        return cmsComponent;
    }

    public void setCmsComponent(String cmsComponent) {
        this.cmsComponent = cmsComponent;
    }

    public String getUwsComponent() {
        return uwsComponent;
    }

    public void setUwsComponent(String uwsComponent) {
        this.uwsComponent = uwsComponent;
    }

    public Boolean getEnableVirtualApplication() {
        return enableVirtualApplication;
    }

    public void setEnableVirtualApplication(Boolean enableVirtualApplication) {
        this.enableVirtualApplication = enableVirtualApplication;
    }
}
