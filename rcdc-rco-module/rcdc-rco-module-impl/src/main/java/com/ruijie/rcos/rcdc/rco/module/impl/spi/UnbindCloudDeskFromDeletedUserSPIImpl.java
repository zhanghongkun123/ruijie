package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserRecycleBinMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 将回收站中的云桌面和被删除的用户进行解绑
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/30 17:33
 *
 * @author yxq
 */
@DispatcherImplemetion("UnbindCloudDeskFromDeletedUserSPIImpl")
public class UnbindCloudDeskFromDeletedUserSPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnbindCloudDeskFromDeletedUserSPIImpl.class);

    @Autowired
    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Override
    public Boolean beforeEnteringMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterMaintenanceEnd(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        Assert.hasText(dispatchKey, "dispatchKey must not be null or empty");
        Assert.notNull(baseUpgradeDTO, "baseUpgradeDTO must not be null");
        if (baseUpgradeDTO.getType() == BaseUpgradeDTO.UpgradeType.ONLINE) {
            LOGGER.info("当前为热补丁升级，无需执行");
            return Boolean.TRUE;
        }

        int page = 0;
        int limit = 1000;
        List<UUID> userIdList = new ArrayList<>();

        // 查询回收站中所有的云桌面，记录其中所有的用户ID
        while (true) {
            PageSearchRequest pageSearchRequest = new PageSearchRequest();
            pageSearchRequest.setPage(page++);
            pageSearchRequest.setLimit(limit);
            DefaultPageResponse<CloudDesktopDTO> pageResponse = recycleBinMgmtAPI.pageQuery(pageSearchRequest);

            CloudDesktopDTO[] cloudDesktopDTOArr = pageResponse.getItemArr();
            if (cloudDesktopDTOArr == null || cloudDesktopDTOArr.length == 0) {
                break;
            }

            List<UUID> currentPageUserIdList = Stream.of(cloudDesktopDTOArr).map(CloudDesktopDTO::getUserId).collect(Collectors.toList());
            userIdList.addAll(currentPageUserIdList);
        }

        // 过滤重复的、为Null的用户id
        userIdList = userIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        LOGGER.info("回收站中云桌面绑定的用户数量为：[{}]", userIdList.size());

        // 判断云桌面绑定的用户是否被删除
        for (UUID userId : userIdList) {
            try {
                cbbUserAPI.getUserDetail(userId);
            } catch (BusinessException e) {
                LOGGER.info("用户[{}]不存在，进行云桌面解绑", userId);
                // 若被删除，则解绑
                try {
                    userDesktopMgmtAPI.unbindCloudDeskFromUser(userId);
                } catch (Exception ex) {
                    LOGGER.error(String.format("用户[%s]解绑云桌面失败，失败原因：", userId), e);
                }
            }
        }

        return Boolean.TRUE;
    }
}
