package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: 大屏监控平台信息控制器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 11:53
 *
 * @author BaiGuoliang
 */
@Controller
@RequestMapping("/rco/platform")
public class PlatformController {

    @Autowired
    private PlatformAPI platformAPI;

    /**
     * 系统时间信息
     *
     * @param request 空请求
     * @return 系统时间信息响应
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "/getSystemTime")
    public DefaultWebResponse getSystemTime(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request,"request 不能为空");

        SystemTimeResponse systemTimeResponse = platformAPI.getSystemTime();
        return DefaultWebResponse.Builder.success(systemTimeResponse);
    }


}
