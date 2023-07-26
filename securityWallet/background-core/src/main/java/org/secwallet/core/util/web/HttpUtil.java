package org.secwallet.core.util.web;


import org.secwallet.core.model.Result;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * Http tool class, providing general Http access operation methods for the system
 * send get request
 * send post request
 */
public class HttpUtil {
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 9000;
    private static final String CODE = "UTF-8";
    static {
        // Set up connection pool
        connMgr = new PoolingHttpClientConnectionManager();
        // Set the connection pool size
        connMgr.setMaxTotal(500);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // Set connection timeout
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // Set read timeout
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // Sets the timeout for getting connection instances from the connection pool
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // Test if the connection is available before submitting the request
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }
    /**
     * Fill in the request header parameters into the get request
     *
     * @param httpPost
     * @param headers
     * @return
     */
    private static HttpGet setHeadersToGet(HttpGet httpPost, Map<String, String> headers) {
        if (headers != null && headers.size() != 0) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        return httpPost;
    }
    /**
     * Fill the request header parameters into the Post request
     *
     * @param httpPost
     * @param headers
     * @return
     */
    private static HttpPost setHeadersToPost(HttpPost httpPost, Map<String, String> headers) {
        if (headers != null && headers.size() != 0) {
            for (String key : headers.keySet()) {
                httpPost.addHeader(key, headers.get(key));
            }
        }
        return httpPost;
    }


