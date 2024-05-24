package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ImageDownloadStateDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageDownloadStateEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageDownloadStateService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 镜像下载状态服务实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/28 14:12
 *
 * @author yxq
 */
@Service
public class ImageDownloadStateServiceImpl implements ImageDownloadStateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownloadStateServiceImpl.class);

    /**
     * 锁的前缀
     */
    private static final String LOCK_PREFIX = "GET_DOWNLOAD_STATE_";

    /**
     * 获取锁的等待时间
     */
    private static final int WAIT_TIME = 3;

    @Autowired
    private ImageDownloadStateDAO imageDownloadStateDAO;

    @Override
    public void update(ImageDownloadStateDTO updateRequest) {
        Assert.notNull(updateRequest, "imageDownloadStateDTO must not be null");

        try {
            LockableExecutor.executeWithTryLock(LOCK_PREFIX + updateRequest.getTerminalId(), () -> {
                ImageDownloadStateEntity entity = getOrNewEntity(updateRequest.getTerminalId());
                entity.setUpdateTime(new Date());
                entity.setDownloadState(updateRequest.getDownloadState());
                entity.setFailCode(updateRequest.getFailCode());
                entity.setImageId(updateRequest.getImageId());
                // 时间以SHINE上报的为准，只有下载成功时会有时间，其他状态时间为null
                if (entity.getDownloadState() != DownloadStateEnum.SUCCESS) {
                    entity.setDownloadFinishTime(null);
                } else if (entity.getTerminalDownloadFinishTime() == null
                        || entity.getTerminalDownloadFinishTime().compareTo(updateRequest.getTerminalDownloadFinishTime()) != 0) {
                    entity.setDownloadFinishTime(new Date());
                }
                entity.setTerminalDownloadFinishTime(updateRequest.getTerminalDownloadFinishTime());
                entity.setImageRecoveryPointId(updateRequest.getImageRecoveryPointId());
                imageDownloadStateDAO.save(entity);
            }, WAIT_TIME);
        } catch (BusinessException e) {
            LOGGER.error(String.format("获取终端[%s]的对应的锁失败，失败原因：", updateRequest.getTerminalId()), e);
        }
    }

    /**
     * 如果表中有终端id对应的实体，则获取；如果没有的话，则创建一个新的实体
     *
     * @param terminalId 终端ID
     * @return 实体
     */
    private ImageDownloadStateEntity getOrNewEntity(String terminalId) {

        ImageDownloadStateEntity imageDownloadStateEntity = imageDownloadStateDAO.findByTerminalId(terminalId);
        if (imageDownloadStateEntity != null) {
            return imageDownloadStateEntity;
        }

        return new ImageDownloadStateEntity(terminalId, new Date());
    }

    @Override
    public void deleteByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId must not be null");

        imageDownloadStateDAO.deleteByTerminalId(terminalId);
    }

    @Override
    public ImageDownloadStateDTO getByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId must not be null");

        ImageDownloadStateEntity imageDownloadStateEntity = imageDownloadStateDAO.findByTerminalId(terminalId);
        return Optional.ofNullable(imageDownloadStateEntity).map(entity -> {
            ImageDownloadStateDTO dto = new ImageDownloadStateDTO();
            BeanUtils.copyProperties(entity, dto);

            return dto;
        }).orElse(null);
    }
}
