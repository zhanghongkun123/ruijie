package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月17日
 *
 * @author xgx
 */
public abstract class AbstractCloudDeskQuartzTask extends AbstractPageQueryAllHandle<UUID> implements QuartzTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCloudDeskQuartzTask.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public void validate(String bizData) throws BusinessException {
        Assert.notNull(bizData, "bizData can not be null");
        CloudDeskQuartzData cloudDeskQuartzData = JSON.parseObject(bizData, CloudDeskQuartzData.class);
        Assert.notNull(cloudDeskQuartzData, "cloudDeskQuartzData can not be null");
        boolean isDesktopPoolArrEmpty = ObjectUtils.isEmpty(cloudDeskQuartzData.getDesktopPoolArr());
        if (!isDesktopPoolArrEmpty) {
            return;
        }
        boolean isUserGroupIdArrIsEmpty = ObjectUtils.isEmpty(cloudDeskQuartzData.getUserGroupArr());
        boolean isUserIdArrIsEmpty = ObjectUtils.isEmpty(cloudDeskQuartzData.getUserArr());
        boolean isDeskIdArrIsEmpty = ObjectUtils.isEmpty(cloudDeskQuartzData.getDeskArr());
        Assert.state(!isUserGroupIdArrIsEmpty || !isUserIdArrIsEmpty || !isDeskIdArrIsEmpty, "用户组、用户、云桌面不能同时为空");
    }

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        Assert.notNull(quartzTaskContext, "quartzTaskContext can not be null");
        CloudDeskQuartzData cloudDeskQuartzData = quartzTaskContext.getByType(CloudDeskQuartzData.class);
        List<UUID> deskIdList = getDeskIdArr(cloudDeskQuartzData, checkStaticPoolIsSupport());

        UUID[] userIdArr = Optional.ofNullable(cloudDeskQuartzData.getUserArr()).orElse(new UUID[0]);
        List<UUID> userIdList = Arrays.stream(userIdArr).collect(Collectors.toList());

        // 获取所有用户组
        UUID[] userGroupIdArr = Optional.ofNullable(cloudDeskQuartzData.getUserGroupArr()).orElse(new UUID[0]);
        Set<UUID> userGroupChildrenSet = Stream.of(userGroupIdArr).collect(Collectors.toSet());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("云桌面定时任务配置涉及所有用户组id[{}]", JSON.toJSONString(userGroupChildrenSet));
        }
        // 查询所有桌面id
        Set<UUID> deskIdSet = queryAll();

        deskIdSet.stream() //
                .forEach(deskId -> getThreadExecutor().execute(() -> executeOperator(deskId, userGroupChildrenSet, userIdList, deskIdList)));

        LOGGER.debug("云桌面操作定时任务执行结束");
    }

    @Override
    protected List<UUID> queryByPage(Pageable pageable) throws BusinessException {
        // 只查询当前启动的桌面 ，分页查询，100，第一次查出总数，然后计算后续分页的下标
        PageSearchRequest pageSearchRequest = new PageSearchRequest();
        pageSearchRequest.setLimit(pageable.getPageSize());
        pageSearchRequest.setPage(pageable.getPageNumber());
        String[] needHandleStateArr = getNeedHandleState();
        MatchEqual[] matchEqualArr = new MatchEqual[] {};
        if (ArrayUtils.isNotEmpty(needHandleStateArr)) {
            MatchEqual stateMatch = new MatchEqual();
            stateMatch.setName("deskState");
            stateMatch.setValueArr(needHandleStateArr);
            matchEqualArr = new MatchEqual[] {stateMatch};
        }
        pageSearchRequest.setMatchEqualArr(matchEqualArr);
        pageSearchRequest.setSortArr(new Sort[] {});

        DefaultPageResponse<CloudDesktopDTO> cloudDesktopDTOResponse = userDesktopMgmtAPI.pageQuery(pageSearchRequest);

        return Arrays.stream(cloudDesktopDTOResponse.getItemArr()).map(item -> item.getId()).collect(Collectors.toList());
    }


    /**
     * 执行云桌面的相关操作
     *
     * @param deskId
     */
    protected abstract void executeOperator(UUID deskId, Set<UUID> userGroupChildrenSet, List<UUID> userIdList, List<UUID> deskIdList);

    protected abstract ThreadExecutor getThreadExecutor();

    protected abstract String[] getNeedHandleState();

    protected void addSystemLog(String businessKey, String[] args) {
        BaseCreateSystemLogRequest createRequest = new BaseCreateSystemLogRequest(businessKey, args);
        baseSystemLogMgmtAPI.createSystemLog(createRequest);
    }

    protected List<UUID> getDeskIdArr(CloudDeskQuartzData cloudDeskQuartzData, boolean enableSupportPool) {
        List<UUID> deskIdList = Arrays.stream(Optional.ofNullable(cloudDeskQuartzData.getDeskArr()).orElse(new UUID[0]))
                .collect(Collectors.toList());
        if (!enableSupportPool || ArrayUtils.isEmpty(cloudDeskQuartzData.getDesktopPoolArr())) {
            return deskIdList;
        }
        // 查询桌面池关联的桌面ID列表
        List<UUID> poolDeskIdList = cbbDeskMgmtAPI.listDeskIdByDesktopPoolIds(Lists.newArrayList(cloudDeskQuartzData.getDesktopPoolArr()), false);
        deskIdList.addAll(poolDeskIdList);
        return deskIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 是否支持静态池配置该定时任务
     *
     * @return false不支持，true支持
     */
    protected boolean checkStaticPoolIsSupport() {
        return false;
    }

    /**
     * 定时任务数据
     */
    public static class CloudDeskQuartzData {
        private UUID[] userGroupArr;

        private UUID[] userArr;

        private UUID[] deskArr;

        private UUID[] desktopPoolArr;
        
        private UUID platformId;

        private UUID extStorageId;

        private Integer backupDuration;

        /**
         * 是否允许取消
         */
        private Boolean allowCancel;

        private Integer amount;

        public UUID[] getUserGroupArr() {
            return userGroupArr;
        }

        public void setUserGroupArr(UUID[] userGroupArr) {
            this.userGroupArr = userGroupArr;
        }

        public UUID[] getUserArr() {
            return userArr;
        }

        public void setUserArr(UUID[] userArr) {
            this.userArr = userArr;
        }

        public UUID[] getDeskArr() {
            return deskArr;
        }

        public void setDeskArr(UUID[] deskArr) {
            this.deskArr = deskArr;
        }

        public UUID[] getDesktopPoolArr() {
            return desktopPoolArr;
        }

        public void setDesktopPoolArr(UUID[] desktopPoolArr) {
            this.desktopPoolArr = desktopPoolArr;
        }

        public UUID getExtStorageId() {
            return extStorageId;
        }

        public void setExtStorageId(UUID extStorageId) {
            this.extStorageId = extStorageId;
        }

        public Integer getBackupDuration() {
            return backupDuration;
        }

        public void setBackupDuration(Integer backupDuration) {
            this.backupDuration = backupDuration;
        }

        public Boolean getAllowCancel() {
            return allowCancel;
        }

        public void setAllowCancel(Boolean allowCancel) {
            this.allowCancel = allowCancel;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public UUID getPlatformId() {
            return platformId;
        }

        public void setPlatformId(UUID platformId) {
            this.platformId = platformId;
        }
    }
}
