package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.HostUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.HostUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.HostUserGroupBindDeskNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewHostUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewHostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryHostUserListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserQueryHelper;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/8
 *
 * @author zqj
 */
public class HostUserAPIImpl implements HostUserAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostUserAPIImpl.class);


    @Autowired
    private QueryHostUserListService queryHostUserListService;

    @Autowired
    private UserQueryHelper userQueryHelper;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private DesktopSessionServiceAPI desktopSessionServiceAPI;

    @Autowired
    private RcoViewHostUserDAO rcoViewHostUserDAO;


    @Override
    public DefaultPageResponse<UserListDTO> deskBindUserPage(PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        Page<RcoViewHostUserEntity> page = queryHostUserListService.pageQuery(request, RcoViewHostUserEntity.class);
        List<RcoViewHostUserEntity> entityList = page.getContent();
        List<RcoViewUserEntity> userEntityList = entityList.stream().map(s -> {
            RcoViewUserEntity rcoViewUserEntity = new RcoViewUserEntity();
            BeanUtils.copyProperties(s, rcoViewUserEntity);
            return rcoViewUserEntity;
        }).collect(Collectors.toList());
        UserListDTO[] dtoArr = userQueryHelper.convertRcoViewUserEntity2UserListDTO(userEntityList);
        DefaultPageResponse<UserListDTO> response = new DefaultPageResponse<>();
        response.setItemArr(dtoArr);
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Override
    public void removeById(UUID id) throws BusinessException {
        Assert.notNull(id, "request can not be null");
        HostUserEntity hostUserEntity = hostUserService.findById(id);
        if (hostUserEntity == null) {
            return;
        }
        DesktopSessionDTO desktopSessionDTO = desktopSessionServiceAPI.findByUserIdAndDeskId(hostUserEntity.getUserId(),
                hostUserEntity.getDesktopId());
        if (desktopSessionDTO != null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_HAS_SESSION_NOT_CANCEL);
        }
        hostUserService.removeById(id);
    }

    @Override
    public HostUserDTO findById(UUID id) {
        Assert.notNull(id, "id can not be null");
        HostUserEntity userEntity = hostUserService.findById(id);
        HostUserDTO hostUserDTO = new HostUserDTO();
        BeanUtils.copyProperties(userEntity, hostUserDTO);
        return hostUserDTO;
    }

    @Override
    public void unBindUser(UUID deskId, UUID userId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        Assert.notNull(userId, "userId can not be null");
        HostUserEntity hostUserEntity = hostUserService.findByDeskIdAndUserId(deskId, userId);
        if (hostUserEntity == null) {
            return;
        }
        DesktopSessionDTO desktopSessionDTO = desktopSessionServiceAPI.findByUserIdAndDeskId(hostUserEntity.getUserId(),
                hostUserEntity.getDesktopId());
        if (desktopSessionDTO != null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESKTOP_HAS_SESSION_NOT_CANCEL);
        }
        hostUserService.unBindUser(deskId, userId);
    }

    @Override
    public List<HostUserDTO> findByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId can not be null");
        List<RcoViewHostUserEntity> userEntityList = rcoViewHostUserDAO.findByDesktopId(deskId);
        return buildHostUserList(userEntityList);
    }

    private List<HostUserDTO> buildHostUserList(List<RcoViewHostUserEntity> userEntityList) {
        List<HostUserDTO> hostUserDTOList = new ArrayList<>();
        for (RcoViewHostUserEntity hostUserEntity : userEntityList) {
            HostUserDTO hostUserDTO = new HostUserDTO();
            hostUserDTO.setId(hostUserEntity.getId());
            hostUserDTO.setDesktopId(hostUserEntity.getDesktopId());
            hostUserDTO.setDesktopPoolId(hostUserEntity.getDesktopPoolId());
            hostUserDTO.setUserId(hostUserEntity.getId());
            hostUserDTO.setUserName(hostUserEntity.getUserName());
            hostUserDTOList.add(hostUserDTO);
        }
        return hostUserDTOList;
    }

    @Override
    public void clearTerminalIdByUserAndTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminalId can not be null");

        hostUserService.clearTerminalIdByTerminalId(terminalId);
    }

    @Override
    public List<HostUserGroupBindDeskNumDTO> countGroupDeskNumByDesktopPoolId(UUID desktopPoolId) {
        Assert.notNull(desktopPoolId, "desktopPoolId can not be null");

        return rcoViewHostUserDAO.countGroupDeskNumByDesktopPoolId(desktopPoolId);
    }
}
