package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCheckAllowImageDriverUploadDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDriverImageBaseDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateDriverImageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUploadImageDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DriverProgramType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageDriverType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.imagedriver.request.UpdateDriverImageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imagedriver.request.ValidDriverNameRequest;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.ServerTypeDriverUploadLimit;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.driver.CheckAllowImageDriverUploadWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.PageQueryWithDataCntResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo.CheckImageDriverUploadVO;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.DeleteImageDriverHandlerFactory;
import com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop.UploadImageDriverHandlerFactory;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.config.ConfigFacadeHolder;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.ApiOperation;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/15
 *
 * @author songxiang
 */
@Controller
@RequestMapping("rco/clouddesktop/imageDriver")
public class ImageDriverCtrl {

    protected final static Logger LOGGER = LoggerFactory.getLogger(ImageDriverCtrl.class);

    @Autowired
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @Autowired
    private UploadImageDriverHandlerFactory uploadImageDriverHandlerFactory;

    @Autowired
    private DeleteImageDriverHandlerFactory deleteImageDriverHandlerFactory;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;


    private static final Integer FILE_NAME_MAX_LENGTH = 64;

    private static final UUID UPLOAD_IMAGE_DRIVER_TASK_ID = UUID.nameUUIDFromBytes("upload_image_driver_task_id".getBytes(StandardCharsets.UTF_8));

    private static final UUID DELETE_IMAGE_DRIVER_TASK_ID = UUID.nameUUIDFromBytes("delete_image_driver_task_id".getBytes(StandardCharsets.UTF_8));

    private static Map<String, Integer> ADMIN_UPLOAD_NUM = new HashMap<>();

