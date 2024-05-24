package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.HostUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/26
 *
 * @author zqj
 */
@Service
public class HostUserServiceImpl implements HostUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostUserServiceImpl.class);

    private static final String LOCK_CLEAR_TERMINAL_ID = "LOCK_CLEAR_TERMINAL_ID_";

    @Autowired
    private HostUserDAO hostUserDAO;

    @Override
    public List<HostUserEntity> findByDesktopPoolIdAndUserId(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");
        return hostUserDAO.findByDesktopPoolIdAndUserId(desktopPoolId, userId);
    }

    @Override
    public void poolDesktopBindUser(PoolDesktopInfoDTO desktopInfoDTO, UUID userId) {
        Assert.notNull(desktopInfoDTO, "desktopInfoDTO can not be null");
        Assert.notNull(userId, "userId can not be null");
        try {
            LockableExecutor.executeWithTryLock(userId + desktopInfoDTO.getDeskId().toString(), () -> {
                HostUserEntity hostUserEntity = hostUserDAO.findByUserIdAndDesktopId(userId, desktopInfoDTO.getDeskId());
                if (hostUserEntity == null) {
                    hostUserEntity = new HostUserEntity();
                }
                hostUserEntity.setDesktopId(desktopInfoDTO.getDeskId());
                hostUserEntity.setUserId(userId);
                hostUserEntity.setCreateTime(new Date());
                hostUserEntity.setDesktopPoolId(desktopInfoDTO.getDesktopPoolId());
                hostUserDAO.save(hostUserEntity);
            }, 3);
        } catch (BusinessException e) {
            LOGGER.error("保存桌面[{}]与用户[{}]关系异常", desktopInfoDTO.getDeskId(), userId, e);
        }

    }

    @Override
    public void removeById(UUID id) {
        Assert.notNull(id, "id can not be null");
        hostUserDAO.deleteById(id);
    }

    @Override
    public void removeByIds(List<UUID> idList) {
        Assert.notEmpty(idList, "idList can not be null");
        hostUserDAO.deleteByIdIn(idList);
    }

    @Override
    public void removeByUserIdAndDeskId(UUID userId, UUID deskId) {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(deskId, "deskId can not be null");
        hostUserDAO.deleteByUserIdAndDesktopId(userId, deskId);
    }

    @Override
    public void deleteByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        hostUserDAO.deleteByDesktopId(deskId);
    }

    @Override
    public void desktopBindUser(UserDesktopBindUserRequest request) {
        Assert.notNull(request, "request can not be null");
        HostUserEntity hostUserEntity = hostUserDAO.findByUserIdAndDesktopId(request.getUserId(), request.getDesktopId());
        if (hostUserEntity != null) {
            return;
        }
        hostUserEntity = new HostUserEntity();
        hostUserEntity.setDesktopId(request.getDesktopId());
        hostUserEntity.setUserId(request.getUserId());
        hostUserEntity.setCreateTime(new Date());
        hostUserEntity.setDesktopPoolId(request.getDesktopPoolId());
        hostUserDAO.save(hostUserEntity);
    }

    @Override
    public HostUserEntity findById(UUID id) {
        Assert.notNull(id, "id can not be null");
        return hostUserDAO.getOne(id);
    }

    @Override
    public List<HostUserEntity> findByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        List<HostUserEntity> userEntityList = hostUserDAO.findByDesktopId(deskId);
        return userEntityList;
    }

    @Override
    public List<HostUserEntity> findByUserId(UUID userId) {
        Assert.notNull(userId, "userId can not be null");
        List<HostUserEntity> userEntityList = hostUserDAO.findByUserId(userId);
        return userEntityList;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Assert.notNull(userId, "userId can not be null");
        hostUserDAO.deleteByUserId(userId);
    }

    @Override
    public HostUserEntity findByDeskIdAndUserId(UUID deskId, UUID userId) {
        Assert.notNull(deskId, "deskId can not be null");
        Assert.notNull(userId, "userId can not be null");
        return hostUserDAO.findByUserIdAndDesktopId(userId, deskId);
    }

    @Override
    public void unBindUser(UUID deskId, UUID userId) {
        Assert.notNull(deskId, "deskId can not be null");
        Assert.notNull(userId, "userId can not be null");
        hostUserDAO.deleteByUserIdAndDesktopId(userId, deskId);
    }

    @Override
    public void clearTerminalIdByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId can not be null");
        List<HostUserEntity> entityList = hostUserDAO.findByTerminalId(terminalId);
        if (CollectionUtils.isEmpty(entityList)) {
            LOGGER.debug("无记录需要清除终端[{}]", terminalId);
            return;
        }
        LOGGER.info("清除终端信息[{}]", terminalId);
        try {
            LockableExecutor.executeWithTryLock(LOCK_CLEAR_TERMINAL_ID + terminalId, () -> {
                List<HostUserEntity> finalEntityList = hostUserDAO.findByTerminalId(terminalId);
                if (CollectionUtils.isEmpty(finalEntityList)) {
                    LOGGER.info("无记录需要清除终端[{}]", terminalId);
                    return;
                }
                finalEntityList.forEach(item -> {
                    item.setTerminalId(null);
                    item.setUpdateTime(new Date());
                });
                hostUserDAO.saveAll(finalEntityList);
            }, 1);
        } catch (BusinessException e) {
            LOGGER.error("清除终端信息[{}]异常", terminalId, e);
        }
    }

    @Override
    public void bindTerminalId(UUID userId, UUID desktopId, String terminalId) {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(desktopId, "desktopId can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        HostUserEntity hostUserEntity = hostUserDAO.findByUserIdAndDesktopId(userId, desktopId);
        if (Objects.isNull(hostUserEntity)) {
            LOGGER.warn("绑定终端[{}]和多会话桌面[{}]关系时，无hostUserEntity记录", terminalId, desktopId);
            return;
        }
        hostUserEntity.setTerminalId(terminalId);
        hostUserDAO.save(hostUserEntity);
    }

    @Override
    public List<String> listTerminalIdByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        List<HostUserEntity> hostUserEntityList = hostUserDAO.findByDesktopId(deskId);
        if (CollectionUtils.isEmpty(hostUserEntityList)) {
            return new ArrayList<>();
        }
        return hostUserEntityList.stream().map(HostUserEntity::getTerminalId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public List<HostUserEntity> findByDeskIds(List<UUID> desktopIdList) {
        Assert.notEmpty(desktopIdList, "desktopIdList can not be null");
        return hostUserDAO.findByDesktopIdIn(desktopIdList);
    }

    @Override
    public List<HostUserEntity> findByUserIds(List<UUID> userIdList) {
        Assert.notEmpty(userIdList, "userIdList can not be null");
        return hostUserDAO.findByUserIdIn(userIdList);
    }

    @Override
    public List<UUID> findUserIdListByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return hostUserDAO.findUserIdListByDesktopPoolId(desktopPoolId);
    }

    @Override
    public void updateHostUserEntity(HostUserEntity entity) {
        Assert.notNull(entity, "entity can not be null");

        hostUserDAO.save(entity);
    }

    @Override
    public List<UUID> findNormalDesktopIdByUserIdAndDesktopPoolId(UUID userId, UUID desktopPoolId) {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return hostUserDAO.findNormalDesktopIdByUserIdAndDesktopPoolId(userId, desktopPoolId);
    }

    @Override
    public void removeByUserIdAndDesktopPoolId(UUID userId, UUID desktopPoolId) {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        hostUserDAO.deleteByUserIdAndDesktopPoolId(userId, desktopPoolId);
    }
}
