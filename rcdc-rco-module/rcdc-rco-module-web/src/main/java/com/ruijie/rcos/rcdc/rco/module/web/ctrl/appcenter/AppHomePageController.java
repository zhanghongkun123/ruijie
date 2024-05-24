package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryRecordDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RequestSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.SearchWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response.HomePageSearchResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/18 11:36
 *
 * @author coderLee23
 */
@Api(tags = "应用中心-首页")
@Controller
@RequestMapping("/rco/appCenter/homePage")
public class AppHomePageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppHomePageController.class);

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    /**
     * 首页搜索框
     * 
     * @param searchWebRequest 请求参数
     * @param sessionContext 上下文
     * @throws BusinessException 业务异常
     * @return CommonWebResponse<HomePageSearchResponse>
     */
    @ApiOperation("首页搜索框")
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public CommonWebResponse<HomePageSearchResponse> search(SearchWebRequest searchWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(searchWebRequest, "searchWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        String searchContent = searchWebRequest.getSearchContent();
        // 应用磁盘
        DefaultPageResponse<UamAppDiskDTO> appDiskResponse = getAppDisk(searchContent, sessionContext);
        // 推送安装包
        DefaultPageResponse<UamPushInstallPackageDTO> pushInstallPackageResponse = getPushInstallPackage(searchContent, sessionContext);
        // 推送安装包交付组
        DefaultPageResponse<UamDeliveryGroupDTO> pushDeliveryGroupResponse =
                getUamDeliveryGroup(AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE, searchContent, sessionContext);

        DefaultPageResponse<UamDeliveryGroupDTO> diskDeliveryGroupResponse =
                getUamDeliveryGroup(AppDeliveryTypeEnum.APP_DISK, searchContent, sessionContext);

        HomePageSearchResponse homePageSearchResponse = new HomePageSearchResponse();
        homePageSearchResponse.setAppDiskPage(appDiskResponse);
        homePageSearchResponse.setPushInstallPackagePage(pushInstallPackageResponse);
        homePageSearchResponse.setPushDeliveryGroupPage(pushDeliveryGroupResponse);
        homePageSearchResponse.setDiskDeliveryGroupPage(diskDeliveryGroupResponse);

        return CommonWebResponse.success(homePageSearchResponse);
    }


    /**
     * 获取10条最新的交付动态
     *
     * @param sessionContext 上下文
     * @throws BusinessException 业务异常
     * @return CommonWebResponse<HomePageSearchResponse>
     */
    @ApiOperation("获取10条最新的交付动态")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<List<CbbUamDeliveryRecordDTO>> getTopNewUamDeliveryGroup(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID adminId = generalPermissionHelper.isAllPermission(sessionContext) ? null : generalPermissionHelper.getAdminId(sessionContext);
        List<CbbUamDeliveryRecordDTO> topNewUamDeliveryRecordList = cbbAppDeliveryMgmtAPI.getTopNewUamDeliveryRecord(adminId);
        if (!CollectionUtils.isEmpty(topNewUamDeliveryRecordList)) {
            cloudDesktopWebService.convertDeskType(topNewUamDeliveryRecordList);
        }

        return CommonWebResponse.success(topNewUamDeliveryRecordList);
    }

    private DefaultPageResponse<UamDeliveryGroupDTO> getUamDeliveryGroup(AppDeliveryTypeEnum appDeliveryType, String searchContent,
            SessionContext sessionContext) throws BusinessException {
        Pageable deliveryGroupNamePageable = getPageable("deliveryGroupName");
        HomePageSearchDeliveryGroupDTO homePageSearchDeliveryGroupDTO = new HomePageSearchDeliveryGroupDTO();
        homePageSearchDeliveryGroupDTO.setDeliveryGroupName(searchContent);
        homePageSearchDeliveryGroupDTO.setAppDeliveryType(appDeliveryType);
        generalPermissionHelper.setPermissionParam(sessionContext, homePageSearchDeliveryGroupDTO);
        return appDeliveryMgmtAPI.pageUamDeliveryGroup(homePageSearchDeliveryGroupDTO, deliveryGroupNamePageable);
    }

    private DefaultPageResponse<UamAppDiskDTO> getAppDisk(String searchContent, SessionContext sessionContext) throws BusinessException {
        Pageable appNamePageable = getPageable(DBConstants.APP_NAME);
        SearchAppDiskDTO searchAppDiskDTO = new SearchAppDiskDTO();
        searchAppDiskDTO.setAppName(searchContent);
        searchAppDiskDTO.setRequestSource(RequestSourceEnum.APP_HOME_PAGE);
        generalPermissionHelper.setPermissionParam(sessionContext, searchAppDiskDTO);
        return appDeliveryMgmtAPI.pageAppDisk(searchAppDiskDTO, appNamePageable);
    }

    private DefaultPageResponse<UamPushInstallPackageDTO> getPushInstallPackage(String searchContent, SessionContext sessionContext)
            throws BusinessException {
        Pageable appNamePageable = getPageable(DBConstants.APP_NAME);
        SearchPushInstallPackageDTO searchPushInstallPackageDTO = new SearchPushInstallPackageDTO();
        searchPushInstallPackageDTO.setAppName(searchContent);
        generalPermissionHelper.setPermissionParam(sessionContext, searchPushInstallPackageDTO);
        return appDeliveryMgmtAPI.pagePushInstallPackage(searchPushInstallPackageDTO, appNamePageable);
    }

    private static Pageable getPageable(String sortName) {
        Sort sort = Sort.by(Sort.Direction.ASC, sortName);
        return PageRequest.of(0, 10, sort);
    }

}
