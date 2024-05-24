package com.ruijie.rcos.rcdc.rco.module.impl.connector.rest.tang;


import com.ruijie.rcos.rcdc.rco.module.def.api.response.NtpResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import org.springframework.lang.Nullable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-04-26 17:21
 *
 * @author wanglianyun
 */
@Path("/v1/compute/free_hosts")
public interface RccpTangClient {

    /**
     * 获取ntp信息
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/ntp_server")
    NtpResponse getNtpServer() throws BusinessException;

}
