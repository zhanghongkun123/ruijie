package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoUserIdentityConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UpdateUserConfigNotifyContentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户身份验证配置类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public class RcoUserIdentityConfigAPIImpl implements RcoUserIdentityConfigAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoUserIdentityConfigAPIImpl.class);

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigMgmtAPI;


    @Override
    public void editUserIdentityConfigNotifyShine(UpdateUserConfigNotifyContentDTO notifyContentDTO) {
        Assert.notNull(notifyContentDTO, "UpdateUserConfigNotifyShineDTO不能为空");

        List<TerminalDTO> terminalDTOList = userTerminalMgmtAPI.listByState(CbbTerminalStateEnums.ONLINE);
        if (CollectionUtils.isEmpty(terminalDTOList)) {
            LOGGER.info("终端集合为空，无需通知");
            return;
        }

        OtpCertificationDTO otpCertification = otpCertificationAPI.getOtpCertification();
        notifyContentDTO.setHasOtpCodeTab(BooleanUtils.isTrue(otpCertification.getHasOtpCodeTab()));
        // IDV和VOI类型是单用户绑定的，所以修改时要通知shine
        terminalDTOList.stream()
            .filter(terminalDTO -> notifyContentDTO.getUserId().equals(terminalDTO.getBindUserId()))//
            .filter(terminalDTO -> terminalDTO.getPlatform() == CbbTerminalPlatformEnums.IDV //
                    || terminalDTO.getPlatform() == CbbTerminalPlatformEnums.VOI)//
            .forEach(terminalDTO -> {
                UpdateUserConfigNotifyContentDTO shineDTO = new UpdateUserConfigNotifyContentDTO();
                BeanUtils.copyProperties(notifyContentDTO, shineDTO);
                    // 用户未绑定动态口令，则动态口令登录为false
                if (BooleanUtils.isNotTrue(terminalDTO.getHasBindOtp())) {
                    LOGGER.info("用户[{}],动态口令绑定状态为[{}]", terminalDTO.getBindUserName(), terminalDTO.getHasBindOtp());
                    shineDTO.setHasOtpCodeTab(false);
                }

                CbbShineMessageRequest shineMessageRequest = CbbShineMessageRequest.create(ShineAction.UPDATE_OTP_CODE, terminalDTO.getId());
                shineMessageRequest.setContent(shineDTO);
                LOGGER.info("发送动态口令给终端[{}],动态口令消息为:[{}]", terminalDTO.getId(), shineDTO);
                try {
                    cbbTranspondMessageHandlerAPI.asyncRequest(shineMessageRequest, new CbbTerminalCallback() {
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
                    LOGGER.error(String.format("向终端:%s下发更新动态口令请求失败", terminalDTO.getId()), e);
                }
            });
    }
}
