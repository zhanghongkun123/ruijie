package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.EmptyWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.response.RccmServerConfigResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Objects;

/**
 * Description: 云桌面集群管理中心ctrl
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/20
 *
 * @author WuShengQiang
 */
@Controller
@RequestMapping("rco/maintenance/rccmManage")
public class RccmManageCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(RccmManageCtrl.class);

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 获取云桌面集群管理中心,纳管配置
     *
     * @param request 空的request
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    @ApiVersions(value = @ApiVersion(value = Version.V3_2_156, descriptions = {"获取云桌面集群管理中心,纳管配置"}))
    public DefaultWebResponse detail(EmptyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        RccmServerConfigResponse configResponse = new RccmServerConfigResponse();
        if (Objects.nonNull(rccmServerConfig)) {
            configResponse.setHasJoin(rccmServerConfig.getHasJoin() != null ? rccmServerConfig.getHasJoin() : false);
            configResponse.setServerIp(rccmServerConfig.getServerIp() != null ? rccmServerConfig.getServerIp() : "");
        }

        return DefaultWebResponse.Builder.success(configResponse);
    }

    /**
     * CDC主动退出云桌面集群管理中心纳管
     *
     * @param request 空的request
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "activeExitManage", method = RequestMethod.POST)
    @ApiVersions(value = @ApiVersion(value = Version.V3_2_156, descriptions = {"CDC主动退出云桌面集群管理中心纳管"}))
    public DefaultWebResponse activeExitManage(EmptyWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        if (Objects.isNull(rccmServerConfig) || StringUtils.isBlank(rccmServerConfig.getServerIp())) {
            throw new BusinessException(BusinessKey.RCDC_RCCM_MANAGE_CONFIG_NOT_EXIST);
        }
        rccmManageAPI.activeExitManage();
        auditLogAPI.recordLog(BusinessKey.RCDC_RCCM_EXIST_MANAGE_SUCC, rccmServerConfig.getServerIp());

        return DefaultWebResponse.Builder.success();
    }

}
