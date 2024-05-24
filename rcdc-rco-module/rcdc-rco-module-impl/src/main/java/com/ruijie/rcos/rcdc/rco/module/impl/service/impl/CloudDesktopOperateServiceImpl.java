package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.DeskDiskExpectDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbThirdPartyDeskOperateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.deskoperate.CbbWakeUpDeskVDIRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbEditDesktopRoleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbStartDeskVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VmState;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaHostSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dao.CloudDeskAppConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.entity.CloudDeskAppConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.CmsUpgradeService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AcpiService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.sql.SqlConstants;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSourceNames;
import com.ruijie.rcos.sk.repositorykit.api.ds.JdbcTemplateHolder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/16
 *
 * @author Jarman
 */
@Service
public class CloudDesktopOperateServiceImpl implements CloudDesktopOperateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDesktopOperateServiceImpl.class);

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Autowired
    CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private UserService userService;


    @Autowired
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CloudDeskAppConfigDAO cloudDeskAppConfigDAO;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private AcpiService acpiUtils;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI appSoftPackageMgmtAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterServerMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private CmsUpgradeService cmsUpgradeService;

    @Autowired
    private CbbThirdPartyDeskOperateMgmtAPI cbbThirdPartyDeskOperateAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    private static final int MAX_TRY_COUNT = 3;

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    @Override
    public void startDesktop(CloudDesktopStartRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        UUID desktopId = request.getId();
        Assert.notNull(desktopId, "desktopId cannot null");

        // 获取VDI桌面信息
        CbbDeskDTO deskVDI = cbbVDIDeskMgmtAPI.getDeskVDI(desktopId);
        checkDesktopBeforeStart(deskVDI);

        LOGGER.info("开始判断是否需要添加cms的ISO");
        UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(desktopId);
        if (userDesktopEntity.getDesktopType() != CbbCloudDeskType.THIRD) {
            //第三方桌面不支持挂载
            handleCmIso(desktopId);
        }


        // 传入的对应云桌面状态不需要处理
        if (request.getForbiddenState() != null && deskVDI.getDeskState() == request.getForbiddenState()) {
            LOGGER.info("传入的对应云桌面状态不需要处理，对应状态：{}", request.getForbiddenState());
            return;
        }
        //第三方桌面执行唤醒
        if (userDesktopEntity.getDesktopType() == CbbCloudDeskType.THIRD) {
            LOGGER.info("第三方桌面[{}]执行唤醒开机", desktopId);
            if (request.getTaskId() == null) {
                request.setTaskId(UUID.randomUUID());
            }
            cbbThirdPartyDeskOperateAPI.start(deskVDI, request.getTaskId());
            return;
        }


        // 池桌面可以为空
        IacUserDetailDTO userEntity = null;
        if (null != userDesktopEntity.getUserId()) {
            userEntity = iacUserMgmtAPI.getUserDetail(userDesktopEntity.getUserId());
        }
        if (userEntity == null && request.getUserId() != null) {
            // 用于补偿静态单会话应用池主机，首次启动并分配主机时，还未建立绑定关系，但是需要自动登录的场景, 其他场景userId没有值
            userEntity = iacUserMgmtAPI.getUserDetail(request.getUserId());
        }
        if (deskVDI.getDeskState() == CbbCloudDeskState.SLEEP || request.getVmState() == VmState.SLEEP) {
            CbbWakeUpDeskVDIRequest cbbReq = new CbbWakeUpDeskVDIRequest();
            cbbReq.setId(desktopId);
            cbbReq.setAcpiParams(acpiUtils.genAcpiPara(userEntity, userDesktopEntity, deskVDI, null));
            cbbReq.setNeedExactMatchVgpu(true);
            // OpenAPI改动
            if (request.getTaskId() != null) {
                cbbReq.setTaskId(request.getTaskId());
            }
            cbbReq.setSupportCrossCpuVendor(request.getSupportCrossCpuVendor());
            cbbReq.setVmState(request.getVmState());
            cbbReq.setDeskBackupState(request.getDeskBackupState());
            cbbVDIDeskOperateAPI.wakeUpDeskVDI(cbbReq);
        } else {
            DeskDiskExpectDetailDTO deskDiskExpectDetailDTO = appSoftPackageMgmtAPI.getDeskDiskExpectDetail(desktopId);
            CbbStartDeskVDIDTO cbbReq = new CbbStartDeskVDIDTO();
            cbbReq.setId(desktopId);
            cbbReq.setAcpiParams(acpiUtils.genAcpiPara(userEntity, userDesktopEntity, deskVDI, deskDiskExpectDetailDTO));
            cbbReq.setNeedExactMatchVgpu(true);
            cbbReq.setEnableMountOldData(request.getEnableMountOldData());
            // OpenAPI改动
            if (request.getTaskId() != null) {
                cbbReq.setTaskId(request.getTaskId());
            }

            cbbReq.setDetachDiskList(deskDiskExpectDetailDTO.getShouldDetachDiskList());
            cbbReq.setCreateAndAttachDiskList(deskDiskExpectDetailDTO.getShouldAttachDiskList());
            cbbReq.setVmState(request.getVmState());
            cbbReq.setDeskBackupState(request.getDeskBackupState());
            cbbReq.setBatchTaskItem(request.getBatchTaskItem());
            cbbVDIDeskOperateAPI.startDeskVDI(cbbReq);
        }
    }


    private void checkDesktopBeforeStart(CbbDeskDTO deskVDI) throws BusinessException {
        // 校验云桌面是否处于维护模式
        if (Boolean.TRUE.equals(deskVDI.getIsOpenDeskMaintenance())) {
            LOGGER.error("云桌面[{}]处于维护模式", deskVDI.getName());
            throw new BusinessException(BusinessKey.RCDC_USER_SHINE_START_VM_UNDER_DESKTOP_MAINTENANCE_ERR);
        }

        if (Objects.isNull(deskVDI.getDesktopPoolId()) || !DesktopPoolType.isPoolDesktop(deskVDI.getDesktopPoolType())) {
            return;
        }
        // 池桌面校验一下桌面池状态
        desktopPoolMgmtAPI.checkDesktopPoolStatusAvailable(deskVDI.getDesktopPoolId());
    }

    @Override
    public void resumeDesktop(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId must not be null.");
        CbbCloudDeskState state = queryCloudDesktopService.queryState(desktopId);
        if (state != CbbCloudDeskState.SLEEP) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_RESUME_STATE_ERR,
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + state.name().toLowerCase()));
        }
        CbbWakeUpDeskVDIRequest cbbReq = new CbbWakeUpDeskVDIRequest();
        cbbReq.setId(desktopId);
        cbbReq.setNeedExactMatchVgpu(true);
        cbbVDIDeskOperateAPI.wakeUpDeskVDI(cbbReq);
    }

    @Override
    public void bindDesktopTerminal(UUID desktopId, String terminalId) {
        Assert.notNull(desktopId, "desktopId cannot null");
        Assert.notNull(terminalId, "terminalId cannot null");

        CbbTerminalBasicInfoDTO terminalBasicInfoDTO = null;
        try {
            terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            LOGGER.error("获取终端信息失败：{}", e);
        }
        if (terminalBasicInfoDTO != null) {
            CbbTerminalPlatformEnums platformEnums = terminalBasicInfoDTO.getTerminalPlatform();
            if (CbbTerminalPlatformEnums.APP != platformEnums) {
                LOGGER.info("非软终端模式，需要先解绑: {}", terminalId);
                bindBefore(terminalId);
            }
        }
        CbbDeskDTO cbbDeskDTO;
        try {
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("绑定终端[{}]和桌面[{}]关系时，获取桌面信息异常", terminalId, desktopId, e);
            return;
        }
        if (cbbDeskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            bindMultiDeskUserTerminal(desktopId, terminalId);
            return;
        }

        UserDesktopEntity bindEntity = userDesktopDAO.findByCbbDesktopId(desktopId);
        bindEntity.setTerminalId(terminalId);
        bindEntity.setHasTerminalRunning(true);
        bindEntity.setHasLogin(true);
        bindEntity.setLatestLoginTime(new Date());
        userDesktopDAO.save(bindEntity);
    }

    private void bindBefore(String terminalId) {
        try {
            // 根据终端id解绑
            List<UserDesktopEntity> userDesktopEntityList = userDesktopDAO.findByTerminalId(terminalId);
            userDesktopEntityList.forEach(userDesktopEntity -> unbindTerminal(userDesktopEntity, false));
        } catch (Exception e) {
            LOGGER.error("解绑终端[{}]异常:", terminalId, e);
        }
    }

    private void bindMultiDeskUserTerminal(UUID desktopId, String terminalId) {
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (Objects.isNull(userTerminalEntity) || Objects.isNull(userTerminalEntity.getUserId())) {
            LOGGER.warn("绑定终端[{}]和桌面[{}]关系时，终端未关联用户", terminalId, desktopId);
            return;
        }

        hostUserService.bindTerminalId(userTerminalEntity.getUserId(), desktopId, terminalId);

        UserDesktopEntity bindEntity = userDesktopDAO.findByCbbDesktopId(desktopId);
        bindEntity.setLatestLoginTime(new Date());
        userDesktopDAO.save(bindEntity);
    }

    @Override
    public void recoverDeskFromError(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId cannot null");
        CbbCloudDeskState state = queryCloudDesktopService.queryState(desktopId);
        if (state != CbbCloudDeskState.ERROR) {
            throw new BusinessException(BusinessKey.RCDC_USER_CLOUDDESKTOP_RECOVER_DESK_FROM_ERROR_STATE_ERR,
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_CLOUDDESKTOP_STATE_PRE + state.name().toLowerCase()));
        }
        cbbVDIDeskOperateAPI.failbackDeskVDI(desktopId);
    }


    @Override
    public void unbindDesktopTerminal(UserTerminalEntity userTerminalEntity) {
        Assert.notNull(userTerminalEntity, "userTerminalEntity can not be null");
        Assert.notNull(userTerminalEntity.getId(), "terminal id can not be null");
        getBindDesktopAndUnbind(userTerminalEntity, false);
    }

    @Override
    public void unbindDesktopTerminalForDeleteTerminal(UserTerminalEntity userTerminalEntity) {
        Assert.notNull(userTerminalEntity, "userTerminalEntity can not be null");
        Assert.notNull(userTerminalEntity.getId(), "terminal id can not be null");
        getBindDesktopAndUnbind(userTerminalEntity, true);
    }

    private void getBindDesktopAndUnbind(UserTerminalEntity userTerminalEntity, boolean isForce) {
        // 多会话桌面相关记录
        unbindHostUserTerminal(userTerminalEntity);
        List<UserDesktopEntity> desktopList = userDesktopDAO.findByTerminalId(userTerminalEntity.getTerminalId());
        if (CollectionUtils.isEmpty(desktopList)) {
            LOGGER.warn("不存在与终端[{}]绑定的云桌面", userTerminalEntity.getTerminalId());
            return;
        }
        desktopList.forEach(desktopEntity -> unbindTerminal(desktopEntity, isForce));
    }

    private void unbindTerminal(UserDesktopEntity userDesktopEntity, boolean isForce) {
        String terminalId = userDesktopEntity.getTerminalId();
        // 判断是否需要解除绑定
        if (isNeedUnBindTerminal(userDesktopEntity, isForce)) {
            terminalId = null;
        }
        int tryCount = 0;
        while (tryCount < MAX_TRY_COUNT) {
            try {
                if (tryCount > 0) {
                    // 乐观锁失败重试，需要重新查库
                    userDesktopEntity = userDesktopDAO.findByCbbDesktopId(userDesktopEntity.getCbbDesktopId());
                }
                userDesktopEntity.setTerminalId(terminalId);
                userDesktopEntity.setHasTerminalRunning(false);
                userDesktopDAO.save(userDesktopEntity);
                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                tryCount++;
                LOGGER.error("解绑用户桌面发生乐观锁异常,重试次数：{}：{}", tryCount, JSON.toJSONString(userDesktopEntity));
            }
        }
    }

    private boolean isNeedUnBindTerminal(UserDesktopEntity userDesktopEntity, boolean isForceUnbindTerminal) {
        if (userDesktopEntity.getDesktopType() == CbbCloudDeskType.IDV) {
            LOGGER.info("IDV桌面不解除终端绑定，桌面id[{}]", userDesktopEntity.getCbbDesktopId());
            return false;
        }
        if (isForceUnbindTerminal) {
            LOGGER.info("非IDV桌面，删除终端行为，桌面需要解除终端绑定，桌面id[{}]", userDesktopEntity.getCbbDesktopId());
            return true;
        }
        if (!isVisitor(userDesktopEntity.getUserId())) {
            LOGGER.info("非删除终端行为，非访客用户，用户[{}]的桌面需要解除终端绑定，桌面id[{}]", userDesktopEntity.getUserId(), //
                    userDesktopEntity.getCbbDesktopId());
            return true;
        }
        LOGGER.info("非删除终端行为，访客用户，用户[{}]的桌面不解除终端绑定，桌面id[{}]", userDesktopEntity.getUserId(), userDesktopEntity.getCbbDesktopId());
        return false;
    }

    private boolean isVisitor(UUID userId) {
        IacUserDetailDTO userDetailDTO;
        try {
            userDetailDTO = iacUserMgmtAPI.getUserDetail(userId);
        } catch (BusinessException e) {
            // 用户不存在会抛异常，正常不会有这种情况出现
            LOGGER.error("获取用户详细信息失败：" + userId, e);
            return false;
        }
        return userDetailDTO.getUserType() == IacUserTypeEnum.VISITOR;
    }

    private void unbindHostUserTerminal(UserTerminalEntity userTerminalEntity) {
        hostUserService.clearTerminalIdByTerminalId(userTerminalEntity.getTerminalId());
    }

    @Override
    public void editDesktopRole(UUID desktopId, DesktopRole desktopRole) throws BusinessException {
        Assert.notNull(desktopId, "desktopId cannot null");
        Assert.notNull(desktopRole, "desktopRole cannot null");

        CbbEditDesktopRoleDTO cbbRequest = new CbbEditDesktopRoleDTO();
        cbbRequest.setId(desktopId);
        cbbRequest.setDesktopRole(desktopRole);
        cbbVDIDeskMgmtAPI.editDesktopRole(cbbRequest);
    }

    @Override
    public void startIdvDesktop(CloudDesktopStartRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        UUID desktopId = request.getId();
        Assert.notNull(desktopId, "desktopId cannot null");

        LOGGER.info("接收到启动IDV云桌面:{}请求, 开始判断是否需要添加cms的ISO", desktopId);
        handleCmIso(desktopId);
        UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(desktopId);
        ViewUserDesktopEntity desktopEntity = queryCloudDesktopService.checkDesktopExistInDeskViewById(desktopId);
        CbbCloudDeskState deskState = queryCloudDesktopService.queryState(desktopEntity.getCbbDesktopId());

        // 传入的对应云桌面状态不需要处理
        if (request.getForbiddenState() != null && deskState == request.getForbiddenState()) {
            LOGGER.info("传入的对应云桌面状态不需要处理，对应状态：{}", request.getForbiddenState());
            return;
        }

        ShineLoginResponseDTO userInfoDTO = new ShineLoginResponseDTO();
        if (!IdvTerminalModeEnums.PUBLIC.name().equalsIgnoreCase(desktopEntity.getIdvTerminalModel())) {
            RcoViewUserEntity userEntity = userService.getUserInfoById(userDesktopEntity.getUserId());
            if (userEntity == null) {
                throw new BusinessException(BusinessKey.RCDC_USER_USER_ENTITY_IS_NOT_EXIST, userDesktopEntity.getUserId().toString());
            }
            userInfoDTO.setUserName(userEntity.getUserName());
            userInfoDTO.setPassword(userEntity.getPassword());
        }

        LOGGER.info("通知shine远程启动虚拟机，终端id:{}", userDesktopEntity.getTerminalId());
        try {
            shineMessageHandler.requestContent(userDesktopEntity.getTerminalId(), ShineAction.SHINE_ACTION_REMOTE_START_VM, userInfoDTO);
        } catch (Exception e) {
            LOGGER.error("RCDC远程启动虚拟机异常，终端ID：{ " + userDesktopEntity.getTerminalId() + "}，异常：", e);
        }

    }

    private void handleCmIso(UUID desktopId) throws BusinessException {
        JdbcTemplate jdbcTemplate = jdbcTemplateHolder.loadJdbcTemplate(DataSourceNames.DEFAULT_DATASOURCE_BEAN_NAME);
        CbbOsType cbbOsType = jdbcTemplate.queryForObject(SqlConstants.QUERY_DESKTOP_OS_TYPE_SQL, CbbOsType.class, desktopId);
        // uos、ubuntu、KYLIN_64不支持cms空间暂时
        if (isNotSupportUWSCMS(cbbOsType)) {
            LOGGER.info("当前云桌面类型是uos、ubuntu、KYLIN_64不需要cms、uws云空间");
            return;
        }

        doHandleUwsIso(desktopId);
        doHandleCmsIso(desktopId);
    }

    private boolean isNotSupportUWSCMS(CbbOsType osType) {
        return osType == CbbOsType.UOS_64 || osType == CbbOsType.UBUNTU_64 || osType == CbbOsType.KYLIN_64;
    }

    private void doHandleCmsIso(UUID desktopId) throws BusinessException {
        CloudDeskAppConfigEntity cloudDeskAppConfigEntity = cloudDeskAppConfigDAO.findByDeskIdAndAppType(desktopId, AppTypeEnum.CMLAUNCHER);
        try {
            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
            // aarch64架构的云桌面不支持cms云空间
            if (Objects.nonNull(cbbDeskDTO.getClusterId())) {
                PlatformComputerClusterDTO clusterDTO = computerClusterServerMgmtAPI.getComputerClusterInfoById(cbbDeskDTO.getClusterId());
                if (ObjectUtils.isNotEmpty(clusterDTO) && CbbCpuArchType.convert(clusterDTO.getArchitecture()) == CbbCpuArchType.ARM) {
                    LOGGER.info("当前云桌面运行ARM集群上：{}，暂时不支持cms云空间", JSON.toJSONString(clusterDTO));
                    return;
                }
            }
            if (Objects.isNull(cloudDeskAppConfigEntity)) {
                LOGGER.info("首次添加CMS应用软件，对应deskId：{}", desktopId);
                // 启动云桌面通过本地ISO挂载，不走samba
                cmsUpgradeAPI.addCmsIso(desktopId);
            } else {
                String isoVersion = cloudDeskAppConfigEntity.getIsoVersion();
                String cmsLauncherVersion = cmsUpgradeService.getCmsLauncherVersionFromConfig();
                // 数据库中不存在（bug修订前的数据）或者数据库保存与当前版本不一致，则重新挂载iso
                if (!StringUtils.hasText(isoVersion) || !Objects.equals(isoVersion, cmsLauncherVersion)) {
                    LOGGER.info("发现CMS应用软件更新，对应deskId {} isoVersion {} uwsLauncherVersion {}", desktopId, isoVersion, cmsLauncherVersion);
                    cmsUpgradeAPI.replaceCmsIso(desktopId);
                }
            }
        } catch (Exception e) {
            LOGGER.error("添加CMS应用软件ISO失败", e);
        }
    }

    private void doHandleUwsIso(UUID desktopId) {
        CloudDeskAppConfigEntity cloudDeskAppConfigEntity = cloudDeskAppConfigDAO.findByDeskIdAndAppType(desktopId, AppTypeEnum.UWSLAUNCHER);
        try {
            if (Objects.isNull(cloudDeskAppConfigEntity)) {
                LOGGER.info("首次添加UWS应用软件，对应deskId：{}", desktopId);
                // 启动云桌面通过本地ISO挂载，不走samba
                cmsUpgradeAPI.addUwsIso(desktopId);
            } else {
                String isoVersion = cloudDeskAppConfigEntity.getIsoVersion();
                String uwsLauncherVersion = cmsUpgradeService.getUwsLauncherVersionFromConfig();
                // 数据库中不存在（bug修订前的数据）或者数据库保存与当前版本不一致，则重新挂载iso
                if (!StringUtils.hasText(isoVersion) || !Objects.equals(isoVersion, uwsLauncherVersion)) {
                    LOGGER.info("发现UWS应用软件更新，对应deskId {} isoVersion {} uwsLauncherVersion {}", desktopId, isoVersion, uwsLauncherVersion);
                    cmsUpgradeAPI.replaceUwsIso(desktopId);
                }
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                BusinessException businessException = (BusinessException) e;
                if (com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_UWS_ISO_PATH_NOT_EXIT.equals(businessException.getKey())) {
                    LOGGER.error("添加UWS应用软件ISO失败，失败原因：{}", businessException.getI18nMessage());
                    return;
                }
            }
            LOGGER.error("添加UWS应用软件ISO失败", e);
        }
    }

    @Override
    public void changeDeskMaintenanceModel(List<UUID> desktopIds, Boolean isOpen) {
        Assert.notEmpty(desktopIds, "desktopIds must not be null");
        Assert.notNull(isOpen, "isOpen must not be null");
        cbbDeskMgmtAPI.changeDeskMaintenanceModel(desktopIds, isOpen);
    }
}
