package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cloudplatform.impl;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cloudplatform.CloudPlatformRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 云平台管理openApi接口实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/19
 *
 * @author WuShengQiang
 */
public class CloudPlatformRestServerImpl implements CloudPlatformRestServer {

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public PageQueryResponse<CloudPlatformDTO> list(PageQueryServerRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return cloudPlatformManageAPI.pageQuery(request);
    }

}
