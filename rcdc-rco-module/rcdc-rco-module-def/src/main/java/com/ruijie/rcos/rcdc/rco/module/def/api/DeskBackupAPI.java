package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbCreateDeskBackupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description: 云桌面备份API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/7
 *
 * @author wuShengQiang
 */
public interface DeskBackupAPI {

    /**
     * 生成备份名称
     * 
     * @param desktopName 云桌面名称
     * @return 备份名称
     * @throws BusinessException 业务异常
     */
    String generateBackupNameByDesktopName(String desktopName) throws BusinessException;

    /**
     * 检测备份名称是否重复， true 重复 false 可用
     *
     * @param backupName 备份名称
     * @return 响应 true 重复 false 可用
     */
    Boolean checkNameDuplication(String backupName);

    /**
     * 获取云桌面备份数量最大限制数
     *
     * @return 云桌面备份数量最大限制数
     */
    Integer getMaxBackups();

    /**
     * 检测云桌面备份数量是否超配
     * 
     * @param deskId 云桌面ID
     * @return true：超配，false：未超配
     * @throws BusinessException 业务异常
     */
    Boolean checkBackupNumberOverByDeskId(UUID deskId) throws BusinessException;

    /**
     * 创建桌面备份,如果数量超限则删除最早的备份
     *
     * @param cbbCreateDeskBackupDTO 入参
     * @throws BusinessException 业务异常
     */
    void createDeskBackup(CbbCreateDeskBackupDTO cbbCreateDeskBackupDTO) throws BusinessException;

    /**
     * 定时任务转换
     * @param scheduleTaskRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    String generateExpression(RcoScheduleTaskRequest scheduleTaskRequest) throws BusinessException;

    /**
     * 解析Cron表达式
     *
     * @param cronExpression Cron表达式
     * @return 响应
     * @throws BusinessException 业务异常
     */
    CronConvertDTO parseCronExpression(String cronExpression) throws BusinessException;
}
