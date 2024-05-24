package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacGetAdminPasswordRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacIdPasswordEntry;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SuperPrivilegeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author luojianmo
 */
public class AdminManageAPIImpl implements AdminManageAPI {

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Override
    public SuperPrivilegeResponse isSuperPrivilege(SuperPrivilegeRequest superPrivilegeRequest) throws BusinessException {
        Assert.notNull(superPrivilegeRequest, "SuperPrivilegeRequest is null");
        Assert.notNull(superPrivilegeRequest.getRoleIdArr(), "RoleIdArr is null");
        List<IacRoleDTO> baseRoleDTOList = baseRoleMgmtAPI.getRoleAllByRoleIds(superPrivilegeRequest.getRoleIdArr());
        if (baseRoleDTOList == null) {
            return new SuperPrivilegeResponse(false);
        }
        for (IacRoleDTO baseRoleDTO : baseRoleDTOList) {
            if (baseRoleDTO.getHasSuperPrivilege()) {
                return new SuperPrivilegeResponse(true);
            }
        }
        return new SuperPrivilegeResponse(false);
    }

    @Override
    public GetAdminPasswordResponse getAdminPassword(IdRequest idRequest) throws BusinessException {
        Assert.notNull(idRequest, "IdRequest is null");
        UUID[] idArr = new UUID[] {idRequest.getId()};
        IacGetAdminPasswordRequest request = new IacGetAdminPasswordRequest();
        request.setIdArr(idArr);
        IacIdPasswordEntry[] idPasswordEntrieArr = baseAdminMgmtAPI.getAdminPassword(request);
        if (ArrayUtils.isEmpty(idPasswordEntrieArr)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_GET_ADMIN_PASSWORD_FAIL);
        }
        IacIdPasswordEntry idPasswordEntry = idPasswordEntrieArr[0];
        GetAdminPasswordResponse getAdminPasswordResponse = new GetAdminPasswordResponse();
        // 返回AES 用RedLineUtil.getRealAdminRedLine()加密的密文
        getAdminPasswordResponse.setPassword(idPasswordEntry.getPassword());
        return getAdminPasswordResponse;
    }
}
