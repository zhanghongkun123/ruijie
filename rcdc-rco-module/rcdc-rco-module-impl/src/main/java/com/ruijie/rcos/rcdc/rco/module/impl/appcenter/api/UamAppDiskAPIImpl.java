package com.ruijie.rcos.rcdc.rco.module.impl.appcenter.api;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTaskAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.request.CbbAppRelativeDesktopRequest;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppSoftwarePackageVersionState;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppDiskAPI;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.CbbAppRelativeDeskInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppDiskVersionRelativeDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppTestDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamAppDiskVersionRelativeDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UamAppService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月31日
 *
 * @author xgx
 */
public class UamAppDiskAPIImpl implements UamAppDiskAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(UamAppDiskAPIImpl.class);

    @Autowired
    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private ViewUamAppTestDesktopDAO viewUamAppTestDesktopDAO;

    @Autowired
    private ViewUamAppDiskVersionRelativeDesktopDAO viewUamAppDiskVersionRelativeDesktopDAO;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    private CbbUamAppTestTaskAPI cbbUamAppTestTaskAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI appSoftPackageMgmtAPI;

    @Autowired
    private UamAppService appService;




    @Override
    public boolean canUpdateAppDisk(UUID appId) throws BusinessException {
        Assert.notNull(appId, "appId can not be null");

        CbbUamAppDTO uamApp = cbbAppStoreMgmtAPI.getUamApp(appId);
        if (AppStatusEnum.checkCanContinueEditAppSoftPackage(uamApp.getAppStatus()) && uamApp.getAppStatus() != AppStatusEnum.PRE_PUBLISH) {
            return true;
        }

        if (AppStatusEnum.PRE_PUBLISH != uamApp.getAppStatus()) {
            LOGGER.warn("应用软件包[{}]当前状态[{}]，返回false", uamApp.getAppName(), uamApp.getAppStatus());
            return false;
        }

        AppSoftwarePackageDTO appSoftwarePackage = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(appId);
        if (appSoftwarePackage.getAppSoftwarePackageType() != CbbImageType.VDI) {
            return true;
        }


        ViewUamAppDiskVersionRelativeDesktopEntity viewUamAppDiskVersionRelativeDesktopEntity = new ViewUamAppDiskVersionRelativeDesktopEntity();
        viewUamAppDiskVersionRelativeDesktopEntity.setDeskType(CbbCloudDeskType.VDI);
        viewUamAppDiskVersionRelativeDesktopEntity.setAppId(appId);
        viewUamAppDiskVersionRelativeDesktopEntity.setAppVersionState(AppSoftwarePackageVersionState.UNAVAILABLE);

        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("deskType", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("appId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("appVersionState", ExampleMatcher.GenericPropertyMatchers.exact());
        return !viewUamAppDiskVersionRelativeDesktopDAO.exists(Example.of(viewUamAppDiskVersionRelativeDesktopEntity, exampleMatcher));

    }

    @Override
    public void pauseTest(UUID appId) throws BusinessException {
        Assert.notNull(appId, "appId can not be null");

        CbbUamAppDTO uamApp = cbbAppStoreMgmtAPI.getUamApp(appId);
        if (uamApp.getAppStatus() == AppStatusEnum.PRE_PUBLISH) {
            Boolean existsTestIngTask = cbbUamAppTestAPI.existsUsedApplication(appId);
            if (existsTestIngTask) {
                // 若存在应用关联测试中的测试任务则更新测试任务状态为暂停中
                cbbUamAppTestTaskAPI.pauseAppTestTask(appId);
            }
        }
    }


    @Override
    public List<CbbAppRelativeDeskInfo> listAppRelativeDeskInfo(CbbAppRelativeDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        ViewUamAppDiskVersionRelativeDesktopEntity viewUamAppDiskVersionRelativeDesktopEntity = new ViewUamAppDiskVersionRelativeDesktopEntity();
        viewUamAppDiskVersionRelativeDesktopEntity.setDeskType(request.getDeskType());
        viewUamAppDiskVersionRelativeDesktopEntity.setAppId(request.getAppId());
        viewUamAppDiskVersionRelativeDesktopEntity.setAppVersionState(request.getVersionState());
        ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("deskType", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("appId", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("appVersionState", ExampleMatcher.GenericPropertyMatchers.exact());
        List<ViewUamAppDiskVersionRelativeDesktopEntity> viewUamAppDiskVersionRelativeDesktopEntityList =
                viewUamAppDiskVersionRelativeDesktopDAO.findAll(Example.of(viewUamAppDiskVersionRelativeDesktopEntity, exampleMatcher));
        return viewUamAppDiskVersionRelativeDesktopEntityList.stream() //
                .map(item -> { //
                    return covertViewUamAppDiskVersionRelativeDesktopEntityToCbbAppRelativeDeskInfo(item);
                }).collect(Collectors.toList());
    }


    @Override
    public PageQueryResponse<CbbAppRelativeDeskInfo> pageQueryRelativeDeskInfo(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest can not be null");

        PageQueryResponse<ViewUamAppDiskVersionRelativeDesktopEntity> pageQueryResponse =
                viewUamAppDiskVersionRelativeDesktopDAO.pageQuery(pageQueryRequest);
        ViewUamAppDiskVersionRelativeDesktopEntity[] itemArr =
                Optional.ofNullable(pageQueryResponse.getItemArr()).orElse(new ViewUamAppDiskVersionRelativeDesktopEntity[0]);
        CbbAppRelativeDeskInfo[] cbbAppRelativeDeskInfoArr = Stream.of(itemArr)
                .map(item -> covertViewUamAppDiskVersionRelativeDesktopEntityToCbbAppRelativeDeskInfo(item)).toArray(CbbAppRelativeDeskInfo[]::new);

        PageQueryResponse<CbbAppRelativeDeskInfo> cbbAppRelativeDeskInfoPageQueryResponse = new PageQueryResponse<>();
        cbbAppRelativeDeskInfoPageQueryResponse.setItemArr(cbbAppRelativeDeskInfoArr);
        cbbAppRelativeDeskInfoPageQueryResponse.setTotal(pageQueryResponse.getTotal());

        return cbbAppRelativeDeskInfoPageQueryResponse;
    }

    @Override
    public void isExistTempVmRunningThrowEx(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");
        appService.isExistTempVmRunningThrowEx(imageId);
    }

    @Override
    public void isExistRelateAppByImageIdThrowEx(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");

        appService.isExistRelateAppByImageIdThrowEx(imageId);
    }

    @Override
    public void isExistRelateAppByNetworkIdThrowEx(UUID networkId) throws BusinessException {
        Assert.notNull(networkId, "networkId can not be null");
        appService.isExistRelateAppByNetworkIdThrowEx(networkId);
    }


    private CbbAppRelativeDeskInfo covertViewUamAppDiskVersionRelativeDesktopEntityToCbbAppRelativeDeskInfo(
            ViewUamAppDiskVersionRelativeDesktopEntity item) {
        CbbAppRelativeDeskInfo appRelativeDeskInfo = new CbbAppRelativeDeskInfo();
        appRelativeDeskInfo.setUserName(item.getUserName());
        appRelativeDeskInfo.setDeskType(item.getDeskType());
        appRelativeDeskInfo.setDeskIp(item.getDeskIp());
        appRelativeDeskInfo.setDeskMac(item.getDeskMac());
        appRelativeDeskInfo.setAppId(item.getAppId());
        appRelativeDeskInfo.setAppVersionId(item.getAppVersionId());
        appRelativeDeskInfo.setAppVersionState(item.getAppVersionState());
        appRelativeDeskInfo.setDesktopId(item.getDeskId());
        appRelativeDeskInfo.setDesktopName(item.getName());
        appRelativeDeskInfo.setDeskState(item.getDeskState());
        appRelativeDeskInfo.setIsAllAppDetach(false);
        appRelativeDeskInfo.setAppSoftwarePackageType(item.getAppSoftwarePackageType());
        return appRelativeDeskInfo;
    }
}
