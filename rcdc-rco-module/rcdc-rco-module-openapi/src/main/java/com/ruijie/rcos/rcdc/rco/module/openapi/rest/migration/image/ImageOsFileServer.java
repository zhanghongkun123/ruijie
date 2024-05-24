package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.DirectorSpaceInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.ImportImageOsFileRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@OpenAPI
@Path("/v1/image/file")
public interface ImageOsFileServer {

    /**
     * 导入镜像文件
     *
     * @param request 请求
     * @throws BusinessException 业务报错
     */
    @POST
    @Path("/importImageOsFile")
    void importImageOsFile(ImportImageOsFileRequest request) throws BusinessException;

    /**
     * 获取镜像空间使用情况
     * 
     * @return 镜像空间使用情况
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/space")
    DirectorSpaceInfoDTO getImageSpaceInfo() throws BusinessException;
}
