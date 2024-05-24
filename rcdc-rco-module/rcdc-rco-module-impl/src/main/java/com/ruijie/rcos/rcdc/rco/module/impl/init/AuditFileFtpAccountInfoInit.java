package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.SecurityConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.util.SecurityUtils;
import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionProductOnConfig;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.FtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.common.SecurityBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.terminal.module.def.util.RandomUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Description: 初始化文件导出审批ftp的密码
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
@Conditional(ConditionProductOnConfig.class)
public class AuditFileFtpAccountInfoInit implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditFileFtpAccountInfoInit.class);

    private static final Integer FTP_PASSWORD_LENGTH = 8;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private AuditFileUpdateNotifyService auditFileUpdateNotifyService;

    @Autowired
    private AuditPrinterUpdateNotifyService auditPrinterUpdateNotifyService;
    
    @Autowired
    private AuditFileService auditFileService;

    @Override
    public void safeInit() {
        LOGGER.info("start to update audit file ftp passwd");
        String passwd = RandomUtils.generatePassword(FTP_PASSWORD_LENGTH);
        String ftpConfigInfo = globalParameterService.findParameter(SecurityConstants.AUDIT_FILE_FTP_CONFIG_KEY);
        FtpConfigDTO config = JSONObject.parseObject(ftpConfigInfo, FtpConfigDTO.class);
        String userName = config.getFtpUserName();
        config.setFtpUserPassword(passwd);

        try {
            String command = String.format("echo %s|passwd --stdin %s", passwd, userName);
            String[] commandArr = new String[] {"sh", "-c", command};
            updatePasswd(commandArr);
            // 密码加密入库
            config.setFtpUserPassword(SecurityUtils.encryptXOR(passwd, RedLineUtil.getAuditFtpRedLine()));
            globalParameterService.updateParameter(SecurityConstants.AUDIT_FILE_FTP_CONFIG_KEY, JSON.toJSONString(config));

            auditFileUpdateNotifyService.notifyAllStrategyDeskAuditFile();
            auditPrinterUpdateNotifyService.notifyAllStrategyDeskAuditPrinter();
            
            // 将文件导出审批中文件校验中的状态，初始化为文件待上传，修复CDC异常宕机导致文件单状态没有更新的场景
            auditFileService.initAuditFileState();
        } catch (Exception e) {
            LOGGER.error("执行系统命令修改用于文件导出审批的ftp账号的密码失败", e);
            config.setFtpUserPassword(SecurityConstants.AUDIT_FILE_FTP_DEFAULT_PASSWORD);
            globalParameterService.updateParameter(SecurityConstants.AUDIT_FILE_FTP_CONFIG_KEY, JSON.toJSONString(config));
        }
    }

    private void updatePasswd(String... commandArr) throws BusinessException {
        int ret;
        try {
            Process process = new ProcessBuilder(commandArr).start();
            ret = process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new BusinessException(SecurityBusinessKey.RCDC_RCO_SYSTEM_CMD_EXECUTE_FAIL, e);
        }

        if (ret != 0) {
            throw new BusinessException(SecurityBusinessKey.RCDC_RCO_SYSTEM_CMD_EXECUTE_FAIL);
        }
    }
}
