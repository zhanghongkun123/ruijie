package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateIDVDeskStrategyIdRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDeskComputerNameConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ChangeDeskVmTypeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.message.MessageUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 切换成对应云桌面策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/11 14:50
 *
 * @author chenli
 */
@DispatcherImplemetion(ShineAction.CHANGE_DESK_VM_TYPE)
public class ChangeDeskVmTypeSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeDeskVmTypeSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    private final static Integer STRATEGY_NAME_MAX_LENGTH = 36;


    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "cbbDispatcherRequest is not null");
        Assert.notNull(cbbDispatcherRequest.getData(), "data is not null");
        LOGGER.info("接收到切换成对应云桌面策略请求，终端id:{}", cbbDispatcherRequest.getTerminalId());

        ChangeDeskVmTypeDTO changeDeskVmTypeDTO = JSONObject.parseObject(cbbDispatcherRequest.getData(), ChangeDeskVmTypeDTO.class);
        try {
            CbbDeskDTO cbbDeskDTO = cbbIDVDeskMgmtAPI.getDeskIDV(changeDeskVmTypeDTO.getDeskId());
            CbbDeskStrategyIDVDTO cbbDeskStrategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(cbbDeskDTO.getStrategyId());
            //如果不一致则创建一个新的云桌面策略
            if (cbbDeskStrategyIDVDTO.getPattern() != changeDeskVmTypeDTO.getVmType()) {

                //创建新的云桌面策略
                CbbDeskStrategyIDVDTO createCbbDeskStrategyIDVDTO = new CbbDeskStrategyIDVDTO();
                BeanUtils.copyProperties(cbbDeskStrategyIDVDTO, createCbbDeskStrategyIDVDTO);
                createCbbDeskStrategyIDVDTO.setPattern(changeDeskVmTypeDTO.getVmType());
                createCbbDeskStrategyIDVDTO.setName(resetStrategyName(createCbbDeskStrategyIDVDTO.getName()));
                createCbbDeskStrategyIDVDTO.setId(null);
                UUID dekStrategyIDVID = cbbIDVDeskStrategyMgmtAPI.createDeskStrategyIDV(createCbbDeskStrategyIDVDTO);

                //创建云桌面计算机名
                String computerName = cloudDeskComputerNameConfigAPI.findCloudDeskComputerName(dekStrategyIDVID);
                cloudDeskComputerNameConfigAPI.createCloudDeskComputerNameConfig(computerName, dekStrategyIDVID);

                //更新桌面的云桌面策略
                cbbIDVDeskMgmtAPI.updateDeskStrategyIDV(new CbbUpdateIDVDeskStrategyIdRequest(cbbDeskDTO.getDeskId(), dekStrategyIDVID));
            }
        } catch (BusinessException e) {
            LOGGER.error("接收到切换成对应云桌面策略请求异常", e);
            messageHandlerAPI.response(MessageUtils.buildErrorResponseMessage(cbbDispatcherRequest));
        }
        messageHandlerAPI.response(MessageUtils.buildResponseMessage(cbbDispatcherRequest, new Object()));
    }

    /**
     *
     * @param oldStrategyName
     * @return
     */
    private String resetStrategyName(String oldStrategyName) {
        String newStrategyName;
        if (oldStrategyName.indexOf(Constants.UNDERLINE) > -1) {
            newStrategyName = StringUtils.substring(oldStrategyName, 0, oldStrategyName.lastIndexOf(Constants.UNDERLINE) + 1);
        } else {
            newStrategyName = oldStrategyName + Constants.UNDERLINE;
        }
        newStrategyName = newStrategyName + systemBusinessMappingAPI.obtainMappingSequenceVal();

        // 超过36长度，则直接截断
        if (newStrategyName.length() > STRATEGY_NAME_MAX_LENGTH) {
            newStrategyName = StringUtils.substring(oldStrategyName, 0, STRATEGY_NAME_MAX_LENGTH);
        }
        return newStrategyName;
    }
}
