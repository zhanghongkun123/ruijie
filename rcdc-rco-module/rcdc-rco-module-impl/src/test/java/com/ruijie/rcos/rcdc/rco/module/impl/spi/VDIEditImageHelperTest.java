package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateEditDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/19 22:30
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class VDIEditImageHelperTest {

    @Tested
    private VDIEditImageHelper helper;

    @Injectable
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Injectable
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Injectable
    private BaseAuditLogAPI auditLogAPI;

    @Injectable
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    /**
     * 检查是否有其它管理员编辑该镜像
     * @throws BusinessException 异常
     */
    @Test
    public void testGetImageEditingInfoIfPresent() throws BusinessException {
        UUID imageId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        String terminalId = "terminalId";

        helper.getImageEditingInfoIfPresent(imageId, adminId, terminalId);

        new Verifications() {
            {
                CbbImageTemplateEditDTO request;
                cbbImageTemplateMgmtAPI.checkImageTemplateEditing(request = withCapture());
                Assert.assertEquals(imageId, request.getImageTemplateId());
                Assert.assertEquals(adminId, request.getAdminId());
                Assert.assertEquals(terminalId, request.getTerminalId());
            }
        };
    }

    /**
     * 获取镜像编辑信息
     * @throws BusinessException 异常
     */
    @Test
    public void testGetImageEditingInfoMustExist() throws BusinessException {
        UUID imageId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        String terminalId = "terminalId";

        CbbImageTemplateEditingInfoDTO infoDTO = new CbbImageTemplateEditingInfoDTO();

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.checkImageTemplateEditing((CbbImageTemplateEditDTO) any);
                result = infoDTO;
            }
        };

        CbbImageTemplateEditingInfoDTO editingInfoDTO = helper.getImageEditingInfoMustExist(imageId, adminId, terminalId);
        Assert.assertNotNull(editingInfoDTO);
    }

    /**
     * 获取镜像编辑信息，为空
     * @throws BusinessException 异常
     */
    @Test
    public void testGetImageEditingInfoMustExistNull() throws BusinessException {
        UUID imageId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        String terminalId = "terminalId";

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.checkImageTemplateEditing((CbbImageTemplateEditDTO) any);
                result = null;
            }
        };

        try {
            helper.getImageEditingInfoMustExist(imageId, adminId, terminalId);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(e.getMessage().contains("image is not editing!"));
        }
    }

    /**
     * 获取镜像编辑信息，其他管理员在编辑
     * @throws BusinessException 异常
     */
    @Test
    public void testGetImageEditingInfoMustExistEditingByOther() throws BusinessException {
        UUID imageId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        String terminalId = "terminalId";

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.checkImageTemplateEditing((CbbImageTemplateEditDTO) any);
                result = new BusinessException("key");
            }
        };

        try {
            CbbImageTemplateEditingInfoDTO infoDTO = helper.getImageEditingInfoMustExist(imageId, adminId, terminalId);
            Assert.assertNull(infoDTO);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    /**
     * 记录日志，正常
     * @throws BusinessException 异常
     */
    @Test
    public void testRecordLog() throws BusinessException {
        UUID imageId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        String terminalMac = "terminalMac";

        IacAdminDTO adminDTO = new IacAdminDTO();
        adminDTO.setUserName("adminName");

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.getImageTemplateDetail((UUID) any).getImageName();
                result = "imageName";
                baseAdminMgmtAPI.getAdmin((UUID) any);
                result = adminDTO;
                cbbTerminalOperatorAPI.findBasicInfoByTerminalId(anyString).getMacAddr();
                result = terminalMac;
            }
        };

        helper.recordLog("key", adminId, imageId, terminalMac);

        new Verifications() {
            {
                String[] stringArr;
                auditLogAPI.recordLog(anyString, stringArr = withCapture());
                Assert.assertEquals("adminName", stringArr[0]);
                Assert.assertEquals("imageName", stringArr[1]);
                Assert.assertEquals("terminalMac", stringArr[2]);
            }
        };
    }

    /**
     * 记录日志，异常
     * @throws BusinessException 异常
     */
    @Test
    public void testRecordLogException() throws BusinessException {
        UUID imageId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        String terminalMac = "terminalMac";

        IacAdminDTO adminDTO = new IacAdminDTO();
        adminDTO.setUserName("adminName");

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.getImageTemplateDetail((UUID) any);
                result = new Exception("xxx");
            }
        };

        helper.recordLog("key", adminId, imageId, terminalMac);

        new Verifications() {
            {
                String[] stringArr;
                auditLogAPI.recordLog(anyString, stringArr = withCapture());
                Assert.assertEquals(adminId.toString(), stringArr[0]);
                Assert.assertEquals(imageId.toString(), stringArr[1]);
                Assert.assertEquals(terminalMac, stringArr[2]);
            }
        };
    }

    /**
     * 获取虚机ID
     */
    @Test
    public void testGetTempVmId() throws BusinessException {
        UUID vmId = UUID.randomUUID();
        CbbImageTemplateDetailDTO templateDetailDTO = new CbbImageTemplateDetailDTO();
        templateDetailDTO.setTempVmId(vmId);

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.getImageTemplateDetail((UUID) any);
                result = templateDetailDTO;
            }
        };

        UUID tempVmId = helper.getTempVmId(UUID.randomUUID());

        Assert.assertEquals(vmId, tempVmId);
    }
}