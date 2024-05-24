package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDISpecDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestDeskCreateExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.CreateVDIDesktopRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: 异步线程管理云桌面抽象类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author lyb
 */
public abstract class AbstractAsyncDesktopMgmtThread extends AbstractAsyncTaskThread {

    private static final int DEFAULT_CPU = 4;

    private static final int DEFAULT_MEMORY = 4096;

    private static final int DEFAULT_SYSTEM_SIZE = 60;

    private static final int DEFAULT_PERSON_SIZE = 80;

    protected UserDesktopMgmtAPI userDesktopMgmtAPI;

    protected CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    protected DeskSpecAPI deskSpecAPI;

    protected CbbDeskSpecDTO buildDeskSpec(CreateVDIDesktopRequest request) throws BusinessException {
        CbbDeskSpecDTO deskSpecDTO = new CbbDeskSpecDTO();
        // 查看是否上传了cpu、内存、系统盘、本地盘、VGPU、额外盘如果都没有，就查询云桌面策略查看是否有对应几个spec的配置
        if (Objects.isNull(request.getCpuCores()) && Objects.isNull(request.getMemory()) && Objects.isNull(request.getSystemDisk())
                && Objects.isNull(request.getDataDisk()) && Objects.isNull(request.getVgpuModel()) && Objects.isNull(request.getExtraDiskArr())
                && Objects.isNull(request.getDataDiskStoragePoolId())) {
            CbbDeskStrategyVDISpecDTO strategyVDISpecDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDISpec(request.getDeskStrategyId());
            if (Objects.nonNull(strategyVDISpecDTO.getCpu()) && strategyVDISpecDTO.getCpu() > 0) {
                deskSpecDTO.setCpu(strategyVDISpecDTO.getCpu());
                deskSpecDTO.setMemory(strategyVDISpecDTO.getMemory());
                deskSpecDTO.setSystemSize(strategyVDISpecDTO.getSystemSize());
                deskSpecDTO.setPersonSize(strategyVDISpecDTO.getPersonSize());
                deskSpecDTO.setEnableHyperVisorImprove(strategyVDISpecDTO.getEnableHyperVisorImprove());
                deskSpecDTO.setSystemDiskStoragePoolId(request.getStoragePoolId());
                if (Optional.ofNullable(deskSpecDTO.getPersonSize()).orElse(0) > 0) {
                    deskSpecDTO.setPersonDiskStoragePoolId(request.getStoragePoolId());
                }
                deskSpecDTO.setVgpuInfoDTO(strategyVDISpecDTO.getVgpuInfoDTO());
                return deskSpecDTO;
            }
        }

        // 无cpu参数说明未有自定义规格，取默认值
        deskSpecDTO.setCpu(Optional.ofNullable(request.getCpuCores()).orElse(DEFAULT_CPU));
        if (Objects.isNull(request.getMemory())) {
            deskSpecDTO.setMemory(DEFAULT_MEMORY);
        } else {
            deskSpecDTO.setMemory(request.getMemory());
        }
        deskSpecDTO.setSystemSize(Optional.ofNullable(request.getSystemDisk()).orElse(DEFAULT_SYSTEM_SIZE));
        deskSpecDTO.setPersonSize(Optional.ofNullable(request.getDataDisk()).orElse(DEFAULT_PERSON_SIZE));
        deskSpecDTO.setEnableHyperVisorImprove(Optional.ofNullable(request.getEnableHyperVisorImprove()).orElse(Boolean.TRUE));
        deskSpecDTO.setSystemDiskStoragePoolId(request.getStoragePoolId());
        if (deskSpecDTO.getPersonSize() > 0) {
            deskSpecDTO.setPersonDiskStoragePoolId(Optional.ofNullable(request.getDataDiskStoragePoolId())
                    .orElse(request.getStoragePoolId()));
        }
        if (StringUtils.isNotEmpty(request.getVgpuModel())) {
            deskSpecDTO.setVgpuInfoDTO(deskSpecAPI.getVGpuByModel(request.getClusterId(), request.getVgpuModel()));
        } else {
            deskSpecDTO.setVgpuInfoDTO(new VgpuInfoDTO());
        }
        List<CbbAddExtraDiskDTO> addExtraDiskDTOList = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(request.getExtraDiskArr())) {
            RestDeskCreateExtraDiskDTO createExtraDiskDTO;
            CbbAddExtraDiskDTO cbbAddExtraDiskDTO;
            for (int i = 0; i < request.getExtraDiskArr().length; i++) {
                createExtraDiskDTO = request.getExtraDiskArr()[i];
                cbbAddExtraDiskDTO = new CbbAddExtraDiskDTO();
                cbbAddExtraDiskDTO.setAssignedStoragePoolId(createExtraDiskDTO.getAssignedStoragePoolId());
                cbbAddExtraDiskDTO.setExtraSize(createExtraDiskDTO.getExtraSize());
                cbbAddExtraDiskDTO.setIndex(i);
                addExtraDiskDTOList.add(cbbAddExtraDiskDTO);
            }
        }
        deskSpecDTO.setExtraDiskList(addExtraDiskDTOList);
        return deskSpecDTO;
    }

    public AbstractAsyncDesktopMgmtThread(UUID businessId, AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                          UserDesktopMgmtAPI userDesktopMgmtAPI) throws BusinessException {
        super(businessId, action, openApiTaskInfoAPI);
        setUserDesktopMgmtAPI(userDesktopMgmtAPI);
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public void setCbbVDIDeskStrategyMgmtAPI(CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI) {
        this.cbbVDIDeskStrategyMgmtAPI = cbbVDIDeskStrategyMgmtAPI;
    }
}
