package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.QrCodeErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoShineUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserLoginParam;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 移动客户端二维码登录
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-15 13:59
 *
 * @author wanglianyun
 */
@DispatcherImplemetion(ShineAction.MOBILE_CLIENT_QR_START_LOGIN)
public class MobileClientQrStartLoginSPIImpl extends AbstractLoginSPI implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobileClientQrStartLoginSPIImpl.class);

    @Autowired
    private ClientQrCodeAPI clientQrCodeAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private RcoShineUserLoginService rcoShineUserLoginService;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");

        LOGGER.info("收到移动客户端二维码登录信息，请求参数为:{}", JSON.toJSONString(cbbDispatcherRequest.getData()));
        String terminalId = cbbDispatcherRequest.getTerminalId();
        CbbDispatcherDTO dispatcherDTO = new CbbDispatcherDTO();
        BeanUtils.copyProperties(cbbDispatcherRequest, dispatcherDTO);
        Assert.hasText(dispatcherDTO.getData(), "data is not null");

        ClientQrCodeDTO clientQrCodeDTO = null;
        try {
            JSONObject data = JSONObject.parseObject(dispatcherDTO.getData());
            ClientQrCodeReq clientQrCodeReq = new ClientQrCodeReq();
            clientQrCodeReq.setQrCode(data.getString("authCode"));
            clientQrCodeReq.setTerminalId(cbbDispatcherRequest.getTerminalId());
            clientQrCodeDTO = clientQrCodeAPI.qrLogin(clientQrCodeReq);
            LOGGER.info("收到gss返回的扫码登录信息，信息为：{}", JSON.toJSONString(clientQrCodeDTO));
        } catch (BusinessException e) {
            LOGGER.error("请求gss二维码登录发生业务异常", e);
            // FIXME 直接取gss 错误码返回，需要跟客户端再对下，用户不存在、禁用、被锁定，都应该是gss返回，cdc不做这部分逻辑校验
            QrCodeErrorEnum qrCodeErrorEnum = QrCodeErrorEnum.getQrCodeErrorEnum(e.getKey());
            Integer errorCode = qrCodeErrorEnum == null ? Constants.FAILURE : qrCodeErrorEnum.getCode();
            messageHandlerAPI.response(ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, errorCode));
            return;
        } catch (Exception e) {
            LOGGER.error("请求gss二维码登录发生异常", e);
            messageHandlerAPI.response(ShineMessageUtil.buildErrorResponseMessage(cbbDispatcherRequest, Constants.FAILURE));
            return;
        }

        JSONObject userDataJson = JSONObject.parseObject(clientQrCodeDTO.getUserData());
        String userName = userDataJson.getString("userName");
        ShineLoginDTO shineLoginDTO = new ShineLoginDTO();
        shineLoginDTO.setUserName(userName);
        shineLoginDTO.setOtherLoginMethod(true);

        try {
            IacUserDetailDTO userDetailDTO = iacUserMgmtAPI.getUserByName(userName);
            LOGGER.info("移动客户端登录shineLoginDTO数据为：{}", JSON.toJSONString(shineLoginDTO));
            UserLoginParam userLoginParam = new UserLoginParam(cbbDispatcherRequest, shineLoginDTO, userDetailDTO, getLoginBusinessService());
            ShineLoginResponseDTO shineLoginResponseDTO = rcoShineUserLoginService.userLogin(userLoginParam);

            LOGGER.info("移动客户端登录返回为：{}", JSON.toJSONString(shineLoginResponseDTO));
            shineMessageHandler.responseContent(cbbDispatcherRequest, shineLoginResponseDTO.getCode(), shineLoginResponseDTO);
            // 认证成功且应答消息成功，绑定终端
            if (shineLoginResponseDTO.getUserId() != null) {
                try {
                    terminalService.setLoginUserOnTerminal(terminalId, shineLoginResponseDTO.getUserId());
                } catch (Exception e) {
                    LOGGER.error("移动客户端登录用户[" + shineLoginDTO.getUserName() + "]绑定终端[" + terminalId + "]失败", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("移动客户端登录用户[" + shineLoginDTO.getUserName() + "]在终端[" + terminalId + "]登录应答消息失败", e);
            // 应答消息失败，解除session绑定
            userLoginSession.removeLoginUser(terminalId);
        }
    }
}
