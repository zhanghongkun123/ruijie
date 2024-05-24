package com.ruijie.rcos.rcdc.rco.module.web.ctrl.physicalserver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.physicalserver.dto.ListAllPhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.physicalserver.request.GetPhysicalServerDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.physicalserver.request.SyncPhysicalServerWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.PhysicalServerSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.PageResponseContent;

/**
 * Description: 服务器管理
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-07-25
 *
 * @author hli
 */
@Controller
@RequestMapping("/rco/clouddesktop/physicalServer")
public class PhysicalServerCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhysicalServerCtrl.class);

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    /**
     * 同步服务器列表
     *
     * @param webRequest     webRequest
     * @return com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse
     * @throws BusinessException 业务异常
     * @author hli on 2019-07-26
     */
    @RequestMapping(value = "sync", method = RequestMethod.POST)
    public DefaultWebResponse syncPhysicalServer(SyncPhysicalServerWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest cannot be null");
        try {
            cbbPhysicalServerMgmtAPI.syncPhysicalServerFromHci();
            auditLogAPI.recordLog(PhysicalServerBusinessKey.RCDC_CLOUDDESKTOP_PHYSICALSERVER_SYNC_SUCCESS_LOG);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(PhysicalServerBusinessKey.RCDC_CLOUDDESKTOP_PHYSICALSERVER_SYNC_FAIL_LOG, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[]{});
    }


    /**
     * 获取指定服务器详细信息
     *
     * @param webRequest webRequest
     * @return com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse
     * @throws BusinessException 业务异常
     * @author hli on 2019-07-26
     */
    @RequestMapping(value = "getInfo", method = RequestMethod.POST)
    public DefaultWebResponse getPhysicalServerDetail(GetPhysicalServerDetailRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest cannot be null");
        try {
            PhysicalServerDTO serverDTO = cbbPhysicalServerMgmtAPI.getPhysicalServer(webRequest.getId());
            return DefaultWebResponse.Builder.success(serverDTO);
        } catch (BusinessException e) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 分页查询虚拟网络
     *
     * @param webRequest 前端请求信息
     * @return 虚拟网络列表
     */
    @RequestMapping("list")
    public DefaultWebResponse listPhysicalServer(PageWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");

        LOGGER.info("rcv listPhysicalServer msg: {}", JSONObject.toJSONString(webRequest));
        PageSearchRequest pageSearchRequest = new PhysicalServerSearchRequest(webRequest);
        DefaultPageResponse<PhysicalServerDTO> response = cbbPhysicalServerMgmtAPI.listPhysicalServer(pageSearchRequest);
        PageResponseContent<PhysicalServerDTO> pageResponseContent = new PageResponseContent<>(response.getItemArr(), response.getTotal());
        return DefaultWebResponse.Builder.success(pageResponseContent);
    }

    /**
     * 查询所有服务器列表信息
     *
     * @param webRequest 前端请求信息
     * @return 服务器信息列表
     */
    @RequestMapping("listAll")
    public DefaultWebResponse listAllPhysicalServer(DefaultWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest can not be null");

        ListAllPhysicalServerDTO serverDTO = new ListAllPhysicalServerDTO();
        List<PhysicalServerDTO> dtoList = cbbPhysicalServerMgmtAPI.listAllPhysicalServer(true);
        serverDTO.setPhysicalServerDTOList(dtoList);

        return DefaultWebResponse.Builder.success(serverDTO);
    }


}
