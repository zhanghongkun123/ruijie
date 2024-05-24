package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * <br>
 * Description: 镜像计算工作 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021.04.25 <br>
 *
 * @author linhj
 */
public interface ImageCalcService {

    /**
     * 获取镜像文件大小
     *
     * @param imageId 镜像ID
     * @return 文件大小
     * @throws BusinessException 异常
     */
    int getImageFileSize(UUID imageId) throws BusinessException;
}
