package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

/**
 * Description:  大屏云主机状态web响应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/10
 *
 * @author xiao'yong'deng
 */
public class ServerHostStatusResponse {

    public static final int NORMAL_COUNT_DEFAULT = 0;

    public static final int ABNORMAL_COUNT_DEFAULT = 0;

    /**
     * 正常数量
     */
    private int normalCount;

    /**
     * 异常数量
     */
    private int abnormalCount;

    public int getNormalCount() {
        return normalCount;
    }

    public void setNormalCount(int normalCount) {
        this.normalCount = normalCount;
    }

    public int getAbnormalCount() {
        return abnormalCount;
    }

    public void setAbnormalCount(int abnormalCount) {
        this.abnormalCount = abnormalCount;
    }
}
