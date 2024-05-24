package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.SystemVersionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response.SystemVersionWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.configcenter.ConfigCenterKvAPI;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 系统版本Controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月13日
 *
 * @author nt
 */
@Api(tags = "查询版本功能")
@Controller
@RequestMapping("rco/systemConfig/upgrade")
public class SystemVersionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemVersionController.class);

    @Autowired
    private ConfigCenterKvAPI configCenterKvAPI;

    @Autowired
    private SystemVersionMgmtAPI systemVersionMgmtAPI;

    /**
     * 查询当前版本的接口
     *
     * @param request 请求参数
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @ApiOperation("查询当前版本的接口")
    @RequestMapping(value = "getCurrentVersion", method = RequestMethod.GET)
    @NoAuthUrl
    public CommonWebResponse<SystemVersionWebResponse> getCurrentVersion(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        DtoResponse<String> stringDtoResponse = systemVersionMgmtAPI.obtainSystemReleaseVersion(new DefaultRequest());
        String version = stringDtoResponse.getDto();
        Assert.hasText(version, "version can not be null");
        String time = systemVersionMgmtAPI.obtainSystemReleaseTime();
        Assert.hasText(time, "time can not be null");
        LOGGER.debug("version<{}>time<{}>", version, time);
        SystemVersionWebResponse webResponse = new SystemVersionWebResponse();
        webResponse.setVersion(version);
        webResponse.setTime(time);
        return CommonWebResponse.success(webResponse);
    }
}
