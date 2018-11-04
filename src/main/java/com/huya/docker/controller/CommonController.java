package com.huya.docker.controller;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.huya.docker.service.UserService;
import com.huya.docker.util.HttpUtil;
import com.huya.docker.vo.RegistoryError;
import com.huya.docker.vo.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class CommonController {

    private static final String REQ_HEADER_AUTH = "Authorization";
    private static final String WWW_AUTHENTICATE = "Www-Authenticate";
    private static final int NO_AUTH_STATUS_CODE = 401;
    @Value("${auth.host}")
    private String authHost;
    @Value("${repository.url}")
    private String repositoryUrl;
    @Value("${repository.username}")
    private String repositoryUsername;
    @Value("${repository.password}")
    private String repositoryPassword;

    @Resource
    private UserService userService;

    @RequestMapping("/v2")
    public ResponseData v2(HttpServletRequest request, HttpServletResponse response){
        String authorization = request.getHeader(REQ_HEADER_AUTH);

        ResponseData responseData = new ResponseData();
        if(StringUtils.isEmpty(authorization) || !hasAuth(authorization.replace("Bearer ", ""))){
            //结果返回
            RegistoryError registoryError = new RegistoryError();
            registoryError.setCode("UNAUTHORIZED");
            registoryError.setMessage("huya proxy test authentication required");

            List<RegistoryError> errors = new ArrayList<RegistoryError>();
            errors.add(registoryError);
            responseData.setErrors(errors);

            String header = "Bearer realm=\"http://"+authHost+"/service/token\",service=\""+authHost+"\"";
            response.setStatus(NO_AUTH_STATUS_CODE);
            response.setHeader(WWW_AUTHENTICATE, header);
        }else{

        }


        return responseData;
    }

    @RequestMapping("/service/token")
    public ResponseData serviceToken(@RequestParam("account") String account,
                                     @RequestParam("client_id") String clientId,
                                     @RequestParam("offline_token") String offlineToken,
                                     @RequestParam("service") String service,
                                     HttpServletRequest request,
                                     HttpServletResponse response){
        ResponseData responseData = new ResponseData();
        String authorization = request.getHeader(REQ_HEADER_AUTH).replace("Basic ", "");
        if(hasAuth(authorization)){
            //调用仓库接口获取token
            try {
                checkRemoteAuth();
            } catch (Exception e) {
                log.info("checkRemoteAuto failed.", e);
            }
            responseData.setToken(authorization);
            responseData.setExpires_in(1800);
            responseData.setIssued_at("2018-11-03T11:43:20.849874263Z");
        }else{
            response.setStatus(NO_AUTH_STATUS_CODE);
            RegistoryError registoryError = new RegistoryError();
            registoryError.setCode("UNAUTHORIZED");
            registoryError.setMessage("huya proxy test authentication required");

            List<RegistoryError> errors = new ArrayList<RegistoryError>();
            errors.add(registoryError);
            responseData.setErrors(errors);
        }

        return responseData;
    }

    private boolean checkRemoteAuth() throws HttpProcessException {
        boolean result = true;
        HttpMethod httpMethod = HttpUtil.doGet(repositoryUrl+"/v2", "");
        if(httpMethod.getStatusCode() == NO_AUTH_STATUS_CODE){
            //获取请求头 Www-Authenticate →Bearer realm="https://auth.docker.io/token",service="registry.docker.io" 中的realm的值
            String authUrl = httpMethod.getResponseHeader(WWW_AUTHENTICATE).getValue()
                    .replace("Bearer ", "").split(",")[0]
                    .split("=")[1].replace("\"", "");
            String authHeader = "Basic " + Base64Utils.encodeToString((repositoryUsername+":"+repositoryPassword).getBytes());
            Header[] headers = HttpHeader.custom().other(REQ_HEADER_AUTH, authHeader).build();
            String html = HttpClientUtil.get(HttpConfig.custom().url(authUrl).headers(headers));
            log.info("auth token success response is {}", html);
        }
        return result;
    }

    private boolean hasAuth(String authorization){
        boolean result = false;
        authorization = new String(Base64Utils.decode(authorization.getBytes()));
        log.info("authorization is {}",authorization);
        String username = authorization.split(":")[0];
        String password = authorization.split(":")[1];
        if(userService.hasAuth(username, password)){
            result = true;
        }
        return result;
    }
}
