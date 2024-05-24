package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterBasicInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: 大屏监控云平台信息API实现
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 11:41
 *
 * @author BaiGuoliang
 */
public class PlatformAPIImpl implements PlatformAPI {

    @Autowired
    private CbbClusterServerMgmtAPI clusterMgmtAPI;

    @Override
    public SystemTimeResponse getSystemTime() throws BusinessException {

        SystemTimeResponse response = new SystemTimeResponse();
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        Date date = new Date();
        response.setServerTime(date);
        response.setWeek(dateFm.format(date));

        CbbClusterBasicInfoDTO cbbClusterBasicInfoDTO = clusterMgmtAPI.getClusterBasicInfo();
        if (null != cbbClusterBasicInfoDTO) {
            response.setSystemWorkTime(cbbClusterBasicInfoDTO.getRunTime());
        }
        return response;
    }

}
