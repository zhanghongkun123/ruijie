package com.ruijie.rcos.rcdc.rco.module.impl.init;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbEditDeskPwdConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 初始化存量公共、Linux终端默认开启密码展示
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/11 18:32
 *
 * @author yxq
 */
@Service
public class DeskRootPwdInit implements SafetySingletonInitializer {

    private static final String HAS_INIT_DESK_ROOT_PWD_CONFIG_KEY = "has_init_desk_root_pwd_config";

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskRootPwdInit.class);

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public void safeInit() {
        LOGGER.info("执行初始化存量桌面root密码显示配置");
        ThreadExecutors.execute("DeskRootPwdInit", this::doInit);
    }

    private void doInit() {

        String paramValue = globalParameterService.findParameter(HAS_INIT_DESK_ROOT_PWD_CONFIG_KEY);
        if (Boolean.parseBoolean(paramValue)) {
            LOGGER.info("已经执行过初始化存量桌面root密码显示配置，无需重复执行");
            return;
        }

        while (true) {
            try {
                PageSearchRequest pageSearchRequest = new PageSearchRequest();
                pageSearchRequest.setPage(0);
                pageSearchRequest.setLimit(Integer.MAX_VALUE);
                DefaultPageResponse<CloudDesktopDTO> pageResponse = cloudDesktopMgmtAPI.pageQuery(pageSearchRequest);

                // 过滤使用linux操作系统
                List<CloudDesktopDTO> deskList = Stream.of(pageResponse.getItemArr())
                    .filter(item -> CbbOsType.UOS_64.name().equals(item.getOsName()) || CbbOsType.KYLIN_64.name().equals(item.getOsName())
                                || CbbOsType.NEOKYLIN_64.name().equals(item.getOsName())) // 麒麟和UOS
                    .filter(item -> IdvTerminalModeEnums.PUBLIC.toString().equals(item.getIdvTerminalMode()) ||
                                IacUserTypeEnum.VISITOR.name().equals(item.getUserType())) // 公共或者访客桌面
                    .collect(Collectors.toList());

                // 默认展示密码
                for (CloudDesktopDTO desktopDTO : deskList) {
                    CbbEditDeskPwdConfigDTO editDeskPwdConfigDTO = new CbbEditDeskPwdConfigDTO();
                    editDeskPwdConfigDTO.setDeskId(desktopDTO.getCbbId());
                    editDeskPwdConfigDTO.setShowRootPwd(Boolean.TRUE);
                    editDeskPwdConfigDTO.setNeedNotifyGt(Boolean.FALSE);
                    try {
                        cbbDeskMgmtAPI.editDeskRootPwdConfig(editDeskPwdConfigDTO);
                    } catch (Exception e) {
                        LOGGER.warn("修改桌面[{}]root密码配置失败", desktopDTO.getCbbId(), e);
                    }
                }
                break;
            } catch (Exception e) {
                LOGGER.error("执行初始化存量桌面root密码配置失败", e);
                sleep();
            }
        }

        LOGGER.info("执行初始化存量桌面root密码配置成功，修改数据库初标识");
        globalParameterService.updateParameter(HAS_INIT_DESK_ROOT_PWD_CONFIG_KEY, Boolean.TRUE.toString());
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            LOGGER.error("执行休眠失败");
        }
    }
}
