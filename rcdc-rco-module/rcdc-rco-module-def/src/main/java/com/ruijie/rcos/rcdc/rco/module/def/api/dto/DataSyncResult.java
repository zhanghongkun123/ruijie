package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.io.Serializable;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/21 13:57
 *
 * @author coderLee23
 */
public class DataSyncResult implements Serializable {

    private static final long serialVersionUID = -2284759595108026541L;

    /**
     * 执行成功数量
     */
    private Integer successNum;

    /**
     * 执行失败数量
     */
    private Integer failureNum;

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public Integer getFailureNum() {
        return failureNum;
    }

    public void setFailureNum(Integer failureNum) {
        this.failureNum = failureNum;
    }
}
