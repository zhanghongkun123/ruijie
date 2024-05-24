package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbSoftClientGlobalStrategyAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.CbbSoftClientGlobalStrategyDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformMgmtAPI;
import com.ruijie.rcos.rcdc.maintenance.module.def.spi.CbbBusinessMaintainChangeVipSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ChangeVipConfigOneClickDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/22 19:38
 *
 * @author yxq
 */
public class CbbBusinessMaintainChangeVipSPIImpl implements CbbBusinessMaintainChangeVipSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbBusinessMaintainChangeVipSPIImpl.class);


    @Autowired
    private CbbSoftClientGlobalStrategyAPI softClientGlobalStrategyAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private CloudPlatformMgmtAPI cloudPlatformMgmtAPI;

    @Override
    public void preOperate() {
        LOGGER.info("执行修改服务器IP前置操作");

        try {
            // 没开启一键安装，无需初始化配置
            CbbSoftClientGlobalStrategyDTO globalStrategyDTO = softClientGlobalStrategyAPI.getGlobalStrategy();
            if (BooleanUtils.isNotTrue(globalStrategyDTO.getOpenOneInstall()) || StringUtils.isBlank(globalStrategyDTO.getServerIp())) {
                LOGGER.info("云办公客户端一键安装未开启或获取的VIP为空，无需处理");
                return;
            }

            ChangeVipConfigOneClickDTO changeVipConfigOneClickDTO = new ChangeVipConfigOneClickDTO();
            changeVipConfigOneClickDTO.setOldVip(cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest()).getDto().getClusterVirtualIpIp());
            LOGGER.info("修改服务器IP前置操作保存信息为[{}]", JSON.toJSONString(changeVipConfigOneClickDTO));
            globalParameterService.updateParameter(Constants.CHANGE_VIP_CONFIG_ONE_CLICK_KEY, JSON.toJSONString(changeVipConfigOneClickDTO));
        } catch (Exception e) {
            LOGGER.error("修改服务器IP前置初始化一键安装信息失败，失败原因：", e);
        }
    }
}
