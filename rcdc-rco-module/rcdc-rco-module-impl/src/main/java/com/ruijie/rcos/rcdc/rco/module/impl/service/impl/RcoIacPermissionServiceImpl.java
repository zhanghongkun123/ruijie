package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPermissionMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.permission.IacEnablePermissionRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoIacPermissionService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月27日
 *
 * @author zdc
 */
@Service
public class RcoIacPermissionServiceImpl implements RcoIacPermissionService {

    /**
     *日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RcoIacPermissionServiceImpl.class);

    @Autowired
    private IacPermissionMgmtAPI iacPermissionMgmtAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public void notifyAllMenuGlobalEnable(@Nullable String key) {
        notifyAllMenuGlobalEnableToIac(key);
    }

    private void notifyAllMenuGlobalEnableToIac(String key) {

        try {
            // 查询所有权限
            List<IacPermissionDTO> iacPermissionList = iacPermissionMgmtAPI.listAllPermissionBySubSystem(SubSystem.CDC);
            // 获取所有开启了全局开关的key
            iacPermissionList.stream().forEach(dto -> {
                String globalEnableSearchKey = getGlobalEnableSearchKey(dto);
                if ((StringUtils.isBlank(key) && globalEnableSearchKey != null)) {
                    notifyMenuGlobalEnableToIac(globalEnableSearchKey, dto.getPermissionCode());
                    return;
                }
                if (StringUtils.isNotBlank(key) && Objects.equals(key, globalEnableSearchKey)) {
                    notifyMenuGlobalEnableToIac(globalEnableSearchKey, dto.getPermissionCode());
                    return;
                }
            });
        } catch (Exception e) {
            LOGGER.error("发送menu全局开关配置失败", e);
        }
    }

    /**
     * 获取配置了业务全局开关-对应的key
     *
     * @param dto
     * @return
     */
    private String getGlobalEnableSearchKey(IacPermissionDTO dto) {
        String globalEnableSearchKey = null;
        JSONObject jsonObject = (JSONObject) dto.getTags();
        if (jsonObject == null) {
            //没有设置自定义标签，直接返回空
            return null;
        }
        Object globalEnableSearch = jsonObject.get(FunTypes.GLOBAL_ENABLE_SEARCH);
        if (Objects.nonNull(globalEnableSearch)
                && FunTypes.YES.equals(String.valueOf(globalEnableSearch))
                && Objects.nonNull(jsonObject.get(FunTypes.GLOBAL_ENABLE_SEARCH_KEY))) {
            globalEnableSearchKey = String.valueOf(jsonObject.get(FunTypes.GLOBAL_ENABLE_SEARCH_KEY));

        }
        return globalEnableSearchKey;
    }

    private void notifyMenuGlobalEnableToIac(String globalEnableSearchKey, String permissionCode) {
        try {
            String parameter = globalParameterService.findParameter(globalEnableSearchKey);
            IacEnablePermissionRequest request = new IacEnablePermissionRequest();
            request.setPermissionCodeList(Collections.singletonList(permissionCode));
            request.setEnable(BooleanUtils.toBoolean(parameter));
            request.setSubSystem(SubSystem.CDC);
            iacPermissionMgmtAPI.enablePermission(request);
        } catch (Exception e) {
            LOGGER.error("发送权限{}失败，globalEnableSearchKey：{}", permissionCode, globalEnableSearchKey, e);
        }
    }
}
