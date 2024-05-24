package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.dto.ViewImageExportDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.CheckExportSizeOverRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.CheckExportSizeOverResponse;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.PageQueryExportImageRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/13 17:15
 *
 * @author ketb
 */
public interface ImageExportAPI extends PageQueryAPI<ViewImageExportDTO> {

    /**
     * 根据镜像模板id删除导出的镜像
     * @param imageId 镜像模板id
     * @throws BusinessException 业务异常
     */
    void deleteExportImage(UUID imageId) throws BusinessException;

    /**
     * 根据镜像模板id查询导出镜像
     * @param templateId 镜像模板id
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    List<ViewImageExportDTO> getExportImageByTemplateId(UUID templateId) throws BusinessException;

    /**
     * 查询正在导出的镜像列表
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    List<ViewImageExportDTO> findAllImageExporting() throws BusinessException;

    /**
     * 校验是否超出同时导出镜像的数量
     * @return 查询结果
     * @param request 导出磁盘请求
     * @throws BusinessException 业务异常
     */
    CheckExportSizeOverResponse checkExportSizeOver(CheckExportSizeOverRequest request) throws BusinessException;


    /**
     * 根据镜像磁盘id查询导出镜像
     *
     * @param imageDiskId 磁盘id
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    List<ViewImageExportDTO> getExportImageByImageDiskId(UUID imageDiskId) throws BusinessException;

    /**
     * 分页查询
     *
     * @param request 分页参数
     * @return 查询结果
     */
    DefaultPageResponse<ViewImageExportDTO> queryImageExportInfoService(PageQueryExportImageRequest request);
}
