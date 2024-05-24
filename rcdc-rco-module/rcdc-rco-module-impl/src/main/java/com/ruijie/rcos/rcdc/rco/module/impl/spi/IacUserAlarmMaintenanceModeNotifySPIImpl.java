package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQueryUserListByTypePageDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Description: 企金2.0版本用户名称默认为20个字节，对长度超过20位的进行告警
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/19 9:36
 *
 * @author tangxu
 */
@DispatcherImplemetion("IacUserAlarmMaintenanceModeNotifySPIImpl")
public class IacUserAlarmMaintenanceModeNotifySPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(IacUserAlarmMaintenanceModeNotifySPIImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    private static final int QUERY_PAGE_SIZE = 1000;

    private static final int USER_NAME_MAX_SIZE = 20;

    @Override
    public Boolean beforeEnteringMaintenance(String dispatchKey, BaseUpgradeDTO upgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String dispatchKey, BaseUpgradeDTO upgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String dispatchKey, BaseUpgradeDTO upgradeDTO) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterMaintenanceEnd(String dispatchKey, BaseUpgradeDTO upgradeDTO) throws BusinessException {
        String parameter = globalParameterService.findParameter(Constants.NEED_CHECK_USER_NAME_AND_ALARM);
        if (StringUtils.isEmpty(parameter) || !Boolean.valueOf(parameter)) {
            LOGGER.info("未找到[{}]配置项或配置项为false", Constants.NEED_CHECK_USER_NAME_AND_ALARM);
            return Boolean.TRUE;
        }
        LOGGER.info("开始用户名称长度告警轮询");
        List<IacUserTypeEnum> iacUserTypeEnumList = Arrays.asList(IacUserTypeEnum.class.getEnumConstants()).stream().collect(Collectors.toList());
        iacUserTypeEnumList.stream().forEach(item -> {
            try {
                // 按用户类型分页查询用户信息
                int page = 0;
                List<IacUserDetailDTO> cbbUserList = new ArrayList<>();
                while (true) {
                    IacQueryUserListByTypePageDTO request = new IacQueryUserListByTypePageDTO();
                    request.setPage(page);
                    request.setLimit(QUERY_PAGE_SIZE);
                    request.setUserType(item);
                    IacPageResponse<IacUserDetailDTO> pageResponse = iacUserMgmtAPI.pageQueryUserListByUserType(request);
                    IacUserDetailDTO[] itemArr = pageResponse.getItemArr();
                    if (ArrayUtils.isEmpty(itemArr)) {
                        break;
                    }
                    page++;
                    cbbUserList.addAll(Arrays.asList(itemArr));
                }
                // 长度名称大于20为的进行告警
                cbbUserList.stream().forEach(temp -> {
                    if (temp.getUserName().length() > USER_NAME_MAX_SIZE) {
                        SaveAlarmRequest request = new SaveAlarmRequest();
                        request.setAlarmCode(StringUtils.join(AlarmConstants.ALARM_TYPE_RCDC_SERVICE, temp.getUserName()));
                        request.setAlarmName(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_NAME_IS_TOO_LONG));
                        request.setAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_NAME_IS_TOO_LONG_NOT_ALLOW_USED_MULTI_DESKTOP,
                                temp.getUserName()));
                        request.setAlarmType(AlarmConstants.ALARM_TYPE_RCDC_SERVICE);
                        request.setEnableSendMail(true);
                        request.setAlarmLevel(AlarmLevel.WARN);
                        request.setAlarmTime(new Date());
                        baseAlarmAPI.saveAlarm(request);
                    }
                });
            } catch (BusinessException ex) {
                LOGGER.error("查询[{}]类型用户信息异常", item, ex);
            }
        });
        LOGGER.info("用户名称长度告警轮询结束");
        globalParameterService.updateParameter(Constants.NEED_CHECK_USER_NAME_AND_ALARM, Boolean.FALSE.toString());
        return Boolean.TRUE;
    }
}
