package com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;

/**
 * Description: 创建文件分发任务请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 14:35
 *
 * @author zhangyichi
 */
public class CreateDistributeTaskRequest extends DefaultRequest {

    @NotBlank
    @TextShort
    private String taskName;

    @NotNull
    private DistributeParameterDTO parameter;

    @Nullable
    private List<UUID> desktopIdList;

    @Nullable
    private List<Integer> cloudAppIdList;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public DistributeParameterDTO getParameter() {
        return parameter;
    }

    public void setParameter(DistributeParameterDTO parameter) {
        this.parameter = parameter;
    }

    @Nullable
    public List<UUID> getDesktopIdList() {
        return desktopIdList;
    }

    public void setDesktopIdList(@Nullable List<UUID> desktopIdList) {
        this.desktopIdList = desktopIdList;
    }

    @Nullable
    public List<Integer> getCloudAppIdList() {
        return cloudAppIdList;
    }

    public void setCloudAppIdList(@Nullable List<Integer> cloudAppIdList) {
        this.cloudAppIdList = cloudAppIdList;
    }

}
