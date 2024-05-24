package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ComputerWorkModelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerNetInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerReportSystemInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.response.GetComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.computer.ComputerBaseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.QueryAssistantVersionResponseContent;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/30 15:13
 *
 * @author ketb
 */
public interface ComputerBusinessService {

    /**
     * 上报pc信息
     * @param baseInfoDTO pc信息
     * @throws BusinessException 业务异常
     */
    void saveComputerInfo(ComputerBaseInfoDTO baseInfoDTO) throws BusinessException;

    /**
     * 解除报障
     * @param computerId pc的id
     * @throws BusinessException 业务异常
     */
    void relieveFault(UUID computerId) throws BusinessException;

    /**
     * 查询小助手版本号
     * @param responseContent 请求参数
     */
    void getAssistantVersion(QueryAssistantVersionResponseContent responseContent);

    /**
     * 查询pc状态
     * @param deskId pcID
     * @return 结果
     * @throws BusinessException 业务异常
     */
    ComputerStateEnum getComputerState(UUID deskId) throws BusinessException;

    /**
     * 根据id查询pc
     * @param deskId pcid
     * @return 结果
     */
    ComputerEntity getComputerById(UUID deskId);

    /**
     * 查询pc信息
     * @param terminalId 终端id
     * @return ComputerEntity
     */
    ComputerEntity getComputerByTerminalId(String terminalId);

    /**
     * 更新pc终端系统信息
     * @param terminalId  终端id
     * @param reportSystemInfoDTO 系统信息
     */
    void updateComputerSystemInfo(String terminalId, ComputerReportSystemInfoDTO reportSystemInfoDTO);

    /**
     * 更新网卡信息
     * @param terminalId 终端id
     * @param computerNetInfoDTO computerNetInfoDTO
     */
    void updateComputerNetworkInfo(String terminalId, ComputerNetInfoDTO computerNetInfoDTO);

    /**
     * 获取pc信息
     * @param ip ip
     * @return ComputerEntity
     */
    ComputerEntity findByIp(String ip);

    /**
     * 根据终端组id数组获取列表
     * @param groupIdList groupIdList
     * @return 列表
     */
    List<ComputerEntity> findComputerInfoByGroupIdList(List<UUID> groupIdList);

    /**
     * 根据pc终端id获取列表
     * @param computerIdList computerIdList
     * @return 列表
     */
    List<ComputerEntity> findComputerInfoComputerInfoByIdList(List<UUID> computerIdList);

    /**
     * 统计数量
     * @param computerIdList computerIdList
     * @return int
     */
    int countComputerByIdList(List<UUID> computerIdList);

    /**
     * 查询pc信息
     * @param terminalId 终端id
     * @param ip ip
     * @return ComputerEntity
     */
    ComputerEntity findByTerminalIdOrIp(String terminalId, String ip);

    /**
     * 保存Computer
     * @param computerEntity 终端
     */
    void saveComputer(ComputerEntity computerEntity);

    /**
     * 离线
     * @param computerId computerId
     */
    void offline(UUID computerId);

    /**
     * 更新工作模式
     * @param computerId computerId
     * @param computerWorkModel computerWorkModel
     */
    void updateWorkModel(UUID computerId, @Nullable ComputerWorkModelEnum computerWorkModel);

    /**
     * 获取信息
     * @param computerEntity computerEntity
     * @return GetComputerInfoDTO
     * @throws BusinessException 业务异常
     */
    GetComputerInfoResponse getComputerInfoDTO(ComputerEntity computerEntity) throws BusinessException;

    /**
     * 移除
     * @param computerId computerId
     */
    void removeById(UUID computerId);

    /**
     * 获取在线第三方PC终端
     * @param computerState computerState
     * @param computerType computerType
     * @return list
     */
    List<ComputerEntity> findAllByStatusAndType(ComputerStateEnum computerState, ComputerTypeEnum computerType);

    /**
     * 更新在线
     * @param computerId computerId
     */
    void online(UUID computerId);

    /**
     * 移动分组通知
     * @param entity 实体
     * @throws BusinessException 业务异常
     */
    void moveGroupNotify(ComputerEntity entity) throws BusinessException;


    /**
     * 更新
     * @param entity 实体
     * @param count 次数
     */
    void update(ComputerEntity entity, int count);

    /**
     * 创建
     * @param entity 实体
     */
    void create(ComputerEntity entity);
}
