package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskActiveStatus;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.GuesttoolObtainBackupDiskSnMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ObtainBackupDiskGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: GT请求挂载蓝屏备份盘
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月16日
 *
 * @author xwx
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_OBTAIN_BACKUP_DISK_SN_CMD_ID)
public class ObtainDesktopBackupDiskSnSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObtainDesktopBackupDiskSnSPIImpl.class);

    private static final String TARGET_CDC = "cdc";

    @Autowired
    private UserDesktopDAO desktopDAO;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cant be null");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("收到GT请求备份磁盘SN消息，request [{}]", JSON.toJSONString(request));
        }
        ObtainBackupDiskGuestToolMsgDTO msgDTO = JSON.parseObject(request.getDto().getBody(), ObtainBackupDiskGuestToolMsgDTO.class);

        //由于CDC和shine都会收到GT的这个消息，因此仅针对发送给CDC的消息进行处理
        if (msgDTO == null || !Objects.equals(msgDTO.getTarget(), TARGET_CDC)) {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("收到GT请求备份磁盘SN消息, target为{},抛弃", JSON.toJSONString(msgDTO));
            }
            throw new BusinessException(BusinessKey.RCDC_RCO_GT_OBTAIN_BACKUP_DISK_SN_FAIL_TARGET_NOT_MATCH);
        }

        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        UUID deskId = request.getDto().getDeskId();
        messageDTO.setDeskId(deskId);
        messageDTO.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_OBTAIN_BACKUP_DISK_SN_CMD_ID));
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_OBTAIN_BACKUP_DISK_SN_PORT_ID);
        UserDesktopEntity userDesktopEntity = desktopDAO.findByCbbDesktopId(deskId);
        if (userDesktopEntity == null) {
            LOGGER.error("未找到桌面ID [{}] 对应的云桌面", deskId);
            return buildFailResponse(messageDTO, LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_GT_OBTAIN_BACKUP_DISK_SN_FAIL_DESKTOP_NOT_EXIST));
        }
        List<CbbDeskDiskDTO> deskDiskDTOList = cbbVDIDeskDiskAPI.findByDeskIdsAndDiskType(Collections.singletonList(deskId), CbbDiskType.BACKUP);
        JSONArray diskArr = new JSONArray();
        if (CollectionUtils.isNotEmpty(deskDiskDTOList)) {
            deskDiskDTOList.forEach(disk -> {
                //只给GT激活的磁盘
                if (disk.getActiveStatus() == CbbDiskActiveStatus.INACTIVE) {
                    return;
                }
                JSONObject diskParam = new JSONObject();
                diskParam.put(Constants.DISK_SN, disk.getDiskSn());
                diskParam.put(Constants.BEFORE_TYPE, disk.getBeforeType());
                diskParam.put(Constants.CREATE_TIME, DateUtil.getDateMinuteFormat(disk.getCreateTime()));
                diskParam.put(Constants.EXPIRE_TIME, DateUtil.getDateMinuteFormat(disk.getExpireTime()));
                diskArr.add(diskParam);
            });
        }
        GuesttoolObtainBackupDiskSnMsgDTO body = new GuesttoolObtainBackupDiskSnMsgDTO();
        body.setCode(CommonMessageCode.SUCCESS);
        body.setContent(diskArr);
        messageDTO.setBody(JSON.toJSONString(body));
        LOGGER.info("返回给云桌面[{}]GT消息内容[{}]", deskId, JSON.toJSONString(messageDTO));
        return messageDTO;
    }


    private CbbGuesttoolMessageDTO buildFailResponse(CbbGuesttoolMessageDTO messageDTO, String message) {
        GuesttoolObtainBackupDiskSnMsgDTO body = new GuesttoolObtainBackupDiskSnMsgDTO();
        body.setCode(CommonMessageCode.FAIL);
        body.setMessage(message);

        messageDTO.setBody(JSON.toJSONString(body));
        return messageDTO;
    }
}
