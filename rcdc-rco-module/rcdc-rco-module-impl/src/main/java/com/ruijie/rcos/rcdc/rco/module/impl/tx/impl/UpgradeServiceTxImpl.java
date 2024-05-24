package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import static com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants.CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_ENABLE_FOREVER;
import static com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants.CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_LEVEL;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationStrategyLevelEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.constants.CertificationStrategyConstants;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.AuthenticationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.AuthenticationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UpgradeServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户登录认证信息事务实现类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/16 22:51
 *
 * @author yxq
 */
@Service
public class UpgradeServiceTxImpl implements UpgradeServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeServiceTxImpl.class);

    /**
     * S2版本中安全红线各个配置的KEY
     */
    private static final String LOCK_TIME = "lockTime";

    private static final String LOCK_ERROR_TIMES = "lockedErrorTimes";

    private static final String PREVENTS_BRUTE_FORCE = "preventsBruteForce";

    private static final String PWD_LEVEL = "pwdLevel";

    private static final String SECURITY_STRATEGY_ENABLE = "securityStrategyEnable";

    private static final String UPDATE_DAYS = "updateDays";

    private static final String ENABLE_FORCE_UPDATE_PASSWORD = "enableForceUpdatePassword";

    private static final String ENABLE_PASSWORD_BLACKLIST = "enablePasswordBlacklist";

    /**
     * 一分钟有60秒
     */
    private static final long ONE_MINUTE_SECONDS = 60L;

    @Autowired
    private AuthenticationDAO authenticationDAO;

    @Autowired
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private UserService userService;

    @Override
    public void upgradeFromS2(List<AuthenticationEntity> authenticationEntityList) throws BusinessException {
        Assert.notNull(authenticationEntityList, "authenticationEntityList must not null");

        upgradeFromS2WithoutGlobalValue(authenticationEntityList);

        // 修改全局表中S2字段的数据
        globalParameterService.updateParameter(Constants.RCDC_RCO_S2_VERSION, Boolean.FALSE.toString());
    }

    @Override
    public void upgradeFromS2WithoutGlobalValue(List<AuthenticationEntity> authenticationEntityList) throws BusinessException {
        Assert.notNull(authenticationEntityList, "authenticationEntityList must not null");
        LOGGER.info("从S2版本进行升级");

        // 从S2版本升级，默认开启评测功能
        evaluationUpgradeOperate();

        // 修改安全红线策略信息
        LOGGER.info("从S2版本升级，修改安全红线策略");
        RcoGlobalParameterEntity globalParameterEntity = buildRcoGlobalParameterEntity();
        rcoGlobalParameterDAO.save(globalParameterEntity);
        LOGGER.info("将用户信息保存到新的表中");
        authenticationDAO.saveAll(authenticationEntityList);
        LOGGER.info("用户信息保存成功，保存S2版本安全红线配置");

    }

    @Override
    public void upgradeFromOtherVersion() throws BusinessException {
        LOGGER.info("从其他版本进行升级，安全红线策略信息为空，插入默认策略");
        Date createTime = new Date();
        RcoGlobalParameterEntity globalParameterEntity = new RcoGlobalParameterEntity();
        globalParameterEntity.setParamKey(CertificationStrategyConstants.CERTIFICATION_STRATEGY_SUMMARY);
        globalParameterEntity.setParamValue(JSON.toJSONString(buildCurrentPwdStrategyDTO()));
        globalParameterEntity.setDefaultValue(JSON.toJSONString(buildDefaultPwdStrategyDTO()));
        globalParameterEntity.setCreateTime(createTime);
        globalParameterEntity.setUpdateTime(createTime);
        LOGGER.info("生成的安全红线策略为：[{}]", JSON.toJSONString(globalParameterEntity));

        globalParameterService.saveParameter(globalParameterEntity);
    }

    @Override
    public void addEnableForceUpdatePassword() {

        try {
            RcoGlobalParameterEntity globalParameterEntity =
                    rcoGlobalParameterDAO.findByParamKey(CertificationStrategyConstants.CERTIFICATION_STRATEGY_SUMMARY);
            LOGGER.info("添加EnableForceUpdatePassword字段，原始策略为：[{}]", JSON.toJSONString(globalParameterEntity));

            String paramValue = globalParameterEntity.getParamValue();
            JSONObject jsonObject = JSON.parseObject(paramValue);
            if (Objects.nonNull(jsonObject.getBoolean(ENABLE_FORCE_UPDATE_PASSWORD))) {
                LOGGER.info("安全红线全局表信息已包含[enableForceUpdatePassword]");
                return;
            }
            PwdStrategyDTO dto = JSON.parseObject(paramValue, PwdStrategyDTO.class);
            dto.setEnableForceUpdatePassword(
                    CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_FIRST_FORCE_UPDATE_PASSWORD_DEFAULT);

            PwdStrategyDTO defaultPwdStrategyDTO = buildDefaultPwdStrategyDTO();
            globalParameterEntity.setParamValue(JSON.toJSONString(dto));
            globalParameterEntity.setDefaultValue(JSON.toJSONString(defaultPwdStrategyDTO));
            globalParameterEntity.setUpdateTime(new Date());
            rcoGlobalParameterDAO.save(globalParameterEntity);
            LOGGER.info("添加EnableForceUpdatePassword字段，构建安全红线全局表信息为：[{}]", JSON.toJSONString(globalParameterEntity));
        } catch (Exception e) {
            LOGGER.error("添加EnableForceUpdatePassword字段异常", e);
        }
    }

    @Override
    public void upgradePwdLevel() {
        try {
            RcoGlobalParameterEntity globalParameterEntity =
                    rcoGlobalParameterDAO.findByParamKey(CertificationStrategyConstants.CERTIFICATION_STRATEGY_SUMMARY);
            LOGGER.info("更新密码强度策略，原始策略为：[{}]", JSON.toJSONString(globalParameterEntity));

            String paramValue = globalParameterEntity.getParamValue();
            JSONObject jsonObject = JSON.parseObject(paramValue);
            if (Objects.equals(Boolean.TRUE, jsonObject.getBoolean(SECURITY_STRATEGY_ENABLE))) {
                LOGGER.info("安全红线全局表信息已开启[securityStrategyEnable]");
                return;
            }
            PwdStrategyDTO dto = JSON.parseObject(paramValue, PwdStrategyDTO.class);
            dto.setSecurityStrategyEnable(RCDC_CERTIFICATION_STRATEGY_ENABLE_FOREVER);
            dto.setPwdLevel(RCDC_CERTIFICATION_STRATEGY_LEVEL);

            PwdStrategyDTO defaultPwdStrategyDTO = buildDefaultPwdStrategyDTO();
            globalParameterEntity.setParamValue(JSON.toJSONString(dto));
            globalParameterEntity.setDefaultValue(JSON.toJSONString(defaultPwdStrategyDTO));
            globalParameterEntity.setUpdateTime(new Date());
            LOGGER.info("更新密码强度策略，构建安全红线全局表信息为：[{}]", JSON.toJSONString(globalParameterEntity));
            rcoGlobalParameterDAO.save(globalParameterEntity);
        } catch (Exception e) {
            LOGGER.error("更新密码强度策略失败", e);
        }

    }

    /**
     * 构建当前安全红线策略
     *
     * @return 安全红线策略
     */
    private PwdStrategyDTO buildCurrentPwdStrategyDTO() {
        PwdStrategyDTO pwdStrategyDTO = buildDefaultPwdStrategyDTO();
        // 如果用户表不为空，则复杂度修改为低
        if (!userService.isRcoViewUserEntityEmpty()) {
            LOGGER.info("低版本用户不为空，升级时修改密码等级为低");
            pwdStrategyDTO.setPwdLevel(CertificationStrategyLevelEnum.LEVEL_ONE.getLevel());
        }
        LOGGER.info("从其他版本升级到5.4，构建安全红线默认策略为：[{}]", JSON.toJSONString(pwdStrategyDTO));

        return pwdStrategyDTO;
    }

    /**
     * 向全局表中插入评测功能默认策略，如果从S2版本升级，则设置为TRUE，否则设置为FALSE
     */
    private void evaluationUpgradeOperate() {
        globalParameterService.updateParameter(Constants.EVALUATION_STRATEGY, Boolean.TRUE.toString());
        LOGGER.info("从S2版本升级，默认开启评测功能");
    }

    private RcoGlobalParameterEntity buildRcoGlobalParameterEntity() {
        RcoGlobalParameterEntity globalParameterEntity =
                rcoGlobalParameterDAO.findByParamKey(CertificationStrategyConstants.CERTIFICATION_STRATEGY_SUMMARY);
        String paramValue = globalParameterEntity.getParamValue();
        PwdStrategyDTO pwdStrategyDTO = buildPwdStrategyDTO(paramValue);
        PwdStrategyDTO defaultPwdStrategyDTO = buildDefaultPwdStrategyDTO();
        globalParameterEntity.setParamValue(JSON.toJSONString(pwdStrategyDTO));
        globalParameterEntity.setDefaultValue(JSON.toJSONString(defaultPwdStrategyDTO));
        globalParameterEntity.setUpdateTime(new Date());
        LOGGER.info("从S2版本升级，构建安全红线全局表信息为：[{}]", JSON.toJSONString(globalParameterEntity));

        return globalParameterEntity;
    }

    /**
     * 根据S2版本的安全红线策略信息，生成新的安全红线策略信息
     * 
     * @param paramValue S2版本
     * @return 安全红线策略
     */
    private PwdStrategyDTO buildPwdStrategyDTO(String paramValue) {
        LOGGER.info("从S2版本升级，构建安全红线配置，原始策略为：[{}]", paramValue);

        JSONObject jsonObject = JSON.parseObject(paramValue);
        PwdStrategyDTO pwdStrategyDTO = new PwdStrategyDTO();
        pwdStrategyDTO.setUserLockTime(jsonObject.getInteger(LOCK_TIME));
        pwdStrategyDTO.setUserLockedErrorTimes(jsonObject.getInteger(LOCK_ERROR_TIMES));
        pwdStrategyDTO.setPreventsBruteForce(jsonObject.getBoolean(PREVENTS_BRUTE_FORCE));
        pwdStrategyDTO.setPwdLevel(jsonObject.getString(PWD_LEVEL));
        pwdStrategyDTO.setSecurityStrategyEnable(jsonObject.getBoolean(SECURITY_STRATEGY_ENABLE));
        Integer originalUpdateDays = jsonObject.getInteger(UPDATE_DAYS);
        // S2版本永久时为0，5.4改为永久时为-1
        if (originalUpdateDays == 0) {
            pwdStrategyDTO.setUpdateDays(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_UPDATE_DATE);
            pwdStrategyDTO.setPwdExpireRemindDays(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_PWD_EXPIRE_REMIND_DAY);
        } else {
            pwdStrategyDTO.setUpdateDays(originalUpdateDays);
            pwdStrategyDTO.setPwdExpireRemindDays(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_PWD_EXPIRE_REMIND_DAYS_DEFAULT);
        }
        pwdStrategyDTO.setAdminLockTime(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_ADMIN_LOCK_TIME_DEFAULT);
        pwdStrategyDTO.setAdminLockedErrorTimes(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_ADMIN_ERROR_TIMES);
        pwdStrategyDTO.setTerminalLockTime(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_TERMINAL_LOCK_TIME_DEFAULT);
        pwdStrategyDTO.setTerminalLockedErrorTimes(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_TERMINAL_ERROR_TIMES);
        Boolean isEnableForceUpdatePassword = jsonObject.getBoolean(ENABLE_FORCE_UPDATE_PASSWORD);
        pwdStrategyDTO.setEnableForceUpdatePassword(isEnableForceUpdatePassword == null ? Boolean.TRUE : isEnableForceUpdatePassword);
        LOGGER.info("构建的新安全红线策略为：[{}]", JSON.toJSONString(pwdStrategyDTO));

        return pwdStrategyDTO;
    }

    private PwdStrategyDTO buildDefaultPwdStrategyDTO() {
        LOGGER.info("从其他版本升级到5.4，构建安全红线默认策略");

        PwdStrategyDTO pwdStrategyDTO = new PwdStrategyDTO();
        pwdStrategyDTO.setUserLockTime(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_USER_LOCK_TIME_DEFAULT);
        pwdStrategyDTO.setUserLockedErrorTimes(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_USER_ERROR_TIMES);
        pwdStrategyDTO.setPreventsBruteForce(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_PREVENTS_BRUTE_FORCE);
        pwdStrategyDTO.setPwdLevel(RCDC_CERTIFICATION_STRATEGY_LEVEL);
        pwdStrategyDTO.setSecurityStrategyEnable(RCDC_CERTIFICATION_STRATEGY_ENABLE_FOREVER);
        pwdStrategyDTO.setUpdateDays(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_UPDATE_DATE);
        pwdStrategyDTO.setPwdExpireRemindDays(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_PWD_EXPIRE_REMIND_DAY);
        pwdStrategyDTO.setAdminLockTime(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_ADMIN_LOCK_TIME_DEFAULT);
        pwdStrategyDTO.setAdminLockedErrorTimes(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_ADMIN_ERROR_TIMES);
        pwdStrategyDTO.setTerminalLockTime(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_TERMINAL_LOCK_TIME_DEFAULT);
        pwdStrategyDTO.setTerminalLockedErrorTimes(CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_TERMINAL_ERROR_TIMES);
        pwdStrategyDTO.setEnableForceUpdatePassword(
                CertificationStrategyConstants.RCDC_CERTIFICATION_STRATEGY_FIRST_FORCE_UPDATE_PASSWORD_DEFAULT);

        return pwdStrategyDTO;
    }

}
