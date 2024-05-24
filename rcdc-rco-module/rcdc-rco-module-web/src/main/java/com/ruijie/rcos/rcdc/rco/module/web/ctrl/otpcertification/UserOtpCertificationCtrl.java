package com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.otp.IacUserOtpCertificationConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.RcoViewUserOtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.batchtask.ResetOtpBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.batchtask.ResetOtpSingleTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * Description: 用户动态口令管理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
@Api(tags = "用户动态口令管理")
@Controller
@RequestMapping("/rco/user/otpCertification")
public class UserOtpCertificationCtrl {

    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    /**
     * 分页获取用户动态口令信息
     *
     * @param request 请求参数
     * @param sessionContext session信息
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "分页获取用户动态口令信息")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"分页获取用户动态口令信息"})})
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<RcoViewUserOtpCertificationDTO>> getPageDeskStrategy(PageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "PageWebRequest can not be null");
        Assert.notNull(sessionContext, "sessionContext can not be null");

        PageSearchRequest apiRequest = new PageSearchRequest(request);

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> userGroupIdStrList = getPermissionUserGroupIdList(sessionContext.getUserId());
            String userGroupId = getUserGroupId(request);
            if (StringUtils.isNotEmpty(userGroupId) && !userGroupIdStrList.contains(userGroupId)) {
                DefaultPageResponse<RcoViewUserOtpCertificationDTO> response = new DefaultPageResponse<>();
                response.setItemArr(new RcoViewUserOtpCertificationDTO[] {});
                return CommonWebResponse.success(response);
            }
            if (StringUtils.isEmpty(userGroupId)) {
                appendUserGroupIdMatchEqual(apiRequest, userGroupIdStrList);
            }
        }

        DefaultPageResponse<RcoViewUserOtpCertificationDTO> pageResponse = userOtpCertificationAPI.pageQuery(apiRequest);
        return CommonWebResponse.success(pageResponse);
    }



    /**
     * 获取管理的组
     *
     * @param adminId adminId
     * @return List<String>
     * @throws BusinessException 业务异常
     */
    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        return listUserGroupIdResponse.getUserGroupIdList();
    }

    /**
     * 获取组
     *
     * @param request request
     * @return String
     */
    private String getUserGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals("userGroupId") && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }

    /**
     * 添加用户组的条件
     *
     * @param request request
     * @param userGroupIdStrList userGroupIdStrList
     */
    private void appendUserGroupIdMatchEqual(PageSearchRequest request, List<String> userGroupIdStrList) {
        UUID[] uuidArr = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(UserGroupHelper.USER_GROUP_ROOT_ID)).map(UUID::fromString)
                .toArray(UUID[]::new);
        request.appendCustomMatchEqual(new MatchEqual("userGroupId", uuidArr));
    }

    /**
     *
     * @param request id请求
     * @param builder builder
     * @return DefaultWebResponse 删除的响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "重置用户动态口令")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"重置用户动态口令"})})
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse resetOtpSecretKey(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "resetOtpSecretKey()的request can not be null");
        Assert.notNull(builder, "resetOtpSecretKey()的builder can not be null");

        UUID[] idArr = request.getIdArr();
        // 批量重置任务
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            final DefaultBatchTaskItem singleTaskItem = DefaultBatchTaskItem.builder().itemId(idArr[0])
                    .itemName(LocaleI18nResolver.resolve(OtpCertificationBusinessKey.RCDC_RESET_OTP_SECRET_KEY_ITEM_NAME)).build();
            ResetOtpSingleTaskHandler singleOtpTashHandler = new ResetOtpSingleTaskHandler(userOtpCertificationAPI, singleTaskItem, auditLogAPI);
            IacUserDetailDTO userDetailResponse = cbbUserAPI.getUserDetail(idArr[0]);
            singleOtpTashHandler.setUserAPI(cbbUserAPI);
            result = builder.setTaskName(OtpCertificationBusinessKey.RCDC_RESET_OTP_SECRET_KEY_SINGLE_TASK_NAME, new String[] {})
                    .setTaskDesc(OtpCertificationBusinessKey.RCDC_RESET_OTP_SECRET_KEY_SINGLE_TASK_DESC,
                            new String[] {userDetailResponse.getUserName()})
                    .registerHandler(singleOtpTashHandler).start();
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                    .itemName(OtpCertificationBusinessKey.RCDC_RESET_OTP_SECRET_KEY_ITEM_NAME, new String[] {}).build()).iterator();
            ResetOtpBatchTaskHandler batchTaskHandler = new ResetOtpBatchTaskHandler(this.userOtpCertificationAPI, iterator, auditLogAPI);
            batchTaskHandler.setUserAPI(cbbUserAPI);
            result = builder.setTaskName(OtpCertificationBusinessKey.RCDC_RESET_OTP_SECRET_KEY_TASK_NAME, new String[] {})
                    .setTaskDesc(OtpCertificationBusinessKey.RCDC_RESET_OTP_SECRET_KEY_TASK_DESC, new String[] {}).enableParallel()
                    .registerHandler(batchTaskHandler).start();
        }
        return CommonWebResponse.success(result);
    }

    /**
     *
     * @param request id请求
     * @param builder builder
     * @return DefaultWebResponse 删除的响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "绑定用户动态口令密钥")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"绑定用户动态口令密钥"})})
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public CommonWebResponse bind(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "bind()的request can not be null");
        Assert.notNull(builder, "bind()的builder can not be null");
        UUID[] idArr = request.getIdArr();
        if (idArr.length != 1) {
            throw new BusinessException(OtpCertificationBusinessKey.RCDC_OTP_SECRET_KEY_RESET_USER_ID_ARR_MUST_BE_ONE);
        }
        if (userOtpCertificationAPI.bindById(idArr[0])) {
            return CommonWebResponse.success(OtpCertificationBusinessKey.RCDC_OTP_SECRET_KEY_RESET_SUCCESS, new String[] {});
        }
        return CommonWebResponse.fail(OtpCertificationBusinessKey.RCDC_OTP_SECRET_KEY_RESET_FAIL, new String[] {});
    }

    /**
     *
     * @param request id请求
     * @param builder builder
     * @return DefaultWebResponse 删除的响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "获取用户动态口令详细配置")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取用户动态口令详细配置"})})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public CommonWebResponse getDeatil(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "getDeatil()的request can not be null");
        Assert.notNull(builder, "getDeatil()的builder can not be null");
        UUID[] idArr = request.getIdArr();
        if (idArr.length != 1) {
            throw new BusinessException(OtpCertificationBusinessKey.RCDC_OTP_SECRET_KEY_RESET_USER_ID_ARR_MUST_BE_ONE);
        }
        IacUserOtpCertificationConfigDTO userOtpCertificationConfig = userOtpCertificationAPI.getUserOtpCertificationConfigById(idArr[0]);
        return CommonWebResponse.success(userOtpCertificationConfig);
    }
}

