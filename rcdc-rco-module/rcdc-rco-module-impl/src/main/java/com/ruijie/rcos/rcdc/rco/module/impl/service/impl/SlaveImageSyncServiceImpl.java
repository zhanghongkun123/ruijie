package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCheckImageNameDuplicationDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageRoleType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ClusterMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.cluster.ComputerClusterBaseRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cluster.ClusterNodesInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.common.resolver.ContextDtoResolver;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.PublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlavePrepareImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.SlaveRollbackImageSyncDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SlaveRollbackImageSyncRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ImageTemplateServerCpuDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ClusterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.SlaveImageSyncService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.vo.DefaultClusterInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.prepare.SlavePrepareStageStateHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.publish.SlavePublishStageStateHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.rollback.SlaveRollbackStageStateHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_NAME_EXISTS;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_NAME_EXISTS;

/**
 * Description: 从集群处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/20
 *
 * @author zhiweiHong
 */
@Service
public class SlaveImageSyncServiceImpl extends AbstractImageSyncServiceImpl implements SlaveImageSyncService {

    private static Logger LOGGER = LoggerFactory.getLogger(SlaveImageSyncServiceImpl.class);

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private ClusterService clusterService;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ClusterMgmtAPI clusterMgmtAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterServerMgmtAPI;
    
    @Override
    public void prepareStage(SlavePrepareImageSyncDTO request) throws BusinessException {
        Assert.notNull(request, "request can not be null");


        notAllowRoleExecuteCheck(rccmManageService.isMaster());

        UnifiedManageDataEntity unifiedManageDataEntity = unifiedManageDataService.findByUnifiedManageDataId(request.getUnifiedManageDataId());
        if (Objects.nonNull(unifiedManageDataEntity)) {
            UUID imageId = unifiedManageDataEntity.getRelatedId();
            isImageExist(imageId);

            CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);

            cbbImageTemplateMgmtAPI.validateVersionNumLimit(imageTemplateDetail.getId());
            isImageStateInSync(imageTemplateDetail);
            isImageStateInError(imageTemplateDetail);

            request.setImageId(imageTemplateDetail.getId());
        }

        if (Objects.nonNull(request.getPublishSnapshot())) {
            validImageVersionName(request.getImageId(), request.getPublishSnapshot().getName());
        }


        DefaultClusterInfo defaultClusterInfo = clusterService.getDefaultClusterInfo();
        request.setClusterId(defaultClusterInfo.getDefaultComputeClusterId());
        request.setStoragePoolId(defaultClusterInfo.getDefaultStorageClusterId());

        CbbImageTemplateSnapshotDTO publishSnapshot = request.getPublishSnapshot();
        validSlaveCpuDriverContainsMaster(publishSnapshot, request.getClusterId());

