package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cmsdocking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BaseAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RandomTokenResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 *
 * @author wjp
 */
@Controller
@RequestMapping("/rco/cmsdocking")
public class CmsDockingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsDockingController.class);

    @Autowired
    private CmsDockingAPI cmsDockingAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * @api {GET} /rco/cmsdocking/getRandomToken 获取随机token
     * @apiName 获取跳转到CmsWeb随机token
     * @apiGroup CMS对接
     * @apiDescription 获取跳转到CmsWeb随机token
     *
     * @apiSuccess (响应字段说明) {Object} content 内容
     * @apiSuccess (响应字段说明) {UUID} content.token 随机token
     * @apiSuccess (响应字段说明) {String} message
     * @apiSuccess (响应字段说明) {String[]} msgArgArr
     * @apiSuccess (响应字段说明) {String} msgKey
     * @apiSuccess (响应字段说明) {String} status 响应状态
     * @apiSuccessExample {json} 成功响应
     * {
     *      "content":{
     *          "token":"1ece0642-aeaa-408c-8abd-30e0ef2ab4c5"
     *       },
     *       "message":null,
     *       "msgArgArr":null,
     *       "msgKey":null,
     *       "status":"SUCCESS"
     *  }
     * @apiErrorExample {json} 异常响应
     * {
     *      "content":null,
     *      "message":"系统内部错误，请联系管理员",
     *      "msgArgArr":[],
     *      "msgKey":"sk_webmvckit_internal_error",
     *      "status":"ERROR"
     *  }
     *
     *  @apiErrorExample {json} 异常响应
     *  {
     *     "content": null,
     *     "message": "",
     *     "msgArgArr": [],
     *     "msgKey": "sk_webmvckit_no_login",
     *     "status": "NO_LOGIN"
     * }
     *
     */

    /**
     * 获取跳转到CmsWeb随机token
     *
     * @param sessionContext session上下文
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "getRandomToken", method = RequestMethod.GET)
    public DefaultWebResponse getRandomToken(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is not null");
        try {
            if (!sessionContext.isLogin()) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_NOT_LOGIN);
            }
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            if (baseAdminDTO == null) {
                throw new BusinessException(CmsDockingBussinessKey.RCDC_RCO_GET_TOKEN_FAIL_BY_ADMIN_NULL);
            }
            if (!DefaultAdmin.ADMIN.getName().equals(sessionContext.getUserName())) {
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ONLY_ADMIN_CAN_JUMP_TO_CMS);
            }
            BaseAdminRequest baseAdminRequest = createBaseAdminRequest(baseAdminDTO);
            RandomTokenResponse randomTokenResponse = cmsDockingAPI.getRandomToken(baseAdminRequest);
            auditLogAPI.recordLog(CmsDockingBussinessKey.RCDC_RCO_GET_TOKEN_SUCCESS);
            LOGGER.info("获取CMS跳转令牌成功。userName = {}, token = {}",
                    baseAdminRequest.getAdminName(), randomTokenResponse.getToken());
            return DefaultWebResponse.Builder.success(randomTokenResponse);
        } catch (BusinessException e) {
            LOGGER.error("获取CMS跳转令牌失败。UserName = {}", sessionContext.getUserName(), e.getI18nMessage());
            auditLogAPI.recordLog(CmsDockingBussinessKey.RCDC_RCO_GET_TOKEN_FAIL, e.getI18nMessage());
            throw new BusinessException(CmsDockingBussinessKey.RCDC_RCO_GET_TOKEN_FAIL, e, e.getI18nMessage());
        }
    }


    /**
     * 创建BaseAdminRequest对象
     *
     * @param baseAdminDTO 获取admin响应对象
     * @return 响应
     */
    private BaseAdminRequest createBaseAdminRequest(IacAdminDTO baseAdminDTO) {
        BaseAdminRequest baseAdminRequest = new BaseAdminRequest();
        baseAdminRequest.setId(baseAdminDTO.getId());
        baseAdminRequest.setAdminName(baseAdminDTO.getUserName());
        return baseAdminRequest;
    }

}
