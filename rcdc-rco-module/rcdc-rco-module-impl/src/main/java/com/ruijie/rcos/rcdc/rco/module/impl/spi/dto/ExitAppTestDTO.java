package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import java.io.Serializable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月10日
 *
 * @author zhk
 */
public class ExitAppTestDTO extends EqualsHashcodeSupport implements Serializable {

    /**
     * true 用户退出/false 业务退出
     */
    @NotNull
    private Boolean isUserExit;

    /**
     * 0、测试成功，1、用户点击失败、xxx业务失败
     */
    @NotNull
    private Integer result;

    public Boolean getUserExit() {
        return isUserExit;
    }

    public void setIsUserExit(Boolean userExit) {
        isUserExit = userExit;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
