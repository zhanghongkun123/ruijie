package com.ruijie.rcos.rcdc.rco.module.openapi.rest.protocolcheck;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.protocolcheck.response.SmsAndScanCodeConfigResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月16日
 *
 * @author zhanghongkun
 */
@Path("/protocol")
public interface SmsAndScanCodeRestServer {
    /**
     * 获取短信和扫码全局开关
     * @return 请求响应
     */
    @GET
    @Path("/checkSMSAndScanCode")
    CommonWebResponse<SmsAndScanCodeConfigResponse> checkSMSAndScanCode();
}
