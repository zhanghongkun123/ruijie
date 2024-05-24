package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyRelatedDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.request.UserProfileStrategyRelatedPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PathTree;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.UserProfileStrategyRelatedIdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.userprofile.UserProfileHelp;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Description: 用户配置策略关联信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
@Api(tags = "用户配置策略关联信息管理")
@Controller
@RequestMapping("/rco/userProfileStrategyRelated")
public class UserProfileStrategyRelatedController {

    private static final String DEFAULT_SORT_FIELD = "createTime";

    private static final String TREE_FIELD = "itemArr";

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI;

    @Autowired
    private UserProfileHelp userProfileHelp;


    /**
     * *查询
     *
     * @param request 页面请求参数
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation("路径列表")
    public CommonWebResponse<DefaultPageResponse<UserProfileStrategyRelatedDetailDTO>> list(PageWebRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        UserProfileStrategyRelatedPageSearchRequest pageSearchRequest = new UserProfileStrategyRelatedPageSearchRequest(request);
        pageSearchRequest.setSortArr(request.getSort() != null ? Collections.singletonList(request.getSort()).toArray(new Sort[0]) : null);
        return CommonWebResponse.success(userProfileMgmtAPI.userProfileStrategyRelatedPageQuery(pageSearchRequest));
    }

    /**
     * 从策略中移除路径关系
     *
     * @param idArrWebRequest 入参
     * @param builder         批处理
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("从策略中移除路径关系")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(UserProfileStrategyRelatedIdArrWebRequest idArrWebRequest,
                                    BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        UUID strategyId = idArrWebRequest.getStrategyId();

        List<UUID> desktopIdList = userProfileStrategyNotifyAPI.getRelatedDesktopIdByUserProfileStrategy(strategyId);
        // 批量删除任务
        for (UUID id : idArr) {
            userProfileMgmtAPI.deletePathFromStrategyRelated(strategyId, id);
        }
        // 通知所有云桌面
        if (!desktopIdList.isEmpty()) {
            userProfileHelp.buildPushUserProfilePathUpdate(builder, desktopIdList.toArray(new UUID[desktopIdList.size()]));
        }
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 获取用户配置策略生效的路径列表
     *
     * @param idWebRequest 策略ID
     * @return 生效路径树结构
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取用户配置策略生效的路径列表,封装成树形结构")
    @RequestMapping(value = "effectiveTree/list", method = RequestMethod.POST)
    public DefaultWebResponse getEffectiveTreeList(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        PathTree pathTree = userProfileMgmtAPI.getEffectiveUserProfilePathTree(idWebRequest.getId());
        return DefaultWebResponse.Builder.success(ImmutableMap.of(TREE_FIELD, pathTree.getChildren()));
    }
}