package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.resquest;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo.DistributeParameterVO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 14:19
 *
 * @author zhangyichi
 */
public class DistributeTaskCreateWebRequest implements WebRequest {

    @NotBlank
    @TextShort
    private String taskName;

    @NotNull
    private DistributeParameterVO parameter;

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

    public DistributeParameterVO getParameter() {
        return parameter;
    }

    public void setParameter(DistributeParameterVO parameter) {
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
