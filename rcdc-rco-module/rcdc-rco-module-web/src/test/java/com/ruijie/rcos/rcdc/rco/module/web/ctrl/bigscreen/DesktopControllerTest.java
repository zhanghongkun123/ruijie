package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AbnormalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.AbnormalPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetUserGroupOverviewRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListDesktopByUserGroupPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.AbnormalPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetDesktopNormalDistributionWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetUserGroupOverviewWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.ListDesktopByUserGroupPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 13:20
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class DesktopControllerTest {

    @Tested
    private DesktopController desktopController;

    @Injectable
    private DesktopAPI desktopAPI;

    @Injectable
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    /**
     * 获取所有云桌面概要数据
     * @throws BusinessException 异常
     */
    @Test
    public void testGetAllDesktopOverview() throws BusinessException {
        AllDesktopOverviewDTO allDesktopOverviewDTO = new AllDesktopOverviewDTO();
        DefaultWebRequest request = new DefaultWebRequest();
        new Expectations() {
            {
                desktopAPI.getAllDesktopOverview();
                result = allDesktopOverviewDTO;
            }
        };

        DefaultWebResponse allDesktopOverview = desktopController.getAllDesktopOverview(request);
        assertEquals(allDesktopOverview.getStatus(), Status.SUCCESS);
        new Verifications() {
            {
                desktopAPI.getAllDesktopOverview();
                times = 1;
            }
        };
    }

    /**
     * 桌面资源使用率正太分布建模数据
     * @throws BusinessException 异常
     */
    @Test
    public void testGetDesktopNormalDistribution() throws BusinessException {
        GetDesktopNormalDistributionWebRequest request = new GetDesktopNormalDistributionWebRequest();
        DesktopNormalDistributionDTO dto = new DesktopNormalDistributionDTO();
        dto.setAverageValue(10D);
        new Expectations() {
            {
                desktopAPI.getDesktopNormalDistribution((ResourceTypeEnum) any);
                result = dto;
            }
        };
        DefaultWebResponse response = desktopController.getDesktopNormalDistribution(request);
        DesktopNormalDistributionDTO content = (DesktopNormalDistributionDTO) response.getContent();
        Assert.assertEquals(Status.SUCCESS, response.getStatus());
        Assert.assertEquals(dto.getAverageValue(), content.getAverageValue());
        new Verifications() {
            {
                desktopAPI.getDesktopNormalDistribution((ResourceTypeEnum) any);
                times = 1;
            }
        };
    }

    /**
     * 根据云桌面id获取桌面资源使用情况
     * @throws BusinessException 异常
     */
    @Test
    public void getDesktopUsageTest() throws BusinessException {
        IdArrWebRequest webRequest = new IdArrWebRequest();
        webRequest.setIdArr(new UUID[]{UUID.randomUUID()});

        new Expectations() {
            {
                desktopAPI.getDesktopUsage((DesktopUsageIdArrRequest) any);
                result = new DesktopUsageResponse();
            }
        };

        DefaultWebResponse webResponse = desktopController.getDesktopUsage(webRequest);
        Assert.assertEquals(Status.SUCCESS, webResponse.getStatus());
    }

    /**
     * 获取异常关注虚机列表
     * @throws BusinessException 异常
     */
    @Test
    public void listAbnormalDesktopTest() throws BusinessException {
        List<UUID> idList = Lists.newArrayList();
        idList.add(UUID.randomUUID());
        AbnormalDesktopListResponse apiResponse = new AbnormalDesktopListResponse();
        apiResponse.setIdList(idList);

        AbnormalPageWebRequest webRequest = new AbnormalPageWebRequest();
        webRequest.setAbnormalType(AbnormalTypeEnum.ABNORMAL);
        webRequest.setResourceType(ResourceTypeEnum.CPU);

        new Expectations() {
            {
                desktopAPI.listAbnormalDesktop((AbnormalPageRequest) any);
                result = apiResponse;
            }
        };

        DefaultWebResponse webResponse = desktopController.listAbnormalDesktop(webRequest);

        Assert.assertEquals(Status.SUCCESS, webResponse.getStatus());
    }

    /**
     * 获取异常关注虚机列表，返回列表为空
     * @throws BusinessException 异常
     */
    @Test
    public void listAbnormalDesktopEmptyTest() throws BusinessException {
        List<UUID> idList = Lists.newArrayList();
        idList.add(UUID.randomUUID());
        AbnormalDesktopListResponse apiResponse = new AbnormalDesktopListResponse();
        apiResponse.setIdList(idList);

        CbbDeskDTO cbbDeskDTO = new CbbDeskDTO();
        cbbDeskDTO.setName("xxx");

        AbnormalPageWebRequest webRequest = new AbnormalPageWebRequest();
        webRequest.setAbnormalType(AbnormalTypeEnum.ABNORMAL);
        webRequest.setResourceType(ResourceTypeEnum.CPU);

        new Expectations() {
            {
                desktopAPI.listAbnormalDesktop((AbnormalPageRequest) any);
                result = apiResponse;
                cbbVDIDeskMgmtAPI.getDeskVDI((UUID) any);
                result = cbbDeskDTO;
            }
        };

        DefaultWebResponse webResponse = desktopController.listAbnormalDesktop(webRequest);

        Assert.assertNotNull(webResponse.getContent());
    }

    /**
     * 获取部门下所有云桌面一个月内各资源每天均值列表
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetUserGroupOverview() throws BusinessException {
        GetUserGroupOverviewWebRequest request = new GetUserGroupOverviewWebRequest();
        request.setGroupId(UUID.randomUUID());
        request.setStartTime(new Date());
        request.setEndTime(new Date());

        new Expectations() {
            {
                desktopAPI.getUserGroupOverview((GetUserGroupOverviewRequest) any);
                result = new UserGroupOverviewResponse();
            }
        };

        DefaultWebResponse response = desktopController.getUserGroupOverview(request);

        Assert.assertEquals(Status.SUCCESS, response.getStatus());
    }

    /**
     * 分页获取部门下云桌面列表
     * @throws BusinessException 业务异常
     */
    @Test
    public void testListDesktopByUserGroup() throws BusinessException {
        ListDesktopByUserGroupPageWebRequest request = new ListDesktopByUserGroupPageWebRequest();
        request.setGroupId(UUID.randomUUID());

        new Expectations() {
            {
                desktopAPI.listDesktopByUserGroup((ListDesktopByUserGroupPageRequest) any);
                result = new ListDesktopResponse();
            }
        };

        DefaultWebResponse response = desktopController.listDesktopByUserGroup(request);

        new Verifications() {
            {
                desktopAPI.listDesktopByUserGroup((ListDesktopByUserGroupPageRequest) any);
                times = 1;
            }
        };
    }
}