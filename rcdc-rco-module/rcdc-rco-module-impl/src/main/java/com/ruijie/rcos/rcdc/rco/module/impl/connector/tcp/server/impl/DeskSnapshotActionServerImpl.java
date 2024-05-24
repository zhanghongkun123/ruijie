package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.task.module.def.dto.msg.BaseMsgDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDeskSnapshotDistributeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.DeskSnapshotActionServer;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionTcpRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilderFactory;
import com.ruijie.rcos.sk.base.batch.InternalBatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/28 10:11
 *
 * @author zdc
 */
public class DeskSnapshotActionServerImpl implements DeskSnapshotActionServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSnapshotActionServerImpl.class);

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private EstCommonActionService estCommonActionService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BatchTaskBuilderFactory batchTaskBuilderFactory;


    @Override
    public EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> snapshotList(String terminalId,
                        EstCommonActionTcpRequest estCommonActionTcpRequest) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(estCommonActionTcpRequest, "estCommonActionTcpRequest must not be null");
        EstCommonActionRequest commonActionReq = JSON.parseObject(estCommonActionTcpRequest.getContent(), EstCommonActionRequest.class);
        Assert.notNull(commonActionReq, "commonActionReq should not be null");
        LOGGER.info("终端{}获取快照列表的入参为{}", terminalId, JSONObject.toJSON(commonActionReq));
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");

        JSONObject data = generateBusinessData(terminalId, commonActionReq);
        EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> response = estCommonActionService.snapshotList(subAction, data);
        return response;
    }

    @Override
    public EstCommonActionResponse create(String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest) throws BusinessException {

        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(estCommonActionTcpRequest, "estCommonActionTcpRequest must not be null");
        EstCommonActionRequest commonActionReq = JSON.parseObject(estCommonActionTcpRequest.getContent(), EstCommonActionRequest.class);
        Assert.notNull(commonActionReq, "commonActionReq should not be null");

        LOGGER.info("终端{}创建快照的入参为{}", terminalId, JSONObject.toJSON(commonActionReq));
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");
        JSONObject data = generateBusinessData(terminalId, commonActionReq);
        UUID deskId = UUID.fromString(data.get("deskId").toString());
        BatchTaskBuilder builder = getBatchTaskBuilder(deskId);
        EstCommonActionResponse estCommonActionResponse = estCommonActionService.create(terminalId, subAction, data, builder);
        return estCommonActionResponse;
    }

    @Override
    public EstCommonActionResponse delete(String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(estCommonActionTcpRequest, "estCommonActionTcpRequest must not be null");
        EstCommonActionRequest commonActionReq = JSON.parseObject(estCommonActionTcpRequest.getContent(), EstCommonActionRequest.class);

        Assert.notNull(commonActionReq, "commonActionReq should not be null");
        LOGGER.info("终端{}删除快照的入参为{}", terminalId, JSONObject.toJSON(commonActionReq));
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");
        JSONObject data = generateBusinessData(terminalId, commonActionReq);
        UUID deskId = UUID.fromString(data.get("deskId").toString());
        BatchTaskBuilder builder = getBatchTaskBuilder(deskId);
        EstCommonActionResponse estCommonActionResponse = estCommonActionService.delete(terminalId, subAction, data, builder);
        return estCommonActionResponse;
    }

    @Override
    public EstCommonActionResponse recover(String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(estCommonActionTcpRequest, "estCommonActionTcpRequest must not be null");
        EstCommonActionRequest commonActionReq = JSON.parseObject(estCommonActionTcpRequest.getContent(), EstCommonActionRequest.class);

        Assert.notNull(commonActionReq, "commonActionReq should not be null");
        LOGGER.info("终端{}还原快照的入参为{}", terminalId, JSONObject.toJSON(commonActionReq));
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");
        JSONObject data = generateBusinessData(terminalId, commonActionReq);
        UUID deskId = UUID.fromString(data.get("deskId").toString());
        BatchTaskBuilder builder = getBatchTaskBuilder(deskId);
        EstCommonActionResponse estCommonActionResponse = estCommonActionService.revert(terminalId, subAction, data, builder);
        return estCommonActionResponse;
    }

    @Override
    public EstCommonActionResponse<BaseMsgDTO> detail(String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest)
            throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(estCommonActionTcpRequest, "estCommonActionTcpRequest must not be null");
        EstCommonActionRequest commonActionReq = JSON.parseObject(estCommonActionTcpRequest.getContent(), EstCommonActionRequest.class);

        Assert.notNull(commonActionReq, "commonActionReq should not be null");
        LOGGER.info("终端{}获取快照详情的入参为{}", terminalId, JSONObject.toJSON(commonActionReq));
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");
        JSONObject data = generateBusinessData(terminalId, commonActionReq);
        EstCommonActionResponse<BaseMsgDTO> estCommonActionResponse = estCommonActionService.detail(subAction, data);
        return estCommonActionResponse;
    }

    @Override
    public EstCommonActionResponse checkUserPwd(String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(estCommonActionTcpRequest, "estCommonActionTcpRequest must not be null");
        EstCommonActionRequest commonActionReq = JSON.parseObject(estCommonActionTcpRequest.getContent(), EstCommonActionRequest.class);

        Assert.notNull(commonActionReq, "commonActionReq should not be null");
        LOGGER.info("终端{}快照操作验证的入参为{}", terminalId, JSONObject.toJSON(commonActionReq));
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");
        JSONObject data = generateBusinessData(terminalId, commonActionReq);
        EstCommonActionResponse estCommonActionResponse = estCommonActionService.checkUserPwd(subAction, data);
        return estCommonActionResponse;
    }

    @Override
    public EstCommonActionResponse checkNum(String terminalId, EstCommonActionTcpRequest estCommonActionTcpRequest) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(estCommonActionTcpRequest, "estCommonActionTcpRequest must not be null");
        EstCommonActionRequest commonActionReq = JSON.parseObject(estCommonActionTcpRequest.getContent(), EstCommonActionRequest.class);

        Assert.notNull(commonActionReq, "commonActionReq should not be null");
        LOGGER.info("终端{}快照校验数量的入参为{}", terminalId, JSONObject.toJSON(commonActionReq));
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");
        JSONObject data = generateBusinessData(terminalId, commonActionReq);
        EstCommonActionResponse<CbbDeskSnapshotDistributeDTO> estCommonActionResponse = estCommonActionService.checkNum(subAction, data);
        return estCommonActionResponse;
    }


    private JSONObject generateBusinessData(String terminalId, EstCommonActionRequest commonActionReq) throws BusinessException {
        // 真正的接口动作 例如 snapshot_list
        JSONObject currentVmData = commonActionReq.getCurrentVmData();
        // 真正的业务参数
        JSONObject data = commonActionReq.getData();
        if (data == null) {
            data = new JSONObject();
        }
        UserInfo terminalUser = userLoginSession.getLoginUserInfo(terminalId);
        Assert.notNull(terminalUser, "terminal user should not be null");
        IacUserDetailDTO cbbUserInfoDTO = cbbUserAPI.getUserByName(terminalUser.getUserName());
        Assert.notNull(cbbUserInfoDTO, "user info not be null");
        UUID deskId = UUID.fromString(currentVmData.get("id").toString());
        data.put("deskId", deskId);
        data.put("userId", cbbUserInfoDTO.getId());
        return data;
    }



    private BatchTaskBuilder getBatchTaskBuilder(UUID deskId) throws BusinessException {
        InternalBatchTaskBuilder builder = (InternalBatchTaskBuilder) batchTaskBuilderFactory.create();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        Assert.notNull(cloudDesktopDetailDTO, "cloudDesktopDetailDTO must not be null");
        builder.setIdentityId(String.valueOf(cloudDesktopDetailDTO.getUserId()));
        Assert.notNull(builder, "builder must not be null");
        return builder;
    }
}
