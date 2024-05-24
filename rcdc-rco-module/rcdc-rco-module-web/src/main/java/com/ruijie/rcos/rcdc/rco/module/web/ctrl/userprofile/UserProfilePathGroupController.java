package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CheckUserProfileNameDuplicationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.request.CreateUserProfilePathGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.response.CheckNameDuplicationForUserProfileResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Description: 用户配置路径组
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
@Api(tags = "用户配置路径组管理")
@Controller
@RequestMapping("/rco/userProfilePathGroup")
@PageQueryWebConfig(url = "/list", dtoType = UserProfilePathGroupDTO.class)
public class UserProfilePathGroupController {

    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建用户配置分组
     *
     * @param createUserProfilePathGroupRequest 入参
     * @return 响应
     * @throws BusinessException 异常抛出
     */
    @ApiOperation("创建用户配置分组")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public CommonWebResponse create(CreateUserProfilePathGroupRequest createUserProfilePathGroupRequest) throws BusinessException {
        Assert.notNull(createUserProfilePathGroupRequest, "createUserProfilePathGroupRequest must not be null");
        UserProfilePathGroupDTO userProfilePathGroupDTO = new UserProfilePathGroupDTO();
        BeanUtils.copyProperties(createUserProfilePathGroupRequest, userProfilePathGroupDTO);
        userProfileMgmtAPI.isImportingUserProfilePath();
        userProfileMgmtAPI.createUserProfilePathGroup(userProfilePathGroupDTO);
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_CREATE, userProfilePathGroupDTO.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[] {});
    }

    /**
     * 编辑用户配置分组
     *
     * @param createUserProfilePathGroupRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑用户配置分组")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    public CommonWebResponse edit(CreateUserProfilePathGroupRequest createUserProfilePathGroupRequest) throws BusinessException {
        Assert.notNull(createUserProfilePathGroupRequest, "createUserProfilePathGroupRequest must not be null");
        UserProfilePathGroupDTO userProfilePathGroupDTO = new UserProfilePathGroupDTO();
        BeanUtils.copyProperties(createUserProfilePathGroupRequest, userProfilePathGroupDTO);
        userProfileMgmtAPI.editUserProfilePathGroup(userProfilePathGroupDTO);
        auditLogAPI.recordLog(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_PATH_GROUP_EDIT, userProfilePathGroupDTO.getName());
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[] {});
    }


    /**
     * 删除用户配置分组
     *
     * @param idArrWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除用户配置分组")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse delete(IdArrWebRequest idArrWebRequest) throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        UUID[] idArr = idArrWebRequest.getIdArr();
        Assert.notEmpty(idArr, "idArr can not be null");
        // 批量删除任务
        for (UUID uuid : idArr) {
            userProfileMgmtAPI.deleteUserProfilePathGroup(uuid);
        }
        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[] {});
    }

    /**
     * 获取用户配置分组详情
     *
     * @param idWebRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取用户配置分组详情")
    @RequestMapping(value = {"detail", "getInfo"}, method = RequestMethod.POST)
    public CommonWebResponse<UserProfilePathGroupDTO> detail(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        UserProfilePathGroupDTO userProfilePathGroupDTO = userProfileMgmtAPI.findUserProfilePathGroupById(idWebRequest.getId());
        return CommonWebResponse.success(userProfilePathGroupDTO);
    }

    /**
     * 检查用户配置分组名称是否重复
     *
     * @param checkUserProfileNameDuplicationRequest 入参
     * @return 响应
     */
    @ApiOperation("检查用户配置分组名称是否重复")
    @RequestMapping(value = "checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckNameDuplicationForUserProfileResponse> checkNameDuplication(
            CheckUserProfileNameDuplicationRequest checkUserProfileNameDuplicationRequest) {
        Assert.notNull(checkUserProfileNameDuplicationRequest, "checkUserProfileNameDuplicationRequest must not be null");
        UUID id = checkUserProfileNameDuplicationRequest.getId();
        String name = checkUserProfileNameDuplicationRequest.getName();
        Boolean hasDuplication = userProfileMgmtAPI.checkUserProfilePathGroupNameDuplication(id, name);
        return CommonWebResponse.success(new CheckNameDuplicationForUserProfileResponse(hasDuplication));
    }
}