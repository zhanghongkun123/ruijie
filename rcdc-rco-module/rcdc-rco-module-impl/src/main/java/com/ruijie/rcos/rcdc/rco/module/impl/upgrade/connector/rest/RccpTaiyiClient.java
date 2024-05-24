package com.ruijie.rcos.rcdc.rco.module.impl.upgrade.connector.rest;

import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.PromptVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.upgrade.dto.CancelPromptVersionDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/13 17:22
 *
 * @author ketb
 */
@Path("/v1/online_upgrade")
public interface RccpTaiyiClient {

    /**
     * 查看推送版本列表
     * @return 版本列表
     * @throws BusinessException 业务异常
     */
    @Path("/prompt_version")
    @GET
    PromptVersionDTO queryVersionsList() throws BusinessException;

    /**
     * 取消提示版本
     * @param cancelPromptVersionDTO cancelPromptVersionDTO
     * @throws BusinessException 业务异常
     */
    @Path("/recommend_version/cancel_prompt")
    @POST
    void cancelPromptVersion(CancelPromptVersionDTO cancelPromptVersionDTO) throws BusinessException;
}
