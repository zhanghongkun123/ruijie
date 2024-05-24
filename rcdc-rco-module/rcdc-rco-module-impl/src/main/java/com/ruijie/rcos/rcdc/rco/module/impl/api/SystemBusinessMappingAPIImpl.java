package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.SystemBusinessMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.SystemBusinessMappingEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.ds.DataSourceNames;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.repositorykit.api.ds.JdbcTemplateHolder;
import org.springframework.util.Assert;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.06
 *
 * @author linhj
 */
@SuppressWarnings("SqlNoDataSourceInspection")
public class SystemBusinessMappingAPIImpl implements SystemBusinessMappingAPI {

    @Autowired
    private SystemBusinessMappingDAO systemBusinessMappingDAO;

    @Autowired
    private JdbcTemplateHolder jdbcTemplateHolder;

    // 自增序列
    private static final String SEQUENCE_GLOBAL_BUSINESS_INCREMENT = "sequence_global_business_increment";

    @Override
    public void saveSystemBusinessMapping(SystemBusinessMappingDTO systemBusinessMappingDTO) {
        Assert.notNull(systemBusinessMappingDTO, "systemBusinessMappingDTO can not be null");

        SystemBusinessMappingEntity systemBusinessMappingEntity;
        if (null != systemBusinessMappingDTO.getId()) {
            systemBusinessMappingEntity =
                    systemBusinessMappingDAO.findById(systemBusinessMappingDTO.getId()).orElse(new SystemBusinessMappingEntity());
        } else {
            systemBusinessMappingEntity = new SystemBusinessMappingEntity();
        }

        BeanUtils.copyProperties(systemBusinessMappingDTO, systemBusinessMappingEntity);
        systemBusinessMappingEntity.setUpdateDate(new Date());
        systemBusinessMappingDAO.save(systemBusinessMappingEntity);
    }

    @Override
    public SystemBusinessMappingDTO findSystemBusinessMapping(String systemType, String businessType, String srcId) {

        Assert.notNull(systemType, "systemType can not be null");
        Assert.notNull(businessType, "businessType can not be null");
        Assert.notNull(srcId, "srcId can not be null");

        SystemBusinessMappingEntity entity = systemBusinessMappingDAO.findBySystemTypeAndBusinessTypeAndSrcId(systemType, businessType, srcId);

        return Optional.ofNullable(entity).map(tempEntity -> {
            SystemBusinessMappingDTO systemBusinessMappingDTO = new SystemBusinessMappingDTO();
            BeanUtils.copyProperties(tempEntity, systemBusinessMappingDTO);
            return systemBusinessMappingDTO;
        }).orElse(null);
    }

    @Override
    public List<SystemBusinessMappingDTO> findMappingByDestId(String systemType, String businessType, String destId) {

        Assert.notNull(systemType, "systemType can not be null");
        Assert.notNull(businessType, "businessType can not be null");
        Assert.notNull(destId, "destId can not be null");

        List<SystemBusinessMappingDTO> systemBusinessMappingDTOList = new ArrayList<>();
        List<SystemBusinessMappingEntity> systemBusinessMappingEntityList =
                systemBusinessMappingDAO.findBySystemTypeAndBusinessTypeAndDestId(systemType, businessType, destId);

        systemBusinessMappingEntityList.forEach(entity -> {
            SystemBusinessMappingDTO systemBusinessMappingDTO = new SystemBusinessMappingDTO();
            BeanUtils.copyProperties(entity, systemBusinessMappingDTO);
            systemBusinessMappingDTOList.add(systemBusinessMappingDTO);
        });
        return systemBusinessMappingDTOList;
    }


    @Override
    public void updateDestIdBySrcId(SystemBusinessMappingDTO mappingDTO) {

        Assert.notNull(mappingDTO, "systemType can not be null");

        systemBusinessMappingDAO.updateDestIdBySrcId(mappingDTO.getSystemType(), mappingDTO.getBusinessType(), mappingDTO.getSrcId(),
                mappingDTO.getDestId(), mappingDTO.getContext());
    }

    /**
     * 根据系统类型、业务类型、业务标识查询纪录
     *
     * @param systemType 系统类型
     * @param businessType 业务类型
     * @return 映射
     */
    @Override
    public List<SystemBusinessMappingDTO> findSystemBusinessMappingList(String systemType, String businessType) {
        Assert.notNull(systemType, "systemType can not be null");
        Assert.notNull(businessType, "businessType can not be null");

        List<SystemBusinessMappingDTO> systemBusinessMappingDTOList = new ArrayList<>();
        List<SystemBusinessMappingEntity> systemBusinessMappingEntityList =
                systemBusinessMappingDAO.findBySystemTypeAndBusinessType(systemType, businessType);

        systemBusinessMappingEntityList.forEach((entity) -> {
            SystemBusinessMappingDTO systemBusinessMappingDTO = new SystemBusinessMappingDTO();
            BeanUtils.copyProperties(entity, systemBusinessMappingDTO);
            systemBusinessMappingDTOList.add(systemBusinessMappingDTO);
        });
        return systemBusinessMappingDTOList;
    }

    @Override
    public PageQueryResponse<SystemBusinessMappingDTO> pageQuery(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "pageQueryRequest can not be null");

        PageQueryResponse<SystemBusinessMappingEntity> pageResponse = systemBusinessMappingDAO.pageQuery(pageQueryRequest);
        SystemBusinessMappingEntity[] systemBusinessMappingEntityArr =
                Optional.ofNullable(pageResponse.getItemArr()).orElse(new SystemBusinessMappingEntity[0]);
        SystemBusinessMappingDTO[] systemBusinessMappingDTOArr = Stream.of(systemBusinessMappingEntityArr).map(item -> {
            SystemBusinessMappingDTO systemBusinessMappingDTO = new SystemBusinessMappingDTO();
            BeanUtils.copyProperties(item, systemBusinessMappingDTO);
            return systemBusinessMappingDTO;
        }).toArray(SystemBusinessMappingDTO[]::new);

        return new PageQueryResponse<>(systemBusinessMappingDTOArr, pageResponse.getTotal());
    }

    /**
     * 获取迁移自增序列标识
     *
     * @return 自增序列值
     */
    @Override
    public String obtainMappingSequenceVal() {
        JdbcTemplate jdbcTemplate = jdbcTemplateHolder.loadJdbcTemplate(DataSourceNames.DEFAULT_DATASOURCE_BEAN_NAME);
        final Long index = jdbcTemplate.queryForObject("select NEXTVAL('" + SEQUENCE_GLOBAL_BUSINESS_INCREMENT + "')", Long.class);
        return String.format("%08d", index);
    }

    @Override
    public void deleteBySrcId(String srcId) {
        Assert.notNull(srcId, "srcId can not be null");
        systemBusinessMappingDAO.deleteBySrcId(srcId);
    }
}
