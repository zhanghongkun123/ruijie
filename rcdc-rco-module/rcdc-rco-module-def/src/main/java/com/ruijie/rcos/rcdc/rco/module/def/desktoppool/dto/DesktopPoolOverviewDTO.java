package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

/**
 * Description: 桌面池总览
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月06日
 *
 * @author linke
 */
public class DesktopPoolOverviewDTO {

    /**
     * 池数量
     */
    private int poolNum;

    /**
     * 桌面数量
     */
    private int desktopNum;

    /**
     * 桌面运行中的数量
     */
    private int running;

    /**
     * 桌面关闭的数量
     */
    private int close;

    /**
     * 未分配的数量
     */
    private int free;

    /**
     * 报障
     */
    private int fault;

    /**
     * 已连接数
     */
    private int connectedNum;

    public int getPoolNum() {
        return poolNum;
    }

    public void setPoolNum(int poolNum) {
        this.poolNum = poolNum;
    }

    public int getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(int desktopNum) {
        this.desktopNum = desktopNum;
    }

    public int getRunning() {
        return running;
    }

    public void setRunning(int running) {
        this.running = running;
    }

    public int getClose() {
        return close;
    }

    public void setClose(int close) {
        this.close = close;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getFault() {
        return fault;
    }

    public void setFault(int fault) {
        this.fault = fault;
    }

    public int getConnectedNum() {
        return connectedNum;
    }

    public void setConnectedNum(int connectedNum) {
        this.connectedNum = connectedNum;
    }
}
