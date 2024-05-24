package com.ruijie.rcos.rcdc.rco.module.impl.api;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_IAC_ADMIN_LIMITED_LOGIN;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_IAC_ADMIN_LIMITED_LOGIN_FOREVER;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_IAC_ADMIN_REMAIN_ERROR_TIMES;
import static com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum.INNER;
import static com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_USER_CHANGE_PWD_FAIL;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.IacPageSearchRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacGetAdminPageRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacValidateAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacEncryptionMode;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUpdatePasswordType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ModifyPasswordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CmsInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SyncAdminContentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CmsDockingAdminOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DefaultAdmin;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.InfeTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BaseAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.LoginAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.LoginUserRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ModifyUserPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SyncAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.VerifAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CmsInfoRespone;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.LoginAdminResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.LoginUserResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ModifyUserPwdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RandomTokenResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.VerifAdminResponse;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CmsLoginCode;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserGroupOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserGroupOperNotifyContentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperNotifyContentDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.AdminOperNotifyContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.AdminLoginExceptionEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.CmsAdminLoginFailEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.CmsDockingProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.ChangeUserPwdHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ChangeUserPwdCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.maintenance.MaintenanceModeValidator;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.shell.ShellCommandRunner;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 *
 * @author wjp
 */
public class CmsDockingAPIImpl implements CmsDockingAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsDockingAPIImpl.class);

    private static final int QUERY_PAGE_SIZE = 1000;

    /**
     * 最大3W
     */
    private static final int QUERY_MQ_INFO_MAX_SIZE = 30;

    /**
     * 一天有多少毫秒
     */
    private static final long ONE_DAY_MILLIS = 86400000L;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CmsDockingProducerAPI cmsDockingProducerAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Autowired
    private DesService desService;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private MaintenanceModeValidator maintenanceModeValidator;

    @Autowired
    private ChangeUserPwdHelper changeUserPwdHelper;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    protected UserService userService;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private ModifyPasswordAPI modifyPasswordAPI;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcoInvalidTimeHelper rcoInvalidTimeHelper;

    /**
     * 用于存放临时令牌 key：临时令牌， value：管理员数据
     */
    private ConcurrentHashMap<String, VerifAdminResponse> tokenMap = new ConcurrentHashMap<>();

    /**
     * cms处理线程池,分配1个线程数
     */
    // ArrayBlockingQueue是一个有界缓存等待队列，可以指定缓存队列的大小，当正在执行的线程数等于corePoolSize时，
    // 多余的元素缓存在ArrayBlockingQueue队列中等待有空闲的线程时继续执行，
    // 当ArrayBlockingQueue已满时，加入ArrayBlockingQueue失败，会开启新的线程去执行，
    // 当线程数已经达到最大的maximumPoolSizes时，再有新的元素尝试加入ArrayBlockingQueue时会报错
    private static final ExecutorService CMS_HANDLER_THREAD_POOL =
            new ThreadPoolExecutor(1, 1, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    Assert.notNull(r, "r is null");
                    Assert.notNull(executor, "executor is null");

                    throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + executor.toString());
                }
            });


    /**
     * 保留最大文件数
     */
    private static final int MAX_FILE_NUMBER = 4;


    @Override
    public GetInfoResponse getInfo(GetInfoRequest request) throws BusinessException {
        Assert.notNull(request, "GetInfoRequest is null");
        GetInfoResponse response = new GetInfoResponse();
        InfeTypeEnum infeTypeEnum = request.getInfo();
        if (infeTypeEnum == null) {
            response.setAuthCode(CommonMessageCode.CODE_ERR_OTHER);
            LOGGER.error("CMS一键同步失败。原因：同步类型为空。");
            return response;
        }
        switch (infeTypeEnum) {
            case ALL:
                syncAll(request);
                break;
            case USER:
                // CMS不会用这个分支 如果出现文件方式同步无法同步，
                syncUserMessageToCms();
                break;
            case USER_GROUP:
                syncUserGroupMessageToCms();
                break;
            case ADMIN:
                syncAdmins();
                break;
            default:
                LOGGER.error("CMS一键同步失败。原因：同步类型不存在。");
                break;
        }
        response.setAuthCode(CommonMessageCode.SUCCESS);
        return response;
    }

    @Override
    public LoginAdminResponse loginAdmin(LoginAdminRequest loginAdminRequest) throws BusinessException {
        Assert.notNull(loginAdminRequest, "LoginAdminRequest is null");
        Assert.hasText(loginAdminRequest.getAdminName(), "adminName must not be null");
        Assert.hasText(loginAdminRequest.getPassword(), "password must not be null");
        
        try {

            IacAdminDTO baseAdminDTO = adminMgmtAPI.getAdminByUserName(loginAdminRequest.getAdminName());
            if (baseAdminDTO == null) {
                LOGGER.error("CMS对接：管理员登入验证失败。失败原因：返回结果内IacAdminDTO为null。adminName = {}", loginAdminRequest.getAdminName());
                return new LoginAdminResponse(CommonMessageCode.CODE_ERR_OTHER);
            }

            IacValidateAdminPwdRequest request = new IacValidateAdminPwdRequest();
            request.setUserName(loginAdminRequest.getAdminName());
            request.setPwd(loginAdminRequest.getPassword());
            request.setKey(RedLineUtil.getRealAdminRedLine());
            request.setEncryptionMode(IacEncryptionMode.SHA256);
            request.setLoginIp(null);
            request.setSubSystem(SubSystem.CDC);
            if (!baseAdminMgmtAPI.validateAdminPwd(request)) {
                LOGGER.error("CMS对接：管理员登入验证失败。失败原因：不是admin。adminName = {}", loginAdminRequest.getAdminName());
                return new LoginAdminResponse(CommonMessageCode.CODE_ERR_OTHER);
            }

            // 判断该管理员是否为超级管理员，如果不是直接返回错误
            boolean isAdmin = DefaultAdmin.ADMIN.getName().equals(loginAdminRequest.getAdminName());
            if (!isAdmin) {
                LOGGER.error("CMS对接：管理员登入验证失败。失败原因：不是admin。adminName = {}", loginAdminRequest.getAdminName());
                return new LoginAdminResponse(CommonMessageCode.CODE_ERR_OTHER);
            }

            LOGGER.info("CMS对接：管理员登入验证成功。adminName = {}", loginAdminRequest.getAdminName());

            LoginAdminResponse response = new LoginAdminResponse();
            response.setAuthCode(CommonMessageCode.SUCCESS);
            response.setHasFirstTimeLoggedIn(baseAdminDTO.getHasFirstTimeLoggedIn());
            LOGGER.info("CMS对接：管理员登入验证成功，响应消息：[{}]", JSON.toJSONString(response));

            return response;
        } catch (BusinessException e) {
            LOGGER.error("CMS对接：管理员{}验证异常", loginAdminRequest.getAdminName(), e);
            LoginAdminResponse response = new LoginAdminResponse();
            Integer code = CmsAdminLoginFailEnum.getCorrespondingCode(e.getKey());
            List<Integer> errorMsgIntegerInfoList = AdminLoginExceptionEnum.getErrorMsgIntegerInfo(e);
            switch (e.getKey()) {
                case RCDC_IAC_ADMIN_REMAIN_ERROR_TIMES:
                    response.setAuthCode(CmsLoginCode.REMIND_ADMIN_REMAINING_TIMES);
                    response.setRemainingTimes(errorMsgIntegerInfoList.get(0));
                    break;
                case RCDC_IAC_ADMIN_LIMITED_LOGIN:
                    response.setAuthCode(CmsLoginCode.ADMIN_LOCKED);
                    Integer pwdLockTime = errorMsgIntegerInfoList.get(0);
                    response.setPwdLockTime(Math.max(pwdLockTime , 1));
                    break;
                // CMS缺少永久锁定提示，规避解法：提示锁定时间99999分钟
                case RCDC_IAC_ADMIN_LIMITED_LOGIN_FOREVER:
                    response.setAuthCode(CmsLoginCode.ADMIN_LOCKED);
                    response.setPwdLockTime(99999);
                    break;
                default:
                    response.setAuthCode(code);
            }
            return response;
        } catch (Exception e) {
            LOGGER.error(String.format("CMS对接：管理员登入验证失败。adminName = [%s]", loginAdminRequest.getAdminName()), e);
            return new LoginAdminResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    @Override
    public LoginUserResponse loginUser(LoginUserRequest loginUserRequest) throws BusinessException {
        Assert.notNull(loginUserRequest, "LoginUserRequest must not be null");
        Assert.hasText(loginUserRequest.getUserName(), "userName must not be null");
        Assert.hasText(loginUserRequest.getPassword(), "password must not be null");


        String userName = loginUserRequest.getUserName();
        String password = loginUserRequest.getPassword();

        IacAuthUserResultDTO authUserResultDTO = authUser(userName, password);

        LoginUserResponse response;
        try {
            response = doAuthResult(authUserResultDTO);

            return response;
        } catch (Exception e) {
            LOGGER.error(String.format("CMS对接：用户[%s]登录失败", userName), e);
            response = new LoginUserResponse(CommonMessageCode.CODE_ERR_OTHER);

            return response;
        }
    }

    private LoginUserResponse doAuthResult(IacAuthUserResultDTO authUserResponse) throws BusinessException {
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(authUserResponse.getUserName());
        if (userDetailDTO == null) {
            LOGGER.info("CMS对接：数据库中不存在用户[{}]", authUserResponse.getUserName());
            return new LoginUserResponse(CommonMessageCode.CODE_ERR_OTHER);
        }

        int authCode = authUserResponse.getAuthCode();

        if (LoginMessageCode.SUCCESS == authCode) {
            LOGGER.info("CMS对接：用户[{}]鉴权成功，构建返回结果", authUserResponse.getUserName());
            return buildLoginSuccessResponse(authCode, userDetailDTO);
        }

        LOGGER.info("CMS对接：用户[{}]鉴权失败，构建返回结果", authUserResponse.getUserName());
        return buildLoginErrorResponse(authCode, authUserResponse.getUserName());
    }

    private LoginUserResponse buildLoginErrorResponse(Integer autCode, String userName) {
        LOGGER.info("CMS对接：用户[{}]鉴权失败，构建返回结果，返回码为：[{}]", userName, autCode);

        LoginUserResponse errorResponse = new LoginUserResponse();
        errorResponse.setAuthCode(autCode);

        // 密码认证策略的错误
        if (autCode == LoginMessageCode.USER_LOCKED || autCode == LoginMessageCode.REMIND_ERROR_TIMES) {
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            if (!pwdStrategyDTO.getPreventsBruteForce()) {
                LOGGER.info("CMS对接：未开启防爆配置，用户[{}]登录失败返回消息", userName);

                return errorResponse;
            }

            if (autCode == LoginMessageCode.USER_LOCKED) {
                errorResponse.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
                LOGGER.info("CMS对接：用户[{}]被锁定，返回登录失败信息为：[{}]", userName, JSON.toJSONString(errorResponse));

                return errorResponse;
            }

            // 用户被锁定
            LOGGER.info("CMS对接：用户[{}]被锁定，返回登录失败信息", userName);
            return buildRemainingTimesMsg(errorResponse, pwdStrategyDTO, userName);
        }

        LOGGER.info("CMS对接：未开启防爆配置，用户[{}]登录失败返回消息", userName);
        return errorResponse;
    }

    private LoginUserResponse buildRemainingTimesMsg(LoginUserResponse errorResponse, PwdStrategyDTO pwdStrategyDTO, String userName) {
        LOGGER.info("CMS对接：用户[{}]鉴权失败，提示密码剩余次数", userName);
        RcoViewUserEntity userEntity = userService.getUserInfoByName(userName);
        if (userEntity == null) {
            LOGGER.info("CMS对接：获取用户RcoViewUserEntity信息为空，用户[{}]", userName);
            return errorResponse;
        }

        errorResponse.setRemainingTimes(pwdStrategyDTO.getUserLockedErrorTimes() - userEntity.getPwdErrorTimes());
        return errorResponse;
    }

    private LoginUserResponse buildLoginSuccessResponse(int code, IacUserDetailDTO userDetailDTO) throws BusinessException {

        LoginUserResponse response = new LoginUserResponse();
        // 判断用户账号是否失效和过期
        Integer authCode = rcoInvalidTimeHelper.obtainLoginStateCode(code, userDetailDTO);
        if (LoginMessageCode.SUCCESS != authCode) {
            response.setAuthCode(authCode);
            LOGGER.info("cms登录校验用户已经失效或者过期，用户为：{}，code为：{}", userDetailDTO.getUserName(), authCode);
            return response;
        }
        response.setAuthCode(code);
        if (IacUserTypeEnum.NORMAL == userDetailDTO.getUserType()) {
            response.setNeedUpdatePassword(needUpdatePasswordWork(userDetailDTO, response));
        }

        IacUserDetailDTO cbbUserInfoDTO = cbbUserAPI.getUserByName(userDetailDTO.getUserName());
        response.setName(cbbUserInfoDTO.getRealName());
        response.setUserGroupId(cbbUserInfoDTO.getGroupId());
        response.setState(cbbUserInfoDTO.getUserState());
        response.setUserType(cbbUserInfoDTO.getUserType());
        response.setId(cbbUserInfoDTO.getId());

        LOGGER.info("CMS对接：用户[{}]登录成功构建的返回结果为：[{}]", userDetailDTO.getUserName(), JSON.toJSONString(response));
        return response;
    }

    private boolean needUpdatePasswordWork(IacUserDetailDTO userDetailDTO, LoginUserResponse response) throws BusinessException {
        String userName = userDetailDTO.getUserName();
        LOGGER.info("CMS对接：校验用户[{}]是否需要修改密码", userName);
        // 调用gss接口判断用户是否需要需要修改密码
        IacUserDetailDTO iacUserDetailDTO = cbbUserAPI.getUserByName(userName);
        LOGGER.info("cms调用gss返回判断用户是否需要修改密码返回为：{}", JSON.toJSONString(iacUserDetailDTO));
        Boolean enableNeedUpdatePassword = iacUserDetailDTO.getNeedUpdatePassword();
        IacUpdatePasswordType updatePasswordType = iacUserDetailDTO.getUpdatePasswordType();
        if (BooleanUtils.isFalse(enableNeedUpdatePassword)) {
            if (updatePasswordType == IacUpdatePasswordType.ALARM) {
                response.setPwdSurplusDays(iacUserDetailDTO.getPasswordRemindDays());
            }
            return false;
        }
        // 用户正常无需修改密码
        if (updatePasswordType == IacUpdatePasswordType.NORMAL) {
            return false;
        }
        // 提醒密码即将到期
        if (updatePasswordType == IacUpdatePasswordType.ALARM) {
            response.setPwdSurplusDays(iacUserDetailDTO.getPasswordRemindDays());
            return false;
        }
        // 密码是否过期
        if (updatePasswordType == IacUpdatePasswordType.EXPIRE) {
            response.setPasswordExpired(true);
        }
        // 校验密码复杂度
        if (updatePasswordType == IacUpdatePasswordType.WEAK) {
            response.setPasswordLevelChange(true);
        }

        return true;
    }

    private boolean checkPwdExpired(Date updatePwdTime, PwdStrategyDTO pwdStrategyDTO, LoginUserResponse response) {
        String name = response.getName();
        LOGGER.info("CMS对接：校验用户[{}]密码是否过期，修改密码时间为：[{}]，密码有效天数为：[{}]", name, updatePwdTime, pwdStrategyDTO.getUpdateDays());

        Integer days = pwdStrategyDTO.getUpdateDays();
        long timeDiff = System.currentTimeMillis() - updatePwdTime.getTime();

        // 判断密码有效期设置
        if (days == null || days <= 0) {
            LOGGER.info("CMS对接：密码有效期天数为[{}]，用户[{}]密码未过期", days, name);

            return false;
        }

        // 密码已过期
        if (timeDiff >= days * ONE_DAY_MILLIS) {
            LOGGER.info("CMS对接：密码有效期天数为[{}]，用户[{}]密码已过期", days, name);
            response.setPasswordExpired(true);

            return true;
        }

        int surplus = loginHelper.computePwdSurplusDays(updatePwdTime, days);
        // 如果开启密码安全管理，并且密码剩余有效天数小于设定的值
        if (pwdStrategyDTO.getSecurityStrategyEnable() && surplus < pwdStrategyDTO.getPwdExpireRemindDays()) {
            // 提示剩余的天数
            LOGGER.info("CMS对接：用户[{}]密码未过期，密码有效期天数为[{}]，剩余有效天数为[{}]提示密码剩余有效天数", name, days, surplus);
            response.setPwdSurplusDays(surplus);

            return false;
        }
        LOGGER.info("CMS对接：未开启本地密码安全配置，密码不会过期");

        return false;
    }


    private IacAuthUserResultDTO authUser(String userName, String password) {
        try {
            IacAuthUserResultDTO authUserResponse;
            // 前置处理
            int resultCode = processLogin(userName);
            // 若用户被锁定或者不存在
            if (LoginMessageCode.SUCCESS != resultCode) {
                authUserResponse = new IacAuthUserResultDTO(resultCode);
                authUserResponse.setUserName(userName);

                LOGGER.info("CMS对接：用户[{}]认证不成功，返回信息为：[{}]", userName, JSON.toJSONString(authUserResponse));
                return authUserResponse;
            }

            IacAuthUserDTO authUserRequest = new IacAuthUserDTO(userName, password);
            // cms认证忽略图形校验码和设备id传参
            authUserRequest.setDeviceId(UUID.randomUUID().toString());
            authUserRequest.setShouldCheckCaptchaCode(false);
            authUserRequest.setSubSystem(SubSystem.CDC);
            authUserResponse = cbbUserAPI.authUser(authUserRequest);
            postAuth(userName, authUserResponse);
            LOGGER.info("CMS对接：用户[{}]登录认证完成，认证码：[{}]", userName, authUserResponse.getAuthCode());

            return authUserResponse;
        } catch (BusinessException e) {
            LOGGER.error(String.format("CMS对接：用户[%s]登录校验失败", userName), e);

            return new IacAuthUserResultDTO(LoginMessageCode.CODE_ERR_OTHER);
        }
    }

    private void postAuth(String userName, IacAuthUserResultDTO authUserResponse) {
        // 当authCode==1且userEntity存在且是本地用户，这时才是密码输错
        RcoViewUserEntity userEntity = userService.getUserInfoByName(userName);
        if (userEntity == null) {
            LOGGER.info("CMS对接：用户[{}]不存在", userName);
            return;
        }

        if (IacUserTypeEnum.THIRD_PARTY == userEntity.getUserType()) {
            LOGGER.info("CMS对接：用户[{}]为第三方用户，不受密码策略管控", userName);
            return;
        }

        // 密码输入正确后，解锁用户（不判断是否上锁）
        if (authUserResponse.getAuthCode() == LoginMessageCode.SUCCESS) {
            LOGGER.info("CMS对接：用户[{}]密码正确，解锁用户", userName);
            return;
        }

        PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
        boolean enablePreventsBruteForce = BooleanUtils.isTrue(pwdStrategyDTO.getPreventsBruteForce());
        // 未开启防爆
        if (!enablePreventsBruteForce) {
            return;
        }

        // 并非密码错误
        if (authUserResponse.getAuthCode() != LoginMessageCode.USERNAME_OR_PASSWORD_ERROR) {
            return;
        }

        LOGGER.info("CMS对接：用户[{}]密码不正确，修改错误次数或者锁定用户", userName);
        certificationHelper.changeErrorTimesAndLock(userName, pwdStrategyDTO, authUserResponse);
    }

    /**
     * 前置校验，查看用户是否存在、是否被锁定
     *
     * @param userName 用户名
     * @return 状态码
     */
    private int processLogin(String userName) {
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(userName);
        if (userDetailDTO == null) {
            LOGGER.info("CMS对接：数据库中不存在用户[{}]", userName);
            return LoginMessageCode.USERNAME_OR_PASSWORD_ERROR;
        }

        return certificationHelper.isLocked(userName) ? LoginMessageCode.USER_LOCKED : LoginMessageCode.SUCCESS;
    }

    /**
     * 全量同步：包括全部管理员、用户组、用户
     */
    private void syncAll(GetInfoRequest request) throws BusinessException {
        syncUserGroupMessageToCms();
        syncAdmins();
        // 新版的TaskId是不会为空的，如果是空说明是旧版，走旧版接口
        if (request.getTaskid() == null) {
            syncUserMessageToCms();
        } else {
            syncUserMessageToCmsByFile(request);
        }

    }

    /**
     * 通知CMS 通过MQ消息
     * 
     * @throws BusinessException
     */
    private void syncUserMessageToCms() throws BusinessException {

        List<UserOperNotifyContentDTO> cbbUserOperNotifyContentDTOList = pageQueryAllUser();
        UserOperNotifyContentDTO[] cbbUserOperNotifyContentDTOArr = cbbUserOperNotifyContentDTOList.toArray(new UserOperNotifyContentDTO[0]);
        notifyUsersChange(cbbUserOperNotifyContentDTOArr);

    }

    /**
     * 平台api不支持获取全部数据，通过分页方式获取
     *
     * @return 返回
     * @throws BusinessException 业务
     */
    private List<UserOperNotifyContentDTO> pageQueryAllUser() throws BusinessException {
        // 查询请求构造
        IacPageSearchRequest iacPageSearchRequest = new IacPageSearchRequest();
        // 初始化分页0
        int page = 0;
        iacPageSearchRequest.setPage(page);
        // 每页最大1000
        iacPageSearchRequest.setLimit(QUERY_PAGE_SIZE);
        List<UserOperNotifyContentDTO> cbbUserList = new ArrayList<>();
        while (true) {
            DefaultPageResponse<IacUserDetailDTO> pageResponse = cbbUserAPI.originPageQuery(iacPageSearchRequest);
            IacUserDetailDTO[] itemArr = pageResponse.getItemArr();
            if (ArrayUtils.isEmpty(itemArr)) {
                return cbbUserList;
            }
            // 页码数量自增
            iacPageSearchRequest.setPage(++page);
            List<UserOperNotifyContentDTO> userList = Arrays.asList(itemArr).stream().map(userListDTO -> {
                UserOperNotifyContentDTO contentDTO = new UserOperNotifyContentDTO();
                contentDTO.setId(userListDTO.getId());
                contentDTO.setGroupId(userListDTO.getGroupId());
                contentDTO.setPassword(userListDTO.getPassword());
                contentDTO.setRealName(userListDTO.getRealName());
                contentDTO.setUserName(userListDTO.getUserName());
                contentDTO.setState(userListDTO.getUserState());
                contentDTO.setUserType(userListDTO.getUserType());
                return contentDTO;
            }).collect(Collectors.toList());

            cbbUserList.addAll(userList);
        }
    }



    private void syncUserMessageToCmsByFile(GetInfoRequest request) {
        // 提交任务
        CMS_HANDLER_THREAD_POOL.submit(new Callable<String>() {
            /**
             * 任务的具体过程，一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行。
             *
             * @return
             */
            @Override
            public String call() {
                asynUserMessageToCmsByFile(request);
                return "call()方法被自动调用，任务的结果是：" + request.getTaskid().toString() + "    " + Thread.currentThread().getName();
            }

        });
    }









    private void notifyUsersChange(UserOperNotifyContentDTO[] contentDTOArr) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oper", UserOperNotifyEnum.SYNC_USERS.getOper());
            jsonObject.put("timestamp", System.currentTimeMillis());
            jsonObject.put("content", contentDTOArr);
            cmsDockingProducerAPI.syncMessageToCMS(jsonObject.toJSONString());
        } catch (Exception e) {
            LOGGER.error("CMS对接：用户同步失败。content = {}", JSONObject.toJSONString(contentDTOArr), e);
        }
    }

    private void syncUserGroupMessageToCms() throws BusinessException {
        List<IacUserGroupDetailDTO> allUserGroupList = pageQueryAllUserGroup();
        UserGroupOperNotifyContentDTO[] contentDTOArr = allUserGroupList.stream().map(userGroupDetailDTO -> {
            UserGroupOperNotifyContentDTO contentDTO = new UserGroupOperNotifyContentDTO();
            contentDTO.setId(userGroupDetailDTO.getId());
            contentDTO.setName(userGroupDetailDTO.getName());
            contentDTO.setParentId(userGroupDetailDTO.getParentId());
            contentDTO.setCreateTime(userGroupDetailDTO.getCreateTime());
            return contentDTO;
        }).toArray(UserGroupOperNotifyContentDTO[]::new);

        notifyUserGroupsChange(contentDTOArr);

    }

    /**
     * 平台api不支持获取全部数据，通过分页方式获取
     *
     * @return 返回
     * @throws BusinessException 业务
     */
    private List<IacUserGroupDetailDTO> pageQueryAllUserGroup() throws BusinessException {
        IacPageSearchRequest iacPageSearchRequest = new IacPageSearchRequest();
        int page = 0;
        iacPageSearchRequest.setPage(page);
        iacPageSearchRequest.setLimit(QUERY_PAGE_SIZE);

        List<IacUserGroupDetailDTO> userGroupList = Lists.newArrayList();
        while (true) {
            DefaultPageResponse<IacUserGroupDetailDTO> pageResponse = cbbUserGroupAPI.pageQuery(iacPageSearchRequest);
            IacUserGroupDetailDTO[] itemArr = pageResponse.getItemArr();
            if (ArrayUtils.isEmpty(itemArr)) {
                LOGGER.info("all user group found, count=" + userGroupList.size());
                return userGroupList;
            }
            userGroupList.addAll(Arrays.asList(itemArr));
            iacPageSearchRequest.setPage(++page);
        }
    }


    private void notifyUserGroupsChange(UserGroupOperNotifyContentDTO[] contentDTOArr) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oper", UserGroupOperNotifyEnum.SYNC_USER_GROUPS.getOper());
            jsonObject.put("timestamp", System.currentTimeMillis());
            jsonObject.put("content", contentDTOArr);
            cmsDockingProducerAPI.syncMessageToCMS(jsonObject.toJSONString());
        } catch (Exception e) {
            LOGGER.error("CMS对接：用户组同步失败。content = {}", JSONObject.toJSONString(contentDTOArr), e);
        }
    }


    /**
     * 全量同步：超级管理员
     */
    private void syncAdmins() throws BusinessException {
        List<IacAdminDTO> baseAdminDTOList = getAllAdminSuperPrivilege();
        if (CollectionUtils.isEmpty(baseAdminDTOList)) {
            LOGGER.error("CMS对接：同步管理员失败。失败原因：不存在超级管理员。");
            return;
        }
        List<AdminOperNotifyContentDTO> contentDTOList = buildSyncAdminsData(baseAdminDTOList);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("oper", CmsDockingAdminOperNotifyEnum.SYNC_ADMINS.getOper());
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("content", contentDTOList);
        cmsDockingProducerAPI.syncMessageToCMS(jsonObject.toJSONString());
    }

    /**
     * 获取所有超级管理员
     */
    private List<IacAdminDTO> getAllAdminSuperPrivilege() throws BusinessException {
        List<IacAdminDTO> baseAdminDTOList = new ArrayList<>();
        int page = 0;
        while (true) {
            IacGetAdminPageRequest baseRequest = new IacGetAdminPageRequest();
            baseRequest.setPage(page);
            baseRequest.setLimit(Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT);
            // 只查CDC
            baseRequest.setSubSystem(SubSystem.CDC);
            DefaultPageResponse<IacAdminDTO> response = baseAdminMgmtAPI.getAdminPage(baseRequest);
            page++;
            if (ArrayUtils.isEmpty(response.getItemArr())) {
                return baseAdminDTOList;
            }
            Map<UUID, IacRoleDTO> baseRoleDTOMap = buildBaseRoleDTOMap(response.getItemArr());

            if (CollectionUtils.isEmpty(baseRoleDTOMap) && response.getTotal() <= page * Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                return baseAdminDTOList;
            }

            for (IacAdminDTO baseAdminDTO : response.getItemArr()) {
                if (ArrayUtils.isEmpty(baseAdminDTO.getRoleIdArr())) {
                    continue;
                }
                for (UUID id : baseAdminDTO.getRoleIdArr()) {
                    IacRoleDTO baseRoleDTO = baseRoleDTOMap.get(id);
                    if (baseRoleDTO.getHasSuperPrivilege()) {
                        baseAdminDTOList.add(baseAdminDTO);
                        break;
                    }
                }
            }
            if (response.getTotal() <= page * Constants.CMS_DOCKING_GET_ADMIN_PAGE_LIMIT) {
                return baseAdminDTOList;
            }
        }
    }

    /**
     * 构建需要同步的超级管理员数组
     */
    private List<AdminOperNotifyContentDTO> buildSyncAdminsData(List<IacAdminDTO> baseAdminDTOList) throws BusinessException {
        List<AdminOperNotifyContentDTO> contentDTOList = new ArrayList<>();
        for (IacAdminDTO baseAdminDTO : baseAdminDTOList) {
            AdminOperNotifyContentDTO contentDTO = new AdminOperNotifyContentDTO();
            contentDTO.setAdminName(baseAdminDTO.getUserName());
            IdRequest idRequest = new IdRequest();
            idRequest.setId(baseAdminDTO.getId());
            GetAdminPasswordResponse response = adminManageAPI.getAdminPassword(idRequest);
            contentDTO.setPassword(response.getPassword());
            contentDTOList.add(contentDTO);
        }
        return contentDTOList;
    }

    /**
     * 构建角色MAP
     */
    private Map<UUID, IacRoleDTO> buildBaseRoleDTOMap(IacAdminDTO[] baseAdminDTOArr) throws BusinessException {
        Set<UUID> roleIdSet = Sets.newHashSet();
        Map<UUID, IacRoleDTO> roleMap = new HashMap<>();
        for (IacAdminDTO baseAdminDTO : baseAdminDTOArr) {
            if (ArrayUtils.isEmpty(baseAdminDTO.getRoleIdArr())) {
                LOGGER.info("管理员[{}]无角色信息", baseAdminDTO.getUserName());
                continue;
            }
            roleIdSet.addAll(Arrays.asList(baseAdminDTO.getRoleIdArr()));
        }
        if (CollectionUtils.isEmpty(roleIdSet)) {
            return roleMap;
        }
        UUID[] roleIdArr = new UUID[roleIdSet.size()];

        List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdSet.toArray(roleIdArr));
        for (IacRoleDTO baseRoleDTO : baseRoleDTOList) {
            if (roleMap.containsKey(baseRoleDTO.getId())) {
                LOGGER.debug("map缓存中已包含角色信息，角色id[{}]", baseRoleDTO.getId());
                continue;
            }
            roleMap.put(baseRoleDTO.getId(), baseRoleDTO);
        }
        return roleMap;
    }

    @Override
    public RandomTokenResponse getRandomToken(BaseAdminRequest baseAdminRequest) throws BusinessException {
        Assert.notNull(baseAdminRequest, "baseAdminRequest is null");
        UUID token = UUID.randomUUID();
        RandomTokenResponse randomTokenResponse = new RandomTokenResponse(token);
        VerifAdminResponse verifAdminResponse = new VerifAdminResponse();
        verifAdminResponse.setCreateTime(System.currentTimeMillis());
        verifAdminResponse.setAdminName(baseAdminRequest.getAdminName());

        IdRequest idRequest = new IdRequest();
        idRequest.setId(baseAdminRequest.getId());
        GetAdminPasswordResponse getAdminPasswordResponse = adminManageAPI.getAdminPassword(idRequest);
        verifAdminResponse.setPassword(getAdminPasswordResponse.getPassword());
        clearTokenMapByOvertime();
        tokenMap.put(token.toString(), verifAdminResponse);
        return randomTokenResponse;
    }

    /**
     * 清楚超时令牌
     */
    private void clearTokenMapByOvertime() {
        Long time = System.currentTimeMillis() - Constants.ONE_MINUTE_MILLIS;
        for (Entry<String, VerifAdminResponse> entry : tokenMap.entrySet()) {
            if (entry.getValue().getCreateTime() < time) {
                tokenMap.remove(entry.getKey());
                LOGGER.info("令牌 [{}] 超时，清除该令牌。userName = {}", entry.getKey(), entry.getValue().getAdminName());
            }
        }
    }

    @Override
    public VerifAdminResponse verifAdmin(VerifAdminRequest verifAdminRequest) throws BusinessException {
        Assert.notNull(verifAdminRequest, "VerifAdminRequest is null");
        VerifAdminResponse response = tokenMap.get(verifAdminRequest.getToken());
        if (response == null) {
            response = new VerifAdminResponse();
            response.setAuthCode(CommonMessageCode.CODE_ERR_OTHER);
            LOGGER.error("CMS管理员登入验证失败。原因：令牌无效。token = {}", verifAdminRequest.getToken());
            return response;
        }

        Long time = System.currentTimeMillis() - Constants.ONE_MINUTE_MILLIS;
        if (response.getCreateTime() < time) {
            response = new VerifAdminResponse();
            response.setAuthCode(CommonMessageCode.CODE_ERR_OTHER);
            LOGGER.error("CMS管理员登入验证失败。原因：令牌超时。token = {}", verifAdminRequest.getToken());
            tokenMap.remove(verifAdminRequest.getToken());
            return response;
        }

        response.setAuthCode(CommonMessageCode.SUCCESS);
        LOGGER.info("CMS管理员登入验证成功。token = {}, userName = {}", verifAdminRequest.getToken(), response.getAdminName());
        tokenMap.remove(verifAdminRequest.getToken());
        return response;

    }

    @Override
    public DefaultResponse syncAdmin(SyncAdminRequest syncAdminRequest) {
        Assert.notNull(syncAdminRequest, "SyncAdminRequest is null");
        Assert.notNull(syncAdminRequest.getName(), "name is null");
        Assert.notNull(syncAdminRequest.getOper(), "oper is null");
        try {
            SyncAdminContentDTO syncAdminContentDTO = new SyncAdminContentDTO();
            syncAdminContentDTO.setAdminName(syncAdminRequest.getName());
            IacAdminDTO iacAdminDTO = adminMgmtAPI.getAdminByUserName(syncAdminRequest.getName());
            GetAdminPasswordResponse adminPasswordResp = adminManageAPI.getAdminPassword(new IdRequest(iacAdminDTO.getId()));
            syncAdminContentDTO.setPassword(adminPasswordResp.getPassword());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("oper", syncAdminRequest.getOper());
            jsonObject.put("timestamp", System.currentTimeMillis());
            jsonObject.put("content", syncAdminContentDTO);
            cmsDockingProducerAPI.syncMessageToCMS(jsonObject.toJSONString());
        } catch (Exception e) {
            LOGGER.error("CMS对接：同步管理员失败。name = {}, oper = {}", syncAdminRequest.getName(), syncAdminRequest.getOper(), e);
        }
        return new DefaultResponse();
    }

    @Override
    public ModifyUserPwdResponse modifyUserPwd(ModifyUserPwdRequest request) throws BusinessException {
        Assert.notNull(request, "modifyUserPwdRequest must not null");

        LOGGER.info("CMS对接：修改用户密码信息为：[{}]", JSON.toJSONString(request));

        boolean isUnderMaintenance = maintenanceModeValidator.validate();
        if (isUnderMaintenance) {
            LOGGER.info("CMS对接：服务器处于维护模式下，无法修改用户密码");
            return new ModifyUserPwdResponse(ChangeUserPwdCode.IN_MAINTENANCE);
        }

        String userName = request.getUserName();
        IacUserDetailDTO userInfoDTO = cbbUserAPI.getUserByName(userName);
        if (userInfoDTO == null) {
            LOGGER.info("CMS对接：数据库找不到用户[{}]信息", userName);
            return new ModifyUserPwdResponse(ChangeUserPwdCode.NOT_THIS_USER);
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.AD) {
            LOGGER.info("CMS对接：AD域用户[{}]不允许修改密码", userName);
            return new ModifyUserPwdResponse(ChangeUserPwdCode.AD_USER_NOT_ALLOW_CHANGE_PASSWORD);
        }

        if (userInfoDTO.getUserType() == IacUserTypeEnum.LDAP) {
            LOGGER.info("CMS对接：LDAP用户[{}]不允许修改密码", userName);
            return new ModifyUserPwdResponse(ChangeUserPwdCode.LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD);
        }

        // 检查newPwd是否符合配置复杂度要求
        if (!changeUserPwdHelper.checkNwdPwd(request.getNewPassword(), INNER)) {
            LOGGER.info("CMS对接：用户[{}]新密码不符合密码复杂度要求", userName);

            return new ModifyUserPwdResponse(ChangeUserPwdCode.CHANGE_PASSWORD_UNABLE_REQUIRE);
        }

        // 开启防暴力破解并且用户处于锁定状态，提前返回
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(userName);
        if (userDetailDTO == null) {
            LOGGER.error("CMS对接：数据库用户信息视图找不到用户[{}]信息", userInfoDTO);

            return new ModifyUserPwdResponse(CommonMessageCode.CODE_ERR_OTHER);
        }
        if (pwdStrategyDTO.getPreventsBruteForce() && certificationHelper.isLocked(userName)) {
            LOGGER.info("CMS对接：用户[{}]被锁定", userName);

            return buildModifyUserPwdResponse(ChangeUserPwdCode.IN_LOCKED, userName, pwdStrategyDTO);
        }

        // 校验oldPwd是否正确
        if (changeUserPwdHelper.checkOldPwd(userInfoDTO.getUserName(), request.getOldPassword(), INNER)) {
            // 修改用户密码,shine发送来的密码已经是加密过的
            ModifyPasswordDTO modifyPasswordDTO =
                    changeUserPwdHelper.buildModifyPasswordDTO(userDetailDTO.getId(), request.getNewPassword(), request.getOldPassword(), INNER);

            boolean isSuccess = false;
            try {
                isSuccess = modifyPasswordAPI.modifUserPwdSyncAdminPwd(modifyPasswordDTO);
                LOGGER.info("CMS对接：修改用户[{}]密码是否成功：[{}]", userName, isSuccess);
            } catch (BusinessException e) {
                LOGGER.error(String.format("CMS对接：修改用户[%s]密码同步管理员密码失败", userName), e);
                String errorMsg = e.getAttachment(UserTipContainer.Constants.USER_TIP, e.getI18nMessage());
                auditLogAPI.recordLog(RCDC_RCO_USER_CHANGE_PWD_FAIL, userName, errorMsg);

            }

            int resultCode = isSuccess ? CommonMessageCode.SUCCESS : CommonMessageCode.CODE_ERR_OTHER;
            LOGGER.info("CMS对接：用户[{}]修改密码响应码为：[{}}", userName, resultCode);

            return new ModifyUserPwdResponse(resultCode);
        }

        // 旧密码错误
        LOGGER.info("CMS对接：修改用户[{}]密码失败，原因：原密码错误", userName);

        // 判断错误次数是否超出，是否需要提示剩余次数，超出就锁定
        int responseCode;
        if (pwdStrategyDTO.getPreventsBruteForce()
                && (responseCode = changeUserPwdHelper.checkNeedRemindOrLock(userName)) != ChangeUserPwdCode.OLD_PASSWORD_ERROR) {
            LOGGER.info("CMS对接：防爆功能开启，用户[{}]密码错误，认证码为[{}]", userName, responseCode);
            return buildModifyUserPwdResponse(responseCode, userName, pwdStrategyDTO);
        }

        // 没有开启防暴，并且密码错误
        return buildModifyUserPwdResponse(ChangeUserPwdCode.OLD_PASSWORD_ERROR, userName, pwdStrategyDTO);
    }

    private ModifyUserPwdResponse buildModifyUserPwdResponse(int code, String userName, PwdStrategyDTO pwdStrategyDTO) {
        ModifyUserPwdResponse response = new ModifyUserPwdResponse();
        RcoViewUserEntity userEntity = userService.getUserInfoByName(userName);
        if (userEntity == null) {
            LOGGER.info("CMS对接：获取用户RcoViewUserEntity信息为空，用户[{}]", userName);
            return response;
        }

        // 未开启防暴
        if (!pwdStrategyDTO.getPreventsBruteForce()) {
            LOGGER.info("CMS对接：未开启防爆功能，无需构建修改密码响应信息");

            return response;
        }

        if (code == ChangeUserPwdCode.IN_LOCKED) {
            response.setCode(ChangeUserPwdCode.IN_LOCKED);
            response.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
            LOGGER.info("CMS对接：锁定用户[{}]响应信息为：[{}]", userName, JSON.toJSONString(response));

            return response;
        } else if (code == ChangeUserPwdCode.REMIND_ERROR_TIMES) {
            response.setCode(ChangeUserPwdCode.REMIND_ERROR_TIMES);
            response.setRemainingTimes(pwdStrategyDTO.getUserLockedErrorTimes() - userEntity.getPwdErrorTimes());
            LOGGER.info("CMS对接：用户[{}]密码错误，提示剩余错误次数响应信息为：[{}]", userName, JSON.toJSONString(response));

            return response;
        }

        response.setCode(ChangeUserPwdCode.OLD_PASSWORD_ERROR);
        return response;
    }


    /**
     * 异步处理CMS请求同步用户
     *
     * @param request
     */
    private void asynUserMessageToCmsByFile(GetInfoRequest request) {
        try {
            // 内部任务ID
            UUID resultTaskid = UUID.randomUUID();
            CmsInfoDTO cmsInfoDTO = new CmsInfoDTO();
            BeanUtils.copyProperties(request, cmsInfoDTO);
            cmsInfoDTO.setResultTaskid(resultTaskid);
            // 写入用户信息
            writeUserListToFile(cmsInfoDTO);
            // 构造返回
            CmsInfoRespone cmsInfoRespone = new CmsInfoRespone();
            BeanUtils.copyProperties(cmsInfoDTO, cmsInfoRespone);
            // 同步通知用户
            notifyUsersChangeByFile(cmsInfoRespone);
        } catch (Exception e) {
            LOGGER.error("CMS同步用户信息出错", e);
        }
    }

    private void notifyUsersChangeByFile(CmsInfoRespone respone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("oper", UserOperNotifyEnum.SYNC_USERS_FOR_FTP.getOper());
        jsonObject.put("timestamp", System.currentTimeMillis());
        jsonObject.put("content", respone);
        LOGGER.info("RCDC数据创建完成，通知CMS{}", jsonObject.toJSONString());
        cmsDockingProducerAPI.syncMessageToCMS(jsonObject.toJSONString());
    }

    /**
     * 写入用户信息到文件
     *
     * @param cmsInfoDTO
     * @return
     * @throws IOException
     * @throws BusinessException
     */
    private void writeUserListToFile(CmsInfoDTO cmsInfoDTO) throws IOException, BusinessException {
        // 初始化文件系统
        File resultFile = initFileSystem(cmsInfoDTO);
        FileWriter fw = null;
        // 流自动关闭
        try {
            fw = new FileWriter(resultFile, true);
            // 获取统计数量
            int count = pageQueryWriterUser(fw);
            // 设置统计数量
            cmsInfoDTO.setCount(count);
        } catch (Exception e) {
            LOGGER.error("写入文件失败{}", JSON.toJSONString(cmsInfoDTO));
            throw e;
        } finally {
            if (fw != null) {
                fw.close();
            }
        }
        // 压缩到ZIP
        zipCompress(cmsInfoDTO);
    }


    private void zipCompress(CmsInfoDTO cmsInfoDTO) throws IOException, BusinessException {
        // TXT绝对路径
        String inputFilePath = cmsInfoDTO.getInsideTxtAbsolutePath();
        File inputFile = new File(inputFilePath);
        // 输出ZIP文件的绝对路径
        String outputFilePath = cmsInfoDTO.buildZipFileAbsolutePath();
        Assert.notNull(outputFilePath, "outputFilePath can not be null");
        // 流自动关闭
        try (
                // 创建zip输出流
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFilePath));
                // 创建缓冲输出流
                BufferedOutputStream bos = new BufferedOutputStream(out);
                // 输入流
                FileInputStream fos = new FileInputStream(inputFile);
                // 输入缓冲
                BufferedInputStream bis = new BufferedInputStream(fos)) {
            // 实体ZipEntry保存
            ZipEntry zipEntry = new ZipEntry(inputFile.getName());
            out.putNextEntry(zipEntry);
            int len;
            byte[] bufArr = new byte[1024];
            while ((len = bis.read(bufArr)) != -1) {
                bos.write(bufArr, 0, len);
            }
            bos.flush();
            out.finish();
        } catch (IOException e) {
            LOGGER.error("写入ZIP文件失败{}", JSON.toJSONString(cmsInfoDTO));
            throw e;
        }
        // 设置结果路径
        cmsInfoDTO.setResultTaskPath(cmsInfoDTO.buildFtpZipFileResultPath());
        // 删除源TXT文件 需要用到 避免在流未关闭前删除文件，会有问题
        if (inputFile != null) {
            inputFile.delete();
            LOGGER.info("压缩成功并删除源TXT文件{}", JSON.toJSONString(cmsInfoDTO));
        }
        ShellCommandRunner shellCommandRunner = new ShellCommandRunner();
        shellCommandRunner.setCommand("chown cmsFtp:cmsFtp " + outputFilePath);
        shellCommandRunner.execute();
        LOGGER.info("cmsFtp 成功设置ZIP文件{}", cmsInfoDTO.getResultTaskPath());
        shellCommandRunner = new ShellCommandRunner();
        shellCommandRunner.setCommand("chown 777 " + outputFilePath);
        shellCommandRunner.execute();
        LOGGER.info("cmsFtp 成功7770授权ZIP文件{}", cmsInfoDTO.getResultTaskPath());
    }

    /**
     * 平台api不支持获取全部数据，通过分页方式获取
     *
     * @param fw
     * @return
     * @throws BusinessException 业务异常
     * @throws IOException IO 异常
     */
    private int pageQueryWriterUser(FileWriter fw) throws BusinessException, IOException {
        // 查询请求构造
        IacPageSearchRequest iacPageSearchRequest = new IacPageSearchRequest();
        // 初始化分页0
        int page = 0;
        iacPageSearchRequest.setPage(page);
        // 每页最大1000
        iacPageSearchRequest.setLimit(QUERY_PAGE_SIZE);
        // 累计数量
        int count = 0;
        while (true) {
            DefaultPageResponse<IacUserDetailDTO> pageResponse = cbbUserAPI.originPageQuery(iacPageSearchRequest);
            IacUserDetailDTO[] itemArr = pageResponse.getItemArr();
            if (ArrayUtils.isEmpty(itemArr)) {
                LOGGER.info("all users found, count{}", count);
                return count;
            }
            // 页码数量自增
            iacPageSearchRequest.setPage(++page);
            // 累加查询数量
            count = count + itemArr.length;
            for (IacUserDetailDTO cbbUserOperNotifyContentDTO : itemArr) {
                // 用户账号
                String userName = "";
                if (StringUtils.isNotBlank(cbbUserOperNotifyContentDTO.getUserName())) {
                    userName = Base64.getEncoder().encodeToString(cbbUserOperNotifyContentDTO.getUserName().getBytes());
                }

                String realName = "";
                // 用户姓名
                if (StringUtils.isNotBlank(cbbUserOperNotifyContentDTO.getRealName())) {
                    realName = Base64.getEncoder().encodeToString(cbbUserOperNotifyContentDTO.getRealName().getBytes());
                }

                StringBuilder stringBuilder = new StringBuilder();
                // 添加ID
                stringBuilder.append(cbbUserOperNotifyContentDTO.getId() + "||");
                // 添加BASE64 处理后的用户账号
                stringBuilder.append(userName + "||");
                // 添加密码
                stringBuilder.append(cbbUserOperNotifyContentDTO.getPassword() + "||");
                // 添加BASE64 处理后的用户姓名
                stringBuilder.append(realName + "||");
                // 添加用户组ID
                stringBuilder.append(cbbUserOperNotifyContentDTO.getGroupId() + "||");
                // 添加用户状态
                stringBuilder.append(cbbUserOperNotifyContentDTO.getUserState() + "||");
                // 添加用户类型
                stringBuilder.append(cbbUserOperNotifyContentDTO.getUserType() + "||");
                // 添加用户角色 并换行
                stringBuilder.append(cbbUserOperNotifyContentDTO.getUserRole() + "\r\n");
                fw.write(stringBuilder.toString());
            }
            fw.flush();
        }
    }

    /**
     * 初始化文件系统
     */
    private File initFileSystem(CmsInfoDTO cmsInfoDTO) throws IOException, BusinessException {
        // 设置权限
        Set perms = getFilePerms();
        File folder = new File(Constants.CMS_FTP_PATH);
        // 如果文件夹不存在,就动态创建文件夹
        if (!folder.isDirectory()) {
            folder.mkdir();
            // 设置文件权限 可执行 可写 可读能力
            setFileAble(folder, perms, cmsInfoDTO);

        }
        // 获取当前时间
        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd")));
        // 获取根据当前时间而创建文件夹
        String timeNowPath = Constants.CMS_FTP_PATH + timeNow + File.separator;
        File timeNowFolder = new File(timeNowPath);
        // 如果文件夹不存在,就动态创建文件夹
        if (!timeNowFolder.isDirectory()) {
            timeNowFolder.mkdir();
            // 设置文件权限 可执行 可写 可读能力
            setFileAble(timeNowFolder, perms, cmsInfoDTO);
            ShellCommandRunner shellCommandRunner = new ShellCommandRunner();
            shellCommandRunner.setCommand("chown cmsFtp:cmsFtp " + timeNowPath);
            shellCommandRunner.execute();
            LOGGER.info("cmsFtp 成功设置时间文件夹路径{}", cmsInfoDTO.getResultTaskPath());
        } else {
            // 说明当前文件夹存在 需要判断文件夹下的文件数量
            if (timeNowFolder.listFiles().length > MAX_FILE_NUMBER) {
                // 如果当前文件夹下的文件大于限制4个 删除前面的 ，保留最新4个
                File[] fileArr = timeNowFolder.listFiles();
                // 过滤删除文件 排序根据修改时间
                filterOrderByModifieTime(fileArr);
            }
        }
        // 设置当前文件容器内的上传路径
        cmsInfoDTO.setInsideRelativePath(timeNowPath);
        // 根据任务ID设置唯一名称
        String resultFileName = timeNowPath + cmsInfoDTO.getResultTaskid().toString() + ".txt";
        File file = new File(resultFileName);
        // 如果文件不存在,就动态创建文件
        if (file.exists()) {
            LOGGER.info("文件{}存在重复,{}需要删除再创建", resultFileName, JSON.toJSONString(cmsInfoDTO));
            file.delete();
        }
        file.createNewFile();
        // 设置文件权限 可执行 可写 可读能力
        setFileAble(file, perms, cmsInfoDTO);
        // 设置TXT的文件的绝对路径
        cmsInfoDTO.setInsideTxtAbsolutePath(resultFileName);
        // 设置FTP的路径 提供CMS下载使用
        cmsInfoDTO.setFtpTaskRelativePath(File.separator + timeNow + File.separator);
        // 设置FTP的路径 提供CMS下载使用
        cmsInfoDTO.setResultTaskPath(File.separator + timeNow + File.separator + cmsInfoDTO.getResultTaskid().toString() + ".txt");
        return file;
    }

    /**
     * 获取文件权限
     *
     * @return
     */
    private Set getFilePerms() {
        // 设置权限
        Set perms = new HashSet();
        // 设置所有者的读取权限
        perms.add(PosixFilePermission.OWNER_READ);
        // 设置所有者的写权限
        perms.add(PosixFilePermission.OWNER_WRITE);
        // 设置所有者的执行权限
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        // 设置组的读取权限
        perms.add(PosixFilePermission.GROUP_READ);
        // 设置组的读取权限
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        // 设置其他的读取权限
        perms.add(PosixFilePermission.OTHERS_READ);
        // 设置其他的读取权限
        perms.add(PosixFilePermission.OTHERS_EXECUTE);

        return perms;
    }

    /**
     * 设置文件权限 可执行 可写 可读能力
     *
     * @return
     */
    private void setFileAble(File file, Set perms, CmsInfoDTO cmsInfoDTO) {
        // 可写
        file.setExecutable(Boolean.TRUE);
        // 可写
        file.setWritable(Boolean.TRUE);
        // 可读
        file.setReadable(Boolean.TRUE);
        try {
            Path pathDest = Paths.get(file.getAbsolutePath());
            // 修改文件夹路径的权限
            Files.setPosixFilePermissions(pathDest, perms);
        } catch (Exception e) {
            LOGGER.info("文件{}权限修改失败，但不应该影响主流程{}", file.getAbsolutePath(), JSON.toJSONString(cmsInfoDTO));
        }
    }

    private void filterOrderByModifieTime(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                Assert.notNull(f1, "f1 is not null");
                Assert.notNull(f2, "f2 is not null");

                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0) {
                    return -1;
                } else if (diff == 0) {
                    return 0;
                } else {
                    // 如果 if 中修改为 返回-1 同时此处修改为返回 1 排序就会是递减
                    return 1;
                }
            }


        });
        // 只保留前4的最近的时间点文件
        for (int i = files.length - 1; i >= 3; i--) {
            if (files[i].isFile()) {
                files[i].delete();
            }
        }

    }
}
