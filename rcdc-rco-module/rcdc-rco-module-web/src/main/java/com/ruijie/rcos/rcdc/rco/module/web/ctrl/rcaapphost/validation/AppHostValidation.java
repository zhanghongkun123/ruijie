package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.validation;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request.EditHostSpecWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * 应用池校验
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018 Company:
 * Ruijie Co., Ltd. Create Time: 2024年1月5日
 * 
 * @author zhengjingyong
 */
@Service
public class AppHostValidation {

    private static final double ZONE = 0.0;

    private static final Integer MEMORY_MIN = 1;

    private static final Integer MEMORY_MAX = 256;

    private static final Double MEMORY_STEP = 0.5;

    private static final Integer PERSON_DISK_MIN = 20;

    private static final Integer PERSON_DISK_MAX = 2048;


    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;


    /**
     * 验证
     *
     * @param request 规格参数
     * @throws BusinessException 业务异常
     */
    public void editAppHostSpecValidate(EditHostSpecWebRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        Double memory = request.getMemory();

        // 内存值的范围
        if (memory < MEMORY_MIN || memory > MEMORY_MAX) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_INVALID_MEM_VALUE);
        }

        double remainder = memory % MEMORY_STEP;
        if (remainder != ZONE) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_INVALID_MEM_VALUE_LIMIT);
        }

        // 开启个人盘时，校验个人盘大小
        if (request.getPersonalDisk() != null) {
            Integer personalDisk = request.getPersonalDisk();
            if (Objects.isNull(personalDisk) || personalDisk < PERSON_DISK_MIN || personalDisk > PERSON_DISK_MAX) {
                throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_INVALID_PERSON_DISK_VALUE_LIMIT);
            }
        }
    }
}
