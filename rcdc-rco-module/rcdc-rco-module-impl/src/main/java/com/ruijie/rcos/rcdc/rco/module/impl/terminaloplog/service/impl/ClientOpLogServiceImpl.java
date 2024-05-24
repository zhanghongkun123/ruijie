package com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.service.impl;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.request.ClientOpLogPageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.PageQuerySpecification;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.dao.ClientOpLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.entity.ClientOpLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.service.ClientOpLogService;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
@Service
public class ClientOpLogServiceImpl extends AbstractPageQueryTemplate<ClientOpLogEntity> implements ClientOpLogService {

    @Autowired
    private ClientOpLogDAO clientOpLogDAO;

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of("mac", "ip", "userName", "operMsg");
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort("createTime", Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {
        //无需实现
    }

    @Override
    protected Page<ClientOpLogEntity> find(Specification<ClientOpLogEntity> specification, Pageable pageable) {

        return clientOpLogDAO.findAll(specification, pageable);
    }

    @Override
    public Page<ClientOpLogEntity> pageQuery(ClientOpLogPageRequest request) {
        Assert.notNull(request, "request can not be null");

        return super.pageQuery(request, ClientOpLogEntity.class);
    }


    @Override
    protected PageQuerySpecification<ClientOpLogEntity> buildSpecification(PageSearchRequest request) {
        ClientOpLogPageRequest timePageRequest = (ClientOpLogPageRequest) request;
        if (timePageRequest.getSearchKeyword() == null && timePageRequest.getStartTime() == null && timePageRequest.getEndTime() == null
                && ArrayUtils.isEmpty(timePageRequest.getMatchEqualArr())) {
            // 允许为空
            return null;
        } else {
            TerminalOpLogPageSpec pageSpec = new TerminalOpLogPageSpec(request.getSearchKeyword(), getSearchColumn(), request.getMatchEqualArr(),
                    request.getIsAnd());
            pageSpec.setStartTime(timePageRequest.getStartTime());
            pageSpec.setEndTime(timePageRequest.getEndTime());
            return pageSpec;
        }
    }
}
