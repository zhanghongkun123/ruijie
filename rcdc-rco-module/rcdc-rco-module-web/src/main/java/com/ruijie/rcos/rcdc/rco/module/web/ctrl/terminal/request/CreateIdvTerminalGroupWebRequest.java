package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvCreateTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.WifiWhitelistConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.wifi.vo.WifiWhitelistVO;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/26 13:40
 *
 * @author conghaifeng
 */
public class CreateIdvTerminalGroupWebRequest extends CreateTerminalGroupWebRequest {

    /**
     * 镜像模板，云桌面策略,DHCP
     */
    @ApiModelProperty(value = "idv桌面配置", required = false)
    @Nullable
    private IdvDesktopConfigVO idvDesktopConfig;

    /**
     * 镜像模板，云桌面策略,DHCP
     */
    @ApiModelProperty(value = "voi桌面配置", required = false)
    @Nullable
    private IdvDesktopConfigVO voiDesktopConfig;

    @ApiModelProperty(value = "白名单", required = false)
    @Nullable
    private WifiWhitelistConfigVO idvWifiWhitelistConfig;

    /**
     * 请求对象转换
     *
     * @param request CreateTerminalGroupWebRequest
     * @return CreateTerminalGroupRequest
     */
    public static IdvCreateTerminalGroupRequest convertFor(CreateIdvTerminalGroupWebRequest request) {
        Assert.notNull(request, "request can not be null");
        IdvCreateTerminalGroupRequest apiRequest = new IdvCreateTerminalGroupRequest();
        apiRequest.setGroupName(request.getGroupName());
        if (request.getParentGroup() != null) {
            apiRequest.setParentGroupId(request.getParentGroup().getId());
        }
        IdvDesktopConfigVO idvconfig = request.getIdvDesktopConfig();
        if (idvconfig != null && idvconfig.getImage() != null) {
            apiRequest.setCbbIdvDesktopImageId(idvconfig.getImage().getId());
            apiRequest.setCbbIdvDesktopStrategyId(idvconfig.getStrategy().getId());
            if (idvconfig.getSoftwareStrategy() != null) {
                apiRequest.setIdvSoftwareStrategyId(idvconfig.getSoftwareStrategy().getId());
            }
            IdLabelEntry userProfileStrategy = idvconfig.getUserProfileStrategy();
            if (userProfileStrategy != null) {
                apiRequest.setCbbIdvUserProfileStrategyId(userProfileStrategy.getId());
            }
        }

        //voi桌面 配置
        IdvDesktopConfigVO config = request.getVoiDesktopConfig();
        if (config != null && config.getImage() != null && config.getStrategy() != null) {
            apiRequest.setCbbVoiDesktopImageId(config.getImage().getId());
            apiRequest.setCbbVoiDesktopStrategyId(config.getStrategy().getId());
            IdLabelEntry userProfileStrategy = config.getUserProfileStrategy();
            if (userProfileStrategy != null) {
                apiRequest.setCbbVoiUserProfileStrategyId(userProfileStrategy.getId());
            }
            if (config.getSoftwareStrategy() != null) {
                apiRequest.setVoiSoftwareStrategyId(config.getSoftwareStrategy().getId());
            }
        }

        if (request.getIdvWifiWhitelistConfig() != null) {
            List<WifiWhitelistVO> wifiWhiteList = request.getIdvWifiWhitelistConfig().getWifiWhiteList();
            List<WifiWhitelistDTO> wifiWhitelistDtoList = wifiWhiteList.stream()
                    .filter(Objects::nonNull)
                    .map(item -> new WifiWhitelistDTO(item.getSsid(), item.getIndex())).collect(Collectors.toList());
            apiRequest.setWifiWhitelistDTOList(wifiWhitelistDtoList);
        }
        return apiRequest;
    }

    @Nullable
    public IdvDesktopConfigVO getIdvDesktopConfig() {
        return idvDesktopConfig;
    }

    public void setIdvDesktopConfig(@Nullable IdvDesktopConfigVO idvDesktopConfig) {
        this.idvDesktopConfig = idvDesktopConfig;
    }

    @Nullable
    public WifiWhitelistConfigVO getIdvWifiWhitelistConfig() {
        return idvWifiWhitelistConfig;
    }

    public void setIdvWifiWhitelistConfig(@Nullable WifiWhitelistConfigVO idvWifiWhitelistConfig) {
        this.idvWifiWhitelistConfig = idvWifiWhitelistConfig;
    }

    @Nullable
    public IdvDesktopConfigVO getVoiDesktopConfig() {
        return voiDesktopConfig;
    }

    public void setVoiDesktopConfig(@Nullable IdvDesktopConfigVO voiDesktopConfig) {
        this.voiDesktopConfig = voiDesktopConfig;
    }
}
