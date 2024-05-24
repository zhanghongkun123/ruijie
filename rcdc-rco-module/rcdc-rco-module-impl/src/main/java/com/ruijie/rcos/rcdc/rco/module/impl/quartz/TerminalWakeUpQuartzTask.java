package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalWakeUpAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/14 19:04
 *
 * @author yxq
 */
@Service
@Quartz(taskGroup = TaskGroup.ADMIN_CONFIG, scheduleTypeCode = ScheduleTypeCodeConstants.TERMINAL_WAKE_UP_SCHEDULE_TYPE_CODE,
        scheduleName = BusinessKey.RCDC_RCO_QUARTZ_TERMINAL_WAKE_UP, blockInMaintenanceMode = true)
public class TerminalWakeUpQuartzTask extends AbstractPageQueryAllHandle<String> implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalWakeUpQuartzTask.class);

    private static final ThreadExecutor THREAD_EXECUTOR = ThreadExecutors.newBuilder("terminalWakeUpQuartzTask") //
            .maxThreadNum(50) //
            .queueSize(10000) //
            .build();


    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private TerminalWakeUpAPI terminalWakeUpAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public void validate(String bizData) throws BusinessException {
        Assert.notNull(bizData, "bizData can not be null");
        QuartzData quartzData = JSON.parseObject(bizData, QuartzData.class);
        Assert.notNull(quartzData, "quartzData can not be null");
        boolean isTerminalArrIsEmpty = ObjectUtils.isEmpty(quartzData.getTerminalArr());
        boolean isTerminalGroupArrIsEmpty = ObjectUtils.isEmpty(quartzData.getTerminalGroupArr());
        Assert.state(!isTerminalArrIsEmpty || !isTerminalGroupArrIsEmpty, "终端组与终端不能同时为空");
    }

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");

        QuartzData quartzData = quartzTaskContext.getByType(QuartzData.class);
        // 获取配置终端id列表
        String[] terminalIdArr = Optional.ofNullable(quartzData.getTerminalArr()).orElse(new String[0]);
        List<String> terminalIdList = Arrays.asList(terminalIdArr);


        UUID[] terminalGroupIdArr = Optional.ofNullable(quartzData.getTerminalGroupArr()).orElse(new UUID[0]);
        List<UUID> groupIdList = Arrays.asList(terminalGroupIdArr);

        Set<String> terminalIdSet = queryAll();
        terminalIdSet.forEach(terminalId -> THREAD_EXECUTOR.execute(() -> executeWakeUp(terminalId, groupIdList, terminalIdList)));
    }

    private void executeWakeUp(String terminalId, List<UUID> groupIdList, List<String> terminalIdList) {
        LOGGER.debug("开始执行终端[{}]唤醒操作", terminalId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        String terminalName = "";
        String terminalMacAddr = terminalId;
        try {
            TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
            terminalName = terminalDTO.getTerminalName();
            terminalMacAddr = terminalDTO.getUpperMacAddrOrTerminalId();

            // 1.确认原来只通过终端组勾选的方式加入的终端是否发生切换组：
            UUID curGroupId = terminalDTO.getTerminalGroupId();
            if (!terminalIdList.contains(terminalId) && !groupIdList.contains(curGroupId)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("终端[终端id：{},终端组组id：{}]不在终端组[{}],终端[{}]中不执行关闭操作", terminalId, curGroupId, JSON.toJSONString(groupIdList),
                            JSON.toJSONString(terminalIdList));
                }
                return;
            }
            // 2. 确认终端是否在线
            if (terminalDTO.getTerminalState() != CbbTerminalStateEnums.OFFLINE) {
                LOGGER.debug("终端[{}{}]状态[{}]无需唤醒", terminalId, terminalName, terminalDTO.getTerminalState());
                return;
            }
            terminalWakeUpAPI.wakeUpTerminal(terminalId, Boolean.FALSE);
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_QUARTZ_TERMINAL_WAKE_UP_SUCCESS_SYSTEM_LOG, terminalMacAddr, timeMillis);
            LOGGER.info("执行唤醒终端[{}]成功；terminalId：{}", terminalName, terminalId);
        } catch (BusinessException e) {
            String timeMillis = String.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_QUARTZ_TERMINAL_WAKE_UP_FAIL_SYSTEM_LOG, e, terminalMacAddr, e.getI18nMessage(), timeMillis);
            LOGGER.error("执行唤醒终端[" + terminalName + "terminalId:" + terminalId + "]任务失败;原因：" + e.getI18nMessage(), e);
        } catch (Exception e) {
            LOGGER.error("执行唤醒终端[" + terminalName + "terminalId:" + terminalId + "]任务失败", e);
        }

        LOGGER.debug("执行终端[{}][{}]唤醒操作结束", terminalId, terminalName);
    }

    @Override
    protected List<String> queryByPage(Pageable pageable) throws BusinessException {
        PageSearchRequest pageSearchRequest = new PageSearchRequest();
        pageSearchRequest.setPage(pageable.getPageNumber());
        pageSearchRequest.setLimit(pageable.getPageSize());

        MatchEqual stateMatch = new MatchEqual("terminalState", new CbbTerminalStateEnums[]{CbbTerminalStateEnums.OFFLINE});
        // 当前只支持IDV、TCI
        MatchEqual platform = new MatchEqual("platform",
                new CbbTerminalPlatformEnums[]{CbbTerminalPlatformEnums.IDV, CbbTerminalPlatformEnums.VOI});
        pageSearchRequest.setMatchEqualArr(new MatchEqual[]{stateMatch, platform});

        DefaultPageResponse<TerminalDTO> terminalListDTODefaultPageResponse = userTerminalMgmtAPI.pageQuery(pageSearchRequest);

        return Arrays.stream(terminalListDTODefaultPageResponse.getItemArr()).map(TerminalDTO::getId).collect(Collectors.toList());
    }


    /**
     * 定时任务数据
     */
    static class QuartzData {

        private UUID[] terminalGroupArr;

        private String[] terminalArr;

        public UUID[] getTerminalGroupArr() {
            return terminalGroupArr;
        }

        public void setTerminalGroupArr(UUID[] terminalGroupArr) {
            this.terminalGroupArr = terminalGroupArr;
        }

        @Nullable
        public String[] getTerminalArr() {
            return terminalArr;
        }

        public void setTerminalArr(@Nullable String[] terminalArr) {
            this.terminalArr = terminalArr;
        }
    }
}
