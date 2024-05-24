package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import java.math.BigDecimal;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/2/19
 *
 * @author Jarman
 */
public class BandwidthSpeedResponse {

    private BigDecimal upSpeed;

    private BigDecimal downSpeed;

    public BandwidthSpeedResponse(BigDecimal upSpeed, BigDecimal downSpeed) {
        this.upSpeed = upSpeed;
        this.downSpeed = downSpeed;
    }

    public BigDecimal getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(BigDecimal upSpeed) {
        this.upSpeed = upSpeed;
    }

    public BigDecimal getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(BigDecimal downSpeed) {
        this.downSpeed = downSpeed;
    }
}
