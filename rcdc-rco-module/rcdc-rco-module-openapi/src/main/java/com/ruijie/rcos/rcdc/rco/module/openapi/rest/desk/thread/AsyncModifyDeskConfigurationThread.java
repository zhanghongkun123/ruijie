package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.DeskConfigurationModifyRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: 异步线程修改云桌面虚拟机配置
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/13 11:29
 *
 * @author lyb
 */
public class AsyncModifyDeskConfigurationThread extends AbstractAsyncTaskThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncModifyDeskConfigurationThread.class);

    private UUID deskId;

    private DeskConfigurationModifyRequest request;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    private CbbDeskSpecAPI cbbDeskSpecAPI;

    private DeskSpecAPI deskSpecAPI;

    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    public AsyncModifyDeskConfigurationThread(UUID deskId,
                                              AsyncTaskEnum action,
                                              OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                              CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI,
                                              CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI);
        setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI);
        setCbbVDIDeskStrategyMgmtAPI(cbbVDIDeskStrategyMgmtAPI);
        setDeskId(deskId);
    }

    @Override
    public void run() {
        try {
            process(request);
            saveTaskSuccess();
        } catch (BusinessException e) {
            LOGGER.error("AsyncModifyDeskConfigurationThread error!", e);
            saveTaskException(e);
        }
    }

    /**
     * 修改云桌面虚拟机配置业务逻辑
     *
     * @param request 请求体
     * @throws BusinessException 异常
     */
    private void process(DeskConfigurationModifyRequest request) throws BusinessException {
        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(deskId);
        CbbDeskSpecDTO cbbDeskSpecDTO = cbbDeskSpecAPI.getById(cbbDeskDTO.getDeskSpecId());
        CbbDeskStrategyVDIDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(cbbDeskDTO.getStrategyId());

        CbbUpdateDeskSpecRequest cbbUpdateReq = new CbbUpdateDeskSpecRequest();
        cbbUpdateReq.setDeskId(deskId);
        cbbUpdateReq.setCustomTaskId(customTaskId);
        cbbUpdateReq.setEnableHyperVisorImprove(cbbDeskSpecDTO.getEnableHyperVisorImprove());
        cbbUpdateReq.setCpu(checkNullGetValue(request.getCpuCores(), cbbDeskDTO.getCpu()));
        cbbUpdateReq.setMemory(checkNullGetValue(request.getMemory(), cbbDeskDTO.getMemory()));
        cbbUpdateReq.setPersonSize(Optional.ofNullable(checkNullGetValue(request.getDataDisk(), cbbDeskDTO.getPersonSize())).orElse(0));

        if (cbbUpdateReq.getPersonSize() > 0) {
            // 如果本地盘是从无到有，就默认使用系统盘的存储
            cbbUpdateReq.setPersonDiskStoragePoolId(checkNullGetValue(cbbDeskSpecDTO.getPersonDiskStoragePoolId(),
                    cbbDeskSpecDTO.getSystemDiskStoragePoolId()));
        }

        if (StringUtils.isNotEmpty(request.getVgpuModel())) {
            cbbUpdateReq.setVgpuInfoDTO(deskSpecAPI.getVGpuByModel(cbbDeskDTO.getClusterId(), request.getVgpuModel()));
        } else {
            cbbUpdateReq.setVgpuInfoDTO(new VgpuInfoDTO());
        }

        if (deskStrategy.getPattern() == CbbCloudDeskPattern.APP_LAYER) {
            if (request.getSystemDisk() == null) {
                cbbUpdateReq.setSystemSize(cbbDeskDTO.getSystemSize());
            } else {
                cbbUpdateReq.setSystemSize(request.getSystemDisk() - Constants.SYSTEM_DISK_CAPACITY_INCREASE_SIZE);
            }
        } else {
            cbbUpdateReq.setSystemSize(checkNullGetValue(request.getSystemDisk(), cbbDeskDTO.getSystemSize()));
        }

        // 云桌面策略中未开启个人盘时，不允许在此处添加个人盘、额外个人盘
        if (Objects.isNull(cbbDeskDTO.getPersonSize()) || cbbDeskDTO.getPersonSize() == 0) {
            cbbUpdateReq.setPersonSize(cbbDeskDTO.getPersonSize());
            cbbUpdateReq.setExtraDiskList(new ArrayList<>());
            LOGGER.info("编辑云桌面[{}]的规格未开启个人盘时，不允许在编辑云桌面规格中变更个人盘、额外个人盘", cbbUpdateReq.getDeskId());
        } else {
            cbbUpdateReq.setExtraDiskList(cbbVDIDeskDiskAPI.listDeskExtraDisk(cbbUpdateReq.getDeskId()));
        }

        //有改变才动作
        if (!checkIsNotChange(cbbUpdateReq, cbbDeskDTO)) {
            cbbUpdateReq.setEnableCustom(true);
            LOGGER.info("编辑云桌面[{}]的规格:{}", cbbUpdateReq.getDeskId(), JSON.toJSONString(cbbUpdateReq));
            cbbVDIDeskMgmtAPI.updateDeskSpec(cbbUpdateReq);
        }
    }

    /**
     * 获取值
     *
     * @param nullableObj 首选值
     * @param defaultObj  首选值为null时的默认值
     * @return Object
     */
    private <T> T checkNullGetValue(T nullableObj, T defaultObj) {
        return Optional.ofNullable(nullableObj).orElse(defaultObj);
    }

    /**
     * 判断是否有修改
     *
     * @param cbbUpdateReq
     * @param cbbDeskDTO
     * @return
     */
    private boolean checkIsNotChange(CbbUpdateDeskSpecRequest cbbUpdateReq, CbbDeskDTO cbbDeskDTO) {
        return Objects.equals(cbbUpdateReq.getCpu(), cbbDeskDTO.getCpu()) && Objects.equals(cbbUpdateReq.getMemory(), cbbDeskDTO.getMemory())
                && Objects.equals(cbbUpdateReq.getPersonSize(), cbbDeskDTO.getPersonSize())
                && Objects.equals(cbbUpdateReq.getSystemSize(), cbbDeskDTO.getSystemSize());
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public void setRequest(DeskConfigurationModifyRequest request) {
        this.request = request;
    }

    public void setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
    }

    public void setCbbVDIDeskStrategyMgmtAPI(CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI) {
        this.cbbVDIDeskStrategyMgmtAPI = cbbVDIDeskStrategyMgmtAPI;
    }

    public void setCbbDeskSpecAPI(CbbDeskSpecAPI cbbDeskSpecAPI) {
        this.cbbDeskSpecAPI = cbbDeskSpecAPI;
    }

    public void setDeskSpecAPI(DeskSpecAPI deskSpecAPI) {
        this.deskSpecAPI = deskSpecAPI;
    }

    public void setCbbVDIDeskDiskAPI(CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI) {
        this.cbbVDIDeskDiskAPI = cbbVDIDeskDiskAPI;
    }
}
