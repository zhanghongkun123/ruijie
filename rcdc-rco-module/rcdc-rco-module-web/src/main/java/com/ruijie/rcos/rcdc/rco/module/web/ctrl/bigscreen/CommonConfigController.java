package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CommonConfigMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalOnlineTimeRecordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CommonConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.EditCommonConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ConfigKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.EditCommonConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.BigScreenPlantTreeResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.BigScreenDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.config.EditConfigWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * 大屏监控参数配置Controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月9日
 *
 * @author bairuqiang
 */
@Controller
@RequestMapping("/rco/commonConfig")
public class CommonConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonConfigController.class);

    @Autowired
    CommonConfigMgmtAPI commonConfigMgmtAPI;

    @Autowired
    TerminalOnlineTimeRecordAPI terminalOnlineTimeRecordAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 查询大屏监控参数配置详情
     *
     * @param request 空白请求
     * @return 配置参数列表
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/detail")
    public DefaultWebResponse getConfigParam(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        BigScreenDetailResponse apiResponse = commonConfigMgmtAPI.getConfigParam(new DefaultRequest());
        return DefaultWebResponse.Builder.success(apiResponse.getConfigArr());
    }

    /**
     * 大屏配置修改
     *
     * @param request 需要修改的配置项
     * @return 修改结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/edit")
    public DefaultWebResponse editConfigParam(EditConfigWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        EditCommonConfigRequest apiRequest = new EditCommonConfigRequest();
        apiRequest.setConfigArr(request.getConfigArr());
        try {
            validateConfigValue(apiRequest);
            commonConfigMgmtAPI.editConfigParam(apiRequest);
            LOGGER.info("配置修改成功");
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_UPDATE_SUCCESS);
            return DefaultWebResponse.Builder.success(BigScreenBussinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_UPDATE_SUCCESS, new String[] {});
        } catch (BusinessException ex) {
            LOGGER.error("配置修改失败", ex);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_UPDATE_FAIL, ex.getI18nMessage());
            throw new BusinessException(BigScreenBussinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_UPDATE_FAIL, ex, ex.getI18nMessage());
        }

    }

    private void validateConfigValue(EditCommonConfigRequest apiRequest) throws BusinessException {
        // 将参数设置整理成map，同时校验是否有小数
        Map<UUID, String> configMap = Maps.newHashMap();
        for (EditCommonConfigDTO dto : apiRequest.getConfigArr()) {
            if (dto.getConfigValue().contains(".")) {
                throw new BusinessException(BigScreenBussinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_VALUE_MUST_BE_INTEGER);
            }
            configMap.put(dto.getId(), dto.getConfigValue());
        }

        // 获取需要校验的配置项ID
        BigScreenDetailResponse apiResponse = commonConfigMgmtAPI.getConfigParam(new DefaultRequest());
        Map<ConfigKeyEnum, UUID> allConfigDTOMap =
                Stream.of(apiResponse.getConfigArr()).collect(Collectors.toMap(CommonConfigDTO::getConfigKey, CommonConfigDTO::getId));
        UUID cpuWarnId = allConfigDTOMap.get(ConfigKeyEnum.BIGSCREEN_DESKTOP_WARN_CPU);
        UUID memoryWarnId = allConfigDTOMap.get(ConfigKeyEnum.BIGSCREEN_DESKTOP_WARN_MEMORY);
        UUID diskWarnId = allConfigDTOMap.get(ConfigKeyEnum.BIGSCREEN_DESKTOP_WARN_DISK);
        UUID cpuAlarmId = allConfigDTOMap.get(ConfigKeyEnum.BIGSCREEN_DESKTOP_ALARM_CPU);
        UUID memoryAlarmId = allConfigDTOMap.get(ConfigKeyEnum.BIGSCREEN_DESKTOP_ALARM_MEMORY);
        UUID diskAlarmId = allConfigDTOMap.get(ConfigKeyEnum.BIGSCREEN_DESKTOP_ALARM_DISK);

        StringBuilder validateInfo = new StringBuilder();
        validateInfo.append(compare(configMap.get(cpuWarnId), configMap.get(cpuAlarmId), ResourceTypeEnum.CPU));
        validateInfo.append(compare(configMap.get(memoryWarnId), configMap.get(memoryAlarmId), ResourceTypeEnum.MEMORY));
        validateInfo.append(compare(configMap.get(diskWarnId), configMap.get(diskAlarmId), ResourceTypeEnum.DISK));

        if (validateInfo.length() > 0) {
            throw new BusinessException(BigScreenBussinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_WARN_BIGGER_THAN_ALARM, validateInfo.toString());
        }

    }

    private String compare(String warnValue, String alarmValue, ResourceTypeEnum resourceType) {
        if (null != warnValue && null != alarmValue && Integer.valueOf(warnValue) >= Integer.valueOf(alarmValue)) {
            return "[" + resourceType.name() + "]";
        }
        return "";
    }

    /**
     * 统计碳排放量
     *
     * @return 碳排放量
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/statisticsPlantTree")
    public DefaultWebResponse statisticsPlantTree() throws BusinessException {
        BigScreenPlantTreeResponse response = terminalOnlineTimeRecordAPI.findPlantTree();
        return DefaultWebResponse.Builder.success(response);
    }

}
