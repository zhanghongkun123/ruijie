package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppBindDeliveryListDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.AppBindDeliveryRequset;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * Description: 交付组管理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月10日
 *
 * @author zdc
 */
@Api(tags = "交付组管理")
@Controller
@RequestMapping("/rco/app/bind/delivery")
public class DeliveryCtrl {

    @Autowired
    CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    /**
     * 查询应用关联交付组列表
     * @param appBindDeliveryRequset 查询应用关联交付组列表请求
     * @param sessionContext 会话上下文
     * @return AppBindDeliveryListDTO
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "查询应用关联交付组列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"查询应用关联交付组列表"})})
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DefaultWebResponse getInfoList(AppBindDeliveryRequset appBindDeliveryRequset,
                                          SessionContext sessionContext) throws BusinessException {
        Assert.notNull(appBindDeliveryRequset, "appBindDeliveryRequset is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        AppBindDeliveryListDTO appBindDeliveryListDTO = cbbAppDeliveryMgmtAPI.getAppBindDeliveryList(appBindDeliveryRequset.getId());
        return DefaultWebResponse.Builder.success(appBindDeliveryListDTO);
    }
}
