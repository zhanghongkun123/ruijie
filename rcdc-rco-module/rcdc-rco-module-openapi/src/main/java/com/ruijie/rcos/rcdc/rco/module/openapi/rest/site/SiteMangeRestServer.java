package com.ruijie.rcos.rcdc.rco.module.openapi.rest.site;


import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.site.PageQuerySiteRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.site.RegisterSiteRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.RegisterSiteResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.site.SiteInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * Description: 站点管理OPEN API
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/25 12:44
 *
 * @author yxq
 */
@OpenAPI
@Path("/v1/sites")
public interface SiteMangeRestServer {


    /**
     * 注册站点
     *
     * @param registerSiteRequest 请求
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("")
    @ServerModel(businessExKey = BusinessKey.RCDC_RCO_ONLY_VDI_MODEL_CAN_SYNC_IMAGE)
    void register(RegisterSiteRequest registerSiteRequest) throws BusinessException;

    /**
     * 查询所有站点信息
     *
     * @param pageRequest 分页请求
     * @return 所有站点信息
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/list")
    PageResponse<SiteInfoDTO> listSites(PageQuerySiteRequest pageRequest) throws BusinessException;
}
