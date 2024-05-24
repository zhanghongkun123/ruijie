package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberHostSessionAPI;
import com.ruijie.rcos.rcdc.rca.module.def.enums.AppVmOperateTypeEnum;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcacRestResponseResultEnum;
import com.ruijie.rcos.rcdc.rca.module.def.request.OcOperateHostRequest;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaHostOperateRcoDesktopSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopRebootRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewRcaHostDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/24 19:00
 *
 * @author liuwc
 */
public class RcaHostOperateRcoDesktopSPIImpl implements RcaHostOperateRcoDesktopSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaHostOperateRcoDesktopSPIImpl.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private ViewRcaHostDesktopDetailDAO viewRcaHostDesktopDetailDAO;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private RcaGroupMemberHostSessionAPI rcaGroupMemberHostSessionAPI;

    @Override
    public UUID oneClientOperateHostDesktop(OcOperateHostRequest restRequest) throws BusinessException {
        Assert.notNull(restRequest, "restRequest cannot be null!");

        LOGGER.info("收到应用主机[{}][{}]操作请求，当前为派生桌面", restRequest.getHostId(), restRequest.getOperationType());
        UUID deskId = restRequest.getHostId();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();
        String tmpUserName = cloudDesktopDetailDTO.getUserName();

        // IDV仅支持关机、强制关机
        checkAllowOperate(restRequest, cloudDesktopDetailDTO);

        AppVmOperateTypeEnum operate = restRequest.getOperationType();
        switch (operate) {
            case WAKE_UP:
            case START:
                userDesktopOperateAPI.start(new CloudDesktopStartRequest(deskId));
                LOGGER.info("启动应用主机[" + tmpDesktopName + "]成功");
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DESKTOP_START_SUC_LOG,
                        tmpUserName, Constants.APP_CLOUD_DESKTOP, tmpDesktopName);
                break;
            case STOP:
            case SHUTDOWN:
                shutdown(deskId, false);
                LOGGER.info("关闭应用主机[" + tmpDesktopName + "]成功");
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_SUC_DESC,
                        tmpUserName, Constants.APP_CLOUD_DESKTOP, tmpDesktopName);
                break;
            case SLEEP:
                sleep(deskId);
                LOGGER.info("休眠应用主机[" + tmpDesktopName + "]成功");
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DESKTOP_SLEEP_ITEM_SUC_DESC, tmpUserName, tmpDesktopName);
                break;
            case REBOOT:
                reboot(deskId);
                LOGGER.info("重启应用主机[" + tmpDesktopName + "]成功");
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DESKTOP_REBOOT_ITEM_SUC_DESC,
                        tmpUserName, Constants.APP_CLOUD_DESKTOP, tmpDesktopName);
                break;
            case FORCE_STOP:
                shutdown(deskId, true);
                LOGGER.info("强制关闭应用主机[" + tmpDesktopName + "]成功");
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_ITEM_SUC_DESC,
                        tmpUserName, Constants.APP_CLOUD_DESKTOP, tmpDesktopName);
                break;
            case FORCE_REBOOT:
                processRebootAppVm(cloudDesktopDetailDTO);
                break;
            default:
                LOGGER.error("不支持的应用主机操作类型[{}]", operate.name());
                throw new BusinessException(RcacRestResponseResultEnum.FAIL.toString(), "不支持的应用主机操作类型[" + operate.name() + "]");
        }

        return deskId;
    }

    @Override
    public CbbCloudDeskState getHostDesktopState(UUID hostId) throws BusinessException {
        Assert.notNull(hostId, "hostId cannot be null!");

        ViewRcaHostDesktopEntity entity = viewRcaHostDesktopDetailDAO.findByRcaHostId(hostId);
        return CbbCloudDeskState.valueOf(entity.getDeskState());
    }

    @Override
    public String getHostDesktopIp(UUID hostId) {
        Assert.notNull(hostId, "hostId cannot be null!");

        ViewRcaHostDesktopEntity entity = viewRcaHostDesktopDetailDAO.findByRcaHostId(hostId);
        if (Objects.nonNull(entity)) {
            return entity.getIp();
        }
        // 未找到对应的主机
        return null;
    }

    @Override
    public int countConflictRcaDeskNum(UUID poolId, UUID imageTemplateId) {
        Assert.notNull(poolId, "poolId cannot be null!");
        Assert.notNull(imageTemplateId, "imageTemplateId cannot be null!");

        return viewRcaHostDesktopDetailDAO.countConflictRcaDeskNum(poolId, imageTemplateId);
    }

    @Override
    public UUID startRcaHostDesktop(UUID hostId, @Nullable UUID userId) throws BusinessException {
        Assert.notNull(hostId, "hostId cannot be null!");

        CloudDesktopStartRequest cloudDesktopStartRequest = new CloudDesktopStartRequest(hostId);
        cloudDesktopStartRequest.setUserId(userId);
        userDesktopOperateAPI.start(cloudDesktopStartRequest);
        LOGGER.info("启动应用主机[" + hostId + "]成功");
        return hostId;
    }

    private void processRebootAppVm(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        UUID deskId = cloudDesktopDetailDTO.getId();
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();
        String tmpUserName = cloudDesktopDetailDTO.getUserName();
        shutdown(deskId, true);
        userDesktopOperateAPI.start(new CloudDesktopStartRequest(deskId));
        LOGGER.info("强制重启应用主机[" + tmpDesktopName + "]成功");
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCO_DESKTOP_REBOOT_ITEM_SUC_DESC, tmpUserName, Constants.APP_CLOUD_DESKTOP, tmpDesktopName);

    }

    private void checkAllowOperate(OcOperateHostRequest restRequest, CloudDesktopDetailDTO cloudDesktopDetailDTO)
            throws BusinessException {

        // 检查虚机类型是有允许操作
        CbbCloudDeskType appVmType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDeskType());
        AppVmOperateTypeEnum operate = restRequest.getOperationType();
        if (!checkAppVmTypeSupportOperate(appVmType, operate)) {
            throw new BusinessException(RcaBusinessKey.RCO_RCA_HOST_VM_TYPE_NOT_SUPPORT_OPERATE, appVmType.name(),
                    operate.name());
        }
    }

    private boolean checkAppVmTypeSupportOperate(CbbCloudDeskType type, AppVmOperateTypeEnum operate) {
        switch (type) {
            case VDI:
                // VDI支持所有操作
                return true;
            case IDV:
                // IDV仅支持关机、强制关机
                return operate == AppVmOperateTypeEnum.STOP || operate == AppVmOperateTypeEnum.FORCE_STOP;
            default:
                return false;
        }
    }

    @Override
    public VgpuInfoDTO checkAndBuildVGpuInfo(UUID clusterId, @Nullable VgpuType vgpuType, @Nullable VgpuExtraInfoSupport extraInfoDTO)
            throws BusinessException {
        Assert.notNull(clusterId, "clusterId cannot be null!");
        return deskSpecAPI.checkAndBuildVGpuInfo(clusterId, vgpuType, extraInfoDTO);
    }

    private void shutdown(UUID deskId, boolean isForce) throws BusinessException {
        userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(deskId, isForce));
    }

    private void reboot(UUID deskId) throws BusinessException {
        userDesktopOperateAPI.rebootDeskVDI(new CloudDesktopRebootRequest(deskId));
        rcaGroupMemberHostSessionAPI.clearSessionWhenHostShutdown(deskId);
    }

    private void sleep(UUID deskId) throws BusinessException {
        userDesktopOperateAPI.sleepRcaHost(new IdRequest(deskId));
    }

}
