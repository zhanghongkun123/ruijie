package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileValidateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyViewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileTreeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask.DeleteUserProfileStrategyBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CheckUserProfileNameDuplicationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CreateUserProfileStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.QueryProfilePathRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.UserProfileStrategyListRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.response.CheckNameDuplicationForUserProfileResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.userprofile.UserProfileHelp;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: 用户配置策略
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/14
 *
 * @author WuShengQiang
 */
@Api(tags = "用户配置策略管理")
@Controller
@RequestMapping("/rco/userProfileStrategy")
public class UserProfileStrategyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileStrategyController.class);

    private static final String TREE_FIELD = "itemArr";

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private UserProfileValidateAPI userProfileValidaAPI;

    @Autowired
    private UserProfileHelp userProfileHelp;

    @Autowired
    private UserProfileStrategyNotifyAPI userProfileStrategyNotifyAPI;

    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    /**
     * 根据特定条件分页查询用户配置策略
     *
     * @param webRequest 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("根据特定条件分页查询用户配置策略")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<UserProfileStrategyViewDTO>> page(PageQueryRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        PageQueryResponse<UserProfileStrategyViewDTO> pageQueryResponse = userProfileMgmtAPI.pageQuery(webRequest);

        return CommonWebResponse.success(pageQueryResponse);
    }

    /**
     * 根据特定条件分页查询用户配置策略
     *
     * @param webRequest 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("根据特定条件分页查询用户配置策略")
    @RequestMapping(value = "condition/list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<UserProfileStrategyDTO>> list(UserProfileStrategyListRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder().setPageLimit(webRequest.getPage(),
                webRequest.getLimit());
        PageQueryRequest pageQueryRequest = webRequest.buildPageQueryRequest(requestBuilder);
        PageQueryResponse<UserProfileStrategyDTO> pageQueryResponse = userProfileMgmtAPI.pageUserProfileStrategyQuery(pageQueryRequest);
        userProfileHelp.filterCanUseUserProfileStrategy(webRequest, pageQueryResponse);

        return CommonWebResponse.success(pageQueryResponse);
    }


    /**
     * 获取选定配置下子路径的总数
     *
     * @param webRequest 请求
     * @return 子路径总数
     */
    @ApiOperation("获取策略选定配置下子路径的总数")
    @RequestMapping(value = "path/count", method = RequestMethod.POST)
    public CommonWebResponse getPathCountOfStrategy(IdArrWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest must not be null");

        CountDTO pathCountDTO = new CountDTO();
        pathCountDTO.setCount(userProfileMgmtAPI.getPathCountOfStrategy(webRequest.getIdArr()));

        return CommonWebResponse.success(pathCountDTO);
    }

    /**
     * 创建用户配置策略
     *
     * @param createUserProfileStrategyRequest 入参
     * @param sessionContext                   session上下文
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建用户配置策略")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CommonWebResponse create(CreateUserProfileStrategyRequest createUserProfileStrategyRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(createUserProfileStrategyRequest, "createUserProfileStrategyRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        validatePathNumWhenCreateStrategy(createUserProfileStrategyRequest.getPathArr());
        userProfileHelp.validateUserProfilePathExist(createUserProfileStrategyRequest);

        UserProfileStrategyDTO userProfileStrategyDTO = new UserProfileStrategyDTO();
        BeanUtils.copyProperties(createUserProfileStrategyRequest, userProfileStrategyDTO);
        userProfileStrategyDTO.setCreatorUserName(sessionContext.getUserName());
        userProfileMgmtAPI.createUserProfileStrategy(userProfileStrategyDTO);
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_CREATE, userProfileStrategyDTO.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 编辑用户配置策略
     *
     * @param createUserProfileStrategyRequest 入参
     * @param builder                          批处理
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑用户配置策略")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public CommonWebResponse edit(CreateUserProfileStrategyRequest createUserProfileStrategyRequest,
                                  BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(createUserProfileStrategyRequest, "createUserProfileStrategyRequest must not be null");
        Assert.notNull(builder, "builder must not be null");

        validatePathNumWhenCreateStrategy(createUserProfileStrategyRequest.getPathArr());
        userProfileHelp.validateUserProfilePathExist(createUserProfileStrategyRequest);

        UserProfileStrategyDTO userProfileStrategyDTO = new UserProfileStrategyDTO();
        BeanUtils.copyProperties(createUserProfileStrategyRequest, userProfileStrategyDTO);
        List<UUID> desktopIdList = userProfileStrategyNotifyAPI.getRelatedDesktopIdByUserProfileStrategy(createUserProfileStrategyRequest.getId());
        userProfileMgmtAPI.editUserProfileStrategy(userProfileStrategyDTO);
        // 通知所有云桌面
        if (!desktopIdList.isEmpty()) {
            userProfileHelp.buildPushUserProfilePathUpdate(builder, desktopIdList.toArray(new UUID[desktopIdList.size()]));
        }
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_EDIT, userProfileStrategyDTO.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[]{});
    }

    /**
     * 删除用户配置策略
     *
     * @param idArrWebRequest 入参
     * @param builder         批处理
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除用户配置策略")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        UUID[] idArr = idArrWebRequest.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be null");
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE, new String[]{}).build()).iterator();
        DeleteUserProfileStrategyBatchTaskHandler handler = new DeleteUserProfileStrategyBatchTaskHandler(iterator, auditLogAPI, userProfileMgmtAPI);
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            handler.setBatch(false);
            UserProfileStrategyDTO userProfileStrategyDTO = userProfileMgmtAPI.findUserProfileStrategyById(idArr[0]);
            result = builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE)
                    .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DELETE_SINGLE_TASK_DESC, userProfileStrategyDTO.getName())
                    .enableParallel().registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_BATCH_DELETE)
                    .setTaskDesc(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_BATCH_DELETE_TASK_DESC).enableParallel()
                    .registerHandler(handler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     * 获取用户配置策略详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取用户配置策略详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse<UserProfileStrategyDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        UserProfileStrategyDTO softwareStrategyDTO = userProfileMgmtAPI.findUserProfileStrategyWrapperById(idWebRequest.getId());
        // 开启文件服务器存储，获取文件服务器相关信息
        if (UserProfileStrategyStorageTypeEnum.FILE_SERVER == softwareStrategyDTO.getStorageType() &&
                Objects.nonNull(softwareStrategyDTO.getExternalStorageId())) {
            try {
                CbbLocalExternalStorageDTO externalStorageDetail = cbbExternalStorageMgmtAPI.getExternalStorageDetail(
                        softwareStrategyDTO.getExternalStorageId());
                if (externalStorageDetail != null) {
                    softwareStrategyDTO.setExternalStorageDTO(externalStorageDetail);
                }
            } catch (Exception ex) {
                LOGGER.error("获取文件服务器[{}]异常", softwareStrategyDTO.getExternalStorageId(), ex);
            }

        }
        return CommonWebResponse.success(softwareStrategyDTO);
    }

    /**
     * 检查用户配置策略名称是否重复
     *
     * @param checkNameDuplicationRequest 入参
     * @return 响应
     */
    @ApiOperation("检查用户配置策略名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationForUserProfileResponse> checkNameDuplication(
            CheckUserProfileNameDuplicationRequest checkNameDuplicationRequest) {
        Assert.notNull(checkNameDuplicationRequest, "checkNameDuplicationRequest must not be null");
        UUID id = checkNameDuplicationRequest.getId();
        String name = checkNameDuplicationRequest.getName();
        Boolean hasDuplication = userProfileMgmtAPI.checkUserProfileStrategyNameDuplication(id, name);
        return CommonWebResponse.success(new CheckNameDuplicationForUserProfileResponse(hasDuplication));
    }

    /**
     * 获取用户配置分组,封装成树形结构
     *
     * @param request request
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取用户配置策略路径列表,封装成树形结构")
    @RequestMapping(value = "tree/list", method = RequestMethod.POST)
    public DefaultWebResponse getTreeList(QueryProfilePathRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        List<UserProfileTreeDTO> treeList = userProfileMgmtAPI.listUserProfileTree(request.getStorageType());
        return DefaultWebResponse.Builder.success(ImmutableMap.of(TREE_FIELD, treeList));
    }

    private void validatePathNumWhenCreateStrategy(UserProfileStrategyRelatedDTO[] pathArr) throws BusinessException {
        int length = pathArr.length;
        UUID[] idArr = new UUID[length];
        for (int i = 0; i < length; i++) {
            idArr[i] = pathArr[i].getId();
        }

        userProfileValidaAPI.validatePathNum(idArr);
    }
}
