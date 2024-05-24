package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: IDV桌面 SHINE => CDC 获取域控信息,镜像加域字段
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/10
 *
 * @author WuShengQiang
 */
public class AutoAddDomainRequestDTO {

    /**
     * GT重试次数
     */
    private int tryTimes;

    /**
     * 镜像是否加域
     */
    private Boolean imageAd;

    public int getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
    }

    public Boolean getImageAd() {
        return imageAd;
    }

    public void setImageAd(Boolean imageAd) {
        this.imageAd = imageAd;
    }
}