    /**
     * @param file           文件对象
     * @param builder        builder 批任务
     * @param sessionContext 会话
     * @return 上传的响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("驱动上传")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/create")
    public DefaultWebResponse uploadDriverIso(ChunkUploadFile file, BatchTaskBuilder builder,
                                              SessionContext sessionContext) throws BusinessException {
        Assert.notNull(file, "file can not be null");
        Assert.notNull(builder, "builder can not be null");

        JSONObject customData = file.getCustomData();
        String note = customData.getString("note");
        String driverType = customData.getString("driverType");

        CbbUploadImageDriverDTO createImageDriverRequest = new CbbUploadImageDriverDTO();
        createImageDriverRequest.setDriverName(file.getFileName());
        createImageDriverRequest.setFilePath(file.getFilePath());
        createImageDriverRequest.setFileSize(file.getFileSize());
        createImageDriverRequest.setNote(note);
        createImageDriverRequest.setDriverType(ImageDriverType.valueOf(driverType));
        createImageDriverRequest.setFileMD5(file.getFileMD5());

        // 校验文件名称长度
        checkDriverNameLength(createImageDriverRequest.getDriverName());

        // 只能允许一个人同时上传驱动:
        checkUploadLimitNum(sessionContext.getUserId(), ImageDriverType.valueOf(driverType));

        BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UPLOAD_IMAGE_DRIVER_TASK_ID,
                LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_NAME));
        BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_NAME)
                .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_UPLOAD_TASK_DESC)
                .registerHandler(uploadImageDriverHandlerFactory.createHandler(batchTaskItem, createImageDriverRequest)).start();
        ADMIN_UPLOAD_NUM.remove(sessionContext.getUserId() + driverType);
        return DefaultWebResponse.Builder.success(result);
    }

    private synchronized void checkUploadLimitNum(UUID adminId, ImageDriverType driverType) throws BusinessException {

        String limitKey = ServerTypeDriverUploadLimit
                .getLimitKeyByServerModelAndDriverType(ServerModelEnum.getByName(serverModelAPI.getServerModel()), driverType);

        Integer limit = Optional.ofNullable(ConfigFacadeHolder.getFacade().read(limitKey)).map(Integer::parseInt).orElse(1);

        Integer num = ADMIN_UPLOAD_NUM.get(adminId + driverType.name());

        if (Objects.isNull(num)) {
            ADMIN_UPLOAD_NUM.put(adminId + driverType.name(), 1);
            return;
        }

        if (limit <= num) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_EXCEED_MAX_NUM, String.valueOf(limit));
        }

        ADMIN_UPLOAD_NUM.put(adminId + driverType.name(), num + 1);
    }

    /**
     * @param request        id请求
     * @param builder        builder
     * @return DefaultWebResponse 删除的响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation("删除")
    @ApiVersions({@ApiVersion(value = Version.V1_0_0), @ApiVersion(value = Version.V3_1_1, descriptions = {"分级分权-区分权限，加入权限控制"})})
    @RequestMapping(value = "/delete")
    public DefaultWebResponse deleteDriverIso(IdArrWebRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");
        UUID[] idArr = request.getIdArr();

        final Iterator<DefaultBatchTaskItem> iterator =
                Stream.of(idArr)
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SINGLE_TASK_NAME, new String[] {}).build())
                        .iterator();

        BatchTaskSubmitResult result = builder.setTaskName(deleteDriverTaskName(idArr))
                .setTaskDesc(deleteDriverTaskDesc(idArr))
                .registerHandler(deleteImageDriverHandlerFactory.createHandler(iterator))
                .start();
        return DefaultWebResponse.Builder.success(result);
    }

    private String deleteDriverTaskName(UUID[] idArr) {
        if (idArr.length == 1) {
            return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SINGLE_TASK_NAME;
        }
        return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_TASK_NAME;
    }
    
    private String deleteDriverTaskDesc(UUID[] idArr) {
        if (idArr.length == 1) {
            return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SINGLE_TASK_DESC;
        }

        return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_TASK_DESC;
    }

    /**
     * 判断是否能够上传
     *
     * @param request        校验是否能够上传的请求
     * @return DefaultWebResponse 响应请求
     */
    @RequestMapping(value = "/batchCheckUpload")
    public DefaultWebResponse batchCheckUpload(CheckAllowImageDriverUploadWebRequest request) {
        Assert.notNull(request, "request can not be null");

        CheckImageDriverUploadVO checkImageDriverUploadVO = new CheckImageDriverUploadVO();
        checkImageDriverUploadVO.setHasDuplication(false);
        try {

            // 校验驱动包上传数量
            checkUploadNum(request);
            // 校验驱动名称
            checkBatchDriverName(request);
            //校验上传空间
            cbbImageDriverMgmtAPI
                    .validUploadSpace(Arrays.stream(request.getFileInfoArr()).mapToLong(CbbCheckAllowImageDriverUploadDTO::getFileSize).sum());

            for (CbbCheckAllowImageDriverUploadDTO item : request.getFileInfoArr()) {
                cbbImageDriverMgmtAPI.validEnableUpload(item);
            }

            return DefaultWebResponse.Builder.success(checkImageDriverUploadVO);
        } catch (BusinessException e) {
            checkImageDriverUploadVO.setHasDuplication(true);
            checkImageDriverUploadVO
                    .setErrorMsg(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_VAILD_FAIL, e.getI18nMessage()));
            return DefaultWebResponse.Builder.success(checkImageDriverUploadVO);
        }
    }

    private void checkBatchDriverName(CheckAllowImageDriverUploadWebRequest request) throws BusinessException {

        for (CbbCheckAllowImageDriverUploadDTO cbbCheckAllowImageDriverUploadDTO : request.getFileInfoArr()) {

            checkDriverNameLength(cbbCheckAllowImageDriverUploadDTO.getFileName());

        }

        List<CbbImageDriverDTO> imageDriverDTOList = cbbImageDriverMgmtAPI.hasExistDriverByNameList(
                Arrays.stream(request.getFileInfoArr()).map(CbbCheckAllowImageDriverUploadDTO::getFileName).collect(Collectors.toList()));

        if (!CollectionUtils.isEmpty(imageDriverDTOList)) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_DUPLICATION_ERROR_MSG, StringUtils.join(
                    imageDriverDTOList.stream().map(CbbDriverImageBaseDTO::getDriverName).collect(Collectors.toList()).toArray(new String[0]), "，"));
        }

    }


    private void checkUploadNum(CheckAllowImageDriverUploadWebRequest request) throws BusinessException {
        String limitKey = ServerTypeDriverUploadLimit.getLimitKeyByServerModelAndDriverType(
                ServerModelEnum.getByName(serverModelAPI.getServerModel()), request.getFileInfoArr()[0].getDriverType());

        Integer limit = Optional.ofNullable(ConfigFacadeHolder.getFacade().read(limitKey)).map(Integer::parseInt).orElse(1);

        if (request.getFileInfoArr().length > limit) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_EXCEED_MAX_NUM_CHECK, String.valueOf(limit));
        }
    }

    /**
     * 分页查询
     *
     * @param request 分页查询请求
     * @return DefaultWebResponse 响应请求
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse listDriver(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        PageQueryResponse<CbbImageDriverDTO> pageResponseContent = cbbImageDriverMgmtAPI.pageQuery(request);
        Long driverCnt = cbbImageDriverMgmtAPI.getDriverCnt();
        PageQueryWithDataCntResponse<CbbImageDriverDTO> pageQueryWithDataCntResponse =
                new PageQueryWithDataCntResponse<>(pageResponseContent.getItemArr(), pageResponseContent.getTotal(), driverCnt);
        return DefaultWebResponse.Builder.success(pageQueryWithDataCntResponse);
    }

    /**
     * 编辑镜像驱动信息
     *
     * @param request        请求
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/edit")
    @EnableAuthority
    public DefaultWebResponse editDriverIso(UpdateDriverImageRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        String key = String.valueOf(request.getId());
        try {
            CbbImageDriverDTO cbbImageDriverDTO = cbbImageDriverMgmtAPI.findImageDriverInfo(request.getId());

            CbbUpdateDriverImageDTO cbbUpdateDriverImageDTO = new CbbUpdateDriverImageDTO();

            checkDriverNameLength(request.getDriverName());

            if (cbbImageDriverDTO.getDriverType() == ImageDriverType.USER_DRIVER_PROGRAM) {
                validReleaseTime(request.getReleaseTime());
                buildUpdateDriverProgram(request, cbbUpdateDriverImageDTO);
            } else {
                buildUpdateDriverPackage(request, cbbUpdateDriverImageDTO);
            }

            key = cbbImageDriverDTO.getDriverName();

            cbbImageDriverMgmtAPI.updateDriverImageInfo(cbbUpdateDriverImageDTO);

            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_SUC, cbbImageDriverDTO.getDriverName());
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_SUC,
                    new String[]{key});
        } catch (BusinessException e) {
            LOGGER.error("更新驱动信息失败[{}]", JSONObject.toJSONString(request), e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_FAIL, e, key);
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_FAIL, e, key, e.getI18nMessage());
        }
    }


    private void buildUpdateDriverPackage(UpdateDriverImageRequest request, CbbUpdateDriverImageDTO cbbUpdateDriverImageDTO) {
        cbbUpdateDriverImageDTO.setNote(request.getNote());
        cbbUpdateDriverImageDTO.setId(request.getId());
    }

    private void buildUpdateDriverProgram(UpdateDriverImageRequest request, CbbUpdateDriverImageDTO cbbUpdateDriverImageDTO) {

        cbbUpdateDriverImageDTO.setId(request.getId());
        cbbUpdateDriverImageDTO.setDriverName(request.getDriverName());
        cbbUpdateDriverImageDTO.setDriverVersion(request.getDriverVersion());
        cbbUpdateDriverImageDTO.setProvider(request.getProvider());
        cbbUpdateDriverImageDTO.setReleaseTime(request.getReleaseTime());
        cbbUpdateDriverImageDTO.setNote(request.getNote());
        if (!ArrayUtils.isEmpty(request.getHardwareIdArr())) {
            cbbUpdateDriverImageDTO.setHardwareIdArr(Arrays.stream(request.getHardwareIdArr()).distinct().toArray(String[]::new));
        }
        cbbUpdateDriverImageDTO.setOperatingSystemArr(request.getOperatingSystemArr());
        if (Objects.nonNull(request.getDriverProgramType())) {
            cbbUpdateDriverImageDTO.setDriverProgramType(DriverProgramType.valueOf(request.getDriverProgramType()));
        }

    }
    
    private void validReleaseTime(Date releaseTime) throws BusinessException {
        if (Objects.isNull(releaseTime)) {
            return;
        }
        if (releaseTime.after(new Date())) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_EDIT_RELEASE_TIME_ERROR);
        }
    }

    /**
     * @param request id请求
     * @return DefaultWebResponse 详情
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/getInfo")
    public DefaultWebResponse detailDriverIso(IdWebRequest request) throws BusinessException {
        return DefaultWebResponse.Builder.success(cbbImageDriverMgmtAPI.findImageDriverInfo(request.getId()));
    }

    /**
     * 驱动空间查询
     *
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "/space")
    public DefaultWebResponse getSpace() {
        return DefaultWebResponse.Builder.success(cbbImageDriverMgmtAPI.getDriverCatalogSpace());
    }


    /**
     * 驱动名称重名校验
     *
     * @param validDriverNameRequest 请求
     * @return CommonWebResponse
     */
    @RequestMapping(value = "/checkDuplication")
    public CommonWebResponse checkDriverNameExist(ValidDriverNameRequest validDriverNameRequest) {
        CheckDuplicationWebResponse webResponse = new CheckDuplicationWebResponse();
        webResponse.setHasDuplication(
                cbbImageDriverMgmtAPI.hasExistDriverByName(validDriverNameRequest.getDriverName(), validDriverNameRequest.getId()));
        return CommonWebResponse.success(webResponse);
    }

    private void checkDriverNameLength(String fileName) throws BusinessException {
        if (fileName.length() > FILE_NAME_MAX_LENGTH) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_DRIVER_IMAGE_NAME_EXCEED_MAX_LENGTH,
                    String.valueOf(FILE_NAME_MAX_LENGTH));
        }
    }

}
