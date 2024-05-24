package com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.service;

import org.springframework.data.domain.Page;

import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.request.ClientOpLogPageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.entity.ClientOpLogEntity;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
public interface ClientOpLogService {

    /**
     * 分页查询客户端操作日志列表
     *
     * @param request request
     * @return page
     */
    Page<ClientOpLogEntity> pageQuery(ClientOpLogPageRequest request);
}
