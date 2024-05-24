package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rccm;

import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RccmClusterUnifiedManageStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Description: 多集群-统一管理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/12
 *
 * @author WuShengQiang
 */
@Api(tags = "多集群-统一管理")
@Controller
@RequestMapping("rco/unifiedManage")
public class UnifiedManageController {

    @Autowired
    private RccmManageAPI rccmManageAPI;

    /**
     * 获取统一管理策略列表
     *
     * @return 策略列表
     */
    @ApiOperation("获取统一管理策略列表")
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"获取统一管理策略列表"})})
    @RequestMapping(value = "/strategy/list", method = RequestMethod.GET)
    public CommonWebResponse<List<RccmClusterUnifiedManageStrategyDTO>> strategyList() {
        List<RccmClusterUnifiedManageStrategyDTO> manageStrategyDTOList = rccmManageAPI.strategyList();
        return CommonWebResponse.success(manageStrategyDTOList);
    }
}
