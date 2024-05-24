package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.rco.module.def.aaa.dto.ModifyPasswordDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/5
 *
 * @author linrenjian
 */

public interface ModifyPasswordService {

    /**
     * 修改用户密码，同步更新管理员密码
     * 
     * @param resetPasswordDTO resetPasswordDTO
     * @return boolean
     * @throws BusinessException BusinessException
     */
    boolean modifUserPwdSyncAdminPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException;

    /**
     * 重置用户密码，并且同步更新管理员密码
     *
     * @param modifyPasswordDTO 修改密码DTO类
     * @throws BusinessException 业务异常
     */
    void resetUserPwdSyncAdminPwd(ModifyPasswordDTO modifyPasswordDTO) throws BusinessException;


    /**
     * 重置用户密码，同步更新管理员密码
     *
     * @param resetPasswordDTO resetPasswordDTO
     * @throws BusinessException BusinessException
     */
    void modifyAdminPasswordSynUserPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException;

    /**
     * 重置用户密码，同步更新管理员密码
     *
     * @param resetPasswordDTO resetPasswordDTO
     * @throws BusinessException BusinessException
     */
    void modifyOtherAdminPwdSyncUserPwd(ModifyPasswordDTO resetPasswordDTO) throws BusinessException;
}
