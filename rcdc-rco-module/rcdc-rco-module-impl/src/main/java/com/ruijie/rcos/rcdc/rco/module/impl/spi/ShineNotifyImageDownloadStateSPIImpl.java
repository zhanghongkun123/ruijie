package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageDownloadStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageDownloadStateService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineNotifyImageDownloadStateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Description: SHINE上报的镜像下载状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/28 14:14
 *
 * @author yxq
 */

@DispatcherImplemetion(ShineAction.NOTIFY_IMAGE_DOWNLOAD_STATUS)
public class ShineNotifyImageDownloadStateSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineNotifyImageDownloadStateSPIImpl.class);

    /**
     * 对于用户不关心的错误码，转成此错误码
     */
    private static final Integer DEFAULT_FAIL_CODE = 99;

    /**
     * 用于转换SHINE上报上来，RCDC关心的一些错误码，-7：取消下载；3：驱动未安装；104：终端离线, -9 GT版本低，接口找不到
     */
    private static final Set<Integer> SUPPORT_FAIL_CODE = new HashSet<>(Arrays.asList(3, -7, 104, -9));

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest dispatcherRequest) {
        Assert.notNull(dispatcherRequest, "dispatcherRequest must not be null");
        Assert.notNull(dispatcherRequest.getData(), "data must not be null");

        String data = dispatcherRequest.getData();
        String terminalId = dispatcherRequest.getTerminalId();
        LOGGER.info("终端[{}]上报的镜像下载信息为[{}]", terminalId, data);
        // 获取DTO
        ShineNotifyImageDownloadStateDTO shineNotifyImageDownloadStateDTO = JSON.parseObject(data, ShineNotifyImageDownloadStateDTO.class);

        // 如果云桌面不存在，则证明这个服务器上面没有这个终端的信息，则不需要修改对应的状态
        UUID deskId = shineNotifyImageDownloadStateDTO.getDeskId();
        if (deskId != null) {
            try {
                cbbDeskMgmtAPI.getDeskById(shineNotifyImageDownloadStateDTO.getDeskId());
            } catch (BusinessException e) {
                LOGGER.info("云桌面[{}]不存在", deskId);
                return;
            }
        }

        // 修改表数据
        ImageDownloadStateDTO imageDownloadStateDTO = new ImageDownloadStateDTO();
        imageDownloadStateDTO.setTerminalId(terminalId);
        imageDownloadStateDTO.setDownloadState(shineNotifyImageDownloadStateDTO.getState());
        // 失败的时候，将用户不关心的错误码转为99
        if (shineNotifyImageDownloadStateDTO.getState() == DownloadStateEnum.FAIL) {
            imageDownloadStateDTO.setFailCode(parseFailCode(shineNotifyImageDownloadStateDTO.getFailCode()));
        }
        imageDownloadStateDTO.setImageId(shineNotifyImageDownloadStateDTO.getImageId());
        imageDownloadStateDTO.setTerminalDownloadFinishTime(shineNotifyImageDownloadStateDTO.getTimeStamp());
        imageDownloadStateDTO.setImageRecoveryPointId(shineNotifyImageDownloadStateDTO.getImageRecoveryPointId());
        imageDownloadStateService.update(imageDownloadStateDTO);
    }

    /**
     * 将SHINE上报上来的错误码，转成RCDC期望的错误码
     *
     * @param failCode SHINE上报的错误码
     * @return RCDC期望的错误码
     */
    private Integer parseFailCode(Integer failCode) {
        return SUPPORT_FAIL_CODE.contains(failCode) ? failCode : DEFAULT_FAIL_CODE;
    }
}
