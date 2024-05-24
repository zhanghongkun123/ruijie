package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.impl;

import java.util.concurrent.RejectedExecutionException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsComponentAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.*;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.CmsDockingRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.enums.AuthTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.GetInfoRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.ModifyUserPwdRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.VerifAdminRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.VerifUserRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 *
 * @author wjp
 */
@Service
public class CmsDockingRestServerImpl implements CmsDockingRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsDockingRestServerImpl.class);


    @Autowired
    private CmsDockingAPI cmsDockingAPI;

    @Autowired
    private CmsComponentAPI cmsComponentAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;


    @Override
    public VerifUserRestServerResponse verifUser(VerifUserRestServerRequest request) {
        Assert.notNull(request, "VerifUserRestServerRequest is not null");
        String userName = request.getUserName();
        String password = request.getPassword();

        try {
            if (request.getAuthType() == AuthTypeEnum.USER) {
                return authUser(userName, password);
            }

            if (request.getAuthType() == AuthTypeEnum.ADMIN) {
                return authAdmin(userName, password);
            }
            LOGGER.error("CMS对接：登入校验失败。失败原因：登入类型不对。");
            return new VerifUserRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        } catch (Exception e) {
            LOGGER.error(String.format("CMS对接：登入校验失败。userName = %s", userName), e);
            return new VerifUserRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public ModifyUserPwdRestServerResponse modifyUserPwd(ModifyUserPwdRestServerRequest request) {
        Assert.notNull(request, "request must not null");

        LOGGER.info("CMS对接：收到CMS修改用户密码请求为：{}", JSON.toJSONString(request));

        try {
            ModifyUserPwdRequest modifyUserPwdRequest = new ModifyUserPwdRequest();
            BeanUtils.copyProperties(request, modifyUserPwdRequest);

            ModifyUserPwdResponse modifyUserPwdResponse =  cmsDockingAPI.modifyUserPwd(modifyUserPwdRequest);
            ModifyUserPwdRestServerResponse response = new ModifyUserPwdRestServerResponse();
            BeanUtils.copyProperties(modifyUserPwdResponse, response);

            return response;
        } catch (Exception e) {
            LOGGER.error(String.format("CMS对接：用户[%s]修改密码失败", request.getUserName()), e);

            return new ModifyUserPwdRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public UserPwdStrategyResponse getUserPwdStrategy() {
        UserPwdStrategyResponse response = new UserPwdStrategyResponse();
        PwdStrategyDTO pwdStrategy = certificationStrategyParameterAPI.getPwdStrategy();
        BeanUtils.copyProperties(pwdStrategy, response);

        // 兼容旧的状态码
        if (response.getPwdLevel() != null) {
            int levelNum = Integer.parseInt(response.getPwdLevel());
            levelNum = levelNum > 0 ? levelNum - 1 : levelNum;
            response.setPwdLevel(String.valueOf(levelNum));
        }
        LOGGER.info("CMS对接：用户安全红线策略为：[{}]", JSON.toJSONString(response));
        return response;
    }

    /**
     * 管理员校验
     *
     * @param userName 账号
     * @param password 密码
     * @return 响应
     * @throws BusinessException 业务异常
     */
    private VerifUserRestServerResponse authAdmin(String userName, String password) throws BusinessException {
        LoginAdminRequest loginAdminRequest = new LoginAdminRequest();
        loginAdminRequest.setAdminName(userName);
        loginAdminRequest.setPassword(password);
        LoginAdminResponse loginAdminResponse = cmsDockingAPI.loginAdmin(loginAdminRequest);
        LOGGER.info("CMS对接：管理员登入校验完成，登录返回信息为：[{}]", JSON.toJSONString(loginAdminResponse));

        VerifUserRestServerResponse response = new VerifUserRestServerResponse();
        BeanUtils.copyProperties(loginAdminResponse, response);

        return response;
    }

    /**
     * 普通用户校验
     *
     * @param userName 用户名
     * @param password 密码
     * @return 响应
     * @throws BusinessException 业务异常
     */
    private VerifUserRestServerResponse authUser(String userName, String password) throws BusinessException {
        LoginUserRequest loginUserRequest = new LoginUserRequest(userName, password);
        LoginUserResponse loginUserResponse = cmsDockingAPI.loginUser(loginUserRequest);
        
        LOGGER.info("CMS用户[{}]登入校验完成。 loginUserResponse = {}", userName, JSON.toJSONString(loginUserResponse));
        
        VerifUserRestServerResponse response = new VerifUserRestServerResponse();
        BeanUtils.copyProperties(loginUserResponse, response);
        LOGGER.info("CMS对接：用户[{}]登入校验完成。 verifUserRestServerResponse = {}", userName, JSON.toJSONString(loginUserResponse));

        return response;
    }


    @Override
    public VerifAdminRestServerResponse verifAdmin(VerifAdminRestServerRequest request) {
        Assert.notNull(request, "VerifAdminRestServerRequest is not null");
        try {
            VerifAdminRequest verifAdminRequest = new VerifAdminRequest();
            verifAdminRequest.setToken(request.getToken());
            VerifAdminResponse verifAdminResponse = cmsDockingAPI.verifAdmin(verifAdminRequest);
            LOGGER.info("CMS管理员登入校验完成。 authCode = {} , adminName = {}, token = {}",
                verifAdminResponse.getAuthCode(), verifAdminResponse.getAdminName(),
                request.getToken());
            VerifAdminRestServerResponse verifAdminRestServerResponse = new VerifAdminRestServerResponse();
            verifAdminRestServerResponse.setAuthCode(verifAdminResponse.getAuthCode());
            verifAdminRestServerResponse.setAdminName(verifAdminResponse.getAdminName());
            verifAdminRestServerResponse.setPassword(verifAdminResponse.getPassword());
            verifAdminRestServerResponse.setCreateTime(verifAdminResponse.getCreateTime());
            return verifAdminRestServerResponse;
        } catch (Exception e) {
            LOGGER.error("CMS管理员登入校验失败。token = {}", request.getToken(), e);
            return new VerifAdminRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public DefaultRestServerResponse getInfo(GetInfoRestServerRequest request) {
        Assert.notNull(request, "GetInfoRestServerRequest is not null");
        GetInfoRequest getInfoRequest = new GetInfoRequest();
        getInfoRequest.setInfo(request.getInfo());
        // 设置任务ID
        getInfoRequest.setTaskid(request.getTaskid());
        try {
            GetInfoResponse getInfoResponse = cmsDockingAPI.getInfo(getInfoRequest);
            LOGGER.info("CMS批量获取同步信息成功。info = {}, authCode = {}",
                getInfoRequest.getInfo().name(), getInfoResponse.getAuthCode());
            return new DefaultRestServerResponse(getInfoResponse.getAuthCode());
        } catch (RejectedExecutionException e) {
            LOGGER.error("线程满CMS批量获取同步信息失败。info = {}", getInfoRequest.getInfo().name(), e);
            return new DefaultRestServerResponse(CommonMessageCode.THREAD_BUSY);
        } catch (Exception e) {
            LOGGER.error("CMS批量获取同步信息失败。info = {}", getInfoRequest.getInfo().name(), e);
            return new DefaultRestServerResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public DefaultRestServerResponse initCmApp() {
        cmsComponentAPI.initCmApp();
        return new DefaultRestServerResponse(CommonMessageCode.SUCCESS);
    }
}
