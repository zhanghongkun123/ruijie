package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.validation;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDesktopDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description: 云应用策略入参校验
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/5
 *
 * @author WuShengQiang
 */
@Service
public class RcaMainStrategyValidation {

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    /**
     * 创建云应用策略入参校验
     *
     * @param request web入参
     * @throws BusinessException 业务异常
     */
    public void createRcaMainStrategyValidate(RcaMainStrategyDTO request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        RcaMainStrategyDesktopDTO deskStrategyConfig = request.getDesktopStrategyConfig();
        if (Objects.isNull(deskStrategyConfig)) {
            return;
        }
        agreementConfigValidate(request.getHostSessionType(), request.getHostSourceType(), deskStrategyConfig);
        // 剪贴板校验
        clipBoardArrValidate(deskStrategyConfig);
    }

    /**
     * 更新云应用策略入参校验
     *
     * @param request web入参
     * @throws BusinessException 业务异常
     */
    public void updateRcaMainStrategyValidate(RcaMainStrategyDTO request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        RcaMainStrategyDesktopDTO deskStrategyConfig = request.getDesktopStrategyConfig();
        if (Objects.isNull(deskStrategyConfig)) {
            return;
        }
        agreementConfigValidate(request.getHostSessionType(), request.getHostSourceType(), deskStrategyConfig);
        // 剪贴板校验
        clipBoardArrValidate(deskStrategyConfig);
    }

    private void agreementConfigValidate(RcaEnum.HostSessionType sessionType, RcaEnum.HostSourceType sourceType,
                                         RcaMainStrategyDesktopDTO deskStrategyConfig) throws BusinessException {
        CbbEstProtocolType estProtocolType = deskStrategyConfig.getEstProtocolType();
        // 第三方和多会话不支持EST
        if (CbbEstProtocolType.EST == estProtocolType &&
                (RcaEnum.HostSessionType.MULTIPLE == sessionType || RcaEnum.HostSourceType.THIRD_PARTY == sourceType)) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_AGREEMENT_NOT_SUPPORTED_SESSION_TYPE);
        }
        deskStrategyAPI.agreementConfigValidate(estProtocolType, deskStrategyConfig.getAgreementInfo());
    }

    private void clipBoardArrValidate(RcaMainStrategyDesktopDTO deskStrategyConfig) throws BusinessException {
        // 根据新配置ClipBoardSupportTypeArr确定clipboardmode字段（兼容旧客户端）
        CbbClipboardMode mode = deskStrategyAPI.clipBoardArrValidate(deskStrategyConfig.getClipBoardSupportTypeArr(), false);
        deskStrategyConfig.setClipBoardMode(mode);
    }
}
