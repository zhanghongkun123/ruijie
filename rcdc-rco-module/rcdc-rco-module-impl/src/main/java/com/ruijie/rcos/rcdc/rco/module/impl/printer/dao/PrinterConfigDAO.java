package com.ruijie.rcos.rcdc.rco.module.impl.printer.dao;

import com.ruijie.rcos.rcdc.rco.module.impl.printer.entity.PrinterConfigEntity;
import com.ruijie.rcos.sk.modulekit.api.ds.SkyEngineJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Description: 打印机配置
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/15
 *
 * @author chenjiehui
 */
public interface PrinterConfigDAO extends SkyEngineJpaRepository<PrinterConfigEntity, UUID> {

    /**
     * 根据configName 查询 配置
     * @param configName 配置名称
     * @return entity
     */
    PrinterConfigEntity findByConfigName(String configName);

    /**
     * 更新打印机配置名和描述
     * @param configName 配置名
     * @param configDescription 打印机描述
     * @param id 打印机id
     */
    @Modifying
    @Transactional
    @Query(value = "update PrinterConfigEntity set configName=:configName, configDescription=:configDescription, " +
            "version=version+1 where id=:id and version = version")
    void updatePrinterConfigById(@Param("configName") String configName, @Param("configDescription") String configDescription,  @Param("id") UUID id);

    /**
     * 根据model,types 和 os 查询
     * @param printerModel 型号
     * @param connectTypes 连接类型
     * @param os 支持的系统
     * @return configName
     */
    @Query(value = "select configName from PrinterConfigEntity where printerModel = ?1 and printerConnectType " +
            "in (?2) and configSupportOs like %?3%")
    List<String> getPrinterConfigNameByModelAndTypesAndOs(String printerModel, List<String> connectTypes, String os);

    /**
     * 根据types 和 os 查询
     * @param connectTypes 连接类型
     * @param os 支持的系统
     * @return  configName
     */
    @Query(value = "select configName from PrinterConfigEntity where printerConnectType in (?1) and configSupportOs like %?2%")
    List<String> getPrinterConfigNameByTypesAndOs(List<String> connectTypes, String os);




}
