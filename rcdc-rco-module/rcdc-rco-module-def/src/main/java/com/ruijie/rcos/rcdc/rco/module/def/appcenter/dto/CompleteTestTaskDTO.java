package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月27日
 *
 * @author zhk
 */
public class CompleteTestTaskDTO {

    private UUID testId;

    private String reason;

    private TestTaskStateEnum state;

    private Boolean isBatchDesktop;

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TestTaskStateEnum getState() {
        return state;
    }

    public void setState(TestTaskStateEnum state) {
        this.state = state;
    }

    public Boolean getBatchDesktop() {
        return isBatchDesktop;
    }

    public void setBatchDesktop(Boolean batchDesktop) {
        isBatchDesktop = batchDesktop;
    }
}
