package com.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

/**
 * HTTPè°ƒç”¨
 * 
 * @author luke
 *
 */
public class HttpCallRunner {

	protected static final Logger LOGGER = Logger.getLogger(HttpCallRunner.class);

	protected static final String APPLICATION_JSON = "application/json";

	protected static final String CONTENT_TYPE_TEXT_JSON = "text/json";

	public String run(String method, String url, Map<String, String> params, String charset) throws Exception {
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
					return doPost(url, params, charset);
				}

				throw new Exception("Invalid method " + method);
			}
		});

	}

	public String doGet(String url, Map<String, String> params, String charset) throws Exception {
		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withWaitStrategy(
						WaitStrategies.incrementingWait(200, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS))
				.build();

		return retryer.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return get(url, params, charset);
			}
		});

	}

	public String doPost(String url, Map<String, String> params, String charset) throws Exception {
		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withWaitStrategy(
						WaitStrategies.incrementingWait(200, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS))
				.build();

		return retryer.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return post(url, params, charset);
			}
		});

	}

	public String doPost(String url, String jsonStr, String charset) throws Exception {
		Retryer<String> retryer = RetryerBuilder.<String> newBuilder().retryIfException()
				.withStopStrategy(StopStrategies.stopAfterAttempt(3))
				.withWaitStrategy(
						WaitStrategies.incrementingWait(200, TimeUnit.MILLISECONDS, 100, TimeUnit.MILLISECONDS))
				.build();

		return retryer.call(new Callable<String>() {

			@Override
			public String call() throws Exception {
				return post(url, jsonStr, charset);
			}
		});

	}

	private String get(String url, Map<String, String> params, String charset) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		HttpGet httpGet = createHttpGet(url, params, charset);
		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
				String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				LOGGER.debug("Result:"+result);
				return result;
			}
		} finally {
			httpGet.releaseConnection();
		}
	}

	private String post(String url, Map<String, String> params, String charset) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		HttpPost httpPost = createHttpPost(url, params, charset);
		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
				String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				LOGGER.debug("Result:"+result);
				return result;
			}
		} finally {
			httpPost.releaseConnection();
		}
	}

	private String post(String url, String jsonStr, String charset) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		HttpPost httpPost = createHttpPost(url, jsonStr, charset);
		try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
			try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
				String result = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
				LOGGER.debug("Result:"+result);
				return result;
			}
		} finally {
			httpPost.releaseConnection();
		}
	}

	protected HttpGet createHttpGet(String url, Map<String, String> params, String charset) throws URISyntaxException {
		URIBuilder uriBuilder = new URIBuilder(url);
		uriBuilder.setParameters(createParameters(params, charset));
		URI uri = uriBuilder.build();
		LOGGER.debug("uri--¡·"+uri);
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setConfig(RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(5000)
				.setSocketTimeout(10000).build());

		return httpGet;
	}

	protected HttpPost createHttpPost(String url, Map<String, String> params, String charset)
			throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(createEntry(params, charset));
		httpPost.setConfig(RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(5000)
				.setSocketTimeout(10000).build());

		return httpPost;
	}

	protected HttpPost createHttpPost(String url, String jsonStr, String charset) throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
		httpPost.setEntity(createEntry(jsonStr, charset));
		httpPost.setConfig(RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(5000)
				.setSocketTimeout(10000).build());

		return httpPost;
	}

	private List<NameValuePair> createParameters(Map<String, String> params, String charset) {
		List<NameValuePair> form = new ArrayList<NameValuePair>();
		if (null != params) {
			for (String name : params.keySet()) {
				form.add(new BasicNameValuePair(name, params.get(name)));
			}
		}

		return form;
	}

	private HttpEntity createEntry(Map<String, String> params, String charset) throws UnsupportedEncodingException {
		List<NameValuePair> form = new ArrayList<NameValuePair>();
		if (null != params) {
			for (String name : params.keySet()) {
				form.add(new BasicNameValuePair(name, params.get(name)));
			}
		}

		return new UrlEncodedFormEntity(form, charset);
	}

	private StringEntity createEntry(String jsonStr, String charset) throws UnsupportedEncodingException {
		StringEntity se = new StringEntity(jsonStr, charset);
		se.setContentType(CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));

		return se;
	}
}
