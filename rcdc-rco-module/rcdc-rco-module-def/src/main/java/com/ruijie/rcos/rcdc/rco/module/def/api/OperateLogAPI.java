package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.gss.log.module.def.dto.BaseOperateLogDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 *
 * Description: 日志API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年06月01日
 *
 * @author zqj
 */
public interface OperateLogAPI {

    /**
     * 获取操作日志列表请求
     *
     * @param request 请求体
     * @return BaseOperateLogDTO
     * @throws BusinessException ex
     */
    PageQueryResponse<BaseOperateLogDTO> operateLogList(PageQueryBuilderFactory.RequestBuilder request) throws BusinessException;


}