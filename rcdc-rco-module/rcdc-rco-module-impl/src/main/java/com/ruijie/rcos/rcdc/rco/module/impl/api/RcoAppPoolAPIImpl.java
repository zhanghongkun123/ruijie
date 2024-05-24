package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDeskSpecRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbDesktopImageUpdateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbUpdateDeskNetworkVDIDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.VgpuUtil;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfoSupport;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rca.module.def.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoAppPoolAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ImageTemplateConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/2 14:51
 *
 * @author zhengjingyong
 */
public class RcoAppPoolAPIImpl implements RcoAppPoolAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoAppPoolAPIImpl.class);

    /**
     * +
     * 等待状态机超时时间1分钟
     */
    private static final Long WAIT_TIME_OUT = 60000L;

    private static final String RESOURCE_LOCKED_EXCEPTION_CODE = "98010001";

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private RcaHostAppAPI rcaHostAppAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Override
    public void syncSpec(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO cbbDeskDTO) throws BusinessException {
        Assert.notNull(rcaAppPoolBaseDTO, "rcaAppPoolBaseDTO cannot be null");
        Assert.notNull(cbbDeskDTO, "cbbDeskDTO cannot be null");

        if (cbbDeskDTO.getEnableCustom()) {
            LOGGER.info("云桌面[{}]开启了自定义规格，无需变更", cbbDeskDTO.getName());
            return;
        }

        // 判断主机规格和应用池规格是否一直，如果一致无需修改
        VgpuExtraInfoSupport vGpuExtraInfo = VgpuUtil.deserializeVgpuExtraInfoByType(
                rcaAppPoolBaseDTO.getVgpuType(), rcaAppPoolBaseDTO.getVgpuExtraInfo());
        VgpuInfoDTO vgpuInfoDTO = new VgpuInfoDTO(rcaAppPoolBaseDTO.getVgpuType(), vGpuExtraInfo);
        if (checkIsNotChangeSpec(rcaAppPoolBaseDTO, cbbDeskDTO, vgpuInfoDTO)) {
            LOGGER.info("云桌面[{}]的规格[{}]和应用池[{}]的规格[{}]一致，无需变更", cbbDeskDTO.getName(),
                    cbbDeskDTO.getImageTemplateId(), rcaAppPoolBaseDTO.getName(), rcaAppPoolBaseDTO.getImageTemplateId());
            return;
        }

        CbbUpdateDeskSpecRequest specRequest = new CbbUpdateDeskSpecRequest();
        specRequest.setDeskId(cbbDeskDTO.getDeskId());
        specRequest.setCpu(rcaAppPoolBaseDTO.getCpu());
        specRequest.setMemory(CapacityUnitUtils.gb2Mb(rcaAppPoolBaseDTO.getMemory()));
        specRequest.setEnableHyperVisorImprove(rcaAppPoolBaseDTO.getEnableHyperVisorImprove());
        // 显卡详细参数构建
        specRequest.setVgpuInfoDTO(deskSpecAPI.checkAndBuildVGpuInfo(cbbDeskDTO.getClusterId(),
                vgpuInfoDTO.getVgpuType(), vgpuInfoDTO.getVgpuExtraInfo()));
        specRequest.setPersonSize(checkNullGetValue(rcaAppPoolBaseDTO.getPersonalConfigDiskSize()
                , cbbDeskDTO.getPersonSize()));
        if (cbbDeskDTO.getPersonSize() == 0 && rcaAppPoolBaseDTO.getPersonalConfigDiskSize() != null &&
                rcaAppPoolBaseDTO.getPersonalConfigDiskSize() > 0) {
            specRequest.setPersonDiskStoragePoolId(rcaAppPoolBaseDTO.getPersonDiskStoragePoolId());
        }
        // 应用池没有额外盘规格
        specRequest.setExtraDiskList(new ArrayList<>());
        specRequest.setSystemSize(checkNullGetValue(rcaAppPoolBaseDTO.getSystemSize(), cbbDeskDTO.getSystemSize()));
        // 应用池无需修改为个性配置
        specRequest.setEnableCustom(Boolean.FALSE);

        try {
            LOGGER.info("编辑云主机[{}]的规格:{}", specRequest.getDeskId(), JSON.toJSONString(specRequest));
            cbbVDIDeskMgmtAPI.updateDeskSpec(specRequest);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM_SUCCESS, rcaAppPoolBaseDTO.getName()
                    , cbbDeskDTO.getName());
        } catch (BusinessException e) {
            LOGGER.error("编辑云主机[{}]的规格失败", cbbDeskDTO.getDeskId(), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_EDIT_SPEC_BATCH_TASK_ITEM_FAIL,
                    rcaAppPoolBaseDTO.getName(), cbbDeskDTO.getName(), e.getI18nMessage());
        }
    }

    @Override
    public void syncImageTemplate(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO desktopInfo, @Nullable BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(rcaAppPoolBaseDTO, "rcaAppPoolBaseDTO cannot be null");
        Assert.notNull(desktopInfo, "desktopInfo cannot be null");

        // 判断镜像模板和模板版本是否一致
        if (Objects.equals(rcaAppPoolBaseDTO.getImageTemplateId(), desktopInfo.getImageTemplateId())) {
            LOGGER.info("云桌面[{}]的镜像模板[{}]和应用池[{}]的镜像模板[{}]一致，无需变更", desktopInfo.getName(),
                    desktopInfo.getImageTemplateId(), rcaAppPoolBaseDTO.getName(), rcaAppPoolBaseDTO.getImageTemplateId());
            return;
        }
        UUID imageTemplateId = rcaAppPoolBaseDTO.getImageTemplateId();
        CbbDesktopImageUpdateDTO cbbDesktopImageUpdateDTO = new CbbDesktopImageUpdateDTO();
        cbbDesktopImageUpdateDTO.setDesktopId(desktopInfo.getDeskId());
        cbbDesktopImageUpdateDTO.setImageId(imageTemplateId);
        cbbDesktopImageUpdateDTO.setBatchTaskItem(taskItem);
        try {
            LOGGER.info("编辑云主机[{}]的镜像模板:[{}]", desktopInfo.getDeskId(), rcaAppPoolBaseDTO.getImageTemplateId());
            checkImageState(desktopInfo, imageTemplateId);
            userDesktopMgmtAPI.updateDesktopImage(cbbDesktopImageUpdateDTO);

            CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(desktopInfo.getDeskId());
            if (cbbDeskDTO.getWillApplyImageId() == null) {
                // 需要从镜像模板的主机和池应用中更新应用
                rcaHostAppAPI.updateAppFromImageHostWithPoolApp(rcaAppPoolBaseDTO.getImageTemplateId(),
                        desktopInfo.getDeskId(), rcaAppPoolBaseDTO.getId());
            }


            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM_SUCCESS,
                    rcaAppPoolBaseDTO.getName(), desktopInfo.getName());
        } catch (BusinessException e) {
            LOGGER.error(String.format("变更应用池[%s]云桌面[%s]镜像异常", rcaAppPoolBaseDTO.getName(), desktopInfo.getDeskId()), e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM_FAIL,
                    rcaAppPoolBaseDTO.getName(), desktopInfo.getName(), e.getI18nMessage());
        }
    }

    @Override
    public void syncNetworkStrategy(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO desktopInfo) throws BusinessException {
        Assert.notNull(rcaAppPoolBaseDTO, "rcaAppPoolBaseDTO cannot be null");
        Assert.notNull(desktopInfo, "desktopInfo cannot be null");

        // 判断网络是否一致
        if (Objects.equals(rcaAppPoolBaseDTO.getNetworkId(), desktopInfo.getNetworkId())) {
            LOGGER.info("云桌面[{}]的网络[{}]和应用池[{}]的网络[{}]一致，无需变更", desktopInfo.getName(),
                    desktopInfo.getImageTemplateId(), rcaAppPoolBaseDTO.getName(), rcaAppPoolBaseDTO.getImageTemplateId());
            return;
        }

        try {
            clusterAPI.validateVDIDesktopNetwork(desktopInfo.getClusterId(), rcaAppPoolBaseDTO.getNetworkId());

            LOGGER.info("编辑云主机[{}]的网络策略:[{}]", desktopInfo.getDeskId(), rcaAppPoolBaseDTO.getNetworkId());
            CbbUpdateDeskNetworkVDIDTO cbbUpdateReq = new CbbUpdateDeskNetworkVDIDTO();
            cbbUpdateReq.setDeskId(desktopInfo.getDeskId());
            cbbUpdateReq.setNetworkId(rcaAppPoolBaseDTO.getNetworkId());
            cbbUpdateReq.setIp(null);
            cbbVDIDeskMgmtAPI.updateDeskNetworkVDI(cbbUpdateReq);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_ITEM_SUCCESS,
                    rcaAppPoolBaseDTO.getName(), desktopInfo.getName());
        } catch (Exception e) {
            LOGGER.error("编辑云桌面的网络策略失败， 应用主机id:{}，ex :", desktopInfo.getDeskId(), e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_NETWORK_BATCH_TASK_ITEM_FAIL,
                    rcaAppPoolBaseDTO.getName(), desktopInfo.getName(), errorMsg);
        }
    }

    @Override
    public void syncMainStrategy(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO desktopInfo) throws BusinessException {
        Assert.notNull(rcaAppPoolBaseDTO, "rcaAppPoolBaseDTO cannot be null");
        Assert.notNull(desktopInfo, "desktopInfo cannot be null");

        // 判断策略是否一致
        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        relationshipDTO.setPoolId(rcaAppPoolBaseDTO.getId());
        RcaMainStrategyDTO currentMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
        if (currentMainStrategyDTO == null || currentMainStrategyDTO.getStrategyId() == null) {
            LOGGER.error("应用池[{}]的云应用策略为空", rcaAppPoolBaseDTO.getName());
            return;
        }

        UUID strategyId = desktopInfo.getStrategyId();
        RcaMainStrategyDTO deskMainStrategyDTO = rcaMainStrategyAPI.getStrategyDetailByDesktopStrategyId(strategyId);
        if (deskMainStrategyDTO == null || deskMainStrategyDTO.getStrategyId() == null) {
            LOGGER.error("云主机[{}]的云应用策略为空", desktopInfo.getName());
            return;
        }

        if (currentMainStrategyDTO.getStrategyId().equals(deskMainStrategyDTO.getStrategyId())) {
            LOGGER.info("云桌面[{}]的云应用策略[{}]和应用池[{}]的云应用策略[{}]一致，无需变更", desktopInfo.getName(),
                    desktopInfo.getStrategyId(), rcaAppPoolBaseDTO.getName(), currentMainStrategyDTO.getStrategyId());
            return;
        }

        LOGGER.info("编辑云主机[{}]的云应用策略:[{}]", desktopInfo.getDeskId(), currentMainStrategyDTO.getStrategyId());
        long hasWaitTime = 0L;
        while (hasWaitTime <= WAIT_TIME_OUT) {
            try {
                cloudDesktopMgmtAPI.configStrategy(new EditDesktopStrategyRequest(desktopInfo.getDeskId(),
                        currentMainStrategyDTO.getDeskStrategyId()));
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_SUCCESS,
                        rcaAppPoolBaseDTO.getName(), desktopInfo.getName());
                return;
            } catch (BusinessException ex) {
                if (RESOURCE_LOCKED_EXCEPTION_CODE.equals(ex.getKey())) {
                    LOGGER.error("检测到云主机资源锁定异常，等待5s后继续变更策略, ex：", ex);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        LOGGER.error("线程睡眠异常, ex：", e);
                    }
                    hasWaitTime += 5000L;
                    if (hasWaitTime > WAIT_TIME_OUT) {
                        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_FAIL,
                                rcaAppPoolBaseDTO.getName(), desktopInfo.getName(), ex.getI18nMessage());
                        return;
                    }
                } else {
                    LOGGER.error("检测到云主机资源锁定异常，超过超时时间，跳过处理, ex:", ex);
                    auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_FAIL,
                            rcaAppPoolBaseDTO.getName(), desktopInfo.getName(), ex.getI18nMessage());
                    return;
                }
            } catch (Exception e) {
                LOGGER.error("编辑云主机的云应用策略失败， 应用主机id:{}，ex :", desktopInfo.getDeskId(), e);
                String errorMsg = e.getMessage();
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_MAIN_STRATEGY_BATCH_TASK_ITEM_FAIL,
                        rcaAppPoolBaseDTO.getName(), desktopInfo.getName(), errorMsg);
                return;
            }
        }

    }

    private String getBusinessKey(BusinessException ex) {
        String outKey = ex.getAttachment("errorCode");
        String innerKey = ex.getKey();
        return outKey != null ? outKey : innerKey;
    }

    private boolean checkIsNotChangeSpec(RcaAppPoolBaseDTO rcaAppPoolBaseDTO, CbbDeskDTO cbbDeskDTO,
                                         VgpuInfoDTO vgpuInfoDTO) {
        return Objects.equals(rcaAppPoolBaseDTO.getCpu(), cbbDeskDTO.getCpu())
                && Objects.equals(CapacityUnitUtils.gb2Mb(rcaAppPoolBaseDTO.getMemory()), cbbDeskDTO.getMemory())
                && Objects.equals(rcaAppPoolBaseDTO.getPersonalConfigDiskSize(), cbbDeskDTO.getPersonSize())
                && Objects.equals(rcaAppPoolBaseDTO.getSystemSize(), cbbDeskDTO.getSystemSize())
                && deskSpecAPI.isVgpuInfoEquals(vgpuInfoDTO, cbbDeskDTO.getVgpuInfoDTO())
                && Objects.equals(rcaAppPoolBaseDTO.getEnableHyperVisorImprove(), cbbDeskDTO.getEnableHyperVisorImprove());
    }

    private void checkImageState(CbbDeskDTO cbbDeskDTO, UUID imageTemplateId) throws BusinessException {
        boolean isImageTemplateExist = cbbImageTemplateMgmtAPI.checkImageTemplateExist(imageTemplateId);
        if (!isImageTemplateExist) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_NOT_EXIST, cbbDeskDTO.getName());
        }
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
        if (!ImageTemplateConstants.IMAGE_CAN_UPDATE_SET.contains(imageTemplateDetail.getImageState())) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_EDIT_IMAGE_STATE_NOT_ALLOW);
        }
    }

    private <T> T checkNullGetValue(T nullableObj, T defaultObj) {
        return Optional.ofNullable(nullableObj).orElse(defaultObj);
    }
}
