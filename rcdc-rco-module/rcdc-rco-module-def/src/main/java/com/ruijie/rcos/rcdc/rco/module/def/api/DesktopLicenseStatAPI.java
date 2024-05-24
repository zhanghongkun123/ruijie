package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopLicenseStatDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;

import java.util.Date;
import java.util.List;

/**
 * 云桌面授权使用情况统计API
 * <p>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2023/6/14  <br>
 *
 * @author zjy
 */
public interface DesktopLicenseStatAPI {

    /**
     * 获取指定时间内的云桌面平均使用率信息
     *
     * @param licenseType 证书类型
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 授权使用趋势数据
     */
    List<DesktopLicenseStatDTO> statsByLicenseTypeAndTime(CbbTerminalLicenseTypeEnums licenseType, Date startTime, Date endTime);

    /**
     * 统计当前时间点的桌面授权信息
     */
    void saveCurrentDesktopLicense();

    /**
     * 获取指定时间内的云桌面平均使用率信息
     *
     * @param date 清理时间
     * @return 清理数量
     */
    Integer deleteOverdueStat(Date date);
}
