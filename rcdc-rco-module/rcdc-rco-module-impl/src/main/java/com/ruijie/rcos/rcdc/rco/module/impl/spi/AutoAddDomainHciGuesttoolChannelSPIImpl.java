package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacLdapServerDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserAuthorityEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.enums.GuesttoolMessageResultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.GuestToolResponseCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.AdGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.AutoAddDomainGuestToolMsgDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.AutoAddDomainRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 查询是否自动加域消息处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/2
 *
 * @author Jarman
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_AD)
public class AutoAddDomainHciGuesttoolChannelSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoAddDomainHciGuesttoolChannelSPIImpl.class);

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    protected CbbVDIDeskStrategyMgmtAPI deskStrategyDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");
        CbbGuesttoolMessageDTO requestDto = request.getDto();
        UUID deskId = requestDto.getDeskId();

        // 1.AD域配置,是否开启自动加域
        IacDomainConfigDetailDTO adConfig = cbbAdMgmtAPI.getAdConfig();
        if (ObjectUtils.isEmpty(adConfig)) {
            LOGGER.info("AD域配置为空，不开启自动加域");
            return replyFailMessageToGuestTool(deskId);
        }
        if (!adConfig.getEnable()) {
            LOGGER.info("当前服务器未开启AD域对接，不自动加域");
            return replyFailMessageToGuestTool(deskId);
        }
        if (!adConfig.getAutoJoin()) {
            LOGGER.info("当前服务器未开启AD域用户自动加域功能，不自动加域");
            return replyFailMessageToGuestTool(deskId);
        }
        CbbDeskDTO cbbDeskDTO;
        CloudDesktopDetailDTO cloudDesktopDetailDTO;
        AutoAddDomainRequestDTO domainRequest;
        try {
            cbbDeskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
            cloudDesktopDetailDTO = queryCloudDesktopService.queryDeskDetail(deskId);
            AutoAddDomainGuestToolMsgDTO domainGuestToolMsg = parseGuestToolMsg(requestDto.getBody(), AutoAddDomainGuestToolMsgDTO.class);
            domainRequest = Objects.isNull(domainGuestToolMsg.getContent()) ? new AutoAddDomainRequestDTO() : domainGuestToolMsg.getContent();
        } catch (Exception e) {
            LOGGER.error("获取域控信息失败,桌面[{}]查询或解析请求体时出现异常:{}", deskId, e);
            return replyFailMessageToGuestTool(deskId);
        }

        UUID userId = cloudDesktopDetailDTO.getUserId();
        RcoViewUserEntity userInfo = getUserInfo(userId);
        // 2.镜像是否加域
        if (Objects.isNull(domainRequest.getImageAd()) && CbbCloudDeskType.VDI == cbbDeskDTO.getDeskType()) {
            // 兼容VDI旧版本,入参没有则使用CDC的镜像配置信息,IDV桌面由shine处理
            LOGGER.info("云桌面:[{}],入参没有镜像加域字段,兼容VDI旧版本,使用还原点查询镜像配置", deskId);
            domainRequest.setImageAd(cbbDeskMgmtAPI.imageTemplateRestorePointJoinAd(cbbDeskDTO.getRestorePointId()));
        }

        if (Objects.nonNull(domainRequest.getImageAd()) && domainRequest.getImageAd()) {
            LOGGER.info("云桌面:[{}],镜像已加域,用户id:[{}]", deskId, userId);
            return replySuccessMessageToGuestTool(adConfig, cloudDesktopDetailDTO, userInfo);
        }

        // 3.是否还原桌面
        if (CbbCloudDeskPattern.RECOVERABLE.name().equals(cloudDesktopDetailDTO.getDesktopType())) {
            LOGGER.info("云桌面:[{}]为还原桌面,且镜像未加域", deskId);
            return replyFailMessageToGuestTool(deskId);
        }

        // 4.桌面绑定AD域用户
        if (userInfo == null || IacUserTypeEnum.AD != userInfo.getUserType()) {
            LOGGER.warn("云桌面:[{}],用户id:{},用户为空或非AD用户,不支持自动加域", deskId, userId);
            return replyFailMessageToGuestTool(deskId);
        }
        return replySuccessMessageToGuestTool(adConfig, cloudDesktopDetailDTO, userInfo);
    }

    private RcoViewUserEntity getUserInfo(UUID userId) {
        if (userId == null) {
            // 上层判断
            return null;
        }
        RcoViewUserEntity rcoViewUserEntity = null;
        Optional<RcoViewUserEntity> optionalRcoViewUser = rcoViewUserDAO.findById(userId);
        if (optionalRcoViewUser.isPresent()) {
            rcoViewUserEntity = optionalRcoViewUser.get();
        }
        return rcoViewUserEntity;
    }

    private CbbGuesttoolMessageDTO replyFailMessageToGuestTool(UUID deskId) {
        CbbGuesttoolMessageDTO dto = buildMessageToGuestTool(deskId);
        AdGuestToolMsgDTO adGuestToolMsgDTO = new AdGuestToolMsgDTO();
        adGuestToolMsgDTO.setCode(GuesttoolMessageResultTypeEnum.FAIL.getCode());
        dto.setBody(JSON.toJSONString(adGuestToolMsgDTO));
        return dto;
    }

    private CbbGuesttoolMessageDTO replySuccessMessageToGuestTool(IacDomainConfigDetailDTO adConfig, CloudDesktopDetailDTO cloudDesktopDetailDTO,
                                                                  RcoViewUserEntity userEntity)
            throws BusinessException {
        CbbGuesttoolMessageDTO dto = buildMessageToGuestTool(cloudDesktopDetailDTO.getId());
        //云桌面绑定策略
        CbbDeskStrategyDTO deskStrategy = deskStrategyDAO.getDeskStrategy(cloudDesktopDetailDTO.getDesktopStrategyId());
        // AD域加入OU路径优先获取云桌面绑定策略的OU信息
        adConfig.setAdOu(StringUtils.isEmpty(deskStrategy.getAdOu()) ? adConfig.getAdOu() : deskStrategy.getAdOu());
        String adGuestToolMsg = buildAdGuestToolMsg(userEntity, adConfig);
        dto.setBody(adGuestToolMsg);

        return dto;
    }

    private CbbGuesttoolMessageDTO buildMessageToGuestTool(UUID deskId) {
        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_CMD_ID_AD));
        dto.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_AD);
        dto.setDeskId(deskId);
        return dto;
    }

    private String buildAdGuestToolMsg(RcoViewUserEntity userEntity, IacDomainConfigDetailDTO adConfig) {
        AdGuestToolMsgDTO adGuestToolMsgDTO = new AdGuestToolMsgDTO();
        AdGuestToolMsgDTO.BodyMessage bodyMessage = new AdGuestToolMsgDTO.BodyMessage();
        bodyMessage.setAccount(adConfig.getManagerName());
        String ip = resolveAdConfigIp(adConfig);
        bodyMessage.setIp(ip);
        String cipherPassword = encrypt(adConfig, adGuestToolMsgDTO);
        bodyMessage.setAccountPassword(cipherPassword);
        bodyMessage.setAutoJoin(true);
        bodyMessage.setDomainUser(true);
        bodyMessage.setAdOu(StringUtils.isEmpty(adConfig.getAdOu()) ? "" : adConfig.getAdOu());

        bodyMessage.setDomain(adConfig.getDomainName());
        IacAdUserAuthorityEnum adUserAuthorityEnum =
                (userEntity == null || userEntity.getAdUserAuthority() == null) ? IacAdUserAuthorityEnum.GUESTS : userEntity.getAdUserAuthority();
        bodyMessage.setAdUserAuthority(adUserAuthorityEnum.getAdUserAuthority());
        adGuestToolMsgDTO.setContent(bodyMessage);

        return JSON.toJSONString(adGuestToolMsgDTO);
    }

    private String resolveAdConfigIp(IacDomainConfigDetailDTO adConfig) {
        IacLdapServerDTO mainServer = adConfig.getMainServer();
        if (BooleanUtils.isTrue(mainServer.getIsNormal())) {
            return mainServer.getServerAddress();
        }
        if (ArrayUtils.isEmpty(adConfig.getBackupServerArr())) {
            return mainServer.getServerAddress();
        }
        for (IacLdapServerDTO iacLdapServerDTO : adConfig.getBackupServerArr()) {
            if (BooleanUtils.isTrue(iacLdapServerDTO.getIsNormal())) {
                return iacLdapServerDTO.getServerAddress();
            }
        }
        return mainServer.getServerAddress();
    }

    private String encrypt(IacDomainConfigDetailDTO adConfig, AdGuestToolMsgDTO content) {
        String cipherPassword = null;
        try {
            cipherPassword = AesUtil.encrypt(adConfig.getManagerPassword(), RedLineUtil.getRealUserRedLine());
        } catch (Exception e) {
            LOGGER.error("AES 加密异常", e);
            content.setCode(GuestToolResponseCode.FAIL);
            content.setMessage("aes encrypt fail");
        }
        return cipherPassword;
    }

    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("guest tool报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }
}
