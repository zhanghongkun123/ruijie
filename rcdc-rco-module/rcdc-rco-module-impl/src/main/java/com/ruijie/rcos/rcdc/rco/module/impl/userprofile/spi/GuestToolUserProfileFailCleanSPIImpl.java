package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileFailCleanRequestDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileFailCleanRequestEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service.UserProfileHelpService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: 接收UPM请求缓存的失败清理请求的额SPI
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/18
 *
 * @author zwf
 */
@DispatcherImplemetion(GuestToolCmdId.NOTIFY_GT_CMD_ID_FAIL_CLEAN)
public class GuestToolUserProfileFailCleanSPIImpl implements CbbGuestToolMessageDispatcherSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolUserProfileFailCleanSPIImpl.class);

    /**
     * JSON转字符串特征,List字段如果为null,输出为[],而非null 字符类型字段如果为null,输出为”“,而非null 数值字段如果为null,输出为0,而非null
     */
    private static final SerializerFeature[] JSON_FEATURES = new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty,
        WriteNullNumberAsZero};

    @Autowired
    private UserProfileFailCleanRequestDAO userProfileFailCleanDAO;

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private UserProfileHelpService userProfileHelpService;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        UUID deskId = request.getDto().getDeskId();
        if (deskId == null) {
            LOGGER.info("来自cmdId={}的请求缺少相应云桌面ID，不进行处理", request.getDto().getPortId());
            return messageDTO;
        }

        LOGGER.info("接收到来自云桌面[{}]请求缓存的清理请求", deskId);
        List<UserProfileFailCleanRequestEntity> failCleanEntityList = userProfileFailCleanDAO.findByDesktopId(deskId);
        //获取之后清理原有数据
        userProfileFailCleanDAO.deleteByDesktopId(deskId);
        if (failCleanEntityList.isEmpty()) {
            LOGGER.info("云桌面[{}]没有缓存的路径清理的请求，不做处理", deskId);
            return messageDTO;
        }

        userProfileHelpService.buildGuestToolMessageForCleanRequest(deskId, messageDTO, failCleanEntityList);

        try {
            guestToolMessageAPI.asyncRequest(messageDTO);
        } catch (BusinessException e) {
            LOGGER.error("云桌面[{}]的清理请求[{}]下发失败，失败原因：", deskId,
                    JSON.toJSONString(messageDTO, JSON_FEATURES),  e);
        }

        return messageDTO;
    }
}
