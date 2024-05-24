package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/13
 *
 * @author xiao'yong'deng
 */
public class UserDesktopItem {

    /**
     * 用户总数
     */
    private Long userCount;

    /**
     * 拥有云桌面的用户数量
     */
    private Long deskTopCount;

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public Long getDeskTopCount() {
        return deskTopCount;
    }

    public void setDeskTopCount(Long deskTopCount) {
        this.deskTopCount = deskTopCount;
    }
}
