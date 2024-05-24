package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PermissionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SuperPrivilegeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListTerminalGroupIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdLabelEntryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListImageIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListTerminalGroupIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdLabelEntryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.TerminalConstants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.AdminInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.AdminInfoMessageDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/24 21:08
 *
 * @author linrenjian
 */
@DispatcherImplemetion(Constants.GET_ADMIN_PERMISSION)
public class GetAdminPermissionSPIImpl implements CbbDispatcherHandlerSPI {

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Autowired
    private IacPermissionMgmtAPI basePermissionMgmtAPI;

    @Autowired
    private PermissionMgmtAPI permissionMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    /**
     * 角色API
     */
    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    /**
     * 用户API
     */
    @Autowired
    private IacUserMgmtAPI userAPI;


    private static final Logger LOGGER = LoggerFactory.getLogger(GetAdminPermissionSPIImpl.class);


    @Override
    public void dispatch(CbbDispatcherRequest request) {
        // 请求不为空
        Assert.notNull(request, "request cannot be null!");
        // 数据不为空
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        // 1、入参校验
        AdminInfoDTO requestDto = JSONObject.parseObject(request.getData(), AdminInfoDTO.class);
        AdminLoginOnTerminalCache adminCache = adminCacheManager.getIfPresent(requestDto.getAdminSessionId());
        // 判断session 是否存在
        if (adminCache == null) {
            // 给shine 信息 -20 代表session过期不存在
            response(request, PermissionConstants.SESSION_NOT_EXIST, null);
            return;
        }
        try {
            // 获取管理员信息
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(adminCache.getAdminId());

            // 是否需要判断当用户被禁用 让session失效
            AdminInfoMessageDTO adminInfoMessageDTO = new AdminInfoMessageDTO();
            // 用户信息
            adminInfoMessageDTO.setBaseAdminDTO(baseAdminDTO);

            if (ArrayUtils.isNotEmpty(baseAdminDTO.getRoleIdArr())) {
                // 设置角色信息
                IacRoleDTO role = baseRoleMgmtAPI.getRole(baseAdminDTO.getRoleIdArr()[0]);
                adminInfoMessageDTO.setRole(role);
            }
            // 查询菜单权限
            adminInfoMessageDTO.setBasePermissionDTOList(getMenuNameArr(baseAdminDTO.getRoleIdArr(), baseAdminDTO.getId()));
            // 如果登录的角色是超级管理员 或者用户名称是系统管理员 拥有全部的数据权限
            if (adminPermissionAPI.roleIsAdminOrAdminNameIsSysadmin(adminCache.getAdminId())) {
                // 设置全部终端组
                adminInfoMessageDTO.setTerminalGroupArr(adminDataPermissionAPI.listAllTerminalGroupEntry());
                // 设置全部用户组
                adminInfoMessageDTO.setUserGroupArr(adminDataPermissionAPI.listAllUserGroupEntry());
                // 设置全部镜像组
                adminInfoMessageDTO.setImageArr(adminDataPermissionAPI.listAllImageEntry());
            } else {
                // 设置终端组
                adminInfoMessageDTO.setTerminalGroupArr(getTerminalGroupIdLabelEntryArr(adminCache.getAdminId()));
                // 设置用户组
                adminInfoMessageDTO.setUserGroupArr(getUserGroupIdLabelEntryArr(adminCache.getAdminId()));
                // 设置镜像组
                adminInfoMessageDTO.setImageArr(getImageIdLabelEntryArr(adminCache.getAdminId()));
            }
            // 通过管理员名称查询普通用户信息
            IacUserDetailDTO userDetail = userAPI.getUserByName(baseAdminDTO.getUserName());
            // 如果当前用户不为空 并且是已设置为管理员 添加进入
            if (userDetail != null && UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole())) {
                adminInfoMessageDTO.setUserDetailDTO(userDetail);
            }
            // 给shine 信息
            response(request, Constants.SUCCESS, adminInfoMessageDTO);
        } catch (Exception e) {
            LOGGER.error("终端{}获取管理员信息失败", request.getTerminalId(), e);
            // 给shine 信息 99 代表查询异常
            response(request, Constants.FAILURE, null);
        }

    }

    /**
     * 给shine 信息
     * 
     * @param request
     * @param code
     * @param adminInfoMessageDTO
     */
    private void response(CbbDispatcherRequest request, Integer code, AdminInfoMessageDTO adminInfoMessageDTO) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, adminInfoMessageDTO);
        } catch (Exception e) {
            LOGGER.error("终端{}获取管理员信息失败", request.getTerminalId(), e);
        }
    }

    /**
     * 获取权限菜单
     *
     * @param roleIdArr
     * @param adminId
     * @return
     */
    private List<IacPermissionDTO> getMenuNameArr(UUID[] roleIdArr, UUID adminId) throws BusinessException {
        // 如果是空 则返回空
        if (ArrayUtils.isEmpty(roleIdArr)) {
            return new ArrayList<>();
        }

        List<IacPermissionDTO> basePermissionDTOList = null;
        SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
        superPrivilegeRequest.setRoleIdArr(roleIdArr);
        SuperPrivilegeResponse superPrivilegeResponse = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
        // 角色权限关联表中未记载超级管理员信息，直接通过是否为超级管理员来返回所有菜单列表
        if (superPrivilegeResponse.isSuperPrivilege()) {
            basePermissionDTOList = permissionMgmtAPI.listAllPermissionByServerModel();
        } else {
            basePermissionDTOList = basePermissionMgmtAPI.listPermissionByAdminIdAndSource(adminId, SubSystem.CDC);
        }
        try {
            // 集合为空 直接返回
            if (CollectionUtils.isEmpty(basePermissionDTOList)) {
                LOGGER.info("当前数据权限为空");
                return basePermissionDTOList;
            }
            List<String> unsupportedMenuNameList = permissionMgmtAPI.getCurrentServerModelUnsupportedMenuNameList();
            LOGGER.info("获取当前服务器模式不支持的菜单列表{}:{}", unsupportedMenuNameList.size(), unsupportedMenuNameList.toArray());
            // 遍历所有权限
            for (int i = basePermissionDTOList.size() - 1; i > 0; i--) {
                // 遍历不支持的菜单权限
                for (int j = unsupportedMenuNameList.size() - 1; j > 0; j--) {
                    if (basePermissionDTOList.get(i).getPermissionCode().equals(unsupportedMenuNameList.get(j))) {
                        basePermissionDTOList.remove(i);
                        // 跳出内层for
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("获取当前服务器模式不支持的菜单列表失败", e);
            return new ArrayList<>();
        }
        return basePermissionDTOList;
    }


    /**
     * 获取终端组数据权限集
     * 
     * @param adminId
     * @return
     * @throws BusinessException
     */
    private GroupIdLabelEntry[] getTerminalGroupIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListTerminalGroupIdLabelEntryRequest request = new ListTerminalGroupIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListTerminalGroupIdLabelEntryResponse response = adminDataPermissionAPI.listTerminalGroupIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getTerminalGroupIdLabelEntryList();
        for (GroupIdLabelEntry entry : idLabelEntryList) {
            if (entry.getId().equals(TerminalConstants.TERMINAL_GROUP_ROOT_ID)) {
                entry.setLabel(TerminalConstants.TERMINAL_GROUP_ROOT_NAME);
                break;
            }
        }
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    /**
     * 获取用户组数据权限集
     * 
     * @param adminId
     * @return
     * @throws BusinessException
     */
    private GroupIdLabelEntry[] getUserGroupIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListUserGroupIdLabelEntryRequest request = new ListUserGroupIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListUserGroupIdLabelEntryResponse response = adminDataPermissionAPI.listUserGroupIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getUserGroupIdLabelEntryList();
        for (GroupIdLabelEntry entry : idLabelEntryList) {
            if (entry.getId().equals(UserConstants.USER_GROUP_ROOT_ID)) {
                entry.setLabel(UserConstants.USER_GROUP_ROOT_NAME);
                break;
            }
        }
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }

    /**
     * 获取镜像组数据权限集
     * 
     * @param adminId
     * @return
     * @throws BusinessException
     */
    private GroupIdLabelEntry[] getImageIdLabelEntryArr(UUID adminId) throws BusinessException {
        ListImageIdLabelEntryRequest request = new ListImageIdLabelEntryRequest();
        request.setAdminId(adminId);
        ListImageIdLabelEntryResponse response = adminDataPermissionAPI.listImageIdLabelEntryByAdminId(request);
        List<GroupIdLabelEntry> idLabelEntryList = response.getImageIdLabelEntryList();
        return idLabelEntryList.toArray(new GroupIdLabelEntry[0]);
    }
}
