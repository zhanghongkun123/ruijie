package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.IdvTerminalGroupDeskConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.IdvTerminalGroupDeskConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.IDVTerminalGroupDeskConfigDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.util.CollectionUtils;

/**
 * Description: 获取终端组对应的桌面配置信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/9 11:29
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(ShineAction.RCDC_SHINE_GET_IDV_TERMINAL_GROUP_DESK_CONFIG)
public class GetIDVTerminalGroupDeskConfigHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetIDVTerminalGroupDeskConfigHandlerSPIImpl.class);

    private static final String TERMINAL_GROUP_ID_FIELD_NAME = "terminalGroupId";

    @Autowired
    private IdvTerminalGroupDeskConfigDAO idvTerminalGroupDeskConfigDAO;

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");
        try {
            JSONObject dataJsonObject = JSONObject.parseObject(request.getData());
            String terminalGroupIdString = dataJsonObject.getString(TERMINAL_GROUP_ID_FIELD_NAME);
            Assert.hasText(terminalGroupIdString, "terminalGroupId is blank!");
            UUID terminalGroupId = UUID.fromString(terminalGroupIdString);

            List<IdvTerminalGroupDeskConfigEntity> configList =
                    idvTerminalGroupDeskConfigDAO.findTerminalGroupDeskConfigEntityByCbbTerminalGroupId(terminalGroupId);
            Assert.notNull(configList, "terminal group desk config not found!");
            IDVTerminalGroupDeskConfigDTO configDTO = new IDVTerminalGroupDeskConfigDTO();
            // 集合不为空
            if (!CollectionUtils.isEmpty(configList)) {
                // 获取终端实体
                CbbTerminalBasicInfoDTO terminal = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
                // 根据上报上来的终端的实体实体类型(VOI|IDV) 跟终端组配置的(VOI|IDV)匹配
                for (int i = 0; i < configList.size(); i++) {
                    IdvTerminalGroupDeskConfigEntity idvTerminalGroupDeskConfigEntity = configList.get(i);
                    // 如果平台类型相同 进行赋值拷贝 返回给shine
                    if (idvTerminalGroupDeskConfigEntity.getDeskType().equals(terminal.getTerminalPlatform())) {
                        BeanUtils.copyProperties(idvTerminalGroupDeskConfigEntity, configDTO);
                    }
                }

            }

            CbbResponseShineMessage shineMessage = buildShineMessage(request, CommonMessageCode.SUCCESS);
            shineMessage.setContent(configDTO);
            messageHandlerAPI.response(shineMessage);
        } catch (Exception e) {
            LOGGER.error("获取IDV终端组桌面配置信息失败", e);
            messageHandlerAPI.response(buildShineMessage(request, CommonMessageCode.CODE_ERR_OTHER));
        }
    }

    private CbbResponseShineMessage buildShineMessage(CbbDispatcherRequest request, int code) {
        CbbResponseShineMessage responseMessage = new CbbResponseShineMessage();
        responseMessage.setAction(request.getDispatcherKey());
        responseMessage.setRequestId(request.getRequestId());
        responseMessage.setTerminalId(request.getTerminalId());
        responseMessage.setCode(code);
        return responseMessage;
    }
}
