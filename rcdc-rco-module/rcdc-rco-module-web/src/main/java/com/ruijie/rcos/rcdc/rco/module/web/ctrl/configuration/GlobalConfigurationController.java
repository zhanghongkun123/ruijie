package com.ruijie.rcos.rcdc.rco.module.web.ctrl.configuration;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.rco.module.def.configuration.enums.GlobalConfigAuditType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.configuration.dto.GlobalConfigBaseWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.configuration.dto.GlobalConfigWebRequest;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * <br>
 * Description: 全局配置 <br>
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022.08.10 <br>
 *
 * @author linhj
 */
@SuppressWarnings("DanglingJavadoc")
@Controller
@RequestMapping("/rco/configuration")
public class GlobalConfigurationController {

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * @api {post} /rco/configuration/updateConfig 更改配置
     * @apiName updateConfig
     * @apiGroup global config
     * @apiVersion 0.0.1
     * @apiDescription 更改配置
     * @apiParam (字段说明) {String} itemKey 更改配置：USER_PROTOCOL:用户协议
     * @apiParam (字段说明) {String} itemValue 配置值，USER_PROTOCOL(true/false)
     * @apiParamExample {json} 请求报文
     *                  {
     *                  "itemKey": "USER_PROTOCOL",
     *                  "itemValue": "true"
     *                  }
     * @apiSuccess (响应字段说明) {string} message 提示信息
     * @apiSuccess (响应字段说明) {string} status 状态：SUCCESS, NO_LOGIN，NO_AUTHORITY，ERROR
     * @apiSuccess (响应字段说明) {string} msgKey 国际化信息key
     * @apiSuccess (响应字段说明) {string[]} msgArgArr 国际化参数
     * @apiSuccessExample {json} 响应报文
     *                    {
     *                    "content": null,
     *                    "message": "操作成功",
     *                    "msgArgArr": null,
     *                    "msgKey": "common_operator_success",
     *                    "status": "SUCCESS"
     *                    }
     * @apiUse ErrorResponse
     */
    /**
     * 修改配置
     *
     * @param request 请求
     * @param session session
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public DefaultWebResponse updateConfig(GlobalConfigWebRequest request, SessionContext session) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(session, "session can not be null");

        globalParameterAPI.updateParameter(request.getItemKey().getValue(), request.getItemValue());
        auditLogAPI.recordLog(GlobalConfigAuditType.findAuditLogKey(request.getItemKey()), session.getUserName());

        return DefaultWebResponse.Builder.success();
    }


    /**
     * @api {get} /rco/configuration/getConfig 查询配置信息
     * @apiName getConfig
     * @apiGroup global config
     * @apiVersion 0.0.1
     * @apiDescription 查询配置信息
     * @apiParam (字段说明) {String} itemKey 更改配置：USER_PROTOCOL:用户协议
     * @apiParamExample {json} 请求报文
     *                  {
     *                  "itemKey": "USER_PROTOCOL"
     *                  }
     * @apiSuccess (响应字段说明) {Boolean} enable 日志保留天数
     * @apiSuccessExample {json} 响应报文
     *                    {
     *                    "content": "true"
     *                    "message": "操作成功",
     *                    "msgArgArr": null,
     *                    "msgKey": "common_operator_success",
     *                    "status": "SUCCESS"
     *                    }
     * @apiUse ErrorResponse
     */
    /**
     * 查询配置
     *
     * @param request 请求
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public DefaultWebResponse getConfig(GlobalConfigBaseWebRequest request) {
        Assert.notNull(request, "request can not be null");

        String paramValue = globalParameterAPI.findParameter(request.getItemKey().getValue());
        return DefaultWebResponse.Builder.success(Boolean.parseBoolean(paramValue));
    }
}
