package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade;

import com.ruijie.rcos.base.upgrade.module.def.dto.AppUpgradeLogDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationUpgradeAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationUpgradeVersionUpdateRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.ApiOperation;

/**
 * Description: 升级包管理ctrl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月21日
 *
 * @author chenl
 */
@Controller
@RequestMapping("/rco/app/upgrade")
public class CommonUpgradeRecordeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUpgradeRecordeController.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private BaseApplicationUpgradeAPI baseApplicationUpgradeAPI;


    /**
     * 查询升级包列表
     * @param request 分页请求
     * @throws BusinessException 业务异常
     * @return 应用列表
     */
    @ApiOperation("升级任务分页查询客户端")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public DefaultWebResponse getPacketList(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        PageQueryResponse<AppUpgradeLogDTO> pageQueryResponse = baseApplicationUpgradeAPI.pageUpgrade(request);
        return DefaultWebResponse.Builder.success(pageQueryResponse);
    }
}
