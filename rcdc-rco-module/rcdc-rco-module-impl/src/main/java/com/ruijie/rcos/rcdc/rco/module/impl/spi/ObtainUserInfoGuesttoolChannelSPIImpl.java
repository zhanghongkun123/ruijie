package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostSessionAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbSyncLoginAccountPermissionEnums;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ObtainUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: GuestTool -> RCDC 获取用户信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/16
 *
 * @author WuShengQiang
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_USER_INFO)
public class ObtainUserInfoGuesttoolChannelSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObtainUserInfoGuesttoolChannelSPIImpl.class);

    @Autowired
    private UserDesktopDAO desktopDAO;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private RcaHostSessionAPI rcaHostSessionAPI;

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) {
        Assert.notNull(request, "request cannot null");
        // Cbb桌面ID
        UUID deskId = request.getDto().getDeskId();

        // 初始化返回对象
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        messageDTO.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_CMD_ID_USER_INFO));
        messageDTO.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_USER_INFO);
        messageDTO.setDeskId(deskId);


        // 查找目标用户ID
        UUID userId = null;
        
        UserDesktopEntity userDesktopEntity = desktopDAO.findByCbbDesktopId(deskId);

        // 桌面绑定用户ID 与 云应用主机id查询静态单会话绑定用户id 必须至少有一个用户ID信息
        if (userDesktopEntity == null || userDesktopEntity.getUserId() == null) {
            LOGGER.warn("GT获取用户信息失败，云桌面ID:{}，不存在用户信息，userDesktopEntity：", deskId, JSONObject.toJSONString(userDesktopEntity));
            try {
                userId = rcaHostSessionAPI.getHostSingleBindUser(deskId, null);
            } catch (BusinessException ex) {
                LOGGER.error("查询应用主机绑定单会话用户失败，ex: ", ex);
            }
        } else {
            userId = userDesktopEntity.getUserId();
        }

        if (userId == null) {
            LOGGER.error("GT获取用户信息失败,查询云桌面ID:{},无关联用户ID", deskId);
            return buildFailResponse(messageDTO, LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_NOT_EXISTS));
        }

        IacUserDetailDTO userDetail = null;
        try {
            userDetail = cbbUserAPI.getUserDetail(userId);
        } catch (BusinessException e) {
            LOGGER.error("GT获取云桌面[{}]关联用户[{}]信息失败，失败原因：", deskId, userId, e);
        }

        if (userDetail == null) {
            LOGGER.error("GT获取云桌面[{}]关联用户[{}]信息失败", deskId);
            return buildFailResponse(messageDTO, LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_NOT_EXISTS));
        }

        ObtainUserInfoDTO body = new ObtainUserInfoDTO();
        body.setCode(CommonMessageCode.SUCCESS);
        ObtainUserInfoDTO.Content userContent = new ObtainUserInfoDTO.Content();
        userContent.setUserType(userDetail.getUserType());
        userContent.setUserName(userDetail.getUserName());
        userContent.setPassword(userDetail.getPassword());
        userContent.setChangeUserName(Boolean.FALSE);
        userContent.setAutoLogin(Boolean.FALSE);
        userContent.setUserGroup(CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue());

        try {
            CbbDeskInfoDTO cbbDeskInfoDTO = cbbVDIDeskMgmtAPI.getByDeskId(deskId);
            if (DesktopPoolType.STATIC.equals(cbbDeskInfoDTO.getDesktopPoolType())
                || DesktopPoolType.COMMON.equals(cbbDeskInfoDTO.getDesktopPoolType())) {
                CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(cbbDeskInfoDTO.getStrategyId());
                userContent.setChangeUserName(deskStrategy.getDesktopSyncLoginAccount() == null ?
                        Boolean.FALSE : deskStrategy.getDesktopSyncLoginAccount());
                userContent.setAutoLogin(deskStrategy.getDesktopSyncLoginPassword() == null ?
                        Boolean.FALSE : deskStrategy.getDesktopSyncLoginPassword());
                userContent.setUserGroup(deskStrategy.getDesktopSyncLoginAccountPermission() == null ?
                        CbbSyncLoginAccountPermissionEnums.DEFAULT.getValue() :
                        deskStrategy.getDesktopSyncLoginAccountPermission().getValue());
            }
        } catch (BusinessException ex) {
            LOGGER.error("查询桌面或策略信息过程中发生异常，ex:", ex);
        }

        body.setContent(userContent);
        messageDTO.setBody(JSON.toJSONString(body));
        return messageDTO;
    }

    private CbbGuesttoolMessageDTO buildFailResponse(CbbGuesttoolMessageDTO messageDTO, String message) {
        ObtainUserInfoDTO body = new ObtainUserInfoDTO();
        body.setCode(CommonMessageCode.FAIL);
        body.setMessage(message);

        messageDTO.setBody(JSON.toJSONString(body));
        return messageDTO;
    }
}
