package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.wifi.vo.WifiWhitelistVO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import io.swagger.annotations.ApiModelProperty;

/**
 *
 * Description: 创建白名单配置实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class WifiWhitelistConfigVO {
    /**
     * 白名单列表
     */
    @Nullable
    @JSONField(name = "wifiWhitelist")
    @ApiModelProperty(value = "白名单集合")
    private List<WifiWhitelistVO> wifiWhiteList;

    public WifiWhitelistConfigVO() {
        // Do nothing because of X and Y.
    }

    /**
     * 获取无线白名单
     * 
     * @return List<WifiWhitelistVO>
     */
    public List<WifiWhitelistVO> getWifiWhiteList() {
        if (Objects.nonNull(wifiWhiteList)) {
            return wifiWhiteList;
        } else {
            return Lists.newArrayList();
        }
    }

    /**
     * 参数校验
     * 
     * @throws BusinessException 业务异常
     */
    public void verify() throws BusinessException {
        if (!CollectionUtils.isEmpty(this.wifiWhiteList) && this.wifiWhiteList.size() > 3) {
            throw new BusinessException(BusinessKey.RCDC_RCO_WIFI_GT_FOUR);
        }
        if (!CollectionUtils.isEmpty(this.wifiWhiteList)) {
            int distinctSize = this.wifiWhiteList.stream().map(WifiWhitelistVO::getSsid).distinct().collect(Collectors.toList()).size();
            if (distinctSize != this.wifiWhiteList.size()) {
                throw new BusinessException(BusinessKey.RCDC_RCO_WIFI_DUPLICATE);
            }
        }
    }

    public void setWifiWhiteList(List<WifiWhitelistVO> wifiWhiteList) {
        this.wifiWhiteList = wifiWhiteList;
    }

}
