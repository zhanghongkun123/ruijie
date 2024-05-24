package com.ruijie.rcos.rcdc.rco.module.impl.est.impl;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionDispatchService;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.EstSubActionCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.message.MessageImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionSubResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.UserSnapshotEstHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilderFactory;
import com.ruijie.rcos.sk.base.batch.InternalBatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description: Est subAction分发器
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31 11:42
 *
 * @author lihengjing
 */
@Service
public class EstCommonActionDispatchServiceImpl implements EstCommonActionDispatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EstCommonActionDispatchServiceImpl.class);

    @Autowired
    private UserSnapshotEstHelper userSnapshotEstHelper;

    @Autowired
    private MessageImpl message;

    @Autowired
    private BatchTaskBuilderFactory batchTaskBuilderFactory;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private EstCommonActionNotifyService estCommonActionNotifyService;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Override
    public void dispatchCommonAction(CbbDispatcherDTO dispatcherDTO) {
        Assert.notNull(dispatcherDTO, "CommonActionData must not be null");
        Assert.notNull(dispatcherDTO.getTerminalId(), "terminal should not be null");

        EstCommonActionRequest commonActionReq = JSON.parseObject(dispatcherDTO.getData(), EstCommonActionRequest.class);
        // 真正的接口动作 例如 snapshot_list
        String subAction = commonActionReq.getSubAction();
        Assert.notNull(subAction, "CommonActionData subAction must not be null");
        JSONObject currentVmData = commonActionReq.getCurrentVmData();
        // 真正的业务参数
        JSONObject data = commonActionReq.getData();
        if (data == null) {
            data = new JSONObject();
        }
        UUID deskId = UUID.fromString(currentVmData.get("id").toString());
        data.put("deskId", deskId);
        try {
            UserInfo terminalUser = userLoginSession.getLoginUserInfo(dispatcherDTO.getTerminalId());
            if (terminalUser == null) {
                throw new BusinessException(BusinessKey.RCDC_RCO_USER_SNAPSHOT_LOGIN_ERROR);
            }
            IacUserDetailDTO cbbUserInfoDTO = cbbUserAPI.getUserByName(terminalUser.getUserName());
            data.put("userId", cbbUserInfoDTO.getId());
            BatchTaskBuilder builder;
            switch (subAction) {
                case EstSubActionCode.EST_SNAPSHOT_LIST:
                    userSnapshotEstHelper.list(dispatcherDTO, subAction, data);
                    break;
                case EstSubActionCode.EST_SNAPSHOT_CREATE:
                    builder = getBatchTaskBuilder(deskId);
                    userSnapshotEstHelper.create(dispatcherDTO, subAction, data, builder);
                    break;
                case EstSubActionCode.EST_SNAPSHOT_DELETE:
                    builder = getBatchTaskBuilder(deskId);
                    userSnapshotEstHelper.delete(dispatcherDTO, subAction, data, builder);
                    break;
                case EstSubActionCode.EST_SNAPSHOT_RECOVER:
                    builder = getBatchTaskBuilder(deskId);
                    userSnapshotEstHelper.revert(dispatcherDTO, subAction, data, builder);
                    break;
                case EstSubActionCode.EST_MSG_DETAIL:
                    message.detail(dispatcherDTO, subAction, data);
                    break;
                case EstSubActionCode.EST_SNAPSHOT_OPERATION_CONFIRM:
                    data.put("userName", terminalUser.getUserName());
                    userSnapshotEstHelper.checkUserPwd(dispatcherDTO, subAction, data);
                    break;
                case EstSubActionCode.EST_SNAPSHOT_CHECK_NUM:
                    userSnapshotEstHelper.checkNum(dispatcherDTO, subAction, data);
                    break;
                default:
                    LOGGER.error("dispatchCommonAction not found subAction:" + subAction + "(" + JSON.toJSONString(dispatcherDTO) + ")");
                    estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, "subAction not found");
                    break;
            }
        } catch (BusinessException e) {
            LOGGER.error("（" + subAction + "）操作失败，原因" + e.getMessage() + "，dispatcherDTO:" + JSON.toJSONString(dispatcherDTO), e);
            try {
                responseExceptionToEst(dispatcherDTO.getTerminalId(), subAction, e.getMessage(), deskId);
            } catch (BusinessException ex) {
                LOGGER.error("通知est异常信息发送失败:" + JSON.toJSONString(dispatcherDTO), e);
            }
        } catch (Exception error) {
            LOGGER.error("（" + subAction + "）操作失败(未知异常)，原因" + error.getMessage() + "，dispatcherDTO:"
                    + JSON.toJSONString(dispatcherDTO), error);
            try {
                responseExceptionToEst(dispatcherDTO.getTerminalId(), subAction, "未知异常，请联系云桌面管理员排查", deskId);
            } catch (BusinessException ex) {
                LOGGER.error("通知est异常信息发送失败:" + JSON.toJSONString(dispatcherDTO), error);
            }
        }
    }

    private BatchTaskBuilder getBatchTaskBuilder(UUID deskId) throws BusinessException {
        InternalBatchTaskBuilder builder = (InternalBatchTaskBuilder) batchTaskBuilderFactory.create();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        Assert.notNull(cloudDesktopDetailDTO, "cloudDesktopDetailDTO must not be null");
        builder.setIdentityId(String.valueOf(cloudDesktopDetailDTO.getUserId()));
        Assert.notNull(builder, "builder must not be null");
        return builder;
    }

    private void responseExceptionToEst(String terminalId, String subAction, String message, UUID deskId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(message, "result is need not null");
        Assert.notNull(deskId, "deskId is need not null");

        EstCommonActionResponse<String> response = new EstCommonActionResponse<>();
        response.setSubAction(subAction);
        response.setData(EstCommonActionSubResponse.fail(message));
        response.setDeskId(deskId);

        // 构造Shine透传Est返回对象 并且发起请求
        CbbShineMessageRequest snapshotCreateShineRequest = CbbShineMessageRequest.create(ShineAction.RCDC_TRANSPARENT_EST_COMMON_ACTION, terminalId);
        snapshotCreateShineRequest.setContent(JSON.toJSONString(response, SerializerFeature.WriteDateUseDateFormat));

        cbbTranspondMessageHandlerAPI.asyncRequest(snapshotCreateShineRequest, new CbbTerminalCallback() {
            @Override
            public void success(String terminalId, CbbShineMessageResponse msg) {
                Assert.notNull(terminalId, "terminalId cannot be null!");
                Assert.notNull(msg, "msg cannot be null!");
                LOGGER.info("通知终端用户自定义快照{}请求过程中发生异常 成功，terminalId[{}]，信息[{}]", subAction, terminalId, JSON.toJSONString(msg));
            }

            @Override
            public void timeout(String terminalId) {
                Assert.notNull(terminalId, "terminalId cannot be null!");
                LOGGER.error("通知终端用户自定义快照{}请求过程中发生异常 超时，terminalId[{}]", subAction, terminalId);
            }
        });
    }
}
