package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AdminDataPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateAdminDataPermissionRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeleteRoleGroupPermissionByGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListImageIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListImageIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AdminDataPermisssionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AdminDataPermissionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AdminDataPermissionService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月14日
 *
 * @author linrenjian
 */
public class AdminDataPermissionAPIImpl implements AdminDataPermissionAPI {


    @Autowired
    private AdminDataPermissionService adminDataPermissionService;

    @Autowired
    private AdminDataPermisssionDAO adminDataPermisssionDAO;

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Override
    public DefaultResponse createAdminGroupPermission(CreateAdminDataPermissionRequest request) {
        Assert.notNull(request, "request is not null");
        AdminDataPermissionDTO adminDataPermissionDTO = request.getAdminDataPermissionDTO();
        AdminDataPermissionEntity entity = new AdminDataPermissionEntity();
        // 管理员ID
        entity.setAdminId(adminDataPermissionDTO.getAdminId());
        // 数据ID
        entity.setPermissionDataId(adminDataPermissionDTO.getPermissionDataId());
        // 用户组权限类型
        entity.setPermissionDataType(adminDataPermissionDTO.getPermissionDataType());
        Date date = new Date();
        // 创建时间
        entity.setCreateDate(date);
        // 更新时间
        entity.setUpdateDate(date);
        // 添加
        adminDataPermissionService.save(entity);
        return new DefaultResponse();
    }

    @Override
    public DefaultResponse deleteAdminGroupPermissionByGroupId(DeleteRoleGroupPermissionByGroupIdRequest request) {
        Assert.notNull(request, "request is not null");

        adminDataPermisssionDAO.deleteByPermissionDataIdAndPermissionDataType(request.getGroupId(),
                AdminDataPermissionType.valueOf(request.getGroupType()));
        return new DefaultResponse();
    }

    @Override
    public int deleteByPermissionDataId(String permissionDataId) {
        Assert.notNull(permissionDataId, "permissionDataId is not null");
        return adminDataPermisssionDAO.deleteByPermissionDataId(permissionDataId);
    }

    @Override
    public ListUserGroupIdResponse listUserGroupIdByAdminId(ListUserGroupIdRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<String> uuidList = adminDataPermissionService.listUserGroupIdByAdminId(request.getAdminId());
        ListUserGroupIdResponse response = new ListUserGroupIdResponse();
        response.setUserGroupIdList(uuidList);
        return response;
    }


