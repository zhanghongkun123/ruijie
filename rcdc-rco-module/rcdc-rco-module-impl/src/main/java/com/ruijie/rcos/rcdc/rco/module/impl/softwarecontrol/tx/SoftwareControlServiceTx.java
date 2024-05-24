package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.tx;

import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareRelationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftwareStrategyBindRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareStrategyDetailEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoSoftwareStrategyEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 软件管控管理事务接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/2
 *
 * @author wuShengQiang
 */
public interface SoftwareControlServiceTx {

    /**
     * 创建软件策略(事务操作)
     * 
     * @param softwareStrategyDTO 软件策略对象
     * @throws BusinessException 业务异常
     */
    void createSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException;

    /**
     * 编辑软件策略(事务操作)
     * 
     * @param softwareStrategyDTO 软件策略对象
     * @throws BusinessException 业务异常
     */
    void editSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException;

    /**
     * 通过软件策略Id删除软件策略(事务操作)
     *
     * @param entity 软件策略
     * @throws BusinessException 异常抛出
     */
    void deleteSoftwareStrategy(RcoSoftwareStrategyEntity entity) throws BusinessException;

    /**
     * 通过软件分组Id删除软件分组(事务操作)
     *
     * @param entity 软件分组Id
     * @throws BusinessException 异常抛出
     */
    void deleteSoftwareGroup(RcoSoftwareGroupEntity entity) throws BusinessException;

    /**
     * 通过软件Id删除软件(事务操作)
     *
     * @param id 软件Id
     * @throws BusinessException 异常抛出
     */
    void deleteSoftware(UUID id) throws BusinessException;

    /**
     * 删除软件策略关联的软件(事务操作)
     *
     * @param strategyId 策略ID
     * @param softwareId 软件ID
     * @throws BusinessException 异常抛出
     */
    void deleteSoftwareFromSoftwareStrategyDetail(UUID strategyId, UUID softwareId) throws BusinessException;

    /**
     * 获取软件组ID 不存在则创建(事务操作)
     * @param softwareGroupDTO 软件组对象
     * @return 软件组ID
     */
    UUID getSoftwareGroupIdIfNotExistCreate(SoftwareGroupDTO softwareGroupDTO);

    /**
     * 添加策略使用对象的绑定关系
     *
     * @param request SoftwareStrategyBindRelationRequest
     */
    void bindRelation(SoftwareStrategyBindRelationRequest request);

    /**
     * 编辑策略使用对象的绑定关系
     * @param relationType 对象类型
     * @param relationId 对象ID
     * @param newStrategyId 新的软件管控策略
     */
    void updateRelationBindStrategy(SoftwareRelationTypeEnum relationType, UUID relationId, UUID newStrategyId);

    /**
     * 删除策略使用对象的绑定关系
     * @param relationType 对象类型
     * @param relationId 对象ID
     */
    void deleteBindRelation(SoftwareRelationTypeEnum relationType, UUID relationId);

    /**
     * 移动软件
     * @param idList 软件id数组
     * @param targetGroupId 目标软件组
     * @throws BusinessException  抛出异常
     */
    void moveSoftware(List<UUID> idList, UUID targetGroupId) throws BusinessException;

    /**
     * 添加软件到软件策略
     * @param strategyId strategyId
     * @param softwareIds softwareIds
     *
     */
    void batchAddSoftwareStrategyDetail(UUID strategyId, Set<UUID> softwareIds);
}
