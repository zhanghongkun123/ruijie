package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.base.upgrade.module.def.dto.ApplicationPacketDTO;
import com.ruijie.rcos.base.upgrade.module.def.enums.PacketSourceType;
import com.ruijie.rcos.base.upgrade.module.def.spi.BaseAppPacketSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientCompressionAPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: AppPacketUploadSPIImpl <br>
 * Copyright: Copyright (c) 2024 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2024/4/10 <br>
 *
 * @author fang
 */
@DispatcherImplemetion("*.ConfigInstaller")
public class AppPacketUploadSPIImpl implements BaseAppPacketSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppPacketUploadSPIImpl.class);

    @Autowired
    private ClientCompressionAPI clientCompressionAPI;

    @Override
    public void packetUploaded(String dispatchKey, ApplicationPacketDTO applicationPacketDTO) {
        Assert.notNull(dispatchKey, "dispatchKey is null");
        Assert.notNull(applicationPacketDTO, "applicationPacketDTO is null");

        if (applicationPacketDTO.getSourceType() == PacketSourceType.LOCAL) {
            //免配置只支持内置升级包
            try {
                clientCompressionAPI.createConfiguredInstaller(applicationPacketDTO.getProductType());
            } catch (Exception e) {
                LOGGER.error("config app installer failed", e);
            }
        }
    }
}
