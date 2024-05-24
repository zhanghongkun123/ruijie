package com.ruijie.rcos.rcdc.rco.module.def.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserGroupVO;
import com.ruijie.rcos.rcdc.rco.module.def.desktop.dto.GroupFilterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeBuilder;

/**
 * Description: 抽象用户组工具类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/8
 *
 * @author xiao'yong'deng
 */
@Service("userGroupBaseHelper")
public class UserGroupBaseHelper {

    /**
     * 构建用户组树形结构
     *
     * @param groupFilterDTO groupFilterDTO对象
     * @return 返回树结构
     */
    public List<UserGroupVO> buildUserGroupTree(GroupFilterDTO groupFilterDTO) {
        Assert.notNull(groupFilterDTO, "groupFilterDTO cannot null");

        List<UserGroupVO> userGroupVOList = filterUserGroup(groupFilterDTO);
        return new TreeBuilder<>(userGroupVOList).build();
    }

    private List<UserGroupVO> filterUserGroup(GroupFilterDTO groupFilterDTO) {
        List<UserGroupVO> userGroupVOList = new ArrayList<>();
        // 记录默认分组（未分组）
        UserGroupVO defaultGroupVO = null;
        if (groupFilterDTO == null) {
            return userGroupVOList;
        }

        for (UserGroupVO userGroupVO : groupFilterDTO.getUserGroupVOList()) {
            //过滤指定组、过滤ad组、过滤ldap组、过滤默认组
            boolean isDefault = IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID.toString().equals(userGroupVO.getId());
            if ((groupFilterDTO.getFilterGroupId() != null && groupFilterDTO.getFilterGroupId().toString().equals(userGroupVO.getId()))
                || (Boolean.TRUE.equals(groupFilterDTO.getEnableFilterAdGroup()) && Boolean.TRUE.equals(userGroupVO.isEnableAd()))
                || (Boolean.TRUE.equals(groupFilterDTO.getEnableFilterLdapGroup()) && Boolean.TRUE.equals(userGroupVO.getEnableLdap()))
                || (Boolean.TRUE.equals(groupFilterDTO.getEnableFilterThirdPartyGroup())
                    && Boolean.TRUE.equals(userGroupVO.getEnableThirdParty()))
                || (groupFilterDTO.getEnableFilterDefaultGroup() && isDefault)
                || isDefault) {

                if (isDefault) {
                    defaultGroupVO = userGroupVO;
                }
                continue;
            }
            userGroupVOList.add(userGroupVO);
        }
        if (defaultGroupVO != null) {
            // 默认未分组置底
            userGroupVOList.add(defaultGroupVO);
        }
        return userGroupVOList;
    }
}
