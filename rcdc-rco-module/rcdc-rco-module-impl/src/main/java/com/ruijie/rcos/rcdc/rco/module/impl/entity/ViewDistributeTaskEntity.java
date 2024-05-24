package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 文件分发任务（父任务）列表VO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/18 14:30
 *
 * @author zhangyichi
 */
@Table(name = "v_rco_distribute_task")
@Entity
public class ViewDistributeTaskEntity {

    @Id
    private UUID id;

    private String taskName;

    private Date createTime;

    private int waitingNum;

    private int successNum;

    private int failNum;

    private int runningNum;

    private int canceledNum;

    @Version
    private int version;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
