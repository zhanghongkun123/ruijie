package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.dto.IDVLicenseRemainCheckResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.sk.webmvc.api.annotation.NoMaintenanceUrl;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/7
 *
 * @author zhangsiming
 */
@OpenAPI
@Path("/v1/migration/prepare")
public interface MigrationPrepareCheckServer {

    /**
     * 检查samba挂载情况
     * 
     * @throws BusinessException 业务异常
     */
    @Path("/check/samba")
    @NoMaintenanceUrl
    @NoAuthUrl
    @POST
    void checkSambaMount() throws BusinessException;


    /**
     * 检查是否进入维护模式
     * 
     * @throws BusinessException 业务异常
     */
    @Path("/check/maintenance")
    @NoMaintenanceUrl
    @NoAuthUrl
    @POST
    void checkMaintenanceMode() throws BusinessException;

    /**
     * 检查服务器是否进入备份
     * 
     * @throws BusinessException 业务异常
     */
    @Path("/check/backup")
    @NoMaintenanceUrl
    @NoAuthUrl
    @POST
    void checkBackup() throws BusinessException;

    /**
     * 检查服务器是否进入备份
     * 
     * @throws BusinessException 业务异常
     */
    @Path("/check/image")
    @NoMaintenanceUrl
    @NoAuthUrl
    @POST
    void checkImageCreate() throws BusinessException;

    /**
     * 检查服务器是否进入备份
     * 
     * @throws BusinessException 业务异常
     */
    @Path("/check/wizard")
    @NoMaintenanceUrl
    @NoAuthUrl
    @POST
    void checkConfigWizard() throws BusinessException;

    /**
     * @return 检查剩余未使用的的idv永久授权数量
     *         -1：表示无限制
     * @throws BusinessException 业务异常
     */
    @Path("/license/idv/select")
    @NoAuthUrl
    @POST
    IDVLicenseRemainCheckResponse checkIDVRemainLicense() throws BusinessException;
}
