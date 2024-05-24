package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWatermarkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.WatermarkMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.WatermarkDisplayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.WatermarkDisplayContentDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy.EditWatermarkWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetWatermarkConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description: 编辑水印配置、水印配置回填、下发水印配置
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月26日
 *
 * @author XiaoJiaXin
 */
@Controller
@RequestMapping("/rco/globalStrategy/watermark")
public class WatermarkConfigController {

    @Autowired
    private CbbWatermarkMgmtAPI cbbWatermarkMgmtAPI;

    @Autowired
    private WatermarkMessageAPI watermarkMessageAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(WatermarkConfigController.class);

    /**
     * 编辑屏幕水印配置
     *
     * @param request        web请求
     * @return 回复结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/edit")
    public DefaultWebResponse editWatermark(EditWatermarkWebRequest request) throws BusinessException {
        Assert.notNull(request, "edit watermark request cannot be null");
        // 接收水印配置信息，保存到数据库
        CbbWatermarkConfigDTO cbbWatermarkConfigDTO = new CbbWatermarkConfigDTO();
        cbbWatermarkConfigDTO.setEnable(request.getEnable());
        cbbWatermarkConfigDTO.setEnableDarkWatermark(request.getEnableDarkWatermark());
        cbbWatermarkConfigDTO.setDisplayContent(JSON.toJSONString(request.getDisplayContent()));

        CbbWatermarkDisplayConfigDTO cbbDisplayConfigDTO = new CbbWatermarkDisplayConfigDTO();
        BeanUtils.copyProperties(request.getDisplayConfig(), cbbDisplayConfigDTO);
        cbbWatermarkConfigDTO.setDisplayConfig(cbbDisplayConfigDTO);
        try {
            cbbWatermarkMgmtAPI.editWatermark(cbbWatermarkConfigDTO);
            watermarkMessageAPI.notifyConfigUpdate();
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_WATERMARK_SUCCESS);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (Exception e) {
            LOGGER.error("编辑云桌面水印失败", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_WATERMARK_FAIL);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e);
        }
    }

    /**
     * 下发屏幕水印配置
     *
     * @param request        web请求
     * @return 回复结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/send")
    public DefaultWebResponse sendWatermark(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "send watermark request cannot be null");
        // 下发水印配置信息到所有在线云桌面
        try {
            watermarkMessageAPI.send();
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_WATERMARK_SEND_SUCCESS);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("水印发送失败", e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_WATERMARK_SEND_FAIL);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 回填屏幕水印表项
     *
     * @param request web请求
     * @return 回复结果
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse detail(DefaultWebRequest request) {
        Assert.notNull(request, "detail watermark config request can not be null");

        CbbWatermarkConfigDTO watermarkConfigDTO = cbbWatermarkMgmtAPI.getWatermarkConfig();
        if (watermarkConfigDTO == null) {
            throw new IllegalArgumentException("获取水印配置异常");
        }
        GetWatermarkConfigResponse watermarkConfigResponse = new GetWatermarkConfigResponse();
        watermarkConfigResponse.setEnable(watermarkConfigDTO.getEnable());
        watermarkConfigResponse.setEnableDarkWatermark(watermarkConfigDTO.getEnableDarkWatermark());
        WatermarkDisplayContentDTO displayContentDTO = JSON.parseObject(watermarkConfigDTO.getDisplayContent(), WatermarkDisplayContentDTO.class);
        watermarkConfigResponse.setDisplayContent(displayContentDTO);
        WatermarkDisplayConfigDTO displayConfig = new WatermarkDisplayConfigDTO();
        BeanUtils.copyProperties(watermarkConfigDTO.getDisplayConfig(), displayConfig);
        watermarkConfigResponse.setDisplayConfig(displayConfig);
        return DefaultWebResponse.Builder.success(watermarkConfigResponse);
    }
}
