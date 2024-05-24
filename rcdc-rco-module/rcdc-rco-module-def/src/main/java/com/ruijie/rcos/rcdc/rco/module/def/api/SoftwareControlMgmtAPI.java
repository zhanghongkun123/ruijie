package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SoftwareStrategyRelatedWebRequest;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareGroupTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.enums.SoftwareRelationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.request.SoftwareStrategyBindRelationRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月21日
 *
 * @author lihengjing
 */
public interface SoftwareControlMgmtAPI {


    /**
     * 默认软件分组id
     */
    UUID DEFAULT_SOFTWARE_GROUP_ID = UUID.fromString("138bf952-880b-46cd-a0ea-71ea81501706");

    /**
     * 软件策略重名检查 （允许修改策略名称，即编辑时自身名称重名不检测）
     *
     * @param id   软件策略ID
     * @param name 软件策略名称
     * @return 是否重名
     */
    Boolean checkSoftwareStrategyNameDuplication(UUID id, String name);

    /**
     * 软件分组同类型重名检查 （允许修改分组名称，即编辑时自身名称重名不检测）
     *
     * @param id        软件分组ID
     * @param name      软件分组名称
     * @param groupType 软件分组类型 内置、自定义软件
     * @return 是否重名
     */
    Boolean checkSoftwareGroupNameDuplication(UUID id, String name, SoftwareGroupTypeEnum groupType);

    /**
     * 软件同类型重名检查
     *
     * @param name 软件名称
     * @return 是否重名
     */
    SoftwareDTO checkSoftwareNameDuplication(String name);

    /**
     * 软件同类型重名检查
     *
     * @param id id
     * @param name 软件名称
     * @return 是否重名
     */
    Boolean checkSoftwareNameDuplication(UUID id, String name);

    /**
     * 软件同类型重名检查
     *
     * @param md5 软件md5值
     * @return 是否重名
     */
    SoftwareDTO checkSoftwareMd5Duplication(String md5);

    /**
     * 创建软件策略
     *
     * @param softwareStrategyDTO 软件策略对象
     * @throws BusinessException 异常抛出
     */
    void createSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException;

    /**
     * 创建软件分组
     * @throws BusinessException 异常
     * @param softwareGroupDTO 软件分组对象
     */
    void createSoftwareGroup(SoftwareGroupDTO softwareGroupDTO) throws BusinessException;

    /**
     * 创建软件
     *
     * @param softwareDTO 软件对象
     */
    void createSoftware(SoftwareDTO softwareDTO);

    /**
     * 添加策略使用的绑定关系
     *
     * @param request SoftwareStrategyBindRelationRequest
     */
    void bindRelation(SoftwareStrategyBindRelationRequest request);

    /**
     * 编辑策略使用对象的绑定关系
     *
     * @param relationType  对象类型
     * @param relationId    对象ID
     * @param newStrategyId 新的软件管控策略
     */
    void updateRelationBindStrategy(SoftwareRelationTypeEnum relationType, UUID relationId, UUID newStrategyId);

    /**
     * 删除策略使用对象的绑定关系
     *
     * @param relationType 对象类型
     * @param relationId   对象ID
     */
    void deleteBindRelation(SoftwareRelationTypeEnum relationType, UUID relationId);

    /**
     * 根据使用的对象查询软件管控策略信息
     *
     * @param relationType 使用的对象类型SoftwareRelationTypeEnum
     * @param relationId   使用的对象ID
     * @return SoftwareStrategyDTO
     * @throws BusinessException 业务异常
     */
    SoftwareStrategyDTO getStrategyByRelation(SoftwareRelationTypeEnum relationType, UUID relationId) throws BusinessException;

    /**
     * 通过软件策略Id查找软件策略
     *
     * @param id 软件策略Id
     * @return 软件策略对象
     * @throws BusinessException 异常抛出
     */
    SoftwareStrategyDTO findSoftwareStrategyWrapperById(UUID id) throws BusinessException;

    /**
     * 通过软件策略Id查找软件策略
     *
     * @param id 软件策略Id
     * @return 软件策略对象
     * @throws BusinessException 异常抛出
     */
    SoftwareStrategyDTO findSoftwareStrategyById(UUID id) throws BusinessException;

    /**
     * 通过软件分组Id查找软件分组
     *
     * @param id 软件分组Id
     * @return 软件分组对象
     * @throws BusinessException 异常抛出
     */
    SoftwareGroupDTO findSoftwareGroupById(UUID id) throws BusinessException;

    /**
     * 通过软件Id查找软件
     *
     * @param id 软件Id
     * @return 软件对象
     * @throws BusinessException 异常抛出
     */
    SoftwareDTO findSoftwareById(UUID id) throws BusinessException;

    /**
     * 通过软件Id查找软件
     *
     * @param ids 软件Id
     * @return 软件对象
     * @throws BusinessException 异常抛出
     */
    List<SoftwareDTO> findSoftwareByIdIn(Iterable<UUID> ids) throws BusinessException;

    /**
     * 编辑软件策略
     *
     * @param softwareStrategyDTO 软件策略对象
     * @throws BusinessException 异常抛出
     */
    void editSoftwareStrategy(SoftwareStrategyDTO softwareStrategyDTO) throws BusinessException;

