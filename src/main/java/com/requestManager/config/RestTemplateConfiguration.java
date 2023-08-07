package com.requestManager.config;

import com.requestManager.constant.BusinessException;
import com.requestManager.constant.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * 绕过ssl证书
 *
 * @author chunxu.zhang
 * @since 2023/7/21
 **/

@Slf4j
@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate() {
        try {
            return new RestTemplate(RestTemplateConfiguration.generateHttpRequestFactory());
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            log.error(ErrorCodeEnum.CREATE_REST_CLIENT_ERROR.getDesc());
            throw new BusinessException(ErrorCodeEnum.CREATE_REST_CLIENT_ERROR);
        }
    }

    /**
     * 通过该工厂类创建的RestTemplate发送请求时，可忽略https证书认证
     *
     * @return 工厂
     */
    private static HttpComponentsClientHttpRequestFactory generateHttpRequestFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = ((x509Certificates, authType) -> true);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);
        return factory;
    }

    // 禁用重定向
    @Bean
    public RestTemplate nonRedirectTemplate() {
        // Create a custom ClientHttpRequestFactory to disable redirects
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(java.net.HttpURLConnection connection, String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                // Disable redirects
                connection.setInstanceFollowRedirects(false);
            }
        };
        return new RestTemplate(factory);
    }

}
