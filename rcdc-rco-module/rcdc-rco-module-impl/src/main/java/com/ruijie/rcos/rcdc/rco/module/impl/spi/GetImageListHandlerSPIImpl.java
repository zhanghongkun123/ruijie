package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ControlStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageCalcService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestImageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.ImageInfoVO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.ImagePageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 终端获取 镜像列表
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.04.25
 *
 * @author linhj
 */
@DispatcherImplemetion(ImageDispatcherConstants.PERMISSION_IMAGE_LIST)
public class GetImageListHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetImageListHandlerSPIImpl.class);

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private ImageCalcService imageCalcService;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.hasText(request.getData(), "data in request cannot be blank!");

        Integer errorCode = -1;

        CbbResponseShineMessage<?> cbbResponseShineMessage;
        LOGGER.info("终端请求镜像模板列表信息，terminalId={},请求信息为{}", request.getTerminalId(), JSON.toJSONString(request));
        // 1、入参校验
        JSONObject dataJson = JSONObject.parseObject(request.getData());
        // 构造镜像请求DTO
        ShineRequestImageDTO shineRequestImageDTO = dataJson.toJavaObject(ShineRequestImageDTO.class);

        // 分页查询构造器
        PageQueryBuilderFactory.RequestBuilder requestBuilder = pageQueryBuilderFactory.newRequestBuilder(new ImagePageQueryRequest())
                .eq("cbbImageType", shineRequestImageDTO.getType()).eq("imageTemplateState", ImageTemplateState.AVAILABLE);
        // JOSN返回结构
        JSONObject result = new JSONObject();
        try {
            // 做数据权限过滤
            AdminLoginOnTerminalCache adminCache = adminCacheManager.getIfPresent(shineRequestImageDTO.getAdminSessionId());
            // 判断session 是否存在
            if (adminCache == null) {
                LOGGER.info("当前终端管理员会话过期，管理员 getAdminSessionId={}", shineRequestImageDTO.getAdminSessionId());
                // 给shine 信息 -20 代表session过期不存在
                messageHandlerAPI.response(ShineMessageUtil.buildErrorResponseMessage(request, PermissionConstants.SESSION_NOT_EXIST));
                return;
            }
            LOGGER.info("当前终端请求镜像模板列表，管理员adminId={},信息为{}", adminCache.getAdminId(), JSON.toJSONString(adminCache));

            PageQueryResponse<CbbImageTemplateDTO> pageQueryResponse = imageTemplateAPI.queryByAdmin(adminCache.getAdminId(),requestBuilder);
            
            if (pageQueryResponse.getTotal() == 0) {
                result.put("imageList", null);
                cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, result);
                messageHandlerAPI.response(cbbResponseShineMessage);
                return;
            }
            
            // 查询终端型号
            TerminalDTO terminal = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());
            
            List<ImageInfoVO> imageInfoVOList = new ArrayList<>();
            // 构造镜像VO列表返回
            buildImageInfoVOList(terminal, imageInfoVOList, pageQueryResponse);
            result.put("imageList", imageInfoVOList);
            cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, result);
        } catch (BusinessException ex) {
            LOGGER.error("获取镜像信息失败", ex);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, errorCode);
        } catch (Exception ex) {
            LOGGER.error("获取镜像未知异常", ex);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, errorCode);
        }
        messageHandlerAPI.response(cbbResponseShineMessage);
    }


    /**
     * 构造镜像VO列表返回
     *
     * @param imageInfoVOList 镜像信息
     * @param pageQueryResponse 分页信息
     * @param terminal 终端信息
     */
    private void buildImageInfoVOList(TerminalDTO terminal, List<ImageInfoVO> imageInfoVOList,
            PageQueryResponse<CbbImageTemplateDTO> pageQueryResponse) throws BusinessException {

        if (pageQueryResponse.getTotal() > 0) {
            for (CbbImageTemplateDTO cbbImageTemplateDTO : pageQueryResponse.getItemArr()) {
                // 服务器处于备份中则不可编辑镜像
                if (ControlStateEnum.BACKUP != cbbImageTemplateDTO.getControlState()) {
                    ImageInfoVO imageInfoVO = new ImageInfoVO();
                    imageInfoVO.setImageId(cbbImageTemplateDTO.getId());
                    imageInfoVO.setImageName(cbbImageTemplateDTO.getImageTemplateName());
                    imageInfoVO.setOsType(cbbImageTemplateDTO.getOsType());
                    imageInfoVO.setSystemDiskSize(cbbImageTemplateDTO.getSystemDiskSize());
                    imageInfoVO.setSupportGoldenImage(cbbImageTemplateDTO.isSupportGoldenImage());
                    imageInfoVO.setDesktopRedirect(false);
                    imageInfoVO.setCbbImageType(cbbImageTemplateDTO.getCbbImageType());
                    try {
                        List<CbbImageDiskInfoDTO> availableImageDiskInfoList = //
                                cbbImageTemplateMgmtAPI.getPublishedImageDiskInfoList(cbbImageTemplateDTO.getId());
                        List<CbbImageDiskInfoDTO> imageDiskList = Optional.ofNullable(availableImageDiskInfoList).orElse(Lists.newArrayList());
                        imageInfoVO.setImageDiskList(imageDiskList);
                    } catch (Exception e) {
                        LOGGER.error("获取镜像磁盘id:[{}]信息失败:[{}]", cbbImageTemplateDTO.getId(), e);
                        imageInfoVO.setImageDiskList(Lists.newArrayList());
                    }
                    try {
                        Integer baseFileSize = imageCalcService.getImageFileSize(cbbImageTemplateDTO.getId());
                        imageInfoVO.setBaseFileSize(baseFileSize);
                    } catch (BusinessException ex) {
                        LOGGER.error("获取镜像大小失败, imageId: {}", cbbImageTemplateDTO.getId());
                        LOGGER.error("获取镜像大小失败", ex);
                        imageInfoVO.setBaseFileSize(0);
                    }
                    imageInfoVOList.add(imageInfoVO);
                }
            }
        }
    }

    /**
     * TCI 镜像类型 Windows 7 不支持 G3 终端镜像编辑
     *
     * @return 是否
     */
    private boolean checkNotEditPermission(TerminalDTO terminalDTO, CbbImageTemplateDTO cbbImageTemplateDTO) {

        // 是否属于 G3 型号终端
        if (StringUtils.isEmpty(terminalDTO.getProductType()) || !terminalDTO.getProductType().contains("G3")) {
            return false;
        }

        // 终端是否属于 TCI 部署模式
        if (terminalDTO.getPlatform() != CbbTerminalPlatformEnums.VOI) {
            return false;
        }

        // 不支持操作 Windows 7
        return cbbImageTemplateDTO.getOsType() == CbbOsType.WIN_7_32 || cbbImageTemplateDTO.getOsType() == CbbOsType.WIN_7_64;
    }
}
