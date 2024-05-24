package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.quartz;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDesktopTempPermissionState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.DesktopTempPermissionBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description: 定时检查临时权限生效生效
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/16
 *
 * @author linke
 */
@Service
@Quartz(scheduleTypeCode = "desktop_temp_permission_effect", scheduleName =
        DesktopTempPermissionBusinessKey.RCO_QUARTZ_DESKTOP_TEMP_PERMISSION_EFFECT, cron = "0 0/1 * * * ?")
public class DesktopTempPermissionEffectQuartzTask implements QuartzTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopTempPermissionEffectQuartzTask.class);

    // 5分钟
    private static final long EXPIRE_NOTICE_TIME_MILLIS = 300000;

    @Autowired
    private DesktopTempPermissionAPI desktopTempPermissionAPI;

    @Autowired
    private CbbDesktopTempPermissionAPI cbbDesktopTempPermissionAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {

        List<CbbDesktopTempPermissionDTO> noEffectList = cbbDesktopTempPermissionAPI.listByState(CbbDesktopTempPermissionState.NOT_IN_EFFECT);
        List<CbbDesktopTempPermissionDTO> inEffectList = cbbDesktopTempPermissionAPI.listByState(CbbDesktopTempPermissionState.IN_EFFECT);
        List<CbbDesktopTempPermissionDTO> expiredList = cbbDesktopTempPermissionAPI.listByState(CbbDesktopTempPermissionState.EXPIRED);

        // 失效列表
        if (CollectionUtils.isNotEmpty(expiredList)) {
            for (CbbDesktopTempPermissionDTO permissionDTO : expiredList) {
                invalidatePermission(permissionDTO);
            }
        }

        // 检查是否需要生效
        if (CollectionUtils.isNotEmpty(noEffectList)) {
            // 判断是否需要生效
            for (CbbDesktopTempPermissionDTO permissionDTO : noEffectList) {
                doEffectPermission(permissionDTO);
            }
        }

        // 检查是否需要失效、创建失效通知（用户消息）
        if (CollectionUtils.isNotEmpty(inEffectList)) {
            for (CbbDesktopTempPermissionDTO permissionDTO : inEffectList) {
                invalidatePermission(permissionDTO);
            }

        }
    }

    private void doEffectPermission(CbbDesktopTempPermissionDTO permissionDTO) throws BusinessException {
        long current = System.currentTimeMillis();
        if (permissionDTO.getStartTime().getTime() > current || permissionDTO.getEndTime().getTime() < current) {
            LOGGER.info("临时权限[{}]不在生效时间内", permissionDTO.getName());
            return;
        }

        LOGGER.info("临时权限[{}]在生效时间内，开始通知运行中桌面", permissionDTO.getName());
        // 发送生效消息给运行中的桌面
        desktopTempPermissionAPI.sendEffectMessageByPermissionId(permissionDTO.getId());

        LOGGER.info("临时权限[{}]通知运行中桌面完成，修改状态为生效中", permissionDTO.getName());
        cbbDesktopTempPermissionAPI.updateDesktopTempPermissionState(permissionDTO.getId(), CbbDesktopTempPermissionState.IN_EFFECT);
    }

    private void invalidatePermission(CbbDesktopTempPermissionDTO permissionDTO) throws BusinessException {
        long current = System.currentTimeMillis();
        if (current > permissionDTO.getEndTime().getTime()) {
            // 失效时间到了，需要通知桌面新的配置信息，删除临时权限及其关联对象
            LOGGER.info("临时权限[{}]已到失效时间，开始删除", permissionDTO.getName());
            try {
                desktopTempPermissionAPI.deleteById(permissionDTO.getId(), false);
                // 通知EST断连
            } catch (BusinessException e) {
                LOGGER.error("临时权限[{}]到期自动删除失败", permissionDTO.getName(), e);
            }
            return;
        }

        if (EXPIRE_NOTICE_TIME_MILLIS > (permissionDTO.getEndTime().getTime() - current)) {
            // 快到期5分钟内要创建到期的用户消息
            LOGGER.info("临时权限[{}]即将到达失效时间，开始创建到期的用户消息", permissionDTO.getName());
            desktopTempPermissionAPI.createUserExpireNoticeMsg(permissionDTO.getId());
        }
    }
}
