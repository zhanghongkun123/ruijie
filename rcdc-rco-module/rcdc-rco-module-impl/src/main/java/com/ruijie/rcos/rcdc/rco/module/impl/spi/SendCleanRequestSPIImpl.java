package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbSendCleanRequestSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileFailCleanRequestDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileFailCleanRequestEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service.UserProfileHelpService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: 通知RCO在热迁移之后继续执行UPM操作
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年09月05日
 *
 * @author zwf
 */
public class SendCleanRequestSPIImpl implements CbbSendCleanRequestSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(SendCleanRequestSPIImpl.class);

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
    public void sendCleanRequestAfterVmMigrate(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId must not be null");

        LOGGER.info("云桌面[{}]热迁移完毕，准备发送清理请求", desktopId);

        List<UserProfileFailCleanRequestEntity> failCleanEntityList = userProfileFailCleanDAO.findByDesktopId(desktopId);
        //获取之后清理原有数据
        userProfileFailCleanDAO.deleteByDesktopId(desktopId);
        if (failCleanEntityList.isEmpty()) {
            LOGGER.info("云桌面[{}]没有缓存的路径清理的请求，不做处理", desktopId);
            return;
        }

        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        userProfileHelpService.buildGuestToolMessageForCleanRequest(desktopId, messageDTO, failCleanEntityList);

        try {
            guestToolMessageAPI.asyncRequest(messageDTO);
        } catch (BusinessException e) {
            LOGGER.error("云桌面[{}]的清理请求[{}]下发失败，失败原因：", desktopId,
                    JSON.toJSONString(messageDTO, JSON_FEATURES),  e);
        }
    }
}
