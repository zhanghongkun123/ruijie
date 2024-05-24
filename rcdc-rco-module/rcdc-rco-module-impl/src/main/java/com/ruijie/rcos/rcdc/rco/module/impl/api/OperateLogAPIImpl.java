package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.gss.log.module.def.api.BaseOperateLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.OperateLogAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:07
 *
 * @author zqj
 */
public class OperateLogAPIImpl implements OperateLogAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperateLogAPIImpl.class);

    @Autowired
    private BaseOperateLogMgmtAPI baseOperateLogMgmtAPI;

    @Override
    public PageQueryResponse<BaseOperateLogDTO> operateLogList(PageQueryBuilderFactory.RequestBuilder request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        PageQueryResponse<BaseOperateLogDTO> response = baseOperateLogMgmtAPI.pageQuery(request.build());
        return response;
    }
}