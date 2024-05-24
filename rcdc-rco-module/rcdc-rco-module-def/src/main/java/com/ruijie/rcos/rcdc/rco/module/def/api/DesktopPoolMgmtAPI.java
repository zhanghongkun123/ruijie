package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbCreateDeskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopNetworkDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.DeskSpecDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.*;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * 池桌面管理API接口
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月12日
 *
 * @author linke
 */
public interface DesktopPoolMgmtAPI {

    /**
     * 分页查询用户关联的池桌面列表
     *
     * @param userId userId
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<DesktopPoolDTO> pageQueryByUser(UUID userId, PageSearchRequest request) throws BusinessException;

    /**
     * 分页查询池桌面列表
     *
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<DesktopPoolDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 查询桌面池列表
     *
     * @param request 请求对象
     * @return 桌面池列表
     * @throws BusinessException 业务异常
     */
    List<DesktopPoolBasicDTO> listByConditions(ConditionQueryRequest request) throws BusinessException;

    /**
     * 查询桌面池树林里
     *
     * @param request 请求对象
     * @return 数量
     * @throws BusinessException 业务异常
     */
    long countByConditions(ConditionQueryRequest request) throws BusinessException;

    /**
     * 根据用户ID查询池桌面列表
     *
     * @param userId 用户
     * @param userType 用户类型
     * @return List<CbbDesktopPoolDTO>
     * @throws BusinessException 业务异常
     */
    List<CbbDesktopPoolDTO> listDesktopPoolByUserId(UUID userId, IacUserTypeEnum userType) throws BusinessException;

    /**
     * 获取桌面池的总览信息
     *
     * @param poolModel 桌面池模式
     * @return DesktopPoolOverviewDTO
     */
    DesktopPoolOverviewDTO getDesktopPoolOverview(@Nullable CbbDesktopPoolModel poolModel);

    /**
     * 查询池桌面基础信息
     *
     * @param desktopPoolId 桌面池ID
     * @return DesktopPoolBasicDTO
     * @throws BusinessException 业务异常
     */
    DesktopPoolBasicDTO getDesktopPoolBasicById(UUID desktopPoolId) throws BusinessException;

    /**
     * 查询池桌面详细信息
     *
     * @param desktopPoolId 桌面池ID
     * @return DesktopPoolDetailDTO
     * @throws BusinessException 业务异常
     */
    DesktopPoolDetailDTO getDesktopPoolDetail(UUID desktopPoolId) throws BusinessException;

    /**
     * 查询桌面池云桌面策略详情
     *
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    CloudDesktopDetailDTO queryDesktopPoolStrategyDetail(IdWebRequest request) throws BusinessException;

    /**
     * 查询桌面池云桌面网络详情
     *
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    DesktopNetworkDTO queryDesktopPoolNetworkDetail(IdWebRequest request) throws BusinessException;

    /**
     * 更新桌面池策略
     *
     * @param desktopPoolId 桌面池ID
     * @param strategyId 策略ID
     * @throws BusinessException 业务异常
     */
    void updateStrategyId(UUID desktopPoolId, UUID strategyId) throws BusinessException;

    /**
     * 更新桌面池网络
     *
     * @param desktopPoolId 桌面池ID
     * @param networkId 网络ID
     * @throws BusinessException 业务异常
     */
    void updateNetworkId(UUID desktopPoolId, UUID networkId) throws BusinessException;

    /**
     * 更新桌面池镜像
     *
     * @param desktopPoolId 桌面池ID
     * @param imageTemplateId 镜像ID
     * @throws BusinessException 业务异常
     */
    void updateImageTemplateId(UUID desktopPoolId, UUID imageTemplateId) throws BusinessException;

    /**
     * 查询桌面池的非回收站、非创建中、非删除中的云桌面列表
     *
     * @param desktopPoolId 桌面池ID
     * @return 返回
     */
    List<PoolDesktopInfoDTO> listNormalDeskInfoByDesktopPoolId(UUID desktopPoolId);

    /**
     * 查询桌面池的所有云桌面列表
     *
     * @param desktopPoolId 桌面池ID
     * @return 返回
     */
    List<PoolDesktopInfoDTO> listAllDeskInfoByDesktopPoolId(UUID desktopPoolId);

    /**
     * 查询桌面池中已绑定用户且非回收站的云桌面列表
     *
     * @param desktopPoolId 桌面池ID
     * @return 云桌面列表
     */
    List<PoolDesktopInfoDTO> listBindUserDeskInfoByDesktopPoolId(UUID desktopPoolId);

