package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.data.domain.Page;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CountCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopStateNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopCountInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:
 * 
 * @author artom
 */
public interface CloudDesktopViewService {

    /**
     * 
     * @param request search request
     * @return view page
     */
    Page<ViewUserDesktopEntity> pageQuery(PageSearchRequest request);

    /**
     * 分页查询桌面详情
     * @param request request
     * @return view page
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<ViewUserDesktopEntity> pageQuery(PageQueryRequest request) throws BusinessException;

    /**
     * 查询绑定临时权限的云桌面列表
     *
     * @param permissionId 临时权限Id
     * @param request      search request
     * @return Page<ViewUserDesktopEntity>
     */
    Page<ViewUserDesktopEntity> pageQueryInDesktopTempPermission(UUID permissionId, PageSearchRequest request);

    /**
     * 统计用户云桌面信息
     * 
     * @param userIdList user id list
     * @return UserDesktopCountVo
     */
    UserDesktopCountInfo countUserDesktopInfo(List<UUID> userIdList);

    /**
     * 根据指定用户组uuid列表下桌面状态数统计
     * 
     * @author hli on 2019-08-09
     * @param userGroupUuidList 用户组id列表
     * @param isDelete 是否删除
     * @return java.util.List<com.ruijie.rcos.rcdc.user.module.def.api.dto.DesktopStateNumDTO>
     */
    List<DesktopStateNumDTO> countByDeskState(List<UUID> userGroupUuidList, Boolean isDelete);

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
     * 根据参数统计数量
     *
     * @param countCloudDesktopDTO 参数集
     * @return 数量
     */
    Integer countByCloudDesktop(CountCloudDesktopDTO countCloudDesktopDTO);

}
