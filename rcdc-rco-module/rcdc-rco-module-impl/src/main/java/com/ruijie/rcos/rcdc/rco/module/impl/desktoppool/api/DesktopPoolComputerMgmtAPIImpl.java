package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolComputerMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.ViewDesktopPoolComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolComputerService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.impl.DesktopPoolComputerListServiceImpl;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 桌面池与PC终端相关API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public class DesktopPoolComputerMgmtAPIImpl implements DesktopPoolComputerMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolComputerMgmtAPIImpl.class);

    @Autowired
    private DesktopPoolComputerService desktopPoolComputerService;

    @Autowired
    private DesktopPoolComputerListServiceImpl desktopPoolComputerListService;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Override
    public List<DesktopPoolComputerDTO> listDeskPoolComputerByRelatedType(ComputerRelatedType computerRelatedType) {
        Assert.notNull(computerRelatedType, "computerRelatedType can not be null");
        return desktopPoolComputerService.listDeskPoolComputerByRelatedType(computerRelatedType);
    }

    @Override
    public Set<String> getDesktopPoolRelationTerminalGroup(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolComputerService.getDesktopPoolRelationComputerGroup(desktopPoolId);
    }

    @Override
    public DefaultPageResponse<ComputerDTO> pageQueryRealBindComputer(UUID desktopPoolId, PageSearchRequest pageRequest) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(pageRequest, "pageRequest can not be null");
        Page<ViewDesktopPoolComputerEntity> page = desktopPoolComputerListService.pageQuery(pageRequest,
                ViewDesktopPoolComputerEntity.class);
        DefaultPageResponse<ComputerDTO> response = new DefaultPageResponse<>();
        response.setTotal(page.getTotalElements());

        if (CollectionUtils.isEmpty(page.getContent())) {
            response.setItemArr(new ComputerDTO[0]);
            return response;
        }
        List<ComputerDTO> computerDTOList = new ArrayList<>();
        for (ViewDesktopPoolComputerEntity viewDesktopPoolComputerEntity : page.getContent()) {
            ComputerDTO computerDTO = new ComputerDTO();
            BeanUtils.copyProperties(viewDesktopPoolComputerEntity, computerDTO);
            computerDTO.setId(viewDesktopPoolComputerEntity.getRelatedId());
            try {
                CbbTerminalGroupDetailDTO terminalGroupDTO = cbbTerminalGroupMgmtAPI.loadById(viewDesktopPoolComputerEntity.getTerminalGroupId());
                if (terminalGroupDTO.getGroupName() == null) {
                    throw new BusinessException(BusinessKey.RCDC_RCO_GROUP_NOT_EXIST);
                }
                computerDTO.setTerminalGroupName(terminalGroupDTO.getGroupName());
            } catch (Exception e) {
                LOGGER.error("get terminal group[{}] error", viewDesktopPoolComputerEntity.getTerminalGroupId(), e);
            }
            computerDTOList.add(computerDTO);
        }
        response.setItemArr(computerDTOList.toArray(new ComputerDTO[0]));
        return response;
    }

    @Override
    public void removeByPoolIdAndRelatedId(UUID poolId, UUID relatedId) {
        Assert.notNull(poolId, "poolId can not be null");
        Assert.notNull(relatedId, "relatedId can not be null");
        desktopPoolComputerService.removeByPoolIdAndRelatedId(poolId, relatedId);
    }
}
