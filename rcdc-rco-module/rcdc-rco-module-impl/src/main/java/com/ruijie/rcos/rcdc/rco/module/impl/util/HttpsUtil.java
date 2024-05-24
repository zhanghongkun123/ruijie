package com.ruijie.rcos.rcdc.rco.module.impl.util;


import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.ruijie.rcos.sk.connectkit.api.connect.SslConfig;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description: Rest构建工具类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/10 14:42
 *
 * @author zqj
 */
public class HttpsUtil {

    private static final long DEFAULT_READ_TIMEOUT = 13L;

    private static final long DEFAULT_WRITE_TIMEOUT = 10L;

    private static final long DEFAULT_CONNECT_TIMEOUT = 10L;

    private static final int MAX_IDLE = 80;

    private static final long KEEP_ALIVE_TIME_KEEP = 60L;

    /**
     * 证书类型，现阶段只支持JKS
     */
    private static final String CERTIFICATE_TYPE = "JKS";

    /**
     * 证书编码算法
     */
    private static final String CERTIFICATE_CODE = "SunX509";

    /**
     * 加密算法
     */
    private static final String ENCRYPTION_TYPE = "TLS";

    /**
     * 证书信息
     */
    private SslConfig sslConfig;

    /**
     * 请求器
     */
    private  RestTemplate restTemplate;

    private static final SerializeConfig SERIALIZE_CONFIG;

    static {
        SERIALIZE_CONFIG = new SerializeConfig();
        SERIALIZE_CONFIG.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
    }

    public HttpsUtil() throws Exception {
        // 对象创建出来的时候,就进行证书初始化
        this.initSslConfig();
    }

    public HttpsUtil(SslConfig sslConfig) throws Exception {
        this.sslConfig = sslConfig;
        // 对象创建出来的时候,就进行证书初始化
        this.initSslConfig();
    }

    private void initSslConfig() throws Exception {
        TrustManager[] trustManagerArr = null;
        KeyManager[] keyManagerArr = null;
        // 判断是否有送证书信息
        if (!ObjectUtils.isEmpty(this.sslConfig) && this.sslConfig.isEnable()) {
            // 加载信任证书信息
            KeyStore trustStore = KeyStore.getInstance(CERTIFICATE_TYPE);
            try (InputStream trustInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.sslConfig.getTrustStore())) {
                trustStore.load(trustInputStream, this.sslConfig.getTrustPass().toCharArray());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_CODE);
                trustManagerFactory.init(trustStore);
                trustManagerArr = trustManagerFactory.getTrustManagers();
            }
            // 加载密钥证书信息
            if (Boolean.TRUE.equals(this.sslConfig.getHasClientAuth())) {
                KeyStore keyStore = KeyStore.getInstance(CERTIFICATE_TYPE);
                try (InputStream keyInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(this.sslConfig.getKeyStore())) {
                    keyStore.load(keyInputStream, this.sslConfig.getKeyPass().toCharArray());
                    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CERTIFICATE_CODE);
                    keyManagerFactory.init(keyStore, this.sslConfig.getKeyPass().toCharArray());
                    keyManagerArr = keyManagerFactory.getKeyManagers();
                }
            }
        } else {
            // 因是HTTPS协议，没有证书信息默认加上信任为true
            trustManagerArr = new TrustManager[] {new DefaultX509TrustManager()};
        }

        SSLContext sslContext = SSLContext.getInstance(ENCRYPTION_TYPE);
        sslContext.init(keyManagerArr, trustManagerArr, new SecureRandom());
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.connectionPool(new ConnectionPool(MAX_IDLE, KEEP_ALIVE_TIME_KEEP, TimeUnit.SECONDS));
        builder.followRedirects(true);

        X509TrustManager trustManager = (X509TrustManager) trustManagerArr[0];
        builder.sslSocketFactory(sslSocketFactory, trustManager);
        builder.hostnameVerifier((s, sslSession) -> {
            return true;
        });
        // 初始化 restTemplate
        restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(builder.build()));
        FastJsonConfig jsonConfig = new FastJsonConfig();
        jsonConfig.setSerializeConfig(SERIALIZE_CONFIG);
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(jsonConfig);
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(converter);
    }

    /**
     * 发送请求
     *
     * @param url 请求接口地址
     * @param headers 请求头部信息
     * @param body 请求体信息
     * @param httpMethod 请求方式
     * @return 返回的数据
     */
    public String doRequest(String url, @Nullable HttpHeaders headers, @Nullable Map<String, Object> body, HttpMethod httpMethod) {
        Assert.notNull(url, "url can not be null");
        Assert.notNull(httpMethod, "httpMethod can not be null");
        HttpEntity<Object> objectHttpEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(url, httpMethod, objectHttpEntity, String.class).getBody();
    }


    /**
     * 发送GET请求
     * 
     * @param url 请求接口地址
     * @param headers 请求头部信息
     * @param params 请求参数信息
     * @return 返回的数据
     */
    public String doGet(String url, @Nullable HttpHeaders headers, @Nullable Map<String, Object> params) {
        Assert.notNull(url, "请求url地址不能为空");
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, addHeaderBody(HttpMethod.GET, headers, null), String.class, params);
        return responseEntity.getBody();
    }

    /**
     * 发送GET请求, 无后缀参数
     * 
     * @param url 请求接口地址
     * @param headers 请求头部信息
     * @return 返回的数据
     */
    public String doGet(String url, @Nullable HttpHeaders headers) {
        Assert.notNull(url, "请求url地址不能为空");
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, addHeaderBody(HttpMethod.GET, headers, null), String.class);
        return responseEntity.getBody();
    }

    private static HttpEntity<MultiValueMap<String, Object>> addHeaderBody(HttpMethod method, HttpHeaders headers, Map<String, Object> body) {

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        if (!CollectionUtils.isEmpty(body)) {
            body.forEach((key, value) -> {
                multiValueMap.add(key, value);
            });
        }
        return method == HttpMethod.POST ? new HttpEntity<>(multiValueMap, headers) : new HttpEntity<>(null, headers);
    }

    /**
     * 默认证书校验实现
     */
    static class DefaultX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            // 此处可编写校验服务端证书的代码逻辑
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
