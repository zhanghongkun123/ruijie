package com.ruijie.rcos.rcdc.rco.module.openapi.rest.site.impl;


import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ClusterMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.site.PageQuerySiteRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.site.RegisterSiteRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.RegisterSiteResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.site.SiteInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.site.SiteMangeRestServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/25 14:39
 *
 * @author yxq
 */
@Service
public class SiteMangeRestServerImpl implements SiteMangeRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteMangeRestServerImpl.class);

    @Autowired
    private ClusterMgmtAPI clusterMgmtAPI;

    @Override
    public void register(RegisterSiteRequest registerSiteRequest) throws BusinessException {
        Assert.notNull(registerSiteRequest, "registerSiteRequest must not be null");

        LOGGER.info("接收到注册站点请求,[{}]", JSON.toJSONString(registerSiteRequest));
        clusterMgmtAPI.registerSite(registerSiteRequest);
    }

    @Override
    public PageResponse<SiteInfoDTO> listSites(PageQuerySiteRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "registerSiteRequest must not be null");

        return clusterMgmtAPI.listSites(pageRequest);
    }
}
