package com.ruijie.rcos.rcdc.rco.module.def.user.common;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/3
 *
 * @author linke
 */
@Service
public class UserCommonHelper {

    private static final IacUserTypeEnum[] NON_VISITOR_TYPE_ENUM =
            new IacUserTypeEnum[] {IacUserTypeEnum.AD, IacUserTypeEnum.LDAP, IacUserTypeEnum.NORMAL, IacUserTypeEnum.THIRD_PARTY};

    /**
     * 用户类型
     */
    private static final String USER_TYPE = "userType";

    public static final String USER_GROUP_ROOT_ID = "root";

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    /**
     * 处理用户类型条件，只查询非访客用户
     *
     * @param request PageSearchRequest
     */
    public void dealNonVisitorUserTypeMatch(PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        if (ArrayUtils.isEmpty(request.getMatchEqualArr())) {
            MatchEqual matchEqual = new MatchEqual(USER_TYPE, NON_VISITOR_TYPE_ENUM);
            request.setMatchEqualArr(new MatchEqual[]{matchEqual});
            return;
        }
        for (MatchEqual matchEqual : request.getMatchEqualArr()) {
            if (Objects.equals(matchEqual.getName(), USER_TYPE)) {
                removeVisitorMatch(matchEqual);
                return;
            }
        }
        // 不存在就加默认条件
        MatchEqual matchEqual = new MatchEqual(USER_TYPE, NON_VISITOR_TYPE_ENUM);
        List<MatchEqual> matchEqualList = Lists.newArrayList(request.getMatchEqualArr());
        matchEqualList.add(matchEqual);
        request.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[0]));
    }

    private static void removeVisitorMatch(MatchEqual matchEqual) {
        if (ArrayUtils.isEmpty(matchEqual.getValueArr())) {
            matchEqual.setValueArr(NON_VISITOR_TYPE_ENUM);
            return;
        }
        List<Object> valueList = Arrays.stream(matchEqual.getValueArr()).filter(item -> {
            IacUserTypeEnum type = (IacUserTypeEnum) item;
            return type != IacUserTypeEnum.VISITOR;
        }).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(valueList)) {
            matchEqual.setValueArr(NON_VISITOR_TYPE_ENUM);
            return;
        }
        matchEqual.setValueArr(valueList.toArray(new Object[0]));
    }

    /**
     * 检查是否有权限，同时添加相关数据权限条件
     *
     * @param request        request
     * @param pageRequest    pageRequest
     * @param sessionContext sessionContext
     * @return true 完成条件添加；false 无权限数据直接返回空数据给前端
     * @throws BusinessException BusinessException
     */
    public boolean checkAndAddQueryUserPermission(PageWebRequest request, PageSearchRequest pageRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(pageRequest, "pageRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        if (permissionHelper.isAllGroupPermission(sessionContext)) {
            return true;
        }
        List<String> userGroupIdStrList = getPermissionUserGroupIdList(sessionContext.getUserId());
        String userGroupId = getUserGroupId(request);
        if (StringUtils.isEmpty(userGroupId)) {
            appendUserGroupIdMatchEqual(pageRequest, userGroupIdStrList);
            return true;
        }
        // 传的组ID不在权限内直接返回空
        return userGroupIdStrList.contains(userGroupId);
    }

    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        return adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest).getUserGroupIdList();
    }

    private void appendUserGroupIdMatchEqual(PageSearchRequest request, List<String> userGroupIdStrList) {
        UUID[] uuidArr = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(USER_GROUP_ROOT_ID)).map(UUID::fromString).toArray(UUID[]::new);
        request.appendCustomMatchEqual(new MatchEqual(UserPageSearchRequest.GROUP_ID, uuidArr));
    }

    /**
     * 迁移代码，来自UserController。
     * <p>原来的注释：当前前端只会上传一个groupId进行查询
     */
    private String getUserGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals(UserPageSearchRequest.GROUP_ID) && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }
}
