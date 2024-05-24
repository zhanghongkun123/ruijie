package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateExportMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbExportImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbExportState;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageExportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListImageIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.dto.ViewImageExportDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.CheckExportSizeOverRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.CheckExportSizeOverResponse;
import com.ruijie.rcos.rcdc.rco.module.def.imageexport.request.PageQueryExportImageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.batchtask.DeleteExportImageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.batchtask.ExportImageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.dto.ImageExportInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request.DeleteImageExportFileWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request.DownloadImageFileWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request.ExportImageBatchTaskHandlerRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request.ImageExportWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.PageResponseContent;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/21 9:59
 *
 * @author ketb
 */
@Api(tags = "镜像导出")
@Controller
@RequestMapping("/rco/imageExport")
public class ImageExportController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageExportController.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbImageTemplateExportMgmtAPI cbbImageTemplateExportMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private ImageExportAPI imageExportAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    private static final String EXPORT_IMAGE_SIZE_ZERO = "0G";

    private static final String EXPORT_IMAGE_SIZE_UNIT_G = "G";

    private static final String SYMBOL_SPOT = ".";

    private static final String SORT_BY_EXPORT_TIME = "exportTime";

    private static final String IMAGE_TEMPLATE_ID = "imageTemplateId";

    private static final Integer EXACT_MATCH_LENGTH = 0;

    /**
     * 分页查询导出镜像
     *
     * @param webRequest     请求参数
     * @param sessionContext 会话
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("分页获取镜像导出信息")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonWebResponse<PageResponseContent> listExportImage(PageWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        // 如果不是超级管理员 添加查询条件
        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            ListImageIdResponse listImageIdResponse = adminDataPermissionAPI.listImageIdByAdminId(new ListImageIdRequest(sessionContext.getUserId()));
            List<String> imageIdList = listImageIdResponse.getImageIdList();
            // 镜像数据权限为空 直接返回
            if (CollectionUtils.isEmpty(imageIdList)) {
                // 返回空数组
                return CommonWebResponse.success(new PageResponseContent<ImageExportInfoDTO>(new ImageExportInfoDTO[0], 0));
            }
            ExactMatch[] exactMatchArr = buildPageWebRequest(webRequest, imageIdList);
            webRequest.setExactMatchArr(exactMatchArr);
        }

        PageQueryExportImageRequest request = new PageQueryExportImageRequest(webRequest);
        DefaultPageResponse<ViewImageExportDTO> response = imageExportAPI.queryImageExportInfoService(request);
        if (ObjectUtils.isEmpty(response)) {
            return CommonWebResponse.success(new PageResponseContent<ImageExportInfoDTO>(new ImageExportInfoDTO[0], 0));
        }
        ViewImageExportDTO[] itemArr = response.getItemArr();

        // 转换为镜像导出dto
        ImageExportInfoDTO[] imageExportInfoDTOArr =
                Arrays.stream(itemArr).map(this::convertDTO).collect(Collectors.toList()).toArray(new ImageExportInfoDTO[]{});

        PageResponseContent<ImageExportInfoDTO> pageResponseContent = new PageResponseContent(imageExportInfoDTOArr, response.getTotal());
        return CommonWebResponse.success(pageResponseContent);
    }


    /**
     * 检测是否已存在导出镜像
     *
     * @param webRequest 参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("是否已导出镜像")
    @RequestMapping(value = "isExported", method = RequestMethod.POST)
    public DefaultWebResponse isExported(ImageExportWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(webRequest.getId());
        if (imageTemplateInfoDTO == null || imageTemplateInfoDTO.getId() == null) {
            throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_CHECK_FAIL_BY_NOT_EXIST);
        }

        List<ViewImageExportDTO> exportImageList = imageExportAPI.getExportImageByImageDiskId(webRequest.getImageDiskIdArr()[0]);
        if (exportImageList == null || exportImageList.size() <= 0) {
            return DefaultWebResponse.Builder.success();
        }

        ImageExportInfoDTO dto = convertDTO(exportImageList.get(0));
        return DefaultWebResponse.Builder.success(dto);
    }

    /**
     * 导出镜像
     *
     * @param webRequest 参数
     * @param builder    参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("重新导出镜像")
    @RequestMapping(value = "reExport", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse reExportImage(ImageExportWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        return doExportImage(webRequest, builder, true);
    }


    /**
     * 导出镜像
     *
     * @param webRequest 参数
     * @param builder    参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("导出镜像")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "export", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse exportImage(ImageExportWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");
        return doExportImage(webRequest, builder, false);
    }


    private CommonWebResponse doExportImage(ImageExportWebRequest webRequest, BatchTaskBuilder builder, boolean reExport) throws BusinessException {
        Integer numOfExportImage = webRequest.getImageDiskIdArr().length;
        CheckExportSizeOverResponse response = imageExportAPI.checkExportSizeOver(new CheckExportSizeOverRequest(numOfExportImage));
        if (response.isOver()) {
            return CommonWebResponse.fail(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_FAIL_BY_EXPORTING_LIMIT,
                    new String[]{String.valueOf(response.getLimitSize())});
        }

        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(webRequest.getId())
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_TASK_NAME)).build())
                .iterator();
        ExportImageBatchTaskHandlerRequest request = new ExportImageBatchTaskHandlerRequest();
        request.setAuditLogAPI(auditLogAPI);
        request.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        request.setCbbImageTemplateExportMgmtAPI(cbbImageTemplateExportMgmtAPI);
        request.setImageExportAPI(imageExportAPI);
        request.setBatchTaskItemIterator(iterator);
        request.setStateMachineFactory(stateMachineFactory);
        request.setImageDiskIdArr(webRequest.getImageDiskIdArr());
        request.setReExport(reExport);
        final ExportImageBatchTaskHandler handler = new ExportImageBatchTaskHandler(request);
        handler.setBatch(false);
        BatchTaskSubmitResult result = builder.setTaskName(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_TASK_NAME)
                .setTaskDesc(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_TASK_DESC).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }


    /**
     * 删除镜像文件
     *
     * @param webRequest 请求参数
     * @param builder    批处理
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除导出的镜像文件")
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public CommonWebResponse<BatchTaskSubmitResult> deleteExportImageFile(DeleteImageExportFileWebRequest webRequest, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(builder, "builder is not null");

        final UUID[] idArr = webRequest.getIdArr();
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_DELETE_TASK_NAME)).build())
                .iterator();
        final DeleteExportImageBatchTaskHandler handler =
                new DeleteExportImageBatchTaskHandler(cbbImageTemplateMgmtAPI, cbbImageTemplateExportMgmtAPI, iterator, auditLogAPI);
        handler.setStateMachineFactory(stateMachineFactory);
        if (Arrays.asList(idArr).size() == 1) {
            handler.setBatch(false);
        }
        BatchTaskSubmitResult result = builder.setTaskName(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_DELETE_TASK_NAME)
                .setTaskDesc(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_BATCH_DELETE_TASK_DESC).registerHandler(handler).start();
        return CommonWebResponse.success(result);
    }

    /**
     * 下载
     *
     * @param webRequest 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下载导出的镜像文件")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    @NoAuthUrl
    public DownloadWebResponse downloadImageFile(DownloadImageFileWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        CbbExportImageTemplateDTO imageExportDTO = cbbImageTemplateExportMgmtAPI.findExportFileById(webRequest.getId());
        if (imageExportDTO == null) {
            auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_EXIST);
            throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_EXIST);
        }

        if (!CbbExportState.AVAILABLE.equals(imageExportDTO.getExportState())) {
            auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_STEADY,
                    imageExportDTO.getExportFileName());
            throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_STEADY,
                    imageExportDTO.getExportFileName());
        }

        String path = imageExportDTO.getExportFilePath();
        File imageFile = new File(path);
        if (!imageFile.exists()) {
            auditLogAPI.recordLog(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_EXIST);
            throw new BusinessException(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DOWNLOAD_FAIL_BY_NOT_EXIST);
        }

        String imageName = imageExportDTO.getExportFileName();
        int lastIndex = imageName.lastIndexOf(SYMBOL_SPOT);
        String suffix = imageName.substring(lastIndex + 1);
        CbbGetImageTemplateInfoDTO templateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageExportDTO.getImageTemplateId());
        String fileName = templateInfoDTO.getImageName();
        switch (imageExportDTO.getImageDiskType()) {
            case DATA:
                fileName += LocaleI18nResolver.resolve(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DATA_PART);
                break;
            case SYSTEM:
                fileName += LocaleI18nResolver.resolve(ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_SYSTEM_PART);
                break;
            default:
        }

        return new DownloadWebResponse.Builder().setContentType("application/octet-stream").setName(fileName, suffix).setFile(imageFile).build();
    }

    private ImageExportInfoDTO convertDTO(ViewImageExportDTO dto) {
        ImageExportInfoDTO infoDTO = new ImageExportInfoDTO();
        infoDTO.setId(dto.getId());
        infoDTO.setExportStatus(dto.getExportState());
        infoDTO.setImageName(dto.getImageTemplateName());
        infoDTO.setImageFileName(dto.getExportFileName());
        infoDTO.setImageTemplateState(dto.getImageTemplateState());
        infoDTO.setDescription(dto.getExportFileDesc());
        infoDTO.setCpuArch(dto.getCpuArch());
        infoDTO.setPlatformId(dto.getPlatformId());
        infoDTO.setPlatformName(dto.getPlatformName());
        infoDTO.setPlatformStatus(dto.getPlatformStatus());
        infoDTO.setPlatformType(dto.getPlatformType());
        if (dto.getExportFileCapacity() == null) {
            infoDTO.setFileSize(EXPORT_IMAGE_SIZE_ZERO);
        } else {
            infoDTO.setFileSize(String.format("%.2f", CapacityUnitUtils.byte2Gb(dto.getExportFileCapacity())) + EXPORT_IMAGE_SIZE_UNIT_G);
        }
        infoDTO.setExportTime(dto.getExportTime());
        infoDTO.setImageTemplateId(dto.getImageTemplateId());
        infoDTO.setImageDiskId(dto.getImageDiskId());
        try {
            CbbExportImageTemplateDTO cbbExportImageTemplateDTO = cbbImageTemplateExportMgmtAPI.findExportFileById(dto.getId());
            infoDTO.setImageDiskType(cbbExportImageTemplateDTO.getImageDiskType());

            switch (cbbExportImageTemplateDTO.getImageDiskType()) {
                case DATA:
                    infoDTO.setImageName(infoDTO.getImageName() + LocaleI18nResolver.resolve(//
                            ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_DATA_PART));
                    break;
                case SYSTEM:
                    infoDTO.setImageName(infoDTO.getImageName() + LocaleI18nResolver.resolve(//
                            ImageExportBusinessKey.RCDC_CLOUDDESKTOP_EXPORTIMAGE_SYSTEM_PART));
                    break;
                default:
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            // 不抛出
        }
        return infoDTO;
    }

    private ExactMatch[] buildPageWebRequest(PageWebRequest webRequest, List<String> imageIdList) {
        ExactMatch[] exactMatchArr = webRequest.getExactMatchArr();

        int matchArrSize;
        if (ObjectUtils.isEmpty(exactMatchArr)) {
            matchArrSize = EXACT_MATCH_LENGTH;
        } else {
            matchArrSize = exactMatchArr.length;
        }
        //增加一个镜像id查询条件
        ExactMatch[] resultArr = new ExactMatch[matchArrSize + 1];
        for (int i = 0; i < resultArr.length; i++) {
            resultArr[i] = new ExactMatch();
        }

        for (int i = 0; i < exactMatchArr.length; i++) {
            resultArr[i].setName(exactMatchArr[i].getName());
            resultArr[i].setValueArr(exactMatchArr[i].getValueArr());
        }

        //分级分权查询镜像
        resultArr[matchArrSize].setName(IMAGE_TEMPLATE_ID);
        String[] imageTemplateIdArr = new String[imageIdList.size()];
        for (int j = 0; j < imageIdList.size(); j++) {
            imageTemplateIdArr[j] = imageIdList.get(j);
        }

        resultArr[matchArrSize].setValueArr(imageTemplateIdArr);
        return resultArr;
    }

}
