package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup;

import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.EmptyWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.annotation.NoMaintenanceUrl;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月25日
 *
 * @author zhanghongkun
 */
@Api(tags = "服务器备份")
@Controller
@RequestMapping("/rco/serverbackup")
public class ServerBackupController {

    @Autowired
    CbbBackupLogAPI cbbBackupLogAPI;

    /**
     * 查询备份恢复任务进度
     *
     * @param emptyWebRequest 请求
     * @return 响应
     * @throws BusinessException BusinessException
     */
    @ApiOperation(value = "查询全量恢复任务进度")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"查询全量恢复任务进度"})})
    @RequestMapping(value = "progress/get", method = RequestMethod.POST)
    @NoMaintenanceUrl
    @NoAuthUrl
    public DefaultWebResponse getTaskProgress(EmptyWebRequest emptyWebRequest) throws BusinessException {
        Assert.notNull(emptyWebRequest, "emptyWebRequest can not be null");

        return DefaultWebResponse.Builder.success(cbbBackupLogAPI.getFullRestoreProgress());
    }

}

