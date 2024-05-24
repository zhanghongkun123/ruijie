package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.common;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCheckDeskPoolNameDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask.CreatePoolDesktopBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask.CreatePoolThirdPartyDesktopBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask.CreateThirdPartyDesktopBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request.CreateThirdPartyDesktopPoolWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request.DesktopPoolAddComputerWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request.DesktopPoolPageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request.CreateComputerDesktopBatchTaskHandlerRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 池桌面WEB协助类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-07-15
 *
 * @author linke
 */
@Service
public class DesktopPoolWebHelper {

    private static final UUID CREATE_POOL_DESKTOP_TASK_ID = UUID.nameUUIDFromBytes("create_pool_desktop".getBytes(StandardCharsets.UTF_8));

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Autowired
    private DesktopPoolComputerMgmtAPI desktopPoolComputerMgmtAPI;

    @Autowired
    private DesktopPoolThirdPartyMgmtAPI desktopPoolThirdPartyMgmtAPI;

    /**
     * 多会话支持系统版本列表
     */
    private static final List<String> MULTI_SESSION_IMAGE_OS_LIST = Arrays.asList(CbbOsType.WIN_SERVER_2016_64.getOsName(),
            CbbOsType.WIN_SERVER_2019_64.getOsName());


    /**
     * 判断是否需要实时变更策略
     *
     * @param desktopPoolBasicDTO cbbDesktopPoolDTO
     * @return true 需要实时变更策略
     */
    public boolean isSkipAutoUpdateStrategy(DesktopPoolBasicDTO desktopPoolBasicDTO) {
        Assert.notNull(desktopPoolBasicDTO, "desktopPoolBasicDTO is null");
        // 目前只有单会话动态池需要自动变更
        return desktopPoolBasicDTO.getPoolModel() != CbbDesktopPoolModel.DYNAMIC;
    }

    /**
     * 修改池状态为可用
     *
     * @param desktopPoolDTO desktopPoolDTO
     */
    public void updatePoolStateFinish(DesktopPoolBasicDTO desktopPoolDTO) {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO is null");
        cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
    }

    /**
     * 根据池ID获取所有关联桌面ID
     *
     * @param idArr 池ID
     * @return List<UUID>
     */
    public List<UUID> listDesktopIdByPoolIds(UUID[] idArr) {
        Assert.notNull(idArr, "idArr is null");
        List<PoolDesktopInfoDTO> desktopList = Lists.newArrayList();
        List<PoolDesktopInfoDTO> tempList;
        for (UUID id : idArr) {
            tempList = desktopPoolMgmtAPI.listNormalDeskInfoByDesktopPoolId(id);
            if (CollectionUtils.isNotEmpty(tempList)) {
                desktopList.addAll(tempList);
            }
        }
        return desktopList.stream().map(PoolDesktopInfoDTO::getDeskId).collect(Collectors.toList());
    }

    /**
     * 批量新增云桌面
     *
     * @param addNum         数量
     * @param desktopPoolDTO 池
     * @param builder        BatchTaskBuilder
     * @return BatchTaskSubmitResult
     * @throws BusinessException BusinessException
     */
    public BatchTaskSubmitResult batchCreatePoolDesktop(int addNum, DesktopPoolBasicDTO desktopPoolDTO, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO is null");
        Assert.notNull(builder, "builder is null");
        // 获取配置信息
        DesktopPoolConfigDTO desktopPoolConfigDTO = desktopPoolMgmtAPI.getDesktopPoolConfig(desktopPoolDTO.getId());

        int maxIndex = desktopPoolMgmtAPI.getMaxIndexNumWhenAddDesktop(desktopPoolDTO.getId());
        String taskName = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_DESKTOP_TASK_NAME);

