package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOneAgentTcpSendAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbCreateExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileBodyMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileRelatedTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.OaUserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.DesktopUserOpTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache.UserProfileStrategyCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileStrategyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileStrategyRelatedDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyRelatedEntity;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: 用户配置策略变更通知云桌面API实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/12
 *
 * @author WuShengQiang
 */
public class UserProfileStrategyNotifyAPIImpl implements UserProfileStrategyNotifyAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileStrategyNotifyAPIImpl.class);

    /**
     * sql查询支持入参为List的最大长度
     */
    private static final int MAX_SQL_NUM = 1000;

    private static final String THREAD_POOL_NAME = "desk-notify-oa-upm-op";

    private static final int MAX_THREAD_NUM = 30;

    private static final int QUEUE_SIZE = 1000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    /**
     * JSON转字符串特征,List字段如果为null,输出为[],而非null 字符类型字段如果为null,输出为”“,而非null 数值字段如果为null,输出为0,而非null
     */
    private static final SerializerFeature[] JSON_FEATURES = new SerializerFeature[] {WriteNullListAsEmpty, WriteNullStringAsEmpty,
        WriteNullNumberAsZero};

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private UserProfileStrategyRelatedDAO rcoUserProfileStrategyRelatedDAO;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private UserProfileStrategyDAO userProfileStrategyDAO;

    @Autowired
    private CbbOneAgentTcpSendAPI cbbOneAgentTcpSendAPI;

    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    @Autowired
    private DesktopUserOpTcpAPI desktopUserOpTcpAPI;

    @Override
    public void updateStrategyNotifyDesk(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId cannot null");

        UserProfileStrategyCacheManager.deleteCache(strategyId);
    }

    @Override
    public void updatePathNotifyDesk(UUID pathId) {
        Assert.notNull(pathId, "pathId cannot null");
        List<UserProfileStrategyRelatedEntity> strategyRelatedEntityList = rcoUserProfileStrategyRelatedDAO.findByRelatedIdAndRelatedType(pathId
                , UserProfileRelatedTypeEnum.PATH);
        List<UUID> strategyIdList =
                strategyRelatedEntityList.stream().map(UserProfileStrategyRelatedEntity::getStrategyId).collect(Collectors.toList());
        if (!strategyIdList.isEmpty()) {
            UserProfileStrategyCacheManager.deleteCaches(strategyIdList);
        }
    }

    @Override
    public void updateDesktopUserProfileStrategy(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId cannot null");

        try {
            pushUserProfileUpdateToRunningDesk(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("向云桌面[{}]同步个性化配置变更失败,失败原因：", desktopId, e);
        }

    }

    @Override
    public List<UUID> getRelatedDesktopIdByUserProfilePath(List<UUID> userProfilePathIdList) {
        Assert.notEmpty(userProfilePathIdList, "userProfilePathIdList must not be null");

        List<UserProfileStrategyRelatedEntity> relatedList = rcoUserProfileStrategyRelatedDAO.findByRelatedIdInAndRelatedType(userProfilePathIdList,
                UserProfileRelatedTypeEnum.PATH);
        List<UUID> strategyIdList = relatedList.stream().map(UserProfileStrategyRelatedEntity::getStrategyId).collect(Collectors.toList());

        List<RcoDeskInfoEntity> deskInfoEntityList = rcoDeskInfoDAO.findByUserProfileStrategyIdIn(strategyIdList);
        return findRunningDesktop(deskInfoEntityList);
    }

    @Override
    public List<UUID> getRelatedDesktopIdByUserProfileStrategy(UUID userProfileStrategyId) {
        Assert.notNull(userProfileStrategyId, "userProfileStrategyId must not be null");

        List<RcoDeskInfoEntity> deskInfoEntityList = rcoDeskInfoDAO.findByUserProfileStrategyId(userProfileStrategyId);
        return findRunningDesktop(deskInfoEntityList);
    }

    private List<UUID> findRunningDesktop(List<RcoDeskInfoEntity> deskInfoEntityList) {
        List<UUID> desktopIdList = new ArrayList<>();
        for (RcoDeskInfoEntity deskInfo : deskInfoEntityList) {
            desktopIdList.add(deskInfo.getDeskId());
        }

        if (desktopIdList.isEmpty()) {
            return desktopIdList;
        }

        List<List<UUID>> desktopIdSubList = ListRequestHelper.subList(desktopIdList, MAX_SQL_NUM);

        List<UUID> runningDesktopIdList = new ArrayList<>();
        for (List<UUID> deskIdSubList : desktopIdSubList) {
            runningDesktopIdList.addAll(cbbDeskMgmtAPI.listDesktopByDeskState(CbbCloudDeskState.RUNNING, deskIdSubList));
        }

        return runningDesktopIdList;
    }

    @Override
    public void pushUserProfileUpdateToRunningDesk(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        RcoDeskInfoEntity deskInfo = rcoDeskInfoDAO.findByDeskId(deskId);
        if (deskInfo == null) {
            LOGGER.error("无法找到deskId[{}]对应的个性化策略信息，直接跳过，不进行个性化策略的同步", deskId);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_NOT_FOUND_DESKTOP, String.valueOf(deskId));
        }

        UUID strategyId = deskInfo.getUserProfileStrategyId();
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(Integer.parseInt(GuestToolCmdId.RCDC_GT_CMD_ID_USER_PROFILE_STRATEGY));
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_PROFILE_STRATEGY);
        UserProfileStrategyGuestToolMsgDTO strategyGuestToolMsgDTO = null;
        if (strategyId != null) {
            strategyGuestToolMsgDTO = UserProfileStrategyCacheManager.get(strategyId);
        }
        // 查询是否存在缓存,策略如果变更会删除缓存信息
        if (Objects.isNull(strategyGuestToolMsgDTO)) {
            strategyGuestToolMsgDTO = new UserProfileStrategyGuestToolMsgDTO();
            if (strategyId == null) {
                LOGGER.info("通知云桌面[{}],策略变更为空 ", deskId);
            } else {
                List<UserProfilePathDetailDTO> userProfilePathDTOList = userProfileMgmtAPI.getEffectiveUserProfilePath(strategyId);
                if (!CollectionUitls.isEmpty(userProfilePathDTOList)) {
                    LOGGER.info("通知云桌面,开始对生效路径列表分组，deskId:{}，strategyId:{} ", deskId, strategyId);
                    userProfileMgmtAPI.getGuestToolStrategyPath(strategyGuestToolMsgDTO, userProfilePathDTOList, strategyId);
                }
                UserProfileStrategyCacheManager.add(strategyId, strategyGuestToolMsgDTO);
            }
        }

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        // 多会话通知给OA
        if (cbbDeskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            pushOaUserProfileStrategy(cbbDeskDTO, strategyId, strategyGuestToolMsgDTO);
            return;
        }

        messageDTO.setBody(JSON.toJSONString(strategyGuestToolMsgDTO, JSON_FEATURES));
        messageDTO.setDeskId(deskId);
        // 异步发送信息
        guestToolMessageAPI.asyncRequest(messageDTO);
    }

    private void pushOaUserProfileStrategy(CbbDeskDTO cbbDeskDTO, UUID strategyId, UserProfileStrategyGuestToolMsgDTO strategyGuestToolMsgDTO)
            throws BusinessException {
        OaUserProfileStrategyDTO oaUserProfileStrategyDTO = new OaUserProfileStrategyDTO();
        oaUserProfileStrategyDTO.setPoolId(cbbDeskDTO.getDesktopPoolId());
        oaUserProfileStrategyDTO.setPersonalDataRetention(true);

        UserProfileBodyMessageDTO userProfileBodyMessageDTO = new UserProfileBodyMessageDTO();
        if (Objects.isNull(strategyId)) {
            oaUserProfileStrategyDTO.setAppDataPathRedirect(userProfileBodyMessageDTO);
            THREAD_EXECUTOR.execute(() -> {
                LOGGER.info("通知OA主机[{}]upm[{}]", cbbDeskDTO.getDeskId(), JSON.toJSONString(oaUserProfileStrategyDTO));
                desktopUserOpTcpAPI.notifyOaDeskUserProfileStrategy(cbbDeskDTO.getDeskId().toString(), oaUserProfileStrategyDTO);
            });
            return;
        }
        if (Objects.nonNull(strategyGuestToolMsgDTO.getContent())) {
            BeanUtils.copyProperties(strategyGuestToolMsgDTO.getContent(), userProfileBodyMessageDTO);
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
        THREAD_EXECUTOR.execute(() -> {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("发送给桌面[{}]OA的upm配置[{}]", cbbDeskDTO.getDeskId(), JSON.toJSONString(oaUserProfileStrategyDTO));
            }
            desktopUserOpTcpAPI.notifyOaDeskUserProfileStrategy(cbbDeskDTO.getDeskId().toString(), oaUserProfileStrategyDTO);
        });
    }
}