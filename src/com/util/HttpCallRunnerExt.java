/**
 * 
 */
package com.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

 
        /**
 * @类型名称：HttpCallRunnerExt
 * @说明： 支持http/https代理
 * @创建者: 万志军  
 * @创建时间: 2020年1月8日 下午2:46:40
 * @修改者: 万志军  
 * @修改时间: 2020年1月8日 下午2:46:40 
        */  
    
public class HttpCallRunnerExt extends HttpCallRunner {

	protected String keyStorePath;

	protected String keyStorePassword;

	protected String keyStoreType;

	protected String trustStorePath;

	protected String trustStorePassword;

	protected String trustStoreType;

	protected String proxyHost;

	protected String proxyPort;

	protected String proxyUser;

	protected String proxyPassword;

	public void init(String keyStorePath, String keyStorePassword, String keyStoreType, String trustStorePath,
			String trustStorePassword, String trustStoreType, String proxyHost, String proxyPort, String proxyUser,
			String proxyPassword) {
		this.keyStorePath = keyStorePath;
		this.keyStorePassword = keyStorePassword;
		this.keyStoreType = keyStoreType;

		this.trustStorePath = trustStorePath;
		this.trustStorePassword = trustStorePassword;
		this.trustStoreType = trustStoreType;

		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyUser = proxyUser;
		this.proxyPassword = proxyPassword;
	}

	public String run(String method, String url, Map<String, String> params, String charset, boolean needCert,
			boolean useProxy) throws Exception {
		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withWaitStrategy(
						WaitStrategies.incrementingWait(200, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS))
				.build();

		return retryer.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				if (StringUtils.equals(method, "get")) {
					return doGet(url, params, charset);
				}

				if (StringUtils.equals(method, "post")) {
					return doPost(url, params, charset, needCert, useProxy);
				}

				throw new Exception("Invalid method " + method);
			}
		});

	}

	public String doGet(String url, Map<String, String> params, String charset, boolean needCert, boolean useProxy)
			throws Exception {
		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withWaitStrategy(
						WaitStrategies.incrementingWait(200, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS))
				.build();

		return retryer.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return get(url, params, charset, needCert, useProxy);
			}
		});

	}

	public String doPost(String url, Map<String, String> params, String charset, boolean needCert, boolean useProxy)
			throws Exception {
		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withWaitStrategy(
						WaitStrategies.incrementingWait(200, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS))
				.build();

		return retryer.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return post(url, params, charset, needCert, useProxy);
			}
		});

	}

	public String doPost(String url, String jsonStr, String charset, boolean needCert, boolean useProxy)
			throws Exception {
		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withWaitStrategy(
						WaitStrategies.incrementingWait(200, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS))
				.build();

		return retryer.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return post(url, jsonStr, charset, needCert, useProxy);
			}
		});

	}

	protected String get(String url, Map<String, String> params, String charset, boolean needCert, boolean useProxy)
			throws Exception {
		HttpGet httpGet = createHttpGet(url, params, charset);
		try (CloseableHttpClient httpClient = createHttpClient(needCert, useProxy)) {
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				LOGGER.debug("Result:"+result);
				return result;
			}
		} finally {
			httpGet.releaseConnection();
		}
	}

	protected String post(String url, Map<String, String> params, String charset, boolean needCert, boolean useProxy)
			throws Exception {
		HttpPost httpPost = createHttpPost(url, params, charset);
		try (CloseableHttpClient httpClient = createHttpClient(needCert, useProxy)) {
			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
				String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				LOGGER.debug("Result:"+result);
				return result;
			}
		} finally {
			httpPost.releaseConnection();
		}
	}

	protected String post(String url, String jsonStr, String charset, boolean needCert, boolean useProxy)
			throws Exception {
		HttpPost httpPost = createHttpPost(url, jsonStr, charset);
		try (CloseableHttpClient httpClient = createHttpClient(needCert, useProxy)) {
			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
				String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				LOGGER.debug("Result:"+result);
				return result;
			}
		} finally {
			httpPost.releaseConnection();
		}
	}

	protected CloseableHttpClient createDefaultHttpClient() throws Exception {
		return createHttpClient(false, false);
	}

	protected CloseableHttpClient createHttpClient(boolean needCert, boolean useProxy) throws Exception {
		// 构建SSLContext
		SSLContext sslContext = null;

		if (needCert) {
			KeyStore keyStore = null;
			if (StringUtils.isNotBlank(keyStorePath)) {
				keyStore = KeyStore.getInstance(keyStoreType);
				keyStore.load(new FileInputStream(new File(keyStorePath)), keyStorePassword.toCharArray());
			}

			KeyStore trustStore = null;
			if (StringUtils.isNotBlank(trustStorePath)) {
				trustStore = KeyStore.getInstance(trustStoreType);
				trustStore.load(new FileInputStream(new File(trustStorePath)), trustStorePassword.toCharArray());
			}

			SSLContextBuilder sslContextBuilder = SSLContexts.custom();

			if (null != keyStore) {
				sslContextBuilder.loadKeyMaterial(keyStore, keyStorePassword.toCharArray());
			}

			if (null != trustStore) {
				sslContextBuilder.loadTrustMaterial(trustStore, new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;

					}
				});
			}

			sslContext = sslContextBuilder.build();
		} else {
			sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;

				}
			}).build();

		}

		// 构建代理
		CredentialsProvider credsProvider = null;
		if (useProxy) {
			credsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(proxyHost, Integer.parseInt(proxyPort));
			Credentials credentials = new UsernamePasswordCredentials(proxyUser, proxyPassword);
			credsProvider.setCredentials(authScope, credentials);

		}

		SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sslContext,
				new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, null, new HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				});

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", ssf).build();

		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(connManager);

		if (null != credsProvider) {
			httpClientBuilder.setDefaultCredentialsProvider(credsProvider);
		}

		return httpClientBuilder.build();

	}
}
