package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.config.AppHostDiskConfig;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyNameDuplicateDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto.AdGroupListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaMainStrategyWatermarkAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaStrategyBindRelationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.batch.DeleteRcaMainStrategyBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request.RcaStrategyDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request.RcaStrategyFilterDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request.RcaStrategyRelatedObjectRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.validation.RcaMainStrategyValidation;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.*;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/3 19:00
 *
 * @author zhangsiming
 */

@Api(tags = "云应用策略管理")
@Controller
@RequestMapping("/rca/strategy/main")
@EnableCustomValidate(validateClass = RcaMainStrategyValidation.class)
public class RcaMainStrategyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaMainStrategyController.class);

    private static final String WIND_DIR_PATH = "\\";

    private static final String WIND_DIR_SPLIT = "\\\\";

    private static final String WIND_DIR_TWO_PATH = "\\\\";

    private static final String WIN_DIR_REGEXP = "[/:*?<>|]";

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private RcaStrategyBindRelationAPI rcaStrategyBindRelationAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaMainStrategyWatermarkAPI rcaMainStrategyWatermarkAPI;

    /**
     * @param rcaMainStrategyDTO 云应用策略
     * @param sessionContext     sessionContext
     * @return 创建结果
     * @throws BusinessException 异常
     */
    @ApiOperation("创建云应用策略")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"创建云应用策略"})})
    @EnableCustomValidate(validateMethod = "createRcaMainStrategyValidate")
    @EnableAuthority
    public CommonWebResponse create(RcaMainStrategyDTO rcaMainStrategyDTO, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(rcaMainStrategyDTO, "rcaMainStrategyDTO can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        validateCustomRedirectDirectoryArr(rcaMainStrategyDTO);

        // 获取登录用户信息
        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
        rcaMainStrategyDTO.setCreatorUserName(baseAdminDTO.getUserName());

        UUID strategyId = rcaMainStrategyAPI.createRcaMainStrategy(rcaMainStrategyDTO);
        permissionHelper.saveAdminGroupPermission(sessionContext, strategyId, AdminDataPermissionType.APP_MAIN_STRATEGY);
        return CommonWebResponse.success(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_CREATE_SUCCESS_LOG, new String[]{rcaMainStrategyDTO.getName()});
    }

    /**
     * @param pageQueryRequest 分页请求
     * @param sessionContext   sessionContext
     * @return 分页结果
     * @throws BusinessException 异常
     */
    @ApiOperation("分页获取云应用策略信息")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取云应用策略信息"})})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse pageQueryRcaMainStrategy(PageQueryRequest pageQueryRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        //拥有所有数据权限
        if (permissionHelper.isAllGroupPermission(sessionContext.getUserId())) {
            PageQueryResponse<RcaMainStrategyDTO> pageQueryResponse = rcaMainStrategyAPI.pageQueryMasterStrategy(pageQueryRequest);
            return CommonWebResponse.success(pageQueryResponse);
        }

        UUID[] dataIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_MAIN_STRATEGY);

        //无数据权限
        if (ArrayUtils.isEmpty(dataIdArr)) {
            return CommonWebResponse.success(new PageQueryResponse<RcaMainStrategyDTO>());
        }

        // 部分数据权限
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        builder.in(PermissionHelper.DATA_PERMISSION_ID_KEY, dataIdArr);
        PageQueryResponse<RcaMainStrategyDTO> pageQueryResponse = rcaMainStrategyAPI.pageQueryMasterStrategy(builder.build());
        return CommonWebResponse.success(pageQueryResponse);

    }

    /**
     * 编辑云应用策略
     *
     * @param rcaMainStrategyDTO 策略详情
     * @param sessionContext     sessionContext
     * @return 编辑结果
     * @throws BusinessException 异常
     */

    @ApiOperation("编辑云应用策略")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑云应用策略"})})
    @EnableCustomValidate(validateMethod = "updateRcaMainStrategyValidate")
    @EnableAuthority
    public CommonWebResponse edit(RcaMainStrategyDTO rcaMainStrategyDTO, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(rcaMainStrategyDTO, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        if (!permissionHelper.isAllGroupPermission(sessionContext) && !permissionHelper.hasDataPermission(sessionContext.getUserId(),
                String.valueOf(rcaMainStrategyDTO.getId()), AdminDataPermissionType.APP_MAIN_STRATEGY)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_RCA_HAS_NO_DATA_PERMISSION);
        }

        validateCustomRedirectDirectoryArr(rcaMainStrategyDTO);

        // 677783 - 若云应用策略的数据路径重定向从关->开，需要检测使用该策略的池是否存在已发布应用不一致的情况
        checkCanSwitchDiskRedirectByPool(rcaMainStrategyDTO);

        //686076 云应用策略绑定了还原动态池（未开启本地盘），编辑云应用策略不能启用个性化配置
        checkCanEnablePersonalDataRetention(rcaMainStrategyDTO);

        rcaMainStrategyAPI.updateRcaMainStrategy(rcaMainStrategyDTO);

        handleNotifyWatermarkConfig(rcaMainStrategyDTO);

        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_UPDATE_SUCCESS_LOG, rcaMainStrategyDTO.getName());
        return CommonWebResponse.success(RcaBusinessKey.RCDC_RCA_STRATEGY_OPERATE_SUCCESS, new String[]{});
    }

    private void checkCanEnablePersonalDataRetention(RcaMainStrategyDTO rcaMainStrategyDTO) throws BusinessException {
        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setId(rcaMainStrategyDTO.getId());
        RcaMainStrategyDTO currentMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
        if (Boolean.TRUE.equals(rcaMainStrategyDTO.getPersonalDataRetention())
                && !Objects.equals(rcaMainStrategyDTO.getPersonalDataRetention(), currentMainStrategyDTO.getPersonalDataRetention())) {
            //需要判断策略绑定的应用池是否开启本地盘
            List<String> poolNameList = rcaMainStrategyAPI.getRcaPoolNameListWithPersonalDiskDisabled(rcaMainStrategyDTO.getStrategyId());
            if (!CollectionUtils.isEmpty(poolNameList)) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_POOL_EDIT_STRATEGY_PERSONAL_DATA_RETENTION_ERROR_MSG_POOL,
                        poolNameList.toString());
            }
        }
    }

    private void validateCustomRedirectDirectoryArr(RcaMainStrategyDTO request) throws BusinessException {
        if (request.getAppHostDiskConfig() != null && request.getAppHostDiskConfig().getAppDataPathRedirect() != null) {
            AppHostDiskConfig.CustomRedirectDirectoryArr[] customRedirectDirectoryArr =
                    request.getAppHostDiskConfig().getAppDataPathRedirect().getCustomDirectoryArr();
            if (ArrayUtils.isNotEmpty(customRedirectDirectoryArr)) {
                for (AppHostDiskConfig.CustomRedirectDirectoryArr customRedirectDirectory : customRedirectDirectoryArr) {
                    if (StringUtils.isBlank(customRedirectDirectory.getSubDirectory())) {
                        throw new BusinessException(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_SUB_DIRECTORY_BLANK_VALID_ERROR);
                    }
                    if (!customRedirectDirectory.getSubDirectory().startsWith(WIND_DIR_PATH)) {
                        throw new BusinessException(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_SUB_DIRECTORY_START_WITH_VALID_ERROR,
                                customRedirectDirectory.getSubDirectory());
                    }
                    if (customRedirectDirectory.getSubDirectory().endsWith(WIND_DIR_PATH)) {
                        throw new BusinessException(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_SUB_DIRECTORY_END_WITH_VALID_ERROR,
                                customRedirectDirectory.getSubDirectory());
                    }
                    if (customRedirectDirectory.getSubDirectory().contains(WIND_DIR_TWO_PATH)) {
                        throw new BusinessException(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_SUB_DIRECTORY_TWO_DIR_VALID_ERROR,
                                customRedirectDirectory.getSubDirectory());
                    }
                    if (validSubDirectoryIsBlank(customRedirectDirectory.getSubDirectory())) {
                        throw new BusinessException(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_SUB_DIRECTORY_IS_BLANK_VALID_ERROR,
                                customRedirectDirectory.getSubDirectory());
                    }
                    if (Pattern.compile(WIN_DIR_REGEXP).matcher(customRedirectDirectory.getSubDirectory()).find()) {
                        throw new BusinessException(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_SUB_DIRECTORY_REGEXP_VALID_ERROR,
                                customRedirectDirectory.getSubDirectory());
                    }
                }
            }
        }
    }

    private boolean validSubDirectoryIsBlank(String subDirectory) {
        final String BLANK_STR = " ";
        String[] subDirArr = subDirectory.split(WIND_DIR_SPLIT);
        for (String subDir : subDirArr) {
            String replaceSubDir = subDir.replaceAll("\\s", BLANK_STR);
            if (replaceSubDir.startsWith(BLANK_STR)) {
                return true;
            }
        }
        return false;
    }

    private void checkCanSwitchDiskRedirectByPool(RcaMainStrategyDTO newMainStrategyDTO) throws BusinessException {
        Boolean isEnableRedirect = newMainStrategyDTO.getAppHostDiskConfig().getAppDataPathRedirect().getEnable();
        if (newMainStrategyDTO.getHostSourceType() != RcaEnum.HostSourceType.THIRD_PARTY || !Boolean.TRUE.equals(isEnableRedirect)) {
            return;
        }

        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setId(newMainStrategyDTO.getId());
        RcaMainStrategyDTO currentMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
        Boolean isCurrentEnableRedirect = newMainStrategyDTO.getAppHostDiskConfig().getAppDataPathRedirect().getEnable();
        if (!Boolean.TRUE.equals(isCurrentEnableRedirect)) {
            return;
        }
        // 数据保存路径重定向从关->开，获取并匹配策略关联池的主机信息
        List<UUID> poolIdList = rcaMainStrategyAPI.getPoolIdListByStrategyId(newMainStrategyDTO.getStrategyId());
        if (CollectionUtils.isEmpty(poolIdList)) {
            return;
        }
        List<String> unBalancedPoolNameList = rcaAppPoolAPI.getAppUnBalancedPoolNameList(poolIdList);

        if (CollectionUtils.isEmpty(unBalancedPoolNameList)) {
            return;
        }
        String message = unBalancedPoolNameList.stream().collect(Collectors.joining(Constants.DELIMITER));
        LOGGER.info("存在池的主机应用不均衡，无法开启应用数据保存路径重定向，poolId={}", JSONObject.toJSONString(poolIdList));
        throw new BusinessException(RcaBusinessKey.RCDC_RCA_POOL_EDIT_STRATEGY_REDIRECT_DISK_ERROR_MSG_POOL, message);
    }


    private void handleNotifyWatermarkConfig(RcaMainStrategyDTO rcaMainStrategyDTO) throws BusinessException {
        rcaMainStrategyWatermarkAPI.handleNotifyWatermarkConfig(rcaMainStrategyDTO);
    }

    /**
     * 删除云应用策略
     *
     * @param webRequest     策略ID
     * @param sessionContext 会话id
     * @param builder        批处理builder
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除云应用策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"删除云应用策略"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse delete(IdArrWebRequest webRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        UUID[] idArr = webRequest.getIdArr();

        final Iterator<? extends I18nBatchTaskItem<?>> iterator = Stream.of(idArr).distinct() //
                .map(id -> {
                    RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
                    relationshipDTO.setId(id);
                    String strategyName = "";
                    try {
                        RcaMainStrategyDTO rcaMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
                        strategyName = rcaMainStrategyDTO.getName();
                    } catch (BusinessException e) {
                        LOGGER.error("云应用策略[{}]不存在", id);
                    }
                    return I18nBatchTaskItem.Builder.build(id, RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_BATCH_DELETE_TASK_ITEM, strategyName);
                })
                .iterator();
        final DeleteRcaMainStrategyBatchHandler handler =
                new DeleteRcaMainStrategyBatchHandler(rcaMainStrategyAPI, permissionHelper, iterator, sessionContext.getUserId());
        BatchTaskSubmitResult result = I18nBatchTaskBuilder.wrap(builder).setAuditLogAPI(auditLogAPI)
                .setTaskName(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_BATCH_DELETE_TASK_NAME)
                .registerHandler(handler)
                .start();
        return CommonWebResponse.success(result);

    }


    /**
     * @param request 切换云应用策略请求
     * @return 切换结果
     * @throws BusinessException 异常
     */
    @ApiOperation("切换云应用策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"切换云应用策略"})})
    @RequestMapping(value = "/alter", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse alter(RcaStrategyRelationshipDTO request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        rcaMainStrategyAPI.alterRcaMainStrategy(request);
        return CommonWebResponse.success(RcaBusinessKey.RCDC_RCA_STRATEGY_ALTER_SUCCESS, new String[]{});
    }

    /**
     * 根据特定Id限制查询云应用策略列表
     *
     * @param pageWebRequest 请求参数
     * @param sessionContext 请sessionContext
     * @return 策略列表
     * @throws BusinessException 异常
     */
    @ApiOperation("根据特定Id限制查询云应用策略列表")
    @RequestMapping(value = "condition/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"根据特定Id限制查询云应用策略列表"})})
    public CommonWebResponse<PageQueryResponse<RcaMainStrategyDTO>> listRcaMainStrategyByCondition(PageQueryRequest pageWebRequest,
                                                                                                   SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(pageWebRequest.getPage(), pageWebRequest.getLimit());
        RcaStrategyFilterDTO rcaStrategyFilterDTO = new RcaStrategyFilterDTO();

        Match[] exactMatchArr = pageWebRequest.getMatchArr();
        if (ArrayUtils.isNotEmpty(exactMatchArr)) {
            for (Match exactMatch1 : exactMatchArr) {
                ExactMatch exactMatch = (ExactMatch) exactMatch1;

                if (StringUtils.equals("imageTemplateId", exactMatch.getFieldName())) {
                    Object[] valueArr = exactMatch.getValueArr();
                    if (valueArr != null && valueArr.length > 0) {
                        UUID imageId = UUID.fromString(String.valueOf(exactMatch.getValueArr()[0]));
                        rcaStrategyFilterDTO.setImageId(imageId);
                    }
                    continue;
                }

                if (StringUtils.equals("poolType", exactMatch.getFieldName())) {
                    RcaEnum.PoolType poolType = RcaEnum.PoolType.valueOf(exactMatch.getValueArr()[0].toString());
                    rcaStrategyFilterDTO.setPoolType(poolType);
                    continue;
                }
                if (StringUtils.equals("hostSessionType", exactMatch.getFieldName())) {
                    RcaEnum.HostSessionType hostSessionType = RcaEnum.HostSessionType.valueOf(exactMatch.getValueArr()[0].toString());
                    rcaStrategyFilterDTO.setHostSessionType(hostSessionType);
                    continue;
                }
                if (StringUtils.equals("hostSourceType", exactMatch.getFieldName())) {
                    RcaEnum.HostSourceType hostSourceType = RcaEnum.HostSourceType.valueOf(exactMatch.getValueArr()[0].toString());
                    rcaStrategyFilterDTO.setHostSourceType(hostSourceType);
                    continue;
                }
                if (StringUtils.equals("pattern", exactMatch.getFieldName())) {
                    CbbCloudDeskPattern pattern = CbbCloudDeskPattern.valueOf(exactMatch.getValueArr()[0].toString());
                    rcaStrategyFilterDTO.setPattern(pattern);
                    continue;
                }
                requestBuilder.in(exactMatch.getFieldName(), exactMatch.getValueArr());
            }
        }
        if (ArrayUtils.isNotEmpty(pageWebRequest.getSortArr())) {
            for (Sort sort : pageWebRequest.getSortArr()) {
                if (sort.getDirection() == Sort.Direction.ASC) {
                    requestBuilder.asc(sort.getFieldName());
                } else {
                    requestBuilder.desc(sort.getFieldName());
                }
            }
        }

        // 拥有所有数据权限
        if (!permissionHelper.isAllGroupPermission(sessionContext.getUserId())) {
            UUID[] dataIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_MAIN_STRATEGY);

            if (ArrayUtils.isEmpty(dataIdArr)) {
                return CommonWebResponse.success(new PageQueryResponse<>());
            }

            requestBuilder.in(PermissionHelper.DATA_PERMISSION_ID_KEY, dataIdArr);
        }

        PageQueryResponse<RcaMainStrategyDTO> pageQueryResponse = rcaMainStrategyAPI.pageQueryMasterStrategy(requestBuilder.build());
        setCanUsedMessageByFilterCondition(pageQueryResponse, rcaStrategyFilterDTO);
        return CommonWebResponse.success(pageQueryResponse);
    }


    /**
     * @param rcaStrategyDetailRequest 请求策略详情
     * @return 策略详情
     * @throws BusinessException 异常
     */
    @ApiOperation("根据特定Id限制查询云应用策略列表")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"根据特定Id限制查询云应用策略列表"})})
    public CommonWebResponse<RcaMainStrategyDTO> detail(RcaStrategyDetailRequest rcaStrategyDetailRequest) throws BusinessException {
        Assert.notNull(rcaStrategyDetailRequest, "rcaStrategyDetailRequest can not be null");
        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setId(rcaStrategyDetailRequest.getId());
        RcaMainStrategyDTO rcaMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
        return CommonWebResponse.success(rcaMainStrategyDTO);
    }

    /**
     * @param rcaStrategyNameDuplicateDTO 策略重名查询请求
     * @return 是否重名
     * @throws BusinessException 异常
     */
    @ApiOperation("检查策略是否重名")
    @RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"检查策略是否重名"})})
    public CommonWebResponse checkNameDuplicate(RcaStrategyNameDuplicateDTO rcaStrategyNameDuplicateDTO) throws BusinessException {
        Assert.notNull(rcaStrategyNameDuplicateDTO, "rcaStrategyNameDuplicateDTO can not be null");
        Boolean isDuplicate = rcaMainStrategyAPI.checkNameDuplication(rcaStrategyNameDuplicateDTO);
        CheckDuplicationWebResponse checkDuplicationWebResponse = new CheckDuplicationWebResponse();
        checkDuplicationWebResponse.setHasDuplication(isDuplicate);
        return CommonWebResponse.success(checkDuplicationWebResponse);
    }

    /**
     * @param pageWebRequest 请求
     * @return 云应用策略关联的用户列表
     * @throws BusinessException 异常
     */
    @ApiOperation("查询云应用策略关联的用户列表")
    @RequestMapping(value = "listRelatedUser", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查询云应用策略关联的用户列表"})})
    public CommonWebResponse listRelatedUser(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest can not be null");
        RcaStrategyRelatedObjectRequest rcaStrategyRelatedObjectRequest = new RcaStrategyRelatedObjectRequest(pageWebRequest);

        DefaultPageResponse<UserListDTO> userListDTODefaultPageResponse =
                rcaStrategyBindRelationAPI.pageQueryRcaMainStrategyBindUser(rcaStrategyRelatedObjectRequest.getStrategyId(),
                        rcaStrategyRelatedObjectRequest);
        return CommonWebResponse.success(userListDTODefaultPageResponse);
    }

    /**
     * @param pageWebRequest 请求
     * @return 云应用策略关联的应用池列表
     * @throws BusinessException 异常
     */
    @ApiOperation("查询云应用策略关联的应用池列表")
    @RequestMapping(value = "listRelatedPool", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查询云应用策略关联的应用池列表"})})
    public CommonWebResponse listRelatedPool(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest can not be null");
        RcaStrategyRelatedObjectRequest rcaStrategyRelatedObjectRequest = new RcaStrategyRelatedObjectRequest(pageWebRequest);

        DefaultPageResponse<RcaAppPoolBaseDTO> poolPageResponse =
                rcaStrategyBindRelationAPI.pageQueryRcaMainStrategyBindPool(rcaStrategyRelatedObjectRequest.getStrategyId(),
                        rcaStrategyRelatedObjectRequest);
        return CommonWebResponse.success(poolPageResponse);
    }

    /**
     * @param pageWebRequest 请求
     * @return 云应用策略关联的ad安全组列表
     * @throws BusinessException 异常
     */
    @ApiOperation("查询云应用策略关联的安全组列表")
    @RequestMapping(value = "listRelatedAdSafetyGroup", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查询云应用策略关联的安全组列表"})})
    public CommonWebResponse listRelatedSafetyGroup(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest can not be null");
        RcaStrategyRelatedObjectRequest rcaStrategyRelatedObjectRequest = new RcaStrategyRelatedObjectRequest(pageWebRequest);

        DefaultPageResponse<AdGroupListDTO> adGroupDTODefaultPageResponse =
                rcaStrategyBindRelationAPI.pageQueryRcaMainStrategyBindSafetyGroup(rcaStrategyRelatedObjectRequest.getStrategyId(),
                        rcaStrategyRelatedObjectRequest);
        return CommonWebResponse.success(adGroupDTODefaultPageResponse);
    }


    private void setCanUsedMessageByFilterCondition(PageQueryResponse<RcaMainStrategyDTO> pageQueryResponse,
                                                    RcaStrategyFilterDTO rcaStrategyFilterDTO) {
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(dto -> {
            String canUsedMessage = null;
            if (rcaStrategyFilterDTO.getImageId() != null && dto.getHostSourceType() == RcaEnum.HostSourceType.VDI) {
                canUsedMessage = rcaMainStrategyAPI.getCanUseMessageByImageId(rcaStrategyFilterDTO.getImageId(), dto);
            }
            if (StringUtils.isEmpty(canUsedMessage) && rcaStrategyFilterDTO.getHostSourceType() != null) {
                canUsedMessage = rcaMainStrategyAPI.getCanUseMessageByHostSourceType(rcaStrategyFilterDTO.getHostSourceType(), dto);
            }
            if (canUsedMessage == null && rcaStrategyFilterDTO.getHostSessionType() != null) {
                canUsedMessage = rcaMainStrategyAPI.getCanUseMessageByHostSessionType(rcaStrategyFilterDTO.getHostSessionType(), dto);
            }
            if (canUsedMessage == null && rcaStrategyFilterDTO.getPoolType() != null) {
                canUsedMessage = rcaMainStrategyAPI.getCanUseMessageByPoolType(rcaStrategyFilterDTO.getPoolType(), dto);
            }
            if (canUsedMessage == null && rcaStrategyFilterDTO.getPattern() != null) {
                canUsedMessage = rcaMainStrategyAPI.getCanUseMessageByPattern(rcaStrategyFilterDTO.getPattern(), dto);
            }

            if (Objects.nonNull(canUsedMessage) && StringUtils.hasText(canUsedMessage)) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }
}
