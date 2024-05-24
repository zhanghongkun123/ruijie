package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;

/**
 * Description: 镜像下载状态服务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/28 14:12
 *
 * @author yxq
 */
public interface ImageDownloadStateService {

    /**
     * 修改镜像下载状态
     *
     * @param updateRequest 请求
     */
    void update(ImageDownloadStateDTO updateRequest);

    /**
     * 根据终端ID删除
     *
     * @param terminalId 终端ID
     */
    void deleteByTerminalId(String terminalId);

    /**
     * 根据终端ID获取镜像下载信息
     *
     * @param terminalId 终端ID
     * @return 下载信息
     */
    ImageDownloadStateDTO getByTerminalId(String terminalId);
}
