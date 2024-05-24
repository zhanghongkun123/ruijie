package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryRecordDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateCloudDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.desktop.CreateDesktopWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 云桌面web service
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author artom
 */
@Service
public class CloudDesktopWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDesktopWebService.class);

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    /**
     * 创建云桌面
     *
     * @param createDesktopRequest 请求参数
     * @param userName 用户名
     * @return 创建结果
     * @throws BusinessException 业务异常
     */
    public CreateDesktopWebResponse createDesktop(CreateCloudDesktopRequest createDesktopRequest, String userName) throws BusinessException {
        Assert.notNull(createDesktopRequest, "request must not be null");
        Assert.hasText(userName, "userName can not empty");

        try {
            CreateDesktopResponse resp = cloudDesktopMgmtAPI.create(createDesktopRequest);
            CreateDesktopWebResponse webResp = new CreateDesktopWebResponse();
            webResp.setId(resp.getId());
            webResp.setDesktopName(resp.getDesktopName());
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_SUC_LOG, userName, resp.getDesktopName());

            return webResp;
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_CREATE_FAIL_LOG, userName, e.getI18nMessage());
            throw e;
        }
    }

    /**
     * 导入云桌面
     *
     * @param createDesktopRequest 请求参数
     * @param userName 用户名
     * @return 创建结果
     * @throws BusinessException 业务异常
     */
    public CreateDesktopWebResponse importDesktop(CreateCloudDesktopRequest createDesktopRequest, String userName) throws BusinessException {
        Assert.notNull(createDesktopRequest, "request must not be null");
        Assert.hasText(userName, "userName can not empty");

        try {
            LOGGER.info("导入云桌面请求:{}", JSON.toJSONString(createDesktopRequest));
            CreateDesktopResponse resp = cloudDesktopMgmtAPI.create(createDesktopRequest);
            CreateDesktopWebResponse webResp = new CreateDesktopWebResponse();
            webResp.setId(resp.getId());
            webResp.setDesktopName(resp.getDesktopName());
            return webResp;
        } catch (BusinessException e) {
            throw e;
        }
    }

    /**
     * 根据云桌面id获取桌面信息
     *
     * @param deskId 云桌面id
     * @return 桌面信息
     */
    public CloudDesktopDetailDTO obtainCloudDesktopResponse(UUID deskId) {
        Assert.notNull(deskId, "deskId must not be null");
        CloudDesktopDetailDTO cloudDesktopDetailDTO;
        try {
            cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            LOGGER.error("获取桌面信息失败，桌面id为[{}]", deskId, e);
            cloudDesktopDetailDTO = new CloudDesktopDetailDTO();
            cloudDesktopDetailDTO.setDesktopName(deskId.toString());
            cloudDesktopDetailDTO.setUserName(StringUtils.EMPTY);
        }
        return cloudDesktopDetailDTO;
    }

    /**
     * 拦截第三方桌面并抛出对应异常信息
     *
     * @param deskId 桌面id
     * @param userBusiness 异常描述信息
     * @throws BusinessException 业务异常
     */
    public void checkThirdPartyDesktop(UUID deskId, String userBusiness) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(userBusiness, "userBusiness must not be null");
        CloudDesktopDetailDTO cloudDesktopDetailDTO = obtainCloudDesktopResponse(deskId);
        if (CbbCloudDeskType.THIRD.name().equals(cloudDesktopDetailDTO.getDeskType())) {
            throw new BusinessException(userBusiness, cloudDesktopDetailDTO.getDesktopName());
        }
    }

    /**
     * 获取云桌面类型
     *
     * @param topNewUamDeliveryRecordList list
     */
    public void convertDeskType(List<CbbUamDeliveryRecordDTO> topNewUamDeliveryRecordList) {
        for (CbbUamDeliveryRecordDTO cbbUamDeliveryRecordDTO : topNewUamDeliveryRecordList) {
            UUID cloudDesktopId = cbbUamDeliveryRecordDTO.getCloudDesktopId();
            CloudDesktopDetailDTO desktopDetail;
            try {
                desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(cloudDesktopId);
                cbbUamDeliveryRecordDTO.setDeskType(desktopDetail.getStrategyType());
            } catch (BusinessException e) {
                LOGGER.error("获取桌面信息失败，桌面id为[{}]", cloudDesktopId, e);
                cbbUamDeliveryRecordDTO.setDeskType(
                        cbbUamDeliveryRecordDTO.getCbbImageType() == null ? "" : cbbUamDeliveryRecordDTO.getCbbImageType().name());
            }
        }
    }
}
