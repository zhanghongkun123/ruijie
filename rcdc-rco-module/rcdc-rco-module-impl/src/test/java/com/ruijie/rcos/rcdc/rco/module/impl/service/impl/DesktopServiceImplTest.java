package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbListVMMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbMonitorDataDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbVMMonitorInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.StatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AllDesktopOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopNormalDistributionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.DesktopUsageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AbnormalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskTypeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.AbnormalPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DesktopUsageIdArrRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsItem;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.DesktopStatisticsResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopStartCountDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopResourceUsageDayEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.DesktopNormalDistributionService;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 13:38
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class DesktopServiceImplTest {

    @Tested
    private DesktopServiceImpl desktopServiceImpl;

    @Injectable
    private DesktopStartCountDayDAO desktopStartCountDayDAO;

    @Injectable
    private StatisticsAPI statisticsAPI;

    @Injectable
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;

    @Injectable
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Injectable
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Injectable
    private DesktopNormalDistributionService desktopNormalDistributionService;

    /**
     * 集群云桌面概要 缓存中有数据
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetAllDesktopOverview() throws BusinessException {

        desktopServiceImpl.getAllDesktopOverview();
        Assert.assertTrue(true);

    }

    /**
     * 集群云桌面概要 缓存无数据
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testGetAllDesktopOverviewWithNull() throws BusinessException {

        AllDesktopOverviewDTO dto = new AllDesktopOverviewDTO();
        dto.setTotal(10);
        Integer count = 1;
        DesktopStatisticsResponse desktopResponse = new DesktopStatisticsResponse();
        DesktopStatisticsItem item = new DesktopStatisticsItem();
        item.setTotal(10);
        item.setOnline(5);
        desktopResponse.setIdv(item);
        desktopResponse.setVdi(item);
        new Expectations() {
            {
                desktopStartCountDayDAO.getAverageStartCount();
                result = count;
                desktopStartCountDayDAO.getMaxStartCount();
                result = count;
                statisticsAPI.statisticsDesktop((DeskTypeRequest) any);
                result = desktopResponse;
            }
        };
        AllDesktopOverviewDTO desktopOverviewDTO = desktopServiceImpl.getAllDesktopOverview();
        Assert.assertEquals(dto.getTotal(), desktopOverviewDTO.getTotal());
        new Verifications() {
            {
                desktopStartCountDayDAO.getAverageStartCount();
                times = 1;
                desktopStartCountDayDAO.getMaxStartCount();
                times = 1;
                statisticsAPI.statisticsDesktop((DeskTypeRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 根据云桌面id获取指定时间内桌面资源平均使用情况（HCI），正常流程
     *
     * @throws BusinessException 异常
     */
    @Test
    public void listDesktopUsageByIdTest() throws BusinessException {
        UUID deskId = UUID.randomUUID();
        DesktopUsageIdArrRequest request = new DesktopUsageIdArrRequest();
        request.setIdArr(new UUID[]{deskId});
        request.setStartTime(new Date());
        request.setEndTime(new Date());

        CbbMonitorDataDTO<Double> dataDTO = new CbbMonitorDataDTO<>();
        dataDTO.setCollectTime(1L);
        dataDTO.setValue(1D);
        List<CbbMonitorDataDTO<Double>> dataDTOList = Lists.newArrayList();
        dataDTOList.add(dataDTO);
        CbbVMMonitorInfoDTO infoDTO = new CbbVMMonitorInfoDTO();
        infoDTO.setUuid(deskId);
        infoDTO.setCpuUseRateDTOList(dataDTOList);
        infoDTO.setMemoryUseRateDTOList(dataDTOList);
        infoDTO.setStorageUseRateDTOList(dataDTOList);
        List<CbbVMMonitorInfoDTO> infoDTOList = Lists.newArrayList();
        infoDTOList.add(infoDTO);

        CbbDeskDTO deskDTO = new CbbDeskDTO();
        deskDTO.setName("xxx");

        new Expectations() {
            {
                cbbVDIDeskMgmtAPI.listVMMonitorInfo((CbbListVMMonitorInfoDTO) any);
                result = infoDTOList;
                cbbVDIDeskMgmtAPI.getDeskVDI((UUID) any);
                result = deskDTO;
            }
        };

        List<DesktopUsageDTO> usageDTOList = desktopServiceImpl.listDesktopUsageById(request.getIdArr(),
                request.getStartTime(), request.getEndTime());

        Assert.assertTrue(usageDTOList.size() > 0);
    }

    /**
     * 根据云桌面id获取指定时间内桌面资源平均使用情况（HCI），异常情况
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testListDesktopUsageByIdWithBusinessException() throws BusinessException {
        UUID deskId = UUID.randomUUID();
        DesktopUsageIdArrRequest request = new DesktopUsageIdArrRequest();
        request.setIdArr(new UUID[]{deskId});
        request.setStartTime(new Date());
        request.setEndTime(new Date());

        new Expectations() {
            {
                cbbVDIDeskMgmtAPI.listVMMonitorInfo((CbbListVMMonitorInfoDTO) any);
                result = new BusinessException("rcdc-hciadapter_http_call_error", new String[0]);
            }
        };

        List<DesktopUsageDTO> usageDTOList = desktopServiceImpl.listDesktopUsageById(request.getIdArr(),
                request.getStartTime(), request.getEndTime());
        Assert.assertTrue(true);
    }

    /**
     * 按列表类型查询CPU使用率异常的云桌面，正常流程
     *
     * @throws BusinessException 异常
     */
    @Test
    public void listCpuAbnormalDesktopTest() throws BusinessException {
        AbnormalPageRequest pageRequest = new AbnormalPageRequest();
        pageRequest.setResourceType(ResourceTypeEnum.CPU);
        pageRequest.setAbnormalType(AbnormalTypeEnum.ABNORMAL);

        DesktopNormalDistributionDTO distributionDTO = new DesktopNormalDistributionDTO();
        distributionDTO.setAverageValue(50D);
        distributionDTO.setStandardDeviation(28.87D);
        Map<ResourceTypeEnum, DesktopNormalDistributionDTO> distributionMap = Maps.newHashMap();
        distributionMap.put(ResourceTypeEnum.CPU, distributionDTO);

        DesktopResourceUsageDayEntity entity = new DesktopResourceUsageDayEntity();
        entity.setDesktopId(UUID.randomUUID());
        List<DesktopResourceUsageDayEntity> entityList = Lists.newArrayList();
        entityList.add(entity);

        CbbDeskDTO deskDTO = new CbbDeskDTO();
        deskDTO.setName("desktop");

        new Expectations() {
            {
                desktopNormalDistributionService.normalDistribution();
                result = distributionMap;
                desktopResourceUsageDayDAO.queryCpuUsageInRange((Double) any, (Double) any);
                result = entityList;
            }
        };

        List<DesktopResourceUsageDayEntity> abnormalList = desktopServiceImpl.listAbnormalDesktop(ResourceTypeEnum.CPU,
                AbnormalTypeEnum.ABNORMAL);
        pageRequest.setAbnormalType(AbnormalTypeEnum.ULTRALOWLOAD);
        List<DesktopResourceUsageDayEntity> ultraLowlList = desktopServiceImpl.listAbnormalDesktop(ResourceTypeEnum.CPU,
                AbnormalTypeEnum.ULTRALOWLOAD);
        pageRequest.setAbnormalType(AbnormalTypeEnum.ULTRAHIGHLOAD);
        List<DesktopResourceUsageDayEntity> ultraHighlList = desktopServiceImpl
                .listAbnormalDesktop(ResourceTypeEnum.CPU,
                        AbnormalTypeEnum.ULTRAHIGHLOAD);

        Assert.assertFalse(CollectionUtils.isEmpty(abnormalList));
        Assert.assertFalse(CollectionUtils.isEmpty(ultraLowlList));
        Assert.assertFalse(CollectionUtils.isEmpty(ultraHighlList));
    }

    /**
     * 根据云桌面id获取指定时间内桌面资源平均使用情况（日表），正常流程
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void listDesktopDayUsageTest() throws BusinessException {
        DesktopUsageIdArrRequest request = new DesktopUsageIdArrRequest();
        request.setStartTime(new Date());
        request.setEndTime(new Date());

        DesktopResourceUsageDayEntity entity = new DesktopResourceUsageDayEntity();
        entity.setDesktopId(UUID.randomUUID());
        entity.setCpuUsage(50D);
        entity.setMemoryUsage(50D);
        entity.setDiskUsage(50D);
        entity.setStatisticTime(new Date());
        entity.setCreateTime(new Date());
        List<DesktopResourceUsageDayEntity> entityList = Lists.newArrayList();
        entityList.add(entity);

        new Expectations() {
            {
                desktopResourceUsageDayDAO
                        .getByDesktopIdInAndStatisticTimeBetween((UUID[]) any, (Date) any, (Date) any);
                result = entityList;
            }
        };

        List<DesktopUsageDTO> dtoList = desktopServiceImpl.listDesktopDayUsage(request);

        Assert.assertFalse(CollectionUtils.isEmpty(dtoList));
    }

    /**
     * 获取用户组下所有云桌面信息，正常流程
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetChildGroupId() throws BusinessException {
        UUID uuid = UUID.randomUUID();
        IacUserGroupDetailDTO userGroupDTO = new IacUserGroupDetailDTO();
        userGroupDTO.setId(UUID.randomUUID());
        userGroupDTO.setParentId(uuid);
        IacUserGroupDetailDTO userGroupDTO2 = new IacUserGroupDetailDTO();
        userGroupDTO.setId(uuid);
        userGroupDTO.setParentId(UUID.randomUUID());

        new Expectations() {
            {
                cbbUserGroupAPI.getAllUserGroup();
                result = new IacUserGroupDetailDTO[]{userGroupDTO, userGroupDTO2};
            }
        };
        List<UUID> childGroupIdList = desktopServiceImpl.getChildGroupId(uuid);
        Assert.assertEquals(childGroupIdList.get(0), uuid);
    }

    /**
     * 获取用户组下所有云桌面信息，用户组不存在
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetChildGroupIdWithGroupIdNotExist() throws BusinessException {
        UUID uuid = UUID.randomUUID();

        try {
            desktopServiceImpl.getChildGroupId(uuid);
        } catch (BusinessException ex) {
            String str = ex.getKey();
            Assert.assertEquals(BusinessKey.RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_GROUP_ID_NOT_EXIST, str);
        }
    }

    /**
     * 获取用户组下所有云桌面信息，获取用户组信息失败
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetChildGroupIdWithGroupNotFound() throws BusinessException {
        UUID uuid = UUID.randomUUID();

        new Expectations() {
            {
                cbbUserGroupAPI.getAllUserGroup();
                result = null;
            }
        };

        try {
            desktopServiceImpl.getChildGroupId(uuid);
        } catch (BusinessException ex) {
            String str = ex.getKey();
            Assert.assertEquals(BusinessKey.RCDC_RCO_BIGSCREEN_GROUP_OVERVIEW_GROUP_NOT_FOUND, str);
        }
    }


    /**
     * 校验镜像虚机运行状态，关闭
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testValidateImageTemplateDesktopNotRunningClose() throws BusinessException {
        CbbDeskInfoDTO deskInfoDTO = new CbbDeskInfoDTO();
        deskInfoDTO.setDeskState(CbbCloudDeskState.CLOSE);
        List<CbbDeskInfoDTO> cbbDeskInfoDTOList = Lists.newArrayList();
        cbbDeskInfoDTOList.add(deskInfoDTO);

        new Expectations() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                result = cbbDeskInfoDTOList;
            }
        };

        desktopServiceImpl.validateImageTemplateDesktopNotRunning(UUID.randomUUID(), "imageName");

        new Verifications() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 校验镜像虚机运行状态，错误
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testValidateImageTemplateDesktopNotRunningError() throws BusinessException {
        CbbDeskInfoDTO deskInfoDTO = new CbbDeskInfoDTO();
        deskInfoDTO.setDeskState(CbbCloudDeskState.ERROR);
        List<CbbDeskInfoDTO> cbbDeskInfoDTOList = Lists.newArrayList();
        cbbDeskInfoDTOList.add(deskInfoDTO);

        new Expectations() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                result = cbbDeskInfoDTOList;
            }
        };

        desktopServiceImpl.validateImageTemplateDesktopNotRunning(UUID.randomUUID(), "imageName");

        new Verifications() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 校验镜像虚机运行状态，删除
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testValidateImageTemplateDesktopNotRunningRecycleBin() throws BusinessException {
        CbbDeskInfoDTO deskInfoDTO = new CbbDeskInfoDTO();
        deskInfoDTO.setDeskState(CbbCloudDeskState.RECYCLE_BIN);
        List<CbbDeskInfoDTO> cbbDeskInfoDTOList = Lists.newArrayList();
        cbbDeskInfoDTOList.add(deskInfoDTO);

        new Expectations() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                result = cbbDeskInfoDTOList;
            }
        };

        desktopServiceImpl.validateImageTemplateDesktopNotRunning(UUID.randomUUID(), "imageName");

        new Verifications() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 校验镜像虚机运行状态，运行中
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testValidateImageTemplateDesktopNotRunning() throws BusinessException {
        CbbDeskInfoDTO deskInfoDTO = new CbbDeskInfoDTO();
        deskInfoDTO.setDeskState(CbbCloudDeskState.RUNNING);
        List<CbbDeskInfoDTO> cbbDeskInfoDTOList = Lists.newArrayList();
        cbbDeskInfoDTOList.add(deskInfoDTO);

        new Expectations() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                result = cbbDeskInfoDTOList;
            }
        };

        try {
            desktopServiceImpl.validateImageTemplateDesktopNotRunning(UUID.randomUUID(), "imageName");
            Assert.fail();
        } catch (BusinessException e) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_CLOUDDESKTOP_START_VM_DESKTOP_NOT_CLOSE_ALL_FAIL, e.getKey());
        }

        new Verifications() {
            {
                cbbVDIDeskMgmtAPI.listDeskInfoByImageTemplate((UUID) any);
                times = 1;
            }
        };
    }
}