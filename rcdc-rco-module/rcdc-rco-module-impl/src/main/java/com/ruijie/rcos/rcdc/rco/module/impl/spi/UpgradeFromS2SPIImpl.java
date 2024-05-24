package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants.CertificationStrategyConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserAuthenticationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AuthenticationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserAuthenticationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UpgradeServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 版本升级过程，进行安全红线相关信息处理以及评测功能的开关处理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/3 10:23
 *
 * @author yxq
 */
@DispatcherImplemetion("UpgradeFromS2SPIImpl")
public class UpgradeFromS2SPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeFromS2SPIImpl.class);

    /**
     * S2版本安全红线特有字段
     */
    private static final String LOCK_TIME = "lockTime";

    private static final String ENABLE_FORCE_UPDATE_PASSWORD = "enableForceUpdatePassword";

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private UpgradeServiceTx upgradeServiceTx;

    @Autowired
    private UserAuthenticationDAO userAuthenticationDAO;

    @Autowired
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    @Override
    public Boolean beforeEnteringMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        // 不需处理，直接响应成功
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) {
        // 不需处理，直接响应成功
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterMaintenanceEnd(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        Assert.hasText(dispatchKey, "dispatchKey must not be null or empty");
        Assert.notNull(baseUpgradeDTO, "baseUpgradeDTO must not be null");
        if (baseUpgradeDTO.getType() == BaseUpgradeDTO.UpgradeType.ONLINE) {
            LOGGER.info("当前为热补丁升级，无需执行");
            return Boolean.TRUE;
        }

        LOGGER.info("进行版本升级到5.4版本的升级处理");

        Boolean isUpgradeFromS2 = isUpgradeFromS2();
        if (isUpgradeFromS2) {
            LOGGER.info("全局表中有S2版本标识字段，从S2版本升级");
            // 进行数据迁移和保持S2版本配置
            List<AuthenticationEntity> authenticationEntityList = buildAuthenticationEntityList();
            upgradeServiceTx.upgradeFromS2(authenticationEntityList);
        } else {
            String parameter = globalParameterService.findParameter(CertificationStrategyConstants.CERTIFICATION_STRATEGY_SUMMARY);
            LOGGER.info("查询的全局策略信息为：[{}]", parameter);
            // 如果为空，说明没有安全红线策略相关信息，需要创建默认策略
            if (parameter == null) {
                LOGGER.info("安全红线策略信息为空，当前版本不是从S2版本升级，插入默认策略");
                upgradeServiceTx.upgradeFromOtherVersion();
                return Boolean.TRUE;
            }

            upgradeServiceTx.addEnableForceUpdatePassword();

            JSONObject jsonObject = JSON.parseObject(parameter);
            Integer lockTime = jsonObject.getInteger(LOCK_TIME);
            // 如果有这个字段，则说明是从S2版本升级
            if (lockTime != null) {
                LOGGER.info("从S2版本进行升级");
                List<AuthenticationEntity> authenticationEntityList = buildAuthenticationEntityList();
                upgradeServiceTx.upgradeFromS2WithoutGlobalValue(authenticationEntityList);
                return Boolean.TRUE;
            }

            LOGGER.info("当前版本已经有正确策略，无需修改");
        }
        upgradeServiceTx.upgradePwdLevel();
        return Boolean.TRUE;
    }

    private List<AuthenticationEntity> buildAuthenticationEntityList() {
        List<UserAuthenticationEntity> userAuthenticationEntityList = userAuthenticationDAO.findAll();
        Date createTime = new Date();
        return userAuthenticationEntityList.stream().map(user -> {
            AuthenticationEntity authenticationEntity = new AuthenticationEntity();
            BeanUtils.copyProperties(user, authenticationEntity);
            authenticationEntity.setResourceId(user.getUserId());
            authenticationEntity.setLock(user.getIsLock());
            authenticationEntity.setCreateTime(createTime);
            authenticationEntity.setUpdateTime(createTime);
            authenticationEntity.setType(CertificationTypeEnum.USER);
            return authenticationEntity;
        }).collect(Collectors.toList());
    }


    /**
     * 判断是否从S2版本升级上来
     *
     * @return 是否从S2版本升级
     */
    private Boolean isUpgradeFromS2() {
        LOGGER.info("检验是否从S2版本升级");
        String parameter = globalParameterService.findParameter(Constants.RCDC_RCO_S2_VERSION);
        LOGGER.info("全局表中S2版本字段的值为：{}", parameter);
        return Boolean.valueOf(parameter);
    }
}
