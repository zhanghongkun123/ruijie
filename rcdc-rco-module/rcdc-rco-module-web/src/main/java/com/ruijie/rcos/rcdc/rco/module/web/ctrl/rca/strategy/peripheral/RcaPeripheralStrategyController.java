package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.peripheral;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.sk.pagekit.api.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPeripheralStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaPeripheralStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyNameDuplicateDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.def.adgroup.dto.AdGroupListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaStrategyBindRelationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request.RcaCopyStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request.RcaStrategyDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request.RcaStrategyFilterDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request.RcaStrategyRelatedObjectRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.peripheral.batch.DeleteRcaPeripheralStrategyBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.peripheral.validation.RcaPeripheralStrategyValidation;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/3 19:00
 *
 * @author zhangsiming
 */

@Api(tags = "云应用外设策略管理")
@Controller
@RequestMapping("/rca/strategy/peripheral")
@EnableCustomValidate(validateClass = RcaPeripheralStrategyValidation.class)
public class RcaPeripheralStrategyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaPeripheralStrategyController.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI;


    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private RcaStrategyBindRelationAPI rcaStrategyBindRelationAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    private static final String DATA_PERMISSION_KEY = "id";

    /**
     * @param rcaPeripheralStrategyDTO 创建外设策略请求
     * @param sessionContext sessionContext
     * @return 创建结果
     * @throws BusinessException 异常
     */
    @ApiOperation("创建云应用外设策略")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"创建云应用外设策略"})})
    @EnableCustomValidate(validateMethod = "createAndUpdateRcaPeripheralStrategyValidate")
    @EnableAuthority
    public CommonWebResponse create(RcaPeripheralStrategyDTO rcaPeripheralStrategyDTO, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(rcaPeripheralStrategyDTO, "rcaPeripheralStrategyDTO can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");
        // 获取登录用户信息
        // FIXME zhangsiming 先注释
        UUID id = rcaPeripheralStrategyAPI.createRcaPeripheralStrategy(rcaPeripheralStrategyDTO);
        permissionHelper.saveAdminGroupPermission(sessionContext, id, AdminDataPermissionType.APP_PERIPHERAL_STRATEGY);
        
        return CommonWebResponse.success(RcaBusinessKey.RCDC_RCA_PERIPHERAL_STRATEGY_CREATE_SUCCESS_LOG,
                new String[]{rcaPeripheralStrategyDTO.getName()});
    }

    /**
     * @param request 复制请求
     * @return 复制结果
     * @throws BusinessException 异常
     */
    @ApiOperation("复制云应用外设策略")
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"复制云应用外设策略"})})
    @EnableAuthority
    public CommonWebResponse copy(RcaCopyStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setStrategyId(request.getStrategyId());
        RcaPeripheralStrategyDTO rcaMainStrategyDTO = rcaPeripheralStrategyAPI.getStrategyDetailBy(relationshipDTO);
        rcaMainStrategyDTO.setName(null);
        return CommonWebResponse.success(rcaMainStrategyDTO);
    }

    /**
     * @param pageQueryRequest 获取外设策略
     * @param sessionContext sessionContext
     * @return 分页结果
     * @throws BusinessException 异常
     */
    @ApiOperation("分页获取云应用外设策略信息")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"分页获取云应用外设策略信息"})})
    public CommonWebResponse queryPageRcaPeripheralStrategy(PageQueryRequest pageQueryRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        // 所有数据权限
        if (permissionHelper.isAllGroupPermission(sessionContext.getUserId())) {
            PageQueryResponse<RcaPeripheralStrategyDTO> pageQueryResponse = rcaPeripheralStrategyAPI.pageQueryMasterStrategy(pageQueryRequest);
            return CommonWebResponse.success(pageQueryResponse);
        }

        UUID[] dataIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_PERIPHERAL_STRATEGY);
        // 无数据权限
        if (ArrayUtils.isEmpty(dataIdArr)) {
            return CommonWebResponse.success(new PageQueryResponse<RcaPeripheralStrategyDTO>());
        }

        // 部分数据权限
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder(pageQueryRequest);
        builder.in(PermissionHelper.DATA_PERMISSION_ID_KEY, dataIdArr);
        PageQueryResponse<RcaPeripheralStrategyDTO> pageQueryResponse = rcaPeripheralStrategyAPI.pageQueryMasterStrategy(builder.build());
        return CommonWebResponse.success(pageQueryResponse);

    }

    /**
     * 编辑云应用策略
     * @param rcaPeripheralStrategyDTO 外设策略详情
     * @param sessionContext sessionContext
     * @return 编辑结果
     * @throws BusinessException 异常
     */

    @ApiOperation("编辑云应用外设策略")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑云应用外设策略"})})
    @EnableCustomValidate(validateMethod = "createAndUpdateRcaPeripheralStrategyValidate")
    @EnableAuthority
    public CommonWebResponse edit(RcaPeripheralStrategyDTO rcaPeripheralStrategyDTO, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(rcaPeripheralStrategyDTO, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        if (!permissionHelper.isAllGroupPermission(sessionContext.getUserId()) && !permissionHelper.hasDataPermission(sessionContext.getUserId(),
                String.valueOf(rcaPeripheralStrategyDTO.getId()), AdminDataPermissionType.APP_PERIPHERAL_STRATEGY)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_RCA_HAS_NO_DATA_PERMISSION);
        }

        rcaPeripheralStrategyAPI.updateRcaPeripheralStrategy(rcaPeripheralStrategyDTO);
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_PERIPHERAL_STRATEGY_UPDATE_SUCCESS_LOG, rcaPeripheralStrategyDTO.getName());
        return CommonWebResponse.success(RcaBusinessKey.RCDC_RCA_STRATEGY_OPERATE_SUCCESS, new String[]{});
    }

    /**
     * 删除云应用外设策略
     *
     * @param webRequest 策略ID
     * @param builder 批处理builder
     * @param sessionContext sessionContext
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除云应用外设策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"删除云应用外设策略"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse delete(IdArrWebRequest webRequest, BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
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
                        RcaPeripheralStrategyDTO peripheralStrategyDTO = rcaPeripheralStrategyAPI.getStrategyDetailBy(relationshipDTO);
                        strategyName = peripheralStrategyDTO.getName();
                    } catch (BusinessException e) {
                        LOGGER.error("获取云应用外设策略失败", e);
                    }
                    return I18nBatchTaskItem.Builder.build(id, RcaBusinessKey.RCDC_RCA_PERIPHERAL_STRATEGY_BATCH_DELETE_TASK_ITEM, strategyName);
                })
                .iterator();
        final DeleteRcaPeripheralStrategyBatchHandler handler =
                new DeleteRcaPeripheralStrategyBatchHandler(rcaPeripheralStrategyAPI, permissionHelper, iterator, sessionContext.getUserId());
        BatchTaskSubmitResult result = I18nBatchTaskBuilder.wrap(builder).setAuditLogAPI(auditLogAPI)
                .setTaskName(RcaBusinessKey.RCDC_RCA_PERIPHERAL_STRATEGY_BATCH_DELETE_TASK_NAME).registerHandler(handler).start();
        return CommonWebResponse.success(result);

    }


    /**
     * @param request 切换策略请求
     * @return 切换结果
     * @throws BusinessException 异常
     */
    @ApiOperation("切换云应用外设策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"切换云应用外设策略"})})
    @RequestMapping(value = "/alter", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse alter(RcaStrategyRelationshipDTO request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        rcaPeripheralStrategyAPI.alterRcaPeripheralStrategy(request);
        return CommonWebResponse.success(RcaBusinessKey.RCDC_RCA_STRATEGY_ALTER_SUCCESS, new String[]{});
    }

    /**
     * 根据特定Id限制查询云应用外设策略列表
     *
     * @param pageWebRequest 请求参数
     * @param sessionContext 请sessionContext
     * @return 策略列表
     * @throws BusinessException 异常
     */
    @ApiOperation("根据特定Id限制查询云应用外设策略列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"根据特定Id限制查询云应用外设策略列表"})})
    @RequestMapping(value = "condition/list", method = RequestMethod.POST)
    public CommonWebResponse<PageQueryResponse<RcaPeripheralStrategyDTO>> listRcaPeripheralStrategyByCondition(PageQueryRequest pageWebRequest,
                                                                                                               SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(pageWebRequest.getPage(), pageWebRequest.getLimit());
        UUID imageId = null;
        RcaEnum.PoolType poolType = null;
        RcaEnum.HostSourceType hostSourceType;
        RcaEnum.HostSessionType hostSessionType;
        RcaStrategyFilterDTO rcaStrategyFilterDTO = new RcaStrategyFilterDTO();

        Match[] exactMatchArr = pageWebRequest.getMatchArr();
        if (ArrayUtils.isNotEmpty(exactMatchArr)) {
            for (Match exactMatch1 : exactMatchArr) {
                ExactMatch exactMatch = (ExactMatch) exactMatch1;

                if (StringUtils.equals("poolType", exactMatch.getFieldName())) {
                    poolType = RcaEnum.PoolType.valueOf(exactMatch.getValueArr()[0].toString());
                    rcaStrategyFilterDTO.setPoolType(poolType);
                    continue;
                }
                if (StringUtils.equals("hostSessionType", exactMatch.getFieldName())) {
                    hostSessionType = RcaEnum.HostSessionType.valueOf(exactMatch.getValueArr()[0].toString());
                    rcaStrategyFilterDTO.setHostSessionType(hostSessionType);
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
            UUID[] dataIdArr = permissionHelper.getPermissionIdArr(sessionContext.getUserId(), AdminDataPermissionType.APP_PERIPHERAL_STRATEGY);
            if (ArrayUtils.isEmpty(dataIdArr)) {
                return CommonWebResponse.success(new PageQueryResponse<>());
            }

            requestBuilder.in(PermissionHelper.DATA_PERMISSION_ID_KEY, dataIdArr);
        }

        PageQueryResponse<RcaPeripheralStrategyDTO> pageQueryResponse = rcaPeripheralStrategyAPI.pageQueryMasterStrategy(requestBuilder.build());
        setCanUsedMessageByFilterCondition(pageQueryResponse, rcaStrategyFilterDTO);
        return CommonWebResponse.success(pageQueryResponse);
    }

    /**
     * @param rcaStrategyDetailRequest 策略配置请求
     * @return 策略详情
     * @throws BusinessException 异常
     */
    @ApiOperation("根据特定Id限制查询云应用策略列表")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"根据特定Id限制查询云应用策略列表"})})
    public CommonWebResponse<RcaPeripheralStrategyDTO> detail(RcaStrategyDetailRequest rcaStrategyDetailRequest) throws BusinessException {
        Assert.notNull(rcaStrategyDetailRequest, "rcaStrategyDetailRequest can not be null");
        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setId(rcaStrategyDetailRequest.getId());
        RcaPeripheralStrategyDTO rcaPeripheralStrategyDTO = rcaPeripheralStrategyAPI.getStrategyDetailBy(relationshipDTO);
        return CommonWebResponse.success(rcaPeripheralStrategyDTO);
    }

    private void setCanUsedMessageByFilterCondition(PageQueryResponse<RcaPeripheralStrategyDTO> pageQueryResponse,
                                                    RcaStrategyFilterDTO rcaStrategyFilterDTO) {
        Arrays.stream(pageQueryResponse.getItemArr()).forEach(dto -> {
            String canUsedMessage = null;
            if (canUsedMessage == null && rcaStrategyFilterDTO.getHostSessionType() != null) {
                canUsedMessage = rcaPeripheralStrategyAPI.getCanUseMessageByHostSessionType(rcaStrategyFilterDTO.getHostSessionType(), dto);
            }
            if (canUsedMessage != null) {
                dto.setCanUsed(false);
                dto.setCanUsedMessage(canUsedMessage);
            }
        });
    }

    /**
     * @param rcaStrategyNameDuplicateDTO 策略重名查询请求
     * @return 是否重名
     * @throws BusinessException 异常
     */
    @ApiOperation("检查策略是否重名")
    @RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"根据特定Id限制查询云应用策略列表"})})
    public CommonWebResponse checkNameDuplicate(RcaStrategyNameDuplicateDTO rcaStrategyNameDuplicateDTO) throws BusinessException {
        Assert.notNull(rcaStrategyNameDuplicateDTO, "rcaStrategyNameDuplicateDTO can not be null");
        Boolean isDuplicate = rcaPeripheralStrategyAPI.checkNameDuplication(rcaStrategyNameDuplicateDTO);
        CheckDuplicationWebResponse checkDuplicationWebResponse = new CheckDuplicationWebResponse();
        checkDuplicationWebResponse.setHasDuplication(isDuplicate);
        return CommonWebResponse.success(checkDuplicationWebResponse);
    }

    /**
     * @param pageWebRequest 请求
     * @return 云应用外设策略关联的用户列表
     * @throws BusinessException 异常
     */
    @ApiOperation("查询云应用外设策略关联的用户列表")
    @RequestMapping(value = "listRelatedUser", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查询云应用策略关联的用户列表"})})
    public CommonWebResponse listRelatedUser(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest can not be null");
        RcaStrategyRelatedObjectRequest rcaStrategyRelatedObjectRequest = new RcaStrategyRelatedObjectRequest(pageWebRequest);

        DefaultPageResponse<UserListDTO> userListDTODefaultPageResponse =
                rcaStrategyBindRelationAPI.pageQueryRcaPeripheralStrategyBindUser(rcaStrategyRelatedObjectRequest.getStrategyId(),
                        rcaStrategyRelatedObjectRequest);
        return CommonWebResponse.success(userListDTODefaultPageResponse);
    }

    /**
     * @param pageWebRequest 请求
     * @return 云应用外设策略关联的应用池列表
     * @throws BusinessException 异常
     */
    @ApiOperation("查询云应用外设策略关联的应用池列表")
    @RequestMapping(value = "listRelatedPool", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查询云应用外设策略关联的应用池列表"})})
    public CommonWebResponse listRelatedPool(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest can not be null");
        RcaStrategyRelatedObjectRequest rcaStrategyRelatedObjectRequest = new RcaStrategyRelatedObjectRequest(pageWebRequest);

        DefaultPageResponse<RcaAppPoolBaseDTO> poolPageResponse =
                rcaStrategyBindRelationAPI.pageQueryRcaPeripheralStrategyBindPool(rcaStrategyRelatedObjectRequest.getStrategyId(),
                        rcaStrategyRelatedObjectRequest);
        return CommonWebResponse.success(poolPageResponse);
    }

    /**
     * @param pageWebRequest 请求
     * @return 云应用外设策略关联的ad安全组列表
     * @throws BusinessException 异常
     */
    @ApiOperation("查询云应用外设策略关联的安全组列表")
    @RequestMapping(value = "listRelatedAdSafetyGroup", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"查询云应用外设策略关联的安全组列表"})})
    public CommonWebResponse listRelatedSafetyGroup(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "pageWebRequest can not be null");
        RcaStrategyRelatedObjectRequest rcaStrategyRelatedObjectRequest = new RcaStrategyRelatedObjectRequest(pageWebRequest);


        DefaultPageResponse<AdGroupListDTO> adGroupDTODefaultPageResponse =
                rcaStrategyBindRelationAPI.pageQueryRcaPeripheralStrategyBindSafetyGroup(rcaStrategyRelatedObjectRequest.getStrategyId(),
                        rcaStrategyRelatedObjectRequest);
        return CommonWebResponse.success(adGroupDTODefaultPageResponse);
    }
}
