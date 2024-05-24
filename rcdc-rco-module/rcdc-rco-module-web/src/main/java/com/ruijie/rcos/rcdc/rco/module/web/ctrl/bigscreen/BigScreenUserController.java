package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.UserDesktopResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description:  用户管理controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/13
 *
 * @author xiaoyongdeng
 */
@Controller
@RequestMapping(value = "/rco/bigScreen/user")
public class BigScreenUserController {

    @Autowired
    private UserInfoAPI userInfoAPI;

    /**
     * 统计用户数、拥有云桌面的用户数
     * @return  数量
     */
    @RequestMapping(value = "/statisticsUserDeskTop", method = RequestMethod.POST)
    DefaultWebResponse statisticsUserDeskTop() throws BusinessException {
        UserDesktopResponse response = userInfoAPI.statisticsUserDeskTop();
        return DefaultWebResponse.Builder.success(response);
    }
}
