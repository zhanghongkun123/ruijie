package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service.DesktopTempPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RecycleBinService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.QueryRecycleBinDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:
 * 
 * @author artom
 */
public class UserRecycleBinMgmtAPIImpl implements UserRecycleBinMgmtAPI {


    @Autowired
    private RecycleBinService recycleBinService;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private QueryRecycleBinDesktopService queryRecycleBinDesktopService;

    @Autowired
    private DesktopTempPermissionService desktopTempPermissionService;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;


    @Override
    public DefaultPageResponse<CloudDesktopDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Page<ViewUserDesktopEntity> page = queryRecycleBinDesktopService.pageQuery(request, ViewUserDesktopEntity.class);
        return queryCloudDesktopService.convertPageInfoAndQuery(page);
    }

    @Override
    public DefaultPageResponse<CloudDesktopDTO> getAll() throws BusinessException {
        List<ViewUserDesktopEntity> entityList = viewDesktopDetailDAO.findByIsDelete(true);
        CloudDesktopDTO[] dtoArr = new CloudDesktopDTO[entityList.size()];
        for (int i = 0, j = entityList.size(); i < j; i++) {
            dtoArr[i] = ViewUserDesktopEntity.convertEntityToDTO(entityList.get(i));
        }

        DefaultPageResponse<CloudDesktopDTO> page = new DefaultPageResponse<>();
        page.setTotal(entityList.size());

        page.setItemArr(dtoArr);
        return page;
    }

    @Override
    public void delete(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        recycleBinService.deleteDeskCompletely(request.getId());
        //删除桌面的临时权限配置
        desktopTempPermissionService.deleteDesktopTempPermissionByDeskId(request.getId());
    }

    @Override
    public void deleteDeskFromDb(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        cbbVDIDeskMgmtAPI.deleteDeskFromDb(deskId);
        desktopTempPermissionService.deleteDesktopTempPermissionByDeskId(deskId);
    }

    @Override
    public void recover(UUID deskId, @Nullable UUID assignUserId) throws BusinessException {
        Assert.notNull(deskId, "request must not be null");
        if (assignUserId == null) {
            recycleBinService.recover(deskId);
        } else {
            recycleBinService.recoverByAssignUserId(deskId, assignUserId);
        }
    }

    @Override
    public List<CloudDesktopDTO> getAllDesktopByUserIdList(List<UUID> userIdList) {
        Assert.notNull(userIdList, "userId must not be null");
        List<ViewUserDesktopEntity> recycleDesktopList = viewDesktopDetailDAO.findByUserIdInAndIsDelete(userIdList, true);
        if (Objects.isNull(recycleDesktopList)) {
            return new ArrayList<>();
        }
        return recycleDesktopList.stream().map(ViewUserDesktopEntity::convertEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public void recoverAssignDesktopPool(UUID deskId, @Nullable List<UUID> userIdList, UUID assignDesktopPoolId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(assignDesktopPoolId, "assignDesktopPoolId must not be null");

        recycleBinService.recoverByAssignDesktopPoolId(deskId, userIdList, assignDesktopPoolId);
    }

}