        stateMachineFactory.newBuilder(request.getTaskId(), SlavePrepareStageStateHandler.class)
                .initArg(ContextDtoResolver.DTO, request)
                .lockResources(request.getUnifiedManageDataId().toString())
                .start();
    }

    private void validImageVersionName(UUID imageId, String imageVersionName) throws BusinessException {
        if (StringUtils.isEmpty(imageVersionName)) {
            return;
        }
        CbbCheckImageNameDuplicationDTO checkImageNameDuplication = new CbbCheckImageNameDuplicationDTO();
        checkImageNameDuplication.setId(imageId);
        checkImageNameDuplication.setImageName(imageVersionName);
        if (cbbImageTemplateMgmtAPI.checkImageNameDuplication(checkImageNameDuplication)) {
            CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfoByImageTemplateName(imageVersionName);

            if (ImageRoleType.TEMPLATE.equals(imageTemplateInfoDTO.getImageRoleType())) {
                throw new BusinessException(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_NAME_EXISTS, imageTemplateInfoDTO.getImageName());
            } else {
                String rootImageName = Optional.of(imageTemplateInfoDTO.getRootImageName()).orElse(org.apache.commons.lang3.StringUtils.EMPTY);
                throw new BusinessException(RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_VERSION_NAME_EXISTS, rootImageName,
                        imageTemplateInfoDTO.getImageName());
            }
        }
    }


    /***
     * 校验从端默认集群cpu集合是主端镜像cpu集合的子集，否则抛出异常
     */
    private void validSlaveCpuDriverContainsMaster(CbbImageTemplateSnapshotDTO publishSnapshot, UUID clusterId) throws BusinessException {
        if (Objects.nonNull(publishSnapshot)) {
            String serverCpuTypeInfo = publishSnapshot.getServerCpuTypeInfo();
            List<String> prepareSyncImageServerCpuList = JSONArray.parseArray(serverCpuTypeInfo, ImageTemplateServerCpuDTO.class)
                    .stream().map(ImageTemplateServerCpuDTO::getServerCpuType).collect(Collectors.toList());
            List<String> defaultClusterCpuList = getDefaultClusterCpuList(clusterId);

            boolean hasNoMatchCpu = defaultClusterCpuList.stream().anyMatch(i -> !prepareSyncImageServerCpuList.contains(i));

            if (hasNoMatchCpu) {
                throw new BusinessException(BusinessKey.RCDC_RCO_CLUSTER_EXIST_MIX_CPU_NODE);
            }
        }
    }


    private List<String> getDefaultClusterCpuList(UUID clusterId) throws BusinessException {
        PlatformComputerClusterDTO computerClusterDTO = computerClusterServerMgmtAPI.getComputerClusterInfoById(clusterId);
        // 获取计算集群全部cpu驱动类型
        return clusterMgmtAPI.getClusterNodesInfo(new ComputerClusterBaseRequest(clusterId, computerClusterDTO.getPlatformId()))
                .getClusterNodesInfoDTOList().stream()
                .map(ClusterNodesInfoDTO::getCpuType).collect(Collectors.toList());
    }


    private void isImageStateInError(CbbImageTemplateDetailDTO imageTemplateDetail) throws BusinessException {
        if (imageTemplateDetail.getImageState() == ImageTemplateState.ERROR) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_STATE_IS_SYNCING_ERROR, imageTemplateDetail.getImageName());
        }
    }


    @Override
    public void publishStage(PublishSyncingImageTemplateDTO request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        notAllowRoleExecuteCheck(rccmManageService.isMaster());

        UUID imageId = getSlaveImageId(request.getUnifiedManageDataId());

        isImageExist(imageId);

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        isImageStateNotInSync(imageTemplateDetail, BusinessKey.RCDC_RCO_IMAGE_NOT_SYNCING_NOT_ALLOW_PUBLISH);


        request.setImageTemplateId(imageId);

        stateMachineFactory.newBuilder(request.getTaskId(), SlavePublishStageStateHandler.class)
                .initArg(ContextDtoResolver.DTO, request)
                .lockResources(request.getUnifiedManageDataId().toString())
                .start();
    }

    @Override
    public void rollbackStage(SlaveRollbackImageSyncRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        notAllowRoleExecuteCheck(rccmManageService.isMaster());


        UUID imageId = getSlaveImageId(request.getUnifiedManageDataId());

        isImageExist(imageId);

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        isImageStateNotInSync(imageTemplateDetail, BusinessKey.RCDC_RCO_IMAGE_NOT_SYNCING_NOT_ALLOW_ROLLBACK);


        SlaveRollbackImageSyncDTO dto = new SlaveRollbackImageSyncDTO();
        dto.setImageId(imageId);
        stateMachineFactory.newBuilder(request.getTaskId(), SlaveRollbackStageStateHandler.class)
                .initArg(ContextDtoResolver.DTO, dto)
                .lockResources(dto.getImageId().toString())
                .start();
    }

    private UUID getSlaveImageId(UUID unifiedMangeDataId) throws BusinessException {
        UnifiedManageDataEntity unifiedManageDataEntity = unifiedManageDataService.findByUnifiedManageDataId(unifiedMangeDataId);
        if (Objects.isNull(unifiedManageDataEntity)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_NOT_FIND_IMAGE_UNIFIED_ID, unifiedMangeDataId.toString());
        }

        return unifiedManageDataEntity.getRelatedId();
    }

}
