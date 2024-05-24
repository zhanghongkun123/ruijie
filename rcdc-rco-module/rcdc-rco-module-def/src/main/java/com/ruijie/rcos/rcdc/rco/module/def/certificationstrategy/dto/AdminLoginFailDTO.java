package com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto;


/**
 * Description: 管理员登陆失败时返回剩余次数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-07-13
 *
 * @author ryx
 */
public class AdminLoginFailDTO {

    /**
     * 密码剩余错误次数
     */
    private Integer remainingTimes;

    public AdminLoginFailDTO(Integer remainingTimes) {
        this.remainingTimes = remainingTimes;
    }

    public Integer getRemainingTimes() {
        return remainingTimes;
    }

    public void setRemainingTimes(Integer remainingTimes) {
        this.remainingTimes = remainingTimes;
    }
}
