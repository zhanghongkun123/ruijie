package com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.create;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpgradeUserToAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.CreateAdminRequest;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.ProcessResult;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.StateProcessContext;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
@Service
public class CreateAdminProcessor implements StateTaskHandle.StateProcessor {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateAdminProcessor.class);

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public ProcessResult doProcess(StateProcessContext stateProcessContext) throws Exception {
        Assert.notNull(stateProcessContext, "stateProcessContext is not null");

        IacUpgradeUserToAdminDTO baseCreateAdminRequest = buildBaseCreateAdminRequest(stateProcessContext);
        iacUserMgmtAPI.upgradeUserToAdmin(baseCreateAdminRequest);

        stateProcessContext.put(CreateAdminContextKey.CREATE_ADMIN_ID, baseCreateAdminRequest.getId());

        return ProcessResult.next();
    }

    private IacUpgradeUserToAdminDTO buildBaseCreateAdminRequest(StateProcessContext stateProcessContext) {
        CreateAdminRequest createAdminRequest = stateProcessContext.get(CreateAdminContextKey.CREATE_ADMIN_REQUEST, CreateAdminRequest.class);
        // 获取角色数组ID
        List<UUID> uuidList = new ArrayList<>();
        uuidList.add(createAdminRequest.getRoleId());
        UUID[] uuidArr = uuidList.toArray(new UUID[uuidList.size()]);

        IacUpgradeUserToAdminDTO baseCreateAdminRequest = new IacUpgradeUserToAdminDTO();
        BeanUtils.copyProperties(createAdminRequest, baseCreateAdminRequest);
        baseCreateAdminRequest.setRoleIdArr(uuidArr);
        baseCreateAdminRequest.setHasDefault(false);
        baseCreateAdminRequest.setUserType(createAdminRequest.getUserType());
        baseCreateAdminRequest.setId(UUID.randomUUID());
        baseCreateAdminRequest.setKey(RedLineUtil.getRealAdminRedLine());
        baseCreateAdminRequest.setSubSystem(SubSystem.CDC);
        return baseCreateAdminRequest;
    }

}
