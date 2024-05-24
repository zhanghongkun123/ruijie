package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskVirtualizationStrategyAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVmsMappingAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.ShineAction;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGlobalVmMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVirtualizationStrategyDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.VmsMappingDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.AbstractDeskStrategyLocalModeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageVmMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.TerminalVmModeTypeEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.autologon.constant.AutoLogonConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.UpmPolicyTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AcpiService;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestDesktopBaseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestIDVDesktopBaseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.exception.ShineRequestIDVDesktopException;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Shine请求虚机启动参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/19
 *
 * @author chen zj
 */
@DispatcherImplemetion(ShineAction.SHINE_REQUEST_DESKTOP_BASE_INFO)
public class ShineRequestIDVDesktopBaseInfoSPIImpl extends AbstractShineRequestIDVDesktopSPIImpl<ShineRequestDesktopBaseDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineRequestIDVDesktopBaseInfoSPIImpl.class);

    /**
     * gt使用，正常用户启动都是未安装， 驱动安装登录虚机时区分是手动还是自动
     */
    private static final String NO_INSTALL_DRIVER = "noinstall";

    /**
     * gt使用,告诉gt虚机试运行在服务器上还是IDV终端上
     * <p>
     * TERM:运行在终端上
     * SERVER:运行在服务器上
     */
    public static final String IDV_VM_START_HOST = "TERM";

    /**
     * 个性桌面
     */
    public static final String IDV_VM_TYPE_SAVE = "SAVE";

    /**
     * 还原桌面
     */
    public static final String IDV_VM_TYPE_RESTORE = "RESTORE";

    /**
     * 应用分层桌面
     */
    public static final String IDV_VM_TYPE_LAYERON = "LAYERON";

    /**
     * 应用分层数，目前默认是:1
     */
    private static final int LAYER_DISK_NUMBER = 1;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbIDVDeskVirtualizationStrategyAPI cbbIDVDeskVirtualizationStrategyAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private AcpiService acpiService;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CbbVmsMappingAPI cbbVmsMappingAPI;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Override
    public void doDispatch(CbbDispatcherRequest dispatcherRequest,
                           ShineRequestDesktopBaseDTO shineRequestDesktopBaseDTO) {
        Assert.notNull(dispatcherRequest, "Param [cbbDispatcherRequest] must not be null");
        Assert.notNull(shineRequestDesktopBaseDTO, "Param [shineRequestDesktopBaseDTO] must not be null");
        Assert.notNull(dispatcherRequest.getData(), "Param [request.data] must not be null");

        LOGGER.info("收到Shine请求[request={}]桌面[id={}]基本信息", JSON.toJSONString(dispatcherRequest),
                shineRequestDesktopBaseDTO.getId());
        UUID desktopBaseDTOId = shineRequestDesktopBaseDTO.getId();
        try {
            CbbDeskDTO cbbDeskDTO = validateDesktopExists(shineRequestDesktopBaseDTO.getId());

            // 验证桌面关联信息是否存在
            validateDesktopImageTemplateExist(cbbDeskDTO, shineRequestDesktopBaseDTO);
            validateDesktopTerminalExist(dispatcherRequest.getTerminalId(), cbbDeskDTO.getDeskId());

            ShineRequestIDVDesktopBaseInfoDTO idvDeskVMStartACPIParamDTO =
                    buildIDVDeskVMStartParamDTO(dispatcherRequest.getTerminalId(), cbbDeskDTO);
            LOGGER.info("响应Shine请求云桌基本信息:[{}]", JSON.toJSONString(idvDeskVMStartACPIParamDTO));
            cbbTranspondMessageHandlerAPI.response(ShineResponseMessage.success(dispatcherRequest,
                    idvDeskVMStartACPIParamDTO));
        } catch (ShineRequestIDVDesktopException ex) {
            LOGGER.error("Shine请求桌面[id={}]基本信息出现异常, 返回Shine错误码:[{}], error:{}", desktopBaseDTOId,
                    ex.getResponseErrorCode(), ex);
            cbbTranspondMessageHandlerAPI.response(ShineResponseMessage.failWhitCode(dispatcherRequest,
                    ex.getResponseErrorCode()));
        } catch (Exception e) {
            LOGGER.error("Shine请求桌面[id={}]基本信息出现异常,返回Shine错误码:[{}], error:{}", desktopBaseDTOId,
                    CommonMessageCode.CODE_ERR_OTHER, e);
            cbbTranspondMessageHandlerAPI.response(ShineResponseMessage.fail(dispatcherRequest));
        }
    }

    private ShineRequestIDVDesktopBaseInfoDTO buildIDVDeskVMStartParamDTO(String terminalId, CbbDeskDTO cbbDeskDTO) throws BusinessException {

        LOGGER.info("开始构建Shine请求的云桌面[id={}]基本信息", cbbDeskDTO.getDeskId());
        CbbGetImageTemplateInfoDTO imageTemplateInfoResponse =
                cbbImageTemplateMgmtAPI.getImageTemplateInfo(cbbDeskDTO.getImageTemplateId());

        AbstractDeskStrategyLocalModeDTO deskStrategyLocalModeDTO;
        if (imageTemplateInfoResponse.getCbbImageType() == CbbImageType.IDV) {
            deskStrategyLocalModeDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(cbbDeskDTO.getStrategyId());
        } else {
            deskStrategyLocalModeDTO = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(cbbDeskDTO.getStrategyId());
        }

        ShineRequestIDVDesktopBaseInfoDTO idvDeskVMStartACPIParamDTO = new ShineRequestIDVDesktopBaseInfoDTO();
        idvDeskVMStartACPIParamDTO.setDeskName(cbbDeskDTO.getName());
        idvDeskVMStartACPIParamDTO.setImageId(imageTemplateInfoResponse.getId());
        idvDeskVMStartACPIParamDTO.setUuid(cbbDeskDTO.getDeskId());
        idvDeskVMStartACPIParamDTO.setDesktopPattern(deskStrategyLocalModeDTO.getPattern().name());
        idvDeskVMStartACPIParamDTO.setImageName(imageTemplateInfoResponse.getImageName());
        idvDeskVMStartACPIParamDTO.setDesktopRedirect(deskStrategyLocalModeDTO.getOpenDesktopRedirect());
        idvDeskVMStartACPIParamDTO.setOpenInternet(deskStrategyLocalModeDTO.getOpenInternet());
        idvDeskVMStartACPIParamDTO.setPersonalConfigStrategy(deskStrategyLocalModeDTO.getDeskPersonalConfigStrategyType());
        // 用户虚机默认都是未安装驱动，只有镜像发起的驱动安装才区分安装类型
        idvDeskVMStartACPIParamDTO.setInstallDriver(NO_INSTALL_DRIVER);
        idvDeskVMStartACPIParamDTO.setOsType(imageTemplateInfoResponse.getCbbOsType());
        idvDeskVMStartACPIParamDTO.setVmType(checkGuestToolUpgrade(deskStrategyLocalModeDTO));
        idvDeskVMStartACPIParamDTO.setHost(IDV_VM_START_HOST);
        idvDeskVMStartACPIParamDTO.setDataDiskEnable(deskStrategyLocalModeDTO.getAllowLocalDisk());
        idvDeskVMStartACPIParamDTO.setSupportGoldenImage(imageTemplateInfoResponse.getSupportGoldenImage());
        idvDeskVMStartACPIParamDTO.setHasUsbKeyboard(deskStrategyLocalModeDTO.getKeyboardEmulationType());
        CbbVirtualizationStrategyDetailDTO dto = obtainIDVDeskVmModel(terminalId,
                imageTemplateInfoResponse.getCbbOsType(), imageTemplateInfoResponse.getCbbImageType());
        LOGGER.info("从数据库中查询到的信息为：{}", JSON.toJSONString(dto));

        String imageVmModeStr = dto.getGraphicMode() != null ? dto.getGraphicMode().getValue() : null;
        idvDeskVMStartACPIParamDTO.setVmModel(imageVmModeStr);
        String usbctrlModeStr = dto.getUsbctrlMode() != null ? dto.getUsbctrlMode().getValue() : null;
        idvDeskVMStartACPIParamDTO.setUsbctrlMode(usbctrlModeStr);
        String serialModeStr = dto.getSerialMode() != null ? dto.getSerialMode().toString() : null;
        idvDeskVMStartACPIParamDTO.setSerialMode(serialModeStr);
        String nicModeStr = dto.getNicMode() != null ? dto.getNicMode().getValue() : null;
        idvDeskVMStartACPIParamDTO.setNicMode(nicModeStr);
        String audioModeStr = dto.getAudioMode() != null ? dto.getAudioMode().getValue() : null;
        idvDeskVMStartACPIParamDTO.setAudioMode(audioModeStr);
        String parallelMode = dto.getParallelMode() != null ? dto.getParallelMode().toString() : null;
        idvDeskVMStartACPIParamDTO.setParallelMode(parallelMode);

        idvDeskVMStartACPIParamDTO.setVmmodeVersion(dto.getLastestVersion() != null ? dto.getLastestVersion() : null);

        idvDeskVMStartACPIParamDTO.setPartType(imageTemplateInfoResponse.getPartType());

        buildDesktopLayerParamWhitLayerPattern(idvDeskVMStartACPIParamDTO, deskStrategyLocalModeDTO);
        CbbDeskDTO cddDeskDTO = cbbIDVDeskMgmtAPI.getDeskIDV(cbbDeskDTO.getDeskId());
        idvDeskVMStartACPIParamDTO.setComputerName(cddDeskDTO.getComputerName());
        idvDeskVMStartACPIParamDTO.setComputerNameSet(Boolean.TRUE);
        // 嵌套虚拟化
        idvDeskVMStartACPIParamDTO.setEnableNested(deskStrategyLocalModeDTO.getEnableNested());
        // 如果开启了系统盘自动扩容，设置为镜像模板的磁盘大小
        if (Boolean.TRUE.equals(deskStrategyLocalModeDTO.getEnableFullSystemDisk())) {
            idvDeskVMStartACPIParamDTO.setSystemDiskSize(imageTemplateInfoResponse.getImageSystemSize());
        } else {
            idvDeskVMStartACPIParamDTO.setSystemDiskSize(deskStrategyLocalModeDTO.getSystemSize());
        }
        idvDeskVMStartACPIParamDTO.setEnableFullSystemDisk(deskStrategyLocalModeDTO.getEnableFullSystemDisk());

        // 用户配置策略
        RcoDeskInfoEntity rcoDeskInfoEntity = rcoDeskInfoDAO.findByDeskId(cbbDeskDTO.getDeskId());
        Integer upmPolicyEnable = getUpmPolicyEnable(rcoDeskInfoEntity, cbbDeskDTO.getRestorePointId());
        idvDeskVMStartACPIParamDTO.setUpmPolicyEnable(upmPolicyEnable);

        // 预占盘符
        if (ArrayUtils.isNotEmpty(deskStrategyLocalModeDTO.getDesktopOccupyDriveArr())) {
            idvDeskVMStartACPIParamDTO.setUsingDrives(StringUtils.join(deskStrategyLocalModeDTO.getDesktopOccupyDriveArr(), ","));
        }

        // 获取vms.uuid
        VmsMappingDTO mappingDTO = cbbVmsMappingAPI.getVmsMappingDTOByBusinessId(cbbDeskDTO.getDeskId());
        if (Objects.nonNull(mappingDTO)) {
            idvDeskVMStartACPIParamDTO.setVmsId(mappingDTO.getId());
        }

        // IDV默认关闭自动登录设置
        idvDeskVMStartACPIParamDTO.setWinUsername("");
        idvDeskVMStartACPIParamDTO.setWinPassword("");
        idvDeskVMStartACPIParamDTO.setAutoLogon(AutoLogonConstants.WINDOWS_AUTO_LOGON_OFF);
        idvDeskVMStartACPIParamDTO.setImageRecoveryPointId(imageTemplateInfoResponse.getLastRecoveryPointId());

        idvDeskVMStartACPIParamDTO.setDiskController(imageTemplateInfoResponse.getDiskController());

        LOGGER.info("构建的桌面[id={}]信息为:{}", cbbDeskDTO.getDeskId(), JSON.toJSONString(idvDeskVMStartACPIParamDTO));
        return idvDeskVMStartACPIParamDTO;
    }

    private void buildDesktopLayerParamWhitLayerPattern(ShineRequestIDVDesktopBaseInfoDTO vmStartParamDTO,
                                                        AbstractDeskStrategyLocalModeDTO deskStrategyIDV) {
        if (deskStrategyIDV.getPattern() != CbbCloudDeskPattern.APP_LAYER) {
            LOGGER.info("云桌面[id={}]非应用分层,不需要构建应用分层参数信息", vmStartParamDTO.getUuid());
            return;
        }

        vmStartParamDTO.setLayerDiskNumber(LAYER_DISK_NUMBER);
        vmStartParamDTO.setLayerOn(true);
        vmStartParamDTO.setLayerX64(true);
    }

    /**
     * gt是否需要升级
     * true的情况：桌面类型为：个性、应用分层
     *
     * @return
     */
    private String checkGuestToolUpgrade(AbstractDeskStrategyLocalModeDTO cbbDeskStrategyIDVDTO) throws BusinessException {
        if (cbbDeskStrategyIDVDTO.getPattern() == CbbCloudDeskPattern.APP_LAYER) {
            return IDV_VM_TYPE_LAYERON;
        }

        if (cbbDeskStrategyIDVDTO.getPattern() == CbbCloudDeskPattern.PERSONAL) {
            return IDV_VM_TYPE_SAVE;
        }

        if (cbbDeskStrategyIDVDTO.getPattern() == CbbCloudDeskPattern.RECOVERABLE) {
            return IDV_VM_TYPE_RESTORE;
        }

        throw new BusinessException("不支持的桌面类型{" + cbbDeskStrategyIDVDTO.getPattern() + "}");
    }

    /**
     * 获取桌面模式：透传还是模拟
     */
    private CbbVirtualizationStrategyDetailDTO obtainIDVDeskVmModel(String terminalId,
                                                                    CbbOsType osType, CbbImageType imageType) throws BusinessException {

        CbbTerminalBasicInfoDTO terminalResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        LOGGER.info("通过终端cpu[{}]类型和操作系统[{}]获取桌面模式", terminalResponse.getCpuType(), osType.name());
        CbbVirtualizationStrategyDetailDTO virtualizationStrategy
                = cbbIDVDeskVirtualizationStrategyAPI.findVirtualizationStrategyByTerminalAndOsType(terminalResponse, osType);

        if (virtualizationStrategy == null) {
            virtualizationStrategy = new CbbVirtualizationStrategyDetailDTO();
        }

        dealVirtualizationStrategyByGlobalVmMode(virtualizationStrategy, terminalResponse.getProductType(), osType, imageType);

        return virtualizationStrategy;
    }

    private Integer getUpmPolicyEnable(RcoDeskInfoEntity rcoDeskInfoEntity, UUID restorePointId) {
        if (Objects.isNull(rcoDeskInfoEntity)) {
            // 无绑定UPM策略
            return UpmPolicyTypeEnum.UPM_UNABLE_START.getCode();
        }
        String userTypeString = viewDesktopDetailDAO.findUserTypeByCbbDesktopId(rcoDeskInfoEntity.getDeskId());
        IacUserTypeEnum userType = getUserType(userTypeString);
        return acpiService.getUpmPolicyEnable(rcoDeskInfoEntity, restorePointId, userType, null);
    }

    private IacUserTypeEnum getUserType(String userTypeString) {
        if (StringUtils.hasText(userTypeString)) {
            return IacUserTypeEnum.valueOf(userTypeString);
        }

        return IacUserTypeEnum.VISITOR;
    }

    @Override
    protected Class jsonToDesktopTargetDTO() {
        return ShineRequestDesktopBaseDTO.class;
    }

    private void dealVirtualizationStrategyByGlobalVmMode(CbbVirtualizationStrategyDetailDTO virtualizationStrategyDetailDTO,
                                                             String productType, CbbOsType osType,
                                                             CbbImageType imageType) {
        // 如果终端为G3+IDV+WIN10的话，获取全局虚机运行策略
        CbbGlobalVmMode g3GlobalVmMode =
                cbbGlobalStrategyMgmtAPI.getGlobalVmModeByTerminalVmModeType(TerminalVmModeTypeEnum.G3_IDV_WIN10);
        if (g3GlobalVmMode.getImageType() != imageType || !g3GlobalVmMode.getProductTypeList().contains(productType)
                || !g3GlobalVmMode.getOsTypeList().contains(osType.toString())) {
            return;
        }
        virtualizationStrategyDetailDTO.setGraphicMode(ImageVmMode.valueOf(g3GlobalVmMode.getVmMode().toString()));
        virtualizationStrategyDetailDTO.setAudioMode(ImageVmMode.valueOf(g3GlobalVmMode.getAudioMode().toString()));
        virtualizationStrategyDetailDTO.setUsbctrlMode(ImageVmMode.valueOf(g3GlobalVmMode.getUsbctrlMode().toString()));

        // 重置版本强制用cdc的策略
        virtualizationStrategyDetailDTO.setLastestVersion(null);
    }

}
