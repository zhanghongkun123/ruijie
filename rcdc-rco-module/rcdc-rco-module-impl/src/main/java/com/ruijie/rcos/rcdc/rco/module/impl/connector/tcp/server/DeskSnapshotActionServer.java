package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.OneClientAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionTcpRequest;
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
public interface DeskSnapshotActionServer {

    /**
     * 快照列表
     * @param terminalId terminalId 终端id
     * @param estCommonActionTcpRequest 请求参数
     * @throws BusinessException 业务异常
     * @return EstCommonActionResponse 返回值
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_LIST)
    EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> snapshotList(@SessionAlias String terminalId,
                             EstCommonActionTcpRequest estCommonActionTcpRequest) throws BusinessException;


    /**
     * 创建快照
     * @param terminalId terminalId 终端id
     * @param estCommonActionTcpRequest 请求参数
     * @throws BusinessException 业务异常
     * @return EstCommonActionResponse 返回值
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_CREATE)
    EstCommonActionResponse create(@SessionAlias String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest)
            throws BusinessException;

    /**
     * 删除快照
     * @param terminalId terminalId 终端id
     * @param estCommonActionTcpRequest 请求参数
     * @throws BusinessException 业务异常
     * @return EstCommonActionResponse 返回值
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_DELETE)
    EstCommonActionResponse delete(@SessionAlias String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest)
            throws BusinessException;

    /**
     * 还原快照
     * @param terminalId terminalId 终端id
     * @param estCommonActionTcpRequest 请求参数
     * @throws BusinessException 业务异常
     * @return EstCommonActionResponse 返回值
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_RECOVER)
    EstCommonActionResponse recover(@SessionAlias String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest)
            throws BusinessException;

    /**
     * 快照详情
     * @param terminalId terminalId 终端id
     * @param estCommonActionTcpRequest 请求参数
     * @throws BusinessException 业务异常
     * @return EstCommonActionResponse 返回值
     */
    @ApiAction(OneClientAction.EST_MSG_DETAIL)
    EstCommonActionResponse<BaseMsgDTO> detail(@SessionAlias String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest)
            throws BusinessException;

    /**
     * 快照操作验证
     * @param terminalId terminalId 终端id
     * @param estCommonActionTcpRequest 请求参数
     * @throws BusinessException 业务异常
     * @return EstCommonActionResponse 返回值
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_OPERATION_CONFIRM)
    EstCommonActionResponse checkUserPwd(@SessionAlias String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest)
            throws BusinessException;


    /**
     * 快照数量验证
     * @param terminalId terminalId 终端id
     * @param estCommonActionTcpRequest 请求参数
     * @throws BusinessException 业务异常
     * @return EstCommonActionResponse 返回值
     */
    @ApiAction(OneClientAction.EST_SNAPSHOT_CHECK_NUM)
    EstCommonActionResponse checkNum(@SessionAlias String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest)
            throws BusinessException;


}
