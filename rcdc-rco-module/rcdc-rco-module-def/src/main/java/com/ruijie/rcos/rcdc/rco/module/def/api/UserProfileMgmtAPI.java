package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfileStrategyStorageTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PathTree;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户配置路径管理API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public interface UserProfileMgmtAPI extends PageQueryAPI<UserProfileStrategyViewDTO> {

    /**
     * 是否有正在导入路径信息任务
     *
     * @throws BusinessException 业务异常
     */
    void isImportingUserProfilePath() throws BusinessException;

    /**
     * 创建路径分组
     *
     * @param userProfilePathGroupDTO 分组对象
     * @throws BusinessException 业务异常
     */
    void createUserProfilePathGroup(UserProfilePathGroupDTO userProfilePathGroupDTO) throws BusinessException;

    /**
     * 编辑路径分组
     *
     * @param userProfilePathGroupDTO 分组对象
     * @throws BusinessException 业务异常
     */
    void editUserProfilePathGroup(UserProfilePathGroupDTO userProfilePathGroupDTO) throws BusinessException;

    /**
     * 删除用户配置分组
     *
     * @param uuid 组id
     * @throws BusinessException 业务异常
     */
    void deleteUserProfilePathGroup(UUID uuid) throws BusinessException;

    /**
     * 获取用户配置分组详情
     *
     * @param id 组id
     * @return 组对象
     * @throws BusinessException 业务异常
     */
    UserProfilePathGroupDTO findUserProfilePathGroupById(UUID id) throws BusinessException;

    /**
     * 组名称重名检查（允许修改分组名称，即编辑时自身名称重名不检测）
     *
     * @param id   组id
     * @param name 组名称
     * @return 是否重名 true:重名 false:未重名
     */
    Boolean checkUserProfilePathGroupNameDuplication(UUID id, String name);

    /**
     * 批量移动分组
     *
     * @param subIdList     路径ID集合
     * @param targetGroupId 目标组ID
     * @throws BusinessException 业务异常
     */
    void moveUserProfilePath(List<UUID> subIdList, UUID targetGroupId) throws BusinessException;

    /**
     * 通过路径ID集合查找路径
     *
     * @param subIdList 路径ID集合
     * @return 路径列表
     * @throws BusinessException 业务异常
     */
    List<UserProfilePathDTO> findUserProfilePathByIdIn(List<UUID> subIdList) throws BusinessException;

    /**
     * 创建路径
     *
     * @param userProfilePathDTO 路径对象
     * @throws BusinessException 业务异常
     */
    void createUserProfilePath(UserProfilePathDTO userProfilePathDTO) throws BusinessException;

    /**
     * 编辑路径
     *
     * @param userProfilePathDTO 路径对象
     * @throws BusinessException 业务异常
     */
    void editUserProfilePath(UserProfilePathDTO userProfilePathDTO) throws BusinessException;

    /**
     * 通过ID查找路径
     *
     * @param uuid 路径ID
     * @return 路径对象
     * @throws BusinessException 业务异常
     */
    UserProfilePathDTO findUserProfilePathById(UUID uuid) throws BusinessException;

    /**
     * 子路径查询
     *
     * @param request 查询请求
     * @return 子路径列表
     * @throws BusinessException 异常处理
     */
    DefaultPageResponse<UserProfileChildPathInfoDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 通过id删除路径
     *
     * @param id 路径ID
     * @throws BusinessException 业务异常
     */
    void deleteUserProfilePath(UUID id) throws BusinessException;

    /**
     * 路径名称重名检查（允许修改路径名称，即编辑时自身名称重名不检测）
     *
     * @param id   路径id
     * @param name 路径名称
     * @return 是否重名 true:重名 false:未重名
     */
    Boolean checkUserProfilePathNameDuplication(UUID id, String name);

    /**
     * 创建用户配置策略
     *
     * @param userProfileStrategyDTO 策略对象
     * @throws BusinessException 业务异常
     */
    void createUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException;

    /**
     * 编辑用户配置策略
     *
     * @param userProfileStrategyDTO 策略对象
     * @throws BusinessException 业务异常
     */
    void editUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException;

    /**
     * 删除用户配置策略
     *
     * @param id 策略id
     * @throws BusinessException 业务异常
     */
    void deleteUserProfileStrategy(UUID id) throws BusinessException;

    /**
     * 通过策略Id查找策略
     *
     * @param id 策略id
     * @return 响应对象
     * @throws BusinessException 业务异常
     */
    UserProfileStrategyDTO findUserProfileStrategyWrapperById(UUID id) throws BusinessException;

    /**
     * 策略重名检查 （允许修改策略名称，即编辑时自身名称重名不检测）
     *
     * @param id   策略id
     * @param name 策略名称
     * @return 响应对象
     */
    Boolean checkUserProfileStrategyNameDuplication(UUID id, String name);

    /**
     * 获取所有路径,用于web策略关联路径页展示
     *
     * @return 响应对象
     * @throws BusinessException 业务异常
     */
    List<UserProfilePathDTO> findAllUserProfilePathForWeb() throws BusinessException;

    /**
     * 获取所有路径组
     *
     * @return 响应对象
     * @throws BusinessException 业务异常
     */
    List<UserProfilePathGroupDTO> findAllUserProfilePathGroup() throws BusinessException;

    /**
     * 获取upm路径树数据，代码逻辑来自UserProfileStrategyController.getTreeList
     *
     * @param storageType storageType
     * @return List<UserProfileTreeDTO>
     */
    List<UserProfileTreeDTO> listUserProfileTree(@Nullable UserProfileStrategyStorageTypeEnum storageType);

    /**
     * 查询策略关联的路径列表
     *
     * @param request 分页请求
     * @return 路径列表
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserProfileStrategyRelatedDetailDTO> userProfileStrategyRelatedPageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 从策略中删除路径
     *
     * @param strategyId 策略ID
     * @param id         路径ID
     * @throws BusinessException 业务异常
     */
    void deletePathFromStrategyRelated(UUID strategyId, UUID id) throws BusinessException;

    /**
     * 通过策略ID查找策略信息
     *
     * @param userProfileStrategyId 策略ID
     * @return 响应对象
     * @throws BusinessException 业务异常
     */
    UserProfileStrategyDTO findUserProfileStrategyById(UUID userProfileStrategyId) throws BusinessException;


    /**
     * 根据策略ID获取生效的路径树对象
     *
     * @param userProfileStrategyId 策略ID
     * @return 路径树对象
     * @throws BusinessException 业务异常
     */
    PathTree getEffectiveUserProfilePathTree(UUID userProfileStrategyId) throws BusinessException;

    /**
     * 根据个性化配置策略ID获取生效的路径列表
     *
     * @param userProfileStrategyId 策略ID
     * @return 路径列表
     */
    List<UserProfilePathDetailDTO> getEffectiveUserProfilePathByStrategyId(UUID userProfileStrategyId);

    /**
     * 根据路径ID获取生效的路径列表
     *
     * @param userProfilePathId 路径ID
     * @return 路径列表
     */
    List<UserProfilePathDetailDTO> getEffectiveUserProfilePathByPathId(UUID userProfilePathId);

    /**
     * 根据子路径ID获取生效的路径列表
     *
     * @param userProfileChildPathId 子路径ID
     * @return 路径列表
     */
    List<UserProfilePathDetailDTO> getEffectiveUserProfilePathByChildPathId(UUID userProfileChildPathId);

    /**
     * 根据策略ID获取生效的路径列表
     *
     * @param userProfileStrategyId 策略ID
     * @return 路径列表
     * @throws BusinessException 业务异常
     */
    List<UserProfilePathDetailDTO> getEffectiveUserProfilePath(UUID userProfileStrategyId) throws BusinessException;

    /**
     * 是否有正在导入的路径任务
     *
     * @throws BusinessException 业务异常
     */
    void isImportingPath() throws BusinessException;

    /**
     * 开始导入路径任务
     *
     * @throws BusinessException 业务异常
     */
    void startAddPathData() throws BusinessException;

    /**
     * 结束导入路径任务
     */
    void finishAddPathData();

    /**
     * 获取路径组ID 不存在则创建
     *
     * @param groupDTO 组对象
     * @return 组ID
     */
    UUID getPathGroupIdIfNotExistCreate(UserProfilePathGroupDTO groupDTO);

    /**
     * 分页查询用户配置策略
     *
     * @param build 分页查询条件
     * @return 策略列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<UserProfileStrategyDTO> pageUserProfileStrategyQuery(PageQueryRequest build) throws BusinessException;

    /**
     * 根据镜像id查询用户配置策略是否可用
     *
     * @param imageId 镜像ID
     * @return 不可用消息
     * @throws BusinessException 业务异常
     */
    String getStrategyUsedMessageByImageId(UUID imageId) throws BusinessException;

    /**
     * 根据云桌面策略id查询用户配置策略是否可用
     *
     * @param strategyId 云桌面策略ID
     * @return 不可用消息
     * @throws BusinessException 业务异常
     */
    String getStrategyUsedMessageByStrategyId(UUID strategyId) throws BusinessException;

    /**
     * 获取发送给GT的策略路径列表
     *
     * @param strategyGuestToolMsgDTO GT消息
     * @param userProfilePathDTOList  路径列表
     * @param userProfileStrategyId 个性化配置策略id
     * @throws BusinessException 业务异常
     */
    void getGuestToolStrategyPath(UserProfileStrategyGuestToolMsgDTO strategyGuestToolMsgDTO, List<UserProfilePathDetailDTO> userProfilePathDTOList,
            UUID userProfileStrategyId) throws BusinessException;

    /**
     * 获取清理配置数据的GT消息对象
     *
     * @param messageDTO             清理配置数据的GT消息对象
     * @param userProfilePathDTOList 路径详细信息列表
     */
    void getGuestToolCleanPath(UserProfileCleanGuestToolMsgDTO messageDTO,
                               List<UserProfilePathDetailDTO> userProfilePathDTOList);

    /**
     * 删除rco云桌面及终端组、用户组、用户配置与用户配置策略的关联记录
     *
     * @param deskStrategyId 云桌面策略ID
     */
    void deleteDeskRelatedUserProfileStrategy(UUID deskStrategyId);

    /**
     * 导入用户特殊配置
     *
     * @param userProfilePathDTO 路径对象
     * @throws BusinessException 异常处理
     */
    void importSpecialUserProfilePath(UserProfilePathDTO userProfilePathDTO) throws BusinessException;

    /**
     * 发送清理配置数据的GT消息
     *
     * @param guestToolMsgDTO 清理配置数据的GT消息对象
     * @param deskId          云桌面ID
     * @throws BusinessException 异常处理
     */
    void sendCleanUserProfilePathMessage(UserProfileCleanGuestToolMsgDTO guestToolMsgDTO, UUID deskId) throws BusinessException;

    /**
     * 保存下发失败的清理路径配置请求
     *
     * @param guestToolMsgDTO 清理配置数据的GT消息对象
     * @param deskId          云桌面ID
     */
    void saveFailCleanRequest(UserProfileCleanGuestToolMsgDTO guestToolMsgDTO, UUID deskId);

    /**
     * 根据云桌面ID删除缓存的清理命令
     *
     * @param desktopId 云桌面ID
     */
    void deleteFailCleanRequestById(UUID desktopId);

    /**
     * 根据云桌面ID列表删除缓存的清理命令
     *
     * @param desktopIdArr 云桌面ID列表
     */
    void deleteFailCleanRequestByIdArr(UUID[] desktopIdArr);

    /**
     * 删除子路径
     *
     * @param userProfileDetailId 子路径ID
     * @throws BusinessException 异常处理
     */
    void deleteChildPath(UUID userProfileDetailId) throws BusinessException;

    /**
     * 统计选定配置下的子路径总数
     *
     * @param userProfilePathIdArr 选定配置列表
     * @return 子路径总数
     */
    int getPathCountOfStrategy(UUID[] userProfilePathIdArr);

    /**
     * 查询策略名称
     *
     * @param strategyId 策略ID
     * @return 策略名称
     */
    String findStrategyNameByStrategyId(UUID strategyId);

    /**
     * 查找子路径所归属的个性化配置
     *
     * @param userProfileDetailId 子路径ID
     * @return 个性化配置ID
     */
    UUID findUserProfilePathIdByChildPath(UUID userProfileDetailId);
}