package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserDeskStrategyRecommendAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskStrategyRecommendDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskStrategyRecommendQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DeskStrategyRecommendDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.DeskStrategyRecommendDetailWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.DeskStrategyRecommendEditWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <br>
 * Description: 云桌面策略推荐 <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/4/3 <br>
 *
 * @author yyz
 */
@Controller
@RequestMapping("/rco/user/strategyRecommend")
public class DeskStrategyRecommendController {

    @Autowired
    private UserDeskStrategyRecommendAPI deskStrategyRecommendAPI;

    /**
     * 分页获取当前推荐的云桌面策略信息
     * 
     * @param webRequest 分页参数
     * @return 结果
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse list(PageWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        DeskStrategyRecommendQueryRequest request = new DeskStrategyRecommendQueryRequest();
        BeanUtils.copyProperties(webRequest, request);
        DefaultPageResponse<DeskStrategyRecommendDTO> pageResponse = deskStrategyRecommendAPI.pageQuery(request);

        return DefaultWebResponse.Builder.success(pageResponse);
    }

    /**
     * 使用推荐策略创建云桌面策略
     * 
     * @param webRequest 云桌面id数组
     * @return 默认返回值
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "edit")
    public DefaultWebResponse edit(DeskStrategyRecommendEditWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");

        deskStrategyRecommendAPI.deskStrategyRecommendEdit(webRequest.getIdArr());

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 获取一条云桌面策略信息
     * 
     * @param webRequest 请求参数id
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "detail")
    public DefaultWebResponse detail(DeskStrategyRecommendDetailWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");

        DeskStrategyRecommendDetailResponse response = deskStrategyRecommendAPI.deskStrategyRecommendDetail(webRequest.getId());

        return DefaultWebResponse.Builder.success(response);
    }
}
