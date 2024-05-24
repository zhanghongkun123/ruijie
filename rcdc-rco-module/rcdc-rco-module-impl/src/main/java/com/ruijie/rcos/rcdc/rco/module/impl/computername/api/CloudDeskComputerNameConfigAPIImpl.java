package com.ruijie.rcos.rcdc.rco.module.impl.computername.api;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDeskComputerNameConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.computername.ComputerNameBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.computername.dao.CloudDeskComputerNameConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.computername.entity.CloudDeskComputerNameConfigEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: CloudDeskComputerNameConfigAPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/10
 *
 * @author wjp
 */
public class CloudDeskComputerNameConfigAPIImpl implements CloudDeskComputerNameConfigAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDeskComputerNameConfigAPIImpl.class);

    @Autowired
    private CloudDeskComputerNameConfigDAO cloudDeskComputerNameConfigDAO;

    @Override
    public void createCloudDeskComputerNameConfig(String computerName, UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        LockableExecutor.executeWithTryLock(deskStrategyId.toString(), () -> {
            CloudDeskComputerNameConfigEntity entity = cloudDeskComputerNameConfigDAO.findByDeskStrategyId(deskStrategyId);
            if (entity != null) {
                throw new BusinessException(ComputerNameBusinessKey.RCDC_RCO_CLOUD_DESK_COMPUTER_NAME_CONFIG_IS_EXIST);
            }
            entity = new CloudDeskComputerNameConfigEntity();
            entity.setComputerName(computerName);
            entity.setDeskStrategyId(deskStrategyId);
            entity.setUpdateTime(new Date());
            cloudDeskComputerNameConfigDAO.save(entity);
        }, 1);
    }

    @Override
    public void updateCloudDeskComputerNameConfig(String computerName, UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        LockableExecutor.executeWithTryLock(deskStrategyId.toString(), () -> {
            CloudDeskComputerNameConfigEntity entity = cloudDeskComputerNameConfigDAO.findByDeskStrategyId(deskStrategyId);
            if (Objects.isNull(entity)) {
                // 支持5.2已有云桌面策略升级场景
                entity = new CloudDeskComputerNameConfigEntity();
                entity.setComputerName(computerName);
                entity.setDeskStrategyId(deskStrategyId);
                entity.setUpdateTime(new Date());
                cloudDeskComputerNameConfigDAO.save(entity);
                return;
            }
            entity.setComputerName(computerName);
            entity.setUpdateTime(new Date());
            cloudDeskComputerNameConfigDAO.save(entity);
        }, 1);
    }

    @Override
    public void deleteCloudDeskComputerNameConfig(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        LockableExecutor.executeWithTryLock(deskStrategyId.toString(), () -> {
            CloudDeskComputerNameConfigEntity entity = cloudDeskComputerNameConfigDAO.findByDeskStrategyId(deskStrategyId);
            if (Objects.isNull(entity)) {
                throw new BusinessException(ComputerNameBusinessKey.RCDC_RCO_CLOUD_DESK_COMPUTER_NAME_CONFIG_NOT_EXIT);
            }
            cloudDeskComputerNameConfigDAO.delete(entity);
        }, 1);
    }

    @Override
    public String findCloudDeskComputerName(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        CloudDeskComputerNameConfigEntity entity = cloudDeskComputerNameConfigDAO.findByDeskStrategyId(deskStrategyId);
        if (Objects.isNull(entity) || StringUtils.isBlank(entity.getComputerName())) {
            LOGGER.warn("通过策略ID[id:{}]获取云桌面计算机名配置异常：{}", deskStrategyId, JSON.toJSONString(entity));
            return StringUtils.EMPTY;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("云桌面计算机名实体信息：[{}}{{}]", entity.getComputerName(), entity.getDeskStrategyId());
        }

        return entity.getComputerName();
    }
}
