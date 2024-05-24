package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 *
 * Description:  用户自助快照 API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月27日
 *
 * @author liusd
 */
public interface UserSnapShotFengBoAPI {

    /**
     * 根据用户与桌面查询用户下快照 列表
     * @param userSnapshotListRequest 快照 查询 条件
     * @return 查询对象
     * @throws BusinessException 业务异常
     */
    UserSnapshotPageQueryResponse list(UserSnapshotListRequest userSnapshotListRequest) throws BusinessException;

    /**
     * 创建用户快照
     * @param userSnapshotCreateRequest 创建对象
     * @return 操作对象
     * @throws BusinessException 业务异常
     */
    UserSnapshotResponse create(UserSnapshotCreateRequest userSnapshotCreateRequest) throws BusinessException;


    /**
     * 删除用户快照
     * @param userSnapshotDeleteRequest 删除对象
     * @return 操作对象
     * @throws BusinessException 业务异常
     */
    UserSnapshotResponse delete(UserSnapshotDeleteRequest userSnapshotDeleteRequest) throws BusinessException;

    /**
     * 获取快照开关
     * @param userSnapshotSwitchRequest 获取开关对象
     * @return 操作对象
     * @throws BusinessException 业务异常
     */
    Boolean getSnapshotSwitch(UserSnapshotSwitchRequest userSnapshotSwitchRequest) throws BusinessException;

    /**
     * 恢复快照
     * @param userSnapshotRevertRequest 恢复对象
     * @return 操作对象
     * @throws BusinessException 业务异常
     */
    UserSnapshotResponse revert(UserSnapshotRevertRequest userSnapshotRevertRequest) throws BusinessException;
}
