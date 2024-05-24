package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.DesktopPoolConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolConfigService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 桌面池配置信息service
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/24
 *
 * @author linke
 */
@Service("rcoDesktopPoolConfigService")
public class DesktopPoolConfigServiceImpl implements DesktopPoolConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolConfigServiceImpl.class);

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private DesktopPoolConfigDAO desktopPoolConfigDAO;

    @Override
    public void saveOrUpdateDesktopPoolConfig(DesktopPoolConfigDTO desktopPoolConfigDTO) {
        Assert.notNull(desktopPoolConfigDTO, "desktopPoolConfigDTO can not be null");
        Assert.notNull(desktopPoolConfigDTO.getDesktopPoolId(), "desktopPoolId can not be null");

        DesktopPoolConfigEntity configEntity = desktopPoolConfigDAO.findByDesktopPoolId(desktopPoolConfigDTO.getDesktopPoolId());

        if (Objects.isNull(configEntity)) {
            configEntity = new DesktopPoolConfigEntity();
            configEntity.setDesktopPoolId(desktopPoolConfigDTO.getDesktopPoolId());
            configEntity.setCreateTime(new Date());
        }

        configEntity.setSoftwareStrategyId(desktopPoolConfigDTO.getSoftwareStrategyId());
        configEntity.setUserProfileStrategyId(desktopPoolConfigDTO.getUserProfileStrategyId());
        configEntity.setUpdateTime(new Date());
        desktopPoolConfigDAO.save(configEntity);
    }

    @Override
    public SoftwareStrategyDTO getSoftwareStrategyByDesktopPoolId(UUID desktopPoolId) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        UUID id = this.getSoftwareStrategyIdByDesktopPoolId(desktopPoolId);
        if (Objects.isNull(id)) {
            // 为空就null
            return null;
        }
        return softwareControlMgmtAPI.findSoftwareStrategyById(id);
    }

    @Override
    public UUID getSoftwareStrategyIdByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        DesktopPoolConfigEntity configEntity = desktopPoolConfigDAO.findByDesktopPoolId(desktopPoolId);
        // 为空就null
        return Objects.nonNull(configEntity) ? configEntity.getSoftwareStrategyId() : null;
    }

    @Override
    public DesktopPoolConfigDTO queryByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        DesktopPoolConfigEntity configEntity = desktopPoolConfigDAO.findByDesktopPoolId(desktopPoolId);
        if (Objects.isNull(configEntity)) {
            // 为空就返回null
            return null;
        }
        DesktopPoolConfigDTO configDTO = new DesktopPoolConfigDTO();
        BeanUtils.copyProperties(configEntity, configDTO);
        return configDTO;
    }

    @Override
    public void deleteByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        desktopPoolConfigDAO.deleteByDesktopPoolId(desktopPoolId);
    }

    @Override
    public boolean existsBySoftwareStrategyId(UUID softwareStrategyId) {
        Assert.notNull(softwareStrategyId, "softwareStrategyId can not be null");

        return desktopPoolConfigDAO.existsBySoftwareStrategyId(softwareStrategyId);
    }
}
