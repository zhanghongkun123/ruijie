package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.TaskQueryRequset;
import com.ruijie.rcos.rcdc.task.ext.module.def.api.CbbBatchTaskProgressAPI;
import com.ruijie.rcos.rcdc.task.ext.module.def.dto.BatchTaskProgressDTO;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * Description: 任务进度查询
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月11日
 *
 * @author zdc
 */
@Api(tags = "任务进度查询")
@Controller
@RequestMapping("/rco/app/task")
public class TaskProgressQueryCtrl {

    @Autowired
    CbbBatchTaskProgressAPI cbbBatchTaskProgressAPI;

    /**
     * 任务进度查询
     * @param taskQueryRequset 任务进度查询请求
     * @param sessionContext 会话上下文
     * @return DefaultWebResponse
     */
    @ApiOperation(value = "任务进度查询")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"任务进度查询"})})
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public DefaultWebResponse queryTaskProcess(TaskQueryRequset taskQueryRequset
            , SessionContext sessionContext) {
        Assert.notNull(taskQueryRequset, "getInfoListRequest is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        BatchTaskProgressDTO batchTaskProgressDTO = cbbBatchTaskProgressAPI.getProgressByTaskId(taskQueryRequset.getResoureId());
        return DefaultWebResponse.Builder.success(batchTaskProgressDTO);
    }
}
