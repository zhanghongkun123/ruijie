package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseExternalAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.SaveAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmStatus;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.AlarmConstants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskext.CbbDetailDeskGtInfo;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskext.CbbGtModuleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbGtAgentState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.util.GtAgentUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopGuestToolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopGuestToolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopGuestToolService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 云桌面GT管理API接口实现.
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author linrenjian
 */
public class DesktopGuestToolMgmtAPIImpl implements DesktopGuestToolMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopGuestToolMgmtAPIImpl.class);


    @Autowired
    private BaseAlarmAPI baseAlarmAPI;

    @Autowired
    private UserDesktopGuestToolService userDesktopGuestToolService;


    @Override
    public void releaseAlarmAndCreateAlarm() throws BusinessException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("解除告警新增告警");
        }
        Set<UUID> deskIdSet = new HashSet<>();
        // 查询GT 离线告警列表
        List<AlarmDTO> alarmDTOList =
                allListGtOffLineAlarm(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_GUESTTOOL_STATUS_EXCEPT_ALARM_NAME));
        Date nowTime = new Date();
        if (CollectionUtils.isNotEmpty(alarmDTOList)) {
            // 查询产生告警的桌面ID
            UUID[] uuidArr = alarmDTOList.stream()
                    .map(alarmDTO -> UUID.fromString(alarmDTO.getAlarmCode().replace(Constants.GUESSTOOL_STATUS_OFFLINE, ""))).toArray(UUID[]::new);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("产生告警的桌面信息[{}]", uuidArr);
            }
            List<UserDesktopGuestToolDTO> allOnlineVDIDeskByInDeskIdList = userDesktopGuestToolService.findAllOnlineVDIDeskByInDeskId(uuidArr);
            List<CbbDetailDeskGtInfo> collectList =
                    allOnlineVDIDeskByInDeskIdList.stream().map(UserDesktopGuestToolDTO::convertDetailDeskExtInfo).collect(Collectors.toList());
            for (CbbDetailDeskGtInfo dto : collectList) {
                CbbGtAgentState cbbGtAgentState = GtAgentUtil.computeExceed60Min(nowTime, dto);
                // 如果检测正常 则解除告警
                if (CbbGtAgentState.NORMAL == cbbGtAgentState) {
                    releaseAlarm(Constants.GUESSTOOL_STATUS_OFFLINE + dto.getId());
                } else {
                    //检测还是异常 则继续告警
                    addGtOffLineAlarm(dto.getId(), dto.getDesktopName());
                }
            }
            deskIdSet = new HashSet<>(Arrays.asList(uuidArr));
        }

        List<UserDesktopGuestToolDTO> allOnlineVDIDeskList = userDesktopGuestToolService.findAllOnlineVDIDesk();
        if (CollectionUtils.isNotEmpty(allOnlineVDIDeskList)) {
            // 查询在线的桌面
            List<CbbDetailDeskGtInfo> collectList =
                    allOnlineVDIDeskList.stream().map(UserDesktopGuestToolDTO::convertDetailDeskExtInfo).collect(Collectors.toList());
            for (CbbDetailDeskGtInfo dto : collectList) {
                // 如果数据已经被离线告警处理过，则不进行再次处理
                if (deskIdSet.contains(dto.getId())) {
                    continue;
                }
                CbbGtAgentState cbbGtAgentState = GtAgentUtil.computeExceed60Min(nowTime, dto);
                // 如果检测异常 则添加告警
                if (CbbGtAgentState.ERROR == cbbGtAgentState) {
                    addGtOffLineAlarm(dto.getId(), dto.getDesktopName());
                }
            }
        }

        // 模块告警处理
        moduleAlarm(allOnlineVDIDeskList);
    }

    /**
     * 解除告警
     * 
     * @param deskId 桌面ID
     */
    @Override
    public void releaseGuestToolAlarm(UUID deskId) {
        Assert.notNull(deskId, "deskId is not null");
        releaseAlarm(Constants.GUESSTOOL_STATUS_OFFLINE + deskId);
        releaseAlarm(Constants.GUESSTOOL_MODULE_STATUS_ERROR + deskId);
    }


    /**
     * 新增 GT 离线告警异常
     * 
     * @param deskId 桌面ID
     * @param deskTopName 桌面名称
     */
    private void addGtOffLineAlarm(UUID deskId, String deskTopName) {
        Assert.notNull(deskId, "deskId cannot empty");
        SaveAlarmRequest alarmRequest = new SaveAlarmRequest();

        alarmRequest.setAlarmCode(Constants.GUESSTOOL_STATUS_OFFLINE + deskId);
        alarmRequest.setAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_GUESTTOOL_STATUS_EXCEPT_ALARM_CONTENT, deskTopName));
        alarmRequest.setAlarmLevel(AlarmLevel.WARN);
        alarmRequest.setAlarmName(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_GUESTTOOL_STATUS_EXCEPT_ALARM_NAME));
        alarmRequest.setAlarmTime(new Date());
        alarmRequest.setAlarmType(AlarmConstants.ALARM_TYPE_DESKTOP);
        alarmRequest.setEnableSendMail(true);
        baseAlarmAPI.saveAlarm(alarmRequest);
    }

    private void moduleAlarm(List<UserDesktopGuestToolDTO> allOnlineVDIDeskList) throws BusinessException {
        Set<UUID> deskIdSet = new HashSet<>();
        // 查询模块告警列表
        List<AlarmDTO> alarmList =
                allListGtOffLineAlarm(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_GUESTTOOL_MODULE_STATUS_ALARM_NAME));
        // 1.解除告警 如果异常还在则添追加告警
        if (CollectionUtils.isNotEmpty(alarmList)) {
            // 查询产生告警的桌面ID
            UUID[] uuidArr = alarmList.stream().map(alarmDTO -> UUID.fromString(alarmDTO.getAlarmCode()
                    .replace(Constants.GUESSTOOL_MODULE_STATUS_ERROR, ""))).toArray(UUID[]::new);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("产生模块告警的桌面id列表[{}]", JSON.toJSONString(uuidArr));
            }
            List<UserDesktopGuestToolDTO> allOnlineDeskAlarmList = userDesktopGuestToolService.findAllOnlineVDIDeskByInDeskId(uuidArr);
            if (CollectionUtils.isEmpty(allOnlineDeskAlarmList)) {
                return;
            }

            for (UserDesktopGuestToolDTO desktopGuestTool : allOnlineDeskAlarmList) {
                List<CbbGtModuleDTO> moduleList = desktopGuestTool.getModuleList();
                // 模块信息为空,不处理
                if (CollectionUtils.isEmpty(moduleList)) {
                    continue;
                }
                List<CbbGtModuleDTO> moduleAlarmList = handleModuleAlarmList(desktopGuestTool, moduleList);
                // 解除告警
                if (CollectionUtils.isEmpty(moduleAlarmList)) {
                    releaseAlarm(Constants.GUESSTOOL_MODULE_STATUS_ERROR + desktopGuestTool.getCbbDesktopId());
                }
            }
            deskIdSet = new HashSet<>(Arrays.asList(uuidArr));
        }

        // 2.新增告警 排除已经添加告警的桌面
        if (CollectionUtils.isEmpty(allOnlineVDIDeskList)) {
            return;
        }

        for (UserDesktopGuestToolDTO desktopGuestTool : allOnlineVDIDeskList) {
            // 如果数据已经被解除告警处理过，则不进行再次处理
            if (deskIdSet.contains(desktopGuestTool.getCbbDesktopId())) {
                continue;
            }

            List<CbbGtModuleDTO> moduleList = desktopGuestTool.getModuleList();
            // 模块信息为空,不告警
            if (CollectionUtils.isEmpty(moduleList)) {
                continue;
            }
            handleModuleAlarmList(desktopGuestTool, moduleList);
        }
    }

    private void releaseAlarm(String alarmCode) {
        ReleaseExternalAlarmRequest alarmRequest = new ReleaseExternalAlarmRequest();
        alarmRequest.setAlarmStatus(AlarmStatus.AUTO_RELEASED);
        alarmRequest.setAlarmCode(alarmCode);
        alarmRequest.setAlarmType(AlarmConstants.ALARM_TYPE_DESKTOP);
        try {
            baseAlarmAPI.releaseExternalAlarm(alarmRequest);
        } catch (BusinessException e) {
            LOGGER.error("解除GT告警[{}]的时候出现异常:", alarmCode, e);
        }
    }

    private List<CbbGtModuleDTO> handleModuleAlarmList(UserDesktopGuestToolDTO desktopGuestTool, List<CbbGtModuleDTO> moduleList) {
        List<CbbGtModuleDTO> moduleAlarmList =
                moduleList.stream().filter(module -> CbbGtAgentState.NORMAL != module.getState()).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(moduleAlarmList)) {
            addModuleAlarm(desktopGuestTool, moduleAlarmList);
        }
        return moduleAlarmList;
    }

    private void addModuleAlarm(UserDesktopGuestToolDTO desktopGuestTool, List<CbbGtModuleDTO> alarmList) {
        List<String> valueList = new ArrayList<>();
        for (CbbGtModuleDTO module : alarmList) {
            valueList.add(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_GUESTTOOL_MODULE_STATUS_ERROR,
                    LocaleI18nResolver.resolve(module.getName().getDescribe()),
                    LocaleI18nResolver.resolve(module.getState().getDescribe())));
        }

        SaveAlarmRequest alarmRequest = new SaveAlarmRequest();
        alarmRequest.setAlarmCode(Constants.GUESSTOOL_MODULE_STATUS_ERROR + desktopGuestTool.getCbbDesktopId());
        alarmRequest.setAlarmContent(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_GUESTTOOL_MODULE_STATUS_ALARM_CONTENT,
                desktopGuestTool.getDesktopName(), StringUtils.join(valueList, Constants.CAESURA)));
        alarmRequest.setAlarmLevel(AlarmLevel.WARN);
        alarmRequest.setAlarmName(LocaleI18nResolver.resolve(BusinessKey.RCDC_CLOUDDESKTOP_GUESTTOOL_MODULE_STATUS_ALARM_NAME));
        alarmRequest.setAlarmTime(new Date());
        alarmRequest.setAlarmType(AlarmConstants.ALARM_TYPE_DESKTOP);
        alarmRequest.setEnableSendMail(true);
        baseAlarmAPI.saveAlarm(alarmRequest);
    }


    /**
     * 全部GT离线告警列表
     *
     * @return 告警列表
     * @throws BusinessException
     */
    private List<AlarmDTO> allListGtOffLineAlarm(String searchKeyword) throws BusinessException {
        ListAlarmRequest apiRequest = new ListAlarmRequest();
        // 初始化分页0
        int page = 0;
        apiRequest.setPage(page);
        // 不查询归档的历史告警
        apiRequest.setEnableQueryHistory(false);
        // 查询 未释放告警
        apiRequest.setAlarmStatusArr(new AlarmStatus[]{AlarmStatus.NOT_RELEASED});

        apiRequest.setSearchKeyword(searchKeyword);
        // 每页最大1000
        apiRequest.setLimit(1000);
        // 缓存告警集合
        List<AlarmDTO> alarmList = new ArrayList<>();
        while (true) {
            DefaultPageResponse<AlarmDTO> pageResponse = baseAlarmAPI.listAlarmList(apiRequest);
            AlarmDTO[] itemArr = pageResponse.getItemArr();
            if (ArrayUtils.isEmpty(itemArr)) {
                return alarmList;
            }
            // 页码数量自增
            apiRequest.setPage(++page);
            alarmList.addAll(Arrays.asList(itemArr));

        }
    }

}
