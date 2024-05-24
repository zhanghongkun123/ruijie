package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto;

/**
 * 
 * Description: 桌面任务进度
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/4
 *
 * @author zhiweiHong
 */
public class DeskProgressInfo {

    /**
     * 总数
     */
    private long total;

    /**
     * 关闭的桌面数量
     */
    private long closeDeskCount;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getCloseDeskCount() {
        return closeDeskCount;
    }

    public void setCloseDeskCount(long closeDeskCount) {
        this.closeDeskCount = closeDeskCount;
    }
}
