package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacQrCodeAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacConfirmQrCodeMobileReqDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacGetQrCodeReqDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeMobileReqDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQrCodeReqDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.qrcode.IacQueryQrCodeReqDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacQrCodeType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientQrCodeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientGetQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrLoginReq;
import com.ruijie.rcos.rcdc.rco.module.impl.qrlogin.service.QrCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.WebClientProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: ClientQrCodeAPI实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 13:15
 *
 * @author wanglianyun
 */
public class ClientQrCodeAPIImpl implements ClientQrCodeAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientQrCodeAPI.class);

    private static final String MOBILE_CLIENT_QR_CODE_BUSINESS = "mobile_client_qr_code_business";

    @Autowired
    private IacQrCodeAPI iacQrCodeAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private WebclientNotifyAPI webclientNotifyAPI;

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;

    @Autowired
    private WebClientProducerAPI webClientProducerAPI;

    @Override
    public ClientQrCodeDTO getQrCode(ClientGetQrCodeReq clientGetQrCodeReq) throws BusinessException {
        Assert.notNull(clientGetQrCodeReq, "clientGetQrCodeReq cannot null");


        IacGetQrCodeReqDTO iacGetQrCodeReqDTO = new IacGetQrCodeReqDTO();
        iacGetQrCodeReqDTO.setClientId(clientGetQrCodeReq.getClientId());
        iacGetQrCodeReqDTO.setQrCodeType(IacQrCodeType.RJ_CLIENT);
        IacQrCodeDTO iacQrCodeDTO = iacQrCodeAPI.getQrCode(iacGetQrCodeReqDTO);
        ClientQrCodeDTO clientQrCodeDTO = new ClientQrCodeDTO();
        BeanUtils.copyProperties(iacQrCodeDTO, clientQrCodeDTO);

        return clientQrCodeDTO;
    }

    @Override
    public ClientQrCodeDTO refreshQrCode(ClientQrCodeReq clientQrCodeReq) throws BusinessException {
        Assert.notNull(clientQrCodeReq, "clientQrCodeReq cannot null");

        IacQrCodeReqDTO iacQrCodeReqDTO = new IacQrCodeReqDTO();
        iacQrCodeReqDTO.setQrCodeType(IacQrCodeType.RJ_CLIENT);
        iacQrCodeReqDTO.setClientId(clientQrCodeReq.getTerminalId());
        iacQrCodeReqDTO.setQrCode(clientQrCodeReq.getQrCode());
        IacQrCodeDTO iacQrCodeDTO = iacQrCodeAPI.refreshQrCode(iacQrCodeReqDTO);
        ClientQrCodeDTO clientQrCodeDTO = new ClientQrCodeDTO();
        BeanUtils.copyProperties(iacQrCodeDTO, clientQrCodeDTO);

        return clientQrCodeDTO;
    }

    @Override
    public ClientQrCodeDTO queryQrCode(ClientQrCodeMobileReq clientQrCodeMobileReq) throws BusinessException {
        Assert.notNull(clientQrCodeMobileReq, "clientQrCodeMobileReq cannot null");

        IacQueryQrCodeReqDTO iacQueryQrCodeReqDTO = new IacQueryQrCodeReqDTO();
        iacQueryQrCodeReqDTO.setQrCode(clientQrCodeMobileReq.getQrCode());
        iacQueryQrCodeReqDTO.setQrCodeType(IacQrCodeType.RJ_CLIENT);
        IacQrCodeDTO iacQrCodeDTO = iacQrCodeAPI.queryQrCode(iacQueryQrCodeReqDTO);
        ClientQrCodeDTO clientQrCodeDTO = new ClientQrCodeDTO();
        BeanUtils.copyProperties(iacQrCodeDTO, clientQrCodeDTO);

        return clientQrCodeDTO;
    }

    @Override
    public void scanQrCode(ClientQrCodeMobileReq clientQrCodeMobileReq) throws BusinessException {
        Assert.notNull(clientQrCodeMobileReq, "clientQrCodeMobileReq cannot null");

        IacQrCodeMobileReqDTO iacQrCodeMobileReqDTO = new IacQrCodeMobileReqDTO();
        iacQrCodeMobileReqDTO.setQrCode(clientQrCodeMobileReq.getQrCode());
        iacQrCodeMobileReqDTO.setQrCodeType(IacQrCodeType.RJ_CLIENT);
        iacQrCodeAPI.scanQrCode(iacQrCodeMobileReqDTO);
    }

    @Override
    public void confirmQrLogin(ClientQrLoginReq clientQrLoginReq) throws BusinessException {
        Assert.notNull(clientQrLoginReq, "clientQrLoginReq cannot null");

        // 用户锁定状态查询
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(clientQrLoginReq.getUserName());

        if (userDetailDTO == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_NOT_EXISTS);
        }
        if (certificationHelper.isLocked(userDetailDTO.getUserName())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_LOCKED);
        }
        if (userDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_USER_DISABLED);
        }

        // 修改状态并绑定用户名
        IacConfirmQrCodeMobileReqDTO iacConfirmQrCodeMobileReqDTO = new IacConfirmQrCodeMobileReqDTO();
        iacConfirmQrCodeMobileReqDTO.setQrCodeType(IacQrCodeType.RJ_CLIENT);
        iacConfirmQrCodeMobileReqDTO.setQrCode(clientQrLoginReq.getQrCode());
        JSONObject userData = new JSONObject();
        userData.put("uuid", userDetailDTO.getId());
        userData.put("userName", userDetailDTO.getUserName());
        userData.put("displayName", userDetailDTO.getRealName());
        userData.put("password", userDetailDTO.getPassword());
        userData.put("userType", userDetailDTO.getUserType());
        iacConfirmQrCodeMobileReqDTO.setUserData(userData.toJSONString());
        iacQrCodeAPI.confirmQrLogin(iacConfirmQrCodeMobileReqDTO);
    }

    @Override
    public void cancelQrLogin(ClientQrCodeMobileReq clientQrCodeMobileReq) throws BusinessException {
        Assert.notNull(clientQrCodeMobileReq, "clientQrCodeMobileReq cannot null");

        IacQrCodeMobileReqDTO iacQrCodeMobileReqDTO = new IacQrCodeMobileReqDTO();
        iacQrCodeMobileReqDTO.setQrCodeType(IacQrCodeType.RJ_CLIENT);
        iacQrCodeMobileReqDTO.setQrCode(clientQrCodeMobileReq.getQrCode());
        iacQrCodeAPI.cancelQrLogin(iacQrCodeMobileReqDTO);
    }

    @Override
    public ClientQrCodeDTO qrLogin(ClientQrCodeReq clientQrCodeReq) throws BusinessException {
        Assert.notNull(clientQrCodeReq, "clientQrCodeReq cannot null");

        IacQrCodeReqDTO iacQrCodeReqDTO = new IacQrCodeReqDTO();
        iacQrCodeReqDTO.setQrCodeType(IacQrCodeType.RJ_CLIENT);
        iacQrCodeReqDTO.setClientId(clientQrCodeReq.getTerminalId());
        iacQrCodeReqDTO.setQrCode(clientQrCodeReq.getQrCode());
        IacQrCodeDTO iacQrCodeDTO = iacQrCodeAPI.qrLogin(iacQrCodeReqDTO);
        ClientQrCodeDTO clientQrCodeDTO = new ClientQrCodeDTO();
        BeanUtils.copyProperties(iacQrCodeDTO, clientQrCodeDTO);

        return clientQrCodeDTO;
    }

    @Override
    public ClientQrCodeConfigDTO getQrCodeConfig(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId cannot null");

        IacQrCodeConfigDTO qrCodeConfig = iacQrCodeAPI.getQrCodeConfig(IacQrCodeType.RJ_CLIENT);
        ClientQrCodeConfigDTO clientQrCodeConfigDTO = new ClientQrCodeConfigDTO();
        BeanUtils.copyProperties(qrCodeConfig, clientQrCodeConfigDTO);
        if (qrCodeConfig.getOpenSwitch() && userLoginRccmOperationService.isUnifiedLoginOn(terminalId)) {
            qrCodeConfig.setOpenSwitch(false);
        }
        return clientQrCodeConfigDTO;
    }

    @Override
    public void updateQrCodeConfig() throws BusinessException {
        IacQrCodeConfigDTO qrCodeConfig = iacQrCodeAPI.getQrCodeConfig(IacQrCodeType.RJ_CLIENT);
        LOGGER.info("移动客户端扫码变更通知客户端信息为：{}", JSON.toJSON(qrCodeConfig));
        ThreadExecutors.execute(MOBILE_CLIENT_QR_CODE_BUSINESS, () -> {
            try {
                qrCodeService.notifyQrConfigUpdateToTerminal(qrCodeConfig);
            } catch (Exception ex) {
                LOGGER.error("移动客户端扫码登录配置发送通知给客户端发生异常，ex:", ex);
            }
        });

        try {
            webClientProducerAPI.notifyQrConfigUpdate(qrCodeConfig);
        } catch (Exception ex) {
            LOGGER.error("通知webclient客户端扫码变更异常", ex);
        }

    }
}
