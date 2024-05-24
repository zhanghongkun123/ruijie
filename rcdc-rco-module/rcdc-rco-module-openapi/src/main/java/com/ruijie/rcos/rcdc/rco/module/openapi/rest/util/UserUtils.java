package com.ruijie.rcos.rcdc.rco.module.openapi.rest.util;

import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.*;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/27 15:51
 *
 * @author zdc
 */
public class UserUtils {

    public static final String USER_NAME = "^[0-9a-zA-Z\\u4e00-\\u9fa5@.-][0-9a-zA-Z\\u4e00-\\u9fa5_@.-]*$";

    public static final int USER_NAME_SIZE = 32;

    /**
     * openAPI校验入参用户名
     * @param userName 用户名
     * @throws BusinessException 业务异常
     */
    public static void checkUserName(String userName) throws BusinessException {
        Assert.notNull(userName, "userName must not be null");
        if (!org.springframework.util.StringUtils.hasText(userName)) {
            throw new BusinessException(OPEN_API_USER_NAME_NOT_EMPTY);
        }

        if (!userName.matches(USER_NAME)) {
            throw new BusinessException(OPEN_API_USER_NAME_INVALID);
        }
        if (userName.length() > USER_NAME_SIZE) {
            throw new BusinessException(OPEN_API_USER_NAME_TOO_LENGTH);
        }
    }
}
