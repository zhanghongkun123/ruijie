package com.ruijie.rcos.rcdc.rco.module.impl.appcenter.api;


import com.alibaba.fastjson.JSONArray;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestApplicationAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbAppTestTargetDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppTestAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.*;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppTestApplicationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppTestDeskAppDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppTestDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestApplicationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestDeskAppEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppTestEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamAppTestService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UamAppTestServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月29日
 *
 * @author zhk
 */
public class UamAppTestAPIImpl implements UamAppTestAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UamAppTestAPIImpl.class);

    @Autowired
    UamAppTestServiceTx uamAppTestServiceTx;

    @Autowired
    CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    CbbUamAppTestApplicationAPI cbbUamAppTestApplicationAPI;

    @Autowired
    CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    @Autowired
    UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    ViewUamAppTestDesktopDAO viewUamAppTestDesktopDAO;

    @Autowired
    ViewUamAppTestApplicationDAO viewUamAppTestApplicationDAO;

    @Autowired
    ViewUamAppTestDeskAppDAO viewUamAppTestDeskAppDAO;

    @Autowired
    private ViewUamAppTestService viewUamAppTestService;

    @Autowired
    private BaseSystemLogMgmtAPI systemLogMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;


    @Override
    public UUID createAppTest(UamAppTestDTO dto) throws BusinessException {
        Assert.notNull(dto, "UpmAppTestDTO  can not be null");

        return uamAppTestServiceTx.createAppTest(dto);
    }


    @Override
    public void addAppTestDesk(UUID testId, List<UUID> deskIdList) throws BusinessException {
        Assert.notNull(testId, "testId  can not be null");
        Assert.notEmpty(deskIdList, "deskIdList  can not be null");

        uamAppTestServiceTx.addAppTestDesk(testId, deskIdList);
    }

    @Override
    public PageQueryResponse<AppTestDesktopInfoDTO> pageQueryAppTestDesktop(PageQueryRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "pageRequest  can not be null");

        final PageQueryResponse<ViewUamAppTestDesktopEntity> response = viewUamAppTestDesktopDAO.pageQuery(pageRequest);
        final AppTestDesktopInfoDTO[] itemArr = Arrays.stream(response.getItemArr()).map(entity -> {
            final AppTestDesktopInfoDTO dto = new AppTestDesktopInfoDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toArray(AppTestDesktopInfoDTO[]::new);
        return new PageQueryResponse<>(itemArr, response.getTotal());
    }

    @Override
    public PageQueryResponse<AppTestApplicationInfoDTO> pageQueryAppTestApp(PageQueryRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "pageRequest  can not be null");

        final PageQueryResponse<ViewUamAppTestApplicationEntity> response = viewUamAppTestApplicationDAO.pageQuery(pageRequest);
        final AppTestApplicationInfoDTO[] itemArr = Arrays.stream(response.getItemArr()).map(entity -> {
            final AppTestApplicationInfoDTO dto = new AppTestApplicationInfoDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toArray(AppTestApplicationInfoDTO[]::new);
        return new PageQueryResponse<>(itemArr, response.getTotal());
    }

    @Override
    public List<AppTestDesktopInfoDTO> findByDeskIdAndTestStateIn(UUID deskId, List<DesktopTestStateEnum> desktopTestStateEnumList) {
        Assert.notNull(deskId, "deskId  can not be null");
        Assert.notEmpty(desktopTestStateEnumList, "desktopTestStateEnumList  can not be null");

        final List<ViewUamAppTestDesktopEntity> uamAppTestDesktopEntityList =
                viewUamAppTestDesktopDAO.findByDeskIdAndTestStateIn(deskId, desktopTestStateEnumList);
        return uamAppTestDesktopEntityList.stream().map(entity -> {
            final AppTestDesktopInfoDTO dto = new AppTestDesktopInfoDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());

    }

    @Override
    public AppTestDesktopInfoDTO findByAppTestIdAndDeskId(UUID testId, UUID deskId) {
        Assert.notNull(testId, "testId  can not be null");
        Assert.notNull(deskId, "deskId  can not be null");
        final Optional<ViewUamAppTestDesktopEntity> desktopEntity = viewUamAppTestDesktopDAO.findByTestIdAndDeskId(testId, deskId);
        return desktopEntity.map(e -> {
            AppTestDesktopInfoDTO appTestDesktopInfoDTO = new AppTestDesktopInfoDTO();
            BeanUtils.copyProperties(e, appTestDesktopInfoDTO);
            return appTestDesktopInfoDTO;
        }).orElse(null);
    }

    @Override
    public List<AppTestDesktopInfoDTO> findByTestStateIn(List<DesktopTestStateEnum> desktopTestStateEnumList) {
        Assert.notEmpty(desktopTestStateEnumList, "desktopTestStateEnumList  can not be null");

        final List<ViewUamAppTestDesktopEntity> uamAppTestDesktopEntityList = viewUamAppTestDesktopDAO.findByTestStateIn(desktopTestStateEnumList);
        return uamAppTestDesktopEntityList.stream().map(entity -> {
            final AppTestDesktopInfoDTO dto = new AppTestDesktopInfoDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public DefaultPageResponse<RcoUamAppTestDTO> pageUamDeliveryTest(SearchDeliveryTestDTO searchDeliveryTestDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryTestDTO, "searchDeliveryTestDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUamAppTestEntity> viewUamAppTestPage = viewUamAppTestService.pageUamAppTest(searchDeliveryTestDTO, pageable);

        Page<RcoUamAppTestDTO> rcoUamAppTestPage = viewUamAppTestPage.map(entity -> {
            RcoUamAppTestDTO rcoUamAppTestDTO = new RcoUamAppTestDTO();
            BeanUtils.copyProperties(entity, rcoUamAppTestDTO);
            return rcoUamAppTestDTO;
        });

        DefaultPageResponse<RcoUamAppTestDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(rcoUamAppTestPage.stream().toArray(RcoUamAppTestDTO[]::new));
        defaultPageResponse.setTotal(rcoUamAppTestPage.getTotalElements());
        return defaultPageResponse;
    }

    @Override
    public PageQueryResponse<AppTestDeskAppInfoDTO> pageQueryAppTestDeskApp(PageQueryRequest pageRequest) throws BusinessException {
        Assert.notNull(pageRequest, "pageRequest  can not be null");

        final PageQueryResponse<ViewUamAppTestDeskAppEntity> response = viewUamAppTestDeskAppDAO.pageQuery(pageRequest);
        final AppTestDeskAppInfoDTO[] itemArr = Arrays.stream(response.getItemArr()).map(entity -> {
            final AppTestDeskAppInfoDTO dto = new AppTestDeskAppInfoDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).toArray(AppTestDeskAppInfoDTO[]::new);
        return new PageQueryResponse<>(itemArr, response.getTotal());
    }

    @Override
    public boolean existTestingDesk(List<UUID> deskIdList) {
        Assert.notEmpty(deskIdList, "deskIdList  can not be null");

        final List<ViewUamAppTestDesktopEntity> uamAppTestDesktopEntityList =
                viewUamAppTestDesktopDAO.findAllByDeskIdInAndTestStateIn(deskIdList, DesktopTestStateEnum.getProcessingStateList());
        return uamAppTestDesktopEntityList.size() > 0;
    }

    @Override
    public void deleteCompletedTestDeskWhenStrategyModify(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");

        List<CbbAppTestTargetDTO> testTargetDTOList = cbbUamAppTestTargetAPI.findByResourceIdAndInState(deskId,
                DesktopTestStateEnum.getFinishedStateList());
        if (CollectionUtils.isEmpty(testTargetDTOList)) {
            return;
        }
        LOGGER.info("删除桌面[{}]关联应用测试对象[{}]", deskId, JSONArray.toJSON(testTargetDTOList));
        CbbDeskDTO deskInfo = cbbDeskMgmtAPI.getDeskById(deskId);
        CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(testTargetDTOList.get(0).getTestId());
        for (CbbAppTestTargetDTO testTargetDTO : testTargetDTOList) {
            cbbUamAppTestTargetAPI.deleteAppTestTarget(testTargetDTO.getTestId(), testTargetDTO.getResourceId());
            recordDeleteSystemLog(deskInfo, uamAppTestInfo);
        }

    }

    @Override
    public List<AppTestDesktopInfoDTO> findAllByDeskIdInAndTestStateIn(List<UUID> deskIdList, List<DesktopTestStateEnum> desktopTestStateEnumList) {
        Assert.notNull(deskIdList, "deskIdList can not be null");
        Assert.notNull(desktopTestStateEnumList, "desktopTestStateEnumList can not be null");

        final List<ViewUamAppTestDesktopEntity> uamAppTestDesktopEntityList =
                viewUamAppTestDesktopDAO.findAllByDeskIdInAndTestStateIn(deskIdList, desktopTestStateEnumList);

        return uamAppTestDesktopEntityList.stream().map(desk -> {
            final AppTestDesktopInfoDTO dto = new AppTestDesktopInfoDTO();
            BeanUtils.copyProperties(desk, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void checkTestingDesk(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        final List<AppTestDesktopInfoDTO> desktopInfoDTOList =
                this.findByDeskIdAndTestStateIn(deskId, DesktopTestStateEnum.getProcessingStateList());
        if (!CollectionUtils.isEmpty(desktopInfoDTOList)) {
            final AppTestDesktopInfoDTO appTestDesktopInfoDTO = desktopInfoDTOList.get(0);
            final CbbUamAppTestDTO uamAppTestInfo = cbbUamAppTestAPI.getUamAppTestInfo(appTestDesktopInfoDTO.getTestId());
            LOGGER.error("云桌面[{}]正在测试任务[{}]中测试，不允许操作", appTestDesktopInfoDTO.getDesktopName(), uamAppTestInfo.getName());
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_STATE_ERROR,
                    appTestDesktopInfoDTO.getDesktopName(), uamAppTestInfo.getName());
        }
    }

    private void recordDeleteSystemLog(CbbDeskDTO deskInfo, CbbUamAppTestDTO uamAppTestInfo) {
        BaseSystemLogDTO logRequest = new BaseSystemLogDTO();
        logRequest.setId(UUID.randomUUID());
        logRequest.setContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UAM_DELETE_TEST_TARGET, deskInfo.getName(), uamAppTestInfo.getName()));
        logRequest.setCreateTime(new Date());
        systemLogMgmtAPI.createSystemLog(logRequest);
    }
}
