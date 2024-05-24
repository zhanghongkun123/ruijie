package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskFaultInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AbnormalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.AbnormalPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetUserGroupOverviewRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListDesktopByUserGroupPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.DesktopUsageResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageMonthDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopNormalDistributionService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 13:29
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class DesktopAPIImplTest {

    @Tested
    private DesktopAPIImpl desktopAPIImpl;

    @Injectable
    private DesktopService desktopService;

    @Injectable
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Injectable
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Injectable
    private DesktopResourceUsageMonthDAO desktopResourceUsageMonthDAO;

    @Injectable
    private DesktopNormalDistributionService desktopNormalDistributionService;

    @Injectable
    private DeskFaultInfoAPI deskFaultInfoAPI;

    @Injectable
    private UserTerminalDAO userTerminalDAO;

    @Injectable
    private QueryCloudDesktopService queryCloudDesktopService;

    @Injectable
    private IacUserMgmtAPI cbbUserAPI;

    /**
     * 测试获取云桌面概述
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetAllDesktopOverview() throws BusinessException {
        AllDesktopOverviewDTO allDesktopOverviewDTO = new AllDesktopOverviewDTO();
        allDesktopOverviewDTO.setHistoryBootUpTop(1);
        new Expectations() {
            {
                desktopService.getAllDesktopOverview();
                result = allDesktopOverviewDTO;
            }
        };
        AllDesktopOverviewDTO dto = desktopAPIImpl.getAllDesktopOverview();
        Assert.assertEquals(dto.getBootUp(), allDesktopOverviewDTO.getBootUp());
        new Verifications() {
            {
                desktopService.getAllDesktopOverview();
                times = 1;
            }
        };
    }

    /**
     * 根据云桌面id获取桌面资源使用情况单元测试
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void getDesktopUsageTest() throws BusinessException {
        DesktopUsageIdArrRequest request = new DesktopUsageIdArrRequest();

        new Expectations() {
            {
                desktopService.listDesktopUsageById((UUID[]) any, (Date) any, (Date) any);
                result = new DesktopUsageResponse();
            }
        };

        desktopAPIImpl.getDesktopUsage(request);

        Assert.assertTrue(true);
    }

    /**
     * 获取云桌面正太分布数据单元测试
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetDesktopNormalDistribution() throws BusinessException {

        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> normalDistributionMap = Maps.newHashMap();
        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> normalDistributionMap2 = Maps.newHashMap();

        DesktopNormalDistributionDTO desktopNormalDistributionDTO = new DesktopNormalDistributionDTO();
        desktopNormalDistributionDTO.setAverageValue(10D);
        desktopNormalDistributionDTO.setStandardDeviation(10D);
        desktopNormalDistributionDTO.setNormalNumLeft(5);
        desktopNormalDistributionDTO.setNormalNumRight(5);
        desktopNormalDistributionDTO.setLowLoad(5);
        desktopNormalDistributionDTO.setHighLoad(5);
        desktopNormalDistributionDTO.setUltraLowLoad(5);
        desktopNormalDistributionDTO.setUltraHighLoad(5);
        desktopNormalDistributionDTO.setLowLoadAbnormal(5);
        desktopNormalDistributionDTO.setHighLoadAbnormal(5);
        normalDistributionMap.put(ResourceTypeEnum.CPU, desktopNormalDistributionDTO);
        normalDistributionMap2.put(ResourceTypeEnum.MEMORY, desktopNormalDistributionDTO);
        new Expectations() {
            {
                desktopNormalDistributionService.normalDistribution();
                result = normalDistributionMap;
                result = normalDistributionMap2;
                result = null;
            }
        };

        DesktopNormalDistributionDTO desktopNormalDistribution = desktopAPIImpl.getDesktopNormalDistribution(ResourceTypeEnum.CPU);
        Assert.assertEquals(desktopNormalDistribution.getAverageValue(), desktopNormalDistributionDTO.getAverageValue());
        DesktopNormalDistributionDTO desktopNormalDistribution2 = desktopAPIImpl.getDesktopNormalDistribution(ResourceTypeEnum.MEMORY);
        Assert.assertEquals(desktopNormalDistribution2.getAverageValue(), desktopNormalDistributionDTO.getAverageValue());
        desktopAPIImpl.getDesktopNormalDistribution(ResourceTypeEnum.CPU);
        Assert.assertTrue(true);
        new Verifications() {
            {
                desktopNormalDistributionService.normalDistribution();
                times = 3;
            }
        };
    }

    /**
     * 获取云桌面正太分布数据单元测试异常
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetDesktopNormalDistributionWithBusinessException() throws BusinessException {

        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> normalDistributionMap = Maps.newHashMap();

        DesktopNormalDistributionDTO desktopNormalDistributionDTO = new DesktopNormalDistributionDTO();
        desktopNormalDistributionDTO.setAverageValue(10D);
        desktopNormalDistributionDTO.setStandardDeviation(10D);
        desktopNormalDistributionDTO.setNormalNumLeft(5);
        desktopNormalDistributionDTO.setNormalNumRight(5);
        desktopNormalDistributionDTO.setLowLoad(5);
        desktopNormalDistributionDTO.setHighLoad(5);
        desktopNormalDistributionDTO.setUltraLowLoad(5);
        desktopNormalDistributionDTO.setUltraHighLoad(5);
        desktopNormalDistributionDTO.setLowLoadAbnormal(5);
        desktopNormalDistributionDTO.setHighLoadAbnormal(5);
        normalDistributionMap.put(ResourceTypeEnum.DISK, desktopNormalDistributionDTO);

        new Expectations() {
            {
                desktopNormalDistributionService.normalDistribution();
                result = normalDistributionMap;
            }
        };

        desktopAPIImpl.getDesktopNormalDistribution(ResourceTypeEnum.DISK);

        new Verifications() {
            {
                desktopNormalDistributionService.normalDistribution();
                times = 1;
            }
        };
    }

    /**
     * 获取部门下所有云桌面一个月内各资源每天均值列表，正常流程
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetUserGroupOverview() throws BusinessException {
        GetUserGroupOverviewRequest request = new GetUserGroupOverviewRequest();
        request.setGroupId(UUID.randomUUID());

        DefaultPageResponse<CloudDesktopDTO> apiPageResponse = new DefaultPageResponse<>();
        CloudDesktopDTO desktopDTO = new CloudDesktopDTO();
        apiPageResponse.setItemArr(new CloudDesktopDTO[] {desktopDTO});

        List<DesktopResourceUsageDayEntity> entityList = Lists.newArrayList();
        DesktopResourceUsageDayEntity entity = new DesktopResourceUsageDayEntity();
        entity.setStatisticTime(new Date());
        entity.setDiskUsage(10.2);
        entity.setMemoryUsage(10.2);
        entity.setCpuUsage(10.2);
        entityList.add(entity);
        new Expectations() {
            {
                desktopService.getChildGroupId((UUID) any);
                result = Lists.newArrayList();
                userDesktopMgmtAPI.pageQuery((PageSearchRequest) any);
                result = apiPageResponse;
                desktopResourceUsageDayDAO.getAverageUsageGroupByStatisticTime((List<UUID>) any, (Date) any, (Date) any);
                result = entityList;
            }
        };

        desktopAPIImpl.getUserGroupOverview(request);

        new Verifications() {
            {
                desktopService.getChildGroupId((UUID) any);
                times = 1;
                userDesktopMgmtAPI.pageQuery((PageSearchRequest) any);
                times = 1;
                desktopResourceUsageDayDAO.getAverageUsageGroupByStatisticTime((List<UUID>) any, (Date) any, (Date) any);
                times = 1;
            }
        };
    }

    /**
     * 获取异常云桌面
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testListAbnormalDesktop() throws BusinessException {
        List<DesktopResourceUsageDayEntity> entityList = Lists.newArrayList();
        DesktopResourceUsageDayEntity entity = new DesktopResourceUsageDayEntity();
        entity.setDesktopId(UUID.randomUUID());
        entityList.add(entity);

        AbnormalPageRequest request = new AbnormalPageRequest();
        request.setResourceType(ResourceTypeEnum.CPU);
        request.setAbnormalType(AbnormalTypeEnum.ABNORMAL);
        request.setPage(1);
        request.setLimit(2);
        new Expectations() {
            {
                desktopService.listAbnormalDesktop((ResourceTypeEnum) any, (AbnormalTypeEnum) any);
                result = entityList;
            }
        };

        desktopAPIImpl.listAbnormalDesktop(request);

        Assert.assertTrue(true);
    }

    /**
     * 获取异常云桌面
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testListDesktopByUserGroup() throws BusinessException {
        DefaultPageResponse<CloudDesktopDTO> desktopResponse = new DefaultPageResponse();
        CloudDesktopDTO[] dtoArr = new CloudDesktopDTO[] {new CloudDesktopDTO()};
        desktopResponse.setItemArr(dtoArr);
        desktopResponse.setTotal(1);
        ListDesktopByUserGroupPageRequest request = new ListDesktopByUserGroupPageRequest();
        request.setGroupId(UUID.randomUUID());
        request.setPage(0);
        request.setLimit(10);
        new Expectations() {
            {
                userDesktopMgmtAPI.pageQuery((PageSearchRequest) any);
                result = desktopResponse;
            }
        };

        desktopAPIImpl.listDesktopByUserGroup(request);

        Assert.assertTrue(true);
    }

    /**
     * 获取异常云桌面出现异常
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testListDesktopByUserGroupWithBusinessException() throws BusinessException {
        ListDesktopByUserGroupPageRequest request = new ListDesktopByUserGroupPageRequest();
        request.setGroupId(UUID.randomUUID());
        request.setPage(0);
        request.setLimit(10);

        new Expectations() {
            {
                userDesktopMgmtAPI.pageQuery((PageSearchRequest) any);
                result = null;
            }
        };

        try {
            desktopAPIImpl.listDesktopByUserGroup(request);
        } catch (BusinessException ex) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_DESKTOP_NOT_FOUND, ex.getKey());
        }

    }

    /**
     * 校验镜像虚机运行状态
     * 
     * @throws BusinessException 异常
     */
    @Test
    public void testValidateImageTemplateDesktopNotRunning() throws BusinessException {
        UUID imageId = UUID.randomUUID();
        String imageName = "imageName";

        desktopAPIImpl.validateImageTemplateDesktopNotRunning(imageId, imageName);

        new Verifications() {
            {
                UUID imgId;
                String imgName;
                desktopService.validateImageTemplateDesktopNotRunning(imgId = withCapture(), imgName = withCapture());
                Assert.assertEquals(imageId, imgId);
                Assert.assertEquals(imageName, imgName);
            }
        };
    }
}

