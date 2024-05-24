package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateAutoRedirectConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateSpecialDeviceConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateCameraConfigStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigMigrateDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/6
 *
 * @author zhangsiming
 */

@OpenAPI
@Path("/v1/migration/conf")
public interface ConfigMigrationServer {

    /**
     * 迁移离线时间配置 处理逻辑同 /rco/terminal/offlineLoginSetting + /rco/certifiedSecurity/edit
     * 
     * @param offlineLoginConfigDTO 离线时间配置
     * @throws BusinessException 业务异常
     */
    @Path("/offline")
    @POST
    void migrateOfflineConf(OfflineLoginConfigDTO offlineLoginConfigDTO) throws BusinessException;

    /**
     * 迁移打印机配置，打印机配置的插入在Python中进行
     * 
     * @param printerConfigMigrateDTO 打印机配置
     * @throws BusinessException 业务异常
     */
    @Path("/printer")
    @POST
    void migratePrinterConf(PrinterConfigMigrateDTO printerConfigMigrateDTO) throws BusinessException;

    /**
     * 迁移特定设备驱动配置
     * 
     * @param specialDeviceDriverDTO 特定设备驱动
     * @throws BusinessException 业务异常
     */
    @Path("/usbAdvance/specialDeviceDriver")
    @POST
    void migrateSpecialDeviceDriver(SpecialDeviceDriverDTO specialDeviceDriverDTO) throws BusinessException;

    /**
     * 迁移特定设备配置
     * 
     * @param createSpecialDeviceConfigDTO 特定设备
     * @throws BusinessException 业务异常
     */
    @Path("/usbAdvance/specialDevice")
    @POST
    void migrateSpecialDevice(CbbCreateSpecialDeviceConfigDTO createSpecialDeviceConfigDTO) throws BusinessException;

    /**
     * 迁移自动重定向策略
     * 
     * @param createAutoRedirectConfigDTO 自动重定向策略
     * @throws BusinessException 业务异常
     */
    @Path("/usbAdvance/autoRedirect")
    @POST
    void migrateAutoRedirect(CbbCreateAutoRedirectConfigDTO createAutoRedirectConfigDTO) throws BusinessException;

    /**
     * 迁移摄像头策略
     * 
     * @param updateCameraConfigStrategyDTO 摄像头策略
     * @throws BusinessException 业务异常
     */
    @Path("/usb/strategy/camera")
    @POST
    void migrateUsbStrategyCamera(CbbUpdateCameraConfigStrategyDTO updateCameraConfigStrategyDTO) throws BusinessException;

    /**
     * 迁移特定设备驱动策略
     * 
     * @param usbOtherConfDeviceDriverStrategyDTO 特定设备驱动策略
     * @throws BusinessException 业务异常
     */
    @Path("/usb/strategy/driver")
    @POST
    void migrateUsbStrategyDriver(UsbOtherConfDeviceDriverStrategyDTO usbOtherConfDeviceDriverStrategyDTO) throws BusinessException;

    /**
     * 迁移自动重定向策略
     * 
     * @param usbOtherConfAutoRedirectDTO 自动重定向策略
     * @throws BusinessException 业务异常
     */
    @Path("/usb/strategy/autoRedirect")
    @POST
    void migrateUsbStrategyAutoRedirect(UsbOtherConfAutoRedirectStrategyDTO usbOtherConfAutoRedirectDTO) throws BusinessException;

    /**
     * 迁移usb设备
     * 
     * @param request usb设备
     * @throws BusinessException 业务异常
     */
    @Path("/usbDevice")
    @POST
    void migrateUsbDevice(MigrateUSBDeviceDTO request) throws BusinessException;
}
