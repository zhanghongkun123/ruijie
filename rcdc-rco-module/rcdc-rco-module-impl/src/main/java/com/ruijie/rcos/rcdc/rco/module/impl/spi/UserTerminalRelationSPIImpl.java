package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.dto.UserTerminalRelationDTO;
import com.ruijie.rcos.rcdc.rca.module.def.spi.UserTerminalRelationSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/24 17:14
 *
 * @author zhangsiming
 */
public class UserTerminalRelationSPIImpl implements UserTerminalRelationSPI {

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Override
    public List<UserTerminalRelationDTO> getUserTerminalRelationListByUserList(List<UUID> userIdList,
                                                                               @Nullable CbbTerminalPlatformEnums terminalPlatform) {
        Assert.notNull(userIdList, "userIdList can not be null");
        List<UserTerminalEntity> userTerminalEntityList;
        if (terminalPlatform != null) {
            userTerminalEntityList = userTerminalDAO.findByTerminalPlatformAndUserIdIn(terminalPlatform, userIdList);
        } else {
            userTerminalEntityList = userTerminalDAO.findByUserIdIn(userIdList);
        }
        return userTerminalEntityList.stream().map(userTerminalEntity -> {
            UserTerminalRelationDTO userTerminalRelationDTO = new UserTerminalRelationDTO();
            userTerminalRelationDTO.setTerminalId(userTerminalEntity.getTerminalId());
            userTerminalRelationDTO.setUserId(userTerminalEntity.getUserId());
            return userTerminalRelationDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserTerminalRelationDTO> getAllLoginTerminalByPlatform(CbbTerminalPlatformEnums terminalPlatform) {
        Assert.notNull(terminalPlatform, "terminalPlatform can not be null");

        List<UserTerminalEntity> userTerminalEntityList = userTerminalDAO.findByTerminalPlatformAndHasLogin(CbbTerminalPlatformEnums.APP, true);
        return userTerminalEntityList.stream().map(userTerminalEntity -> {
            UserTerminalRelationDTO userTerminalRelationDTO = new UserTerminalRelationDTO();
            userTerminalRelationDTO.setTerminalId(userTerminalEntity.getTerminalId());
            userTerminalRelationDTO.setUserId(userTerminalEntity.getUserId());
            return userTerminalRelationDTO;
        }).collect(Collectors.toList());
    }
}
