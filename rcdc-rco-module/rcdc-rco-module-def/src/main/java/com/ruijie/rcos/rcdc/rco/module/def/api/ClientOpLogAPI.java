package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.dto.ClientOptLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.enums.ClientOperateLogType;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.request.ClientOpLogPageRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
public interface ClientOpLogAPI {

    /**
     * 保存用户客户端操作日志
     *
     * @param terminalId     终端id
     * @param userId         用户id
     * @param operateLogType 操作类型
     */
    void saveUserOperateLog(String terminalId, UUID userId, ClientOperateLogType operateLogType);

    /**
     * 分页查询客户端操作日志
     *
     * @param request request
     * @return 响应
     */
    DefaultPageResponse<ClientOptLogDTO> pageQuery(ClientOpLogPageRequest request);

    /**
     * 清除指定时间前的日志
     *
     * @param specifiedDate 指定时间
     */
    void clear(Date specifiedDate);

}
