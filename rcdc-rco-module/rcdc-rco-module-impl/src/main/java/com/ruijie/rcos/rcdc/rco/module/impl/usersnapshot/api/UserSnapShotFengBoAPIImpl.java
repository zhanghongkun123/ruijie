package com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserSnapShotFengBoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.UserSnapshotHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilderFactory;
import com.ruijie.rcos.sk.base.batch.InternalBatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 *
 * Description:  用户自助快照 API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月27日
 *
 * @author liusd
 */
public class UserSnapShotFengBoAPIImpl implements UserSnapShotFengBoAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSnapShotFengBoAPIImpl.class);

    @Autowired
    private BatchTaskBuilderFactory batchTaskBuilderFactory;

    @Autowired
    private UserSnapshotHelper userSnapshotFengBoHelper;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Override
    public UserSnapshotPageQueryResponse list(UserSnapshotListRequest userSnapshotListRequest) throws BusinessException {
        Assert.notNull(userSnapshotListRequest, "userSnapshotListRequest must not be null");
        LOGGER.info("用户快照列表查询，userSnapshotListRequest：{}", JSON.toJSONString(userSnapshotListRequest));
        UserSnapshotListDTO userSnapshotListDTO = new UserSnapshotListDTO();
        BeanUtils.copyProperties(userSnapshotListRequest, userSnapshotListDTO);
        PageQueryResponse<CbbDeskSnapshotDTO> pageQueryResponse = userSnapshotFengBoHelper.list(userSnapshotListDTO);
        CloudDesktopDetailDTO cloudDesktopDetailDTO =
                userDesktopMgmtAPI.getDesktopDetailById(userSnapshotListRequest.getDeskId());
        UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO> response = new UserSnapshotPageQueryResponse<>();
        response.setTotal(pageQueryResponse.getTotal());
        response.setItemArr(pageQueryResponse.getItemArr());
        response.setDesktopState(cloudDesktopDetailDTO.getDesktopState());
        return response;
    }

    @Override
    public UserSnapshotResponse create(UserSnapshotCreateRequest userSnapshotCreateRequest) throws BusinessException {
        Assert.notNull(userSnapshotCreateRequest, "userSnapshotCreateRequest must not be null");
        LOGGER.info("用户快照创建，userSnapshotCreateRequest：{}", JSON.toJSONString(userSnapshotCreateRequest));
        UserSnapshotCreateDTO userSnapshotCreateDTO = new UserSnapshotCreateDTO();
        BeanUtils.copyProperties(userSnapshotCreateRequest, userSnapshotCreateDTO);

        BatchTaskBuilder taskBuilder = this.getBatchTaskBuilder(userSnapshotCreateRequest.getUserId());
        return userSnapshotFengBoHelper.create(userSnapshotCreateDTO, taskBuilder);
    }

    @Override
    public UserSnapshotResponse delete(UserSnapshotDeleteRequest userSnapshotDeleteRequest) throws BusinessException {
        Assert.notNull(userSnapshotDeleteRequest, "userSnapshotDeleteRequest must not be null");
        LOGGER.info("用户快照创建，userSnapshotDeleteRequest：{}", JSON.toJSONString(userSnapshotDeleteRequest));
        UserSnapshotDeleteDTO userSnapshotDeleteDTO = new UserSnapshotDeleteDTO();
        BeanUtils.copyProperties(userSnapshotDeleteRequest, userSnapshotDeleteDTO);

        BatchTaskBuilder taskBuilder = this.getBatchTaskBuilder(userSnapshotDeleteRequest.getUserId());
        return userSnapshotFengBoHelper.delete(userSnapshotDeleteDTO, taskBuilder);
    }

    @Override
    public Boolean getSnapshotSwitch(UserSnapshotSwitchRequest userSnapshotSwitchRequest) throws BusinessException {
        Assert.notNull(userSnapshotSwitchRequest, "userSnapshotSwitchRequest must not be null");
        LOGGER.info("用户快照开关，userSnapshotSwitchRequest：{}", JSON.toJSONString(userSnapshotSwitchRequest));
        UserSnapshotSwitchDTO userSnapshotSwitchDTO = new UserSnapshotSwitchDTO();
        BeanUtils.copyProperties(userSnapshotSwitchRequest, userSnapshotSwitchDTO);
        CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(userSnapshotSwitchDTO.getDeskId());
        if (desktopDetailDTO == null) {
            LOGGER.warn("用户快照开关查询失败，找不到对应桌面信息，userSnapshotSwitchRequest：{}", JSON.toJSONString(userSnapshotSwitchRequest));
            return Boolean.FALSE;
        }

        if (!StringUtils.equals(desktopDetailDTO.getDesktopPoolType(), DesktopPoolType.COMMON.name())) {
            LOGGER.warn("用户快照开关：查询到对应桌面为非普通桌面，不能进行自助快照开启，userSnapshotSwitchRequest：{}", //
                    JSON.toJSONString(userSnapshotSwitchRequest));
            return Boolean.FALSE;
        }

        CbbDeskStrategyVDIDTO strategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(desktopDetailDTO.getDesktopStrategyId());
        return BooleanUtils.toBoolean(strategyDTO.getEnableUserSnapshot() != null && strategyDTO.getEnableUserSnapshot());
    }

    @Override
    public UserSnapshotResponse revert(UserSnapshotRevertRequest userSnapshotRevertRequest) throws BusinessException {
        Assert.notNull(userSnapshotRevertRequest, "userSnapshotRevertRequest must not be null");
        LOGGER.info("用户快照恢复，userSnapshotRevertRequest：{}", JSON.toJSONString(userSnapshotRevertRequest));
        UserSnapshotRevertDTO userSnapshotRevertDTO = new UserSnapshotRevertDTO();
        BeanUtils.copyProperties(userSnapshotRevertRequest, userSnapshotRevertDTO);

        BatchTaskBuilder taskBuilder = this.getBatchTaskBuilder(userSnapshotRevertRequest.getUserId());
        return userSnapshotFengBoHelper.revert(userSnapshotRevertDTO, taskBuilder);
    }

    private BatchTaskBuilder getBatchTaskBuilder(UUID userId) {
        InternalBatchTaskBuilder builder = (InternalBatchTaskBuilder) batchTaskBuilderFactory.create();
        builder.setIdentityId(String.valueOf(userId));
        return builder;
    }
}
