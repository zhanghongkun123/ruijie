package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopVisitorConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopVisitorConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 用户模块事务操作实现类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月18日
 *
 * @author zhuangchenwu
 */
@Service("rcoUserServiceTxImpl")
public class UserServiceTxImpl implements UserServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceTxImpl.class);


    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private DesktopVisitorConfigDAO desktopVisitorConfigDAO;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;


    @PersistenceContext(unitName = "datasourceRootDefaultEntityManager")
    private EntityManager entityManager;

    @Override
    public void updateRecoverDeskData(CbbDeskDTO cbbDeskDTO, IacUserDetailDTO userDetail, String desktopName) throws BusinessException {
        Assert.notNull(cbbDeskDTO, "cbbDeskDTO must not be null");
        Assert.notNull(userDetail, "userDetail must not be null");
        Assert.hasText(desktopName, "desktopName must not be empyt");
        UUID userId = userDetail.getId();
        Assert.notNull(userDetail, "userEntity must not be null, userId = [" + userId + "]");

        UUID desktopId = cbbDeskDTO.getDeskId();
        // 更新用户-桌面关系表
        updateUserDesktop(desktopId, desktopName, userId);
        // 更新访客用户桌面配置表
        updateUserDesktopVisitorConfig(userDetail, cbbDeskDTO);
    }

    private void updateUserDesktop(UUID cbbDesktopId, String newDesktopName, UUID userId) throws BusinessException {
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(cbbDesktopId);
        Assert.notNull(userDesktopEntity, "userDesktopEntity must not be null, cbbDesktopId = [" + cbbDesktopId + "]");

        userDesktopEntity.setDesktopName(newDesktopName);
        userDesktopEntity.setUserId(userId);
        userDesktopDAO.save(userDesktopEntity);
    }

    private void updateUserDesktopVisitorConfig(IacUserDetailDTO userDetail, CbbDeskDTO cbbDeskDTO)
            throws BusinessException {
        if (userDetail.getUserType() != IacUserTypeEnum.VISITOR) {
            return;
        }
        DesktopVisitorConfigEntity visitorConfigEntity = desktopVisitorConfigDAO.getOne(userDetail.getId());
        if (visitorConfigEntity == null) {
            visitorConfigEntity = new DesktopVisitorConfigEntity();
            visitorConfigEntity.setUserId(userDetail.getId());
            visitorConfigEntity.setUserName(userDetail.getUserName());
            visitorConfigEntity.setCreateTime(new Date());
        }
        visitorConfigEntity.setImageTemplateId(cbbDeskDTO.getImageTemplateId());
        visitorConfigEntity.setStrategyId(cbbDeskDTO.getStrategyId());
        visitorConfigEntity.setNetworkId(cbbDeskDTO.getNetworkId());
        RcoDeskInfoEntity rcoDeskInfoEntity = rcoDeskInfoDAO.findByDeskId(cbbDeskDTO.getDeskId());
        if (rcoDeskInfoEntity != null && rcoDeskInfoEntity.getSoftwareStrategyId() != null) {
            visitorConfigEntity.setSoftwareStrategyId(rcoDeskInfoEntity.getSoftwareStrategyId());
        }
        desktopVisitorConfigDAO.save(visitorConfigEntity);
    }

    @Override
    public void deleteDeskIDVRelativeData(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId不能为null");

        // 删除用户和云桌面的关联关系
        userDesktopDAO.deleteByCbbDesktopId(desktopId);
        // 更新用户和终端的绑定关系
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findByBindDeskId(desktopId);
        if (null != userTerminalEntity) {
            userTerminalEntity.setBindUserId(null);
            userTerminalEntity.setBindUserName(null);
            userTerminalEntity.setBindUserTime(null);
            userTerminalEntity.setBindDeskId(null);
            userTerminalEntity.setTerminalMode(IdvTerminalModeEnums.UNKNOWN);
            userTerminalDAO.save(userTerminalEntity);
        }
    }
}
