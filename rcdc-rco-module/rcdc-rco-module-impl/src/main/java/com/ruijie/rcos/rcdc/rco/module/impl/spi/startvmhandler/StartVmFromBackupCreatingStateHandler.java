package com.ruijie.rcos.rcdc.rco.module.impl.spi.startvmhandler;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.enums.CbbDeskBackupStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.VMMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.vm.VmIdRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.vm.VmDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VmState;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/10 10:59
 *
 * @author zhangsiming
 */

@Service
public class StartVmFromBackupCreatingStateHandler extends AbstractMessageHandlerTemplate<StartVmDispatcherDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmFromBackupCreatingStateHandler.class);

    @Autowired
    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    @Autowired
    private VMMgmtAPI vmMgmtAPI;
    
    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Override
    protected boolean isNeedHandleMessage(StartVmDispatcherDTO request) {
        boolean isBackupCreatingOrResuming = CbbCloudDeskState.BACKUP_CREATING == request.getDeskState();
        if (!isBackupCreatingOrResuming) {
            //检查是否处于resuming状态
            CbbDeskBackupDTO deskBackupDTO =
                    cbbVDIDeskBackupAPI.findLatestDeskBackupByDeskIdAndState(request.getDesktopId(), CbbDeskBackupStateEnum.RESUMING);
            if (deskBackupDTO.getState() == CbbDeskBackupStateEnum.RESUMING) {
                request.setDeskBackupState(CbbDeskBackupStateEnum.RESUMING);
                isBackupCreatingOrResuming = true;
            }
        } else {
            request.setDeskBackupState(CbbDeskBackupStateEnum.CREATING);
        }

        if (isBackupCreatingOrResuming) {
            VmDTO vmDTO = null;
            try {
                CbbDeskDTO deskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(request.getDesktopId());
                vmDTO = vmMgmtAPI.queryById(new VmIdRequest(deskDTO.getPlatformId(), deskDTO.getDeskId())).getDto();
                if (vmDTO != null) {
                    final VmState vmState = vmDTO.getVmState();
                    switch (vmState) {
                        case STOPPED:
                        case SLEEP:
                            //如果处于备份中，需要暂停备份

                        case ACTIVE:
                            LOGGER.info("查询并设置虚机[{}]状态[{}]", request.getDesktopId(), vmState);
                            request.setVmState(vmState);
                            break;
                        default:
                    }
                }
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }

        }
        return false;
    }

    @Override
    protected void processMessage(StartVmDispatcherDTO request) throws Exception {
        //空方法


    }
}
