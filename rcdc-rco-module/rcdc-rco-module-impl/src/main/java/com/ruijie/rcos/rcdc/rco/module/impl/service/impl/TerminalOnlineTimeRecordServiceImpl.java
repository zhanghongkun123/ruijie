package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.ImmutableList;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.TerminalTotalOnlineTimeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.TerminalOnlineTimeSearchPageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultDataSort;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.TerminalOnlineTimeRecordDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalOnlineTimeRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.TerminalOnlineTimeRecordEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractPageQueryTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.EntityFieldMapper;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalOnlineTimeRecordService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Description:  处理终端在线总时长service
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
@Service
public class TerminalOnlineTimeRecordServiceImpl extends AbstractPageQueryTemplate<TerminalOnlineTimeRecordEntity>
    implements TerminalOnlineTimeRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOnlineTimeRecordServiceImpl.class);

    @Autowired
    private TerminalOnlineTimeRecordDAO terminalOnlineTimeRecordDAO;

    private static final Integer TERMINAL_ONLINE_PAGE_SIZE = 1000;

    private static final String PAGE_SEARCH_HAS_ONLINE = "hasOnline";

    private static final String PAGE_SORT_CREATE_TIME = "createTime";

    private static final String LOCK_PREFIX = "SAVE_TERMINAL_ONLINE_TIME_";

    private static final int TRY_LOCK_TIME = 5;

    @Override
    public TerminalOnlineTimeRecordEntity findByTerminalId(String terminalId) {
        Assert.notNull(terminalId, "terminal id is not be null");
        return terminalOnlineTimeRecordDAO.findByTerminalId(terminalId);
    }

    @Override
    public TerminalOnlineTimeRecordEntity findByMacAddr(String macAddr) {
        Assert.notNull(macAddr, "macAddr is not be null");
        return terminalOnlineTimeRecordDAO.findByMacAddr(macAddr);
    }

    @Override
    public void saveTerminalOnlineTimeRecord(TerminalOnlineTimeRecordDTO terminalOnlineTimeRecordDTO) {
        Assert.notNull(terminalOnlineTimeRecordDTO, "terminalOnlineTimeRecordDTO id is not be null");
        TerminalOnlineTimeRecordEntity entity  = new TerminalOnlineTimeRecordEntity();
        BeanUtils.copyProperties(terminalOnlineTimeRecordDTO,entity);
        terminalOnlineTimeRecordDAO.save(entity);
    }

    @Override
    public List<TerminalTotalOnlineTimeDTO> findTotalOnlineTime(List<String> platforms) {
        Assert.notNull(platforms, "platforms is not be null");
        return terminalOnlineTimeRecordDAO.findTotalOnlineTimeByPlatform(platforms);
    }

    @Override
    public void doUpdateByOnline(TerminalOnlineTimeRecordDTO terminalOnlineTimeRecordDTO) throws BusinessException {
        Assert.notNull(terminalOnlineTimeRecordDTO, "terminalOnlineTimeRecordDTO is not be null");
        String terminalId = terminalOnlineTimeRecordDTO.getTerminalId();
        Assert.notNull(terminalId, "terminalOnlineTimeRecordDTO.getTerminalId() is not be null");
        LockableExecutor.executeWithTryLock(LOCK_PREFIX + terminalId, () -> {
            TerminalOnlineTimeRecordEntity terminalOnlineTimeRecordDB = terminalOnlineTimeRecordDAO.findByTerminalId(terminalId);
            if (terminalOnlineTimeRecordDB == null) {
                generateDTO(terminalOnlineTimeRecordDTO);
                saveTerminalOnlineTimeRecord(terminalOnlineTimeRecordDTO);
                return;
            }
            updateTerminalOnlineTimeRecord(terminalOnlineTimeRecordDB, terminalOnlineTimeRecordDTO.getPlatform());
        }, TRY_LOCK_TIME);
    }

    @Override
    public void handleTerminalOfflineTime(String terminalId, Boolean hasServerOnline) {
        Assert.notNull(terminalId, "terminalId is not be null");
        Assert.notNull(hasServerOnline, "hasServerOnline is not be null");
        TerminalOnlineTimeRecordEntity terminalOnlineTimeRecordDB = terminalOnlineTimeRecordDAO.findByTerminalId(terminalId);
        if (terminalOnlineTimeRecordDB == null) {
            return;
        }
        Date date = new Date();
        if (Boolean.TRUE.equals(hasServerOnline)) {
            Long onTimeLong = countOnTime(date, terminalOnlineTimeRecordDB.getLastOnlineTime(), terminalOnlineTimeRecordDB.getOnlineTotalTime());
            terminalOnlineTimeRecordDB.setOnlineTotalTime(onTimeLong);
        }
        terminalOnlineTimeRecordDB.setHasOnline(Boolean.FALSE);
        terminalOnlineTimeRecordDB.setUpdateTime(date);
        terminalOnlineTimeRecordDAO.save(terminalOnlineTimeRecordDB);
    }

    @Override
    public void handleTerminalOnlineTimeByQuartz() {
        long total = terminalOnlineTimeRecordDAO.countByHasOnline(Boolean.TRUE);
        int totalPage = (int) (total / TERMINAL_ONLINE_PAGE_SIZE);
        int lastCount = (int) (total % TERMINAL_ONLINE_PAGE_SIZE);
        for (int i = 0; i < totalPage; i++) {
            updateOnlineTime(i);
        }
        if (lastCount != 0) {
            updateOnlineTime(totalPage);
        }
    }

    /**
     * 定时器更新在线时间
     * @param currentPage 当前页数
     */
    private void updateOnlineTime(int currentPage) {
        PageSearchRequest request = getPageSearchRequest(currentPage);
        List<TerminalOnlineTimeRecordEntity> terminalOnlineTimeRecordList;
        Page<TerminalOnlineTimeRecordEntity> page = super.pageQuery(request, TerminalOnlineTimeRecordEntity.class);
        terminalOnlineTimeRecordList = page.getContent();
        if (CollectionUtils.isEmpty(terminalOnlineTimeRecordList)) {
            return;
        }
        terminalOnlineTimeRecordList.stream().forEach(entity -> {
            Date date = new Date();
            Long onTimeLong = countOnTime(date, entity.getLastOnlineTime(), entity.getOnlineTotalTime());
            entity.setOnlineTotalTime(onTimeLong);
            entity.setLastOnlineTime(date);
            entity.setUpdateTime(date);
        });
        terminalOnlineTimeRecordDAO.saveAll(terminalOnlineTimeRecordList);
    }

    @Override
    protected List<String> getSearchColumn() {
        return ImmutableList.of(PAGE_SEARCH_HAS_ONLINE);
    }

    @Override
    protected DefaultDataSort getDefaultDataSort() {
        return new DefaultDataSort(PAGE_SORT_CREATE_TIME, Sort.Direction.DESC);
    }

    @Override
    protected void mappingField(EntityFieldMapper entityFieldMapper) {

    }

    @Override
    protected Page<TerminalOnlineTimeRecordEntity> find(Specification<TerminalOnlineTimeRecordEntity> specification, Pageable pageable) {
        if (specification == null) {
            return terminalOnlineTimeRecordDAO.findAll(pageable);
        }
        return terminalOnlineTimeRecordDAO.findAll(specification, pageable);
    }

    /**
     * 构造DTO数据
     * @param terminalOnlineTimeRecordDTO 终端在线时长DTO
     */
    private void generateDTO(TerminalOnlineTimeRecordDTO terminalOnlineTimeRecordDTO) {
        Date date = new Date();
        terminalOnlineTimeRecordDTO.setOnlineTotalTime(0L);
        terminalOnlineTimeRecordDTO.setHasOnline(Boolean.TRUE);
        terminalOnlineTimeRecordDTO.setLastOnlineTime(date);
        terminalOnlineTimeRecordDTO.setCreateTime(date);
        terminalOnlineTimeRecordDTO.setUpdateTime(date);
    }

    /**
     * 更新终端在线时长
     * @param entity  构造终端在线实体类
     * @param platform  终端类型
     */
    private void updateTerminalOnlineTimeRecord(TerminalOnlineTimeRecordEntity entity, String platform) {
        Date date = new Date();
        entity.setHasOnline(Boolean.TRUE);
        entity.setLastOnlineTime(date);
        entity.setUpdateTime(date);
        entity.setPlatform(platform);
        terminalOnlineTimeRecordDAO.save(entity);
    }

    /**
     * 计算在线时长
     * @param currentOnTime  当前时间
     * @param lastTime 上次更新时间
     * @param onlineTotalTime  终端在线总时长
     * @return  处理后的在线总时长
     */
    private Long countOnTime(Date currentOnTime, Date lastTime, Long onlineTotalTime) {
        Long currentOnTimeLong = DateUtil.dateToSecondLong(currentOnTime);
        Long lastTimeLong = DateUtil.dateToSecondLong(lastTime);
        long timeDiff = currentOnTimeLong - lastTimeLong;
        Long result = onlineTotalTime + timeDiff;
        return result;
    }

    /**
     * 构造分页查询的参数
     * @param page  查询页数
     * @return  查询参数
     */
    private PageSearchRequest getPageSearchRequest(Integer page) {
        PageSearchRequest request = new TerminalOnlineTimeSearchPageRequest();
        request.setLimit(TERMINAL_ONLINE_PAGE_SIZE);
        request.setPage(page);
        MatchEqual matchEqual = new MatchEqual("hasOnline", new Object[] {true});
        request.setMatchEqualArr(new MatchEqual[]{matchEqual});
        return request;
    }

}
