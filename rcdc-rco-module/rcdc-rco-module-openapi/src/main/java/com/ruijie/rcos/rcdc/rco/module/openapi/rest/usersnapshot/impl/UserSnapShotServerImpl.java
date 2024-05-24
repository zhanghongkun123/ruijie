package com.ruijie.rcos.rcdc.rco.module.openapi.rest.usersnapshot.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.task.module.def.api.BaseMsgMgmtAPI;
import com.ruijie.rcos.base.task.module.def.api.request.msg.BaseGetMsgRequest;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.base.task.module.def.enums.MsgType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserSnapShotFengBoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotTaskStateResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.usersnapshot.UserSnapShotServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 用户快照相关openapi 实现类
 * <p>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-12-27
 *
 * @author liusd
 */
@Service
public class UserSnapShotServerImpl implements UserSnapShotServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSnapShotServerImpl.class);

    @Autowired
    private UserSnapShotFengBoAPI userSnapShotAPI;

    @Autowired
    private BaseMsgMgmtAPI baseMsgMgmtAPI;

    @Override
    public UserSnapshotPageQueryResponse list(UserSnapshotListRequest listRequest) throws BusinessException {
        Assert.notNull(listRequest, "userSnapshotListRequest must not be null");
        LOGGER.info("用户快照列表查询，userSnapshotListRequest：{}", JSON.toJSONString(listRequest));
        UserSnapshotPageQueryResponse response = userSnapShotAPI.list(listRequest);
        return response;
    }

    @Override
    public UserSnapshotTaskStateResponse taskState(UserSnapshotTaskStateRequest stateRequest) throws BusinessException {
        Assert.notNull(stateRequest, "stateRequest must not be null");
        LOGGER.info("用户快照执行状态查询，stateRequest：{}", JSON.toJSONString(stateRequest));
        BaseGetMsgRequest baseGetMsgRequest = new BaseGetMsgRequest();
        baseGetMsgRequest.setMsgRelationId(stateRequest.getTaskId());
        baseGetMsgRequest.setMsgType(MsgType.BATCH_MSG);
        BaseMsgDTO baseMsgDTO = baseMsgMgmtAPI.getMsg(baseGetMsgRequest);
        UserSnapshotTaskStateResponse response = new UserSnapshotTaskStateResponse();
        BeanUtils.copyProperties(baseMsgDTO, response);
        return response;
    }

    @Override
    public Boolean getSnapshotSwitch(UserSnapshotSwitchRequest switchRequest) throws BusinessException {
        Assert.notNull(switchRequest, "switchRequest is not null");
        LOGGER.info("用户快照开关查询，switchRequest：{}", JSON.toJSONString(switchRequest));
        return userSnapShotAPI.getSnapshotSwitch(switchRequest);
    }

    @Override
    public UserSnapshotResponse create(UserSnapshotCreateRequest createRequest) throws BusinessException {
        Assert.notNull(createRequest, "createRequest cannot be null!");
        LOGGER.info("用户快照创建，createRequest：{}", JSON.toJSONString(createRequest));
        return userSnapShotAPI.create(createRequest);
    }

    @Override
    public UserSnapshotResponse delete(UserSnapshotDeleteRequest deleteRequest) throws BusinessException {
        Assert.notNull(deleteRequest, "deleteRequest cannot be null!");
        LOGGER.info("用户快照删除，deleteRequest：{}", JSON.toJSONString(deleteRequest));
        return userSnapShotAPI.delete(deleteRequest);
    }

    @Override
    public UserSnapshotResponse revert(UserSnapshotRevertRequest revertRequest) throws BusinessException {
        Assert.notNull(revertRequest, "revertRequest cannot be null!");
        LOGGER.info("用户快照恢复，deleteRequest：{}", JSON.toJSONString(revertRequest));
        return userSnapShotAPI.revert(revertRequest);
    }

}
