package com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.impl;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdGroupEntityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdGroupAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacCreateDomainUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: Ad域组相关操作
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/27
 *
 * @author zqj
 */
@Service
public class RcoAdGroupServiceImpl implements RcoAdGroupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoAdGroupServiceImpl.class);

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    protected IacUserMgmtAPI cbbUserAPI;

    @Autowired
    protected IacAdGroupAPI cbbAdGroupAPI;

    @Autowired
    protected BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Override
    public boolean checkUserAdGroupResult(String userName) {
        Assert.hasText(userName, "userName cannot be null.");

        try {
            // 获取用户AD域服务器组集合
            List<IacAdGroupDTO> adGroupList = cbbAdMgmtAPI.getUserParentAdGroupList(userName);
            if (adGroupList.size() == 0) {
                return Boolean.FALSE;
            }
            List<String> objectGuidList = adGroupList.stream().map(IacAdGroupDTO::getObjectGUID).collect(Collectors.toList());
            int count = cbbAdGroupAPI.countByObjectGuidList(objectGuidList);
            // 引入AD域安全组，CDC不存在该用户时，获取隶属集合（ad域组集合）。
            // 该用户AD域服务器组集合存在CDC-AD域安全组组中，就同步增量同步到CDC
            if (count > 0) {
                // 增量同步用户到CDC
                IacCreateDomainUserRequest iacCreateDomainUserRequest = new IacCreateDomainUserRequest();
                iacCreateDomainUserRequest.setUserName(userName);
                iacCreateDomainUserRequest.setUserType(IacUserTypeEnum.AD);
                cbbUserAPI.createDomainUser(iacCreateDomainUserRequest);
                return Boolean.TRUE;
            }
            LOGGER.info("用户[{}]的AD域服务组集合[{}]不存在CDC安全组中", userName, objectGuidList.toString());
            saveSystemLog(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_AD_GROUP_NOT_EXIST, userName));
            return Boolean.FALSE;
        } catch (Exception e) {
            LOGGER.error("登录触发同步域用户[{}]异常", userName, e);
            if (e instanceof BusinessException) {
                String message = ((BusinessException) e).getI18nMessage();
                saveSystemLog(LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_LOGIN_FAIL_LOG, userName, message));
            }
        }
        return Boolean.FALSE;
    }

    private void saveSystemLog(String content) {
        BaseSystemLogDTO logDTO = new BaseSystemLogDTO();
        logDTO.setId(UUID.randomUUID());
        logDTO.setContent(content);
        logDTO.setCreateTime(new Date());
        baseSystemLogMgmtAPI.createSystemLog(logDTO);
    }

    @Override
    public List<UUID> getUserRelatedAdGroupList(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId cannot be null");
        List<String> objectGuidList = cbbUserAPI.getParentAdGroupList(userId);
        if (CollectionUtils.isEmpty(objectGuidList)) {
            return Collections.emptyList();
        }
        return cbbAdGroupAPI.getAdGroupByObjectGuidList(objectGuidList)
                .stream()
                .sorted(Comparator.comparingInt(dto -> objectGuidList.indexOf(dto.getObjectGuid())))
                .map(IacAdGroupEntityDTO::getId)
                .collect(Collectors.toList());
    }
}
