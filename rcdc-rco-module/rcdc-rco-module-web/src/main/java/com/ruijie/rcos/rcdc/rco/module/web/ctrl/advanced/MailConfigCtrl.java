package com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced;

import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.MailMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.mail.dto.ServerMailConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced.request.BaseUpdateMailConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced.request.CheckRandomPasswordWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced.response.CheckRandomPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.validation.MailConfigCustomValidation;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月04日
 *
 * @author guoyongxin
 */
@Controller
@RequestMapping("/rco/advancedConfig")
@EnableCustomValidate(validateClass = MailConfigCustomValidation.class)
public class MailConfigCtrl {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MailConfigCtrl.class);

    @Autowired
    private MailMgmtAPI mailMgmtAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 检查是否支持随机密码生成
     * @param request 随机密码检查入参
     * @return 返回结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "检查是否支持随机密码生成")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"检查是否支持随机密码生成"})})
    @RequestMapping(value = "/checkRandomPassword")
    public DefaultWebResponse checkRandomPassword(CheckRandomPasswordWebRequest request) throws BusinessException {
        Assert.notNull(request, "CheckRandomPasswordWebRequest不能为null");

        CheckRandomPasswordResponse response = new CheckRandomPasswordResponse(false);
        FindParameterRequest findParameterRequest = new FindParameterRequest(Constants.SERVER_MAIL_CONFIG);
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);

        if (ArrayUtils.isEmpty(request.getIdArr())) {
            LOGGER.warn("用户参数缺失");
            return DefaultWebResponse.Builder.success(response);
        }

        if (findParameterResponse == null) {
            LOGGER.warn("未配置邮件服务器，邮箱全局配置key=[{}]", Constants.SERVER_MAIL_CONFIG);
            return DefaultWebResponse.Builder.success(response);
        }
        ServerMailConfigDTO serverMailConfigDTO = JSONObject.parseObject(findParameterResponse.getValue(), ServerMailConfigDTO.class);
        if (serverMailConfigDTO == null || serverMailConfigDTO.getEnableSendMail() == null) {
            LOGGER.warn("未配置邮件服务器，邮箱全局配置value=[{}]", findParameterResponse.getValue());
            return DefaultWebResponse.Builder.success(response);
        }

        if (!serverMailConfigDTO.getEnableSendMail()) {
            LOGGER.warn("邮件服务器未启用, 邮箱全局配置value=[{}]", findParameterResponse.getValue());
            return DefaultWebResponse.Builder.success(response);
        }

        //当只有一个用户时需要判断用户是否有配置邮箱，如果是批量则不判断
        if (request.getIdArr().length == 1) {
            UUID user = request.getIdArr()[0];
            IacUserDetailDTO cbbUserDetailDTO = userAPI.getUserDetail(user);
            if (StringUtils.isBlank(cbbUserDetailDTO.getEmail())) {
                LOGGER.warn("用户没有配置邮箱地址, 用户ID=[{}]", user.toString());
                return DefaultWebResponse.Builder.success(response);
            }
        }
        response = new CheckRandomPasswordResponse(true);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 获取邮件配置详情
     *
     * @return 邮件配置详情
     * @throws BusinessException 编码转换异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse detail() throws BusinessException {
        FindParameterRequest findParameterRequest = new FindParameterRequest(Constants.SERVER_MAIL_CONFIG);
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
        ServerMailConfigDTO serverMailConfigDTO = new ServerMailConfigDTO();
        if (findParameterResponse == null) {
            LOGGER.info("未配置邮件服务器，邮箱全局配置key=[{}]", Constants.SERVER_MAIL_CONFIG);
            serverMailConfigDTO.setEnableSendMail(false);
            return DefaultWebResponse.Builder.success(serverMailConfigDTO);
        }
        serverMailConfigDTO = JSONObject.parseObject(findParameterResponse.getValue(), ServerMailConfigDTO.class);
        if (serverMailConfigDTO == null || serverMailConfigDTO.getEnableSendMail() == null) {
            LOGGER.info("未配置邮件服务器，邮箱全局配置value=[{}]", findParameterResponse.getValue());
            serverMailConfigDTO = new ServerMailConfigDTO();
            serverMailConfigDTO.setEnableSendMail(false);
            return DefaultWebResponse.Builder.success(serverMailConfigDTO);
        }

        return DefaultWebResponse.Builder.success(serverMailConfigDTO);
    }

    /**
     * 更新邮件配置
     *
     * @param baseUpdateMailConfigWebRequest 邮件更新请求参数
     * @throws BusinessException 业务异常
     * @return 响应
     */
    @EnableCustomValidate(validateMethod = "updateMailConfigValidate")
    @RequestMapping(value = "/edit")
    public DefaultWebResponse edit(BaseUpdateMailConfigWebRequest baseUpdateMailConfigWebRequest) throws BusinessException {
        Assert.notNull(baseUpdateMailConfigWebRequest, "请求参数不能为空");

        ServerMailConfigDTO serverMailConfigDTO = new ServerMailConfigDTO();
        BeanUtils.copyProperties(baseUpdateMailConfigWebRequest, serverMailConfigDTO);

        UpdateParameterRequest updateParameterRequest = new UpdateParameterRequest(Constants.SERVER_MAIL_CONFIG,
                JSONObject.toJSONString(serverMailConfigDTO));

        FindParameterRequest findParameterRequest = new FindParameterRequest(Constants.SERVER_MAIL_CONFIG);
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
        if (findParameterResponse == null) {
            rcoGlobalParameterAPI.saveParameter(updateParameterRequest);
            return DefaultWebResponse.Builder.success("success");
        }
        rcoGlobalParameterAPI.updateParameter(updateParameterRequest);
        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EMAIL_UPDATE_CONFIG_SUCCESS);
        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_EMAIL_UPDATE_CONFIG_SUCCESS, new String[]{});
    }

    /**
     * 测试邮箱配置连通性
     *
     * @param baseUpdateMailConfigWebRequest 邮件测试请求参数
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @EnableCustomValidate(validateMethod = "updateMailConfigValidate")
    @RequestMapping(value = "/sendTestMail")
    public DefaultWebResponse sendTestMail(BaseUpdateMailConfigWebRequest baseUpdateMailConfigWebRequest) throws BusinessException {
        Assert.notNull(baseUpdateMailConfigWebRequest, "请求参数不能为空");

        ServerMailConfigDTO serverMailConfigDTO = new ServerMailConfigDTO();
        BeanUtils.copyProperties(baseUpdateMailConfigWebRequest, serverMailConfigDTO);

        mailMgmtAPI.testMailConfig(serverMailConfigDTO);

        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EMAIL_SEND_TEST_SUCCESS);
        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_EMAIL_SEND_TEST_SUCCESS, new String[]{});
    }

    /**
     * 重置邮箱服务器配置
     *
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("重置邮箱服务器配置")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "重置邮箱服务器配置")})
    public DefaultWebResponse resetMailConfig() throws BusinessException {

        ServerMailConfigDTO serverMailConfigDTO = new ServerMailConfigDTO();
        UpdateParameterRequest updateParameterRequest = new UpdateParameterRequest(Constants.SERVER_MAIL_CONFIG,
                JSONObject.toJSONString(serverMailConfigDTO));
        rcoGlobalParameterAPI.updateParameter(updateParameterRequest);
        auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_EMAIL_RESET_CONFIG_SUCCESS);
        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_EMAIL_RESET_CONFIG_SUCCESS, new String[]{});
    }
}