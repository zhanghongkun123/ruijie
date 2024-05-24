package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 统一管理推送服务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/17
 *
 * @author TD
 */
public interface UnifiedManageDataService {

    /**
     * 保存统一管理数据
     * @param request 请求
     */
    void saveUnifiedManage(UnifiedManageDataRequest request);

    /**
     * 获取统一管理数据
     *
     * @param request 请求参数
     * @return UnifiedManageDataEntity
     */
    UnifiedManageDataEntity findByRelatedIdAndRelatedType(UnifiedManageDataRequest request);

    /**
     * 根据FunctionKey获取统一管理数据
     *
     * @param relatedType 方法key
     * @return UnifiedManageDataEntity集合
     */
    List<UnifiedManageDataEntity> findByRelatedType(UnifiedManageFunctionKeyEnum relatedType);

    /**
     * 根据管理ID查询数据
     *
     * @param unifiedManageDataId  管理ID
     * @return UnifiedManageDataEntity
     */
    UnifiedManageDataEntity findByUnifiedManageDataId(UUID unifiedManageDataId);

    /**
     * 根据管理ID列表查询数据
     *
     * @param unifiedManageDataIdList 管理ID列表
     * @return 结果集
     */
    List<UnifiedManageDataEntity> findByUnifiedManageDataIdIn(List<UUID> unifiedManageDataIdList);

    /**
     * 删除统一管理数据
     *
     * @param request 请求
     */
    void deleteUnifiedManage(UnifiedManageDataRequest request);

    /**
     * 推送修订的云桌面信息给RCCM
     * @param deskStrategyId 云桌面策略ID
     * @throws BusinessException 业务异常
     */
    void pushSyncUnifiedManage(UUID deskStrategyId) throws BusinessException;

    /**
     * 推送删除的云桌面信息给RCCM
     * @param deskStrategyId 云桌面策略ID
     * @param deskStrategyName 云桌面策略名称
     * @param unifiedManageDataId 统一管理标识
     * @throws BusinessException 业务异常
     */
    void pushDeleteUnifiedManage(UUID deskStrategyId, String deskStrategyName, UUID unifiedManageDataId) throws BusinessException;

    /**
     * 策略创建通知
     * @param deskStrategyId 策略ID
     * @throws BusinessException 业务异常
     */
    void createNotify(UUID deskStrategyId) throws BusinessException;

    /**
     * 策略修订通知
     * @param deskStrategyId 策略ID
     * @throws BusinessException 业务异常
     */
    void updateNotify(UUID deskStrategyId) throws BusinessException;

    /**
     * 策略删除通知
     * @param deskStrategyId 策略ID
     * @param deskStrategyName 云桌面策略名称
     * @throws BusinessException 业务异常
     */
    void deleteNotify(UUID deskStrategyId, String deskStrategyName) throws BusinessException;

    /**
     * 根据关联id和资源类型查找统一管理数据唯一ID
     *
     * @param relatedId 关联id
     * @param type 类型
     * @return 统一管理数据唯一ID
     */
    UUID getUnifiedManageDataId(UUID relatedId, UnifiedManageFunctionKeyEnum type);

    /**
     * 删除镜像和镜像快照信息
     *
     * @param imageId 镜像id
     */
    void deleteImageAndRelateData(UUID imageId);

    /**
     * 判断资源是否存在
     *
     * @param relatedId 关联id
     * @param relatedType      类型
     * @return 是否存在
     */
    boolean existsUnifiedData(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType);

    /**
     * 根据关联ID列表和类型查询
     *
     * @param relatedType 类型
     * @param relatedIdList 关联id列表
     * @return 结果集
     */
    List<UnifiedManageDataEntity> findByRelatedTypeAndRelatedIdIn(UnifiedManageFunctionKeyEnum relatedType , List<UUID> relatedIdList);
}
