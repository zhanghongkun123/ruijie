package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Description:应用交付API
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/03 14:49
 *
 * @author coderLee23
 */
public interface AppDeliveryMgmtAPI {


    /**
     * 根据交付组名称和云桌面名称查找
     *
     * @param searchDeliveryGroupDTO 查询参数
     * @param pageable 分页参数
     * @return 返回值
     */
    DefaultPageResponse<UamDeliveryGroupDTO> pageUamDeliveryGroup(SearchDeliveryGroupDTO searchDeliveryGroupDTO, Pageable pageable);


    /**
     * 首页根据交付组名称查找
     *
     * @param homePageSearchDeliveryGroupDTO 查询参数
     * @param pageable 分页参数
     * @return 返回值
     */
    DefaultPageResponse<UamDeliveryGroupDTO> pageUamDeliveryGroup(HomePageSearchDeliveryGroupDTO homePageSearchDeliveryGroupDTO, Pageable pageable);


    /**
     * 根据交付组id 查询交付应用
     * 
     * @param searchDeliveryAppDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<UamDeliveryAppDTO>
     */
    DefaultPageResponse<UamDeliveryAppDTO> pageUamDeliveryApp(SearchDeliveryAppDTO searchDeliveryAppDTO, Pageable pageable);


    /**
     * 根据查询条件，返回应用磁盘列表
     *
     * @param searchAppDiskDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<UamAppDiskDTO>
     */
    DefaultPageResponse<UamAppDiskDTO> pageAppDisk(SearchAppDiskDTO searchAppDiskDTO, Pageable pageable);


    /**
     * 根据查询条件，返回推送安装包列表
     *
     * @param searchPushInstallPackageDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<UamPushInstallPackageDTO>
     */
    DefaultPageResponse<UamPushInstallPackageDTO> pagePushInstallPackage(SearchPushInstallPackageDTO searchPushInstallPackageDTO, Pageable pageable);


    /**
     * 根据交付组id和应用id查询 交付应用详情
     *
     * @param searchDeliveryAppDetailDTO 查询对象
     * @param pageable 分页参数
     * @return List<UamDeliveryAppDetailDTO>
     */
    DefaultPageResponse<UamDeliveryAppDetailDTO> pageUamDeliveryAppDetail(SearchDeliveryAppDetailDTO searchDeliveryAppDetailDTO, Pageable pageable);



    /**
     * 根据交付组id 查询交付对象
     *
     * @param searchDeliveryObjectDTO 查询对象
     * @param pageable 分页参数
     * @return List<UamDeliveryObjectDTO>
     */
    DefaultPageResponse<UamDeliveryObjectDTO> pageUamDeliveryObject(SearchDeliveryObjectDTO searchDeliveryObjectDTO, Pageable pageable);

    /**
     * 根据交付组id和云桌面id查询 交付对象详情
     *
     * @param searchDeliveryObjectDetailDTO 查询对象
     * @param pageable 分页参数
     * @return List<UamDeliveryObjectDetailDTO>
     */
    DefaultPageResponse<UamDeliveryObjectDetailDTO> pageUamDeliveryObjectDetail(SearchDeliveryObjectDetailDTO searchDeliveryObjectDetailDTO,
            Pageable pageable);


    /**
     * 根据id查找
     * 
     * @param id 唯一标识
     * @return UamDeliveryObjectDetailDTO
     * @throws BusinessException 业务异常
     */
    UamDeliveryObjectDetailDTO getDeliveryObjectDetail(UUID id) throws BusinessException;


    /**
     *
     * 根据终端组id查询
     *
     * @param searchTerminalGroupDesktopRelatedDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<TerminalGroupDesktopRelatedDTO>
     */
    DefaultPageResponse<TerminalGroupDesktopRelatedDTO> pageTerminalGroupDesktopRelated(
            SearchGroupDesktopRelatedDTO searchTerminalGroupDesktopRelatedDTO, Pageable pageable);


