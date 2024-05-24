package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.InfeTypeEnum;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月15日
 *
 * @author ljm
 */
public class GetInfoRequest implements Request {

    @Nullable
    private InfeTypeEnum info;

    /**
     * 任务ID
     */
    @Nullable
    private UUID taskid;

    /**
     * 文件路径
     */
    @Nullable
    private String resultTaskPath;

    /**
     * RCDC 内部任务ID
     */
    @Nullable
    private UUID resultTaskid;

    public InfeTypeEnum getInfo() {
        return info;
    }

    public void setInfo(InfeTypeEnum info) {
        this.info = info;
    }

    public UUID getTaskid() {
        return taskid;
    }

    public void setTaskid(UUID taskid) {
        this.taskid = taskid;
    }

    public String getResultTaskPath() {
        return resultTaskPath;
    }

    public void setResultTaskPath(String resultTaskPath) {
        this.resultTaskPath = resultTaskPath;
    }

    public UUID getResultTaskid() {
        return resultTaskid;
    }

    public void setResultTaskid(UUID resultTaskid) {
        this.resultTaskid = resultTaskid;
    }
}
