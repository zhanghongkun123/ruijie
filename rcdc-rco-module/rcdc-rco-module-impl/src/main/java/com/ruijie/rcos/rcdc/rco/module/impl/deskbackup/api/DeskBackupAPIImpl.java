package com.ruijie.rcos.rcdc.rco.module.impl.deskbackup.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbCreateDeskBackupDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskBackupAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler.CronExpressionConvertHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/10
 *
 * @author wuShengQiang
 */
public class DeskBackupAPIImpl implements DeskBackupAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskBackupAPIImpl.class);

    @Autowired
    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    @Value("${rccpmile.limit.max_backup_per_volume:10}")
    private Integer maxBackups;

    /**
     * 根据云桌面名称生成备份名称最大次数
     */
    private static final Integer MAX = 1000;

    @Override
    public String generateBackupNameByDesktopName(String desktopName) throws BusinessException {
        Assert.hasText(desktopName, "desktopName cannot null");
        int index = 1;
        String backupName;

        while (true) {
            backupName = desktopName + "_" + index;
            if (Boolean.TRUE.equals(checkNameDuplication(backupName))) {
                index++;
            } else {
                return backupName;
            }
            if (index >= MAX) {
                LOGGER.error("根据云桌面名称[{}]生成云桌面备份名称[{}]次未成功，放弃此次生成", desktopName, MAX);
                throw new BusinessException(DeskBackupBusinessKey.RCDC_RCO_DESK_BACKUP_NAME_GENERATE_BURST, desktopName, MAX.toString());
            }
        }
    }

    @Override
    public Boolean checkNameDuplication(String backupName) {
        Assert.hasText(backupName, "backupName cannot null");
        try {
            CbbDeskBackupDTO cbbDeskBackupDTO = cbbVDIDeskBackupAPI.findDeskBackupInfoByName(backupName);
            if (cbbDeskBackupDTO != null) {
                LOGGER.warn("检查云桌面备份名称是否重复: [{}] 重复了", backupName);
                return true;
            }
        } catch (BusinessException e) {
            LOGGER.error("检查云桌面备份名称是否重复,异常信息:[{}] ", e.getI18nMessage());
        }
        return false;
    }

    @Override
    public Integer getMaxBackups() {
        return maxBackups;
    }

    @Override
    public Boolean checkBackupNumberOverByDeskId(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId cannot null");
        List<CbbDeskBackupDTO> cbbDeskBackupDTOList = cbbVDIDeskBackupAPI.listDeskBackupInfoByDeskId(deskId);
        if (cbbDeskBackupDTOList.isEmpty()) {
            return false;
        }
        Integer size = cbbDeskBackupDTOList.size();
        if (size >= maxBackups) {
            LOGGER.info("云桌面【{}】备份数量【{}】，最大限制数量【{}】", deskId, size, maxBackups);
            return true;
        }
        return false;
    }

    @Override
    public void createDeskBackup(CbbCreateDeskBackupDTO cbbCreateDeskBackupDTO) throws BusinessException {
        Assert.notNull(cbbCreateDeskBackupDTO, "cbbCreateDeskBackupDTO cannot null");
        cbbCreateDeskBackupDTO.setMaxBackups(maxBackups);
        cbbVDIDeskBackupAPI.createDeskBackup(cbbCreateDeskBackupDTO);
    }

    @Override
    public String generateExpression(RcoScheduleTaskRequest scheduleTaskRequest) throws BusinessException {
        Assert.notNull(scheduleTaskRequest, "RcoScheduleTaskRequest cannot null");

        return CronExpressionConvertHandler.generateExpression(scheduleTaskRequest);
    }

    @Override
    public CronConvertDTO parseCronExpression(String cronExpression) throws BusinessException {
        Assert.notNull(cronExpression, "cronExpression cannot null");

        return CronExpressionConvertHandler.parseCronExpression(cronExpression);
    }
}
