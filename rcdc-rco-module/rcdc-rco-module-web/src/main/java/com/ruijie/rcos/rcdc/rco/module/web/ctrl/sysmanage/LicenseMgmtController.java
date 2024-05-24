package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.BaseLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask.LicenseBatchUploadHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.common.SystemManagerConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.BaseUploadDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license.BaseDownLoadBatchLicenseFileWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license.BaseDownLoadLicenseFileWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license.BaseLicenseListWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license.CheckDuplicationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license.CreateDatFileWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.license.ViewDeviceIdWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.utils.DeleteFileUtil;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.crypto.Md5Builder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.DateUtils;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.base.zip.ZipUtil;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;

/**
 * Description: License管理组件对外controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年3月4日
 * 
 * @author zouqi
 */
@Controller
@RequestMapping("rco/license")
public class LicenseMgmtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseMgmtController.class);

    private static final String DATE_PATTERN = "yyyyMMddHHmmssSSS";

    private static final String DOT = ".";

    /** 导出平台唯一码的后缀名 */
    private static final String DEVICE_CODE_FILE_SUFFIX = "dat";

    /** 导出LICENSE 文件后缀名 */
    private static final String LICENSE_FILE_SUFFIX = "lic";

    private static final String ZIP_SUFFIX = "zip";

    @Autowired
    private BaseLicenseMgmtAPI baseLicenseMgmtAPI;

    @Autowired
    private ConfigFacade configFacade;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * l 获取平台唯一码请求
     *
     * @param request 获取平台唯一码的request
     *
     * @return 请求结果
     */
    @RequestMapping("/createDatFile/download")
    public DownloadWebResponse createDatFile(CreateDatFileWebRequest request) {
        Assert.notNull(request, "请求参数不能为空");

        return new DownloadWebResponse.Builder() // 创建者模式
                .setContentType("multipart/form-data") // 返回类型
                .setName("fileName", DEVICE_CODE_FILE_SUFFIX) // 设置文件下载名称
                // .setInputStream(baseCreateDatFileResponse.toInputStream(), baseCreateDatFileResponse.getFileContent().length())// 设置文件流
                .build();

    }

    /**
     * license文件导入
     *
     * @param file 上传的文件
     * @param batchTaskBuilder 批量操作类
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/uploadLicFile/create")
    public DefaultWebResponse uploadLicFile(ChunkUploadFile file, BatchTaskBuilder batchTaskBuilder) throws BusinessException {
        Assert.notNull(file, "ChunkUploadFile is null");
        Assert.notNull(batchTaskBuilder, "batchTaskBuilder is null");
        // 如果上传zip文件
        return batchUploadZipLicense(file, batchTaskBuilder);
    }

    /**
     * 批量上传 license zip文件
     * 
     * @param file 文件
     * @param batchTaskBuilder 批量处理参数
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    private DefaultWebResponse batchUploadZipLicense(ChunkUploadFile file, BatchTaskBuilder batchTaskBuilder) throws BusinessException {
        // 文件上传之前的操作
        List<BaseUploadDTO> dtoList = licenseUploadBefore(file);

        // 说明zip包内，只有文件夹，或为空
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_UPLOAD_FILE_NOT_FOUND_LIC_FILE, file.getFileName());
        }

        final Map<UUID, BaseUploadDTO> licFileInfoMap = new HashMap<>();

        dtoList.stream().forEach(dto ->
            licFileInfoMap.put(UUID.randomUUID(), dto)
        );

        final Iterator<DefaultBatchTaskItem> iterator = licFileInfoMap.keySet().stream()//
                .map(uid -> DefaultBatchTaskItem.builder().itemId(uid)//
                        .itemName(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_UPLOAD_TASK_NAME, new String[] {}).build())//
                .iterator();

        LicenseBatchUploadHandler licenseBatchZipUploadHandler =
                new LicenseBatchUploadHandler(iterator, auditLogAPI, licFileInfoMap, baseLicenseMgmtAPI);
        BatchTaskSubmitResult result = batchTaskBuilder.setTaskName(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_UPLOAD_BATCH_TASK_NAME)
                .setTaskDesc(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_BATCH_UPLOAD_TASK_DESC).registerHandler(licenseBatchZipUploadHandler)
                .start();

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 批量上传 license zip文件之前，的操作
     * 
     * @param file
     * @return BaseUploadDTO[] zip包里文件的信息
     * @throws BusinessException 业务异常
     */
    private List<BaseUploadDTO> licenseUploadBefore(ChunkUploadFile file) throws BusinessException {
        // 如果上传的zip文件
        File tmpFile = new File(file.getFilePath());
        // /opt/web/rcdc/zip/时间戳
        String filePath = configFacade.read(SystemManagerConstants.LICENSE_FILE_ZIP_PATH) + DateUtils.format(new Date(), DATE_PATTERN);
        File zipFolder = new File(filePath);

        // 如果文件夹不存在，创建文件夹
        if (!zipFolder.exists()) {
            zipFolder.mkdirs();
        }

        // 上传的文件类型
        String suffix = file.getFileName().substring(file.getFileName().lastIndexOf(DOT) + 1);
        if (ZIP_SUFFIX.equals(suffix)) {
            try {
                LOGGER.info("开始解压文件，开始地址：{}，结束地址{}", file.getFilePath(), zipFolder.getPath());
                ZipUtil.unzipFile(tmpFile, zipFolder);
            } catch (IOException e) {
                LOGGER.error("zip解压出错，文件名：{}", tmpFile.getName(), e);
                // 如果出问题，需要删除解压的文件
                DeleteFileUtil.deleteFile(zipFolder);
                throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_UN_ZIP_ERROR, e);
            }
        } else {
            String tempFilePath = filePath + File.separator + file.getFileName();
            try {
                LOGGER.info("开始移动文件，开始地址：{}，结束地址{}", file.getFilePath(), tempFilePath);
                Files.move(tmpFile.toPath(), new File(tempFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                LOGGER.error("文件移动异常，文件名：{}", tmpFile.getName(), e);
                throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_LICENSE_FILE_BEFORE_MOVE_ERROR, e);
            }
        }

        File[] fileArr = zipFolder.listFiles();

        return Arrays.stream(fileArr).filter(licFile -> !licFile.isDirectory()).map(licFile -> {
            BaseUploadDTO dto = new BaseUploadDTO();
            String fileName = licFile.getPath().substring(licFile.getPath().lastIndexOf(File.separator) + 1);
            dto.setFileName(fileName);
            try {
                dto.setFileMd5(getFileMD5(licFile.getPath()));
            } catch (IOException e) {
                // 不会抛出该异常
                throw new RuntimeException(e);
            }
            dto.setFilePath(licFile.getPath());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取文件的MD5值
     * 
     * @param filePath 文件路径
     *
     * @return String md5的值
     * @throws IOException 业务异常
     */
    private String getFileMD5(String filePath) throws IOException {
        byte[] md5Arr = Md5Builder.computeFileMd5(new File(filePath));
        return StringUtils.bytes2Hex(md5Arr);
    }

    /**
     * license文件列表
     *
     * @param webRequest web请求
     * @return 请求结果
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse listLicenseFeature(BaseLicenseListWebRequest webRequest) {

        Assert.notNull(webRequest, "请求参数不能为空");

        return DefaultWebResponse.Builder.success();
    }

    /**
     * license文件名校验
     *
     * @param request 请求参数
     *
     * @return 请求结果
     */
    @RequestMapping("/checkDuplication")
    public DefaultWebResponse checkDuplication(CheckDuplicationWebRequest request) {
        Assert.notNull(request, "CheckSoftNameDuplicationWebRequest is null");

        return DefaultWebResponse.Builder.success();
    }

    /**
     * 下载license文件
     *
     * @param webRequest web请求
     * @throws BusinessException 业务异常
     * @return 请求结果
     */
    @RequestMapping(value = "/download")
    public DownloadWebResponse downloadLicenseFile(BaseDownLoadLicenseFileWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "BaseDownLoadLicenseFileWebRequest is null");



        return new DownloadWebResponse.Builder()//
                .setContentType("application/octet-stream")//
                .setName("", LICENSE_FILE_SUFFIX)//
                .build();
    }

    /**
     * 下载license文件
     *
     * @param webRequest web请求
     * @throws BusinessException 业务异常
     * @return 请求结果
     */
    @RequestMapping(value = "batchLicenseFile/download")
    public DownloadWebResponse downloadBatchLicenseFile(BaseDownLoadBatchLicenseFileWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "BaseDownLoadLicenseFileWebRequest is null");


        return new DownloadWebResponse.Builder()//
                .setContentType("application/octet-stream")//
                .setName("", ZIP_SUFFIX).build();
    }

    /**
     * 查看平台唯一码
     *
     * @param webRequest web请求
     * @return 请求结果
     */
    @RequestMapping(value = "/getLicenseCode")
    public DefaultWebResponse viewDeviceId(ViewDeviceIdWebRequest webRequest) {
        Assert.notNull(webRequest, "请求参数不能为空");


        return DefaultWebResponse.Builder.success();
    }
}
