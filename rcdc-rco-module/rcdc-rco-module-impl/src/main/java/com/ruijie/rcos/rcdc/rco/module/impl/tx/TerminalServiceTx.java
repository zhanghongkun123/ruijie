package com.ruijie.rcos.rcdc.rco.module.impl.tx;


import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 终端存在事物的操作
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年6月18日
 *
 * @author nt
 */
public interface TerminalServiceTx {

    /**
     * 删除终端
     *
     * @param terminal 终端实体对象
     */
    void deleteById(UserTerminalEntity terminal);

    /**
     * 用户变更
     *
     * @param userTerminalEntity 终端信息
     * @param userDetail         userDetail
     * @throws BusinessException 业务异常
     */
    void bindUser(UserTerminalEntity userTerminalEntity, IacUserDetailDTO userDetail) throws BusinessException;
}
