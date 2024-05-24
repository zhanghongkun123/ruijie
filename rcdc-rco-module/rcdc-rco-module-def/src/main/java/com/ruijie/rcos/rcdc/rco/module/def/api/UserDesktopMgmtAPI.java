package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserInfoDTO;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbChangeDeskNicMacDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbEditDeskPwdConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.vm.VmGraphicsDTO;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.QueryPlatformTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.VdiDesktopDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.DeskSpecDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.CompensateSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.GuestToolForDiskStateTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;


/**
 * 云桌面管理API接口
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public interface UserDesktopMgmtAPI {

    /**
     * * 分页查询云桌面
     *
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */

    DefaultPageResponse<CloudDesktopDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 分页查询云桌面
     *
     * @param request 请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<CloudDesktopDTO> pageQuery(PageQueryRequest request) throws BusinessException;

    /**
     * 查询绑定临时权限的云桌面列表
     *
     * @param permissionId 临时权限Id
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<CloudDesktopDTO> pageQueryInDesktopTempPermission(UUID permissionId, PageSearchRequest request) throws BusinessException;

    /**
     * * 分页查询云桌面并校验绑定信息
     *
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<CloudDesktopDTO> pageQueryWithAssignment(DesktopAssignmentPageSearchRequest request) throws BusinessException;

    /**
     * *根据指定UUID获取云桌面DTO对象
     *
     * @param desktopId 页面查询请求参数：uuid
     * @return reposne CloudDesktopVO 对象
     * @throws BusinessException 业务异常
     */

    CloudDesktopDetailDTO getDesktopDetailById(UUID desktopId) throws BusinessException;

    /**
     * * 创建云桌面,创建用户时调用
     *
     * @param request 创建云桌面请求参数
     * @return reposne 执行状态：成功、失败（消息）
     * @throws BusinessException 业务异常
     */
    CreateDesktopResponse create(CreateCloudDesktopRequest request) throws BusinessException;

    /**
     * * 创建第三方云桌面,创建PC终端时调用
     *
     * @param request 创建云桌面请求参数
     * @throws BusinessException 业务异常
     */
    void createThirdParty(CreateThirdPartyDesktopRequest request) throws BusinessException;

    /**
     * * 创建IDV云桌面
     *
     * @param cloudDesktopDTO 创建云桌面请求参数
     * @return reposne 响应数据
     * @throws BusinessException 业务异常
     */
    CreateDesktopResponse createIDV(IDVCloudDesktopDTO cloudDesktopDTO) throws BusinessException;

    /**
     * * 创建IDV云桌面
     *
     * @param cloudDesktopDTO 创建云桌面请求参数
     * @return reposne 响应数据
     * @throws BusinessException 业务异常
     */
    CreateDesktopResponse createVOI(IDVCloudDesktopDTO cloudDesktopDTO) throws BusinessException;

    /**
     * 把桌面移入回收站中
     *
     * @param moveDesktopToRecycleBinRequest moveDesktopToRecycleBinRequest
     * @throws BusinessException 业务异常
     */
    void moveDesktopToRecycleBin(MoveDesktopToRecycleBinRequest moveDesktopToRecycleBinRequest) throws BusinessException;

    /**
     * 彻底删除桌面
     *
     * @param desktopId 桌面ID
     * @throws BusinessException 业务异常
     */
    void forceDeleteDesktop(UUID desktopId) throws BusinessException;

    /**
     * 删除桌面关联的应用ISO挂载信息
     *
     * @param desktopId desktopId
     */
    void deleteDesktopAppConfig(UUID desktopId);

    /**
     * *批量修改云桌面策略
     *
     * @param request 页面请求参数：uuid
     * @throws BusinessException 业务异常
     */
    void configStrategy(EditDesktopStrategyRequest request) throws BusinessException;

    /**
     * *批量修改网络策略
     *
     * @param request 页面请求参数：uuids
     * @throws BusinessException 业务异常
     */
    void configNetwork(EditDesktopNetworkRequest request) throws BusinessException;

    /**
     * 获取用户VDI云桌面配置
     *
     * @param request request
     * @return 返回云桌面配置信息
     * @throws BusinessException 业务异常
     */

    VdiDesktopDetailResponse getVisitorVdiDesktopConfig(IdRequest request) throws BusinessException;

    /**
     * 获取云桌面策略详情
     *
     * @param request 请求参数
     * @return 云桌面策略详情
     * @throws BusinessException 业务异常
     */
    CloudDesktopDetailDTO getDesktopStrategyByDesktopId(IdRequest request) throws BusinessException;

    /**
     * 获取云桌面网络策略详情
     *
     * @param request 请求参数
     * @return 网络策略详情
     * @throws BusinessException 业务异常
     */
    DesktopNetworkDTO getNetworkByDesktopId(IdRequest request) throws BusinessException;

    /**
     * *编辑云桌面角色
     *
     * @param request 页面请求参数
     * @throws BusinessException 业务异常
     */

    void editDesktopRole(EditDesktopRoleRequest request) throws BusinessException;


    /**
     * 查询指定用户组列表下的桌面状态数统计信息
     *
     * @param uuidArr 用户组id列表
     * @return com.ruijie.rcos.rcdc.user.module.def.api.response.ListDesktopNumByUserGroupResponse
     * @throws BusinessException 业务异常
     * @author hli on 2019-08-08
     */

    List<DesktopStateNumDTO> listDesktopNumByUserGroup(UUID[] uuidArr) throws BusinessException;

    /**
     * 查询所有在线云桌面的信息
     *
     * @return 在线云桌面信息列表
     * @throws BusinessException 业务异常
     */

    CloudDesktopDetailListDTO getAllRunningDesktopDetail() throws BusinessException;

    /**
     * 查询所有在线且需要发送全局水印配置的桌面列表
     *
     * @return 在线云桌面信息列表
     * @throws BusinessException 业务异常
     */
    List<CloudDesktopDetailDTO> getSendGlobalWatermarkDesktopList() throws BusinessException;

    /**
     * 根据用户组、终端组ID 桌面池ID 查询所有云桌面ID
     *
     * @param request request信息
     * @return DtoResponse
     */
    List<UUID> getDesktopIdArr(GetAllByGroupIdAndIsDeleteRequest request);

    /**
     * 获取用户桌面数(不包括池桌面)
     *
     * @param userId userId
     * @return 桌面数
     */
    long getUserVdiDesktopNum(UUID userId);

    /**
     * 获取创建中的云桌面个数
     *
     * @param userId 用户id
     * @return 返回桌面个数
     */
    int getCreatingDesktopNum(UUID userId);

    /**
     * 用户是否已登录
     *
     * @param userId userId
     * @return true 已登录
     * @throws BusinessException 业务异常
     */
    boolean isUserHasLogin(UUID userId) throws BusinessException;

    /**
     * 查询用户绑定的VDI云桌面列表信息，访客用户只有一条记录
     *
     * @param cbbUserDetailDTO cbbUserDetailDTO
     * @param terminalId terminalId
     * @return List<CloudDesktopDTO>
     */
    List<CloudDesktopDTO> listUserVDIDesktop(IacUserDetailDTO cbbUserDetailDTO, String terminalId);

    /**
     * 查询用户绑定的VDI云桌面列表信息，访客用户只有一条记录
     *
     * @param request request
     * @return List<QueryDesktopItemDTO>
     */
    List<QueryDesktopItemDTO> listUserVDIDesktop(ListUserVdiDesktopRequest request);


    /**
     * *根据指定用户UUID获取所有云桌面DTO
     *
     * @param userId 页面查询请求参数：uuid
     * @return reposne List<CloudDesktopDTO> 云桌面DTO集合
     */

    List<CloudDesktopDTO> getAllDesktopByUserId(UUID userId);

    /**
     * 根据用户Id集合查询运行的桌面信息
     * @param userIdList 用户ID
     * @return 回收站云桌面
     */
    List<CloudDesktopDTO> getAllRunningVDIDesktopByUserIdList(List<UUID> userIdList);

    /**
     * *根据指定桌面集合 获取所有所有运行中桌面
     *
     * @param uuidList 页面查询请求参数：uuid
     * @return reposne List<CloudDesktopDTO> 云桌面DTO集合
     */

    List<CloudDesktopDTO> getAllRunningVDIDesktopByDeskIdIn(List<UUID> uuidList);



    /**
     * 查询在回收站的云桌面
     *
     * @param uuidList 页面查询请求参数：uuid
     * @return reposne List<CloudDesktopDTO> 云桌面DTO集合
     */
    List<CloudDesktopDTO> getAllDesktopByDeskIdInDeskStateInRecycleBin(List<UUID> uuidList);


    /**
     * 根据云桌面Id和云桌面类型删除云桌面
     *
     * @param desktopId 桌面id
     * @param deskType 桌面类型
     * @throws BusinessException 业务异常
     */
    void deleteDesktop(UUID desktopId, CbbCloudDeskType deskType) throws BusinessException;

    /**
     * 编辑云桌面镜像
     *
     * @param cbbDesktopImageUpdateDTO 云桌面ID、镜像id
     * @throws BusinessException 业务异常
     */
    void updateDesktopImage(CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO) throws BusinessException;

    /**
     * 执行云桌面待变更镜像模板
     *
     * @param desktopImageUpdateDTO 请求参数
     * @return boolean 执行是否成功
     */
    boolean doWaitUpdateDesktopImage(DesktopImageUpdateDTO desktopImageUpdateDTO);

    /**
     * 根据指定云桌面策略UUID获取所有云桌面DTO
     *
     * @param strategyId 页面查询请求参数：uuid
     * @return List<CloudDesktopDTO> List<CloudDesktopDTO> 云桌面DTO集合
     */
    List<CloudDesktopDTO> getAllDesktopByStrategyId(UUID strategyId);

    /**
     * 根据云桌面UUDI列表，查询云桌面DTO
     *
     * @param desktopIdList 云桌面UUID列表
     * @return List<CloudDesktopDTO>
     */
    List<CloudDesktopDTO> listDesktopByDesktopIds(List<UUID> desktopIdList);

    /**
     * 统计获取单个用户组下用户绑定的云桌面个数,包括正在创建的云桌面个数（不包括回收站里的桌面）
     *
     * @param userGroupId 用户id
     * @param deskType VDI/VOI/IDV
     * @return 返回用户桌面个数
     */
    List<CloudDesktopDTO> listUserDesktopByGroupId(UUID userGroupId, UserCloudDeskTypeEnum deskType);

    /**
     * 重置VDI云桌面MAC地址
     *
     * @param cbbChangeDeskNicMacDTO 重置信息
     * @throws BusinessException 业务异常
     */
    void resetDesktopMac(CbbChangeDeskNicMacDTO cbbChangeDeskNicMacDTO) throws BusinessException;

    /**
     * 桌面故障分页
     *
     * @param pageRequest 页面请求
     * @return 请求结果
     * @throws BusinessException ex
     */
    DefaultPageResponse<CloudDesktopDTO> desktopFaultPageQuery(PageSearchRequest pageRequest) throws BusinessException;

    /**
     * 获取云桌面数量
     *
     * @param deskState 桌面状态
     * @return 数量
     */
    Long findCountByDeskState(CbbCloudDeskState deskState);

    /**
     * 获取云桌面总数
     *
     * @return 数量
     */
    Long findCount();

    /**
     * 获取在线使用中云桌面总数
     *
     * @return 数量
     */
    Long findOnlineDesktopCount();


    /**
     * 解除和指定用户绑定的所有云桌面（包括已经放入回收站的云桌面）
     *
     * @param userId 云桌面ID
     */
    void unbindCloudDeskFromUser(UUID userId);

    /**
     * 编辑云桌面软件管控策略
     *
     * @param desktopId 云桌面ID
     * @param softwareStrategyId 软件管控策略ID
     * @throws BusinessException 业务异常
     */
    void updateDesktopSoftwareStrategy(UUID desktopId, UUID softwareStrategyId) throws BusinessException;

    /**
     * 清空用户绑定胖终端镜像下载状态的信息
     *
     * @param userId 用户ID
     */
    void clearDownloadStateInfo(UUID userId);

    /**
     * *根据指定用户UUID获取所有云桌面DTO
     *
     * @param userName 用户名
     * @return reposne List<CloudDesktopDTO> 云桌面DTO集合
     */
    List<CloudDesktopDTO> getAllDesktopByUserName(String userName);

    /**
     * 指定VDI云桌面请求协助接口
     *
     * @param deskId 云桌面Id
     * @throws BusinessException 业务异常
     */
    void requestRemoteAssist(UUID deskId) throws BusinessException;

    /**
     * 指定VDI云桌面取消请求协助接口
     *
     * @param deskId 云桌面Id
     */
    void cancelRemoteAssist(UUID deskId);

    /**
     * 指定VDI云桌面请求协助状态查询接口
     *
     * @param deskId 云桌面Id
     * @return 结果
     */
    boolean queryRemoteAssistStatus(UUID deskId);

    /**
     * 云桌面抢占
     *
     * @param deskId 云桌面ID
     * @param terminalId 终端ID
     */
    void desktopIsRobbed(UUID deskId, String terminalId);

    /**
     * 获取云桌面是否开启系统盘自动扩容
     *
     * @param deskId 云桌面ID
     * @return 是否开启系统盘自动扩容
     */
    Boolean getDeskEnableFullSystemDiskByDeskId(UUID deskId);

    /**
     * 根据指定桌面池UUID获取所有云桌面DTO
     *
     * @param desktopPoolId desktopPoolId
     * @return reposne List<CloudDesktopDTO> 云桌面DTO集合
     */
    List<CloudDesktopDTO> getAllDesktopByDesktopPoolId(UUID desktopPoolId);

    /**
     * 为桌面绑定用户
     *
     * @param request 参数
     * @throws BusinessException 业务异常
     */
    void desktopBindUser(UserDesktopBindUserRequest request) throws BusinessException;

    /**
     * 编辑云桌面用户配置策略
     *
     * @param id 桌面ID
     * @param userProfileStrategyId 用户配置策略ID
     * @throws BusinessException 业务异常
     */
    void updateDesktopUserProfileStrategy(UUID id, UUID userProfileStrategyId) throws BusinessException;

    /**
     * 判断云桌面是否有个人盘
     *
     * @param desktopId 云桌面ID
     * @return 个人盘
     */
    GuestToolForDiskStateTypeEnum checkDesktopWithPersonalDiskState(UUID desktopId);

    /**
     * 获取所有云桌面的ID
     *
     * @return 云桌面ID列表
     */
    List<UUID> findAllDesktopId();

    /**
     * 获取所有IDV云桌面的ID
     *
     * @return IDV云桌面ID列表
     */
    List<UUID> findAllIDVDesktopId();

    /**
     * 补偿桌面断开连接时间
     *
     * @param compensateSessionType 补偿类型
     */
    void compensateConnectClosedTime(CompensateSessionType compensateSessionType);

    /**
     * 根据云桌面ID更新桌面状态
     *
     * @param deskId 桌面ID
     * @param deskState 桌面状态
     * @throws BusinessException 业务异常
     */
    void updateState(UUID deskId, CbbCloudDeskState deskState) throws BusinessException;

    /**
     * 查询用户桌面VNC地址
     *
     * @param deskId 桌面ID
     * @throws BusinessException 业务异常
     * @return VmGraphicsDTO VNC地址信息
     * @throws BusinessException 业务异常
     */

    VmGraphicsDTO queryVncByDeskId(UUID deskId) throws BusinessException;

    /**
     * 是否允许云桌面登录使用
     *
     * @param strategyId  云桌面策略id
     * @param desktopName 云桌面名称
     * @param isPool      是否桌面池
     * @param deskType    桌面类型
     * @return 是否允许
     * @throws BusinessException 业务异常
     * @throws BusinessException 业务异常
     */
    boolean isAllowDesktopLogin(UUID strategyId, String desktopName, Boolean isPool, String deskType) throws BusinessException;


    /**
     * 根据参数统计数量
     *
     * @param countCloudDesktopDTO 参数集
     * @return 数量
     */
    Integer countByCloudDesktop(CountCloudDesktopDTO countCloudDesktopDTO);

    /**
     * 根据终端id查询绑定的云桌面列表
     *
     * @param terminalId rco终端id
     * @return 云桌面列表
     * @throws BusinessException 业务异常
     */
    CloudDesktopDTO findByTerminalId(String terminalId) throws BusinessException;


    /**
     * 根据桌面id列表跟系统类型查询符合的总数
     *
     * @param deskIdList 桌面列表
     * @return 符合的总数
     */
    List<DeskImageRelatedDTO> findByDeskIdIn(List<UUID> deskIdList);

    /**
     * 获取桌面信息，仅查rco业务数据
     *
     * @param desktopId 桌面id
     * @return 用户桌面
     * @throws BusinessException 业务异常
     */
    RcoUserDesktopDTO findByDesktopId(UUID desktopId) throws BusinessException;

    /**
     * * 分页查询云桌面
     *
     * @param request 页面请求
     * @param queryPlatformType 查询平台类型
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */

    DefaultPageResponse<CloudDesktopDTO> pageQuery(PageSearchRequest request, QueryPlatformTypeEnum queryPlatformType) throws BusinessException;

    /**
     * 通过用户id查询
     * @param userId 用户id
     * @return 列表
     */
    List<CloudDesktopDTO> findByUserId(UUID userId);

    /**
     * 查询桌面列表
     *
     * @param request 请求对象
     * @return 桌面池列表
     * @throws BusinessException 业务异常
     */
    List<CloudDesktopDTO> listByConditions(ConditionQueryRequest request) throws BusinessException;

    /**
     * 查询桌面数量
     *
     * @param request 请求对象
     * @return 数量
     * @throws BusinessException 业务异常
     */
    long countByConditions(ConditionQueryRequest request) throws BusinessException;

    /**
     * 取消关联用户
     *
     * @param desktopId 桌面ID
     * @throws BusinessException 业务异常
     */
    void desktopUnBindUser(UUID desktopId) throws BusinessException;

    /**
     * @param deskIdList 桌面id列表
     * @param deskPoolIdList 桌面池id列表
     * @param userIdList 用户id列表
     * @param userGroupIdList 用户组id列表
     * @param platformId 云平台ID
     * @return 满足其中一个条件的桌面信息集合
     */
    List<CloudDesktopDTO> listByDeskIdsOrPoolIdsOrUserIdsOrUserGroupIdsAndPlatformId(List<UUID> deskIdList, List<UUID> deskPoolIdList,
                                                                                     List<UUID> userIdList, List<UUID> userGroupIdList,
                                                                                     UUID platformId);

    /**
     * 编辑云桌面密码显示配置
     *
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    void editDeskRootPwdConfig(CbbEditDeskPwdConfigDTO request) throws BusinessException;

    /**
     * *根据桌面ID获取桌面类型
     *
     * @param deskId 桌面ID
     * @return String ”应用主机“/”云桌面“
     * @throws BusinessException 业务异常
     */
    String getImageUsageByDeskId(@Nullable UUID deskId) throws BusinessException;

    /**
     * 根据镜像id获取所有云桌面信息
     *
     * @param imageTemplateId 镜像id
     * @return 云桌面列表
     * @throws BusinessException 业务异常
     */
    List<CloudDesktopDTO> findAllByImageTemplateId(UUID imageTemplateId) throws BusinessException;

    /**
     * 获取桌面规格配置详情
     *
     * @param deskId 桌面ID
     * @return DeskSpecDetailDTO
     * @throws BusinessException 业务异常
     */
    DeskSpecDetailDTO getDesktopSpecDetail(UUID deskId) throws BusinessException;

    /**
     * 更新第三方云桌面配置的策略ID
     * @param request 请求实体
     * @throws BusinessException 业务异常
     */
    void updateDeskStrategyThirdParty(EditDesktopStrategyRequest request) throws BusinessException;

    /**
     * 删除第三方云桌面
     * @param deskId 桌面id
     * @throws BusinessException 业务异常
     */
    void deleteDesktopThirdParty(UUID deskId) throws BusinessException;

    /**
     * 将桌面数据从数据库中删除
     *
     * @param deskId 桌面ID
     * @throws BusinessException 业务异常
     */
    void deleteDeskFromDb(UUID deskId) throws BusinessException;
}