    /**
     * 创建桌面池下云桌面
     *
     * @param request 创建桌面池下云桌面请求
     * @return 创建响应
     * @throws BusinessException 业务异常
     */
    CreateDesktopResponse createDesktop(CreatePoolDesktopRequest request) throws BusinessException;

    /**
     * 创建桌面池下ThirdParty云桌面
     *
     * @param request 创建桌面池下云桌面请求
     * @return 创建响应
     * @throws BusinessException 业务异常
     */
    CreateDesktopResponse createThirdPartyDesktop(CreatePoolComputerDesktopRequest request) throws BusinessException;

    /**
     * 删除桌面池
     *
     * @param desktopPoolId 桌面池id
     * @throws BusinessException ex
     */
    void deleteDesktopPool(UUID desktopPoolId) throws BusinessException;

    /**
     * 修改软件管控策略绑定
     *
     * @param desktopPoolId 桌面池ID
     * @param softwareStrategyId 软件管控策略ID
     * @throws BusinessException 业务异常
     */
    void updateSoftwareStrategy(UUID desktopPoolId, UUID softwareStrategyId) throws BusinessException;

    /**
     * 删除软件管控策略绑定
     *
     * @param desktopPoolId 桌面池ID
     */
    void unbindSoftwareStrategy(UUID desktopPoolId);

    /**
     * 修改UPM策略绑定
     *
     * @param desktopPoolId 桌面池ID
     * @param userProfileStrategyId UPM策略ID
     * @throws BusinessException 业务异常
     */
    void updateUserProfileStrategy(UUID desktopPoolId, UUID userProfileStrategyId) throws BusinessException;

    /**
     * 删除UPM策略绑定
     *
     * @param desktopPoolId 桌面池ID
     */
    void unbindUserProfileStrategy(UUID desktopPoolId);

    /**
     * 查询软件管控策略
     *
     * @param desktopPoolId 桌面池ID
     * @return DesktopPoolSoftwareStrategyDTO
     * @throws BusinessException 业务异常
     */
    DesktopPoolSoftwareStrategyDTO queryDesktopPoolSoftwareStrategy(UUID desktopPoolId) throws BusinessException;

    /**
     * 查询UPM配置策略
     *
     * @param desktopPoolId 桌面池ID
     * @return UPM配置策略
     * @throws BusinessException 异常处理
     */
    DesktopPoolUserProfileStrategyDTO queryDesktopPoolUserProfileStrategy(UUID desktopPoolId) throws BusinessException;

    /**
     * 查询镜像模板
     *
     * @param desktopPoolId 桌面池ID
     * @return DesktopPoolImageTemplateDTO
     * @throws BusinessException 业务异常
     */
    DesktopPoolImageTemplateDTO queryDesktopPoolImageTemplate(UUID desktopPoolId) throws BusinessException;

    /**
     * 计算云桌面名称最大后缀
     *
     * @param desktopPoolId 桌面池ID
     * @return 最大后缀数字
     */
    int getMaxIndexNumWhenAddDesktop(UUID desktopPoolId);

