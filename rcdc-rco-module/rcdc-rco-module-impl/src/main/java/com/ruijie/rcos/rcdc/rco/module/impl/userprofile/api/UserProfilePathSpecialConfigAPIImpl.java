package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfilePathSpecialConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.special.dto.SpecialConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.special.api.AbstractSpecialConfigAPIImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.special.dao.UserProfileSpecialConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.special.entity.UserProfileSpecialConfigEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * Description: 用户配置特殊配置API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/8
 *
 * @author zwf
 */
public class UserProfilePathSpecialConfigAPIImpl extends AbstractSpecialConfigAPIImpl implements UserProfilePathSpecialConfigAPI {
    @Autowired
    private UserProfileMgmtAPI userProfileMgmtAPI;

    @Autowired
    private UserProfileSpecialConfigDAO userProfileSpecialConfigDAO;

    @Override
    public String importUserProfilePathSpecialConfig(ChunkUploadFile file) throws BusinessException {
        Assert.notNull(file, "file can not be null");

        SpecialConfigDTO userProfileSpecialSpecialConfig = parseSpecialConfigFile(file);
        List<UserProfileSpecialConfigEntity> specialConfigEntityList = userProfileSpecialConfigDAO.findAll();

        UserProfileSpecialConfigEntity specialConfigEntity = new UserProfileSpecialConfigEntity();
        if (specialConfigEntityList.isEmpty()) {
            BeanUtils.copyProperties(userProfileSpecialSpecialConfig, specialConfigEntity);
        } else {
            UserProfileSpecialConfigEntity oldUserProfileSpecialConfig = specialConfigEntityList.get(0);
            oldUserProfileSpecialConfig .setConfigContent(userProfileSpecialSpecialConfig.getConfigContent());
            oldUserProfileSpecialConfig .setConfigMd5(userProfileSpecialSpecialConfig.getConfigMd5());
            oldUserProfileSpecialConfig .setConfigVersion(userProfileSpecialSpecialConfig.getConfigVersion());
            oldUserProfileSpecialConfig .setFileName(userProfileSpecialSpecialConfig.getFileName());
            BeanUtils.copyProperties(oldUserProfileSpecialConfig, specialConfigEntity);
        }
        specialConfigEntity.setCreateTime(new Date());
        userProfileSpecialConfigDAO.save(specialConfigEntity);

        return userProfileSpecialSpecialConfig.getConfigContent();
    }

    @Override
    public SpecialConfigDTO getUserProfilePathSpecialConfig() {
        SpecialConfigDTO specialConfigDTO = new SpecialConfigDTO();
        List<UserProfileSpecialConfigEntity> specialConfigEntityList = userProfileSpecialConfigDAO.findAll();
        if (!specialConfigEntityList.isEmpty()) {
            BeanUtils.copyProperties(specialConfigEntityList.get(0), specialConfigDTO);
        }
        return specialConfigDTO;
    }
}
