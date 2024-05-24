package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOsFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.osfile.PageQueryOsFileRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.OsFileDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.OsFileServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestOsFileDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: 镜像文件service业务类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29
 *
 * @author xiao'yong'deng
 */
public class OsFileServerImpl implements OsFileServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsFileServerImpl.class);

    @Autowired
    private CbbOsFileMgmtAPI cbbOsFileMgmtAPI;

    @Override
    public DefaultPageResponse pagequery(PageQueryServerRequest pageQueryServerRequest) throws BusinessException {
        Assert.notNull(pageQueryServerRequest, "pageQueryServerRequest must not be null");

        try {
            PageQueryOsFileRequest request = new PageQueryOsFileRequest();
            request.setPage(pageQueryServerRequest.getPage());
            request.setLimit(pageQueryServerRequest.getLimit());
            PageSearchRequest pageSearchRequest = pageQueryServerRequest.convertCbb();
            request.setMatchEqualArr(pageSearchRequest.getMatchEqualArr());
            request.setSortList(pageSearchRequest.getSortList());
            DefaultPageResponse<OsFileDTO> response = cbbOsFileMgmtAPI.pageQueryOsFile(request);
            return convert(response);
        } catch (Exception e) {
            LOGGER.error("OpenAPI查询镜像列表失败！", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
    }

    private DefaultPageResponse<RestOsFileDTO> convert(DefaultPageResponse<OsFileDTO> pageResponse) {
        List<RestOsFileDTO> dtoList = new ArrayList<>();
        DefaultPageResponse<RestOsFileDTO> result = new DefaultPageResponse<>();
        if (pageResponse == null || pageResponse.getItemArr() == null) {
            result.setTotal(0);
            result.setItemArr(dtoList.toArray(new RestOsFileDTO[0]));
            return result;
        }

        Arrays.stream(pageResponse.getItemArr()).forEach(osFileDTO -> {
            RestOsFileDTO dto = new RestOsFileDTO();
            BeanUtils.copyProperties(osFileDTO, dto);
            dtoList.add(dto);
        });

        result.setTotal(pageResponse.getTotal());
        result.setItemArr(dtoList.toArray(new RestOsFileDTO[dtoList.size()]));
        return result;
    }
}
