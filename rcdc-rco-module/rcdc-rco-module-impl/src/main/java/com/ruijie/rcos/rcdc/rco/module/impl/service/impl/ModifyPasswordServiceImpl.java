package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacChangeUserPasswordDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacModifyAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacModifyOtherAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ModifyPasswordService;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/5 21:30
 *
 * @author linrenjian
 */
@Service
public class ModifyPasswordServiceImpl implements ModifyPasswordService {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(ModifyPasswordServiceImpl.class);

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Override
    public boolean modifUserPwdSyncAdminPwd(ModifyPasswordDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto can not be null");
        Assert.notNull(dto.getId(), "UserId can not be null");

        LOGGER.info("修改用户[{}]密码同步修改管理员密码，needUpdatePassword=[{}]", dto.getId(), dto.getNeedUpdatePassword());
        // 查询用户信息
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(dto.getId());
        LOGGER.info("查询到的用户信息:{}", JSON.toJSONString(userDetail));

        String encUserNewPwd = AesUtil.encrypt(dto.getNewPwd(), RedLineUtil.getRealUserRedLine());
        IacChangeUserPasswordDTO cbbChangeUserPasswordDTO = new IacChangeUserPasswordDTO(dto.getId(), encUserNewPwd);
        cbbChangeUserPasswordDTO.setSubSystem(SubSystem.CDC);
        // 更新用户密码
        return cbbUserAPI.changeUserPassword(cbbChangeUserPasswordDTO);
    }

    @Override
    public void resetUserPwdSyncAdminPwd(ModifyPasswordDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto must not null");

        LOGGER.info("修改用户[{}]密码同步修改管理员密码，needUpdatePassword=[{}]", dto.getId(), dto.getNeedUpdatePassword());
        // 查询用户信息
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(dto.getId());
        LOGGER.info("查询到的用户信息:{}", JSON.toJSONString(userDetail));
        String encNewPwd = AesUtil.encrypt(dto.getNewPwd(), RedLineUtil.getRealUserRedLine());
        IacChangeUserPasswordDTO cbbChangeUserPasswordDTO = new IacChangeUserPasswordDTO(dto.getId(), encNewPwd);
        Boolean shouldUpdatePwd = false;
        if (IacUserTypeEnum.THIRD_PARTY != userDetail.getUserType()) {
            shouldUpdatePwd = certificationStrategyParameterAPI.isNeedUpdatePassword(dto.getNewPwd());;
        }
        cbbChangeUserPasswordDTO.setShouldChangePassword(shouldUpdatePwd);
        LOGGER.info("重置普通用户[{}]是否需要需要修改密码结果为：[{}]", userDetail.getUserName(), shouldUpdatePwd);

        cbbChangeUserPasswordDTO.setSubSystem(SubSystem.CDC);
        // 重置用户密码
        cbbUserAPI.resetPassword(cbbChangeUserPasswordDTO);
    }

    private void modifyAdminPassword(ModifyPasswordDTO modifyPasswordDTO, String userName) throws BusinessException {
        IacAdminDTO baseAdminDTO;
        try {
            baseAdminDTO = adminMgmtAPI.getAdminByUserName(userName);
        } catch (BusinessException e) {
            LOGGER.info(String.format("用户[%s]不存在对应对应管理员，无需同步修改密码，错误信息为：[%s]", userName, e.getI18nMessage()));
            return;
        }
        // 是否需要强制修改密码
        if (modifyPasswordDTO.getNeedUpdatePassword()) {
            LOGGER.info("用户[{}]密码不符合管理员密码复杂度，管理员首次登录需要修改密码", userName);
            // 查询管理员信息
            IacModifyOtherAdminPwdRequest baseRequest = new IacModifyOtherAdminPwdRequest();
            baseRequest.setNewPwd(AesUtil.encrypt(modifyPasswordDTO.getNewPwd(), RedLineUtil.getRealAdminRedLine()));
            baseRequest.setId(baseAdminDTO.getId());

            // 平台提供修改强制密码标志位
            baseAdminMgmtAPI.modifyOtherAdminPwd(baseRequest);
        } else {
            LOGGER.info("用户[{}]密码符合管理员密码复杂度，管理员首次登录不需要修改密码", userName);
            IacModifyAdminPwdRequest baseRequest = new IacModifyAdminPwdRequest();
            baseRequest.setOldPwd(AesUtil.encrypt(modifyPasswordDTO.getOldPwd(), RedLineUtil.getRealAdminRedLine()));
            baseRequest.setNewPwd(AesUtil.encrypt(modifyPasswordDTO.getNewPwd(), RedLineUtil.getRealAdminRedLine()));
            baseRequest.setId(baseAdminDTO.getId());
            baseRequest.setSubSystem(SubSystem.CDC);

            // 修改管理员密码
            baseAdminMgmtAPI.modifyAdminPwd(baseRequest);
        }
    }


