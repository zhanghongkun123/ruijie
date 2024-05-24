package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileCleanGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileFailCleanRequestEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.service.UserProfileHelpService;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: UPM辅助工作类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年09月05日
 *
 * @author zwf
 */
@Service
public class UserProfileHelpServiceImpl implements UserProfileHelpService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileHelpServiceImpl.class);

    /**
     * JSON转字符串特征,List字段如果为null,输出为[],而非null 字符类型字段如果为null,输出为”“,而非null 数值字段如果为null,输出为0,而非null
     */
    private static final SerializerFeature[] JSON_FEATURES = new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty,
        WriteNullNumberAsZero};

    @Override
    public void buildGuestToolMessageForCleanRequest(UUID desktopId, CbbGuesttoolMessageDTO messageDTO,
                                                     List<UserProfileFailCleanRequestEntity> failCleanEntityList) {
        Assert.notNull(desktopId, "desktopId must not be null");
        Assert.notNull(messageDTO, "messageDTO must not be null");
        Assert.notEmpty(failCleanEntityList, "failCleanEntityList must not be null");

        UserProfileCleanGuestToolMsgDTO guestToolMsgDTO = buildCleanGuestToolMsgDTO(failCleanEntityList);

        messageDTO.setCmdId(GuestToolCmdId.NOTIFY_GT_CMD_ID_CLEAN_USER_PROFILE_PATH);
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
        messageDTO.setDeskId(desktopId);
        messageDTO.setBody(JSON.toJSONString(guestToolMsgDTO, JSON_FEATURES));
    }

    @Override
    public UserProfileCleanGuestToolMsgDTO buildCleanGuestToolMsgDTO(List<UserProfileFailCleanRequestEntity> failCleanEntityList) {
        Assert.notEmpty(failCleanEntityList, "failCleanEntityList must not be null");

        UserProfileCleanGuestToolMsgDTO guestToolMsgDTO = new UserProfileCleanGuestToolMsgDTO();
        UserProfileCleanGuestToolMsgDTO.BodyMessage bodyMessage = new UserProfileCleanGuestToolMsgDTO.BodyMessage();

        List<String> configDirList = new ArrayList<>();
        List<String> configFileList = new ArrayList<>();
        List<String> configKeyList = new ArrayList<>();
        List<String> configValueList = new ArrayList<>();
        for (UserProfileFailCleanRequestEntity failClean : failCleanEntityList) {
            UserProfilePathTypeEnum type = failClean.getType();
            String path = failClean.getPath();

            switch (type) {
                case FOLDER:
                    configDirList.add(path);
                    break;
                case DOCUMENT:
                    configFileList.add(path);
                    break;
                case REGISTRY_KEY:
                    configKeyList.add(path);
                    break;
                case REGISTRY_VALUE:
                    configValueList.add(path);
                    break;
                default:
                    LOGGER.error("未知的路径类型，路径DTO:{}", JSON.toJSONString(failClean));
                    break;
            }
        }
        bodyMessage.setConfigDirList(configDirList);
        bodyMessage.setConfigFileList(configFileList);
        bodyMessage.setConfigKeyList(configKeyList);
        bodyMessage.setConfigValueList(configValueList);
        String contentString = JSON.toJSONString(bodyMessage, JSON_FEATURES);
        String md5 = Md5Builder.computeTextMd5(contentString);
        bodyMessage.setMd5(md5);
        guestToolMsgDTO.setContent(bodyMessage);
        LOGGER.info("将要被清理的路径：{}", JSONObject.toJSONString(guestToolMsgDTO.getContent()));

        return guestToolMsgDTO;
    }
}
