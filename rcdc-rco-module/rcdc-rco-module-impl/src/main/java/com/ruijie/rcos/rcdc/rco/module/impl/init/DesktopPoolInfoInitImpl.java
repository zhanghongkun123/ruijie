package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 系统异常恢复后，桌面池相关恢复初始化
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/01/26
 *
 * @author linke
 */
@Service
public class DesktopPoolInfoInitImpl implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopPoolInfoInitImpl.class);

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Override
    public void safeInit() {

        List<CbbDesktopPoolDTO> cbbDesktopPoolDTOList = cbbDesktopPoolMgmtAPI.listAllDesktopPool();
        cbbDesktopPoolDTOList = cbbDesktopPoolDTOList.stream()
                .filter(pool -> pool.getPoolState() == CbbDesktopPoolState.CREATING || pool.getPoolState() == CbbDesktopPoolState.UPDATING)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cbbDesktopPoolDTOList)) {
            return;
        }

        List<String> nameList = new ArrayList<>();
        for (CbbDesktopPoolDTO desktopPoolDTO : cbbDesktopPoolDTOList) {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
            nameList.add(desktopPoolDTO.getName());
        }
        LOGGER.info("完成rcdc异常恢复后，桌面池[{}]从中间状态恢复为可用状态", StringUtils.join(nameList, ","));
    }
}