        List<DefaultBatchTaskItem> taskItemList = new ArrayList<>();
        for (int i = 0; i < addNum; i++) {
            taskItemList.add(DefaultBatchTaskItem.builder().itemId(UUID.randomUUID()).itemName(taskName).build());
        }
        CreatePoolDesktopBatchTaskHandler handler =
                new CreatePoolDesktopBatchTaskHandler(desktopPoolDTO, maxIndex, taskItemList.iterator(), desktopPoolConfigDTO);
        return builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_DESKTOP_TASK_NAME)
                // 不允许同一个桌面池 同一时间多次创建任务
                .setUniqueId(desktopPoolDTO.getId())
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_DESKTOP_TASK_DESC, desktopPoolDTO.getName()).enableParallel()
                .enablePerformanceMode(CREATE_POOL_DESKTOP_TASK_ID, 30).registerHandler(handler).start();
    }


    /**
     * 验证第三方桌面池
     * @param request request
     * @throws BusinessException 业务异常
     */
    public void validateCreateThirdPartyDesktopPoolParam(CreateThirdPartyDesktopPoolWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is null");

        // 校验桌面池名称是否重复
        CbbCheckDeskPoolNameDTO cbbCheckDeskPoolNameDTO = new CbbCheckDeskPoolNameDTO();
        cbbCheckDeskPoolNameDTO.setId(null);
        cbbCheckDeskPoolNameDTO.setName(request.getName());
        checkDeskPoolNameDuplicate(cbbCheckDeskPoolNameDTO, request.getName());
    }

    /**
     * 校验组ID是否在权限内
     *
     * @param request request
     * @param groupIdArr    groupIdArr
     * @throws BusinessException BusinessException
     */
    public void checkTerminalGroupPermission(DesktopPoolAddComputerWebRequest request, UUID[] groupIdArr) throws BusinessException {
        Assert.notNull(request, "bindObjectDTO is null");
        Assert.notNull(groupIdArr, "groupIdArr is null");
        if (ArrayUtils.isEmpty(groupIdArr)) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NO_TERMINAL_GROUP_AUTH);
        }
        // 校验上传的这些涉及终端组的数据是否有权限
        Set<UUID> authGroupIdSet = Sets.newHashSet(groupIdArr);
        List<UUID> terminalGroupIdList = Arrays.stream(Objects.requireNonNull(request.getTerminalGroupIdArr())).collect(Collectors.toList());
        isGroupsHasAuth(terminalGroupIdList, authGroupIdSet, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NO_TERMINAL_GROUP_AUTH);

        List<UUID> computerIdList = Arrays.stream(Objects.requireNonNull(request.getComputerIdArr())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(computerIdList)) {
            return;
        }
        // 获取pc终端的终端组集合，校验是否有权限
        terminalGroupIdList = computerBusinessAPI.listGroupIdByComputerIdList(computerIdList);
        isGroupsHasAuth(terminalGroupIdList, authGroupIdSet, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NO_COMPUTER_AUTH);
    }

    private void checkDeskPoolNameDuplicate(CbbCheckDeskPoolNameDTO cbbCheckDeskPoolNameDTO, String request) throws BusinessException {
        boolean hasDuplicate = cbbDesktopPoolMgmtAPI.checkDeskPoolNameDuplicate(cbbCheckDeskPoolNameDTO);
        if (hasDuplicate) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NAME_EXIST, request);
        }
    }

    /**
     * 校验组ID是否在权限内
     *
     * @param bindObjectDTO UpdatePoolBindObjectDTO
     * @param groupIdArr    groupIdArr
     * @throws BusinessException BusinessException
     */
    public void checkGroupPermission(UpdatePoolBindObjectDTO bindObjectDTO, UUID[] groupIdArr) throws BusinessException {
        Assert.notNull(bindObjectDTO, "bindObjectDTO is null");
        Assert.notNull(groupIdArr, "groupIdArr is null");
        if (ArrayUtils.isEmpty(groupIdArr)) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NO_USER_GROUP_AUTH);
        }
        // 校验上传的这些涉及用户组的数据是否有权限
        Set<UUID> authGroupIdSet = Sets.newHashSet(groupIdArr);
        List<UUID> groupIdList = bindObjectDTO.getAllInvolvedGroupIdList();
        isGroupsHasAuth(groupIdList, authGroupIdSet, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NO_USER_GROUP_AUTH);

        List<UUID> userIdList = bindObjectDTO.getAllInvolvedUserIdList();
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        // 获取用户的用户组集合，校验是否有权限
        groupIdList = userMgmtAPI.listGroupIdByUserIdList(userIdList);
        isGroupsHasAuth(groupIdList, authGroupIdSet, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NO_USER_AUTH);
    }

    /**
     * 批量新增PC终端云桌面
     *
     * @param computerDTOList  computerDTOList
     * @param desktopPoolDTO desktopPoolDTO
     * @param builder        BatchTaskBuilder
     * @return BatchTaskSubmitResult
     * @throws BusinessException BusinessException
     */
    public BatchTaskSubmitResult batchCreatePoolThirdPartyDesktop(List<ComputerDTO> computerDTOList, DesktopPoolBasicDTO desktopPoolDTO,
                                                                  BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO is not null");
        Assert.notEmpty(computerDTOList, "computerDTOList is not empty");
        Assert.notNull(builder, "builder is not null");
        // 获取配置信息
        DesktopPoolConfigDTO desktopPoolConfigDTO = desktopPoolMgmtAPI.getDesktopPoolConfig(desktopPoolDTO.getId());

        ComputerDTO[] computerArr = computerDTOList.toArray(new ComputerDTO[0]);

        final Iterator<CreateThirdPartyDesktopBatchTaskItem> iterator =
                Stream.of(computerArr).map(computer -> CreateThirdPartyDesktopBatchTaskItem.builder().itemId(UUID.randomUUID()) //
                        .itemName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_DESKTOP_TASK_NAME, new String[] {})
                        .itemComputer(computer).build()).iterator();
        CreateComputerDesktopBatchTaskHandlerRequest request = new CreateComputerDesktopBatchTaskHandlerRequest();

        request.setAuditLogAPI(auditLogAPI);
        request.setCbbDesktopPoolMgmtAPI(cbbDesktopPoolMgmtAPI);
        request.setDesktopPoolMgmtAPI(desktopPoolMgmtAPI);
        request.setDesktopPoolDTO(desktopPoolDTO);
        request.setHasSecondAdd(desktopPoolDTO.getHasSecondAdd());
        request.setDesktopPoolComputerMgmtAPI(desktopPoolComputerMgmtAPI);
        request.setDesktopPoolThirdPartyMgmtAPI(desktopPoolThirdPartyMgmtAPI);
        CreatePoolThirdPartyDesktopBatchTaskHandler handler =
                new CreatePoolThirdPartyDesktopBatchTaskHandler(request, iterator, desktopPoolConfigDTO);
        return builder.setTaskName(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_DESKTOP_TASK_NAME)
                // 不允许同一个桌面池 同一时间多次创建任务
                .setUniqueId(desktopPoolDTO.getId())
                .setTaskDesc(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_CREATE_DESKTOP_TASK_DESC, desktopPoolDTO.getName()).enableParallel()
                .enablePerformanceMode(CREATE_POOL_DESKTOP_TASK_ID, 30).registerHandler(handler).start();
    }

    private void isGroupsHasAuth(List<UUID> groupIdList, Set<UUID> authGroupIdSet, String errorKey) throws BusinessException {
        if (CollectionUtils.isEmpty(groupIdList)) {
            return;
        }
        // 检查是否存在不在这个管理员权限内的组ID
        if (groupIdList.stream().anyMatch(item -> !authGroupIdSet.contains(item))) {
            throw new BusinessException(errorKey);
        }
    }

    /**
     * 处理是否可用
     *
     * @param pageReq pageReq
     * @param resp resp
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    public DefaultPageResponse<DesktopPoolDTO> dealCanUsed(DesktopPoolPageRequest pageReq, DefaultPageResponse<DesktopPoolDTO> resp)
            throws BusinessException {
        Assert.notNull(pageReq, "pageReq is null");
        Assert.notNull(resp, "resp is null");

        if (ArrayUtils.isEmpty(resp.getItemArr())) {
            return resp;
        }

        dealCanUsedByDesktopId(pageReq.getDesktopId(), resp);
        return resp;
    }

    private void dealCanUsedByDesktopId(UUID desktopId, DefaultPageResponse<DesktopPoolDTO> resp) throws BusinessException {
        if (Objects.isNull(desktopId)) {
            return;
        }
        CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
        String message = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_PATTERN_DIFFERENT);
        for (DesktopPoolDTO desktopPoolDTO : resp.getItemArr()) {
            if (desktopPoolDTO.getCanUsed() && !Objects.equals(desktopDetailDTO.getDesktopType(), desktopPoolDTO.getDesktopType().name())) {
                desktopPoolDTO.setCanUsed(Boolean.FALSE);
                desktopPoolDTO.setCanUsedMessage(message);
            }
            CloudPlatformStatus platformStatus = desktopPoolDTO.getPlatformStatus();
            if (platformStatus != null && !CloudPlatformStatus.isAvailableBotNotMaintenance(platformStatus)) {
                desktopPoolDTO.setCanUsed(Boolean.FALSE);
                desktopPoolDTO.setCanUsedMessage(
                        LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_RCO_CLOUD_PLATFORM_IS_UN_AVAILABLE, platformStatus.getDesc()));
            }
        }
    }

    /**
     * 过滤多会话静态池
     *
     * @param resp resp
     * @return DefaultPageResponse
     */
    public DefaultPageResponse<DesktopPoolDTO> dealCanUsedStaticPool(DefaultPageResponse<DesktopPoolDTO> resp) {
        Assert.notNull(resp, "resp is null");
        if (ArrayUtils.isEmpty(resp.getItemArr())) {
            return resp;
        }

        String message = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_NOT_STATIC_MULTI);
        for (DesktopPoolDTO desktopPoolDTO : resp.getItemArr()) {
            if (desktopPoolDTO.getCanUsed() &&
                    (desktopPoolDTO.getPoolModel() == CbbDesktopPoolModel.STATIC && CbbDesktopSessionType.MULTIPLE == desktopPoolDTO.getSessionType())
                    || desktopPoolDTO.getPoolType() == CbbDesktopPoolType.THIRD) {
                desktopPoolDTO.setCanUsed(Boolean.FALSE);
                desktopPoolDTO.setCanUsedMessage(message);
            }
        }
        return resp;
    }

}
