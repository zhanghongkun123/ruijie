package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalGroupService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Description: 终端分组service
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月20日
 *
 * @author nt
 */
@Service("rcoTerminalGroupServiceImpl")
public class TerminalGroupServiceImpl implements TerminalGroupService {

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Override
    public String[] getTerminalGroupNameArr(UUID groupId) throws BusinessException {
        Assert.notNull(groupId, "groupId can not be null");
        CbbTerminalGroupDetailDTO terminalGroupInfo = getTerminalGroupDTO(groupId);
        List<String> groupNameList = new ArrayList<>();
        groupNameList.add(terminalGroupInfo.getGroupName());
        UUID parentId = terminalGroupInfo.getParentGroupId();
        while (parentId != null) {
            terminalGroupInfo = getTerminalGroupDTO(parentId);
            groupNameList.add(terminalGroupInfo.getGroupName());
            parentId = terminalGroupInfo.getParentGroupId();
        }
        String[] groupNameArr = new String[groupNameList.size()];
        Collections.reverse(groupNameList);
        groupNameList.toArray(groupNameArr);
        return groupNameArr;
    }

    private CbbTerminalGroupDetailDTO getTerminalGroupDTO(UUID groupId) throws BusinessException {

        return cbbTerminalGroupMgmtAPI.loadById(groupId);
    }
}
