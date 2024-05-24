package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.ComputerListServiceImpl;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-02-16 21:50
 */
@RunWith(JMockit.class)
public class ComputerBusinessAPIImplTest {

    @Tested
    private ComputerBusinessAPIImpl computerBusinessAPIImpl;

    @Injectable
    private ComputerListServiceImpl computerListService;

    @Injectable
    private ComputerBusinessDAO computerBusinessDAO;

    @Injectable
    private ComputerBusinessService computerBusinessService;

    @Injectable
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;

    @Test
    public void testPageQueryIsNull() throws Exception {
        ThrowExceptionTester.throwIllegalArgumentException(() -> computerBusinessAPIImpl.pageQuery(null), "request can not be null");
        assertTrue(true);
    }

    @Test
    public void testPageQuerySuccess() throws BusinessException {
        final PageSearchRequest request = new PageSearchRequest();
        List<ComputerEntity> contentList = new ArrayList<>();
        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setId(UUID.randomUUID());
        computerEntity.setTerminalGroupId(UUID.randomUUID());
        contentList.add(computerEntity);
        Page<ComputerEntity> page = new PageImpl<>(contentList);
        List<ComputerEntity> computerEntityList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            ComputerEntity computerTempEntity = new ComputerEntity();
            computerTempEntity.setId(UUID.randomUUID());
            computerTempEntity.setTerminalGroupId(UUID.randomUUID());
            computerEntityList.add(computerTempEntity);
        }
        CbbTerminalGroupDetailDTO terminalGroupDTO = new CbbTerminalGroupDetailDTO();
        terminalGroupDTO.setGroupName("name");
        new Expectations() {
            {
                computerListService.pageQuery((PageSearchRequest) any, ComputerEntity.class);
                result = page;
                terminalGroupMgmtAPI.loadById((UUID) any);
                result = terminalGroupDTO;
            }
        };
        DefaultPageResponse<ComputerDTO> response = computerBusinessAPIImpl.pageQuery(request);
        Assert.assertEquals(Response.Status.SUCCESS, response.getStatus());

        DefaultPageResponse<ComputerDTO> response1 = computerBusinessAPIImpl.pageQuery(request);
        Assert.assertEquals(Response.Status.SUCCESS, response1.getStatus());
    }

    @Test
    public void testGetComputerInfoByComputerId() throws Exception {
        final UUID uuid = UUID.randomUUID();
        ComputerIdRequest request = new ComputerIdRequest();
        request.setComputerId(uuid);

        ComputerEntity baseInfoEntity = new ComputerEntity();
        baseInfoEntity.setTerminalGroupId(uuid);

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = baseInfoEntity;
            }
        };
        ComputerInfoResponse computerInfoResponse = computerBusinessAPIImpl.getComputerInfoByComputerId(request);
        Assert.assertEquals(uuid, computerInfoResponse.getGroupId());
    }

    @Test
    public void testGetComputerInfoByComputerIdByException() throws Exception {
        final UUID uuid = UUID.randomUUID();
        ComputerIdRequest request = new ComputerIdRequest();
        request.setComputerId(uuid);

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = null;
            }
        };
        try {
            ComputerInfoResponse computerInfoResponse = computerBusinessAPIImpl.getComputerInfoByComputerId(request);
            Assert.assertEquals(uuid, computerInfoResponse.getGroupId());
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER_NAME);
        }

    }

    /**
     * 测试deleteComputer
     *
     *@throws BusinessException 业务异常
     */
    @Test
    public void testDeleteComputer() throws BusinessException{
        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setState(ComputerStateEnum.OFFLINE);

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = computerEntity;
            }
        };

        computerBusinessAPIImpl.deleteComputer(new ComputerIdRequest());

        new Verifications() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                times = 1;
                computerBusinessDAO.deleteById((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 测试deleteComputer
     *
     *@throws BusinessException 业务异常
     */
    @Test
    public void testDeleteComputerStateOnline() throws BusinessException{
        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setState(ComputerStateEnum.ONLINE);

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = computerEntity;
            }
        };

        try {
            computerBusinessAPIImpl.deleteComputer(new ComputerIdRequest());
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), BusinessKey.RCDC_RCO_COMPUTER_ONLINE_NOT_ALLOW_DELETE);
        }

    }

    /**
     * 测试deleteComputer
     *
     *@throws BusinessException 业务异常
     */
    @Test
    public void testDeleteComputerDeleteException() throws BusinessException{
        ComputerEntity computerEntity = new ComputerEntity();
        computerEntity.setState(ComputerStateEnum.OFFLINE);

        ComputerIdRequest request = new ComputerIdRequest();

        new Expectations() {
            {
                computerBusinessDAO.findComputerEntityById((UUID) any);
                result = computerEntity;

                computerBusinessDAO.deleteById(request.getComputerId());
                result = new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_DELETE_COMPUTER_FAIL);
            }
        };

        try {
            computerBusinessAPIImpl.deleteComputer(request);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), BusinessKey.RCDC_RCO_COMPUTER_DELETE_COMPUTER_FAIL);
        }

    }

    /**
     *测试statisticsComputer
     *
     *@throws BusinessException 业务异常
     */
    @Test
    public void testStatisticsComputer() throws BusinessException {
        List<ComputerEntity> resultList = Lists.newArrayList();
        ComputerEntity computerEntity1 = new ComputerEntity();
        computerEntity1.setState(ComputerStateEnum.ONLINE);
        ComputerEntity computerEntity2 = new ComputerEntity();
        computerEntity2.setState(ComputerStateEnum.OFFLINE);
        resultList.add(computerEntity1);
        resultList.add(computerEntity2);

        new Expectations() {
            {
                computerBusinessDAO.findByTerminalGroupIdList((List<UUID>) any);
                result = resultList;
            }
        };

        computerBusinessAPIImpl.statisticsComputer(new UUID[]{UUID.randomUUID()});
        new Verifications() {
            {
                computerBusinessDAO.findAll();
                times = 0;
                computerBusinessDAO.findByTerminalGroupIdList((List<UUID>) any);
                times = 1;
            }
        };

    }

    /**
     *测试statisticsComputer
     *
     *@throws BusinessException 业务异常
     */
    @Test
    public void testStatisticsComputerWithGroupIdArr() throws BusinessException {

        computerBusinessAPIImpl.statisticsComputer(new UUID[]{});

        new Verifications() {
            {
                computerBusinessDAO.findAll();
                times = 1;
                computerBusinessDAO.findByTerminalGroupIdList((List<UUID>) any);
                times = 0;
            }
        };

    }

}
