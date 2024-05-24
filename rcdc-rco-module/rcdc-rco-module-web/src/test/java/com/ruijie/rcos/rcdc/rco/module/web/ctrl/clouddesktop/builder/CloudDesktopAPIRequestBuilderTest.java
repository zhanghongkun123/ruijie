package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.builder;


import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCreateImageTemplateByCloneExistDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbUpdateImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.osfile.CbbImportOsFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.CloneImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ConfigVmForEditImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.UpdateImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo.NetworkVO;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

import mockit.Expectations;
import mockit.integration.junit4.JMockit;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/3/30 <br>
 *
 * @author yyz
 */

@RunWith(JMockit.class)
public class CloudDesktopAPIRequestBuilderTest {

    /**
     * 测试构建导入Os Iso文件请求
     */
    @Test
    public void testBuildImportOsFileRequest() {
        ChunkUploadFile chunkUploadFile = new ChunkUploadFile();
        chunkUploadFile.setFileMD5("f86aa38cbf0e5a46520ba2ebdbde5e88");
        chunkUploadFile.setFileName("filename");
        chunkUploadFile.setFilePath("path");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("note", "note");
        chunkUploadFile.setCustomData(jsonObject);

        CbbImportOsFileDTO ImportOsFileRequest =
                CloudDesktopAPIRequestBuilder.buildImportOsFileRequest(chunkUploadFile);

        Assert.assertEquals(ImportOsFileRequest.getOsFileMD5(), "f86aa38cbf0e5a46520ba2ebdbde5e88");
        Assert.assertEquals(ImportOsFileRequest.getOsFileName(), "filename");
        Assert.assertEquals(ImportOsFileRequest.getOsFilePath(), "path");
    }


    /**
     * 测试构建配置Vm用于编辑模板请求
     */
    @Test
    public void testBuildConfigVmForEditImageTemplateRequest() {
        ConfigVmForEditImageTemplateWebRequest request = new ConfigVmForEditImageTemplateWebRequest();
        UUID uuid = UUID.randomUUID();
        Integer cpuCoreCount = new Integer(2);
        Integer diskSize = new Integer(50);
        UUID imageTemplateId = UUID.randomUUID();
        Double memorySize = new Double(16);
        Integer mbMemorySize = 16384;
        NetworkVO networkVO = new NetworkVO();
        networkVO.setId(uuid);

        request.setCpu(cpuCoreCount);
        request.setNetwork(networkVO);
        request.setSystemDisk(diskSize);
        request.setId(imageTemplateId);
        request.setMemory(memorySize);

        CbbConfigVmForEditImageTemplateDTO webRequest =
                CloudDesktopAPIRequestBuilder.buildConfigVmForEditImageTemplateRequest(request);

        Assert.assertEquals(webRequest.getCpuCoreCount(), cpuCoreCount);
        Assert.assertEquals(webRequest.getDeskNetworkId(), uuid);
        Assert.assertEquals(webRequest.getDiskSize(), diskSize);
        Assert.assertEquals(webRequest.getImageTemplateId(), imageTemplateId);
        Assert.assertEquals(webRequest.getMemorySize(), mbMemorySize);
    }

    /**
     * 测试通过克隆存在请求构建模板的参数
     */
    @Test
    public void testBuildCreateImageTemplateByCloneExistRequest() {
        UUID oldId = UUID.randomUUID();
        CloneImageTemplateWebRequest webRequest = new CloneImageTemplateWebRequest();
        webRequest.setId(oldId);
        webRequest.setImageName("label");

        new Expectations(UUID.class) {
            {
                UUID.randomUUID();
                result = UUID.randomUUID();
            }
        };

        CbbCreateImageTemplateByCloneExistDTO request =
                CloudDesktopAPIRequestBuilder.buildCreateImageTemplateByCloneExistRequest(webRequest);

        Assert.assertEquals(request.getOldImageTemplateId(), oldId);
        Assert.assertEquals(request.getNewImageTemplateName(), "label");
    }

    /**
     * 测试构建更新图板请求
     */
    @Test
    public void testBuildUpdateImageTemplateRequest() {

        UpdateImageTemplateWebRequest webRequest = new UpdateImageTemplateWebRequest();
        UUID uuid = UUID.randomUUID();
        webRequest.setId(uuid);
        webRequest.setImageName("imageTemplateName");
        webRequest.setImageSystemType(CbbOsType.WIN_7_32);

        CbbUpdateImageTemplateDTO request = CloudDesktopAPIRequestBuilder.buildUpdateImageTemplateRequest(webRequest);

        Assert.assertEquals(request.getImageTemplateId(), uuid);
        Assert.assertEquals(request.getImageTemplateName(), "imageTemplateName");
        Assert.assertEquals(request.getCbbOsType(), CbbOsType.WIN_7_32);
    }


}
