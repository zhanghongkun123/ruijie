package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DynamicUserGroupMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaDynamicUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopCountInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaDynamicUserInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rca.service.QueryDynamicUserGroupUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.CloudDesktopViewServiceImpl;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 应用组绑定用户API实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 24/10/2022 下午 3:30
 *
 * @author gaoxueyuan
 */
public class DynamicUserGroupMappingAPIImpl implements DynamicUserGroupMappingAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicUserGroupMappingAPIImpl.class);

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    private UserRecycleBinMgmtAPI userRecycleBinMgmtAPI;

    @Autowired
    private QueryDynamicUserGroupUserService queryDynamicUserGroupUserService;

    @Autowired
    private CloudDesktopViewServiceImpl cloudDesktopViewService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private RcoInvalidTimeHelper invalidTimeUtil;

    /**
     * 用户永久锁定时的默认值
     */
    private static final int PERMANENT_LOCK = -1;

    private static final int SIZE_ZERO = 0;

    /**
     * 分钟转换成毫秒
     */
    private static final int ONE_MINUTE_MILLIS = 60 * 1000;

    @Override
    public DefaultPageResponse<RcaDynamicUserInfoDTO> pageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "pageQuery方法的request can not be null");

        Page<ViewRcaDynamicUserInfoEntity> page = queryDynamicUserGroupUserService.pageQuery(request);
        DefaultPageResponse<RcaDynamicUserInfoDTO> response = new DefaultPageResponse<>();
        if (page.getTotalElements() == SIZE_ZERO) {
            response.setTotal(SIZE_ZERO);
            response.setItemArr(new RcaDynamicUserInfoDTO[0]);
            return response;
        }
        List<RcaDynamicUserInfoDTO> dtoList = generateAppStrategyBindUserDTO(page);
        response.setItemArr(dtoList.toArray(new RcaDynamicUserInfoDTO[0]));
        response.setTotal(page.getTotalElements());
        return response;
    }

    private List<RcaDynamicUserInfoDTO> generateAppStrategyBindUserDTO(Page<ViewRcaDynamicUserInfoEntity> page) throws BusinessException {
        List<ViewRcaDynamicUserInfoEntity> entityList = page.getContent();
        List<RcaDynamicUserInfoDTO> bindUserDTOList = Lists.newArrayList();
        for (ViewRcaDynamicUserInfoEntity entity : entityList) {
            RcaDynamicUserInfoDTO bindUserDTO = buildBindUserDTO(entity);
            bindUserDTO.setAppGroupId(entity.getAppGroupId());
            bindUserDTOList.add(bindUserDTO);
        }
        return bindUserDTOList;
    }

    private RcaDynamicUserInfoDTO buildBindUserDTO(ViewRcaDynamicUserInfoEntity entity) throws BusinessException {
        RcaDynamicUserInfoDTO bindUserDTO = new RcaDynamicUserInfoDTO();

        // 计算获取解锁时间，公式当前时间减去配置锁定时间
        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        Date realUnLockTime = new Date(System.currentTimeMillis() - (long) ONE_MINUTE_MILLIS * pwdStrategyDTO.getUserLockTime());

        List<UUID> userIdList = Lists.newArrayList(entity.getId());
        UserDesktopCountInfo userDesktopCountInfo = getUserDesktopCountInfo(userIdList);
        List<CloudDesktopDTO> recycleUserList = userRecycleBinMgmtAPI.getAllDesktopByUserIdList(userIdList);
        Map<UUID, List<CloudDesktopDTO>> userIdToDesktopMap = recycleUserList.stream().collect(Collectors.groupingBy(CloudDesktopDTO::getUserId));

        List<CloudDesktopDTO> emptyList = Lists.newLinkedList();
        BeanUtils.copyProperties(entity, bindUserDTO);
        bindUserDTO.setUserState(entity.getState());
        boolean canDelete = userDesktopCountInfo.isUserCanDelete(entity.getId());
        bindUserDTO.setCanDelete(canDelete);
        boolean hasRecycleBin = userIdToDesktopMap.getOrDefault(bindUserDTO.getId(), emptyList).size() > SIZE_ZERO;
        bindUserDTO.setHasRecycleBin(hasRecycleBin);
        // 暂时未移植独享应用主机相关桌面信息，后续需要再移植
        boolean isLock = checkUserIsLock(pwdStrategyDTO, bindUserDTO, realUnLockTime);
        bindUserDTO.setLock(isLock);
        try {
            if (ObjectUtils.isNotEmpty(entity.getId())) {
                IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(entity.getId());
                bindUserDTO.setInvalid(invalidTimeUtil.isAccountInvalid(cbbUserDetailDTO));
                bindUserDTO.setInvalidDescription(invalidTimeUtil.obtainInvalidDescription(cbbUserDetailDTO));
                bindUserDTO.setAccountExpireDate(invalidTimeUtil.expireDateFormat(cbbUserDetailDTO));
            }
        } catch (BusinessException e) {
            LOGGER.error("查询应用组配置的用户[{}]详情出错", entity.getId());
            String exceptionMsg = UserTipUtil.resolveBusizExceptionMsg(e);
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_QUERY_ERROR, e, exceptionMsg);
        }
        return bindUserDTO;
    }

    private UserDesktopCountInfo getUserDesktopCountInfo(List<UUID> userIdList) {
        return cloudDesktopViewService.countUserDesktopInfo(userIdList);
    }

    private boolean checkUserIsLock(PwdStrategyDTO pwdStrategyDTO, RcaDynamicUserInfoDTO dto, Date realUnLockTime) {
        // 如果没有开启防爆功能，则用户都处于未锁定状态
        if (!pwdStrategyDTO.getPreventsBruteForce()) {
            return false;
        }

        boolean isLock = checkToGetDefaultValue(dto.getLock(), false);
        // 1.基于防暴设置位true,锁定时长不等于null判断是否上锁
        // 2.基于lockTime重新判断isLock字段
        if (pwdStrategyDTO.getPreventsBruteForce() && dto.getLockTime() != null) {
            if (pwdStrategyDTO.getUserLockTime() != PERMANENT_LOCK) {
                // 指定时长锁定
                isLock = dto.getLockTime().after(realUnLockTime);
            } else {
                // 永久锁定
                isLock = true;
            }
        }
        return isLock;
    }


    /**
     * 参考optional的ofNullable方法重写校验方法
     *
     * @param value
     * @param defaultValue
     * @param <T>
     * @return
     */
    private <T> T checkToGetDefaultValue(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
