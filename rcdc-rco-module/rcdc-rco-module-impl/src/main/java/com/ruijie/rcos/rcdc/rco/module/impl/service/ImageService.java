package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.UUID;

/**
 * Description: 镜像服务接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/2
 *
 * @author zhiweiHong
 */
public interface ImageService {

    /**
     * 镜像是否被锁定
     * @param imageId imageId
     * @return true 锁定 false  未锁定
     */
    boolean isLockImage(UUID imageId);
}
