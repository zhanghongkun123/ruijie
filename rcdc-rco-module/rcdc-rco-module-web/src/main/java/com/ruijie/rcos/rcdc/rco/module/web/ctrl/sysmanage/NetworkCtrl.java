package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.NetworkAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.network.BaseUpdateNetworkRequest;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseNetworkDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.network.BaseDetailNetworkWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.network.BaseUpdateNetworkWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.util.IPv4Util;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: 网卡操作Controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 *
 * @author fyq
 */
@Controller
@RequestMapping("rco/systemConfig/network")
public class NetworkCtrl {

    @Autowired
    private NetworkAPI networkAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 获取网卡信息接口
     * 
     * @param webRequest 获取请求
     * @return 获取结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "detail")
    public DefaultWebResponse getNetworkInfo(BaseDetailNetworkWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "请求不能为空");

        BaseNetworkDTO baseNetworkDTO = networkAPI.detailNetwork();

        return DefaultWebResponse.Builder.success(baseNetworkDTO);
    }

    /**
     * 更新网卡信息接口
     * 
     * @param webRequest 更新请求
     * @return 更新结果
     * @throws BusinessException 业务异常
     */
    protected DefaultWebResponse updateNetwork(BaseUpdateNetworkWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "请求不能为空");
        IPv4Util.validateNetworkConfig(webRequest.getIp(), webRequest.getNetmask(), webRequest.getGateway());
        BaseUpdateNetworkRequest apiRequest = new BaseUpdateNetworkRequest();
        BeanUtils.copyProperties(webRequest, apiRequest);

        try {
            networkAPI.updateNetwork(apiRequest);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_NETWORK_UPDATE, apiRequest.getIp(), //
                    apiRequest.getNetmask(), //
                    apiRequest.getGateway(), //
                    apiRequest.getDns());//
        } catch (BusinessException e) {
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_NETWORK_UPDATE_FAIL, e.getI18nMessage());
            throw e;
        }

        return DefaultWebResponse.Builder.success(webRequest);
    }
}
