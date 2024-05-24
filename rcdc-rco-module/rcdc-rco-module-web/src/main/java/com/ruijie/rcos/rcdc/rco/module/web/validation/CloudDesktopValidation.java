package com.ruijie.rcos.rcdc.rco.module.web.validation;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkIpPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.DeskStrategyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common.UserConfigHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.ResetCloudDeskMacRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop.*;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 
 * Description: Function Description 
 * Copyright: Copyright (c) 2018 Company:
 * Ruijie Co., Ltd. Create Time: 2019年1月7日
 * 
 * @author artom
 */
@Service
public class CloudDesktopValidation {

    private static final double ZONE = 0.0;

    private static final Integer MEMORY_MIN = 1;

    private static final Integer MEMORY_MAX = 256;

    private static final Double MEMORY_STEP = 0.5;

    private static final Integer PERSON_DISK_MIN = 20;

    private static final Integer PERSON_DISK_MAX = 2048;

    /**
     * MAC地址正则表达式
     */
    private static final String MAC_PATTERN = "^[A-Fa-f0-9]{2}(:[A-Fa-f0-9]{2}){5}$";

    /**
     * MAC地址分隔符
     */
    private static final String MAC_SEPARATOR = ":";

    /**
     * 组播地址的标志位
     */
    private static final char MULTICAST_SIGN = '1';

    /**
     * MAC地址为16进制
     */
    private static final int MAC_RADIX = 16;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private UserConfigHelper userConfigHelper;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private AppCenterHelper appCenterHelper;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    /**
     * @param request edit desktop request
     * @param builder builder
     * @throws BusinessException 业务异常
     */
    public void networkIpValidate(EditNeworkWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request is null");
        validateDesktopIpRange(request.getNetwork().getAddress().getIp(), request.getNetwork().getAddress().getId());
    }

    /**
     * create request validate
     * 
     * @param request request
     * @param builder builder
     * @throws BusinessException business exception
     */
    public void createValidate(CreateDesktopWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        validateDesktopIpRange(request.getVdiDesktopConfig().getNetwork().getAddress().getIp(), 
                request.getVdiDesktopConfig().getNetwork().getAddress().getId());
        userConfigHelper.validateUserVDIConfig(request.getVdiDesktopConfig());
    }

    private void validateDesktopIpRange(String ip, UUID networkId) throws BusinessException {
        if (StringUtils.isNotEmpty(ip)) {
            if (!ValidatorUtil.isIPv4Address(ip)) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_IP_INVALID);
            }
            CbbDeskNetworkDetailDTO dto = cbbNetworkMgmtAPI.getDeskNetwork(networkId);
            CbbDeskNetworkIpPoolDTO[] ipPoolArr = dto.getIpPoolArr();
            if (ipPoolArr == null || ipPoolArr.length == 0) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_NETWORK_IP_INVALID, networkId.toString());
            }
        }
    }

    /**
     * create request validate
     *
     * @param request request
     * @param builder builder
     * @throws BusinessException business exception
     */
    public void editIndependentVDIStrategyValidate(UpdateDesktopSpecWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(builder, "builder must not be null");

        Double memory = request.getMemory();

        // 内存值的范围
        if (memory < MEMORY_MIN || memory > MEMORY_MAX) {
            throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_INVALID_VDI_MEM_VALUE);
        }

        double remainder = memory % MEMORY_STEP;
        if (remainder != ZONE) {
            throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_INVALID_VDI_MEM_VALUE_LIMIT);
        }

        // 开启个人盘时，校验个人盘大小
        if (BooleanUtils.isTrue(request.getEnableChangePersonalDisk())) {
            Integer personalDisk = request.getPersonalDisk();
            if (Objects.isNull(personalDisk) || personalDisk < PERSON_DISK_MIN || personalDisk > PERSON_DISK_MAX) {
                throw new BusinessException(DeskStrategyBusinessKey.RCO_DESK_STRATEGY_INVALID_VDI_PERSON_DISK_VALUE_LIMIT);
            }
        }
    }

    /**
     * 重置mac地址校验
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    public void resetMacValidate(ResetCloudDeskMacRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request.getId() must not be null");

        validateMac(request.getMac());
    }


    /**
     * 校验mac地址是否合法
     * @param mac mac地址
     * @throws BusinessException 业务异常
     */
    private void validateMac(String mac) throws BusinessException {
        // 为Null时，表示自动，是合法值
        if (mac == null) {
            return;
        }
        // mac格式错误
        if (!Pattern.matches(MAC_PATTERN, mac)) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_ILLEGAL_MAC, mac);
        }

        String[] partArr = mac.split(MAC_SEPARATOR);
        // 不能是组播地址（第一部分转换成二进制后，最后一位为1，则为组播地址）
        String binaryString = Long.toBinaryString(Long.parseLong(partArr[0], MAC_RADIX));
        if (binaryString.charAt(binaryString.length() - 1) == MULTICAST_SIGN) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RESET_MAC_MUST_NOT_MULTICAST);
        }

        // 不能全为0
        boolean isValidateMac = Arrays.stream(partArr).anyMatch(part -> Long.parseLong(part, MAC_RADIX) != 0);
        if (!isValidateMac) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_RESET_MAC_MUST_NOT_ALL_ZERO);
        }
    }

    /**
     * 校验是否能修改为指定的镜像模板
     *
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    public void editImageValidate(EditDesktopImageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getId(), "request.getId() must not be null");
        Assert.notNull(request.getImageTemplateId(), "request.getImageTemplateId() must not be null");

        // 如果TC终端不支持TC引导，则不允许使用WIN_7_32镜像
        Boolean canSupportTc = userTerminalMgmtAPI.getSupportTcByBindDeskIdAndPlatform(request.getId(), CbbTerminalPlatformEnums.VOI);
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(request.getId());
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageTemplateId());
        // 如果模板改变为WIN_7_32类型的，并且TCI终端不支持TC引导
        if (!request.getImageTemplateId().equals(cbbDeskDTO.getImageTemplateId()) && !canSupportTc
                && imageTemplateDetail.getOsType() == CbbOsType.WIN_7_32) {
            String terminalId = userTerminalMgmtAPI.getTerminalIdByBindDeskId(request.getId());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_TCI_TERMINAL_NOT_SUPPORT_CAN_NOT_USE_WIN7_32, terminalId.toUpperCase());
        }
        //校验当前桌面是否正在应用测试中
        appCenterHelper.checkTestingDesk(request.getId());
    }

    /**
     * @param webRequest webRequest
     * @param builder    builder
     * @throws BusinessException 业务异常
     */
    public void desktopRoleValidate(EditDesktopRoleWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(webRequest.getId(), "webRequest.getId() must not be null");

        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(webRequest.getId());
        if (cbbDeskDTO.getDesktopPoolType() == DesktopPoolType.DYNAMIC) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_ROLE_DYNAMIC_POOL_NOT_SUPPORT, cbbDeskDTO.getName());
        }
    }
}
