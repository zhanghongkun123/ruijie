package com.ruijie.rcos.rcdc.rco.module.impl.init;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopImageUpdateDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;

/**
 * Description: 异常情况-云桌面变更镜像模板任务继续执行
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-05-07
 *
 * @author zqj
 */
@Service
public class DesktopEditImageTaskInit implements SafetySingletonInitializer, Ordered {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopEditImageTaskInit.class);

    private static final String BATCH_CREATE_VDI_DESKTOP_INIT_THREAD_MAIN = "batch_edit_image_template_init_thread_main";

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private static final int DEFAULT_ORDER = 999;

    @Override
    public void safeInit() {
        ThreadExecutors.execute(BATCH_CREATE_VDI_DESKTOP_INIT_THREAD_MAIN, () -> {
            while (true) {
                try {
                    List<CbbDeskDTO> deskDTOList = cbbDeskMgmtAPI.findByWaitUpdateImageTemplate();
                    deskDTOList = deskDTOList.stream()
                            .filter(s -> s.getDeskState() == CbbCloudDeskState.CLOSE || s.getDeskState() == CbbCloudDeskState.ERROR)
                            .collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(deskDTOList)) {
                        LOGGER.info("云桌面变更镜像模板任务已完成");
                        break;
                    }
                    LOGGER.info("云桌面变更镜像模板任务开始");
                    for (CbbDeskDTO cbbDeskDTO : deskDTOList) {
                        // 处理待变更镜像模板
                        userDesktopMgmtAPI.doWaitUpdateDesktopImage(new DesktopImageUpdateDTO(cbbDeskDTO.getDeskId()));
                    }
                    LOGGER.info("云桌面变更镜像模板任务结束");
                } catch (Exception ex) {
                    LOGGER.error("云桌面变更镜像模板任务异常", ex);
                }
            }
        });

    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }
}
