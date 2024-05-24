package com.ruijie.rcos.rcdc.rco.module.def.distribution.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/05/04 11:41
 *
 * @author coderLee23
 */
public class DistributeTaskDetailDTO {

    private UUID id;

    private String taskName;

    private Date createTime;

    private int waitingNum;

    private int successNum;

    private int failNum;

    private int runningNum;

    private int canceledNum;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getWaitingNum() {
        return waitingNum;
    }

    public void setWaitingNum(int waitingNum) {
        this.waitingNum = waitingNum;
    }

    public int getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(int successNum) {
        this.successNum = successNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public int getRunningNum() {
        return runningNum;
    }

    public void setRunningNum(int runningNum) {
        this.runningNum = runningNum;
    }

    public int getCanceledNum() {
        return canceledNum;
    }

    public void setCanceledNum(int canceledNum) {
        this.canceledNum = canceledNum;
    }

}
