package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 *
 * @author wjp
 */
public interface CmsDockingAPI {

    /**
     * cmsServer 发起请求，批量获取用户、用户组、管理员信息
     *
     * @param getInfoRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    GetInfoResponse getInfo(GetInfoRequest getInfoRequest) throws BusinessException;

    /**
     * 获取跳转到CMS页面随机令牌
     *
     * @param baseAdminRequest 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    RandomTokenResponse getRandomToken(BaseAdminRequest baseAdminRequest) throws BusinessException;

    /**
     * CMS管理员登入验证：token验证
     *
     * @param verifAdminRequest token验证入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    VerifAdminResponse verifAdmin(VerifAdminRequest verifAdminRequest) throws BusinessException;

    /**
     * CMS对接：管理员登入认证
     *
     * @param loginAdminRequest 管理员登入认证入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    LoginAdminResponse loginAdmin(LoginAdminRequest loginAdminRequest) throws BusinessException;

    /**
     * CMS对接：用户登入认证
     *
     * @param loginUserRequest 用户认证入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    LoginUserResponse loginUser(LoginUserRequest loginUserRequest) throws BusinessException;


    /**
     * CMS对接：全量同步管理员
     *
     * @param syncAdminRequest 入参
     * @return 响应
     */
    DefaultResponse syncAdmin(SyncAdminRequest syncAdminRequest);


    /**
     * CMS对接：修改用户密码
     * @param modifyUserPwdRequest 修改用户密码请求
     * @return 响应结果
     * @throws BusinessException 业务异常
     */
    ModifyUserPwdResponse modifyUserPwd(ModifyUserPwdRequest modifyUserPwdRequest) throws BusinessException;
}
