package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.response.AlarmWebResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description:告警API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/29 9:44
 *
 * @author zqj
 */
public interface AlarmAPI {


    /**
     * 分页
     * @param request 请求消息体
     * @return 请求结果
     */
    DefaultPageResponse<AlarmWebResponse> pageQuery(PageWebRequest request);
}
