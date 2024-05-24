package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.api;

import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.ClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.OtpUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.OtpParamRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.otp.IacUserOtpCertificationConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.RcoViewUserOtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.entity.RcoViewUserOtpCertificationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.service.QueryUserOtpCertificationListService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;


/**
 *
 * Description: 动态口令认证策略API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
public class UserOtpCertificationAPIImpl implements UserOtpCertificationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserOtpCertificationAPIImpl.class);

    private static final String USER_TYPE = "userType";

    private static final String SOURCE = "source";

    private static final String CDC = "CDC";

    @Autowired
    private QueryUserOtpCertificationListService queryUserOtpCertificationListService;

    @Autowired
    private IacUserOtpCertificationAPI iacUserOtpCertificationAPI;

    @Autowired
    private CloudPlatformMgmtAPI cloudPlatformMgmtAPI;

    @Override
    public DefaultPageResponse<RcoViewUserOtpCertificationDTO> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "PageSearchRequest can not be null");

        Page<RcoViewUserOtpCertificationEntity> page =
                queryUserOtpCertificationListService.pageQuery(request, RcoViewUserOtpCertificationEntity.class);
        List<RcoViewUserOtpCertificationEntity> entityList = page.getContent();
        RcoViewUserOtpCertificationDTO[] dtoArr = buildListDTO(entityList);
        DefaultPageResponse<RcoViewUserOtpCertificationDTO> response = new DefaultPageResponse<>();
        response.setItemArr(dtoArr);
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Override
    public boolean resetById(UUID userId) throws BusinessException {
        Assert.notNull(userId, "resetById()的userId can not be null");
        return iacUserOtpCertificationAPI.resetById(userId);
    }

    @Override
    public boolean bindById(UUID userId) throws BusinessException {
        Assert.notNull(userId, "bindById()的id can not be null");

        return iacUserOtpCertificationAPI.bindById(userId);
    }

    @Override
    public boolean checkUserOtpCode(UUID userId, String code) throws BusinessException {
        Assert.notNull(userId, "checkUserOtpCode()的userId can not be null");
        Assert.notNull(code, "code can not be null");

        return iacUserOtpCertificationAPI.checkUserOtpCode(userId, code);
    }


    @Override
    public IacUserOtpCertificationConfigDTO getUserOtpCertificationConfigById(UUID userId) throws BusinessException {
        Assert.notNull(userId, "getUserOtpCertificationConfigById()的userId can not be null");

        return iacUserOtpCertificationAPI.getUserOtpCertificationConfigById(userId);

    }

    @Override
    public String obtainOtpAttachmentParams(OtpParamRequest otpParamRequest) {
        Assert.notNull(otpParamRequest,"otpParamRequest must not null");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USER_TYPE, otpParamRequest.getUserType().name());

        switch (otpParamRequest.getUserType()) {
            case ADMIN:
                try {
                    jsonObject.put(SOURCE, CDC);
                    DtoResponse<ClusterVirtualIpDTO> dtoResponse = cloudPlatformMgmtAPI.getClusterVirtualIp(new DefaultRequest());
                    jsonObject.put("clusterIp", dtoResponse.getDto().getClusterVirtualIpIp());
                } catch (BusinessException e) {
                    LOGGER.error("Otp额外参数中，获取集群VIP失败，但不影响业务逻辑", e);
                }
                break;
            case USER:
                break;
            default:
                throw new UnsupportedOperationException("unsupport optParam user type");
        }
        return jsonObject.toJSONString();
    }

    private RcoViewUserOtpCertificationDTO[] buildListDTO(List<RcoViewUserOtpCertificationEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return new RcoViewUserOtpCertificationDTO[] {};
        }
        return entityList.stream().map(item -> {
            RcoViewUserOtpCertificationDTO certificationDTO = new RcoViewUserOtpCertificationDTO();
            BeanUtils.copyProperties(item, certificationDTO);
            return certificationDTO;
        }).toArray(RcoViewUserOtpCertificationDTO[]::new);
    }

}
