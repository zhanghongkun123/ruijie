package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.spi;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileFailCleanRequestDAO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: 接收UPM的反馈应答
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/02
 *
 * @author zwf
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_ACK_FROM_UPM)
public class GuestToolUserProfileReceiveAckSPIImpl implements CbbGuestToolMessageDispatcherSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolUserProfileFailCleanSPIImpl.class);

    /**
     * JSON转字符串特征,List字段如果为null,输出为[],而非null 字符类型字段如果为null,输出为”“,而非null 数值字段如果为null,输出为0,而非null
     */
    private static final SerializerFeature[] JSON_FEATURES = new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty,
        WriteNullNumberAsZero};

    @Autowired
    private UserProfileFailCleanRequestDAO userProfileFailCleanDAO;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        UUID deskId = request.getDto().getDeskId();
        if (deskId == null) {
            LOGGER.error("来自cmdId={}的请求缺少相应云桌面ID，不进行处理", request.getDto().getPortId());
            return messageDTO;
        }

        LOGGER.info("接收到来自云桌面[{}]清理配置数据的应答，清除相关记录", deskId);
        //清除相关记录
        userProfileFailCleanDAO.deleteByDesktopId(deskId);
        return messageDTO;
    }
}
