package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.connect;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.utils.SmsConverterUtils;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.connect.SslConfig;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.http.*;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.*;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description: https操作类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/26
 *
 * @author TD
 */
public class HttpsClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpsClient.class);

    private static final long DEFAULT_READ_TIMEOUT = 3L;

    private static final long DEFAULT_WRITE_TIMEOUT = 3L;

    private static final long DEFAULT_CONNECT_TIMEOUT = 3L;

    private static final int MAX_IDLE = 10;

    private static final long KEEP_ALIVE_TIME_KEEP = 6L;

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
    private final RestTemplate restTemplate;

    public HttpsClient(SslConfig sslConfig) throws Exception {
        this.sslConfig = sslConfig;
        // 对象创建出来的时候,就进行证书初始化
        this.restTemplate = this.initSslConfig();
    }
    
    public HttpsClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS).writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(MAX_IDLE, KEEP_ALIVE_TIME_KEEP, TimeUnit.SECONDS)).followRedirects(true);
        // 初始化 restTemplate
        this.restTemplate =  new RestTemplate(new OkHttp3ClientHttpRequestFactory(builder.build()));
    }

    private RestTemplate initSslConfig() throws Exception {
        TrustManager[] trustManagerArr = null;
        KeyManager[] keyManagerArr = null;
        // 判断是否有送证书信息
        if (!ObjectUtils.isEmpty(this.sslConfig) && this.sslConfig.isEnable()) {
            // 加载信任证书信息
            KeyStore trustStore = KeyStore.getInstance(CERTIFICATE_TYPE);
            try (InputStream trustInputStream = getStoreAsStream(this.sslConfig.getTrustStore())) {
                trustStore.load(trustInputStream, this.sslConfig.getTrustPass().toCharArray());
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_CODE);
                trustManagerFactory.init(trustStore);
                trustManagerArr = trustManagerFactory.getTrustManagers();
            }
            // 加载密钥证书信息
            if (Boolean.TRUE.equals(this.sslConfig.getHasClientAuth())) {
                KeyStore keyStore = KeyStore.getInstance(CERTIFICATE_TYPE);
                try (InputStream keyInputStream = getStoreAsStream(this.sslConfig.getKeyStore())) {
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
        builder.hostnameVerifier((s, sslSession) -> true);
        // 初始化 restTemplate
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory(builder.build()));
    }

    /**
     * 发送POST请求
     * 
     * @param url 请求接口地址
     * @param header 请求头部信息
     * @param body 请求体信息
     * @return 返回的数据
     */
    public String doPost(String url, @Nullable Map<String, Object> header, @Nullable Map<String, Object> body) {
        Assert.notNull(url, "doPost请求url地址不能为空");
        LOGGER.info("doPost request url：{}，header content: {}，body content：{}", url, JSON.toJSONString(header), JSON.toJSONString(body));
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url, HttpMethod.POST, addHeaderBody(HttpMethod.POST, header, body), String.class);
        LOGGER.info("doPost response url：{}，return content: {}", url, responseEntity.getBody());
        return responseEntity.getBody();
    }

    /**
     * 发送GET请求
     * 
     * @param url 请求接口地址
     * @param header 请求头部信息
     * @param params 请求参数信息
     * @return 返回的数据
     */
    public String doGet(String url, @Nullable Map<String, Object> header, @NotNull Map<String, Object> params) {
        Assert.notNull(url, "doGet请求url地址不能为空");
        Assert.notNull(params, "doGet请求params内容不能为空");
        LOGGER.info("doGet request url：{}，header content: {}，params content：{}", url, JSON.toJSONString(header), JSON.toJSONString(params));
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, addHeaderBody(HttpMethod.GET, header, null), String.class, params);
        LOGGER.info("doGet response url：{}，return content: {}", url, responseEntity.getBody());
        return responseEntity.getBody();
    }

    /**
     * 发送GET请求, 无后缀参数
     * 
     * @param url 请求接口地址
     * @param header 请求头部信息
     * @return 返回的数据
     */
    public String doGet(String url, @Nullable Map<String, Object> header) {
        Assert.notNull(url, "无参doGet请求url地址不能为空");
        LOGGER.debug("doGet not params request url：{}，header content: {}", url, JSON.toJSONString(header));
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, addHeaderBody(HttpMethod.GET, header, null), String.class);
        LOGGER.debug("doGet not params response url：{}，return content: {}", url, responseEntity.getBody());
        return responseEntity.getBody();
    }

    /**
     * 发送HTTP请求
     *
     * @param url 请求接口地址
     * @param httpMethod 方法类型
     * @param charset 编码方式
     * @param header 请求头部信息
     * @param params 请求体信息
     * @return 返回的数据
     * @throws UnsupportedEncodingException 编解码异常
     */
    public String doExchange(String url, HttpMethod httpMethod, Charset charset, 
                             @Nullable Map<String, Object> header, String params) throws UnsupportedEncodingException {
        Assert.notNull(url, "doExchange请求url地址不能为空");
        Assert.notNull(httpMethod, "doExchange请求httpMethod不能为空");
        Assert.notNull(params, "params can be not null");
        Assert.notNull(charset, "charset can be not null");
        LOGGER.debug("doExchange request url：{}，header: {}，params：{}", url, JSON.toJSONString(header), JSON.toJSONString(params));
        ResponseEntity<String> responseEntity;
        if (HttpMethod.GET == httpMethod) {
            responseEntity = restTemplate.exchange(convertMapToUrl(url, params, charset), httpMethod, 
                    addDefaultHeaderBody(httpMethod, header, null, charset), String.class);
        } else {
            responseEntity = restTemplate.exchange(url, httpMethod, addDefaultHeaderBody(httpMethod, header, params, charset), String.class);
        }
        LOGGER.debug("doExchange response url：{}，return content: {}", url, responseEntity.getBody());
        return responseEntity.getBody();
    }
    
    private static HttpEntity<Object> addDefaultHeaderBody(HttpMethod method, Map<String, Object> header, 
                                                           String params, Charset charset) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        if (!CollectionUtils.isEmpty(header)) {
            // 对请求头中的内容中文进行URL编码
            for (Map.Entry<String, Object> entry : header.entrySet()) {
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                headers.set(SmsConverterUtils.isChineseStr(key) ? URLEncoder.encode(key, charset.name()) : key,
                        SmsConverterUtils.isChineseStr(value) ? URLEncoder.encode(value, charset.name()) : value);
            }
        }
        // 请求头不存在CONTENT_TYPE，则设置为APPLICATION_JSON
        if (!headers.containsKey(HttpHeaders.CONTENT_TYPE)) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        // 设置请求头编码格式
        MediaType mediaType = Objects.requireNonNull(headers.getContentType());
        headers.setContentType(new MediaType(mediaType, charset));
        // 根据请求头Content-Type，设置请求内容的格式
        if (method == HttpMethod.POST && StringUtils.hasText(params)) {
            return new HttpEntity<>(buildRequestContent(mediaType.toString(), params), headers);
        }
        return new HttpEntity<>(null, headers);
    }

    private static Map<String, String> convertUrlToMap(String url) {
        UriComponents uriComponents = UriComponentsBuilder.newInstance().query(url).build();
        return uriComponents.getQueryParams().toSingleValueMap(); 
    }           
    
    private static Object buildRequestContent(String contentTypeValue, String params) {
        if (contentTypeValue.startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
            MultiValueMap<String, Object> paramsMap = new LinkedMultiValueMap<>();
            convertUrlToMap(params).forEach(paramsMap::add);
            return paramsMap;
        }
        return params;
    }

    private static URI convertMapToUrl(String url, String params, Charset charset) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        return builder.query(params).encode(charset).build().toUri();
    }

    private static HttpEntity<Object> addHeaderBody(HttpMethod method, Map<String, Object> header, Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!CollectionUtils.isEmpty(header)) {
            header.forEach((key, value) -> headers.add(key, JSON.toJSONString(value)));
        }
        return method == HttpMethod.POST ? new HttpEntity<>(body, headers) : new HttpEntity<>(null, headers);
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

    protected InputStream getStoreAsStream(String store) throws IOException {
        Assert.notNull(store, "证书的路径不能为空");
        try {
            return new FileInputStream(store);
        } catch (FileNotFoundException var2) {
            LOGGER.debug("read ssl config not found.", var2);
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(store);
        }
    }
}
