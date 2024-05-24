package com.ruijie.rcos.rcdc.rco.module.web.ctrl.upgrade;

import com.ruijie.rcos.rcdc.rco.module.def.api.UpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.PromptVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.PromptVersionInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.upgrade.request.CancelPromptVersionWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/13 17:09
 *
 * @author ketb
 */
@Api(tags = "升级信息推送")
@Controller
@RequestMapping("/rco/upgrade")
public class UpgradeController {

    @Autowired
    private UpgradeAPI upgradeAPI;

    /**
     * 获取升级列表
     * @return 列表信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查看提示升级版本列表")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public DefaultPageResponse<PromptVersionInfoDTO> queryVersionList() throws BusinessException {
        PromptVersionDTO promptVersionDTO = upgradeAPI.queryVersionList();
        DefaultPageResponse response = new DefaultPageResponse();
        if (promptVersionDTO != null && promptVersionDTO.getVersionList() != null && promptVersionDTO.getVersionList().size() > 0) {
            // 根据升级等级排序
            List<PromptVersionInfoDTO> infoDTOList = promptVersionDTO.getVersionList()
                    .stream().sorted(Comparator.comparing(PromptVersionInfoDTO::getLevel).reversed())
                    .collect(Collectors.toList());
            response.setItemArr(infoDTOList.toArray());
            response.setTotal(infoDTOList.size());
        }

        return response;
    }

    /**
     * 取消版本提示
     * @param request 请求参数
     * @return 结果返回
     * @throws BusinessException 业务异常
     */
    @ApiOperation("取消提示版本")
    @RequestMapping(value = "cancel", method = RequestMethod.POST)
    public CommonWebResponse cancelPromptVersion(CancelPromptVersionWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        upgradeAPI.cancelPromptVersion(request.getPkgName());
        return CommonWebResponse.success();
    }
}
