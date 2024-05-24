package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbIDVDeskImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IDVCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserTerminalRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.IDVTerminalReportConfigWizardDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalWorkModeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTypeSupportTerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopConfigService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalAuthorizationWhitelistAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbModifyTerminalDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBizConfigDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalConnectHandlerSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbTerminalAuthRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description: IDV终端上报配置向导数据
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/1
 *
 * @author brq
 */
@DispatcherImplemetion(Constants.IDV_REPORT_CONFIG_WIZARD)
@SpiCustomThreadPoolConfig(threadPoolName = "report_config_wizard_thread_pool")
public class IDVReportConfigWizardSPIImpl extends AbstractIDVConfigWizardSPI implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(IDVReportConfigWizardSPIImpl.class);

    private static final String CREATE_IDV_ERROR_MSG = "not_available";

    private static final String DO_BIND_LOCK_PREFIX = "do_bind_lock_prefix_";

    private static final int DO_BIND_LOCK_TIME = 10;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    protected CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private UserDesktopConfigService userDesktopConfigService;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    CbbTerminalConnectHandlerSPI cbbTerminalConnectHandlerSPI;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private ImageTypeSupportTerminalService imageTypeSupportTerminalService;


    @Autowired
    private TerminalService terminalService;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        Assert.hasText(request.getData(), "报文data不能为空");

        LOGGER.info("收到IDV终端配置向导结束后上报RCDC报文:terminalId:{};data:{}", request.getTerminalId(), request.getData());

        // 1、入参校验
        JSONObject dataJson = JSONObject.parseObject(request.getData());
        IDVTerminalReportConfigWizardDTO requestDto = dataJson.toJavaObject(IDVTerminalReportConfigWizardDTO.class);

        // 2、验证终端数据是否合法，获取t_rco_user_terminal表中的终端信息
        if (checkTerminal(request, requestDto)) {
            return;
        }

        IDVCloudDesktopDTO idvCloudDesktopDTO = new IDVCloudDesktopDTO();
        idvCloudDesktopDTO.setTerminalId(request.getTerminalId());
        idvCloudDesktopDTO.setIdvTerminalMode(requestDto.getIdvTerminalMode());
        // 3、判断终端模式：个人模式or公用模式，个人模式需要判断用户信息，公用模式需要判断终端分组信息
        switch (requestDto.getIdvTerminalMode()) {
            case PERSONAL:
                personalModeProcess(request, requestDto, idvCloudDesktopDTO);
                break;
            case PUBLIC:
                publicModeProcess(request, requestDto, idvCloudDesktopDTO);
                break;
            default:
                responseErrorMessage(request, ConfigWizardForIDVCode.UN_SUPPORT_TERMINAL_MODE);
        }
    }


    protected void validateSupportOs(CbbGetImageTemplateInfoDTO imageTemplateInfo, CbbTerminalBasicInfoDTO terminalDTO) throws BusinessException {

        String productType = terminalDTO.getProductType();
        CbbImageType imageType = imageTemplateInfo.getCbbImageType();
        CbbOsType osType = imageTemplateInfo.getCbbOsType();
        if (imageTypeSupportTerminalService.hasImageSupportTerminal(productType, imageType, osType)) {
            return;
        }

        LOGGER.error("终端[{}]镜像支持检查校验不通过，终端型号[{}]，镜像类型[{}]，操作系统[{}]", terminalDTO.getTerminalId(), productType,
                imageType, osType);
        throw new BusinessException(BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TERMINAL);
    }

    @Override
    protected boolean personalModeProcess(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto,
                                          IDVCloudDesktopDTO idvCloudDesktopDTO) {
        if (!super.personalModeProcess(request, requestDto, idvCloudDesktopDTO)) {
            doBindService(request, idvCloudDesktopDTO, requestDto);
        }
        return true;
    }

    @Override
    protected boolean publicModeProcess(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto,
                                        IDVCloudDesktopDTO idvCloudDesktopDTO) {
        // 判断终端分组是否存在
        boolean hasNotifyShine = super.publicModeProcess(request, requestDto, idvCloudDesktopDTO);
        if (!hasNotifyShine) {
            try {
                modifyTerminalGroup(request, idvCloudDesktopDTO.getTerminalGroupId());
            } catch (BusinessException e) {
                LOGGER.error("移动IDV分组失败，e=[{}]", e.getI18nMessage());
                responseErrorMessage(request, ConfigWizardForIDVCode.CODE_ERR_OTHER);
                return true;
            }
            doBindService(request, idvCloudDesktopDTO, requestDto);
        }
        return true;
    }

    @Override
    protected boolean isNeedCheckUserBindRelation(CbbDispatcherRequest request, IacUserDetailDTO cbbUserInfoDTO) {
        IdWebRequest idWebRequest = new IdWebRequest();
        idWebRequest.setId(cbbUserInfoDTO.getId());

        TerminalDTO terminalDTO = userTerminalMgmtAPI.findByTerminalIdAndBindUserId(request.getTerminalId(), cbbUserInfoDTO.getId());
        if (null == terminalDTO) {
            LOGGER.info("用户{}还未存在绑定关系", cbbUserInfoDTO.getUserName());
            return false;
        }
        LOGGER.info("根据终端和绑定用户{}获取到的终端信息{}", cbbUserInfoDTO.getUserName(), JSONObject.toJSONString(terminalDTO));
        try {
            // 查询 终端的信息
            CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            LOGGER.info("查询 CbbTerminalBasicInfoDTO终端的信息，cbbTerminalBasicInfoDTO={}", JSONObject.toJSONString(cbbTerminalBasicInfoDTO));
            // 当终端的工作模式与 查询桌面的镜像类型（IDV|VOI）比对 不一样 返回错误码
            // 当前终端工作模式是IDV|VOI 并且是个人模式 （在上层已经判断） 并且桌面ID不为空 终端运行平台也不为空
            if (terminalDTO.getBindDeskId() != null && cbbTerminalBasicInfoDTO.getTerminalPlatform() != null) {
                // 查询桌面信息
                CloudDesktopDetailDTO desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(terminalDTO.getBindDeskId());
                // 桌面不为空，并且镜像的的类型也不为空
                if (desktopDetail != null && StringUtils.isNotBlank(desktopDetail.getCbbImageType())) {
                    LOGGER.info("根据终端桌面ID{}查询到桌面信息：{}", terminalDTO.getBindDeskId(), JSON.toJSONString(desktopDetail));
                    // 如果当终端的工作模式与 桌面的类型不匹配 进行细化提示给shine
                    if (!desktopDetail.getCbbImageType().equals(cbbTerminalBasicInfoDTO.getTerminalPlatform().name())) {
                        LOGGER.info("当终端的工作模式:{}与 桌面的类型:{}不匹配 提示前端进行初始化", terminalDTO.getPlatform(), desktopDetail.getCbbImageType());
                        if (cbbTerminalBasicInfoDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.IDV) {
                            // 目前终端 IDV 模式 是原先VOI云桌面 刷机后 选择了IDV
                            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_PLATFORM_VOI_TO_IDV);
                            // 返回已经绑定过 需要终端重新初始化解除绑定
                            return true;
                        } else if (cbbTerminalBasicInfoDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.VOI) {
                            // 目前终端 VOI模式 是原先IDV云桌面 刷机后 选择了VOI
                            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_PLATFORM_IDV_TO_VOI);
                            // 返回已经绑定过 需要终端重新初始化解除绑定
                            return true;
                        }
                    }
                }
            }
        } catch (BusinessException e) {
            LOGGER.error("查询终端桌面与终端信息发生异常, terminalId={}", request.getTerminalId(), e);
            responseErrorMessage(request, ConfigWizardForIDVCode.CODE_ERR_OTHER);
        }
        IDVCloudDesktopDTO idvCloudDesktopDTO = new IDVCloudDesktopDTO();
        idvCloudDesktopDTO.setDeskId(terminalDTO.getBindDeskId());
        LOGGER.info("获取用户{}和终端{}原有的绑定关系", cbbUserInfoDTO.getUserName(), terminalDTO.getId());
        responseSuccessMessage(request, idvCloudDesktopDTO);
        return true;
    }

    private void modifyTerminalGroup(CbbDispatcherRequest request, UUID groupId) throws BusinessException {

        TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
        LOGGER.info("公用终端绑定时修改终端所在分组，getTerminalResponse={}", JSONObject.toJSONString(terminalDTO));
        // 修改终端所在分组
        CbbModifyTerminalDTO terminalRequest = new CbbModifyTerminalDTO();
        terminalRequest.setCbbTerminalId(request.getTerminalId());
        terminalRequest.setGroupId(groupId);
        terminalRequest.setTerminalName(terminalDTO.getTerminalName());

        LOGGER.info("修改终端所在分组request param，terminalRequest={}", JSONObject.toJSONString(terminalRequest));
        cbbTerminalOperatorAPI.modifyTerminal(terminalRequest);
    }

    private synchronized void doBindService(CbbDispatcherRequest request, IDVCloudDesktopDTO idvCloudDesktopDTO,
            IDVTerminalReportConfigWizardDTO requestDto) {
        if (checkCpuTypeAndOsType(request, idvCloudDesktopDTO)) {
            return;
        }
        // 创建云桌面
        try {
            // 检查镜像驱动
            checkImageDriverWithTerminal(request.getTerminalId(), idvCloudDesktopDTO.getImageId());

            // 在check_upgrade中跳过授权的终端，此处需要授权
            if (!doTerminalAuth(request, requestDto.getTerminalAuthDTO())) {
                responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_AUTH_FAIL);
                return;
            }

            CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(idvCloudDesktopDTO.getImageId());

            // 查询 终端的信息
            CbbTerminalBasicInfoDTO terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            LOGGER.info("查询 终端的信息，terminalDTO={}", JSONObject.toJSONString(terminalDTO));
            validateSupportOs(imageTemplateInfo, terminalDTO);
            AtomicReference<CreateDesktopResponse> responseReference = new AtomicReference<>();
            idvCloudDesktopDTO.setTerminalMac(terminalDTO.getMacAddr());
            // 锁名称（个人终端锁用户id 防止不同终端同一时间绑定同一用户创建出来的云桌面名称重复、公用终端锁终端id）
            String localSuffix = idvCloudDesktopDTO.getIdvTerminalMode() == IdvTerminalModeEnums.PERSONAL ?
                    idvCloudDesktopDTO.getUserId().toString() : request.getTerminalId();
            // 根据终端的平台类型 创建IDV云桌面
            LockableExecutor.executeWithTryLock(DO_BIND_LOCK_PREFIX + localSuffix, () -> {
                if (terminalDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.IDV) {
                    LOGGER.info("创建IDV云桌面[{}]", JSON.toJSONString(idvCloudDesktopDTO));
                    responseReference.set(createIDV(idvCloudDesktopDTO));
                } else if (terminalDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.VOI) {
                    // 创建VOI云桌面
                    LOGGER.info("创建VOI云桌面[{}]", JSON.toJSONString(idvCloudDesktopDTO));
                    responseReference.set(createVOI(idvCloudDesktopDTO));
                }
            }, DO_BIND_LOCK_TIME);

            //设置桌面ID
            idvCloudDesktopDTO.setDeskId(responseReference.get().getId());
            // 通知UWS桌面新增
            uwsDockingAPI.notifyDesktopAdd(responseReference.get().getId());

        } catch (BusinessException e) {
            LOGGER.error("创建云桌面业务处理异常, request=[{}], e={}", JSONObject.toJSONString(request), e);
            // 在check_upgrade中跳过授权的终端，此处需要回收授权
            doCancelTerminalAuth(request.getTerminalId(), requestDto.getTerminalAuthDTO());

            if (e.getMessage().contains(CREATE_IDV_ERROR_MSG)) {
                responseErrorMessage(request, ConfigWizardForIDVCode.IMAGE_TEMPLATE_STATUS_NOT_AVAILABLE);
                return;
            } else if (BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TERMINAL.equals(e.getKey())) {
                responseErrorMessage(request, ConfigWizardForIDVCode.IMAGE_NOT_SUPPORT_TERMINAL);
                return;
            } else if (BusinessKey.RCDC_IMAGE_DRIVER_NOT_INSTALL.equals(e.getKey())) {
                responseErrorMessage(request, ConfigWizardForIDVCode.IMAGE_DRIVER_NOT_INSTALL);
                return;
            }
            responseErrorMessage(request, ConfigWizardForIDVCode.CODE_ERR_OTHER);
            return;
        }
        try {
            // 更新IDV|VOI终端和用户的绑定关系，IDV|VOI终端和云桌面的关系，设置IDV|VOI终端模式（分别处理个人模式和公共模式）
            updateUserTerminal(request.getTerminalId(), idvCloudDesktopDTO);
        } catch (BusinessException e) {
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            return;
        }

        responseSuccessMessage(request, idvCloudDesktopDTO);
    }

    private void checkImageDriverWithTerminal(String terminalId, UUID imageId) throws BusinessException {
        CbbTerminalBasicInfoDTO terminalBasicInfoResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        if (CbbTerminalPlatformEnums.VOI == terminalBasicInfoResponse.getTerminalPlatform()
                /*&& !cbbTerminalConfigAPI.isRjTerminal(terminalBasicInfoResponse.getProductType())*/) {
            LOGGER.info("TCI模式且非自研终端不需要判断驱动信息，终端信息：[{}]", terminalId);
            return;
        }
        String driverType = terminalBasicInfoResponse.getCpuType();
        // 查找已安装的镜像驱动中是否有与该终端cpu相匹配的
        CbbIDVDeskImageTemplateDTO idvDeskImageTemplate = cbbIDVDeskMgmtAPI.getIDVDeskImageTemplate(imageId);
        if (CbbOsType.WIN_XP_SP3 == idvDeskImageTemplate.getCbbImageTemplateDetailDTO().getOsType()) {
            LOGGER.info("镜像[{}]操作系统为[XP]不需要判断驱动信息", imageId);
            return;
        }

        Optional<CbbImageTemplateDriverDTO> cbbImageTemplateDriverDTOOptional = idvDeskImageTemplate.getCbbImageTemplateDriverDTOList().stream()
                .filter(driverDTO -> driverDTO.getDriverType().equals(driverType)).findFirst();

        // 未找到与cpu匹配的驱动信息
        if (!cbbImageTemplateDriverDTOOptional.isPresent()) {
            LOGGER.error("镜像[id:{}]未安装对应的cpu类型为[{}]驱动", imageId, terminalBasicInfoResponse.getCpuType());
            if (isNotSupportInstallDriverOsType(imageId)) {
                throw new BusinessException(BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TERMINAL);
            }
            throw new BusinessException(BusinessKey.RCDC_IMAGE_DRIVER_NOT_INSTALL);
        }
    }

    /**
     * 镜像操作系统是否不支持安装驱动
     *
     * @param imageTemplateId 镜像ID
     * @return true：不支持, 否则：支持
     * @throws BusinessException 业务异常
     */
    private boolean isNotSupportInstallDriverOsType(UUID imageTemplateId) throws BusinessException {
        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
        Assert.notNull(imageTemplateInfo, "imageTemplateInfo can not be null");

        LOGGER.info("镜像[{}]操作系统类型为:[{}]", imageTemplateInfo.getImageName(), imageTemplateInfo.getCbbOsType());
        return imageTemplateInfo.getCbbOsType() == CbbOsType.UOS_64;
    }

    private boolean checkCpuTypeAndOsType(CbbDispatcherRequest request, IDVCloudDesktopDTO idvCloudDesktopDTO) {
        try {
            CbbGetImageTemplateInfoDTO imageTemplateInfoResponse = cbbImageTemplateMgmtAPI.getImageTemplateInfo((idvCloudDesktopDTO.getImageId()));
            LOGGER.info("验证镜像系统类型信息, imageTemplateInfoResponse={}", JSONObject.toJSONString(imageTemplateInfoResponse));
            TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
            LOGGER.info("验证终端信息, terminalDTO={}", JSONObject.toJSONString(terminalDTO));

        } catch (BusinessException e) {
            LOGGER.error("镜像系统类型和终端CPU类型不匹配");
            responseErrorMessage(request, ConfigWizardForIDVCode.DESK_RELEASE_IMAGE_UN_SUPPORT_WITH_TERMINAL_CPU);
            return true;
        }
        return false;
    }

    private boolean checkG3Terminal(CbbDispatcherRequest request, IDVTerminalReportConfigWizardDTO requestDto) {

        TerminalDTO terminalDTO;

        try {
            terminalDTO = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
            Assert.notNull(terminalDTO, "terminalDTO is null!");
        } catch (BusinessException ex) {
            LOGGER.error(String.format("terminal not exist, terminal id=%s", request.getTerminalId()), ex);
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            return true;
        }

        // 终端是否属于 TCI 部署模式
        if (terminalDTO.getPlatform() != CbbTerminalPlatformEnums.VOI) {
            return false;
        }

        // 是否属于 G3 型号终端
        if (StringUtils.isEmpty(terminalDTO.getProductType()) || !terminalDTO.getProductType().contains("G3")) {
            return false;
        }

        // 判断镜像相关维度
        IacUserDetailDTO cbbUserInfoDTO = getUserEntity(request, requestDto.getUserName());
        if (cbbUserInfoDTO == null) {
            LOGGER.error("通过用户名称[%s]查询用户信息为空", requestDto.getUserName());
            return false;
        }
        UserDesktopConfigDTO userDesktopConfigDTO = userDesktopConfigService.getUserDesktopConfig(cbbUserInfoDTO.getId(), UserCloudDeskTypeEnum.VOI);
        if (userDesktopConfigDTO == null) {
            LOGGER.error("通过用户标识[%s]查询桌面信息为空", cbbUserInfoDTO.getId());
            return false;
        }

        CbbGetImageTemplateInfoDTO imageTemplateInfo;
        try {
            imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(userDesktopConfigDTO.getImageTemplateId());
        } catch (BusinessException ex) {
            LOGGER.error(String.format("通过镜像标识[%s]查询镜像记录不存在", userDesktopConfigDTO.getImageTemplateId()), ex);
            return false;
        }

        if (imageTemplateInfo.getCbbOsType() == CbbOsType.WIN_7_32 || imageTemplateInfo.getCbbOsType() == CbbOsType.WIN_7_64) {
            LOGGER.info("镜像标识 {} 关联 G3 终端部署 TCI 不支持 Windows 7", userDesktopConfigDTO.getImageTemplateId());
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_PLATFORM_VOI_G3_LIMIT);
            return true;
        }

        return false;
    }

    private CreateDesktopResponse createIDV(IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException {

        return userDesktopMgmtAPI.createIDV(idvCloudDesktopDTO);
    }

    private CreateDesktopResponse createVOI(IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException {

        return userDesktopMgmtAPI.createVOI(idvCloudDesktopDTO);
    }

    private void updateUserTerminal(String terminalId, IDVCloudDesktopDTO idvCloudDesktopDTO) throws BusinessException {
        UserTerminalRequest request = new UserTerminalRequest();
        BeanUtils.copyProperties(idvCloudDesktopDTO, request);
        request.setTerminalId(terminalId);

        userTerminalMgmtAPI.editTerminalSetting(request);
    }

    private boolean doTerminalAuth(CbbDispatcherRequest request, CbbTerminalAuthRequest terminalAuthRequest) {

        if (Objects.isNull(terminalAuthRequest)) {
            return true;
        }

        try {
            CbbTerminalBasicInfoDTO basicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());

            CbbShineTerminalBasicInfo cbbShineTerminalBasicInfo = new CbbShineTerminalBasicInfo();
            cbbShineTerminalBasicInfo.setTerminalId(basicInfo.getTerminalId());
            cbbShineTerminalBasicInfo.setAllDiskInfo(JSON.toJSONString(basicInfo.getDiskInfoArr()));
            cbbShineTerminalBasicInfo.setPlatform(basicInfo.getTerminalPlatform());
            cbbShineTerminalBasicInfo.setSerialNumber(basicInfo.getSerialNumber());
            cbbShineTerminalBasicInfo.setProductType(basicInfo.getProductType());

            if (cbbTerminalConfigAPI.isTerminalInWhiteList(basicInfo.getProductType(), cbbShineTerminalBasicInfo)) {
                return true;
            }
        } catch (Throwable e) {
            // 仅记录日志，不做异常处理
            LOGGER.error("终端[{}]授权异常", request.getTerminalId(), e);
        }


        TerminalWorkModeConfigDTO terminalWorkModeConfigDTO = new TerminalWorkModeConfigDTO();
        terminalWorkModeConfigDTO.setTerminalId(request.getTerminalId());
        terminalWorkModeConfigDTO.setProductType(terminalAuthRequest.getProductType());
        terminalWorkModeConfigDTO.setPlatform(terminalAuthRequest.getPlatform());
        terminalWorkModeConfigDTO.setTerminalWorkSupportModeArr(terminalAuthRequest.getTerminalWorkSupportModeArr());
        CbbTerminalBizConfigDTO cbbTerminalBizConfigDTO = terminalService.getTerminalSupportMode(terminalWorkModeConfigDTO);

        try {
            LOGGER.info("接入终端[{}]进行授权", request.getTerminalId());

            return cbbTerminalLicenseMgmtAPI.checkEnableAuthTerminal(request.getTerminalId(), cbbTerminalBizConfigDTO.getAuthMode());

        } catch (BusinessException e) {
            LOGGER.error("终端[{}]授权异常", request.getTerminalId(), e);
            responseErrorMessage(request, ConfigWizardForIDVCode.TERMINAL_AUTH_FAIL);
            return false;
        }

    }

    private void doCancelTerminalAuth(String terminalId, CbbTerminalAuthRequest terminalAuthRequest) {
        if (Objects.isNull(terminalAuthRequest)) {
            return;
        }

        if (!cbbTerminalConfigAPI.hasTerminalSupportVdiAndIdv(terminalAuthRequest.getProductType())) {
            return;
        }

        try {
            LOGGER.info("终端[{}]取消授权", terminalId);
            cbbTerminalLicenseMgmtAPI.cancelTerminalAuth(terminalId);
        } catch (BusinessException e) {
            LOGGER.error("终端[{}]取消授权失败", terminalId, e);
        }

    }
}
