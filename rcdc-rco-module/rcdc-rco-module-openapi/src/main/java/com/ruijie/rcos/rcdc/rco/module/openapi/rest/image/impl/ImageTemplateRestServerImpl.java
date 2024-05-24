package com.ruijie.rcos.rcdc.rco.module.openapi.rest.image.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ImageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.image.replication.GetSyncProgressRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.disk.replication.SyncProgressDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.cache.DiskReplicationCacheManager;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.image.ImageTemplateRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.image.request.RestGetSyncProgressRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ruijie.rcos.rcdc.hciadapter.module.def.constants.Constants.DEFAULT_PLATFORM_ID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29 0:15
 *
 * @author yxq
 */
@Service
public class ImageTemplateRestServerImpl implements ImageTemplateRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTemplateRestServerImpl.class);

    private static final String[] SEARCH_ARR = new String[]{"imageTemplateName"};

    private static final String PLATFORM_ID = "platformId";


    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private CbbImageDiskSnapshotMgmtAPI cbbImageDiskSnapshotMgmtAPI;

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Autowired
    private ImageMgmtAPI imageMgmtAPI;

    @Autowired
    private DiskReplicationCacheManager diskReplicationCacheManager;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    private static final Long DEFAULT_PROGRESS = 0L;

    @Override
    public PageQueryResponse<RestImageTemplateDetailDTO> pageQuery(PageQueryServerRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "pageRequest must not be null");

        // 分页查询构造器
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder(pageRequest);
        if (!StringUtils.isEmpty(pageRequest.getSearchKeyword())) {
            requestBuilder.like(SEARCH_ARR, pageRequest.getSearchKeyword());
        }

        requestBuilder.eq(PLATFORM_ID , DEFAULT_PLATFORM_ID);
        PageQueryResponse<CbbImageTemplateDTO> pageResponse = cbbImageTemplateMgmtAPI.pageQuery(requestBuilder.build());
        RestImageTemplateDetailDTO[] detailArr = Stream.of(pageResponse.getItemArr()).map(
            dto -> buildImageDetail(dto, false)).toArray(RestImageTemplateDetailDTO[]::new);

        return new PageQueryResponse<>(detailArr, pageResponse.getTotal());

    }

    @Override
    public RestImageTemplateDetailDTO getImageInfo(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId must not be null");

        CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(imageId);

        return buildImageDetail(cbbImageTemplateDTO, true);
    }

    private RestImageTemplateDetailDTO buildImageDetail(CbbImageTemplateDTO dto, boolean needWrapSnapshotInfo) {
        if (Objects.isNull(dto)) {
            // 找不到信息时，直接返回空
            return null;
        }
        RestImageTemplateDetailDTO detailDTO = new RestImageTemplateDetailDTO();

        BeanUtils.copyProperties(dto, detailDTO);
        UUID unifiedManageDataId = rccmManageAPI.getUnifiedManageDataId(dto.getId(), UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
        detailDTO.setUnifiedManageDataId(unifiedManageDataId);
        // 分页查询的时候不需要返回快照信息，只有获取详情的时候才需要
        if (needWrapSnapshotInfo) {
            detailDTO.setSnapshotList(buildRcoSnapshot(dto.getId()));
        }

        return detailDTO;
    }

    private List<RcoImageTemplateSnapshotDTO> buildRcoSnapshot(UUID imageId) {
        List<ImageTemplateSnapshotDTO> cbbSnapshotList = cbbImageDiskSnapshotMgmtAPI.findAllSnapshot(imageId);
        List<UUID> snapshotIdList = cbbSnapshotList.stream().map(ImageTemplateSnapshotDTO::getId).collect(Collectors.toList());
        // key是快照点id，value是全局id
        Map<UUID, UUID> unifiedDataIdMap = rccmManageAPI.findByRelatedTypeAndRelatedIdIn(UnifiedManageFunctionKeyEnum.IMAGE_SNAPSHOT, snapshotIdList)
                .stream().collect(Collectors.toMap(UnifiedManageDataDTO::getRelatedId, UnifiedManageDataDTO::getUnifiedManageDataId, (k1, k2) -> k1));
        // key是快照点id，value是模板名称
        Map<UUID, String> templateNameMap = cbbImageTemplateMgmtAPI.listImageTemplateVersionByRootImageId(imageId).stream()
                .collect(Collectors.toMap(CbbImageTemplateDetailDTO::getSourceSnapshotId, CbbImageTemplateDetailDTO::getImageName, (k1, k2) -> k1));

        return cbbSnapshotList.stream().map(snapshot -> {
            RcoImageTemplateSnapshotDTO rcoSnapshotDTO = new RcoImageTemplateSnapshotDTO();
            BeanUtils.copyProperties(snapshot, rcoSnapshotDTO);
            // 由于还原点表中的名称和镜像表中不一致，此处需要用镜像表中的名称
            rcoSnapshotDTO.setName(templateNameMap.getOrDefault(snapshot.getId(), rcoSnapshotDTO.getName()));
            rcoSnapshotDTO.setUnifiedManageDataId(unifiedDataIdMap.get(snapshot.getId()));
            return rcoSnapshotDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public SyncProgressDTO getSyncInfo(RestGetSyncProgressRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID imageId = request.getImageTemplateId();
        UUID targetSiteId = request.getTargetSiteId();
        UUID targetDiskId = request.getTargetDiskId();

        GetSyncProgressRequest getSyncProgressRequest = new GetSyncProgressRequest(imageId, targetSiteId);
        // 获取缓存的远程站点id
        UUID replicationId = diskReplicationCacheManager.getCache(imageId, targetSiteId, targetDiskId);
        getSyncProgressRequest.setReplicationId(replicationId);
        getSyncProgressRequest.setTargetDiskId(targetDiskId);

        CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(imageId);
        getSyncProgressRequest.setPlatformId(cbbImageTemplateDTO.getPlatformId());
        // 获取进度
        SyncProgressDTO syncInfo;
        try {
            syncInfo = imageMgmtAPI.getSyncInfo(getSyncProgressRequest);

            if (!request.getSyncTaskId().equals(syncInfo.getSyncPointId())) {
                syncInfo.setProgress(DEFAULT_PROGRESS);
            }

            diskReplicationCacheManager.addCache(imageId, targetSiteId, syncInfo.getReplicationId(), targetDiskId);
        } catch (BusinessException e) {
            LOGGER.warn("获取镜像[{}]-站点[{}]失败，清空缓存信息", imageId, targetSiteId);
            diskReplicationCacheManager.delCache(imageId, targetSiteId, targetDiskId);
            throw e;
        }

        return syncInfo;
    }
}
