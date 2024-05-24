package com.ruijie.rcos.rcdc.rco.module.web.ctrl.helppage;

import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.helppage.request.UpdateViewHelpPageStateRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-08-26 21:49
 */
@Api(tags = "帮助页面行为")
@Controller
@RequestMapping("/rco/helpPage/")
public class HelpPageController {

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    private static final String KEY = "view_help_page_state";

    /**
     * 获取帮助页面状态
     * @return CommonWebResponse<FindParameterResponse>
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取帮助页面状态")
    @RequestMapping(path = "getState", method = RequestMethod.GET)
    public CommonWebResponse<FindParameterResponse> getViewHelpPageState() throws BusinessException {
        FindParameterRequest findParameterRequest = new FindParameterRequest(KEY);
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
        return CommonWebResponse.success(findParameterResponse);
    }

    /**
     * 更新帮助页面状态
     * @param request 入参
     * @return CommonWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("更新帮助页面状态")
    @RequestMapping(path = "updateState", method = RequestMethod.POST)
    public CommonWebResponse updateViewHelpPageState(UpdateViewHelpPageStateRequest request) throws BusinessException {
        Assert.notNull(request, "UpdateViewHelpPageStateRequest not is null.");
        UpdateParameterRequest updateParameterRequest = new UpdateParameterRequest(KEY, request.getValue().toString());
        rcoGlobalParameterAPI.updateParameter(updateParameterRequest);
        return CommonWebResponse.success();
    }

}
