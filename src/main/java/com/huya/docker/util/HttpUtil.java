
package com.huya.docker.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.URIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 * @author huang.lixing
 * @date 2018/3/14 16:24
 * @descriprion http 統一調用工具
 */
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);    //日志记录


    /**
     * 执行一个HTTP GET请求，返回请求响应的HTML
     *
     * @param url         请求的URL地址
     * @param queryString 请求的查询参数,可以为null
     * @return 返回请求响应的HTML
     */
    public static HttpMethod doGet(String url, String queryString) {
        String response = null;
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        try {
            if (!StrUtil.isNullOrEmpty(queryString)){
                method.setQueryString(URIUtil.encodeQuery(queryString));
            }
            client.executeMethod(method);
        } catch (URIException e) {
            logger.error("Runing HTTP Get request,request message “" + queryString + "”Exceptio!");
        } catch (IOException e) {
            logger.error("Runing HTTP Get request" + url + ",Exception！");
        } finally {
            method.releaseConnection();
        }
        return method;
    }


}
