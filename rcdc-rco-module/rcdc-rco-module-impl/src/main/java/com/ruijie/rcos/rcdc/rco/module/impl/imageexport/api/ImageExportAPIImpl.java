package com.ruijie.rcos.rcdc.rco.module.impl.imageexport.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.PageQueryExportImageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.imageexport.entity.ViewImageExportEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.imageexport.service.ViewImageExportService;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateExportMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbDeleteExportFileReq;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbExportState;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.dto.ViewImageExportDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.CheckExportSizeOverRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.CheckExportSizeOverResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.util.ObjectUtils;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/13 17:02
 *
 * @author ketb
 */
public class ImageExportAPIImpl implements ImageExportAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageExportAPIImpl.class);

    private static final String IMAGE_DISK_ID = "imageDiskId";

    @Autowired
    private CbbImageTemplateExportMgmtAPI cbbImageTemplateExportMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private ViewImageExportService viewImageExportService;

    private static final int PAGE_QUERY_DEFAULT_PAGE = 0;

    private static final int PAGE_QUERY_DEFAULT_LIMIT = 10;



    @Value("${rccpmile.limit.max_export_volumes:5}")
    private Integer maxExportingSize;

    @Override
    public void deleteExportImage(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageid is not null");
        UUID taskId = UUID.randomUUID();
        CbbDeleteExportFileReq req = new CbbDeleteExportFileReq();
        req.setId(imageId);
        req.setTaskId(taskId);
        // deleteExportFile接口将同时删除导出的镜像文件和导出记录
        cbbImageTemplateExportMgmtAPI.deleteExportFile(req);
        StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
        stateMachineMgmtAgent.waitForAllProcessFinish();
        LOGGER.warn("删除导出镜像文件:" + imageId);
    }

    @Override
    public List<ViewImageExportDTO> getExportImageByTemplateId(UUID templateId) throws BusinessException {
        Assert.notNull(templateId, "templateId is not null");

        PageQueryRequest pageQueryRequest = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(PAGE_QUERY_DEFAULT_PAGE, PAGE_QUERY_DEFAULT_LIMIT).eq("imageTemplateId", templateId).build();

        return queryImageExport(pageQueryRequest);
    }

    @Override
    public List<ViewImageExportDTO> findAllImageExporting() throws BusinessException {
        PageQueryRequest pageQueryRequest = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(PAGE_QUERY_DEFAULT_PAGE, PAGE_QUERY_DEFAULT_LIMIT).eq("exportState", CbbExportState.EXPORTING).build();

        return queryImageExport(pageQueryRequest);

    }

    /**
     * 根据镜像磁盘id查询导出镜像
     *
     * @param imageDiskId 磁盘id
     * @return 查询结果
     */
    @Override
    public List<ViewImageExportDTO> getExportImageByImageDiskId(UUID imageDiskId) throws BusinessException {
        Assert.notNull(imageDiskId, "imageDiskId is not null");

        PageQueryRequest pageQueryRequest = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(PAGE_QUERY_DEFAULT_PAGE, PAGE_QUERY_DEFAULT_LIMIT)
                .eq(IMAGE_DISK_ID, imageDiskId).build();

        return queryImageExport(pageQueryRequest);
    }

    private List<ViewImageExportDTO> queryImageExport(PageQueryRequest pageQueryRequest) throws BusinessException {
        PageQueryResponse<ViewImageExportDTO> response = ImageExportAPI.super.pageQuery(pageQueryRequest);
        List<ViewImageExportDTO> exportImageList = new ArrayList<>();
        if (response != null && response.getTotal() > 0) {
            exportImageList = Arrays.asList(response.getItemArr());
        }

        return exportImageList;
    }

    @Override
    public CheckExportSizeOverResponse checkExportSizeOver(CheckExportSizeOverRequest request) throws BusinessException {
        Assert.notNull(request, "request cant be null" );
        List<ViewImageExportDTO> imageExportingList = findAllImageExporting();
        // imageExportingList 必不为空，无须判空

        if (isOver(imageExportingList.size(), request.getNumOfExportImage())) {
            LOGGER.info("当前正在导出的磁盘数量已达到最大支持数量[{}]！", maxExportingSize);
            return new CheckExportSizeOverResponse(true, maxExportingSize);
        }
        return new CheckExportSizeOverResponse(false, maxExportingSize);
    }

    private boolean isOver(int size, Integer num) {
        int total = size + num;

        return total > maxExportingSize;
    }

    @Override
    public DefaultPageResponse<ViewImageExportDTO> queryImageExportInfoService(PageQueryExportImageRequest request) {
        Assert.notNull(request, "request is not null");
        Page<ViewImageExportEntity> pageEntity = viewImageExportService.pageQuery(request, ViewImageExportEntity.class);
        if (ObjectUtils.isEmpty(pageEntity)) {
            return new DefaultPageResponse<>();
        }
        List<ViewImageExportEntity> entityList = pageEntity.getContent();
        ViewImageExportDTO[] imageInfoDTOArr = new ViewImageExportDTO[entityList.size()];
        for (int i = 0; i < entityList.size(); i++) {
            ViewImageExportDTO imageInfoDTO = new ViewImageExportDTO();
            BeanUtils.copyProperties(entityList.get(i), imageInfoDTO);
            imageInfoDTOArr[i] = imageInfoDTO;
        }
        DefaultPageResponse<ViewImageExportDTO> response = new DefaultPageResponse<>();
        response.setItemArr((imageInfoDTOArr));
        response.setTotal(pageEntity.getTotalElements());
        return response;
    }

}
