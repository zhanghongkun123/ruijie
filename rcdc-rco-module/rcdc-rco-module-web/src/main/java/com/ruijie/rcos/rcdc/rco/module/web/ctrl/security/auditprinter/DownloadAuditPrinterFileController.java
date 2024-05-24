package com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditprinter;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditFileMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileDTO;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.common.helper.AuditApplyWebHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.common.request.DownloadAuditFileWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.io.IoUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.pagekit.api.PageQueryWebConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 流转审计的文件管理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/27
 *
 * @author WuShengQiang
 */
@Api(tags = "流转审计的文件管理")
@Controller
@RequestMapping("/rco/security/auditPrinter/printFile")
@PageQueryWebConfig(url = "/list", dtoType = AuditFileDTO.class)
public class DownloadAuditPrinterFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadAuditPrinterFileController.class);

    @Autowired
    private AuditFileMgmtAPI auditFileMgmtAPI;

    @Autowired
    private AuditApplyWebHelper auditApplyWebHelper;

    /**
     * 下载流转审计的文件
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     * @throws IOException io异常
     */
    @ApiOperation("下载流转审计的文件")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"安全中心-导出文件审计，下载流转审计的文件"})})
    @EnableAuthority
    public void download(DownloadAuditFileWebRequest request) throws BusinessException, IOException {
        Assert.notNull(request, "request不能为null");
        AuditFileDTO auditFileDTO = auditFileMgmtAPI.findAuditFileById(request.getId());
        UUID externalStorageId = auditFileMgmtAPI.obtainAuditFileGlobalConfig().getExternalStorageId();
        if (StringUtils.isBlank(auditFileDTO.getFileServerStoragePath()) || Objects.isNull(externalStorageId)) {
            throw new BusinessException(AuditFileBusinessKey.RCDC_AUDIT_FILE_DOWNLOAD_PATH_NOT_EXIST);
        }
        String fileServerStoragePrefix = String.format(Constants.HOME_EXTERNAL_STORAGE_PATH, externalStorageId);
        final File file = Paths.get(fileServerStoragePrefix, auditFileDTO.getFileServerStoragePath()).toFile();
        // 检查文件服务器是否健康、文件是否存在
        auditApplyWebHelper.checkExternalStorageAndFileExists(externalStorageId, file);

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes != null) {
            HttpServletResponse response = servletRequestAttributes.getResponse();
            if (response != null) {
                response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE);
                String fileName = StringUtils.convertEncoding(auditFileDTO.getFileName(), "utf-8", "ISO8859-1");
                response.setHeader("Content-disposition", "attachment;filename=" + fileName);
                response.addHeader("Content-Length", "" + file.length());
                ServletOutputStream outputStream = response.getOutputStream();
                FileInputStream fileInputStream = new FileInputStream(file);
                IoUtil.copy(fileInputStream, outputStream);
            }
        }
    }

}
