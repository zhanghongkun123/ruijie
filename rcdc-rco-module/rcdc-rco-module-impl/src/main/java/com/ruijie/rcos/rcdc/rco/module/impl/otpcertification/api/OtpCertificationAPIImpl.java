package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.api;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacMfaConfigDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.SystemVersionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.OtpCertificationChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.enums.OtpTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.WebClientProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: 动态口令认证策略API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 * 
 * 
 * @author lihengjing
 */
public class OtpCertificationAPIImpl implements OtpCertificationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtpCertificationAPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    SystemVersionMgmtAPI systemVersionMgmtAPI;

    @Autowired
    private WebClientProducerAPI webClientProducerAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityPolicyAPI;

    @Override
    public OtpCertificationDTO getOtpCertification() {
        IacMfaConfigDTO iacMfaConfigDTO = null;
        try {
            iacMfaConfigDTO = iacSecurityPolicyAPI.getMfaConfig(SubSystem.CDC);
        } catch (Exception e) {
            LOGGER.error("getMfaConfig fail", e);
            throw new IllegalStateException("getMfaConfig fail", e);
        }
        OtpCertificationDTO resultOtpDTO = new OtpCertificationDTO();
        resultOtpDTO.setOpenOtp(iacMfaConfigDTO.getEnableMfa());
        resultOtpDTO.setOtpType(OtpTypeEnum.TOTP);
        resultOtpDTO.setPeriod(iacMfaConfigDTO.getPeriod());
        resultOtpDTO.setAlgorithm(iacMfaConfigDTO.getAlgorithm());
        resultOtpDTO.setDigits(iacMfaConfigDTO.getDigits());
        resultOtpDTO.setTimeDifferent(iacMfaConfigDTO.getTimeDifferent());
        resultOtpDTO.setSystemHost(iacMfaConfigDTO.getSystemHost());
        resultOtpDTO.setEnableAdmin(iacMfaConfigDTO.getEnableAdmin());
        // 开启统一登录和辅助认证-不支持动态口令直接登录
        resultOtpDTO.setHasOtpCodeTab(Boolean.TRUE.equals(iacMfaConfigDTO.getEnableMfa()) && Boolean.TRUE.equals(iacMfaConfigDTO.getEnableMfaLogin())
                && rccmManageService.hasOtpCodeTab());

        DtoResponse<String> versionDto = null;
        try {
            versionDto = systemVersionMgmtAPI.obtainSystemReleaseVersion(new DefaultRequest());
        } catch (BusinessException e) {
            LOGGER.error("获取当前版本号异常", e);
        }
        if (versionDto != null) {
            LOGGER.debug("当前版本号为[{}]", versionDto.getDto());
            resultOtpDTO.setSystemName(versionDto.getDto());
        }
        return resultOtpDTO;
    }

    @Override
    public void updateOtpCertification(OtpCertificationDTO otpDTO) {
        Assert.notNull(otpDTO, "otpDTO can not be null");
        LOGGER.info("认证策略配置更新，当前配置内容为[{}]", JSON.toJSONString(otpDTO));

        IacMfaConfigDTO iacMfaConfigDTO = new IacMfaConfigDTO();
        iacMfaConfigDTO.setSubSystem(SubSystem.CDC);
        iacMfaConfigDTO.setEnableMfa(otpDTO.getOpenOtp());
        iacMfaConfigDTO.setEnableMfaLogin(BooleanUtils.toBooleanDefaultIfNull(otpDTO.getHasOtpCodeTab(),false));
        if (otpDTO.getPeriod() != null) {
            iacMfaConfigDTO.setPeriod(otpDTO.getPeriod());
        }
        iacMfaConfigDTO.setSystemHost(otpDTO.getSystemHost());
        iacMfaConfigDTO.setSystemName(otpDTO.getSystemName());
        iacMfaConfigDTO.setEnableAdmin(otpDTO.getEnableAdmin());
        try {
            iacSecurityPolicyAPI.updateMfaConfig(iacMfaConfigDTO);
        } catch (BusinessException e) {
            LOGGER.error("updateOtpCertification fail", e);
            throw new IllegalStateException("updateOtpCertification fail", e);
        }

        // 通知web客户端
        OtpCertificationChangeDTO otpCertificationChangeDTO = new OtpCertificationChangeDTO();

        // 开启统一登录和辅助认证-不支持动态口令直接登录
        otpDTO.setHasOtpCodeTab(otpDTO.getOpenOtp() && otpDTO.getHasOtpCodeTab() && rccmManageService.hasOtpCodeTab());

        BeanUtils.copyProperties(otpDTO, otpCertificationChangeDTO);
        webClientProducerAPI.notifyOtpChange(otpCertificationChangeDTO);

        // 动态口令全局策略发生变更通知shine
        List<TerminalDTO> terminalDTOList = userTerminalMgmtAPI.listByState(CbbTerminalStateEnums.ONLINE);
        if (CollectionUtils.isEmpty(terminalDTOList)) {
            LOGGER.info("终端集合为空，无需通知");
            return;
        }

        for (TerminalDTO terminalDTO : terminalDTOList) {
            OtpCertificationDTO shineDTO = new OtpCertificationDTO();
            BeanUtils.copyProperties(otpDTO, shineDTO);
            // IDV和TCI是单用户绑定需要特殊处理
            if (terminalDTO.getPlatform() == CbbTerminalPlatformEnums.IDV || terminalDTO.getPlatform() == CbbTerminalPlatformEnums.VOI) {
                LOGGER.info("当前终端[{}]类型为[{}]，需要进一步判断", terminalDTO.getId(), terminalDTO.getPlatform());
                // IDV和TCI只有用户开启了动态口令才需要通知
                if (BooleanUtils.isTrue(terminalDTO.getOpenOtpCertification())) {
                    LOGGER.info("用户[{}],动态口令状态为[{}]", terminalDTO.getBindUserName(), terminalDTO.getOpenOtpCertification());
                    // 用户未绑定动态口令，则动态登录为false
                    if (BooleanUtils.isNotTrue(terminalDTO.getHasBindOtp())) {
                        LOGGER.info("用户[{}],动态口令绑定状态为[{}]", terminalDTO.getBindUserName(), terminalDTO.getHasBindOtp());
                        shineDTO.setHasOtpCodeTab(false);
                    }
                    sendShine(terminalDTO.getId(), shineDTO);
                }
                continue;
            }
            sendShine(terminalDTO.getId(), shineDTO);
        }
    }

    private void sendShine(String id, OtpCertificationDTO certificationDTO) {
        CbbShineMessageRequest request = CbbShineMessageRequest.create(ShineAction.UPDATE_OTP_CODE, id);
        request.setContent(certificationDTO);
        LOGGER.info("发送动态口令给终端[{}],动态口令消息为:[{}],request实体为:[{}]", id, certificationDTO, JSON.toJSONString(request));
        try {
            cbbTranspondMessageHandlerAPI.asyncRequest(request, new CbbTerminalCallback() {
                @Override
                public void success(String terminalId, CbbShineMessageResponse response) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");
                    Assert.notNull(response, "response cannot be null!");
                    LOGGER.info("通知终端动态口令变更陈成功，terminalId[{}]，信息[{}]", terminalId, response.toString());
                }

                @Override
                public void timeout(String terminalId) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");
                    LOGGER.error("通知终端动态口令变更超时，terminalId[{}]", terminalId);
                }
            });
        } catch (Exception e) {
            LOGGER.error(String.format("向终端:%s下发更新动态口令请求失败", id), e);
        }
    }
}
