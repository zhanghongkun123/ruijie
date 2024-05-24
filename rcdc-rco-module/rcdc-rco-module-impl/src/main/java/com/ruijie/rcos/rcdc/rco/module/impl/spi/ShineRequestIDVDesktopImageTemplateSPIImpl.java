package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.ShineAction;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbIDVDeskImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.IDVImageFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbIDVImageDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.ex.NoSuchResourceException;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalDriverConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.terminaldriver.response.TerminalDriverConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineRequestIDVDeskMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestDesktopBaseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.exception.ShineRequestIDVDesktopException;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/27
 *
 * @author chen zj
 */
@DispatcherImplemetion(ShineAction.SHINE_REQUEST_IMAGE_INFO)
public class ShineRequestIDVDesktopImageTemplateSPIImpl extends AbstractShineRequestIDVDesktopSPIImpl<ShineRequestDesktopBaseDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineRequestIDVDesktopImageTemplateSPIImpl.class);

    private static final String IMAGE_TORRENT_PATH_PREFIX = "/opt/ftp/terminal";

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private TerminalDriverConfigAPI terminalDriverConfigAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public void doDispatch(CbbDispatcherRequest dispatcherRequest, ShineRequestDesktopBaseDTO desktopBaseDTO) {
        Assert.notNull(dispatcherRequest, "Param [cbbDispatcherRequest] must not be null");
        Assert.notNull(desktopBaseDTO, "Param [desktopBaseDTO] must not be null");
        UUID imageID = desktopBaseDTO.getId();
        LOGGER.info("收到Shine请求[request={}]桌面镜像ID[id={}]镜像信息", JSON.toJSONString(dispatcherRequest), imageID);
        try {

            CbbIDVDeskImageTemplateDTO idvDeskImageTemplate = cbbIDVDeskMgmtAPI.getIDVDeskImageTemplate(imageID);
            List<CbbIDVImageDiskDTO> imageDiskList = Optional.ofNullable(idvDeskImageTemplate.getImageDiskList()).orElse(new ArrayList<>());
            CbbImageTemplateDetailDTO cbbImageTemplateDetailDTO = idvDeskImageTemplate.getCbbImageTemplateDetailDTO();

            for (CbbIDVImageDiskDTO idvImageDiskDTO : imageDiskList) {
                IDVImageFileDTO idvImageFileDTO = idvImageDiskDTO.getIdvImageFileDTO();
                if (ObjectUtils.isEmpty(idvImageFileDTO)) {
                    continue;
                }
                // 验证种子是否存在
                validateImageTemplateTorrentExist(idvImageFileDTO);
                idvImageFileDTO.setTorrentFilePath(StringUtils.remove(idvImageFileDTO.getTorrentFilePath(), IMAGE_TORRENT_PATH_PREFIX));
                idvImageFileDTO.getBackingFile()
                        .setTorrentFilePath(StringUtils.remove(idvImageFileDTO.getBackingFile().getTorrentFilePath(), IMAGE_TORRENT_PATH_PREFIX));
            }

            // VOI 不支持服务端安装驱动，下载无需校验
            if (cbbImageTemplateDetailDTO.getCbbImageType() != CbbImageType.VOI) {
                // 验证是否安装了对应的驱动信息
                validateImageTemplateDriverInstalled(idvDeskImageTemplate, dispatcherRequest.getTerminalId(), imageID);
            }

            // 根据最后还原点查询镜像加域状态
            if (Objects.nonNull(cbbImageTemplateDetailDTO.getLastRecoveryPointId())) {
                cbbImageTemplateDetailDTO.setAd(cbbDeskMgmtAPI.imageTemplateRestorePointJoinAd(cbbImageTemplateDetailDTO.getLastRecoveryPointId()));
            }

            // 去掉/opt/ftp/terminal
            LOGGER.info("响应Shine[{}]请求镜像种子信息：[{}]", dispatcherRequest.getTerminalId(), JSON.toJSONString(imageDiskList));
            idvDeskImageTemplate.setImageDiskList(imageDiskList);
            // 特殊处理storageDriverVersion字段。因为SHINE此前用的是这个字段，并且有持久化，项目临近发布，为了减小影响面，不用新的字段，由CDC做转换。
            // storageDriverVersion是服务器上镜像里面当前安装的的版本号；beforeStorageDriverVersion是最后一次发布镜像时所使用的版本号；在镜像发布完成时，会把前者赋值给后者
            // 所以在镜像编辑安装新的GT，但是还没有发布等场景，SHINE拿到的应该是最后一次发布的版本号，不应该是当前的版本号
            // 此时因为没有重新做种，SHINE下载的实际上也是最后一次发布的镜像
            cbbImageTemplateDetailDTO.setStorageDriverVersion(cbbImageTemplateDetailDTO.getBeforeStorageDriverVersion());
            cbbTranspondMessageHandlerAPI.response(ShineResponseMessage.success(dispatcherRequest, idvDeskImageTemplate));
        } catch (NoSuchResourceException ex) {
            LOGGER.error("Shine请求桌面镜像ID[id= " + imageID + "]镜像信息出现异常, 返回Shine错误码:[" + ex.getKey() + "], error:{}", ex);
            cbbTranspondMessageHandlerAPI.response(
                    ShineResponseMessage.failWhitCode(dispatcherRequest, ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_NOT_FIND_BACKING_FILE));
        } catch (ShineRequestIDVDesktopException ex) {
            LOGGER.error("Shine请求桌面镜像ID[id= " + imageID + "]镜像信息出现异常, 返回Shine错误码:[" + ex.getResponseErrorCode() + "], error:{}", ex);
            cbbTranspondMessageHandlerAPI.response(ShineResponseMessage.failWhitCode(dispatcherRequest, ex.getResponseErrorCode()));
        } catch (Exception e) {
            LOGGER.error("Shine请求桌面镜像ID[id={ " + imageID + "}]镜像信息出现异常,返回Shine错误码[{" + CommonMessageCode.CODE_ERR_OTHER + "}], error:{}", e);
            cbbTranspondMessageHandlerAPI.response(ShineResponseMessage.fail(dispatcherRequest));
        }
    }

    /**
     * 判断桌面关联的镜像是否安装了终端支持的驱动
     *
     * @param cbbIDVDeskImageTemplateDTO 镜像文件
     * @throws ShineRequestIDVDesktopException 驱动不支持异常
     */
    private void validateImageTemplateDriverInstalled(CbbIDVDeskImageTemplateDTO cbbIDVDeskImageTemplateDTO, String terminalId, UUID imageId)
            throws BusinessException {
        Assert.notNull(imageId, "桌面绑定的镜像ID不能为空");

        if (isSystemTypeWithXP(imageId)) {
            LOGGER.info("镜像[{}]操作系统为[XP]不需要判断驱动信息", imageId);
            return;
        }

        CbbTerminalBasicInfoDTO terminalBasicInfoResponse = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

        String driverType = terminalBasicInfoResponse.getCpuType();

        // 查找已安装的镜像驱动中是否有与该终端cpu相匹配的
        Optional<CbbImageTemplateDriverDTO> cbbImageTemplateDriverDTOOptional = cbbIDVDeskImageTemplateDTO.getCbbImageTemplateDriverDTOList().stream()
                .filter(driverDTO -> driverDTO.getDriverType().equals(driverType)).findFirst();

        // 未找到与cpu匹配的驱动信息
        if (!cbbImageTemplateDriverDTOOptional.isPresent()) {
            LOGGER.error("镜像[id:{}]未安装对应的cpu类型为[{}]驱动", imageId, terminalBasicInfoResponse.getCpuType());
            if (isNotSupportInstallDriverOsType(imageId)) {
                throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.TERMINAL_NOT_SUPPORT_OS_TYPE);
            }
            throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_DRIVER_NOT_INSTALL);
        }

        // 找到与cpu匹配的驱动信息，但是该驱动状态还是处于未发布
        CbbImageTemplateDriverDTO cbbImageTemplateDriverDTO = cbbImageTemplateDriverDTOOptional.get();
        if (Boolean.FALSE.equals(cbbImageTemplateDriverDTO.getPublished())) {
            LOGGER.error("镜像[id:{}]对应的驱动已经安装,但是镜像还处于待发布状态,不支持下载", imageId);
            throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_NOT_AVAILABLE);
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

    /**
     * 镜像操作系统是否为xp类型
     * 
     * @param imageTemplateId 镜像ID
     * @return true：xp操作系统, 否则：false
     * @throws BusinessException 业务异常
     */
    private boolean isSystemTypeWithXP(UUID imageTemplateId) throws BusinessException {
        // XP不需要验证驱动：
        CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
        Assert.notNull(imageTemplateInfo, "imageTemplateInfo can not be null");

        LOGGER.info("镜像[{}]操作系统类型为:[{}]", imageTemplateInfo.getImageName(), imageTemplateInfo.getCbbOsType());
        return imageTemplateInfo.getCbbOsType() == CbbOsType.WIN_XP_SP3;
    }

    /**
     * 验证镜像种子是否存在
     * 
     * @param iDVImageFileDTO 镜像种子信息封装对象
     * @throws ShineRequestIDVDesktopException 返回shine错误码
     */
    private void validateImageTemplateTorrentExist(IDVImageFileDTO iDVImageFileDTO) throws ShineRequestIDVDesktopException {
        if (StringUtils.isBlank(iDVImageFileDTO.getTorrentFilePath())) {
            LOGGER.error("镜像[id:{}]种子信息不存在", iDVImageFileDTO.getFileName());
            throw new ShineRequestIDVDesktopException(ShineRequestIDVDeskMessageCode.DESK_RELEASE_IMAGE_NOT_FIND_BACKING_FILE);
        }
    }

    @Override
    protected Class jsonToDesktopTargetDTO() {
        return ShineRequestDesktopBaseDTO.class;
    }
}
