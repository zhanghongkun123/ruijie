package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacPageResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQueryUserListPageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbSoftDeleteDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.CreatingDesktopNumCache;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/3/7
 *
 * @author Jarman
 */
@Service("rcoUserServiceImpl")
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private Integer retry = 0;

    private static final Integer RETRY_TIMES = 2;

    private static final String ID = "id";


    private static final String MAIL_HTML_PATH = "template/mail.html";

    private static final String LOGO_PATH = "template/logo.png";

    private static final String LOGO_CID = "logo";

    private static final String USER_NAME = "userName";

    private static final String REAL_NAME = "realName";

    private static final String PASSWORD = "password";

    private static final String SUBJECT = "初始账号密码";

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CreatingDesktopNumCache creatingDesktopNumCache;

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserServiceTx userServiceTx;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;


    @Override
    public long countUserDesktopNumContainCreatingNum(UUID userId) {
        Assert.notNull(userId, "userId can not null");
        // 获取用户已创建的云桌面个数，不包括回收站的桌面
        long alreadyExistDesktopNum = viewDesktopDetailDAO.countByUserIdAndDesktopTypeAndIsDeleteAndDesktopPoolType(userId,
                CbbCloudDeskType.VDI.name(), false, DesktopPoolType.COMMON.name());
        LOGGER.info("用户[{}]已创建[{}]个云桌面", userId, alreadyExistDesktopNum);
        // 获取用户正在创建的云桌面个数
        Integer creatingDesktopNum = getCreatingDesktopNum(userId);
        LOGGER.info("用户[{}]正在创建的云桌面个数为：[{}]", userId, creatingDesktopNum);
        return alreadyExistDesktopNum + creatingDesktopNum;
    }

    @Override
    public Integer getCreatingDesktopNum(UUID userId) {
        Assert.notNull(userId, "userId can not null");
        return creatingDesktopNumCache.getCreatingNum(userId);
    }

    @Override
    public String[] getUserGroupNameArr(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId can not null");
        RcoViewUserEntity userEntity = rcoViewUserDAO.getOne(userId);
        Assert.notNull(userEntity, "userEntity can not null");
        UUID groupId = userEntity.getGroupId();
        List<String> groupNameList = new ArrayList<>();
        IacUserGroupDetailDTO userGroupDetail = cbbUserGroupAPI.getUserGroupDetail(groupId);
        groupNameList.add(userGroupDetail.getName());
        UUID parentId = userGroupDetail.getParentId();
        while (parentId != null) {
            IacUserGroupDetailDTO tempGroup = cbbUserGroupAPI.getUserGroupDetail(parentId);
            groupNameList.add(tempGroup.getName());
            parentId = tempGroup.getParentId();
        }
        Collections.reverse(groupNameList);
        String[] groupNameArr = new String[groupNameList.size()];
        groupNameList.toArray(groupNameArr);
        return groupNameArr;
    }

    @Override
    public void deleteDesktop(UUID desktopId, CbbCloudDeskType deskType) throws BusinessException {
        Assert.notNull(desktopId, "desktopId不能为null");
        Assert.notNull(deskType, "deskType不能为null");

        if (deskType == CbbCloudDeskType.VDI) {
            CbbSoftDeleteDeskDTO cbbSoftDeleteDeskDTO = new CbbSoftDeleteDeskDTO();
            cbbSoftDeleteDeskDTO.setDeskId(desktopId);
            cbbVDIDeskMgmtAPI.deleteDeskVDI(cbbSoftDeleteDeskDTO);
        } else if (deskType != CbbCloudDeskType.THIRD) {
            try {
                userServiceTx.deleteDeskIDVRelativeData(desktopId);
            } catch (ObjectOptimisticLockingFailureException e) {
                LOGGER.error("发生乐观锁异常，开始进行重试调用, 重试次数retry={}", retry);
                retry++;
                if (retry > RETRY_TIMES) {
                    LOGGER.error("乐观锁异常进行多次重试后仍然失败, retry={}", retry);
                    throw new BusinessException(BusinessKey.RCDC_USER_DELETE_IDV_RELATIVE_DATA_ERROR, e, desktopId.toString());
                }
                deleteDesktop(desktopId, deskType);
            } finally {
                retry = 0;
            }
            // 调用云桌面cbb组件删除idv云桌面
            cbbIDVDeskMgmtAPI.deleteDeskIDV(desktopId);
        } else {
            // 删除第三方桌面
            userDesktopMgmtAPI.deleteDesktopThirdParty(desktopId);
        }
    }


    @Override
    public RcoViewUserEntity getUserInfoById(UUID userId) {
        Assert.notNull(userId, "userId must not be null");
        RcoViewUserEntity viewUserEntity = rcoViewUserDAO.getOne(userId);
        // 身份中心的加密key与rco加密key不一样，需要做转换为rco密文
        return resolvePassword(viewUserEntity);
    }

    @Override
    public RcoViewUserEntity getUserInfoByName(String userName) {
        Assert.notNull(userName, "userName must not be null");
        RcoViewUserEntity viewUserEntity = rcoViewUserDAO.findByUserName(userName);
        return resolvePassword(viewUserEntity);
    }

    private RcoViewUserEntity resolvePassword(RcoViewUserEntity viewUserEntity) {
        if (viewUserEntity == null || StringUtils.isBlank(viewUserEntity.getPassword())) {
            return viewUserEntity;
        }
        // 身份中心的加密key与rco加密key不一样，需要做转换为rco密文
        String rcoEncPwd = RedLineUtil.convertIacUserPwdToRcoPwd(viewUserEntity.getPassword());
        viewUserEntity.setPassword(rcoEncPwd);

        return viewUserEntity;
    }

    @Override
    public List<RcoViewUserEntity> getUserInfoByIdList(List<UUID> userIdList) {
        Assert.notEmpty(userIdList, "userIdList must not be empty");

        return rcoViewUserDAO.findAllByIdIn(userIdList);
    }

    @Override
    public boolean isRcoViewUserEntityEmpty() {
        List<UUID> uuidList = rcoViewUserDAO.obtainIdList(PageRequest.of(0, 1));
        LOGGER.info("查询到的结果集信息为：{}", JSON.toJSONString(uuidList));

        // 如果视图中不存在用户ID，则证明用户表为空
        return CollectionUtils.isEmpty(uuidList);
    }

    @Override
    public boolean checkAllUserGroupExistByIds(List<UUID> idList) throws BusinessException {
        Assert.notNull(idList, "idList can not be null");
        if (CollectionUtils.isEmpty(idList)) {
            return true;
        }
        idList = idList.stream().distinct().collect(Collectors.toList());
        List<IacUserGroupDetailDTO> groupList = this.listUserGroupByIds(idList);
        return idList.size() == groupList.size();
    }

    /**
     * 查询用户组列表
     *
     * @param groupIdList groupIdList
     * @return List<BaseUserGroupDetailDTO>
     * @throws BusinessException BusinessException
     */
    @Override
    public List<IacUserGroupDetailDTO> listUserGroupByIds(List<UUID> groupIdList) throws BusinessException {
        Assert.notNull(groupIdList, "groupIdList can not be null");
        if (CollectionUtils.isEmpty(groupIdList)) {
            return Collections.emptyList();
        }
        List<IacUserGroupDetailDTO> groupDetailDTOList = cbbUserGroupAPI.listByGroupIdList(groupIdList);
        if (CollectionUtils.isEmpty(groupDetailDTOList)) {
            return Collections.emptyList();
        }
        return groupDetailDTOList;
    }

    /**
     * 查询用户列表
     *
     * @param idList idList
     * @return List<CbbUserDetailDTO>
     * @throws BusinessException BusinessException
     */
    @Override
    public List<IacUserDetailDTO> listUserByIds(List<UUID> idList) throws BusinessException {
        Assert.notNull(idList, "idList can not be null");
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        List<IacUserDetailDTO> detailDTOList = cbbUserAPI.listUserByUserIds(idList);
        return detailDTOList;
    }

    @Override
    public boolean checkAllUserExistByIds(List<UUID> idList) {
        Assert.notNull(idList, "idList can not be null");
        if (CollectionUtils.isEmpty(idList)) {
            return true;
        }
        idList = idList.stream().distinct().collect(Collectors.toList());
        return idList.size() == rcoViewUserDAO.countByIdIn(idList);
    }

    @Override
    public boolean checkAnyVisitor(List<UUID> idList) {
        Assert.notNull(idList, "idList can not be null");
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        idList = idList.stream().distinct().collect(Collectors.toList());
        List<IacUserTypeEnum> typeList = Lists.newArrayList(IacUserTypeEnum.VISITOR);
        return rcoViewUserDAO.existsByIdInAndUserTypeIn(idList, typeList);
    }

    @Override
    public Long findByDesktopNumAndUserType(int desktopNum, List<IacUserTypeEnum> userTypeList) {
        Assert.notNull(desktopNum, "desktopNum can not be null");
        Assert.notEmpty(userTypeList, "userTypeList can not be null");
        return rcoViewUserDAO.findByDesktopNumAndUserType(desktopNum, userTypeList);
    }

    @Override
    public IacPageResponse<IacUserDetailDTO> pageQueryByGroupId(UUID groupId, Integer page) throws BusinessException {
        Assert.notNull(groupId, "groupId can not be null");
        Assert.notNull(page, "page can not be null");
        IacQueryUserListPageDTO pageDTO = new IacQueryUserListPageDTO();
        pageDTO.setGroupId(groupId);
        pageDTO.setPage(page);
        pageDTO.setLimit(Constants.MAX_QUERY_LIST_SIZE);
        return cbbUserAPI.pageQueryUserListByGroupId(pageDTO);
    }

    @Override
    public IacUserDetailDTO getUserDetailByName(String userName) {
        Assert.hasText(userName, "userName can not be empty");
        try {
            return cbbUserAPI.getUserByName(userName);
        } catch (BusinessException e) {
            throw new IllegalStateException("获取用户信息失败：" + userName, e);
        }
    }
}
