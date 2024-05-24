package com.ruijie.rcos.rcdc.rco.module.impl.est;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDeskSnapshotDistributeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31
 *
 * @author chenl
 */
public interface EstCommonActionService {


    /**
     * 获取快照列表
     *
     * @param data          业务数据
     * @param subAction     真实动作
     * @return PageQueryResponse 快照列表
     * @throws BusinessException 业务异常
     */
    EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> snapshotList(String subAction, JSONObject data)
            throws BusinessException;

    /**
     * Est用户创建快照
     *
     * @param terminalId 终端id
     * @param subAction     真实动作
     * @param data          业务参数
     * @param builder       批任务处理器
     * @return EstCommonActionResponse 返回值
     * @throws BusinessException 业务异常
     */
    EstCommonActionResponse create(String terminalId, String subAction, JSONObject data, BatchTaskBuilder builder)
            throws BusinessException;


    /**
     * Est用户删除快照
     *
     * @param terminalId 终端id
     * @param subAction     真实动作
     * @param data          业务参数
     * @param builder       批处理
     * @return EstCommonActionResponse 返回值
     * @throws BusinessException 业务异常
     */
    EstCommonActionResponse delete(String terminalId, String subAction, JSONObject data, BatchTaskBuilder builder)
            throws BusinessException;


    /**
     * Est用户恢复快照
     *
     * @param terminalId 终端id
     * @param subAction     真实动作
     * @param data          业务参数
     * @param builder       批处理
     * @return EstCommonActionResponse 返回值
     * @throws BusinessException 业务异常
     */
    EstCommonActionResponse revert(String terminalId, String subAction, JSONObject data, BatchTaskBuilder builder)
            throws BusinessException;

    /**
     * 任务详情查询
     *
     * @param data          业务参数
     * @param subAction     真实动作
     * @return EstCommonActionResponse 返回值
     * @throws BusinessException 业务异常
     */
    EstCommonActionResponse<BaseMsgDTO> detail(String subAction, JSONObject data) throws BusinessException;


    /**
     * @param subAction     真实动作
     * @param data data
     * @return EstCommonActionResponse 返回值
     * @throws BusinessException 异常
     */
    EstCommonActionResponse checkUserPwd(String subAction, JSONObject data) throws BusinessException;

    /**
     *
     * @param subAction     est操作
     * @param data          数据
     * @throws BusinessException 异常
     * @return EstCommonActionResponse 返回值
     */
    EstCommonActionResponse<CbbDeskSnapshotDistributeDTO> checkNum(String subAction, JSONObject data)
            throws BusinessException;



}
