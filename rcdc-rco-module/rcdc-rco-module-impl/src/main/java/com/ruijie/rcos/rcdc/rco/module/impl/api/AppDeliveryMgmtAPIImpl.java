package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.PushAppConfigDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbDeleteDeliveryObjectDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DataSourceTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.*;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppTestDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamDeliveryObjectDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.*;
import com.ruijie.rcos.rcdc.rco.module.impl.service.*;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.kernel.request.PageQueryConstant;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_RCO_UAM_DELETE_DELIVERY_DESKTOP;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/03 18:32
 *
 * @author coderLee23
 */
public class AppDeliveryMgmtAPIImpl implements AppDeliveryMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDeliveryMgmtAPIImpl.class);

    @Autowired
    private ViewUamDeliveryGroupService viewUamDeliveryGroupService;

    @Autowired
    private ViewUamDeliveryAppService viewUamDeliveryAppService;

    @Autowired
    private ViewUamDeliveryObjectService viewUamDeliveryObjectService;

    @Autowired
    private ViewUamDeliveryDetailService viewUamDeliveryDetailService;

    @Autowired
    private ViewPushInstallPackageService viewPushInstallPackageService;

    @Autowired
    private ViewAppDiskService viewAppDiskService;

    @Autowired
    private ViewTerminalGroupDesktopRelatedService viewTerminalGroupDesktopRelatedService;

    @Autowired
    private ViewUserGroupDesktopRelatedService viewUserGroupDesktopRelatedService;

    @Autowired
    private ViewUamDeliveryGroupObjectSpecService viewUamDeliveryGroupObjectSpecService;

    @Autowired
    private ViewUamDeliveryObjectDAO viewUamDeliveryObjectDAO;

    @Autowired
    private ViewUamAppTestDesktopDAO viewUamAppTestDesktopDAO;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;


    @Override
    public DefaultPageResponse<UamDeliveryGroupDTO> pageUamDeliveryGroup(SearchDeliveryGroupDTO searchDeliveryGroupDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryGroupDTO, "cbbSearchDeliveryGroupDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUamDeliveryGroupEntity> uamDeliveryGroupPage = viewUamDeliveryGroupService.pageUamDeliveryGroup(searchDeliveryGroupDTO, pageable);
        return getUamDeliveryGroupPage(uamDeliveryGroupPage);
    }

    @Override
    public DefaultPageResponse<UamDeliveryGroupDTO> pageUamDeliveryGroup(HomePageSearchDeliveryGroupDTO homePageSearchDeliveryGroupDTO,
            Pageable pageable) {
        Assert.notNull(homePageSearchDeliveryGroupDTO, "homePageSearchDeliveryGroupDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUamDeliveryGroupEntity> uamDeliveryGroupPage =
                viewUamDeliveryGroupService.pageUamDeliveryGroup(homePageSearchDeliveryGroupDTO, pageable);
        return getUamDeliveryGroupPage(uamDeliveryGroupPage);
    }

    @Override
    public DefaultPageResponse<UamDeliveryAppDTO> pageUamDeliveryApp(SearchDeliveryAppDTO searchDeliveryAppDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryAppDTO, "cbbSearchDeliveryAppDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUamDeliveryAppEntity> viewUamDeliveryAppPage = viewUamDeliveryAppService.pageUamDeliveryApp(searchDeliveryAppDTO, pageable);
        return getUamDeliveryAppPage(viewUamDeliveryAppPage);
    }

    @Override
    public DefaultPageResponse<UamAppDiskDTO> pageAppDisk(SearchAppDiskDTO searchAppDiskDTO, Pageable pageable) {
        Assert.notNull(searchAppDiskDTO, "searchUamAppDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewAppDiskEntity> viewAppDiskEntityPage = viewAppDiskService.pageAppDisk(searchAppDiskDTO, pageable);

        Page<UamAppDiskDTO> uamAppDiskDTOPage = viewAppDiskEntityPage.map(entity -> {
            UamAppDiskDTO uamAppDiskDTO = new UamAppDiskDTO();
            BeanUtils.copyProperties(entity, uamAppDiskDTO);
            uamAppDiskDTO.setEnableGlobalImage(isEnableGlobalImage(entity));
            return uamAppDiskDTO;
        });

        DefaultPageResponse<UamAppDiskDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(uamAppDiskDTOPage.stream().toArray(UamAppDiskDTO[]::new));
        defaultPageResponse.setTotal(uamAppDiskDTOPage.getTotalElements());
        return defaultPageResponse;
    }

    private Boolean isEnableGlobalImage(ViewAppDiskEntity entity) {
        try {
            // todo 目前够用，未来对接其他云平台考虑HCI封装接口
            PageQueryResponse<CloudPlatformDTO> pageQueryResponse =
                    cloudPlatformManageAPI.pageQuery(pageQueryBuilderFactory.newRequestBuilder()
                            .setPageLimit(PageQueryConstant.DEFAULT_PAGE, PageQueryConstant.MAX_LIMIT).build());
            return pageQueryResponse.getTotal() > 1 && entity.getIsGlobalImage();
        } catch (BusinessException e) {
            LOGGER.error("获取云平台列表异常" , e);
            return false;
        }
    }

    @Override
    public DefaultPageResponse<UamPushInstallPackageDTO> pagePushInstallPackage(SearchPushInstallPackageDTO searchPushInstallPackageDTO,
            Pageable pageable) {
        Assert.notNull(searchPushInstallPackageDTO, "searchPushInstallPackageDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewPushInstallPackageEntity> viewPushInstallPackageEntityPage =
                viewPushInstallPackageService.pagePushInstallPackage(searchPushInstallPackageDTO, pageable);

        Page<UamPushInstallPackageDTO> uamPushInstallPackageDTOPage = viewPushInstallPackageEntityPage.map(entity -> {
            UamPushInstallPackageDTO uamPushInstallPackageDTO = new UamPushInstallPackageDTO();
            BeanUtils.copyProperties(entity, uamPushInstallPackageDTO);
            return uamPushInstallPackageDTO;
        });
        DefaultPageResponse<UamPushInstallPackageDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(uamPushInstallPackageDTOPage.stream().toArray(UamPushInstallPackageDTO[]::new));
        defaultPageResponse.setTotal(uamPushInstallPackageDTOPage.getTotalElements());
        return defaultPageResponse;
    }

    @Override
    public DefaultPageResponse<UamDeliveryAppDetailDTO> pageUamDeliveryAppDetail(SearchDeliveryAppDetailDTO searchDeliveryAppDetailDTO,
            Pageable pageable) {
        Assert.notNull(searchDeliveryAppDetailDTO, "cbbSearchDeliveryAppDetailDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUamDeliveryDetailEntity> viewUamDeliveryDetailPage =
                viewUamDeliveryDetailService.pageUamDeliveryAppDetail(searchDeliveryAppDetailDTO, pageable);
        return getUamDeliveryAppDetailPage(viewUamDeliveryDetailPage);
    }


    @Override
    public DefaultPageResponse<UamDeliveryObjectDTO> pageUamDeliveryObject(SearchDeliveryObjectDTO searchDeliveryObjectDTO, Pageable pageable) {
        Assert.notNull(searchDeliveryObjectDTO, "cbbSearchDeliveryObjectDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUamDeliveryObjectEntity> viewUamDeliveryObjectPage =
                viewUamDeliveryObjectService.pageUamDeliveryObject(searchDeliveryObjectDTO, pageable);
        return getUamDeliveryObjectPage(viewUamDeliveryObjectPage);
    }


    @Override
    public DefaultPageResponse<UamDeliveryObjectDetailDTO> pageUamDeliveryObjectDetail(SearchDeliveryObjectDetailDTO searchDeliveryObjectDetailDTO,
            Pageable pageable) {
        Assert.notNull(searchDeliveryObjectDetailDTO, "cbbSearchDeliveryObjectDetailDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewUamDeliveryDetailEntity> viewUamDeliveryDetailPage =
                viewUamDeliveryDetailService.pageUamDeliveryObjectDetail(searchDeliveryObjectDetailDTO, pageable);
        return getUamDeliveryObjectDetailPage(viewUamDeliveryDetailPage);
    }

    @Override
    public UamDeliveryObjectDetailDTO getDeliveryObjectDetail(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        ViewUamDeliveryDetailEntity viewUamDeliveryDetailEntity = viewUamDeliveryDetailService.getById(id);
        UamDeliveryObjectDetailDTO uamDeliveryObjectDetailDTO = new UamDeliveryObjectDetailDTO();
        BeanUtils.copyProperties(viewUamDeliveryDetailEntity, uamDeliveryObjectDetailDTO);
        return uamDeliveryObjectDetailDTO;
    }

    @Override
    public DefaultPageResponse<TerminalGroupDesktopRelatedDTO> pageTerminalGroupDesktopRelated(
            SearchGroupDesktopRelatedDTO searchTerminalGroupDesktopRelatedDTO, Pageable pageable) {
        Assert.notNull(searchTerminalGroupDesktopRelatedDTO, "searchTerminalGroupDesktopRelatedDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewTerminalGroupDesktopRelatedEntity> viewTerminalGroupDesktopRelatedList =
                viewTerminalGroupDesktopRelatedService.pageTerminalGroupDesktopRelated(searchTerminalGroupDesktopRelatedDTO, pageable);

        Page<TerminalGroupDesktopRelatedDTO> page = viewTerminalGroupDesktopRelatedList.map(viewTerminalGroupDesktopRelatedEntity -> {
            TerminalGroupDesktopRelatedDTO terminalGroupDesktopRelatedDTO = new TerminalGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(viewTerminalGroupDesktopRelatedEntity, terminalGroupDesktopRelatedDTO);

            // 根据云桌面id查询云桌面类型、会话类型
            convertDeskType(viewTerminalGroupDesktopRelatedEntity, terminalGroupDesktopRelatedDTO);

            AppDeliveryTypeEnum appDeliveryType = searchTerminalGroupDesktopRelatedDTO.getAppDeliveryType();
            DataSourceTypeEnum dataSourceType = searchTerminalGroupDesktopRelatedDTO.getDataSourceType();

            // 优先提示版本号问题
            String osVersion = searchTerminalGroupDesktopRelatedDTO.getOsVersion();
            String desktopOsVersion = viewTerminalGroupDesktopRelatedEntity.getOsVersion();
            if (isSameOsVersion(appDeliveryType, desktopOsVersion, osVersion)) {
                String tip = StringUtils.hasText(desktopOsVersion) ? BusinessKey.RCDC_RCO_UAM_CLOUD_DESKTOP_OS_VERSION_INCONSISTENT
                        : BusinessKey.RCDC_RCO_UAM_CLOUD_DESKTOP_OS_VERSION_NOT_EXISTS;
                terminalGroupDesktopRelatedDTO.setNotOptionalTip(LocaleI18nResolver.resolve(tip));
                return terminalGroupDesktopRelatedDTO;
            }

            // 只有 应用磁盘交付组 才需要提示该云桌面被其他交付组使用【不可选状态】，且增加提示
            UUID cloudDesktopId = viewTerminalGroupDesktopRelatedEntity.getCloudDesktopId();
            if (appDeliveryType == AppDeliveryTypeEnum.APP_DISK && dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {

                List<ViewUamDeliveryObjectEntity> objectList =
                        viewUamDeliveryObjectDAO.findByCloudDesktopIdAndAppDeliveryType(cloudDesktopId, AppDeliveryTypeEnum.APP_DISK);
                if (!CollectionUtils.isEmpty(objectList)) {
                    // 应用磁盘只会存在一条记录
                    ViewUamDeliveryObjectEntity viewUamDeliveryObjectEntity = objectList.get(0);
                    String tip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UAM_USED_BY_DELIVERY_GROUP,
                            viewUamDeliveryObjectEntity.getDeliveryGroupName());
                    terminalGroupDesktopRelatedDTO.setNotOptionalTip(tip);
                }
            } else if (dataSourceType == DataSourceTypeEnum.DELIVERY_TEST) {
                terminalGroupDesktopRelatedDTO.setNotOptionalTip(getNotUseMessage(viewTerminalGroupDesktopRelatedEntity.getCloudDesktopId(),
                        viewTerminalGroupDesktopRelatedEntity.getCbbImageType(), viewTerminalGroupDesktopRelatedEntity.getState(),
                        viewTerminalGroupDesktopRelatedEntity.getDesktopName(), viewTerminalGroupDesktopRelatedEntity.getDeskState()));
            }

            return terminalGroupDesktopRelatedDTO;
        });

        DefaultPageResponse<TerminalGroupDesktopRelatedDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(page.stream().toArray(TerminalGroupDesktopRelatedDTO[]::new));
        defaultPageResponse.setTotal(page.getTotalElements());
        return defaultPageResponse;
    }

    @Override
    public List<TerminalGroupDesktopCountDTO> listTerminalGroupDesktopCount(GetGroupDesktopCountDTO getGroupDesktopCountDTO) {
        Assert.notNull(getGroupDesktopCountDTO, "getGroupDesktopCountDTO must not be null");
        return viewTerminalGroupDesktopRelatedService.listTerminalGroupDesktopCount(getGroupDesktopCountDTO);
    }

    @Override
    public List<TerminalGroupDesktopRelatedDTO> listTerminalGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO) {
        Assert.notNull(searchGroupDesktopRelatedDTO, "searchGroupDesktopRelatedDTO must not be null");
        List<ViewTerminalGroupDesktopRelatedEntity> viewTerminalGroupDesktopRelatedList =
                viewTerminalGroupDesktopRelatedService.listTerminalGroupDesktopRelated(searchGroupDesktopRelatedDTO);

        return viewTerminalGroupDesktopRelatedList.stream().map(entity -> {
            TerminalGroupDesktopRelatedDTO terminalGroupDesktopRelatedDTO = new TerminalGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(entity, terminalGroupDesktopRelatedDTO);
            return terminalGroupDesktopRelatedDTO;
        }).collect(Collectors.toList());
    }


    @Override
    public DefaultPageResponse<UserGroupDesktopRelatedDTO> pageUserGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO,
            Pageable pageable) {
        Assert.notNull(searchGroupDesktopRelatedDTO, "searchGroupDesktopRelatedDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");

        Page<ViewUserGroupDesktopRelatedEntity> viewUserGroupDesktopRelatedList =
                viewUserGroupDesktopRelatedService.pageUserGroupDesktopRelated(searchGroupDesktopRelatedDTO, pageable);

        Page<UserGroupDesktopRelatedDTO> page = viewUserGroupDesktopRelatedList.map(viewUserGroupDesktopRelatedEntity -> {
            UserGroupDesktopRelatedDTO userGroupDesktopRelatedDTO = new UserGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(viewUserGroupDesktopRelatedEntity, userGroupDesktopRelatedDTO);

            // 根据云桌面id查询云桌面类型、会话类型
            convertDeskType(viewUserGroupDesktopRelatedEntity, userGroupDesktopRelatedDTO);

            AppDeliveryTypeEnum appDeliveryType = searchGroupDesktopRelatedDTO.getAppDeliveryType();
            DataSourceTypeEnum dataSourceType = searchGroupDesktopRelatedDTO.getDataSourceType();

            // 优先提示版本号问题
            String desktopOsVersion = viewUserGroupDesktopRelatedEntity.getOsVersion();
            String osVersion = searchGroupDesktopRelatedDTO.getOsVersion();
            if (isSameOsVersion(appDeliveryType, desktopOsVersion, osVersion)) {
                String tip = StringUtils.hasText(desktopOsVersion) ? BusinessKey.RCDC_RCO_UAM_CLOUD_DESKTOP_OS_VERSION_INCONSISTENT
                        : BusinessKey.RCDC_RCO_UAM_CLOUD_DESKTOP_OS_VERSION_NOT_EXISTS;
                userGroupDesktopRelatedDTO.setNotOptionalTip(LocaleI18nResolver.resolve(tip));
                return userGroupDesktopRelatedDTO;
            }

            // 只有 应用磁盘交付组 才需要提示该云桌面被其他交付组使用【不可选状态】，且增加提示
            UUID cloudDesktopId = viewUserGroupDesktopRelatedEntity.getCloudDesktopId();
            if (appDeliveryType == AppDeliveryTypeEnum.APP_DISK && dataSourceType == DataSourceTypeEnum.DELIVERY_GROUP) {
                List<ViewUamDeliveryObjectEntity> objectList =
                        viewUamDeliveryObjectDAO.findByCloudDesktopIdAndAppDeliveryType(cloudDesktopId, AppDeliveryTypeEnum.APP_DISK);
                if (!CollectionUtils.isEmpty(objectList)) {
                    // 应用磁盘只会存在一条记录
                    ViewUamDeliveryObjectEntity viewUamDeliveryObjectEntity = objectList.get(0);
                    String tip = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UAM_USED_BY_DELIVERY_GROUP,
                            viewUamDeliveryObjectEntity.getDeliveryGroupName());
                    userGroupDesktopRelatedDTO.setNotOptionalTip(tip);
                }
            } else if (dataSourceType == DataSourceTypeEnum.DELIVERY_TEST) {
                userGroupDesktopRelatedDTO.setNotOptionalTip(getNotUseMessage(viewUserGroupDesktopRelatedEntity.getCloudDesktopId(),
                        viewUserGroupDesktopRelatedEntity.getCbbImageType(), viewUserGroupDesktopRelatedEntity.getState(),
                        viewUserGroupDesktopRelatedEntity.getDesktopName(), viewUserGroupDesktopRelatedEntity.getDeskState()));
            }
            return userGroupDesktopRelatedDTO;
        });

        DefaultPageResponse<UserGroupDesktopRelatedDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(page.stream().toArray(UserGroupDesktopRelatedDTO[]::new));
        defaultPageResponse.setTotal(page.getTotalElements());
        return defaultPageResponse;
    }

    private void convertDeskType(ViewTerminalGroupDesktopRelatedEntity entity,
                                 TerminalGroupDesktopRelatedDTO terminalGroupDesktopRelatedDTO) {
        UUID desktopId = terminalGroupDesktopRelatedDTO.getCloudDesktopId();
        CloudDesktopDetailDTO desktopDetail;
        try {
            desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
            terminalGroupDesktopRelatedDTO.setSessionType(desktopDetail.getSessionType());
            terminalGroupDesktopRelatedDTO.setDeskType(desktopDetail.getStrategyType());
        } catch (BusinessException e) {
            LOGGER.error("获取桌面信息失败，桌面id为[{}]", desktopId, e);
            terminalGroupDesktopRelatedDTO.setDeskType(entity.getCbbImageType().name());
        }
    }

    private void convertDeskType(ViewUserGroupDesktopRelatedEntity viewUserGroupDesktopRelatedEntity,
                                 UserGroupDesktopRelatedDTO userGroupDesktopRelatedDTO) {
        UUID desktopId = userGroupDesktopRelatedDTO.getCloudDesktopId();
        CloudDesktopDetailDTO desktopDetail;
        try {
            desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
            userGroupDesktopRelatedDTO.setSessionType(desktopDetail.getSessionType());
            userGroupDesktopRelatedDTO.setDeskType(desktopDetail.getStrategyType());
        } catch (BusinessException e) {
            LOGGER.error("获取桌面信息失败，桌面id为[{}]", desktopId, e);
            userGroupDesktopRelatedDTO.setDeskType(viewUserGroupDesktopRelatedEntity.getCbbImageType().name());
        }
    }

    private static boolean isSameOsVersion(AppDeliveryTypeEnum appDeliveryType, String desktopOsVersion, String osVersion) {
        return appDeliveryType != AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE
                && (!StringUtils.hasText(desktopOsVersion) || !Objects.equals(osVersion, desktopOsVersion));
    }

    private String getNotUseMessage(UUID desktopId, CbbImageType cbbImageType, CbbTerminalStateEnums terminalState, String desktopName,
            CbbCloudDeskState cbbCloudDeskState) {
        if (cbbImageType == CbbImageType.VDI) {
            if (cbbCloudDeskState != CbbCloudDeskState.RUNNING) {
                return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UAM_TEST_GROUP_CLOUD_DESKTOP_NOT_RUNNING, desktopName);
            }
        } else {
            if (terminalState != CbbTerminalStateEnums.ONLINE) {
                return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UAM_TEST_GROUP_TERMINAL_NO_ONLINE, desktopName);
            }
        }
        // 当前是否有被其它测试任务使用
        List<ViewUamAppTestDesktopEntity> uamAppTestDesktopEntityList =
                viewUamAppTestDesktopDAO.findByDeskIdAndTestStateIn(desktopId, DesktopTestStateEnum.getProcessingStateList());
        if (CollectionUtils.isNotEmpty(uamAppTestDesktopEntityList)) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UAM_USED_BY_TEST_GROUP, uamAppTestDesktopEntityList.get(0).getTestName());
        }
        return "";
    }


    @Override
    public List<UserGroupDesktopCountDTO> listUserGroupDesktopCount(GetGroupDesktopCountDTO getGroupDesktopCountDTO) {
        Assert.notNull(getGroupDesktopCountDTO, "getGroupDesktopCountDTO must not be null");
        return viewUserGroupDesktopRelatedService.listUserGroupDesktopCount(getGroupDesktopCountDTO);
    }


    @Override
    public List<UserGroupDesktopRelatedDTO> listUserGroupDesktopRelated(SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO) {
        Assert.notNull(searchGroupDesktopRelatedDTO, "searchGroupDesktopRelatedDTO must not be null");
        List<ViewUserGroupDesktopRelatedEntity> viewUserGroupDesktopRelatedList =
                viewUserGroupDesktopRelatedService.listUserGroupDesktopRelated(searchGroupDesktopRelatedDTO);

        return viewUserGroupDesktopRelatedList.stream().map(entity -> {
            UserGroupDesktopRelatedDTO userGroupDesktopRelatedDTO = new UserGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(entity, userGroupDesktopRelatedDTO);
            return userGroupDesktopRelatedDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public UamDeliveryAppDTO findDeliveryAppById(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        ViewUamDeliveryAppEntity viewUamDeliveryAppEntity = viewUamDeliveryAppService.findById(id);
        return convert(viewUamDeliveryAppEntity);
    }

    @Override
    public List<UamDeliveryAppDTO> findDeliveryAppListByGroupId(UUID deliveryGroupId) throws BusinessException {
        Assert.notNull(deliveryGroupId, "deliveryGroupId can not be null");
        List<ViewUamDeliveryAppEntity> appEntityList = viewUamDeliveryAppService.findAppListByGroupId(deliveryGroupId);
        return appEntityList.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<String> findAppNameListByGroupId(List<UUID> groupIdList) throws BusinessException {
        Assert.notEmpty(groupIdList, "groupIdList must not be null");
        return viewUamDeliveryAppService.findAppNameListByGroupId(groupIdList);
    }

    private UamDeliveryAppDTO convert(ViewUamDeliveryAppEntity entity) {
        UamDeliveryAppDTO uamDeliveryAppDTO = new UamDeliveryAppDTO();
        BeanUtils.copyProperties(entity, uamDeliveryAppDTO);
        return uamDeliveryAppDTO;
    }

    @Override
    public UamDeliveryObjectDTO findDeliveryObjectById(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        ViewUamDeliveryObjectEntity viewUamDeliveryObjectEntity = viewUamDeliveryObjectService.findById(id);
        UamDeliveryObjectDTO uamDeliveryObjectDTO = new UamDeliveryObjectDTO();
        BeanUtils.copyProperties(viewUamDeliveryObjectEntity, uamDeliveryObjectDTO);
        return uamDeliveryObjectDTO;
    }

    @Override
    public List<UamDeliveryGroupObjectSpecDTO> listByDeliveryGroupId(UUID deliveryGroupId) {
        Assert.notNull(deliveryGroupId, "deliveryGroupId must not be null");
        List<ViewUamDeliveryGroupObjectSpecEntity> viewUamDeliveryGroupObjectSpecList =
                viewUamDeliveryGroupObjectSpecService.listByDeliveryGroupId(deliveryGroupId);

        return viewUamDeliveryGroupObjectSpecList.stream().map(entity -> {
            UamDeliveryGroupObjectSpecDTO uamDeliveryGroupObjectSpecDTO = new UamDeliveryGroupObjectSpecDTO();
            BeanUtils.copyProperties(entity, uamDeliveryGroupObjectSpecDTO);
            return uamDeliveryGroupObjectSpecDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UamDeliveryObjectDTO> findByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");

        List<ViewUamDeliveryObjectEntity> objectEntityList = viewUamDeliveryObjectService.findByDeskId(deskId);
        return objectEntityList.stream().map(objectEntity -> {
            UamDeliveryObjectDTO dto = new UamDeliveryObjectDTO();
            BeanUtils.copyProperties(objectEntity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Boolean existsByAppDeliveryTypeAndCloudDesktopIdIn(AppDeliveryTypeEnum appDeliveryType, List<UUID> cloudDesktopIdList) {
        Assert.notNull(appDeliveryType, "deskId must not be null");
        Assert.notEmpty(cloudDesktopIdList, "deskId must not be null");
        return viewUamDeliveryObjectService.existsByAppDeliveryTypeAndCloudDesktopIdIn(appDeliveryType, cloudDesktopIdList);
    }

    @Override
    public void deleteDeliveryObjectWhenStrategyModify(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId can not be null");

        LOGGER.info("删除桌面[{}]绑定的交付组对象", desktopId);
        List<UamDeliveryObjectDTO> objectDTOList = findByDeskId(desktopId);
        for (UamDeliveryObjectDTO uamDeliveryObjectDTO : objectDTOList) {
            try {
                CbbDeleteDeliveryObjectDTO deleteRequest = new CbbDeleteDeliveryObjectDTO();
                deleteRequest.setId(uamDeliveryObjectDTO.getId());
                deleteRequest.setCloudDesktopId(desktopId);
                deleteRequest.setDeliveryGroupId(uamDeliveryObjectDTO.getDeliveryGroupId());
                deleteRequest.setDeskState(CbbCloudDeskState.RUNNING);
                cbbAppDeliveryMgmtAPI.deleteUamDeliveryObject(deleteRequest);
                recordSystemLog(LocaleI18nResolver.resolve(RCDC_RCO_UAM_DELETE_DELIVERY_DESKTOP, uamDeliveryObjectDTO.getCloudDesktopName(),
                        uamDeliveryObjectDTO.getDeliveryGroupName()));
            } catch (Exception e) {
                LOGGER.error("交付组不存在该云桌面{}，或该云桌面不在交付对象中忽略异常", desktopId);
            }
        }
    }

    @Override
    public UamDeliveryGroupDTO getUamDeliveryGroup(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        ViewUamDeliveryGroupEntity viewUamDeliveryGroupEntity = viewUamDeliveryGroupService.findById(id);
        UamDeliveryGroupDTO uamDeliveryGroupDTO = new UamDeliveryGroupDTO();
        BeanUtils.copyProperties(viewUamDeliveryGroupEntity, uamDeliveryGroupDTO);
        String pushAppConfig = viewUamDeliveryGroupEntity.getPushAppConfig();
        PushAppConfigDTO pushAppConfigDTO = JSON.parseObject(pushAppConfig, PushAppConfigDTO.class);
        uamDeliveryGroupDTO.setPushAppConfig(pushAppConfigDTO);
        return uamDeliveryGroupDTO;
    }

    @Override
    public long countByConditions(ConditionQueryRequest request) {
        Assert.notNull(request, "request can not be null");
        return viewAppDiskService.countByConditions(request);
    }

    private void recordSystemLog(String content) {
        BaseSystemLogDTO logDTO = new BaseSystemLogDTO();
        logDTO.setId(UUID.randomUUID());
        logDTO.setContent(content);
        logDTO.setCreateTime(new Date());
        baseSystemLogMgmtAPI.createSystemLog(logDTO);
    }


    private DefaultPageResponse<UamDeliveryGroupDTO> getUamDeliveryGroupPage(Page<ViewUamDeliveryGroupEntity> uamDeliveryGroupPage) {
        Page<UamDeliveryGroupDTO> cbbUamDeliveryGroupPage = uamDeliveryGroupPage.map(entity -> {
            UamDeliveryGroupDTO uamDeliveryGroupDTO = new UamDeliveryGroupDTO();
            BeanUtils.copyProperties(entity, uamDeliveryGroupDTO);
            return uamDeliveryGroupDTO;
        });
        DefaultPageResponse<UamDeliveryGroupDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(cbbUamDeliveryGroupPage.stream().toArray(UamDeliveryGroupDTO[]::new));
        defaultPageResponse.setTotal(cbbUamDeliveryGroupPage.getTotalElements());
        return defaultPageResponse;
    }


    private DefaultPageResponse<UamDeliveryAppDTO> getUamDeliveryAppPage(Page<ViewUamDeliveryAppEntity> viewUamDeliveryAppEntityPage) {
        Page<UamDeliveryAppDTO> uamDeliveryAppPage = viewUamDeliveryAppEntityPage.map(entity -> {
            UamDeliveryAppDTO uamDeliveryAppDTO = new UamDeliveryAppDTO();
            BeanUtils.copyProperties(entity, uamDeliveryAppDTO);
            return uamDeliveryAppDTO;
        });
        DefaultPageResponse<UamDeliveryAppDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(uamDeliveryAppPage.stream().toArray(UamDeliveryAppDTO[]::new));
        defaultPageResponse.setTotal(uamDeliveryAppPage.getTotalElements());
        return defaultPageResponse;
    }


    private DefaultPageResponse<UamDeliveryObjectDTO> getUamDeliveryObjectPage(Page<ViewUamDeliveryObjectEntity> viewUamDeliveryObjectEntityPage) {
        Page<UamDeliveryObjectDTO> cbbUamDeliveryObjectPage = viewUamDeliveryObjectEntityPage.map(entity -> {
            UamDeliveryObjectDTO uamDeliveryObjectDTO = new UamDeliveryObjectDTO();
            BeanUtils.copyProperties(entity, uamDeliveryObjectDTO);
            return uamDeliveryObjectDTO;
        });
        DefaultPageResponse<UamDeliveryObjectDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(cbbUamDeliveryObjectPage.stream().toArray(UamDeliveryObjectDTO[]::new));
        defaultPageResponse.setTotal(cbbUamDeliveryObjectPage.getTotalElements());
        return defaultPageResponse;
    }

    private DefaultPageResponse<UamDeliveryAppDetailDTO> getUamDeliveryAppDetailPage(Page<ViewUamDeliveryDetailEntity> viewUamDeliveryDetailPage) {
        Page<UamDeliveryAppDetailDTO> cbbUamDeliveryAppDetailPage = viewUamDeliveryDetailPage.map(entity -> {
            UamDeliveryAppDetailDTO cbUamDeliveryAppDetailDTO = new UamDeliveryAppDetailDTO();
            BeanUtils.copyProperties(entity, cbUamDeliveryAppDetailDTO);
            return cbUamDeliveryAppDetailDTO;
        });
        DefaultPageResponse<UamDeliveryAppDetailDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(cbbUamDeliveryAppDetailPage.stream().toArray(UamDeliveryAppDetailDTO[]::new));
        defaultPageResponse.setTotal(cbbUamDeliveryAppDetailPage.getTotalElements());
        return defaultPageResponse;
    }


    private DefaultPageResponse<UamDeliveryObjectDetailDTO> getUamDeliveryObjectDetailPage(
            Page<ViewUamDeliveryDetailEntity> viewUamDeliveryDetailPage) {
        Page<UamDeliveryObjectDetailDTO> cbbUamDeliveryObjectDetailPage = viewUamDeliveryDetailPage.map(entity -> {
            UamDeliveryObjectDetailDTO uamDeliveryObjectDetailDTO = new UamDeliveryObjectDetailDTO();
            BeanUtils.copyProperties(entity, uamDeliveryObjectDetailDTO);
            uamDeliveryObjectDetailDTO.setInstallTime(entity.getDeliveryTime());
            return uamDeliveryObjectDetailDTO;
        });
        DefaultPageResponse<UamDeliveryObjectDetailDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(cbbUamDeliveryObjectDetailPage.stream().toArray(UamDeliveryObjectDetailDTO[]::new));
        defaultPageResponse.setTotal(cbbUamDeliveryObjectDetailPage.getTotalElements());
        return defaultPageResponse;
    }

}
