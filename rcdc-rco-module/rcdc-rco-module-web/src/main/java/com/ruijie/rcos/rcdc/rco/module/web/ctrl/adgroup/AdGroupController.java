package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto.AdGroupListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.adgroup.request.AdGroupAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdGroupPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: AD域组操作相关接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-22
 *
 * @author zqj
 */
@Api(tags = "AD域组管理")
@Controller
@RequestMapping("/rco/adGroup")
public class AdGroupController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdGroupController.class);


    @Autowired
    private AdGroupPoolMgmtAPI adGroupPoolMgmtAPI;

    /**
     * 获取对应池安全组分配信息列表
     *
     * @param request 请求参数对象
     * @return 返回安全组列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取对应池安全组分配信息列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取对应池安全组分配信息列表"})})
    @RequestMapping(value = "/listWithAssignment", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<AdGroupListDTO>> getUserWithAssignmentList(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "PageWebRequest 不能为null");
        AdGroupAssignmentPageRequest pageRequest = new AdGroupAssignmentPageRequest(request);
        UUID poolId = pageRequest.getRelatedPoolId();
        String key = pageRequest.getRelatedPoolKey();
        Assert.notNull(poolId, "relatedPoolId can not be null");
        Assert.hasText(key, "key can not be null");

        DefaultPageResponse<AdGroupListDTO> pageResponse = adGroupPoolMgmtAPI.pageAdGroupWithAssignment(poolId, key, pageRequest);
        return CommonWebResponse.success(pageResponse);
    }

    /**
     * 查看池对应安全组分配信息列表
     *
     * @param request 请求参数对象
     * @return 返回安全组列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "查看池对应安全组分配信息列表")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"查看池对应安全组分配信息列表"})})
    @RequestMapping(value = "/pool/realBindAdGroup/page", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<AdGroupListDTO>> getAdGroupList(PageWebRequest request) throws BusinessException {
        Assert.notNull(request, "PageWebRequest 不能为null");
        AdGroupAssignmentPageRequest pageRequest = new AdGroupAssignmentPageRequest(request);
        UUID poolId = pageRequest.getRelatedPoolId();
        String key = pageRequest.getRelatedPoolKey();
        Assert.notNull(poolId, "relatedPoolId can not be null");
        Assert.hasText(key, "key can not be null");

        DefaultPageResponse<AdGroupListDTO> pageResponse = adGroupPoolMgmtAPI.pageQueryPoolBindAdGroup(poolId, key, pageRequest);
        return CommonWebResponse.success(pageResponse);
    }
}
