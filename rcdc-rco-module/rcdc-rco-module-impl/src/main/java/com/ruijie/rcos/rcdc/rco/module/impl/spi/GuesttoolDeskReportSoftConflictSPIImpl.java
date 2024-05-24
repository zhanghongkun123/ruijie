package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUamAppDiskVersionRelativeDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUamDeliveryAppEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUamDeliveryAppService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DeskInstallSoftDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/07/26
 *
 * @author ting
 */
@DispatcherImplemetion(GuestToolCmdId.REPORT_SOFT_CONFLICT)
public class GuesttoolDeskReportSoftConflictSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private final static Logger LOGGER = LoggerFactory.getLogger(GuesttoolDeskReportSoftConflictSPIImpl.class);

    private static final String SPLIT = "、";

    private static final String ALARM_BUSINESS_ID_PRE = "SOFT_CONFLICT_";

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private CbbDeskMgmtAPI deskMgmtAPI;

    @Autowired
    private ViewUamAppDiskVersionRelativeDesktopDAO appDiskRelateDeskDAO;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private ViewUamDeliveryAppService viewUamDeliveryAppService;


    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");
        LOGGER.info("收到桌面应用磁盘软件冲突告警:{}", JSON.toJSONString(request));

        final CbbGuesttoolMessageDTO messageDTO = request.getDto();
        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.GT_GET_USER_PWD_CMD_ID));
        dto.setPortId(messageDTO.getPortId());
        dto.setDeskId(messageDTO.getDeskId());

        GuesttoolMessageContent body = convertBodyFor(messageDTO.getBody());
        JSONObject jsonObject = (JSONObject) JSON.toJSON(body.getContent());
        List<DeskInstallSoftDTO> deskSoftList = JSON.parseArray(jsonObject.getString(Constants.INSTALL_SOFT_LIST), DeskInstallSoftDTO.class);

        UUID deskId = messageDTO.getDeskId();
        CbbDeskDTO deskInfo = deskMgmtAPI.getDeskById(deskId);

        boolean isDeskInTest = cbbUamAppTestTargetAPI.existResourceInTest(AppResourceTypeEnum.CLOUD_DESKTOP, deskId);
        if (isDeskInTest) {
            LOGGER.warn("桌面[{}]正在应用测试中，不记录告警", deskId);
            return dto;
        }

        String appName = obtainDeskRelateUamApp(deskId);
        if (StringUtils.isEmpty(appName)) {
            LOGGER.warn("桌面[{}]无应用磁盘，不记录告警", deskId);
            return dto;
        }

        String conflictSoftNames = buildConflictSoftMessage(deskSoftList);
        saveAlarm(conflictSoftNames, deskId, deskInfo, appName);

        return dto;
    }

    private String obtainDeskRelateUamApp(UUID deskId) throws BusinessException {
        boolean existAppDisk = cbbAppDeliveryMgmtAPI.existAppDisk(deskId);
        if (!existAppDisk) {
            LOGGER.warn("桌面[{}]未应用应用磁盘，不记录告警", deskId);
            return StringUtils.EMPTY;
        }

        List<CbbUamDeliveryGroupDTO> dtoList = cbbAppDeliveryMgmtAPI.listByCloudDesktopId(deskId);
        if (CollectionUtils.isEmpty(dtoList)) {
            LOGGER.warn("桌面[{}]无关联应用交互组，不记录告警", deskId);
            return StringUtils.EMPTY;
        }

        for (CbbUamDeliveryGroupDTO deliveryGroupDTO : dtoList) {
            CbbUamDeliveryGroupDetailDTO deliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupDTO.getId());
            if (deliveryGroupDetail.getAppDeliveryType() == AppDeliveryTypeEnum.APP_DISK) {
                // 当前桌面仅支持应用一个应用磁盘
                List<ViewUamDeliveryAppEntity> appList = viewUamDeliveryAppService.findAppListByGroupId(deliveryGroupDTO.getId());
                return CollectionUtils.isEmpty(appList) ? StringUtils.EMPTY : appList.get(0).getAppName();
            }
        }

        return StringUtils.EMPTY;
    }


    private String buildConflictSoftMessage(List<DeskInstallSoftDTO> deskSoftList) {
        if (deskSoftList.isEmpty()) {
            return StringUtils.EMPTY;
        }

        StringBuilder conflictSoftNameBuilder = new StringBuilder();
        for (DeskInstallSoftDTO soft : deskSoftList) {
            conflictSoftNameBuilder.append(SPLIT).append(soft.getDisplayName());
        }
        return conflictSoftNameBuilder.substring(1);
    }

    private void saveAlarm(String conflictSoftNames, UUID deskId, CbbDeskDTO deskInfo, String appName) {
        SaveAlarmRequest alarmRequest = new SaveAlarmRequest();
        alarmRequest.setAlarmCode(buildAlarmCode(deskId));
        alarmRequest.setAlarmTime(new Date());
        alarmRequest.setAlarmLevel(AlarmLevel.WARN);
        alarmRequest.setAlarmName(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_SOFT_CONFLICT_ALARM_NAME));
        alarmRequest.setAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_DESK_SOFT_CONFLICT,
                new String[] {appName, deskInfo.getName(), conflictSoftNames}));
        alarmRequest.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
        alarmRequest.setEnableSendMail(false);
        alarmRequest.setBusinessId(buildAlarmBusinessId(deskId));
        baseAlarmAPI.saveAlarm(alarmRequest);
    }


    private String buildAlarmCode(UUID deskId) {
        return BusinessKey.RCDC_CLOUDDESKTOP_DESK_SOFT_CONFLICT_ALARM_CODE + deskId;
    }

    private String buildAlarmBusinessId(UUID deskId) {
        return ALARM_BUSINESS_ID_PRE + deskId;
    }


    /**
     * 将json格式字符串body转对象处理
     *
     * @param bodyStr body字符串
     * @return 转换后的对象
     */
    protected final GuesttoolMessageContent convertBodyFor(String bodyStr) {
        Assert.hasText(bodyStr, "body string is not null");

        return JSON.parseObject(bodyStr, new TypeReference<GuesttoolMessageContent>() {
            //
        });
    }
}
