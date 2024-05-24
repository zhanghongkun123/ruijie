package com.ruijie.rcos.rcdc.rco.module.openapi.service.image;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.ImportImageOsFileRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public interface ImageOsFileService {

    /**
     * 检查镜像文件相关业务
     *
     * @param request 入参请求
     * @throws BusinessException 业务错误
     */
    void validateImageOsFile(ImportImageOsFileRequest request) throws BusinessException;

}
