package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.CabinetRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ConfigServerRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DeleteServerCabinetRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.NameRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import java.util.List;


/**
 *
 * Description: 机柜管理接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
public interface CabinetMgmtAPI {

    /**
     * 机柜分页查询
     *
     * @param request 请求
     * @return 响应
     */
    
    DefaultPageResponse<CabinetDTO> list(PageSearchRequest request);

    /**
     * 机柜详情
     * @param request 机柜ID请求
     * @return CabinetDTO
     * @throws BusinessException 异常
     */

    CabinetDTO detail(IdRequest request) throws BusinessException;

    /**
     * 机柜编辑
     *
     * @param request 请求
     * @throws BusinessException 异常
     */
    
    void edit(CabinetRequest request) throws BusinessException;

    /**
     * 创建机柜
     *
     * @param request 机柜创建请求
     * @throws BusinessException 业务异常
     */
    
    void create(CabinetRequest request) throws BusinessException;

    /**
     * 删除机柜
     *
     * @param request 删除机柜请求
     * @throws BusinessException 异常
     */
    
    void delete(IdRequest request) throws BusinessException;

    /**
     * 机柜配置服务器
     *
     * @param request 机柜配置服务求请求
     * @throws BusinessException 异常
     */
    
    void configServer(ConfigServerRequest request) throws BusinessException;

    /**
     * 删除机柜上配置的服务器
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    
    void deleteServerFromCabinet(DeleteServerCabinetRelationRequest request) throws BusinessException ;

    /**
     * 根据名称获取机柜信息
     *
     * @param request 请求
     * @return 响应
     */

    CabinetDTO getCabinetByName(NameRequest request);

    /**
     * 列出机柜上配置的服务器
     * @param request 空白请求
     * @return  服务器列表
     */

    List<PhysicalServerInfoDTO> listCabinetConfigedServer(IdRequest request);

}