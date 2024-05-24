package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: 编辑idv终端分组回填数据
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/27 13:57
 *
 * @author conghaifeng
 */
public class IdvTerminalGroupDetailVO {

    @ApiModelProperty(value = "分组ID")
    private UUID id;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    /**
     * 父级分组
     */
    @ApiModelProperty(value = "父级分组")
    private IdLabelStringEntry parentGroup;

    @ApiModelProperty(value = "IDV桌面配置")
    private IdvDesktopConfigVO idvDesktopConfig;

    @ApiModelProperty(value = "VOI桌面配置")
    private IdvDesktopConfigVO voiDesktopConfig;

    @ApiModelProperty(value = "白名单配置")
    private WifiWhitelistConfigVO idvWifiWhitelistConfig;

    public IdvDesktopConfigVO getVoiDesktopConfig() {
        return voiDesktopConfig;
    }

    public void setVoiDesktopConfig(IdvDesktopConfigVO voiDesktopConfig) {
        this.voiDesktopConfig = voiDesktopConfig;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public IdLabelStringEntry getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(IdLabelStringEntry parentGroup) {
        this.parentGroup = parentGroup;
    }

    public IdvDesktopConfigVO getIdvDesktopConfig() {
        return idvDesktopConfig;
    }

    public void setIdvDesktopConfig(IdvDesktopConfigVO idvDesktopConfig) {
        this.idvDesktopConfig = idvDesktopConfig;
    }

    public WifiWhitelistConfigVO getIdvWifiWhitelistConfig() {
        return idvWifiWhitelistConfig;
    }

    public void setIdvWifiWhitelistConfig(WifiWhitelistConfigVO idvWifiWhitelistConfig) {
        this.idvWifiWhitelistConfig = idvWifiWhitelistConfig;
    }
}
