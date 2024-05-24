package com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDeskSnapshotDistributeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.est.EstCommonActionService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.EstCommonActionResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description: Est发起的 用户自定义快照功能
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31 11:42
 *
 * @author lihengjing
 */
@Service
public class UserSnapshotEstHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSnapshotEstHelper.class);

    @Autowired
    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    @Autowired
    private EstCommonActionNotifyService estCommonActionNotifyService;

    @Autowired
    private EstCommonActionService estCommonActionService;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    private static final String SORT_FIELD_CREATE_TIME = "createTime";

    private static final String EQ_FIELD_DESK_ID = "deskId";


    /**
     * Est用户获取快照列表
     *
     * @param dispatcherDTO 原始的分发器传输对象
     * @param subAction     真实动作
     * @param data          业务参数
     * @throws BusinessException 业务异常
     */
    public void list(CbbDispatcherDTO dispatcherDTO, String subAction, JSONObject data) throws BusinessException {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");

        EstCommonActionResponse<UserSnapshotPageQueryResponse<CbbDeskSnapshotDTO>> estCommonActionResponse =
                estCommonActionService.snapshotList(subAction, data);
        String result = JSON.toJSONString(estCommonActionResponse, SerializerFeature.WriteDateUseDateFormat);
        estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, result);
    }

    /**
     * Est用户创建快照
     *
     * @param dispatcherDTO 原始的分发器传输对象
     * @param subAction     真实动作
     * @param data          业务参数
     * @param builder       批任务处理器
     * @throws BusinessException 业务异常
     */
    public void create(CbbDispatcherDTO dispatcherDTO, String subAction, JSONObject data, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        Assert.notNull(builder, "builder must not be null");
        EstCommonActionResponse estCommonActionResponse =
                estCommonActionService.create(dispatcherDTO.getTerminalId(), subAction, data, builder);
        String result = JSON.toJSONString(estCommonActionResponse, SerializerFeature.WriteDateUseDateFormat);
        estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, result);
    }


    /**
     * Est用户删除快照
     *
     * @param dispatcherDTO 原始的分发器传输对象
     * @param subAction     真实动作
     * @param data          业务参数
     * @param builder       批处理
     * @throws BusinessException 业务异常
     */
    public void delete(CbbDispatcherDTO dispatcherDTO, String subAction, JSONObject data, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        Assert.notNull(builder, "builder must not be null");

        EstCommonActionResponse estCommonActionResponse =
                estCommonActionService.delete(dispatcherDTO.getTerminalId(), subAction, data, builder);
        String result = JSON.toJSONString(estCommonActionResponse, SerializerFeature.WriteDateUseDateFormat);

        estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, result);
    }

    /**
     * Est用户恢复快照
     *
     * @param dispatcherDTO 原始的分发器传输对象
     * @param subAction     真实动作
     * @param data          业务参数
     * @param builder       批处理
     * @throws BusinessException 业务异常
     */
    public void revert(CbbDispatcherDTO dispatcherDTO, String subAction, JSONObject data, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(dispatcherDTO, "dispatcherDTO is need not null");
        Assert.notNull(subAction, "subAction is need not null");
        Assert.notNull(data, "data is need not null");
        Assert.notNull(builder, "builder must not be null");

        EstCommonActionResponse estCommonActionResponse =
                estCommonActionService.revert(dispatcherDTO.getTerminalId(), subAction, data, builder);
        String result = JSON.toJSONString(estCommonActionResponse, SerializerFeature.WriteDateUseDateFormat);

        estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, result);
    }


    /**
     * @param dispatcherDTO est消息
     * @param subAction est操作
     * @param data data
     * @throws BusinessException 异常
     */
    public void checkUserPwd(CbbDispatcherDTO dispatcherDTO, String subAction, JSONObject data) throws BusinessException {
        Assert.notNull(dispatcherDTO, "dispatcherDTO can not be null");
        Assert.hasText(subAction, "subAction can not be empty");
        Assert.notNull(data, "data can not be null");

        EstCommonActionResponse estCommonActionResponse = estCommonActionService.checkUserPwd(subAction, data);
        String result = JSON.toJSONString(estCommonActionResponse, SerializerFeature.WriteDateUseDateFormat);
        estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, result);
    }

    /**
     * @param dispatcherDTO est消息
     * @param subAction est操作
     * @param data 数据
     * @throws BusinessException 异常
     */
    public void checkNum(CbbDispatcherDTO dispatcherDTO, String subAction, JSONObject data) throws BusinessException {
        Assert.notNull(dispatcherDTO, "dispatcherDTO can not be null");
        Assert.hasText(subAction, "subAction can not be empty");
        Assert.notNull(data, "data can not be null");
        EstCommonActionResponse<CbbDeskSnapshotDistributeDTO> estCommonActionResponse = estCommonActionService.checkNum(subAction, data);
        String result = JSON.toJSONString(estCommonActionResponse, SerializerFeature.WriteDateUseDateFormat);
        estCommonActionNotifyService.responseToEst(dispatcherDTO.getTerminalId(), subAction, result);

    }
}