    /**
     * Fill the request header parameters into the Put request
     *
     * @param httpPut
     * @param headers
     * @return
     */
    private static HttpPut setHeadersToPut(HttpPut httpPut, Map<String, String> headers) {
        if (headers != null && headers.size() != 0) {
            for (String key : headers.keySet()) {
                httpPut.addHeader(key, headers.get(key));
            }
        }
        return httpPut;
    }
    /**
     * Fill the request body parameters into the Post request
     *
     * @param httpPost
     * @param params
     * @return
     */
    private static HttpPost setParamsToRequest(HttpPost httpPost, Map<String, Object> params) {

        List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            NameValuePair pair = null;
            if (entry.getValue() == null) {
                pair = new BasicNameValuePair(entry.getKey(), null);
            } else {
                pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
            }
            pairList.add(pair);
        }
        httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
        return httpPost;
    }
    /**
     * Send a GET request (HTTP) without input data
     *
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<String, Object>(), new HashMap<String, String>());
    }

    /**
     * Send GET request (HTTP), K-V form, no request header parameters
     *
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        return doGet(url, params, null);
    }

    /**
     * Send a GET request (HTTP), in K-V form, with request header parameters
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String doGet(String url, Map<String, Object> params, Map<String, String> headers) {

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = null;
        HttpResponse response = null;
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }
        }
        apiUrl += param;
        System.out.println("apiUrl"+apiUrl);
        String result = null;
        try {
            HttpEntity entity = null;
            httpGet = new HttpGet(apiUrl);
            httpGet = setHeadersToGet(httpGet, headers);
            response = httpClient.execute(httpGet);
            if (response != null) {
                entity = response.getEntity();
            }

            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String doGet1(String url,Map<String, Object> params,String param1) {

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = null;
        HttpResponse response = null;
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        if (param != null && params.size() > 0) {
            for (String key : params.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }
        }
        apiUrl += param+param1;
        System.out.println("apiUrl"+apiUrl);
        String result = null;
        try {
            HttpEntity entity = null;
            httpGet = new HttpGet(apiUrl);
            httpGet = setHeadersToGet(httpGet, null);
            response = httpClient.execute(httpGet);
            if (response != null) {
                entity = response.getEntity();
            }

            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Send a POST request (HTTP) without input data
     *
     * @param url
     * @return
     */
    public static Result doPost(String url) {
        return doPost(url, new HashMap<String, Object>(), new HashMap<String, String>());
    }

    /**
     * Send a POST request (HTTP), in JSON form, without request header parameters
     *
     * @param url
     * @param json json obj
     * @return
     */
    public static Result doPost(String url, Object json) {
        return doPost(url, json, null);
    }

    /**
     * Send POST request (HTTP), K-V form, no request header parameters
     *
     * @param url
     * @param params
     * @return
     */
    public static Result doPost(String url, Map<String, Object> params) {
        return doPost(url, params, null);
    }

    /**
     * Send POST request (HTTP), K-V form, with request header parameters
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Result doPost(String url, Map<String, Object> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        Result requestResult = new Result();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        try {

            httpPost.setConfig(requestConfig);
            httpPost = setHeadersToPost(httpPost, headers);
            httpPost = setParamsToRequest(httpPost, params);
            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity entity = response.getEntity();
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
            requestResult.ok(httpStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            requestResult.ok(response.getStatusLine().getStatusCode());
        }
        return requestResult;
    }

    /**
     * Send a POST request (HTTP), in JSON form, with request header parameters
     *
     * @param url
     * @param json
     * @param headers
     * @return
     */
    public static Result doPost(String url, Object json, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        Result requestResult = new Result();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            httpPost = setHeadersToPost(httpPost, headers);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity entity = response.getEntity();
                httpStr = EntityUtils.toString(entity, "UTF-8");
            }
            requestResult.ok(httpStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            requestResult.ok(response.getStatusLine().getStatusCode());
        }
        return requestResult;
    }

    /**
     * Send SSL POST request (HTTPS), no K-V formal parameters, no request header parameters
     *
     * @param url
     * @return
     */
    public static Result doPostSSL(String url) {
        return doPostSSL(url, null, null);
    }

    /**
     * Send SSL POST request (HTTPS), K-V form, no request header parameters
     *
     * @param url
     * @param params
     * @return
     */
    public static Result doPostSSL(String url, Map<String, Object> params) {
        return doPostSSL(url, params, null);
    }

    /**
     * Send SSL POST request (HTTPS), K-V form, with request header parameters
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Result doPostSSL(String url, Map<String, Object> params, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        Result requestResult = new Result();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            HttpEntity entity = null;
            httpPost.setConfig(requestConfig);

            httpPost = setHeadersToPost(httpPost, headers);
            httpPost = setParamsToRequest(httpPost, params);
            response = httpClient.execute(httpPost);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                entity = response.getEntity();
                requestResult.ok(statusCode);
                if (statusCode != HttpStatus.SC_OK || entity == null) {
                    return requestResult;
                }
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
            requestResult.ok(httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return requestResult;
    }

    /**
     * Send SSL POST request (HTTPS), in JSON format, with request header parameters
     *
     * @param url
     * @param json
     * @return
     */
    public static Result doPostSSL(String url, Object json) {
        return doPostSSL(url, json, null);
    }

    /**
     * Send SSL POST request (HTTPS), in JSON format, with request header parameters
     *
     * @param url
     * @param json
     * @param headers
     * @return
     */
    public static Result doPostSSL(String url, Object json, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        Result requestResult = new Result();
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            HttpEntity entity = null;
            httpPost.setConfig(requestConfig);
            httpPost = setHeadersToPost(httpPost, headers);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                entity = response.getEntity();
                requestResult.ok(statusCode);
                if (statusCode != HttpStatus.SC_OK || entity == null) {
                    return requestResult;
                }
            }

            httpStr = EntityUtils.toString(entity, "utf-8");
            requestResult.ok(httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            requestResult.ok(response.getStatusLine().getStatusCode());
        }
        return requestResult;
    }

    /**
     * Send SSL GET request (HTTPs), no parameters, no request header parameters
     *
     * @param url
     * @return
     */
    public static Result doGetSSL(String url) {
        return doGetSSL(url, null, null);
    }

    /**
     * Send SSL GET request (HTTPs), K-V form, no request header parameters
     *
     * @param url
     * @param params
     * @return
     */
    public static Result doGetSSL(String url, Map<String, Object> params) {
        return doGetSSL(url, params, null);
    }

    /**
     * Send SSL GET request (HTTPs), K-V form, with request header parameters
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Result doGetSSL(String url, Map<String, Object> params, Map<String, String> headers) {

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = null;
        HttpResponse response = null;
        Result requestResult = new Result();
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }
        }
        apiUrl += param;
        String result = null;
        try {
            HttpEntity entity = null;
            httpGet = new HttpGet(apiUrl);
            httpGet.setConfig(requestConfig);
            httpGet = setHeadersToGet(httpGet, headers);
            response = httpclient.execute(httpGet);
            if (response != null) {
                entity = response.getEntity();
            }

            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
                requestResult.ok(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            requestResult.ok(response.getStatusLine().getStatusCode());
        }
        return requestResult;
    }


    /**
     * Send PUT request (HTTP), no parameters, no request header parameters
     *
     * @param url
     * @return
     */
    public static Result put(String url) {
        return put(url, null, null);
    }

    /**
     * Send PUT request (HTTP), K-V form, no request header parameters
     *
     * @param url
     * @param params
     * @return
     */
    public static Result put(String url, Map<String, String> params) {
        return put(url, params, null);
    }

    /**
     * Send PUT request (HTTP), K-V form, with request header parameters
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Result put(String url, Map<String, String> params, Map<String, String> headers) {
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        String responseText = "";
        HttpEntity entity = null;
        Result requestResult = null;
        CloseableHttpResponse response = null;
        String httpStr = null;
        try {
            HttpPut httpPut = new HttpPut(url);
            if (headers != null) {
                Set<String> set = headers.keySet();
                for (String item : set) {
                    String value = headers.get(item);
                    httpPut.addHeader(item, value);
                }
            }
            if (params != null) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : params.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    paramList.add(pair);
                }
                httpPut.setEntity(new UrlEncodedFormEntity(paramList, CODE));
            }
            response = client.execute(httpPut);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                entity = response.getEntity();
                requestResult.ok(statusCode);
                if (statusCode != HttpStatus.SC_OK || entity == null) {
                    return requestResult;
                }
            }

            httpStr = EntityUtils.toString(entity, "utf-8");
            requestResult.ok(httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return requestResult;
    }


    /**
     * Send an SSL PUT request (HTTP), in JSON format, without request header parameters
     *
     * @param url
     * @param json
     * @return
     */
    public static Result doPut(String url, Object json) {
        return doPut(url, json, null);
    }

    /**
     * Send a PUT request (HTTP), in JSON form, with request header parameters
     *
     * @param url     
     * @param json    
     * @param headers 
     * @return
     */
    public static Result doPut(String url, Object json, Map<String, String> headers) {
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        Result requestResult = new Result();
        HttpPut httpPut = new HttpPut(url);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            HttpEntity entity = null;
            httpPut.setConfig(requestConfig);
            httpPut = setHeadersToPut(httpPut, headers);
            StringEntity stringEntity = new StringEntity(json.toString(), "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPut.setEntity(stringEntity);
            response = httpClient.execute(httpPut);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                entity = response.getEntity();
                requestResult.ok(statusCode);
                if (statusCode != HttpStatus.SC_OK || entity == null) {
                    return requestResult;
                }
            }

            httpStr = EntityUtils.toString(entity, "utf-8");
            requestResult.ok(httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            requestResult.ok(response.getStatusLine().getStatusCode());
        }
        return requestResult;
    }

    /**
     * Send DELETE request (HTTP), no parameters
     *
     * @param url
     * @return
     */
    public static Result doDelete(String url) {
        return doDelete(url, null, null);
    }

    /**
     * Send DELETE request (HTTP), K-V form, no request header parameters
     *
     * @param url
     * @param params
     * @return
     */
    public static Result doDelete(String url, Map<String, String> params) {
        return doDelete(url, params, null);
    }

    /**
     * Send DELETE request (HTTP), K-V form, with request header parameters
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static Result doDelete(String url, Map<String, String> params, Map<String, String> headers) {
        CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
        String responseText = "";
        HttpEntity entity = null;
        Result requestResult = null;
        CloseableHttpResponse response = null;
        String httpStr = null;
        try {
            HttpDelete httpDelete = new HttpDelete(url);
            if (headers != null) {
                Set<String> set = headers.keySet();
                for (String item : set) {
                    String value = headers.get(item);
                    httpDelete.addHeader(item, value);
                }
            }
            if (params != null) {
                URIBuilder uriBuilder = new URIBuilder(url);
                if (params != null) {
                    for (String key : params.keySet()) {
                        uriBuilder.setParameter(key, params.get(key));
                    }
                }
                URI uri = uriBuilder.build();
                httpDelete.setURI(uri);
            }
            response = client.execute(httpDelete);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                entity = response.getEntity();
                requestResult.ok(statusCode);
                if (statusCode != HttpStatus.SC_OK || entity == null) {
                    return requestResult;
                }
            }

            httpStr = EntityUtils.toString(entity, "utf-8");
            requestResult.ok(httpStr);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    EntityUtils.consume(response.getEntity());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return requestResult;
    }

    /**
     * Create an SSL secure connection
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }
    public static void main(String[] args) throws Exception {
        Map<String,Object> params=new HashMap<>();
        params.put("orderNo","559166696787562496");
        params.put("amount","400");
        params.put("sign","2274BFD64EB5BC2E0C900D91959D6B50");
    }
}
