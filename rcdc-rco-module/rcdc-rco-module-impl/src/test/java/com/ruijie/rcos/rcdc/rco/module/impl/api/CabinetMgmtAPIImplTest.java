package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.CabinetMappingServerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.CabinetRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ConfigServerRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.DeleteServerCabinetRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.NameRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CabinetServerMappingDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CabinetServerMappingEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ServerService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.QueryCabinetServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CabinetServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Description: 机柜管理API实现测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/2 16:05
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class CabinetMgmtAPIImplTest {

    @Tested(fullyInitialized = true)
    private CabinetMgmtAPIImpl cabinetMgmtAPI;

    @Injectable
    private QueryCabinetServiceImpl queryCabinetService;

    @Injectable
    private CabinetDAO cabinetDAO;

    @Injectable
    private CabinetServiceTx cabinetServiceTx;

    @Injectable
    private CabinetServerMappingDAO cabinetServerMappingDAO;

    @Injectable
    private ServerService serverService;

    @Injectable
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    /**
     * 测试机柜列表
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testList() throws BusinessException {
        PageSearchRequest pageRequest = buildPageRequest();
        Page<CabinetEntity> page = buildCabinetPage(false);
        new Expectations() {
            {
                queryCabinetService.pageQuery((PageSearchRequest) any, CabinetEntity.class);
                result = page;
            }
        };
        DefaultPageResponse<CabinetDTO> pageRet = cabinetMgmtAPI.list(pageRequest);
        Assert.assertEquals(page.getTotalElements(), pageRet.getTotal());
    }

    /**
     * 测试加载机柜列表-返回空列表
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testListReturnEmptyList() throws BusinessException {
        PageSearchRequest pageRequest = buildPageRequest();
        Page<CabinetEntity> page = buildCabinetPage(true);
        new Expectations() {
            {
                queryCabinetService.pageQuery((PageSearchRequest) any, CabinetEntity.class);
                result = page;
            }
        };
        DefaultPageResponse<CabinetDTO> pageRet = cabinetMgmtAPI.list(pageRequest);
        Assert.assertEquals(page.getTotalElements(), pageRet.getTotal());
    }

    /**
     * 测试加载机柜列表-请求参数为空时
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testPageQueryRequestIsNull() throws BusinessException {
        PageSearchRequest request = null;

        try {
            cabinetMgmtAPI.list(request);
            fail();
        } catch (Exception e) {
            Assert.assertEquals("PageSearchRequest不能为null", e.getMessage());
        }
    }

    /**
     * 测试根据Id获取机柜详情 不为空
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testDetail() throws BusinessException {
        CabinetEntity entity = new CabinetEntity();
        IdRequest idRequest = new IdRequest();
        idRequest.setId(UUID.randomUUID());
        new Expectations() {
            {
                cabinetDAO.getOne((UUID) any);
                result = entity;
            }
        };
        cabinetMgmtAPI.detail(idRequest);
        new Verifications() {
            {
                cabinetDAO.getOne((UUID) any);
                times = 1;
            }
        };

    }

    /**
     * 测试根据Id获取机柜详情 空
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testDetailWithNull() throws BusinessException {
        CabinetEntity entity = null;
        List<CabinetServerMappingEntity> entitiesList = new ArrayList<>();
        UUID id = UUID.randomUUID();
        IdRequest idRequest = new IdRequest();
        idRequest.setId(id);
        new Expectations() {
            {
                cabinetDAO.getOne((UUID) any);
                result = entity;
            }
        };

        try {
            cabinetMgmtAPI.detail(idRequest);
            fail();
        } catch (BusinessException ex) {
            Assert.assertEquals(ex.getMessage(), BusinessKey.RCDC_RCO_CABINET_NOT_EXIST);
        }
        new Verifications() {
            {
                cabinetDAO.getOne((UUID) any);
                times = 1;
                cabinetServerMappingDAO.findAllByCabinetId((UUID) any);
                times = 0;
            }
        };
    }

    /**
     * 测试根据id加载机柜详情-请求参数为空
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testGetCabinetByIdRequestIsNull() {
        try {
            cabinetMgmtAPI.detail(null);
            fail();
        } catch (Exception e) {
            Assert.assertEquals("IdRequest不能为null", e.getMessage());
        }
    }

    /**
     * 测试编辑机柜
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testEdit() throws BusinessException {

        CabinetEntity entity = new CabinetEntity();
        UUID id = UUID.randomUUID();
        CabinetRequest request = new CabinetRequest();
        request.setId(id);
        request.setServerNum(4);
        new Expectations() {
            {
                cabinetDAO.getByName(anyString);
                result = null;
                cabinetDAO.getOne(id);
                result = entity;
            }
        };
        cabinetMgmtAPI.edit(request);

        new Verifications() {
            {
                cabinetDAO.getByName(anyString);
                times = 1;
                cabinetDAO.getOne(id);
                times = 1;
            }
        };
    }

    /**
     * 测试编辑机柜-机柜实体为null
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testEditWithCabinetEntityNull() throws BusinessException {
        CabinetRequest request = new CabinetRequest();
        request.setName("test");
        request.setId(UUID.randomUUID());

        new Expectations() {
            {
                cabinetDAO.getByName(anyString);
                result = null;
                cabinetDAO.getOne(request.getId());
                result = null;
            }
        };

        try {
            cabinetMgmtAPI.edit(request);
        } catch (BusinessException ex) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_CABINET_NOT_EXIST, ex.getKey());
        }

        new Verifications() {
            {
                cabinetDAO.getByName(anyString);
                times = 1;
                cabinetDAO.getOne((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 测试编辑机柜 名称重复
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testEditNameDuplication() throws BusinessException {
        CabinetRequest request = new CabinetRequest();
        request.setName("name");
        UUID uuid = UUID.randomUUID();
        request.setId(uuid);
        CabinetEntity resultEntity = new CabinetEntity();
        resultEntity.setName("name");
        resultEntity.setId(uuid);
        new Expectations() {
            {
                cabinetDAO.getByName(anyString);
                result = resultEntity;
            }
        };
        try {
            cabinetMgmtAPI.edit(request);
        } catch (BusinessException ex) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_CABINET_VALIDATE_EDIT_CABINET_NAME_EXIST, ex.getMessage());
        }
    }

    /**
     * 测试机柜创建
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testCreat() throws BusinessException {
        CabinetRequest request = new CabinetRequest();
        request.setServerNum(1);
        request.setName("name");
        new Expectations() {
            {
                cabinetDAO.getByName(anyString);
                result = null;
                cabinetDAO.save((CabinetEntity) any);
            }
        };
        cabinetMgmtAPI.create(request);
        new Verifications() {
            {
                cabinetDAO.getByName(anyString);
                times = 1;
                cabinetDAO.save((CabinetEntity) any);
                times = 1;
            }
        };
    }

    /**
     * 测试创建机柜 名称重复
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testCreateNameDuplication() throws BusinessException {
        CabinetRequest request = new CabinetRequest();
        request.setName("name");
        CabinetEntity resultEntity = new CabinetEntity();
        resultEntity.setName("name");
        new Expectations() {
            {
                cabinetDAO.getByName(anyString);
                result = resultEntity;
            }
        };
        try {
            cabinetMgmtAPI.create(request);
        } catch (BusinessException ex) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_CABINET_VALIDATE_CREATE_CABINET_NAME_EXIST, ex.getMessage());
        }
    }

    /**
     * 测试删除机柜
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testDelete() throws BusinessException {
        IdRequest request = new IdRequest(UUID.randomUUID());
        cabinetMgmtAPI.delete(request);
        new Verifications() {
            {

            }
        };
    }

    /**
     * 测试删除机柜，机柜不存在
     *
     * @throws BusinessException 业务异常
     */
    @Test(expected = BusinessException.class)
    public void testDeleteCabinetNotExist() throws BusinessException {
        IdRequest request = new IdRequest(UUID.randomUUID());
        new Expectations() {
            {
                cabinetDAO.getOne((UUID) any);
                result = null;
            }
        };
        cabinetMgmtAPI.delete(request);
        new Verifications() {
            {

            }
        };
    }


    /**
     * 测试重名检验 无Id
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testCheckCabinetNameDuplication() throws BusinessException {
        NameRequest request = new NameRequest();
        request.setName("name");
        CabinetEntity cabinetEntity = new CabinetEntity();
        new Expectations() {
            {
                cabinetDAO.getByName(request.getName());
                result = cabinetEntity;
            }
        };
        cabinetMgmtAPI.getCabinetByName(request);
        new Verifications() {
            {
                cabinetDAO.getByName(request.getName());
                times = 1;
            }
        };
    }

    /**
     * 测试重名检验 重名，有Id
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testCheckCabinetNameDuplicationId() throws BusinessException {
        NameRequest request = new NameRequest();
        request.setName("name");
        CabinetEntity entity = new CabinetEntity();
        entity.setName("name");
        new Expectations() {
            {
                cabinetDAO.getByName(request.getName());
                result = entity;

            }
        };

        CabinetDTO cabinetDTO = cabinetMgmtAPI.getCabinetByName(request);
        Assert.assertEquals(cabinetDTO.getName(), request.getName());

        new Verifications() {
            {
                cabinetDAO.getByName(request.getName());
                times = 1;
            }
        };
    }

    /**
     * 测试配置服务器，正常流程
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testConfigServer() throws BusinessException {
        UUID cabinetId = UUID.randomUUID();
        ConfigServerRequest request = new ConfigServerRequest();
        request.setCabinetId(cabinetId);
        request.setServerId(UUID.randomUUID());
        request.setCabinetLocationBegin(1);
        request.setCabinetLocationEnd(3);

        CabinetServerMappingEntity mappingEntity = new CabinetServerMappingEntity();
        mappingEntity.setCabinetId(cabinetId);
        mappingEntity.setCabinetLocationBegin(21);
        mappingEntity.setCabinetLocationEnd(23);

        List<CabinetServerMappingEntity> mappingEntityList = Lists.newArrayList();
        mappingEntityList.add(mappingEntity);

        new Expectations() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                result = null;
                cabinetServerMappingDAO.findAllByCabinetId((UUID) any);
                result = mappingEntityList;
                cabinetServiceTx.configServer((CabinetMappingServerDTO) any);
            }
        };

        cabinetMgmtAPI.configServer(request);

        new Verifications() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                times = 1;
                cabinetServerMappingDAO.findAllByCabinetId((UUID) any);
                times = 2;
                cabinetServiceTx.configServer((CabinetMappingServerDTO) any);
                times = 1;
            }
        };
    }

    private PageSearchRequest buildPageRequest() {
        PageSearchRequest webRequest = new PageSearchRequest();
        webRequest.setPage(0);
        webRequest.setLimit(20);
        return webRequest;
    }

    private Page<CabinetEntity> buildCabinetPage(boolean isEmpty) {
        Pageable pageable = PageRequest.of(1, 10);
        List<CabinetEntity> entityList = new ArrayList<>();
        if (!isEmpty) {
            for (int i = 0; i < 5; i++) {
                CabinetEntity entity = new CabinetEntity();
                entityList.add(entity);
            }
        }
        Page<CabinetEntity> page = new PageImpl<>(entityList, pageable, entityList.size());
        return page;
    }

    private Page<CabinetServerMappingEntity> buildCabinetMappingServerPage(boolean isEmpty) {
        Pageable pageable = PageRequest.of(0, 10);
        List<CabinetServerMappingEntity> entityList = new ArrayList<>();
        if (!isEmpty) {
            for (int i = 0; i < 5; i++) {
                CabinetServerMappingEntity entity = new CabinetServerMappingEntity();
                entityList.add(entity);
            }
        }
        Page<CabinetServerMappingEntity> page = new PageImpl<>(entityList, pageable, entityList.size());
        return page;
    }

    /**
     * 测试从机柜上删除服务器，正常流程
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testDeleteServerFromCabinet() throws BusinessException {
        UUID cabinetId = UUID.randomUUID();
        DeleteServerCabinetRelationRequest request = new DeleteServerCabinetRelationRequest();
        request.setCabinetId(cabinetId);
        request.setServerId(UUID.randomUUID());

        CabinetServerMappingEntity mappingEntity = new CabinetServerMappingEntity();
        mappingEntity.setCabinetId(cabinetId);

        new Expectations() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                result = mappingEntity;
                cabinetServiceTx.deleteServerFromCabinet((UUID) any, (List<UUID>) any);
            }
        };

        cabinetMgmtAPI.deleteServerFromCabinet(request);

        new Verifications() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                times = 1;
                cabinetServiceTx.deleteServerFromCabinet((UUID) any, (List<UUID>) any);
                times = 1;
            }
        };
    }

    /**
     * 测试从机柜上删除服务器，映射关系已被删除
     *
     * @throws BusinessException 业务异常
     */
    @Test(expected = BusinessException.class)
    public void testDeleteServerFromCabinetIsDeleted() throws BusinessException {
        UUID cabinetId = UUID.randomUUID();
        DeleteServerCabinetRelationRequest request = new DeleteServerCabinetRelationRequest();
        request.setCabinetId(cabinetId);
        request.setServerId(UUID.randomUUID());

        new Expectations() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                result = null;
            }
        };

        cabinetMgmtAPI.deleteServerFromCabinet(request);

        new Verifications() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 测试从机柜上删除服务器，服务器不属于当前机柜
     *
     * @throws BusinessException 业务异常
     */
    @Test(expected = BusinessException.class)
    public void testDeleteServerFromCabinetNoSuchServer() throws BusinessException {
        DeleteServerCabinetRelationRequest request = new DeleteServerCabinetRelationRequest();
        request.setCabinetId(UUID.randomUUID());
        request.setServerId(UUID.randomUUID());

        CabinetServerMappingEntity mappingEntity = new CabinetServerMappingEntity();
        mappingEntity.setCabinetId(UUID.randomUUID());

        new Expectations() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                result = mappingEntity;
            }
        };

        cabinetMgmtAPI.deleteServerFromCabinet(request);

        new Verifications() {
            {
                cabinetServerMappingDAO.getByServerId((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 测试列出机柜上已配置的服务器，正常流程
     */
    @Test
    public void testListCabinetConfigedServer() {
        UUID cabinetId = UUID.randomUUID();
        IdRequest request = new IdRequest(cabinetId);

        UUID serverId = UUID.randomUUID();
        PhysicalServerInfoDTO serverInfoDTO = new PhysicalServerInfoDTO();
        serverInfoDTO.setId(serverId);
        List<PhysicalServerInfoDTO> serverInfoDTOList = Lists.newArrayList();
        serverInfoDTOList.add(serverInfoDTO);

        CabinetServerMappingEntity mappingEntity = new CabinetServerMappingEntity();
        mappingEntity.setServerId(serverId);
        mappingEntity.setCabinetId(cabinetId);
        List<CabinetServerMappingEntity> mappingEntityList = Lists.newArrayList();
        mappingEntityList.add(mappingEntity);

        new Expectations() {
            {
                serverService.listAllPhysicalServer();
                result = serverInfoDTOList;
                cabinetServerMappingDAO.findAllByCabinetId((UUID) any);
                result = mappingEntityList;
            }
        };

        List<PhysicalServerInfoDTO> physicalServerDTOList = cabinetMgmtAPI.listCabinetConfigedServer(request);

        Assert.assertFalse(physicalServerDTOList.size() == 0);
    }
}
