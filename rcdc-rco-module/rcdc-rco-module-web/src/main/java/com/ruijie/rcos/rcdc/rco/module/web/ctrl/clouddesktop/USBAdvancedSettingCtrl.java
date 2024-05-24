package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBAdvancedSettingMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbAutoRedirectConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCameraConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateAutoRedirectConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateSpecialDeviceConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateSpecialDeviceDriverConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbSpecialDeviceConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbSpecialDeviceDriverConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateCameraConfigStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DeleteAutoRedirectConfigBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DeleteSpecialDeviceConfigBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.DeleteSpecialDeviceDriverConfigBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting.AutoRedirectConfigStrategyVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting.CameraConfigStrategyVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting.SpecialDeviceConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting.SpecialDeviceDriverConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting.SpecialDeviceDriverStrategyVO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.PageResponseContent;

/**
 * USB高级配置Controller
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月16日
 *
 * @author zhuangchenwu
 */
@Controller
@RequestMapping("/rco/usbAdvancedSetting")
public class USBAdvancedSettingCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(USBAdvancedSettingCtrl.class);

    @Autowired
    private CbbUSBAdvancedSettingMgmtAPI cbbUSBAdvancedSettingMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    private static final String HEX_STRING_PREFIX = "0x";

    private static final String STRING_0 = "0";

    private static final int INT_4 = 4;

    private static final String STRING_NEGATIVE_1 = "-1";

    /**
     * 查询摄像头配置策略信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "cameraConfigStrategy/detail", method = RequestMethod.POST)
    public DefaultWebResponse getCameraConfigStrategyDetail(GetCameraConfigStrategyDetailWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        CbbCameraConfigDTO cameraConfigDTO =
                cbbUSBAdvancedSettingMgmtAPI.getCameraConfigStrategyDetail();
        CameraConfigStrategyVO cameraVO = new CameraConfigStrategyVO();
        cameraVO.setBandwidth(Integer.valueOf(cameraConfigDTO.getBandwidth()));
        cameraVO.setEnableOpenVirtualCamera(
                Boolean.valueOf(cameraConfigDTO.getIsOpenVirtualCamera()));
        return DefaultWebResponse.Builder.success(cameraVO);
    }

    /**
     * 更新摄像头配置策略信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "cameraConfigStrategy/edit", method = RequestMethod.POST)
    public DefaultWebResponse updateCameraConfigStrategy(UpdateCameraConfigStrategyWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        CbbUpdateCameraConfigStrategyDTO apiRequest = new CbbUpdateCameraConfigStrategyDTO();
        apiRequest.setBandwidth(webRequest.getBandwidth());
        try {
            cbbUSBAdvancedSettingMgmtAPI.updateCameraConfigStrategy(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CAMERA_CONFIG_STRATEGY_UPDATE_SUCCESS);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_CAMERA_CONFIG_STRATEGY_UPDATE_FAIL, e,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 查询特定设备驱动配置策略信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceDriverConfigStrategy/detail", method = RequestMethod.POST)
    public DefaultWebResponse getSpecialDeviceDriverConfigStrategyDetail(
            GetSpecialDeviceDriverConfigStrategyDetailWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        String defaultDeviceDriverType =
                cbbUSBAdvancedSettingMgmtAPI.getSpecialDeviceDriverConfigStrategyDetail();
        SpecialDeviceDriverStrategyVO strategyVO = new SpecialDeviceDriverStrategyVO();
        strategyVO.setDefaultDeviceDriverType(defaultDeviceDriverType);
        return DefaultWebResponse.Builder.success(strategyVO);
    }

    /**
     * 更新特定设备驱动配置策略信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceDriverConfigStrategy/edit", method = RequestMethod.POST)
    public DefaultWebResponse updateSpecialDeviceDriverConfigStrategy(
            UpdateSpecialDeviceDriverConfigStrategyWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        try {
            cbbUSBAdvancedSettingMgmtAPI.updateSpecialDeviceDriverConfigStrategy(webRequest.getDefaultDeviceDriverType());
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_STRATEGY_UPDATE_SUCCESS);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_STRATEGY_UPDATE_FAIL,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 添加特定设备驱动配置信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceDriverConfig/create", method = RequestMethod.POST)
    public DefaultWebResponse createSpecialDeviceDriverConfig(CreateSpecialDeviceDriverConfigWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        CbbCreateSpecialDeviceDriverConfigDTO apiRequest = new CbbCreateSpecialDeviceDriverConfigDTO();
        String firmFlag = webRequest.getFirmFlag();
        String productFlag = webRequest.getProductFlag();
        apiRequest.setFirmFlag(firmFlag);
        apiRequest.setProductFlag(productFlag);
        apiRequest.setDeviceDriverType(webRequest.getDeviceDriverType());

        firmFlag = obtainHexFlagValue(firmFlag);
        productFlag = obtainHexFlagValue(productFlag);
        try {
            cbbUSBAdvancedSettingMgmtAPI.createSpecialDeviceDriverConfig(apiRequest);
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_CREATE_SUCCESS, firmFlag,
                    productFlag);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_CREATE_FAIL, firmFlag,
                    productFlag, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 删除特定设备驱动配置信息
     *
     * @param webRequest     请求参数
     * @param builder        builder
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceDriverConfig/delete", method = RequestMethod.POST)
    public DefaultWebResponse deleteSpecialDeviceDriverConfig(DeleteSpecialDeviceDriverConfigWebRequest webRequest,
                                                              BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        Assert.notNull(builder, "builder is null");
        final UUID[] idArr = webRequest.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleSpecialDeviceDriverConfig(idArr[0]);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(LocaleI18nResolver.resolve(
                                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_ITEM_NAME))
                            .build())
                    .iterator();
            DeleteSpecialDeviceDriverConfigBatchHandler handler = new DeleteSpecialDeviceDriverConfigBatchHandler(
                    iterator, auditLogAPI, cbbUSBAdvancedSettingMgmtAPI);
            BatchTaskSubmitResult result = builder.setTaskName(
                            CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_TASK_NAME)
                    .setTaskDesc(
                            CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_BATCH_DELETE_TASK_DESC)
                    .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleSpecialDeviceDriverConfig(UUID id) throws BusinessException {
        CbbSpecialDeviceDriverConfigDTO configDTO = getSpecialDeviceDriverConfig(id);
        if (configDTO == null) {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_NOT_EXIST_DELETE_FAIL,
                    id.toString());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_NOT_EXIST_DELETE_FAIL);
        } else {
            try {
                cbbUSBAdvancedSettingMgmtAPI.deleteSpecialDeviceDriverConfig(id);
                auditLogAPI.recordLog(
                        CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_SUCCESS,
                        configDTO.getFirmFlag(), configDTO.getProductFlag());
                return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                        new String[]{});
            } catch (BusinessException e) {
                LOGGER.error("删除USB特定设备驱动配置信息出现异常，id为：" + id, e);
                return deleteSingleSpecialDeviceDriverConfigFail(id, configDTO, e);
            }
        }
    }


    private DefaultWebResponse deleteSingleSpecialDeviceDriverConfigFail(UUID id,
                                                                         CbbSpecialDeviceDriverConfigDTO configDTO,
                                                                         BusinessException e) throws BusinessException {
        if (configDTO == null) {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_NOT_EXIST_DELETE_FAIL,
                    id.toString(), e.getI18nMessage());
        } else {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_DRIVER_CONFIG_DELETE_SUCCESS,
                    configDTO.getFirmFlag(), configDTO.getProductFlag(), e.getI18nMessage());
        }
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
    }

    /**
     * 查询特定设备驱动配置分页信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceDriverConfig/list", method = RequestMethod.POST)
    public DefaultWebResponse getSpecialDeviceDriverConfigPage(PageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        DefaultPageRequest pageRequest = new DefaultPageRequest();
        pageRequest.setLimit(webRequest.getLimit());
        pageRequest.setPage(webRequest.getPage());
        DefaultPageResponse<CbbSpecialDeviceDriverConfigDTO> pageResponse =
                cbbUSBAdvancedSettingMgmtAPI.pageQuery(pageRequest);
        SpecialDeviceDriverConfigVO[] voArr = buildSpecialDeviceDriverConfigVOArr(pageResponse);
        PageResponseContent<SpecialDeviceDriverConfigVO> pageResponseContent =
                new PageResponseContent<>(voArr, pageResponse.getTotal());
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }

    private SpecialDeviceDriverConfigVO[] buildSpecialDeviceDriverConfigVOArr(
            DefaultPageResponse<CbbSpecialDeviceDriverConfigDTO> response) {
        SpecialDeviceDriverConfigVO[] voArr = new SpecialDeviceDriverConfigVO[response.getItemArr().length];
        for (int i = 0; i < response.getItemArr().length; i++) {
            CbbSpecialDeviceDriverConfigDTO dto = response.getItemArr()[i];
            SpecialDeviceDriverConfigVO vo = new SpecialDeviceDriverConfigVO();
            vo.setId(dto.getId());
            vo.setFirmFlag(dto.getFirmFlag());
            vo.setProductFlag(dto.getProductFlag());
            vo.setDeviceDriverType(dto.getDeviceDriverType().getDeviceDriverTypeName());
            voArr[i] = vo;
        }
        return voArr;
    }

    /**
     * 添加特定设备配置信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceConfig/create", method = RequestMethod.POST)
    public DefaultWebResponse createSpecialDeviceConfig(CreateSpecialDeviceConfigWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        CbbCreateSpecialDeviceConfigDTO apiRequest = new CbbCreateSpecialDeviceConfigDTO();
        BeanUtils.copyProperties(webRequest, apiRequest);
        apiRequest.setIsRestore(webRequest.getEnableRestore());
        apiRequest.setIsReuse(webRequest.getEnableReuse());
        apiRequest.setIsPcRedirect(webRequest.getEnablePcRedirect());

        String firmFlag = obtainHexFlagValue(webRequest.getFirmFlag());
        String productFlag = obtainHexFlagValue(webRequest.getProductFlag());
        try {
            cbbUSBAdvancedSettingMgmtAPI.createSpecialDeviceConfig(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_CREATE_SUCCESS,
                    firmFlag, productFlag);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_CREATE_FAIL,
                    firmFlag, productFlag, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 删除特定设备配置信息
     *
     * @param webRequest     请求参数
     * @param builder        builder
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceConfig/delete", method = RequestMethod.POST)
    public DefaultWebResponse deleteSpecialDeviceDriverConfig(DeleteSpecialDeviceConfigWebRequest webRequest,
                                                              BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        Assert.notNull(builder, "builder is null");

        final UUID[] idArr = webRequest.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleSpecialDeviceConfig(idArr[0]);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(LocaleI18nResolver.resolve(
                                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_BATCH_DELETE_ITEM_NAME))
                            .build())
                    .iterator();
            DeleteSpecialDeviceConfigBatchHandler handler =
                    new DeleteSpecialDeviceConfigBatchHandler(iterator, auditLogAPI, cbbUSBAdvancedSettingMgmtAPI);
            BatchTaskSubmitResult result = builder
                    .setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_BATCH_DELETE_TASK_NAME)
                    .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_BATCH_DELETE_TASK_DESC)
                    .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleSpecialDeviceConfig(UUID id) throws BusinessException {
        CbbSpecialDeviceConfigDTO configDTO = getSpecialDeviceConfig(id);
        try {
            cbbUSBAdvancedSettingMgmtAPI.deleteSpecialDeviceConfig(id);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_DELETE_SUCCESS,
                    Objects.requireNonNull(configDTO).getFirmFlag(), configDTO.getProductFlag());
        } catch (BusinessException e) {
            LOGGER.error("删除USB特定设备配置信息出现异常，id为：" + id, e);
            deleteSingleSpecialDeviceConfigFail(id, configDTO, e);
        }
        return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                new String[]{});
    }

    private void deleteSingleSpecialDeviceConfigFail(UUID id, CbbSpecialDeviceConfigDTO configDTO, BusinessException e) throws BusinessException {
        if (configDTO == null) {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_NOT_EXIST_DELETE_FAIL,
                    id.toString(), e.getI18nMessage());
        } else {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_SPECIAL_DEVICE_CONFIG_DELETE_FAIL,
                    configDTO.getFirmFlag(), configDTO.getProductFlag(), e.getI18nMessage());
        }
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
    }

    /**
     * 查询特定设备配置分页信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "specialDeviceConfig/list", method = RequestMethod.POST)
    public DefaultWebResponse getSpecialDeviceConfigPage(PageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        DefaultPageRequest pageRequest = new DefaultPageRequest();
        pageRequest.setLimit(webRequest.getLimit());
        pageRequest.setPage(webRequest.getPage());
        DefaultPageResponse<CbbSpecialDeviceConfigDTO> pageResponse =
                cbbUSBAdvancedSettingMgmtAPI.pageQuerySpecialDeviceConfigPage(pageRequest);
        SpecialDeviceConfigVO[] voArr = buildSpecialDeviceConfigVOArr(pageResponse);
        PageResponseContent<SpecialDeviceConfigVO> pageResponseContent =
                new PageResponseContent<>(voArr, pageResponse.getTotal());
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }

    private SpecialDeviceConfigVO[] buildSpecialDeviceConfigVOArr(
            DefaultPageResponse<CbbSpecialDeviceConfigDTO> response) {
        SpecialDeviceConfigVO[] voArr = new SpecialDeviceConfigVO[response.getItemArr().length];
        for (int i = 0; i < response.getItemArr().length; i++) {
            CbbSpecialDeviceConfigDTO dto = response.getItemArr()[i];
            SpecialDeviceConfigVO vo = new SpecialDeviceConfigVO();
            vo.setId(dto.getId());
            vo.setFirmFlag(dto.getFirmFlag());
            vo.setProductFlag(dto.getProductFlag());
            vo.setSpecialDeviceFlag(dto.getSpecialDeviceFlag());
            vo.setRequestType(dto.getRequestType());
            vo.setRequest(dto.getRequest());
            vo.setValue(dto.getValue());
            vo.setIndex(dto.getIndex());
            vo.setStatus(dto.getStatus());
            vo.setConfStr(dto.getConfStr());
            vo.setCustom(dto.getCustom());
            vo.setEnableRestore(dto.getIsRestore());
            vo.setEnableReuse(dto.getIsReuse());
            vo.setEnablePcRedirect(dto.getIsPcRedirect());
            voArr[i] = vo;
        }
        return voArr;
    }

    /**
     * 查询自动重定向配置策略信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "autoRedirectConfigStrategy/detail", method = RequestMethod.POST)
    public DefaultWebResponse getAutoRedirectConfigStrategyDetail(GetAutoRedirectConfigStrategyDetailWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        String autoRedirectType =
                cbbUSBAdvancedSettingMgmtAPI.getAutoRedirectConfigStrategyDetail();
        AutoRedirectConfigStrategyVO strategyVO = new AutoRedirectConfigStrategyVO();
        strategyVO.setAutoRedirectType(autoRedirectType);
        return DefaultWebResponse.Builder.success(strategyVO);
    }

    /**
     * 更新自动重定向配置策略信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "autoRedirectConfigStrategy/edit", method = RequestMethod.POST)
    public DefaultWebResponse updateAutoRedirectConfigStrategy(UpdateAutoRedirectConfigStrategyWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        try {
            cbbUSBAdvancedSettingMgmtAPI.updateAutoRedirectConfigStrategy(webRequest.getAutoRedirectType());
            LOGGER.info("修改USB重定向成功，修改为{}", webRequest.getAutoRedirectType().name());
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_STRATEGY_UPDATE_SUCCESS);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_STRATEGY_UPDATE_FAIL,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 添加自动重定向配置信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "autoRedirectConfig/create", method = RequestMethod.POST)
    public DefaultWebResponse createAutoRedirectConfig(CreateAutoRedirectConfigWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        CbbCreateAutoRedirectConfigDTO apiRequest = new CbbCreateAutoRedirectConfigDTO();
        String firmFlag = webRequest.getFirmFlag();
        String productFlag = webRequest.getProductFlag();
        apiRequest.setFirmFlag(firmFlag);
        apiRequest.setProductFlag(productFlag);

        firmFlag = obtainHexFlagValue(firmFlag);
        productFlag = obtainHexFlagValue(productFlag);
        try {
            cbbUSBAdvancedSettingMgmtAPI.createAutoRedirectConfig(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_CREATE_SUCCESS, firmFlag,
                    productFlag);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                    new String[]{});
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_USBDEVICE_CREATE_FAIL, firmFlag,
                    productFlag, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 删除自动重定向配置信息
     *
     * @param webRequest     请求参数
     * @param builder        builder
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "autoRedirectConfig/delete", method = RequestMethod.POST)
    public DefaultWebResponse deleteAutoRedirectConfig(DeleteAutoRedirectConfigWebRequest webRequest,
                                                       BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        Assert.notNull(builder, "builder is null");
        final UUID[] idArr = webRequest.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleAutoRedirectConfig(idArr[0]);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(LocaleI18nResolver.resolve(
                                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_ITEM_NAME))
                            .build())
                    .iterator();
            DeleteAutoRedirectConfigBatchHandler handler =
                    new DeleteAutoRedirectConfigBatchHandler(iterator, auditLogAPI, cbbUSBAdvancedSettingMgmtAPI);
            BatchTaskSubmitResult result = builder
                    .setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_TASK_NAME)
                    .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_BATCH_DELETE_TASK_DESC)
                    .registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleAutoRedirectConfig(UUID id) throws BusinessException {
        CbbAutoRedirectConfigDTO configDTO = getAutoRedirectConfig(id);
        try {
            cbbUSBAdvancedSettingMgmtAPI.deleteAutoRedirectConfig(id);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_SUCCESS,
                    Objects.requireNonNull(configDTO).getFirmFlag(), configDTO.getProductFlag());
        } catch (BusinessException e) {
            LOGGER.error("删除USB自动重定向配置信息出现异常，id为：" + id, e);
            deleteSingleAutoRedirectConfigFail(id, configDTO, e);
        }
        return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS,
                new String[]{});
    }

    private void deleteSingleAutoRedirectConfigFail(UUID id, CbbAutoRedirectConfigDTO configDTO, BusinessException e) throws BusinessException {
        if (configDTO == null) {
            auditLogAPI.recordLog(
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_NOT_EXIST_DELETE_FAIL, id.toString(),
                    e.getI18nMessage());
        } else {
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_AUTO_REDIRECT_CONFIG_DELETE_FAIL,
                    configDTO.getFirmFlag(), configDTO.getProductFlag(), e.getI18nMessage());
        }
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
    }

    /**
     * 查询自动重定向配置分页信息
     *
     * @param webRequest     请求参数
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "autoRedirectConfig/list", method = RequestMethod.POST)
    public DefaultWebResponse getAutoRedirectConfigPage(PageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "the webRequest must not be null");
        DefaultPageRequest pageRequest = new DefaultPageRequest();
        pageRequest.setLimit(webRequest.getLimit());
        pageRequest.setPage(webRequest.getPage());
        DefaultPageResponse<CbbAutoRedirectConfigDTO> pageResponse =
                cbbUSBAdvancedSettingMgmtAPI.pageQueryAutoRedirectConfigPage(pageRequest);
        return DefaultWebResponse.Builder.success(pageResponse);
    }

    private CbbAutoRedirectConfigDTO getAutoRedirectConfig(UUID id) {
        CbbAutoRedirectConfigDTO configDTO = null;
        try {
            configDTO = cbbUSBAdvancedSettingMgmtAPI.getAutoRedirectConfig(id);
        } catch (Exception e) {
            LOGGER.error("查询USB自动重定向配置信息出现异常，id为：" + id, e);
        }
        return configDTO;
    }

    private CbbSpecialDeviceConfigDTO getSpecialDeviceConfig(UUID id) {
        CbbSpecialDeviceConfigDTO configDTO = null;
        try {
            configDTO = cbbUSBAdvancedSettingMgmtAPI.getSpecialDeviceConfig(id);
        } catch (Exception e) {
            LOGGER.error("查询USB特定设备配置信息出现异常，id为：" + id, e);
        }
        return configDTO;
    }

    private CbbSpecialDeviceDriverConfigDTO getSpecialDeviceDriverConfig(UUID id) {
        CbbSpecialDeviceDriverConfigDTO specialDeviceDriverConfigDTO = null;
        try {
            specialDeviceDriverConfigDTO = cbbUSBAdvancedSettingMgmtAPI.getSpecialDeviceDriverConfig(id);
        } catch (Exception e) {
            LOGGER.error("查询USB特定设备驱动配置信息出现异常，id为：" + id, e);
        }
        return specialDeviceDriverConfigDTO;
    }

    private String obtainHexFlagValue(String flagValue) {
        if (!StringUtils.equals(flagValue, STRING_NEGATIVE_1)) {
            return convertHexString(flagValue, INT_4);
        }
        return flagValue;
    }

    private String convertHexString(String hexString, int bitNum) {
        if (bitNum < hexString.length()) {
            throw new IllegalArgumentException(
                    "params is illegal, hexString is : " + hexString + ", bitNum is : " + bitNum);
        }
        StringBuilder convertStringBuilder = new StringBuilder(HEX_STRING_PREFIX);
        for (int i = 0; i < bitNum - hexString.length(); i++) {
            convertStringBuilder.append(STRING_0);
        }
        return convertStringBuilder.append(hexString).toString();
    }
}
