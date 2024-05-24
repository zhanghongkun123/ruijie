package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.impl;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.sysmanage.module.def.api.SambaServiceAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.ClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeSubTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.DistributeTaskProcessor;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalFtpAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.TerminalFtpConfigInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/23 11:32
 *
 * @author zhangyichi
 */
public abstract class AbstractDistributeTaskProcessor implements DistributeTaskProcessor {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractDistributeTaskProcessor.class);

    /**
     * 任务超时时间（30分钟）
     */
    protected static final long TIMEOUT_MILLI = TimeUnit.MINUTES.toMillis(30);

    protected static final String APP_SHARE_NAME = "app";

    protected static final String DISK_SYMBOL = "S:";

    protected static final String DISK_NAME = "共享目录";

    /**
     * 软件种子 FTP子目录
     */
    protected static final String SOFTWARE_SEED_DIR = "/software/seed";

    @Autowired
    protected DistributeTaskService taskService;

    @Autowired
    protected DistributeSubTaskService subTaskService;

    @Autowired
    protected SambaServiceAPI sambaServiceAPI;

    @Autowired
    protected FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    @Autowired
    protected CbbTerminalFtpAPI cbbTerminalFtpAPI;

    @Autowired
    protected CbbDeskSoftMgmtAPI cbbDeskSoftMgmtAPI;

    @Autowired
    protected CloudPlatformMgmtAPI cloudPlatformMgmtAPI;

    /**
     * 任务信息基础检查
     *
     * @param subTaskDTO 任务信息
     * @return 是否检查通过
     */
    protected boolean preProcessBasicCheck(DistributeSubTaskDTO subTaskDTO) throws BusinessException {
        // 检查子任务状态
        if (subTaskDTO.getStatus() != FileDistributionTaskStatus.WAITING) {
            LOGGER.warn("子任务[{}]处于[{}]状态，不执行前置检查", subTaskDTO.getId(), subTaskDTO.getStatus().name());
            return false;
        }
        return true;
    }


    @Deprecated
    @Override
    public void postProcess(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        // 更新任务信息
        subTaskDTO = subTaskService.findById(subTaskDTO.getId());

        if (subTaskDTO.getStatus() == FileDistributionTaskStatus.CANCELED) {
            LOGGER.warn("文件分发子任务[{}]处于[{}]状态，不执行后置处理", subTaskDTO.getId(), subTaskDTO.getStatus().name());
        }
    }

    @Deprecated
    @Override
    public void errorProcess(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO, Exception exception) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");
        Assert.notNull(exception, "exception cannot be null!");
        LOGGER.error("文件分发子任务[{}]执行失败", subTaskDTO.getId(), exception);
        String failMessage = LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_FAIL_MSG_OTHER);
        if (exception.getClass().isAssignableFrom(BusinessException.class)) {
            failMessage = ((BusinessException) exception).getI18nMessage();
        }
        // 变更子任务状态
        try {
            subTaskService.changeSubTaskToFail(subTaskDTO, failMessage);
        } catch (Exception e) {
            LOGGER.error("忽略保存数据库异常！", e.getMessage());
        }
    }

    @Override
    public void execute(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");
        preProcess(subTaskDTO, parameterDTO);
        doDistribute(subTaskDTO, parameterDTO);
        postProcess(subTaskDTO, parameterDTO);
    }


    protected FtpConfigDTO getFtpConfig() throws BusinessException {
        TerminalFtpConfigInfo terminalFtpConfigInfo = cbbTerminalFtpAPI.getTerminalFtpConfigInfo();
        FtpConfigDTO ftpConfigDTO = new FtpConfigDTO();
        BeanUtils.copyProperties(terminalFtpConfigInfo, ftpConfigDTO);
        // 设置 软件种子ftp子目录
        ftpConfigDTO.setFileDir(SOFTWARE_SEED_DIR);
        ftpConfigDTO.setFtpServerIp(getClusterVirtualIp());
        return ftpConfigDTO;
    }


    private String getClusterVirtualIp() throws BusinessException {
        DtoResponse<ClusterVirtualIpDTO> clusterVirtualIpResponse = cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest());
        return Optional.ofNullable(clusterVirtualIpResponse).map(DtoResponse::getDto).map(ClusterVirtualIpDTO::getClusterVirtualIpIp).orElse(null);
    }

}
