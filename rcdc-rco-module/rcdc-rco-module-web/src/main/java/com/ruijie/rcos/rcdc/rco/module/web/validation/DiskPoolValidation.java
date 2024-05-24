package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbCheckDiskPoolNameDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolUserAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskBusinessKeyEnums;
import com.ruijie.rcos.rcdc.rco.module.web.PublicBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.request.BindUserWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request.CreateDiskPoolWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request.DiskPoolNameWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.request.UpdateDiskPoolWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description: 磁盘池相关校验
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
@Service
public class DiskPoolValidation {

    /**
     * 磁盘符号正则表达式：只支持[D-Z]排除MNX
     */
    private static final String DISK_LETTER_REGEX = "[D-Z&&[^MNX]]";

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskPoolValidation.class);

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private DiskPoolUserAPI diskPoolUserAPI;

    @Autowired
    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;
    
    @Autowired
    private ClusterAPI clusterAPI;

    /**
     * 校验磁盘池名称
     *
     * @param request 请求类
     * @throws BusinessException 业务异常
     */
    public void diskPoolCreateValidation(CreateDiskPoolWebRequest request) throws BusinessException {
        Assert.notNull(request, "diskPoolCreateValidation request can not be null");
        diskPoolNameValidation(request);
        // 前缀重复校验
        String prefix = StringUtils.isEmpty(request.getDiskNamePrefix()) ? request.getName() : request.getDiskNamePrefix();
        diskNamePrefixValidation(request.getId(), prefix);
    }

    /**
     * 校验磁盘池名称
     *
     * @param request 请求类
     * @throws BusinessException 业务异常
     */
    public void diskPoolEditValidation(UpdateDiskPoolWebRequest request) throws BusinessException {
        Assert.notNull(request, "diskPoolEditValidation request can not be null");
        diskPoolNameValidation(request);
        // 前缀重复校验
        diskNamePrefixValidation(request.getId(), request.getDiskNamePrefix());
    }

    private void diskNamePrefixValidation(UUID diskPoolId, String diskNamePrefix) throws BusinessException {
        Assert.hasText(diskNamePrefix, "diskNamePrefixValidation diskNamePrefix can not be null");
        // 校验前缀是否重复
        CbbCheckDiskPoolNameDTO diskNamePrefixDTO = new CbbCheckDiskPoolNameDTO();
        diskNamePrefixDTO.setId(diskPoolId);
        diskNamePrefixDTO.setName(diskNamePrefix);
        if (BooleanUtils.toBoolean(cbbDiskPoolMgmtAPI.checkDiskNamePrefixDuplicate(diskNamePrefixDTO))) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_NAME_PREFIX_EXIST, diskNamePrefix);
        }
    }

    /**
     * 校验磁盘池名称
     *
     * @param request 请求类
     * @throws BusinessException 业务异常
     */
    public void diskPoolNameValidation(DiskPoolNameWebRequest request) throws BusinessException {
        Assert.notNull(request, "diskPoolNameValidation request can not be null");
        // 校验计算集群和存储池
        clusterStoragePoolValidation(request);
        String diskPoolName = request.getName();
        CbbCheckDiskPoolNameDTO poolNameDTO = new CbbCheckDiskPoolNameDTO();
        BeanUtils.copyProperties(request, poolNameDTO);
        if (BooleanUtils.toBoolean(cbbDiskPoolMgmtAPI.checkDiskPoolNameDuplicate(poolNameDTO))) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_NAME_EXIST, diskPoolName);
        }
        String diskLetter = request.getDiskLetter();
        // 默认名称，直接跳过
        if (Objects.equals(diskLetter, DiskPoolConstants.DEFAULT_LETTER)) {
            return;
        }
        // 磁盘符号校验
        if (StringUtils.isBlank(diskLetter) || !Pattern.matches(DISK_LETTER_REGEX, diskLetter)) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_LETTER_ERROR, diskPoolName);
        }
    }

    private void clusterStoragePoolValidation(DiskPoolNameWebRequest request) throws BusinessException {
        UUID clusterId = request.getClusterId();
        UUID storagePoolId = request.getStoragePoolId();
        if (Objects.isNull(clusterId)) {
            throw new BusinessException(PublicBusinessKey.RCDC_RCO_CLUSTER_NOT_NULL_ERROR);
        }
        if (Objects.isNull(storagePoolId)) {
            throw new BusinessException(PublicBusinessKey.RCDC_RCO_STORAGE_POOL_NOT_NULL_ERROR);
        }
        // 校验计算集群和存储池
        clusterAPI.validateComputerClusterStoragePool(clusterId, storagePoolId, request.getPlatformId());
    }

    /**
     * 绑定用户校验
     *
     * @param request 请求类
     * @throws BusinessException 业务异常
     */
    public void bindUserValidation(BindUserWebRequest request) throws BusinessException {
        Assert.notNull(request, "bindUserValidation request can not be null");
        UUID diskId = request.getDiskId();
        UUID diskPoolId = request.getDiskPoolId();
        UserDiskDetailDTO userDiskDetailDTO = userDiskMgmtAPI.userDiskDetail(diskId);
        String diskName = userDiskDetailDTO.getDiskName();
        CbbDiskPoolDTO diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
        if (!Objects.equals(diskPoolId, userDiskDetailDTO.getDiskPoolId())) {
            LOGGER.info("磁盘[{}]不属于磁盘池[{}]，不允许进行绑定/解绑操作", diskName, diskPoolDTO.getName());
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_NOT_BELONG_POOL, diskName, diskPoolDTO.getName());
        }
        // 非启用-禁用-故障状态，不支持解绑-绑定磁盘
        if (!DiskPoolConstants.BIND_DISK_STATUS.contains(userDiskDetailDTO.getDiskState())) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_STATUS_OPERATION_UNALLOWED, diskName,
                    DiskBusinessKeyEnums.obtainResolve(userDiskDetailDTO.getDiskState().name()),
                    LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_USER_UNBIND_OR_BIND_DISK_LOG));
        }
        UUID userId = request.getUserId();
        // 相等说明磁盘绑定用户信息未进行变更，为空则进行解绑
        if (Objects.isNull(userId) || Objects.equals(userId, userDiskDetailDTO.getUserId())) {
            return;
        }
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        // 访客用户不支持绑定磁盘
        if (userDetail.getUserType() == IacUserTypeEnum.VISITOR) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_VISITOR_USER_NOT_BOUND_DISK_ERROR, userDetail.getUserName());
        }
        // 用户未分配到磁盘池中
        if (!diskPoolUserAPI.checkUserInDiskPool(diskPoolId, userId)) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_USER_NOT_BOUND_DISK_POOL_ERROR, userDetail.getUserName(), diskPoolDTO.getName());
        }
        Optional<UserDiskDetailDTO> optional = userDiskMgmtAPI.diskDetailByUserId(userId);
        if (optional.isPresent()) {
            UserDiskDetailDTO userDiskDTO = optional.get();
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_USER_BOUND_DISK_ERROR, userDiskDTO.getUserName(), userDiskDTO.getDiskName());
        }
    }
}
