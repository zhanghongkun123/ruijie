package com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.UserInfoService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;

/**
 * @author yxq
 * @date 2021/6/29 23:24
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Override
    public String getUsernameById(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId must not be null");
        RcoViewUserEntity entity = userService.getUserInfoById(userId);
        if (entity == null) {
            LOGGER.error("id={}对应用户不存在", userId);
            throw new BusinessException(BusinessKey.RCDC_USER_USER_MESSAGE_NOT_EXIST);
        }
        return entity.getUserName();
    }

    @Override
    public UUID getUserIdByUserName(String userName) throws BusinessException {
        Assert.notNull(userName, "userName must not be null");

        RcoViewUserEntity entity = userService.getUserInfoByName(userName);
        if (entity == null) {
            LOGGER.error("用户[{}]不存在", userName);
            throw new BusinessException(BusinessKey.RCDC_USER_USER_MESSAGE_NOT_EXIST);
        }
        return entity.getId();
    }

    @Override
    public List<String> findByDesktopNumGe(int desktopNum) {
        Assert.notNull(desktopNum, "desktopNum must not be null");
        return rcoViewUserDAO.findByDesktopNumGe(desktopNum);
    }

    @Override
    public long findUserCount() {
        return rcoViewUserDAO.count();
    }

    @Override
    public UserCertificationDTO getUserCertificationDTO(UUID userId) {
        Assert.notNull(userId, "userId must not be null");

        RcoViewUserEntity entity = userService.getUserInfoById(userId);
        UserCertificationDTO userCertificationDTO = new UserCertificationDTO();
        BeanUtils.copyProperties(entity, userCertificationDTO);

        return userCertificationDTO;
    }

    @Override
    public List<String> getUserDesktopResource(List<String> usernameList) {
        Assert.notNull(usernameList, "usernameList must not be null");
        if (CollectionUitls.isEmpty(usernameList)) {
            return Collections.emptyList();
        }
        // 判断普通桌面数量
        List<RcoViewUserEntity> userEntityList = rcoViewUserDAO.findAllByUserNameIn(usernameList);
        Set<String> existList = new HashSet<>();
        List<UUID> userIdList = new ArrayList<>();
        userEntityList.stream().forEach(userEntity -> {
            if (userEntity.getDesktopNum() != null && userEntity.getDesktopNum() >= Constants.INT_1) {
                existList.add(userEntity.getUserName());
            } else {
                userIdList.add(userEntity.getId());
            }
        });

        // 判断池桌面资源
        existList.addAll(desktopPoolUserService.listBindDesktopPoolUserName(userIdList));

        return new ArrayList<>(existList);
    }

    @Override
    public IacUserDetailDTO getDetail(UUID userId) {
        Assert.notNull(userId, "userId must not be null");
        RcoViewUserEntity entity = userService.getUserInfoById(userId);
        if (entity == null) {
            // 不存在用户
            return null;
        }
        IacUserDetailDTO userDetailDTO = new IacUserDetailDTO();
        BeanUtils.copyProperties(entity, userDetailDTO);
        return userDetailDTO;
    }
}
