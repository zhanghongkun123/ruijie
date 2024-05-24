package com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacUpdateAdminRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacRoleDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.UpdateAdminRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.ProcessResult;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.StateProcessContext;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
@Service
public class UpdateAdminProcessor implements StateTaskHandle.StateProcessor {



    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Override
    public ProcessResult doProcess(StateProcessContext stateProcessContext) throws Exception {
        Assert.notNull(stateProcessContext, "stateProcessContext is not null");

        cacheAdminInfo(stateProcessContext);

        IacUpdateAdminRequest request = new IacUpdateAdminRequest();
        UpdateAdminRequest updateAdminRequest = stateProcessContext
            .get(UpdateAdminContextKey.UPDATE_ADMIN_REQUEST, UpdateAdminRequest.class);
        copyProperties(request, updateAdminRequest);
        baseAdminMgmtAPI.updateAdmin(request);

        return ProcessResult.next();
    }

    @Override
    public ProcessResult undoProcess(StateProcessContext context) throws Exception {
        Assert.notNull(context, "stateProcessContext is not null");

        IacUpdateAdminRequest request = new IacUpdateAdminRequest();
        UpdateAdminRequest updateAdminRequest = context
            .get(UpdateAdminContextKey.ADMIN_INFO_CACHE, UpdateAdminRequest.class);
        copyProperties(request, updateAdminRequest);
        baseAdminMgmtAPI.updateAdmin(request);

        return ProcessResult.next();
    }

    private void cacheAdminInfo(StateProcessContext stateProcessContext) throws BusinessException {
        UpdateAdminRequest updateAdminRequest = stateProcessContext
            .get(UpdateAdminContextKey.UPDATE_ADMIN_REQUEST, UpdateAdminRequest.class);

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(updateAdminRequest.getId());
        UpdateAdminRequest cacheRequest = new UpdateAdminRequest();
        cacheRequest.setId(baseAdminDTO.getId());
        cacheRequest.setRealName(baseAdminDTO.getRealName());
        cacheRequest.setUserName(baseAdminDTO.getUserName());
        cacheRequest.setEnabled(baseAdminDTO.getEnabled());
        cacheRequest.setEmail(baseAdminDTO.getEmail());
        cacheRequest.setDescribe(baseAdminDTO.getDescribe());

        List<IdLabelEntry> idLabelEntryList = new ArrayList<>();
        UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();
        for (UUID roleId : roleIdArr) {
            IdLabelEntry entry = new IdLabelEntry();
            entry.setId(roleId);
            idLabelEntryList.add(entry);
        }
        // 不为空 长度大于0 设置角色ID
        if (!CollectionUtils.isEmpty(idLabelEntryList)) {
            cacheRequest.setRoleId(idLabelEntryList.get(0).getId());
        }

        stateProcessContext.put(UpdateAdminContextKey.ADMIN_INFO_CACHE, cacheRequest);
    }

    private void copyProperties(IacUpdateAdminRequest request, UpdateAdminRequest updateAdminRequest)
        throws BusinessException {
        request.setDescribe(updateAdminRequest.getDescribe());
        request.setEmail(updateAdminRequest.getEmail());
        request.setId(updateAdminRequest.getId());
        request.setRealName(updateAdminRequest.getRealName());
        request.setUserName(updateAdminRequest.getUserName());
        request.setEnabled(updateAdminRequest.getEnabled());

        IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(updateAdminRequest.getId());
        UUID[] roleIdArr = baseAdminDTO.getRoleIdArr();

        List<IacRoleDTO> dtoList = baseRoleMgmtAPI.getRoleAllByRoleIds(roleIdArr);
        List<UUID> unmodifyRoleIdList = dtoList.stream()
            .filter(baseRoleDTO -> !baseRoleDTO.getHasDefault())
            .map(IacRoleDTO::getId).collect(Collectors.toList());

        List<UUID> modifyRoleIdList = new ArrayList<>();
        modifyRoleIdList.add(updateAdminRequest.getRoleId());
        List<UUID> roleIdList = new ArrayList<>();
        roleIdList.addAll(unmodifyRoleIdList);
        roleIdList.addAll(modifyRoleIdList);

        request.setRoleIdArr(roleIdList.toArray(new UUID[roleIdList.size()]));

        IacUserDetailDTO userDetail = cbbUserAPI.getUserByName(updateAdminRequest.getUserName());
        if (Objects.nonNull(userDetail)) {
            request.setUserType(IacUserTypeEnum.getUserDomainType(userDetail.getUserType()));
        }

    }
}
