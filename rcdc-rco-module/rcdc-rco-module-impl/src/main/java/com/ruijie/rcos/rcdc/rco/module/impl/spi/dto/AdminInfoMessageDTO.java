package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.List;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.GroupIdLabelEntry;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/26 17:13
 *
 * @author linrenjian
 */
public class AdminInfoMessageDTO {

    /**
     * 管理员信息
     */
    private IacAdminDTO baseAdminDTO;

    /**
     * 权限
     */

    private List<IacPermissionDTO> basePermissionDTOList;

    /**
     * 菜单
     */
    private String[] menuNameArr;


    /**
     * 用户组
     */
    private GroupIdLabelEntry[] userGroupArr;

    /***
     * 终端组
     */
    private GroupIdLabelEntry[] terminalGroupArr;

    /**
     * 镜像组
     */
    private GroupIdLabelEntry[] imageArr;


    /**
     * 角色信息
     */
    private IacRoleDTO role;

    /**
     * 用户信息
     */
    private IacUserDetailDTO userDetailDTO;

    public IacAdminDTO getBaseAdminDTO() {
        return baseAdminDTO;
    }

    public void setBaseAdminDTO(IacAdminDTO baseAdminDTO) {
        this.baseAdminDTO = baseAdminDTO;
    }

    public IacRoleDTO getRole() {
        return role;
    }

    public void setRole(IacRoleDTO role) {
        this.role = role;
    }

    public List<IacPermissionDTO> getBasePermissionDTOList() {
        return basePermissionDTOList;
    }

    public void setBasePermissionDTOList(List<IacPermissionDTO> basePermissionDTOList) {
        this.basePermissionDTOList = basePermissionDTOList;
    }

    public String[] getMenuNameArr() {
        return menuNameArr;
    }

    public void setMenuNameArr(String[] menuNameArr) {
        this.menuNameArr = menuNameArr;
    }


    public GroupIdLabelEntry[] getUserGroupArr() {
        return userGroupArr;
    }

    public void setUserGroupArr(GroupIdLabelEntry[] userGroupArr) {
        this.userGroupArr = userGroupArr;
    }

    public GroupIdLabelEntry[] getTerminalGroupArr() {
        return terminalGroupArr;
    }

    public void setTerminalGroupArr(GroupIdLabelEntry[] terminalGroupArr) {
        this.terminalGroupArr = terminalGroupArr;
    }

    public GroupIdLabelEntry[] getImageArr() {
        return imageArr;
    }

    public void setImageArr(GroupIdLabelEntry[] imageArr) {
        this.imageArr = imageArr;
    }

    public IacUserDetailDTO getUserDetailDTO() {
        return userDetailDTO;
    }

    public void setUserDetailDTO(IacUserDetailDTO userDetailDTO) {
        this.userDetailDTO = userDetailDTO;
    }
}
