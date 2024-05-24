package com.ruijie.rcos.rcdc.rco.module.impl.connector;

/**
 * 连接信息
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月17日
 * 
 * @author lin
 */
public enum RccpConnectContext {

    /**
     * 单例对象
     */
    INSTANCE;

    private boolean isConnect = false;

    private int connectFailTimes = 0;

    public boolean isConnect() {
        return isConnect;
    }
    
    /**
     * 连接失败次数自增
     */
    public void inscreaseConnectFailTimes() {
        this.connectFailTimes++;
    }

    /**
     * 重置连接失败次数
     */
    public void resetConnectFailTimes() {
        this.connectFailTimes = 0;
    }

    /**
     * 获取连接失败次数
     *
     * @return 连接失败次数
     */
    public int queryConnectFailTimes() {
        return this.connectFailTimes;
    }

}
