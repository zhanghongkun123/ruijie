package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbCreateExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.common.message.AbstractRcdcHostMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileBodyMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.OaUserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.OaRequestUserProfileDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache.UserProfileStrategyCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileStrategyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: oa请求个性配置策略
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/7
 *
 * @author linke
 */
@TcpAction(RcoOneAgentToRcdcAction.OA_GET_CDC_USER_PROFILE_STRATEGY)
@TargetHost({HostTypeEnums.THIRD_HOST, HostTypeEnums.CLOUD_DESK})
@Service
public class DesktopOaRequestUserProfileServiceImpl extends AbstractRcdcHostMessageHandler<OaUserProfileStrategyDTO, OaRequestUserProfileDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopOaRequestUserProfileServiceImpl.class);

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private UserProfileStrategyDAO userProfileStrategyDAO;

    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    @Override
    protected OaUserProfileStrategyDTO innerProcessMessage(OaRequestUserProfileDTO oaRequestUserProfileDTO) throws BusinessException {
        LOGGER.info("oa请求个性配置策略,[{}]", JSON.toJSONString(oaRequestUserProfileDTO));
        UUID deskId = oaRequestUserProfileDTO.getHostId();

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        if (cbbDeskDTO.getSessionType() != CbbDesktopSessionType.MULTIPLE) {
            LOGGER.warn("非多会话桌面[{}]直接返回空", deskId);
            return new OaUserProfileStrategyDTO();
        }

        RcoDeskInfoEntity deskInfo = rcoDeskInfoDAO.findByDeskId(deskId);
        if (deskInfo == null) {
            LOGGER.error("无法找到deskId[{}]对应的个性化策略信息，直接跳过，不进行个性化策略的同步", deskId);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_NOT_FOUND_DESKTOP, String.valueOf(deskId));
        }

        UUID strategyId = deskInfo.getUserProfileStrategyId();
        UserProfileStrategyGuestToolMsgDTO strategyGuestToolMsgDTO = null;
        if (Objects.nonNull(strategyId)) {
            strategyGuestToolMsgDTO = UserProfileStrategyCacheManager.get(strategyId);
        }
        // 查询是否存在缓存,策略如果变更会删除缓存信息
        if (Objects.nonNull(strategyGuestToolMsgDTO)) {
            return buildOaUserProfileStrategy(cbbDeskDTO, strategyId, strategyGuestToolMsgDTO);
        }
        strategyGuestToolMsgDTO = new UserProfileStrategyGuestToolMsgDTO();
        if (Objects.isNull(strategyId)) {
            LOGGER.info("通知云桌面[{}],策略变更为空 ", deskId);
            return buildOaUserProfileStrategy(cbbDeskDTO, strategyId, strategyGuestToolMsgDTO);
        }
        List<UserProfilePathDetailDTO> userProfilePathDTOList = userProfileMgmtAPI.getEffectiveUserProfilePath(strategyId);
        if (!CollectionUitls.isEmpty(userProfilePathDTOList)) {
            LOGGER.info("通知云桌面,开始对生效路径列表分组，deskId:{}，strategyId:{} ", deskId, strategyId);
            userProfileMgmtAPI.getGuestToolStrategyPath(strategyGuestToolMsgDTO, userProfilePathDTOList, strategyId);
        }
        UserProfileStrategyCacheManager.add(strategyId, strategyGuestToolMsgDTO);
        return buildOaUserProfileStrategy(cbbDeskDTO, strategyId, strategyGuestToolMsgDTO);
    }

    private OaUserProfileStrategyDTO buildOaUserProfileStrategy(CbbDeskDTO cbbDeskDTO, UUID strategyId, UserProfileStrategyGuestToolMsgDTO msgDTO)
            throws BusinessException {
        OaUserProfileStrategyDTO oaUserProfileStrategyDTO = new OaUserProfileStrategyDTO();
        oaUserProfileStrategyDTO.setPoolId(cbbDeskDTO.getDesktopPoolId());
        oaUserProfileStrategyDTO.setPersonalDataRetention(true);

        UserProfileBodyMessageDTO userProfileBodyMessageDTO = new UserProfileBodyMessageDTO();
        if (Objects.nonNull(msgDTO.getContent())) {
            BeanUtils.copyProperties(msgDTO.getContent(), userProfileBodyMessageDTO);
        }
        if (Objects.isNull(strategyId)) {
            oaUserProfileStrategyDTO.setAppDataPathRedirect(userProfileBodyMessageDTO);
            return oaUserProfileStrategyDTO;
        }

        UserProfileStrategyEntity userProfileStrategy = userProfileStrategyDAO.getOne(strategyId);
        userProfileBodyMessageDTO.setRedirectLocation(userProfileStrategy.getStorageType().name());
        if (userProfileStrategy.getStorageType() == UserProfileStrategyStorageTypeEnum.FILE_SERVER
                && Objects.nonNull(userProfileStrategy.getExternalStorageId())) {
            CbbLocalExternalStorageDTO storageDTO = cbbExternalStorageMgmtAPI.getExternalStorageDetail(userProfileStrategy.getExternalStorageId());
            CbbCreateExternalStorageDTO cbbCreateExternalStorageDTO = new CbbCreateExternalStorageDTO();
            cbbCreateExternalStorageDTO.setName(storageDTO.getName());
            cbbCreateExternalStorageDTO.setDescription(storageDTO.getDescription());
            cbbCreateExternalStorageDTO.setProtocolType(storageDTO.getProtocolType());
            cbbCreateExternalStorageDTO.setServerName(storageDTO.getServerName());
            cbbCreateExternalStorageDTO.setShareName(storageDTO.getShareName());
            cbbCreateExternalStorageDTO.setUserName(storageDTO.getUserName());
            cbbCreateExternalStorageDTO.setPassword(storageDTO.getPassword());
            cbbCreateExternalStorageDTO.setPort(storageDTO.getPort());
            userProfileBodyMessageDTO.setFileServerConfig(cbbCreateExternalStorageDTO);
        }
        oaUserProfileStrategyDTO.setAppDataPathRedirect(userProfileBodyMessageDTO);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("发送给桌面[{}]OA的upm配置[{}]", cbbDeskDTO.getDeskId(), JSON.toJSONString(oaUserProfileStrategyDTO));
        }
        return oaUserProfileStrategyDTO;
    }
}
