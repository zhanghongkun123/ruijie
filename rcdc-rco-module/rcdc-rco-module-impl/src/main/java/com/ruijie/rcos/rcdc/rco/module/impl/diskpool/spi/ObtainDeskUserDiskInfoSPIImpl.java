package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.AttachDiskMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.GuestToolForDiskStateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.ViewUserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.ViewUserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.service.UserDiskPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: GT获取用户个人磁盘信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/22
 *
 * @author TD
 */
@DispatcherImplemetion(GuestToolCmdId.NOTIFY_GT_CMD_ID_DISK_INFO)
public class ObtainDeskUserDiskInfoSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObtainDeskUserDiskInfoSPIImpl.class);

    @Autowired
    private ViewUserDiskDAO viewUserDiskDAO;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI deskDiskAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private static final int UPM_PORT = 601;

    @Autowired
    private UserDiskPoolService userDiskPoolService;

    @Autowired
    private ViewDesktopDetailDAO userDesktopDAO;

    /**
     * JSON转字符串特征,List字段如果为null,输出为[],而非null 字符类型字段如果为null,输出为”“,而非null 数值字段如果为null,输出为0,而非null
     */
    private static final SerializerFeature[] JSON_FEATURES = new SerializerFeature[]{WriteNullListAsEmpty, WriteNullStringAsEmpty,
        WriteNullNumberAsZero};

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "deskId can not be null");

        if (request.getDto().getPortId() == UPM_PORT) {
            return buildMessageToUpm(request);
        } else {
            return buildMessageToGt(request);
        }
    }

    private CbbGuesttoolMessageDTO buildMessageToGt(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        UUID deskId = request.getDto().getDeskId();
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(Integer.valueOf(GuestToolCmdId.NOTIFY_GT_CMD_ID_DISK_INFO));
        messageDTO.setPortId(GuestToolCmdId.NOTIFY_GT_PORT_ID);
        messageDTO.setDeskId(deskId);
        messageDTO.setBody(JSON.toJSONString(buildGuesttoolMessageContent(deskId)));
        return messageDTO;
    }

    private GuesttoolMessageContent buildGuesttoolMessageContent(UUID deskId) throws BusinessException {
        // 存在池属性盘
        Optional<ViewUserDiskEntity> optional = viewUserDiskDAO.findByDeskId(deskId).stream()
                .filter(entity -> entity.getDiskPoolType() == DiskPoolType.POOL).findFirst();
        if (optional.isPresent()) {
            return buildMessageContent(optional.get());
        }
        //初始化
        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        guesttoolMessageContent.setContent(StringUtils.EMPTY);
        guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.LOSS.getCode());
        guesttoolMessageContent.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_NOT_EXIST_PERSONAL_DISK,
                String.valueOf(deskId)));
        // 桌面未绑定用户/非VDI桌面，直接认定磁盘不存在
        ViewUserDesktopEntity desktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
        if (Objects.isNull(desktopEntity) || Objects.isNull(desktopEntity.getUserId()) 
                || !StringUtils.equals(desktopEntity.getDesktopType(), CbbCloudDeskType.VDI.name())) {
            return guesttoolMessageContent;
        }
        // 用户未关联磁盘/或者当前磁盘处于被其它桌面使用中/桌面和磁盘不在同一个云平台，认定磁盘不能被该桌面使用
        UUID userId = desktopEntity.getUserId();
        ViewUserDiskEntity userDiskEntity = viewUserDiskDAO.findByUserId(userId);
        if (Objects.isNull(userDiskEntity) || Objects.nonNull(userDiskEntity.getDeskId()) 
                || !Objects.equals(userDiskEntity.getPlatformId(), desktopEntity.getPlatformId())) {
            return guesttoolMessageContent;
        }
        // 其它认定磁盘暂时不可用，重新填充信息发给GT
        guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.FAIL.getCode());
        guesttoolMessageContent.setMessage(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_TEMPORARY_UNAVAILABLE));
        return guesttoolMessageContent;
    }

    private CbbGuesttoolMessageDTO buildMessageToUpm(CbbGuestToolSPIReceiveRequest request) {
        UUID deskId = request.getDto().getDeskId();
        String requestBody = getRequestBody(deskId);

        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(Integer.valueOf(GuestToolCmdId.NOTIFY_GT_CMD_ID_DISK_INFO));
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
        messageDTO.setDeskId(deskId);
        messageDTO.setBody(requestBody);

        LOGGER.info("云桌面[{}]的UPMAgent请求挂载磁盘信息，请求所得到内容:{}", deskId, requestBody);

        return messageDTO;
    }

    private String getRequestBody(UUID deskId) {
        GuestToolForDiskStateTypeEnum diskState = userDesktopMgmtAPI.checkDesktopWithPersonalDiskState(deskId);
        if (diskState != GuestToolForDiskStateTypeEnum.IGNORE) {
            return JSON.toJSONString(userDiskPoolService.buildFailMessage(deskId, diskState.getCode())
                    , JSON_FEATURES);
        }

        Optional<ViewUserDiskEntity> optional =
                viewUserDiskDAO.findByDeskId(deskId).stream().filter(entity -> entity.getDiskPoolType() == DiskPoolType.POOL).findFirst();
        if (optional.isPresent()) {
            ViewUserDiskEntity viewUserDiskEntity = optional.get();
            try {
                return JSON.toJSONString(buildMessageContent(viewUserDiskEntity), JSON_FEATURES);
            } catch (BusinessException e) {
                LOGGER.error("对云桌面[{}]磁盘消息[{}]构造失败，失败原因：", deskId, JSON.toJSONString(viewUserDiskEntity), e);
                return JSON.toJSONString(userDiskPoolService.buildFailMessage(deskId, GuesttoolMessageResultTypeEnum.LOSS.getCode()),
                        JSON_FEATURES);
            }
        } else {
            LOGGER.error("找不到云桌面[{}]对应的磁盘信息", deskId);
            return JSON.toJSONString(userDiskPoolService.buildFailMessage(deskId, GuesttoolMessageResultTypeEnum.LOSS.getCode())
                    , JSON_FEATURES);
        }
    }

    private GuesttoolMessageContent buildMessageContent(ViewUserDiskEntity viewUserDiskEntity) throws BusinessException {
        GuesttoolMessageContent guesttoolMessageContent = new GuesttoolMessageContent();
        guesttoolMessageContent.setCode(GuesttoolMessageResultTypeEnum.SUCCESS.getCode());
        guesttoolMessageContent.setMessage(GuesttoolMessageResultTypeEnum.SUCCESS.getMessage());

        CbbDiskPoolDTO diskPoolDetail = cbbDiskPoolMgmtAPI.getDiskPoolDetail(viewUserDiskEntity.getDiskPoolId());
        AttachDiskMessageDTO attachDiskMessageDTO = new AttachDiskMessageDTO();
        BeanUtils.copyProperties(viewUserDiskEntity, attachDiskMessageDTO);
        String diskLetter = diskPoolDetail.getDiskLetter();
        attachDiskMessageDTO.setDiskLetter(Objects.equals(DiskPoolConstants.DEFAULT_LETTER, diskLetter) ? StringUtils.EMPTY : diskLetter);
        attachDiskMessageDTO.setSupportEnumList(userDiskMgmtAPI.getDiskUseApplySupport(viewUserDiskEntity.getDiskId()));
        attachDiskMessageDTO.setDiskNum(deskDiskAPI.listDeskDisk(viewUserDiskEntity.getDeskId()).size());
        attachDiskMessageDTO.setDiskName(LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_DISK_DEFAULT_NAME));
        attachDiskMessageDTO.setCode(DiskPoolConstants.SUCCESS);
        userDiskPoolService.setUserInfoForAttachDiskMessage(attachDiskMessageDTO, viewUserDiskEntity.getUserId());
        // 设置内容
        guesttoolMessageContent.setContent(attachDiskMessageDTO);
        return guesttoolMessageContent;
    }
}
