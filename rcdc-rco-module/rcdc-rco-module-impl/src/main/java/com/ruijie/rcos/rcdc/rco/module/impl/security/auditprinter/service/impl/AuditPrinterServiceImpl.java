package com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditFilePrintInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrintLogStaticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.dto.AuditPrinterStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.AuditPrintLogStaticTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums.PrintStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao.AuditFilePrintInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.dao.ViewAuditPrintLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.entity.AuditFilePrintInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 安全打印机管理实现
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
@Service
public class AuditPrinterServiceImpl implements AuditPrinterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditPrinterServiceImpl.class);

    @Autowired
    private AuditFilePrintInfoDAO auditFilePrintInfoDAO;

    @Autowired
    private ViewAuditPrintLogDAO viewAuditPrintLogDao;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;


    @Override
    public AuditPrinterStrategyDTO obtainAuditPrinterStrategy(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId not be null");
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskDTO.getStrategyId());
        AuditPrinterStrategyDTO auditPrinterStrategyDTO = new AuditPrinterStrategyDTO();
        if (BooleanUtils.isTrue(deskStrategy.getEnableAuditPrinter()) && Objects.nonNull(deskStrategy.getAuditPrinterInfo())) {
            BeanUtils.copyProperties(deskStrategy.getAuditPrinterInfo(), auditPrinterStrategyDTO);
        }
        auditPrinterStrategyDTO.setEnableAuditPrinter(deskStrategy.getEnableAuditPrinter());
        return auditPrinterStrategyDTO;
    }

    @Override
    public void checkEnableAuditPrinter(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId not be null");
        AuditPrinterStrategyDTO auditPrinterGlobalStrategyDTO = obtainAuditPrinterStrategy(deskId);
        if (!BooleanUtils.isTrue(auditPrinterGlobalStrategyDTO.getEnableAuditPrinter())) {
            throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINTER_DISABLE);
        }
    }

    @Override
    public void handleAuditPrintApplyResult(AuditFilePrintInfoDTO auditFilePrintInfoDTO) throws BusinessException {
        Assert.notNull(auditFilePrintInfoDTO, "auditFilePrintInfoDTO must not be null");

        AuditFilePrintInfoEntity printInfoEntity = auditFilePrintInfoDAO.findByFileId(auditFilePrintInfoDTO.getFileId())
                .orElseThrow(() -> new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_INFO_NOT_EXIST,
                        String.valueOf(auditFilePrintInfoDTO.getFileId())));
        if (printInfoEntity.getPrintState() != PrintStateEnum.PENDING_PRINT) {
            throw new BusinessException(AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_APPLY_RESULT_ALREADY_EXIST);
        }
        printInfoEntity.setPrinterName(auditFilePrintInfoDTO.getPrinterName());
        printInfoEntity.setPrinterModel(auditFilePrintInfoDTO.getPrinterModel());
        printInfoEntity.setPrinterBrand(auditFilePrintInfoDTO.getPrinterBrand());
        printInfoEntity.setPrinterSn(auditFilePrintInfoDTO.getPrinterSn());
        printInfoEntity.setFileId(auditFilePrintInfoDTO.getFileId());
        printInfoEntity.setPrintState(auditFilePrintInfoDTO.getPrintState());
        printInfoEntity.setPrintProcessName(auditFilePrintInfoDTO.getPrintProcessName());
        printInfoEntity.setPrintPaperSize(auditFilePrintInfoDTO.getPrintPaperSize());
        printInfoEntity.setPrintPageCount(auditFilePrintInfoDTO.getPrintPageCount());
        printInfoEntity.setPrintTime(auditFilePrintInfoDTO.getPrintTime());
        printInfoEntity.setPrintResultMsg(auditFilePrintInfoDTO.getPrintResultMsg());
        printInfoEntity.setUpdateTime(new Date());

        auditFilePrintInfoDAO.save(printInfoEntity);

    }

    @Override
    public PageQueryResponse<AuditPrintLogStaticDTO> auditPrintLogStaticPageQuery(PageQueryRequest request) {
        Assert.notNull(request, "request is null");

        Date startTime = new Date(0L);
        Date endTime = new Date();
        AuditPrintLogStaticTypeEnum staticType = AuditPrintLogStaticTypeEnum.USER;
        for (Match match : request.getMatchArr()) {
            JSONObject matchObject = (JSONObject) JSON.toJSON(match);
            if (match.getType() == Match.Type.RANGE) {
                if (matchObject.get("fieldName").equals("printTime") && matchObject.get("matchRule").equals("BETWEEN")) {
                    Long startTimestamp = matchObject.getLong("valueFrom");
                    if (startTimestamp != null) {
                        startTime = new Date(startTimestamp);
                    }
                    Long endTimestamp = matchObject.getLong("valueTo");
                    if (startTimestamp != null) {
                        endTime = new Date(endTimestamp);
                    }
                }
            }
            if (matchObject.get("fieldName").equals("staticType") && !matchObject.getJSONArray("valueArr").isEmpty()) {
                staticType = AuditPrintLogStaticTypeEnum.valueOf(matchObject.getJSONArray("valueArr").getString(0));
            }
        }

        List<Map<String, Object>> queryResultList = null;
        List<AuditPrintLogStaticDTO> allDTOList = new ArrayList<>();
        switch (staticType) {
            case USER:
                queryResultList = viewAuditPrintLogDao.staticUserPrintLogByPrintTime(startTime, endTime);
                break;
            case USER_GROUP:
                queryResultList = viewAuditPrintLogDao.staticUserGroupPrintLogByPrintTime(startTime, endTime);
                break;
            case DESKTOP:
                queryResultList = viewAuditPrintLogDao.staticDesktopPrintLogByPrintTime(startTime, endTime);
                break;
            case TERMINAL:
                queryResultList = viewAuditPrintLogDao.staticTerminalPrintLogByPrintTime(startTime, endTime);
                break;
            default:
                break;

        }
        if (!CollectionUtils.isEmpty(queryResultList)) {
            AuditPrintLogStaticTypeEnum finalStaticType = staticType;
            queryResultList.forEach(item -> {
                AuditPrintLogStaticDTO auditPrintLogStaticDTO = JSON.parseObject(JSON.toJSONString(item), AuditPrintLogStaticDTO.class);
                auditPrintLogStaticDTO.setStaticType(finalStaticType);
                allDTOList.add(auditPrintLogStaticDTO);
            });
        } else {
            return new PageQueryResponse<>(new AuditPrintLogStaticDTO[0], 0L);
        }


        List<AuditPrintLogStaticDTO> resultList;
        if (allDTOList.size() <= request.getLimit()) {
            resultList = allDTOList;
        } else {
            List<List<AuditPrintLogStaticDTO>> subList = ListRequestHelper.subList(allDTOList, request.getLimit());
            resultList = subList.get(request.getPage());
        }
        AuditPrintLogStaticDTO[] resultArr = new AuditPrintLogStaticDTO[resultList.size()];
        return new PageQueryResponse<>(resultList.toArray(resultArr), allDTOList.size());
    }
    
    @Override
    public List<AuditFilePrintInfoDTO> findAuditFilePrintInfoListByFileIdList(List<UUID> fileIdList) {
        Assert.notNull(fileIdList, "fileIdList cannot be null");
        return auditFilePrintInfoDAO.findByFileIdIn(fileIdList).stream().map(entity -> {
            AuditFilePrintInfoDTO dto = new AuditFilePrintInfoDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }


}
