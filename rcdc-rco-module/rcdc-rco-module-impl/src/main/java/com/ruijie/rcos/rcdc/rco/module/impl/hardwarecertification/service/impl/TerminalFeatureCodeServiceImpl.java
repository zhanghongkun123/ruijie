package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.HardwareCertificationBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.dao.TerminalFeatureCodeDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.entity.TerminalFeatureCodeEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service.TerminalFeatureCodeService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;

/**
 *
 * Description: 终端特征码关联表service服务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
@Service
public class TerminalFeatureCodeServiceImpl implements TerminalFeatureCodeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalFeatureCodeServiceImpl.class);

    /** 全局表序列记录的key值 */
    private static final String FEATURE_CODE_SEQUENCE = "feature_code_sequence";

    private static final String FEATURE_PREFIX_APP = "APP";

    private static final String FEATURE_PREFIX_ROS = "ROS";

    private static final String FEATURE_FORMAT = "%09x";

    private static final int FEATURE_CODE_MAX_LEN = 9;

    private static final int FAIL_TRY_COUNT = 5;

    @Autowired
    private TerminalFeatureCodeDAO terminalFeatureCodeDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Override
    public String saveAndGetFeatureCode(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId can not be null");
        TerminalFeatureCodeEntity entity = terminalFeatureCodeDAO.getOneByTerminalId(terminalId);
        if (entity == null) {
            CbbTerminalBasicInfoDTO basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            entity = buildTerminalFeatureCodeEntity(terminalId);
            trySaveTerminalFeatureCode(entity, basicInfoDTO.getTerminalPlatform());
            LOGGER.info("新增终端特征码，terminalId[{}], featureCode[{}]", terminalId, entity.getFeatureCode());
        }
        return entity.getFeatureCode();
    }

    @Override
    public void deleteByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId can not be null");
        LOGGER.warn("删除终端特征码，terminalId[{}]", terminalId);
        terminalFeatureCodeDAO.deleteByTerminalId(terminalId);
    }

    private void trySaveTerminalFeatureCode(TerminalFeatureCodeEntity entity, CbbTerminalPlatformEnums platform) throws BusinessException {
        LockableExecutor.executeWithTryLock(FEATURE_CODE_SEQUENCE + entity.getTerminalId(), () -> {
            // 再查一次
            TerminalFeatureCodeEntity featureCodeEntity = terminalFeatureCodeDAO.getOneByTerminalId(entity.getTerminalId());
            if (featureCodeEntity != null) {
                entity.setFeatureCode(featureCodeEntity.getFeatureCode());
                return;
            }
            entity.setFeatureCode(computeFeatureCode(platform));
            // 保存信息
            boolean isSaveSuccess = saveTerminalFeatureCode(entity);
            int count = 0;
            // 失败，尝试5次
            while (!isSaveSuccess && count++ < FAIL_TRY_COUNT) {
                LOGGER.error("开始第{}次保存终端特征码信息，terminalId=[{}]", count, entity.getTerminalId());
                entity.setFeatureCode(computeFeatureCode(platform));
                isSaveSuccess = saveTerminalFeatureCode(entity);
            }
            if (!isSaveSuccess) {
                LOGGER.error("保存终端特征码信息失败，terminalId=[{}]", entity.getTerminalId());
                throw new BusinessException(HardwareCertificationBusinessKey.TERMINAL_FEATURE_CODE_TRY_SAVE_FAIL);
            }
        }, 1);
    }

    private boolean saveTerminalFeatureCode(TerminalFeatureCodeEntity entity) {
        try {
            terminalFeatureCodeDAO.save(entity);
            return true;
        } catch (Exception e) {
            LOGGER.error("保存终端特征码信息失败！将进行重试", e);
            return false;
        }
    }

    private TerminalFeatureCodeEntity buildTerminalFeatureCodeEntity(String terminalId) {
        TerminalFeatureCodeEntity entity = new TerminalFeatureCodeEntity();
        entity.setTerminalId(terminalId);
        entity.setCreateTime(new Date());
        return entity;
    }

    /**
     * 获取特征码并修改序列值
     *
     * @param platform 终端平台类型
     * @return 特征码
     */
    private synchronized String computeFeatureCode(CbbTerminalPlatformEnums platform) {
        StringBuilder code = new StringBuilder(platform == CbbTerminalPlatformEnums.APP ? FEATURE_PREFIX_APP : FEATURE_PREFIX_ROS);
        String sequence = globalParameterAPI.findParameter(FEATURE_CODE_SEQUENCE);
        // 16进制字符串转10进制数字
        long num = Long.parseLong(sequence, 16) + 1;
        String hex = Long.toHexString(num);
        if (hex.length() > FEATURE_CODE_MAX_LEN) {
            num = 1;
            hex = Long.toHexString(num);
        }
        globalParameterAPI.updateParameter(FEATURE_CODE_SEQUENCE, hex);
        return code.append(String.format(FEATURE_FORMAT, num)).toString().toUpperCase();
    }
}
