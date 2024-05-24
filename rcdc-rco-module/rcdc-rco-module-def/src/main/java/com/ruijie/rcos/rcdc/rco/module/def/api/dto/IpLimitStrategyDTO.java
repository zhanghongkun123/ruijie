package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.util.List;

/**
 * Description: IP网段限制策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/28 9:58
 *
 * @author yxq
 */
public class IpLimitStrategyDTO extends EqualsHashcodeSupport {

    /**
     * 是否开启IP限制
     */
    private Boolean enableIpLimit;

    /**
     * IP限制网段列表
     */
    private List<IpLimitDTO> ipLimitDTOList;

    public IpLimitStrategyDTO() {
    }

    public IpLimitStrategyDTO(Boolean enableIpLimit, List<IpLimitDTO> ipLimitDTOList) {
        this.enableIpLimit = enableIpLimit;
        this.ipLimitDTOList = ipLimitDTOList;
    }

    public Boolean getEnableIpLimit() {
        return enableIpLimit;
    }

    public void setEnableIpLimit(Boolean enableIpLimit) {
        this.enableIpLimit = enableIpLimit;
    }

    public List<IpLimitDTO> getIpLimitDTOList() {
        return ipLimitDTOList;
    }

    public void setIpLimitDTOList(List<IpLimitDTO> ipLimitDTOList) {
        this.ipLimitDTOList = ipLimitDTOList;
    }
}