    /**
     * 编辑软件分组
     *
     * @param softwareGroupDTO 软件分组对象
     * @throws BusinessException 异常抛出
     */
    void editSoftwareGroup(SoftwareGroupDTO softwareGroupDTO) throws BusinessException;

    /**
     * 编辑软件
     *
     * @param softwareDTO 软件对象
     * @throws BusinessException 异常抛出
     */
    void editSoftware(SoftwareDTO softwareDTO) throws BusinessException;

    /**
     * 通过软件策略Id删除软件策略
     *
     * @param id 软件策略Id
     * @throws BusinessException 异常抛出
     */
    void deleteSoftwareStrategy(UUID id) throws BusinessException;

    /**
     * 通过软件分组Id删除软件分组
     *
     * @param id 软件分组Id
     * @throws BusinessException 异常抛出
     */
    void deleteSoftwareGroup(UUID id) throws BusinessException;

    /**
     * 通过软件Id删除软件
     *
     * @param id 软件Id
     * @throws BusinessException 异常抛出
     */
    void deleteSoftware(UUID id) throws BusinessException;

    /**
     * 删除软件策略关联的软件
     *
     * @param strategyId 策略ID
     * @param softwareIds 软件ID
     * @throws BusinessException 异常抛出
     */
    void deleteSoftwareFromSoftwareStrategyDetail(UUID strategyId, List<UUID> softwareIds) throws BusinessException;

    /**
     * 获取所有软件分组
     *
     * @return 软件分组集合
     * @throws BusinessException 异常抛出
     */
    List<SoftwareGroupDTO> findAllSoftwareGroup() throws BusinessException;

    /**
     * 获取所有软件,用于web展示，故过滤目录下的文件
     *
     * @return 软件集合
     * @throws BusinessException 异常抛出
     */
    List<SoftwareDTO> findAllSoftwareForWeb() throws BusinessException;

    /**
     * 获取所有软件,用于web展示，故过滤目录下的文件
     *
     * @return 软件集合
     * @throws BusinessException 异常抛出
     */
    List<SoftwareDTO> findAllSoftwareForGT() throws BusinessException;


    /**
     * 获取软件组ID 不存在则创建
     * @param softwareGroupDTO 软件组对象
     * @return 软件组ID
     */
    UUID getSoftwareGroupIdIfNotExistCreate(SoftwareGroupDTO softwareGroupDTO);

    /**
     * 是否有正在导入软件信息任务
     * @throws BusinessException 异常抛出
     */
    void isImportingSoftware() throws BusinessException;

    /**
     * 开始增加软件、软件组 任务
     *
     * @throws BusinessException BusinessException
     */
    void startAddSoftwareData() throws BusinessException;

    /**
     * 结束增加软件、软件组 任务
     *
     * @throws BusinessException BusinessException
     */
    void finishAddSoftwareData();

    /**
     * @param idList        软件id 数组
     * @param sourceGroupId 源软件组
     * @param targetGroupId 目标软件组
     * @throws BusinessException 抛出异常
     */
    void moveSoftware(List<UUID> idList, UUID sourceGroupId, UUID targetGroupId) throws BusinessException;


    /**
     * 根据策略获取当前策略下所有的软件清单
     *
     * @param strategyId 策略id
     * @return 返回软件列表
     */
    List<SoftwareDTO> findAllByStrategyId(UUID strategyId);

    /**
     * @param softwareGroupId 软件组id
     * @return
     */
    void updateSoftwareGroupNotNotifyDesk(UUID softwareGroupId);

    /**
     * 批量添加软件
     *
     * @param softwareIdList         软件id
     * @param softwareStrategyIdList 软件策略id
     * @throws BusinessException 业务异常
     */
    void addSoftwareStrategyRelation(Set<UUID> softwareIdList, Set<UUID> softwareStrategyIdList) throws BusinessException;

    /**
     * * 分页查询
     *
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<SoftRelatedSoftStrategyDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * * 分页查询
     *
     * @param request 页面请求
     * @param softwareId 软件id
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<SoftRelatedSoftStrategyRelatedDTO> pageQueryForRelated(PageSearchRequest request, @Nullable UUID softwareId)
            throws BusinessException;


    /**
     * 根据软件分组ids返回分组列表
     * @param groupIdList 分组列表
     * @return groupIdList SoftwareGroupDTO
     */
    List<SoftwareGroupDTO> findSoftwareGroupByIdIn(List<UUID> groupIdList);

    /**
     * 通过软件策略Id查找软件策略
     *
     * @param softwareStrategyIdList 软件策略Id
     * @return 软件策略对象
     * @throws BusinessException 异常抛出
     */
    List<SoftwareStrategyDTO> findSoftwareStrategyByIdIn(Iterable<UUID> softwareStrategyIdList);

    /**
     *
     * @param request search request
     * @return view page
     */
    DefaultPageResponse<SoftwareDTO> softwarePageQuery(PageSearchRequest request);

    /**
     *
     * @param request search request
     * @return view page
     */
    DefaultPageResponse<SoftwareStrategyRelatedSoftwareDTO> softwareStrategyRelatedSoftwarePageQuery(PageSearchRequest request);


    /**
     * 获取软件白名单全局总开关
     * @return boolean
     */
    Boolean getGlobalSoftwareStrategy();

    /**
     * 查询软件个数
     * @return 软件总数
     */
    long count();

}