    /**
     *
     * 获取所有的终端组的可选桌面数量
     *
     * @param getGroupDesktopCountDTO 参数
     * @return List<TerminalGroupDesktopCountDTO>
     */
    List<TerminalGroupDesktopCountDTO> listTerminalGroupDesktopCount(GetGroupDesktopCountDTO getGroupDesktopCountDTO);

    /**
     *
     * 根据终端组id列表获取所有可用云桌面
     *
     * @param searchGroupDesktopRelatedDTO 查询参数
     * @return List<TerminalGroupDesktopRelatedDTO>
     */
    List<TerminalGroupDesktopRelatedDTO> listTerminalGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO);


    /**
     *
     * 根据终端组id查询
     *
     * @param searchGroupDesktopRelatedDTO 查询对象
     * @param pageable 分页参数
     * @return DefaultPageResponse<UserGroupDesktopRelatedDTO>
     */
    DefaultPageResponse<UserGroupDesktopRelatedDTO> pageUserGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO,
            Pageable pageable);


    /**
     *
     * 获取所有的用户组的可选桌面数量
     *
     * @param getGroupDesktopCountDTO 参数
     * @return List<UserGroupDesktopCountDTO>
     */
    List<UserGroupDesktopCountDTO> listUserGroupDesktopCount(GetGroupDesktopCountDTO getGroupDesktopCountDTO);

    /**
     *
     * 根据用户组id列表获取所有可用云桌面
     *
     * @param searchGroupDesktopRelatedDTO 参数
     * @return List<UserGroupDesktopRelatedDTO>
     */
    List<UserGroupDesktopRelatedDTO> listUserGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO);


    /**
     *
     * 交付组-根据id查找应用
     *
     * @param id 唯一标识
     * @return ViewUamDeliveryAppEntity
     * @throws BusinessException 业务异常
     */
    UamDeliveryAppDTO findDeliveryAppById(UUID id) throws BusinessException;


    /**
     * 基于交付组id查询应用列表
     *
     * @param deliveryGroupId deliveryGroupId
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    List<UamDeliveryAppDTO> findDeliveryAppListByGroupId(UUID deliveryGroupId) throws BusinessException;

    /**
     * 基于交付组ID查询应用名称列表
     * 
     * @param groupIdList groupIdList
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    List<String> findAppNameListByGroupId(List<UUID> groupIdList) throws BusinessException;

    /**
     *
     * 交付组-根据id查找对象
     *
     * @param id 唯一标识
     * @return ViewUamDeliveryAppEntity
     * @throws BusinessException 业务异常
     */
    UamDeliveryObjectDTO findDeliveryObjectById(UUID id) throws BusinessException;

    /**
     * 根据交付组id 获取交付对象的规格数据
     *
     * @param deliveryGroupId 交付组id
     * @return List<ViewUamDeliveryGroupObjectSpecEntity>
     */
    List<UamDeliveryGroupObjectSpecDTO> listByDeliveryGroupId(UUID deliveryGroupId);


    /**
     * 基于桌面Id查询交付组实体
     * 
     * @param deskId 桌面Id
     * @return List<UamDeliveryObjectDTO>
     */
    List<UamDeliveryObjectDTO> findByDeskId(UUID deskId);

    /**
     * 根据应用交付类型 判定云桌面列表是否被使用
     * 
     * @param appDeliveryType 应用交付类型
     * @param cloudDesktopIdList 判定云桌面列表是否被使用
     * @return true or false
     */
    Boolean existsByAppDeliveryTypeAndCloudDesktopIdIn(AppDeliveryTypeEnum appDeliveryType, List<UUID> cloudDesktopIdList);


    /**
     * 基于桌面ID删除交付组
     * 
     * @param desktopId 桌面ID
     */
    void deleteDeliveryObjectWhenStrategyModify(UUID desktopId);


    /**
     * 根据交付组id获取交付信息
     * 
     * @param id 交付组id
     * @return UamDeliveryGroupDTO 交付对象
     * @throws BusinessException 业务异常
     */
    UamDeliveryGroupDTO getUamDeliveryGroup(UUID id) throws BusinessException;

    /**
     * 根据条件统计数量
     *
     * @param request 请求对象
     * @return 数量
     */
    long countByConditions(ConditionQueryRequest request);
}
