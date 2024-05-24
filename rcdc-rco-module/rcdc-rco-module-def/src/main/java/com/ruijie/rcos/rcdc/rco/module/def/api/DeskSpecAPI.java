package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterGpuInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.StrategyHardwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ImageDeskSpecGpuCheckParamDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/22
 *
 * @author linke
 */
public interface DeskSpecAPI {

    /**
     * 比较策略VGPU信息是否更改
     *
     * @param vgpuInfoDTO1 vgpuInfoDTO1
     * @param vgpuInfoDTO2 vgpuInfoDTO2
     * @return boolean
     */
    Boolean isVgpuInfoEquals(VgpuInfoDTO vgpuInfoDTO1, VgpuInfoDTO vgpuInfoDTO2);

    /**
     * 比较硬件信息（只校验cpu, memory, personSize, systemSize)是否更改
     *
     * @param strategyHardwareDTO  StrategyHardwareDTO
     * @param deskSpecDTO desktopSpecDTO
     * @return boolean
     */
    Boolean isHardwareEquals(StrategyHardwareDTO strategyHardwareDTO, CbbDeskSpecDTO deskSpecDTO);

    /**
     * 比较策略硬件信息(cpu, memory, personSize, systemSize, 半虚拟化优化和VGPU)是否更改
     *
     * @param strategyHardwareDTO  StrategyHardwareDTO
     * @param deskSpecDTO desktopSpecDTO
     * @return boolean
     */
    Boolean specHardwareEquals(StrategyHardwareDTO strategyHardwareDTO, CbbDeskSpecDTO deskSpecDTO);

    /**
     * 获取集群显卡信息
     *
     * @param clusterId 计算集群ID
     * @return 集群显卡信息列表 List<CbbClusterGpuInfoDTO>
     */
    List<CbbClusterGpuInfoDTO> getClusterGpuInfo(@Nullable UUID clusterId);

    /**
     * 根据model返回vgpu生产商信息，不能存在null;
     *
     * @param clusterId 计算集群ID
     * @param model     显卡model
     * @return String生产商信息，比如 “AMD”
     * @throws BusinessException 业务异常
     */
    String getGpuVendorInfo(UUID clusterId, String model) throws BusinessException;

    /**
     * 构建A卡默认的虚拟型号
     *
     * @param vgpuExtraInfo 显卡信息
     * @return 虚拟型号
     */
    String buildDefaultAmdModel(VgpuExtraInfo vgpuExtraInfo);

    /**
     * 检查镜像模板是否安装过这个云桌面关联的云桌面策略里的显卡驱动，检查这个镜像系统是否支持这个型号的显卡
     *
     * @param desktopId 云桌面Id
     * @param imageId 镜像模板Id
     * @param imageEditionId 镜像模板版本ID
     * @throws BusinessException 业务异常
     */
    void checkGpuSupportByNewImage(UUID desktopId, UUID imageId, @Nullable UUID imageEditionId) throws BusinessException;

    /**
     * 检查镜像模板是否安装了云桌面规格里配置的显卡驱动，检查这个镜像系统是否支持这个型号的显卡
     *
     * @param checkParamDTO 参数
     * @throws BusinessException 业务异常
     */
    void checkGpuSupportByImageAndSpec(ImageDeskSpecGpuCheckParamDTO checkParamDTO) throws BusinessException;

    /**
     * 检查镜像模板是否支持策略的显卡配置
     * 目前只支持WIN7_64位和WIN10_64位, 其中WIN7_64位不支持 N卡型号A40和A16
     *
     * @param imageId    镜像模板Id
     * @param deskVgpuInfo 桌面显卡配置
     * @param clusterId 计算集群ID
     * @throws BusinessException 业务异常
     */
    void checkImageOsSupportGpu(UUID imageId, VgpuInfoDTO deskVgpuInfo, UUID clusterId) throws BusinessException;

    /**
     * 检查桌面是否无需变更规格
     *
     * @param deskId 桌面
     * @return true不变更，false需要变更
     * @throws BusinessException 业务异常
     */
    boolean isSkipChangeDeskSpec(UUID deskId) throws BusinessException;

    /**
     * 额外盘是否一致
     *
     * @param deskExtraDiskList deskExtraDiskList
     * @param specExtraDiskList specExtraDiskList
     * @return true不变更，false需要变更
     */
    boolean isExtraDiskEquals(@Nullable List<CbbAddExtraDiskDTO> deskExtraDiskList, @Nullable List<CbbAddExtraDiskDTO> specExtraDiskList);

    /**
     * 构建正确的VgpuInfo信息
     *
     * @param clusterId    计算集群ID
     * @param vgpuType     vgpuType
     * @param extraInfoDTO extraInfoDTO
     * @return VgpuInfoDTO
     * @throws BusinessException 业务异常
     */
    VgpuInfoDTO checkAndBuildVGpuInfo(UUID clusterId, @Nullable VgpuType vgpuType, @Nullable VgpuExtraInfoSupport extraInfoDTO)
            throws BusinessException;

    /**
     * 获取精确的显卡配置信息，没有相应配置就会抛出错误
     *
     * @param clusterId 计算集群ID
     * @param model     显卡model
     * @return VgpuInfoDTO
     * @throws BusinessException 业务异常
     */
    VgpuInfoDTO getVGpuByModel(UUID clusterId, String model) throws BusinessException;

    /**
     * 检查系统类型是否支持显卡
     *
     * @param gpuModel 显卡model
     * @param osType   系统类型
     * @throws BusinessException 业务异常
     */
    void checkOsTypeSupportGpuModel(String gpuModel, CbbOsType osType) throws BusinessException;

    /**
     * 构建修改规格的参数
     *
     * @param deskId  桌面ID
     * @param deskSpecDTO deskSpecDTO
     * @return CbbUpdateDeskSpecRequest
     */
    CbbUpdateDeskSpecRequest buildUpdateDeskSpecRequest(UUID deskId, CbbDeskSpecDTO deskSpecDTO);

    /**
     * 变更桌面规格，如果桌面非关机状态，将自保存规格信息到规格表中，等关机后自动变更
     *
     * @param request 变更参数
     * @return true 向底层触发修改规格；false 仅保存desk_spec表
     * @throws BusinessException BusinessException
     */
    boolean updateDeskSpec(CbbUpdateDeskSpecRequest request) throws BusinessException;

    /**
     * 构建 CbbDeskSpecDTO
     *
     * @param clusterId       clusterId
     * @param deskSpecRequest deskSpecRequest
     * @return CbbDeskSpecDTO
     * @throws BusinessException BusinessException
     */
    CbbDeskSpecDTO buildCbbDeskSpec(UUID clusterId, DeskSpecRequest deskSpecRequest) throws BusinessException;
}
