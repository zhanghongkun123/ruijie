package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.constant;

/**
 * 用户管理常量类
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年5月27日
 *
 * @author zhang.zhiwen
 */
public interface UserConstant {

    String USER_PWD_REGEX = "^(?!.*[\\u4e00-\\u9fa5]).{1,32}$";

    String USER_PWD_EMOJI_REGEX = "[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]";

    String PASSWORD_RANDOM_MODE = "RANDOM";
}
