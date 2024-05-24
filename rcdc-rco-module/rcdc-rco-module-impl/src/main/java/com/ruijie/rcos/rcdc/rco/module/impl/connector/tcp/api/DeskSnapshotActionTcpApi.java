package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api;

import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.OneClientAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: 用户自定义快照功能
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/28 9:48
 *
 * @author chenl
 */
@Tcp
public interface DeskSnapshotActionTcpApi {

    /**
     * 快照列表发生变更通知事件
     * @param terminalId terminalId 终端id
     * @param response 请求参数
     * @throws BusinessException 业务异常
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_REFRESH_LIST)
    void snapshotRefreshList(@SessionAlias String terminalId, EstCommonActionResponse<String> response) throws BusinessException;


    /**
     * 快照消息通知
     * @param terminalId terminalId 终端id
     * @param response 请求参数
     * @throws BusinessException 业务异常
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_NOTIFY)
    void snapshotNotify(@SessionAlias String terminalId, EstCommonActionResponse<String> response) throws BusinessException;

}