    @Override
    public void modifyAdminPasswordSynUserPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException {
        Assert.notNull(resetPasswordDTO, "baseRequest can not be null");

        LOGGER.info("修改管理员自身密码同步修改用户密码");
        IacModifyAdminPwdRequest baseRequest = new IacModifyAdminPwdRequest();
        baseRequest.setId(resetPasswordDTO.getId());
        baseRequest.setNewPwd(AesUtil.encrypt(resetPasswordDTO.getNewPwd(), RedLineUtil.getRealAdminRedLine()));
        baseRequest.setOldPwd(AesUtil.encrypt(resetPasswordDTO.getOldPwd(), RedLineUtil.getRealAdminRedLine()));
        baseRequest.setSubSystem(SubSystem.CDC);
        // 获取管理员信息
        IacAdminDTO admin = baseAdminMgmtAPI.getAdmin(baseRequest.getId());
        // 更新管理员密码
        baseAdminMgmtAPI.modifyAdminPwd(baseRequest);

        // 用户信息 根据管理员名称
        IacUserDetailDTO userDetail = cbbUserAPI.getUserByName(admin.getUserName());
        LOGGER.info("管理员[{}]查询用户信息为：[{}]", admin.getUserName(), JSON.toJSONString(userDetail));

        // 用户不为空 并且管理员标识存在
        if (userDetail != null && UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole())) {
            LOGGER.info("管理员[{}]为用户，需要同步修改用户密码", admin.getUserName());
            if (IacUserTypeEnum.AD == userDetail.getUserType() || IacUserTypeEnum.LDAP == userDetail.getUserType()) {
                LOGGER.info("管理员[{}]关联的用户类型是AD用户，不支持修改密码", admin.getUserName());
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_USER_IS_AD_OR_LDAP_NOT_ALLOW_EDIT_PASSWAORD);
            }
            // 构造用户重置密码请求
            IacChangeUserPasswordDTO dto = new IacChangeUserPasswordDTO(userDetail.getId(),
                    AesUtil.encrypt(resetPasswordDTO.getNewPwd(), RedLineUtil.getRealUserRedLine()));
            dto.setSubSystem(SubSystem.CDC);
            // 进行管理员密码修改
            cbbUserAPI.changeUserPassword(dto);
        }
    }



    @Override
    public void modifyOtherAdminPwdSyncUserPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException {
        Assert.notNull(resetPasswordDTO, "baseRequest can not be null");

        IacModifyOtherAdminPwdRequest baseRequest = new IacModifyOtherAdminPwdRequest();
        baseRequest.setId(resetPasswordDTO.getId());
        baseRequest.setNewPwd(AesUtil.encrypt(resetPasswordDTO.getNewPwd(), RedLineUtil.getRealAdminRedLine()));
        // 获取管理员信息
        IacAdminDTO admin = baseAdminMgmtAPI.getAdmin(baseRequest.getId());
        // 用户信息 根据管理员名称
        IacUserDetailDTO userDetail = cbbUserAPI.getUserByName(admin.getUserName());
        // 更新管理员密码
        baseAdminMgmtAPI.modifyOtherAdminPwd(baseRequest);

        // 用户不为空 并且管理员标识存在
        if (userDetail != null && UserRoleEnum.ADMIN.name().equals(userDetail.getUserRole())) {
            LOGGER.info("管理员[{}]为用户，需要同步修改用户密码", admin.getUserName());
            if (IacUserTypeEnum.AD == userDetail.getUserType() || IacUserTypeEnum.LDAP == userDetail.getUserType()) {
                LOGGER.info("管理员[{}]关联的用户类型是AD用户，不支持重置密码", admin.getUserName());
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_USER_IS_AD_OR_LDAP_NOT_ALLOW_RESET_PASSWAORD);
            }
            if (IacUserTypeEnum.THIRD_PARTY == userDetail.getUserType()) {
                LOGGER.info("管理员[{}]关联的用户类型是第三方用户，不支持重置密码", admin.getUserName());
                throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_USER_IS_THIRD_PARTY_NOT_ALLOW_RESET_PASSWORD);
            }

            // 构造用户重置密码请求
            IacChangeUserPasswordDTO dto = new IacChangeUserPasswordDTO(userDetail.getId(), resetPasswordDTO.getNewPwd());
            Boolean shouldUpdatePwd = certificationStrategyParameterAPI.isNeedUpdatePassword(resetPasswordDTO.getNewPwd());
            dto.setShouldChangePassword(shouldUpdatePwd);
            // 进行用户密码重置
            dto.setSubSystem(SubSystem.CDC);
            cbbUserAPI.resetPassword(dto);
        }
    }
}
