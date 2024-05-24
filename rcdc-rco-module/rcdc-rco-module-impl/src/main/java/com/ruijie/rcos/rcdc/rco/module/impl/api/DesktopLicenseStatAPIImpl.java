package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.DashboardStatisticsAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopLicenseStatAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.LicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopLicenseStatDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.LicenseTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ObtainRcdcLicenseNumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainIdvUpLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainVOILicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopLicenseStatDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopLicenseStatEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

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
public class DesktopLicenseStatAPIImpl implements DesktopLicenseStatAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopLicenseStatAPIImpl.class);

    public static final String STEP_ONE_MINUTE = "1 minutes";

    public static final String STEP_TWO_MINUTE = "2 minutes";

    public static final String STEP_ONE_HOUR = "1 hours";

    public static final String STEP_SIX_HOUR = "6 hours";

    public static final String STEP_ONE_DAY = "1 days";

    public static final Integer TIME_DIFF_ONE_HOUR = 60 * 60 * 1000;

    public static final Integer TIME_DIFF_ONE_DAY = 24 * 60 * 60 * 1000;

    public static final Integer TIME_DIFF_ONE_WEEK = 7 * 24 * 60 * 60 * 1000;

    public static final Long TIME_DIFF_ONE_MONTH = 30 * 24 * 60 * 60 * 1000L;

    @Autowired
    private DesktopLicenseStatDAO desktopLicenseStatDAO;

    @Autowired
    private LicenseAPI licenseAPI;

    @Autowired
    private DashboardStatisticsAPI dashboardStatisticsAPI;

    @Override
    public List<DesktopLicenseStatDTO> statsByLicenseTypeAndTime(CbbTerminalLicenseTypeEnums licenseType, Date startTime, Date endTime) {
        Assert.notNull(licenseType, "licenseType不能为null");
        Assert.notNull(startTime, "startTime不能为null");
        Assert.notNull(endTime, "endTime不能为null");

        // 根据规则计算步长
        String step = STEP_ONE_DAY;
        Calendar calendarStartTime = Calendar.getInstance();
        calendarStartTime.setTime(startTime);
        Calendar calendarEndTimeBeforeOneMonth = Calendar.getInstance();
        calendarEndTimeBeforeOneMonth.setTime(endTime);
        calendarEndTimeBeforeOneMonth.add(Calendar.MONTH, -1);

        long diffTime = endTime.getTime() - startTime.getTime();
        if (diffTime < 0) {
            return Collections.emptyList();
        } else if (diffTime <= TIME_DIFF_ONE_HOUR) {
            step = STEP_ONE_MINUTE;
        } else if (diffTime <= TIME_DIFF_ONE_DAY) {
            step = STEP_TWO_MINUTE;
        } else if (diffTime <= TIME_DIFF_ONE_WEEK) {
            step = STEP_ONE_HOUR;
        } else if (calendarStartTime.compareTo(calendarEndTimeBeforeOneMonth) >= 0) {
            step = STEP_SIX_HOUR;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("计算得到步长为：{}", step);
        }

        List<Map<String, Object>> statDataList = desktopLicenseStatDAO.statsByLicenseTypeAndTime(licenseType.toString(),
                startTime, endTime, step);
        if (CollectionUtils.isEmpty(statDataList)) {
            return Collections.emptyList();
        }

        List<DesktopLicenseStatDTO> desktopLicenseStatDTOList = new ArrayList<>(statDataList.size());
        statDataList.forEach(item -> {
            DesktopLicenseStatDTO desktopLicenseStatDTO = JSON.parseObject(JSON.toJSONString(item), DesktopLicenseStatDTO.class);
            desktopLicenseStatDTOList.add(desktopLicenseStatDTO);
        });
        return desktopLicenseStatDTOList;
    }

    @Override
    public void saveCurrentDesktopLicense() {
        Date currentTimeWithoutSecond = getCurrentTimeWithoutSecond();
        LOGGER.info("start save current desktop license, currentTime ：{}", currentTimeWithoutSecond);

        // 收集VDI授权使用情况
        DesktopLicenseStatEntity vdiDesktopLicenseStat = new DesktopLicenseStatEntity();
        vdiDesktopLicenseStat.setId(UUID.randomUUID());
        vdiDesktopLicenseStat.setLicenseType(CbbTerminalLicenseTypeEnums.VDI);
        vdiDesktopLicenseStat.setCreateTime(currentTimeWithoutSecond);
        try {
            ObtainRcdcLicenseNumResponse vdiLicenseNumResponse = licenseAPI.acquireLicenseNum(LicenseTypeEnum.DESKTOP);
            vdiDesktopLicenseStat.setTotalCount(vdiLicenseNumResponse.getLicenseNum());
            vdiDesktopLicenseStat.setUsedCount(vdiLicenseNumResponse.getUsedNum());
            vdiDesktopLicenseStat.setVdiUsedCount(vdiLicenseNumResponse.getVdiCloudDesktopUsedLicenseNum());
            vdiDesktopLicenseStat.setIdvUsedCount(vdiLicenseNumResponse.getIdvTerminalUsedLicenseNum());
            vdiDesktopLicenseStat.setVoiUsedCount(vdiLicenseNumResponse.getTciTerminalUsedLicenseNum());
            vdiDesktopLicenseStat.setRcaUsedCount(vdiLicenseNumResponse.getRcaUsedLicenseNum());
        } catch (Exception e) {
            LOGGER.error("Error in get vdi license info", e);
        }
        desktopLicenseStatDAO.save(vdiDesktopLicenseStat);

        // 收集IDV授权使用情况
        DesktopLicenseStatEntity idvDesktopLicenseStat = new DesktopLicenseStatEntity();
        idvDesktopLicenseStat.setId(UUID.randomUUID());
        idvDesktopLicenseStat.setLicenseType(CbbTerminalLicenseTypeEnums.IDV);
        idvDesktopLicenseStat.setCreateTime(currentTimeWithoutSecond);
        try {
            ObtainLicenseInfoResponse idvLicenseInfoResponse = dashboardStatisticsAPI.obtainLicenseInfo(CbbTerminalLicenseTypeEnums.IDV);
            idvDesktopLicenseStat.setTotalCount(idvLicenseInfoResponse.getTotal());
            idvDesktopLicenseStat.setUsedCount(idvLicenseInfoResponse.getUsed());
            idvDesktopLicenseStat.setIdvUsedCount(idvLicenseInfoResponse.getIdvUsed());
            idvDesktopLicenseStat.setVoiUsedCount(idvLicenseInfoResponse.getVoiUsed());
        } catch (BusinessException e) {
            LOGGER.error("Error in get idv license info", e);
        }
        desktopLicenseStatDAO.save(idvDesktopLicenseStat);

        // 收集VOI授权使用情况
        DesktopLicenseStatEntity voiDesktopLicenseStat = new DesktopLicenseStatEntity();
        voiDesktopLicenseStat.setId(UUID.randomUUID());
        voiDesktopLicenseStat.setLicenseType(CbbTerminalLicenseTypeEnums.VOI);
        voiDesktopLicenseStat.setCreateTime(currentTimeWithoutSecond);
        try {
            ObtainVOILicenseInfoResponse obtainVOILicenseInfoResponse = dashboardStatisticsAPI.obtainVOILicenseInfo(CbbTerminalLicenseTypeEnums.VOI);
            voiDesktopLicenseStat.setTotalCount(obtainVOILicenseInfoResponse.getTotal());
            voiDesktopLicenseStat.setUsedCount(obtainVOILicenseInfoResponse.getUsed());
            voiDesktopLicenseStat.setEduVoiUsedCount(obtainVOILicenseInfoResponse.getEduVoiNumber());
        } catch (BusinessException e) {
            LOGGER.error("Error in get voi license info", e);
        }
        desktopLicenseStatDAO.save(voiDesktopLicenseStat);

        // 收集VOI_PLUS授权使用情况
        DesktopLicenseStatEntity voiPlusDesktopLicenseStat = new DesktopLicenseStatEntity();
        voiPlusDesktopLicenseStat.setId(UUID.randomUUID());
        voiPlusDesktopLicenseStat.setLicenseType(CbbTerminalLicenseTypeEnums.VOI_PLUS_UPGRADED);
        voiPlusDesktopLicenseStat.setCreateTime(currentTimeWithoutSecond);
        try {
            ObtainIdvUpLicenseInfoResponse obtainIdvUpLicenseInfoResponse =
                    dashboardStatisticsAPI.obtainIdvUpLicenseInfo(CbbTerminalLicenseTypeEnums.VOI_PLUS_UPGRADED);
            voiPlusDesktopLicenseStat.setTotalCount(obtainIdvUpLicenseInfoResponse.getTotal());
            voiPlusDesktopLicenseStat.setUsedCount(obtainIdvUpLicenseInfoResponse.getUsed());
        } catch (BusinessException e) {
            LOGGER.error("Error in get voi plus license info", e);
        }
        desktopLicenseStatDAO.save(voiPlusDesktopLicenseStat);
    }

    @Override
    public Integer deleteOverdueStat(Date date) {
        Assert.notNull(date, "date cannot be null");

        return desktopLicenseStatDAO.deleteByCreateTimeBefore(date);
    }

    private Date getCurrentTimeWithoutSecond() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
