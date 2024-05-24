package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clientoplog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.rcdc.rco.module.def.api.ClientOpLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.dto.ClientOptLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.request.ClientOpLogPageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.TimePageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import io.swagger.annotations.ApiOperation;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
@Controller
@RequestMapping("/rco/client")
public class RcoClientOpLogController {

    @Autowired
    private ClientOpLogAPI clientOpLogAPI;

    /**
     * 客户端操作日志
     *
     * @param webRequest 请求体
     * @return 响应
     */
    @ApiOperation("客户端操作日志列表")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = {"客户端操作日志列表"})})
    @RequestMapping("/listOptLog")
    public CommonWebResponse listOptLog(TimePageWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");

        ClientOpLogPageRequest request = new ClientOpLogPageRequest(webRequest);
        request.setStartTime(webRequest.getStartTime());
        request.setEndTime(webRequest.getEndTime());

        DefaultPageResponse<ClientOptLogDTO> response = clientOpLogAPI.pageQuery(request);
        return CommonWebResponse.success(response);
    }
}
