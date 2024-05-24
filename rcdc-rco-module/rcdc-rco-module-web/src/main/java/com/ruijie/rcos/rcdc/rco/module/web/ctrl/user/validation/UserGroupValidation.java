package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.validation;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.UpdateUserGroupWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
@Service
public class UserGroupValidation {
    private static final Pattern TEXT_NAME_PATTERN = 
            Pattern.compile("^[0-9a-zA-Z\\u4e00-\\u9fa5，,（）()@.-][0-9a-zA-Z\\u4e00-\\u9fa5，,（）()_@.-]*$");


    /**
     * 验证用户组
     * 
     * @param request request
     * @throws BusinessException BusinessException
     */
    public void editUserGroupValidate(UpdateUserGroupWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        String groupName = request.getGroupName();
        Matcher matcher = TEXT_NAME_PATTERN.matcher(groupName);

        if (!matcher.matches()) {
            throw new BusinessException(BusinessKey.RCDC_USER_GROUP_NAME_VALID_ERROR, groupName);
        }

    }
}
