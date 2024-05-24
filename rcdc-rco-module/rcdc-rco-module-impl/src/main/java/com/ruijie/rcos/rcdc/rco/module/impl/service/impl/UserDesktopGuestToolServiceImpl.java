package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopGuestToolDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.UserDesktopGuestToolDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopGuestToolEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopGuestToolService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 14:41
 *
 * @author linrenjian
 */
@Service
public class UserDesktopGuestToolServiceImpl implements UserDesktopGuestToolService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDesktopGuestToolServiceImpl.class);

    @Autowired
    private ViewDesktopGuestToolDAO viewDesktopGuestToolDAO;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    public static final int DEFAULT_PAGE_NUM = 1;

    public static final int DEFAULT_PAGE_SIZE = 1000;

    @Override
    public List<UserDesktopGuestToolDTO> findAllOnlineVDIDeskByInDeskId(UUID[] uuidArr) throws BusinessException {
        Assert.notNull(uuidArr, "uuidArr  can not be null");
        // 缓存集合
        List<UserDesktopGuestToolDTO> deskGtList = new ArrayList<>();
        // 初始化分页0
        int currentPage = 0;
        PageQueryBuilderFactory.RequestBuilder requestBuilder =
                pageQueryBuilderFactory.newRequestBuilder().setPageLimit(currentPage, DEFAULT_PAGE_SIZE);
        requestBuilder.eq("deskState", CbbCloudDeskState.RUNNING.toString()).eq("cbbImageType", CbbImageType.VDI.toString()).asc("latestRunningTime");
        requestBuilder.in("cbbDesktopId", uuidArr);
        while (true) {
            requestBuilder.setPageLimit(currentPage, DEFAULT_PAGE_SIZE);
            final PageQueryRequest pageQueryRequest = requestBuilder.build();
            PageQueryResponse<ViewUserDesktopGuestToolEntity> pageResponse = viewDesktopGuestToolDAO.pageQuery(pageQueryRequest);
            ViewUserDesktopGuestToolEntity[] itemArr = pageResponse.getItemArr();
            if (ArrayUtils.isEmpty(itemArr)) {
                return deskGtList;
            }
            // 页码数量自增
            currentPage++;
            List<UserDesktopGuestToolDTO> pageList =
                    Arrays.stream(pageResponse.getItemArr()).map(ViewUserDesktopGuestToolEntity::convertToDTO).collect(Collectors.toList());
            deskGtList.addAll(pageList);

        }
    }

    @Override
    public List<UserDesktopGuestToolDTO> findAllOnlineVDIDesk() throws BusinessException {
        // 缓存集合
        List<UserDesktopGuestToolDTO> deskGtList = new ArrayList<>();
        // 初始化分页0
        int currentPage = 0;
        PageQueryBuilderFactory.RequestBuilder requestBuilder =
                pageQueryBuilderFactory.newRequestBuilder().setPageLimit(currentPage, DEFAULT_PAGE_SIZE);
        requestBuilder.eq("deskState", CbbCloudDeskState.RUNNING.toString()).eq("cbbImageType", CbbImageType.VDI.toString()).asc("latestRunningTime");
        while (true) {
            requestBuilder.setPageLimit(currentPage, DEFAULT_PAGE_SIZE);
            final PageQueryRequest pageQueryRequest = requestBuilder.build();
            PageQueryResponse<ViewUserDesktopGuestToolEntity> pageResponse = viewDesktopGuestToolDAO.pageQuery(pageQueryRequest);
            ViewUserDesktopGuestToolEntity[] itemArr = pageResponse.getItemArr();
            if (ArrayUtils.isEmpty(itemArr)) {
                return deskGtList;
            }
            // 页码数量自增
            currentPage++;
            List<UserDesktopGuestToolDTO> pageList =
                    Arrays.stream(pageResponse.getItemArr()).map(ViewUserDesktopGuestToolEntity::convertToDTO).collect(Collectors.toList());
            deskGtList.addAll(pageList);

        }
    }

}
