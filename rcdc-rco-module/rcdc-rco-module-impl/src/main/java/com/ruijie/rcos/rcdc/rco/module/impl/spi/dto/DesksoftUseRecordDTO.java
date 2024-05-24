package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import org.springframework.util.Assert;

/**
 * Description: 软件使用次数DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 20:54
 *
 * @author linrenjian
 */
public class DesksoftUseRecordDTO extends DesksoftOperateDTO {

    /**
     * 操作次数
     */
    private Integer operateTimes;

    public Integer getOperateTimes() {
        return operateTimes;
    }

    public void setOperateTimes(Integer operateTimes) {
        this.operateTimes = operateTimes;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(o, "object cannot be null");

        return super.equals(o);
    }
}
