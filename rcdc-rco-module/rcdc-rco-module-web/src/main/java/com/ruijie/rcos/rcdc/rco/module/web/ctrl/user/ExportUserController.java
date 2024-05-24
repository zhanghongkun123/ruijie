package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.google.common.io.Files;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ExportUserExcelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportUserCacheResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportUserFileResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.ExportUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 导出用户
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/11
 *
 * @author guoyongxin
 */
@Api(tags = "用户导出功能")
@Controller
@RequestMapping("/rco/user")
@EnableCustomValidate(enable = false)
public class ExportUserController {

    @Autowired
    private ExportUserExcelAPI exportUserExcelAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    /** 用户文件名称 */
    private static final String EXPORT_USER_FILE_NAME = "用户信息";
    
    /** 用户组标识 */
    private static final String EXPORT_USER_GROUP = "groupId";

    /**
     * @param request        请求体
     * @param sessionContext sessionContext
     * @return downloadWebResponse
     * @throws BusinessException 异常抛出
     */
    @ApiOperation("导出用户数据")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "exportUser", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse exportUser(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为空");

        UserPageSearchRequest userPageSearchRequest = new UserPageSearchRequest(request);
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            List<String> userGroupIdStrList = this.getPermissionUserGroupIdList(sessionContext.getUserId());
            String userGroupId = this.getUserGroupId(request);
            if (StringUtils.isEmpty(userGroupId)) {
                //限制当前用户组权限范围
                this.appendUserGroupIdMatchEqual(userPageSearchRequest, userGroupIdStrList);
            } else {
                if (!userGroupIdStrList.contains(userGroupId)) {
                    //没权限
                    return CommonWebResponse.fail(BusinessKey.RCDC_RCO_EXPORT_USER_NO_PERMISSION, new String[] {userGroupId});
                }
            }
        }

        exportUserExcelAPI.exportUserDataAsync(userPageSearchRequest, sessionContext.getUserId());
        return CommonWebResponse.success();
    }

    /**
     * 获取用户数据导出任务情况
     *
     * @param sessionContext sessionContext
     * @return DataResult
     */
    @ApiOperation("获取导出结果")
    @RequestMapping(value = "/getExportResult", method = RequestMethod.POST)
    public CommonWebResponse<ExportUserInfoDTO> getExportResult(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportUserCacheResponse response = exportUserExcelAPI.getExportDataCache(userId);
        return CommonWebResponse.success(response.getExportUserInfoDTO());
    }

    /**
     * 下载用户数据excel
     *
     * @param sessionContext sessionContext
     * @return DownloadWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/downloadExportData", method = RequestMethod.GET)
    public DownloadWebResponse downloadExportFile(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext is null");
        String userId = sessionContext.getUserId().toString();
        GetExportUserFileResponse response = exportUserExcelAPI.getExportFile(userId);
        DownloadWebResponse.Builder builder = new DownloadWebResponse.Builder();
        SimpleDateFormat df = new SimpleDateFormat(DateUtil.YYYYMMDDHH24MISS);
        String fileName = EXPORT_USER_FILE_NAME + df.format(new Date());
        String ext = Files.getFileExtension(response.getExportFile().getName());
        return builder.setFile(response.getExportFile(), false).setName(fileName, ext).build();
    }

    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        return listUserGroupIdResponse.getUserGroupIdList();
    }

    /**
     * 当前前端只会上传一个groupId进行查询
     */
    private String getUserGroupId(PageWebRequest request) {
        if (ArrayUtils.isNotEmpty(request.getExactMatchArr())) {
            for (ExactMatch exactMatch : request.getExactMatchArr()) {
                if (exactMatch.getName().equals(EXPORT_USER_GROUP) && exactMatch.getValueArr().length > 0) {
                    return exactMatch.getValueArr()[0];
                }
            }
        }
        return "";
    }

    private void appendUserGroupIdMatchEqual(UserPageSearchRequest request, List<String> userGroupIdStrList) {
        List<UUID> uuidList = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(UserGroupHelper.USER_GROUP_ROOT_ID)).map(UUID::fromString)
                .collect(Collectors.toList());
        UUID[] uuidArr = uuidList.toArray(new UUID[uuidList.size()]);
        request.appendCustomMatchEqual(new MatchEqual(EXPORT_USER_GROUP, uuidArr));
    }
}
