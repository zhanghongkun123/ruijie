package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto;

/**
 * 
 * Description: 镜像进度
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/4
 *
 * @author zhiweiHong
 */
public class ImageProgressInfo {

    /**
     * 总数
     */
    private long total;

    /**
     * 稳定态镜像
     */
    private long steadyStateImage;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSteadyStateImage() {
        return steadyStateImage;
    }

    public void setSteadyStateImage(long steadyStateImage) {
        this.steadyStateImage = steadyStateImage;
    }
}
