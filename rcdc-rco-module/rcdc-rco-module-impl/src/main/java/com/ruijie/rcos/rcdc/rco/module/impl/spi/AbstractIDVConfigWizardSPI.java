package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IDVCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserTerminalRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.IdvTerminalGroupDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.IDVTerminalReportConfigWizardDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopConfigService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: IDV终端配置向导数据上报抽象类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/3
 *
 * @author brq
 */
public abstract class AbstractIDVConfigWizardSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIDVConfigWizardSPI.class);

    @Autowired
    private IacUserMgmtAPI userAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserDesktopConfigService userDesktopConfigService;

    @Autowired
    private UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private static final String RCDC_TERMINALGROUP_GROUP_NOT_EXIST = "23260052";


    protected boolean checkTerminal(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto) {
        TerminalDTO terminalDTO = null;

        try {
            terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());

            Assert.notNull(terminalDTO, "terminalDTO is null!");
        } catch (BusinessException e) {
            LOGGER.error("terminal not exist, terminal id={}", request.getTerminalId());
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            return true;
        }

        LOGGER.info("获取终端信息，判断是否是IDV终端，entity={}", JSONObject.toJSONString(terminalDTO));
        // 根据用户已经上报的终端类型来确定
        if (terminalDTO.getPlatform() != CbbTerminalPlatformEnums.IDV && terminalDTO.getPlatform() != CbbTerminalPlatformEnums.VOI) {
            // 非IDV|VOI终端，不允许进行用户绑定
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_VOIANDIDV_NOT_ALLOW_BIND);
            return true;
        }

        // 终端未绑定任何桌面，此时终端模式、绑定的用户、桌面id都为null
        if (null == terminalDTO.getBindDeskId()) {
            return false;
        }

        // 终端已经存在绑定的云桌面
        return terminalExistBindDesk(request, requestDto, terminalDTO);
    }

    private boolean terminalExistBindDesk(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto, TerminalDTO terminalDTO) {
        LOGGER.info("检查终端，终端{}存在绑定的桌面", terminalDTO.getId());
        if (requestDto.getIdvTerminalMode() != terminalDTO.getIdvTerminalMode()) {
            // 终端存在绑定的桌面，判断终端模式是否一致，如果不一致，返回提示，需要先进行终端初始化操作
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_MODE_CAN_NOT_MODIFY);
            return true;
        }

        if (IdvTerminalModeEnums.PUBLIC == terminalDTO.getIdvTerminalMode()) {
            return publicTerminal(request, terminalDTO);
        }

        // 判断当前终端已绑定其他用户或者其他桌面
        return personalTerminal(request, requestDto, terminalDTO);
    }

    private boolean personalTerminal(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto, TerminalDTO terminalDTO) {
        if (!requestDto.getUserName().equals(terminalDTO.getBindUserName())) {
            LOGGER.info("检查终端，用户{}已经和终端{}存在绑定关系", requestDto.getUserName(), terminalDTO.getId());
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_HAS_BIND_RELATION);
            return true;
        }

        // 根据用户名获取用户信息
        IacUserDetailDTO cbbUserInfoDTO = getUserEntity(request, requestDto.getUserName());
        return isNeedCheckUserBindRelation(request, cbbUserInfoDTO);
    }

    private boolean publicTerminal(CbbDispatcherRequest request, TerminalDTO terminalDTO) {
        // 删除公用桌面，重置终端和桌面的绑定关系
        UserTerminalRequest userTerminalRequest = new UserTerminalRequest();
        userTerminalRequest.setTerminalId(request.getTerminalId());
        try {
            userTerminalMgmtAPI.editTerminalSetting(userTerminalRequest);
            cbbIDVDeskMgmtAPI.deleteDeskIDV(terminalDTO.getBindDeskId());
            LOGGER.error("取消公用终端原有绑定关系成功, terminalId={}", request.getTerminalId());
        } catch (BusinessException e) {
            LOGGER.error("取消公用终端原有绑定关系发生异常, terminalId={}", request.getTerminalId());
            responseErrorMessage(request, ConfigWizardForIDVCode.CANCEL_PUBLIC_TERMINAL_BIND_RELATION_ERROR);
            return true;
        }
        return false;
    }

    protected IacUserDetailDTO getUserEntity(CbbDispatcherRequest request, String userName) {
        IacUserDetailDTO cbbUserInfoDTO = null;
        try {
            cbbUserInfoDTO = userAPI.getUserByName(userName);

        } catch (BusinessException e) {
            LOGGER.error("getUserByName error", e);
            // 用户信息为空时，返回null
            return null;
        }
        if (cbbUserInfoDTO == null) {
            LOGGER.info("数据库中不存在用户[{}]", userName);
            responseErrorMessage(request, ConfigWizardForIDVCode.USER_NOT_EXIST);
            // 用户信息为空时，返回null
            return null;
        }
        return cbbUserInfoDTO;
    }

    protected abstract boolean isNeedCheckUserBindRelation(CbbDispatcherRequest request, IacUserDetailDTO cbbUserInfoDTO);

    protected void responseSuccessMessage(CbbDispatcherRequest request, IDVCloudDesktopDTO idvCloudDesktopDTO) {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        LOGGER.info("应答IDV终端配置向导成功报文：terminalId={}, object={}", request.getTerminalId(), JSONObject.toJSONString(idvCloudDesktopDTO));

        CbbResponseShineMessage<IDVCloudDesktopDTO> shineMessage = new CbbResponseShineMessage<>();
        shineMessage.setCode(ConfigWizardForIDVCode.SUCCESS);
        shineMessage.setRequestId(request.getRequestId());
        shineMessage.setTerminalId(request.getTerminalId());
        shineMessage.setAction(request.getDispatcherKey());
        shineMessage.setContent(idvCloudDesktopDTO);
        messageHandlerAPI.response(shineMessage);
    }

    protected void responseErrorMessage(CbbDispatcherRequest request, int responseCode) {
        Assert.notNull(request, "CbbDispatcherRequest can not null");
        Assert.notNull(responseCode, "resultCode can not null");
        LOGGER.info("应答IDV终端配置向导失败报文：terminalId={}, responseCode={}", request.getTerminalId(), responseCode);

        CbbResponseShineMessage<Object> shineMessage = new CbbResponseShineMessage();
        shineMessage.setCode(responseCode);
        shineMessage.setRequestId(request.getRequestId());
        shineMessage.setTerminalId(request.getTerminalId());
        shineMessage.setAction(request.getDispatcherKey());
        shineMessage.setContent(new Object());
        messageHandlerAPI.response(shineMessage);
    }

    /**
     * 个人部署模式处理
     *
     * @param request request
     * @param requestDto requestDto
     * @param idvCloudDesktopDTO idvCloudDesktopDTO
     * @return 是否应答过SHINE。如果应答过，则上层逻辑不需要继续往下走
     */
    protected boolean personalModeProcess(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto,
            IDVCloudDesktopDTO idvCloudDesktopDTO) {
        // 根据用户名获取用户信息
        IacUserDetailDTO cbbUserInfoDTO = getUserEntity(request, requestDto.getUserName());
        if (cbbUserInfoDTO == null) {
            return true;
        }

        if (cbbUserInfoDTO.getUserState() == IacUserStateEnum.DISABLE) {
            responseErrorMessage(request, ConfigWizardForIDVCode.USER_STATE_DISABLE);
            return true;
        }
        //查询 终端的信息
        CbbTerminalBasicInfoDTO terminalDTO = null;
        try {
            terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            LOGGER.info("查询到终端信息{}",terminalDTO);  //排查SPI阻塞日志
        } catch (BusinessException e) {
            LOGGER.info("无法查询到终端信息{}",request.getTerminalId());
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            return true;
        }

        // 不支持TC引导的VOI，不能使用WIN7-32镜像
        if (!checkUserSupportWin732(request, requestDto)) {
            LOGGER.info("终端[{}]型号为[{}]，不支持TC引导，不允许使用WIN7-32镜像", terminalDTO.getTerminalName(), terminalDTO.getProductType());
            responseErrorMessage(request, ConfigWizardForIDVCode.NOT_SUPPORT_TC_CAN_NOT_USER_WIN7_32);
            return true;
        }

        // 用户桌面信息
        UserDesktopConfigDTO userDesktopConfigDTO = null;
        //
        if (terminalDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.IDV) {
            userDesktopConfigDTO = userDesktopConfigService.getUserDesktopConfig(cbbUserInfoDTO.getId(), UserCloudDeskTypeEnum.IDV);
        } else if (terminalDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.VOI) {
            userDesktopConfigDTO = userDesktopConfigService.getUserDesktopConfig(cbbUserInfoDTO.getId(), UserCloudDeskTypeEnum.VOI);
        }
        LOGGER.info("查询到用户桌面配置信息{}",userDesktopConfigDTO);  //排查SPI阻塞日志
        // 判断用户与桌面关系配置表 用户策略为空 用户镜像为空 说明都未开启IDV|VOI特性
        if (userDesktopConfigDTO == null || userDesktopConfigDTO.getStrategyId() == null || userDesktopConfigDTO.getImageTemplateId() == null) {
            // 终端的信息上来的请求是IDV 但是视图终端-用户查询为空 提示用户未开启IDV特性
            if (terminalDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.IDV) {
                LOGGER.info("当前用户[{}]没有开启IDV桌面", requestDto.getUserName());
                responseErrorMessage(request, ConfigWizardForIDVCode.USER_NOT_OPEN_IDV_CONFIG);
            } else if (terminalDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.VOI) {
                // 上报上来的请求是VOI 但是查询为空 提示用户未开启VOI特性
                LOGGER.info("当前用户[{}]没有开启VOI桌面", requestDto.getUserName());
                responseErrorMessage(request, ConfigWizardForIDVCode.USER_NOT_OPEN_VOI_CONFIG);
            }
            return true;
        }
        // 判断当前用户是否已经绑定其他IDV终端
        if (isNeedCheckUserBindRelation(request, cbbUserInfoDTO)) {
            return true;
        }

        idvCloudDesktopDTO.setUserId(cbbUserInfoDTO.getId());
        idvCloudDesktopDTO.setUserName(cbbUserInfoDTO.getUserName());
        idvCloudDesktopDTO.setStrategyId(userDesktopConfigDTO.getStrategyId());
        idvCloudDesktopDTO.setImageId(userDesktopConfigDTO.getImageTemplateId());
        idvCloudDesktopDTO.setUserProfileStrategyId(userDesktopConfigDTO.getUserProfileStrategyId());
        idvCloudDesktopDTO.setSoftwareStrategyId(userDesktopConfigDTO.getSoftwareStrategyId());
        return false;
    }

    /**
     * 校验用户绑定的镜像是否支持TCI
     *
     * @param request 请求
     * @param requestDto 请求信息
     * @return 是否支持
     */
    protected boolean checkUserSupportWin732(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto) {
        TerminalDTO terminalDTO;

        // 若支持TC引导，则不限制系统类型
        if (Boolean.TRUE.equals(requestDto.getCanSupportTC())) {
            return true;
        }

        try {
            terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
            Assert.notNull(terminalDTO, "terminalDTO must not null!");
        } catch (BusinessException ex) {
            LOGGER.error(String.format("终端[%s]不存在，ex=", request.getTerminalId()), ex);
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            return false;
        }

        // 如果不是VOI，则不限制系统类型
        if (terminalDTO.getPlatform() != CbbTerminalPlatformEnums.VOI) {
            return true;
        }

        // 获取用户信息
        IacUserDetailDTO cbbUserInfoDTO = getUserEntity(request, requestDto.getUserName());
        if (cbbUserInfoDTO == null) {
            LOGGER.error("用户名称[%s]查询用户信息为空", requestDto.getUserName());
            return false;
        }

        // 获取用户绑定镜像信息
        UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigService.getUserDesktopConfig(cbbUserInfoDTO.getId(), UserCloudDeskTypeEnum.VOI);
        if (userDesktopConfigDTO == null || userDesktopConfigDTO.getImageTemplateId() == null) {
            LOGGER.error("用户[{}]对应VOI桌面信息为空", cbbUserInfoDTO.getId());
            return true;
        }

        // 获取镜像类型
        CbbGetImageTemplateInfoDTO imageTemplateInfo;
        try {
            imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(userDesktopConfigDTO.getImageTemplateId());
        } catch (BusinessException ex) {
            LOGGER.error(String.format("通过镜像标识[%s]查询镜像记录不存在", userDesktopConfigDTO.getImageTemplateId()), ex);
            return false;
        }

        if (imageTemplateInfo.getCbbOsType() == CbbOsType.WIN_7_32) {
            LOGGER.info("终端[{}]不支持TC引导，并且使用WIN7-32镜像", terminalDTO.getTerminalName());
            return false;
        }

        return true;
    }

    /**
     * 公共部署模式处理
     *
     * @param request request
     * @param requestDto requestDto
     * @param idvCloudDesktopDTO idvCloudDesktopDTO
     * @return 是否应答过SHINE。如果应答过，则上层逻辑不需要继续往下走
     */
    protected boolean publicModeProcess(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto,
            IDVCloudDesktopDTO idvCloudDesktopDTO) {
        // 判断终端分组是否存在
        try {
            IdvTerminalGroupDetailResponse detailResponse = userTerminalGroupMgmtAPI.idvTerminalGroupDetail(requestDto.getTerminalGroupId());
            if (null == detailResponse || null == detailResponse.getId()) {
                responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_GROUP_NOT_EXIST);
                return true;
            }
            // 查询 终端的信息
            CbbTerminalBasicInfoDTO terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            LOGGER.info("终端的信息，terminalDTO=[{}]", JSONObject.toJSONString(terminalDTO));
            if (CbbTerminalPlatformEnums.IDV == terminalDTO.getTerminalPlatform()) {
                // 判断终端组是否开启IDV特性
                if (null == detailResponse.getIdvDesktopImageId() || null == detailResponse.getIdvDesktopStrategyId()) {
                    LOGGER.error("当前终端组[{}]没有开启IDV特性", requestDto.getTerminalGroupId());
                    responseErrorMessage(request, ConfigWizardForIDVCode.GROUP_NOT_OPEN_IDV_CONFIG);
                    return true;
                }
                idvCloudDesktopDTO.setTerminalGroupId(requestDto.getTerminalGroupId());
                idvCloudDesktopDTO.setStrategyId(detailResponse.getIdvDesktopStrategyId());
                idvCloudDesktopDTO.setImageId(detailResponse.getIdvDesktopImageId());
                idvCloudDesktopDTO.setUserProfileStrategyId(detailResponse.getIdvUserProfileStrategyId());
            } else if (CbbTerminalPlatformEnums.VOI == terminalDTO.getTerminalPlatform()) {
                // 判断终端组是否开启VOI特性
                if (null == detailResponse.getVoiDesktopImageId() || null == detailResponse.getVoiDesktopStrategyId()) {
                    LOGGER.error("当前终端组[{}]没有开启VOI特性", requestDto.getTerminalGroupId());
                    responseErrorMessage(request, ConfigWizardForIDVCode.GROUP_NOT_OPEN_VOI_CONFIG);
                    return true;
                }
                // VOI终端是否支持WIN7-32镜像
                if (!checkTerminalGroupSupportWin732(request, requestDto, detailResponse, terminalDTO)) {
                    LOGGER.info("终端[{}]型号为[{}]，不支持TC引导，不允许使用WIN7-32镜像", terminalDTO.getTerminalName(), terminalDTO.getProductType());
                    return true;
                }

                idvCloudDesktopDTO.setTerminalGroupId(requestDto.getTerminalGroupId());
                idvCloudDesktopDTO.setStrategyId(detailResponse.getVoiDesktopStrategyId());
                idvCloudDesktopDTO.setImageId(detailResponse.getVoiDesktopImageId());
                idvCloudDesktopDTO.setSoftwareStrategyId(detailResponse.getVoiSoftwareStrategyId());
                idvCloudDesktopDTO.setUserProfileStrategyId(detailResponse.getVoiUserProfileStrategyId());
            }

        } catch (BusinessException e) {
            LOGGER.error("获取终端组IDV|VOI特性失败，e=[{}]", e.getI18nMessage());
            if (e.getKey().equals(RCDC_TERMINALGROUP_GROUP_NOT_EXIST)) {
                responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_GROUP_NOT_EXIST);
                return true;
            } else if ("23260009".equals(e.getKey())) {
                LOGGER.info("无法查询到终端信息{}", request.getTerminalId());
                responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
                return true;
            }
            responseErrorMessage(request, ConfigWizardForIDVCode.CODE_ERR_OTHER);
            return true;
        }

        return false;
    }

    protected boolean checkTerminalGroupSupportWin732(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto,
            IdvTerminalGroupDetailResponse detailResponse, CbbTerminalBasicInfoDTO terminalDTO) {
        // 若支持TC引导，则不限制系统类型
        if (Boolean.TRUE.equals(requestDto.getCanSupportTC())) {
            return true;
        }

        CbbGetImageTemplateInfoDTO imageTemplateInfo;
        try {
            imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(detailResponse.getVoiDesktopImageId());
        } catch (BusinessException e) {
            LOGGER.error(String.format("通过镜像标识[%s]查询镜像记录不存在", detailResponse.getVoiDesktopImageId()), e);
            return false;
        }

        // 镜像为WIN7-32
        if (imageTemplateInfo.getCbbOsType() == CbbOsType.WIN_7_32) {
            LOGGER.info("终端组[{}]绑定的VOI镜像类型为[{}]，终端[{}]不支持TC引导，并且使用WIN7-32镜像", detailResponse.getName(), imageTemplateInfo.getImageName(),
                    terminalDTO.getTerminalName());
            responseErrorMessage(request, ConfigWizardForIDVCode.NOT_SUPPORT_TC_CAN_NOT_USER_WIN7_32);
            return false;
        }

        return true;
    }
}
