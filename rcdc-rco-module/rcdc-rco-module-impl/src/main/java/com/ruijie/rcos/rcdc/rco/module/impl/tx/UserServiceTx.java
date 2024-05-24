package com.ruijie.rcos.rcdc.rco.module.impl.tx;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 用户管理模块事务操作接口类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月18日
 *
 * @author zhuangchenwu
 */
public interface UserServiceTx {


    /**
     * 更新恢复桌面操作相关数据
     *
     * @param cbbDeskDTO  桌面DTO对象
     * @param userDetail  userDetail
     * @param desktopName 桌面名称
     * @throws BusinessException 业务异常
     */
    void updateRecoverDeskData(CbbDeskDTO cbbDeskDTO, IacUserDetailDTO userDetail, String desktopName) throws BusinessException;

    /**
     * 删除IDV云桌面的关联数据
     *
     * @param desktopId desktopId
     */
    void deleteDeskIDVRelativeData(UUID desktopId);

}
