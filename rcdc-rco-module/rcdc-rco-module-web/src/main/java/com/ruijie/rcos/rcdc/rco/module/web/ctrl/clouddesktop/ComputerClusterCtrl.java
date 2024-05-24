package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月27日
 *
 * @author xgx
 */
@Api(tags = "计算集群")
@Controller
@RequestMapping("/rco/clouddesktop")
public class ComputerClusterCtrl {
    @Autowired
    private ComputerClusterServerMgmtAPI clusterServerMgmtAPI;

    /**
     * 获取默认计算集群信息
     * 
     * @return 计算集群信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取默认计算集群信息")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0)})
    @RequestMapping(value = "computerCluster/get", method = RequestMethod.POST)
    public DefaultWebResponse getDefaultComputerCluster() throws BusinessException {
        PlatformComputerClusterDTO computerClusterDTO = clusterServerMgmtAPI.getDefaultComputeCluster();
        return DefaultWebResponse.Builder.success(computerClusterDTO);
    }
}
