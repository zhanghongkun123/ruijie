package com.ruijie.rcos.rcdc.rco.module.impl.desktopspec.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterGpuInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbUpdateDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.ImageRestorePointDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuModelType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.StrategyHardwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ImageDeskSpecGpuCheckParamDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktopspec.DeskSpecBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultListResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_CLUSTER_NOT_EXIST_GPU_RESOURCES;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/23
 *
 * @author linke
 */
public class DeskSpecAPIImpl implements DeskSpecAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSpecAPIImpl.class);

    private static final String DEFAULT_AMD_FORMAT = "AMD %.1f%s";

    private static final String GPU_AMD = "AMD";

    private static final List<String> AMD_NOT_SUPPORT_WIN7_LIST = Lists.newArrayList("V520", "V620", "CG620");

    private static final String GPU_NVIDIA = "NVIDIA";

    private static final List<String> NVIDIA_NOT_SUPPORT_WIN7_LIST = Lists.newArrayList("A40", "A16");

    @Autowired
    private CbbClusterServerMgmtAPI cbbClusterServerMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterServerMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Override
    public Boolean isVgpuInfoEquals(VgpuInfoDTO vgpuInfoDTO1, VgpuInfoDTO vgpuInfoDTO2) {
        Assert.notNull(vgpuInfoDTO1, "vgpuInfoDTO1 must not be null");
        Assert.notNull(vgpuInfoDTO2, "vgpuInfoDTO2 must not be null");
        if (vgpuInfoDTO1.getVgpuType() != vgpuInfoDTO2.getVgpuType()) {
            return false;
        }
        if (vgpuInfoDTO1.getVgpuExtraInfo() == null && vgpuInfoDTO2.getVgpuExtraInfo() == null) {
            return true;
        }
        if (vgpuInfoDTO1.getVgpuExtraInfo() == null || vgpuInfoDTO2.getVgpuExtraInfo() == null) {
            return false;
        }
        VgpuExtraInfo extraInfo1 = getVgpuExtraInfoFromVgpuInfo(vgpuInfoDTO1);
        VgpuExtraInfo extraInfo2 = getVgpuExtraInfoFromVgpuInfo(vgpuInfoDTO2);

        return JSON.toJSONString(extraInfo1).equals(JSON.toJSONString(extraInfo2));
    }

    @Override
    public Boolean isHardwareEquals(StrategyHardwareDTO strategyHardwareDTO, CbbDeskSpecDTO deskSpecDTO) {
        Assert.notNull(strategyHardwareDTO, "strategyHardwareDTO must not be null");
        Assert.notNull(deskSpecDTO, "desktopSpecDTO must not be null");
        strategyHardwareDTO.setPersonSize(Optional.ofNullable(strategyHardwareDTO.getPersonSize()).orElse(0));
        deskSpecDTO.setPersonSize(Optional.ofNullable(deskSpecDTO.getPersonSize()).orElse(0));
        return Objects.equals(strategyHardwareDTO.getCpu(), deskSpecDTO.getCpu())
                && Objects.equals(strategyHardwareDTO.getMemory(), deskSpecDTO.getMemory())
                && Objects.equals(strategyHardwareDTO.getPersonSize(), deskSpecDTO.getPersonSize())
                && Objects.equals(strategyHardwareDTO.getSystemSize(), deskSpecDTO.getSystemSize())
                && Objects.equals(strategyHardwareDTO.getEnableHyperVisorImprove(), deskSpecDTO.getEnableHyperVisorImprove());
    }

    @Override
    public Boolean specHardwareEquals(StrategyHardwareDTO strategyHardwareDTO, CbbDeskSpecDTO deskSpecDTO) {
        Assert.notNull(strategyHardwareDTO, "request must not be null");
        Assert.notNull(deskSpecDTO, "desktopSpecDTO must not be null");
        if (!isHardwareEquals(strategyHardwareDTO, deskSpecDTO)) {
            return false;
        }
        return isVgpuInfoEquals(strategyHardwareDTO.getVgpuInfoDTO(), deskSpecDTO.getVgpuInfoDTO());
    }

    @Override
    public List<CbbClusterGpuInfoDTO> getClusterGpuInfo(@Nullable UUID clusterId) {
        List<CbbClusterGpuInfoDTO> gpuInfoDTOList;
        try {
            if (Objects.isNull(clusterId)) {
                DefaultListResponse<CbbClusterGpuInfoDTO> dtoResponse = cbbClusterServerMgmtAPI.getClusterGpuInfo();
                gpuInfoDTOList = Lists.newArrayList(Optional.ofNullable(dtoResponse.getItemArr()).orElse(new CbbClusterGpuInfoDTO[0]));
            } else {
                gpuInfoDTOList = cbbClusterServerMgmtAPI.getClusterGpuInfoByClusterId(clusterId);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("获取集群GPU信息：{}", JSON.toJSONString(gpuInfoDTOList));
            }
            if (CollectionUtils.isEmpty(gpuInfoDTOList)) {
                return gpuInfoDTOList;
            }
            return gpuInfoDTOList.stream().peek(gpuInfo -> {
                if (Objects.isNull(gpuInfo.getVgpuModelType())) {
                    gpuInfo.setVgpuModelType(VgpuModelType.G);
                }
            }).distinct().collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("获取集群显卡信息异常", e);
            return Lists.newArrayList();
        }
    }

    @Override
    public String getGpuVendorInfo(UUID clusterId, String model) throws BusinessException {
        Assert.notNull(clusterId, "clusterId must not be null");
        Assert.hasText(model, "model must not be null");

        List<CbbClusterGpuInfoDTO> gpuInfoDTOList = getClusterGpuInfo(clusterId);
        if (CollectionUtils.isEmpty(gpuInfoDTOList)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_GPU_CACHE_ERROR, model);
        }
        Optional<CbbClusterGpuInfoDTO> optional = gpuInfoDTOList.stream().filter(item -> Objects.equals(item.getModel(), model)).findFirst();
        if (!optional.isPresent()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_GPU_CACHE_ERROR, model);
        }
        return optional.get().getVendor();
    }

    @Override
    public String buildDefaultAmdModel(VgpuExtraInfo vgpuExtraInfo) {
        Assert.notNull(vgpuExtraInfo, "vgpuExtraInfo must not be null");
        if (StringUtils.isNotEmpty(vgpuExtraInfo.getModel())) {
            return vgpuExtraInfo.getModel();
        }
        if (Objects.isNull(vgpuExtraInfo.getGraphicsMemorySize())) {
            return GPU_AMD;
        }
        double size = CapacityUnitUtils.bit2Gb(vgpuExtraInfo.getGraphicsMemorySize());
        return String.format(DEFAULT_AMD_FORMAT, size, VgpuModelType.G);
    }

    @Override
    public void checkGpuSupportByNewImage(UUID desktopId, UUID imageId, @Nullable UUID imageEditionId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId must not be null");
        Assert.notNull(imageId, "imageId must not be null");

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);

        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(imageId);
        checkParamDTO.setImageEditionId(imageEditionId);
        checkParamDTO.setClusterId(cbbDeskDTO.getClusterId());
        checkParamDTO.setVgpuInfoDTO(cbbDeskSpecAPI.getById(cbbDeskDTO.getDeskSpecId()).getVgpuInfoDTO());
        checkParamDTO.setStrategyId(cbbDeskDTO.getStrategyId());
        checkGpuSupportByImageAndSpec(checkParamDTO);
    }

    @Override
    public void checkGpuSupportByImageAndSpec(ImageDeskSpecGpuCheckParamDTO checkParamDTO) throws BusinessException {
        Assert.notNull(checkParamDTO, "checkParamDTO must not be null");
        Assert.notNull(checkParamDTO.getImageId(), "imageId must not be null");

        UUID imageId = checkParamDTO.getImageId();
        VgpuInfoDTO vgpuInfoDTO = checkParamDTO.getVgpuInfoDTO();
        if (Objects.isNull(vgpuInfoDTO) || VgpuType.QXL == vgpuInfoDTO.getVgpuType()) {
            // 未启用gpu，直接通过
            return;
        }
        CbbImageTemplateDetailDTO imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        // 检查系统是否支持这个显卡型号
        checkImageOsSupportGpu(imageTemplate, vgpuInfoDTO, checkParamDTO.getClusterId());
        //检查计算集群VGPU资源情况
        UUID clusterId = checkParamDTO.getClusterId();
        if (Objects.nonNull(clusterId)) {
            checkClusterVgpuInfoSupport(clusterId, vgpuInfoDTO);
        }
        if (Objects.nonNull(checkParamDTO.getStrategyId()) && isPersonalStrategy(checkParamDTO.getStrategyId())) {
            // 个性桌面无需判断镜像是否按照了驱动，需要支持配置显卡后，桌面挂载驱动自动安装显卡驱动
            return;
        }
        UUID editionId = checkParamDTO.getImageEditionId();
        if (Objects.isNull(editionId)) {
            CbbImageTemplateDriverDTO[] driverArr = cbbImageTemplateMgmtAPI.findImageTemplateDriverInfos(imageTemplate.getId());
            List<CbbImageTemplateDriverDTO> driverList = Arrays.stream(driverArr).filter(dto -> Boolean.TRUE.equals(dto.getPublished()))
                    .collect(Collectors.toList());
            checkImageVGpuDriverSupport(driverList, imageTemplate, vgpuInfoDTO);
            return;
        }

        // 检查还原点的驱动安装情况
        ImageRestorePointDTO restorePointDTO = cbbImageTemplateMgmtAPI.getImageEditionById(editionId);
        List<CbbImageTemplateDriverDTO> driverDTOList = Optional.ofNullable(restorePointDTO.getDriverDTOList()).orElse(Collections.emptyList());
        checkImageVGpuDriverSupport(driverDTOList, imageTemplate, vgpuInfoDTO);
    }

    private boolean isPersonalStrategy(UUID strategyId) throws BusinessException {
        CbbDeskStrategyDTO cbbDeskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
        return cbbDeskStrategyDTO.getPattern() == CbbCloudDeskPattern.PERSONAL;
    }

    @Override
    public void checkImageOsSupportGpu(UUID imageId, VgpuInfoDTO deskVgpuInfo, UUID clusterId) throws BusinessException {
        Assert.notNull(imageId, "imageId must not be null");
        Assert.notNull(deskVgpuInfo, "deskVgpuInfo must not be null");
        Assert.notNull(clusterId, "clusterId must not be null");

        CbbImageTemplateDetailDTO imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        checkImageOsSupportGpu(imageTemplate, deskVgpuInfo, clusterId);
        //检查计算集群VGPU资源情况
        checkClusterVgpuInfoSupport(clusterId, deskVgpuInfo);
    }

    @Override
    public boolean isSkipChangeDeskSpec(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
        if (Objects.isNull(cbbDeskDTO.getDeskSpecId())) {
            return true;
        }
        CbbDeskSpecDTO deskSpecDTO = cbbDeskSpecAPI.getById(cbbDeskDTO.getDeskSpecId());
        return Objects.equals(cbbDeskDTO.getCpu(), deskSpecDTO.getCpu())
                && Objects.equals(cbbDeskDTO.getMemory(), deskSpecDTO.getMemory())
                && Objects.equals(cbbDeskDTO.getPersonSize(), deskSpecDTO.getPersonSize())
                && Objects.equals(cbbDeskDTO.getSystemSize(), deskSpecDTO.getSystemSize())
                && Objects.equals(cbbDeskDTO.getEnableHyperVisorImprove(), deskSpecDTO.getEnableHyperVisorImprove())
                && isVgpuInfoEquals(cbbDeskDTO.getVgpuInfoDTO(), deskSpecDTO.getVgpuInfoDTO())
                && isExtraDiskEquals(cbbVDIDeskDiskAPI.listDeskExtraDisk(cbbDeskDTO.getDeskId()), deskSpecDTO.getExtraDiskList());
    }

    @Override
    public boolean isExtraDiskEquals(@Nullable List<CbbAddExtraDiskDTO> deskExtraDiskList, @Nullable List<CbbAddExtraDiskDTO> specExtraDiskList) {
        if (CollectionUtils.isEmpty(deskExtraDiskList) && CollectionUtils.isEmpty(specExtraDiskList)) {
            return true;
        }
        if (Objects.isNull(deskExtraDiskList) || Objects.isNull(specExtraDiskList) || deskExtraDiskList.size() != specExtraDiskList.size()) {
            return false;
        }
        // 存在未创建的磁盘
        if (specExtraDiskList.stream().anyMatch(extraDisk -> Objects.isNull(extraDisk.getDiskId()))) {
            return false;
        }
        Map<UUID, CbbAddExtraDiskDTO> deskExtraDiskMap = deskExtraDiskList.stream()
                .collect(Collectors.toMap(CbbAddExtraDiskDTO::getDiskId, Function.identity()));
        for (CbbAddExtraDiskDTO specExtraDisk : specExtraDiskList) {
            // 存在未创建的磁盘
            if (!deskExtraDiskMap.containsKey(specExtraDisk.getDiskId())) {
                return false;
            }
            // 存在大小不一样
            if (!Objects.equals(deskExtraDiskMap.get(specExtraDisk.getDiskId()).getExtraSize(), specExtraDisk.getExtraSize())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public VgpuInfoDTO checkAndBuildVGpuInfo(UUID clusterId, @Nullable VgpuType vgpuType, @Nullable VgpuExtraInfoSupport extraInfoDTO)
            throws BusinessException {
        Assert.notNull(clusterId, "clusterId must not be null");
        VgpuInfoDTO vgpuInfoDTO = new VgpuInfoDTO();
        if (vgpuType == VgpuType.QXL || Objects.isNull(extraInfoDTO) || !(extraInfoDTO instanceof VgpuExtraInfo)) {
            return vgpuInfoDTO;
        }
        VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) extraInfoDTO;
        CbbClusterGpuInfoDTO clusterGpuInfoDTO = getVGpuMustPresent(clusterId, vgpuType, vgpuExtraInfo);
        vgpuExtraInfo.setGraphicsMemorySize(clusterGpuInfoDTO.getGraphicsMemorySize());
        vgpuExtraInfo.setParentGpuModel(clusterGpuInfoDTO.getParentGpuModel());
        vgpuExtraInfo.setModel(clusterGpuInfoDTO.getModel());
        vgpuExtraInfo.setVgpuModelType(clusterGpuInfoDTO.getVgpuModelType());
        vgpuInfoDTO.setVgpuType(clusterGpuInfoDTO.getVgpuType());
        vgpuInfoDTO.setVgpuExtraInfo(vgpuExtraInfo);
        return vgpuInfoDTO;
    }

    private CbbClusterGpuInfoDTO getVGpuMustPresent(UUID clusterId, @Nullable VgpuType vgpuType, @Nullable VgpuExtraInfo extraInfoDTO)
            throws BusinessException {
        if (VgpuType.QXL == vgpuType) {
            CbbClusterGpuInfoDTO cbbClusterGpuInfoDTO = new CbbClusterGpuInfoDTO();
            cbbClusterGpuInfoDTO.setVgpuType(VgpuType.QXL);
            return cbbClusterGpuInfoDTO;
        }
        if (Objects.isNull(extraInfoDTO) || StringUtils.isEmpty(extraInfoDTO.getModel())) {
            // null
            LOGGER.error("VgpuType=[{}]时，无显卡model信息", vgpuType);
            throw new BusinessException(DeskSpecBusinessKey.RCO_DESK_SPEC_VGPU_EXTRA_INFO_NULL);
        }
        return this.getMatchVGpuInfo(extraInfoDTO.getModel(), this.getClusterGpuInfo(clusterId));
    }

    @Override
    public VgpuInfoDTO getVGpuByModel(UUID clusterId, String model) throws BusinessException {
        Assert.notNull(clusterId, "clusterId must not be null");
        Assert.hasText(model, "model must not be null");

        CbbClusterGpuInfoDTO clusterGpuInfoDTO = this.getMatchVGpuInfo(model, this.getClusterGpuInfo(clusterId));
        VgpuInfoDTO vgpuInfoDTO = new VgpuInfoDTO();
        vgpuInfoDTO.setVgpuType(clusterGpuInfoDTO.getVgpuType());
        VgpuExtraInfo vgpuExtraInfo = new VgpuExtraInfo();
        vgpuExtraInfo.setGraphicsMemorySize(clusterGpuInfoDTO.getGraphicsMemorySize());
        vgpuExtraInfo.setParentGpuModel(clusterGpuInfoDTO.getParentGpuModel());
        vgpuExtraInfo.setModel(clusterGpuInfoDTO.getModel());
        vgpuExtraInfo.setVgpuModelType(clusterGpuInfoDTO.getVgpuModelType());
        vgpuInfoDTO.setVgpuExtraInfo(vgpuExtraInfo);

        return vgpuInfoDTO;
    }

    /**
     * 校验并获取VGPU信息
     *
     * @param model          显卡配置model
     * @param allGpuInfoList 服务器配置的显卡列表
     * @return CbbClusterGpuInfoDTO
     * @throws BusinessException 业务异常
     */
    private CbbClusterGpuInfoDTO getMatchVGpuInfo(String model, List<CbbClusterGpuInfoDTO> allGpuInfoList)
            throws BusinessException {
        Assert.hasText(model, "model must not be null");
        Assert.notNull(allGpuInfoList, "allGpuInfoList must not be null");

        Map<String, List<CbbClusterGpuInfoDTO>> gpuMap = allGpuInfoList.stream().collect(Collectors.groupingBy(CbbClusterGpuInfoDTO::getModel));
        List<CbbClusterGpuInfoDTO> gpuInfoDTOList = gpuMap.get(model);
        if (CollectionUtils.isEmpty(gpuInfoDTOList)) {
            LOGGER.error("服务器不存在此vGPU配置选项[{}]", model);
            throw new BusinessException(DeskSpecBusinessKey.RCO_DESK_SPEC_VGPU_OPTIONS_NOT_EXIST, model);
        }
        return gpuInfoDTOList.get(0);
    }

    private void checkImageOsSupportGpu(CbbImageTemplateDetailDTO imageTemplate, VgpuInfoDTO deskVgpuInfo, UUID clusterId) throws BusinessException {
        Assert.notNull(imageTemplate, "imageTemplate must not be null");
        Assert.notNull(deskVgpuInfo, "deskVgpuInfo must not be null");

        VgpuType specVgpuType = deskVgpuInfo.getVgpuType();
        if (VgpuType.QXL == specVgpuType) {
            // 未启用gpu，直接通过
            return;
        }
        VgpuExtraInfo vgpuExtraInfo = (VgpuExtraInfo) deskVgpuInfo.getVgpuExtraInfo();
        CbbOsType osType = imageTemplate.getOsType();
        // 目前只支持WIN7_64位和WIN10_64位, 其中WIN7_64位不支持 N卡型号A40和A16
        if (!CbbOsType.isWin764UpOS(osType)) {
            LOGGER.error("仅WIN7_64和WIN10_64系统支持显卡功能，imageId:{}", imageTemplate.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_NOT_SUPPORT_GPU, imageTemplate.getImageName(), osType.name());
        }
        String model = vgpuExtraInfo.getModel();
        if (CbbOsType.WIN_7_64 != osType || StringUtils.isEmpty(model)) {
            // 非win7，win10_64支持所有显卡
            return;
        }
        if (model.contains(GPU_NVIDIA) && NVIDIA_NOT_SUPPORT_WIN7_LIST.stream().anyMatch(model::contains)) {
            // N卡
            LOGGER.error("WIN7_64位系统不支持N卡A40和A16，imageId:{}, model:{}", imageTemplate.getId(), model);
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_NOT_SUPPORT_GPU_MODEL, imageTemplate.getImageName(), model);
        }
        if (Objects.equals(getGpuVendorInfo(clusterId, model), GPU_AMD) && AMD_NOT_SUPPORT_WIN7_LIST.stream().anyMatch(model::contains)) {
            // A卡
            LOGGER.error("WIN7_64位系统不支持A卡V520、V620和CG620，imageId:{}, model:{}", imageTemplate.getId(), model);
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_NOT_SUPPORT_GPU_MODEL, imageTemplate.getImageName(), model);
        }
    }

    private void checkClusterVgpuInfoSupport(UUID clusterId, VgpuInfoDTO vgpuInfoDTO) throws BusinessException {
        if (Objects.isNull(vgpuInfoDTO) || VgpuType.QXL == vgpuInfoDTO.getVgpuType()) {
            // 未启用gpu，直接通过
            return;
        }
        VgpuExtraInfo extraInfo = (VgpuExtraInfo) vgpuInfoDTO.getVgpuExtraInfo();
        boolean canMatch = cbbClusterServerMgmtAPI.getClusterGpuInfoByClusterId(clusterId).stream()
                .anyMatch(cbbClusterGpuInfoDTO -> Objects.equals(extraInfo.getModel(), cbbClusterGpuInfoDTO.getModel()));
        if (!canMatch) {
            PlatformComputerClusterDTO computerClusterDTO = computerClusterServerMgmtAPI.getComputerClusterInfoById(clusterId);
            // 计算集群没有该类型的显卡资源，直接不通过
            LOGGER.error("计算集群[{}]没有显卡[{}]的资源", clusterId, extraInfo.getModel());
            throw new BusinessException(RCDC_RCO_CLUSTER_NOT_EXIST_GPU_RESOURCES,
                    computerClusterDTO.getClusterName(), extraInfo.getModel());
        }
    }

    private void checkImageVGpuDriverSupport(List<CbbImageTemplateDriverDTO> imageDriverList, CbbImageTemplateDetailDTO imageTemplate,
                                             VgpuInfoDTO vgpuInfoDTO) throws BusinessException {
        if (Objects.isNull(vgpuInfoDTO) || Objects.isNull(vgpuInfoDTO.getVgpuExtraInfo())) {
            LOGGER.error("云桌面规格中对应的显卡配置信息为空");
            return;
        }
        VgpuExtraInfo vgpuExtraInfo = getVgpuExtraInfoFromVgpuInfo(vgpuInfoDTO);
        if (StringUtils.isEmpty(vgpuExtraInfo.getModel())) {
            LOGGER.error("云桌面规格中对应的显卡配置model为空");
            return;
        }
        String vgpuType = VgpuUtil.getVgpuTypeDriverType(vgpuInfoDTO);
        if (CollectionUtils.isEmpty(imageDriverList)) {
            // 镜像模板已安装驱动列表为空，直接不通过
            LOGGER.error("镜像模板[{}]未安装任何驱动", imageTemplate.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_NOT_EXIST_GPU_DRIVER, imageTemplate.getImageName(), vgpuExtraInfo.getModel());
        }
        if (imageDriverList.stream().noneMatch(driver -> Objects.equals(vgpuType, driver.getDriverType()))) {
            // 镜像模板未安装驱动，直接不通过
            LOGGER.error("镜像模板[{}]未安装显卡[{}]驱动", imageTemplate.getId(), vgpuType);
            throw new BusinessException(BusinessKey.RCDC_RCO_IMAGE_NOT_EXIST_GPU_DRIVER, imageTemplate.getImageName(), vgpuExtraInfo.getModel());
        }
    }

    private VgpuExtraInfo getVgpuExtraInfoFromVgpuInfo(VgpuInfoDTO vgpuInfoDTO) {
        VgpuExtraInfoSupport extraInfo = vgpuInfoDTO.getVgpuExtraInfo();
        if (extraInfo instanceof VgpuExtraInfo) {
            return (VgpuExtraInfo) extraInfo;
        }
        return new VgpuExtraInfo();
    }

    @Override
    public void checkOsTypeSupportGpuModel(String gpuModel, CbbOsType osType) throws BusinessException {
        Assert.hasText(gpuModel, "gpuModel must not be null");
        Assert.notNull(osType, "osType must not be null");

        if (!CbbOsType.isWin764UpOS(osType)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_OS_TYPE_NOT_SUPPORT_GPU, osType.getTitle());
        }

        if (osType == CbbOsType.WIN_7_64) {
            if (AMD_NOT_SUPPORT_WIN7_LIST.stream().anyMatch(gpuModel::contains)) {
                // A卡
                LOGGER.error("WIN7_64位系统不支持A卡V520、V620和CG620");
                throw new BusinessException(BusinessKey.RCDC_RCO_OS_TYPE_WIN7_X64_NOT_SUPPORT_AMD_GPU);
            }

            if (NVIDIA_NOT_SUPPORT_WIN7_LIST.stream().anyMatch(gpuModel::contains)) {
                // N卡
                LOGGER.error("WIN7_64位系统不支持N卡A40和A16");
                throw new BusinessException(BusinessKey.RCDC_RCO_OS_TYPE_WIN7_X64_NOT_SUPPORT_N_GPU);
            }
        }
    }

    @Override
    public CbbUpdateDeskSpecRequest buildUpdateDeskSpecRequest(UUID deskId, CbbDeskSpecDTO deskSpecDTO) {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(deskSpecDTO, "deskSpecDTO must not be null");

        CbbUpdateDeskSpecRequest cbbUpdateDeskSpecRequest = new CbbUpdateDeskSpecRequest();
        cbbUpdateDeskSpecRequest.setDeskId(deskId);
        cbbUpdateDeskSpecRequest.setCpu(deskSpecDTO.getCpu());
        cbbUpdateDeskSpecRequest.setMemory(deskSpecDTO.getMemory());
        cbbUpdateDeskSpecRequest.setPersonSize(Optional.ofNullable(deskSpecDTO.getPersonSize()).orElse(0));
        cbbUpdateDeskSpecRequest.setPersonDiskStoragePoolId(deskSpecDTO.getPersonDiskStoragePoolId());
        cbbUpdateDeskSpecRequest.setSystemSize(deskSpecDTO.getSystemSize());
        cbbUpdateDeskSpecRequest.setVgpuInfoDTO(deskSpecDTO.getVgpuInfoDTO());
        cbbUpdateDeskSpecRequest.setEnableHyperVisorImprove(deskSpecDTO.getEnableHyperVisorImprove());

        List<CbbAddExtraDiskDTO> deskExtraDiskList = Optional.ofNullable(cbbVDIDeskDiskAPI.listDeskExtraDisk(deskId))
                .orElse(Lists.newArrayList());
        List<CbbAddExtraDiskDTO> specExtraDiskList = Optional.ofNullable(deskSpecDTO.getExtraDiskList()).orElse(Lists.newArrayList());
        if (deskExtraDiskList.size() == 0 && specExtraDiskList.size() == 0) {
            cbbUpdateDeskSpecRequest.setExtraDiskList(new ArrayList<>());
        } else {
            cbbUpdateDeskSpecRequest.setExtraDiskList(deskSpecDTO.getExtraDiskList());
        }

        return cbbUpdateDeskSpecRequest;
    }

    @Override
    public boolean updateDeskSpec(CbbUpdateDeskSpecRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(request.getDeskId());

        // 检查系统盘、个人盘不能变小
        checkSpecUpdatePersonAndSystemSize(cbbDeskDTO, request.getSystemSize(), request.getPersonSize());

        // 检查存储池
        checkUpdateSpecStoragePool(request, cbbDeskDTO);

        // 校验显卡和镜像是否匹配
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(cbbDeskDTO.getImageTemplateId());
        checkParamDTO.setClusterId(cbbDeskDTO.getClusterId());
        checkParamDTO.setVgpuInfoDTO(request.getVgpuInfoDTO());
        checkParamDTO.setStrategyId(cbbDeskDTO.getStrategyId());
        checkGpuSupportByImageAndSpec(checkParamDTO);

        if (cbbDeskDTO.getDeskState() != CbbCloudDeskState.CLOSE) {
            // 只保存deskSpec
            CbbUpdateDeskSpecDTO updateDeskSpecDTO = new CbbUpdateDeskSpecDTO();
            updateDeskSpecDTO.setId(cbbDeskDTO.getDeskSpecId());
            updateDeskSpecDTO.setCpu(request.getCpu());
            updateDeskSpecDTO.setMemory(request.getMemory());
            updateDeskSpecDTO.setSystemSize(request.getSystemSize());
            updateDeskSpecDTO.setPersonSize(Optional.ofNullable(request.getPersonSize()).orElse(0));
            if (cbbDeskDTO.getPersonSize() == 0 && updateDeskSpecDTO.getPersonSize() > 0) {
                updateDeskSpecDTO.setPersonDiskStoragePoolId(request.getPersonDiskStoragePoolId());
            }
            updateDeskSpecDTO.setEnableHyperVisorImprove(request.getEnableHyperVisorImprove());
            updateDeskSpecDTO.setVgpuInfo(Optional.ofNullable(request.getVgpuInfoDTO()).orElse(new VgpuInfoDTO()));
            updateDeskSpecDTO.setExtraDiskList(Optional.ofNullable(request.getExtraDiskList()).orElse(Collections.emptyList()));
            LOGGER.info("桌面[{}]非关机状态，暂时保存规格信息，参数[{}]", request.getDeskId(), JSON.toJSONString(updateDeskSpecDTO));
            cbbDeskSpecAPI.updateDeskSpec(updateDeskSpecDTO);
            cbbVDIDeskMgmtAPI.updateDeskEnableCustom(cbbDeskDTO.getDeskId(), Optional.ofNullable(request.getEnableCustom()).orElse(Boolean.TRUE));
            return false;
        }
        LOGGER.info("更新桌面[{}]的规格信息，参数[{}]", request.getDeskId(), JSON.toJSONString(request));
        cbbVDIDeskMgmtAPI.updateDeskSpec(request);
        return true;
    }

    @Override
    public CbbDeskSpecDTO buildCbbDeskSpec(UUID clusterId, DeskSpecRequest deskSpecRequest) throws BusinessException {
        Assert.notNull(clusterId, "clusterId must not be null");
        Assert.notNull(deskSpecRequest, "request must not be null");

        CbbDeskSpecDTO deskSpecDTO = new CbbDeskSpecDTO();
        deskSpecDTO.setCpu(deskSpecRequest.getCpu());
        deskSpecDTO.setMemory(CapacityUnitUtils.gb2Mb(deskSpecRequest.getMemory()));
        deskSpecDTO.setSystemSize(deskSpecRequest.getSystemDisk());
        deskSpecDTO.setSystemDiskStoragePoolId(Objects.nonNull(deskSpecRequest.getSystemDiskStoragePool()) ?
                deskSpecRequest.getSystemDiskStoragePool().getId() : null);
        deskSpecDTO.setPersonSize(deskSpecRequest.getPersonalDisk());
        deskSpecDTO.setPersonDiskStoragePoolId(Objects.nonNull(deskSpecRequest.getPersonDiskStoragePool()) ?
                deskSpecRequest.getPersonDiskStoragePool().getId() : null);
        deskSpecDTO.setEnableHyperVisorImprove(deskSpecRequest.getEnableHyperVisorImprove());
        VgpuExtraInfo vgpuExtraInfo = deskSpecRequest.getVgpuExtraInfo();
        if (Objects.isNull(vgpuExtraInfo) || StringUtils.isBlank(vgpuExtraInfo.getModel()) || deskSpecRequest.getVgpuType() == VgpuType.QXL) {
            deskSpecDTO.setVgpuInfoDTO(new VgpuInfoDTO());
        } else {
            // 构建显卡详情
            VgpuInfoDTO vgpuInfoDTO = checkAndBuildVGpuInfo(clusterId, deskSpecRequest.getVgpuType(), deskSpecRequest.getVgpuExtraInfo());
            deskSpecDTO.setVgpuInfoDTO(vgpuInfoDTO);
        }
        deskSpecDTO.setExtraDiskList(convertToCbbAddExtraDiskDTOList(deskSpecRequest.getExtraDiskArr()));
        return deskSpecDTO;
    }

    private void checkUpdateSpecStoragePool(CbbUpdateDeskSpecRequest request, CbbDeskDTO cbbDeskDTO) throws BusinessException {
        List<UUID> storagePoolIdList = new ArrayList<>();
        if (Objects.nonNull(request.getPersonDiskStoragePoolId())) {
            storagePoolIdList.add(request.getPersonDiskStoragePoolId());
        }
        if (CollectionUtils.isNotEmpty(request.getExtraDiskList())) {
            for (CbbAddExtraDiskDTO cbbAddExtraDiskDTO : request.getExtraDiskList()) {
                storagePoolIdList.add(cbbAddExtraDiskDTO.getAssignedStoragePoolId());
            }
        }
        clusterAPI.validateClusterStoragePoolList(cbbDeskDTO.getClusterId(), storagePoolIdList);
    }

    private void checkSpecUpdatePersonAndSystemSize(CbbDeskDTO cbbDeskDTO, Integer newSystemSize, Integer newPersonSize) throws BusinessException {
        newPersonSize = Optional.ofNullable(newPersonSize).orElse(0);

        if (cbbDeskDTO.getPersonSize() > newPersonSize) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_SYSTEM_PERSON_SIZE_LESS);
        }

        if (cbbDeskDTO.getSystemSize() > newSystemSize) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_SYSTEM_PERSON_SIZE_LESS);
        }
    }

    private List<CbbAddExtraDiskDTO> convertToCbbAddExtraDiskDTOList(ExtraDiskDTO[] extraDiskDTOArr) {
        if (ArrayUtils.isEmpty(extraDiskDTOArr)) {
            return new ArrayList<>();
        }
        return Arrays.stream(extraDiskDTOArr).map(item -> {
            CbbAddExtraDiskDTO extraDiskDTO = new CbbAddExtraDiskDTO();
            BeanUtils.copyProperties(item, extraDiskDTO);
            IdLabelEntry extraDiskStoragePool = item.getExtraDiskStoragePool();
            if (Objects.nonNull(extraDiskStoragePool)) {
                extraDiskDTO.setAssignedStoragePoolId(extraDiskStoragePool.getId());
            }
            return extraDiskDTO;
        }).collect(Collectors.toList());
    }
}