    /**
     * 应用池的云桌面策略到vdi桌面
     *
     * @param desktopPool 桌面池
     * @param desktopInfo vdi桌面
     * @return SyncConfigResultDTO
     */
    SyncConfigResultDTO syncStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo);

    /**
     * 应用池的桌面规格到vdi桌面
     *
     * @param desktopPool 桌面池
     * @param cbbDeskDTO vdi桌面
     * @return SyncConfigResultDTO
     */
    SyncConfigResultDTO syncDeskSpec(CbbDesktopPoolDTO desktopPool, CbbDeskDTO cbbDeskDTO);

    /**
     * 应用池的镜像模板到vdi桌面
     *
     * @param desktopPool 桌面池
     * @param desktopInfo vdi桌面
     * @param taskItem BatchTaskItem
     * @return SyncConfigResultDTO
     */
    SyncConfigResultDTO syncImageTemplate(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo, @Nullable BatchTaskItem taskItem);

    /**
     * 应用池的网络策略到vdi桌面
     *
     * @param desktopPool 桌面池
     * @param desktopInfo vdi桌面
     * @return SyncConfigResultDTO
     */
    SyncConfigResultDTO syncNetworkStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo);

    /**
     * 应用池的软件策略到vdi桌面
     *
     * @param desktopPool 桌面池
     * @param desktopInfo vdi桌面
     * @return SyncConfigResultDTO
     */
    SyncConfigResultDTO syncSoftwareStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo);

    /**
     * 应用池的软件UPM策略到vdi桌面
     *
     * @param desktopPool 桌面池
     * @param desktopInfo vdi桌面
     * @return SyncConfigResultDTO
     */
    SyncConfigResultDTO syncUserProfileStrategy(CbbDesktopPoolDTO desktopPool, CbbDeskDTO desktopInfo);

    /**
     * 保存桌面池配置信息
     *
     * @param desktopPoolConfigDTO 配置信息
     */
    void saveDesktopPoolConfig(DesktopPoolConfigDTO desktopPoolConfigDTO);

    /**
     * 根据桌面池ID获取配置信息
     *
     * @param desktopPoolId 桌面池ID
     * @return DesktopPoolConfigDTO 配置信息
     */
    DesktopPoolConfigDTO getDesktopPoolConfig(UUID desktopPoolId);

    /**
     * 检查桌面池状态是否可用
     *
     * @param desktopPoolId 桌面池ID
     * @throws BusinessException ex
     */
    void checkDesktopPoolStatusAvailable(UUID desktopPoolId) throws BusinessException;

    /**
     * 检查桌面池维护模式
     *
     * @param desktopPoolId 桌面池ID
     * @throws BusinessException ex
     */
    void checkDesktopPoolMaintenanceReady(UUID desktopPoolId) throws BusinessException;

    /**
     * 分页查询AD域组关联的池桌面列表
     *
     * @param adGroupId adGroupId
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<DesktopPoolDTO> pageQueryByAdGroup(UUID adGroupId, PageSearchRequest request) throws BusinessException;

    /**
     * 根据桌面ID列表和池类型查询，按桌面池ID和用户ID进行分组统计，是否有用户在一个池里桌面数量>1
     * @param desktopIdList 桌面ID列表
     * @param poolModel     池类型
     * @return true 存在有用户在一个池里桌面数量>1
     */
    boolean existUserBindMoreDesktop(List<UUID> desktopIdList, CbbDesktopPoolModel poolModel);

    /**
     * 根据镜像ID查询是否有关联的桌面池，有就抛出错误
     *
     * @param imageTemplateId 镜像ID
     * @throws BusinessException 业务异常
     */
    void isExistRelateDesktopPoolByImageIdThrowEx(UUID imageTemplateId) throws BusinessException;

    /**
     * 创建桌面池
     *
     * @param cbbCreateDeskPoolDTO 桌面池创建Request
     * @throws BusinessException 业务异常
     */
    void createDesktopPool(CbbCreateDeskPoolDTO cbbCreateDeskPoolDTO) throws BusinessException;

    /**
     * 编辑桌面池规格配置
     *
     * @param editPoolDeskSpecRequest request
     * @throws BusinessException 业务异常
     */
    void updateDeskSpec(EditPoolDeskSpecRequest editPoolDeskSpecRequest) throws BusinessException;

    /**
     * 获取桌面池规格配置详情
     *
     * @param id 桌面池ID
     * @return DeskSpecDetailDTO
     * @throws BusinessException 业务异常
     */
    DeskSpecDetailDTO getDesktopPoolSpecDetail(UUID id) throws BusinessException;

    /**
     * 获取桌面池规格配置
     *
     * @param id 桌面池ID
     * @return DeskSpecDetailDTO
     * @throws BusinessException 业务异常
     */
    CbbDeskSpecDTO getDesktopPoolCbbDeskSpec(UUID id) throws BusinessException;

    /**
     * 编辑桌面池及其相关配置
     *
     * @param updateDesktopPoolRequest updateDesktopPoolRequest
     * @throws BusinessException 业务异常
     */
    void updateDesktopPoolWithConfig(UpdateDesktopPoolRequest updateDesktopPoolRequest) throws BusinessException;

    /**
     * 编辑桌面池不涉及配置信息
     *
     * @param desktopPoolDTO desktopPoolDTO
     * @throws BusinessException 业务异常
     */
    void updateDesktopPoolWithoutConfig(CbbDesktopPoolDTO desktopPoolDTO) throws BusinessException;

    /**
     * 编辑桌面池负载均衡
     *
     * @param updateLoadBalanceRequest updateLoadBalanceRequest
     * @param desktopPoolDTO desktopPoolDTO
     * @throws BusinessException 业务异常
     */
    void updateLoadBalance(UpdateLoadBalanceRequest updateLoadBalanceRequest, CbbDesktopPoolDTO desktopPoolDTO) throws BusinessException;

    /**
     * 从数据库将桌面池删除
     *
     * @param id 桌面池ID
     * @throws BusinessException 业务异常
     */
    void deleteDesktopPoolFromDb(UUID id) throws BusinessException;

}
