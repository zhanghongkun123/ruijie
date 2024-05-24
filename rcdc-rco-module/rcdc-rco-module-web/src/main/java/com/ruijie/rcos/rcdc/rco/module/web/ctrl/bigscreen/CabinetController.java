package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CabinetMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.CabinetRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ConfigServerRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DeleteServerCabinetRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.NameRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet.CheckCabinetNameDuplicationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet.ConfigServerWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet.CreateCabinetWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet.DeleteServerCabinetRelationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.cabinet.EditCabinetWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.response.ListPhysicalServerCabinetWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.CheckDuplicationResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: 机柜管理
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author BaiGuoliang
 */
@Controller
@RequestMapping(value = "/rco/cabinet")
public class CabinetController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CabinetController.class);

    @Autowired
    private CabinetMgmtAPI cabinetMgmtAPI;

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 查看机柜列表
     *
     * @param request 请求
     * @return 响应
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse list(PageWebRequest request) {
        Assert.notNull(request, "PageWebRequest不能为null");

        PageSearchRequest apiRequest = new PageSearchRequest(request);
        return DefaultWebResponse.Builder.success(cabinetMgmtAPI.list(apiRequest));
    }

    /**
     * 查看机柜详情
     *
     * @param request 机柜详情请求
     * @return 响应
     * @throws BusinessException 异常
     */
    @RequestMapping(value = {"/detail", "/getInfo"})
    public DefaultWebResponse detail(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "IdWebRequest不能为null");

        IdRequest idRequest = new IdRequest(request.getId());
        CabinetDTO cabinetDTO = cabinetMgmtAPI.detail(idRequest);
        return DefaultWebResponse.Builder.success(cabinetDTO);
    }

    /**
     * 编辑机柜
     *
     * @param request        编辑机柜请求
     * @return 响应
     */
    @RequestMapping(value = "/edit")
    public DefaultWebResponse edit(EditCabinetWebRequest request) {
        Assert.notNull(request, "EditCabinetWebRequest不能为null");

        CabinetRequest cabinetRequest = new CabinetRequest();
        BeanUtils.copyProperties(request, cabinetRequest);
        LOGGER.info("edit cabinet info, param : " + request.toString());
        try {
            cabinetMgmtAPI.edit(cabinetRequest);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_EDIT_SUCCESS, request.getName());
            return DefaultWebResponse.Builder.success(BigScreenBussinessKey.RCDC_RCO_CABINET_EDIT_SUCCESS,
                    new String[]{request.getName()});
        } catch (BusinessException ex) {
            LOGGER.error("编辑机柜失败", ex);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_EDIT_FAIL, request.getName());
            return DefaultWebResponse.Builder
                    .fail(BigScreenBussinessKey.RCDC_RCO_CABINET_EDIT_FAIL, new String[]{ex.getI18nMessage()});
        }
    }

    /**
     * 创建机柜
     *
     * @param request        创建机柜请求
     * @return 响应
     */
    @RequestMapping(value = "/create")
    public DefaultWebResponse create(CreateCabinetWebRequest request) {
        Assert.notNull(request, "CreateCabinetWebRequest不能为null");

        CabinetRequest createCabinetRequest = new CabinetRequest();
        BeanUtils.copyProperties(request, createCabinetRequest);
        try {
            cabinetMgmtAPI.create(createCabinetRequest);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_CREATE_SUCCESS, request.getName());
            return DefaultWebResponse.Builder.success(BigScreenBussinessKey.RCDC_RCO_CABINET_CREATE_SUCCESS, new String[]{request.getName()});
        } catch (BusinessException ex) {
            LOGGER.error(BigScreenBussinessKey.RCDC_RCO_CABINET_CREATE_FAIL, ex);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_CREATE_FAIL, request.getName());
            return DefaultWebResponse.Builder
                    .fail(BigScreenBussinessKey.RCDC_RCO_CABINET_CREATE_FAIL, new String[]{ex.getI18nMessage()});
        }
    }

    /**
     * 删除机柜
     *
     * @param request        删除机柜请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/delete")
    public DefaultWebResponse delete(IdArrWebRequest request) throws BusinessException {
        Assert.notNull(request, "IdArrWebRequest不能为null");

        IdRequest idRequest = new IdRequest(request.getIdArr()[0]);
        String cabinetName = idRequest.getId().toString();
        try {
            CabinetDTO cabinetDTO = cabinetMgmtAPI.detail(idRequest);
            cabinetName = cabinetDTO.getName();
            cabinetMgmtAPI.delete(idRequest);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_SUCCESS, cabinetName);
            return DefaultWebResponse.Builder.success(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_SUCCESS,
                    new String[]{cabinetName});
        } catch (BusinessException ex) {
            LOGGER.error(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_FAIL, ex);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_FAIL, cabinetName, ex.getI18nMessage());
            throw new BusinessException(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_FAIL, ex, cabinetName, ex.getI18nMessage());
        }
    }

    /**
     * 机柜配置服务器
     *
     * @param request        请求
     * @return 响应
     */
    @RequestMapping(value = "/configServer")
    public DefaultWebResponse configServer(ConfigServerWebRequest request) {
        Assert.notNull(request, "ConfigServerWebRequest不能为空");

        String cabinetName = request.getCabinetId().toString();
        String hostName = request.getServerId().toString();
        try {
            // 1、验证机柜是否存在
            IdRequest idRequest = new IdRequest(request.getCabinetId());
            CabinetDTO cabinetDTO = cabinetMgmtAPI.detail(idRequest);
            cabinetName = cabinetDTO.getName();

            // 2、验证服务器是否存在，并获取服务器名称
            PhysicalServerDTO physicalServerDTO = getServerInfo(request.getServerId());
            hostName = physicalServerDTO.getHostName();

            ConfigServerRequest serverRequest = new ConfigServerRequest();
            BeanUtils.copyProperties(request, serverRequest);

            cabinetMgmtAPI.configServer(serverRequest);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_SUCCESS, cabinetName,
                    hostName);
            return DefaultWebResponse.Builder.success(BigScreenBussinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_SUCCESS,
                    new String[]{cabinetName, hostName});
        } catch (BusinessException ex) {
            LOGGER.error(BigScreenBussinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_FAIL, cabinetName, hostName, ex);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_FAIL, cabinetName,
                    hostName, ex.getI18nMessage());
            return DefaultWebResponse.Builder
                    .fail(BigScreenBussinessKey.RCDC_RCO_CABINET_CONFIG_SERVER_FAIL, new String[]{cabinetName, hostName,
                            ex.getI18nMessage()});
        }
    }

    /**
     * 删除机柜上配置的服务器
     *
     * @param webRequest     请求参数
     * @return 响应
     */
    @RequestMapping(value = "/deleteServerFromCabinet")
    public DefaultWebResponse deleteServerFromCabinet(DeleteServerCabinetRelationWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest must not be null!");

        String cabinetName = webRequest.getCabinetId().toString();
        String hostName = webRequest.getServerId().toString();
        try {
            // 1、验证机柜是否存在
            IdRequest idRequest = new IdRequest(webRequest.getCabinetId());
            CabinetDTO cabinetDTO = cabinetMgmtAPI.detail(idRequest);
            cabinetName = cabinetDTO.getName();

            // 2、验证服务器是否存在，并获取服务器名称
            PhysicalServerDTO physicalServerDTO = getServerInfo(webRequest.getServerId());
            hostName = physicalServerDTO.getHostName();

            DeleteServerCabinetRelationRequest request = new DeleteServerCabinetRelationRequest();
            BeanUtils.copyProperties(webRequest, request);
            cabinetMgmtAPI.deleteServerFromCabinet(request);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_SERVER_SUCCESS, cabinetName, hostName);
        } catch (BusinessException ex) {
            LOGGER.error(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_SERVER_FAIL, cabinetName, hostName, ex);
            auditLogAPI.recordLog(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_SERVER_FAIL, cabinetName,
                    hostName, ex.getI18nMessage());
            return DefaultWebResponse.Builder
                    .fail(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_SERVER_FAIL, new String[]{cabinetName, hostName,
                            ex.getI18nMessage()});
        }

        return DefaultWebResponse.Builder.success(BigScreenBussinessKey.RCDC_RCO_CABINET_DELETE_SERVER_SUCCESS,
                new String[]{cabinetName, hostName});
    }

    /**
     * 机柜名称重复检验，若名称重复，则返回内容true
     *
     * @param request 机柜名称校验请求
     * @return 响应
     */
    @RequestMapping(value = "/checkCabinetNameDuplication")
    public DefaultWebResponse checkCabinetNameDuplication(CheckCabinetNameDuplicationWebRequest request) {
        Assert.notNull(request, "CheckDuplicationGroupNameWebRequest不能为空");

        NameRequest apiRequest = new NameRequest();
        BeanUtils.copyProperties(request, apiRequest);
        CabinetDTO cabinetDTO = cabinetMgmtAPI.getCabinetByName(apiRequest);
        boolean isExist = null != cabinetDTO && null != cabinetDTO.getId() && !cabinetDTO.getId().equals(request.getId());
        CheckDuplicationResponse checkDuplicationResponse = new CheckDuplicationResponse(isExist);
        return DefaultWebResponse.Builder.success(checkDuplicationResponse);
    }

    /**
     * 获得机柜配置的服务器列表
     *
     * @param request 机柜Id
     * @return 响应
     */
    @RequestMapping(value = "/cabinetConfigedServer/list")
    public DefaultWebResponse listCabinetConfigedServer(IdWebRequest request) {
        Assert.notNull(request, "request must not be null!");

        IdRequest apiRequest = new IdRequest(request.getId());
        List<PhysicalServerInfoDTO> physicalServerDTOList = cabinetMgmtAPI.listCabinetConfigedServer(apiRequest);
        ListPhysicalServerCabinetWebResponse webResponse = new ListPhysicalServerCabinetWebResponse();
        webResponse.setItemArr(physicalServerDTOList.toArray(new PhysicalServerInfoDTO[]{}));
        return DefaultWebResponse.Builder.success(webResponse);
    }

    private PhysicalServerDTO getServerInfo(UUID serverId) throws BusinessException {

        PhysicalServerDTO serverDTO = cbbPhysicalServerMgmtAPI.getPhysicalServer(serverId);
        if (null == serverDTO) {
            LOGGER.error("服务器信息不存在，serverId = {}", serverId);
            throw new BusinessException(BigScreenBussinessKey.RCDC_RCO_CABINET_SERVER_INFO_NOT_EXIST);
        }

        return serverDTO;
    }

}
