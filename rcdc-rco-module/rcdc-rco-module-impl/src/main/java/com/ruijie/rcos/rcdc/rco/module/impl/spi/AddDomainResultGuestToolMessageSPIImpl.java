package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_NO_AUTO_JOIN_DOMAIN_INFO;

/**
 * Description: GT上报自动加入AD域结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/28
 *
 * @author lyb
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_AD_RESULT)
public class AddDomainResultGuestToolMessageSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddDomainResultGuestToolMessageSPIImpl.class);

    @Autowired
    private UserDesktopDAO desktopDAO;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");
        LOGGER.info("收到自动加域结果反馈：request={}", JSON.toJSONString(request));
        UUID deskId = request.getDto().getDeskId();
        UserDesktopEntity userDesktopEntity = desktopDAO.findByCbbDesktopId(deskId);

        if (userDesktopEntity == null) {
            throw new BusinessException("不存在cbbDesktopId={}的桌面数据", String.valueOf(deskId));
        }

        CbbGuesttoolMessageDTO requestDto = request.getDto();
        GuesttoolMessageContent body = convertBodyFor(requestDto.getBody());
        Integer code = body.getCode();
        if (code == null) {
            LOGGER.error("无法获取GT上报的加入AD域结果，body:{}", body.toString());
            throw new BusinessException(RCDC_RCO_NO_AUTO_JOIN_DOMAIN_INFO);
        }
        Boolean hasAutoJoinDomain = BooleanUtils.toBoolean(code.intValue(), 0, 1);

        desktopDAO.updateHasAutoJoinDomainByDesktopId(deskId, hasAutoJoinDomain);

        return new CbbGuesttoolMessageDTO();
    }

    /**
     * 将json格式字符串body转对象处理
     *
     * @param bodyStr body字符串
     * @return 转换后的对象
     */
    protected final GuesttoolMessageContent convertBodyFor(String bodyStr) {
        Assert.hasText(bodyStr, "body string is not null");

        return JSON.parseObject(bodyStr, new TypeReference<GuesttoolMessageContent>() {
            //
        });
    }
}
