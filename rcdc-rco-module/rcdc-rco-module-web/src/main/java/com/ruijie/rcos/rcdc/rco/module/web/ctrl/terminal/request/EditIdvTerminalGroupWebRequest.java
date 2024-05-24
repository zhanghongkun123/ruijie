package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VOIDesktopConfig;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.IdvEditTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.wifi.vo.EditWifiWhitelistConfigVO;

import io.swagger.annotations.ApiModelProperty;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/26 18:31
 *
 * @author conghaifeng
 */
public class EditIdvTerminalGroupWebRequest extends EditTerminalGroupWebRequest {

    /**
     * 镜像模板，网络策略，云桌面策略,DHCP
     */
    @Nullable
    @ApiModelProperty(value = "idv桌面配置")
    private IdvDesktopConfigVO idvDesktopConfig;

    /**
     * 镜像模板，网络策略，云桌面策略,DHCP
     */
    @Nullable
    @ApiModelProperty(value = "voi桌面配置")
    private IdvDesktopConfigVO voiDesktopConfig;

    /**
     * 白名单配置信息
     * 列表最大长度不大于3
     */
    @Nullable
    @ApiModelProperty(value = "白名单配置")
    private EditWifiWhitelistConfigVO idvWifiWhitelistConfig;

    /**
     * 请求对象转换
     *
     * @param request EditTerminalGroupWebRequest
     * @return CbbEditTerminalGroupRequest
     */
    public static IdvEditTerminalGroupRequest convertFor(EditIdvTerminalGroupWebRequest request) {
        Assert.notNull(request, "request can not be null");
        IdvEditTerminalGroupRequest apiRequest = new IdvEditTerminalGroupRequest();
        apiRequest.setGroupName(request.getGroupName());
        apiRequest.setId(request.getId());
        if (request.getParentGroup() != null) {
            apiRequest.setParentGroupId(request.getParentGroup().getId());
        }
        if (request.getIdvDesktopConfig() != null) {
            IdvDesktopConfigVO config = request.getIdvDesktopConfig();
            apiRequest.setCbbIdvDesktopImageId(config.getImage().getId());
            apiRequest.setCbbIdvDesktopStrategyId(config.getStrategy().getId());
            IdLabelEntry userProfileStrategy = config.getUserProfileStrategy();
            if (userProfileStrategy != null) {
                apiRequest.setCbbIdvUserProfileStrategyId(userProfileStrategy.getId());
            }
            if (config.getSoftwareStrategy() != null) {
                apiRequest.setIdvSoftwareStrategyId(config.getSoftwareStrategy().getId());
            }
        }
        //voi 配置不为空
        if (request.getVoiDesktopConfig() != null) {
            IdvDesktopConfigVO config = request.getVoiDesktopConfig();
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

        EditWifiWhitelistConfigVO whitelistVO = request.getIdvWifiWhitelistConfig();
        if (Objects.isNull(whitelistVO)) {
            apiRequest.setNeedApplyToSubgroup(false);
            apiRequest.setWifiWhitelistDTOList(Lists.newArrayList());
        } else {
            apiRequest.setNeedApplyToSubgroup(whitelistVO.getNeedApplyToSubgroup());
            List<WifiWhitelistDTO> wifiWhitelistDTOList = whitelistVO.getWifiWhiteList().stream().filter(Objects::nonNull).map(item -> {
                WifiWhitelistDTO wifiWhitelistDTO = new WifiWhitelistDTO();
                BeanUtils.copyProperties(item, wifiWhitelistDTO);
                return wifiWhitelistDTO;
            }).collect(Collectors.toList());
            apiRequest.setWifiWhitelistDTOList(wifiWhitelistDTOList);
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
    public EditWifiWhitelistConfigVO getIdvWifiWhitelistConfig() {
        return idvWifiWhitelistConfig;
    }

    public void setIdvWifiWhitelistConfig(@Nullable EditWifiWhitelistConfigVO idvWifiWhitelistConfig) {
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
