package com.ruijie.rcos.rcdc.rco.module.web.service;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.StoragePoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.UserGroupDetailWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

;

/**
 * Description: UserGroupHelper测试类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/15 6:00 下午
 *
 * @author zhouhuan
 */
@RunWith(SkyEngineRunner.class)
public class UserGroupHelperTest {

    @Tested
    UserGroupHelper userGroupHelper;

    @Injectable
    IacUserMgmtAPI cbbUserAPI;

    @Injectable
    CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Injectable
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Injectable
    CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Injectable
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Injectable
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Injectable
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Injectable
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Injectable
    private StoragePoolAPI storagePoolAPI;

    @Test
    public void testBuildUserGroupDetailForWebResponse() throws BusinessException {
        IacUserGroupDetailDTO response = new IacUserGroupDetailDTO();
        UUID groupId = UUID.randomUUID();
        response.setId(groupId);
        response.setParentId(null);
        UserGroupDesktopConfigDTO configDTO = new UserGroupDesktopConfigDTO();
        configDTO.setStrategyId(UUID.randomUUID());
        configDTO.setNetworkId(UUID.randomUUID());
        configDTO.setImageTemplateId(UUID.randomUUID());

        new Expectations() {
            {
                userDesktopConfigAPI.getUserGroupDesktopConfig((UUID) any, (UserCloudDeskTypeEnum) any);
                result = configDTO;
            }
        };

        UserGroupDetailWebResponse webResponse = userGroupHelper.buildUserGroupDetailForWebResponse(response);
        Assert.assertEquals(groupId, webResponse.getId());
    }

    /**
     * 测试getUnBindIDVDesktopUserListByGroupId方法
     * 
     * @throws BusinessException
     */
    @Test
    public void testGetUnBindIDVDesktopUserListByGroupIdUserListEmpty() throws BusinessException {
        new Expectations() {
            {
                cbbUserAPI.queryUserListByGroupId((UUID) any);
                result = new IacUserDetailDTO[] {};
            }
        };

        UUID[] idArr = userGroupHelper.getUnBindDesktopUserListByGroupId(null, UserCloudDeskTypeEnum.IDV);

        Assert.assertTrue(idArr.length == 0);
    }

    /**
     * 测试getUnBindIDVDesktopUserListByGroupId方法
     * 
     * @throws BusinessException ex
     */
    @Test
    public void testGetUnBindIDVDesktopUserListByGroupId() throws BusinessException {
        IacUserDetailDTO dto1 = new IacUserDetailDTO();
        dto1.setUserState(IacUserStateEnum.DISABLE);
        IacUserDetailDTO dto2 = new IacUserDetailDTO();
        dto2.setUserState(IacUserStateEnum.ENABLE);
        dto2.setUserType(IacUserTypeEnum.VISITOR);
        IacUserDetailDTO dto3 = new IacUserDetailDTO();
        dto3.setUserState(IacUserStateEnum.ENABLE);
        dto3.setUserType(IacUserTypeEnum.NORMAL);
        IacUserDetailDTO dto4 = new IacUserDetailDTO();
        dto4.setUserState(IacUserStateEnum.ENABLE);
        dto4.setUserType(IacUserTypeEnum.NORMAL);

        IacUserDetailDTO userDetailResponse1 = new IacUserDetailDTO();
        IacUserDetailDTO userDetailResponse2 = new IacUserDetailDTO();
        new Expectations() {
            {
                cbbUserAPI.queryUserListByGroupId((UUID) any);
                result = new IacUserDetailDTO[] {dto1, dto2, dto3, dto4};
                cbbUserAPI.getUserDetail((UUID) any);
                returns(userDetailResponse1, userDetailResponse2);
                userDesktopConfigAPI.getUserDesktopConfig((UUID) any, UserCloudDeskTypeEnum.IDV);
                result = null;
            }
        };

        UUID[] idArr = userGroupHelper.getUnBindDesktopUserListByGroupId(null, UserCloudDeskTypeEnum.IDV);

        Assert.assertEquals(2, idArr.length);
    }
}
