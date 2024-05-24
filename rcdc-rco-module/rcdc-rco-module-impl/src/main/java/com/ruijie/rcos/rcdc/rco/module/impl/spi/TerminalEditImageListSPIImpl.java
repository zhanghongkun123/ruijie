package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ControlStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.PermissionConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ImageTypeSupportTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageCalcService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTypeSupportTerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineRequestImageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.ImagePageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.TerminalEditImageInfoVO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 终端编辑镜像查询镜像列表
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/15
 *
 * @author ypp
 */
@DispatcherImplemetion(ImageDispatcherConstants.TERMINAL_EDIT_IMAGE_LIST)
public class TerminalEditImageListSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalEditImageListSPIImpl.class);

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager adminCacheManager;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ImageCalcService imageCalcService;

    @Autowired
    private ImageTypeSupportTerminalService imageTypeSupportTerminalService;

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

            PageQueryResponse<CbbImageTemplateDTO> pageQueryResponse = imageTemplateAPI.queryByAdmin(adminCache.getAdminId(), requestBuilder);

            if (pageQueryResponse.getTotal() == 0) {
                result.put("imageList", new ArrayList<>());
                cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, result);
                messageHandlerAPI.response(cbbResponseShineMessage);
                return;
            }

            // 查询终端型号
            TerminalDTO terminal = userTerminalMgmtAPI.getTerminalById(request.getTerminalId());

            List<TerminalEditImageInfoVO> imageInfoVOList = new ArrayList<>();
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
    private void buildImageInfoVOList(TerminalDTO terminal, List<TerminalEditImageInfoVO> imageInfoVOList,
            PageQueryResponse<CbbImageTemplateDTO> pageQueryResponse) throws BusinessException {

        List<ImageTypeSupportTerminalEntity> imageTypeSupportTerminalEntityList = imageTypeSupportTerminalService.findAll();
        
        if (pageQueryResponse.getTotal() > 0) {
            for (CbbImageTemplateDTO cbbImageTemplateDTO : pageQueryResponse.getItemArr()) {
                // 服务器处于备份中则不可编辑镜像
                if (ControlStateEnum.BACKUP != cbbImageTemplateDTO.getControlState()) {
                    TerminalEditImageInfoVO imageInfoVO = new TerminalEditImageInfoVO();
                    imageInfoVO.setImageId(cbbImageTemplateDTO.getId());
                    imageInfoVO.setImageName(cbbImageTemplateDTO.getImageTemplateName());
                    imageInfoVO.setOsType(cbbImageTemplateDTO.getOsType());
                    imageInfoVO.setSystemDiskSize(cbbImageTemplateDTO.getSystemDiskSize());
                    imageInfoVO.setSupportGoldenImage(cbbImageTemplateDTO.isSupportGoldenImage());
                    imageInfoVO.setDesktopRedirect(false);
                    imageInfoVO.setCbbImageType(cbbImageTemplateDTO.getCbbImageType());
                    imageInfoVO.setCanUse(true);
                    imageInfoVO.setDiskController(cbbImageTemplateDTO.getDiskController());

                    ImageTypeSupportTerminalEntity imageTypeSupportTerminalEntity = new ImageTypeSupportTerminalEntity();
                    imageTypeSupportTerminalEntity.setCbbImageType(cbbImageTemplateDTO.getCbbImageType());
                    imageTypeSupportTerminalEntity.setOsType(cbbImageTemplateDTO.getOsType());
                    imageTypeSupportTerminalEntity.setProductType(terminal.getProductType());

                    if (!imageTypeSupportTerminalService.hasImageSupportTerminal(imageTypeSupportTerminalEntityList, imageTypeSupportTerminalEntity)) {
                        imageInfoVO.setCanUse(false);
                        imageInfoVO.setCanNotUseMessage(LocaleI18nResolver.resolve(BusinessKey.RCDC_IMAGE_NOT_SUPPORT_TERMINAL));
                    }

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

}
