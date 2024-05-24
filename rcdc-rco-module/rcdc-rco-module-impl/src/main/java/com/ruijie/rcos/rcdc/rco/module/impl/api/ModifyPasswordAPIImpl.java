package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ModifyPasswordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CmsDockingAdminOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SuperPrivilegeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SyncAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SuperPrivilegeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ModifyPasswordService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/5 20:55
 *
 * @author linrenjian
 */
public class ModifyPasswordAPIImpl implements ModifyPasswordAPI {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPasswordAPIImpl.class);

    @Autowired
    private ModifyPasswordService resetPasswordService;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Autowired
    private CmsDockingAPI cmsDockingAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;


    @Override
    public boolean modifUserPwdSyncAdminPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException {
        Assert.notNull(resetPasswordDTO, "resetPasswordDTO can not be null");

        boolean isSuccess = resetPasswordService.modifUserPwdSyncAdminPwd(resetPasswordDTO);

        String userName = cbbUserAPI.getUserDetail(resetPasswordDTO.getId()).getUserName();

        if (!isSuccess) {
            LOGGER.info("修改用户[{}]密码失败，无需同步修改管理密码", userName);
            return false;
        }

        IacAdminDTO baseAdminDTO;
        try {
            baseAdminDTO = adminMgmtAPI.getAdminByUserName(userName);
        } catch (BusinessException e) {
            LOGGER.info(String.format("用户[%s]不存在对应管理员，无需进行CMS密码同步，错误信息为：[%s]", userName, e.getI18nMessage()));
            return true;
        }

        LOGGER.info("用户[{}]密码修改成功，并且存在对应管理员，进行CMS密码同步", userName);
        syncAdminByUpdatePwd(resetPasswordDTO, baseAdminDTO);

        return true;
    }

    @Override
    public void resetUserPwdSyncAdminPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException {
        Assert.notNull(resetPasswordDTO, "resetPasswordDTO must not null");

        resetPasswordService.resetUserPwdSyncAdminPwd(resetPasswordDTO);

        String userName = cbbUserAPI.getUserDetail(resetPasswordDTO.getId()).getUserName();
        IacAdminDTO baseAdminDTO;
        try {
            baseAdminDTO = adminMgmtAPI.getAdminByUserName(userName);
        } catch (BusinessException e) {
            LOGGER.info(String.format("用户[%s]不存在对应管理员，无需进行CMS密码同步，错误信息为：[%s]", userName, e.getI18nMessage()));
            return;
        }
        LOGGER.info("用户[{}]存在对应管理员，进行CMS密码同步", userName);
        syncAdminByUpdatePwd(resetPasswordDTO, baseAdminDTO);

    }

    /**
     * CMS对接：修改管理员密码触发同步
     */
    private void syncAdminByUpdatePwd(ModifyPasswordDTO modifyPasswordDTO, IacAdminDTO baseAdminDTO) {
        try {
            SuperPrivilegeRequest superPrivilegeRequest = new SuperPrivilegeRequest();
            superPrivilegeRequest.setRoleIdArr(baseAdminDTO.getRoleIdArr());
            SuperPrivilegeResponse response = adminManageAPI.isSuperPrivilege(superPrivilegeRequest);
            if (response.isSuperPrivilege()) {
                SyncAdminRequest syncAdminRequest = new SyncAdminRequest();
                syncAdminRequest.setOper(CmsDockingAdminOperNotifyEnum.SYNC_ADMIN.getOper());
                syncAdminRequest.setName(baseAdminDTO.getUserName());
                syncAdminRequest.setPassword(modifyPasswordDTO.getNewPwd());
                cmsDockingAPI.syncAdmin(syncAdminRequest);
            }
        } catch (Exception e) {
            LOGGER.error("CMS对接：修改管理员密码触发同步失败。id = " + baseAdminDTO.getId(), e);
        }
    }


    @Override
    public void modifyAdminPasswordSynUserPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException {
        Assert.notNull(resetPasswordDTO, "resetPasswordDTO can not be null");

        resetPasswordService.modifyAdminPasswordSynUserPwd(resetPasswordDTO);
    }

    @Override
    public void modifyOtherAdminPwdSyncUserPwd(ModifyPasswordDTO modifyPasswordDTO) throws BusinessException {
        Assert.notNull(modifyPasswordDTO, "modifyPasswordDTO can not be null");

        resetPasswordService.modifyOtherAdminPwdSyncUserPwd(modifyPasswordDTO);
    }
}
