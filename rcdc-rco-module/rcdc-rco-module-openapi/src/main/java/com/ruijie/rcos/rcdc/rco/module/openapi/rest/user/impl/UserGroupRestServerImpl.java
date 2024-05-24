package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacCreateUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDelUserGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.AssistCertification;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.PrimaryCertification;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserGroupVO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktop.dto.GroupFilterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.service.UserGroupBaseHelper;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.OpenApiBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.UserGroupRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.helper.UserValidateHelper;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.CreateUserGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.DeleteUserGroupRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

;

/**
 * Description: 用户组业务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/21
 *
 * @author xiao'yong'deng
 */
@Service
public class UserGroupRestServerImpl implements UserGroupRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserGroupRestServerImpl.class);

    private static final int INVALID_TIME_MAX_VALUE = 1000;

    private static final int INVALID_TIME_MIN_VALUE = 0;

    private static final Long EXPIRE_TIME = 0L;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private IacUserGroupMgmtAPI userGroupAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private UserGroupBaseHelper userGroupBaseHelper;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private UserValidateHelper userValidateHelper;

    /**
     * 用户默认组ID
     */
    private final static String DEFAULT_GROUP_ID = "a4209626-1df1-499f-93ca-de15c3f17fe7";

    /**
     * 用户组正则表达式：只包含中英文，数字，“_”，“-”，“@”，“.”且不能以“_”开头
     */
    private static final String DEFAULT_GROUP_NAME_REGEX_EXPRESSION = "^[0-9a-zA-Z\\u4e00-\\u9fa5，,（）()@.-][0-9a-zA-Z\\u4e00-\\u9fa5，,（）()_@.-]*$";

    @Override
    public DefaultWebResponse create(CreateUserGroupRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        String userGroupName = request.getName();
        if (!userGroupName.matches(DEFAULT_GROUP_NAME_REGEX_EXPRESSION)) {
            LOGGER.error("用户组名称不合法,名称为：[{}]", userGroupName);
            throw new BusinessException(OpenApiBusinessKey.RCDC_OPENAPI_GROUP_NAME_CHECK_FAIL, userGroupName);
        }

        validateUserGroupCertification(request.getPrimaryCertification(), request.getAssistCertification());

        try {
            IacCreateUserGroupDTO createUserGroupRequest = new IacCreateUserGroupDTO();
            createUserGroupRequest.setName(userGroupName);
            createUserGroupRequest.setParentId(request.getParentId());

            validateInvalidTime(request.getInvalidTime());
            if (ObjectUtils.isEmpty(request.getAccountExpireDate())) {
                createUserGroupRequest.setAccountExpires(EXPIRE_TIME);
            } else {
                validateExpireDate(request.getAccountExpireDate());
                createUserGroupRequest.setAccountExpires(request.getAccountExpireDate());
            }
            createUserGroupRequest.setInvalidTime(request.getInvalidTime());
            IacUserGroupDetailDTO userGroup = cbbUserGroupAPI.createUserGroup(createUserGroupRequest);

            saveUserGroupIdentityConfig(userGroup.getId(), request.getPrimaryCertification(), request.getAssistCertification());
            return DefaultWebResponse.Builder.success(OpenApiBusinessKey.RCDC_OPENAPI_CREATE_USER_GROUP_SUCCESS_LOG, new String[] {userGroupName});
        } catch (BusinessException e) {
            LOGGER.error("创建用户组[{}]失败", userGroupName, e);
            throw new BusinessException(OpenApiBusinessKey.RCDC_OPENAPI_CREATE_USER_GROUP_FAIL_LOG, e, userGroupName,
                    UserTipUtil.resolveBusizExceptionMsg(e));
        }
    }

    private void validateExpireDate(Long expireDate) throws BusinessException {
        if (ObjectUtils.isEmpty(expireDate)) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (expireDate < new Date().getTime()) {
            throw new BusinessException(OpenApiBusinessKey.RCDC_OPENAPI_GROUP_EXPIRE_TIME_CHECK_FAIL, simpleDateFormat.format(new Date(expireDate)),
                    simpleDateFormat.format(new Date()));
        }
    }

    private void validateInvalidTime(Integer invalidTime) throws BusinessException {
        if (ObjectUtils.isEmpty(invalidTime)) {
            return;
        }
        if (invalidTime > INVALID_TIME_MAX_VALUE || invalidTime < INVALID_TIME_MIN_VALUE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_INVALID_TIME_VALIDATE_ERROR, invalidTime.toString());
        }
    }

    private void validateUserGroupCertification(PrimaryCertification primaryCertification, AssistCertification assistCertification)
            throws BusinessException {
        if (Objects.nonNull(primaryCertification)) {
            userValidateHelper.validatePrimaryCertification(primaryCertification);
        }

        if (Objects.nonNull(assistCertification)) {
            userValidateHelper.validateUserAssistCertification(assistCertification);
        }
    }

    private void saveUserGroupIdentityConfig(UUID groupId, PrimaryCertification primaryCertification, AssistCertification assistCertification)
            throws BusinessException {
        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USERGROUP, groupId);

        if (Objects.nonNull(primaryCertification)) {
            userIdentityConfigRequest.setOpenAccountPasswordCertification(primaryCertification.getOpenAccountPasswordCertification());
            userIdentityConfigRequest.setOpenCasCertification(primaryCertification.getOpenCasCertification());
            userIdentityConfigRequest.setOpenWorkWeixinCertification(primaryCertification.getOpenWorkWeixinCertification());
            userIdentityConfigRequest.setOpenFeishuCertification(primaryCertification.getOpenFeishuCertification());
            userIdentityConfigRequest.setOpenDingdingCertification(primaryCertification.getOpenDingdingCertification());
            userIdentityConfigRequest.setOpenOauth2Certification(primaryCertification.getOpenOauth2Certification());
            userIdentityConfigRequest.setOpenRjclientCertification(primaryCertification.getOpenRjclientCertification());
        }

        if (Objects.nonNull(assistCertification)) {
            userIdentityConfigRequest.setOpenHardwareCertification(assistCertification.getOpenHardwareCertification());
            userIdentityConfigRequest.setMaxHardwareNum(assistCertification.getMaxHardwareNum());
            userIdentityConfigRequest.setOpenOtpCertification(assistCertification.getOpenOtpCertification());
            userIdentityConfigRequest.setOpenSmsCertification(assistCertification.getOpenSmsCertification());
            userIdentityConfigRequest.setOpenRadiusCertification(assistCertification.getOpenRadiusCertification());
        }

        // 主要认证和辅助认证有值才更新
        if (Objects.nonNull(primaryCertification) || Objects.nonNull(assistCertification)) {
            userIdentityConfigAPI.updateUserIdentityConfig(userIdentityConfigRequest);
        }

    }

    @Override
    public DefaultWebResponse list() throws BusinessException {
        IacUserGroupDetailDTO[] userGroupDTOArr = userGroupAPI.getAllUserGroup();
        if (userGroupDTOArr.length == 0) {
            return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));
        }
        List<UserGroupVO> resultUserGroupList = buildUserGroupVO(userGroupDTOArr);

        if (resultUserGroupList.size() == 0) {
            return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));
        }
        GroupFilterDTO groupFilterDTO = new GroupFilterDTO();
        groupFilterDTO.setUserGroupVOList(resultUserGroupList);
        groupFilterDTO.setEnableFilterAdGroup(false);
        groupFilterDTO.setEnableFilterLdapGroup(false);
        groupFilterDTO.setEnableFilterDefaultGroup(false);
        // 构建树结构
        List<UserGroupVO> resultList = userGroupBaseHelper.buildUserGroupTree(groupFilterDTO);
        return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", resultList));
    }

    private List<UserGroupVO> buildUserGroupVO(IacUserGroupDetailDTO[] userGroupDTOArr) {
        List<UserGroupVO> voList = new ArrayList<>();
        for (IacUserGroupDetailDTO dto : userGroupDTOArr) {
            UserGroupVO vo = new UserGroupVO();
            vo.setId(dto.getId().toString());
            vo.setLabel(dto.getName());
            vo.setEnableDefault(dto.isDefault());
            vo.setAllowDelete(dto.isAllowDelete());
            vo.setEnableAd(dto.isAdGroup());
            vo.setEnableLdap(dto.isLdapGroup());
            vo.setDisabled(true);
            if (dto.getParentId() == null) {
                vo.setParentId(UserConstants.USER_GROUP_ROOT_ID);
            } else {
                vo.setParentId(dto.getParentId().toString());
            }
            voList.add(vo);
        }
        voList.add(addRootUserGroupVO());
        return voList;
    }

    private UserGroupVO addRootUserGroupVO() {
        UserGroupVO vo = new UserGroupVO();
        vo.setId(UserConstants.USER_GROUP_ROOT_ID);
        vo.setLabel(UserConstants.USER_GROUP_ROOT_NAME);
        vo.setParentId(null);
        vo.setDisabled(true);
        vo.setEnableAd(false);
        vo.setAllowDelete(false);
        vo.setEnableDefault(false);
        return vo;
    }

    @Override
    public DefaultWebResponse delete(DeleteUserGroupRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID groupId = request.getId();
        UUID moveGroupId = UUID.fromString(DEFAULT_GROUP_ID);
        String localGroupName = groupId.toString();
        try {
            IacDelUserGroupDTO cbbDelUserGroupDTO = new IacDelUserGroupDTO(groupId, moveGroupId);
            IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr = cbbUserGroupAPI.getAllUserGroup();
            IacUserGroupDetailDTO userGroupDTO = cbbUserGroupAPI.getUserGroupDetail(groupId);
            localGroupName = userGroupDTO.getName();

            cbbUserGroupAPI.deleteUserGroup(cbbDelUserGroupDTO);
            deleteGroupDesktopConfig(groupId, moveGroupId, cbbUserGroupDetailDTOArr);
            return DefaultWebResponse.Builder.success(OpenApiBusinessKey.RCDC_OPENAPI_DELETE_USER_GROUP_SUCCESS_LOG, new String[] {localGroupName});
        } catch (BusinessException e) {
            LOGGER.error("删除用户组[{}]失败: ", localGroupName, e);
            throw new BusinessException(OpenApiBusinessKey.RCDC_OPENAPI_DELETE_USER_GROUP_FAIL_LOG, e, localGroupName, e.getI18nMessage());
        }
    }

    private void deleteGroupDesktopConfig(UUID groupId, UUID moveGroupId, IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr) {
        List<UUID> deleteGroupIdList = findDeleteGroupIdList(groupId, moveGroupId, cbbUserGroupDetailDTOArr);
        deleteGroupIdList.stream().forEach(dto -> {
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(dto, UserCloudDeskTypeEnum.IDV);
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(dto, UserCloudDeskTypeEnum.VDI);
            // 删除VOI的云桌面配置
            userDesktopConfigAPI.deleteUserGroupDesktopConfig(dto, UserCloudDeskTypeEnum.VOI);
        });
    }

    private List<UUID> findDeleteGroupIdList(UUID groupId, UUID moveGroupId, IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr) {
        List<UUID> deleteGroupIdList = Lists.newArrayList();
        /** 只有移动到未分组，才删除组及底下组 */
        if (moveGroupId != null && moveGroupId.equals(IacUserGroupMgmtAPI.DEFAULT_USER_GROUP_ID)) {
            /** 从删除节点开始递归获取所有子孙节点进行递归删除 */
            List<IacUserGroupDetailDTO> parentGroupDTOList =
                    Arrays.stream(cbbUserGroupDetailDTOArr).filter(dto -> dto.getId().equals(groupId)).collect(Collectors.toList());
            getChildrenTreeNode(parentGroupDTOList, deleteGroupIdList, cbbUserGroupDetailDTOArr);
        } else {
            deleteGroupIdList.add(groupId);
        }
        return deleteGroupIdList;
    }

    private void getChildrenTreeNode(List<IacUserGroupDetailDTO> parentGroupDTOList, List<UUID> deleteGroupIdList,
            IacUserGroupDetailDTO[] cbbUserGroupDetailDTOArr) {
        deleteGroupIdList.addAll(parentGroupDTOList.stream().map(IacUserGroupDetailDTO::getId).collect(Collectors.toList()));
        List<IacUserGroupDetailDTO> childrenDTOList = Lists.newArrayList();
        parentGroupDTOList.stream().forEach(parentGroupDTO -> childrenDTOList.addAll(Arrays.stream(cbbUserGroupDetailDTOArr)
                .filter(dto -> parentGroupDTO.getId().equals(dto.getParentId())).collect(Collectors.toList())));
        if (!childrenDTOList.isEmpty()) {
            getChildrenTreeNode(childrenDTOList, deleteGroupIdList, cbbUserGroupDetailDTOArr);
        }
    }
}
