package com.ruijie.rcos.rcdc.rco.module.web.ctrl.datasync;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DataSyncLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchDataSyncLogDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.datasync.constants.DataSyncConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.datasync.request.DataSyncPageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.match.BetweenRangeMatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/22 15:28
 *
 * @author coderLee23
 */
@Api(tags = "数据同步日志控制器")
@Controller
@RequestMapping("/rco/dataSyncLog")
public class DataSyncLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncLogController.class);

    @Autowired
    private DataSyncLogAPI dataSyncLogAPI;

    /**
     * 分页获取数据同步日志
     *
     * @param request 分页查询参数
     * @return CommonWebResponse 响应参数
     */
    @ApiOperation("分页获取数据同步日志")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<DataSyncLogDTO>> pageDataSyncLog(DataSyncPageQueryRequest request) {
        Assert.notNull(request, "request must not be null");

        com.ruijie.rcos.sk.pagekit.api.Sort[] sortArr = request.getSortArr();
        Sort sort = Sort.by(Sort.Direction.DESC, DataSyncConstants.CREATE_TIME);
        if (Objects.nonNull(sortArr) && sortArr.length > 0) {
            com.ruijie.rcos.sk.pagekit.api.Sort skSort = sortArr[0];
            String fieldName = skSort.getFieldName();
            com.ruijie.rcos.sk.pagekit.api.Sort.Direction direction = skSort.getDirection();
            sort = Sort.by(Sort.Direction.valueOf(direction.name()), fieldName);
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchDataSyncLogDTO searchDataSyncLogDTO = new SearchDataSyncLogDTO();
        searchDataSyncLogDTO.setContent(request.getSearchKeyword());

        Match[] matchArr = request.getMatchArr();
        for (Match match : matchArr) {
            if (match instanceof BetweenRangeMatch) {
                BetweenRangeMatch betweenRangeMatch = (BetweenRangeMatch) match;
                Instant fromDate = Instant.ofEpochMilli((Long) betweenRangeMatch.getValueFrom());
                Instant toDate = Instant.ofEpochMilli((Long) betweenRangeMatch.getValueTo());
                searchDataSyncLogDTO.setStartCreateTime(Date.from(fromDate));
                searchDataSyncLogDTO.setEndCreateTime(Date.from(toDate));
                break;
            }
        }

        DefaultPageResponse<DataSyncLogDTO> dataSyncLogPage = dataSyncLogAPI.pageDataSyncLog(searchDataSyncLogDTO, pageable);
        return CommonWebResponse.success(dataSyncLogPage);
    }



}
