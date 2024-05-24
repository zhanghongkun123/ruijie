package com.ruijie.rcos.rcdc.rco.module.impl.rca.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseByBusinessIdRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmStatus;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.api.BusinessMaintenanceAPI;
import com.ruijie.rcos.rcdc.maintenance.module.def.dto.enums.BusinessMaintenanceStatusEnums;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaHostDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.StartupTaskInfoDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description: 应用池主机维持预启动数定时任务,每分钟执行
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/18
 *
 * @author zhengjingyong
 */
@Service
@Quartz(scheduleTypeCode = "host_of_app_pool_pre_start", scheduleName = RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START,
        cron = "0 0/1 * * * ?")
public class AppPoolPreStartQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppPoolPreStartQuartzTask.class);

    private static final List<CbbCloudDeskState> HOST_READY_START_LIST = Lists.newArrayList(CbbCloudDeskState.SLEEP, CbbCloudDeskState.CLOSE);

    private static final List<CbbCloudDeskState> HOST_START_LIST = Lists.newArrayList(CbbCloudDeskState.RUNNING, CbbCloudDeskState.START_UP,
            CbbCloudDeskState.WAKING);

    private static final int MAX_WORKING_NUM = 20;

    private static final long TASK_SLEEP_TIME = TimeUnit.SECONDS.toMillis(3);

    /**
     * 分配20个线程数处理池主机开机
     */
    private static final ExecutorService START_THREAD_POOL = ThreadExecutors.newBuilder("appPoolPreStartQuartzTask")
            .maxThreadNum(20).queueSize(1000).build();

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    @Autowired
    private BusinessMaintenanceAPI businessMaintenanceAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    @Autowired
    private RcaHostDesktopMgmtAPI rcaHostDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private BaseAlarmAPI alarmAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {

        SystemMaintenanceState maintenanceMode = maintenanceModeMgmtAPI.getMaintenanceMode();
        BusinessMaintenanceStatusEnums businessMaintenanceMode = businessMaintenanceAPI.getBusinessStatus();
        if (maintenanceMode != SystemMaintenanceState.NORMAL || businessMaintenanceMode != BusinessMaintenanceStatusEnums.NORMAL) {
            LOGGER.info("维持预启动数定时任务不执行,当前系统维护模式:{},业务维护模式:{}", maintenanceMode, businessMaintenanceMode);
            return;
        }

        // 1.获取符合状态的池列表
        List<RcaAppPoolBaseDTO> vdiAppPoolBaseDTOList = rcaAppPoolAPI.getAllByHostSourceType(RcaEnum.HostSourceType.VDI);
        vdiAppPoolBaseDTOList = vdiAppPoolBaseDTOList.stream().filter(item -> RcaEnum.PoolState.AVAILABLE.equals(item.getPoolState())
                && RcaEnum.PoolType.DYNAMIC == item.getPoolType() && !item.getOpenMaintenance() && item.getPreStartHostNum() > 0)
                .collect(Collectors.toList());
        LOGGER.info("维持预启动数定时任务,获取可用状态且未开启维护模式且预启动数大于0的应用池列表,总数:{}", vdiAppPoolBaseDTOList.size());
        if (CollectionUtils.isEmpty(vdiAppPoolBaseDTOList)) {
            return;
        }

        Queue<UUID> desktopIdQueue = new LinkedList<>();
        PerStartQuartzTaskAlarmHandler alarmHandler = new PerStartQuartzTaskAlarmHandler();
        for (RcaAppPoolBaseDTO appPoolBaseDTO : vdiAppPoolBaseDTOList) {
            try {
                checkPoolAndGetPreStartHost(appPoolBaseDTO, alarmHandler, desktopIdQueue);
            } catch (BusinessException ex) {
                LOGGER.info("查询并获取应用池[{}]需预启动的主机发生异常，跳过处理，ex: ", appPoolBaseDTO.getId(), ex);
            }
        }

        // 在线程池中，进行主机启动
        startHostByQueue(desktopIdQueue, alarmHandler);
        alarmHandler.handleAlarm();
        LOGGER.info("维持预启动数定时任务执行结束");
    }

    private void checkPoolAndGetPreStartHost(RcaAppPoolBaseDTO appPoolBaseDTO, PerStartQuartzTaskAlarmHandler alarmHandler, Queue<UUID> desktopIdQueue) throws BusinessException {
        // 根据应用池筛选主机列表
        UUID poolId = appPoolBaseDTO.getId();
        List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Lists.newArrayList(poolId));
        List<UUID> hostIdList = hostDTOList.stream().map(RcaHostDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(hostIdList)) {
            LOGGER.info("应用池[{}]配置了预启动信息，但是主机列表为空，跳过预启动处理", poolId);
            return;
        }

        List<RcaHostDesktopDTO> rcaHostDesktopDTOList = rcaHostDesktopMgmtAPI.listByHostIdIn(hostIdList);
        // 过滤出状态正常，未绑定用户且策略一致的桌面
        rcaHostDesktopDTOList = rcaHostDesktopDTOList.stream().filter(item -> checkNotNormalStateDesktop(item)
                && checkDesktopNotUserWithSameSpecAndStrategy(appPoolBaseDTO, item)).collect(Collectors.toList());

        // 筛选在线主机
        List<RcaHostDesktopDTO> onlineHostDesktopDTOList = rcaHostDesktopDTOList.stream().filter(item ->
                HOST_START_LIST.contains(CbbCloudDeskState.valueOf(item.getDesktopState()))).collect(Collectors.toList());
        int onlineHostCount = onlineHostDesktopDTOList.size();
        if (appPoolBaseDTO.getPreStartHostNum() <= onlineHostCount) {
            LOGGER.info("应用池[{}]筛选出在线主机数量:{}，满足预启动配置数：{}，跳过预启动处理",
                    poolId, onlineHostCount, appPoolBaseDTO.getPreStartHostNum());
            LOGGER.info("应用池[{}]满足预启动配置数，尝试解除告警", poolId);
            alarmHandler.releaseIfExistAlarm(poolId);
            return;
        }

        // 筛选离线主机
        int diffHostCount = appPoolBaseDTO.getPreStartHostNum() - onlineHostCount;
        LOGGER.info("应用池[{}]筛选出离线主机,缺口主机数:{}", poolId, diffHostCount);
        List<RcaHostDesktopDTO> offlineHostDesktopDTOList = rcaHostDesktopDTOList.stream().filter(item ->
                HOST_READY_START_LIST.contains(CbbCloudDeskState.valueOf(item.getDesktopState()))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(offlineHostDesktopDTOList)) {
            LOGGER.info("应用池[{}]筛选出离线主机为空，跳过处理", poolId);
            return;
        }

        LOGGER.info("开始预启动应用池[{}]-[{}]主机", poolId, appPoolBaseDTO.getName());
        int startHostCount = 0;
        // 优先加入休眠的主机，顺序是由HOST_READY_START_LIST决定
        DESK_FILTER:
        for (CbbCloudDeskState deskState : HOST_READY_START_LIST) {
            LOGGER.info("当前处理的状态为：{}", deskState);
            for (RcaHostDesktopDTO rcaHostDesktopDTO : offlineHostDesktopDTOList) {
                if (rcaHostDesktopDTO.getDesktopState().equals(deskState.name())) {
                    LOGGER.info("主机加入预启动逻辑");

                    desktopIdQueue.add(rcaHostDesktopDTO.getCbbId());
                    diffHostCount -= 1;
                    startHostCount++;
                    alarmHandler.putRcaHostAndDesktopRelation(appPoolBaseDTO.getId(), rcaHostDesktopDTO.getCbbId());
                    LOGGER.info("应用池[{}]主机[{}]加入预启动逻辑，剩余差距数量[{}]", appPoolBaseDTO.getId(),
                            rcaHostDesktopDTO.getId(), diffHostCount);
                    if (diffHostCount <= 0) {
                        break DESK_FILTER;
                    }
                } else {
                    LOGGER.info("应用池[{}]主机[{}]状态[{}]不符合预期，跳过处理", appPoolBaseDTO.getId(),
                            rcaHostDesktopDTO.getId(), rcaHostDesktopDTO.getDesktopState());
                }
            }
        }
        LOGGER.info("应用池[{}]筛选出不在线主机,缺口主机数已达标，需要启动的主机数量为[{}]", appPoolBaseDTO.getId(), startHostCount);
        alarmHandler.putStartHostNum(appPoolBaseDTO.getId(), startHostCount);
        alarmHandler.putRcaAppPoolBaseDTO(appPoolBaseDTO);
    }

    private void  startHostByQueue(Queue<UUID> desktopIdQueue, PerStartQuartzTaskAlarmHandler alarmHandler) {
        List<StartupTaskInfoDTO> workingList = new ArrayList<>();
        Map<UUID, CloudDesktopDetailDTO> desktopMap = new HashMap<>();
        while (true) {
            // 遍历检查执行中的任务
            checkWorkingList(workingList, desktopMap, alarmHandler);

            // 添加启动桌面子任务
            while (workingList.size() < MAX_WORKING_NUM && !desktopIdQueue.isEmpty()) {
                UUID deskId = desktopIdQueue.poll();
                CloudDesktopDetailDTO cloudDesktopDetailDTO = getCloudDesktopDetail(deskId);
                if (cloudDesktopDetailDTO == null) {
                    continue;
                }
                addStartupTask(cloudDesktopDetailDTO, workingList, desktopMap);
            }
            if (workingList.isEmpty()) {
                break;
            }
            try {
                Thread.sleep(TASK_SLEEP_TIME);
            } catch (InterruptedException e) {
                LOGGER.error("预启动应用池主机任务睡眠发生异常，ex: ", e);
            }
        }
    }

    private void putPoolStartHostResultMap(Map<UUID, Map<UUID, Boolean>> poolStartHostResultMap, UUID poolId, UUID desktopId, Boolean result) {
        poolStartHostResultMap.computeIfAbsent(poolId, k -> new HashMap<>()).putIfAbsent(desktopId, result);
    }

    private boolean checkNotNormalStateDesktop(RcaHostDesktopDTO rcaHostDesktopDTO) {
        CbbCloudDeskState state = CbbCloudDeskState.valueOf(rcaHostDesktopDTO.getDesktopState());
        boolean isNormalStatus = !Boolean.TRUE.equals(rcaHostDesktopDTO.getDelete()) && state != CbbCloudDeskState.RECYCLE_BIN
                && state != CbbCloudDeskState.DELETING && state != CbbCloudDeskState.CREATING
                && state != CbbCloudDeskState.COMPLETE_DELETING;
        if (!isNormalStatus) {
            LOGGER.info("主机桌面跳过预启动处理，主机id为：{}", rcaHostDesktopDTO.getId());
        }
        return isNormalStatus;
    }

    private boolean checkDesktopNotUserWithSameSpecAndStrategy(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, RcaHostDesktopDTO hostDesktopDTO) {
        try {
            // 过滤出未绑定的且策略，镜像模板都和池配置相等的桌面
            boolean isResult = Objects.isNull(hostDesktopDTO.getUserId()) && Boolean.FALSE.equals(hostDesktopDTO.getIsOpenDeskMaintenance())
                    && isDesktopSpecEqualsToPoolSpec(hostDesktopDTO, rcaAppPoolBaseDTO);
            if (!isResult) {
                LOGGER.info("主机桌面跳过预启动处理，主机id为：{}", hostDesktopDTO.getId());
            }
            return isResult;
        } catch (Exception e) {
            LOGGER.error("检查主机和池的规格策略等信息是否一致是发生异常, 池id:{}, 主机id:{}，ex:",
                    rcaAppPoolBaseDTO.getId(), hostDesktopDTO.getRcaHostId(), e);
            return false;
        }
    }

    private boolean isDesktopSpecEqualsToPoolSpec(RcaHostDesktopDTO desktopDTO, RcaAppPoolBaseDTO rcaAppPoolBaseDTO) {
        if (!Objects.equals(rcaAppPoolBaseDTO.getImageTemplateId(), desktopDTO.getImageTemplateId())) {
            LOGGER.info("应用池的镜像和主机镜像不同，不做预启动：应用池[{}], 主机镜像[{}]", rcaAppPoolBaseDTO.getImageTemplateId(),
                    desktopDTO.getImageTemplateId());
            return false;
        }

        if (!Objects.equals(rcaAppPoolBaseDTO.getCpu(), desktopDTO.getCpu())) {
            LOGGER.info("应用池的CPU和主机CPU不同，不做预启动：应用池[{}], 主机[{}]", rcaAppPoolBaseDTO.getCpu(),
                    desktopDTO.getCpu());
            return false;
        }

        if (!Objects.equals(rcaAppPoolBaseDTO.getMemory(), desktopDTO.getMemory())) {
            LOGGER.info("应用池的MEM和主机MEM不同，不做预启动：应用池[{}], 主机[{}]", rcaAppPoolBaseDTO.getMemory(),
                    desktopDTO.getMemory());
            return false;
        }

        if (!Objects.equals(rcaAppPoolBaseDTO.getSystemSize(), desktopDTO.getSystemDisk())) {
            LOGGER.info("应用池的SystemSize和主机SystemSize不同，不做预启动：应用池[{}], 主机[{}]",
                    rcaAppPoolBaseDTO.getSystemSize(), desktopDTO.getSystemDisk());
            return false;
        }

        if (desktopDTO.getPersonDisk() == null) {
            if (rcaAppPoolBaseDTO.getPersonalConfigDiskSize() != null && rcaAppPoolBaseDTO.getPersonalConfigDiskSize() != 0) {
                LOGGER.info("应用池的PersonSize和主机PersonSize不同，不做预启动：应用池[{}], 主机[{}]",
                        rcaAppPoolBaseDTO.getPersonalConfigDiskSize(), "null");
                return false;
            }
        } else {
            Integer poolPersonDisk = rcaAppPoolBaseDTO.getPersonalConfigDiskSize() == null ? 0 :
                    rcaAppPoolBaseDTO.getPersonalConfigDiskSize();
            if (!Objects.equals(poolPersonDisk, desktopDTO.getPersonDisk())) {
                LOGGER.info("应用池的PersonSize和主机PersonSize不同，不做预启动：应用池[{}], 主机[{}]",
                        poolPersonDisk, desktopDTO.getPersonDisk());
                return false;
            }
        }

        VgpuExtraInfoSupport vGpuExtraInfo = VgpuUtil.deserializeVgpuExtraInfoByType(
                rcaAppPoolBaseDTO.getVgpuType(), rcaAppPoolBaseDTO.getVgpuExtraInfo());
        VgpuInfoDTO poolVgpuInfo = new VgpuInfoDTO(rcaAppPoolBaseDTO.getVgpuType(), vGpuExtraInfo);
        VgpuInfoDTO deskVgpuInfo = new VgpuInfoDTO(rcaAppPoolBaseDTO.getVgpuType(), vGpuExtraInfo);
        if (!deskSpecAPI.isVgpuInfoEquals(deskVgpuInfo , poolVgpuInfo)) {
            LOGGER.info("应用池的vgpu和主机vgpu不同，不做预启动：应用池[{}], 主机[{}]",
                    JSON.toJSONString(poolVgpuInfo), JSON.toJSONString(deskVgpuInfo));
            return false;
        }

        return true;
    }

    private void checkWorkingList(List<StartupTaskInfoDTO> workingList, Map<UUID, CloudDesktopDetailDTO> cloudDesktopMap,
                                  PerStartQuartzTaskAlarmHandler alarmHandler) {
        if (workingList.isEmpty()) {
            return;
        }
        List<StartupTaskInfoDTO> removeList = new ArrayList<>();
        for (StartupTaskInfoDTO taskInfo : workingList) {
            checkTaskResult(taskInfo, removeList, cloudDesktopMap, alarmHandler);
        }

        workingList.removeAll(removeList);
    }

    private void checkTaskResult(StartupTaskInfoDTO taskInfo,
                                 List<StartupTaskInfoDTO> removeList,
                                 Map<UUID, CloudDesktopDetailDTO> cloudDesktopMap,
                                 PerStartQuartzTaskAlarmHandler alarmHandler) {
        // 查询状态机
        StateMachineMgmtAgent stateMachineMgmtAgent;
        try {
            stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskInfo.getTaskId());
        } catch (NoSuchElementException e) {
            LOGGER.error("维持预启动数定时任务,查询状态机无任务taskInfo[{}]", JSON.toJSONString(taskInfo), e);
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                cloudDesktopMap.remove(taskInfo.getDeskId());
                alarmHandler.putStartRcaHostDesktopResult(taskInfo.getDeskId(), false);

            }
            return;
        } catch (Exception e) {
            LOGGER.error("维持预启动数定时任务,查看启动桌面[{}]状态机[{}]任务异常", taskInfo.getDeskId(), taskInfo.getTaskId(), e);
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_FAIL_LOG,
                        cloudDesktop.getDesktopName(), getUnknownErrorMsg());
                cloudDesktopMap.remove(taskInfo.getDeskId());
                alarmHandler.putStartRcaHostDesktopResult(taskInfo.getDeskId(), false);
            }
            return;
        }

        StateMachineMgmtAgent.SmRunningState runningState = stateMachineMgmtAgent.getRunningState();
        if (runningState != StateMachineMgmtAgent.SmRunningState.FINISHED) {
            // 未结束
            return;
        }
        if (StateMachineMgmtAgent.SmExecutionStage.UNDO == stateMachineMgmtAgent.getExecutionStage()) {
            // 任务失败
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                StateMachineMgmtAgent.ExceptionInfo errorInfo = stateMachineMgmtAgent.getExceptionInfo();
                String errorMessage = Objects.isNull(errorInfo) ? getUnknownErrorMsg() : errorInfo.getExceptionMessage();
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_FAIL_LOG,
                        cloudDesktop.getDesktopName(), errorMessage);
                cloudDesktopMap.remove(taskInfo.getDeskId());
                alarmHandler.putStartRcaHostDesktopResult(taskInfo.getDeskId(), false);
            }
            return;
        }
        if (StateMachineMgmtAgent.SmExecutionStage.DO == stateMachineMgmtAgent.getExecutionStage()) {
            // 任务成功
            removeList.add(taskInfo);
            CloudDesktopDetailDTO cloudDesktop = cloudDesktopMap.get(taskInfo.getDeskId());
            if (Objects.nonNull(cloudDesktop)) {
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_SUCCESS_LOG,
                        cloudDesktop.getDesktopName());
                cloudDesktopMap.remove(taskInfo.getDeskId());
                alarmHandler.putStartRcaHostDesktopResult(taskInfo.getDeskId(), true);
            }
        }
    }

    private CloudDesktopDetailDTO getCloudDesktopDetail(UUID deskId) {
        try {
            return userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_FAIL_LOG,
                    deskId.toString(), e.getI18nMessage());
            LOGGER.error(String.format("执行启动池主机失败。deskId:[%s]", deskId), e);
            // 云桌面信息为空
            return null;
        } catch (Exception e) {
            String message = getUnknownErrorMsg();
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_FAIL_LOG, deskId.toString(), message);
            LOGGER.error(String.format("执行启动池主机失败。deskId:[%s]", deskId), e);
            // 云桌面信息为空
            return null;
        }
    }

    private void addStartupTask(CloudDesktopDetailDTO cloudDesktopDetailDTO, List<StartupTaskInfoDTO> workingList,
                                Map<UUID, CloudDesktopDetailDTO> cloudDesktopMap) {
        UUID deskId = cloudDesktopDetailDTO.getId();
        UUID taskId = UUID.randomUUID();

        // 启动桌面
        START_THREAD_POOL.execute(() -> {
            try {
                userDesktopOperateAPI.start(new CloudDesktopStartRequest(deskId, taskId));
            } catch (BusinessException e) {
                LOGGER.error("执行启动池桌面失败。deskId:[{}]", deskId, e);
                // 添加审计日志
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_FAIL_LOG,
                        cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
            } catch (Exception e) {
                LOGGER.error("执行启动池桌面失败。deskId:[{}]", deskId, e);
                // 添加审计日志
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_FAIL_LOG,
                        cloudDesktopDetailDTO.getDesktopName(), e.getMessage());
            }
        });

        workingList.add(new StartupTaskInfoDTO(deskId, taskId));
        cloudDesktopMap.put(deskId, cloudDesktopDetailDTO);
    }

    private String getUnknownErrorMsg() {
        return LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_RCA_APP_POOL_QUARTZ_HOST_OF_POOL_PRE_START_UNKNOWN_ERROR);
    }

    /**
     * Description: 应用池预启动告警处理内部类
     * Copyright: Copyright (c) 2024
     * Company: Ruijie Co., Ltd.
     * Create Time: 2024年04月17日
     *
     * @author xiejian
     */
    private class PerStartQuartzTaskAlarmHandler {

        /**
         * 池信息map,<poolId, RcaAppPoolBaseDTO>
         */
        private Map<UUID, RcaAppPoolBaseDTO> rcaAppPoolBaseDTOMap;

        /**
         * 本次启动数量<poolId, startHost>
         */
        private Map<UUID, Integer> startHostNumMap;

        /**
         * 根据desktopId查询池id
         */
        private Map<UUID, UUID> desktopIdAndPoolIdRelationMap;

        /**
         * 本次启动具体结果<desktopId, result>
         */
        private Map<UUID, Map<UUID, Boolean>> startRcaHostDesktopResultMap;

        PerStartQuartzTaskAlarmHandler() {
            this.rcaAppPoolBaseDTOMap = new HashMap<>();
            this.startHostNumMap = new HashMap<>();
            this.desktopIdAndPoolIdRelationMap = new HashMap<>();
            this.startRcaHostDesktopResultMap = new HashMap<>();
        }

        public void putRcaAppPoolBaseDTO(RcaAppPoolBaseDTO rcaAppPoolBaseDTO) {
            Assert.notNull(rcaAppPoolBaseDTO, "rcaAppPoolBaseDTO is not null");

            this.rcaAppPoolBaseDTOMap.put(rcaAppPoolBaseDTO.getId(), rcaAppPoolBaseDTO);
        }

        public void putStartHostNum(UUID poolId, Integer startHostNum) {
            Assert.notNull(poolId, "poolId is not null");
            Assert.notNull(startHostNum, "startHostNum is not null");

            this.startHostNumMap.put(poolId, startHostNum);
        }

        public void putRcaHostAndDesktopRelation(UUID poolId, UUID desktopId) {
            Assert.notNull(poolId, "poolId is not null");
            Assert.notNull(desktopId, "desktopId is not null");

            this.desktopIdAndPoolIdRelationMap.put(desktopId, poolId);
        }

        public void putStartRcaHostDesktopResult(UUID desktopId, Boolean result) {
            Assert.notNull(desktopId, "desktopId is not null");
            Assert.notNull(result, "result is not null");

            UUID poolId = this.desktopIdAndPoolIdRelationMap.get(desktopId);
            Map<UUID, Boolean> resultMap = this.startRcaHostDesktopResultMap.computeIfAbsent(poolId, k -> new HashMap<>());
            resultMap.put(desktopId, result);
        }

        public void handleAlarm() {
            for (Map.Entry<UUID, Integer> entry : startHostNumMap.entrySet()) {
                UUID poolId = entry.getKey();
                Integer startHostNum = entry.getValue();
                int failCount = 0;
                for (Map.Entry<UUID, Boolean> resultEntry : this.startRcaHostDesktopResultMap.get(poolId).entrySet()) {
                    if (Boolean.FALSE.equals(resultEntry.getValue())) {
                        failCount++;
                    }
                }
                if (failCount > 0) {
                    RcaAppPoolBaseDTO baseDTO = rcaAppPoolBaseDTOMap.get(poolId);
                    String poolName = baseDTO.getName();
                    Integer preHostNum = baseDTO.getPreStartHostNum();
                    saveAlarm(poolId, poolName, preHostNum, startHostNum, failCount);
                    LOGGER.info("应用池{}启动失败数量为{}，生成告警", poolId, failCount);
                } else {
                    releaseIfExistAlarm(poolId);
                    LOGGER.info("应用池{}启动失败数量为0，解除告警", poolId);
                }
            }
        }

        public void saveAlarm(UUID poolId, String poolName, Integer preHostNum, Integer startHostCount, Integer failCount) {
            Assert.notNull(poolId, "poolId is not null");
            Assert.notNull(poolName, "poolName is not null");
            Assert.notNull(preHostNum, "preHostNum is not null");
            Assert.notNull(startHostCount, "startHostCount is not null");
            Assert.notNull(failCount, "failCount is not null");

            try { //防止告警问题影响定时任务
                SaveAlarmRequest request = new SaveAlarmRequest();
                request.setAlarmCode(Constants.APP_POOL_PRE_START_ERROR_ALARM_CODE + poolId);
                request.setAlarmLevel(AlarmLevel.WARN);
                request.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
                request.setAlarmNameByI18nKey(BusinessKey.RCDC_RCO_APP_POOL_QUARTZ_ALARM_NAME);
                request.setAlarmContentByI18nKey(
                        BusinessKey.RCDC_RCO_APP_POOL_QUARTZ_ALARM_CONTENT, poolName,
                        String.valueOf(preHostNum),
                        String.valueOf(startHostCount),
                        String.valueOf(failCount));
                request.setAlarmTime(new Date());
                request.setEnableSendMail(true);
                request.setBusinessId(poolId.toString());
                alarmAPI.saveAlarm(request);
            } catch (Exception e) {
                LOGGER.error("应用池{}-{}预启动主机任务告警失败", poolId, poolName, e);
            }
        }

        public void releaseIfExistAlarm(UUID poolId) {
            Assert.notNull(poolId, "poolId is not null");

            try { //防止告警问题影响定时任务
                String businessId = poolId.toString();
                ListAlarmRequest listRequest = new ListAlarmRequest();
                listRequest.setBusinessIdArr(new String[] { businessId });
                listRequest.setEnableQueryHistory(false);
                DefaultPageResponse<AlarmDTO> pageResponse =  alarmAPI.listAlarmList(listRequest);
                if (pageResponse.getTotal() > 0) {
                    ReleaseByBusinessIdRequest releaseByBusinessIdRequest = new ReleaseByBusinessIdRequest();
                    releaseByBusinessIdRequest.setBusinessId(businessId);
                    releaseByBusinessIdRequest.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
                    releaseByBusinessIdRequest.setAlarmStatus(AlarmStatus.AUTO_RELEASED);
                    alarmAPI.releaseByBusinessId(releaseByBusinessIdRequest);
                    LOGGER.info("应用池[{}]满足预启动配置数，自动解除告警成功", poolId);
                }
            } catch (BusinessException e) {
                LOGGER.error("自动解除应用池{}预启动主机任务告警失败", poolId, e);
            }
        }
    }
}
