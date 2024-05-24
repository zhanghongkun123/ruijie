package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DesktopLockDiskResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DeskAgentLoadInfoCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopUserSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolAPIHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.cache.DesktopPoolCache;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.DesktopPoolBindGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.DeskAgentLoadInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopUserSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.*;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.DesktopPoolServiceTx;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Description: 桌面池用户相关API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/07/06
 *
 * @author linke
 */
public class DesktopPoolUserMgmtAPIImpl implements DesktopPoolUserMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolUserMgmtAPIImpl.class);

    private static final String RESOURCE_LOCKED_ERROR_KEY = "sk-resource_locked";

    private static final String DESKTOP_POOL_UPDATE_BIND_LOCK = "DESKTOP_POOL_UPDATE_BIND_LOCK_";

    private static final String DESKTOP_POOL_ASSIGN_LOCK = "DESKTOP_POOL_ASSIGN_LOCK_";

    private static final String DESKTOP_POOL_MOVE_LOCK = "DESKTOP_POOL_MOVE_LOCK_";

    private static final int LOCK_TIME = 10;

    private static final Integer SUCCEED = 0;

    private static final Integer HUNDRED = 100;

    private static final Set<CbbCloudDeskState> DESKTOP_READY_STATES_SET = Sets.newHashSet(CbbCloudDeskState.RUNNING, CbbCloudDeskState.CLOSE,
            CbbCloudDeskState.OFF_LINE, CbbCloudDeskState.SLEEP);

    private static final Set<String> SUPPORT_MOVE_DESKTOP_TYPE_SET = Sets.newHashSet(DesktopPoolType.STATIC.name(), DesktopPoolType.COMMON.name());

    private static final Set<String> DESKTOP_MOVE_STATES_SET = Sets.newHashSet(CbbCloudDeskState.CLOSE.name(), CbbCloudDeskState.OFF_LINE.name());

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Autowired
    private DesktopPoolAPIHelper desktopPoolAPIHelper;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private DesktopPoolServiceTx desktopPoolServiceTx;

    @Autowired
    private QueryUserListService queryUserListService;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private BaseAlarmAPI alarmAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private DesktopPoolCache desktopPoolCache;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private DesktopUserSessionDAO desktopUserSessionDAO;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Override
    public boolean checkIsDesktopInUse(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        List<PoolDesktopInfoDTO> deskInfoDTOList = desktopPoolService.listNormalDeskInfoByDesktopPoolId(desktopPoolId);
        if (CollectionUtils.isEmpty(deskInfoDTOList)) {
            LOGGER.info("桌面池[{}]下没有桌面", desktopPoolId);
            return false;
        }

        return deskInfoDTOList.stream().anyMatch(deskInfoDTO -> {
            if (Objects.nonNull(deskInfoDTO.getUserId()) || Objects.nonNull(deskInfoDTO.getTerminalId())) {
                LOGGER.warn("桌面池[{}]下的桌面[{}]正在使用中，userId：{}，terminalId：{}", desktopPoolId, deskInfoDTO.getDeskId(), deskInfoDTO.getUserId(),
                        deskInfoDTO.getTerminalId());
                return true;
            }
            return false;
        });
    }

    @Override
    public List<DesktopPoolUserDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable IacConfigRelatedType relatedType) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        return desktopPoolUserService.listDesktopPoolUser(desktopPoolId, relatedType);
    }

    @Override
    public void unbindUserAllDesktopPool(UUID userId) {
        Assert.notNull(userId, "userId can not be null");

        desktopPoolUserService.deleteByRelatedId(userId);
        // 多会话要删除关联关系
        hostUserService.deleteByUserId(userId);
    }

    @Override
    public DesktopPoolAssignResultDTO assignDesktop(UUID userId, UUID desktopPoolId) throws BusinessException {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        DesktopPoolAssignResultDTO desktopResultDTO = new DesktopPoolAssignResultDTO();
        if (!checkUserInDesktopPool(desktopPoolId, userId)) {
            // 不在池中
            desktopResultDTO.setCode(DesktopPoolConstants.USER_NOT_ASSIGN_POOL);
            LOGGER.warn("用户[{}]未分配桌面池[{}]无法获取池桌面资源", userId, desktopPoolId);
            return desktopResultDTO;
        }

        CbbDesktopPoolDTO desktopPool = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        if (desktopPool.getPoolState() != CbbDesktopPoolState.AVAILABLE && desktopPool.getPoolState() != CbbDesktopPoolState.UPDATING) {
            // 桌面池状态不可以
            desktopResultDTO.setCode(DesktopPoolConstants.DESKTOP_POOL_STATUS_ERROR);
            LOGGER.warn("桌面池[{}]状态为[{}]无法分配资源给用户[{}]", desktopPoolId, desktopPool.getPoolState().name(), userId);
            return desktopResultDTO;
        }
        if (desktopPool.getPoolType() == CbbDesktopPoolType.VDI && !checkImageBeforeAssign(desktopPool, userId)) {
            // 桌面池关联的镜像模板状态不可以启动虚机
            desktopResultDTO.setCode(DesktopPoolConstants.DESKTOP_POOL_IMAGE_STATUS_ERROR);
            return desktopResultDTO;
        }

        if (Boolean.TRUE.equals(desktopPool.getIsOpenMaintenance())) {
            // 开启维护模式不可分配
            desktopResultDTO.setCode(DesktopPoolConstants.DESKTOP_UNDER_MAINTENANCE);
            LOGGER.warn("桌面池[{}]处于维护模式无法分配资源给用户[{}]", desktopPoolId, userId);
            return desktopResultDTO;
        }

        if (!userDesktopMgmtAPI.isAllowDesktopLogin(desktopPool.getStrategyId(), desktopPool.getName(),
                Boolean.TRUE, desktopPool.getPoolType().name())) {
            //云桌面策略开启时间段限制登录使用
            desktopResultDTO.setCode(DesktopPoolConstants.DESKTOP_LOGIN_TIME_LIMIT);
            return desktopResultDTO;
        }
        LockableExecutor.executeWithTryLock(DESKTOP_POOL_ASSIGN_LOCK + desktopPoolId, () -> assignSinglePoolDesktop(desktopResultDTO,
                desktopPool, userId), LOCK_TIME);

        if (!Objects.equals(desktopResultDTO.getCode(), SUCCEED)) {
            // 分配失败返回
            return desktopResultDTO;
        }
        // 分配成功执行后续动作
        return afterAssignSuccess(userId, desktopResultDTO, desktopPool);
    }

    private boolean checkImageBeforeAssign(CbbDesktopPoolDTO desktopPool, UUID userId) throws BusinessException {
        // 校验镜像模板状态
        CbbImageTemplateDetailDTO imageTemplate = cbbImageTemplateMgmtAPI.getImageTemplateDetail(desktopPool.getImageTemplateId());
        ImageTemplateState imageTemplateState = imageTemplate.getImageState();
        if (ImageTemplateState.isInSteadyState(imageTemplateState) || ImageTemplateState.isExporting(imageTemplateState)
                || ImageTemplateState.EDITING == imageTemplateState || ImageTemplateState.GIVEUPING == imageTemplateState
                || ImageTemplateState.COPING == imageTemplateState) {
            return true;
        }
        LOGGER.warn("桌面池[{}]关联的镜像模板[{}]状态为[{}]无法分配资源给用户[{}]", desktopPool.getId(), imageTemplate.getImageName(),
                imageTemplateState.name(), userId);
        return false;
    }

    private void assignSinglePoolDesktop(DesktopPoolAssignResultDTO assignResultDTO, CbbDesktopPoolDTO desktopPool, UUID userId)
            throws BusinessException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始进入池[{}]锁进行分配桌面给用户[{}]", desktopPool.getId(), userId);
        }

        UUID desktopPoolId = desktopPool.getId();
        assignResultDTO.setDesktopPoolType(DesktopPoolType.convertToPoolDeskType(desktopPool.getPoolModel()).name());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("池[{}]锁定后，用户[{}]，获取所有桌面列表结束", desktopPool.getId(), userId);
        }

        // 多会话桌面池分配桌面
        if (desktopPool.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            dealMultipleSessionDesktop(assignResultDTO, desktopPool, userId);
        } else {
            // 判断这个用户是否已分配，如果没有优先返回用户个人盘绑定的桌面
            PoolDesktopInfoDTO desktop = getBindingDesktop(userId, desktopPool.getId());
            // 判断第三方是否离线,删除记录关系后,进行重新分配
            if (Objects.nonNull(desktop) && !checkThirdOffLineAndUnbindUser(desktopPool, desktop.getDeskId(), userId)) {
                // 若桌面绑定的用户为空，则重新绑定
                if (Objects.isNull(desktop.getUserId())) {
                    handleAssignResult(assignResultDTO, desktop, userId);
                } else {
                    assignResultDTO.setDesktopId(desktop.getDeskId());
                }
                LOGGER.info("用户[{}], 桌面池[{}]分配结果为：{}", userId, desktopPoolId, JSON.toJSONString(assignResultDTO));
                return;
            }

            // 获取池中所有桌面
            List<PoolDesktopInfoDTO> poolDeskInfoList = desktopPoolService.listNormalDeskInfoByDesktopPoolId(desktopPool.getId());
            if (CollectionUtils.isEmpty(poolDeskInfoList)) {
                // 无虚机可以分配
                assignResultDTO.setCode(DesktopPoolConstants.NO_AVAILABLE_DESKTOP);
                LOGGER.error("池[{}]无虚机可以分配给用户[{}], 云桌面分配响应为：{}", desktopPool.getId(), userId, JSON.toJSONString(assignResultDTO));
                return;
            }

            // 过滤出未绑定用户且策略一致且未绑定磁盘的桌面，再优先分配开机的，其次是休眠和关机，其他状态不可分配，直接返回无资源
            poolDeskInfoList = desktopPoolAPIHelper.filterPoolDeskNotUserAndAccordWithList(desktopPoolId, poolDeskInfoList);

            if (CollectionUtils.isEmpty(poolDeskInfoList)) {
                // 无虚机可以分配
                assignResultDTO.setCode(DesktopPoolConstants.NO_AVAILABLE_DESKTOP);
                LOGGER.error("池[{}]无虚机可以分配给用户[{}], 云桌面分配响应为：{}", desktopPoolId, userId, JSON.toJSONString(assignResultDTO));
                return;
            }

            // 从池中分配开机的云桌面
            PoolDesktopInfoDTO runningDeskInfo = getRunningDeskInfo(poolDeskInfoList);
            if (Objects.nonNull(runningDeskInfo)) {
                LOGGER.info("池[{}]为用户[{}]分配运行中状态虚机, desktopInfo:{}", desktopPoolId, userId, JSON.toJSONString(runningDeskInfo));
                handleAssignResult(assignResultDTO, runningDeskInfo, userId);
                return;
            }

            // 从池中分配可用状态的云桌面：运行中>休眠>关机。
            PoolDesktopInfoDTO closeDeskInfo = getAvailableDeskInfo(poolDeskInfoList);
            if (Objects.nonNull(closeDeskInfo)) {
                LOGGER.info("池[{}]为用户[{}]分配关机状态虚机, desktopInfo:{}", desktopPoolId, userId, JSON.toJSONString(closeDeskInfo));
                handleAssignResult(assignResultDTO, closeDeskInfo, userId);
                return;
            }
            // 只有关机，运行中，休眠的可以返回正常，其他报错，稍后重试
            assignResultDTO.setCode(DesktopPoolConstants.NO_AVAILABLE_DESKTOP);
            LOGGER.error("池[{}]无正常可用的虚机可以分配给用户[{}], 云桌面分配响应为：{}", desktopPoolId, userId, JSON.toJSONString(assignResultDTO));
        }
    }

    private void dealMultipleSessionDesktop(DesktopPoolAssignResultDTO assignResultDTO, CbbDesktopPoolDTO desktopPool, UUID userId) {
        // 找有会话记录的
        checkUserHadDeskSession(assignResultDTO, desktopPool, userId);
        if (Objects.nonNull(assignResultDTO.getDesktopId()) || !Objects.equals(assignResultDTO.getCode(), SUCCEED)) {
            return;
        }

        // 先检查绑定的桌面是否可用
        checkUserHadBindMultiDesk(assignResultDTO, desktopPool, userId);
        if (Objects.nonNull(assignResultDTO.getDesktopId()) || !Objects.equals(assignResultDTO.getCode(), SUCCEED)) {
            return;
        }

        List<PoolDesktopInfoDTO> poolDeskInfoList = desktopPoolService.listNormalDeskInfoByDesktopPoolId(desktopPool.getId());
        if (CollectionUtils.isEmpty(poolDeskInfoList)) {
            // 无虚机可以分配
            assignResultDTO.setCode(DesktopPoolConstants.NO_AVAILABLE_DESKTOP);
            LOGGER.error("池[{}]无可用状态虚机可以分配给用户[{}], 云桌面分配响应为：{}", desktopPool.getId(), userId, JSON.toJSONString(assignResultDTO));
            return;
        }
        poolDeskInfoList = poolDeskInfoList.stream().filter(item -> BooleanUtils.isFalse(item.getIsOpenDeskMaintenance()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(poolDeskInfoList)) {
            // 无虚机可以分配
            assignResultDTO.setCode(DesktopPoolConstants.NO_AVAILABLE_DESKTOP);
            LOGGER.error("池[{}]过滤掉进入维护模式的桌面后，无虚机，用户[{}], 响应为：{}", desktopPool.getId(), userId, JSON.toJSONString(assignResultDTO));
            return;
        }

        // 过滤出running状态的主机
        List<PoolDesktopInfoDTO> canDeskList = poolDeskInfoList.stream().filter(desk -> desk.getDeskState() == CbbCloudDeskState.RUNNING)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(canDeskList)) {
            // 分配非运行状态主机
            LOGGER.info("用户[{}]请求桌面池[{}]无运行中状态主机，开始分配非运行中桌面", userId, desktopPool.getId());
            assignOffLineMultiDeskToUser(assignResultDTO, desktopPool, userId, poolDeskInfoList);
            return;
        }
        // 根据负载均衡策略筛选出合适的主机
        Map<UUID, Long> deskSessionUsageMap = getDeskSessionUsageMap(canDeskList.stream().map(PoolDesktopInfoDTO::getDeskId)
                .collect(Collectors.toList()));
        List<FilterPoolDesktopInfoDTO> filterPoolDesktopInfoDTOList = new ArrayList<>();
        for (PoolDesktopInfoDTO desktopInfo : canDeskList) {
            if (Boolean.FALSE.equals(desktopInfo.getIsWindowsOsActive()) && Boolean.FALSE.equals(desktopInfo.getOsActiveBySystem())) {
                // 授权过期过滤掉
                LOGGER.info("用户[{}]请求桌面池[{}]，主机[{}]授权过期[{}-{}]", userId, desktopPool.getId(), desktopInfo.getDeskId(),
                        desktopInfo.getIsWindowsOsActive(), desktopInfo.getOsActiveBySystem());
                continue;
            }
            if (checkIsDeskOverLoadLimit(desktopPool, desktopInfo.getDeskId(), desktopInfo, deskSessionUsageMap)) {
                continue;
            }
            FilterPoolDesktopInfoDTO filterPoolDesktopInfoDTO = buildAvailableResourceData(desktopPool, desktopInfo, deskSessionUsageMap);
            if (Objects.nonNull(filterPoolDesktopInfoDTO)) {
                filterPoolDesktopInfoDTOList.add(filterPoolDesktopInfoDTO);
            }
        }
        if (CollectionUtils.isNotEmpty(filterPoolDesktopInfoDTOList)) {
            UUID deskId = getBestDeskId(desktopPool, filterPoolDesktopInfoDTOList);
            Optional<PoolDesktopInfoDTO> desktopOptional = canDeskList.stream().filter(item -> Objects.equals(item.getDeskId(), deskId)).findFirst();
            if (desktopOptional.isPresent()) {
                handleHostUserAssignResult(assignResultDTO, desktopOptional.get(), userId);
                return;
            }
            LOGGER.info("用户[{}]请求桌面池[{}]无running状态负载合适的主机", userId, desktopPool.getId());
        }
        // 分配非运行状态主机
        assignOffLineMultiDeskToUser(assignResultDTO, desktopPool, userId, poolDeskInfoList);
    }

    private void checkUserHadDeskSession(DesktopPoolAssignResultDTO assignResultDTO, CbbDesktopPoolDTO desktopPool, UUID userId) {
        // 找有会话记录的
        List<UUID> sessionDeskIdList = desktopUserSessionDAO.findDeskIdByUserIdAndDesktopPoolId(userId, desktopPool.getId());
        sessionDeskIdList = sessionDeskIdList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sessionDeskIdList)) {
            return;
        }
        UUID deskId = sessionDeskIdList.get(0);
        // 判断第三方是否离线,删除记录关系后,进行重新分配
        if (checkThirdOffLineAndUnbindUser(desktopPool, deskId, userId)) {
            return;
        }
        LOGGER.info("用户[{}]分配桌面池[{}]有会话记录，返回桌面[{}]", userId, desktopPool.getId(), deskId);
        assignResultDTO.setDesktopId(deskId);
    }

    /**
     * 检查第三方桌面是否离线,删除用户关系记录
     *
     * @param desktopPool 池桌面
     * @param deskId      桌面ID
     * @param userId      用户ID
     * @return true:离线,需要重新分配 false:在线
     */
    private boolean checkThirdOffLineAndUnbindUser(CbbDesktopPoolDTO desktopPool, UUID deskId, UUID userId) {
        if (CbbDesktopPoolType.THIRD == desktopPool.getPoolType() && (CbbDesktopPoolModel.DYNAMIC == desktopPool.getPoolModel())) {
            try {
                CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
                if (CbbCloudDeskState.OFF_LINE == deskDTO.getDeskState()) {
                    LOGGER.info("用户[{}]关联的第三方动态池桌面[{}]已经离线,删除会话记录和绑定关系后,重新分配", userId, deskId);
                    userDesktopService.unbindUserAndDesktopRelation(deskId, userId);
                    return true;
                }
            } catch (BusinessException e) {
                LOGGER.info("用户[{}]关联的第三方动态池桌面[{}]已经离线,删除会话记录和绑定关系异常:", userId, deskId, e);
            }
        }
        return false;
    }

    private void checkUserHadBindMultiDesk(DesktopPoolAssignResultDTO assignResultDTO, CbbDesktopPoolDTO desktopPool, UUID userId) {
        List<UUID> deskIdList = hostUserService.findNormalDesktopIdByUserIdAndDesktopPoolId(userId, desktopPool.getId());
        deskIdList = deskIdList.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deskIdList)) {
            return;
        }
        // 静态要校验是否超出负载，超载就抛出失败
        UUID desktopId = deskIdList.get(0);
        PoolDesktopInfoDTO desktopInfo = desktopPoolService.getPoolDeskInfoByDeskId(desktopId);
        if (Objects.isNull(desktopInfo) || BooleanUtils.isTrue(desktopInfo.getIsDelete())) {
            LOGGER.warn("用户[{}]之前关联的桌面[{}]不存在或者在回收站中", userId, desktopId);
            return;
        }
        assignResultDTO.setDesktopId(desktopId);
    }

    private void assignOffLineMultiDeskToUser(DesktopPoolAssignResultDTO assignResultDTO, CbbDesktopPoolDTO desktopPool, UUID userId,
                                              List<PoolDesktopInfoDTO> poolDeskInfoList) {
        LOGGER.info("用户[{}]请求桌面池[{}]，开始分配非运行状态主机给用户", userId, desktopPool.getId());
        // 不存在查询离线空闲桌面主机
        List<PoolDesktopInfoDTO> offLineDeskList = poolDeskInfoList.stream().filter(s -> s.getDeskState() != CbbCloudDeskState.RUNNING)
                .collect(Collectors.toList());
        // 从池中分配可用状态的云桌面：运行中>休眠->>关机。
        PoolDesktopInfoDTO closeDeskInfo = getAvailableDeskInfo(offLineDeskList);
        if (closeDeskInfo == null) {
            // 无虚机可以分配
            assignResultDTO.setCode(DesktopPoolConstants.NO_AVAILABLE_DESKTOP);
            LOGGER.error("池[{}]无虚机可以分配给用户[{}], 云桌面分配响应为：{}", desktopPool.getId(), userId, JSON.toJSONString(assignResultDTO));
            return;
        }
        handleHostUserAssignResult(assignResultDTO, closeDeskInfo, userId);
    }

    private boolean checkIsDeskOverLoadLimit(CbbDesktopPoolDTO desktopPool, UUID desktopId, PoolDesktopInfoDTO desktopInfo,
                                             Map<UUID, Long> deskSessionUsageMap) {
        // 检查cpu、内存、系统盘负载情况
        DeskAgentLoadInfoDTO deskLoadInfo = DeskAgentLoadInfoCacheManager.getCacheIfPresentByHostId(desktopId);
        if (Objects.isNull(deskLoadInfo)) {
            LOGGER.info("上报系统使用信息为空（即主机未上传），默认认为桌面池[{}]中的主机[{}]不可分配", desktopPool.getId(), desktopId);
            return true;
        }
        if (Objects.isNull(deskLoadInfo.getCpuUsage()) || deskLoadInfo.getCpuUsage() > desktopPool.getCpuUsage()) {
            LOGGER.info("桌面池[{}]中的主机[{}]cpu使用率超出上限，总数[{}], 已使用[{}], 设置上限[{}]", desktopPool.getId(), desktopId,
                    desktopInfo.getCpu(), deskLoadInfo.getCpuUsage(), desktopPool.getCpuUsage());
            return true;
        }
        if (isLimitOver(deskLoadInfo.getMemoryUsage(), desktopInfo.getMemory(), desktopPool.getMemoryUsage())) {
            LOGGER.info("桌面池[{}]中的主机[{}]memory使用率超出上限，总数[{}], 已使用[{}], 设置上限[{}]", desktopPool.getId(), desktopId,
                    desktopInfo.getMemory(), deskLoadInfo.getMemoryUsage(), desktopPool.getMemoryUsage());
            return true;
        }
        if (isLimitOver(deskLoadInfo.getSystemDiskUsage(), desktopInfo.getSystemSize(), desktopPool.getSystemDiskUsage())) {
            LOGGER.info("桌面池[{}]中的主机[{}]systemDisk使用率超出上限，总数[{}], 已使用[{}], 设置上限[{}]", desktopPool.getId(), desktopId,
                    desktopInfo.getSystemSize(), deskLoadInfo.getSystemDiskUsage(), desktopPool.getSystemDiskUsage());
            return true;
        }
        // 检查会话数是否符合要求
        if (desktopPool.getMaxSession() > 0 && deskSessionUsageMap.getOrDefault(desktopId, 0L) >= desktopPool.getMaxSession()) {
            LOGGER.info("桌面池[{}]中的主机[{}]会话数超出上限，已使用[{}], 设置上限[{}]", desktopPool.getId(), desktopId,
                    deskSessionUsageMap.getOrDefault(desktopId, 0L), desktopPool.getMaxSession());
            return true;
        }
        return false;
    }

    private boolean isLimitOver(Integer usage, Integer all, Integer limit) {
        if (Objects.isNull(usage) || Objects.isNull(all) || Objects.isNull(limit)) {
            return true;
        }
        return (usage * 1.0F / all) * 1000 > limit * 10;
    }

    private Map<UUID, Long> getDeskSessionUsageMap(List<UUID> deskIdList) {
        List<DesktopUserSessionEntity> userSessionList = desktopUserSessionDAO.findAllByDeskIdIn(deskIdList);
        if (CollectionUtils.isEmpty(userSessionList)) {
            return new HashMap<>();
        }
        return userSessionList.stream().collect(Collectors.groupingBy(DesktopUserSessionEntity::getDeskId, Collectors.counting()));
    }

    private FilterPoolDesktopInfoDTO buildAvailableResourceData(CbbDesktopPoolDTO desktopPool, PoolDesktopInfoDTO desktopInfo,
                                                                Map<UUID, Long> deskSessionUsageMap) {
        DeskAgentLoadInfoDTO deskLoadInfo = DeskAgentLoadInfoCacheManager.getCacheIfPresentByHostId(desktopInfo.getDeskId());
        if (deskLoadInfo == null) {
            LOGGER.info("上报系统使用信息为空（即主机未上传），桌面池[{}]的主机[{}]不可分配", desktopPool.getId(), desktopInfo.getDeskId());
            // null
            return null;
        }
        FilterPoolDesktopInfoDTO filterPoolDesktopInfo = new FilterPoolDesktopInfoDTO();
        filterPoolDesktopInfo.setDeskId(desktopInfo.getDeskId());
        filterPoolDesktopInfo.setSessionUsage(deskSessionUsageMap.getOrDefault(desktopInfo.getDeskId(), 0L).intValue());
        if (desktopPool.getMaxSession() > 0) {
            filterPoolDesktopInfo.setSessionAvailable(desktopPool.getMaxSession() - filterPoolDesktopInfo.getSessionUsage());
        } else {
            // 0代表不限
            filterPoolDesktopInfo.setSessionAvailable(999999999 - filterPoolDesktopInfo.getSessionUsage());
        }

        filterPoolDesktopInfo.setCpuAvailable(HUNDRED - deskLoadInfo.getCpuUsage());
        filterPoolDesktopInfo.setMemoryAvailable(desktopInfo.getMemory() - deskLoadInfo.getMemoryUsage());
        filterPoolDesktopInfo.setSystemDiskAvailable(desktopInfo.getSystemSize() - deskLoadInfo.getSystemDiskUsage());
        return filterPoolDesktopInfo;
    }


    private static UUID getBestDeskId(CbbDesktopPoolDTO desktopPool, List<FilterPoolDesktopInfoDTO> filterPoolDesktopInfoDTOList) {
        List<FilterPoolDesktopInfoDTO> resultList;
        UUID deskId = null;
        switch (desktopPool.getLoadBalanceStrategy()) {
            case SESSION_PRIORITY:
                resultList = filterPoolDesktopInfoDTOList.stream().
                        sorted(Comparator.comparing(FilterPoolDesktopInfoDTO::getSessionAvailable).reversed()).collect(Collectors.toList());
                deskId = resultList.get(0).getDeskId();
                break;
            case CPU_PRIORITY:
                resultList = filterPoolDesktopInfoDTOList.stream().
                        sorted(Comparator.comparing(FilterPoolDesktopInfoDTO::getCpuAvailable).reversed()).collect(Collectors.toList());
                deskId = resultList.get(0).getDeskId();
                break;
            case MEMORY_PRIORITY:
                resultList = filterPoolDesktopInfoDTOList.stream().
                        sorted(Comparator.comparing(FilterPoolDesktopInfoDTO::getMemoryAvailable).reversed()).collect(Collectors.toList());
                deskId = resultList.get(0).getDeskId();
                break;
            case SYSTEM_DISK_PRIORITY:
                resultList = filterPoolDesktopInfoDTOList.stream().
                        sorted(Comparator.comparing(FilterPoolDesktopInfoDTO::getSystemDiskAvailable).reversed()).collect(Collectors.toList());
                deskId = resultList.get(0).getDeskId();
                break;
        }
        return deskId;
    }

    /**
     * 获取用户已绑定的桌面，如果没有优先返回用户个人盘绑定的桌面
     *
     * @param userId userId
     * @param poolId poolId
     * @return PoolDesktopInfoDTO 未分配返回空
     */
    private PoolDesktopInfoDTO getBindingDesktop(UUID userId, UUID poolId) {
        return desktopPoolService.getUserBindPoolDesktop(userId, poolId);
    }

    /**
     * 获取用户已绑定个人磁盘的桌面
     *
     * @param userId           用户ID
     * @param poolDeskInfoList 池桌面集合
     * @return 桌面
     */
    private PoolDesktopInfoDTO getUserDiskBindingDesktop(UUID userId, List<PoolDesktopInfoDTO> poolDeskInfoList) {
        AtomicReference<PoolDesktopInfoDTO> atomicReference = new AtomicReference<>();
        userDiskMgmtAPI.diskDetailByUserId(userId)
                .filter(Objects::nonNull)
                .filter(userDiskDetailDTO -> Objects.nonNull(userDiskDetailDTO.getDeskId()))
                .filter(userDiskDetailDTO -> userDiskDetailDTO.getDiskState() != DiskStatus.ERROR)
                .ifPresent(userDiskDetailDTO -> atomicReference.set(
                        poolDeskInfoList.stream()
                                .filter(desktopInfoDTO -> Objects.equals(desktopInfoDTO.getDeskId(), userDiskDetailDTO.getDeskId()))
                                .findFirst()
                                .orElse(null)
                ));
        return atomicReference.get();
    }

    private PoolDesktopInfoDTO getRunningDeskInfo(List<PoolDesktopInfoDTO> poolDeskInfoList) {
        List<PoolDesktopInfoDTO> deskList =
                poolDeskInfoList.stream().filter(desktopDTO -> CbbCloudDeskState.RUNNING == desktopDTO.getDeskState()).collect(Collectors.toList());
        return sortDesktopList(deskList);
    }

    private PoolDesktopInfoDTO getAvailableDeskInfo(List<PoolDesktopInfoDTO> poolDesktopList) {
        List<PoolDesktopInfoDTO> sleepDesktopList = poolDesktopList.stream()
                .filter(desktopDTO -> CbbCloudDeskState.SLEEP == desktopDTO.getDeskState()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(sleepDesktopList)) {
            return sortDesktopList(sleepDesktopList);
        }
        List<PoolDesktopInfoDTO> deskList = poolDesktopList.stream()
                .filter(desktopDTO -> DESKTOP_READY_STATES_SET.contains(desktopDTO.getDeskState())).collect(Collectors.toList());
        return sortDesktopList(deskList);
    }

    private PoolDesktopInfoDTO sortDesktopList(List<PoolDesktopInfoDTO> deskList) {
        // 打乱随机返回
        if (CollectionUtils.isEmpty(deskList)) {
            // 返回空
            return null;
        }
        Collections.shuffle(deskList);
        return deskList.get(0);
    }

    private void handleAssignResult(DesktopPoolAssignResultDTO resultDTO, PoolDesktopInfoDTO desktopInfoDTO, UUID userId) throws BusinessException {
        // 绑定云桌面，修改云桌面名称，添加绑定关系
        UUID desktopId = desktopInfoDTO.getDeskId();
        // 绑定云桌面
        desktopPoolUserService.poolDesktopBindUser(desktopInfoDTO, userId);
        resultDTO.setDesktopId(desktopId);
    }

    private void handleHostUserAssignResult(DesktopPoolAssignResultDTO resultDTO, PoolDesktopInfoDTO desktopInfoDTO, UUID userId) {
        // 绑定云桌面，修改云桌面名称，添加绑定关系
        UUID desktopId = desktopInfoDTO.getDeskId();
        // 绑定云桌面
        hostUserService.poolDesktopBindUser(desktopInfoDTO, userId);
        resultDTO.setDesktopId(desktopId);
    }

    private DesktopPoolAssignResultDTO afterAssignSuccess(UUID userId, DesktopPoolAssignResultDTO desktopResultDTO, CbbDesktopPoolDTO desktopPool)
            throws BusinessException {
        if (desktopPool.getSessionType() == CbbDesktopSessionType.SINGLE) {
            return afterAssignSingleSuccess(userId, desktopResultDTO, desktopPool);
        }
        return afterAssignMultiSuccess(userId, desktopResultDTO, desktopPool);
    }

    private DesktopPoolAssignResultDTO afterAssignSingleSuccess(UUID userId, DesktopPoolAssignResultDTO desktopResultDTO,
                                                                CbbDesktopPoolDTO desktopPool) throws BusinessException {
        UUID desktopId = desktopResultDTO.getDesktopId();
        // 单会话，分配成功且是动态池时，进行磁盘挂载操作
        // 桌面锁定磁盘
        DesktopLockDiskResultDTO lockDiskResult = userDiskMgmtAPI.desktopLockDisk(desktopId);
        if (desktopPool.getPoolModel() == CbbDesktopPoolModel.DYNAMIC && !Objects.equals(lockDiskResult.getCode(), SUCCEED)
                && userDiskMgmtAPI.checkDesktopUserProFile(desktopId)) {
            // 动态池桌面开启了个人盘的个性化配置策略，如果无个人盘就不允许进桌面
            desktopResultDTO.setCode(DesktopPoolConstants.DESKTOP_USER_DISK_STATUS_ERROR);
            desktopResultDTO.setMessage(lockDiskResult.getMessage());
            LOGGER.warn("用户[{}]个人盘锁定失败无法启动虚拟机资源，分配资源[{}]进行回滚", userId, desktopId);
            userDesktopService.unbindUserAndDesktopRelation(desktopId);
            return desktopResultDTO;
        }

        if (desktopPool.getPoolModel() == CbbDesktopPoolModel.STATIC) {
            // 将AD安全组的用户加到desktopPoolUser表里
            saveAdGroupUserToPool(desktopPool.getId(), userId);
        }

        ViewUserDesktopEntity viewUserDesktopEntity = queryCloudDesktopService.checkDesktopExistInDeskViewById(desktopId);
        if (CbbCloudDeskState.valueOf(viewUserDesktopEntity.getDeskState()) == CbbCloudDeskState.RUNNING) {
            // 用户登录预启动的池桌面时,向GT发送用户信息
            desktopPoolAPIHelper.asyncNotifyGuestToolUserInfo(viewUserDesktopEntity, userId);
            // 用户登录预启动的池桌面时,向GT水印信息
            CloudDesktopDetailDTO cloudDesktopDetailDTO = queryCloudDesktopService.queryDeskDetail(desktopId);
            deskStrategyAPI.sendDesktopStrategyWatermark(cloudDesktopDetailDTO);
        }
        // 动态挂载磁盘
        userDiskMgmtAPI.attachDesktopDisk(desktopId);
        // 分配成功，清空回收倒计时
        userDesktopService.clearConnectClosedTime(desktopId);
        return desktopResultDTO;
    }

    private DesktopPoolAssignResultDTO afterAssignMultiSuccess(UUID userId, DesktopPoolAssignResultDTO desktopResultDTO,
                                                                CbbDesktopPoolDTO desktopPool) {
        if (desktopPool.getPoolModel() == CbbDesktopPoolModel.STATIC) {
            // 将AD安全组的用户加到desktopPoolUser表里
            saveAdGroupUserToPool(desktopPool.getId(), userId);
        }
        return desktopResultDTO;
    }

    private void saveAdGroupUserToPool(UUID desktopPoolId, UUID userId) {
        // 判断用户组是否已加入，再判断是否是因为AD安全组加入了才有这个池
        try {
            desktopPoolUserService.checkAndSaveAdGroupUser(desktopPoolId, userId);
        } catch (Exception e) {
            LOGGER.error("判断用户[{}]是否已加入，再判断是否是因为AD安全组加入了才有这个池[{}]过程异常", userId, desktopPoolId, e);
        }
    }

    @Override
    public boolean checkUserInDesktopPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");

        return desktopPoolUserService.checkUserInDesktopPool(desktopPoolId, userId);
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageQueryRealBindUser(UUID desktopPoolId, PageSearchRequest request) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(request, "request can not be null");
        // 添加默认的用户类型条件
        Page<RcoViewUserEntity> userPage = queryUserListService.pageUserInOrNotInDesktopPool(desktopPoolId, request, true);
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();
        response.setTotal(userPage.getTotalElements());

        if (CollectionUtils.isEmpty(userPage.getContent())) {
            response.setItemArr(new UserListDTO[0]);
            return response;
        }
        UserListDTO[] dtoArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userPage.getContent());

        // 赋值hasBindPoolDesktop
        DesktopPoolBasicDTO poolBasicDTO = null;
        try {
            poolBasicDTO = desktopPoolService.getDesktopPoolBasicById(desktopPoolId);
        } catch (BusinessException e) {
            LOGGER.error("获取桌面池[{}]异常", desktopPoolId, e);
        }
        Set<UUID> userIdSet;
        if (poolBasicDTO != null && poolBasicDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            List<UUID> userIdList = hostUserService.findUserIdListByDesktopPoolId(desktopPoolId);
            userIdSet = new HashSet<>(userIdList);
        } else {
            List<PoolDesktopInfoDTO> desktopList = desktopPoolService.listDesktopByDesktopPoolIds(Lists.newArrayList(desktopPoolId));
            userIdSet = desktopList.stream().filter(item -> Objects.nonNull(item.getUserId()) &&
                    item.getDeskState() != CbbCloudDeskState.RECYCLE_BIN).map(PoolDesktopInfoDTO::getUserId).collect(Collectors.toSet());
        }
        for (UserListDTO user : dtoArr) {
            user.setHasBindPoolDesktop(userIdSet.contains(user.getId()));
        }
        response.setItemArr(dtoArr);
        return response;
    }

    @Override
    public Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId) {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolUserService.countAssignedUserNumByGroup(groupId, desktopPoolId);
    }

    @Override
    public List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        return desktopPoolUserService.countAssignedUserNumInGroupByDesktopPoolId(desktopPoolId);
    }

    @Override
    public void updatePoolBindObject(UpdatePoolBindObjectDTO bindObjectDTO) throws BusinessException {
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");
        Assert.notNull(bindObjectDTO.getPoolId(), "desktopPoolId can not be null");
        String name = bindObjectDTO.getPoolId().toString();
        try {
            CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(bindObjectDTO.getPoolId());
            name = desktopPoolDTO.getName();
            LockableExecutor.executeWithTryLock(DESKTOP_POOL_UPDATE_BIND_LOCK + desktopPoolDTO.getId().toString(), () -> {
                List<DesktopPoolUserDTO> oldBindGroupList = desktopPoolUserService.listDesktopPoolUser(desktopPoolDTO.getId(),
                        IacConfigRelatedType.USERGROUP);
                DesktopPoolBindGroupDTO poolBindGroupDTO = new DesktopPoolBindGroupDTO();
                poolBindGroupDTO.setAddGroupIdList(getNeedAddGroupIdList(bindObjectDTO.getSelectedGroupIdList(), oldBindGroupList));
                poolBindGroupDTO.setDeleteGroupIdList(getNeedDeleteGroupIdList(bindObjectDTO.getSelectedGroupIdList(), oldBindGroupList));
                // 池原有绑定的安全组
                List<DesktopPoolUserDTO> oldBindAdGroupList = desktopPoolUserService.listDesktopPoolUser(desktopPoolDTO.getId(),
                        IacConfigRelatedType.AD_GROUP);
                poolBindGroupDTO.setAddAdGroupIdList(getNeedAddGroupIdList(bindObjectDTO.getSelectedAdGroupIdList(), oldBindAdGroupList));
                poolBindGroupDTO.setDeleteAdGroupIdList(getNeedDeleteGroupIdList(bindObjectDTO.getSelectedAdGroupIdList(), oldBindAdGroupList));

                // 处理exceptList，取出组下所有用户ID，再根据exceptUserIdList删除对应用户，再全部加入到addUserByIdList中
                // 静态池如果删除用户组，组下已绑定桌面的用户要加回来
                mergeAddUserIdList(bindObjectDTO, desktopPoolDTO, poolBindGroupDTO.getDeleteGroupIdList());
                mergeDeleteUserIdList(bindObjectDTO, desktopPoolDTO);

                // 过滤掉删除用户和添加用户冲突的数据，新增的用户过滤掉已保存的userId
                filterSavedRelatedObj(bindObjectDTO, desktopPoolDTO.getId());

                // 检查用户ID和组ID是否真是存在
                desktopPoolAPIHelper.checkUserAndGroupExist(bindObjectDTO);
                // 检查安全组是否真是存在
                desktopPoolAPIHelper.checkAdGroupExist(bindObjectDTO);
                desktopPoolServiceTx.updatePoolBindObject(bindObjectDTO, poolBindGroupDTO);

                // 推送用户数据到rccm
                desktopPoolAPIHelper.pushUserToRccmWhenUpdateBindUser(bindObjectDTO);

                // 保存审计日志
                desktopPoolAPIHelper.saveUpdateBindObjLog(desktopPoolDTO, poolBindGroupDTO, bindObjectDTO);
            }, 1);
        } catch (BusinessException e) {
            LOGGER.error("修改桌面池[{}]绑定对像关联关系出现异常，参数[{}]", name, JSON.toJSONString(bindObjectDTO), e);
            if (Objects.equals(e.getKey(), RESOURCE_LOCKED_ERROR_KEY)) {
                // 资源锁住的错误
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_TASK_REPEAT, e, name);
            }
            throw e;
        } catch (Exception e) {
            LOGGER.error("修改桌面池[{}]绑定对像关联关系出现异常，参数[{}]", name, JSON.toJSONString(bindObjectDTO), e);
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UPDATE_BIND_ERROR, e, name);
        }
    }

    private List<UUID> getNeedAddGroupIdList(List<UUID> updateGroupIdList, List<DesktopPoolUserDTO> oldBindGroupList) {
        Set<UUID> oldGroupIdSet = oldBindGroupList.stream().map(DesktopPoolUserDTO::getRelatedId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(updateGroupIdList)) {
            return Collections.emptyList();
        }
        return updateGroupIdList.stream().filter(item -> !oldGroupIdSet.contains(item)).distinct().collect(Collectors.toList());
    }

    private List<UUID> getNeedDeleteGroupIdList(List<UUID> updateGroupIdList, List<DesktopPoolUserDTO> oldBindGroupList) {
        if (CollectionUtils.isEmpty(updateGroupIdList)) {
            return oldBindGroupList.stream().map(DesktopPoolUserDTO::getRelatedId).collect(Collectors.toList());
        }
        Set<UUID> updateGroupIdSet = new HashSet<>(updateGroupIdList);
        return oldBindGroupList.stream().map(DesktopPoolUserDTO::getRelatedId)
                .filter(relatedId -> !updateGroupIdSet.contains(relatedId)).collect(Collectors.toList());
    }

    private void mergeAddUserIdList(UpdatePoolBindObjectDTO bindObjectDTO, CbbDesktopPoolDTO desktopPoolDTO, List<UUID> deleteGroupIdList)
            throws BusinessException {
        List<UUID> groupIdList = bindObjectDTO.getAllExceptGroupIdList();
        List<UUID> userIdOfExceptGroupList = getUserIdByGroupIds(groupIdList);
        bindObjectDTO.mergeAllAddUserIdList(userIdOfExceptGroupList);
        // 静态池如果删除用户组，组下已绑定桌面的用户要加回来
        mergeStaticPoolBindDesktopUser(bindObjectDTO, desktopPoolDTO, deleteGroupIdList);
    }

    private List<UUID> getUserIdByGroupIds(List<UUID> groupIdList) throws BusinessException {
        List<UUID> userIdList = new ArrayList<>();
        if (CollectionUtils.isEmpty(groupIdList)) {
            return userIdList;
        }
        // 组下用户可能会超过1000，按组分批查询
        for (UUID userGroupId : groupIdList) {
            IacPageResponse<IacUserDetailDTO> pageResult = userService.pageQueryByGroupId(userGroupId, 0);
            // 总页数
            int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
            for (int page = 0; page < totalPage; page++) {
                // 前面已查过，不重复查
                if (page == 0) {
                    userIdList.addAll(extractUserId(pageResult.getItemArr()));
                    continue;
                }
                pageResult = userService.pageQueryByGroupId(userGroupId, page);
                if (pageResult.getTotal() == 0) {
                    break;
                }
                userIdList.addAll(extractUserId(pageResult.getItemArr()));
            }
        }
        return userIdList;
    }

    private List<UUID> extractUserId(IacUserDetailDTO[] userDetailDTOArr) {
        List<UUID> idList = Arrays.stream(userDetailDTOArr)//
            .filter(item -> item.getUserType() != IacUserTypeEnum.VISITOR)//
            .map(IacUserDetailDTO::getId)//
            .collect(Collectors.toList());
        return idList;
    }

    private void mergeStaticPoolBindDesktopUser(UpdatePoolBindObjectDTO bindObjectDTO, CbbDesktopPoolDTO desktopPoolDTO,
                                                List<UUID> deleteGroupIdList) {
        if (desktopPoolDTO.getPoolModel() != CbbDesktopPoolModel.STATIC || CollectionUtils.isEmpty(deleteGroupIdList)) {
            return;
        }
        // 静态池如果删除用户组，组下已绑定桌面的用户要加回来
        List<PoolDesktopInfoDTO> desktopList = desktopPoolService.listNormalDesktopByPoolIdAndUserGroupIds(desktopPoolDTO.getId(),
                deleteGroupIdList);
        if (CollectionUtils.isEmpty(desktopList)) {
            return;
        }
        List<UUID> userIdList = desktopList.stream().map(PoolDesktopInfoDTO::getUserId).filter(Objects::nonNull).collect(Collectors.toList());
        List<UUID> addUserByIdList = Optional.ofNullable(bindObjectDTO.getAddUserByIdList()).orElse(new ArrayList<>());
        addUserByIdList.addAll(userIdList);
        bindObjectDTO.setAddUserByIdList(addUserByIdList.stream().distinct().collect(Collectors.toList()));
    }

    private void filterSavedRelatedObj(UpdatePoolBindObjectDTO bindObjectDTO, UUID poolId) {
        // 去除要删除的userId和要新增的userId的冲突部分
        bindObjectDTO.refresh();

        if (CollectionUtils.isNotEmpty(bindObjectDTO.getAddUserByIdList())) {
            List<UUID> addUserIdList = bindObjectDTO.getAddUserByIdList();
            Set<UUID> finalExistIdSet = new HashSet<>();
            if (addUserIdList.size() <= DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM) {
                finalExistIdSet.addAll(desktopPoolUserService.listRelatedIdByPoolIdAndRelatedObj(poolId, IacConfigRelatedType.USER, addUserIdList));
            } else {
                List<List<UUID>> tempIdList = Lists.partition(addUserIdList, DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM);
                for (List<UUID> idList : tempIdList) {
                    finalExistIdSet.addAll(desktopPoolUserService.listRelatedIdByPoolIdAndRelatedObj(poolId, IacConfigRelatedType.USER, idList));
                }
            }
            if (CollectionUtils.isEmpty(finalExistIdSet)) {
                return;
            }
            bindObjectDTO.setAddUserByIdList(addUserIdList.stream().filter(id -> !finalExistIdSet.contains(id)).collect(Collectors.toList()));
        }

    }

    private void mergeDeleteUserIdList(UpdatePoolBindObjectDTO updateDTO, CbbDesktopPoolDTO desktopPoolDTO) {
        // 这是前端上传过来需要去根据用户组删除用户绑定记录的用户组列表
        // 如果用户组被选了，其底下的用户记录需要清掉，不需要再记录了，因为他的组已经绑定这个池了
        List<UUID> finalDelUserByGroupIdList = updateDTO.getSelectedGroupIdAndDeleteUserByGroupId();

        // 根据用户组去查询其下的用户ID列表
        List<UUID> needDeleteByGroupUserIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(finalDelUserByGroupIdList)) {
            needDeleteByGroupUserIdList = desktopPoolUserService.listBindUserIdByUserGroupIds(updateDTO.getPoolId(), finalDelUserByGroupIdList);
            needDeleteByGroupUserIdList = needDeleteByGroupUserIdList.stream().distinct().collect(Collectors.toList());
        }

        updateDTO.mergeDeleteUserIdList(needDeleteByGroupUserIdList);

        if (desktopPoolDTO.getPoolModel() == CbbDesktopPoolModel.STATIC && CollectionUtils.isNotEmpty(updateDTO.getDeleteUserByIdList())) {
            // 防止已绑定静态池桌面的用户被删除，如果该用户所属的组未分配不需要删除该用户
            List<PoolDesktopInfoDTO> desktopList = desktopPoolService.listBindUserDesktopByPoolId(desktopPoolDTO.getId());
            if (CollectionUtils.isEmpty(desktopList)) {
                return;
            }
            Set<UUID> selectedGroupIdSet = new HashSet<>();
            if (Objects.nonNull(updateDTO.getSelectedGroupIdList())) {
                selectedGroupIdSet.addAll(updateDTO.getSelectedGroupIdList());
            }
            // 用户组不在桌面池里的用户ID的Set
            Set<UUID> bindDeskUserIdSet = desktopList.stream().filter(desk -> !selectedGroupIdSet.contains(desk.getUserGroupId()))
                    .map(PoolDesktopInfoDTO::getUserId).collect(Collectors.toSet());
            // 有绑定桌面且用户组不在池内的用户不能删除
            updateDTO.setDeleteUserByIdList(updateDTO.getDeleteUserByIdList().stream().filter(id -> !bindDeskUserIdSet.contains(id))
                    .collect(Collectors.toList()));
        }
    }

    @Override
    public DefaultPageResponse<UserListDTO> pageUserWithAssignment(UserAssignmentPageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getDesktopPoolId(), "desktopPoolId is null");
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();

        // 因为交互要求已分配的优先排序在前面，所以需要后端分别查询已分配和未分配的分页数据进行合并再返回给前端
        UUID desktopPoolId = request.getDesktopPoolId();
        int startIndex = request.getLimit() * request.getPage() + 1;
        int endIndex = request.getLimit() * (1 + request.getPage());

        Page<RcoViewUserEntity> allPage = queryUserListService.pageQuery(request, RcoViewUserEntity.class);
        int total = (int) allPage.getTotalElements();

        response.setTotal(total);
        // 起始下标超过了总量直接返回空
        if (startIndex > total) {
            // 返回空的
            response.setTotal(total);
            response.setItemArr(new UserListDTO[0]);
            return response;
        }
        // 先查询已分配的用户列表
        Page<RcoViewUserEntity> pageUserIn = queryUserListService.pageUserInOrNotInDesktopPool(desktopPoolId, request, true);

        // 获取桌面池中已绑定桌面的用户列表
        Set<UUID> userIdSet = findBindDeskUserIdSet(desktopPoolId);

        int pageUserInTotal = (int) pageUserIn.getTotalElements();
        // 已分配用户的满足分页数量，不需要再查后续的数据了，直接返回结果
        if (pageUserInTotal >= endIndex) {
            response.setItemArr(convert2UserListDTOArr(pageUserIn.getContent(), userIdSet, true));
            return response;
        }

        // 以下为已分配数量不满足此次分页查询，需要再查询未分配的用户进行组装
        int notInLastPage = request.getPage() - pageUserInTotal / request.getLimit();
        request.setPage(notInLastPage);

        // 查询未分配的用户列表
        Page<RcoViewUserEntity> lastPageUserNotIn = queryUserListService.pageUserInOrNotInDesktopPool(desktopPoolId, request, false);
        // 未有用户被分配，或者已分配的刚好是整数页
        if (pageUserInTotal == 0 || pageUserInTotal % request.getLimit() == 0) {
            // pageUserNotIn入参page和limit需要重新根据pageUserIn的数量计算
            response.setItemArr(convert2UserListDTOArr(lastPageUserNotIn.getContent(), userIdSet, false));
            return response;
        }

        // 已分配的取一部分，未分配的取一部分
        if (startIndex <= pageUserInTotal) {
            response.setItemArr(getFromAssignedAndUnAssigned(request, pageUserIn, lastPageUserNotIn, userIdSet));
            return response;
        }

        // 只取非分配的组成分页数据
        response.setItemArr(getFromUnAssigned(request, lastPageUserNotIn, pageUserInTotal, userIdSet));
        return response;
    }

    private UserListDTO[] getFromAssignedAndUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> pageUserIn,
                                                       Page<RcoViewUserEntity> lastPageUserNotIn, Set<UUID> bindDesktopUserIdSet) {
        UserListDTO[] assignedUserArr = convert2UserListDTOArr(pageUserIn.getContent(), bindDesktopUserIdSet, true);
        // notIn部分不需要请求第二次
        int diffNum = request.getLimit() - pageUserIn.getContent().size();
        // 还差diffNum个用户凑成limit个
        if (lastPageUserNotIn.getContent().size() <= diffNum) {
            // lastPageUserNotIn + pageUserIn的数据不足一页，后面已经没数据了
            UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(lastPageUserNotIn.getContent(), bindDesktopUserIdSet, false);
            return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
        }

        // 把lastPageUserNotIn里的部分记录和pageUserIn里的记录合成一页
        List<RcoViewUserEntity> subNotInUserList = lastPageUserNotIn.getContent().subList(0, diffNum);
        UserListDTO[] unAssignedUserArr = convert2UserListDTOArr(subNotInUserList, bindDesktopUserIdSet, false);
        return UserQueryHelper.mergeUserListDTOArr(assignedUserArr, unAssignedUserArr);
    }

    private UserListDTO[] getFromUnAssigned(UserAssignmentPageRequest request, Page<RcoViewUserEntity> lastPageUserNotIn, int pageUserInTotal,
                                            Set<UUID> bindDesktopUserIdSet) {
        request.setPage(request.getPage() - 1);
        int diffNumByLimit = request.getLimit() - pageUserInTotal % request.getLimit();

        Page<RcoViewUserEntity> prePageUserNotIn = queryUserListService.pageUserInOrNotInDesktopPool(request.getDesktopPoolId(), request, false);
        List<RcoViewUserEntity> preUserList = prePageUserNotIn.getContent();

        // lastPageUserNotIn为空，prePageUserNotIn的数据已经是最后的记录了
        if (CollectionUtils.isEmpty(lastPageUserNotIn.getContent())) {
            // 取prePageUserNotIn中的部分
            List<RcoViewUserEntity> finalUserList = preUserList.subList(diffNumByLimit, preUserList.size());
            return convert2UserListDTOArr(finalUserList, bindDesktopUserIdSet, false);
        }

        // 取prePageUserNotIn中的部分和lastPageUserNotIn中的部分
        List<RcoViewUserEntity> finalUserList = Lists.newArrayList(preUserList.subList(diffNumByLimit, preUserList.size()));
        int lastPageEndIndex = Math.min(diffNumByLimit, lastPageUserNotIn.getContent().size());
        finalUserList.addAll(lastPageUserNotIn.getContent().subList(0, lastPageEndIndex));
        return convert2UserListDTOArr(finalUserList, bindDesktopUserIdSet, false);
    }

    private UserListDTO[] convert2UserListDTOArr(List<RcoViewUserEntity> userEntityList, Set<UUID> userIdSet, boolean isInPool) {
        UserListDTO[] userListDTOArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userEntityList);
        for (UserListDTO user : userListDTOArr) {
            user.setIsAssigned(isInPool);
            user.setHasBindPoolDesktop(userIdSet.contains(user.getId()));
        }
        return userListDTOArr;
    }

    private Set<UUID> findBindDeskUserIdSet(UUID desktopPoolId) {
        List<PoolDesktopInfoDTO> desktopList = desktopPoolService.listDesktopByDesktopPoolIds(Lists.newArrayList(desktopPoolId));
        Set<UUID> userIdSet = desktopList.stream().filter(item -> Objects.nonNull(item.getUserId()) &&
                item.getDeskState() != CbbCloudDeskState.RECYCLE_BIN).map(PoolDesktopInfoDTO::getUserId).collect(Collectors.toSet());
        if (CollectionUtils.isNotEmpty(desktopList) && desktopList.get(0).getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            userIdSet.addAll(hostUserService.findUserIdListByDesktopPoolId(desktopPoolId));
        }
        return userIdSet;
    }

    @Override
    public void saveAssignFailWarnLog(String userName, CbbDesktopPoolDTO desktopPoolDTO) throws BusinessException {
        Assert.notNull(userName, "userName can not be null");
        Assert.notNull(desktopPoolDTO, "desktopPoolDTO is null");

        String warnLog = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ASSIGN_FAIL, desktopPoolDTO.getName(), userName);
        SaveAlarmRequest request = new SaveAlarmRequest();
        request.setAlarmLevel(AlarmLevel.TIPS);
        request.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
        request.setAlarmNameByI18nKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_ASSIGN_FAIL_NAME);
        request.setAlarmContent(warnLog);
        request.setAlarmTime(new Date());
        request.setEnableSendMail(true);
        request.setAlarmCode(buildAlarmCode(request.getAlarmType(), desktopPoolDTO.getId().toString(), warnLog));
        alarmAPI.saveAlarm(request);
    }

    private String buildAlarmCode(String type, String id, String content) {
        // 告警返回信息中没有明确方式区分alarmCode,这里基于资源类型，id和告警内容的md5做alarmCode
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(id).append(content);
        byte[] byteArr = sb.toString().getBytes();
        byte[] md5ByteArr = new Md5Builder().update(byteArr, 0, byteArr.length).build();
        // 携带上resourceType，用以区分告警类型
        return type + "|" + StringUtils.bytes2Hex(md5ByteArr);
    }

    @Override
    public List<String> listDesktopPoolAllUserName() throws BusinessException {
        List<DesktopPoolUserDTO> desktopPoolUserDTOList = desktopPoolUserService.listDesktopPoolAllUser();
        if (CollectionUtils.isEmpty(desktopPoolUserDTOList)) {
            return Collections.emptyList();
        }

        // 过滤出绑定了池的组列表，并根据组ID查询获取组下的所有用户的用户名列表
        List<UUID> groupIdList = desktopPoolUserDTOList.stream()
                .filter(desktopPoolUserDTO -> IacConfigRelatedType.USERGROUP == desktopPoolUserDTO.getRelatedType())
                .map(DesktopPoolUserDTO::getRelatedId).distinct().collect(Collectors.toList());
        List<String> userNameList = getUserNameList(groupIdList);

        // 过滤出绑定了池的用户列表，并查询获取用户的用户名列表
        List<UUID> userIdList = desktopPoolUserDTOList.stream()
                .filter(desktopPoolUserDTO -> IacConfigRelatedType.USER == desktopPoolUserDTO.getRelatedType())
                .map(DesktopPoolUserDTO::getRelatedId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(userIdList)) {
            List<RcoViewUserEntity> userInfoByIdList = userService.getUserInfoByIdList(userIdList);
            userNameList.addAll(userInfoByIdList.stream().map(RcoViewUserEntity::getUserName).collect(Collectors.toList()));
        }
        return userNameList;
    }

    private List<String> getUserNameList(List<UUID> groupIdList) throws BusinessException {
        if (CollectionUtils.isEmpty(groupIdList)) {
            return Collections.emptyList();
        }
        List<String> userNameList = new ArrayList<>();
        // 组下用户可能会超过1000，按组分页查询
        for (UUID groupId : groupIdList) {
            IacPageResponse<IacUserDetailDTO> pageResult = userService.pageQueryByGroupId(groupId, 0);
            // 总页数
            int totalPage = (int) Math.ceil((double) pageResult.getTotal() / Constants.MAX_QUERY_LIST_SIZE);
            for (int page = 0; page < totalPage; page++) {
                // 前面已查过，不重复查
                if (page == 0) {
                    userNameList.addAll(Arrays.stream(pageResult.getItemArr()).map(IacUserDetailDTO::getUserName).collect(Collectors.toList()));
                    continue;
                }
                pageResult = userService.pageQueryByGroupId(groupId, page);
                if (pageResult.getTotal() == 0) {
                    break;
                }
                userNameList.addAll(Arrays.stream(pageResult.getItemArr()).map(IacUserDetailDTO::getUserName).collect(Collectors.toList()));
            }
        }

        return userNameList;
    }

    @Override
    public Set<String> getDesktopPoolRelationUserGroup(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "getDesktopPoolRelationUserGroup desktopPoolId can not be null");
        return desktopPoolUserService.getDesktopPoolRelationUserGroup(desktopPoolId);
    }

    @Override
    public int countByRelatedType(IacConfigRelatedType relatedType) {
        Assert.notNull(relatedType, "relatedType can not be null");
        return desktopPoolUserService.countByRelatedType(relatedType);
    }

    @Override
    public void moveDesktop(CloudDesktopDetailDTO desktopDetailDTO, CbbDesktopPoolDTO targetDesktopPoolDTO) throws BusinessException {
        Assert.notNull(desktopDetailDTO, "desktopDetailDTO can not be null");
        Assert.notNull(targetDesktopPoolDTO, "targetDesktopPoolDTO can not be null");

        // 只有VDI和第三方桌面可以移动
        if (Objects.equals(desktopDetailDTO.getDesktopCategory(), CbbCloudDeskType.IDV.name()) ||
                Objects.equals(desktopDetailDTO.getDesktopCategory(), CbbCloudDeskType.VOI.name())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_ONLY_SUPPORT_VDI);
        }

        if (Objects.equals(desktopDetailDTO.getDesktopPoolId(), targetDesktopPoolDTO.getId())) {
            LOGGER.info("云桌面[{}]已在桌面池[{}]中无需移动", desktopDetailDTO.getDesktopName(), targetDesktopPoolDTO.getName());
            return;
        }

        // 访客桌面不能移动
        if (Objects.equals(desktopDetailDTO.getUserType(), IacUserTypeEnum.VISITOR.name())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_NOT_SUPPORT_VISITOR);
        }

        // 只支持移动关机和离线状态的云桌面
        if (!DESKTOP_MOVE_STATES_SET.contains(desktopDetailDTO.getDesktopState())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_STATE_NOT_SUPPORT);
        }

        // 只有普通桌面和静态桌面能移动
        if (!SUPPORT_MOVE_DESKTOP_TYPE_SET.contains(desktopDetailDTO.getDesktopPoolType())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_TYPE_NOT_SUPPORT, desktopDetailDTO.getDesktopName());
        }

        // 只能移动到静态池
        if (targetDesktopPoolDTO.getPoolModel() != CbbDesktopPoolModel.STATIC) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_TARGET_MUST_POOL);
        }

        // 桌面会话类型与桌面池会话类型不同，不能移动
        if (targetDesktopPoolDTO.getSessionType() != desktopDetailDTO.getSessionType()) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_SESSION_TYPE_ERROR);
        }

        // 桌面类型与桌面池不同，VDI不能移动到第三方，第三方不能移动到VDI
        if (!desktopDetailDTO.getDesktopCategory().equals(targetDesktopPoolDTO.getPoolType().name())) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_STRATEGY_NOT_SAME_TYPE);
        }

        CbbDeskStrategyDTO poolDeskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(targetDesktopPoolDTO.getStrategyId());
        // 云桌面策略中云桌面类型必须是相同的，同为个性或者同为还原
        if (Objects.nonNull(poolDeskStrategyDTO.getPattern()) && Objects.nonNull(desktopDetailDTO.getDesktopType())
                && !Objects.equals(desktopDetailDTO.getDesktopCategory(), CbbCloudDeskType.THIRD.name())) {
            if (!Objects.equals(poolDeskStrategyDTO.getPattern().name(), desktopDetailDTO.getDesktopType())) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_STRATEGY_NOT_SAME_TYPE);
            }
        }

        MoveDesktopDTO moveDesktopDTO = new MoveDesktopDTO();
        moveDesktopDTO.setDesktopId(desktopDetailDTO.getId());
        moveDesktopDTO.setTargetDesktopPoolId(targetDesktopPoolDTO.getId());
        moveDesktopDTO.setDesktopPoolType(DesktopPoolType.convertToPoolDeskType(targetDesktopPoolDTO.getPoolModel()));

        List<UUID> userIdList = new ArrayList<>();
        List<UUID> filterUserIdList = new ArrayList<>();
        List<HostUserEntity> entityList = hostUserService.findByDeskId(desktopDetailDTO.getId());
        if (CbbDesktopSessionType.MULTIPLE == desktopDetailDTO.getSessionType() && entityList != null) {
            // 多会话桌面全部关联用户
            userIdList = entityList.stream().map(HostUserEntity::getUserId).collect(Collectors.toList());
            // 过滤目的桌面池已关联的用户
            filterUserIdList = userIdList.stream().filter(userId -> !checkUserInDesktopPool(targetDesktopPoolDTO.getId(), userId))
                    .collect(Collectors.toList());
            moveDesktopDTO.setUserIdList(filterUserIdList);
        } else {
            // 单会话桌面关联的用户
            if (Objects.nonNull(desktopDetailDTO.getUserId()) && !checkUserInDesktopPool(targetDesktopPoolDTO.getId(),
                    desktopDetailDTO.getUserId())) {
                filterUserIdList.add(desktopDetailDTO.getUserId());
                userIdList.add(desktopDetailDTO.getUserId());
                moveDesktopDTO.setUserIdList(filterUserIdList);
            }
        }

        List<UUID> finalUserIdList = userIdList;
        LockableExecutor.executeWithTryLock(DESKTOP_POOL_MOVE_LOCK + moveDesktopDTO.getTargetDesktopPoolId().toString(), () -> {
            // 检查用户在静态池中是否已有绑定的桌面
            if (!CollectionUtils.isEmpty(finalUserIdList)
                    && desktopPoolUserService.checkUserIdListBindDesktopInPool(targetDesktopPoolDTO.getId(), finalUserIdList)) {
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_USER_ALREADY_BIND_IN_POOL);
            }
            try {
                desktopPoolCache.checkUserAndIncreaseDesktopNum(moveDesktopDTO.getTargetDesktopPoolId(), moveDesktopDTO.getDesktopId(),
                        finalUserIdList);
                // 修改桌面并添加用户到桌面池
                desktopPoolServiceTx.moveDesktop(moveDesktopDTO);
            } finally {
                desktopPoolCache.reduceDesktopNum(moveDesktopDTO.getTargetDesktopPoolId(), moveDesktopDTO.getDesktopId(),
                        moveDesktopDTO.getUserIdList());
            }
        }, 1);

        if (moveDesktopDTO.getUserIdList() != null && !moveDesktopDTO.getUserIdList().isEmpty()) {
            CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(moveDesktopDTO.getTargetDesktopPoolId());
            // 添加用户被加入桌面池的审计日志
            desktopPoolAPIHelper.saveDesktopPoolAddUserLog(desktopPoolDTO, moveDesktopDTO.getUserIdList());
        }
    }

    @Override
    public boolean checkUserBindDesktopInPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");
        return desktopPoolUserService.checkUserBindDesktopInPool(desktopPoolId, userId);
    }

    @Override
    public boolean checkUserIdListBindDesktopInPool(UUID desktopPoolId, List<UUID> userIdList) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userIdList, "userIdList can not be null");
        return desktopPoolUserService.checkUserIdListBindDesktopInPool(desktopPoolId, userIdList);
    }

    @Override
    public void addUserToDesktopPool(UUID desktopPoolId, UUID userId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");
        Assert.notNull(userId, "userId can not be null");

        LOGGER.info("添加用户[{}]到桌面池[{}]", userId, desktopPoolId);
        desktopPoolUserService.addUserToDesktopPool(desktopPoolId, userId);
    }

    @Override
    public void dealRelationAfterUpdateUser(UUID userId) {
        Assert.notNull(userId, "userId can not be null");

        desktopPoolUserService.dealRelationAfterUpdateUser(userId);
    }
}
