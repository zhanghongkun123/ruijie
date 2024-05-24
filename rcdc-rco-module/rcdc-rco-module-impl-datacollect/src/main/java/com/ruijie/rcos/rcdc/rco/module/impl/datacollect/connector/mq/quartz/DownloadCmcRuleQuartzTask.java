package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.quartz;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.SystemVersionMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.common.condition.ConditionProductOnConfig;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.DataCollectConstant;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto.CollectRuleDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto.CollectRulesDTO;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.quartz.Quartz;
import com.ruijie.rcos.sk.base.quartz.QuartzTask;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.configcenter.ConfigCenterKvAPI;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

import javax.net.ssl.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <br>
 * Description: 数据收集 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021/6/21 <br>
 *
 * @author xwx * 5,10,15,20,25,30,35,40,45,50,55 * * * ?
 */
@Quartz(scheduleName = BusinessKey.RCDC_DOWNLOAD_CMC_RULE_QUARTZTASK, cron = "0 0 0,12 * * ?", scheduleTypeCode = "download_cmc_rule")
@Service
@Conditional(ConditionProductOnConfig.class)
public class DownloadCmcRuleQuartzTask implements QuartzTask, SafetySingletonInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadCmcRuleQuartzTask.class);

    private static final String CMC_RULE_URL = "https://cmccdn.ruijie.com.cn/config/rule.zip";

    private static final String CMC_RULE_ZIP_FILE_PATH = "/data/web/config/rule.zip";

    private static final String CMC_AES_KEY = "CDDNDQBTTXPSELFZ";

    private static final String UTF_8 = "UTF-8";

    private static final String SUCCESS = "success";

    private static final String USER_AGENT = "User-Agent";

    private static final String MSIE_5_0_WINDOWS_NT_DIG_EXT = "Mozilla/4.0(compatible;MSIE 5.0;Windows NT;DigExt)";

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final int CONNECT_TIMEOUT = 3 * 1000;

    private static final String SYSTEM_VERSION_SPLIT = "_";

    private static final String SSL = "SSL";

    private static final String SUN_JSSE = "SunJSSE";

    private static final int TIMEOUT = 6000;


    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private ConfigCenterKvAPI configCenterKvAPI;

    @Autowired
    private SystemVersionMgmtAPI systemVersionMgmtAPI;

    @Override
    public void execute(QuartzTaskContext quartzTaskContext) throws Exception {
        downloadRule();
    }

    @Override
    public void safeInit() {
        try {
            downloadRule();
        } catch (Exception e) {
            LOGGER.error("下载规则失败", e);
        }
    }

    /**
     * 版本校验使用productLine_publicVersion, eg: RCC_V5.0R1P2
     */
    private void downloadRule() throws Exception {
        LOGGER.info("开始下载CMC数据采集规则");

        // 下载文件
        downloadFile();
        // 解压、读取、解密文件
        String content;
        try (ZipFile zipFile = new ZipFile(CMC_RULE_ZIP_FILE_PATH)) {
            ZipEntry entry = zipFile.entries().nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            content = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new BusinessException("解析zip文件失败", e);
        }

        String resultContent = AesUtil.descrypt(content, CMC_AES_KEY);
        LOGGER.info("CMC数据收集resultContent:{},长度为:{}", resultContent, resultContent.length());
        // 解析json,找到与该版本符合的规则
        CollectRulesDTO collectRulesDTO = JSON.parseObject(resultContent, CollectRulesDTO.class);
        String versionInfo = "";
        try {
            versionInfo = systemVersionMgmtAPI.obtainSystemReleaseVersion(new DefaultPageRequest()).getDto();
        } catch (Exception e) {
            LOGGER.error("未获取到当前系统版本号，采集规则失败", e);
            return;
        }
        LOGGER.info("获取到系统版本号为{}", versionInfo);
        String version = versionInfo;
        List<CollectRuleDTO> fitRuleList = collectRulesDTO.getCollectRuleList().stream().filter(rule -> {
            String regex = rule.getProductVersionRegEx();
            if (regex == null) {
                return true;
            }
            return version.matches(regex);
        }).collect(Collectors.toList());

        collectRulesDTO.setCollectRuleList(fitRuleList);
        LOGGER.info("CMC数据收集 collectRulesDTO:{},字符串长度为:{}", JSON.toJSONString(collectRulesDTO), JSON.toJSONString(collectRulesDTO).length());
        // 存入数据库
        rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(DataCollectConstant.COLLECT_RULE, collectRulesDTO.toString()));
    }


    /**
     * @throws Exception 文件异常
     */
    private static void downloadFile() throws Exception {
        SSLContext sslcontext = SSLContext.getInstance(SSL, SUN_JSSE);
        sslcontext.init(null, new TrustManager[]{new X509TrustUtiil()}, new java.security.SecureRandom());
        URL url = new URL(DownloadCmcRuleQuartzTask.CMC_RULE_URL);
        HostnameVerifier ignoreHostnameVerifier = (s, sslSession) -> {
            LOGGER.warn("Hostname is not matched for cert.");
            return true;
        };
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
        HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
        urlCon.setConnectTimeout(TIMEOUT);
        urlCon.setReadTimeout(TIMEOUT);
        int code = urlCon.getResponseCode();
        if (code != HttpURLConnection.HTTP_OK) {
            throw new Exception("文件读取失败");
        }
        // 读文件流
        try (DataInputStream in = new DataInputStream(urlCon.getInputStream())) {
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(DownloadCmcRuleQuartzTask.CMC_RULE_ZIP_FILE_PATH))) {
                byte[] bufferArr = new byte[2048];
                int count;
                while ((count = in.read(bufferArr)) > 0) {
                    out.write(bufferArr, 0, count);
                }
            }
        }
    }

    /**
     * X509Trust
     */
    static class X509TrustUtiil implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            //Doing nothing because of waiting someone to complete it...
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            //Doing nothing because of waiting someone to complete it...
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            //默认返回
            return new X509Certificate[0];
        }

    }

    @Override
    public void validate(String bizData) throws BusinessException {
        //Doing nothing because of waiting someone to complete it...
    }
}
