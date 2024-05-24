package com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDiskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateFileMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.IDVImageFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.util.FileSignatureUtils;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.constants.AppConstants;
import com.ruijie.rcos.rcdc.rca.module.def.dto.ImageProgramDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.clouddock.response.CloudDockCommonResponse;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.FtpConfigInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.CloudDockRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto.BtInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto.ImageAppInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto.ImageFileDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.dto.ImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.request.CloudDockPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.request.ImageAppListPageRequest;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

/**
 * Description: 云坞rest impl
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/4
 *
 * @author chenjuan
 */
public class CloudDockRestServerImpl implements CloudDockRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDockRestServerImpl.class);

    // OA-应用执行器
    private static final String NEW_LAUCHER_APP_PATH = "C:\\Windows\\RCDAgent\\RcaRemoteLaucher.exe";

    // OA-自发布器
    private static final String NEW_PUBLISH_APP_ARGS = "RCDAgent.exe -c ProductPublishCloudApp.json";

    // GT-应用执行器
    private static final String OLD_LAUCHER_APP_PATH = "C:\\Windows\\RCA-HostAgent\\rcaremotelaucher.exe";

    // GT-自发布器 C:\\Windows\\RCA-HostAgent\\win-ia32-unpacked\\PublishCloudApp.exe
    private static final String OLD_PUBLISH_APP_PATH = "C:\\Windows\\RCA-HostAgent\\win-ia32-unpacked\\PublishCloudApp.exe";

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private CbbImageTemplateFileMgmtAPI cbbImageTemplateFileMgmtAPI;

    @Autowired
    private CbbImageDiskMgmtAPI cbbImageDiskMgmtAPI;

    @Autowired
    private RcaHostAppAPI rcaHostAppAPI;

    @Autowired
    private ConfigFacade configFacade;

    private static final String IMAGE_TORRENT_PATH_PREFIX = "/opt/ftp/terminal";

    private static final String GROUP_ID = "groupId";

    private static final String RESTORE_POINT_ID = "restorePointId";


    @Override
    public CloudDockCommonResponse<FtpConfigInfoDTO> getFtpAccount() {

        CloudDockCommonResponse<FtpConfigInfoDTO> response = new CloudDockCommonResponse<>();
        try {
            FtpConfigInfoDTO ftpConfigInfoDTO = imageTemplateAPI.getFtpAccount();
            response.setStatus(WebResponse.Status.SUCCESS);
            response.setContent(ftpConfigInfoDTO);
        } catch (BusinessException e) {
            LOGGER.error("获取镜像ftp异常", e);
            response.setStatus(WebResponse.Status.ERROR);
            response.setContent(null);
        }
        return response;
    }

    @Override
    public CloudDockCommonResponse<DefaultPageResponse<ImageTemplateInfoDTO>> getImageTemplateList(CloudDockPageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        LOGGER.debug("getImageTemplateList request：{}", JSON.toJSONString(request));
        // 构建分页查询数据
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder()
                .setPageLimit(request.getPage(), request.getLimit());
        if (StringUtils.isNotBlank(request.getSearchKeyword())) {
            requestBuilder.composite(criteriaBuilder -> {
                List<Match> matchList = Lists.newArrayList();
                matchList.add(criteriaBuilder.like("imageTemplateName", request.getSearchKeyword()));
                matchList.add(criteriaBuilder.like("imageFileName", request.getSearchKeyword()));
                return criteriaBuilder.or(matchList.stream().toArray(Match[]::new));
            });
        }

        MatchEqual[] matchEqualArr = request.getExactMatchArr();
        if (ArrayUtils.isNotEmpty(matchEqualArr)) {
            for (MatchEqual matchEqual : matchEqualArr) {
                requestBuilder.in(matchEqual.getName(), matchEqual.getValueArr());
            }
        }

        CloudDockCommonResponse<DefaultPageResponse<ImageTemplateInfoDTO>> response = new CloudDockCommonResponse<>();
        DefaultPageResponse<ImageTemplateInfoDTO> pageResponse = new DefaultPageResponse<>();
        try {
            PageQueryResponse<CbbImageTemplateDTO> pageQueryResponse = cbbImageTemplateMgmtAPI.pageQuery(requestBuilder.build());

            List<ImageTemplateInfoDTO> imageTemplateInfoDTOList = new ArrayList<>();
            buildImageTemplateInfoDTOList(imageTemplateInfoDTOList, pageQueryResponse);

            LOGGER.debug("返回镜像信息imageTemplateInfoDTOList：{}", JSON.toJSONString(imageTemplateInfoDTOList));
            pageResponse.setItemArr(imageTemplateInfoDTOList.stream().toArray(ImageTemplateInfoDTO[]::new));
            pageResponse.setTotal(pageQueryResponse.getTotal());

            response.setContent(pageResponse);
            response.setStatus(WebResponse.Status.SUCCESS);
        } catch (BusinessException e) {
            LOGGER.error("云坞分页获取镜像列表异常，异常原因：", e);
            response.setContent(pageResponse);
            response.setStatus(WebResponse.Status.ERROR);
        }
        return response;
    }

    private void buildImageTemplateInfoDTOList(List<ImageTemplateInfoDTO> imageTemplateInfoDTOList,
                                               PageQueryResponse<CbbImageTemplateDTO> pageQueryResponse) throws BusinessException {
        if (pageQueryResponse.getTotal() > 0) {
            for (CbbImageTemplateDTO cbbImageTemplateDTO : pageQueryResponse.getItemArr()) {
                ImageTemplateInfoDTO cbbImageTemplateInfoDTO = new ImageTemplateInfoDTO();
                BeanUtils.copyProperties(cbbImageTemplateDTO, cbbImageTemplateInfoDTO);
                List<ImageFileDTO> imageFileDTOList = new ArrayList<>();
                UUID imageId = cbbImageTemplateDTO.getId();
                // 镜像总大小
                long totalImageFileSize = 0L;
                List<CbbImageDiskDetailDTO> imageDiskDetailDTOList = cbbImageDiskMgmtAPI.findByImageId(imageId);
                for (CbbImageDiskDetailDTO imageDiskDetailDTO : imageDiskDetailDTOList) {
                    LOGGER.debug("镜像[{}]存在磁盘信息：{}", imageId, JSON.toJSONString(imageDiskDetailDTO));
                    ImageFileDTO imageDataFileInfo = getImageFileInfo(imageDiskDetailDTO);
                    imageFileDTOList.add(imageDataFileInfo);
                    // 获取镜像base和差分文件大小
                    totalImageFileSize += imageDataFileInfo.getBaseFileSize() + imageDataFileInfo.getDiffFileSize();

                }
                cbbImageTemplateInfoDTO.setTotalSize(totalImageFileSize);
                cbbImageTemplateInfoDTO.setImageFileList(imageFileDTOList);
                imageTemplateInfoDTOList.add(cbbImageTemplateInfoDTO);
            }
        }
    }

    private ImageFileDTO getImageFileInfo(CbbImageDiskDetailDTO imageDiskDetailDTO) throws BusinessException {
        UUID imageId = imageDiskDetailDTO.getId();
        UUID lastRecoveryPointId = imageDiskDetailDTO.getLastRecoveryPointId();
        IDVImageFileDTO idvImageFileDTO;
        if (lastRecoveryPointId != null) {
            idvImageFileDTO = cbbImageTemplateFileMgmtAPI.buildQcow2FileOfImageId(lastRecoveryPointId);
            LOGGER.debug("镜像还原点[{}]文件信息：{}", lastRecoveryPointId, idvImageFileDTO);
        } else {
            idvImageFileDTO = cbbImageTemplateFileMgmtAPI.buildQcow2FileOfImageId(imageId);
            LOGGER.debug("镜像[{}]文件信息：{}", imageId, idvImageFileDTO);
        }
        ImageFileDTO imageFileDTO = new ImageFileDTO();
        if (idvImageFileDTO == null) {
            return imageFileDTO;
        }


        IDVImageFileDTO baseImageFile = getBaseImageFile(idvImageFileDTO);
        IDVImageFileDTO diffImageFile = getDiffImageFile(idvImageFileDTO);

        buildBaseImageFile(imageFileDTO, baseImageFile, imageDiskDetailDTO);
        buildDiffImageFile(imageFileDTO, diffImageFile, imageDiskDetailDTO);

        // 磁盘大小和盘符
        imageFileDTO.setDiskType(imageDiskDetailDTO.getImageDiskType());
        if (CbbImageDiskType.SYSTEM == imageDiskDetailDTO.getImageDiskType()) {
            imageFileDTO.setDiskSize(imageDiskDetailDTO.getSystemDiskSize());
            imageFileDTO.setDiskSymbol(null);
        } else {
            imageFileDTO.setDiskSize(imageDiskDetailDTO.getVmDiskSize());
            imageFileDTO.setDiskSymbol(imageDiskDetailDTO.getDiskSymbol());
        }
        return imageFileDTO;
    }

    private void buildDiffImageFile(ImageFileDTO imageFileDTO, IDVImageFileDTO diffImageFile, CbbImageDiskDetailDTO imageDiskDetailDTO) {
        if (diffImageFile != null) {
            imageFileDTO.setDiffFileName(diffImageFile.getFileName());
            imageFileDTO.setDiffFilePath(diffImageFile.getFilePath());
            if (diffImageFile.getFileMD5() == null) {
                String calMd5 = FileSignatureUtils.calMd5(diffImageFile.getFilePath());
                imageFileDTO.setDiffFileMd5(calMd5);
            } else {
                imageFileDTO.setDiffFileMd5(diffImageFile.getFileMD5());
            }
            Long fileSize = diffImageFile.getFileSize();
            if (fileSize == null) {
                imageFileDTO.setDiffFileSize(0L);
            } else {
                imageFileDTO.setDiffFileSize(fileSize);
            }

            BtInfoDTO diffBtInfoDTO = new BtInfoDTO();
            if (diffImageFile.getTorrentFilePath() == null) {
                imageFileDTO.setDiffBtInfo(diffBtInfoDTO);
            } else {
                diffBtInfoDTO.setTorrentFileMD5(diffImageFile.getTorrentFileMD5());
                diffBtInfoDTO.setTorrentFileName(diffImageFile.getTorrentFileName());
                diffBtInfoDTO.setTorrentFilePath(StringUtils.remove(diffImageFile.getTorrentFilePath(), IMAGE_TORRENT_PATH_PREFIX));
                imageFileDTO.setDiffBtInfo(diffBtInfoDTO);
            }
        }
    }

    private void buildBaseImageFile(ImageFileDTO imageFileDTO, IDVImageFileDTO baseImageFile, CbbImageDiskDetailDTO imageDiskDetailDTO) {
        if (imageFileDTO != null) {
            imageFileDTO.setBaseFileName(baseImageFile.getFileName());
            imageFileDTO.setBaseFilePath(baseImageFile.getFilePath());
            if (baseImageFile.getFileMD5() == null) {
                String calMd5 = FileSignatureUtils.calMd5(baseImageFile.getFilePath());
                imageFileDTO.setBaseFileMd5(calMd5);
            } else {
                imageFileDTO.setBaseFileMd5(baseImageFile.getFileMD5());
            }

            imageFileDTO.setBaseFileSize(baseImageFile.getFileSize());
            BtInfoDTO baseBtInfoDTO = new BtInfoDTO();

            if (baseImageFile.getTorrentFilePath() == null) {
                imageFileDTO.setBaseBtInfo(baseBtInfoDTO);
            } else {
                baseBtInfoDTO.setTorrentFileMD5(baseImageFile.getTorrentFileMD5());
                baseBtInfoDTO.setTorrentFileName(baseImageFile.getTorrentFileName());
                baseBtInfoDTO.setTorrentFilePath(StringUtils.remove(baseImageFile.getTorrentFilePath(), IMAGE_TORRENT_PATH_PREFIX));
                imageFileDTO.setBaseBtInfo(baseBtInfoDTO);
            }
        }
    }

    private IDVImageFileDTO getDiffImageFile(IDVImageFileDTO idvImageFileDTO) {

        if (idvImageFileDTO.getBackingFile() == null) {
            // 镜像未发布，无差分文件
            return null;
        }
        if (idvImageFileDTO.getBackingFile() != null && idvImageFileDTO.getBackingFile().getBackingFile() != null) {
            return idvImageFileDTO.getBackingFile();
        }
        return idvImageFileDTO;
    }

    private IDVImageFileDTO getBaseImageFile(IDVImageFileDTO idvImageFileDTO) {
        // 只有base文件
        if (idvImageFileDTO.getBackingFile() == null) {
            return idvImageFileDTO;
        }
        if (idvImageFileDTO.getBackingFile() != null && idvImageFileDTO.getBackingFile().getBackingFile() != null) {
            return idvImageFileDTO.getBackingFile().getBackingFile();
        }
        return idvImageFileDTO.getBackingFile();
    }

    @Override
    public CloudDockCommonResponse<DefaultPageResponse<ImageAppInfoDTO>> getAppList(PageWebRequest request) {
        Assert.notNull(request, "request can not be null");

        CloudDockCommonResponse<DefaultPageResponse<ImageAppInfoDTO>> response = new CloudDockCommonResponse<>();
        DefaultPageResponse<ImageAppInfoDTO> pageResponse = new DefaultPageResponse<>();
        try {
            ImageAppListPageRequest pageRequest = new ImageAppListPageRequest(request);

            DefaultPageResponse<ImageProgramDTO> pageQueryResponse = rcaHostAppAPI.pageQuery(pageRequest);

            ImageProgramDTO[] responseItemArr = pageQueryResponse.getItemArr();
            ImageAppInfoDTO[] imageAppInfoDTOArr = Arrays.stream(responseItemArr).map(imageProgramDTO -> {
                ImageAppInfoDTO imageAppInfoDTO = new ImageAppInfoDTO();
                // 684911-通过核对，转给云坞时，OA的应用发布器和执行器需要强转成GT的路径
                convertOaAppPath(imageProgramDTO);
                BeanUtils.copyProperties(imageProgramDTO, imageAppInfoDTO);
                getAbsAppLogoPath(imageProgramDTO.getIconPath(), imageAppInfoDTO);
                return imageAppInfoDTO;
            }).toArray(ImageAppInfoDTO[]::new);
            pageResponse.setItemArr(imageAppInfoDTOArr);
            pageResponse.setTotal(pageQueryResponse.getTotal());

            response.setStatus(WebResponse.Status.SUCCESS);
            response.setContent(pageResponse);
        } catch (BusinessException e) {
            response.setStatus(WebResponse.Status.ERROR);
            response.setContent(pageResponse);
        }
        return response;
    }

    private void getAbsAppLogoPath(String appPath, ImageAppInfoDTO imageAppInfoDTO) {
        if (StringUtils.isEmpty(appPath)) {
            return;
        }
        String saveDirectory = configFacade.read(AppConstants.RCDC_RCA_APP_LOGO_PATH_CONFIG);
        String fileName = StringUtils.substring(appPath, AppConstants.RCDC_RCA_APP_LOGO_URL.length());
        String iconPath = saveDirectory + File.separator + fileName;
        File tmpFile = new File(iconPath);
        if (tmpFile.exists()) {
            try {
                byte[] imageBytesArr = Files.readAllBytes(tmpFile.toPath());
                String icon = Base64.getEncoder().encodeToString(imageBytesArr);
                imageAppInfoDTO.setIcon(icon);
            } catch (Exception e) {
                LOGGER.error("获取应用图标异常，e={}", e);
            }
        }
    }

    private void convertOaAppPath(ImageProgramDTO imageProgramDTO) {
        if (NEW_LAUCHER_APP_PATH.equals(imageProgramDTO.getPath())) {
            // 启动参数为空的是执行器
            if (StringUtils.isEmpty(imageProgramDTO.getArgs())) {
                imageProgramDTO.setPath(OLD_LAUCHER_APP_PATH);
                return;
            }
            if (NEW_PUBLISH_APP_ARGS.equals(imageProgramDTO.getArgs())) {
                imageProgramDTO.setPath(OLD_PUBLISH_APP_PATH);
                imageProgramDTO.setArgs(StringUtils.EMPTY);
                return;
            }
        }
    }
}
