package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.spi;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache.UserProfileStrategyCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.ViewRcoUserProfileStrategyRelatedDAO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: GT => RCDC 获取用户配置策略
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_USER_PROFILE_STRATEGY)
public class GuestToolUserProfileStrategySPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolUserProfileStrategySPIImpl.class);

    /**
     * JSON转字符串特征,List字段如果为null,输出为[],而非null 字符类型字段如果为null,输出为”“,而非null 数值字段如果为null,输出为0,而非null
     */
    private static final SerializerFeature[] JSON_FEATURES = new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty,
        WriteNullNumberAsZero};

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private ViewRcoUserProfileStrategyRelatedDAO viewRcoUserProfileStrategyRelatedDAO;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");

        // 1.查询桌面的用户配置策略
        RcoDeskInfoEntity deskInfoEntity = rcoDeskInfoDAO.findByDeskId(deskId);
        if (deskInfoEntity == null || deskInfoEntity.getUserProfileStrategyId() == null) {
            LOGGER.info("云桌面未关联用户配置策略，deskId:{}，deskInfoEntity:{} ", deskId, JSONObject.toJSONString(deskInfoEntity));
            CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
            messageDTO.setDeskId(deskId);
            messageDTO.setCmdId(requestDto.getCmdId());
            messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
            messageDTO.setBody(JSON.toJSONString(new UserProfileStrategyGuestToolMsgDTO(), JSON_FEATURES));
            return messageDTO;
        }

        // 2.根据策略ID查生效的路径列表
        UUID strategyId = deskInfoEntity.getUserProfileStrategyId();
        // 查询是否存在缓存,策略如果变更会删除缓存信息
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(requestDto.getCmdId());
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
        UserProfileStrategyGuestToolMsgDTO userProfileStrategyGuestToolMsgDTO = UserProfileStrategyCacheManager.get(strategyId);
        if (Objects.isNull(userProfileStrategyGuestToolMsgDTO)) {
            List<UserProfilePathDetailDTO> userProfilePathDTOList = userProfileMgmtAPI.getEffectiveUserProfilePath(strategyId);
            userProfileStrategyGuestToolMsgDTO = new UserProfileStrategyGuestToolMsgDTO();
            if (!CollectionUitls.isEmpty(userProfilePathDTOList)) {
                LOGGER.info("获取用户配置策略生效的路径列表长度:{}，deskId:{}，strategyId:{} ", userProfilePathDTOList.size(), deskId, strategyId);
                userProfileMgmtAPI.getGuestToolStrategyPath(userProfileStrategyGuestToolMsgDTO, userProfilePathDTOList, strategyId);
            }
            messageDTO.setBody(JSON.toJSONString(userProfileStrategyGuestToolMsgDTO, JSON_FEATURES));
            UserProfileStrategyCacheManager.add(strategyId, userProfileStrategyGuestToolMsgDTO);
        } else {
            messageDTO = new CbbGuesttoolMessageDTO();
            messageDTO.setCmdId(requestDto.getCmdId());
            messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
            messageDTO.setBody(JSON.toJSONString(userProfileStrategyGuestToolMsgDTO, JSON_FEATURES));
        }

        messageDTO.setDeskId(deskId);
        return messageDTO;
    }
}