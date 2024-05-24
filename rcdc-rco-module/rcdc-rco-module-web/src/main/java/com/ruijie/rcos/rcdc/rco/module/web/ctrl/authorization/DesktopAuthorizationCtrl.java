package com.ruijie.rcos.rcdc.rco.module.web.ctrl.authorization;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopLicenseStatAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopLicenseStatDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.authorization.dto.DesktopLicenseStatValueDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.authorization.request.DesktopLicenseStatWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Description: 桌面授权
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年06月14日
 *
 * @author zjy
 */
@Api(tags = "桌面授权")
@Controller
@RequestMapping("/rco/desktopAuthorization")
public class DesktopAuthorizationCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopAuthorizationCtrl.class);

    @Autowired
    private DesktopLicenseStatAPI desktopLicenseStatAPI;

    /**
     * 查询云桌面授权趋势数据
     *
     * @param webRequest 请求
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询云桌面授权趋势数据")
    @RequestMapping(value = "statisticsHistory", method = RequestMethod.POST)
    public DefaultWebResponse statisticsHistory(DesktopLicenseStatWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");

        List<Date> dateList = getDurationByTimeQueryType(webRequest.getTimeQueryType(), webRequest.getEndTime());
        List<DesktopLicenseStatDTO> desktopLicenseStatDTOList = desktopLicenseStatAPI.statsByLicenseTypeAndTime(
                webRequest.getAuthorizationType(), dateList.get(0), dateList.get(1));
        if (CollectionUtils.isEmpty(desktopLicenseStatDTOList)) {
            return DefaultWebResponse.Builder.success(new String[]{});
        }

        List<DesktopLicenseStatValueDTO> licenseStatValueDTOList = new ArrayList<>();
        desktopLicenseStatDTOList.forEach(item -> {
            DesktopLicenseStatValueDTO statValueDTO = new DesktopLicenseStatValueDTO();
            BeanUtils.copyProperties(item, statValueDTO);
            licenseStatValueDTOList.add(statValueDTO);
        });
        return DefaultWebResponse.Builder.success(licenseStatValueDTOList);
    }

    private List<Date> getDurationByTimeQueryType(TimeQueryTypeEnum timeQueryType, Date endTime) {
        // 数据库中记录的时间均为整分钟数，需要将数据格式进行处理
        Calendar calendarEndTime = Calendar.getInstance();
        calendarEndTime.setTime(endTime);
        if (calendarEndTime.get(Calendar.SECOND) != 0 || calendarEndTime.get(Calendar.MILLISECOND) != 0) {
            // 秒数不为0的情况下，将时间取整
            calendarEndTime.set(Calendar.SECOND, 0);
            calendarEndTime.set(Calendar.MILLISECOND, 0);
        }

        Calendar calendarStartTime = Calendar.getInstance();
        calendarStartTime.setTime(calendarEndTime.getTime());
        if (TimeQueryTypeEnum.HOUR == timeQueryType) {
            calendarStartTime.add(Calendar.HOUR_OF_DAY, -1);
        } else if (TimeQueryTypeEnum.DAY == timeQueryType) {
            calendarStartTime.add(Calendar.DAY_OF_MONTH, -1);
        } else if (TimeQueryTypeEnum.WEEK == timeQueryType) {
            calendarStartTime.add(Calendar.WEEK_OF_MONTH, -1);
        } else if (TimeQueryTypeEnum.MONTHLY == timeQueryType) {
            calendarStartTime.add(Calendar.MONTH, -1);
        } else if (TimeQueryTypeEnum.YEAR == timeQueryType) {
            calendarStartTime.add(Calendar.YEAR, -1);
        }

        LOGGER.debug("输入的结束时间为：{}, 计算得到的开始时间为：{}，结束时间为：{}",
                DateFormatUtils.format(endTime, "yyyy-MM-dd HH:mm:ss"),
                DateFormatUtils.format(calendarStartTime.getTime(), "yyyy-MM-dd HH:mm:ss"),
                DateFormatUtils.format(calendarEndTime.getTime(), "yyyy-MM-dd HH:mm:ss"));

        return Lists.newArrayList(calendarStartTime.getTime(), calendarEndTime.getTime());
    }

}
