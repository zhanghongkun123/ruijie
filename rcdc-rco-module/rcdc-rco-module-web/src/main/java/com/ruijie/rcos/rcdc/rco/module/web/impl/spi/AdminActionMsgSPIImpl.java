package com.ruijie.rcos.rcdc.rco.module.web.impl.spi;

import java.util.Objects;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacGetAdminPageRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.spi.AdminActionMsgSPI;
import com.ruijie.rcos.rcdc.rco.module.web.service.SessionContextRegistry;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 管理员退出通知
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author lifeng
 */
public class AdminActionMsgSPIImpl implements AdminActionMsgSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminActionMsgSPIImpl.class);

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private SessionContextRegistry sessionContextRegistry;

    private static final int GET_ADMIN_PAGE_LIMIT = 100;

    private static final int FIRST_PAGE = 0;

    @Override
    public void notifyAllAdminLogout() throws BusinessException {
        LOGGER.info("notifyAllAdminLogout: 通知所有管理退出登录");
        IacGetAdminPageRequest baseGetAdminPageRequest = new IacGetAdminPageRequest();
        baseGetAdminPageRequest.setLimit(GET_ADMIN_PAGE_LIMIT);

        // 处理首页100个管理员并且获取总共管理员数量
        DefaultPageResponse<IacAdminDTO> adminPageList = pageRequestAdminAndLogout(FIRST_PAGE);
        if (adminPageList.getTotal() <= GET_ADMIN_PAGE_LIMIT) {
            return;
        }

        // 处理其他批次的管理员退出登录
        long remainPage = adminPageList.getTotal() / GET_ADMIN_PAGE_LIMIT;
        for (int page = FIRST_PAGE + 1; page <= remainPage; page++) {
            pageRequestAdminAndLogout(page);
        }
    }



    private DefaultPageResponse<IacAdminDTO> pageRequestAdminAndLogout(int page) throws BusinessException {
        IacGetAdminPageRequest baseGetAdminPageRequest = new IacGetAdminPageRequest();
        baseGetAdminPageRequest.setLimit(GET_ADMIN_PAGE_LIMIT);
        baseGetAdminPageRequest.setPage(page);
        baseGetAdminPageRequest.setSubSystem(SubSystem.CDC);

        DefaultPageResponse<IacAdminDTO> adminPageList = baseAdminMgmtAPI.getAdminPage(baseGetAdminPageRequest);
        if (adminPageList.getTotal() <= 0) {
            return adminPageList;
        }

        if (Objects.nonNull(adminPageList.getItemArr())) {
            for (IacAdminDTO baseAdminDTO : adminPageList.getItemArr()) {
                baseAdminMgmtAPI.logout(baseAdminDTO.getId(),SubSystem.CDC);
                sessionContextRegistry.logoutSessionContext(baseAdminDTO.getId());
            }
        }
        return adminPageList;
    }
}
