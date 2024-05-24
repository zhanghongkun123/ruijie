package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.InfeTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/20 17:19
 *
 * @author linrenjian
 */
public class CmsInfoRespone extends DefaultResponse {
    @Nullable
    private InfeTypeEnum info;

    /**
     * 任务ID
     */
    @NotNull
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

    /**
     * RCDC 统计数量
     */
    @Nullable
    private int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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
