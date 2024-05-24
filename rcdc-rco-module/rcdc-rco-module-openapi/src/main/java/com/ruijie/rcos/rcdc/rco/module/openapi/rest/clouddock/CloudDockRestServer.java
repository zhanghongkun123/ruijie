package com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.clouddock.response.CloudDockCommonResponse;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.FtpConfigInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto.ImageAppInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto.ImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.request.CloudDockPageSearchRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 云坞rest接口
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @author chenjuan
 */
@Path("/v1/cloudDock")
public interface CloudDockRestServer {

    /**
     * 获取FTP信息
     * @return CloudDockCommonResponse<FtpConfigInfo>
     */
    @GET
    @Path("/getFtpAccount")
    CloudDockCommonResponse<FtpConfigInfoDTO> getFtpAccount();

    /**
     * 分页获取镜像列表
     * @param request request
     * @return 镜像信息
     */
    @POST
    @Path("/getImageTemplateList")
    CloudDockCommonResponse<DefaultPageResponse<ImageTemplateInfoDTO>>  getImageTemplateList(CloudDockPageSearchRequest request);

    /**
     * 分页获取应用列比奥
     * @param request request
     * @return 镜像应用信息
     */
    @POST
    @Path("/getAppList")
    CloudDockCommonResponse<DefaultPageResponse<ImageAppInfoDTO>> getAppList(PageWebRequest request);
}
