package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.utils;


import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkDisplayFieldDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.WatermarkFieldMappingValueDTO;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Description: 安全打印机工具类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月28日
 *
 * @author lihengjing
 */
public class AuditPrinterUtils {

    /**
     * 水印信息赋值
     *
     * @param cloudDesktopDTO 云桌面详情
     * @param displayContent  水印显示内容
     * @return 水印真正显示内容
     */
    public static WatermarkFieldMappingValueDTO getMappingValueDTO(CloudDesktopDTO cloudDesktopDTO, String displayContent) {
        Assert.notNull(cloudDesktopDTO, "cloudDesktopDTO must not be null");
        Assert.notNull(displayContent, "displayContent must not be null");

        WatermarkFieldMappingValueDTO mappingValue = new WatermarkFieldMappingValueDTO();
        WatermarkDisplayFieldDTO displayFieldDTO = JSON.parseObject(displayContent, WatermarkDisplayFieldDTO.class);
        mappingValue.setDeskIp(StringUtils.hasText(cloudDesktopDTO.getDesktopIp()) ? cloudDesktopDTO.getDesktopIp()
                : com.ruijie.rcos.sk.base.util.StringUtils.EMPTY);
        mappingValue.setUserName(StringUtils.hasText(cloudDesktopDTO.getUserName()) ? cloudDesktopDTO.getUserName()
                : com.ruijie.rcos.sk.base.util.StringUtils.EMPTY);
        mappingValue.setDeskName(StringUtils.hasText(cloudDesktopDTO.getDesktopName()) ? cloudDesktopDTO.getDesktopName()
                        : com.ruijie.rcos.sk.base.util.StringUtils.EMPTY);
        mappingValue.setDeskMac(StringUtils.hasText(cloudDesktopDTO.getDesktopMac()) ? cloudDesktopDTO.getDesktopMac()
                : com.ruijie.rcos.sk.base.util.StringUtils.EMPTY);
        mappingValue.setCustomContent(displayFieldDTO.getCustomContent());

        return mappingValue;
    }
}
