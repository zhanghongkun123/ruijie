package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

/**
 * Description: 磁盘池总览统计信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
public class DiskPoolOverviewDTO {

    /**
     * 池数量
     */
    private int poolNum;

    /**
     * 磁盘数量
     */
    private int diskNum;

    /**
     * 磁盘使用中的数量
     */
    private int inUseNum;

    /**
     * 已分配的数量
     */
    private int assignedNum;

    public int getPoolNum() {
        return poolNum;
    }

    public void setPoolNum(int poolNum) {
        this.poolNum = poolNum;
    }


    public int getInUseNum() {
        return inUseNum;
    }

    public void setInUseNum(int inUseNum) {
        this.inUseNum = inUseNum;
    }

    public int getAssignedNum() {
        return assignedNum;
    }

    public void setAssignedNum(int assignedNum) {
        this.assignedNum = assignedNum;
    }

    public int getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(int diskNum) {
        this.diskNum = diskNum;
    }

    /**
     * 已分配数自增
     */
    public void assignedNumIncrement() {
        this.assignedNum++;
    }

    /**
     * 使用数自增
     */
    public void inUseNumIncrement() {
        this.inUseNum++;
    }
}
