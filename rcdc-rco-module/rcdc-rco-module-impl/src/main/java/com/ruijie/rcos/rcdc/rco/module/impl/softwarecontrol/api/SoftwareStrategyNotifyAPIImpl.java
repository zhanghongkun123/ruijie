package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskInfoSoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopSoftwareNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareStrategyRelatedTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.SoftwareStrategyCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoSoftwareStrategyDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto.SoftwareControlGuestToolMsgContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto.SoftwareControlGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto.SoftwareResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 16:58
 *
 * @author chenl
 */
public class SoftwareStrategyNotifyAPIImpl implements SoftwareStrategyNotifyAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftwareStrategyNotifyAPIImpl.class);

    @Autowired
    private CbbGuestToolMessageAPI guestToolMessageAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private RcoSoftwareStrategyDetailDAO softwareStrategyDetailDAO;

    @Autowired
    private RcoDeskInfoDAO deskInfoDAO;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    /**
     * 每页大小
     */
    private static final int PAGE_SIZE = 20;

    /**
     * 当前页结束标记
     */
    private static final int END_TAG = -1;

    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[]{WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};

    /**
     * 终端通知处理线程池,分配1个线程数
     */
    private static final ThreadExecutor NOTICE_HANDLER_THREAD_POOL =
            ThreadExecutors.newBuilder(SoftwareStrategyNotifyAPIImpl.class.getName()).maxThreadNum(1).queueSize(200).build();


    @Override
    public void notifyAllDesk(List<DeskInfoSoftwareStrategyDTO> deskInfoSoftwareStrategyDTOList) {
        Assert.notNull(deskInfoSoftwareStrategyDTOList, "deskInfoEntityList must not null");

        List<UUID> deskIdList = deskInfoSoftwareStrategyDTOList.stream().map(dto -> dto.getDeskId()).collect(Collectors.toList());
        Map<UUID, DeskInfoSoftwareStrategyDTO> deskInfoDTOMap = new HashMap<>(deskInfoSoftwareStrategyDTOList.size());
        deskInfoSoftwareStrategyDTOList.stream().forEach((dto) -> {
            deskInfoDTOMap.put(dto.getDeskId(), dto);
        });

        // 优化逻辑，每次只取500条，防止入参数组过大
        List<List<UUID>> subDeskIdList = ListRequestHelper.subList(deskIdList, Constants.SOFTWARE_DESK_MAXSIZE);
        for (List<UUID> tempIdList : subDeskIdList) {
            // 查询所有在线云桌面
            List<DesktopSoftwareNotifyDTO> subUserDesktopEntityList =
                    viewDesktopDetailDAO.findListByDeskIds(tempIdList, CbbCloudDeskState.RUNNING, false);
            if (CollectionUtils.isNotEmpty(subUserDesktopEntityList)) {
                // 向在线终端发送策略变更消息
                NOTICE_HANDLER_THREAD_POOL.execute(() -> sentDesk(subUserDesktopEntityList, deskInfoDTOMap));
            }
        }
    }

    /**
     * @param softwareIds 软件ids
     * @throws BusinessException 异常
     */
    @Override
    public void updateSoftwareNotifyDesk(List<UUID> softwareIds) throws BusinessException {
        Assert.notNull(softwareIds, "softwareIds cannot null");
        List<UUID> strategyIdList = softwareStrategyDetailDAO.findStrategyIds(softwareIds, SoftwareStrategyRelatedTypeEnum.SOFTWARE);
        if (!strategyIdList.isEmpty()) {
            SoftwareStrategyCacheManager.deleteCaches(strategyIdList);
            List<RcoDeskInfoEntity> deskInfoEntityList = deskInfoDAO.findBySoftwareStrategyIdIn(strategyIdList);
            if (!deskInfoEntityList.isEmpty()) {
                notifyAllDesk(deskInfoEntityList.stream().map(entity -> new DeskInfoSoftwareStrategyDTO(entity.getDeskId(),
                        entity.getSoftwareStrategyId())).collect(Collectors.toList()));
            }
        }
    }

    /**
     * 通知所有关联了软控策略的桌面
     *
     * @throws BusinessException 异常
     */
    @Override
    public void notifyAllSoftwareStrategyDesk() {
        List<RcoDeskInfoEntity> deskInfoEntityList = deskInfoDAO.findAll();
        if (!deskInfoEntityList.isEmpty()) {
            notifyAllDesk(deskInfoEntityList.stream().map(entity -> new DeskInfoSoftwareStrategyDTO(entity.getDeskId(),
                    entity.getSoftwareStrategyId())).collect(Collectors.toList()));
        }
    }

    /**
     * @param softwareGroupId 软件分组id
     * @throws BusinessException 异常
     */
    @Override
    public void updateSoftwareGroupNotifyDesk(UUID softwareGroupId) {
        Assert.notNull(softwareGroupId, "softwareGroupId cannot null");
        List<UUID> strategyIdList = softwareStrategyDetailDAO.findStrategyIds(Arrays.asList(softwareGroupId), SoftwareStrategyRelatedTypeEnum.GROUP);
        if (!strategyIdList.isEmpty()) {
            SoftwareStrategyCacheManager.deleteCaches(strategyIdList);
            List<RcoDeskInfoEntity> deskInfoEntityList = deskInfoDAO.findBySoftwareStrategyIdIn(strategyIdList);
            if (!deskInfoEntityList.isEmpty()) {
                notifyAllDesk(deskInfoEntityList.stream().map(entity -> new DeskInfoSoftwareStrategyDTO(entity.getDeskId(),
                        entity.getSoftwareStrategyId())).collect(Collectors.toList()));
            }
        }
    }

    /**
     * @param softwareStrategyId 软件策略id
     * @throws BusinessException 异常
     */
    @Override
    public void updateSoftwareStrategyNotifyDesk(UUID softwareStrategyId) {
        Assert.notNull(softwareStrategyId, "softwareStrategyId cannot null");
        SoftwareStrategyCacheManager.deleteCache(softwareStrategyId);
        List<RcoDeskInfoEntity> deskInfoEntityList = deskInfoDAO.findBySoftwareStrategyIdIn(Arrays.asList(softwareStrategyId));
        if (!deskInfoEntityList.isEmpty()) {
            notifyAllDesk(deskInfoEntityList.stream().map(entity -> new DeskInfoSoftwareStrategyDTO(entity.getDeskId(),
                    entity.getSoftwareStrategyId())).collect(Collectors.toList()));
        }
    }


    /**
     * @param viewUserDesktopEntityList
     * @param deskInfoDTOMap
     */
    private void sentDesk(List<DesktopSoftwareNotifyDTO> viewUserDesktopEntityList, Map<UUID, DeskInfoSoftwareStrategyDTO> deskInfoDTOMap) {

        //获取黑白名单全局开关
        Boolean isGlobalSoftwareStrategy = softwareControlMgmtAPI.getGlobalSoftwareStrategy();
        // 逐个向运行中的桌面发送消息
        for (DesktopSoftwareNotifyDTO viewUserDesktopEntity : viewUserDesktopEntityList) {
            UUID deskId = viewUserDesktopEntity.getCbbDesktopId();
            DeskInfoSoftwareStrategyDTO deskInfoSoftwareStrategyDTO = deskInfoDTOMap.get(viewUserDesktopEntity.getCbbDesktopId());
            if (deskInfoSoftwareStrategyDTO == null) {
                continue;
            }

            SoftwareControlGuestToolMsgContentDTO contentDTO = new SoftwareControlGuestToolMsgContentDTO();
            CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
            messageDTO.setDeskId(deskId);
            messageDTO.setCmdId(Integer.parseInt(GuestToolCmdId.RCDC_GT_CMD_ID_SOFTWARE_CONTROL_INFO));
            messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_SOFTWARE_CONTROL_INFO);
            SoftwareControlGuestToolMsgDTO msgDTO = new SoftwareControlGuestToolMsgDTO();
            contentDTO.setPageIndex(NumberUtils.INTEGER_ONE);
            contentDTO.setSoftwareArr(null);
            contentDTO.setBlackSoftwareArr(null);
            contentDTO.setPageSize(NumberUtils.INTEGER_ZERO);
            msgDTO.setContent(contentDTO);

            //获取软件策略
            UUID strategyId = deskInfoSoftwareStrategyDTO.getSoftwareStrategyId();
            SoftwareStrategyDTO softwareStrategyDTO = null;
            if (strategyId != null) {
                try {
                    softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(strategyId);
                } catch (BusinessException e) {
                    LOGGER.info("策略 {} 已经被删除 ", strategyId);
                }
            }

            contentDTO.setRequestId(UUID.randomUUID().toString());
            contentDTO.setIsOpen(isGlobalSoftwareStrategy && (strategyId != null));
            contentDTO.setIsWhiteListMode(Objects.isNull(softwareStrategyDTO) ? true : softwareStrategyDTO.getIsWhitelistMode());
            String strategyVersion = softwareStrategyDTO != null ? softwareStrategyDTO.generateStrategyVersion() : contentDTO.getRequestId();
            contentDTO.setSoftwareStrategyVersion(strategyVersion);
            messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));

            if (contentDTO.getIsOpen()) {
                // 根据策略ID查下属的软件列表
                List<SoftwareDTO> softwareEntityList = SoftwareStrategyCacheManager.get(strategyId);
                if (softwareEntityList == null) {
                    softwareEntityList = softwareControlMgmtAPI.findAllByStrategyId(strategyId);
                    SoftwareStrategyCacheManager.add(strategyId, softwareEntityList);
                }

                if (CollectionUitls.isEmpty(softwareEntityList)) {
                    LOGGER.info("获取软件策略下属的软件列表为空，deskId:{}，strategyId:{} ", deskId, strategyId);
                    messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
                    asyncRequest(messageDTO, deskInfoSoftwareStrategyDTO.getSoftwareStrategyId(),
                            viewUserDesktopEntity.getCbbDesktopId());
                } else {
                    int total = softwareEntityList.size();
                    List<List<SoftwareDTO>> softWareSubList = ListRequestHelper.subList(softwareEntityList, PAGE_SIZE);
                    // 计算总页数
                    int totalPage = softWareSubList.size();
                    contentDTO.setTotal(total);
                    contentDTO.setCount(total);

                    LOGGER.info("根据分页参数截取List分批发送给GT，deskId:{}，总条数:{}，总页数:{} ", deskId, total, totalPage);
                    int currentPage = 1;
                    for (List<SoftwareDTO> tempList : softWareSubList) {
                        contentDTO.setPageIndex(currentPage);
                        contentDTO.setPageSize(tempList.size());
                        //白名单名单则返回白名单清单
                        if (contentDTO.getIsWhiteListMode()) {
                            contentDTO.setSoftwareArr(SoftwareResponseDTO.getWhiteSoftwareResponseDTOArr(tempList));
                        } else {
                            //黑名单则返回黑名单清单
                            contentDTO.setBlackSoftwareArr(SoftwareResponseDTO.getBlackSoftwareResponseDTOArr(tempList));
                        }
                        messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
                        LOGGER.info("根据分页参数截取List分批发送给GT，deskId:{}，正在发送当前页:{} ", deskId, currentPage);
                        asyncRequest(messageDTO, deskInfoSoftwareStrategyDTO.getSoftwareStrategyId(),
                                viewUserDesktopEntity.getCbbDesktopId());
                        currentPage++;
                    }
                }
            } else {
                messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
                // 最后响应当前页结束标记 去除软件信息
                asyncRequest(messageDTO, deskInfoSoftwareStrategyDTO.getSoftwareStrategyId(),
                        viewUserDesktopEntity.getCbbDesktopId());
            }
        }
    }

    /**
     * @param messageDTO
     * @param softwareStrategyId
     * @param cbbDesktopId
     */
    private void asyncRequest(CbbGuesttoolMessageDTO messageDTO, UUID softwareStrategyId, UUID cbbDesktopId) {
        // 异步发送信息
        try {
            guestToolMessageAPI.asyncRequest(messageDTO);
        } catch (Exception e) {
            LOGGER.error("软件管控策略[" + softwareStrategyId + "]变更，发送变更通知到云桌面["
                    + cbbDesktopId + "]失败", e);
        }
    }
}
