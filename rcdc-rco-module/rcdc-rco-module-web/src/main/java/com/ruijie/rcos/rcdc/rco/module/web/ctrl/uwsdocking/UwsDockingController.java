package com.ruijie.rcos.rcdc.rco.module.web.ctrl.uwsdocking;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BaseAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RandomTokenResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: UWS 相关接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 20:20:00
 *
 * @author zjy
 */
@Controller
@RequestMapping("/rco/uwsdocking")
public class UwsDockingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsDockingController.class);

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    /**
     * 获取管理员随机token
     *
     * @param sessionContext session
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2021/11/25 17:25
     * @Author zjy
     **/
    @RequestMapping(value = "getRandomToken", method = RequestMethod.GET)
    public DefaultWebResponse getRandomToken(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null");
        try {
            if (!sessionContext.isLogin()) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_NOT_LOGIN);
            }
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            if (baseAdminDTO == null) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_NOT_EXIST_UWS);
            }
            if (!DefaultAdmin.ADMIN.getName().equals(sessionContext.getUserName())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ONLY_ADMIN_CAN_JUMP_TO_UWS);
            }
            BaseAdminRequest baseAdminRequest = new BaseAdminRequest();
            baseAdminRequest.setId(baseAdminDTO.getId());
            baseAdminRequest.setAdminName(baseAdminDTO.getUserName());
            RandomTokenResponse randomTokenResponse = uwsDockingAPI.getRandomToken(baseAdminRequest);
            LOGGER.info("获取UWS跳转令牌成功。userName = {}, token = {}", baseAdminRequest.getAdminName(), randomTokenResponse.getToken());
            return DefaultWebResponse.Builder.success(randomTokenResponse);
        } catch (BusinessException e) {
            LOGGER.error("获取UWS跳转令牌失败。UserName = {}", sessionContext.getUserName(), e.getI18nMessage());
            throw new BusinessException(UwsDockingBussinessKey.RCDC_RCO_GET_UWS_TOKEN_FAIL, e, e.getI18nMessage());
        }
    }

}
