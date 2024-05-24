package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbCreateDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDISpecDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserGroupDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Description: 自定义桌面规格功能升级数据初始化
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/31
 *
 * @author linke
 */
@Service
public class DeskSpecUpgradeInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSpecUpgradeInitializer.class);

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Override
    public void safeInit() {
        // 全局配置表标识查询
        String updateFlag = globalParameterAPI.findParameter(Constants.NEED_UPGRADE_DESK_SPEC_INFO);
        if (!Boolean.parseBoolean(updateFlag)) {
            LOGGER.info("初始化处理自定义桌面规格升级数据处理已完成，无需处理");
            return;
        }
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOGGER.info("开始启动初始化处理自定义桌面规格升级数据处理任务");
        // 处理桌面和桌面池的Spec信息
        cbbDeskSpecAPI.initDesktopAndPoolDeskSpec();
        // 用户组规格信息
        initUserGroupDeskSpecData();
        globalParameterAPI.updateParameter(Constants.NEED_UPGRADE_DESK_SPEC_INFO, Boolean.FALSE.toString());
        LOGGER.info("初始化处理自定义桌面规格升级数据处理完成，更新标志位为false，耗时[{}]毫秒", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    private void initUserGroupDeskSpecData() {
        // 用户组规格信息
        List<UserGroupDesktopConfigDTO> groupConfigList = userDesktopConfigAPI.getUserGroupDesktopConfigListByDeskType(UserCloudDeskTypeEnum.VDI);
        if (CollectionUtils.isEmpty(groupConfigList)) {
            return;
        }
        for (UserGroupDesktopConfigDTO groupConfig : groupConfigList) {
            if (Objects.isNull(groupConfig.getStrategyId()) || Objects.nonNull(groupConfig.getDeskSpecId())) {
                // 无需构造spec信息记录
                continue;
            }
            saveUserGroupDesktopConfigDeskSpec(groupConfig);
        }
    }

    private void saveUserGroupDesktopConfigDeskSpec(UserGroupDesktopConfigDTO groupConfig) {

        CbbDeskStrategyVDISpecDTO deskStrategyVDISpec;
        try {
            deskStrategyVDISpec = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDISpec(groupConfig.getStrategyId());
        } catch (BusinessException e) {
            LOGGER.error("获取用户组[{}]的原云桌面策略[{}]中规格信息失败", groupConfig.getGroupId(), groupConfig.getStrategyId(), e);
            return;
        }
        CbbCreateDeskSpecDTO createDeskSpecDTO = new CbbCreateDeskSpecDTO();
        createDeskSpecDTO.setCpu(deskStrategyVDISpec.getCpu());
        createDeskSpecDTO.setMemory(deskStrategyVDISpec.getMemory());
        createDeskSpecDTO.setSystemSize(deskStrategyVDISpec.getSystemSize());
        createDeskSpecDTO.setPersonSize(Optional.ofNullable(deskStrategyVDISpec.getPersonSize()).orElse(0));
        createDeskSpecDTO.setEnableHyperVisorImprove(Optional.ofNullable(deskStrategyVDISpec.getEnableHyperVisorImprove()).orElse(true));
        createDeskSpecDTO.setVgpuInfoDTO(deskStrategyVDISpec.getVgpuInfoDTO());
        createDeskSpecDTO.setSystemDiskStoragePoolId(groupConfig.getStoragePoolId());
        if (createDeskSpecDTO.getPersonSize() > 0) {
            createDeskSpecDTO.setPersonDiskStoragePoolId(groupConfig.getStoragePoolId());
        }
        createDeskSpecDTO.setExtraDiskList(Collections.emptyList());

        CreateUserGroupDesktopConfigRequest createGroupConfigRequest = new CreateUserGroupDesktopConfigRequest(groupConfig.getGroupId(),
                UserCloudDeskTypeEnum.VDI);
        createGroupConfigRequest.setDeskSpecId(cbbDeskSpecAPI.create(createDeskSpecDTO));
        createGroupConfigRequest.setImageTemplateId(groupConfig.getImageTemplateId());
        createGroupConfigRequest.setStrategyId(groupConfig.getStrategyId());
        createGroupConfigRequest.setNetworkId(groupConfig.getNetworkId());
        createGroupConfigRequest.setSoftwareStrategyId(groupConfig.getSoftwareStrategyId());
        createGroupConfigRequest.setUserProfileStrategyId(groupConfig.getUserProfileStrategyId());
        createGroupConfigRequest.setClusterId(groupConfig.getClusterId());
        createGroupConfigRequest.setPlatformId(groupConfig.getPlatformId());
        userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(createGroupConfigRequest);
    }
}
