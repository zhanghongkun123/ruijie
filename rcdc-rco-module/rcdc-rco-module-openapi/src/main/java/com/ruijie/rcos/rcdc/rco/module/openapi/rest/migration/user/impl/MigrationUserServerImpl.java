package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoLogonAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDeskComputerNameConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserGroupDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.autologon.enums.AutoLogonEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.spi.AdminActionMsgSPI;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.MtoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.consts.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.service.BusinessCommonService;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportDesktopConfig;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.MigrationUserServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.AuthType;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportRelationDataRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportUserDataRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportUserGroupDataRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportUserGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.response.AdConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.response.UserCustomDataResponse;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author chenl
 */
@Service
public class MigrationUserServerImpl implements MigrationUserServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrationUserServerImpl.class);

    private static final String BUSINESS_TYPE_COLUMN_NAME = "businessType";

    private static final String CREATE_DATE_COLUMN_NAME = "createDate";

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private BusinessCommonService businessCommonService;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private AutoLogonAPI autoLogonAPI;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private AdminActionMsgSPI adminActionMsgSPI;

    private static final String DESK_STRATEGY_KEY = "desk_strategy_key";

    private static final ExecutorService USER_MIGRATION_THREADS =
            ThreadExecutors.newBuilder("user-module-migration").maxThreadNum(10).queueSize(1000).build();

    /**
     * 4.x总览组ID
     */
    private final static String DEFAULT_ROOT_ID = "-1";
    /**
     * 4.x 未分组ID
     */

    private final static String DEFAULT_UNGROUP_ID = "0";

    // 计算机名称前缀
    private final static String PREFIX_COMPUTER_NAME = "PC";


    /**
     * 导入用户组
     * 1、前提是管理员
     *
     * @param request 请求
     * @return CommonResponse 响应
     * @throws BusinessException 业务异常
     */
    @Override
    public List<UserCustomDataResponse> createUserGroup(ImportUserGroupRequest request) throws BusinessException {

        Assert.notNull(request, "ImportUserGroupRequest is not null");
        List<ImportUserGroupDataRequest> normalUserGroupList = new ArrayList<>();
        List<ImportUserGroupDataRequest> adUserGroupList = new ArrayList<>();

        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_BEGIN, String.valueOf(request.getDataArr().length));
        //1、先区分普通用户组跟AD域用户组
        Map<String, SystemBusinessMappingDTO> stringSystemBusinessMappingDTOMap = new HashMap<>();
        if (ArrayUtils.isNotEmpty(request.getDataArr())) {
            List<SystemBusinessMappingDTO> userGroupMappingDTOList = systemBusinessMappingAPI.findSystemBusinessMappingList(
                    SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP);
            stringSystemBusinessMappingDTOMap = userGroupMappingDTOList.stream()
                    .collect(Collectors.toMap(SystemBusinessMappingDTO::getSrcId, systemBusinessMappingDTO -> systemBusinessMappingDTO));

            for (ImportUserGroupDataRequest importUserGroupDataRequest : request.getDataArr()) {
                //判断用户组是不是已经迁移过
                if (importUserGroupDataRequest.getAuthType() == AuthType.AD) {
                    adUserGroupList.add(importUserGroupDataRequest);
                } else {
                    normalUserGroupList.add(importUserGroupDataRequest);
                }
            }
        }

        // 2、处理AD用户组
        List<UserCustomDataResponse> userCustomDataResponseList = dealADUserGroup(adUserGroupList, stringSystemBusinessMappingDTOMap);
        // 3、处理普通用户组
        userCustomDataResponseList.addAll(dealNormalUserGroup(normalUserGroupList, stringSystemBusinessMappingDTOMap));

        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_END);
        return userCustomDataResponseList;
    }

    /**
     * @param normalUserGroupList
     * @throws BusinessException
     */
    private List<UserCustomDataResponse> dealNormalUserGroup(List<ImportUserGroupDataRequest> normalUserGroupList,
            Map<String, SystemBusinessMappingDTO> systemBusinessMappingDTOMap) throws BusinessException {

        List<UserCustomDataResponse> userCustomDataResponseList = new ArrayList<>();
        Map<String, ImportUserGroupDataRequest> oldUserGroupMap = new HashMap<>(normalUserGroupList.size());
        Map<String, UUID> oldToNewUserGroupMapping = new HashMap<>(normalUserGroupList.size());
        Map<UUID, String> newToOldUserGroupMapping = new HashMap<>(normalUserGroupList.size());
        List<IacUserGroupDetailDTO> createdUserGroupList = new ArrayList<>();
        normalUserGroupList.sort((o1, o2) -> {
            Integer o1p = Integer.parseInt(o1.getParentGroupId());
            Integer o2p = Integer.parseInt(o2.getParentGroupId());
            return  o1p.compareTo(o2p);
        });

        //开始创建新普通用户组，默认parentId为null
        for (ImportUserGroupDataRequest normalUserGroup : normalUserGroupList) {
            //开始构造旧平台的用户组关系
            oldUserGroupMap.put(normalUserGroup.getId(), normalUserGroup);

            // 如果是根节点不处理。
            if (normalUserGroup.getId().equals(DEFAULT_ROOT_ID)) {
                continue;
            }

            // 如果是未分组，直接建立关系。
            if (normalUserGroup.getId().equals(DEFAULT_UNGROUP_ID)) {
                SystemBusinessMappingDTO unGroupMappingDTO = systemBusinessMappingDTOMap.get(normalUserGroup.getId());
                if (unGroupMappingDTO == null) {
                    unGroupMappingDTO = new SystemBusinessMappingDTO(
                        SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP,
                        normalUserGroup.getId(), IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID.toString(),
                        JSONObject.toJSONString(normalUserGroup)
                    );
                    systemBusinessMappingAPI.saveSystemBusinessMapping(unGroupMappingDTO);
                }
                continue;
            }

            // 处理普通用户组
            IacCreateUserGroupDTO dto = new IacCreateUserGroupDTO();
            dto.setName(normalUserGroup.getName());
            try {
                SystemBusinessMappingDTO normalUserGroupMappingDTO = systemBusinessMappingDTOMap.get(normalUserGroup.getId());
                IacUserGroupDetailDTO existGroup = null;
                if (normalUserGroupMappingDTO != null) {
                    try {
                        existGroup = cbbUserGroupAPI.getUserGroupDetail(UUID.fromString(normalUserGroupMappingDTO.getDestId()));
                    } catch (BusinessException e) {
                        LOGGER.error("迁移普通用户组报错：[{}]", e.getI18nMessage());
                    }
                }

                UUID groupId;
                UUID parentGroupId = oldToNewUserGroupMapping.get(normalUserGroup.getParentGroupId());
                if (existGroup == null) {
                    // 用户组在新平台不存在的时候创建
                    dto.setParentId(parentGroupId);
                    IacUserGroupDetailDTO cbbUserGroupDetailDTO = cbbUserGroupAPI.createUserGroup(dto);
                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_SUCCESS_LOG, dto.getName());
                    createdUserGroupList.add(cbbUserGroupDetailDTO);
                    groupId = cbbUserGroupDetailDTO.getId();
                } else {
                    // 用户组在新平台存在的时候编辑
                    groupId = existGroup.getId();
                    IacUpdateUserGroupDTO updateUserGroupDTO = new IacUpdateUserGroupDTO();
                    updateUserGroupDTO.setName(normalUserGroup.getName());
                    updateUserGroupDTO.setParentId(parentGroupId);
                    updateUserGroupDTO.setId(groupId);
                    cbbUserGroupAPI.updateUserGroup(updateUserGroupDTO);
                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_SUCCESS_LOG, dto.getName());
                    existGroup.setName(normalUserGroup.getName());
                    createdUserGroupList.add(existGroup);
                }

                // 建立新旧分组的mapping
                oldToNewUserGroupMapping.put(normalUserGroup.getId(), groupId);
                newToOldUserGroupMapping.put(groupId, normalUserGroup.getId());

                normalUserGroupMappingDTO = Optional.ofNullable(normalUserGroupMappingDTO).orElseGet(
                    () -> new SystemBusinessMappingDTO(
                        SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP,
                        normalUserGroup.getId(),
                        groupId.toString(),
                        JSONObject.toJSONString(normalUserGroup)
                    ));
                normalUserGroupMappingDTO.setDestId(groupId.toString());
                normalUserGroupMappingDTO.setContext(JSONObject.toJSONString(normalUserGroup));
                systemBusinessMappingAPI.saveSystemBusinessMapping(normalUserGroupMappingDTO);

            } catch (BusinessException e) {
                LOGGER.error("迁移普通用户组 [" + normalUserGroup.getId() + "] 出错", e);
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_FAIL_LOG, e, normalUserGroup.getName(),
                        normalUserGroup.getAuthType().name(), e.getI18nMessage());

                UserCustomDataResponse userCustomDataResponse =
                        new UserCustomDataResponse(normalUserGroup.getId(), normalUserGroup.getName()
                                , e.getKey(), e.getI18nMessage());
                userCustomDataResponseList.add(userCustomDataResponse);
            }
        }
        return userCustomDataResponseList;

    }


    /**
     * 处理AD域用户组，如果旧平台用户组不在新平台里，则报错
     *
     * @param adUserGroupList
     * @return 返回有问题的用户数组
     */
    private List<UserCustomDataResponse> dealADUserGroup(List<ImportUserGroupDataRequest> adUserGroupList,
                                                         Map<String, SystemBusinessMappingDTO> systemBusinessMappingDTOMap) throws BusinessException {
        List<UserCustomDataResponse> userCustomDataResponseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(adUserGroupList)) {
            IacUserGroupDetailDTO[] newAllAdUserGroupArr = cbbUserGroupAPI.getAllUserGroup();
            //构建ad用户map
            Map<UUID, IacUserGroupDetailDTO> adUserGroupMap = new HashMap<>(newAllAdUserGroupArr.length);
            Map<String, List<IacUserGroupDetailDTO>> newAdUserGroupNameListMap = new HashMap<>(newAllAdUserGroupArr.length);
            for (IacUserGroupDetailDTO cbbUserGroupDetailDTO : newAllAdUserGroupArr) {
                if (cbbUserGroupDetailDTO.isAdGroup()) {
                    String name = cbbUserGroupDetailDTO.getName();
                    adUserGroupMap.put(cbbUserGroupDetailDTO.getId(), cbbUserGroupDetailDTO);

                    // 构造每个ad域用户组map
                    List<IacUserGroupDetailDTO> cbbUserGroupDetailDTOList = newAdUserGroupNameListMap.get(name);
                    if (cbbUserGroupDetailDTOList == null) {
                        cbbUserGroupDetailDTOList = new ArrayList<>();
                    }
                    cbbUserGroupDetailDTOList.add(cbbUserGroupDetailDTO);
                    newAdUserGroupNameListMap.put(name, cbbUserGroupDetailDTOList);
                }
            }

            Map<String, ImportUserGroupDataRequest> oldAdUserGroupMap = new HashMap<>(adUserGroupList.size());
            for (ImportUserGroupDataRequest importUserGroupDataRequest : adUserGroupList) {
                oldAdUserGroupMap.put(importUserGroupDataRequest.getId(), importUserGroupDataRequest);
            }

            //开始创建ad域用户组映射关系
            for (ImportUserGroupDataRequest adUserGroup : adUserGroupList) {
                String groupName = adUserGroup.getName();
                IacUserGroupDetailDTO cbbUserGroupDetailDTO = null;
                List<IacUserGroupDetailDTO> cbbUserGroupDetailDTOList = newAdUserGroupNameListMap.get(groupName);
                if (cbbUserGroupDetailDTOList != null) {
                    if (cbbUserGroupDetailDTOList.size() == 1) {
                        //正常添加映射关系
                        cbbUserGroupDetailDTO = cbbUserGroupDetailDTOList.iterator().next();
                    } else {
                        StringBuilder oldAdUserGroupNameParents = new StringBuilder();
                        oldGroupNameParent(adUserGroup, oldAdUserGroupMap, oldAdUserGroupNameParents);
                        oldAdUserGroupNameParents.append(adUserGroup.getName());

                        StringBuilder newAdUserGroupNameParents;
                        String cbbUserGroupName;
                        for (IacUserGroupDetailDTO cbbUserGroup : cbbUserGroupDetailDTOList) {
                            newAdUserGroupNameParents = new StringBuilder();
                            cbbUserGroupName = cbbUserGroup.getName();
                            newGroupNameParent(cbbUserGroup, adUserGroupMap, newAdUserGroupNameParents);
                            newAdUserGroupNameParents.append(cbbUserGroupName);
                            if (oldAdUserGroupNameParents.toString().equals(newAdUserGroupNameParents.toString())) {
                                cbbUserGroupDetailDTO = cbbUserGroup;
                                break;
                            }
                        }
                    }
                }

                if (cbbUserGroupDetailDTO != null) {
                    UUID userGroupId = cbbUserGroupDetailDTO.getId();
                    SystemBusinessMappingDTO adUserGroupMappingDTO =
                        Optional.ofNullable(systemBusinessMappingDTOMap.get(adUserGroup.getId())).orElseGet(
                            () -> new SystemBusinessMappingDTO(
                                    SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP,
                                    adUserGroup.getId(),
                                    userGroupId.toString(),
                                    JSONObject.toJSONString(adUserGroup)
                            ));
                    adUserGroupMappingDTO.setDestId(userGroupId.toString());
                    adUserGroupMappingDTO.setContext(JSONObject.toJSONString(adUserGroup));
                    systemBusinessMappingAPI.saveSystemBusinessMapping(adUserGroupMappingDTO);
                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_SUCCESS_LOG, cbbUserGroupDetailDTO.getName());
                } else {
                    // 旧平台用户组在新平台不存在
                    UserCustomDataResponse userCustomDataResponse = new UserCustomDataResponse(
                            adUserGroup.getId(), adUserGroup.getName(), RestErrorCode.RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST);

                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_FAIL_LOG,
                            adUserGroup.getName(),
                            adUserGroup.getAuthType().name(),
                            LocaleI18nResolver.resolve(RestErrorCode.RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST));
                    userCustomDataResponseList.add(userCustomDataResponse);
                }
            }
        }

        return userCustomDataResponseList;
    }

    /**
     * 构造旧平台用户组的
     *
     * @param source
     * @param oldAdUserGroupMap
     * @return
     */
    private void oldGroupNameParent(ImportUserGroupDataRequest source, Map<String, ImportUserGroupDataRequest> oldAdUserGroupMap, StringBuilder sb) {
        if (source == null) {
            return;
        }
        ImportUserGroupDataRequest parent = oldAdUserGroupMap.get(source.getParentGroupId());
        oldGroupNameParent(parent, oldAdUserGroupMap, sb);
        if (parent != null && !parent.getId().equals(DEFAULT_ROOT_ID)) {
            sb.append(parent.getName()).append(Constants.COMMA_SEPARATION_CHARACTER);
        }
    }

    /**
     * @param source
     * @param adUserGroupMap
     * @param sb
     */
    private void newGroupNameParent(IacUserGroupDetailDTO source, Map<UUID, IacUserGroupDetailDTO> adUserGroupMap, StringBuilder sb) {
        if (source == null) {
            return;
        }
        IacUserGroupDetailDTO parent = adUserGroupMap.get(source.getParentId());
        newGroupNameParent(parent, adUserGroupMap, sb);
        if (parent != null) {
            sb.append(parent.getName()).append(Constants.COMMA_SEPARATION_CHARACTER);
        }
    }


    /**
     * 导入用户
     *
     * @param request 请求
     * @return List<UserCustomDataResponse>
     * @throws ExecutionException ExecutionException
     * @throws InterruptedException InterruptedException
     */
    @Override
    public List<UserCustomDataResponse> createUser(ImportUserRequest request) throws ExecutionException, InterruptedException {
        Assert.notNull(request, "ImportUserRequest is not null");

        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_BEGIN, String.valueOf(request.getDataArr().length));
        List<UserCustomDataResponse> dataList = new ArrayList<>();
        List<FutureTask<UserCustomDataResponse>> futureTaskList = new ArrayList<>();
        for (ImportUserDataRequest importUserDataRequest : request.getDataArr()) {
            FutureTask futureTask = dealCreateUser(importUserDataRequest);
            USER_MIGRATION_THREADS.execute(futureTask);
            futureTaskList.add(futureTask);
        }

        for (FutureTask<UserCustomDataResponse> futureTask : futureTaskList) {
            UserCustomDataResponse userCustomDataResponse = futureTask.get();
            if (userCustomDataResponse != null) {
                dataList.add(userCustomDataResponse);
            }
        }
        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_END);
        return dataList;
    }

    @Override
    public PageQueryResponse<SystemBusinessMappingDTO> pageQueryUserMapping(PageServerRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        Integer page = Optional.ofNullable(request.getPage()).orElse(0);
        Integer limit = Optional.ofNullable(request.getLimit()).orElse(1);
        PageQueryBuilderFactory.RequestBuilder builder = pageQueryBuilderFactory.newRequestBuilder() //
                .setPageLimit(page, limit)
                .asc(CREATE_DATE_COLUMN_NAME);
        PageQueryRequest pageQueryRequest = builder.eq(BUSINESS_TYPE_COLUMN_NAME, SyncUpgradeConsts.BUSINESS_TYPE_USER).build();

        return systemBusinessMappingAPI.pageQuery(pageQueryRequest);
    }

    /**
     * @param importUserDataRequest
     * @return
     */
    private FutureTask<UserCustomDataResponse> dealCreateUser(ImportUserDataRequest importUserDataRequest) {
        return new FutureTask<>(() -> {
            UserCustomDataResponse userCustomDataResponse = null;
            //判断用户是不是已经迁移过
            SystemBusinessMappingDTO userMappingDto =
                    systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                            SyncUpgradeConsts.BUSINESS_TYPE_USER, importUserDataRequest.getUserId());
            IacUserDetailDTO existCbbUserDetailDTO = null;
            if (userMappingDto != null) {
                try {
                    existCbbUserDetailDTO = cbbUserAPI.getUserDetail(UUID.fromString(userMappingDto.getDestId()));
                    //如果用户是禁用状态，直接提示
                    if (existCbbUserDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
                        userCustomDataResponse = new UserCustomDataResponse(importUserDataRequest.getUserId(),
                                importUserDataRequest.getUserName(), RestErrorCode.OPEN_API_USER_NOT_EXISTS);
                        return userCustomDataResponse;
                    }
                } catch (BusinessException e) {
                    LOGGER.info("迁移用户出错： {}", e.getI18nMessage());
                }
            }

            SystemBusinessMappingDTO userGroupMappingDTO =
                    systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                            SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP, importUserDataRequest.getUserGroupId());

            if (userGroupMappingDTO != null) {
                try {
                    String userName = importUserDataRequest.getUserName();
                    userMappingDto = Optional.ofNullable(userMappingDto).orElseGet(() -> new SystemBusinessMappingDTO(
                            SyncUpgradeConsts.BUSINESS_TYPE_USER, importUserDataRequest.getUserId(),
                            null, JSONObject.toJSONString(importUserDataRequest)
                    ));

                    if (importUserDataRequest.getAuthType() == AuthType.LOCAL) {
                        importLocalUser(existCbbUserDetailDTO, importUserDataRequest, userGroupMappingDTO, userMappingDto);
                    } else {
                        //AD用户若在新平台找不到，则提示异常
                        List<IacUserDetailDTO> cbbUserDetailDTOList =
                                cbbUserAPI.listUserByUserNames(Arrays.asList(importUserDataRequest.getUserName()));
                        Boolean isAdUserExist = false;
                        if (CollectionUtils.isNotEmpty(cbbUserDetailDTOList)) {
                            for (IacUserDetailDTO cbbUserDetailDTO : cbbUserDetailDTOList) {
                                if (cbbUserDetailDTO.getUserType() == IacUserTypeEnum.AD
                                        && cbbUserDetailDTO.getUserState() == IacUserStateEnum.ENABLE) {
                                    userMappingDto.setDestId(cbbUserDetailDTO.getId().toString());
                                    systemBusinessMappingAPI.saveSystemBusinessMapping(userMappingDto);
                                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_SUCCESS_LOG, userName);
                                    isAdUserExist = true;
                                    break;
                                }
                            }
                        }

                        if (!isAdUserExist) {
                            userCustomDataResponse = new UserCustomDataResponse(importUserDataRequest.getUserId(),
                                    importUserDataRequest.getUserName(), RestErrorCode.OPEN_API_USER_NOT_EXISTS);

                            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_FAIL_LOG, importUserDataRequest.getUserName(),
                                    importUserDataRequest.getAuthType().name(), LocaleI18nResolver.resolve(RestErrorCode.OPEN_API_USER_NOT_EXISTS));
                        }
                    }
                } catch (BusinessException e) {
                    LOGGER.error("迁移用户 [" + importUserDataRequest.getUserId() + "] 出错,", e);
                    userCustomDataResponse = new UserCustomDataResponse(importUserDataRequest.getUserId(),
                            importUserDataRequest.getUserName(), e.getKey(), e.getI18nMessage());

                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_FAIL_LOG, e, importUserDataRequest.getUserName(),
                            importUserDataRequest.getAuthType().name(), e.getI18nMessage());
                }
            } else {
                userCustomDataResponse = new UserCustomDataResponse(importUserDataRequest.getUserId(),
                        importUserDataRequest.getUserName(), RestErrorCode.RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST);

                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_FAIL_LOG, importUserDataRequest.getUserName(),
                        importUserDataRequest.getAuthType().name(),
                        LocaleI18nResolver.resolve(RestErrorCode.RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST));
            }
            return userCustomDataResponse;
        });
    }

    /**
     *
     * @param existCbbUserDetailDTO
     * @param importUserDataRequest
     * @param userGroupMappingDTO
     * @param userMappingDto
     * @throws BusinessException
     */
    private void importLocalUser(IacUserDetailDTO existCbbUserDetailDTO, ImportUserDataRequest importUserDataRequest,
                                 SystemBusinessMappingDTO userGroupMappingDTO, SystemBusinessMappingDTO userMappingDto) throws BusinessException {
        String userName = importUserDataRequest.getUserName();
        if (existCbbUserDetailDTO != null) {
            //如果已存在，则直接更新
            IacUpdateUserDTO cbbUpdateUserDTO = new IacUpdateUserDTO();
            BeanUtils.copyProperties(existCbbUserDetailDTO, cbbUpdateUserDTO);
            cbbUpdateUserDTO.setRealName(importUserDataRequest.getRealName());
            cbbUpdateUserDTO.setPhoneNum(importUserDataRequest.getPhoneNum());
            cbbUpdateUserDTO.setUserName(userName);
            cbbUpdateUserDTO.setGroupId(UUID.fromString(userGroupMappingDTO.getDestId()));
            cbbUserAPI.updateUser(cbbUpdateUserDTO);
            userMappingDto.setContext(JSONObject.toJSONString(importUserDataRequest));
            userMappingDto.setDestId(cbbUpdateUserDTO.getId().toString());
        } else {
            IacCreateUserDTO dto = new IacCreateUserDTO();
            BeanUtils.copyProperties(importUserDataRequest, dto);
            dto.setGroupId(UUID.fromString(userGroupMappingDTO.getDestId()));
            dto.setUserType(IacUserTypeEnum.NORMAL);
            IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.createUserWithoutAdCheck(dto);
            UUID userId = cbbUserDetailDTO.getId();
            userMappingDto.setDestId(userId.toString());
        }
        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_SUCCESS_LOG, userName);
        systemBusinessMappingAPI.saveSystemBusinessMapping(userMappingDto);
    }


    /**
     * 用户关联关系导入
     *
     * @param request 请求
     * @return CommonResponse 响应
     * @throws ExecutionException 异常
     * @throws InterruptedException 异常
     */
    @Override
    public List<UserCustomDataResponse> createUserRelation(ImportRelationRequest request) throws ExecutionException, InterruptedException {
        Assert.notNull(request, "ImportUserRelationRequest is not null");

        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_IMAGE_RELATION_BEGIN, String.valueOf(request.getDataArr().length));
        List<UserCustomDataResponse> dataList = new ArrayList<>();
        List<FutureTask<UserCustomDataResponse>> futureTaskList = new ArrayList<>();
        for (ImportRelationDataRequest importRelationDataRequest : request.getDataArr()) {
            FutureTask<UserCustomDataResponse> futureTask = dealCreateUserRelation(importRelationDataRequest);
            USER_MIGRATION_THREADS.execute(futureTask);
            futureTaskList.add(futureTask);
        }

        for (FutureTask<UserCustomDataResponse> futureTask : futureTaskList) {
            UserCustomDataResponse userCustomDataResponse = futureTask.get();
            if (userCustomDataResponse != null) {
                dataList.add(userCustomDataResponse);
            }
        }

        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_IMAGE_RELATION_END);
        return dataList;
    }


    /**
     * @param importRelationDataRequest
     * @return
     */
    private FutureTask<UserCustomDataResponse> dealCreateUserRelation(ImportRelationDataRequest importRelationDataRequest) {
        return new FutureTask<>(() -> {
            UserCustomDataResponse userCustomDataResponse = null;
            //确认镜像存在
            UUID imageId = getImageId(importRelationDataRequest.getImageId());
            if (imageId == null) {
                userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                        importRelationDataRequest.getName(), RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND);

                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_IMAGE_RELATION_FAIL_LOG,
                        importRelationDataRequest.getName(), LocaleI18nResolver.resolve(RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND));
                return userCustomDataResponse;
            }

            //确认用户存在
            SystemBusinessMappingDTO userMapping =
                    systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                            SyncUpgradeConsts.BUSINESS_TYPE_USER, importRelationDataRequest.getId());
            if (userMapping == null) {
                userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                        importRelationDataRequest.getName(), RestErrorCode.OPEN_API_USER_NOT_EXISTS);

                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_IMAGE_RELATION_FAIL_LOG,
                        importRelationDataRequest.getName(), LocaleI18nResolver.resolve(RestErrorCode.OPEN_API_USER_NOT_EXISTS));
                return userCustomDataResponse;
            } else {
                try {
                    IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(UUID.fromString(userMapping.getDestId()));
                    if (cbbUserDetailDTO.getUserState() == IacUserStateEnum.DISABLE) {
                        userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                                importRelationDataRequest.getName(), RestErrorCode.OPEN_API_USER_NOT_EXISTS);
                        return userCustomDataResponse;
                    }
                } catch (BusinessException e) {
                    userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                            importRelationDataRequest.getName(), e.getKey(), e.getI18nMessage());

                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_IMAGE_RELATION_FAIL_LOG, e,
                            importRelationDataRequest.getName(), e.getI18nMessage());
                    return userCustomDataResponse;
                }
            }
            UUID userId = UUID.fromString(userMapping.getDestId());

            try {
                UUID deskStrategyIDVId = createDeskStrategy(importRelationDataRequest,
                        LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_USER_DESK_STRATEGY_PRE_NAME));
                CreateUserDesktopConfigRequest desktopConfigRequest =
                        new CreateUserDesktopConfigRequest(userId, UserCloudDeskTypeEnum.IDV);
                desktopConfigRequest.setImageTemplateId(imageId);
                desktopConfigRequest.setStrategyId(deskStrategyIDVId);
                userDesktopConfigAPI.createOrUpdateUserDesktopConfig(desktopConfigRequest);
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_IMAGE_RELATION_SUCCESS_LOG, importRelationDataRequest.getName());
            } catch (BusinessException e) {
                LOGGER.error("迁移用户关联关系 [" + importRelationDataRequest.getId() + "] 出错,", e);
                userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                        importRelationDataRequest.getName(), e.getKey(), e.getI18nMessage());

                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_IMAGE_RELATION_FAIL_LOG, e,
                        importRelationDataRequest.getName(), e.getI18nMessage());
            }

            //创建mapping需要判断是否已经创建过
            SystemBusinessMappingDTO userRelationMappingDto =
                    systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                            SyncUpgradeConsts.BUSINESS_TYPE_USER_RELATION, importRelationDataRequest.getId());
            userRelationMappingDto = Optional.ofNullable(userRelationMappingDto).orElseGet(
                () -> new SystemBusinessMappingDTO(
                    SyncUpgradeConsts.BUSINESS_TYPE_USER_RELATION,
                    importRelationDataRequest.getId(),
                    userMapping.getDestId(),
                    JSONObject.toJSONString(importRelationDataRequest)));
            userRelationMappingDto.setDestId(userMapping.getDestId());
            userRelationMappingDto.setContext(JSONObject.toJSONString(importRelationDataRequest));
            systemBusinessMappingAPI.saveSystemBusinessMapping(userRelationMappingDto);
            return userCustomDataResponse;
        });
    }

    /**
     * 用户关联关系导入
     *
     * @param request 请求
     * @return CommonResponse 响应
     * @throws InterruptedException 业务异常
     * @throws ExecutionException   业务异常
     */
    @Override
    public List<UserCustomDataResponse> createUserGroupRelation(ImportRelationRequest request) throws ExecutionException, InterruptedException {
        Assert.notNull(request, "ImportGroupRelationRequest is not null");

        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_BEGIN, String.valueOf(request.getDataArr().length));
        List<UserCustomDataResponse> dataList = new ArrayList<>();
        List<FutureTask<UserCustomDataResponse>> futureTaskList = new ArrayList<>();
        for (ImportRelationDataRequest importRelationDataRequest : request.getDataArr()) {
            //用户组【未分组】的关联关系不迁入
            if (!DEFAULT_UNGROUP_ID.equals(importRelationDataRequest.getId())) {
                FutureTask<UserCustomDataResponse> futureTask = dealCreateGroupRelation(importRelationDataRequest);
                USER_MIGRATION_THREADS.execute(futureTask);
                futureTaskList.add(futureTask);
            }
        }

        for (FutureTask<UserCustomDataResponse> futureTask : futureTaskList) {
            UserCustomDataResponse userCustomDataResponse = futureTask.get();
            if (userCustomDataResponse != null) {
                dataList.add(userCustomDataResponse);
            }
        }

        auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_END);
        return dataList;
    }

    /**
     * @param importRelationDataRequest
     * @return
     */
    private FutureTask<UserCustomDataResponse> dealCreateGroupRelation(ImportRelationDataRequest importRelationDataRequest) {
        return new FutureTask<>(() -> {
            UserCustomDataResponse userCustomDataResponse = null;

            //确认镜像存在
            UUID imageId = getImageId(importRelationDataRequest.getImageId());
            if (imageId == null) {
                userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                        importRelationDataRequest.getName(), RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND);

                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_FAIL_LOG,
                        importRelationDataRequest.getName(), LocaleI18nResolver.resolve(RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND));
                return userCustomDataResponse;
            }

            //确认用户组存在
            SystemBusinessMappingDTO userGroupMapping =
                    systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                            SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP, importRelationDataRequest.getId());
            if (userGroupMapping == null) {
                userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                        importRelationDataRequest.getName(), RestErrorCode.RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST);

                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_FAIL_LOG,
                        importRelationDataRequest.getName(), LocaleI18nResolver.resolve(RestErrorCode.RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST));
                return userCustomDataResponse;
            } else {
                try {
                    cbbUserGroupAPI.getUserGroupDetail(UUID.fromString(userGroupMapping.getDestId()));
                } catch (BusinessException e) {
                    userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                            importRelationDataRequest.getName(), e.getKey(), e.getI18nMessage());

                    auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_FAIL_LOG, e,
                            importRelationDataRequest.getName(), e.getI18nMessage());
                    return userCustomDataResponse;
                }
            }
            UUID groupId = UUID.fromString(userGroupMapping.getDestId());

            try {
                UUID deskStrategyIDVId = createDeskStrategy(importRelationDataRequest,
                        LocaleI18nResolver.resolve(BusinessKey.OPEN_API_MIGRATION_USER_GROUP_DESK_STRATEGY_PRE_NAME));
                CreateUserGroupDesktopConfigRequest idvConfigRequest = new CreateUserGroupDesktopConfigRequest(groupId, UserCloudDeskTypeEnum.IDV);
                idvConfigRequest.setStrategyId(deskStrategyIDVId);
                idvConfigRequest.setImageTemplateId(imageId);
                userDesktopConfigAPI.createOrUpdateUserGroupDesktopConfig(idvConfigRequest);
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_LOG, importRelationDataRequest.getName());

                SystemBusinessMappingDTO groupRelationMappingDto =
                        systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                                SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP_RELATION, importRelationDataRequest.getId());
                groupRelationMappingDto = Optional.ofNullable(groupRelationMappingDto).orElseGet(
                    () -> new SystemBusinessMappingDTO(
                        SyncUpgradeConsts.BUSINESS_TYPE_USER_GROUP_RELATION,
                        importRelationDataRequest.getId(),
                        userGroupMapping.getDestId(),
                        JSONObject.toJSONString(importRelationDataRequest)
                    ));
                groupRelationMappingDto.setDestId(userGroupMapping.getDestId());
                groupRelationMappingDto.setContext(JSONObject.toJSONString(importRelationDataRequest));
                systemBusinessMappingAPI.saveSystemBusinessMapping(groupRelationMappingDto);

            } catch (BusinessException e) {
                LOGGER.error("迁移用户组关联关系[" + importRelationDataRequest.getId() + "]出错", e);
                userCustomDataResponse = new UserCustomDataResponse(importRelationDataRequest.getId(),
                        importRelationDataRequest.getName(), e.getKey(), e.getI18nMessage());

                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_USER_GROUP_IMAGE_RELATION_FAIL_LOG, e,
                        importRelationDataRequest.getName(), e.getI18nMessage());
            }
            return userCustomDataResponse;
        });
    }

    /**
     * 判断镜像是否迁移成功，
     *
     * @param oldImageId 旧平台镜像ID
     * @return UUID 镜像ID
     */
    private UUID getImageId(String oldImageId) {
        UUID imageId = null;
        SystemBusinessMappingDTO imageMapping =
                systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                        SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, oldImageId);

        if (imageMapping != null && StringUtils.isNotBlank(imageMapping.getDestId())) {
            try {
                CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO =
                        cbbImageTemplateMgmtAPI.getImageTemplateDetail(UUID.fromString(imageMapping.getDestId()));
                if (cbbImageTemplateDetailDTO.getLastRecoveryPointId() != null) {
                    imageId = UUID.fromString(imageMapping.getDestId());
                }
            } catch (Exception ex) {
                LOGGER.error("迁移镜像数据出错，旧镜像:[" + oldImageId + "]", ex);
            }
        }
        return imageId;
    }

    /**
     * 创建桌面策略
     *
     * @param importRelationDataRequest
     * @param preName
     * @return
     * @throws BusinessException
     */
    private UUID createDeskStrategy(ImportRelationDataRequest importRelationDataRequest, String preName) throws BusinessException {
        //创建云桌面策略
        CbbDeskStrategyIDVDTO idvRequest = new CbbDeskStrategyIDVDTO();
        idvRequest.setAllowLocalDisk(importRelationDataRequest.getUseLocalDisk());
        idvRequest.setOpenDesktopRedirect(importRelationDataRequest.getOpenDesktopRedirect());
        idvRequest.setPattern(importRelationDataRequest.getDesktopType());
        idvRequest.setSystemSize(importRelationDataRequest.getSystemDisk());
        idvRequest.setName(preName + Constants.UNDERLINE + systemBusinessMappingAPI.obtainMappingSequenceVal());
        idvRequest.setAdOu("");
        idvRequest.setEnableNested(false);
        idvRequest.setOpenUsbReadOnly(false);
        idvRequest.setDeskPersonalConfigStrategyType(businessCommonService.getCbbDeskPersonalConfigStrategyType(idvRequest.getPattern()));

        AtomicReference<UUID> deskStrategyIDVIdRef = new AtomicReference<>();
        LockableExecutor.executeWithTryLock(DESK_STRATEGY_KEY, () -> {
            // 云桌面策略关联外设策略
            if (importRelationDataRequest.getUsbTypeIdArr() != null) {
                UUID[] usbIdArr = Arrays.stream(importRelationDataRequest.getUsbTypeIdArr()).
                        map(ImportDesktopConfig.USBType::getId).toArray(UUID[]::new);
                idvRequest.setUsbTypeIdArr(usbIdArr);
            }

            deskStrategyIDVIdRef.set(businessCommonService.getRepeatDeskStrategy(idvRequest, preName));
            if (deskStrategyIDVIdRef.get() == null) {
                deskStrategyIDVIdRef.set(cbbIDVDeskStrategyMgmtAPI.createDeskStrategyIDV(idvRequest));
                cloudDeskComputerNameConfigAPI.createCloudDeskComputerNameConfig(PREFIX_COMPUTER_NAME, deskStrategyIDVIdRef.get());

                // 如果存在应用分发桌面且当前应用分发是隐藏的则去更新应用分发隐藏策略
                if (CbbCloudDeskPattern.APP_LAYER == idvRequest.getPattern()
                        && cbbGlobalStrategyMgmtAPI.getAppLayerHidden()) {
                    cbbGlobalStrategyMgmtAPI.updateAppLayerHidden();
                    // 通知所有管理员退出登录(因为全局策略修订，前端需要刷新)
                    adminActionMsgSPI.notifyAllAdminLogout();
                }
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_RELATION_LOG, idvRequest.getName());
            }
        }, 60);
        return deskStrategyIDVIdRef.get();
    }


    /**
     * AD域的配置信息查询
     *
     * @return CommonResponse 响应
     * @throws BusinessException 业务异常
     */
    @Override
    public AdConfigResponse getADConfig() throws BusinessException {
        AdConfigResponse adConfigResponse = null;
        IacDomainConfigDetailDTO cbbDomainConfigDetailDTO = cbbAdMgmtAPI.getAdConfig();
        if (cbbDomainConfigDetailDTO != null && cbbDomainConfigDetailDTO.getEnable()) {
            adConfigResponse = new AdConfigResponse();
            adConfigResponse.setEnable(cbbDomainConfigDetailDTO.getEnable());
            adConfigResponse.setDomainName(cbbDomainConfigDetailDTO.getDomainName());
            adConfigResponse.setMainServer(cbbDomainConfigDetailDTO.getMainServer());
            adConfigResponse.setBackupServerArr(cbbDomainConfigDetailDTO.getBackupServerArr());
            adConfigResponse.setServerIp(cbbDomainConfigDetailDTO.getMainServer().getServerAddress());
            adConfigResponse.setServerName(cbbDomainConfigDetailDTO.getServerName());
            adConfigResponse.setServerPort(cbbDomainConfigDetailDTO.getServerPort());
            adConfigResponse.setManagerName(cbbDomainConfigDetailDTO.getManagerName());
            adConfigResponse.setAutoJoin(cbbDomainConfigDetailDTO.getAutoJoin());
            adConfigResponse.setCoverType(cbbDomainConfigDetailDTO.getCoverType());
            adConfigResponse.setAdAutoLogon(autoLogonAPI.getGlobalAutoLogonStrategy(AutoLogonEnum.AD_AUTO_LOGON));
        }
        return adConfigResponse;
    }


}