    @Override
    public GroupIdLabelEntry[] listAllUserGroupEntry() throws BusinessException {

        // 用户组标签
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 查询所有的用户组
        IacUserGroupDetailDTO[] userGroupDTOArr = userGroupAPI.getAllUserGroup();
        // 遍历添加标签元素
        Stream.of(userGroupDTOArr).forEach(item -> {
            GroupIdLabelEntry idLabelEntry = new GroupIdLabelEntry();
            idLabelEntry.setId(item.getId().toString());
            idLabelEntry.setLabel(item.getName());
            idLabelEntryList.add(idLabelEntry);
        });
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    @Override
    public ListTerminalGroupIdResponse listTerminalGroupIdByAdminId(ListTerminalGroupIdRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<String> uuidList = adminDataPermissionService.listTerminalGroupIdByAdminId(request.getAdminId());
        ListTerminalGroupIdResponse response = new ListTerminalGroupIdResponse();
        response.setTerminalGroupIdList(uuidList);
        return response;
    }

    @Override
    public GroupIdLabelEntry[] listAllTerminalGroupEntry() throws BusinessException {
        // 终端组标签
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 查询所有的终端组
        List<CbbTerminalGroupDetailDTO> terminalGroupDTOList = terminalGroupMgmtAPI.listTerminalGroup();
        // 遍历添加标签元素
        terminalGroupDTOList.stream().forEach(item -> {
            GroupIdLabelEntry idLabelEntry = new GroupIdLabelEntry();
            idLabelEntry.setId(item.getId().toString());
            idLabelEntry.setLabel(item.getGroupName());
            idLabelEntryList.add(idLabelEntry);
        });
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    @Override
    public ListImageIdResponse listImageIdByAdminId(ListImageIdRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<String> uuidList = adminDataPermissionService.listImageIdByAdminId(request.getAdminId());
        ListImageIdResponse response = new ListImageIdResponse();
        response.setImageIdList(uuidList);
        return response;
    }

    @Override
    public GroupIdLabelEntry[] listAllImageEntry() throws BusinessException {
        // 镜像标签
        List<GroupIdLabelEntry> idLabelEntryList = new ArrayList<>();
        // 查询所有的镜像
        List<CbbImageTemplateDetailDTO> cbbImageTemplateDetailDTOList = cbbImageTemplateMgmtAPI.listAllImageTemplate();
        // 遍历添加标签元素
        cbbImageTemplateDetailDTOList.stream().forEach(item -> {
            GroupIdLabelEntry idLabelEntry = new GroupIdLabelEntry();
            idLabelEntry.setId(item.getId().toString());
            idLabelEntry.setLabel(item.getImageName());
            idLabelEntryList.add(idLabelEntry);
        });
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    @Override
    public ListUserGroupIdLabelEntryResponse listUserGroupIdLabelEntryByAdminId(ListUserGroupIdLabelEntryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listUserGroupIdLabelEntryByAdminId(request.getAdminId());
        ListUserGroupIdLabelEntryResponse response = new ListUserGroupIdLabelEntryResponse();
        response.setUserGroupIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public ListTerminalGroupIdLabelEntryResponse listTerminalGroupIdLabelEntryByAdminId(ListTerminalGroupIdLabelEntryRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listTerminalGroupIdLabelEntryByAdminId(request.getAdminId());
        ListTerminalGroupIdLabelEntryResponse response = new ListTerminalGroupIdLabelEntryResponse();
        response.setTerminalGroupIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public ListImageIdLabelEntryResponse listImageIdLabelEntryByAdminId(ListImageIdLabelEntryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listImageIdLabelEntryByAdminId(request.getAdminId());
        ListImageIdLabelEntryResponse response = new ListImageIdLabelEntryResponse();
        response.setImageIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public Boolean hasImageByAdminIdAndImageId(UUID adminId, UUID permissionDataId) throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        Assert.notNull(permissionDataId, "permissionDataId is not null");

        // 查询管理员是否有这个镜像的权限
        List<GroupIdLabelEntry> groupIdLabelEntryList =
                adminDataPermissionService.listImageIdLabelEntryByAdminIdAndImageId(adminId, permissionDataId.toString());
        return CollectionUtils.isNotEmpty(groupIdLabelEntryList);
    }

    @Override
    public void initializeAdminDataPermission() throws BusinessException {
        adminDataPermissionService.initializeAdminDataPermission();
    }

    @Override
    public void initDeskStrategyAdminDataPermission() throws BusinessException {
        adminDataPermissionService.initDeskStrategyAdminDataPermission();
    }

    @Override
    public void initRcaStrategyAdminDataPermission() throws BusinessException {
        adminDataPermissionService.initRcaStrategyAdminDataPermission();
    }

    @Override
    public List<String> listByAdminIdAndPermissionType(UUID adminId, AdminDataPermissionType permissionType) {
        Assert.notNull(adminId, "adminId is not null");
        Assert.notNull(permissionType, "permissionType is not null");
        return adminDataPermissionService.listByAdminIdAndPermissionType(adminId, permissionType);
    }

    @Override
    public ListIdLabelEntryResponse listDesktopPoolIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listDesktopPoolIdLabelEntryByAdminId(request.getAdminId());
        ListIdLabelEntryResponse response = new ListIdLabelEntryResponse();
        response.setIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public ListIdLabelEntryResponse listDiskPoolIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listDiskPoolIdLabelEntryByAdminId(request.getAdminId());
        ListIdLabelEntryResponse response = new ListIdLabelEntryResponse();
        response.setIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public ListIdLabelEntryResponse listDeskStrategyIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listDeskStrategyIdLabelEntryByAdminId(request.getAdminId());
        ListIdLabelEntryResponse response = new ListIdLabelEntryResponse();
        response.setIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public ListIdLabelEntryResponse listAppPoolIdLabelEntryByAdminId(ListIdLabelEntryRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listAppPoolIdLabelEntryByAdminId(request.getAdminId());
        ListIdLabelEntryResponse response = new ListIdLabelEntryResponse();
        response.setIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public ListIdLabelEntryResponse listAppMainStrategyIdLabelEntryByAdminId(ListIdLabelEntryRequest request)
            throws BusinessException {

        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listAppMainStrategyIdLabelEntryByAdminId(
                request.getAdminId());
        ListIdLabelEntryResponse response = new ListIdLabelEntryResponse();
        response.setIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public ListIdLabelEntryResponse listAppPeripheralStrategyIdLabelEntryByAdminId(ListIdLabelEntryRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getAdminId(), "adminId is not null");

        List<GroupIdLabelEntry> idLabelEntryList = adminDataPermissionService.listAppPeripheralStrategyIdLabelEntryByAdminId(
                request.getAdminId());
        ListIdLabelEntryResponse response = new ListIdLabelEntryResponse();
        response.setIdLabelEntryList(idLabelEntryList);
        return response;
    }

    @Override
    public Boolean hasDataPermission(UUID adminId, String permissionDataId, AdminDataPermissionType permissionDataType) {
        Assert.notNull(adminId, "adminId must not be null");
        Assert.hasText(permissionDataId, "permissionDataId must not be null");
        Assert.notNull(permissionDataType, "permissionDataType must not be null");
        return adminDataPermissionService.hasDataPermission(adminId, permissionDataId, permissionDataType);
    }

    @Override
    public void deleteByPermissionDataIdAndPermissionDataType(String permissionDataId, AdminDataPermissionType permissionDataType) {
        Assert.hasText(permissionDataId, "permissionDataId must not be null");
        Assert.notNull(permissionDataType, "permissionDataType must not be null");
        adminDataPermissionService.deleteByPermissionDataIdAndPermissionDataType(permissionDataId, permissionDataType);
    }

    @Override
    public void addSysadminNewPermission() throws BusinessException {
        adminDataPermissionService.addSysadminNewPermission();
    }
}
