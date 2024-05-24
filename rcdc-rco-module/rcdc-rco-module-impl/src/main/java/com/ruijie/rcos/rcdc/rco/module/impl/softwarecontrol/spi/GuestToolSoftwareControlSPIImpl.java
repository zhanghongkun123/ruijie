package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.base.sysmanage.module.def.common.Constant;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.VMChannelAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.guesttool.GuesttoolChannelRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.HciGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.SoftwareStrategyRunTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.SoftwareStrategyCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto.SoftwareControlGuestToolMsgContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto.SoftwareControlGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto.SoftwareResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * Description: GT请求RCDC获取软件策略信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author wuShengQiang
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_SOFTWARE_CONTROL_INFO)
public class GuestToolSoftwareControlSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolSoftwareControlSPIImpl.class);

    /**
     * 每页大小
     */
    private static final int PAGE_SIZE = 20;

    /**
     * 当前页结束标记
     */
    private static final int END_TAG = -1;

    private static final String VERSION_EQUAL_TAG = "-1";

    /**
     * JSON转字符串特征
     */
    private static final SerializerFeature[] JSON_FEATURES =
            new SerializerFeature[]{WriteNullListAsEmpty, WriteNullStringAsEmpty, WriteNullNumberAsZero};

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private CbbGuestToolMessageAPI cbbGuestToolMessageAPI;

    @Autowired
    private VMChannelAPI guestToolChannelAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {

        Assert.notNull(request, "request can not be null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        Assert.notNull(requestDto, "requestDto can not be null");
        UUID deskId = requestDto.getDeskId();
        Assert.notNull(deskId, "deskId can not be null");
        Integer cmdId = requestDto.getCmdId();
        LOGGER.info("软件白名单gt消息接收： {}", JSONObject.toJSONString(request));

        SoftwareControlGuestToolMsgDTO guestToolMsgDTO = parseGuestToolMsg(requestDto.getBody(), SoftwareControlGuestToolMsgDTO.class);
        SoftwareControlGuestToolMsgContentDTO contentDTO = guestToolMsgDTO.getContent();
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setDeskId(deskId);
        messageDTO.setCmdId(cmdId);
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_SOFTWARE_CONTROL_INFO);
        SoftwareControlGuestToolMsgDTO msgDTO = new SoftwareControlGuestToolMsgDTO();
        contentDTO.setPageIndex(NumberUtils.INTEGER_ONE);
        contentDTO.setSoftwareArr(null);
        contentDTO.setBlackSoftwareArr(null);
        contentDTO.setPageSize(NumberUtils.INTEGER_ZERO);
        contentDTO.setRequestId(UUID.randomUUID().toString());
        msgDTO.setContent(contentDTO);
        messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));

        SoftwareStrategyRunTypeEnum runType = SoftwareStrategyRunTypeEnum.getByCode(contentDTO.getRunType());
        Assert.notNull(runType, "runType不能为NULL");

        List<SoftwareDTO> softwareEntityList;
        //GT重启场景下，需同步对应的白名单策略
        Boolean isGlobalSoftwareStrategy = softwareControlMgmtAPI.getGlobalSoftwareStrategy();
        if (SoftwareStrategyRunTypeEnum.USER == runType) {
            //获取黑白名单全局开关
            RcoDeskInfoEntity deskInfoEntity = rcoDeskInfoDAO.findByDeskId(deskId);
            if (!isGlobalSoftwareStrategy || deskInfoEntity == null || deskInfoEntity.getSoftwareStrategyId() == null) {
                contentDTO.setIsOpen(false);
                LOGGER.info("云桌面未关联软件策略，deskId:{}，deskInfoEntity:{} ", deskId, JSONObject.toJSONString(deskInfoEntity));
                contentDTO.setSoftwareStrategyVersion(contentDTO.getRequestId());
                messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
                return messageDTO;
            }

            UUID strategyId = deskInfoEntity.getSoftwareStrategyId();
            contentDTO.setIsOpen(strategyId != null ? true : false);
            SoftwareStrategyDTO softwareStrategyDTO = softwareControlMgmtAPI.findSoftwareStrategyById(strategyId);
            contentDTO.setIsWhiteListMode(softwareStrategyDTO.getIsWhitelistMode());

            String shineSoftwareStrategyVersion = contentDTO.getSoftwareStrategyVersion();
            String serverVersion = softwareStrategyDTO.getId() + Constants.UNDERLINE + softwareStrategyDTO.getVersion();
            if (serverVersion.equals(shineSoftwareStrategyVersion)) {
                LOGGER.info("gt或者shine的版本 [{}] 跟服务器的软控策略版本 [{}] 一致", shineSoftwareStrategyVersion, serverVersion);
                contentDTO.setSoftwareStrategyVersion(VERSION_EQUAL_TAG);
                messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
                return messageDTO;
            }

            contentDTO.setSoftwareStrategyVersion(serverVersion);
            // 根据策略ID查下属的软件列表
            softwareEntityList = SoftwareStrategyCacheManager.get(strategyId);
            if (softwareEntityList == null) {
                softwareEntityList = softwareControlMgmtAPI.findAllByStrategyId(strategyId);
                SoftwareStrategyCacheManager.add(strategyId, softwareEntityList);
            }
            if (CollectionUitls.isEmpty(softwareEntityList)) {
                LOGGER.info("获取软件策略下属的软件列表为空，deskId:{}，strategyId:{} ", deskId, strategyId);
                messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
                return messageDTO;
            }
        } else {
            //镜像编辑场景下，返回所有的白名单
            contentDTO.setIsOpen(isGlobalSoftwareStrategy);
            //跟GT确认过，镜像虚机只要给出白名单列表
            contentDTO.setIsWhiteListMode(true);
            softwareEntityList = softwareControlMgmtAPI.findAllSoftwareForGT();
            if (CollectionUitls.isEmpty(softwareEntityList)) {
                LOGGER.info("编辑镜像获取所有软件列表为空");
                messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
                return messageDTO;
            }
        }

        int total = softwareEntityList.size();
        List<List<SoftwareDTO>> softWareSubList = ListRequestHelper.subList(softwareEntityList, PAGE_SIZE);
        // 计算总页数
        int totalPage = softWareSubList.size();

        contentDTO.setTotal(total);
        contentDTO.setCount(total);
        LOGGER.info("根据分页参数截取List分批发送给GT/镜像，deskId:{}，总条数:{}，总页数:{} ", deskId, total, totalPage);

        int currentPage = 1;
        for (List<SoftwareDTO> tempList : softWareSubList) {
            contentDTO.setPageIndex(currentPage);
            contentDTO.setPageSize(tempList.size());
            //白名单名单则返回黑名单清单
            if (contentDTO.getIsWhiteListMode()) {
                contentDTO.setSoftwareArr(SoftwareResponseDTO.getWhiteSoftwareResponseDTOArr(tempList));
            } else {
                //黑名单则返回黑名单清单
                contentDTO.setBlackSoftwareArr(SoftwareResponseDTO.getBlackSoftwareResponseDTOArr(tempList));
            }

            messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
            asyncRequest(runType, messageDTO, deskId);
            LOGGER.info("根据分页参数截取List分批发送给GT/镜像，deskId:{}，正在发送当前页:{} , 类型：{}", deskId, currentPage, runType);
            currentPage++;
        }

        LOGGER.info("根据分页参数截取List分批发送给GT/镜像结束，deskId:{}，发送次数:{} ", deskId, currentPage);
        // 最后响应当前页结束标记 去除软件信息
        contentDTO.setPageIndex(END_TAG);
        contentDTO.setSoftwareArr(null);
        contentDTO.setBlackSoftwareArr(null);
        contentDTO.setPageSize(NumberUtils.INTEGER_ZERO);
        messageDTO.setBody(JSON.toJSONString(msgDTO, JSON_FEATURES));
        return messageDTO;
    }

    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("guest tool报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }

    /**
     *
     * @param runType
     * @param messageDTO
     * @param cbbDesktopId
     */
    private void asyncRequest(SoftwareStrategyRunTypeEnum runType, CbbGuesttoolMessageDTO messageDTO, UUID cbbDesktopId) {
        try {
            if (SoftwareStrategyRunTypeEnum.USER == runType) {
                cbbGuestToolMessageAPI.asyncRequest(messageDTO);
            } else {
                guestToolChannelAPI.asyncRequest(new GuesttoolChannelRequest(covertToHciMessageDTO(messageDTO)));
            }
        } catch (BusinessException e) {
            LOGGER.error("云桌面["  + cbbDesktopId + "]获取软件管控策略失败", e);
        }
    }

    /**
     *
     * @param cbbMessageDTO
     * @return
     */
    private HciGuesttoolMessageDTO covertToHciMessageDTO(CbbGuesttoolMessageDTO cbbMessageDTO) {
        HciGuesttoolMessageDTO hciMessageDTO = new HciGuesttoolMessageDTO();
        BeanUtils.copyProperties(cbbMessageDTO, hciMessageDTO);
        return hciMessageDTO;
    }

}
