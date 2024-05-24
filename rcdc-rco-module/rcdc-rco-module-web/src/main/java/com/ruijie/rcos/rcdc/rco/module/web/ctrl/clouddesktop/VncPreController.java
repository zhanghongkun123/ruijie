package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageTemplateEditStateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageTemplateEditStateResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryVmVncURLResultDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description: VncPreController Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/04/25
 *
 * @author chixin
 */
@Controller
@RequestMapping("/rco/clouddesktop/imageTemplate/vnc")
public class VncPreController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VncPreController.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    /**
     * * 查询VNC地址
     *
     * @param webRequest 请求查询
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "queryVncCondition")
    public DefaultWebResponse queryVmVncURL(IdWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        try {
            final CbbQueryVmVncURLResultDTO response = cbbImageTemplateMgmtAPI.queryVmVncURL(webRequest.getId());
            return DefaultWebResponse.Builder.success(response);
        } catch (BusinessException e) {
            LOGGER.error("查询vnc状态出错{}", e);
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_QUERY_VNC_CONDITION_FAIL_LOG, e, e.getI18nMessage());
        }

    }

    /**
     * 查询编辑中的镜像状态
     *
     * @param webRequest     请求参数
     * @param sessionContext sessionContext
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "queryState")
    public DefaultWebResponse queryImageTemplateEditState(IdWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        final CbbQueryImageTemplateEditStateDTO request =
                new CbbQueryImageTemplateEditStateDTO(webRequest.getId(), sessionContext.getUserId());
        final CbbQueryImageTemplateEditStateResultDTO response =
                cbbImageTemplateMgmtAPI.queryImageTemplateEditState(request);
        return DefaultWebResponse.Builder.success(response);
    }
}
