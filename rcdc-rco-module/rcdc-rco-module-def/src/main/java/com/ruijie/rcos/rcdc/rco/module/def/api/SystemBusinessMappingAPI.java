package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: MTool 系统和 RCDC 业务映射表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.06
 *
 * @author linhj
 *         <p>
 *         fixme 后续名称 + 位置需要专门移动 MTool 相关模块
 */
public interface SystemBusinessMappingAPI {

    /**
     * 保存映射纪录
     *
     * @param systemBusinessMappingDTO systemBusinessMappingDTO
     */
    void saveSystemBusinessMapping(SystemBusinessMappingDTO systemBusinessMappingDTO);

    /**
     * 根据系统类型、业务类型、业务标识查询纪录
     *
     * @param systemType 系统类型
     * @param businessType 业务类型
     * @param srcId 业务标识
     * @return 映射
     */
    SystemBusinessMappingDTO findSystemBusinessMapping(String systemType, String businessType, String srcId);

    /**
     * 根据系统类型、业务类型、业务标识查询纪录
     *
     * @param systemType 系统类型
     * @param businessType 业务类型
     * @param destId 目标业务标识
     * @return 映射
     */
    List<SystemBusinessMappingDTO> findMappingByDestId(String systemType, String businessType, String destId);

    /**
     * 根据系统类型、业务类型、业务标识更新纪录
     *
     * @param systemBusinessMappingDTO systemBusinessMappingDTO
     */
    void updateDestIdBySrcId(SystemBusinessMappingDTO systemBusinessMappingDTO);

    /**
     * 根据系统类型、业务类型、业务标识查询纪录
     *
     * @param systemType 系统类型
     * @param businessType 业务类型
     * @return 映射
     */
    List<SystemBusinessMappingDTO> findSystemBusinessMappingList(String systemType, String businessType);

    /**
     * 获取迁移自增序列标识，前八位补零
     *
     * @return 自增序列值
     */
    String obtainMappingSequenceVal();

    /**
     * 分页查询映射关系
     *
     * @param pageQueryRequest 请求对象
     * @return 映射列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<SystemBusinessMappingDTO> pageQuery(PageQueryRequest pageQueryRequest) throws BusinessException;

    /**
     * 删除4升5终端记录
     *
     * @param srcId 资源id
     */
    void deleteBySrcId(String srcId);
}
